package com.gettec.fsnip.fsn.service.erp.buss.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.buss.MerchandiseStorageInfoDAO;
import com.gettec.fsnip.fsn.dao.erp.buss.OutStorageRecordDAO;
import com.gettec.fsnip.fsn.enums.BusinessTypeEnums;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.model.erp.buss.BussToMerchandises;
import com.gettec.fsnip.fsn.model.erp.buss.MerchandiseStorageInfo;
import com.gettec.fsnip.fsn.model.erp.buss.MerchandiseStorageInfoPK;
import com.gettec.fsnip.fsn.model.erp.buss.OutStorageRecord;
import com.gettec.fsnip.fsn.model.erp.buss.OutStorageType;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.OrderTypeService;
import com.gettec.fsnip.fsn.service.erp.OutStorageTypeService;
import com.gettec.fsnip.fsn.service.erp.buss.BussToMerchandisesService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseInstanceService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseStorageInfoService;
import com.gettec.fsnip.fsn.service.erp.buss.OutStorageRecordService;
import com.gettec.fsnip.fsn.transfer.BusinessOrderTransfer;
import com.gettec.fsnip.fsn.transfer.impl.OutStorageRecordTransfer;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.erp.BusinessOrderVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service
public class OutStorageRecordServiceImpl extends BaseServiceImpl<OutStorageRecord, OutStorageRecordDAO>
		implements OutStorageRecordService {
	@Autowired private OutStorageRecordDAO outStorageRecordDAO;
	@Autowired private OutStorageTypeService outStorageTypeService;
	@Autowired private BussToMerchandisesService bussToMerchandisesService;
	@Autowired private MerchandiseStorageInfoService merchandiseStorageInfoService;
	@Autowired private OrderTypeService orderTypeService;
	@Autowired private MerchandiseInstanceService merchandiseInstanceService;
	@Autowired private MerchandiseStorageInfoDAO merchandiseStorageInfoDAO;
	private BusinessOrderTransfer<OutStorageRecord> transfer = new OutStorageRecordTransfer();
	
	/**
	 * 商品出库
	 * @param vo
	 * @param organization
	 * @param userName
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public OutStorageRecord addBusinessOrder(BusinessOrderVO vo,Long organization,String userName, ResultVO resultVO) throws ServiceException{
		try {
			OutStorageRecord outStore = transfer.transferToRealEntity(vo);
			vo.setResult(outStore);
			/* 1. 自动生成编号  */
			String autoNo = null;
			String no = outStore.getNo(); // 获取单别(三位)
			String noStart = orderTypeService.getAutoCodeNumStart(no) + BusinessTypeEnums.OUT_STORAGE.getId();
			String noMax = getDAO().findNoMaxByNoStart(noStart); // 获取已有的最大编号
			if(noMax != null){
				autoNo = orderTypeService.getAutoCodeNum(noMax);
			}else{
				autoNo = noStart + "0000";
			}
			outStore.setCreateUserName(userName);
			outStore.setNo(autoNo);
			outStore.setOrganization(organization);
			/* 2. 新增一条出库基本记录 */
			OutStorageType type = outStorageTypeService.findById(vo.getTypeInstance());
			outStore.setType(type);
			create(outStore);
			/* 3. 保存商品列表信息 */
			boolean success = bussToMerchandisesService.save(vo.getMerchandises(), autoNo, new Long(BusinessTypeEnums.OUT_STORAGE.getId()));
			/* 4. 出库成功后，添加库存数量 */
			if(success){
				boolean reduceOK = true;
				for(BussToMerchandises item : vo.getMerchandises()){
					reduceOK = reduceOK && merchandiseStorageInfoService.reduceStorage(item.getInstance(), 
							item.getCount(), item.getStorage_2(), outStore.getNo(), true,
							BusinessTypeEnums.OUT_STORAGE.getTypeName(), userName,organization);
					if(!reduceOK){
						resultVO.setErrorMessage(item.getDisplayName()+"的出货量大于库存量");
						throw new Exception();
					}
				}
			}else{
				throw new Exception();
			}
			return outStore;
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]OutStorageRecordServiceImpl.addBusinessOrder()-->", sex.getException());
		} catch (Exception e) {
			throw new ServiceException("[Exception]OutStorageRecordServiceImpl.addBusinessOrder()-->", e);
		}
	}
	
	/**
	 * 判断出库数量是否大于现有库存量
	 * @param productId
	 * @param value
	 * @param batch
	 * @param storage
	 * @return
	 */
	public boolean JudgeOutNum(Long productId,String value,String batch,String storage) throws ServiceException{	
		try {
			boolean flag = false;
			int Ivalue = Integer.parseInt(value);
			MerchandiseInstance instance = merchandiseInstanceService.getInstanceByNoAndBatchNumber(productId, batch);
			MerchandiseStorageInfoPK id = new MerchandiseStorageInfoPK(storage,instance.getInstanceID());
			MerchandiseStorageInfo info = merchandiseStorageInfoDAO.findById(id);
			if(info != null){
				if(info.getCount() > 0 && info.getCount()>=Ivalue){
					flag = true;
				}
			}
			return flag;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException] OutStorageRecordServiceImpl.JudgeOutNum()-->", jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException] OutStorageRecordServiceImpl.JudgeOutNum()-->", sex.getException());
		}
	}
	
	/**
	 * 商品出库分页查询
	 * @param page
	 * @param pageSize
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<OutStorageRecord> getPaging(int page,
			int pageSize, String keywords, Long organization) throws ServiceException {
		try {
			PagingSimpleModelVO<OutStorageRecord> result = new PagingSimpleModelVO<OutStorageRecord>();
			String condition = " where e.organization=?1";
			Object[] params = new Object[]{organization};
			result.setCount(outStorageRecordDAO.count(condition, params));
			result.setListOfModel(outStorageRecordDAO.getListByPage(page, pageSize, condition, params));
			return result;
		} catch (JPAException jpae) {
			throw new ServiceException("[ServiceException] OutStorageRecordServiceImpl.getPaging()-->", jpae.getException());
		}
	}

	public PagingSimpleModelVO<OutStorageRecord> getOutStorageRecordfilter(int page, int pageSize,String configure, Long organization)throws ServiceException {
		try{
			PagingSimpleModelVO<OutStorageRecord> result = new PagingSimpleModelVO<OutStorageRecord>();
			String realConfigure = getConfigure(configure);
			realConfigure += " and e.organization = " + organization;
			long count = outStorageRecordDAO.countOutStorageRecord(realConfigure);
			result.setCount(count);
			result.setListOfModel(outStorageRecordDAO.getOutStorageRecordfilter_(page, pageSize, realConfigure));
			return result;
		}catch(DaoException daoe){
			throw new ServiceException("OutStorageRecordServiceImpl.getOutStorageRecordfilter() "+daoe.getMessage(),daoe.getException());
		}
	}
	
	public long countOutStorageRecordfilter(String configure) throws ServiceException{
		try{
			String realConfigure = getConfigure(configure);
			return outStorageRecordDAO.countOutStorageRecord(realConfigure);
		}catch(DaoException daoe){
			throw new ServiceException("InStorageRecordServiceImpl.countOutStorageRecordfilter() "+daoe.getMessage(),daoe.getException());
		}
	}
	
	//start
	private String getConfigure(String configure) {
		if(configure == null){
			return null;
		}
	    String new_configure = "where ";
	    String filter[] = configure.split("@@");
	    for(int i=0;i<filter.length;i++){
	    	String filters[] = filter[i].split("@");
	    	try {
	    		if(i==0){
	    			new_configure = new_configure + splitJointConfigure(filters[0],filters[1],filters[2]);
	    		}else{
	    			new_configure = new_configure +" AND " + splitJointConfigure(filters[0],filters[1],filters[2]);
	    		}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    return new_configure;
	}
	
	private String splitJointConfigure (String field,String mark,String value){
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(field.equals("type_name")){
			if(mark.equals("eq")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("eq", value, "type.name"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("neq",value,"type.name"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("startswith",value,"type.name"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("endswith",value,"type.name"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("contains",value,"type.name"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("doesnotcontain",value,"type.name"));
			}
		}
		if(field.equals("createUserName")){
			if(mark.equals("eq")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("eq", value, "createUserName"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("neq",value,"createUserName"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("startswith",value,"createUserName"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("endswith",value,"createUserName"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("contains",value,"createUserName"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("doesnotcontain",value,"createUserName"));
			}
		}
		if(field.equals("note")){
			if(mark.equals("eq")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("eq", value, "note"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("neq",value,"note"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("startswith",value,"note"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("endswith",value,"note"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("contains",value,"note"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(outStorageRecordDAO.getfilter("doesnotcontain",value,"note"));
			}
		}
		if(field.equals("no")){
			return FilterUtils.getConditionStr("e.no",mark,value);
		}
		return null;
	}
	
	private String getProviderTypeId(List<OutStorageRecord> serviceProviderId){
		List<String> searchId = new ArrayList<String>();
		for(int i=0;i<serviceProviderId.size();i++){
			searchId.add(new String(serviceProviderId.get(i).getNo()));
		}
		return FilterUtils.FieldConfigureStr(searchId, "e.no");
	}

	@Override
	public OutStorageRecordDAO getDAO() {
		return outStorageRecordDAO;
	}
	
	/*@Transactional(propagation = Propagation.REQUIRED)
	public OutStorageRecord reviewOrder(String no,String userName, Long organization) throws Exception {
		OutStorageRecord result = null;
		try {
			//获取出库单
			//获取业务单_2_商品
			//商品实例出库
			result = outStorageRecordDAO.findById(no);
			BusinessOrderVO vo = new BusinessOrderVO(no);
			
			//TODO: 添加未审核校验
			if(result != null){
				PagingSimpleModelVO<BussToMerchandises> vos = bussToMerchandisesService.getAll(result.getNo());
				if(!CollectionUtils.isEmpty(vos.getListOfModel())){
					List<BussToMerchandises> relatoins = vos.getListOfModel();
					for(BussToMerchandises item : relatoins){
						MerchandiseInstance instance = merchandiseInstanceService.findById(item.getNo_2());
						item.setInstance(instance);
					}
					vo.setMerchandises(relatoins);
				}
			}
			boolean reduceOK = true;
			for(BussToMerchandises item : vo.getMerchandises()){
				reduceOK = reduceOK && merchandiseStorageInfoService.reduceStorage(item.getInstance(), item.getCount(), item.getStorage_2(), result.getNo(), true, BusinessTypeEnums.OUT_STORAGE.getTypeName(),userName,organization);
				if(!reduceOK){
					throw new Exception();
				}
			}
		} catch (Exception e) {
			LOG.error("addBusinessOrder()");
			throw new Exception();
		}
		return result;
	}*/
}
