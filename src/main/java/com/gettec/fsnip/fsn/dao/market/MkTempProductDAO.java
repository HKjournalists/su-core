package com.gettec.fsnip.fsn.dao.market;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.market.MkTempProduct;

public interface MkTempProductDAO extends BaseDAO<MkTempProduct>{

	MkTempProduct findByUserRealNameAndOrganization(String realUserName,
			Long myRealOrgnizationId) throws DaoException;
	
}
