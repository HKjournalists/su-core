package com.gettec.fsnip.fsn.dao.business.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.business.LiutongToProduceDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.LiutongToProduce;

@Repository(value="liutongToProduceDAO")
public class LiutongToProduceDAOImpl extends BaseDAOImpl<LiutongToProduce>
	implements LiutongToProduceDAO {

	@Override
	public LiutongToProduce findByOrganizationAndProduceId(Long orgId,
			Long produceId) throws DaoException {
		try{
			String condition = " where e.organization = ?1 and e.producerId = ?2";
			List<LiutongToProduce> listProducer = this.getListByCondition(condition, new Object[]{orgId,produceId});
			if(listProducer !=null && listProducer.size()>0) return listProducer.get(0);
			return null;
		}catch(JPAException jpae){
			throw new DaoException("LiutongToProduceDAOImpl.findByOrganizationAndProduceId() "+jpae.getMessage(),jpae);
		}
	}

}
