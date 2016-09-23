package com.lhfs.fsn.dao.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.lhfs.fsn.vo.TestBusinessUnitVo;
import com.lhfs.fsn.vo.atgoo.ProductVO;
import com.lhfs.fsn.vo.product.ProductIdAndNameVO;
import com.lhfs.fsn.vo.product.ProductInfoVOToPortal;
import com.lhfs.fsn.vo.product.ProductSortVo;
import com.lhfs.fsn.vo.product.ProductVOWda;
import com.lhfs.fsn.vo.product.SearchProductVO;

public interface ProductDao extends BaseDAO<Product> {

	public List<Object[]> findNutritionById(long id);
	
	public ProductInstance findInstance(String barcode, String batchSeriaNo);
	
	/**
	 * 功能描述：根据产品条形码查找产品信息
	 * @throws DaoException
	 * @author ZhangHui 2015/6/5
	 */
	public Product findByBarcode(String barcode) throws DaoException;
	
	public Product findByName(String sampleName);
	
	public long countOfHotProduct() throws DaoException;
	
	List<SearchProductVO> getListOfProductByCategory(String category, int page,
			int pageSize) throws DaoException;
	
	public int countProductByBusNameAndCategoryAndBrand(String enterpriseName,String category, Integer catLevel,String Brand,
			 String ordername, String ordertype, String nutriLabel);
	
	List<Object[]> getListOfProductByName2(String enterpriseName, String category, Integer catLevel, String brand,
			int page, int pageSize, String ordername, String ordertype, String nutriLabel)
			throws DaoException;
	
	List<Product> getProductByOrg(long org) throws DaoException;
	
	/**
	 * 根据商品名称或是条码，或是名称和条目得到相关商品
	 * @param name
	 * @param barcode
	 * @param orgId 
	 * @return List<Product>
	 * @throws DaoException
	 */
    public List<Product> findByNameAndBarcode(int page,int pageSize,String name, String barcode, Long orgId) throws DaoException;
    
    /**
     * 根据商品名称或是条码，或是名称和条目得到相关商品 的总条数
     * @param name 商品名称
     * @param barcode 商品barcode
     * @param orgId 
     * @return long
     * @throws DaoException
     */
    public long getListOfProductCount(String name, String barcode, long orgId) throws DaoException;
    
    /**
     * 根据批次或是生产日期范围，得到相关商品的总条数
     * @param batch 批次
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return long
     * @throws DaoException
     */
    public Long getListOfProductCountByBatchOrProductDate(String batch,
            String startTime, String endTime,long productId)throws DaoException;
    
    /**
     * 根据企业名称或是营业执照号 ，获取该企业下经营的所有产品
     * @param name 企业名称
     * @param licenseNo 营业执照号
     * @return List<Product>
     * @throws DaoException
     */
    public List<Product> loadProductListForBusinessUnit(String name,String licenseNo)
    		throws DaoException;
    
	public List<ProductInstance> getProductInstanceListById(int page, int pageSize, Long id) 
			throws DaoException;
	
	public Long getCountByproId(Long id) throws DaoException;
	
    public long countMarketProduct(Long organizationId, boolean haveReport,String name,String barcode) 
    		throws DaoException;
    
	List<ProductVOWda> getListOfMarketByMarketIdWithPage(Boolean flag,Long organizationId, boolean haveReport, int page,
			int pageSize,String name,String barcode) throws DaoException;
	
	long countNoneProLicinfoByOrganization(Long organization,String name,String barcode)throws DaoException;
	
	List<ProductVOWda> getListOfMarketNoneProlicinfoByOrgIdWithPage(Boolean flag,Long organization, int page, 
			int pageSize,String name,String barcode) throws DaoException;

	public List<ProductInstance> getListOfProductInstByBatchOrProductDate(
			int page, int pageSize, String batch, String startT,
			String endT, long productId) throws DaoException;

	List<SearchProductVO> getListOfHotProductWithPage(String condition, int page, int pageSize)
			throws DaoException;
	
