package com.gettec.fsnip.fsn.dao.receive.impl;

import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.receive.ReceiveTestResultDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.receive.ReceiveTestResult;

/**
 * 
 * @author ZhangHui 2015/4/24
 */
@Repository(value="receiveTestResultDAO")
public class ReceiveTestResultDAOImpl extends BaseDAOImpl<ReceiveTestResult> 
		implements ReceiveTestResultDAO{

	@Override
	public long countByReceiveIdAndEdition(String receive_id, String edition) throws DaoException {
		try{
			String condition = "  WHERE e.receive_id = ?1 AND e.edition = ?2";
			return this.count(condition, new Object[]{receive_id, edition});
		}catch(JPAException jpae){
			throw new DaoException("ReceiveTestResultDAOImpl.countByReceiveIdAndEdition "+jpae.getMessage(),jpae.getException());
		}
	}
	
}
