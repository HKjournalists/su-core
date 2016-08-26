package com.gettec.fsnip.fsn.service.erp.buss.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.buss.FlittingOrderDAO;
import com.gettec.fsnip.fsn.dao.erp.buss.MerchandiseStorageInfoDAO;
import com.gettec.fsnip.fsn.enums.BusinessTypeEnums;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.model.erp.buss.BussToMerchandises;
import com.gettec.fsnip.fsn.model.erp.buss.FlittingOrder;
import com.gettec.fsnip.fsn.model.erp.buss.MerchandiseStorageInfo;
import com.gettec.fsnip.fsn.model.erp.buss.MerchandiseStorageInfoPK;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.OrderTypeService;
import com.gettec.fsnip.fsn.service.erp.buss.BussToMerchandisesService;
import com.gettec.fsnip.fsn.service.erp.buss.FlittingOrderService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseInstanceService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseStorageInfoService;
import com.gettec.fsnip.fsn.transfer.BusinessOrderTransfer;
import com.gettec.fsnip.fsn.transfer.impl.FlittingOrderTransfer;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.BusinessOrderVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service
public class FlittingOrderServiceImpl extends BaseServiceImpl<FlittingOrder,FlittingOrderDAO>
		implements FlittingOrderService {
	@Autowired private FlittingOrderDAO flittingOrderDAO;
	@Autowired private BussToMerchandisesService bussToMerchandisesService;
	@Autowired private MerchandiseStorageInfoDAO merchandiseStorageInfoDAO;
	@Autowired private MerchandiseStorageInfoService merchandiseStorageInfoService;
	@Autowired private OrderTypeService orderTypeService;
	@Autowired private MerchandiseInstanceService merchandiseInstanceService;

	private BusinessOrderTransfer<FlittingOrder> transfer = new FlittingOrderTransfer();

	/**
	 * 商品调拨
	 * @param vo
	 * @param organization
	 * @param userName
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public FlittingOrder addBusinessOrder(BusinessOrderVO vo,
			Long organization, String userName) throws ServiceException {
		try {
			FlittingOrder flitOrder = transfer.transferToRealEntity(vo);
			vo.setResult1(flitOrder);
			/* 1. 自动生成编号  */
			String autoNo = null;
			String no = flitOrder.getNo(); // 获取单别(三位)
			String noStart = orderTypeService.getAutoCodeNumStart(no) + BusinessTypeEnums.FLITTING_ORDER.getId();
			String noMax = getDAO().findNoMaxByNoStart(noStart); // 获取已有的最大编号
			if(noMax != null){
				autoNo = orderTypeService.getAutoCodeNum(noMax);
			}else{
				autoNo = noStart + "0000";
			}
			flitOrder.setCreateUserName(userName);
			flitOrder.setNo(autoNo);
			flitOrder.setOrganization(organization);
			/* 2. 新增一条调拨基本记录 */
			create(flitOrder);
			/* 3. 保存调拨商品列表信息 */
			boolean success = bussToMerchandisesService.save(vo.getMerchandises(), autoNo, new Long(BusinessTypeEnums.FLITTING_ORDER.getId()));
			/* 4. 调拨成功后，更新库存数量 */
			if (success) {
				for (BussToMerchandises item : vo.getMerchandises()) {
					merchandiseStorageInfoService.flittingStorage(
						item.getInstance(), item.getCount(),
						item.getStorage_2(), item.getStorage_1(),
						flitOrder.getNo(), BusinessTypeEnums.FLITTING_ORDER.getTypeName(),userName, organization);				
				}
			}
			return flitOrder;
		} catch (DaoException dex) {
			throw new ServiceException("[DaoException]FlittingOrderServiceImpl.addBusinessOrder()-->", dex.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]FlittingOrderServiceImpl.addBusinessOrder()-->", sex.getException());
		}
	}
	//判断调拨仓库数量
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean JudgeflittingNum(Long mid,String value,String batch,String storage) throws ServiceException{
		try {
			boolean flag = false;
			int Ivalue = Integer.parseInt(value);
			MerchandiseInstance instance = merchandiseInstanceService.getInstanceByNoAndBatchNumber(mid, batch);	
			MerchandiseStorageInfoPK id = new MerchandiseStorageInfoPK(storage,instance.getInstanceID());
			MerchandiseStorageInfo info = merchandiseStorageInfoDAO.findById(id);
			if(info != null){
				if(info.getCount() > 0 && info.getCount()>=Ivalue){
					flag = true;
				}
			}
			return flag;
		} catch (JPAException jpae) {
			throw new ServiceException("FlittingOrderServiceImpl.JudgeflittingNum()-->", jpae.getException());
		}
	}
	
	/**
	 * 调拨分页查询
	 * @param page
	 * @param pageSize
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<FlittingOrder> getPaging(int page, int pageSize,
			String keywords, Long organization) throws ServiceException {
		try {
			PagingSimpleModelVO<FlittingOrder> result = new PagingSimpleModelVO<FlittingOrder>();
			String condition = " where e.organization=?1";
			Object[] params = new Object[]{organization};
			result.setCount(flittingOrderDAO.count(condition, params));
			result.setListOfModel(flittingOrderDAO.getListByPage(page, pageSize, condition, params));
			return result;
		} catch (JPAException jpae) {
			throw new ServiceException("FlittingOrderServiceImpl.getPaging()-->", jpae.getException());
		}
	}

	public PagingSimpleModelVO<FlittingOrder> getFlittingOrderfilter(int page, int pageSize,String configure, Long organization)throws ServiceException {
		try{
			PagingSimpleModelVO<FlittingOrder> result = new PagingSimpleModelVO<FlittingOrder>();
			String condition = getConfigure(configure);
			condition += " and e.organization = ?1";
			Object[] params = new Object[]{organization};
			long count = flittingOrderDAO.count(condition, params);
			result.setCount(count);
			result.setListOfModel(flittingOrderDAO.getListByPage(page, pageSize, condition, params));
			return result;
		}catch(JPAException jpae){
			throw new ServiceException("FlittingOrderServiceImpl.getFlittingOrderfilter() "+jpae.getMessage(),jpae.getException());
		}
	}
	
	public long countFlittingOrderfilter(String configure)throws ServiceException  {
		try{
			String condition = getConfigure(configure);
			return flittingOrderDAO.count(condition);
		}catch(JPAException jpae){
			throw new ServiceException("FlittingOrderServiceImpl.countFlittingOrderfilter() "+jpae.getMessage(),jpae.getException());
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
				return getProviderTypeId(flittingOrderDAO.getfilter("eq", value, "type.name"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(flittingOrderDAO.getfilter("neq",value,"type.name"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(flittingOrderDAO.getfilter("startswith",value,"type.name"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(flittingOrderDAO.getfilter("endswith",value,"type.name"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(flittingOrderDAO.getfilter("contains",value,"type.name"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(flittingOrderDAO.getfilter("doesnotcontain",value,"type.name"));
			}
		}
		if(field.equals("createUserName")){
			if(mark.equals("eq")){
				return getProviderTypeId(flittingOrderDAO.getfilter("eq", value, "createUserName"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(flittingOrderDAO.getfilter("neq",value,"createUserName"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(flittingOrderDAO.getfilter("startswith",value,"createUserName"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(flittingOrderDAO.getfilter("endswith",value,"createUserName"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(flittingOrderDAO.getfilter("contains",value,"createUserName"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(flittingOrderDAO.getfilter("doesnotcontain",value,"createUserName"));
			}
		}
		if(field.equals("note")){
			if(mark.equals("eq")){
				return getProviderTypeId(flittingOrderDAO.getfilter("eq", value, "note"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(flittingOrderDAO.getfilter("neq",value,"note"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(flittingOrderDAO.getfilter("startswith",value,"note"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(flittingOrderDAO.getfilter("endswith",value,"note"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(flittingOrderDAO.getfilter("contains",value,"note"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(flittingOrderDAO.getfilter("doesnotcontain",value,"note"));
			}
		}
		if(field.equals("no")){
			return FilterUtils.getConditionStr("e.no",mark,value);
		}
		return null;
	}
	
	private String getProviderTypeId(List<FlittingOrder> serviceProviderId){
		List<String> searchId = new ArrayList<String>();
		for(int i=0;i<serviceProviderId.size();i++){
			searchId.add(new String(serviceProviderId.get(i).getNo()));
		}
		return FilterUtils.FieldConfigureStr(searchId, "e.no");
	}

	@Override
	public FlittingOrderDAO getDAO() {
		return flittingOrderDAO;
	}
	
	/*@Transactional(propagation = Propagation.REQUIRED)
	public FlittingOrder reviewOrder(String no, String userName,
			Long organization) throws Exception {
		FlittingOrder result = null;
		try {
			// 获取调拨单
			// 获取业务单_2_商品
			// 商品实例调拨
			result = flittingOrderDAO.findById(no);
			BusinessOrderVO vo = new BusinessOrderVO(no);

			// TODO: 添加未审核校验
			if (result != null) {
				PagingSimpleModelVO<BussToMerchandises> vos = bussToMerchandisesService
						.getAll(result.getNo());
				if (!CollectionUtils.isEmpty(vos.getListOfModel())) {
					List<BussToMerchandises> relatoins = vos.getListOfModel();
					for (BussToMerchandises item : relatoins) {
						MerchandiseInstance instance = merchandiseInstanceService.findById(item.getNo_2());				
						item.setInstance(instance);
					}
					vo.setMerchandises(relatoins);
				}
			}
			boolean operationOK = true;
			for (BussToMerchandises item : vo.getMerchandises()) {
				operationOK = operationOK
						&& merchandiseStorageInfoService.flittingStorage(
								item.getInstance(), item.getCount(),
								item.getStorage_2(), item.getStorage_1(),
								result.getNo(),BusinessTypeEnums.FLITTING_ORDER.getTypeName(), userName, organization);
				if (!operationOK) {
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
