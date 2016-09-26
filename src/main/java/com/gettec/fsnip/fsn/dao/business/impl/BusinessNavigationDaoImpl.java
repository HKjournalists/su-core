package com.gettec.fsnip.fsn.dao.business.impl;

import java.util.List;

import javax.persistence.Query;

import com.gettec.fsnip.fsn.exception.JPAException;
import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.business.BusinessNavigationDao;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.model.business.BusinessNavigation;

@Repository(value = "businessNavigationDao")
public class BusinessNavigationDaoImpl extends BaseDAOImpl<BusinessNavigation> implements BusinessNavigationDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessNavigation> getNavigationList(Long businessID) {
		try {
			String condition = " WHERE e.businessID = ?1 ORDER BY e.addressId ASC ";
			return this.getListByCondition(condition,new Object[]{businessID});
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getCount(Long currentUserOrganization) {
		String sql = "SELECT bn.id,MAX(bn.addressId) FROM business_navigation bn WHERE organization = ?! ";

		Query query = entityManager.createNativeQuery(sql).setParameter(1, currentUserOrganization);

		Object count = query.getSingleResult();

		return (Integer)count;
	}

}