	public List<SearchProductVO> getHotProductByProductId(Long productId) throws DaoException;
	/**
	 * 提供给食安监的接口：根据产品barcode 模糊查找相关batch（批次）
	 * @param barcode 产品条形码
	 * @return List<Object>
	 * @throws DaoException
	 */
	public List<Object> getBatchForBarcode(String barcode)throws DaoException;
	
	public List<Product> getListOfMarketAllProductsWithPage(
			Long organization, int page, int pageSize) throws DaoException;
	
	public long countMarketAllProduct(Long organization) throws DaoException;
	
	/**
	 * 根据产品组织机构  查找产品 并按报告数量降序
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public List<Product> getReportMaxProductByOrganization(Long productCount,Long organization)
			throws DaoException;
	
	public long getCountByBusNameOrLisNo(String name, String licenseNo) throws DaoException;

	List<ProductIdAndNameVO> searchBrandInfoByPQ(String queryName, String queryParam,
			boolean bCn);
	public List<ProductSortVo> getClassifyProductByid(long id,int pageSize,int page) throws DaoException;
	
	/**
	 * Portal 接口:根据产品id集合返回产品集合
	 * @author LongXianZhen 2015/06/25
	 */
	public List<ProductIdAndNameVO> getProductByProIds(String proIds)throws DaoException;

	/**
	 * 获取进口食品列表
	 * @author LongXianZhen 2015/07/01
	 */
	public List<ProductIdAndNameVO> getImportProductList(int page, int pageSize)throws DaoException;

	/**
	 * 获取进口食品数量
	 * @author LongXianZhen 2015/07/01
	 */
	public int getImportProductCount()throws DaoException;
	
	/**
	 * 获取进口食品专区报告数量
	 * @return
	 * @throws DaoException
	 */
	public int getImportProductReportCount()throws DaoException;
	/**
	 * 根据产品id获取 ProductVO 目前提供给爱特购系统使用
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public ProductVO findById_(Long id) throws DaoException;
	
	/**
	 * 根据产品 barcode 获取 ProductVO 目前提供给爱特购系统使用
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public ProductVO findByBarcode_(String barcode) throws DaoException;
	
	//跟据二维码查询有检测报告的产品
	public List<Object> findTestResultByBarcode(List<String> barcodes) throws DaoException;
	
	//根据产品id查询有报告产品
	public List<Object> findTestResultByProduct(List<String> productIds) throws DaoException;

	/**
	 * 根据条形码查询产品部分信息
	 * @author LongXianZhen 2015/08/06
	 */
	public ProductInfoVOToPortal getProductInfoByBarcode(String barcode)throws DaoException;
	
	/**
	 * 根据产品id查询产品基本信息
	 * @author longxianzhen 20150807
	 */
	public Product findProductBasicById(long id)throws DaoException;

	/**
	 * 根据产品id集合查找产品ProductSortVo信息
	 * @param proIds
	 * @author lxz 2015/09/26
	 */
	public List<ProductSortVo> getProductSortVoByProIds(String proIds)throws DaoException;
	/**
	 * 根据条形吗查询生产企业
	 * @param barcode
	 * @author wb 2015/11/13
	 */
	public List<TestBusinessUnitVo> getBusinessUnitVOList(String barcode, Integer page,
			Integer pageSize);

	/**
	 * 根据条形码和生产企业id获取生产企业基本信息
	 * @param barcode  条形码
	 * @param id  生产企业id
	 * @return 基本信息
	 */
	public TestBusinessUnitVo getBusinessUnitVO(String barcode, long id);
	
	
	
	/**
	 * 功能描述：根据二维码id获取产品条形码
	 * @throws DaoException
	 * @author liuyuanjing 2015/12/28
	 */
	public String findProductBarcodeByQRcode(String QRcode) throws DaoException;
	
	
	public String findProductIdByQRcode(String QRcode) throws DaoException ;
}