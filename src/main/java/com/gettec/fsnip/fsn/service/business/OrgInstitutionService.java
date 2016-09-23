package com.gettec.fsnip.fsn.service.business;

import com.gettec.fsnip.fsn.dao.business.OrgInstitutionDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.OrganizingInstitution;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface OrgInstitutionService extends BaseService<OrganizingInstitution, OrgInstitutionDAO>{

	OrganizingInstitution save(OrganizingInstitution organizingInstitution, boolean isUpdate) throws ServiceException;

	OrganizingInstitution findByOrgCode(String organizationNo) throws ServiceException;

	OrganizingInstitution save(String orgcode) throws ServiceException;
	
}