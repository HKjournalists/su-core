package com.gettec.fsnip.fsn.service.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.business.OrgInstitutionDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.OrganizingInstitution;
import com.gettec.fsnip.fsn.service.business.OrgInstitutionService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;

/**
 * OrgInstitutionServiceImpl service implementation
 * @author Hui Zhang
 */
@Service(value="orgInstitutionService")
public class OrgInstitutionServiceImpl extends BaseServiceImpl<OrganizingInstitution, OrgInstitutionDAO> 
		implements OrgInstitutionService{
	@Autowired private OrgInstitutionDAO orgInstitutionDAO;

	/**
	 * 按组织机构代码编号查找一条织机构代码信息
	 * @param organizationNo
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public OrganizingInstitution findByOrgCode(String organizationNo) throws ServiceException {
		try {
			return orgInstitutionDAO.findById(organizationNo);
		} catch (JPAException jpae) {
			throw new ServiceException("【service-error】按组织机构代码编号查找一条织机构代码信息，出现异常。", jpae);
		}
	}
	
	/**
	 * 保存织机构代码信息
	 * @param orgInstitution
	 * @return void
	 * @throws ServiceException 
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public OrganizingInstitution save(OrganizingInstitution org, boolean isUpdate) throws ServiceException {
		try {
			if(org == null){ return null; }
			OrganizingInstitution orig_org = getDAO().findById(org.getOrgCode());
			if(orig_org == null){
				create(org);
				orig_org = org;
			}else if(isUpdate){
					if(org.isRhFlag()){
						/* 如果是仁怀企业登记表界面 */
						orig_org.setRegisterNo(org.getRegisterNo());
					}else{
						orig_org.setOrgName(org.getOrgName());
						orig_org.setStartTime(org.getStartTime());
						orig_org.setEndTime(org.getEndTime());
						orig_org.setUnitsAwarded(org.getUnitsAwarded());
						orig_org.setOrgType(org.getOrgType());
						orig_org.setOrgAddress(org.getOrgAddress());
						orig_org.setOtherAddress(org.getOtherAddress());
					}
					update(orig_org);
			}
			return orig_org;
		} catch (JPAException jpae) {
			throw new ServiceException("【service-error】保存一条织机构代码信息，出现异常。", jpae);
		}
	}
	
	/**
	 * 保存织机构代码信息
	 * @param orgInstitution
	 * @return void
	 * @throws ServiceException 
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public OrganizingInstitution save(String orgcode) throws ServiceException {
		try {
			if(orgcode==null || orgcode.trim().length()==0){ return null; }
			OrganizingInstitution org = new OrganizingInstitution(orgcode);
			return save(org, false);
		} catch (ServiceException sex) {
			throw new ServiceException("OrgInstitutionServiceImpl.save()-->" + sex.getMessage(), sex.getException());
		}
	}

	@Override
	public OrgInstitutionDAO getDAO() {
		return orgInstitutionDAO;
	}
}