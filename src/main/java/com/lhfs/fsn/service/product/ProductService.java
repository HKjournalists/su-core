package com.lhfs.fsn.service.product;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.NutritionReport;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.lhfs.fsn.dao.product.ProductDao;
import com.lhfs.fsn.vo.atgoo.ProductVO;
import com.lhfs.fsn.vo.product.ProductIdAndNameVO;
import com.lhfs.fsn.vo.product.ProductInfoVO;
import com.lhfs.fsn.vo.product.ProductInfoVOToPortal;
import com.lhfs.fsn.vo.product.ProductVOWda;
import com.lhfs.fsn.vo.product.SearchProductVO;
import com.lhfs.fsn.web.controller.RESTResult;

public interface ProductService extends BaseService<Product, ProductDao> {
	
	public Product findProductById(long id);
	
	public NutritionReport findNutritionById(long id);
	
	public void completeProduct(Product prod);

	long countOfHotProduct() throws ServiceException;

	/**
	 * 给portal提供接口：已知产品类别，查找报告最多的若干个产品
	 * @param category 
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws ServiceException
	 */
	List<SearchProductVO> getListOfProductByCategory(String category, int page,
			int pageSize) throws ServiceException;

	/**
     * 给portal提供接口：已知企业名称，按产品名称模糊查询产品
     * @param name
     * @param name 
     * @param page
     * @param pageSize
     * @return List<Object[]>
     * @throws ServiceException
     */
	List<Object[]> getListOfProductByName(String enterpriseName,String category,Integer catLevel,String brand,
			int page, int pageSize, String ordername, String ordertype, String nutriLabel) throws ServiceException;

	public Product findProductById(long id, int proImgWidth, int proImgHeight, 
			int certImgWidth, int certImgHeight, int docImgWidth, int docImgHeight);

	/**
	 * 给食安监提供的根据产品id 得到产品基本信息和产品认证
	 * @param id
	 * @return ProductInfoVO
	 */
    public ProductInfoVO getProductInfoAndCertById(Long id) throws ServiceException;

    /**
     * 根据传来的产品barcode 获取product的 商品 id,名称、商标 和 图片
     * @param barcode
     * @return Map<String, String>
     * @throws ServiceException
     */
	public Map<String, String> getProductInfoSomeByBarcode(String barcode) throws ServiceException;

	/**
	 * 根据商品名称或是条码，或是名称和条目得到相关商品
	 * @param name 商品名称
	 * @param barcode 商品barcode
	 * @param orgId 
	 * @return List<ProductInfoVO> 
	 * @throws ServiceException
	 */
    public List<Product> getListOfProduct(int page,int pageSize,String name, String barcode, Long orgId) throws ServiceException;

    /**
     * 根据商品名称或是条码，或是名称和条目得到相关商品 的总条数
     * @param name 商品名称
     * @param barcode 商品barcode
     * @param orgId 
     * @return long 
     * @throws ServiceException
     */
    public long getListOfProductCount(String name, String barcode, long orgId) throws ServiceException;

    /**
     * 根据批次或是生产日期范围，得到相关商品的总条数
     * @param batch 批次
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return long
     * @throws ServiceException
     */
    public Long getListOfProductCountByBatchOrProductDate(String batch,String startTime, String endTime,long productId) throws ServiceException;
    
    /**
     * 根据批次或是生产日期范围，得到相关商品
     * @param page 当前页
     * @param pageSize 显示条数
     * @param batch 批次
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<ProductInfoVO>
     * @throws ServiceException
     */
    public List<ProductInfoVO> getListOfProductByBatchOrProductDate(int page,
            int pageSize, String batch, String startTime, String endTime,long productId)throws ServiceException;


    /**
     * 根据企业名称或是营业执照号 ，获取该企业下经营的所有产品
     * @param name
     * @param licenseNo
     * @return List<ProductInfoVO>
     * @throws ServiceException
     */
    List<ProductInfoVO> loadProductListForBusinessUnit(String name, String licenseNo)throws ServiceException;

