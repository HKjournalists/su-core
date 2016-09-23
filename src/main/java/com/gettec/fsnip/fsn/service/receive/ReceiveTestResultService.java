package com.gettec.fsnip.fsn.service.receive;

import com.gettec.fsnip.fsn.dao.receive.ReceiveTestResultDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.receive.ReceiveTestResult;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.receive.ResultVO;

/**
 * 
 * @author ZhangHui 2015/4/24
 */
public interface ReceiveTestResultService 
		extends BaseService<ReceiveTestResult, ReceiveTestResultDAO>{

	/**
	 * 保存样品检测数据
	 * @author ZhangHui 2015/4/24
	 */
	public void save(ReceiveTestResult recResult, ResultVO resultVO) throws ServiceException;
	
}
