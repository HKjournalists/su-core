package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.CustomerTypeDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;

@Repository("customerTypeDAO")
public class CustomerTypeDAOImpl extends BaseDAOImpl<CustomerAndProviderType> 
		implements CustomerTypeDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerAndProviderType> getCustomerTypefilter_(int from, int size,String configure) {
		String jpql="select e from t_meta_customer_and_provider_type e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
//		query.setFirstResult(from);  
//		query.setMaxResults(size); 
//		System.out.println(query);
		List<CustomerAndProviderType> result = query.getResultList();
		return result;
	}

	@Override
	public long countCtype(String configure) {
		String jpql = "SELECT count(*) FROM t_meta_customer_and_provider_type e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
		Object rtn = query.getSingleResult();
    	return rtn == null ? 0 : Long.parseLong(rtn.toString());
	}

	public CustomerAndProviderType findByUniqueNameAndOrganization(String name, Long organization) {
		try {
			String condition = " WHERE e.name = ?1 and e.organization = ?2 and e.type = 1";
			List<CustomerAndProviderType> result = (List<CustomerAndProviderType>) this.getListByCondition(condition, new Object[]{name, organization});
			if(result.size() > 0) {
				return (CustomerAndProviderType) result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			return null;
		}
	}
	
	/**
	 * 根据客户id和企业组织机构查询指定类型的一条客户类型实体
	 * @author Tangxin 2015-05-27
	 */
	@Override
	public CustomerAndProviderType findByCustomerAndOrganization(Long organization, Integer type, String name) throws DaoException{
		try {
			String condition = " WHERE e.organization = ?1 and e.type = ?2 and e.name = ?3";
			List<CustomerAndProviderType> result = (List<CustomerAndProviderType>) this.getListByCondition(condition, new Object[]{organization, type, name});
			if(result.size() > 0) {
				return (CustomerAndProviderType) result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae);
		}
	}
}