	public List<ProductInstance> getProductInstanceListById(int page, int pageSize, Long id) throws ServiceException;

	public Long getCountByproId(Long id) throws ServiceException;

    public long countMarketProduct(Long organizationId, boolean isHaveReport,String name,String barcode) throws ServiceException;
    
	List<ProductVOWda> getListOfMarketByMarketIdWithPage(Boolean flag,Long busId, boolean isHaveReport, int page,
			int pageSize,String name,String barcode) throws ServiceException;

	List<ProductVOWda> getListOfMarketNoneProlicinfoByOrgIdWithPage(Boolean flag,Long organization, int page, int pageSize,String name,String barcode) throws ServiceException;

	long countNoneProlicinfoByOrgId(Long organization,String name,String barcode) throws ServiceException;

	public List<SearchProductVO> getHotProductByProductId(Long productId) throws ServiceException;

	List<SearchProductVO> getListOfHotProductWithPage(String productIds, int page, int pageSize)
			throws ServiceException;
	/**
	 * 食安监接口：根据产品的barcode 查找相关的batch
	 */
	public List<Object> getBatchForBarcode(String barcode)throws ServiceException;

	public List<Product> getListOfMarketAllProductsWithPage(
			Long organization, int page, int pageSize) throws ServiceException;
	//cxl
	public long countMarketAllProduct(Long organization) throws ServiceException;

	/**
	 * 根据产品条形码查找该产品
	 * @param barcode 产品条形码
	 * @return Product
	 */
	public Product findProductByBarcode(String barcode)throws ServiceException;

	public long getCountByBusNameOrLisNo(String name, String licenseNo) throws ServiceException;

	RESTResult<ProductIdAndNameVO> searchProductInfoByPQ(String queryName,
			String queryParam);
	
	/**
	 * Portal 接口:根据产品id集合返回产品集合
	 * @author LongXianZhen 2015/06/25
	 */
	public List<ProductIdAndNameVO> getProductByProIds(String ids)throws ServiceException;

	/**
	 * 获取进口食品列表
	 * @author LongXianZhen 2015/07/01
	 */
	public List<ProductIdAndNameVO> getImportProductList(int page, int pageSize)throws ServiceException;

	/**
	 * 获取进口食品数量
	 * @author LongXianZhen 2015/07/01
	 */
	public int getImportProductCount()throws ServiceException;
	/**
	 * 获取进口食品专区报告数量
	 * @return
	 * @throws ServiceException
	 */
	public int getImportProductReportCount() throws ServiceException;

	/**
	 * 根据产品id获取 ProductVO 目前提供给爱特购系统使用
	 * @param id 产品id
	 * @return
	 * @throws ServiceException
	 */
	ProductVO findById_(Long id) throws ServiceException;

	/**
	 * 根据产品 barcode 获取 ProductVO 目前提供给爱特购系统使用
	 * @param barcode 产品条形码
	 * @return
	 * @throws ServiceException
	 */
	ProductVO findByBarcode_(String barcode) throws ServiceException;
	
    public List findTestResultByBarcode(List<String> barCodes) throws ServiceException;
	
	
	public List findTestResultByProductIds(List<String> products) throws ServiceException;

	/**
	 * 根据条形码查询食品详细信息
	 * @author LongXianZhen 2015/08/06
	 */
	public ProductInfoVOToPortal getProductInfoByBarcode(String barcode)throws ServiceException;
	
	
	/**
	 * 功能描述：根据二维码id获取产品条形码
	 * @throws DaoException
	 * @author liuyuanjing 2015/12/28
	 */
	public String findProductBarcodeByQRcode(String QRcode)  throws ServiceException;
	

	/**
	 * 功能描述：根据二维码id获取产品id
	 * @throws DaoException
	 * @author liuyuanjing 2016/1/20
	 */
	public String findProductIdByQRcode(String QRcode) throws ServiceException;
}
