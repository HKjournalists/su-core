package com.gettec.fsnip.fsn.service.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.business.ProductionLicenseDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.model.product.ProductionLicenseInfoApplicantClaim;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface ProductionLicenseService extends BaseService<ProductionLicenseInfo, ProductionLicenseDAO>{

	/**
	 * 保存生产许可证信息（不处理图片），并维护 企业-qs 关系
	 * @param owerBusName 当前qs号真正的主企业名称
	 * @param executeClaimOperate 是否立即执行认领操作
	 * @return boolean
	 *           true   代表 当前操作下，允许修改生产许可证信息
	 *           false  代表 当前操作下，不允许修改生产许可证信息
	 * @author ZhangHui 2015/5/21
	 */
	public void save(ProductionLicenseInfo proLicInfo, String owerBusName) throws ServiceException;
	
	/**
	 * 功能描述：获取当前企业的所有qs号
	 * @author ZhangHui 2015/5/21
	 */
	public List<ProductionLicenseInfo> getListByBusId(Long bussinessId) throws ServiceException;

	/**
	 * 功能描述：根据qs号，获取当前一条详细的qs信息
	 * @author ZhangHui 2015/5/25
	 */
	public ProductionLicenseInfo findByQsno(String qsno) throws ServiceException;

	/**
	 * 背景：当qs号认领申请被审核通过后
	 * 功能描述：将申请的qs号信息同步到正常的qs号信息
	 * @author ZhangHui 2015/6/1
	 * @throws ServiceException 
	 */
	public void updateInfo(ProductionLicenseInfo orig_proLic, ProductionLicenseInfoApplicantClaim prolic_applicant) throws ServiceException;

	/**
	 * 功能描述：根据生产许可证编号，获取qs id
	 * @author ZhangHui 2015/6/4
	 * @throws ServiceException 
	 */
	public Long findIdByQsno(String qsno) throws ServiceException;

	/**
	 * 功能描述：新增一条qs号
	 * @author ZhangHui 2015/6/10
	 */
	public void createNewQsInfo(ProductionLicenseInfo proLicInfo) throws ServiceException;

	/**
	 * 根据产品id查找其所有已绑定且没过期的生产许可证信息
	 * @author longxianzhen  2015/06/26
	 */
	public List<ProductionLicenseInfo> getListByProId(Long proId)throws ServiceException;
}