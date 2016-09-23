package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.CustomerToContactinfo;

public interface CustomerToContactinfoDAO extends BaseDAO<CustomerToContactinfo> {

	List<CustomerToContactinfo> getListByIdAndType(Long id, int type) throws DaoException;

	void deleteByCIdAndOrgId(Long cId, Long organization)throws DaoException;
}
