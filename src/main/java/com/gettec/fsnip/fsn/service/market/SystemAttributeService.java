package com.gettec.fsnip.fsn.service.market;

import com.gettec.fsnip.fsn.exception.ServiceException;


public interface SystemAttributeService {

	String getValueByAttrKey(String key) throws ServiceException;
	
}
