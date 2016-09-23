package com.gettec.fsnip.fsn.service.erp.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.gettec.fsnip.fsn.dao.erp.ReceivingNoteDAO;
import com.gettec.fsnip.fsn.dao.product.ProductDAO;
import com.gettec.fsnip.fsn.enums.BusinessTypeEnums;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.InitializeProduct;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.model.erp.base.PurchaseorderInfo;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote.state;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNoteToContactinfo;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNoteToContactinfoPK;
import com.gettec.fsnip.fsn.model.erp.base.StorageInfo;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.ContactInfoService;
import com.gettec.fsnip.fsn.service.erp.InitializeProductService;
import com.gettec.fsnip.fsn.service.erp.OrderTypeService;
import com.gettec.fsnip.fsn.service.erp.ProviderService;
import com.gettec.fsnip.fsn.service.erp.PurchaseorderInfoService;
import com.gettec.fsnip.fsn.service.erp.ReceivingNoteService;
import com.gettec.fsnip.fsn.service.erp.ReceivingNoteToPurchaseorderInfoService;
import com.gettec.fsnip.fsn.service.erp.StorageInfoService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseInstanceService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseStorageInfoService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("receivingNoteService")
public class ReceivingNoteServiceImpl extends BaseServiceImpl<ReceivingNote, ReceivingNoteDAO>
		implements ReceivingNoteService{
	@Autowired private ReceivingNoteDAO receivingNoteDAO;
	@Autowired private OrderTypeService orderTypeService;
	@Autowired private InitializeProductService initializeProductService;
	@Autowired private PurchaseorderInfoService purchaseorderInfoService;
	@Autowired private ProductDAO productDAO;
	@Autowired private ReceivingNoteToPurchaseorderInfoService receivingNoteToPurchaseorderInfoService;
	@Autowired private ProviderService providerService;
	@Autowired private ContactInfoService contactInfoService;
	@Autowired private StorageInfoService storageInfoService;
	@Autowired private MerchandiseInstanceService merchandiseInstanceService;
	@Autowired private MerchandiseStorageInfoService merchandiseStorageInfoService;
	
	/**
	 * 新增收货单
	 * @param receivNote
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ReceivingNote add(ReceivingNote receivNote, Long organization) throws ServiceException {
		try {
			/* 1. 自动生成编号  */
			String autoNo = null;
			String no = receivNote.getRe_num(); // 获取单别(三位)
			String noStart = orderTypeService.getAutoCodeNumStart(no) + BusinessTypeEnums.PURCHASE_ORDER.getId();
			String noMax = receivingNoteDAO.findNoMaxByNoStart(noStart); // 获取已有的最大编号
			if(noMax != null){
				autoNo = orderTypeService.getAutoCodeNum(noMax);
			}else{
				autoNo = noStart + "0000";
			}
			receivNote.setRe_num(autoNo);
			receivNote.setRe_purchase_check(state.已确认);
			/* 2. 新增收货单基本信息 */
			create(receivNote);
			/* 3. 保存收货单商品及其关系 */
			purchaseorderInfoService.save(receivNote, organization);
			return receivNote;
		} catch (DaoException dex) {
			throw new ServiceException("[DaoException]ReceivingNoteServiceImpl.add()-->", dex.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]ReceivingNoteServiceImpl.add()-->", sex.getException());
		}
	}
	
	/**
	 * 更新收货单
	 * @param receivNote
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ReceivingNote updateReceivNote(ReceivingNote receivNote) throws ServiceException {
		try {
			ReceivingNote orig_receivNote = receivingNoteDAO.findById(receivNote.getRe_num());
			/* 1. 处理供应商 */
			if(!orig_receivNote.getRe_provide_num().getId().equals(receivNote.getRe_provide_num().getId())){
				orig_receivNote.setRe_provide_num(providerService.findById(receivNote.getRe_provide_num().getId()));
			}
			/* 2. 处理联系人 */
			if(!orig_receivNote.getRe_contact_id().getId().equals(receivNote.getRe_contact_id().getId()) ){
				orig_receivNote.setRe_contact_id(contactInfoService.findById(receivNote.getRe_contact_id().getId()));
			}
			/* 2. 更新收货单基本信息 */
			setReceivNoteValue(orig_receivNote, receivNote);
			update(orig_receivNote);
			/* 3. 获取删除的商品列表 */
			List<PurchaseorderInfo> orig_purchaseGood = purchaseorderInfoService.getListByNo(orig_receivNote.getRe_num());
			List<PurchaseorderInfo> removes = getListOfRemoves(orig_purchaseGood, receivNote.getContacts());
			/* 4. 保存收货单商品及其关系 */
			purchaseorderInfoService.save(receivNote, receivNote.getOrganization());
			/* 5. 删除商品及其关系 */
			if(!CollectionUtils.isEmpty(removes)){
				for(PurchaseorderInfo good : removes){
					purchaseorderInfoService.delete(good);
					ReceivingNoteToContactinfoPK pk = new ReceivingNoteToContactinfoPK(receivNote.getRe_num(), good.getPo_id());
					ReceivingNoteToContactinfo orig_receivToGood = receivingNoteToPurchaseorderInfoService.findByPk(pk);
					receivingNoteToPurchaseorderInfoService.delete(orig_receivToGood);
				}
			}
			return orig_receivNote;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]ReceivingNoteServiceImpl.updateReceivNote()-->", jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]ReceivingNoteServiceImpl.updateReceivNote()-->", sex.getException());
		}
	}
	
	/**
	 * 对比前后的收货单产品列表，获取删除的集合
	 */
	private List<PurchaseorderInfo> getListOfRemoves(List<PurchaseorderInfo> orig_receivNotes, List<PurchaseorderInfo> receivNotes) {
		List<PurchaseorderInfo> removes = new ArrayList<PurchaseorderInfo>();
		List<Long> currentId = new ArrayList<Long>();
		for (PurchaseorderInfo good : receivNotes) {
			Long id = good.getPo_id();
			if (id != null) {
				currentId.add(id);
			}
		}
		for (PurchaseorderInfo good : orig_receivNotes) {
			if (good.getPo_id()!=null && !currentId.contains(good.getPo_id())) {
				removes.add(good);
			}
		}
		return removes;
	}
	
	/**
	 * 赋值操作
	 * @param orig_receivNote
	 * @param receivNote
	 */
	private void setReceivNoteValue(ReceivingNote orig_receivNote, ReceivingNote receivNote){
		orig_receivNote.setRe_date(receivNote.getRe_date());
		orig_receivNote.setRe_source(receivNote.getRe_source());
		orig_receivNote.setRe_checkman(receivNote.getRe_checkman());
		orig_receivNote.setRe_before_pay(receivNote.getRe_before_pay());
		orig_receivNote.setRe_fact_pay(receivNote.getRe_fact_pay());
		orig_receivNote.setRe_purchase_check(receivNote.getRe_purchase_check());
		orig_receivNote.setRe_remarks(receivNote.getRe_remarks());
		orig_receivNote.setUserName(receivNote.getUserName());
		orig_receivNote.setRe_pdate(receivNote.getRe_pdate());
	}

	/**
	 * 删除收货单
	 * @param orig_receivNote
	 * @param receivNote
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean remove(ReceivingNote recievNote) {
		try {
			/* 1. 删除收货单基本信息 */
			ReceivingNote orig_recievNote = receivingNoteDAO.findById(recievNote.getRe_num());
			delete(orig_recievNote);
			/* 2. 删除商品及其关联 */
			contactInfoService.removeByNo(recievNote.getRe_num());
			return true;
		} catch (Exception e2) {
			return false;
		}
	}

	/**
	 * 收货单确认分页查询
	 * @param page
	 * @param pageSize
	 * @param keywords
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<ReceivingNote> getPagingSureReceivingnote(int page, int size,
			String keywords, Long organization) throws ServiceException {
		try {
			PagingSimpleModelVO<ReceivingNote> result = new PagingSimpleModelVO<ReceivingNote>();
			String condition = " WHERE e.organization=?1 AND e.re_purchase_check = '已确认'";
			Object[] params = new Object[]{organization};
			result.setCount(getDAO().count(condition, params));
			List<ReceivingNote> receivingNotes = getDAO().getListByPage(page, size, condition, params);
			for(ReceivingNote receivingNote : receivingNotes){
				List<PurchaseorderInfo> contacts = purchaseorderInfoService.getListByNo(receivingNote.getRe_num());
				receivingNote.setContacts(contacts);
			}
			result.setListOfModel(receivingNotes);
			return result;
		} catch (JPAException jpae) {
			throw new ServiceException("ReceivingNoteServiceImpl.getPagingSureReceivingnote()-->", jpae.getException());
		}
	}
	
	/**
	 * 收货单确认
	 * @param no  收货单号
	 * @param userName
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean checkReceivingnote(String no, String userName, Long organization) {
		try {
			ReceivingNote orig_receivNote = getDAO().findById(no);
			orig_receivNote.setContacts(purchaseorderInfoService.getListByNo(no));
			
			for(int i = 0; i < orig_receivNote.getContacts().size(); i++) {
				orig_receivNote.getContacts().get(i).getProduct().setId(
						productDAO.findByBarcode(orig_receivNote.getContacts().get(i).getProduct().getBarcode()).getId());
				//查看以前是否记录过该商品，如果没有则新增记录
				InitializeProduct initializeProduct=initializeProductService.findByProIdAndOrgId(
						orig_receivNote.getContacts().get(i).getProduct().getId(),organization);		
				MerchandiseInstance instance = merchandiseInstanceService.saveInstance(
						initializeProduct,orig_receivNote.getContacts().get(i).getPo_batch());
				StorageInfo storageInfo = storageInfoService.findByName(
						orig_receivNote.getContacts().get(i).getPo_storage_address(),organization);
				
				merchandiseStorageInfoService.addStorage(instance, (int)orig_receivNote.getContacts().get(i).getPo_receivenum(),
						storageInfo, orig_receivNote.getRe_num(),
						true,BusinessTypeEnums.PURCHASE_BILLING.getTypeName(),userName, organization);
			}
			return receivingNoteDAO.checkReceivingnote(no);
		} catch (JPAException jpae) {
			jpae.printStackTrace();
			return false;
		}  catch (DaoException dex) {
			dex.printStackTrace();
			return false;
		} catch (ServiceException sex) {
			sex.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 收货单确认过滤分页查询
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @param userName
	 * @return
	 * @throws ServiceException 
	 */
	public Object getReceivingNoteCheckfilter(int page, int pageSize, String configure,
				Long organization, String userName) throws ServiceException {
		try {
			PagingSimpleModelVO<ReceivingNote> result = new PagingSimpleModelVO<ReceivingNote>();
			Map<String, Object> map = getConfigure(configure, organization, userName);
			String condition = (String) map.get("condition") + " and re_purchase_check = '已确认'";
			Object[] params = (Object[]) map.get("params");
			result.setCount(receivingNoteDAO.count(condition, params));
			result.setListOfModel(receivingNoteDAO.getListByPage(page, pageSize, condition, params));
			return result;
		} catch (JPAException jpae) {
			throw new ServiceException("ReceivingNoteServiceImpl.getReceivingNoteCheckfilter()-->", jpae.getException());
		}
	}

	/**
	 * 根据过滤条件查询收货单列表信息
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @param userName
	 * @return
	 * @throws ServiceException 
	 */
	public PagingSimpleModelVO<ReceivingNote> getReceivingNotefilter(int page, int pageSize,String configure,
				Long organization, String userName) throws ServiceException {
		try {
			PagingSimpleModelVO<ReceivingNote> result = new PagingSimpleModelVO<ReceivingNote>();
			Map<String, Object> map = getConfigure(configure, organization, userName);
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			result.setCount(receivingNoteDAO.count(condition, params));
			result.setListOfModel(receivingNoteDAO.getListByPage(page, pageSize, condition, params));
			return result;
		} catch (JPAException jpae) {
			throw new ServiceException("ReceivingNoteServiceImpl.getReceivingNotefilter()-->", jpae.getException());
		}
	}
	
	/**
	 * 按过滤条件拼接where字符串
	 * @param configure 页面过滤条件
	 * @param organization 
	 * @param userName  操作员
	 * @return
	 */
	private  Map<String, Object> getConfigure(String configure, Long organization, String userName) {
		if(configure == null){
			return null;
		}
	    String new_configure = " WHERE organization = ?1 AND userName = ?2";
	    String filter[] = configure.split("@@");
	    for(int i=0;i<filter.length;i++){
	    	String filters[] = filter[i].split("@");
	    	try {
	    		new_configure += " AND " + splitJointConfigure(filters[0],filters[1],filters[2]);
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
		if(field.equals("re_checkman")){
			return FilterUtils.getConditionStr("re_checkman",mark,value,true);
		}
		if(field.equals("re_provide_num_name")){
			return FilterUtils.getConditionStr("re_provide_num_name",mark,value,true);
		}
		if(field.equals("re_remarks")){
			return FilterUtils.getConditionStr("re_remarks",mark,value,true);
		}
		if(field.equals("re_num")){
			return FilterUtils.getConditionStr("e.re_num",mark,value);
		}
		return null;
	}
	
	/**
	 * 收货单分页查询
	 * @param page
	 * @param size
	 * @param keywords
	 * @param userName
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<ReceivingNote> getPagingByUserName(int page, int size,
			String keywords,String userName, Long organization) throws ServiceException {
		try {
			PagingSimpleModelVO<ReceivingNote> result = new PagingSimpleModelVO<ReceivingNote>();
			String condition = " WHERE e.organization=?1 and e.userName=?2 ";
			Object[] params = new Object[]{organization, userName};
			result.setCount(getDAO().count(condition, params));
			List<ReceivingNote> receivingNotes = getDAO().getListByPage(page, size, condition, params);
			for(ReceivingNote receivingNote : receivingNotes){
				List<PurchaseorderInfo> contacts = purchaseorderInfoService.getListByNo(receivingNote.getRe_num());
				receivingNote.setContacts(contacts);
			}
			result.setListOfModel(receivingNotes);
			return result;
		} catch (JPAException jpae) {
			throw new ServiceException("ReceivingNoteServiceImpl.getPagingByUserName()-->", jpae.getException());
		}
	}
	
	@Override
	public ReceivingNoteDAO getDAO() {
		return receivingNoteDAO;
	}
}
