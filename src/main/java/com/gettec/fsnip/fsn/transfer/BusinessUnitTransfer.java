package com.gettec.fsnip.fsn.transfer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gettec.fsnip.fsn.model.base.District;
import com.gettec.fsnip.fsn.model.base.Office;
import com.gettec.fsnip.fsn.model.base.SysArea;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.BusinessUnitToLims;
import com.gettec.fsnip.fsn.model.business.CirculationPermitInfo;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.model.business.LiquorSalesLicenseInfo;
import com.gettec.fsnip.fsn.model.business.OrganizingInstitution;
import com.gettec.fsnip.fsn.model.business.TaxRegisterInfo;
import com.gettec.fsnip.fsn.model.market.Resource;

public class BusinessUnitTransfer {
	public static BusinessUnit transfer(BusinessUnit a){
		if(a==null){
			return null;
		}
		BusinessUnit b=new BusinessUnit();
		b.setAbout(a.getAbout());
		b.setAddress(a.getAddress());
		b.setAdministrativeLevel(a.getAdministrativeLevel());
		b.setBindQsFlag(a.isBindQsFlag());
		b.setContact(a.getContact());
		b.setCountNum(a.getCountNum());
		b.setEmail(a.getEmail());
		b.setEnterpriteDate(a.getEnterpriteDate());
		b.setFax(a.getFax());
		b.setGuid(a.getGuid());
		b.setId(a.getId());
		b.setLincesNo(a.getLincesNo());
		b.setMarketOrg(a.getMarketOrg());
		b.setMobile(a.getMobile());
		b.setName(a.getName());
		b.setNote(a.getNote());
		b.setNowPdfUrl(a.getNowPdfUrl());
		b.setOrganization(a.getOrganization());
		b.setOtherAddress(a.getOtherAddress());
		b.setParentOrganizationId(b.getParentOrganizationId());
		b.setPersonInCharge(a.getPersonInCharge());
		b.setPostalCode(a.getPostalCode());
		b.setRegion(a.getRegion());
		b.setSampleLocal(a.getSampleLocal());
		b.setSignFlag(a.isSignFlag());
		b.setStep(a.getStep());
		b.setTelephone(a.getTelephone());
		b.setType(a.getType());
		b.setWdaBackFlag(a.isWdaBackFlag());
		b.setWdaBackMsg(a.getWdaBackMsg());
		b.setWebsite(a.getWebsite());
		b.setRtspUrl(a.getRtspUrl());
		if(a.getLicense()!=null){
			LicenseInfo license=new LicenseInfo();
			license.setBusinessAddress(a.getLicense().getBusinessAddress());
			license.setEndTime(a.getLicense().getEndTime());
			license.setEstablishTime(a.getLicense().getEstablishTime());
			license.setIssuingAuthority(a.getLicense().getIssuingAuthority());
			license.setLegalName(a.getLicense().getLegalName());
			license.setLicensename(a.getLicense().getLicensename());
			license.setLicenseNo(a.getLicense().getLicenseNo());
			license.setOtherAddress(a.getLicense().getOtherAddress());
			license.setPracticalCapital(a.getLicense().getPracticalCapital());
			license.setRegisteredCapital(a.getLicense().getRegisteredCapital());
			license.setRegistrationTime(a.getLicense().getRegistrationTime());
			license.setRhFlag(a.getLicense().isRhFlag());
			license.setStartTime(a.getLicense().getStartTime());
			license.setSubjectType(a.getLicense().getSubjectType());
			license.setToleranceRange(a.getLicense().getToleranceRange());
			b.setLicense(license);
		}
		if(a.getOrgInstitution()!=null){
			OrganizingInstitution orgInstitution=new OrganizingInstitution();
			orgInstitution.setEndTime(a.getOrgInstitution().getEndTime());
			orgInstitution.setOrgAddress(a.getOrgInstitution().getOrgAddress());
			orgInstitution.setOrgCode(a.getOrgInstitution().getOrgCode());
			orgInstitution.setOrgName(a.getOrgInstitution().getOrgName());
			orgInstitution.setOrgType(a.getOrgInstitution().getOrgType());
			orgInstitution.setOtherAddress(a.getOrgInstitution().getOtherAddress());
			orgInstitution.setRegisterNo(a.getOrgInstitution().getRegisterNo());
			orgInstitution.setRhFlag(a.getOrgInstitution().isRhFlag());
			orgInstitution.setStartTime(a.getOrgInstitution().getStartTime());
			orgInstitution.setUnitsAwarded(a.getOrgInstitution().getUnitsAwarded());
			b.setOrgInstitution(orgInstitution);
		}
		if(a.getDistribution()!=null){
			CirculationPermitInfo distribution=new CirculationPermitInfo();
			distribution.setBusinessAddress(a.getDistribution().getBusinessAddress());
			distribution.setDistributionNo(a.getDistribution().getDistributionNo());
			distribution.setEndTime(a.getDistribution().getEndTime());
			distribution.setLegalName(a.getDistribution().getLegalName());
			distribution.setLicenseName(a.getDistribution().getLicenseName());
			distribution.setLicensingAuthority(a.getDistribution().getLicensingAuthority());
			distribution.setManageProject(a.getDistribution().getManageProject());
			distribution.setManageType(a.getDistribution().getManageType());
			distribution.setOtherAddress(a.getDistribution().getOtherAddress());
			distribution.setStartTime(a.getDistribution().getStartTime());
			distribution.setSubjectType(a.getDistribution().getSubjectType());
			distribution.setToleranceRange(a.getDistribution().getToleranceRange());
			distribution.setToleranceTime(a.getDistribution().getToleranceTime());
			a.setDistribution(distribution);
		}
		if(a.getTaxRegister()!=null){
			TaxRegisterInfo taxRegister=new TaxRegisterInfo();
			taxRegister.setAddress(a.getTaxRegister().getAddress());
			taxRegister.setApproveSetUpAuthority(a.getTaxRegister().getApproveSetUpAuthority());
			taxRegister.setBusinessScope(a.getTaxRegister().getBusinessScope());
			taxRegister.setId(a.getTaxRegister().getId());
			taxRegister.setIssuingAuthority(a.getTaxRegister().getIssuingAuthority());
			taxRegister.setLegalName(a.getTaxRegister().getLegalName());
			taxRegister.setRegisterType(a.getTaxRegister().getRegisterType());
			taxRegister.setTaxerName(a.getTaxRegister().getTaxerName());
			taxRegister.setWithholdingObligations(a.getTaxRegister().getWithholdingObligations());
			Set<Resource> taxAttachments=new HashSet<Resource>();
			for(Resource r:a.getTaxRegAttachments()){
				taxAttachments.add(r);
			}
			taxRegister.setTaxAttachments(taxAttachments);
			b.setTaxRegister(taxRegister);
		}
		if(a.getLiquorSalesLicense()!=null){
			LiquorSalesLicenseInfo liquorSalesLicense=new LiquorSalesLicenseInfo();
			liquorSalesLicense.setAddress(a.getLiquorSalesLicense().getAddress());
			liquorSalesLicense.setBusinessScope(a.getLiquorSalesLicense().getBusinessScope());
			liquorSalesLicense.setBusinessType(a.getLiquorSalesLicense().getBusinessType());
			liquorSalesLicense.setCertificateNo(a.getLiquorSalesLicense().getCertificateNo());
			liquorSalesLicense.setEndTime(a.getLiquorSalesLicense().getEndTime());
			liquorSalesLicense.setId(a.getLiquorSalesLicense().getId());
			liquorSalesLicense.setIssuingAuthority(a.getLiquorSalesLicense().getIssuingAuthority());
			liquorSalesLicense.setLegalName(a.getLiquorSalesLicense().getLegalName());
			liquorSalesLicense.setStartTime(a.getLiquorSalesLicense().getStartTime());
			b.setLiquorSalesLicense(liquorSalesLicense);
		}
		
		Set<Resource> busPdfAttachments=new HashSet<Resource>();
		for(Resource r:b.getBusPdfAttachments()){
			busPdfAttachments.add(new Resource(r));
		}
		b.setBusPdfAttachments(busPdfAttachments);
		Set<BusinessUnit> providers=new HashSet<BusinessUnit>();
		for(BusinessUnit bu:a.getProviders()){
			BusinessUnit buu=new BusinessUnit();
			buu.setAbout(bu.getAbout());
			buu.setAddress(bu.getAddress());
			buu.setAdministrativeLevel(bu.getAdministrativeLevel());
			buu.setBindQsFlag(bu.isBindQsFlag());
			buu.setContact(bu.getContact());
			buu.setCountNum(bu.getCountNum());
			buu.setEmail(bu.getEmail());
			buu.setEnterpriteDate(bu.getEnterpriteDate());
			buu.setFax(bu.getFax());
			buu.setGuid(bu.getGuid());
			buu.setId(bu.getId());
			buu.setLincesNo(bu.getLincesNo());
			buu.setMarketOrg(bu.getMarketOrg());
			buu.setMobile(bu.getMobile());
			buu.setName(bu.getName());
			buu.setNote(bu.getNote());
			buu.setNowPdfUrl(bu.getNowPdfUrl());
			buu.setOrganization(bu.getOrganization());
			buu.setOtherAddress(bu.getOtherAddress());
			buu.setParentOrganizationId(buu.getParentOrganizationId());
			buu.setPersonInCharge(bu.getPersonInCharge());
			buu.setPostalCode(bu.getPostalCode());
			buu.setRegion(bu.getRegion());
			buu.setSampleLocal(bu.getSampleLocal());
			buu.setSignFlag(bu.isSignFlag());
			buu.setStep(bu.getStep());
			buu.setTelephone(bu.getTelephone());
			buu.setType(bu.getType());
			buu.setWdaBackFlag(bu.isWdaBackFlag());
			buu.setWdaBackMsg(bu.getWdaBackMsg());
			buu.setWebsite(bu.getWebsite());
			providers.add(buu);
		}
		b.setProviders(providers);
		Set<BusinessUnit> customers=new HashSet<BusinessUnit>();
		for(BusinessUnit bu:a.getCustomers()){
			BusinessUnit buu=new BusinessUnit();
			buu.setAbout(bu.getAbout());
			buu.setAddress(bu.getAddress());
			buu.setAdministrativeLevel(bu.getAdministrativeLevel());
			buu.setBindQsFlag(bu.isBindQsFlag());
			buu.setContact(bu.getContact());
			buu.setCountNum(bu.getCountNum());
			buu.setEmail(bu.getEmail());
			buu.setEnterpriteDate(bu.getEnterpriteDate());
			buu.setFax(bu.getFax());
			buu.setGuid(bu.getGuid());
			buu.setId(bu.getId());
			buu.setLincesNo(bu.getLincesNo());
			buu.setMarketOrg(bu.getMarketOrg());
			buu.setMobile(bu.getMobile());
			buu.setName(bu.getName());
			buu.setNote(bu.getNote());
			buu.setNowPdfUrl(bu.getNowPdfUrl());
			buu.setOrganization(bu.getOrganization());
			buu.setOtherAddress(bu.getOtherAddress());
			buu.setParentOrganizationId(buu.getParentOrganizationId());
			buu.setPersonInCharge(bu.getPersonInCharge());
			buu.setPostalCode(bu.getPostalCode());
			buu.setRegion(bu.getRegion());
			buu.setSampleLocal(bu.getSampleLocal());
			buu.setSignFlag(bu.isSignFlag());
			buu.setStep(bu.getStep());
			buu.setTelephone(bu.getTelephone());
			buu.setType(bu.getType());
			buu.setWdaBackFlag(bu.isWdaBackFlag());
			buu.setWdaBackMsg(bu.getWdaBackMsg());
			buu.setWebsite(bu.getWebsite());
			customers.add(buu);
		}
		b.setCustomers(customers);
		if(a.getBusinessUnitToLims()!=null){
			BusinessUnitToLims businessUnitToLims=new BusinessUnitToLims();
			businessUnitToLims.setCreateByUser(a.getBusinessUnitToLims().getCreateByUser());
			businessUnitToLims.setCreateTime(a.getBusinessUnitToLims().getCreateTime());
			businessUnitToLims.setEdition(a.getBusinessUnitToLims().getEdition());
			businessUnitToLims.setId(a.getBusinessUnitToLims().getId());
			b.setBusinessUnitToLims(businessUnitToLims);
		}
		if(a.getSysArea()!=null){
			SysArea sysArea=new SysArea();
			sysArea.setCode(a.getSysArea().getCode());
			sysArea.setHasChildren(a.getSysArea().isHasChildren());
			sysArea.setId(a.getSysArea().getId());
			sysArea.setLeafId(a.getSysArea().getLeafId());
			sysArea.setName(a.getSysArea().getName());
			sysArea.setParentId(a.getSysArea().getParentId());
			sysArea.setParentIds(a.getSysArea().getParentIds());
			sysArea.setType(a.getSysArea().getType());
			b.setSysArea(sysArea);
		}
		if(a.getOffice()!=null){
			Office office=new Office();
			office.setAreaId(a.getOffice().getId());
			office.setCode(a.getOffice().getCode());
			office.setDqjbxh(a.getOffice().getDqjbxh());
			office.setHasChildren(a.getOffice().isHasChildren());
			office.setId(a.getOffice().getId());
			office.setLeafId(a.getOffice().getLeafId());
			office.setName(a.getOffice().getName());
			office.setParentId(a.getOffice().getParentId());
			office.setParentIds(a.getOffice().getParentIds());
			office.setType(a.getOffice().getType());
			b.setOffice(office);
		}
		Set<District> district = new HashSet<District>();
		for(District d:a.getDistrict()){
			District di=new District();
			di.setDescription(d.getDescription());
			di.setId(d.getId());
			di.setLabel(d.getLabel());
			di.setSort(d.getSort());
			di.setType(d.getType());
			di.setValue(d.getValue());
			district.add(di);
		}
		b.setDistrict(district);
		return b;
	}
	public static void transfer(List<BusinessUnit> businessUnitList){
		for(BusinessUnit b:businessUnitList){
			b=transfer(b);
		}
	}
}
