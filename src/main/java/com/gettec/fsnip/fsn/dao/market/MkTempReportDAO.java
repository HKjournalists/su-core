package com.gettec.fsnip.fsn.dao.market;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.market.MkTempReport;

public interface MkTempReportDAO extends BaseDAO<MkTempReport>{

	MkTempReport findByUserRealNameAndOrganization(String realUserName,
			Long myRealOrgnizationId) throws DaoException;

	MkTempReport getTempReportOrderNo(String serviceOrder);
	
}
