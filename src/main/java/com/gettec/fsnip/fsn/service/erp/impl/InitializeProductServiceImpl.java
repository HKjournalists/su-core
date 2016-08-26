package com.gettec.fsnip.fsn.service.erp.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.erp.InitializeProductDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.InitializeProduct;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.InitializeProductService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("initializeProductService")
public class InitializeProductServiceImpl extends BaseServiceImpl<InitializeProduct, InitializeProductDAO>  
		implements InitializeProductService{
	@Autowired private InitializeProductDAO initializeProductDAO;
	@Autowired private ProductService productService;
	
	@Override 
	public InitializeProductDAO getDAO() {
		return initializeProductDAO;
	}
	
	/**
	 * 初始化产品信息
	 * @param req
	 * @param resp
	 * @param initializeProduct
	 * @param model
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(InitializeProduct initializeProduct,Long organization) throws ServiceException{
		try {
			InitializeProduct orig_initializeProduct = findByProIdAndOrgId(initializeProduct.getProduct().getId(),organization);
			if(orig_initializeProduct == null){
				/* 1.1 初始化本地产品 */
				initializeProduct.setOrganization(organization);
				initializeProduct.setIsLocal(true);
				create(initializeProduct);
			}else{
				/* 1.2 初始化引进产品 */
				orig_initializeProduct.setFirstStorage(initializeProduct.getFirstStorage());
				orig_initializeProduct.setInspectionReport(initializeProduct.getInspectionReport());
				orig_initializeProduct.setSafeNumber(initializeProduct.getSafeNumber());
				orig_initializeProduct.setType(initializeProduct.getType());
				update(orig_initializeProduct);
			}
		} catch (Exception e) {
			throw new ServiceException("InitializeProductServiceImpl.save-->", e);
		}
	}
	
	//获取已初始化的商品
	@Override
	public PagingSimpleModelVO<InitializeProduct> getAllInitializeProduct(int page,int pageSize,Long organization) {
		try {
			PagingSimpleModelVO<InitializeProduct> result = new PagingSimpleModelVO<InitializeProduct>();
			result.setListOfModel(initializeProductDAO.getAllInitializeProduct(page,pageSize,organization));
			Long count = initializeProductDAO.getCountInitializeProduct(organization);
			result.setCount(count);
			return result;
			 
		} catch (Exception e) {
			return null;
		}
	}
	//加载库存已初始化且有库存量的商品
	//商品出库、商品调拨、出货单
	@Override
	public List<InitializeProduct> getAllOutInitializeProduct(Long organization) {
		try {
			return initializeProductDAO.getAllOutInitializeProduct(organization);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 初始化产品信息
	 * @param productId
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public InitializeProduct findByProIdAndOrgId(Long productId, Long organization) throws ServiceException {
		try {
			return initializeProductDAO.findByProIdAndOrgId(productId, organization);
		} catch (DaoException dex) {
			throw new ServiceException("InitializeProductServiceImpl.findByProIdAndOrgId()-->", dex.getException());
		}
	}
	
	@Override
	public List<InitializeProduct> getInitializeProduct(Long organization) {
		
		return initializeProductDAO.getInitializeProducts(organization);
	}

	/**
	 * 初始化产品信息
	 * @param barcode
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public InitializeProduct findByBarcodeAndOrgId(String barcode, Long organization) throws ServiceException {
		try {
			return initializeProductDAO.findByBarcodeAndOrgId(barcode, organization);
		} catch (DaoException dex) {
			throw new ServiceException("InitializeProductServiceImpl.findByBarcodeAndOrgId()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 根据企业organization 统计本地或引进的商品条数
	 */
	@Override
	public long countByOrganizationAndLocal(Long organization, boolean local, String configure)
			throws ServiceException {
		try{
			return initializeProductDAO.countConditon(getConfigure(organization, local, configure));
		}catch(DaoException daoe){
			throw new ServiceException("InitializeProductServiceImpl.countByOrganizationAndLocal() "+daoe.getMessage(),daoe);
		}
	}
	
	/**
	 * 根据企业organization 统计本地或引进的商品集合
	 */
	@Override
	public List<InitializeProduct> getByOrganizationAndLocal(Long organization,
			boolean local, String configure, int page, int pageSize)
			throws ServiceException {
		try{
			return initializeProductDAO.getByConditon(getConfigure(organization, local, configure), page, pageSize);
		}catch(DaoException daoe){
			throw new ServiceException("InitializeProductServiceImpl.getByOrganizationAndLocal() " + daoe.getMessage(), daoe);
		}
	}

	/**
	 * 根据kendoGrid中数据筛选条件拼接查询的sql语句
	 * @param organization 
	 * @param configure
	 * @param local  是否为本地商品，false为引进商品
	 * @return
	 * @throws ServiceException 
	 */
	private Map<String, Object> getConfigure(Long organization, boolean local, String configure) throws ServiceException{
		Object[] params = null;
		String new_configure = "";
		params = new Object[2];
		new_configure = " WHERE e.organization = ?1 and e.isLocal = ?2";
		params[0] = organization;
		params[1] =local;
		String filter[] = configure.split("@@");
		for(int i=0;i<filter.length;i++){
			String filters[] = filter[i].split("@");
			if(filters.length > 3){
				try {
					String config = splitJointConfigure(filters[0],filters[1],filters[2]);
					if(config==null){
						continue;
					}
					if(i==0){
						new_configure = new_configure + " AND " + config;
					}else{
						new_configure = new_configure +" AND " + config;
					}
				} catch (Exception e) {
				e.printStackTrace();
				}
			}
		}
	    Map<String, Object> map = new HashMap<String, Object>();
		map.put("condition", new_configure);
		map.put("params", params);
	    return map;
	}
	
	/**
	 * 分割页面的过滤信息
	 */
	private String splitJointConfigure(String field, String mark, String value) throws ServiceException{
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException("【Service-Error】数据筛选时，利用utf-8解码筛选条件时出现异常！",e);
		}
		if(field.equals("product_id")){
			return FilterUtils.getConditionStr("e.product.id",mark,value);
		}
		if(field.equals("product_name")){
			return FilterUtils.getConditionStr("e.product.name",mark,value);
		}
		if(field.equals("product_businessBrand_name")){
			return FilterUtils.getConditionStr("e.product.businessBrand.name",mark,value);
		}
		if(field.equals("product_format")){
			return FilterUtils.getConditionStr("format",mark,value);
		}
		if(field.equals("product_barcode")){
			return FilterUtils.getConditionStr("e.product.barcode",mark,value);
		}
		if(field.equals("product_cstm")){
			return FilterUtils.getConditionStr("e.product.cstm",mark,value);
		}
		if(field.equals("product_ingredient")){
			return FilterUtils.getConditionStr("e.product.ingredient",mark,value);
		}
		return null;
	}

	/**
	 * 删除/恢复 产品
	 * @param proId 产品id
	 * @param organization 组织机构id
	 * @param isDel<br>
	 * true: 已删除<br>
	 * false:未删除<br>
	 * @author ZhangHui 2015/4/14
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateDelFlag(Long proId, Long organization, boolean isDel) throws ServiceException {
		try {
			getDAO().updateDelFlag(proId, organization, isDel);
		} catch (DaoException e) {
			throw new ServiceException("InitializeProductServiceImpl.updateDelFlag()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 根据产品id,组织机构id,删除标记,引进标记，查找产品数量
	 * @param proId 产品id
	 * @param organization 组织机构id
	 * @param isDel<br>
	 * 			true: 已删除<br>
	 * 			false:未删除<br>
	 * @author ZhangHui 2015/4/14
	 */
	@Override
	public long count(Long proId, Long organization, boolean isDel) throws ServiceException {
		try {
			String condition = " WHERE e.product.id = ?1 AND e.organization = ?2 AND e.isDel = ?3";
			return getDAO().count(condition, new Object[]{proId, organization, isDel});
		} catch (JPAException e) {
			throw new ServiceException("InitializeProductServiceImpl.count()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 检查企业是否引进该产品
	 * @author ZhangHui 2015/4/15
	 */
	@Override
	public boolean checkLeadProduct(String barcode, Long organization) throws ServiceException {
		try {
			String condition = " WHERE e.product.barcode = ?1 AND e.organization = ?2 AND e.isLocal = '0' AND e.isDel = '0'";
			long count = getDAO().count(condition, new Object[]{barcode, organization});
			if(count>0){
				return true;
			}
			return false;
		} catch (JPAException e) {
			throw new ServiceException("InitializeProductServiceImpl.checkLeadProduct()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 引进已存在的产品
	 * @author ZhangHui 2015/4/15
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void leadProduct(String barcode, Long organization) throws ServiceException {
		try {
			InitializeProduct orig_iniPro = getDAO().findByBarcodeAndOrgId(barcode, organization);
			Product product = productService.findByBarcode(barcode);
			if(orig_iniPro == null){
				InitializeProduct initProduct = new InitializeProduct();
				initProduct.setIsLocal(false);
				initProduct.setOrganization(organization);
				initProduct.setProduct(product);
				initProduct.setIsDel(false);
				create(initProduct);
			}else{
				orig_iniPro.setIsDel(false);
				update(orig_iniPro);
			}
			/**
			 * 判断产品组织机构是否能修改
			 * 修改 LongXianZhen 2015/05/06
			 */
			boolean orgModifyFlag=productService.judgeProductOrgModify(product.getOrganization());
			if(orgModifyFlag){//可以则把产品组织机构改为该供应商组织机构
				product.setOrganization(organization);
				productService.update(product);
			}
		} catch (DaoException e) {
			throw new ServiceException("ProductServiceImpl.leadProduct()-->" + e.getMessage(), e.getException());
		}
	}
}
