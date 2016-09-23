package com.gettec.fsnip.fsn.service.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZStockInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZBusAccountOut;
import com.gettec.fsnip.fsn.model.account.TZBusaccountInfoOut;
import com.gettec.fsnip.fsn.model.account.TZStock;
import com.gettec.fsnip.fsn.model.account.TZStockInfo;
import com.gettec.fsnip.fsn.service.account.TZBusAccountOutService;
import com.gettec.fsnip.fsn.service.account.TZBusaccountInfoOutService;
import com.gettec.fsnip.fsn.service.account.TZStockInfoService;
import com.gettec.fsnip.fsn.service.account.TZStockService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 
 * Author:chenxiaolin
 */
@Service(value="tZStockInfoService")
public class TZStockInfoServiceImpl extends BaseServiceImpl<TZStockInfo,TZStockInfoDAO> implements TZStockInfoService {

    @Autowired TZStockInfoDAO tZStockInfoDAO;
    @Autowired
    TZBusAccountOutService tzBusAccountOutService;
    @Autowired
    TZBusaccountInfoOutService tzBusaccountInfoOutService;
    @Autowired
    TZStockService tzStockService;
    SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public TZStockInfoDAO getDAO() {
        return tZStockInfoDAO;
    }
    /* 
     * @author chenxiaolin
     */
	@Override
	public boolean save(TZStockInfo tZStockInfo){
		try {
			create(tZStockInfo);
			return true;
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return false;
	}

	/* 
     * @author chenxiaolin
     */
	@Override
	public boolean updateStockInfo(TZStockInfo tZStockInfo) {
		try {
			update(tZStockInfo);
			return true;
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return false;
	}

    /**
     * 加载商品库存信息
     * @author HY
     */
    @Override
    public Model loadStoreInfoList(Long org, Integer page, Integer pageSize, String str, Model model) throws ServiceException {
        try {
            String condition = "";
            if(str!=null&&!"".equals(str)){
                condition = " AND ( p.barcode LIKE '%"+str+"%' OR p.name LIKE '%"+str+"%' ) ";
            }
            List<ReturnProductVO> lists = tZStockInfoDAO.loadStoreInfoList(org, page, pageSize, condition);
            Long totals = tZStockInfoDAO.loadStoreInfoListTotals(org, condition);
            model.addAttribute("data",lists);
            model.addAttribute("totals", totals);
            return model;
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        }
    }

	/**
     * 根据库存ID 加载产品库存明细数据List
     * @author HY
     */
    @Override
    public Model loadStoreDetailList(Integer page, Integer pageSize, Long sid, Model model) throws ServiceException {
        try {
            List<ReturnProductVO> lists = tZStockInfoDAO.loadStoreDetailList(page, pageSize, sid);
            Long totals = tZStockInfoDAO.loadStoreDetailListTotals(sid);
            model.addAttribute("data",lists);
            model.addAttribute("totals",totals);
            return model;
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        }
    }

    /**
     * 确认收到退货信息
     * @author HY
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Model receiptReturnGoods(Long outId, Model model) throws ServiceException {
        boolean stockSu = false;
        boolean accountOut = false;
        /* 1.根据去也id和产品id查找库存信息 */
        TZBusAccountOut orig_accountOut = tzBusAccountOutService.findById(outId);
        if(orig_accountOut.getOutStatus()==1 && orig_accountOut.getInStatus()!=1){
            List<TZBusaccountInfoOut> outInfos = tzBusaccountInfoOutService.getListByOutId(outId);
            if(orig_accountOut!=null&&outInfos!=null&&outInfos.size()>0){
                for(int i = 0; i < outInfos.size();i++){
                    TZStock tzStock = tzStockService.findByProductIdAndBusId(outInfos.get(i).getProductId(), orig_accountOut.getInBusId(),outInfos.get(i).getQsNo());
                    if(tzStock!=null){
                        tzStock.setProductNum(tzStock.getProductNum() + outInfos.get(i).getProductNum());
                        tzStockService.update(tzStock);
                        stockSu = true;
                    }else{
                        tzStock = new TZStock();
                        tzStock.setBusinessId(orig_accountOut.getInBusId());
                        tzStock.setProductId(outInfos.get(i).getProductId());
                        tzStock.setProductNum(outInfos.get(i).getProductNum());
                        tzStock.setQsNo(outInfos.get(i).getQsNo());
                        tzStockService.create(tzStock);
                        stockSu = true;
                    }
                    if(stockSu){
                        addStockInfo(outInfos.get(i),tzStock);
                    }
                }
                if(stockSu){
                    orig_accountOut.setInDate(SDF.format(new Date()));
                    orig_accountOut.setInStatus(1);
                    tzBusAccountOutService.update(orig_accountOut);
                    accountOut = true;
                }
            }
        }
        model.addAttribute("save",accountOut);
        return model;
    }

    /**
     * 创建库存详情
     * @author HY
     */
    private void addStockInfo(TZBusaccountInfoOut tzBusaccountInfoOut, TZStock tzStock) throws ServiceException {
        TZStockInfo info = setTZStockInfo(tzBusaccountInfoOut,tzStock);
        create(info);
    }

    private TZStockInfo setTZStockInfo(TZBusaccountInfoOut infoOut, TZStock tzStock) {
        TZStockInfo info = new TZStockInfo();
        info.setBusinessId(tzStock.getBusinessId());
        info.setProductNum(infoOut.getProductNum());
        info.setInDate(SDF.format(new Date()));
        info.setProductId(tzStock.getProductId());
        info.setProductBatch(infoOut.getProductBatch());
        info.setStockId(tzStock.getId());
        info.setQsNo(infoOut.getQsNo());
        info.setType(0);
        info.setCreateType(1);
        return info;
    }

	@Override
	public List<TZStockInfo> getTZStockInfoList(long stockId, Long inBusId)
			throws ServiceException {
		 try {
	           return tZStockInfoDAO.getTZStockInfoList(stockId,inBusId);
	        } catch (DaoException daoe) {
	            throw new ServiceException(daoe.getMessage(),daoe.getException());
	        }
	}
	@Override
	public void deleteListByStockId(Long stockId) throws ServiceException {
		 try {
	          tZStockInfoDAO.deleteListByStockId(stockId);
	        } catch (DaoException daoe) {
	            throw new ServiceException(daoe.getMessage(),daoe.getException());
	        }
	}

}
