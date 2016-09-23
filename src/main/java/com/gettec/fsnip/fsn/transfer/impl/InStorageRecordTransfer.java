package com.gettec.fsnip.fsn.transfer.impl;

import java.util.Date;

import com.gettec.fsnip.fsn.model.erp.buss.InStorageRecord;
import com.gettec.fsnip.fsn.transfer.BusinessOrderTransfer;
import com.gettec.fsnip.fsn.vo.erp.BusinessOrderVO;

public class InStorageRecordTransfer implements BusinessOrderTransfer<InStorageRecord> {

	@Override
	public InStorageRecord transferToRealEntity(BusinessOrderVO vo) {
		InStorageRecord record = new InStorageRecord();
		record.setNo(vo.getNo());
		record.setCreateTime(new Date());
		record.setNote(vo.getNote());
		
		record.setCreateUserID(1L);
		record.setCreateUserName("admin");
		
		return record;
	}

}
