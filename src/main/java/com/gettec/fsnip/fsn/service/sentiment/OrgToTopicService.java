package com.gettec.fsnip.fsn.service.sentiment;

import java.util.List;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sentiment.OrganizationTopic;

public interface OrgToTopicService {

	OrganizationTopic add(OrganizationTopic organizationTopic) throws ServiceException;

	List<OrganizationTopic> findAll();
	
	List<OrganizationTopic> findByOrg(Long organizationId);
}
