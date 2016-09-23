package com.gettec.fsnip.fsn.dao.statistics;



import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;

import com.gettec.fsnip.fsn.model.statistics.ProductVisitStatistics;
/**
 * ProductPoll DAO
 * 
 * @author 
 */
 public interface ProductVisitStatisticsDAO extends BaseDAO<ProductVisitStatistics>{

	 ProductVisitStatistics findByProductId(Long productId)throws DaoException;

	List<ProductVisitStatistics> getAllProViStaListByPage(int page,
			int pageSize, Map<String, Object> configure)throws DaoException;

	Long getCountByproId(Map<String, Object> configure)throws DaoException;

	
}