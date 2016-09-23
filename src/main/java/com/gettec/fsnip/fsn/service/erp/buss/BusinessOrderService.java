package com.gettec.fsnip.fsn.service.erp.buss;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.vo.erp.BusinessOrderVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface BusinessOrderService<E> {

	E addBusinessOrder(BusinessOrderVO vo,Long organization, String userName) throws Exception;
	
	E reviewOrder(String no,String userName, Long organization) throws Exception;
	
	PagingSimpleModelVO<E> getPaging(int page, int pageSize, String keywords, Long organization) throws ServiceException;
}
