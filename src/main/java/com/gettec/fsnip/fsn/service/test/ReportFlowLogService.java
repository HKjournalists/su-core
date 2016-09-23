package com.gettec.fsnip.fsn.service.test;

import com.gettec.fsnip.fsn.dao.test.ReportFlowLogDAO;
import com.gettec.fsnip.fsn.model.test.ReportFlowLog;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface ReportFlowLogService extends BaseService<ReportFlowLog, ReportFlowLogDAO>{

	void save(ReportFlowLog data);

	
	
}