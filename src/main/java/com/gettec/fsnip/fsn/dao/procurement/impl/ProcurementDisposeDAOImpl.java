package com.gettec.fsnip.fsn.dao.procurement.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.procurement.ProcurementDisposeDAO;
import com.gettec.fsnip.fsn.model.procurement.ProcurementDispose;


/**
 * ProcurementInfo customized operation implementation
 * 
 * @author lxz
 */
@Repository(value="procurementDisposeDAO")
public class ProcurementDisposeDAOImpl extends BaseDAOImpl<ProcurementDispose>
		implements ProcurementDisposeDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	
	
}