package com.gettec.fsnip.fsn.service.erp.buss.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.buss.InStorageRecordDAO;
import com.gettec.fsnip.fsn.enums.BusinessTypeEnums;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.buss.BussToMerchandises;
import com.gettec.fsnip.fsn.model.erp.buss.InStorageRecord;
import com.gettec.fsnip.fsn.model.erp.buss.InStorageType;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.InStorageTypeService;
import com.gettec.fsnip.fsn.service.erp.OrderTypeService;
import com.gettec.fsnip.fsn.service.erp.buss.BussToMerchandisesService;
import com.gettec.fsnip.fsn.service.erp.buss.InStorageRecordService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseStorageInfoService;
import com.gettec.fsnip.fsn.transfer.BusinessOrderTransfer;
import com.gettec.fsnip.fsn.transfer.impl.InStorageRecordTransfer;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.BusinessOrderVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service
public class InStorageRecordServiceImpl extends BaseServiceImpl<InStorageRecord, InStorageRecordDAO>
		implements InStorageRecordService {
	@Autowired private InStorageRecordDAO inStorageRecordDAO; 
	@Autowired private InStorageTypeService inStorageTypeService;
	@Autowired private BussToMerchandisesService bussToMerchandisesService;
	@Autowired private MerchandiseStorageInfoService merchandiseStorageInfoService;
	@Autowired private OrderTypeService orderTypeService;
	
	private BusinessOrderTransfer<InStorageRecord> transfer = new InStorageRecordTransfer();
	
	/**
	 * 商品入库
	 * @param vo
	 * @param organization
	 * @param userName
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public InStorageRecord addBusinessOrder(BusinessOrderVO vo, Long organization, String userName) throws ServiceException {
		try {
			InStorageRecord inRecord = transfer.transferToRealEntity(vo);
			/* 1. 自动生成编号  */
			String autoNo = null;
			String no = inRecord.getNo(); // 获取单别(三位)
			String noStart = orderTypeService.getAutoCodeNumStart(no) + BusinessTypeEnums.IN_STORAGE.getId();
			String noMax = getDAO().findNoMaxByNoStart(noStart); // 获取已有的最大编号
			if(noMax != null){
				autoNo = orderTypeService.getAutoCodeNum(noMax);
			}else{
				autoNo = noStart + "0000";
			}
			inRecord.setCreateUserName(userName);
			inRecord.setNo(autoNo);
			inRecord.setOrganization(organization);
			/* 2. 新增一条入库基本记录 */
			InStorageType orig_type = inStorageTypeService.findById(vo.getTypeInstance());
			inRecord.setType(orig_type);
			create(inRecord);
			/* 3. 保存商品列表信息 */
			boolean success = bussToMerchandisesService.save(vo.getMerchandises(), autoNo, new Long(BusinessTypeEnums.IN_STORAGE.getId()));
			/* 4. 入库成功后，添加库存数量 */
			if(success){
				for(BussToMerchandises item : vo.getMerchandises()){
					merchandiseStorageInfoService.addStorage(item.getInstance(), item.getCount(),
							item.getStorage_1(), inRecord.getNo(), true, BusinessTypeEnums.IN_STORAGE.getTypeName(),userName, organization);
				}
			}
			return inRecord;
		} catch (DaoException dex) {
			throw new ServiceException("[DaoException]InStorageRecordServiceImpl.addBusinessOrder()-->" + dex.getMessage(), dex.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]InStorageRecordServiceImpl.addBusinessOrder()-->" + sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 商品入库分页查询
	 * @param page
	 * @param pageSize
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<InStorageRecord> getPaging(int page,
			int pageSize, String keywords, Long organization) throws ServiceException {
		try {
			PagingSimpleModelVO<InStorageRecord> result = new PagingSimpleModelVO<InStorageRecord>();
			String condition = " WHERE e.organization=?1";
			Object[] params = new Object[]{organization};
			result.setCount(inStorageRecordDAO.count(condition, params));	
			result.setListOfModel(inStorageRecordDAO.getListByPage(page, pageSize, condition, params));
			return result;
		} catch (JPAException jpae) {
			throw new ServiceException("InStorageRecordServiceImpl.getPaging()-->", jpae.getException());
		}
	}

	public PagingSimpleModelVO<InStorageRecord> getInStorageRecordfilter(int page, int pageSize,String configure, Long organization) throws ServiceException{
		PagingSimpleModelVO<InStorageRecord> result = new PagingSimpleModelVO<InStorageRecord>();
		try{
			String realConfigure = getConfigure(configure);
			realConfigure += " and e.organization = " + organization;
			long count = inStorageRecordDAO.countInStorageRecord(realConfigure);
			result.setCount(count);
			result.setListOfModel(inStorageRecordDAO.getInStorageRecordfilter_(page, pageSize, realConfigure));
			return result;
		}catch(DaoException daoe){
			throw new ServiceException("InStorageRecordServiceImpl.getInStorageRecordfilter() "+daoe.getMessage(),daoe.getException());
		}
	}
	
	public long countInStorageRecordfilter(String configure) throws ServiceException {
		try{
			String realConfigure = getConfigure(configure);
			return inStorageRecordDAO.countInStorageRecord(realConfigure);
		}catch(DaoException daoe){
			throw new ServiceException("InStorageRecordServiceImpl.countInStorageRecordfilter() "+daoe.getMessage(),daoe.getException());
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
				return getProviderTypeId(inStorageRecordDAO.getfilter("eq", value, "type.name"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("neq",value,"type.name"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("startswith",value,"type.name"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("endswith",value,"type.name"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("contains",value,"type.name"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("doesnotcontain",value,"type.name"));
			}
		}
		if(field.equals("createUserName")){
			if(mark.equals("eq")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("eq", value, "createUserName"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("neq",value,"createUserName"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("startswith",value,"createUserName"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("endswith",value,"createUserName"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("contains",value,"createUserName"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("doesnotcontain",value,"createUserName"));
			}
		}
		if(field.equals("note")){
			if(mark.equals("eq")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("eq", value, "note"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("neq",value,"note"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("startswith",value,"note"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("endswith",value,"note"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("contains",value,"note"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(inStorageRecordDAO.getfilter("doesnotcontain",value,"note"));
			}
		}
		if(field.equals("no")){
			return FilterUtils.getConditionStr("e.no",mark,value);
		}
		return null;
	}
	
	private String getProviderTypeId(List<InStorageRecord> serviceProviderId){
		List<String> searchId = new ArrayList<String>();
		for(int i=0;i<serviceProviderId.size();i++){
			searchId.add(new String(serviceProviderId.get(i).getNo()));
		}
		return FilterUtils.FieldConfigureStr(searchId, "e.no");
	}

	@Override
	public InStorageRecordDAO getDAO() {
		return inStorageRecordDAO;
	}
	
	/*@Transactional(propagation = Propagation.REQUIRED)
	public InStorageRecord reviewOrder(String no,String userName, Long organization) throws Exception {
		InStorageRecord result = null;
		try {
			//获取入库单
			//获取业务单_2_商品
			//商品实例入库
			result = inStorageRecordDAO.findById(no);
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
			for(BussToMerchandises item : vo.getMerchandises()){
				merchandiseStorageInfoService.addStorage(item.getInstance(), item.getCount(), item.getStorage_1(), result.getNo(), true,BusinessTypeEnums.IN_STORAGE.getTypeName(), userName,organization);
			}
		} catch (Exception e) {
			LOG.error("addBusinessOrder()");
			throw new Exception();
		}
		return result;
	}*/
}
