package com.gettec.fsnip.fsn.service.product;

import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import com.gettec.fsnip.fsn.dao.product.ProductInstanceDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.lhfs.fsn.vo.SampleVO;

public interface ProductInstanceService extends
		BaseService<ProductInstance, ProductInstanceDAO> {

	public ProductInstance findLastBySP(String serial, Long productId);
	
	public ProductInstance findLastByPID(Long productId);
	
	public ProductInstance findByBSP(String batchSerialNo, String serial, Long productId);
	
	public ProductInstance findByBSB(String batchSerialNo, String serial, String barcode);
	
	public ProductInstance findByBatchAndProductId(String batchSerialNo, Long productId);
	
	public List<ProductInstance> findProductInstancesByPID(Long productId);
	
	public List<ProductInstance> getProductInstancesByStorageInfoAndStorage(Long productId,String storageId,Long organization);
	/*
	 * describe:根据批次和产品id获取商品实例
	 * author：cgw
	 * date：2014-10-30*/
	public List<ProductInstance> getProductInstancesByBatchAndProductId(String batch,Long productId) throws ServiceException;

	public ProductInstance findByBarcodeAndProducerId(String barcode,
			Long producerId)throws ServiceException;

	/**
	 * 根据产品id 和batch 在实例中找对应的 实例id
	 * @param batch 批次
	 * @param id 产品id
	 * @return List<Long>
	 */
	public List<Long> getInstanceIdForProductIdAndbatch(String batch, Long id)throws ServiceException;
	 
	ProductInstance addSampleProduct(JSONObject sample,JSONObject producerJSON, Boolean isBatch) throws ServiceException;
    
	public ProductInstance findInstance(String barcode, String batchSeriaNo) throws ServiceException;
	
	public List<Long> findInstancebyProductId(Long productId)throws ServiceException;

	/**
	 * 保存产品实例
	 * @param sample
	 * @param batch
	 * @param organizationID
	 * @return Map<String, Object>
	 * @author LongxXianZhen
	 */
	public Map<String, Object> saveProductInstance(SampleVO sample,
			boolean batch, Long organizationID);

	/**
	 * 功能描述：保存产品信息<br>
	 * 作用于保存泊银等其他外部系统的样品数据。
	 * @author ZhangHui 2015/4/24
	 */
	boolean saveBYProductIns(TestResult testReport, Long organization);

}
