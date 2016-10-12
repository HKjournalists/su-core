package com.gettec.fsnip.fsn.service.procurement;

import java.util.List;

import com.gettec.fsnip.fsn.dao.procurement.SaleRecordDao;
import com.gettec.fsnip.fsn.model.procurement.SaleRecord;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface SaleRecordService extends BaseService<SaleRecord, SaleRecordDao>{

	long getRecordTotalByPid(String saleDate, Long onlineSaleId);

	List<SaleRecord> getRecordListByPid(int page, int pageSize,
			String saleDate, Long onlineSaleId);

}
