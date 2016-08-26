package com.gettec.fsnip.fsn.service.account;

import java.util.List;

import com.gettec.fsnip.fsn.dao.account.TZStockInfoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZStockInfo;
import com.gettec.fsnip.fsn.service.common.BaseService;
import org.springframework.ui.Model;

/**
 *
 * 
 */
public interface TZStockInfoService extends BaseService<TZStockInfo, TZStockInfoDAO> {
	//cxl
	boolean save(TZStockInfo tZStockInfo) throws ServiceException;
	//cxl
	boolean updateStockInfo(TZStockInfo tZStockInfo);

	Model loadStoreInfoList(Long org, Integer page, Integer pageSize,
							String condition, Model model)throws ServiceException;

	Model loadStoreDetailList(Integer page, Integer pageSize, Long sid,
							  Model model)throws ServiceException;

	Model receiptReturnGoods(Long outId, Model model)throws ServiceException;
	List<TZStockInfo> getTZStockInfoList(long stockId, Long inBusId)throws ServiceException;
	void deleteListByStockId(Long stockId)throws ServiceException;
}
