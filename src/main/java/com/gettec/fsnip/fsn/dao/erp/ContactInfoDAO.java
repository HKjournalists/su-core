package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.ContactInfo;

public interface ContactInfoDAO extends BaseDAO<ContactInfo> {

	List<ContactInfo> getContactsByTypeAndNo(int type, Long no,Long organization);
	void removeByNo(String no) throws DaoException;
	List<ContactInfo> getListByBusIdAndType(int page, int pageSize, 
			Long bussId, int type, Long organization) throws ServiceException;
	Long countByBusIdAndTypeAndOrgId(Long bussId, int type, Long organization) throws ServiceException;
}
