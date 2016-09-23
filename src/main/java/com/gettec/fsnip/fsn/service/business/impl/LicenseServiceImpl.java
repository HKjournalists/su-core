package com.gettec.fsnip.fsn.service.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.business.LicenseDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.service.business.LicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;

/**
 * LicenseServiceImpl service implementation
 * @author Hui Zhang
 */
@Service(value="licenseService")
public class LicenseServiceImpl extends BaseServiceImpl<LicenseInfo, LicenseDAO> 
		implements LicenseService{
	@Autowired private LicenseDAO licenseDAO;

	@Override
	public LicenseDAO getDAO() {
		return licenseDAO;
	}
	
	/**
	 * 保存营业执照号信息
	 * @param license
	 * @param isUpdate  是否执行更新操作
	 * @return LicenseInfo
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public LicenseInfo save(LicenseInfo license) throws ServiceException{
		try {
			if(license==null || license.getLicenseNo().trim().length()==0){ 
				return null; 
			}
			
			LicenseInfo orig_license = getDAO().findById(license.getLicenseNo());
			if(orig_license == null){
				create(license);
				orig_license = license;
			}else{
				if(license.isRhFlag()){
					/* 如果是仁怀页面提交的营业执照信息 */
					orig_license.setEstablishTime(license.getEstablishTime());
					orig_license.setPracticalCapital(license.getPracticalCapital());
				}else{
					setLicenseValue(orig_license, license);
				}
				update(orig_license);
			}
			return orig_license;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]LicenseServiceImpl.save(license,isUpdate)-->" + jpae.getMessage(), jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]LicenseServiceImpl.save(license,isUpdate)-->" + sex.getMessage(), sex.getException());
		}
	}
	
	/**
	 * 更新前的赋值操作
	 * @param orig_license
	 * @param license
	 * @return void
	 * @author ZhangHui
	 */
	private void setLicenseValue(LicenseInfo orig_license, LicenseInfo license) {
		orig_license.setLicensename(license.getLicensename());
		orig_license.setLegalName(license.getLegalName());
		orig_license.setRegistrationTime(license.getRegistrationTime());
		orig_license.setSubjectType(license.getSubjectType());
		orig_license.setBusinessAddress(license.getBusinessAddress());
		orig_license.setOtherAddress(license.getOtherAddress());
		orig_license.setToleranceRange(license.getToleranceRange());
		orig_license.setRegisteredCapital(license.getRegisteredCapital());
		orig_license.setIssuingAuthority(license.getIssuingAuthority());
		orig_license.setStartTime(license.getStartTime());
		orig_license.setEndTime(license.getEndTime());
	}
	
	@Override
	public LicenseInfo findByLic(String lic){
		try {
			List<LicenseInfo> lics=licenseDAO.getListByCondition(" WHERE e.licenseNo=?1 ", new Object[]{lic});
			if(lics!=null&&lics.size()>0){
				return lics.get(0);
			}
		} catch (JPAException e) {
			((Throwable) e.getException()).printStackTrace();
		}
		return null;
	}

	/**
	 * 功能描述：保存营业执照号
	 * @author ZhangHui 2015/6/5
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public LicenseInfo save(String licenseno) throws ServiceException {
		try {
			if(licenseno != null){
				licenseno.replace(" ", "");
			}
			
			if(licenseno==null || "".equals(licenseno)){
				return null;
			}
			
			LicenseInfo orig_lic = findByLic(licenseno);
			
			if(orig_lic != null){
				return orig_lic;
			}
			
			LicenseInfo lic = new LicenseInfo(licenseno);
			create(lic);
			return lic;
		} catch (Exception e) {
			throw new ServiceException("LicenseServiceImpl.save()-->" + e.getMessage(), e);
		}
	}
}