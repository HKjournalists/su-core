package com.gettec.fsnip.fsn.dao.product.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.KmsLabelDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.product.KmsLabel;

/**
 * 产品标签的Dao层实现
 * @author TangXin
 */
@Repository(value="kmsLabelDAO")
public class KmsLabelDAOImpl extends BaseDAOImpl<KmsLabel>
	implements KmsLabelDAO{

	/**
	 * 根据kms提供的标签id查找一天产品标签信息
	 * @param kmsLabelId
	 * @return
	 * @throws DaoException
	 * @author TangXin
	 */
	@Override
	public KmsLabel findByKmsLabelId(Long kmsLabelId) throws DaoException{
		try{
			String condition = " where e.kmsLabelId = ?1 ";
			List<KmsLabel> listLabel = this.getListByCondition(condition, new Object[]{kmsLabelId});
			if(listLabel!=null&&listLabel.size()>0){
				return listLabel.get(0);
			}
			return null;
		}catch(JPAException jpae){
			throw new DaoException("KmsLabelDAOImpl.findByKmsLabelId()->"+jpae.getMessage(),jpae);
		}
	}
	
}
