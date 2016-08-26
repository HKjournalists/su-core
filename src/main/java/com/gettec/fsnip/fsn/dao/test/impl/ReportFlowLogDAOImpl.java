package com.gettec.fsnip.fsn.dao.test.impl;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.test.ReportFlowLogDAO;
import com.gettec.fsnip.fsn.model.test.ReportFlowLog;

/**
 * ReportFlowLogDAOImpl customized operation implementation
 * 
 * @author LongXianZhen 2015/04/29
 */
@Repository(value="reportFlowLogDAO")
public class ReportFlowLogDAOImpl extends BaseDAOImpl<ReportFlowLog>
		implements ReportFlowLogDAO {
		
	
}