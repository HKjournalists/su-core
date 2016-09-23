package com.gettec.fsnip.fsn.service.product.impl;

import java.util.Date;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.ProductLogDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductLog;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.ProductLogService;

/**
 * ProductLogServiceImpl service implementation
 * 
 * @author LongXianZhen  2015/06/03
 */
@Service(value="productLogService")
public class ProductLogServiceImpl extends BaseServiceImpl<ProductLog, ProductLogDAO> 
		implements ProductLogService{
	@Autowired protected ProductLogDAO productLogDAO;
	
	@Override
	public ProductLogDAO getDAO() {
		return productLogDAO;
	}
	/**
	 * 保存产品日志
	 * @param data 
	 * @author LongXianZhen 2015/06/04
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveProductLog(ProductLog data) {
		try {
			Product pro=data.getProduct();
			pro.getBusinessBrand().setBusinessUnit(null);
			pro.getProducer().setCustomers(null);
			pro.getProducer().setCustomerlist(null);
			pro.getProducer().setProviders(null);
			try {
				JSON json=JSONSerializer.toJSON(pro); //JSONObject.fromObject(pro,jsonConfig); //
				data.setOperationData(json.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			data.setProductName(pro.getName());
			data.setProductId(pro.getId());
			data.setBarcode(pro.getBarcode());
			data.setOperationTime(new Date());
			productLogDAO.persistent(data);
		} catch (JPAException e) {
			((Throwable) e.getException()).printStackTrace();
		}
	}

}