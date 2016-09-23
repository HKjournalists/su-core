package com.gettec.fsnip.fsn.dao.product.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductLicenseInfoOfApplicantClaimDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.ProductionLicenseInfoApplicantClaim;

/**
 * 
 * @author ZhangHui 2015/5/29
 */
@Repository(value="productLicenseInfoOfApplicantClaimDAO")
public class ProductLicenseInfoOfApplicantClaimDAOImpl extends BaseDAOImpl<ProductionLicenseInfoApplicantClaim>
		implements ProductLicenseInfoOfApplicantClaimDAO {

	@PersistenceContext private EntityManager entityManager;
	
	/**
	 * 功能描述: 根据当前正在处理的认领申请id 获取提出认领申请时，该生产企业提交的qs号信息
	 * @param applicantId 当前正在处理的认领申请id
	 * @author ZhangHui 2015/6/1
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProductionLicenseInfoApplicantClaim findByApplicantId(Long applicantId) throws DaoException {
		try {
			if(applicantId == null){
				throw new Exception("参数为空");
			}
			
			List<ProductionLicenseInfoApplicantClaim> result = entityManager
					.createNativeQuery(
							"SELECT pli.* FROM production_license_info_applicant_claim_backup pli "+
							"INNER JOIN prolicinfo_qs_applicant_claim pqa ON pqa.qs_id_applicant_claim_backup = pli.id AND pqa.id = ?1",
							ProductionLicenseInfoApplicantClaim.class)
					.setParameter(1, applicantId)
					.getResultList();
			
			if(result != null && result.size() > 0){
				return result.get(0);
			}
			
			return null;
		} catch (Exception e) {
			throw new DaoException("ProductLicenseInfoOfApplicantClaimDAOImpl.findByApplicantId()-->" + e.getMessage(), e);
		}
	}
}