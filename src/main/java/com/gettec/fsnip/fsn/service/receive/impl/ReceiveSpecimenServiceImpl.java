package com.gettec.fsnip.fsn.service.receive.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.receive.ReceiveSpecimenDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.receive.ReceiveSpecimendata;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.receive.ReceiveSpecimenService;

/**
 * 
 * @author ZhangHui 2015/4/24
 */
@Service(value="receiveSpecimenService")
public class ReceiveSpecimenServiceImpl 
			extends BaseServiceImpl<ReceiveSpecimendata, ReceiveSpecimenDAO>
			implements ReceiveSpecimenService{
	@Autowired private ReceiveSpecimenDAO receiveSpecimenDAO;

	@Override
	public ReceiveSpecimenDAO getDAO() {
		return receiveSpecimenDAO;
	}

	/**
	 * 保存检测数据
	 * @author ZhangHui 2015/4/24
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void create(List<ReceiveSpecimendata> recSpecimens, Long rec_test_result_id) throws ServiceException {
		if(recSpecimens==null || recSpecimens.size()<1 || rec_test_result_id==null){
			return;
		}
		try {
			for(ReceiveSpecimendata recSpecimen : recSpecimens){
				recSpecimen.setId(null);
				recSpecimen.setRec_test_result_id(rec_test_result_id);
				create(recSpecimen);
			}
		} catch (ServiceException e) {
			throw new ServiceException("ReceiveSpecimenServiceImpl.create()-->" + e.getMessage(), e.getException());
		}
	}

}
