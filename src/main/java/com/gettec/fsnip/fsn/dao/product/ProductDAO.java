package com.gettec.fsnip.fsn.dao.product;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.vo.ProductStaVO;
import com.gettec.fsnip.fsn.vo.product.ProductLismVo;
import com.gettec.fsnip.fsn.vo.product.ProductManageViewVO;
import com.gettec.fsnip.fsn.vo.sales.DetailAlbumVO;
import com.lhfs.fsn.vo.product.ProductBarcodeToQRcodeVO;
import com.lhfs.fsn.vo.product.ProductListVo;
import com.lhfs.fsn.vo.product.ProductRiskVo;

public interface ProductDAO extends BaseDAO<Product>{
	public List<Product> findProducts(String name, Long businessUnitId, Long businessBrandId, List<Long> producerId, Long productGroupId, int pageSize, int page);

	public Product findByBarcode(String barcode) throws DaoException;

	/**
	 * 根据条件获取条形码集合有分页处理
	 * @param condition 查询条件 为all时查询所有
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws DaoException
	 */
	public List<String> getBarcodeListByCondition(String condition,int page,int pageSize) throws DaoException;

	public long countByCondition(Map<String, Object> map) throws DaoException;

	public List<Product> getListOfProductByConditionWithPage(
			Map<String, Object> map, int page, int pageSize) throws DaoException;

	public List<Product> getListOfProductByConditionOfSonWithPage(
			Map<String, Object> map, int page, int pageSize) throws DaoException;

	public List<Product> searchProductListByName(String name)throws DaoException;

	public long getCountByName(String name)throws DaoException;
	
	public List<Product> getAllProductsByOrg(long organization);
	public Product getAllProductsByOrgandid(long organization,long id);
	/**
	 * 根据条件（产品名称或条形码）分页查询某个企业的产品列表
	 * @param bu 生产企业
	 * @param page 第几页
	 * @param pageSize 每页显示条数
	 * @param productName 产品名称
	 * @param barcode 条形码
	 * @return List<Product>
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	public List<Product> getProListByBusiness(BusinessUnit bu, int page,
			int pageSize, String productName, String barcode)throws DaoException;
	/**
	 * 根据条件查询某个企业下产品总数
	 * @param organizationId 企业组织机构ID
	 * @param productName 产品名称
	 * @param barcode 条形码
	 * @return Long
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	public Long getProductStaCountByConfigure(Long organizationId,
			String productName, String barcode)throws DaoException;

	public List<Product> getListByNameAndFormat(String name, String format) throws DaoException;

	public List<Product> getListByOtherNameAndFormat(String otherName,
			String format) throws DaoException;

	public List<Product> getListByOtherNameAndPDFformat(String otherName,
			String pdfFormat, boolean isObscure) throws DaoException;

	public long countByHotWord(Long organization, String hotWord) throws DaoException;

	public List<Product> getListByHotWordWithPage(Long organization,
			String hotWord, int page, int pageSize) throws DaoException;
	
	public List<Product> getListByStorageInfo(Long organization) throws DaoException;
	
	public List<Product> getListProductByIds(List<Long> ids);

	public List<Product> getAllNotLocalProduct(int page, int size,Long organization) throws DaoException;

	public List<Product> getAllLocalProduct(int page, int size,Long organization) throws DaoException;

	public Long getCountOfAllLocalProduct(Long organization) throws DaoException;

	public Long getCountOfAllNotLocalProduct(Long organization) throws DaoException;

	public Long findByBarcodeAndBrandNameAndBusunitId(String barcode,
			String brandName, Long bussunitId) throws DaoException;

	List<Product> getListByBarcode(String barcode) throws DaoException;

	/**
	 * 查询所有条形码
	 * @return List<String>
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	public List<String> getAllBarcode()throws DaoException;
	public Product findByName(String sampleName) throws DaoException;
	
	/**
	 * 根据组织机构ID查询一个企业下的所有产品数量
	 * @param organization 企业组织机构ID
	 * @return Long
	 * @author LongXianZhen
	 */
	public Long getAllProCountByOrganization(Long organization)throws DaoException;

	
	public Long productCount() throws DaoException;
	
	public List<ProductListVo> getProductList(Long type,int pageSize,int page) throws DaoException;
	
	public List<ProductRiskVo> riskBillboard(String type,int pageSize,int page) throws DaoException;
	
	public String productCode(String name) throws DaoException;
	
	public int countriskBill(String code) throws DaoException ;


