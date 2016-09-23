package com.gettec.fsnip.fsn.dao.statistics.impl;


import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.statistics.ProductVisitStatisticsDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.statistics.ProductVisitStatistics;
import com.gettec.fsnip.fsn.transfer.BusinessUnitTransfer;
import com.gettec.fsnip.fsn.transfer.ProductTransfer;

/**
 * ProductPoll customized operation implementation
 * 
 * @author
 */
@Repository(value="productVisitStatisticsDAO")
public class ProductVisitStatisticsDAOImpl extends BaseDAOImpl<ProductVisitStatistics>
		implements ProductVisitStatisticsDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public ProductVisitStatistics findByProductId(Long productId)
			throws DaoException {
		ProductVisitStatistics proViSta=null;
		try {
			String jpql="SELECT * FROM product_visit_statistics WHERE product_id=?1";
			List<ProductVisitStatistics> result = entityManager.createNativeQuery(jpql,ProductVisitStatistics.class).setParameter(1, productId)
					.getResultList();
			if(result != null && result.size() > 0){
				proViSta = result.get(0);
			}
		} catch (Exception e) {
			throw new DaoException("dao层获取所有注册企业集合出错", e);
		}
		return proViSta;
	}

	@Override
	public List<ProductVisitStatistics> getAllProViStaListByPage(int page,
			int pageSize, Map<String, Object> map) throws DaoException {
		try {
			 String condition = (String) map.get("condition");
	         Object[] params = (Object[]) map.get("params");
	        // condition += " order by e.id asc";
	         List<ProductVisitStatistics> ProductVisitStatisticsList= this.getListByPage(page, pageSize, condition, params);
	         for(ProductVisitStatistics p:ProductVisitStatisticsList){
	        	 ProductTransfer.transfer(p.getProduct());
	         }
	         return ProductVisitStatisticsList;
		} catch (Exception e) {
			throw new DaoException("dao层获取所有注册企业集合出错", e);
		}
	}

	@Override
	public Long getCountByproId(Map<String, Object> map)
			throws DaoException {
		try {
			String condition = (String) map.get("condition");
            Object[] params = (Object[]) map.get("params");
            return this.count(condition, params);
		} catch (Exception e) {
			throw new DaoException("dao层查询所有注册企业个数出错", e);
		}
	}

	
	
	
}