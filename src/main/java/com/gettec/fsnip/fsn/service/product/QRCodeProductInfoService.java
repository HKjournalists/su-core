package com.gettec.fsnip.fsn.service.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.product.QRCodeProductInfoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.QRCodeProductInfo;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;


/**
 * 二维码产品信息service层接口
 * @author TangXin
 *
 */
public interface QRCodeProductInfoService extends BaseService<QRCodeProductInfo,QRCodeProductInfoDAO>{

	QRCodeProductInfo findByProductId(Long productId) throws ServiceException;

	QRCodeProductInfo save(QRCodeProductInfo qrcodeProduct) throws ServiceException;

	List<QRCodeProductInfo> getListByOrganization(Long organization, int page, int pageSize, String configure)
			throws ServiceException;

	long countByOrganization(Long organization, String configure)
			throws ServiceException;

	long getMaxSerialNumberByOrganization(Long organization)
			throws ServiceException;

	boolean validateInnerCode(String innerCode, Long organization,
			Long productId) throws ServiceException;

	QRCodeProductInfo save(QRCodeProductInfo qrcodeProduct,
			Long curOrganziation, boolean newFlag, AuthenticateInfo info)
			throws ServiceException;
	

}
