package com.gettec.fsnip.fsn.dao.resource.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.resource.ResourceOfProlicinfoApplicantClaimDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.resource.ResourceOfProlicinfoApplicantClaim;

/**
 * 
 * @author ZhangHui 2015/6/1
 */
@Repository(value="resourceOfProlicinfoApplicantClaimDAO")
public class ResourceOfProlicinfoApplicantClaimDAOImpl extends BaseDAOImpl<ResourceOfProlicinfoApplicantClaim>
		implements ResourceOfProlicinfoApplicantClaimDAO {

	@PersistenceContext private EntityManager entityManager;
	
	/**
	 * 获取企业在申请认领qs号时，上传的图片
	 * @param applicantQsid 当前正在处理的认领申请对应的，qs备注信息对应的id
	 * @author ZhangHui 2015-6-1
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ResourceOfProlicinfoApplicantClaim> findByApplicantQsId(Long applicantQsid) throws DaoException {
		try {
			if(applicantQsid == null){
				throw new Exception("参数为空");
			}
			
			List<ResourceOfProlicinfoApplicantClaim> result = entityManager
					.createNativeQuery(
							"SELECT * FROM resource_of_prolicinfo_qs_applicant_claim rop "+
							"WHERE qs_id_applicant_claim_back = ?1",
							ResourceOfProlicinfoApplicantClaim.class)
					.setParameter(1, applicantQsid)
					.getResultList();
			
			return result;
		} catch (Exception e) {
			throw new DaoException("ProductLicenseInfoOfApplicantClaimDAOImpl.findByApplicantQsId()-->" + e.getMessage(), e);
		}
	}
}