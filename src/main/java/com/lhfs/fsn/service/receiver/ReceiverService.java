package com.lhfs.fsn.service.receiver;

import com.lhfs.fsn.vo.ResultVO;
import com.lhfs.fsn.vo.TestResultsVO;


public interface ReceiverService {
	/**
	 * 接收LIMS传过来的报告
	 * @param testResultVO
	 * @return ResultVO
	 * @author LongXianZhen
	 * @param status 
	 * @param path 
	 */
	ResultVO receiverReportFromLims(TestResultsVO testResultVO,String json);


	
}