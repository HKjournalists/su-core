package com.gettec.fsnip.fsn.dao.product;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.ProductionLicenseInfoApplicantClaim;

public interface ProductLicenseInfoOfApplicantClaimDAO extends BaseDAO<ProductionLicenseInfoApplicantClaim>{

	/**
	 * 功能描述: 根据当前正在处理的认领申请id 获取提出认领申请时，该生产企业提交的qs号信息
	 * @param applicantId 当前正在处理的认领申请id
	 * @author ZhangHui 2015/6/1
	 * @throws DaoException 
	 */
	public ProductionLicenseInfoApplicantClaim findByApplicantId(Long applicantId) throws DaoException;
	
}