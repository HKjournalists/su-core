package com.gettec.fsnip.fsn.service.resource.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gettec.fsnip.fsn.dao.resource.ResourceOfProlicinfoApplicantClaimDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.resource.ResourceOfProlicinfoApplicantClaim;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.resource.ResourceOfProlicinfoApplicantClaimService;

/**
 * 
 * @author ZhangHui 2015/6/1
 */
@Service(value="resourceOfProlicinfoApplicantClaimService")
public class ResourceOfProlicinfoApplicantClaimServiceImpl
		extends BaseServiceImpl<ResourceOfProlicinfoApplicantClaim, ResourceOfProlicinfoApplicantClaimDAO> 
		implements ResourceOfProlicinfoApplicantClaimService{

	@Autowired private ResourceOfProlicinfoApplicantClaimDAO resourceOfProlicinfoApplicantClaimDAO;
	
	@Override
	public ResourceOfProlicinfoApplicantClaimDAO getDAO() {
		return resourceOfProlicinfoApplicantClaimDAO;
	}

	/**
	 * 获取企业在申请认领qs号时，上传的图片
	 * @param applicantQsid 当前正在处理的认领申请对应的，qs备注信息对应的id
	 * @author ZhangHui 2015-6-1
	 * @throws ServiceException 
	 */
	@Override
	public List<ResourceOfProlicinfoApplicantClaim> findByApplicantQsId(Long applicantQsid) throws ServiceException {
		try {
			return getDAO().findByApplicantQsId(applicantQsid);
		} catch (Exception e) {
			throw new ServiceException("ProductionLicenseServiceImpl.findByApplicantId()" + e.getMessage(), e);
		}
	}

}