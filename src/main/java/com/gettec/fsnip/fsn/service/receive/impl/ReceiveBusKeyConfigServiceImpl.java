package com.gettec.fsnip.fsn.service.receive.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gettec.fsnip.fsn.dao.receive.ReceiveBusKeyConfigDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.receive.ReceiveBusKeyConfig;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.receive.ReceiveBusKeyConfigService;
import com.gettec.fsnip.fsn.sign.SignConfig;

/**
 * 
 * @author ZhangHui 2015/4/24
 */
@Service(value="receiveBusKeyConfigService")
public class ReceiveBusKeyConfigServiceImpl 
			extends BaseServiceImpl<ReceiveBusKeyConfig, ReceiveBusKeyConfigDAO>
			implements ReceiveBusKeyConfigService{
	@Autowired private ReceiveBusKeyConfigDAO receiveBusKeyConfigDAO;
	
	@Override
	public ReceiveBusKeyConfigDAO getDAO() {
		return receiveBusKeyConfigDAO;
	}

	/**
	 * 根据企业唯一编号，超找签名的配置信息
	 * @author ZhangHui 2014/4/24
	 */
	@Override
	public SignConfig findByNo(String no) throws ServiceException {
		try {
			return getDAO().findByNo(no);
		} catch (DaoException e) {
			throw new ServiceException("ReceiveBusKeyConfigServiceImpl.findByNo()-->" + e.getMessage(), e.getException());
		}
	}

}
