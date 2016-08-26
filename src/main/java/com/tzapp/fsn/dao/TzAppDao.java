package com.tzapp.fsn.dao;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.exception.DaoException;
import com.tzapp.fsn.vo.TzAppBusInfoVO;
import com.tzapp.fsn.vo.TzAppProductDetailVO;
import com.tzapp.fsn.vo.TzAppReceiptVO;
import com.tzapp.fsn.vo.TzAppReportAndProductDetailVO;
import com.tzapp.fsn.vo.TzAppRequestParamVO;
import com.tzapp.fsn.vo.TzAppSearchAndScanVO;
import com.tzapp.fsn.vo.TzAppTestProperty;

public interface TzAppDao {

	TzAppSearchAndScanVO getBusNameByOrg(TzAppSearchAndScanVO vo)throws DaoException;
	
	List<TzAppReceiptVO> loadReceipt(TzAppSearchAndScanVO paramVo) throws DaoException;

	List<TzAppProductDetailVO> loadReceiptDetail(TzAppRequestParamVO busInfo)throws DaoException;

	TzAppRequestParamVO getBusInfo(TzAppRequestParamVO requestParamVO)throws DaoException;

	TzAppReportAndProductDetailVO lookProductDetail(TzAppRequestParamVO busInfo)throws DaoException;

	List<TzAppTestProperty> getTestPropertyList(TzAppRequestParamVO busInfo)throws DaoException;

	TzAppBusInfoVO lookBusInfo(TzAppRequestParamVO requestParam)throws DaoException;

	void confirmReturnOfGoods(Long id)throws DaoException;

	List<TzAppReportAndProductDetailVO> noCheck(TzAppSearchAndScanVO paramVo)throws DaoException;

	Long getTotal(TzAppSearchAndScanVO busInfo)throws DaoException;

	TzAppReportAndProductDetailVO reportlDetail(TzAppRequestParamVO busInfo)throws DaoException;

	List<TzAppReportAndProductDetailVO> searchReport(TzAppSearchAndScanVO paramVo)throws DaoException;

	List<Map<String, String>> backResults(TzAppRequestParamVO busInfo)throws DaoException;

	Long getScanTotal(TzAppSearchAndScanVO paramVo)throws DaoException;

	/**
	 * 获取收货或退货查询的总数
	 * @author ChenXiaolin 2015-11-24
	 */
	Long receiptOrRefuseTotal(TzAppSearchAndScanVO paramVo)throws DaoException;

	/**
	 * 初始化台账相关总数
	 * @author ChenXiaolin 2015-11-24
	 */
	Long loadTotal(TzAppSearchAndScanVO vo)throws DaoException;

	/**
	 * update报告审核通过标识
	 * @author ChenXiaolin 2015-12-09
	 * @param charAt
	 * @param reportId
	 * @param returnReason
	 * @param curBusName
	 */
	void updatePublishFlag(char charAt, Long reportId, String returnReason,
			String curBusName)throws DaoException;

}