	/**
	 * 根据产品条形码获取产品id
	 * @author ZhangHui 2015/4/10
	 */
	public Long getIdByBarcode(String barcode) throws DaoException;

	public List<ProductManageViewVO> getLightProductVOsByPage(int page,
			int pageSize, String condition, String condition_barnd, Long organization, Long fromBusId, boolean isDel) throws DaoException;
	
	public List<ProductManageViewVO> getLightProductVOsByPage(int page,
			int pageSize, String condition, String condition_barnd) throws DaoException;
	
	/**
	 * 经销商只能加载出自己的产品条形码
	 * @author HuangYog
	 * Create date 2015/04/13
	 */
	public List<String> getAllBarcode(Long myOrg)throws DaoException ;

	/**
	 * 获取轻量级产品信息(包括引进产品)
	 * @author ZhangHui 2015/4/14
	 */
	List<ProductManageViewVO> getAllLightProductVOsByPage(int page,
			int pageSize, String condition, String condition_barnd,
			Long organization, Long fromBusId, boolean isDel)
			throws DaoException;

	/**
	 * 获取轻量级产品数量(包括引进产品)
	 * @author ZhangHui 2015/4/14
	 */
	long countAllProduct(String condition, String condition_barnd,
			Long organization) throws DaoException;

	boolean checkExistBarcode(String barcode) throws DaoException;
	
	public List<Product> getproductList(int pageSize,int page) throws DaoException;

	List<DetailAlbumVO> getProductAlbums(Long organization, int page, int pageSize, String cut) throws DaoException;

	/**
	 * 判断产品组织机构是否能修改
     * @author LongXianZhen	2015/05/06
	 */
	public boolean judgeProductOrgModify(Long organization)throws DaoException;

	/**
	 * 功能描述：根据产品条形码查找一条已经被生产企业认领的产品信息（如果没有被认领，则返回null）
     * @author ZhangHui 2015/06/04
	 * @throws DaoException 
	 */
	public Product findByBarcodeOfHasClaim(String barCode) throws DaoException;

	/**
	 * 功能描述：检查产品有无被生产企业认领
	 * @return  true  代表已经被生产企业认领
	 * 			false 代表没有没生产企业认领
	 * @throws DaoException 
     * @author ZhangHui 2015/06/05
	 */
	public boolean checkClaimOfProduct(Long id) throws DaoException;
	
	/**
	 * 根据产品id查询产品轻量级分装
	 * @param productId
	 * @author longxianzhen 2015/08/03
	 */
	public ProductManageViewVO findByProductManageViewVOByProId(Long productId)throws DaoException;
	
	/**
	 * 根据产品id集合查找产品集合 
	 * @author longxianzhen 2015/08/07
	 */
	public List<Product> getProListByCondition(String configure)throws DaoException;
	
	
	/**
	 * 根据相应属性验证对象是否存在
	 * @param businessBrand
	 * @return
	 */
	public Product checkProduct(Product product) throws ServiceException;
	/**
	 * 查询企业下的产品信息
	 * @param businessId 企业ID
	 * @param productName 产品名称
	 * @param barcode   产品条形码
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @param page     
	 * @param pageSize
	 * author:wubiao
	 * date : 2015.12.17 16:05
	 * @return
	 */
	public List<ProductStaVO> getProductStaListByConfigureData(Long businessId,
			String productName, String barcode, String startDate,
			String endDate, int page, int pageSize);
	/**
	 * 查询企业下的产品信息
	 * @param businessId 企业ID
	 * @param productName 产品名称
	 * @param barcode   产品条形码
	 * author:wubiao
	 * date : 2015.12.17 16:05
	 * @return
	 */
	public Long getProductStaCountByConfigureData(Long businessId,
			String productName, String barcode);
	
	
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
	public List<ProductBarcodeToQRcodeVO> getBarcodeToQRcode()throws DaoException ;
	
	/**
	 * 删除产品条形码与二维码的对应关系
	 * @param id
	 * @return
	 * @throws DaoException
	 * @author lyj  2016-01-07
	 */
	public boolean deleteBarcodeToQRcode(Long id)throws DaoException;

	/**
	    * 根据条形码查询产品信息以及生产厂商信息
	    * @param barcode
	    * @param model
	    * @return
	    */
	public ProductLismVo getByBarcodeProList(String barcode);

	
	public int updateProductCertByBarcode(String barcode,int cert);
	/**
	    * 根据条形码获取产品ID
	    * @param barcode
	    * @param model
	    * @return
	    */
	public Long getByBarcodeProduct(String barcode);
}

