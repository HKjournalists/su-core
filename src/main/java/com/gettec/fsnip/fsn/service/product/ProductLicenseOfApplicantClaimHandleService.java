package com.gettec.fsnip.fsn.service.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.product.ProductionLicenseApplicantClaimHandleDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductionLicenseApplicantClaimHandle;
import com.gettec.fsnip.fsn.model.product.ProductionLicenseInfoApplicantClaim;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.product.ProductionLicenseApplicantClaimHandleVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 
 * @author ZhangHui 2015/5/29
 */
public interface ProductLicenseOfApplicantClaimHandleService extends BaseService<ProductionLicenseApplicantClaimHandle, 
		ProductionLicenseApplicantClaimHandleDAO>{

	/**
	 * 功能描述：查找一条未过期的企业申请认领qs的记录
	 * @param qs_id         // 需要申请认领qsId
	 * @param currentBusId  // 提出申请认领企业id
	 * @author ZhangHui 2015/6/1 
	 * @throws ServiceException 
	 */
	public ProductionLicenseApplicantClaimHandleVO findOfNotOverdue(Long qs_id, Long applicantBusId) throws ServiceException;

	/**
	 * 功能描述：查找一条未过期的企业申请认领qs的记录
	 * @param qs_no         // 需要申请认领qs号
	 * @param organization  // 提出申请认领企业组织机构
	 * @author ZhangHui 2015/6/19
	 * @throws ServiceException 
	 */
	public ProductionLicenseApplicantClaimHandleVO findOfNotOverdue(String qs_no, Long organization) throws ServiceException;
	
	/**
	 * 功能描述：新增一条企业申请认领qs号记录
	 * @author ZhangHui 2015/6/1
	 * @throws ServiceException 
	 */
	public void createNewRecord(ProductionLicenseInfoApplicantClaim proLic_applicant, Long applicantBusId, String userName) throws ServiceException, com.gettec.fsnip.fsn.exception.ServiceException;

	/**
	 * 功能描述：设置申请过期
	 * @author ZhangHui 2015/6/1
	 * @throws ServiceException 
	 */
	public void setApplicantOverdue(Long id) throws ServiceException;
	
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
	public void executeProcess(Long applicantId, Long qsId, boolean pass, String note, String applicant_bus_name, AuthenticateInfo info) 
			throws ServiceException;

	/**
	 * 功能描述：根据处理结果获取企业认领申请列表
	 * @author ZhangHui 2015/6/2
	 * @throws ServiceException 
	 */
	public List<ProductionLicenseApplicantClaimHandleVO> getListByByHandleResultOfPage(int handle_result, int page, int pageSize) 
			throws ServiceException;

	/**
	 * 功能描述：根据处理结果获取企业认领申请数量
	 * @author ZhangHui 2015/6/2
	 * @throws ServiceException 
	 */
	public long countByHandleResult(int handle_result) throws ServiceException;

	/**
	 * 功能描述：获取当前登录企业 qs认领申请处理消息数量
	 * @author ZhangHui 2015/11
	 * @throws ServiceException
	 */
	public long countWithoutOverdue(Long organization) throws ServiceException;

	/**
	 * 功能描述：获取当前登录企业 qs认领申请处理消息
	 * @author ZhangHui 2015/6/11
	 * @throws ServiceException
	 */
	public List<ProductionLicenseApplicantClaimHandleVO> getListWithoutOverdueByPage(
			Long organization, int page, int pageSize) throws ServiceException;
}