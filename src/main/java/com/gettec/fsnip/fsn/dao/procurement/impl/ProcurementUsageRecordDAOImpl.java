package com.gettec.fsnip.fsn.dao.procurement.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.procurement.ProcurementUsageRecordDAO;
import com.gettec.fsnip.fsn.model.procurement.ProcurementUsageRecord;


/**
 * ProcurementUsageRecord customized operation implementation
 * 
 * @author lxz
 */
@Repository(value="procurementUsageRecordDAO")
public class ProcurementUsageRecordDAOImpl extends BaseDAOImpl<ProcurementUsageRecord>
		implements ProcurementUsageRecordDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	
	
}