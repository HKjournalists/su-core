package com.gettec.fsnip.fsn.service.account;

import com.gettec.fsnip.fsn.dao.account.TZAccountDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZAccount;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;

import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author chenxiaolin
 *
 */
public interface TZAccountService extends BaseService<TZAccount, TZAccountDAO>{

    Model loadTZReturnProduct(Long organization, int page, int pageSize, String number, String licOrName, Model model, int status)throws ServiceException;

	Model lookTZPurchaseProduct(long id, String type, Model model)throws ServiceException;

	Model viewPurchaseNotSubmit(long id,Long curOrg, int page, int pageSize, Model model)throws ServiceException;

	Model selectProduct(int page, int pageSize, Model model)throws ServiceException;

	Model submitTZWholeSaleProduct(Long organization, AccountOutVO accountOut,Model model,
			String status,Integer type)throws ServiceException;

	Model viewWholeSale(Long org, int page, int pageSize, String number, String licOrName, Model model, int status)throws ServiceException;

	Model viewReceiptGoods(Long organization, int page, int pageSize,String number, String licOrName, Model model)throws ServiceException;

	void submitReceiptGoods(long tZId, Long curOrg,List<Map<String,Object>> rvo)throws ServiceException;

	Model lookTZReceiptGoods(long accountId, Model model)throws ServiceException;

	Model viewNoReceiptGoods(long id, int page, int pageSize, Model model)throws ServiceException;

	Model selectBuyGoods(Long organization, int page, int pageSize,String name, String barcode, Model model)throws ServiceException;

	Model selectSaleGoods(Long organization, String name, String barcode, int page, int pageSize, Model model)throws ServiceException;

	Model loadingDetailGoods(Long tzId, Long curOrg, int page, int pageSize, Model model)throws ServiceException;

	void deleteGoods(long tZId, String type)throws ServiceException;

	Model loadingFDAMSDate(Model model)throws ServiceException;

	Model loadBustBasicInfo(Long organization, int type, Model model)throws ServiceException;

	Model loadZFReportList(int page, int pageSize, Long busId, String name,
			String batch, Model model)throws ServiceException;

	Model loadZFEnterList(int page, int pageSize, String name, int type,
			Model model)throws ServiceException;

	void returnOfGoods(long tZId, Long myOrg, String refuseReason)throws ServiceException;

	Model checkReport(Model model, String prodate,Long proId,boolean reportFlag)throws ServiceException;

	Model selectBuyGoodsById(Long orgId, String name, String barcode, int page,
			int pageSize, Model model)throws ServiceException;

	Model submitTZWholeSaleProductGYS(Long organization, AccountOutVO accountOut,Model model,
			String status,Integer type)throws ServiceException;

	Model viewWholeSaleGYS(Long myOrg, int page, int pageSize, String number,
			String licOrName, Model model, int status) throws ServiceException;

	TZAccount saleSure(long id);

	/**
	 * 超市进货时选择自己的产品
	 * @param orgId
	 * @param name
	 * @param barcode
	 * @param page
	 * @param pageSize
	 * @param model
	 * @return
     * @throws ServiceException
     */
	Model selectBuyGoodsOfCS(Long orgId, String name, String barcode, int page, int pageSize, Model model) throws ServiceException;
}