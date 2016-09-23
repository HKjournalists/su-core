package com.gettec.fsnip.fsn.dao.receive;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.receive.ReceiveBusKeyConfig;
import com.gettec.fsnip.fsn.sign.SignConfig;

/**
 * 
 * @author ZhangHui 2015/4/24
 */
public interface ReceiveBusKeyConfigDAO extends BaseDAO<ReceiveBusKeyConfig>{

	/**
	 * 根据企业唯一编号，超找签名的配置信息
	 * @author ZhangHui 2014/4/24
	 */
	public SignConfig findByNo(String no) throws DaoException;
	
}
