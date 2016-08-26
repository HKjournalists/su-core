package com.gettec.fsnip.fsn.service.business;

import com.gettec.fsnip.fsn.dao.business.TaxRegisterDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.TaxRegisterInfo;
import com.gettec.fsnip.fsn.service.common.BaseService;



public interface TaxRegisterService extends BaseService<TaxRegisterInfo, TaxRegisterDAO>{

	void save(TaxRegisterInfo taxRegister) throws ServiceException;

}