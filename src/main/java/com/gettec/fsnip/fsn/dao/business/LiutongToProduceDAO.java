package com.gettec.fsnip.fsn.dao.business;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.LiutongToProduce;

public interface LiutongToProduceDAO extends BaseDAO<LiutongToProduce> {
 
	LiutongToProduce findByOrganizationAndProduceId(Long orgId, Long produceId) throws DaoException;
}
