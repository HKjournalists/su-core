package com.gettec.fsnip.fsn.dao.account;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZStock;
import com.gettec.fsnip.fsn.vo.account.WholeSaleVO;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;
import com.gettec.fsnip.fsn.vo.business.BusinessMarketVO;

/**
 * Created by HY on 2015/5/19.
 * desc:
 */
public interface TZStockDAO extends BaseDAO<TZStock> {

	long getMaxId();

	WholeSaleVO findByProductId(Long productId) throws DaoException;

	List<TZStock> getTZStockListByPage(int page, int pageSize)throws DaoException;
	
	ReturnProductVO findVOByBarcode(String barcode)throws DaoException;

	List<TZStock> getStoreProductByBarcode(String barcode, Long org,String qs)throws DaoException;

	TZStock getByProIdAndSelfBusId(Long productId, Long outBusId,String qsNO)throws DaoException;

	List<TZStock> getTZStockListByCurBusAndProId(Long inBusId, Long productId)throws DaoException;

	List<String> getQsNoByBarcode(Long proId)throws DaoException;

	TZStock findByProIdAndQsNo(Long busId, String qsNo,Long productId)throws DaoException;

	/**
	 * 加载企业的所有类型
	 * @return List<BusinessMarketVO>
	 * @throws DaoException
	 * @author HY
	 */
	List<BusinessMarketVO> loadBusinessType()throws DaoException;

	/**
	 * 查询报告转态更加企业类型，商品id，批次
	 * @author HY
	 */
	int getReportStatus(String batch, Long productId, String busType)throws DaoException;

	List<ReturnProductVO> loadIntakeProductList(Long busId,String pname, String pbarcode, int page, int pageSize)throws DaoException;

	Long loadIntakeProductTotals(Long busId,String pname, String pbarcode)throws DaoException;

}
