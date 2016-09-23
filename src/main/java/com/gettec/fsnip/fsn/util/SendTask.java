package com.gettec.fsnip.fsn.util;

import java.util.concurrent.atomic.AtomicBoolean;

import com.gettec.fsnip.fsn.model.business.BusinessUnitInfoLog;
import com.gettec.fsnip.fsn.model.product.ProductLog;
import com.gettec.fsnip.fsn.model.test.ReportFlowLog;
import com.gettec.fsnip.fsn.service.business.BusinessUnitInfoLogService;
import com.gettec.fsnip.fsn.service.product.ProductLogService;
import com.gettec.fsnip.fsn.service.statistics.ProductVisitStatisticsService;
import com.gettec.fsnip.fsn.service.test.LimsNotFindProductService;
import com.gettec.fsnip.fsn.service.test.ReportFlowLogService;

/**
 * 发送任务
 * 
 * @author licw
 * 
 */

class SendTask implements Runnable {
	
	public SendTask() {
		super();
	}

	private DoubleQueue<Object> queue;
	// 队列切换的时间间隔
	private static final int changedTime = 1000;
	private AtomicBoolean isclosing = new AtomicBoolean(false);
	private ProductVisitStatisticsService productVisitStatisticsService;
	private LimsNotFindProductService limsNotFindProductService;
	private ReportFlowLogService reportFlowLogService;
	private ProductLogService productLogService;
	private BusinessUnitInfoLogService businessUnitInfoLogService;

	public SendTask(
			DoubleQueue<Object> queue ) {
		this.queue = queue;
		this.productVisitStatisticsService=(ProductVisitStatisticsService)SpringUtil.getObject("productVisitStatisticsService");
		this.limsNotFindProductService=(LimsNotFindProductService)SpringUtil.getObject("limsNotFindProductService");
		this.reportFlowLogService=(ReportFlowLogService)SpringUtil.getObject("reportFlowLogService");
		this.productLogService=(ProductLogService)SpringUtil.getObject("productLogService");
		this.businessUnitInfoLogService=(BusinessUnitInfoLogService)SpringUtil.getObject("businessUnitInfoLogService");
	}

	public void run() {
		try {
			doTask();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doTask() throws InterruptedException {

		while (true) {
			Thread.sleep(changedTime);
			if (!isclosing.get()) {
				doSend();
				queue.changeFlag();
			}
			if (isclosing.get()) {
				//break;
			}
		}
	}

	public void close() {
		
	}

	private void doSend() {

		Object data = null;
		
			while (queue.getPollCount() > 0) {
				data = queue.poll();
				if(data!=null&&data instanceof String){
					String[] d=data.toString().split("_");
					if(d.length==2){
						productVisitStatisticsService.statisticalVsits(data.toString());
					}else{
						limsNotFindProductService.save(data.toString());
					}
				}else if(data!=null&&data instanceof ReportFlowLog){
					reportFlowLogService.save((ReportFlowLog)data);
				}else if(data!=null&&data instanceof ProductLog){
					productLogService.saveProductLog((ProductLog)data);
				}else if(data!=null&&data instanceof BusinessUnitInfoLog){
					businessUnitInfoLogService.saveBusinessUnitInfoLog((BusinessUnitInfoLog) data);
				}
		}
	}


}
