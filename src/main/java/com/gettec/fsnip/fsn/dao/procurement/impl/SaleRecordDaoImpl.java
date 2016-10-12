package com.gettec.fsnip.fsn.dao.procurement.impl;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.procurement.SaleRecordDao;
import com.gettec.fsnip.fsn.model.procurement.SaleRecord;


/**
 * OnlineSaleGoods customized operation implementation
 * 
 * @author suxiang
 */
@Repository(value="SaleRecordDao")
public class SaleRecordDaoImpl extends BaseDAOImpl<SaleRecord> implements SaleRecordDao{

}
