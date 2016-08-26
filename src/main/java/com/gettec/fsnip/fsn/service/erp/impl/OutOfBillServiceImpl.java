package com.gettec.fsnip.fsn.service.erp.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.OutOfBillDAO;
import com.gettec.fsnip.fsn.dao.erp.ReceivingNoteDAO;
import com.gettec.fsnip.fsn.dao.erp.buss.MerchandiseStorageInfoDAO;
import com.gettec.fsnip.fsn.enums.BusinessTypeEnums;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.erp.base.InitializeProduct;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.model.erp.base.OutBillToOutGoods;
import com.gettec.fsnip.fsn.model.erp.base.OutBillToOutGoodsPK;
import com.gettec.fsnip.fsn.model.erp.base.OutGoodsInfo;
import com.gettec.fsnip.fsn.model.erp.base.OutOfBill;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote;
import com.gettec.fsnip.fsn.model.erp.buss.MerchandiseStorageInfo;
import com.gettec.fsnip.fsn.model.erp.buss.MerchandiseStorageInfoPK;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.InitializeProductService;
import com.gettec.fsnip.fsn.service.erp.OrderTypeService;
import com.gettec.fsnip.fsn.service.erp.OutBillToOutGoodsService;
import com.gettec.fsnip.fsn.service.erp.OutGoodsInfoService;
import com.gettec.fsnip.fsn.service.erp.OutOfBillService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseInstanceService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseStorageInfoService;
import com.gettec.fsnip.fsn.service.erp.buss.StorageChangeLogService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("outOfBillService")
public class OutOfBillServiceImpl extends BaseServiceImpl<OutOfBill, OutOfBillDAO>
		implements OutOfBillService {
	@Autowired private OutOfBillDAO outOfBillDAO;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private OutBillToOutGoodsService outBillToOutGoodsService;
	@Autowired private OutGoodsInfoService outGoodsInfoService;
	@Autowired private OrderTypeService orderTypeService;
	@Autowired private ReceivingNoteDAO receivingNoteDAO;
	@Autowired private MerchandiseInstanceService merchandiseInstanceService;
	@Autowired private MerchandiseStorageInfoService merchandiseStorageInfoService;
	@Autowired private OutGoodsInfoService outOfGoodsService;
	@Autowired private InitializeProductService initializeProductService;
	@Autowired private StorageChangeLogService storageChangeLogService;
	@Autowired private MerchandiseStorageInfoDAO merchandiseStorageInfoDAO;

	/**
	 * 新建出货单
	 * @param outOfBill
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public OutOfBill add(OutOfBill outOfBill, Long organization) throws ServiceException {
		try{
			/* 1. 自动生成编号  */
			String autoNo = null;
			String no = outOfBill.getOutOfBillNo(); // 获取单别(三位)
			String noStart = orderTypeService.getAutoCodeNumStart(no) + BusinessTypeEnums.SALES_ORDER.getId();
			String noMax = outOfBillDAO.findNoMaxByNoStart(noStart); // 获取已有的最大编号
			if(noMax != null){
				autoNo = orderTypeService.getAutoCodeNum(noMax);
			}else{
				autoNo = noStart + "0000";
			}
			outOfBill.setOutOfBillNo(autoNo);
			/* 2. 新建一条收货单基本信息 */
			create(outOfBill);
			for(OutGoodsInfo good : outOfBill.getContacts()){
				if(good.getId() == null){
					/* 3. 新增商品信息 */
					outGoodsInfoService.create(good);
					/*4.添加储备数量*/
					InitializeProduct initializeProduct = initializeProductService.findByBarcodeAndOrgId(good.getNo(), organization);
					MerchandiseInstance merchandiseInstance = merchandiseInstanceService.getInstanceByNoAndBatchNumber(initializeProduct.getId(), good.getBatchNo());
					int count = Integer.parseInt(good.getOutNumber().toString());
					merchandiseStorageInfoService.changeReserves(merchandiseInstance,count,good.getFirstStorage(),"add");
				}
			}
			/* 5. 新增关系 */
			for(OutGoodsInfo good : outOfBill.getContacts()){
				outOfBill.addRelationShipInfo(good);
			}
			outBillToOutGoodsService.addRelationShips(outOfBill.getInfos());
			return outOfBill;
		}catch(DaoException dex){
			throw new ServiceException("[DaoException]OutOfBillServiceImpl.add()-->", dex.getException());
		}catch(ServiceException sex){
			throw new ServiceException("[ServiceException]OutOfBillServiceImpl.add()-->", sex.getException());
		}
	}

	/**
	 * 更新出货单
	 * @param outOfBill
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public OutOfBill updateOutOfBill(OutOfBill outOfBill) {
		try {
			/* 1. 更新出货单基本信息 */
			OutOfBill orig_outOfBill = outOfBillDAO.findById(outOfBill.getOutOfBillNo());
			if(!orig_outOfBill.getCustomer().getId().equals(outOfBill.getCustomer().getId())){
				orig_outOfBill.setCustomer(businessUnitService.findById(outOfBill.getCustomer().getId()));
			}
			setCustomerValue(orig_outOfBill, outOfBill);
			/* 2. 根据出货单号查找上关联的商品信息 */
			/* 3. 保存出库商品信息 */
			outGoodsInfoService.save(outOfBill);
			return orig_outOfBill;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void setCustomerValue(OutOfBill orig_outOfBill, OutOfBill outOfBill){
		orig_outOfBill.setOutDate(outOfBill.getOutDate());
		orig_outOfBill.setSource(outOfBill.getSource());
		orig_outOfBill.setContactInfo(outOfBill.getContactInfo());
		orig_outOfBill.setInvoice(outOfBill.getInvoice());
		orig_outOfBill.setTransportation(outOfBill.getTransportation());
		orig_outOfBill.setNote(outOfBill.getNote());
		orig_outOfBill.setOutOfBillState(outOfBill.getOutOfBillState());
		orig_outOfBill.setTotalPrice(outOfBill.getTotalPrice());
		orig_outOfBill.setContactProvince(outOfBill.getContactProvince());
		orig_outOfBill.setContactCity(outOfBill.getContactCity());
		orig_outOfBill.setContactArea(outOfBill.getContactArea());
		orig_outOfBill.setContactAddr(outOfBill.getContactAddr());
		orig_outOfBill.setContactZipcode(outOfBill.getContactZipcode());
		orig_outOfBill.setContactTel(outOfBill.getContactTel());
		orig_outOfBill.setUserName(outOfBill.getUserName());
	}
