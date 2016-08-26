package com.gettec.fsnip.fsn.service.sentiment.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.sentiment.SentimentWebsiteDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.sentiment.SentimentWebsite;
import com.gettec.fsnip.fsn.service.sentiment.SentimentWebsiteService;
import com.gettec.fsnip.fsn.vo.page.PageVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;

@Service("sentimentWebsiteService")
public class SentimentWebServiceImpl implements SentimentWebsiteService {

	@Autowired
	private SentimentWebsiteDAO sentimentWebsiteDAO;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public SentimentWebsite create(SentimentWebsite sentimentWebsite) throws JPAException {
		if(sentimentWebsite != null){
			sentimentWebsite.setOrganizationId(Long.valueOf(AccessUtils.getUserOrg().toString()));
			sentimentWebsite.setOrganizationName(AccessUtils.getUserOrgName().toString());
			sentimentWebsiteDAO.persistent(sentimentWebsite);
			return sentimentWebsite;
		}
		return null;
	}

	@Override
	public List<SentimentWebsite> getWebsitesByOrg(Integer page,
			Integer pageSize, Long organizationId) {
		return sentimentWebsiteDAO.findByOrg(page, pageSize, organizationId);
	}

	@Override
	public boolean checkUnique(Long websiteId, String host) {
		return sentimentWebsiteDAO.checkUnique(websiteId, host);
	}

	@Override
	public Long countByOrg(Long organizationId) {
		return sentimentWebsiteDAO.countByOrg(organizationId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public SentimentWebsite update(SentimentWebsite sentimentWebsite) throws JPAException {
		SentimentWebsite oldWeb = sentimentWebsiteDAO.findById(sentimentWebsite.getId());
		oldWeb.setStatus(1);
		oldWeb.setWebsiteUrl(sentimentWebsite.getWebsiteUrl());
		oldWeb.setWebsiteName(sentimentWebsite.getWebsiteName());
		sentimentWebsiteDAO.merge(oldWeb);
		return oldWeb;
	}

	@Override
	@Transactional
	public Map<String, Object> getPageWebsite(PageVO pageVo,
			SentimentWebsite sentimentWebsite){
		return sentimentWebsiteDAO.getPageWebsite(pageVo, sentimentWebsite);
	}

	@Override
	@Transactional
	public boolean unique(SentimentWebsite sentimentWebsite) {
		return sentimentWebsiteDAO.unique(sentimentWebsite);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateWebsite(SentimentWebsite sentimentWebsite) {
		try {
			sentimentWebsiteDAO.merge(sentimentWebsite);
			return true;
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteWebsite(Long id) {
		SentimentWebsite sentimentWebsite;
		try {
			sentimentWebsite = sentimentWebsiteDAO.findById(id);
			sentimentWebsiteDAO.remove(sentimentWebsite);
			return true;
		} catch (JPAException e1) {
			e1.printStackTrace();
		}
		return false;
	}

}
