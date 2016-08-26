package com.gettec.fsnip.fsn.service.receive;

import java.util.List;

import com.gettec.fsnip.fsn.dao.receive.ReceiveSpecimenDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.receive.ReceiveSpecimendata;
import com.gettec.fsnip.fsn.service.common.BaseService;

/**
 * 
 * @author ZhangHui 2015/4/24
 */
public interface ReceiveSpecimenService 
		extends BaseService<ReceiveSpecimendata, ReceiveSpecimenDAO>{
	
	/**
	 * 保存检测数据
	 * @author ZhangHui 2015/4/24
	 */
	public void create(List<ReceiveSpecimendata> recSpecimens, Long rec_test_result_id) throws ServiceException;
	
}
