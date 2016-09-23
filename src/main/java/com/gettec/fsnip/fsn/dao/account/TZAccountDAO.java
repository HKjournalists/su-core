package com.gettec.fsnip.fsn.dao.account;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.account.TZAccount;
import com.gettec.fsnip.fsn.vo.account.*;

import java.util.List;
import java.util.Map;
/**
 * 
 * @author chenxiaolin
 *
 */
public interface TZAccountDAO extends BaseDAO<TZAccount>{

	List<BusRelationVO> loadTZBusRelation(Long myBusId, int type,int page, int pageSize) throws DaoException;

    Long loadTZBusRelationToatl(Long myBusId, int type)throws DaoException;

    Long getTZReturnProductTotal(Long org)throws DaoException;

	long getTZAccountId();

	List<TZAccount> loadTZPurchaseProduct(Long id, int page, int pageSize, String number)throws DaoException;

	Long getTZPurchaseProductTotal(Long curBusId, String number)throws DaoException;

	TZAccount findByIdAndOutStatus(long id, int i)throws DaoException;

	List<PurchaseAccountVO> loadTZWholeSaleProduct(Long id, int page, int pageSize, String number, String licOrName, int status)throws DaoException;

	Long getTZWholeSaleProductTotal(Long id, String number, String licOrName, int status)throws DaoException;
	
	List<PurchaseAccountVO> loadTZReceiptGoods(Long id, int page, int pageSize, String number, String licOrName)throws DaoException;

	Long getTZReceiptGoodsTotal(Long id, String number, String licOrName)throws DaoException;

	List<ReturnProductVO> getselectBuyGoodsList(Long busId, int page, int pageSize, String name, String barcode)throws DaoException;

	Long getselectBuyGoodsTotal(Long curOrg, String name, String barcode)throws DaoException;

	List<PurchaseAccountVO> searchBuyGoods(Long org, int page, int pageSize,
			String number, String licOrName, int status)throws DaoException;

	Long getTsearchBuyGoodsTotal(Long org, String number, String licOrName, int status)throws DaoException;

	List<ReturnProductVO> getSaleGoodsList(Long busId, int page, int pageSize, String name, String barcode)throws DaoException;

	Long getSaleGoodsListTotal(Long org, String name, String barcode)throws DaoException;

	List<ReturnProductVO> getSaleGoodsListToSC(Long curOrg, Long busId, int page,
			int pageSize, String name, String barcode)throws DaoException;

	Long getSaleGoodsListToSCTotal(Long curOrg,Long busId, String name, String barcode)throws DaoException;

	String getBarcodeByProductId(long l)throws DaoException;

	Map<String, Map<String, Long>> loadingFDAMSDate()throws DaoException;

	BusRelationVO findBusRelationById(long busId)throws DaoException;

	String[] getBusType(Long curOrg)throws DaoException;

	String[] getBusById(Long outBusinessId)throws DaoException;

	Long getOrgByBusId(Long busId)throws DaoException;

	List<ZhengFuVO> loadZFReportList(int page, int pageSize, Long org,
			String name, String batch)throws DaoException;

	Long loadZFReportTotal(Long org, String name, String batch)throws DaoException;

	List<ZhengFuEnterVO> loadZFEnterList(int page, int pageSize, String name,
			int type)throws DaoException;

	Long loadZFEnterTotal(String name, int type)throws DaoException;

	/**
	 * 台帐 加载生产企业自己的产品（生产企业）
	 * @author HY
	 */
	List<ReturnProductVO> loadSelfProductList(Long myOrg,String pname, String pbar,String type, int page, int pageSize) throws DaoException ;

	/**
	 * 台帐 加载生产企业自己的产品的数量（生产企业）
	 * @author HY
	 */
	long loadSelfProductListTotals(Long myOrg,String pname, String pbar,String type) throws DaoException ;

	/**
	 * 产品批次 = 报告批次(包括送检，抽检，自检)
	 * 产品批次 = 报告批次+ 6 个月(报告过期时间) (包括送检和抽检)
	 * @param busId
	 * @param name
	 * @param barcode
	 * @return
	 */
	List<ReturnProductVO> getSaleGoodsListByReportBatch(Long busId, String name, String barcode,int page, int pageSize)throws DaoException;

	/**
	 * 产品批次 = 报告批次(包括送检，抽检，自检)
	 * 产品批次 = 报告批次+ 6 个月(报告过期时间) (包括送检和抽检)
	 * 的总数
	 * @param busId
	 * @param name
	 * @param barcode
	 * @return
	 * @throws DaoException
	 */
	Long getSaleGoodsListByReportBatchTotal(Long busId, String name, String barcode)throws DaoException;


	String checkReport(String prodate,Long proId)throws DaoException;
}