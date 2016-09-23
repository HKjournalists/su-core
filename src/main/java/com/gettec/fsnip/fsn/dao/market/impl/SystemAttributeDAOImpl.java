package com.gettec.fsnip.fsn.dao.market.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.market.SystemAttributeDAO;
import com.gettec.fsnip.fsn.model.sys.SystemAttribute;

@Repository(value="attributeDAO")
public class SystemAttributeDAOImpl extends BaseDAOImpl<SystemAttribute> implements SystemAttributeDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<SystemAttribute> getSortedAttrs() {
		String jpql = "SELECT e FROM " + entityClass.getName() + " e ORDER BY e.attributeName";
		Query query = entityManager.createQuery(jpql);
		
		return query.getResultList();
	}
	
}
