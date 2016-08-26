package com.gettec.fsnip.fsn.service.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.business.LiutongFieldValueDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.LiutongFieldValue;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface LiutongFieldValueService extends
		BaseService<LiutongFieldValue, LiutongFieldValueDAO> {

	List<LiutongFieldValue> getByProducerId(Long producerId) throws ServiceException;
	
	void save(List<LiutongFieldValue> fieldValues) throws ServiceException;
	
	LiutongFieldValue findByProducerIdAndValueAndDisplay(long produceId, String value, String display)
			throws ServiceException;
	
	void approved(List<LiutongFieldValue> fieldValues) throws ServiceException;

	void approvedByProducerId(Long producerId, boolean isPass)
			throws ServiceException;
	
	List<Long> getResourceIds(long produceId, String value, String display) throws ServiceException;
}
