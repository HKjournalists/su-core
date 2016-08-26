package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.MerchandiseCategoryDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.product.ProductCategory;

@Repository("merchandiseCategoryDAO")
public class MerchandiseCategoryDAOImpl extends BaseDAOImpl<ProductCategory> 
		implements MerchandiseCategoryDAO {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<ProductCategory> getMerchandiseCategoryfilter_(int from,int size,String configure) {
		String jpql="select e from T_META_MERCHANDISE_CATEGORY e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
		List<ProductCategory> result = query.getResultList();
		return result;
	}
	
	public long countMerchandiseCategory(String configure) {
		String jpql = "SELECT count(*) FROM T_META_MERCHANDISE_CATEGORY e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
		Object rtn = query.getSingleResult();
    	return rtn == null ? 0 : Long.parseLong(rtn.toString());
	}

	/**
	 * @throws DaoException 
	 * 根据名称查找一条产品类型
	 * @param name
	 * @return ProductCategory
	 * @throws DaoException
	 */
	@Override
	public ProductCategory findByName(String name) throws DaoException {
		try {
			String condition = " WHERE e.name = ?1 ";
			List<ProductCategory> result = this.getListByCondition(condition, new Object[]{name});
			if(result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】根据名称查找一条产品类型，出现异常", jpae.getException());
		}
	}
}
