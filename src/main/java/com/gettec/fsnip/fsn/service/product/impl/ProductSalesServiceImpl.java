package com.gettec.fsnip.fsn.service.product.impl;



import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.ProductDAO;
import com.gettec.fsnip.fsn.dao.product.ProductSalesDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.Area;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductSales;
import com.gettec.fsnip.fsn.service.product.ProductSalesService;
import com.gettec.fsnip.fsn.util.FilterUtils;




/**
 * ProductPoll service implementation
 * 
 * @author 
 */
@Service(value="productSalesService")
public class ProductSalesServiceImpl implements ProductSalesService{

	@SuppressWarnings("unused")
	private final static Logger logger = Logger.getLogger(ProductSalesServiceImpl.class);
	
	@Autowired
	protected ProductSalesDAO productSalesDAO;
	@Autowired
	protected ProductDAO productDAO;

	@Override
	public List<ProductSales> getListByProIdPage(int page, int pageSize,
			String configure, Long productId ,Long org) throws ServiceException {
		List<ProductSales> productSales=null;
		try {
			productSales = productSalesDAO.getListByProIdPage(page,pageSize,getConfigure(org,configure,productId));			
		} catch (DaoException dex) {
             throw new ServiceException(dex.getMessage(),dex.getException());
		}
		return productSales;
	}

	@Override
	public Long getCountByproId(Long productId,Long org,String configure) throws ServiceException {
		Long count=0L;
		try {
			count=productSalesDAO.getCountByproId(getConfigure(org,configure,productId));
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
		return count;
	}

	@Override
	public List<Area> getAreaByLevel(int level) throws ServiceException {
		List<Area> area=null;
		try {
			area = productSalesDAO.getAreaByLevel(level);			
		} catch (DaoException dex) {
             throw new ServiceException(dex.getMessage(),dex.getException());
		}
		return area;
	}

	@Override
	public List<Area> getMunicipalityByparentId(int parentId)
			throws ServiceException {
		List<Area> area=null;
		try {
			area = productSalesDAO.getMunicipalityByparentId(parentId);			
		} catch (DaoException dex) {
             throw new ServiceException(dex.getMessage(),dex.getException());
		}
		return area;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void addProductSales(ProductSales productSales, Long org)
			throws ServiceException {
		try {
			Product pro=productDAO.findById(productSales.getProductId());
			productSales.setProduct(pro);
			productSales.setOrganizationId(org);
			productSalesDAO.persistent(productSales);
		} catch (JPAException dex) {
			throw new ServiceException(dex.getMessage(),dex.getException());
		}
	}

	@Override
	public ProductSales getById(Long id) throws ServiceException {
		try {
			return productSalesDAO.findById(id);
		} catch (JPAException dex) {
			throw new ServiceException(dex.getMessage(),dex.getException());
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void editProductSales(ProductSales productSales)
			throws ServiceException {
		try {
			ProductSales oProSales=productSalesDAO.findById(productSales.getId());
			oProSales.setBatchSerialNo(productSales.getBatchSerialNo());
			oProSales.setTargetCustomer(productSales.getTargetCustomer());
			oProSales.setSalesQuantity(productSales.getSalesQuantity());
			oProSales.setSalesTerritory(productSales.getSalesTerritory());
			oProSales.setSalesDate(productSales.getSalesDate());
			oProSales.setpAreaId(productSales.getpAreaId());
			oProSales.setmAreaId(productSales.getmAreaId());
			oProSales.setcAreaId(productSales.getcAreaId());
			productSalesDAO.merge(oProSales);
		} catch (JPAException dex) {
			throw new ServiceException(dex.getMessage(),dex.getException());
		}
		
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteById(Long id) throws ServiceException {
		try {
			ProductSales pSales=productSalesDAO.findById(id);
			productSalesDAO.remove(pSales);
		} catch (JPAException dex) {
			throw new ServiceException(dex.getMessage(),dex.getException());
		}
		
	}
	
	  /**
     * 根据kendoGrid中数据筛选条件拼接查询的sql语句
     * @param organizationId 
     * @param configure
     * @return
     * @throws ServiceException 
     */
    private Map<String, Object> getConfigure(Long organizationId, String configure,Long proId) throws ServiceException{
        Object[] params = null;
        String new_configure = "";
        params = new Object[2];
        new_configure = " WHERE e.organizationId = ?1 AND e.product.id=?2 ";
        params[0] = organizationId;
        params[1]= proId;
        String filter[] = configure.split("@@");
        for(int i=0;i<filter.length;i++){
            String filters[] = filter[i].split("@");
            if(filters.length > 3){
                try {
                    String config = splitJointConfigure(filters[0],filters[1],filters[2],organizationId);
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
     * @param field
     * @param mark
     * @param value
     * @param isSon 
     * @return
     * @throws ServiceException
     */
    private String splitJointConfigure(String field, String mark, String value, Long organizationId) throws ServiceException{
        try {
            value = URLDecoder.decode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("【Service-Error】数据筛选时，利用utf-8解码筛选条件时出现异常！",e);
        }
        if(field.equals("id")){
            return FilterUtils.getConditionStr("id",mark,value);
        }
        if(field.equals("product.name")){
            return FilterUtils.getConditionStr("product.name",mark,value);
        }
        if(field.equals("batchSerialNo")){
            return FilterUtils.getConditionStr("batchSerialNo",mark,value);
        }
        if(field.equals("targetCustomer")){
            return FilterUtils.getConditionStr("targetCustomer",mark,value);
        }
        if(field.equals("salesTerritory")){
            return FilterUtils.getConditionStr("salesTerritory",mark,value);
        }
        if(field.equals("salesQuantity")){
            return FilterUtils.getConditionStr("salesQuantity",mark,value);
        }
        if(field.equals("salesDate")){
            return FilterUtils.getConditionStr("salesDate", mark, value);//getConditionStr("lastModifyTime",mark,value);
        }
        return null;
    }
}