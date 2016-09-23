package com.gettec.fsnip.fsn.dao.business.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.business.BrandCategoryDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.BrandCategory;
import com.gettec.fsnip.fsn.model.business.BrandCategoryDetail;

/**
 * BrandCategory dao implementation
 * @author Hui Zhang
 */
@Repository(value="brandcategoryDAO")
public class BrandCategoryDAOImpl extends BaseDAOImpl<BrandCategory> 
		implements BrandCategoryDAO{
	@PersistenceContext private EntityManager entityManager;
	
	/**
	 * 根据level&keyWords获取BrandCategoryDetail集合(用于前台树形展示)
	 * @param level
	 * @param keyword
	 * @param orgs
	 * @return List<BrandCategoryDetail>
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BrandCategoryDetail> getTreeNodes(int level, String keyword) throws DaoException {
		try {
			List<BrandCategoryDetail> details = new ArrayList<BrandCategoryDetail>();
			if (level <= 1) {
				level = 1;
				String jpql = " select distinct -1 "
						+ " as id, substring_index(p.CATEGORY_NAME, '.', 1) as name,	 "
						+ " sum(case when length(p.CATEGORY_NAME) = length(substring_index(p.CATEGORY_NAME, '.', 1))"
						+ "	then 0 else 1 end)		as childrenNum, "
						+ " case when length(p.CATEGORY_NAME) = length(substring_index(p.CATEGORY_NAME, '.', 1)) "
						+ " then p.id else -1 end as productId "
						+ " from brand_category p "
						+ " group by substring_index(p.CATEGORY_NAME, '.', 1) "
						+ " order by p.CATEGORY_NAME";
				Query query =  entityManager.createNativeQuery(jpql);
				List<Object[]> result = query.getResultList();
				for(Object[] obj : result){
					BrandCategoryDetail brandDetail = new BrandCategoryDetail(
							Long.parseLong(obj[0].toString()),obj[1].toString(),
							Long.parseLong(obj[2].toString()),Long.parseLong(obj[3].toString()));
					details.add(brandDetail);
				}
			} else {
				String jpql = " select "
						+ " -1   as id, substring_index(p.CATEGORY_NAME, '.', :level) as name, "
						+ " sum(case when length(p.CATEGORY_NAME) = length(substring_index(p.CATEGORY_NAME, '.', :level)) "
						+ " then 0 else 1 end)			 as childrenNum, "
						+ " case when length(p.CATEGORY_NAME) = length(substring_index(p.CATEGORY_NAME, '.', :level))"
						+ " then p.id else -1 end as productId  from brand_category p "
						+ " where p.CATEGORY_NAME like concat(:keyword,'%') "
						+ " and p.CATEGORY_NAME<>:keyword "
						+ " group by substring_index(p.CATEGORY_NAME, '.', :level) "
						+ " order by p.CATEGORY_NAME ";
				Query query =  entityManager.createNativeQuery(jpql);
				query.setParameter("keyword", keyword);
				query.setParameter("level", level);
				List<Object[]> result = query.getResultList();
				for(Object[] obj : result){
					BrandCategoryDetail brandDetail = new BrandCategoryDetail(
							Long.parseLong(obj[0].toString()),obj[1].toString(),
							Long.parseLong(obj[2].toString()),Long.parseLong(obj[3].toString()));
					details.add(brandDetail);
				}
			}
			return details;
		} catch (Exception e) {
			throw new DaoException("BrandCategoryDAOImpl.getTreeNodes() 出现异常!", e);
		}
	}
}
