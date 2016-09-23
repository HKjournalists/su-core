package com.gettec.fsnip.fsn.dao.sentiment.impl;


import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.sentiment.SentimentWebsiteDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.sentiment.SentimentWebsite;
import com.gettec.fsnip.fsn.vo.page.PageVO;
import com.lhfs.fsn.util.StringUtil;

@Repository("sentimentWebsiteDAO")
public class SentimentWebsiteDAOImpl extends BaseDAOImpl<SentimentWebsite> implements SentimentWebsiteDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<SentimentWebsite> findByOrg(Integer page, Integer pageSize,
			Long organizationId) {
		String sql = "SELECT e FROM " + entityClass.getName() + " e WHERE e.organizationId =:organizationId";
		Query query = entityManager.createQuery(sql, SentimentWebsite.class);
		query.setParameter("organizationId", organizationId);
		query.setFirstResult((page -1) * pageSize);
		query.setMaxResults(pageSize);
		
		
		return query.getResultList();
	}

	@Override
	public boolean checkUnique(Long websiteId, String host) {
		String sql = null;
		if(websiteId != null){
			 sql = "SELECT count(*) FROM t_sentimen_website WHERE t_sentimen_website.website_url='" + host +"' AND t_sentimen_website.id <>" + websiteId;
		}else{
			 sql = "SELECT count(*) FROM t_sentimen_website WHERE t_sentimen_website.website_url='" + host +"'";
		}
		Query query = this.entityManager.createNativeQuery(sql);
		Number result=(Number)query.getSingleResult();
		if(result.intValue() == 0){
			return true;
		}else{
			return false;
		}
		
	}

	@Override
	public Long countByOrg(Long organizationId) {
		String sql = "SELECT count(*) FROM t_sentimen_website WHERE t_sentimen_website.organizationId=:organizationId";
		Query query = this.entityManager.createNativeQuery(sql);
		query.setParameter("organizationId", organizationId);
		Number result=(Number)query.getSingleResult();
		
		return result.longValue();
	}

	@Override
	public Map<String, Object> getPageWebsite(PageVO pageVo,
			SentimentWebsite sentimentWebsite){
		Criteria criteria=getSession().createCriteria(SentimentWebsite.class);
		if(StringUtil.isBlank(pageVo.getSort())){
			criteria.addOrder(Order.desc("id"));
		}
		if(!StringUtil.isBlank(sentimentWebsite.getOrganizationId())){
			criteria.add(Restrictions.eq("organizationId",sentimentWebsite.getOrganizationId()));
		}
		Map<String,Object> map = this.findQueryPage(pageVo, criteria);
		return map;
	}

	@Override
	public boolean unique(SentimentWebsite sentimentWebsite){
		Criteria criteria=this.getSession().createCriteria(SentimentWebsite.class);
		if(!StringUtil.isBlank(sentimentWebsite.getWebsiteName())){
			criteria.add(Restrictions.eq("websiteName",sentimentWebsite.getWebsiteName()));
		}
		if(!StringUtil.isBlank(sentimentWebsite.getWebsiteUrl())){
			criteria.add(Restrictions.eq("websiteUrl",sentimentWebsite.getWebsiteUrl()).ignoreCase());
		}
		
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);// ROOT_ENTITY
		return criteria.list().size()>0;
	}
	
}
