package com.gettec.fsnip.fsn.transfer.impl;

import java.util.Date;

import com.gettec.fsnip.fsn.model.erp.buss.FlittingOrder;
import com.gettec.fsnip.fsn.transfer.BusinessOrderTransfer;
import com.gettec.fsnip.fsn.vo.erp.BusinessOrderVO;

public class FlittingOrderTransfer implements BusinessOrderTransfer<FlittingOrder> {

	@Override
	public FlittingOrder transferToRealEntity(BusinessOrderVO vo) {
		FlittingOrder record = new FlittingOrder();
		
		record.setNo(vo.getNo());
		record.setCreateTime(new Date());
		record.setNote(vo.getNote());
		
		record.setCreateUserID(1L);
		record.setCreateUserName("admin");
		return record;
	}

}
