package com.gettec.fsnip.fsn.service.product.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.product.ProductionLicenseApplicantClaimHandleDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.model.product.ProductionLicenseApplicantClaimHandle;
import com.gettec.fsnip.fsn.model.product.ProductionLicenseInfoApplicantClaim;
import com.gettec.fsnip.fsn.model.resource.ResourceOfProlicinfoApplicantClaim;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.ProductionLicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.Business2QsRelationService;
import com.gettec.fsnip.fsn.service.product.ProductLicenseInfoOfApplicantClaimService;
import com.gettec.fsnip.fsn.service.product.ProductLicenseOfApplicantClaimHandleService;
import com.gettec.fsnip.fsn.service.resource.ResourceOfProlicinfoApplicantClaimService;
import com.gettec.fsnip.fsn.vo.product.ProductionLicenseApplicantClaimHandleVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 
 * @author ZhangHui 2015/5/29
 */
@Service(value="productLicenseOfApplicantClaimHandleService")
public class ProductLicenseOfApplicantClaimHandleServiceImpl
		extends BaseServiceImpl<ProductionLicenseApplicantClaimHandle, ProductionLicenseApplicantClaimHandleDAO> 
		implements ProductLicenseOfApplicantClaimHandleService{

    @Autowired private ProductionLicenseApplicantClaimHandleDAO productLicenseOfApplicantClaimHandleDAO;
    @Autowired private ResourceOfProlicinfoApplicantClaimService resourceOfProlicinfoApplicantClaimService;
    @Autowired private ProductLicenseInfoOfApplicantClaimService productLicenseInfoOfApplicantClaimService;
    @Autowired private ProductionLicenseService productionLicenseService;
    @Autowired private Business2QsRelationService business2QsRelationService;
    @Autowired protected BusinessUnitService businessUnitService;

	@Override
	public ProductionLicenseApplicantClaimHandleDAO getDAO() {
		return productLicenseOfApplicantClaimHandleDAO;
	}

	/**
	 * 新增一条企业申请认领qs号记录
	 * @author ZhangHui 2015/6/1
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void createNewRecord(ProductionLicenseInfoApplicantClaim proLic_applicant, Long applicantBusId, String userName) throws ServiceException {
		try {
			if(proLic_applicant==null || proLic_applicant.getQs_id()==null || 
					proLic_applicant.getBusunitName()==null || "".equals(proLic_applicant.getBusunitName())){
				throw new Exception("参数异常");
			}
			
			// 如果之前已经存在一条 企业-qs号 的已退回申请记录，则将其强制设置为过期
			ProductionLicenseApplicantClaimHandleVO proLic_handle_vo = findOfNotOverdue(proLic_applicant.getQs_id(), applicantBusId);
			if(proLic_handle_vo!=null && proLic_handle_vo.getId()!=null && proLic_handle_vo.getHandle_result()==4){
				setApplicantOverdue(proLic_handle_vo.getId());
			}
			
			// 申请信息备份
			productLicenseInfoOfApplicantClaimService.create(proLic_applicant);
			
			// 新增一条申请记录
			ProductionLicenseApplicantClaimHandle prolic_handle = new ProductionLicenseApplicantClaimHandle();
			prolic_handle.setQs_id(proLic_applicant.getId());
			prolic_handle.setBusiness_id(applicantBusId);
			prolic_handle.setApplicant(userName);
			prolic_handle.setApplicant_time(new Date());
			prolic_handle.setHandle_result(1);  // 1代表待审核
			create(prolic_handle);
			
			// 生成资源
			for(ResourceOfProlicinfoApplicantClaim resource_applicant : proLic_applicant.getQsAttachments()){
				if(resource_applicant.getFile_name()==null || "".equals(resource_applicant.getFile_name()) ||
						resource_applicant.getResource_name()==null || "".equals(resource_applicant.getResource_name()) ||
						resource_applicant.getUrl()==null || "".equals(resource_applicant.getUrl()) ||
						resource_applicant.getResource_type_id()==null){
					throw new Exception("参数异常");
				}
				
				resource_applicant.setQs_id_applicant_claim_back(proLic_applicant.getId());
				resource_applicant.setUpload_user_name(userName);
				resource_applicant.setUpload_time(new Date());
				resourceOfProlicinfoApplicantClaimService.create(resource_applicant);
			}
		} catch (ServiceException e) {
			throw new ServiceException("[ServiceException]ProductLicenseInfoOfApplicantClaimServiceImpl.isEffective-->" + e.getMessage(), e);
		} catch (Exception e) {
			throw new ServiceException("[Exception]ProductLicenseInfoOfApplicantClaimServiceImpl.isEffective-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 查找一条未过期的企业申请认领qs的记录
	 * @param qs_id         // 需要申请认领qsId
	 * @param currentBusId  // 提出申请认领企业id
	 * @author ZhangHui 2015/6/1 
	 * @throws ServiceException 
	 */
	@Override
	public ProductionLicenseApplicantClaimHandleVO findOfNotOverdue(Long qs_id, Long applicantBusId) throws ServiceException {
		try {
			return getDAO().findOfNotOverdue(qs_id, applicantBusId);
		} catch (DaoException e) {
			throw new ServiceException("ProductLicenseOfApplicantClaimHandleServiceImpl.find()-->" + e.getMessage(),e);
		}
	}
	
	/**
	 * 功能描述：查找一条未过期的企业申请认领qs的记录
	 * @param qs_no         // 需要申请认领qs号
	 * @param organization  // 提出申请认领企业组织机构
	 * @author ZhangHui 2015/6/19
	 * @throws ServiceException 
	 */
	@Override
	public ProductionLicenseApplicantClaimHandleVO findOfNotOverdue(String qs_no, Long organization) throws ServiceException {
		try {
			return getDAO().findOfNotOverdue(qs_no, organization);
		} catch (DaoException e) {
			throw new ServiceException("ProductLicenseOfApplicantClaimHandleServiceImpl.find()-->" + e.getMessage(),e);
		}
	}

	/**
	 * 设置申请过期
	 * @author ZhangHui 2015/6/1
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void setApplicantOverdue(Long id) throws ServiceException {
		try {
			getDAO().setApplicantOverdue(id);
		} catch (DaoException e) {
			throw new ServiceException("ProductLicenseOfApplicantClaimHandleServiceImpl.setApplicantOverdue()-->" + e.getMessage(),e);
		}
	}

	/**
	 * 功能描述：执行处理操作
	 * @param applicantId    当前正在审核的申请记录id
	 * @param qsId           原生产许可证信息的qsId（审核通过后，此qs信息会被申请时qs信息覆盖）
	 * @param pass
	 * 			true:  通过审核
	 * 			false: 未通过审核
	 * @param note 退回原因
	 * @author Zhanghui 2015/6/1
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void executeProcess(Long applicantId, Long qsId, boolean pass, String note, String applicant_bus_name, AuthenticateInfo info) 
				throws ServiceException {
		try {
			if(applicantId==null || qsId==null){
				throw new Exception("参数为空");
			}
			
			// 准备1：  验证qsId是否正常
			ProductionLicenseInfo orig_proLic = productionLicenseService.findById(qsId);
			if(orig_proLic == null){
				throw new Exception("参数 qsId 无效");
			}
			
			// 准备2： 获取申请时的qs号信息
			ProductionLicenseInfoApplicantClaim prolic_applicant = productLicenseInfoOfApplicantClaimService.findByApplicantId(applicantId);
			if(prolic_applicant == null){
				throw new Exception("参数 applicantId 无效");
			}
			// 准备3： 获取申请时的qs图片
			List<ResourceOfProlicinfoApplicantClaim> resource_applicants = resourceOfProlicinfoApplicantClaimService.
					findByApplicantQsId(prolic_applicant.getId());
			if(resource_applicants.size() < 1){
				throw new Exception("申请时的qs图片不存在");
			}
			prolic_applicant.setQsAttachments(resource_applicants);
			
			// 第一步：改变当前申请的处理状态
			getDAO().executeProcess(applicantId, pass, note, info.getUserName());
			
			if(pass){
				// 第二步：将申请的生产许可证信息以及图片放行，使其可以正常使用
				productionLicenseService.updateInfo(orig_proLic, prolic_applicant);
				
				// 第三步：授予企业该qs号的所有权
				Long bussiness_id = businessUnitService.findIdByName(prolic_applicant.getBusunitName());
				business2QsRelationService.conferOwnership(bussiness_id, qsId, true);
				
				// 第三步：将该qs号的其他待申请处理状态设置为退回，退回原因为：该qs号已经被xx企业认领。
				List<Long> applicant_ids = getDAO().getIdsByHandleResult(qsId, 1);
				
				String idStrs = "";
				for(int i=0; i<applicant_ids.size(); i++){
					if(applicantId.equals(applicant_ids.get(i))){
						continue; // 不包括当前审核通过的认领申请
					}
					
					if(i == applicant_ids.size()-1){
						idStrs += applicant_ids.get(i) + ")";
					}else{
						idStrs += applicant_ids.get(i) + ",";
					}
				}
				
				if(idStrs != ""){
					getDAO().batchSetBackByQsidstrs("(" + idStrs, info.getUserName(), "该qs号已经被企业[" + applicant_bus_name + "]认领，您的认领申请被退回！");
				}
			}
			
		} catch (DaoException e) {
			throw new ServiceException("[DaoException]ProductLicenseOfApplicantClaimHandleServiceImpl.setApplicantOverdue()-->" + e.getMessage(),e);
		} catch (Exception e) {
			throw new ServiceException("[Exception]ProductLicenseOfApplicantClaimHandleServiceImpl.setApplicantOverdue()-->" + e.getMessage(),e);
		}
	}

	/**
	 * 功能描述：根据处理结果获取企业认领申请列表
	 * @author ZhangHui 2015/6/2
	 * @throws ServiceException 
	 */
	@Override
	public List<ProductionLicenseApplicantClaimHandleVO> getListByByHandleResultOfPage(int handle_result, int page, int pageSize) 
			throws ServiceException {
		try {
			return getDAO().getListByPage(handle_result, page, pageSize);
		} catch (DaoException e) {
			throw new ServiceException("ProductLicenseOfApplicantClaimHandleServiceImpl.getListOfNotProcessByPage()-->" + e.getMessage(),e);
		}
	}

	/**
	 * 功能描述：根据处理结果获取企业认领申请数量
	 * @author ZhangHui 2015/6/2
	 * @throws ServiceException 
	 */
	@Override
	public long countByHandleResult(int handle_result) throws ServiceException {
		try {
			return getDAO().count(handle_result);
		} catch (DaoException e) {
			throw new ServiceException("ProductLicenseOfApplicantClaimHandleServiceImpl.countOfNotProcess()-->" + e.getMessage(),e);
		}
	}

	/**
	 * 功能描述：获取当前登录企业 qs认领申请处理消息数量
	 * @author ZhangHui 2015/11
	 * @throws ServiceException
	 */
	@Override
	public long countWithoutOverdue(Long organization) throws ServiceException {
		try {
			return getDAO().countWithoutOverdue(organization);
		} catch (DaoException e) {
			throw new ServiceException("ProductLicenseOfApplicantClaimHandleServiceImpl.countWithoutOverdue()-->" + e.getMessage(),e);
		}
	}

	/**
	 * 功能描述：获取当前登录企业 qs认领申请处理消息
	 * @author ZhangHui 2015/6/11
	 * @throws ServiceException
	 */
	@Override
	public List<ProductionLicenseApplicantClaimHandleVO> getListWithoutOverdueByPage(
			Long organization, int page, int pageSize) throws ServiceException {
		try {
			return getDAO().getListWithoutOverdueByPage(organization, page, pageSize);
		} catch (DaoException e) {
			throw new ServiceException("ProductLicenseOfApplicantClaimHandleServiceImpl.getListWithoutOverdueByPage()-->" + e.getMessage(),e);
		}
	}
}