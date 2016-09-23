package com.gettec.fsnip.fsn.service.product.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.KmsLabelDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.KmsLabel;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.KmsLabelService;

/**
 * 产品标签service 层实现
 * @author TangXin
 */
@Service(value="kmsLabelService")
public class KmsLabelServiceImpl extends BaseServiceImpl<KmsLabel,KmsLabelDAO>
	implements KmsLabelService{
	
	@Autowired
	private KmsLabelDAO kmsLabelDAO;

	@Override
	public KmsLabelDAO getDAO() {
		return kmsLabelDAO;
	}
	
	/**
	 * 根据kms提供的标签id查找一天产品标签信息
	 * @param kmsLabelId
	 * @return
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	public KmsLabel findByKmsLabelId(Long kmsLabelId) throws ServiceException{
		try{
			return getDAO().findByKmsLabelId(kmsLabelId);
		}catch(DaoException daoe){
			throw new ServiceException("KmsLabelServiceImpl.findByKmsLabelId()->"+daoe.getMessage(),daoe);
		}
	}
	
	/**
	 * 将产品关联的标签保存在本地
	 * @param kmsLabel
	 * @return
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public KmsLabel save(KmsLabel kmsLabel) throws ServiceException{
		try{
			if(kmsLabel==null||kmsLabel.getLabelName().equals("")) return null;
			KmsLabel orig_label = getDAO().findByKmsLabelId(kmsLabel.getKmsLabelId());
			if(orig_label==null){
				create(kmsLabel);
				orig_label = kmsLabel;
			}
			return orig_label;
		}catch(Exception e){
			throw new ServiceException("KmsLabelServiceImpl.save()->"+e.getMessage(),e);
		}
	}
}
