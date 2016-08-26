package com.gettec.fsnip.fsn.dao.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZIntakeLogDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.account.TZIntakeLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author:chenxiaolin
 * 
 */
@Repository(value = "tZIntakeLogDAO")
public class TZIntakeLogDAOImpl extends BaseDAOImpl<TZIntakeLog> implements TZIntakeLogDAO {

    @Override
    public TZIntakeLog getTZIntakeLogForUuid(String uuid) throws DaoException {

        String condition = "WHERE e.uuid = ?1 ";
        try {
            List<TZIntakeLog> logs = this.getListByCondition(condition,new Object[]{uuid});
            return logs!=null && logs.size() > 0 ? logs.get(0) : null;
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(),jpae.getException());
        }
    }
}