/**
 * 删除出货单
 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean remove(OutOfBill outOfBill) {
		try {
			OutOfBill orig_outOfBill = outOfBillDAO.findById(outOfBill.getOutOfBillNo());
			List<OutGoodsInfo> lists = outGoodsInfoService.getListByNo(outOfBill.getOutOfBillNo());
			/* 1.删除商品信息及其关系 */
			for(OutGoodsInfo good : lists){
				/*1.1减少储备数量*/
				InitializeProduct initializeProduct = initializeProductService.findByBarcodeAndOrgId(good.getNo(), orig_outOfBill.getOrganization());
				 MerchandiseInstance merchandiseInstance = merchandiseInstanceService.getInstanceByNoAndBatchNumber(initializeProduct.getId(), good.getBatchNo());
				 int count = Integer.parseInt(good.getOutNumber().toString());
				 merchandiseStorageInfoService.changeReserves(merchandiseInstance,count,good.getFirstStorage(),"reduce");	
				 /*1.2删除商品信息关系*/
				outOfGoodsService.delete(good);
				OutBillToOutGoodsPK pk = new OutBillToOutGoodsPK(outOfBill.getOutOfBillNo(), good.getId());
				OutBillToOutGoods orig_outBillToGood = outBillToOutGoodsService.findByPk(pk);
				outBillToOutGoodsService.delete(orig_outBillToGood);
			}
			/*2.删除出货单*/
			outOfBillDAO.remove(orig_outOfBill);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PagingSimpleModelVO<OutOfBill> getPaging(int page, int pageSize,
			String keywords, Long organization) throws ServiceException {
		try {
			PagingSimpleModelVO<OutOfBill> result = new PagingSimpleModelVO<OutOfBill>();
			String condition = " WHERE e.organization=?1";
			Object[] params = new Object[]{organization};
			if(keywords != null && keywords.trim()!="") {
				condition += " AND e.outOfBillNo like '%" + keywords + "%' or e.outOfBillState like '%" + keywords + "%'";
			}
			result.setCount(outOfBillDAO.count(condition, params));
			result.setListOfModel(outOfBillDAO.getListByPage(page, pageSize, condition, params));
			
			for(OutOfBill c : result.getListOfModel()){
				c.setContacts(outGoodsInfoService.getListByNo(c.getOutOfBillNo()));
			}
			
			return result;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]OutOfBillServiceImpl.getPaging()-->", jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]OutOfBillServiceImpl.getPaging()-->", sex.getException());
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONArray getOurOfBillByProviderNum(Long num,Long organization) throws ServiceException {
		try {
			BusinessUnit bus = null;
			BusinessUnit selfBus = null;
			bus = businessUnitService.findById(num);
			selfBus = businessUnitService.findByOrganization(organization);
			List<String> out_no = outOfBillDAO.getOurOfBillByProviderNum(bus.getOrganization(),selfBus.getId());
			JSONArray json = new JSONArray();
			int size = out_no.size();
			if(out_no!=null&&size>0){
				int count = 0;
				for(int i=0;i<size;i++){
					JSONObject jo = new JSONObject();
					List<ReceivingNote> result = receivingNoteDAO.getfilter("eq", out_no.get(count), "re_outofbill_num");
					if(!result.isEmpty()){
						out_no.remove(count);
						if(count != 0){
							count--;
						}else{
							count = 0;
						}
						
					}else{
						jo.put("no", count);
						jo.put("name", out_no.get(count));
						json.add(jo );
						count++;
					}
				}
			}
			return json;
		} catch (ServiceException sex) {
			throw new ServiceException("OutOfBillServiceImpl.getOurOfBillByProviderNum()-->", sex.getException());
		}
	}
	
	/**
	 * 出货单确认发货
	 * @param outOfBillNo
	 * @param userName
	 * @param organization
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean checkOne(String outOfBillNo,String userName, Long organization) throws ServiceException {
		try {
			List<OutGoodsInfo> outGoods = outGoodsInfoService.getListByNo(outOfBillNo);
			for(OutGoodsInfo good : outGoods) {
				InitializeProduct initProduct = initializeProductService.findByBarcodeAndOrgId(good.getNo(), organization);
				MerchandiseInstance instance = merchandiseInstanceService.getInstanceByNoAndBatchNumber(initProduct.getId(),
						good.getBatchNo());
				MerchandiseStorageInfoPK id = new MerchandiseStorageInfoPK(good.getFirstStorage().getNo(),instance.getInstanceID());
				MerchandiseStorageInfo storeInfo = merchandiseStorageInfoDAO.findById(id);
				int count = Integer.parseInt(good.getOutNumber().toString());
				storeInfo.setReserves(storeInfo.getReserves()-count);
				storageChangeLogService.log(instance, "无", good.getFirstStorage().getName(), count, BusinessTypeEnums.SALES_BILLING.getTypeName(), outOfBillNo,userName, organization);
			}
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]OutOfBillServiceImpl.checkOne()-->", sex.getException());
		}catch (JPAException jpae) {
			throw new ServiceException("[JPAException]OutOfBillServiceImpl.checkOne()-->", jpae.getException());
		}
		return outOfBillDAO.checkOne(outOfBillNo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean checkTwo(String outOfBillNo,String userName, Long organization) {
		return outOfBillDAO.checkTwo(outOfBillNo);
	}
	
	/**
	 * 出货单确认分页查询
	 * @param no  收货单号
	 * @param userName
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PagingSimpleModelVO<OutOfBill> getPagingSureOutofgood(int page, int pageSize,
			String keywords,String userName, Long organization) throws ServiceException {
		try {
			PagingSimpleModelVO<OutOfBill> result = new PagingSimpleModelVO<OutOfBill>();
			String condition = " WHERE e.organization=?1 AND e.outOfBillState='待发货'";
			Object[] params = new Object[]{organization};
			result.setCount(outOfBillDAO.count(condition, params));
			List<OutOfBill> outOfBills = outOfBillDAO.getListByPage(page, pageSize, condition, params);
			for(OutOfBill outOfBill : outOfBills){
				List<OutGoodsInfo> contacts = outGoodsInfoService.getListByNo(outOfBill.getOutOfBillNo());
				outOfBill.setContacts(contacts);
			}
			result.setListOfModel(outOfBills);
			return result;
		} catch (JPAException jpae) {
			throw new ServiceException("OutOfBillServiceImpl.getPagingSureOutofgood()-->", jpae.getException());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<BigInteger> getCidByOutOrder(String num) {
		return outOfBillDAO.getCidByOutOrder(num);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public OutOfBill getOutOrderInfoByOutOrder(String num) throws ServiceException {
		try {
			return outOfBillDAO.findById(num);
		} catch (JPAException jpae) {
			throw new ServiceException("OutOfBillServiceImpl.getOutOrderInfoByOutOrder()-->", jpae.getException());
		}
	}
	
	/**
	 * 根据过滤条件查询出货单列表信息(出货单管理模块)
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @param userName
	 * @return
	 * @throws ServiceException 
	 */
	public PagingSimpleModelVO<OutOfBill> getOutOfBillfilter(int page, int pageSize,
				String configure, Long organization, String userName) throws ServiceException {
		try {
			PagingSimpleModelVO<OutOfBill> result = new PagingSimpleModelVO<OutOfBill>();
			Map<String, Object> map = getConfigure(configure, organization, userName);
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			result.setListOfModel(outOfBillDAO.getListByPage(page, pageSize, condition, params));
			result.setCount(outOfBillDAO.count(condition, params));
			return result;
		} catch (JPAException jpae) {
			throw new ServiceException("OutOfBillServiceImpl.getOutOfBillfilter()-->", jpae.getException());
		}
	}

	/**
	 * 根据过滤条件查询出货单列表信息(出货单确认模块)
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @param userName
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public PagingSimpleModelVO<OutOfBill> getOutOfBillCheckfilter(int page, int pageSize,
			String configure, Long organization, String userName) throws ServiceException {
		try {
			PagingSimpleModelVO<OutOfBill> result = new PagingSimpleModelVO<OutOfBill>();
			Map<String, Object> map = getConfigure(configure, organization, userName);
			String condition = (String) map.get("condition") + " AND outOfBillState='待发货'";
			Object[] params = (Object[]) map.get("params");
			result.setListOfModel(outOfBillDAO.getListByPage(page, pageSize, condition, params));
			result.setCount(outOfBillDAO.count(condition, params));
			return result;
		} catch (JPAException jpae) {
			throw new ServiceException("OutOfBillServiceImpl.getOutOfBillCheckfilter()-->", jpae.getException());
		}
	}
	
	/**
	 * 按过滤条件拼接where字符串
	 * @param configure 页面过滤条件
	 * @param userName 
	 * @param organization 
	 * @param organization 
	 * @param userName  操作员
	 * @return
	 */
	private Map<String, Object> getConfigure(String configure, Long organization, String userName) {
		if(configure == null){
			return null;
		}
		String new_configure = " WHERE organization = ?1 AND userName = ?2";
	    String filter[] = configure.split("@@");
	    for(int i=0;i<filter.length;i++){
	    	String filters[] = filter[i].split("@");
	    	try {
	    		new_configure = new_configure +" AND " + splitJointConfigure(filters[0],filters[1],filters[2]);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    Map<String, Object> map = new HashMap<String, Object>();
		map.put("condition", new_configure);
		map.put("params", new Object[]{organization,userName});
	    return map;
	}
	
	private String splitJointConfigure (String field,String mark,String value){
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if(field.equals("customer_name")){
			if(mark.equals("eq")){
				return getProviderTypeId(outOfBillDAO.getfilter("eq", value, "customer.name"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(outOfBillDAO.getfilter("neq",value,"customer.name"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(outOfBillDAO.getfilter("startswith",value,"customer.name"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(outOfBillDAO.getfilter("endswith",value,"customer.name"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(outOfBillDAO.getfilter("contains",value,"customer.name"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(outOfBillDAO.getfilter("doesnotcontain",value,"customer.name"));
			}
		}
		if(field.equals("outOfBillState")){
			if(mark.equals("eq")){
				return getProviderTypeId(outOfBillDAO.getfilter("eq", value, "outOfBillState"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(outOfBillDAO.getfilter("neq",value,"outOfBillState"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(outOfBillDAO.getfilter("startswith",value,"outOfBillState"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(outOfBillDAO.getfilter("endswith",value,"outOfBillState"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(outOfBillDAO.getfilter("contains",value,"outOfBillState"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(outOfBillDAO.getfilter("doesnotcontain",value,"outOfBillState"));
			}
		}
		if(field.equals("outDate")){
			if(mark.equals("eq")){
				return getProviderTypeId(outOfBillDAO.getfilter("eq", value, "outDate"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(outOfBillDAO.getfilter("neq",value,"outDate"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(outOfBillDAO.getfilter("startswith",value,"outDate"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(outOfBillDAO.getfilter("endswith",value,"outDate"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(outOfBillDAO.getfilter("contains",value,"outDate"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(outOfBillDAO.getfilter("doesnotcontain",value,"outDate"));
			}
		}
		if(field.equals("outOfBillNo")){
			return FilterUtils.getConditionStr("e.outOfBillNo",mark,value);
		}
		return null;
	}
	
	private String getProviderTypeId(List<OutOfBill> serviceProviderId){
		List<String> searchId = new ArrayList<String>();
		for(int i=0;i<serviceProviderId.size();i++){
			searchId.add(new String(serviceProviderId.get(i).getOutOfBillNo()));
		}
		return FilterUtils.FieldConfigureStr(searchId, "e.outOfBillNo");
	}
	//end
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PagingSimpleModelVO<OutOfBill> getPagingByUserName(int page, int pageSize,
			String keywords,String userName, Long organization) throws ServiceException {
		try {
			PagingSimpleModelVO<OutOfBill> result = new PagingSimpleModelVO<OutOfBill>();
			String condition = " where e.organization=?1 and e.userName=?2 ";
			Object[] params = new Object[]{organization, userName};
			result.setCount(outOfBillDAO.count(condition, params));
			result.setListOfModel(outOfBillDAO.getListByPage(page, pageSize, condition, params));
			for(OutOfBill c : result.getListOfModel()){
				c.setContacts(outGoodsInfoService.getListByNo(c.getOutOfBillNo()));
			}
			return result;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]OutOfBillServiceImpl.getPagingByUserName()-->", jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]OutOfBillServiceImpl.getPagingByUserName()-->", sex.getException());
		}
	}

	@Override
	public OutOfBillDAO getDAO() {
		return outOfBillDAO;
	}
}
