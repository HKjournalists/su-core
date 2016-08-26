package com.gettec.fsnip.fsn.dao.trace;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.gettec.fsnip.fsn.model.trace.TraceDataProductName;

public interface TraceDataProductNameDao extends BaseDAO<TraceDataProductName> {
	TraceDataProductName findByProductName(String productName);
	
}
