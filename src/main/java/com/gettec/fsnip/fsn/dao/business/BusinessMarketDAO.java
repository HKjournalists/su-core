package com.gettec.fsnip.fsn.dao.business;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.BusinessMarket;
import com.gettec.fsnip.fsn.vo.business.BusinessMarketVO;

public interface BusinessMarketDAO extends BaseDAO<BusinessMarket>{

	public BusinessMarket findByOrg(Long organization)throws DaoException;

	public long getCountByBusidAndOrg(Long businessId, Long organization)throws DaoException;
	
	BusinessMarketVO findByBusinessId(Long businessId)throws DaoException;

}
