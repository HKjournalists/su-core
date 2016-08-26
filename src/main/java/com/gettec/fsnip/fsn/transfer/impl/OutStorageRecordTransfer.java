package com.gettec.fsnip.fsn.transfer.impl;

import java.util.Date;

import com.gettec.fsnip.fsn.model.erp.buss.OutStorageRecord;
import com.gettec.fsnip.fsn.transfer.BusinessOrderTransfer;
import com.gettec.fsnip.fsn.vo.erp.BusinessOrderVO;

public class OutStorageRecordTransfer implements BusinessOrderTransfer<OutStorageRecord> {

	@Override
	public OutStorageRecord transferToRealEntity(BusinessOrderVO vo) {
		OutStorageRecord record = new OutStorageRecord();
		record.setNo(vo.getNo());
		record.setCreateTime(new Date());
		record.setNote(vo.getNote());
		record.setCreateUserID(1L);
		record.setCreateUserName("admin");
		return record;
	}

}
