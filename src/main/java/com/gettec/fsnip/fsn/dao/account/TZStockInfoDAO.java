package com.gettec.fsnip.fsn.dao.account;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.account.TZStockInfo;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;

import java.util.List;

/**
 * 
 * Author:chenxiaolin
 */
public interface TZStockInfoDAO extends BaseDAO<TZStockInfo> {

    List<ReturnProductVO> loadStoreInfoList(Long org, Integer page, Integer pageSize,
                                            String condition) throws DaoException;

    Long loadStoreInfoListTotals(Long org, String condition)throws DaoException;

    List<ReturnProductVO> loadStoreDetailList(Integer page, Integer pageSize,
                                              Long sid)throws DaoException;

    Long loadStoreDetailListTotals(Long sid)throws DaoException;

	List<TZStockInfo> getTZStockInfoList(long stockId, Long inBusId)throws DaoException;

    Long getStockCountByStockId(Long productId, String batch)throws DaoException;

	void deleteListByStockId(Long stockId)throws DaoException;
}
