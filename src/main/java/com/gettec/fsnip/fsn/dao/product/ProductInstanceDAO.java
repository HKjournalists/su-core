package com.gettec.fsnip.fsn.dao.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.ProductInstance;

public interface ProductInstanceDAO extends BaseDAO<ProductInstance>{

	public ProductInstance findLastBySP(String serial, Long productId);
	
	public ProductInstance findByBSP(String batchSerialNo, String serial, Long productId);
	
	public ProductInstance findByBSB(String batchSerialNo, String serial, String barcode);
	
	public List<ProductInstance> findProductInstancesByPID(Long productId);
	
	public List<ProductInstance> findProductInstances(String batchSerialNo, String serial, Long productId);

	ProductInstance getListByBarcodeAndBatchSerialNo(String barcode,
			String batchSerialNo) throws DaoException;

	public ProductInstance findByBatchAndProductId(String batchSerialNo, Long productId);

	public List<ProductInstance> getProductInstancesByStorageInfoAndStorage(
			Long productId, String storageId, Long organization);
	
	/**
	 * 商品库存过滤barcode
	 * @author Liang Zhou
	 * 2014-10-30
	 * @param filter
	 * @param name
	 * @param fieldName
	 * @return
	 */
	public List<ProductInstance> getInfoNofilter(String filter, String name,String fieldName);
	
	/**
	 * 商品库存过滤商品名称
	 * @author Liang Zhou
	 * 2014-10-30
	 * @param filter
	 * @param name
	 * @param fieldName
	 * @return
	 */
	public List<ProductInstance> getInfoNamefilter(String filter, String name,String fieldName, Long organization);

	/**
	 * 商品库存过滤单位名称
	 * @author Liang Zhou
	 * 2014-10-30
	 * @param filter
	 * @param name
	 * @param fieldName
	 * @return
	 */
	public List<ProductInstance> getInfoUnitNamefilter(String filter,String name, String fieldName, Long organization);

	public ProductInstance findByBarcodeAndProducerId(String barcode,
			Long orgnization) throws DaoException;
	public ProductInstance findInstance(String barcode, String batchSeriaNo) throws DaoException;
	public List<Long> findInstancebyProductId(Long ProductId) throws DaoException;
}
