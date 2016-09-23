package com.gettec.fsnip.fsn.service.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZIntakeLogDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZIntakeLog;
import com.gettec.fsnip.fsn.service.account.TZIntakeLogService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * Author:chenxiaolin
 */
@Service(value="tZIntakeLogService")
public class TZIntakeLogServiceImpl extends BaseServiceImpl<TZIntakeLog,TZIntakeLogDAO> implements TZIntakeLogService {

    @Autowired
    TZIntakeLogDAO tZIntakeLogDAO;

    @Override
    public TZIntakeLogDAO getDAO() {
        return tZIntakeLogDAO;
    }

    @Override
    public TZIntakeLog getTZIntakeLogForUuid(String uuid) throws ServiceException {
        try {
            return tZIntakeLogDAO.getTZIntakeLogForUuid(uuid);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e.getException());
        }
    }
}
