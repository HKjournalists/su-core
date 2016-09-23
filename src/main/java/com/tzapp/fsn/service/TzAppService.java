package com.tzapp.fsn.service;

import org.springframework.ui.Model;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.tzapp.fsn.vo.ConfirmReceiptVO;
import com.tzapp.fsn.vo.TzAppRequestParamVO;
import com.tzapp.fsn.vo.TzAppSearchAndScanVO;

public interface TzAppService {

	Model loadReceipt(Model model, TzAppSearchAndScanVO paramVo) throws ServiceException;

	TzAppSearchAndScanVO getBusNameByOrg(TzAppSearchAndScanVO vo)throws ServiceException;

	Model loadReceiptDetail(Model model, TzAppRequestParamVO busInfo)throws ServiceException;

	Model lookProductDetail(Model model, TzAppRequestParamVO busInfo)throws ServiceException;

	Model lookBusInfo(Model model, TzAppRequestParamVO busInfo)throws ServiceException;

	void confirmReturnOfGoods(Long id)throws ServiceException;

	Model noCheck(Model model, TzAppSearchAndScanVO paramVo)throws ServiceException;

	Model reportlDetail(Model model, TzAppRequestParamVO busInfo)throws ServiceException;

	void reportPass(TzAppSearchAndScanVO paramVo)throws ServiceException;

	Model searchReport(Model model, TzAppSearchAndScanVO paramVo)throws ServiceException;

	void confirmReceipt(ConfirmReceiptVO vo)throws ServiceException;

	Model backResults(Model model, TzAppRequestParamVO busInfo)throws ServiceException;

	/**
	 * 初始化台账相关总数
	 * @author ChenXiaolin 2015-11-24
	 */
	Model loadTotal(Model model, TzAppSearchAndScanVO vo)throws ServiceException;

}
