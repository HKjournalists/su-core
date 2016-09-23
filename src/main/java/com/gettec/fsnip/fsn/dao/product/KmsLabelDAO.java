package com.gettec.fsnip.fsn.dao.product;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.KmsLabel;

/**
 * 产品标签DAO 层接口 
 * @author TangXin
 */
public interface KmsLabelDAO extends BaseDAO<KmsLabel>{

	KmsLabel findByKmsLabelId(Long kmsLabelId) throws DaoException;

	
}
