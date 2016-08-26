package com.gettec.fsnip.fsn.service.sentiment.impl;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fsnip.SentimentInvokeUtils;
import com.gettec.fsnip.fsn.dao.sentiment.OrgToTopicDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sentiment.OrganizationTopic;
import com.gettec.fsnip.fsn.service.sentiment.OrgToTopicService;
import com.gettec.fsnip.sso.client.util.AccessUtils;

@Service(value="orgToTopicService")
public class OrgToTopicServiceImpl implements OrgToTopicService{

	@Autowired
	OrgToTopicDAO orgToTopicDAO;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public OrganizationTopic add(OrganizationTopic organizationTopic) throws ServiceException {
		String result = SentimentInvokeUtils.addTopic(organizationTopic.getTopicName(), "2", organizationTopic.getKeyword(), organizationTopic.getSearchWords());
		if(result != null && !"".equals(result)){
			JSONObject topic = null;
			try{
				topic = JSONObject.fromObject(result);
			}catch (Exception e) {
				throw new ServiceException("该主题名称已经被使用",e);
			}
			if("success".equals(topic.getString("result"))){
				String topicId = topic.getString("subject_id");
				organizationTopic.setOrganizationName(AccessUtils.getUserOrgName().toString());
				organizationTopic.setOrganizationId(Long.valueOf(AccessUtils.getUserOrg().toString()));
				organizationTopic.setTopicId(topicId);
				organizationTopic.setTopicName(organizationTopic.getTopicName());
				try {
					orgToTopicDAO.persistent(organizationTopic);
				} catch (JPAException e) {
					SentimentInvokeUtils.deleteTopic(topicId);
					throw new ServiceException(e.getMessage(), e);
				}
				return organizationTopic;
			}
			return null;
		}else{
			return null;
		}
		
	}

	@Override
	public List<OrganizationTopic> findAll() {
		
		try {
			return orgToTopicDAO.findAll();
		} catch (JPAException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<OrganizationTopic> findByOrg(Long organizationId) {
		return orgToTopicDAO.findByOrg(organizationId);
	}

}
