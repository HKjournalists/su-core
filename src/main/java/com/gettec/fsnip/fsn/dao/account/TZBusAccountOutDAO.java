package com.gettec.fsnip.fsn.dao.account;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.account.TZBusAccountOut;
import com.gettec.fsnip.fsn.vo.account.RetrunAccountVO;

import java.util.List;

/**
 * Created by HY on 2015/5/18.
 * desc:
 */
public interface TZBusAccountOutDAO extends BaseDAO<TZBusAccountOut> {

    List<RetrunAccountVO> loadReturnAccountList( Long busId, String busName,String busLic,
                                                int page, int pageSize, String condition) throws DaoException;

    Long loadReturnAccountListTotal( Long busId, String condition) throws DaoException;

    Long loadReturnInAccountListTotal(Long id, String condition)throws DaoException;

    List<RetrunAccountVO> loadReturnInAccountList(Long id, String name,
           String licNo, int page, int pageSize, String condition)throws DaoException;

    List<RetrunAccountVO> loadAllReturnAccount(String nameOrLic, int page,
                                               int pageSize)throws DaoException;

    Long loadAllReturnAccountTotals(String nameOrLic)throws DaoException;
}
