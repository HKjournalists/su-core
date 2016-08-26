package com.gettec.fsnip.fsn.service.account;

import java.util.List;

import com.gettec.fsnip.fsn.dao.account.TZAccountInfoDAO;
import com.gettec.fsnip.fsn.dao.account.TZBusaccountInfoOutDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZAccountInfo;
import com.gettec.fsnip.fsn.model.account.TZBusaccountInfoOut;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;

/**
 * Created by HY on 2015/5/19.
 * desc:
 */
public interface TZAccountInfoService extends BaseService<TZAccountInfo, TZAccountInfoDAO> {

    boolean save(AccountOutVO accountOut, Long org) throws ServiceException;

	boolean save(TZAccountInfo tzAccountInfo);

	List<ReturnProductVO> getPurchaseList(Long id,int type, Long curBusId, int page, int pageSize)throws ServiceException;

	Long getPurchaseTotal(long id)throws ServiceException;

	TZAccountInfo findByAccountId(Long id)throws ServiceException;

	boolean updateAccountInfo(TZAccountInfo tzAccountInfo)throws ServiceException;

	List<ReturnProductVO> getProList(long proId, int page, int pageSize)throws ServiceException;

	Long getTZReceiptTotal(long tzId)throws ServiceException;

	List<TZAccountInfo> getListByAccountId(Long id)throws ServiceException;

	List<ReturnProductVO> loadingDetailGoods(Long id,int busType, Integer outStatus,
			Long curBusId, int page, int pageSize)throws ServiceException;

	long getloadingDetailGoodsTotal(Long tzId)throws ServiceException;

	List<TZAccountInfo> getTZInfolist(Long id)throws ServiceException;

	void deleteInfoByaccountId(long tZId)throws ServiceException;

}
