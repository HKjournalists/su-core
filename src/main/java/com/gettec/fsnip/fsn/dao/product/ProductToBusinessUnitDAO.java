package com.gettec.fsnip.fsn.dao.product;

import java.util.List;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductToBusinessUnit;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;

public interface ProductToBusinessUnitDAO extends BaseDAO<ProductToBusinessUnit>{

	/**
	 * 背景：生产企业 报告录入界面，产品条形码下拉选择
	 * 功能描述：查找当前登录的生产企业的所有产品条形码
	 * @author ZhangHui 2015/6/5
	 */
	public List<ProductOfMarketVO> getListBarcodeByOrganization(Long organization) throws DaoException;

	/**
	 * 功能描述：根据产品 barcode 查找 企业-产品-qs 集合
	 * @author ZhangHui 2015/6/5
	 * @throws DaoException
	 */
	public List<BusinessUnitOfReportVO> getListByBarcode(String barcode) throws DaoException;

	/**
	 * 功能描述：根据产品id和企业id，查找一条 企业-产品-qs 关系
	 * @author ZhangHui 2015/6/4
	 * @throws DaoException 
	 */
	public ProductToBusinessUnit find(Long product_id, Long business_id) throws DaoException;

	/**
	 * 功能描述：根据产品条形码 和 生产许可证编码，查找一条生产企业信息
	 * @author ZhangHui 2015/6/4
	 * @throws DaoException
	 */
	public BusinessUnitOfReportVO getBusUnitOfReportVOByQsno(String barcode, String qsNo) throws DaoException;
	
	/**
	 * 背景：在报告录入界面
	 * 功能描述：根据生产企业名称，查找一条生产企业信息
	 * @author ZhangHui 2015/6/4
	 * @throws DaoException
	 */
	public BusinessUnitOfReportVO getBusUnitOfReportVOByBusname(String bus_name) throws DaoException;

	/**
	 * 背景：在报告录入界面
	 * 功能描述：根据生产企业名称和产品条形码，查找一条生产企业信息（包括生产许可证信息）
	 * @author ZhangHui 2015/6/4
	 * @throws ServiceException 
	 */
	public BusinessUnitOfReportVO getBusUnitOfReportVOByBusname(String bus_name, String barcode)
			throws DaoException;

	/**
	 * 功能描述：根据产品id和企业组织机构，查找qs号
	 * @author ZhangHui 2015/6/5
	 * @throws DaoException 
	 */
	public Long getQsId(Long product_id, Long organization) throws DaoException;

	/**
	 * 背景：在报告录入界面
	 * 功能描述：根据营业执照号，查找一条生产企业信息
	 * @author ZhangHui 2015/6/4
	 * @throws ServiceException 
	 */
	public BusinessUnitOfReportVO getBusUnitOfReportVOLicenseNo(String licenseNo)
			throws DaoException;

	/**
	 * 背景：在报告录入界面
	 * 功能描述：根据营业执照号和产品条形码，查找一条生产企业信息（必须存在 企业-产品-qs 关系）
	 * @author ZhangHui 2015/6/4
	 * @throws ServiceException 
	 */
	public BusinessUnitOfReportVO getBusUnitOfReportVOLicenseNo(String licenseNo,
			String barcode) throws DaoException;

	/**
	 * 功能描述：将企业-产品-qs号 设置为有效/无效
	 * @param effect
	 *          0 代表设置为无效
	 *          1 代表设置为有效
	 * @author Zhanghui 2015/6/15
	 * @throws DaoException 
	 */
	public void updateByEffect(long qs_id, long business_id, int effect)
			throws DaoException;

	/**
	 * 功能描述：除了当前qs号的主企业，将其他被授权企业-产品-qs号 设置为有效/无效
	 * @param effect
	 *          0 代表设置为无效
	 *          1 代表设置为有效
	 * @author Zhanghui 2015/6/15
	 * @throws DaoException 
	 */
	public void updateExOwnerbusByEffect(Long qs_id, Long owner_bus_id,
			int effect) throws DaoException;
}