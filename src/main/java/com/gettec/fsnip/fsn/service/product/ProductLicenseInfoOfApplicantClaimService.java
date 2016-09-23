package com.gettec.fsnip.fsn.service.product;

import com.gettec.fsnip.fsn.dao.product.ProductLicenseInfoOfApplicantClaimDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductionLicenseInfoApplicantClaim;
import com.gettec.fsnip.fsn.service.common.BaseService;

/**
 * 
 * @author ZhangHui 2015/5/29
 */
public interface ProductLicenseInfoOfApplicantClaimService extends BaseService<ProductionLicenseInfoApplicantClaim, 
		ProductLicenseInfoOfApplicantClaimDAO>{

	/**
	 * 功能描述: 根据当前正在处理的认领申请id 获取提出认领申请时，该生产企业提交的qs号信息
	 * @param applicantId 当前正在处理的认领申请id
	 * @author ZhangHui 2015/6/1
	 * @throws ServiceException 
	 */
	public ProductionLicenseInfoApplicantClaim findByApplicantId(Long applicantId) throws ServiceException;
}