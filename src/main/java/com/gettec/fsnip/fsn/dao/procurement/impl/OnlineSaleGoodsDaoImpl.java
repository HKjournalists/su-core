package com.gettec.fsnip.fsn.dao.procurement.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.procurement.OnlineSaleGoodsDao;
import com.gettec.fsnip.fsn.model.procurement.OnlineSaleGoods;

/**
 * OnlineSaleGoods customized operation implementation
 * 
 * @author suxiang
 */
@Repository(value="OnlineSaleGoodsDao")
public class OnlineSaleGoodsDaoImpl extends BaseDAOImpl<OnlineSaleGoods> implements
		OnlineSaleGoodsDao {
	
	@PersistenceContext
	private EntityManager entityManager;

}
