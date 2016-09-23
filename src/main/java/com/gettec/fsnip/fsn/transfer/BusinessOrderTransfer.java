package com.gettec.fsnip.fsn.transfer;

import com.gettec.fsnip.fsn.vo.erp.BusinessOrderVO;

public interface BusinessOrderTransfer<E> {
	
	E transferToRealEntity(BusinessOrderVO vo);
	
}
