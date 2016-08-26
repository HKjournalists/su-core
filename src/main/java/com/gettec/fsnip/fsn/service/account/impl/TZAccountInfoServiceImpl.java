package com.gettec.fsnip.fsn.service.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZAccountInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZAccountInfo;
import com.gettec.fsnip.fsn.service.account.TZAccountInfoService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * Created by HY on 2015/5/19.
 * desc:
 */
@Service(value = "tZAccountInfoService")
public class TZAccountInfoServiceImpl extends BaseServiceImpl<TZAccountInfo, TZAccountInfoDAO> implements TZAccountInfoService {

	@Autowired
	TZAccountInfoDAO tZAccountInfoDAO;

	@Override
	public TZAccountInfoDAO getDAO() {
		return tZAccountInfoDAO;
	}

	/**
	 * 保存供销关系中的产品信息
	 *
	 * @author chenxiaolin
	 */
	@Override
	public boolean save(AccountOutVO accountOut, Long org) throws ServiceException {
		try {
			boolean isSuccess = false;
			List<ReturnProductVO> pVOs = accountOut.getProList();//获取商品列表
			for (ReturnProductVO vo : pVOs) {
				TZAccountInfo tzAccountInfo = new TZAccountInfo(vo);//没有设AccountId 的值
				TZAccountInfo orig_Info = null;
				if (tzAccountInfo.getId() != null) {//跟新的情况
					orig_Info = tZAccountInfoDAO.findById(tzAccountInfo.getId());
					if (orig_Info instanceof TZAccountInfo) {
						setReturnProduct(orig_Info, tzAccountInfo);
						update(orig_Info);
						isSuccess = true;
					}
				} else {//新建的情况
					orig_Info = new TZAccountInfo();
					setReturnProduct(orig_Info, tzAccountInfo);
					create(orig_Info);
					isSuccess = true;
				}
			}
			return isSuccess;
		} catch (JPAException jpae) {
			throw new ServiceException(jpae.getMessage(), jpae.getException());
		} catch (ParseException pe) {
			throw new ServiceException(pe.getMessage(), pe);
		}
	}

	//设置进货商品
	private void setReturnProduct(TZAccountInfo orig_info, TZAccountInfo tzAccountInfo) {
		orig_info.setProductionDate(tzAccountInfo.getProductionDate());
		orig_info.setOverDate(tzAccountInfo.getOverDate());
		orig_info.setProductBatch(tzAccountInfo.getProductBatch());
		orig_info.setProductId(tzAccountInfo.getProductId());
		orig_info.setProductNum(tzAccountInfo.getProductNum() != null ? tzAccountInfo.getProductNum() : 0);
		orig_info.setProductPrice(tzAccountInfo.getProductPrice() != null ? tzAccountInfo.getProductPrice() : BigDecimal.valueOf(0.00));
	}

	@Override
	public boolean save(TZAccountInfo tzAccountInfo) {
		try {
			create(tzAccountInfo);
			return true;
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<ReturnProductVO> getPurchaseList(Long id, int type, Long curBusId, int page, int pageSize)
			throws ServiceException {
		try {
			return tZAccountInfoDAO.getPurchaseList(id, type, curBusId, page, pageSize);
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}


	}

	@Override
	public Long getPurchaseTotal(long id) throws ServiceException {
		try {
			return tZAccountInfoDAO.getPurchaseTotal(id);
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	@Override
	public TZAccountInfo findByAccountId(Long id) throws ServiceException {
		try {
			return tZAccountInfoDAO.findByAccountId(id);
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	@Override
	public boolean updateAccountInfo(TZAccountInfo tzAccountInfo) throws ServiceException {
		this.update(tzAccountInfo);
		return true;
	}

	@Override
	public List<TZAccountInfo> getListByAccountId(Long id)
			throws ServiceException {
		try {
			return tZAccountInfoDAO.getListByAccountId(id);
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	@Override
	public List<ReturnProductVO> getProList(long proId, int page, int pageSize) throws ServiceException {
		try {
			List<ReturnProductVO> productVOs = tZAccountInfoDAO.getProList(proId, page, pageSize);
			if (productVOs == null || productVOs.size() == 0) {
				return productVOs;
			}
			for(int i = 0 ; i < productVOs.size(); i++){
				Long reportId= productVOs.get(i).getReportId();
				if(reportId==null || reportId <= 0L){
					continue;
				}
				String reportUrl = tZAccountInfoDAO.getReportUrlByReportId(reportId);
				productVOs.get(i).setReportUrl(reportUrl);
			}
			return productVOs;
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	@Override
	public Long getTZReceiptTotal(long tzId) throws ServiceException {
		try {
			return tZAccountInfoDAO.getTZReceiptTotal(tzId);
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	@Override
	public List<ReturnProductVO> loadingDetailGoods(Long tzId, int busType, Integer outStatus, Long curBusId,
													int page, int pageSize) throws ServiceException {
		try {
			return tZAccountInfoDAO.loadingDetailGoods(tzId, busType, outStatus, curBusId, page, pageSize);
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	@Override
	public long getloadingDetailGoodsTotal(Long tzId) throws ServiceException {
		try {
			return tZAccountInfoDAO.getloadingDetailGoodsTotal(tzId);
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	@Override
	public List<TZAccountInfo> getTZInfolist(Long tzId) throws ServiceException {
		try {
			return tZAccountInfoDAO.getTZInfolist(tzId);
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	@Override
	public void deleteInfoByaccountId(long tZId) throws ServiceException {
		try {
			tZAccountInfoDAO.deleteInfoByaccountId(tZId);
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}


}