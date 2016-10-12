package com.gettec.fsnip.fsn.dao.procurement.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.procurement.OnlineSaleGoodsDisposeDao;
import com.gettec.fsnip.fsn.model.procurement.OnlineSaleGoodsDispose;


/**
 * OnlineSaleGoodsDispose customized operation implementation
 * 
 * @author suxiang
 */
@Repository(value="OnlineSaleGoodsDisposeDao")
public class OnlineSaleGoodsDisposeDaoImpl extends BaseDAOImpl<OnlineSaleGoodsDispose> implements OnlineSaleGoodsDisposeDao{

	@PersistenceContext
	private EntityManager entityManager;
	
}
