package com.gettec.fsnip.fsn.service.receive.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.receive.ReceiveTestPropertyDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.receive.ReceiveTestProperty;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.receive.ReceiveTestPropertyService;

/**
 * 
 * @author ZhangHui 2015/4/24
 */
@Service(value="receiveTestPropertyService")
public class ReceiveTestPropertyServiceImpl 
			extends BaseServiceImpl<ReceiveTestProperty, ReceiveTestPropertyDAO>
			implements ReceiveTestPropertyService{
	@Autowired private ReceiveTestPropertyDAO receiveTestPropertyDAO;

	@Override
	public ReceiveTestPropertyDAO getDAO() {
		return receiveTestPropertyDAO;
	}

	/**
	 * 保存检测项目和结论
	 * @author ZhangHui 2015/4/24
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void create(List<ReceiveTestProperty> recTestProperties, Long rec_test_result_id) throws ServiceException {
		if(recTestProperties==null || recTestProperties.size()<1 || rec_test_result_id==null){
			return;
		}
		try {
			for(ReceiveTestProperty recTestPropertie : recTestProperties){
				recTestPropertie.setId(null);
				recTestPropertie.setRec_test_result_id(rec_test_result_id);
				create(recTestPropertie);
			}
		} catch (ServiceException e) {
			throw new ServiceException("ReceiveTestPropertyServiceImpl.create()-->" + e.getMessage(), e.getException());
		}
	}

}
