package com.gettec.fsnip.fsn.service.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.business.LiutongToProduceLicenseDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.LiutongToProduceLicense;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface LiutongToProduceLicenseService extends
		BaseService<LiutongToProduceLicense, LiutongToProduceLicenseDAO> {

	List<LiutongToProduceLicense> getByOrganizationAndProducerId(Long organization, Long producerId)
		throws ServiceException;
	
	LiutongToProduceLicense findByOrgIdAndProducerIdAndQs(Long organization, Long producerId, String qsNo)
		throws ServiceException;
	
	void save(LiutongToProduceLicense produceLicense) throws ServiceException;
	
	void save(List<LiutongToProduceLicense> listQSInfo, Long organization) throws ServiceException;
	
	void approved(List<LiutongToProduceLicense> listLtQs) throws ServiceException;

	void approvedByOrgIdAndProduceId(Long organization, Long producerId,
			boolean isPass) throws ServiceException;

	void updateQsNoByOldQsNo(ProductionLicenseInfo origQsNo, String oldQsNo)
			throws ServiceException;

	long countByQSNo(String qsNo) throws ServiceException;

	List<LiutongToProduceLicense> findByProducerIdAndQs(Long producerId, String qsNo)throws ServiceException;

	boolean ValidateByProducerIdAndQs(Long producerId, String qsNo)
			throws ServiceException;
}
