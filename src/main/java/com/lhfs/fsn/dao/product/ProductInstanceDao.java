package com.lhfs.fsn.dao.product;

import java.util.List;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.ProductInstance;

public interface ProductInstanceDao extends BaseDAO<ProductInstance>{
	/**
	 * 根据时间范围和barcode 查处所有的batch
     * @param barcode 商品条形码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<String>
	 * @throws DaoException
	 */
    List<String> getProductBacth4ProductDateAndBarcode(String barcode,String startTime, String endTime) throws DaoException;

}
