package com.gettec.fsnip.fsn.service.market.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.market.SystemAttributeDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sys.SystemAttribute;
import com.gettec.fsnip.fsn.service.market.SystemAttributeService;


import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.DEFAULT_ZONE_STRING;

@Service(value="attributeService")
public class SystemAttributeServiceImpl implements SystemAttributeService {

	@Autowired SystemAttributeDAO attributeDAO;

	/**
	 * 按key查找attrbute的值
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public String getValueByAttrKey(String key) throws ServiceException{
		try {
			String result = DEFAULT_ZONE_STRING;
			SystemAttribute speciafyAttr = attributeDAO.findById(key);
			if(speciafyAttr!= null){
				result = speciafyAttr.getAttributeValue();
			}
			return result;
		}catch(JPAException jpae){
			throw new ServiceException("getValueByAttrKey，出现异常", jpae.getException());
		}
	}
}
