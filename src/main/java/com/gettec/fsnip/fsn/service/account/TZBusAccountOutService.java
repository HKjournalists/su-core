package com.gettec.fsnip.fsn.service.account;

import com.gettec.fsnip.fsn.dao.account.TZBusAccountOutDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZBusAccountOut;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import org.springframework.ui.Model;

/**
 * Created by HY on 2015/5/18.
 * desc:
 */
public interface TZBusAccountOutService extends BaseService<TZBusAccountOut, TZBusAccountOutDAO> {

    TZBusAccountOut save(TZBusAccountOut out,AccountOutVO accountOut, Long org) throws ServiceException;

    Model loadReturnAccountList(String type,Long org, String accountNo, String nameAndLic,
                                int page, int pageSize, Model model)throws ServiceException;

    Model initReturnGoods(Long id,Model model)throws ServiceException;

    Model loadReturnGoodsDetail(Long outId, Integer page,
                                Integer pageSize, Model model)throws ServiceException;

    Model initReturnInGoods(Long id, Model model)throws ServiceException;

    Model deleteReturnInfo(Long organization, Long id, Model model)throws ServiceException;

    Model loadAllReturnAccount(String nameOrLic, int page, int pageSize,
                               Model model)throws ServiceException;
}
