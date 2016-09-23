package com.gettec.fsnip.fsn.service.product.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.QRCodeProductInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.KmsLabel;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.QRCodeProductInfo;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.market.ResourceTypeService;
import com.gettec.fsnip.fsn.service.product.KmsLabelService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.product.QRCodeProductInfoService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.util.QRCodeUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 二维码产品信息service层实现
 * @author TangXin
 *
 */
@Service(value="qRCodeProductInfoService")
public class QRCodeProductInfoServiceImpl extends BaseServiceImpl<QRCodeProductInfo,QRCodeProductInfoDAO>
	implements QRCodeProductInfoService{

	@Autowired private QRCodeProductInfoDAO qRCodeProductInfoDAO;
	@Autowired private ResourceService mkTestResourceService;
	@Autowired private ResourceTypeService resourceTypeService;
	@Autowired private KmsLabelService kmsLabelService;
	@Autowired protected BusinessUnitService businessUnitService;
	@Autowired protected ProductService productService;
	@Autowired private ServletContext servletContext;
	
	@Override
	public QRCodeProductInfoDAO getDAO() {
		return qRCodeProductInfoDAO;
	}

	/**
	 * 根据产品ID获取当前产品的二维码信息
	 * @param productIds
	 * @return QRCodeProductInfo
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	public QRCodeProductInfo findByProductId(Long productId) throws ServiceException{
		try{
			return getDAO().findByProductId(productId);
		}catch(DaoException daoe){
			throw new ServiceException("QRCodeProductInfoServiceImpl.findByProductId()->"+daoe.getMessage(),daoe);
		}
	}
	
	/**
	 * 保存二维码产品信息
	 * @param qrcodeProduct
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public QRCodeProductInfo save(QRCodeProductInfo qrcodeProduct) throws ServiceException{
		try{
			if(qrcodeProduct == null) return null;
			/*1.固定二维码图片类型为.png格式*/
			Resource resource = qrcodeProduct.getResource();
			resource.setType(resourceTypeService.findById(12L));
			Product product = qrcodeProduct.getProduct();
			/*2.处理产品标签信息*/
			KmsLabel orig_label = kmsLabelService.save(qrcodeProduct.getKmsLabel());
			qrcodeProduct.setKmsLabel(orig_label);
			/*3.处理二维码产品信息*/
			QRCodeProductInfo orig_instance = getDAO().findByProductId(product.getId());
			if(orig_instance == null){
				mkTestResourceService.create(resource);
				create(qrcodeProduct);
				orig_instance = qrcodeProduct;
			}else{
				Resource orig_res = orig_instance.getResource();
				orig_res.setFileName(resource.getFileName());
				orig_res.setName(resource.getFileName());
				orig_res.setUrl(resource.getUrl());
				orig_instance.setInnerCode(qrcodeProduct.getInnerCode());
				orig_instance.setKmsLabel(qrcodeProduct.getKmsLabel());
				orig_instance.setProductAreaCode(qrcodeProduct.getProductAreaCode());
				update(orig_instance);
			}
			return orig_instance;
		}catch(Exception e){
			throw new ServiceException("QRCodeProductInfoServiceImpl.save()->"+e.getMessage(),e);
		}
	}
	
	/**
	 * 根据组织机构id统计企业二维码产品数量
	 * @param organization
	 * @param configure
	 * @return
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	public long countByOrganization(Long organization, String configure) throws ServiceException{
		try{
			Map<String, Object> conMap = getConfigure(organization,configure);
			String condition = conMap.get("condition").toString();
			Object[] params = (Object[])conMap.get("params");
			return getDAO().count(condition, params);
		}catch(JPAException jpae){
			throw new ServiceException("QRCodeProductInfoServiceImpl.countByOrganization()->"+jpae.getMessage(),jpae);
		}
	}
	
	/**
	 * 根据企业组织机构id获取该企业的二维码产品列表
	 * @param organization
	 * @return List<QRCodeProductInfo>
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	public List<QRCodeProductInfo> getListByOrganization(Long organization, int page, int pageSize, String configure) 
			throws ServiceException{
		try{
			Map<String, Object> conMap = getConfigure(organization,configure);
			String condition = conMap.get("condition").toString();
			Object[] params = (Object[])conMap.get("params");
			return getDAO().getListByPage(page, pageSize, condition + " order by e.product.id desc ", params);
		}catch(JPAException jpae){
			throw new ServiceException("QRCodeProductInfoServiceImpl.getListByOrganization()->"+jpae.getMessage(),jpae);
		}
	}
	
	/**
	 * 根据企业组织机构id获取该企业下最大产品流水号
	 * @param organization
	 * @return
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	public long getMaxSerialNumberByOrganization(Long organization) throws ServiceException{
		try{
			return getDAO().getMaxSerialNumberByOrganization(organization);
		}catch(DaoException daoe){
			throw new ServiceException("QRCodeProductInfoServiceImpl.getMaxSerialNumberByOrganization()->"+daoe.getMessage(),daoe);
		}
	}
	
	/**
	 * 保存二维码产品信息
	 * @param qrcodeProduct
	 * @param curOrganziation
	 * @param newFlag
	 * @param info
	 * @return
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public QRCodeProductInfo save(QRCodeProductInfo qrcodeProduct, Long curOrganziation,
			boolean newFlag, AuthenticateInfo info) throws ServiceException{
		if(qrcodeProduct==null) return null;
		Product product = qrcodeProduct.getProduct();
		BusinessUnit busunit = product.getProducer();
		/*1.保存产品基本信息*/
		busunit = businessUnitService.save(busunit);
		product.setProducer(busunit);
		if(newFlag){
			/*获取当前企业产品的最大流水号*/
			long maxSN = this.getMaxSerialNumberByOrganization(curOrganziation);
			qrcodeProduct.setSerialNumber(maxSN+1);
			String barcode = QRCodeUtil.createBarCode(busunit.getId(),qrcodeProduct.getCityCode(), 
					qrcodeProduct.getProductAreaCode(), qrcodeProduct.getSerialNumber());
			product.setBarcode(barcode);
			Product orig_product = productService.getProductByBarcode(barcode);
			if(orig_product!=null){
				product.setId(orig_product.getId());
				//productService.updateProduct(product, busunit.getName(), curOrganziation);
			}else{
				//productService.createProduct(product, info.getOrganization(), busunit.getName(), curOrganziation);
			}
		}else{
			String barcode = QRCodeUtil.createBarCode(busunit.getId(),qrcodeProduct.getCityCode(), 
					qrcodeProduct.getProductAreaCode(), qrcodeProduct.getSerialNumber());
			product.setBarcode(barcode);
			//productService.updateProduct(product, busunit.getName(), curOrganziation);
		}
		/*创建二维码图片*/
		String logoPath = servletContext.getRealPath("/WEB-INF/qrcodelogo.png");
		Resource resource = QRCodeUtil.createQRCode(qrcodeProduct, logoPath);
		qrcodeProduct.setResource(resource);
		/*2.保存二维码产品信息*/
		qrcodeProduct.setProduct(product);
		qrcodeProduct = this.save(qrcodeProduct);
		return qrcodeProduct;
	}
	
	/**
	 * 验证产品内部码是否重复，针对同一个企业内部验证。
	 * @param innderCode
	 * @param organization
	 * @param productId
	 * @return true:验证通过  false：验证失败
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	public boolean validateInnerCode(String innerCode, Long organization, Long productId) throws ServiceException{
		try{
			return getDAO().validateInnerCode(innerCode, organization, productId);
		}catch(DaoException daoe){
			throw new ServiceException("QRCodeProductInfoServiceImpl.getMaxSerialNumberByOrganization()->"+daoe.getMessage(),daoe);
		}
	}
	
	/**
	 * 根据kendoGrid中数据筛选条件拼接查询的sql语句
	 * @param configure
	 * @return
	 * @throws ServiceException 
	 * @author TangXin
	 */
	private Map<String, Object> getConfigure(Long organizationId, String configure) throws ServiceException{
		Object[] params = null;
		String new_configure = "";
		params = new Object[1];
		new_configure = " WHERE e.product.organization = ?1 ";
		params[0] = organizationId;
		String filter[] = configure.split("@@");
		for(int i=0;i<filter.length;i++){
			String filters[] = filter[i].split("@");
			if(filters.length > 3){
				try {
					String config = splitJointConfigure(filters[0],filters[1],filters[2]);
					if(config==null){
						continue;
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
	 * @param field
	 * @param mark
	 * @param value
	 * @param isSon 
	 * @return
	 * @throws ServiceException
	 * @author TangXin
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
			return FilterUtils.getConditionStr("e.product.format",mark,value);
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
}
