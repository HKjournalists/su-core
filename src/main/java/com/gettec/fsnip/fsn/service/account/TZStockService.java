package com.gettec.fsnip.fsn.service.account;

import java.util.List;

import com.gettec.fsnip.fsn.vo.business.BusinessMarketVO;
import org.springframework.ui.Model;

import com.gettec.fsnip.fsn.dao.account.TZStockDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZBusaccountInfoOut;
import com.gettec.fsnip.fsn.model.account.TZStock;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import com.gettec.fsnip.fsn.vo.account.WholeSaleVO;

/**
 *
 * 
 */
public interface TZStockService extends BaseService<TZStock, TZStockDAO> {

	//cxl
	long getMaxId();
	//cxl
	boolean save(TZStock saveTZStock)throws ServiceException;
	//cxl
	boolean updateStock(TZStock tZStock);
	//cxl
	WholeSaleVO findByProductId(Long productId)throws ServiceException;
	//cxl
	List<TZStock> getTZStockListByPage(int page, int pageSize)throws ServiceException;

	Model createStore(Long org, String barcode, Integer num,String qs,Long stackid, Model model)throws ServiceException;

	Model getStoreProductByBarcode(String barcode, Model model)throws ServiceException;

	Model validatebarcode(Long org,String barcode,String qs, Model model)throws ServiceException;

	void updateTZStock(AccountOutVO accountOut, TZBusaccountInfoOut outInfo)throws ServiceException;

	TZStock findByProductIdAndBusId(Long productId, Long inBusId,String sqNo)throws ServiceException;
	List<TZStock> getTZStockListByCurBusAndProId(Long inBusId, Long productId)throws ServiceException;
	TZStock findByProIdAndQsNo(Long busId, String qsNo,Long productId)throws ServiceException;

	/**
	 * 加载企业的所有类型
	 * @return List<BusinessMarketVO>
	 * @throws ServiceException
	 * @author HY
	 */
	List<BusinessMarketVO> loadBusinessType()throws ServiceException;

	/**
	 * 台帐 生产企业添加自己的产品到库存中（生产企业）
	 *
	 * @author HY
	 */
	Model loadSelfProductList(Long myOrg, String pname, String pbar,int page, int pageSize, Model model)throws ServiceException;

	/**
	 * 企业自己将自己的产品添加到库存
	 * @author HY
	 */
	Model addSelfProductToStore(Long myOrg,String userName, AccountOutVO accountOut, Model model)throws ServiceException;

	/**
	 * 台帐 在商品入库页面加载已经添加商品入库了的产品
	 * @author HY
	 */
	Model loadIntakeProductList(Long myOrg, String pname, String pbarcode, int page, int pageSize, Model model)throws ServiceException;
}
