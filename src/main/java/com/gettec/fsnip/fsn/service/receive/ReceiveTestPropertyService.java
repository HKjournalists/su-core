package com.gettec.fsnip.fsn.service.receive;

import java.util.List;

import com.gettec.fsnip.fsn.dao.receive.ReceiveTestPropertyDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.receive.ReceiveTestProperty;
import com.gettec.fsnip.fsn.service.common.BaseService;

/**
 * 
 * @author ZhangHui 2015/4/24
 */
public interface ReceiveTestPropertyService 
		extends BaseService<ReceiveTestProperty, ReceiveTestPropertyDAO>{

	/**
	 * 保存检测项目和结论
	 * @author ZhangHui 2015/4/24
	 */
	public void create(List<ReceiveTestProperty> recTestProperties, Long rec_test_result_id) throws ServiceException;
	
}
