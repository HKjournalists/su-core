package com.gettec.fsnip.fsn.service.test.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.test.ReportFlowLogDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.test.ReportFlowLog;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.test.ReportFlowLogService;

/**
 * ReportFlowLogServiceImpl service implementation
 * 
 * @author LongXianZhen  2015/04/29
 */
@Service(value="reportFlowLogService")
public class ReportFlowLogServiceImpl extends BaseServiceImpl<ReportFlowLog, ReportFlowLogDAO> 
		implements ReportFlowLogService{
	@Autowired protected ReportFlowLogDAO reportFlowLogDAO;
	
	@Override
	public ReportFlowLogDAO getDAO() {
		return reportFlowLogDAO;
	}
	/**
	 * 保存报告日志
	 * @param data 格式为 test_result_id&&&handlers&&&barcode&&&batch_serial_no&&&service_order&&&operation
	 * @author LongXianZhen 2015/04/30
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(ReportFlowLog data) {
		try {
			/*ReportFlowLog tl= new ReportFlowLog();
			tl.setTestResultId(data.getTestResultId());
			tl.setHandlers(data.getHandlers());
			tl.setBarcode(data.getBarcode());
			tl.setBatchSerialNo(data.getBatchSerialNo());
			tl.setServiceOrder(data.getServiceOrder());
			tl.setOperation(data.getOperation());*/
			data.setOperationTime(new Date());
			reportFlowLogDAO.persistent(data);
		} catch (JPAException e) {
			((Throwable) e.getException()).printStackTrace();
		}
	}

}