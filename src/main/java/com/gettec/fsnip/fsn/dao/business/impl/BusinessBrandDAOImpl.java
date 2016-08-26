package com.gettec.fsnip.fsn.dao.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.business.BusinessBrandDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.transfer.BusinessBrandTransfer;
import com.gettec.fsnip.fsn.transfer.BusinessUnitTransfer;
import com.lhfs.fsn.util.StringUtil;

/**
 * BusinessBrand customized operation implementation
 * 
 * @author Ryan Wang
 */
@Repository(value="businessBrandDAO")
public class BusinessBrandDAOImpl extends BaseDAOImpl<BusinessBrand>
		implements BusinessBrandDAO {
	@PersistenceContext
	private EntityManager entityManager;


	@SuppressWarnings("unchecked")
	@Override
	public BusinessBrand findByName(String name) {
		BusinessBrand businessBrand = null;
		if (StringUtils.isNotBlank(name)) {
			String sql = "select * from business_brand where name = :name ";
			List<BusinessBrand> result = entityManager
					.createNativeQuery(sql, BusinessBrand.class)
					.setParameter("name", name).getResultList();
			if(result != null && result.size() > 0){
				businessBrand = result.get(0);
			}
		}
		return businessBrand;
	}

	/**
	 * 按最值机构Id获取商标总数
	 * @throws DaoException 
	 */
	@Override
	public long countByOrgnizationId(Long organizationId) throws DaoException {
		try {
			String condition = " WHERE e.organization = ?1";
			Object[] params = new Object[]{organizationId};
			return this.count(condition, params);
		} catch (JPAException jpae) {
			throw new DaoException("BusinessBrandDAOImpl.countByOrgnizationId() "+jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 按组织机构id获取所有商标信息
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessBrand> getListByOrganization(Long organization) throws DaoException {
		try {
			String sql = "SELECT b.id,b.name,c.CATEGORY_NAME from business_brand b" +
							" LEFT JOIN brand_category c ON b.brand_category_id=c.ID" +
							" WHERE organization = " + organization;
			Query query =  entityManager.createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			List<BusinessBrand> brands = new ArrayList<BusinessBrand>();
			for(Object[] obj : result){
				BusinessBrand brand = new BusinessBrand(obj[0]==null?null:Long.parseLong(obj[0].toString()),
						obj[1]==null?null:obj[1].toString(), obj[2]==null?"无":obj[2].toString());
				brands.add(brand);
			}
			return brands;
		} catch (Exception e) {
			throw new DaoException("BusinessBrandDAOImpl.getListByOrganization() 按组织机构id获取所有商标信息, 出现异常！", e);
		}
	}

	/**
	 * 根据筛选条件condition来查询品牌条数
	 * @throws DaoException 
	 */
	@Override
	public long countBrandByCondition(Map<String, Object> condition)
			throws DaoException {
		try{
			return this.count(condition.get("condition").toString(), (Object[])condition.get("params"));
		}catch(JPAException jpae){
			throw new DaoException("BusinessBrandDAOImpl.countBrandByCondition "+jpae.getMessage(),jpae.getException());
		}
		
	}

	/**
	 * 根据筛选条件condition分页查询品牌信息
	 * @throws DaoException 
	 */
	@Override
	public List<BusinessBrand> getListOfBrandByConditionWithPage(
			Map<String, Object> condition, int page, int pageSize)
			throws DaoException {
		try{
			return this.getListByPage(page, pageSize, condition.get("condition").toString(), (Object [])condition.get("params"));
		}catch(JPAException jpae){
			throw new DaoException("BusinessBrandDAOImpl.getListOfBrandByConditionWithPage() "+jpae.getMessage(),jpae.getException());
		}
		
	}

	/**
	 * 按企业id查找品牌信息
	 * @throws DaoException
	 */
	@Override
	public List<BusinessBrand> getListByBusunitId(Long busunitId) throws DaoException {
		try {
			String condition = " WHERE e.businessUnit.id = ?1";
			return this.getListByCondition(condition, new Object[]{busunitId});
		} catch (JPAException jpae) {
			throw new DaoException("BusinessBrandDAOImpl.getListByBusunitId() "+jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 获取所有品牌名称
	 * @throws DaoException
	 */
	@Override
	public List<String> getAllBrandName() throws DaoException {
		try {
			String sql = "select DISTINCT name from business_brand";
			return this.getListBySQLWithoutType(String.class, sql, null);
		} catch (JPAException jpae) {
			throw new DaoException("BusinessBrandDAOImpl.getAllBrandName() "+jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 按品牌名称和品牌类型id查找品牌条数
	 * @param brandname
	 * @param categoryId
	 * @throws DaoException
	 */
	@Override
	public long countbyNameAndCategoryId(Long brandId, String brandname, Long categoryId) throws DaoException {
		try {
			String condition = " WHERE e.name = ?1 AND e.brandCategory.id = ?2";
			if(brandId != null) condition += " and e.id != ?3 ";
			return this.count(condition, new Object[]{brandname, categoryId, brandId});
		} catch (Exception e) {
			throw new DaoException("BusinessBrandDAOImpl.countbyNameAndCategoryId() 按品牌名称和品牌类型id查找品牌条数，出现异常！", e);
		}
	}

	/**
	 * 按品牌名称和品牌类型名称查找品牌条数
	 * @param brandname
	 * @param categoryId
	 * @return long
	 * @throws DaoException
	 */
	@Override
	public long countByNameAndCategoryName(String brandname, String categoryName, Long brandId) throws DaoException {
		try {
			String condition = " WHERE e.name = ?1 AND e.brandCategory.name = ?2";
			if(brandId != null){
				condition += " AND e.id <> ?3";
				return this.count(condition, new Object[]{brandname, categoryName, brandId});
			}
			return this.count(condition, new Object[]{brandname, categoryName});
		} catch (Exception e) {
			throw new DaoException("BusinessBrandDAOImpl.countByNameAndCategoryName() 按品牌名称和品牌类型名称查找品牌条数，出现异常！", e);
		}
	}

	/**
	 * 按品牌名称和组织机构id查找品牌条数
	 * @param brandname
	 * @param organization
	 * @return long
	 * @throws DaoException
	 */
	@Override
	public long countByNameAndOrgId(String brandName, Long organization) throws DaoException {
		try {
			String condition = " WHERE e.name = ?1 AND e.organization = ?2";
			return this.count(condition, new Object[]{brandName, organization});
		} catch (Exception e) {
			throw new DaoException("BusinessBrandDAOImpl.countByNameAndOrgId() 按品牌名称和组织机构id查找品牌条数，出现异常！", e);
		}
	}

	/**
	 * 根据品牌名称和类型id查找一条品牌信息
	 * @param brandName
	 * @param categoryId
	 * @return BusinessBrand
	 * @throws DaoException
	 */
	@Override
	public BusinessBrand findByNameAndCategoryId(String brandName,
			Long categoryId) throws DaoException {
		try {
			String condition = " WHERE e.name = ?1 AND e.brandCategory is null";
			List<BusinessBrand> result = this.getListByCondition(condition, new Object[]{brandName});
			if(result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("BusinessBrandDAOImpl.findByNameAndCategoryId() 根据品牌名称和类型id查找一条品牌信息，出现异常！", jpae.getException());
		}
	}

	/**
	 * 根据品牌名称和类型id查找一条品牌信息
	 * @param brandName
	 * @param bussunitId
	 * @return BusinessBrand
	 * @throws DaoException
	 */
	@Override
	public BusinessBrand findByNameAndBussunitId(String brandName, Long bussunitId) throws DaoException {
		try {
			String condition = " WHERE e.name = ?1 AND e.businessUnit.id = ?2";
			List<BusinessBrand> result = this.getListByCondition(condition, new Object[]{brandName, bussunitId});
			if(result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("BusinessBrandDAOImpl.findByNameAndBussunitId() 根据品牌名称和生产企业id查找一条品牌信息，出现异常！", jpae.getException());
		}
	}

	
	@Override
	public BusinessBrand checkBusinessBrand(BusinessBrand businessBrand)
			throws ServiceException {
		Criteria criteria=this.getSession().createCriteria(BusinessBrand.class);
		
		BusinessUnit businessUnit = businessBrand.getBusinessUnit();
		if(null!=businessUnit&&!StringUtil.isBlank(businessUnit.getName())){
			criteria.createAlias("businessUnit", "businessUnit");
			criteria.add(Restrictions.eq("businessUnit.name",businessUnit.getName()));
		}
		if(!StringUtil.isBlank(businessBrand.getName())){
			criteria.add(Restrictions.eq("name",businessBrand.getName()));
		}
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);// ROOT_ENTITY
		if(criteria.list().size()>0){
			return (BusinessBrand) criteria.list().get(0);
		}
		return null;
	}
}
