package com.gettec.fsnip.fsn.dao.product.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductNutriDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.product.ProductNutrition;

@Repository(value="productNutriDAO")
public class ProductNutriDAOImpl  extends BaseDAOImpl<ProductNutrition>
		implements ProductNutriDAO {

    /**
     * 按营养报告的不同列名查找不同列的集合信息
     * @throws DaoException
     */
    @Override
    public List<String> getListOfColumeValue(int colNameId) throws DaoException {
        try {
            String sql = null;
            final int ITEM_NAME = 1,ITEM_VALUE = 2,  ITEM_UNIT = 3, ITEM_PER = 4, ITEM_NRV = 5;
            switch (colNameId) {
            case ITEM_NAME:
                sql = "SELECT DISTINCT name FROM nutri_rpt WHERE name IS NOT NULL";
                break;
            case ITEM_VALUE:
                sql = "SELECT DISTINCT value FROM nutri_rpt WHERE value IS NOT NULL";
                break;
            case ITEM_UNIT:
                sql = "SELECT DISTINCT unit FROM nutri_rpt WHERE unit IS NOT NULL";
                break;
            case ITEM_PER:
                sql = "SELECT DISTINCT per FROM nutri_rpt WHERE per IS NOT NULL";
                break;
            case ITEM_NRV:
                sql = "SELECT DISTINCT nrv FROM nutri_rpt WHERE nrv IS NOT NULL";
                break;
            }
            if (sql == null) {
                return null;
            }
            return this.getListBySQLWithoutType(String.class, sql, null);
        } catch (Exception e) {
            throw new DaoException("【DAO-error】按营养报告的不同列名查找不同列的集合信息，出现异常", e);
        }
    }

    /**
	 * 按产品id查找营养报告列表
	 * @throws DaoException 
	 */
	@Override
	public List<ProductNutrition> getListOfNutrisByProductId(Long productId) throws DaoException {
		try {
			String condition = " WHERE e.productId = ?1";
			Object[] params = new Object[]{productId};
			return this.getListByCondition(condition, params);
		} catch (JPAException jpae) {
			throw new DaoException("【dao-error】按产品id查找营养报告列表，出现异常！", jpae.getException());
		}
	}
	
	/**
	 * 按产品id查找营养报告列表的分页信息
	 */
	  @Override
	    public List<ProductNutrition> getListOfProductNutritionByProductIdWithPage(
	           Long productId, int page, int pageSize)
	            throws DaoException {
	        try {
	            return this.getListOfProductNutritionByPage( productId, page, pageSize);
	        } catch (JPAException jpae) {
	            throw new DaoException("【dao-error】按当前登录子企业的组织机构id查找分页产品列表，出现异常！", jpae.getException());
	        }
	    }
	    
	  /**
	   *  按产品id查找营养报告列表的分页信息 分页的具体方法
	   * @param productId 
	   * @param page
	   * @param pageSize
	   * @return 
	   * @throws JPAException
	   */
	    @SuppressWarnings("unchecked")
	    public List<ProductNutrition> getListOfProductNutritionByPage(Long productId, int page, int pageSize) throws JPAException {
	        try {

	            String jpql = "SELECT e FROM " + entityClass.getName() + " e " + "where e.productId = ?1";
	            Query query = entityManager.createQuery(jpql);
	            query.setParameter(1, productId);
	            // 判断是否分页查询
	            if (page > 0) {
	                query.setFirstResult((page - 1) * pageSize);
	                query.setMaxResults(pageSize);
	            }
	            return query.getResultList();
	        } catch (Exception e) {
	            throw new JPAException("", e);
	        }

	    }
	    
	    /**
	     * 根据productId 得到对应的营养报告总条数
	     * @throws DaoException 
	     */
        @Override
        public long countByproductId(Long productId) throws DaoException {
            try {
                String condition = " WHERE e.productId = ?1";
                Object[] params = new Object[]{productId};
                return this.count(condition, params);
            } catch (JPAException jpae) {
                throw new DaoException("【dao-error】根据productId 得到对应的营养报告总条数，出现异常！", jpae.getException());
            }
        }
}
