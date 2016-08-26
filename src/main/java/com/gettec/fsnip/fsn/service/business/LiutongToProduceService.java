package com.gettec.fsnip.fsn.service.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.business.LiutongToProduceDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.LiutongToProduce;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface LiutongToProduceService extends
		BaseService<LiutongToProduce, LiutongToProduceDAO> {

	long countByOrgIdAndCondition(Long orgId, String configure) throws ServiceException;
	
	List<LiutongToProduce> getProducerByOrganizationWithPage(Long orgId, String configure, int page,
			int pageSize) throws ServiceException;
	
	LiutongToProduce findByOrganizationAnProducerId(Long organization, Long producerId) throws ServiceException;
	
	// LiutongToProduce save(BusinessUnit business, Long liutongOrgId) throws ServiceException;
	
	void save(LiutongToProduce liutongToProduce, long liutongOrgId) throws ServiceException;
	
	//LiutongToProduce validateProducerByReportId(long reportId, long organization) throws ServiceException;

	void approved(LiutongToProduce liutongToProduce) throws ServiceException;
}
