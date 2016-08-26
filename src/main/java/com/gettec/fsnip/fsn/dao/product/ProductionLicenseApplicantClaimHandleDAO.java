package com.gettec.fsnip.fsn.dao.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductionLicenseApplicantClaimHandle;
import com.gettec.fsnip.fsn.vo.product.ProductionLicenseApplicantClaimHandleVO;

public interface ProductionLicenseApplicantClaimHandleDAO extends BaseDAO<ProductionLicenseApplicantClaimHandle>{

	/**
	 * 功能描述：查找一条未过期的企业申请认领qs的记录
	 * @param qs_id         // 需要申请认领qsId
	 * @param currentBusId  // 提出申请认领企业id
	 * @author ZhangHui 2015/6/1 
	 * @throws DaoException 
	 */
	public ProductionLicenseApplicantClaimHandleVO findOfNotOverdue(Long qs_id, Long applicantBusId) throws DaoException;
	
	/**
	 * 功能描述：查找一条未过期的企业申请认领qs的记录
	 * @param qs_no         // 需要申请认领qs号
	 * @param organization  // 提出申请认领企业组织机构
	 * @author ZhangHui 2015/6/19
	 * @throws ServiceException 
	 */
	public ProductionLicenseApplicantClaimHandleVO findOfNotOverdue(String qs_no, Long organization) throws DaoException;

	/**
	 * 功能描述：设置申请过期
	 * @author ZhangHui 2015/6/1
	 * @throws DaoException 
	 */
	public void setApplicantOverdue(Long id) throws DaoException;

	/**
	 * 功能描述：执行处理操作
	 * @param applicantId 当前正在审核的申请记录id
	 * @param pass
	 * 			true:  通过审核
	 * 			false: 未通过审核
	 * @param note 处理意见
	 * @author Zhanghui 2015/6/1
	 * @throws DaoException 
	 */
	public void executeProcess(Long applicantId, boolean pass, String note, String handler) throws DaoException;

	/**
	 * 功能描述：根据处理结果查询企业认领申请列表
	 * @param handle_result
	 * 				1 代表待处理
	 * 				2 代表审核通过
	 * 				4 代表未通过审核
	 * 				8 代表申请过期
	 * @author ZhangHui 2015/6/2
	 * @throws DaoException 
	 */
	public List<ProductionLicenseApplicantClaimHandleVO> getListByPage(int handle_result, int page, int pageSize) throws DaoException;

	/**
	 * 功能描述：根据处理结果查询企业认领申请数量
	 * @param handle_result
	 * 				1 代表待处理
	 * 				2 代表审核通过
	 * 				4 代表未通过审核
	 * 				8 代表申请过期
	 * @author ZhangHui 2015/6/2
	 * @throws DaoException 
	 */
	public long count(int handle_result) throws DaoException;

	/**
	 * 功能描述：获取当前登录企业 qs认领申请处理消息数量
	 * @author ZhangHui 2015/11
	 * @throws DaoException 
	 */
	public long countWithoutOverdue(Long organization) throws DaoException;

	/**
	 * 功能描述：获取当前登录企业 qs认领申请处理消息
	 * @author ZhangHui 2015/6/11
	 * @throws DaoException
	 */
	public List<ProductionLicenseApplicantClaimHandleVO> getListWithoutOverdueByPage(
			Long organization, int page, int pageSize) throws DaoException;

	/**
	 * 功能描述：根据qs_id和处理结果，获取所有申请记录ids
	 * @author ZhangHui 2015/6/18
	 * @throws DaoException
	 */
	public List<Long> getIdsByHandleResult(Long qs_id, int handle_result) throws DaoException;

	/**
	 * 功能描述：批量将idStrs申请认领设置为退回状态
	 * @author ZhangHui 2015/6/18
	 * @throws DaoException
	 */
	public void batchSetBackByQsidstrs(String idStrs, String back_msg, String handler) throws DaoException;
}