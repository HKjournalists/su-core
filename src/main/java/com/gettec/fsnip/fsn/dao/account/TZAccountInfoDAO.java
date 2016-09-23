package com.gettec.fsnip.fsn.dao.account;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.account.TZAccountInfo;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;

/**
 * Created by HY on 2015/5/19.
 * desc:
 */
public interface TZAccountInfoDAO extends BaseDAO<TZAccountInfo> {

	List<ReturnProductVO> getPurchaseList(Long id, int type, Long curBusId, int page, int pageSize)throws DaoException;

	Long getPurchaseTotal(long id)throws DaoException;

	TZAccountInfo findByAccountId(Long id)throws DaoException;

	List<ReturnProductVO> getProList(long proId, int page, int pageSize)throws DaoException;

	Long getTZReceiptTotal(long proId)throws DaoException;

	List<TZAccountInfo> getListByAccountId(Long id)throws DaoException;

	List<ReturnProductVO> loadingDetailGoods(Long tzId,int busType, int outStatus, Long curOrg, int page, int pageSize)throws DaoException;

	long getloadingDetailGoodsTotal(Long tzId)throws DaoException;

	List<TZAccountInfo> getTZInfolist(Long tzId)throws DaoException;

	void deleteInfoByaccountId(long tZId)throws DaoException;

	String getReportUrlByReportId(Long reportId)throws DaoException;

}
