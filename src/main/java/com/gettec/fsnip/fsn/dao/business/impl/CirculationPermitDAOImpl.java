package com.gettec.fsnip.fsn.dao.business.impl;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.business.CirculationPermitDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.model.business.CirculationPermitInfo;

/**
 * CirculationPermitDAOImpl dao implementation
 * @author Hui Zhang
 */
@Repository(value="circulationPermitDAO")
public class CirculationPermitDAOImpl extends BaseDAOImpl<CirculationPermitInfo> implements CirculationPermitDAO {
	
}