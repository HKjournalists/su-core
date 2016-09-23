package com.gettec.fsnip.fsn.dao.product;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.QRCodeProductInfo;

/**
 * 二维码产品信息 DAO 层接口
 * @author TangXin
 *
 */
public interface QRCodeProductInfoDAO extends BaseDAO<QRCodeProductInfo>{

	QRCodeProductInfo findByProductId(Long productId) throws DaoException;

	long getMaxSerialNumberByOrganization(Long organization)
			throws DaoException;

	boolean validateInnerCode(String innderCode, Long organization,
			Long productId) throws DaoException;
	
}
