package com.gettec.fsnip.fsn.service.product.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gettec.fsnip.fsn.dao.product.ProductLicenseInfoOfApplicantClaimDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductionLicenseInfoApplicantClaim;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.ProductLicenseInfoOfApplicantClaimService;

/**
 * 
 * @author ZhangHui 2015/5/29
 */
@Service(value="productLicenseInfoOfApplicantClaimService")
public class ProductLicenseInfoOfApplicantClaimServiceImpl
		extends BaseServiceImpl<ProductionLicenseInfoApplicantClaim, ProductLicenseInfoOfApplicantClaimDAO> 
		implements ProductLicenseInfoOfApplicantClaimService{

    @Autowired private ProductLicenseInfoOfApplicantClaimDAO productLicenseInfoOfApplicantClaimDAO;

	@Override
	public ProductLicenseInfoOfApplicantClaimDAO getDAO() {
		return productLicenseInfoOfApplicantClaimDAO;
	}

	/**
	 * 功能描述: 根据当前正在处理的认领申请id 获取提出认领申请时，该生产企业提交的qs号信息
	 * @param applicantId 当前正在处理的认领申请id
	 * @author ZhangHui 2015/6/1
	 * @throws ServiceException 
	 */
	@Override
	public ProductionLicenseInfoApplicantClaim findByApplicantId(Long applicantId) throws ServiceException {
		try {
			return getDAO().findByApplicantId(applicantId);
		} catch (Exception e) {
			throw new ServiceException("ProductionLicenseServiceImpl.findByApplicantId()" + e.getMessage(), e);
		}
	}
}