package com.gettec.fsnip.fsn.service.trace;

import com.gettec.fsnip.fsn.dao.trace.TraceDataProductNameDao;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.gettec.fsnip.fsn.model.trace.TraceDataProductName;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface TraceDataProductNameService extends BaseService<TraceDataProductName, TraceDataProductNameDao> {
	TraceDataProductName findByProductName(String productName);
	TraceData isKeywordTraceData(String keyword);
	TraceDataProductName getBarcodeByProductName(String productName);
}
