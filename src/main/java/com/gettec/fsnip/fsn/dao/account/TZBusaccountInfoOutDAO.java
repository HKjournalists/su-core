package com.gettec.fsnip.fsn.dao.account;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.account.TZBusaccountInfoOut;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;

import java.util.List;

/**
 * Created by HY on 2015/5/19.
 * desc:
 */
public interface TZBusaccountInfoOutDAO extends BaseDAO<TZBusaccountInfoOut> {

    List<ReturnProductVO> loadTZReturnInProductByOutId(Long outId, int page, int pageSize)throws DaoException;

    List<TZBusaccountInfoOut> findListByOutId(Long id)throws DaoException;
}
