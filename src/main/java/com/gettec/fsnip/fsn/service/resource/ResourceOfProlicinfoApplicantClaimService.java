package com.gettec.fsnip.fsn.service.resource;

import java.util.List;

import com.gettec.fsnip.fsn.dao.resource.ResourceOfProlicinfoApplicantClaimDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.resource.ResourceOfProlicinfoApplicantClaim;
import com.gettec.fsnip.fsn.service.common.BaseService;

/**
 * 
 * @author ZhangHui 2015/6/1
 */
public interface ResourceOfProlicinfoApplicantClaimService extends BaseService<ResourceOfProlicinfoApplicantClaim, 
			ResourceOfProlicinfoApplicantClaimDAO>{

	/**
	 * 获取企业在申请认领qs号时，上传的图片
	 * @param applicantQsid 当前正在处理的认领申请对应的，qs备注信息对应的id
	 * @author ZhangHui 2015-6-1
	 * @throws ServiceException 
	 */
	public List<ResourceOfProlicinfoApplicantClaim> findByApplicantQsId(Long applicantQsid) throws ServiceException;
	
}