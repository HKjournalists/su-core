package com.gettec.fsnip.fsn.dao.resource;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.resource.ResourceOfProlicinfoApplicantClaim;

/**
 * 
 * @author ZhangHui 2015/6/1
 */
public interface ResourceOfProlicinfoApplicantClaimDAO extends BaseDAO<ResourceOfProlicinfoApplicantClaim>{

	/**
	 * 获取企业在申请认领qs号时，上传的图片
	 * @param applicantQsid 当前正在处理的认领申请对应的，qs备注信息对应的id
	 * @author ZhangHui 2015-6-1
	 * @throws DaoException 
	 */
	public List<ResourceOfProlicinfoApplicantClaim> findByApplicantQsId(Long applicantQsid) throws DaoException;

}