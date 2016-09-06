package com.gettec.fsnip.fsn.service.product;

import java.util.List;

import net.sf.json.JSONObject;

import com.gettec.fsnip.fsn.dao.product.ProductDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductRecommendUrl;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.lhfs.fsn.vo.SampleVO;
import com.gettec.fsnip.fsn.vo.product.ProductLismVo;
import com.gettec.fsnip.fsn.vo.product.ProductManageViewVO;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;
import com.gettec.fsnip.fsn.vo.sales.DetailAlbumVO;
import com.lhfs.fsn.vo.product.ProductBarcodeToQRcodeVO;
import com.lhfs.fsn.vo.product.ProductListVo;
import com.lhfs.fsn.vo.product.ProductRiskVo;


public interface ProductService extends BaseService<Product, ProductDAO>{
	public boolean checkExistBarcode(String barcode) throws ServiceException;
	
	public Product eagerFindById(Long id) throws ServiceException;

	/**
	 * 根据条件获取条形码集合有分页处理
	 * @param condition
	 * @param page
	 * @param pageSize
	 * @return List<String>
	 * @throws ServiceException
	 * @author longxianzhen
	 */
	public List<String> getBarcodeListByCondition(String condition,int page,int pageSize)throws ServiceException;

	public long count(Long organization, String configure) throws ServiceException;
	public long count(String configure) throws ServiceException;
	
	public Product findByProductId(Long id,String identify) throws ServiceException;

	public List<Product> getProductListByConfigWithPage(String configure,
			int page, int pageSize)throws ServiceException;

	public long allCountByConfig(String configure)throws ServiceException;

	public List<Product> searchProductListByName(String name)throws ServiceException;

	public long getCountByName(String name)throws ServiceException;

	public List<Product> getListByNameAndFormat(String name, String format) throws ServiceException;

	public List<Product> getListByOtherNameAndFormat(String name, String format) throws ServiceException;

	public List<Product> getListByOtherNameAndPDFformat(String name, String format, boolean isObscure) throws ServiceException;

	public long countByHotWord(Long organization, String hotWord) throws ServiceException;

	public List<Product> getListByHotWordWithPage(Long organization,
			int page, int pageSize, String hotWord) throws ServiceException;

	public Product getProductByBarcode(String barcode) throws ServiceException;

	public List<Product> getAllLocalProduct(int page, int size,Long organization) throws ServiceException;

	public List<Product> getAllNotLocalProduct(int page, int size,Long organization) throws ServiceException;

	public Long getCountOfAllLocalProduct(Long organization) throws ServiceException;

	public Long getCountOfAllNotLocalProduct(Long organization) throws ServiceException;
	
	public List<Product> getAllProductsByOrg(long organization);

	/**
	 * 背景：产品新增/编辑页面
	 * 功能描述：保存产品
	 * @param current_business_name 当前正在执行产品新增操作的企业名称
	 * @param organization          当前正在执行产品新增操作的企业组织机构id
	 * @param isNew 
	 * 			true  代表 新增
	 * 			false 代表 更新
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/3
	 */
	public void saveProduct(Product product, String current_business_name, Long organization, boolean isNew)
			throws ServiceException;

	public Long findByBarcodeAndBrandNameAndBusunitId(String barcode,
			String name, Long bussunitId) throws ServiceException;

	boolean createProduct(JSONObject productVO, Long organization);

	
	/**
	 * 查询所有的条形码
	 * @return List<String>
	 * @throws ServiceException
	 */
	public List<String> getAllBarcode()throws ServiceException;
		
	public Product findByBarcode(String barcode) throws ServiceException;
		
	public Product findByName(String sampleName) throws ServiceException;
	
	public ResultVO recalculatedNutri(Long productID) throws ServiceException;
	
	public Long productCount() throws ServiceException;
	
	public List<ProductListVo> getProductList(Long type,int pageSize,int page) throws ServiceException;
    
    public List<ProductRiskVo> riskBillboard(String type,int pageSize,int page) throws ServiceException;
 	
	public String productCode(String name) throws ServiceException;
	
	public int countriskBill(String code) throws ServiceException ;
	
	/**
	 * 根据产品条形码查询产品id
	 * @author ZhangHui 2015/4/10
	 */
	public Long getIdByBarcode(String barcode) throws ServiceException;

	/**
	 * 根据LIMS传过来的样品信息 新增产品
	 * @param sample
	 * @param organizationID
	 * @return Product
	 * @author LongXianZhen
	 */
	public Product saveProduct(SampleVO sample, Long organizationID);


	/**
	 * 按条件查找产品
	 * @author ZhangHui 2015/4/10
	 */
	public List<Product> getListByConfigure(String configure, Object[] params) throws ServiceException;

	/**
	 * 获取轻量级产品信息
	 * @author ZhangHui 2015/4/11
	 */
	List<ProductManageViewVO> getLightProductVOsByPage(Long organization,
			String configure, int page, int pageSize, Long fromBusId, boolean isDel) throws ServiceException;
	
