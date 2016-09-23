package com.gettec.fsnip.fsn.dao.procurement.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.procurement.ProcurementInfoDAO;
import com.gettec.fsnip.fsn.model.procurement.ProcurementInfo;


/**
 * ProcurementInfo customized operation implementation
 * 
 * @author lxz
 */
@Repository(value="procurementInfoDAO")
public class ProcurementInfoDAOImpl extends BaseDAOImpl<ProcurementInfo>
		implements ProcurementInfoDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	
	
}