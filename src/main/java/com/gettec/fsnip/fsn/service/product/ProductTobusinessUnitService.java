package com.gettec.fsnip.fsn.service.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.product.ProductToBusinessUnitDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.model.product.ProductToBusinessUnit;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.business.QsNoAndFormatVo;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;

public interface ProductTobusinessUnitService extends BaseService<ProductToBusinessUnit, ProductToBusinessUnitDAO>{

	/**
	 * 背景：生产企业 报告录入界面，产品条形码下拉选择
	 * 功能描述：查找当前登录的生产企业已经绑定过qs号的所有产品条形码
	 * @author ZhangHui 2015/6/5
	 */
	public List<ProductOfMarketVO> getListBarcodeByOrganization(Long userOrganization) throws ServiceException;

	/**
	 * 根据商品ID得到ProductNutrition 的List
	 * @param myRealOrgnizationId
	 * @param productId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws ServiceException
	 */
	List<ProductNutrition> getListOfProductNutritionByProductIdWithPage(
			Long productId, int page, int pageSize) throws ServiceException;

	/**
	 * 根据productId 取到 total
	 * @param productId
	 * @return Long
	 * @throws ServiceException
	 */
	long countByproductId(Long productId) throws ServiceException;

	/**
	 * 功能描述：新增/更新 企业-产品-qs号 关系
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/4
	 */
	public void save(Long product_id, Long bus_id, boolean qs_bind,	QsNoAndFormatVo qs_info) 
			throws ServiceException;

	/**
	 * 功能描述：根据产品 barcode 查找 企业-产品-qs 集合
	 * @author ZhangHui 2015/6/5
	 * @throws DaoException
	 */
	public List<BusinessUnitOfReportVO> getListByBarcode(String barcode) throws ServiceException;

	/**
	 * 加载该企业可以绑定的所有qs号
	 * @param organization
	 * @param firstpart qs号的前半部分
	 * @return List<Object>
	 */
	List<String> loadSonqsno(Long organization,Long myOrganization,String firstpart,Long formatId) throws ServiceException;

	/**
	 * 根据产品ID查询所有ProductToBusinessUnit
	 * @author LongXianZhen 2015/05/13
	 */
	List<ProductToBusinessUnit> getByProductId(Long proId)throws ServiceException;

	/**
	 * 背景：在报告录入界面
	 * 功能描述：根据产品条形码 和 生产许可证编码，查找一条生产企业信息
	 * @author ZhangHui 2015/6/4
	 * @throws ServiceException
	 */
	public BusinessUnitOfReportVO getBusUnitOfReportVOByQsno(String barcode, String qsNo) throws ServiceException;
	
	/**
	 * 背景：在报告录入界面
	 * 功能描述：根据生产企业名称和产品条形码，查找一条生产企业信息（包括生产许可证信息）
	 * @author ZhangHui 2015/6/4
	 * @throws ServiceException 
	 */
	public BusinessUnitOfReportVO getBusUnitOfReportVOByBusname(String bus_name,
			String barcode) throws ServiceException;

	/**
	 * 功能描述：根据产品id和企业组织机构，查找qs号
	 * @author ZhangHui 2015/6/5
	 * @throws ServiceException 
	 */
	public Long getQsId(Long product_id, Long organization) throws ServiceException;

	/**
	 * 背景：在报告录入界面
	 * 功能描述：根据营业执照号，查找一条生产企业信息
	 * @author ZhangHui 2015/6/4
	 * @throws ServiceException 
	 */
	public BusinessUnitOfReportVO getBusUnitOfReportVOLicenseNo(String licenseNo, String barcode)
			throws ServiceException;

	/**
	 * 功能描述：根据产品id和企业id查找一条  产品-企业-qs 关系
	 * @author ZhangHui 2015/6/9
	 * @throws ServiceException
	 */
	public ProductToBusinessUnit find(Long product_id, Long bus_id)
			throws ServiceException;

	
	/**
	 * 功能描述：将企业-产品-qs号 设置为有效/无效
	 * @param effect
	 *          0 代表设置为无效
	 *          1 代表设置为有效
	 * @author Zhanghui 2015/6/15
	 * @throws ServiceException 
	 */
	public void updateByEffect(long qs_id, long business_id, int effect)
			throws ServiceException;

	/**
	 * 功能描述：除了当前qs号的主企业，将其他被授权企业-产品-qs号 设置为有效/无效
	 * @param effect
	 *          0 代表设置为无效
	 *          1 代表设置为有效
	 * @author Zhanghui 2015/6/15
	 * @throws ServiceException 
	 */
	public void updateExOwnerbusByEffect(Long qs_id, Long owner_bus_id, int effect) throws ServiceException;

}