	/**
	 * 获取轻量级产品信息
	 * @author ZhangHui 2015/4/11
	 */
	List<ProductManageViewVO> getLightProductVOsByPage(
			String configure, int page, int pageSize) throws ServiceException;
	
	/**
	 * 经销商只能加载出自己的产品条形码
	 * @author HuangYog
	 * Create date 2015/04/13
	 */
	public List<String> getAllBarcode(Long myOrg)throws ServiceException;

	/**
	 * 获取产品信息(包括引进产品)
	 * @author ZhangHui 2015/4/14
	 */
	public List<ProductManageViewVO> getAllLightProductVOsByPage(
			Long currentUserOrganization, String configure, int page,
			int pageSize, Long fromBusId, boolean isDel) throws ServiceException;

	/**
	 * 获取产品数量(包括引进产品)
	 * @author ZhangHui 2015/4/14
	 */
	public long countAllProduct(Long currentUserOrganization, String configure) throws ServiceException;

	/**
	 * 获取产品信息和认证信息（轻量级）
     * @author tangxin	2015/04/15
	 */
	ProductManageViewVO getProductAndCert(Long productId,Long toBusId,Long fromBusId) throws ServiceException;
	
	public List<Product> getproductList(int pageSize,int page) throws ServiceException;
	
	/**
	 * 判断产品组织机构是否能修改
     * @author LongXianZhen	2015/05/06
	 */
	public boolean judgeProductOrgModify(Long organization)throws ServiceException;

	List<DetailAlbumVO> getProductAlbums(Long organization, int page,
			int pageSize, String cut) throws ServiceException;

	/**
	 * 保存营养标签
	 * @param orig_product 必须是从数据库中查出来的对象
	 * @author ChenXiaolin
	 * 最后更新者：ZhangHui 2015/6/3
	 * 更新内容：计算营养标签后，增加持久到数据库操作
	 */
	public void saveNutriLabel(Product orig_product);

	/**
	 * 功能描述：根据产品条形码查找一条已经被生产企业认领的产品信息（如果没有被认领，则返回null）
     * @author ZhangHui 2015/06/04
	 * @throws ServiceException 
	 */
	public Product findByBarcodeOfHasClaim(String barCode) throws ServiceException;

	/**
	 * 功能描述：检查产品有无被生产企业认领
	 * @return  true  代表已经被生产企业认领
	 * 			false 代表没有没生产企业认领
	 * @throws ServiceException 
     * @author ZhangHui 2015/06/05
	 */
	public boolean checkClaimOfProduct(Long id) throws ServiceException;

	/**
	 * 背景：报告录入页面
	 * 功能描述：保存产品
	 * @param current_business_id 当前正在执行产品新增/更新操作的企业id
	 * @param myOrganization      当前正在执行产品新增操作的企业组织机构id
	 * @param can_edit_qs         
	 * 				true  代表前台允许用户编辑当前产品的qs号
	 * 				false 代表前台不允许用户更改当前产品的qs号
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/5
	 * @return 
	 */
	public ProductOfMarketVO saveProduct(ProductOfMarketVO product_vo, Long current_business_id, 
			Long organization, boolean can_edit_qs) throws ServiceException;
	
	/**
	 * 根据相应属性验证对象是否存在
	 * @param businessBrand
	 * @return
	 */
	public Product checkProduct(Product product) throws ServiceException;
    /**
     * 描述：产品销售方，添加推荐url
     * @param rulvo  保存url数据对象
     * @return
     */
	public boolean saveProUrl(ProductRecommendUrl rulvo);
    /**
     * 描述：删除推荐的url
     * @param id
     * @return
     */
	public boolean delUrl(Long id);
	
	public void updateSpeicalProduct(long productId,boolean isSpeicalProduct);
	
	
	/**
	 * 建立产品条形码和二维码id对应关系
	 * @param barcode
	 * @param QRNum
	 * @author liuyuanjing
	 * date:2015-12-26
	 */
	public boolean setBarcodeToQRcode(String barcode ,Long productID,String QRStart,String QREnd);
	
	
	
	
	/**
	 * 获取产品条形码和二维码id对应关系
	 * 
	 * @param barcode
	 * @param QRNum
	 * @author liuyuanjing date:2015-12-20
	 */
	public List<ProductBarcodeToQRcodeVO> getBarcodeToQRcode()throws ServiceException;
	
	
	
	
	/**
	 * 删除产品条形码与二维码的对应关系
	 * @param id
	 * @return
	 * @throws DaoException
	 * @author lyj  2016-01-07
	 */
	public boolean deleteBarcodeToQRcode(Long id)throws ServiceException;

	/**
	    * 根据条形码查询产品信息以及生产厂商信息
	    * @param barcode
	    * @param model
	    * @return
	    */
	public ProductLismVo getByBarcodeBusProLims(String barcode)throws ServiceException;
	
	public int updateProductCertByBarcode(String barcode,int cert);
	public Product getAllProductsByOrgandid(long organization,long id);
    /**
     * 根据条形码获取产品ID
     * @param barcode
     * @return
     */
	public Long getByBarcodeProduct(String barcode);


}