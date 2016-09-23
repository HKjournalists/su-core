package com.gettec.fsnip.fsn.service.product;

import com.gettec.fsnip.fsn.dao.product.KmsLabelDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.KmsLabel;
import com.gettec.fsnip.fsn.service.common.BaseService;

/**
 * 产品标签 service 层接口
 * @author TangXin
 */
public interface KmsLabelService extends BaseService<KmsLabel,KmsLabelDAO>{
	
	KmsLabel findByKmsLabelId(Long kmsLabelId) throws ServiceException;
	
	KmsLabel save(KmsLabel kmsLabel) throws ServiceException;

}
