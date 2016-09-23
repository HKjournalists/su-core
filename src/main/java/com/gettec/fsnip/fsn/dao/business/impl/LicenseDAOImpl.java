package com.gettec.fsnip.fsn.dao.business.impl;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.business.LicenseDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;

/**
 * LicenseDAO dao implementation
 * @author Hui Zhang
 */
@Repository(value="licenseDAO")
public class LicenseDAOImpl extends BaseDAOImpl<LicenseInfo> implements LicenseDAO {
	
}