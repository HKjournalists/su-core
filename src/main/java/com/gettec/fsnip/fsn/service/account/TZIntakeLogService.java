package com.gettec.fsnip.fsn.service.account;

import com.gettec.fsnip.fsn.dao.account.TZIntakeLogDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZIntakeLog;
import com.gettec.fsnip.fsn.service.common.BaseService;

/**
 *
 * 
 */
public interface TZIntakeLogService extends BaseService<TZIntakeLog, TZIntakeLogDAO> {

    TZIntakeLog getTZIntakeLogForUuid(String uuid)throws ServiceException;
}
