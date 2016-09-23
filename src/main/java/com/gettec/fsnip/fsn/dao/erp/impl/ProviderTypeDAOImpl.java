package com.gettec.fsnip.fsn.dao.erp.impl;


import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.ProviderTypeDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;

@Repository("providerTypeDAO")
public class ProviderTypeDAOImpl extends BaseDAOImpl<CustomerAndProviderType> implements
		ProviderTypeDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerAndProviderType> getProviderTypefilter_(int from, int size,
			String configure) {
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
	public long countProviderType(String configure) {
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
			String condition = " WHERE e.name = ?1 and e.organization = ?2 and e.type = 2";
			List<CustomerAndProviderType> result = (List<CustomerAndProviderType>) this.getListByCondition(condition, new Object[]{name, organization});
			if(result.size() > 0) {
				return (CustomerAndProviderType) result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			return null;
		}
	}
}
