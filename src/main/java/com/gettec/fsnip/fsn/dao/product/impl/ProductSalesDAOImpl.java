package com.gettec.fsnip.fsn.dao.product.impl;


import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductSalesDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.Area;
import com.gettec.fsnip.fsn.model.product.ProductSales;

/**
 * ProductPoll customized operation implementation
 * 
 * @author
 */
@Repository(value="productSalesDAO")
public class ProductSalesDAOImpl extends BaseDAOImpl<ProductSales>
		implements ProductSalesDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<ProductSales> getListByProIdPage(int page, int pageSize,
			Map<String, Object> map) throws DaoException {
		try {
			 String condition = (String) map.get("condition");
	         Object[] params = (Object[]) map.get("params");
	         condition += " order by e.salesDate desc";
	         return this.getListByPage(page, pageSize, condition, params);
		} catch (Exception e) {
			throw new DaoException("dao层获取所有注册企业集合出错", e);
		}
	}

	@Override
	public Long getCountByproId(Map<String, Object> map) throws DaoException {
		try {
			String condition = (String) map.get("condition");
            Object[] params = (Object[]) map.get("params");
            return this.count(condition, params);
		} catch (Exception e) {
			throw new DaoException("dao层查询所有注册企业个数出错", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> getAreaByLevel(int level) throws DaoException {
		List<Area> srea=null;
		try {
			String jpql="SELECT * FROM t_buss_area WHERE level=?1";
			srea = entityManager.createNativeQuery(jpql,Area.class).setParameter(1, level)
					.getResultList();
		} catch (Exception e) {
			throw new DaoException("dao层获取所有注册企业集合出错", e);
		}
		return srea;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> getMunicipalityByparentId(int parentId)
			throws DaoException {
		List<Area> srea=null;
		try {
			String jpql="SELECT * FROM t_buss_area WHERE parent_id=?1";
			srea = entityManager.createNativeQuery(jpql,Area.class).setParameter(1, parentId)
					.getResultList();
		} catch (Exception e) {
			throw new DaoException("dao层获取所有注册企业集合出错", e);
		}
		return srea;
	}	
	
	
}