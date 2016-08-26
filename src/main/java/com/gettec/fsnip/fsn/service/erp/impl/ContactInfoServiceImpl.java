package com.gettec.fsnip.fsn.service.erp.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.gettec.fsnip.fsn.dao.erp.ContactInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.erp.base.BusinessUnitToType;
import com.gettec.fsnip.fsn.model.erp.base.BusinessUnitToTypePK;
import com.gettec.fsnip.fsn.model.erp.base.ContactInfo;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;
import com.gettec.fsnip.fsn.model.erp.base.CustomerToContactinfo;
import com.gettec.fsnip.fsn.model.erp.base.CustomerToContactinfoPK;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.BusinessUnitToTypeService;
import com.gettec.fsnip.fsn.service.erp.ContactInfoService;
import com.gettec.fsnip.fsn.service.erp.CustomerAndProviderTypeService;
import com.gettec.fsnip.fsn.service.erp.CustomerToContactinfoService;
import com.gettec.fsnip.fsn.service.erp.OutOfBillService;
import com.gettec.fsnip.fsn.service.erp.ReceivingNoteService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("contactInfoService")
public class ContactInfoServiceImpl extends BaseServiceImpl<ContactInfo,ContactInfoDAO> implements ContactInfoService {

	@Autowired private ContactInfoDAO contactInfoDAO;
	@Autowired private OutOfBillService outOfBillService;
	@Autowired private CustomerToContactinfoService customerToContactinfoService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private BusinessUnitToTypeService businessUnitToTypeService;
	@Autowired private ReceivingNoteService receivingNoteService;
	@Autowired private CustomerAndProviderTypeService customerAndProviderTypeService;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<ContactInfo> addListOfContactInfo(List<ContactInfo> infos) {
		try{
			if(!CollectionUtils.isEmpty(infos)){
				for(ContactInfo info : infos){
					contactInfoDAO.persistent(info);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ContactInfo> updatedListOfContactInfo(List<ContactInfo> infos, Long NO) {
		
		try {
			for(ContactInfo info : infos){
				ContactInfo org = contactInfoDAO.findById(info.getId());
				org.updateContactInfo(info);
				info = org;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return infos;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<ContactInfo> getContactsByTypeAndNo(int type, Long no,Long organization) {
		return contactInfoDAO.getContactsByTypeAndNo(type, no,organization);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public ContactInfo getContactsByOutOrder(String num) {
		try {
			List<BigInteger> C_id = outOfBillService.getCidByOutOrder(num);
			Long cid = C_id.get(0).longValue();
			ContactInfo result = contactInfoDAO.findById(cid);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 保存客户的联系人
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(BusinessUnit businessUnit) {
		try {
			//1代表客户的联系人2代表供应商的联系人
			BusinessUnit orig_businessUnit = null;
			orig_businessUnit = businessUnitService.findById(businessUnit.getId());
			List<ContactInfo> contacts = businessUnit.getContacts(); // 联系人列表
			List<CustomerToContactinfo> orig_contacts = customerToContactinfoService.getListByIdAndType(
					businessUnit.getId(), businessUnit.getDiyType().getType()); // 原有的联系人列表
			/* 1. 获取删除的联系人列表 */
			List<CustomerToContactinfo> removes = getListOfRemoves(orig_contacts, contacts);
			/* 2. 保存联系人 */
			List<CustomerToContactinfo> adds = new ArrayList<CustomerToContactinfo>();
			for(ContactInfo contact : contacts){
				if(contact.getId() == null){
					contact.setOrganization(businessUnit.getOrganization());
					create(contact);
					CustomerToContactinfoPK pk = new CustomerToContactinfoPK(orig_businessUnit.getId(), contact.getId());
					CustomerToContactinfo custo2conta = new CustomerToContactinfo(pk);
					custo2conta.setType(businessUnit.getDiyType().getType());
					adds.add(custo2conta);
				}else{
					CustomerToContactinfo orig_cci = customerToContactinfoService.getDAO().findById(
							new CustomerToContactinfoPK(orig_businessUnit.getId(), contact.getId()));
					//更联系人中的 type
					if(orig_cci != null && businessUnit.getDiyType() != null) {
						orig_cci.setType(businessUnit.getDiyType().getType());
						customerToContactinfoService.update(orig_cci);
					}
					ContactInfo orig_contact = findById(contact.getId());
					setValueOfContact(orig_contact, contact);
					update(orig_contact);
				}
			}
			/* 3. 保存客户\供应商与联系人的关系 */
			if(!CollectionUtils.isEmpty(adds)){
				customerToContactinfoService.save(adds);
			}
			if(!CollectionUtils.isEmpty(removes)){
				for(CustomerToContactinfo customToContact : removes){
					customerToContactinfoService.delete(customToContact);
				}
			}
			/* 4. 保存客户\供应商自定义类型 */
			if(businessUnit.getDiyType() != null && businessUnit.getDiyType().getId() != null) {
				BusinessUnitToType orig_bus2type = businessUnitToTypeService.findByBusIdAndOrgIdAndType(businessUnit.getId(), businessUnit.getOrganization(), (Long)businessUnit.getDiyType().getType().longValue());
				if(orig_bus2type == null){
					BusinessUnitToTypePK pk = new BusinessUnitToTypePK();
					pk.setBusinessId(businessUnit.getId());
					pk.setTypeId(businessUnit.getDiyType().getId());
					BusinessUnitToType orig_bus2type1 = new BusinessUnitToType();
					orig_bus2type1.setId(pk);
					orig_bus2type1.setOrganization(businessUnit.getOrganization());
					orig_bus2type1.setType((Long)businessUnit.getDiyType().getType().longValue());
					businessUnitToTypeService.create(orig_bus2type1);
				}else{
					businessUnitToTypeService.getDAO().updateBusinessUnitTypeByBidAndOid(orig_bus2type.getId().getBusinessId(), businessUnit.getDiyType().getId(), orig_bus2type.getOrganization(), (Long)businessUnit.getDiyType().getType().longValue());
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setValueOfContact(ContactInfo orig_contact, ContactInfo contact) {
		orig_contact.setName(contact.getName());
		orig_contact.setAddr(contact.getAddr());
		orig_contact.setArea(contact.getArea());
		orig_contact.setCity(contact.getCity());
		orig_contact.setProvince(contact.getProvince());
		orig_contact.setEmail(contact.getEmail());
		orig_contact.setZipcode(contact.getZipcode());
		orig_contact.setIm_account(contact.getIm_account());
		orig_contact.setTel_1(contact.getTel_1());
		orig_contact.setTel_2(contact.getTel_2());
	}

	/**
	 * 对比前后的联系人列表，获取删除的集合
	 */
	private List<CustomerToContactinfo> getListOfRemoves(List<CustomerToContactinfo> orig_contacts, List<ContactInfo> contacts) {
		List<CustomerToContactinfo> removes = new ArrayList<CustomerToContactinfo>();
		List<Long> currentId = new ArrayList<Long>();
		
		for (ContactInfo contact : contacts) {
			Long id = contact.getId();
			if (id != null) {
				currentId.add(id);
			}
		}
		for (CustomerToContactinfo custo2conta : orig_contacts) {
			if (custo2conta.getId().getContactID()!=null && !currentId.contains(custo2conta.getId().getContactID())) {
				removes.add(custo2conta);
			}
		}
		return removes;
	}

	/**
	 * 根据企业id获取联系人信息
	 * @param businessUnit
	 * @param organization
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PagingSimpleModelVO<ContactInfo> getListContacts(int page,
			int pageSize, Long bussId, int type,Long organization) throws ServiceException {
		try {
			PagingSimpleModelVO<ContactInfo> result = new PagingSimpleModelVO<ContactInfo>();
			CustomerAndProviderType ctype = customerAndProviderTypeService.findByBid(bussId, null, organization);
			type = (ctype != null ? ctype.getType() : type);
			List<ContactInfo> contacts = contactInfoDAO.getListByBusIdAndType(page, pageSize, bussId, type, organization);
			result.setCount(contactInfoDAO.countByBusIdAndTypeAndOrgId(bussId, type, organization));
			result.setListOfModel(contacts);
			return result;
		} catch (ServiceException sex) {
			throw new ServiceException("ContactInfoServiceImpl.getListContacts()-->" + sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 根据收货单号，删除商品及其关联
	 * @param no
	 * @throws ServiceException 
	 */
	@Override
	public void removeByNo(String no) throws ServiceException {
		try {
			getDAO().removeByNo(no);
			receivingNoteService.getDAO().removeByNo(no);
		} catch (DaoException dex) {
			throw new ServiceException("ContacInfoServiceImpl.removeByNo()-->", dex.getException());
		}
	}
	
	@Override
	public ContactInfoDAO getDAO() {
		return contactInfoDAO;
	}
	/**
	 * 根据企业id获取联系人信息
	 * @param businessUnit
	 * @param organization
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ContactInfo> getListByBusIdAndType(int page,
			int pageSize, Long bussId, int type,Long organization) throws ServiceException {
		try {
			List<ContactInfo> contacts = contactInfoDAO.getListByBusIdAndType(1, 1, bussId, type, organization);
			if(contacts.size()<=0){
				return null;
			}
			return contacts;
		} catch (ServiceException sex) {
			throw new ServiceException("ContactInfoServiceImpl.getListByBusIdAndType()-->" + sex.getMessage(), sex.getException());
		}
	}
}
