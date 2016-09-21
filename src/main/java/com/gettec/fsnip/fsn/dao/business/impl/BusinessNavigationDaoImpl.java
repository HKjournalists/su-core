package com.gettec.fsnip.fsn.dao.business.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.business.BusinessNavigationDao;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.model.business.BusinessNavigation;

@Repository(value = "businessNavigationDao")
public class BusinessNavigationDaoImpl extends BaseDAOImpl<BusinessNavigation> implements BusinessNavigationDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessNavigation> getNavigationList(Long businessID) {
		
		String sql = "select bn.* from business_navigation bn where business_id = ?1";
		
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, businessID);
		List<Object[]> objList = query.getResultList();
		List<BusinessNavigation> navigationList = new ArrayList<BusinessNavigation>();
		for (Object[] obj : objList) {
			BusinessNavigation navigation = new BusinessNavigation();
			navigation.setId(obj[0] == null?null:Long.parseLong(obj[0].toString()));
			navigation.setBigOption(obj[1] == null?null:obj[1].toString());
			navigation.setSmallOption(obj[2] == null?null:obj[2].toString());
			navigation.setNavigationURL(obj[3] == null?null:obj[3].toString());
			navigation.setBusinessID(obj[4] == null?null:Long.parseLong(obj[4].toString()));
			navigation.setOrganization(obj[5] == null?null:Long.parseLong(obj[5].toString()));
			navigationList.add(navigation);
		}
		
		return navigationList;
	}

}
