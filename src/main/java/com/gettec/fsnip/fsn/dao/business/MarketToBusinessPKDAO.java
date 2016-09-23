package com.gettec.fsnip.fsn.dao.business;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.MarketToBusiness;

public interface MarketToBusinessPKDAO extends BaseDAO<MarketToBusiness>{

		public long getCountByLicenseAndOrg(String license, Long organization)throws DaoException;

		public List<MarketToBusiness> getByOrganization(
				Map<String, Object> map, int page, int pageSize)throws DaoException;

		public long countByOrganization(Map<String, Object> map)throws DaoException;

		public long getCountBybusIdAndOrg(Long businessId, Long organization)throws DaoException;
		
		MarketToBusiness findByBusinessId(Long businessId)throws DaoException;
}
