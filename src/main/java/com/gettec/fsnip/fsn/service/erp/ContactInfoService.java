package com.gettec.fsnip.fsn.service.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.erp.ContactInfoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.erp.base.ContactInfo;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface ContactInfoService extends BaseService<ContactInfo,ContactInfoDAO>{

	List<ContactInfo> addListOfContactInfo(List<ContactInfo> infos);
	List<ContactInfo> updatedListOfContactInfo(List<ContactInfo> infos, Long NO);
	
	List<ContactInfo> getContactsByTypeAndNo(int type, Long no,Long organization);
	//根据来源单号查询联系人
	ContactInfo getContactsByOutOrder(String num);
	void save(BusinessUnit businessUnit);
	/*
	 * 获取联系人
	 * author：cgw
	 * date：2014-11-3*/
	public PagingSimpleModelVO<ContactInfo> getListContacts(int page,
			int pageSize,Long busnessItemID,int type,Long organization) throws ServiceException;
	void removeByNo(String no) throws ServiceException;
	public List<ContactInfo> getListByBusIdAndType(int page,
			int pageSize, Long bussId, int type,Long organization) throws ServiceException;
}
