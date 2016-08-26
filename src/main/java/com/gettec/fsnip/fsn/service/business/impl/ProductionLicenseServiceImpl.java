package com.gettec.fsnip.fsn.service.business.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.business.ProductionLicenseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.LicenceFormat;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.ProductionLicenseInfoApplicantClaim;
import com.gettec.fsnip.fsn.model.resource.ResourceOfProlicinfoApplicantClaim;
import com.gettec.fsnip.fsn.service.business.LicenceFormatService;
import com.gettec.fsnip.fsn.service.business.ProductionLicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.market.ResourceTypeService;
import com.gettec.fsnip.fsn.service.product.Business2QsRelationService;

/**
 * ProductionLicenseServiceImpl service implementation
 * @author Hui Zhang
 */
@Service(value="productionLicenseService")
public class ProductionLicenseServiceImpl extends BaseServiceImpl<ProductionLicenseInfo, ProductionLicenseDAO>
     	implements ProductionLicenseService{
	
	@Autowired private ProductionLicenseDAO productionLicenseDAO;
	@Autowired private LicenceFormatService licenceFormatService;
	@Autowired private Business2QsRelationService business2QsRelationService;
	@Autowired private ResourceTypeService resourceTypeService;
	@Autowired private ResourceService testResourceService;
	
	@Override
	public ProductionLicenseDAO getDAO() {
		return productionLicenseDAO;
	}
	
	/**
	 * 保存生产许可证信息（不处理图片），并维护 企业-qs 关系
	 * @param currentBusName 当前正在操作该qs号的企业名称
	 * @return boolean
	 *           true   代表 当前操作下，允许修改生产许可证信息
	 *           false  代表 当前操作下，不允许修改生产许可证信息
	 * @author ZhangHui 2015/5/21
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(ProductionLicenseInfo proLicInfo, String currentBusName) throws ServiceException {
		try {
			if(proLicInfo==null || proLicInfo.getQsNo().trim().length()==0 || proLicInfo.getBussinessId()==null ||
					currentBusName==null || proLicInfo.getBusunitName()==null ||
					"".equals(currentBusName) || "".equals(proLicInfo.getBusunitName())){
				return;
			}
			
			ProductionLicenseInfo orig_proLicense = getDAO().findByQsno(proLicInfo.getQsNo());
			
			// 当前操作下，当前qs信息能否被修改的标识
			boolean isHaveUpdateRight = false;
			// 更新后qsId
			Long now_edit_qsId = null;
			// 更新前qsId
			Long old_edit_qsId = proLicInfo.getId();
			
			int operate_type = 0; // 0 代表新增操作下，qs号在系统中不存在的情况
			if(old_edit_qsId != null){
				if(orig_proLicense == null){
					operate_type = 2; // 2 代表编辑操作下，qs号在系统中不存在的情况
				}else{
					operate_type = 3; // 3 代表编辑操作下，qs号在系统中存在的情况
					now_edit_qsId = orig_proLicense.getId();
				}
			}else if(orig_proLicense != null){
				operate_type = 1; // 1 代表新增操作下，qs号在系统中存在的情况
				now_edit_qsId = orig_proLicense.getId();
				
			}
			
			if(now_edit_qsId != null){
				proLicInfo.setId(now_edit_qsId);
			}
			Long bussinessId = proLicInfo.getBussinessId();
			
			if(operate_type == 0){
				// 1.1 新增一条生产许可证信息
				createNewQsInfo(proLicInfo);
				// 1.2 新增一条 企业-qs号 关系
				business2QsRelationService.create(bussinessId, proLicInfo.getId(), proLicInfo.getBusunitName(), currentBusName);
			}else if(operate_type == 1){
				// 2.1 判断有无修改的权限
				isHaveUpdateRight = business2QsRelationService.isHaveUpdateRightOfQs(currentBusName, now_edit_qsId);
				// 2.2 更新qs号基本信息
				if(isHaveUpdateRight){
					setProductionLicenseInfoValue(orig_proLicense, proLicInfo);
					update(orig_proLicense);
				}
				// 2.3 新增一条 企业-qs号 关系
				business2QsRelationService.create(bussinessId, now_edit_qsId, proLicInfo.getBusunitName(), currentBusName);
			}else if(operate_type == 2){
				// 3.1 新增一条生产许可证信息
				LicenceFormat licenceFormat = proLicInfo.getQsnoFormat();
				if(licenceFormat != null) {
					proLicInfo.setQsnoFormat(licenceFormatService.findById(licenceFormat.getId()));
				}
				create(proLicInfo);
				// 3.2 更新 企业-qs号 关系
				business2QsRelationService.updateByContrast(bussinessId, proLicInfo.getId(), old_edit_qsId, proLicInfo.getBusunitName(), currentBusName);
			}else if(operate_type == 3){
				// 4.1 判断有无修改的权限
				isHaveUpdateRight = business2QsRelationService.isHaveUpdateRightOfQs(currentBusName, now_edit_qsId);
				// 4.2 更新qs号基本信息
				if(isHaveUpdateRight){
					setProductionLicenseInfoValue(orig_proLicense, proLicInfo);
					update(orig_proLicense);
				}
				// 4.3 更新 企业-qs号 关系
				business2QsRelationService.updateByContrast(bussinessId, proLicInfo.getId(), old_edit_qsId, proLicInfo.getBusunitName(), currentBusName);
			}
		} catch (DaoException e) {
			throw new ServiceException("[DaoException]ProductionLicenseServiceImpl.save()-->"+e.getMessage(), e.getException());
		} catch (ServiceException e) {
			throw new ServiceException("[ServiceException]ProductionLicenseServiceImpl.save()-->"+e.getMessage(), e.getException());
		} catch (Exception e) {
			throw new ServiceException("[Exception]ProductionLicenseServiceImpl.save()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：新增一条qs记录
	 * @author ZhangHui 2015/6/10
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void createNewQsInfo(ProductionLicenseInfo proLicInfo) throws ServiceException{
		try {
			if(proLicInfo==null || proLicInfo.getId()!=null){
				return;
			}
			
			// 新增一条qs
			LicenceFormat licenceFormat = proLicInfo.getQsnoFormat();
			proLicInfo.setQsnoFormat(licenceFormatService.findById(licenceFormat.getId()));
			create(proLicInfo);
		} catch (Exception e) {
			throw new ServiceException("[Exception]ProductionLicenseServiceImpl.createNewQsInfo()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 更新前的赋值操作
	 * @param orig_proLicense
	 * @param proLicense
	 * @author ZhangHui 2015/5/21
	 * @throws Exception 
	 */
	private void setProductionLicenseInfoValue(ProductionLicenseInfo orig_qs, ProductionLicenseInfo qs) throws Exception {
		if(orig_qs==null || qs==null){
			throw new Exception("方法 setProductionLicenseInfoValue 参数异常");
		}
		orig_qs.setBusunitName(qs.getBusunitName());
		orig_qs.setProductName(qs.getProductName());
		orig_qs.setStartTime(qs.getStartTime());
		orig_qs.setEndTime(qs.getEndTime());
		orig_qs.setAccommodation(qs.getAccommodation());
		orig_qs.setProductionAddress(qs.getProductionAddress());
		orig_qs.setProOtherAddress(qs.getProOtherAddress());
		orig_qs.setAccOtherAddress(qs.getAccOtherAddress());
		orig_qs.setCheckType(qs.getCheckType());
		
		/**
		 * 处理图片
		 */
		Set<Resource> removes = testResourceService.getListOfRemoves(orig_qs.getQsAttachments(), qs.getQsAttachments());
		orig_qs.removeResources(removes);
		
		Set<Resource> adds = testResourceService.getListOfAdds(qs.getQsAttachments());
		orig_qs.addResources(adds);
	}

	/**
	 * 功能描述：获取当前企业的所有qs号
	 * @author ZhangHui 2015/5/21
	 */
	@Override
	public List<ProductionLicenseInfo> getListByBusId(Long bussinessId) throws ServiceException {
		try {
			return getDAO().getListByBusId(bussinessId);
		} catch (DaoException e) {
			throw new ServiceException("ProductionLicenseServiceImpl.getListByBusId() "+e.getMessage(),e);
		}
	}

	/**
	 * 功能描述：根据qs号，获取当前一条详细的qs信息
	 * @author ZhangHui 2015/5/25
	 */
	@Override
	public ProductionLicenseInfo findByQsno(String qsno) throws ServiceException {
		try {
			return getDAO().findByQsno(qsno);
		} catch (DaoException e) {
			throw new ServiceException("ProductionLicenseServiceImpl.findByQsno() "+e.getMessage(),e);
		}
	}

	/**
	 * 背景：当qs号认领申请被审核通过后
	 * 功能描述：将申请的qs号信息同步到正常的qs号信息
	 * @author ZhangHui 2015/6/1
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateInfo(ProductionLicenseInfo orig_proLic, ProductionLicenseInfoApplicantClaim prolic_applicant) throws ServiceException {
		try {
			setProductionLicenseInfoValue(orig_proLic, prolic_applicant);
			update(orig_proLic);
		} catch (Exception e) {
			throw new ServiceException("ProductionLicenseServiceImpl.updateInfo()" + e.getMessage(), e);
		}
	}

	
	/** 
	 * 功能描述：企业提出认领申请时的qs号信息  覆盖掉   目前正在使用的qs信息
	 * @param orig_proLic       目前正在使用的qs信息
	 * @param prolic_applicant  企业提出认领申请时的qs号信息
	 * @author ZhangHui 2015-6-1
	 * @throws Exception 
	 */
	private void setProductionLicenseInfoValue(ProductionLicenseInfo orig_proLic, ProductionLicenseInfoApplicantClaim prolic_applicant) 
			throws Exception {
		
		if(orig_proLic==null || prolic_applicant==null){
			throw new Exception("参数为空");
		}
		
		orig_proLic.setBusunitName(prolic_applicant.getBusunitName());
		orig_proLic.setProductName(prolic_applicant.getProductName());
		orig_proLic.setStartTime(prolic_applicant.getStartTime());
		orig_proLic.setEndTime(prolic_applicant.getEndTime());
		orig_proLic.setAccommodation(prolic_applicant.getAccommodation());
		orig_proLic.setAccOtherAddress(prolic_applicant.getAccOtherAddress());
		orig_proLic.setProductionAddress(prolic_applicant.getProductionAddress());
		orig_proLic.setProOtherAddress(prolic_applicant.getProOtherAddress());
		orig_proLic.setCheckType(prolic_applicant.getCheckType());
		
		Set<Resource> qsAttachments = new HashSet<Resource>();
		for(ResourceOfProlicinfoApplicantClaim res_applicant : prolic_applicant.getQsAttachments()){
			Resource resource = new Resource();
			resource.setFileName(res_applicant.getFile_name());
			resource.setName(res_applicant.getResource_name());
			resource.setUploadDate(res_applicant.getUpload_time());
			resource.setUrl(res_applicant.getUrl());
			resource.setType(resourceTypeService.findById(res_applicant.getResource_type_id()));
			
			qsAttachments.add(resource);
		}
		orig_proLic.setQsAttachments(qsAttachments);
	}

	/**
	 * 功能描述：根据生产许可证编号，获取qs id
	 * @author ZhangHui 2015/6/4
	 * @throws ServiceException 
	 */
	@Override
	public Long findIdByQsno(String qsno) throws ServiceException {
		try {
			return getDAO().findIdByQsno(qsno);
		} catch (DaoException e) {
			throw new ServiceException("ProductionLicenseServiceImpl.findIdByQsno()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 根据产品id查找其所有已绑定且没过期的生产许可证信息
	 * @author longxianzhen  2015/06/26
	 */
	@Override
	public List<ProductionLicenseInfo> getListByProId(Long proId) throws ServiceException{
		try {
			return getDAO().getListByProId(proId);
		} catch (DaoException e) {
			throw new ServiceException("ProductionLicenseServiceImpl.getListByProId()-->" + e.getMessage(), e.getException());
		}
	}
}