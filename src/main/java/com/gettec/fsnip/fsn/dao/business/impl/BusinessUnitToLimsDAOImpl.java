package com.gettec.fsnip.fsn.dao.business.impl;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.business.BusinessUnitToLimsDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.model.business.BusinessUnitToLims;

@Repository(value="businessUnitToLimsDao")
public class BusinessUnitToLimsDAOImpl extends BaseDAOImpl<BusinessUnitToLims> implements BusinessUnitToLimsDAO{

}
