package com.gettec.fsnip.fsn.dao.sentiment;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.sentiment.OrganizationTopic;

public interface OrgToTopicDAO extends BaseDAO<OrganizationTopic>{

	List<OrganizationTopic> findByOrg(Long organizationId);

}
