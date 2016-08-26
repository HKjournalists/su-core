package com.gettec.fsnip.fsn.service.account;

import com.gettec.fsnip.fsn.dao.account.TZBusaccountInfoOutDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZBusAccountOut;
import com.gettec.fsnip.fsn.model.account.TZBusaccountInfoOut;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;

import java.util.List;

/**
 * Created by HY on 2015/5/19.
 * desc:
 */
public interface TZBusaccountInfoOutService extends BaseService<TZBusaccountInfoOut, TZBusaccountInfoOutDAO> {

    List<TZBusaccountInfoOut> save(AccountOutVO accountOut, TZBusAccountOut out) throws ServiceException;

    List<ReturnProductVO> loadTZReturnInProductByOutId(Long id, int i, int i1)throws ServiceException;

    List<TZBusaccountInfoOut> getListByOutId(Long outId)throws ServiceException;

    void deleteReturnOutInfoByOutId(Long outId)throws ServiceException;
}
