package com.gettec.fsnip.fsn.dao.account;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.account.TZIntakeLog;

/**
 * Created by HY on 2015/5/19.
 * desc:
 */
public interface TZIntakeLogDAO extends BaseDAO<TZIntakeLog> {

    TZIntakeLog getTZIntakeLogForUuid(String uuid)throws DaoException;
}
