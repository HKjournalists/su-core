package com.gettec.fsnip.fsn.service.business.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.business.LiutongFieldValueDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.model.business.LiutongFieldValue;
import com.gettec.fsnip.fsn.model.business.OrganizingInstitution;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.LicenseService;
import com.gettec.fsnip.fsn.service.business.LiutongFieldValueService;
import com.gettec.fsnip.fsn.service.business.OrgInstitutionService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;

@Service(value = "liutongFieldValueService")
public class LiutongFieldValueServiceImpl extends BaseServiceImpl<LiutongFieldValue, LiutongFieldValueDAO>
		implements LiutongFieldValueService {
	@Autowired LiutongFieldValueDAO liutongFieldValueDAO;
	@Autowired ResourceService mkTestResourceService;
	@Autowired BusinessUnitService businessUnitService;
	@Autowired ResourceService testResourceService;
	@Autowired protected OrgInstitutionService orgInstitutionService;
	@Autowired protected LicenseService licenseService;
	
	/**
	 * 通过生产企业id获取组织机构代码和营业执照号
	 */
	@Override
	public List<LiutongFieldValue> getByProducerId(Long producerId)
			throws ServiceException {
		try{
			String condition = " where e.producerId = ?1";
			return getDAO().getListByCondition(condition, new Object[]{producerId});
		}catch(JPAException jpae){
			throw new ServiceException("LiutongFieldValueServiceImpl.getByProducerId()-->" + jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 查询一条营业执照或组织机构代码信息
	 * @param produceId
	 * @param value
	 * @param display
	 * @return LiutongFieldValue
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	public LiutongFieldValue findByProducerIdAndValueAndDisplay(long produceId,
			String value, String display) throws ServiceException {
		try{
			String condition = " where e.producerId =?1 and e.value = ?2 and e.display = ?3";
			Object[] params = new Object[]{produceId,value,display};
			if(value==null||value.equals("")){
				condition = " where e.producerId =?1 and e.display = ?2";
				params = new Object[]{produceId,display};
			}
			List<LiutongFieldValue> listFieldValue = getDAO().getListByCondition(condition, params);
			if(listFieldValue==null || listFieldValue.size()<1) return null;
			return listFieldValue.get(0);
		}catch(JPAException jpae){
			throw new ServiceException("LiutongFieldValueServiceImpl.findByProducerIdAndValueAndDisplay()-->" + jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 保存[生产企业-营业执照号]、[生产企业-组织机构]关系
	 * @param fieldValues
	 * @return void
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(List<LiutongFieldValue> fieldValues) throws ServiceException {
		try{
			if(fieldValues == null || fieldValues.size()<1) return;
			BusinessUnit orig_busUnit = businessUnitService.findById(fieldValues.get(0).getProducerId());
			for(LiutongFieldValue fieldValue : fieldValues){
				LiutongFieldValue liu2fieldval = new LiutongFieldValue(fieldValue);
				/* 保存组织机构或营业执照信息 */
				saveOrgOrLicense(liu2fieldval);
				/* 保存[生产企业-营业执照号]或[生产企业-组织机构]关系 */
				if(liu2fieldval.getId() == null){
					create(liu2fieldval);
				}
				/* 保存图片资源 */
				liu2fieldval = mkTestResourceService.saveResouce(liu2fieldval);
				/* 更新企业信息 */
				setBusunitValue(orig_busUnit, liu2fieldval);
			}
			businessUnitService.update(orig_busUnit);
		}catch(ServiceException sex){
			throw new ServiceException("LiutongFieldValueServiceImpl.save()-->" + sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 保存组织机构或营业执照信息
	 * @param liu2fieldval
	 * @return void
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	private void saveOrgOrLicense(LiutongFieldValue fieldValue) throws ServiceException {
		try {
			if(fieldValue.getDisplay().equals("组织机构代码")){
				orgInstitutionService.save(fieldValue.getValue());
			}else if(fieldValue.getDisplay().equals("营业执照号")){
				licenseService.save(fieldValue.getValue());
			}else{
				return;
			}
		} catch (ServiceException sex) {
			throw new ServiceException("LiutongFieldValueServiceImpl.saveOrgOrLicense()-->" + sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 保存企业信息
	 * @param fieldValue
	 * @return void
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	private void setBusunitValue(BusinessUnit orig_busUnit, LiutongFieldValue fieldValue) throws ServiceException {
		try{
			if(fieldValue.getDisplay().equals("组织机构代码")){
				OrganizingInstitution orig_org = orgInstitutionService.getDAO().findById(fieldValue.getValue());
				orig_busUnit.setOrgInstitution(orig_org);
			}else if(fieldValue.getDisplay().equals("营业执照号")){
				LicenseInfo orig_license = licenseService.getDAO().findById(fieldValue.getValue());
				orig_busUnit.setLicense(orig_license);
			}else{
				return;
			}
		}catch(JPAException jpae){
			throw new ServiceException("LiutongFieldValueServiceImpl.updateBusunit()-->" + jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 审核通过生产企业的营业执照/组织机构代码信息
	 * @param fieldValues
	 * @return void
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void approved(List<LiutongFieldValue> fieldValues) throws ServiceException {
		try{
			for(LiutongFieldValue fieldValue : fieldValues){
				LiutongFieldValue orig_value = findById(fieldValue.getId());
				Set<Resource> attachments = fieldValue.getAttachments(); // 获取页面删除的图片集合
				if(attachments.size()>0){
					for(Resource remove : attachments){
						Resource orig_remove = testResourceService.findById(remove.getId());
						orig_value.removeResources(orig_remove);
					}
				}
				orig_value.setMsg(fieldValue.getMsg());
				orig_value.setPassFlag(fieldValue.getPassFlag());
				update(orig_value);
			}
		}catch(ServiceException sex){
			throw new ServiceException("LiutongFieldValueServiceImpl.approved()-->" + sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 审核通过或退回生产企业的营业执照
	 * 和组织机构代码信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void approvedByProducerId(Long producerId ,boolean isPass)
			throws ServiceException {
		try{
			String condition = " where e.producerId = ?1";
			List<LiutongFieldValue> orig_fields = getDAO().getListByCondition(condition, new Object[]{producerId});
			if(orig_fields == null || orig_fields.size()<1) return;
			for(LiutongFieldValue fd : orig_fields){
				fd.setMsg("");
				fd.setPassFlag(isPass?"审核通过":"审核退回");
				update(fd);
			}
		}catch(JPAException jpae){
			throw new ServiceException("[JPAException]LiutongFieldValueServiceImpl.approvedByProducerId()-->" + jpae.getMessage(), jpae.getException());
		}catch(ServiceException sex){
			throw new ServiceException("[ServiceException]LiutongFieldValueServiceImpl.approvedByProducerId()-->" + sex.getMessage(), sex.getException());
		}
	}
	
	/**
	 * 跟生产企业id，组织机构代码(营业执照号),查找资源id
	 * @author TangXin 
	 */
	@Override
	public List<Long> getResourceIds(long produceId, String value, String display) throws ServiceException {
		try {
			return getDAO().getResourceIds(produceId, value, display);
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(),daoe);
		}
	}

	@Override
	public LiutongFieldValueDAO getDAO() {
		return liutongFieldValueDAO;
	}

}
