package com.lhfs.fsn.service.product;

import java.util.List;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.lhfs.fsn.dao.product.ProductInstanceDao;

public interface ProductInstanceService extends BaseService<ProductInstance, ProductInstanceDao>{
	/**
	 * 根据时间范围和barcode 查处所有的batch
	 * @param barcode 商品条形码
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return List<String>
	 * @throws ServiceException
	 */
    List<String> getProductBacth4ProductDateAndBarcode(String barcode,String startTime, String endTime)throws ServiceException;

}
