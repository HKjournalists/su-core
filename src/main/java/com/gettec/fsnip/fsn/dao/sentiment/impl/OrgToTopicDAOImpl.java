package com.gettec.fsnip.fsn.dao.sentiment.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.sentiment.OrgToTopicDAO;
import com.gettec.fsnip.fsn.model.sentiment.OrganizationTopic;

@Repository("orgToTopicDAO")
public class OrgToTopicDAOImpl  extends BaseDAOImpl<OrganizationTopic> implements OrgToTopicDAO{

	@Override
	public List<OrganizationTopic> findByOrg(Long organizationId) {
		String jpql = "SELECT * from t_sentiment_org_to_topic e WHERE e.organizationId=:organizationId";
		Query query = this.entityManager.createNativeQuery(jpql, OrganizationTopic.class);
		query.setParameter("organizationId", organizationId);
		@SuppressWarnings("unchecked")
		List<OrganizationTopic> orgTopics = query.getResultList();
		return orgTopics;
	}

}
