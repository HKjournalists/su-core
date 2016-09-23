package com.gettec.fsnip.fsn.dao.business.impl;

import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.business.OrgInstitutionDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.model.business.OrganizingInstitution;

/**
 * OrgInstitutionDAO dao implementation
 * @author Hui Zhang
 */
@Repository(value="orgInstitutionDAO")
public class OrgInstitutionDAOImpl extends BaseDAOImpl<OrganizingInstitution> implements OrgInstitutionDAO {
	
}