package com.gettec.fsnip.fsn.service.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZAccountDAO;
import com.gettec.fsnip.fsn.dao.account.TZStockDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZBusaccountInfoOut;
import com.gettec.fsnip.fsn.model.account.TZIntakeLog;
import com.gettec.fsnip.fsn.model.account.TZStock;
import com.gettec.fsnip.fsn.model.account.TZStockInfo;
import com.gettec.fsnip.fsn.service.account.TZIntakeLogService;
import com.gettec.fsnip.fsn.service.account.TZStockInfoService;
import com.gettec.fsnip.fsn.service.account.TZStockService;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;
import com.gettec.fsnip.fsn.vo.account.WholeSaleVO;
import com.gettec.fsnip.fsn.vo.business.BusinessMarketVO;
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
@Service(value="tZStockService")
public class TZStockServiceImpl extends BaseServiceImpl<TZStock,TZStockDAO> implements TZStockService {

    @Autowired
    TZStockDAO          tZStockDAO;
    @Autowired
    BusinessUnitService businessUnitService;
    @Autowired
    TZStockInfoService  tzStockInfoService;
    @Autowired
    TZAccountDAO        tzAccountDAO;
    @Autowired
    TZIntakeLogService  tzIntakeLogService;

    SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public TZStockDAO getDAO() {
        return tZStockDAO;
    }

    /**
    * @author chenxiaolin
    */
	@Override
	public long getMaxId() {
		return tZStockDAO.getMaxId();
	}

	/**
	 * @author chenxiaolin
	 */
	@Override
	public boolean save(TZStock saveTZStock){
		 try {
			create(saveTZStock);
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
	public boolean updateStock(TZStock tZStock) {
		try {
			update(tZStock);
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
	public WholeSaleVO findByProductId(Long productId)throws ServiceException {
		try {
			return tZStockDAO.findByProductId(productId);
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

	/* 
	 * @author chenxiaolin
	 */
	@Override
	public List<TZStock> getTZStockListByPage(int page, int pageSize)
			throws ServiceException {
		try {
			return tZStockDAO.getTZStockListByPage(page, pageSize);
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}

    /**
     * 保存库存信息 没有更新功能
     * @author HY
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Model createStore(Long org, String barcode, Integer num,String qs,Long stackid, Model model) throws ServiceException {
        try {
            Long busId = businessUnitService.findIdByOrg(org);
            ReturnProductVO productVO = tZStockDAO.findVOByBarcode(barcode);
            boolean success = false;
            if (busId != null&&productVO instanceof ReturnProductVO) {
                Long result = 0L;
                /* 修改库存数量 */
                TZStock stack_orig = null;
                if(stackid!=null && -1L!=stackid && !Long.valueOf(-1).equals(stackid)){
                    stack_orig = tZStockDAO.findById(stackid);
                    if(stack_orig!=null){
                        Long stackCount_orig = stack_orig.getProductNum(); //原来的库存数量
                        Long editCount = Long.valueOf(num!=null?num:0); //用户修改后的库存数量
                        result = editCount - stackCount_orig; //库存数量变动的数量
                        stack_orig.setProductNum(editCount);
                        update(stack_orig);
                    }
                }else{ /* 新增库存 */
                    stack_orig = new TZStock();
                    stack_orig.setBusinessId(busId);
                    stack_orig.setProductNum(Long.valueOf(num!=null?num:0));
                    stack_orig.setProductId(productVO.getProductId());
                    stack_orig.setQsNo(qs);
                    create(stack_orig);
                }

                /* 保存产品库存信息 */
                TZStockInfo tzStockInfo = new TZStockInfo();
                tzStockInfo.setBusinessId(busId);
                tzStockInfo.setProductBatch(productVO.getBatch());
                tzStockInfo.setProductId(productVO.getProductId());
                tzStockInfo.setCreateType(result != 0L ? 2 : 1); //标识数据来源 流水库存:0，台账库存:1，2用户手动修改库存数据
                tzStockInfo.setInDate(SDF.format(new Date()));
                tzStockInfo.setProductFormat(productVO.getFormat());
                tzStockInfo.setStockId(stack_orig.getId());
                tzStockInfo.setQsNo(stack_orig.getQsNo());

                tzStockInfo.setType(result < 0L ? 1 : 0);
                if(result != 0L){
                    tzStockInfo.setProductNum(Math.abs(result));
                    tzStockInfoService.create(tzStockInfo);
                }else if(result == 0L && stackid <= 0){
                    tzStockInfo.setProductNum(stack_orig.getProductNum());
                    tzStockInfoService.create(tzStockInfo);
                }
                success = true;
            }
            model.addAttribute("success", success);
            return model;
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        } catch (JPAException e) {
            throw new ServiceException(e.getMessage(),e.getException());
        }
    }

    /**
     * 按条形码查找库存中是否已存在相关信息
     * @author HY
     */
    @Override
    public Model getStoreProductByBarcode(String barcode, Model model) throws ServiceException {
        try {
            ReturnProductVO productVO = tZStockDAO.findVOByBarcode(barcode);
            if(productVO!=null){
                productVO.setQsNoList(tZStockDAO.getQsNoByBarcode(productVO.getProductId()));
            }
            if(productVO instanceof ReturnProductVO){
                model.addAttribute("data",productVO);
            }else{
                model.addAttribute("data",null);
            }
            return model;
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        }
    }

    /**
     * 验证该条码在库存在是否已存在
     * @auhtor HY
     */
    @Override
    public Model validatebarcode(Long org, String barcode, String qs, Model model) throws ServiceException {
        try {
            boolean isExist = false;
            List<TZStock> orig_tzStock = tZStockDAO.getStoreProductByBarcode(barcode, org, qs);
            if(orig_tzStock != null && orig_tzStock.size() > 0){
                isExist = true;
            }
            model.addAttribute("isExist", isExist);
            return model;
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        }
    }
    /* 退貨修改庫存 */
    @Override
    public void updateTZStock(AccountOutVO accountOut, TZBusaccountInfoOut outInfo) throws ServiceException {
        try {
            /* 查找庫存 */
            TZStock orig_tzStock = tZStockDAO.getByProIdAndSelfBusId(outInfo.getProductId(),accountOut.getOutBusId(),outInfo.getQsNo());
            if(orig_tzStock instanceof TZStock){
                /* 更新库存数量 */
                Long count = (orig_tzStock.getProductNum()!=null?orig_tzStock.getProductNum():0)-outInfo.getProductNum();
                orig_tzStock.setProductNum(count<0?0:count);
                update(orig_tzStock);
            }else{
                orig_tzStock = new TZStock();
                orig_tzStock.setProductNum(outInfo.getProductNum());
                orig_tzStock.setBusinessId(accountOut.getOutBusId());
                orig_tzStock.setProductId(outInfo.getProductId());
                orig_tzStock.setQsNo(outInfo.getQsNo());
                create(orig_tzStock);
            }
            /* 保存退货库存信息 */
            TZStockInfo tzStockInfo = new TZStockInfo();
            tzStockInfo.setStockId(orig_tzStock.getId());
            tzStockInfo.setBusinessId(orig_tzStock.getBusinessId());
            tzStockInfo.setProductNum(outInfo.getProductNum());
            tzStockInfo.setType(1);
            tzStockInfo.setCreateType(1);
            tzStockInfo.setProductBatch(outInfo.getProductBatch());
            tzStockInfo.setProductId(outInfo.getProductId());
            tzStockInfo.setQsNo(outInfo.getQsNo());
            tzStockInfo.setInDate(SDF.format(new Date()));
            tzStockInfoService.create(tzStockInfo);
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        }
    }

    /**
     * 根据企业id和产品id 确认一条库存信息
     */
    @Override
    public TZStock findByProductIdAndBusId(Long productId, Long inBusId,String sqNo) throws ServiceException {
        String condition = "WHERE e.productId = ?1 AND e.businessId = ?2 AND e.qsNo = ?3";
        try {
            List<TZStock> lists = tZStockDAO.getListByCondition(condition, new Object[]{productId, inBusId,sqNo});
            return lists!=null&&lists.size()>0?lists.get(0):null;
        } catch (JPAException jpae) {
            throw new ServiceException(jpae.getMessage(),jpae.getException());
        }
    }

    private TZStockInfo setZTStoreInfo(ReturnProductVO productVO, TZStock orig_tzStock) {
        TZStockInfo tzStockInfo = new TZStockInfo();
        tzStockInfo.setBusinessId(orig_tzStock.getBusinessId());
        tzStockInfo.setStockId(orig_tzStock.getId());
        tzStockInfo.setProductNum(orig_tzStock.getProductNum());
        tzStockInfo.setProductId(productVO.getId());
        tzStockInfo.setCreateType(0);
        tzStockInfo.setProductBatch(productVO.getBatch());
        tzStockInfo.setProductFormat(productVO.getFormat());
        return tzStockInfo;
    }

	@Override
	public List<TZStock> getTZStockListByCurBusAndProId(Long inBusId,Long productId) throws ServiceException {
		try {
				return tZStockDAO.getTZStockListByCurBusAndProId(inBusId, productId);
			} catch (DaoException e) {
				throw new ServiceException(e.getMessage(),e.getException());
		}
	}

	@Override
	public TZStock findByProIdAndQsNo(Long busId, String qsNo,Long productId)
			throws ServiceException {
		try {
			return tZStockDAO.findByProIdAndQsNo(busId, qsNo, productId);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage(),e.getException());
			
		}
	}

    /**
     * 加载企业的所有类型
     * @author HY
     */
    @Override
    public List<BusinessMarketVO> loadBusinessType() throws ServiceException {
        try {
            return tZStockDAO.loadBusinessType();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(),e.getException());
        }
    }

    /**
     * 台帐 生产企业添加自己的产品到库存中（生产企业）
     * @author HY
     */
    @Override
    public Model loadSelfProductList(Long myOrg, String pname, String pbar, int page, int pageSize, Model model) throws ServiceException {
        try {
            /* 1 获取当前企业的id */
            String[] busT = tzAccountDAO.getBusType(myOrg);
            String busType = "";
            if(busT!=null){
                busType = busT[1];
            }
            List<ReturnProductVO> lists = tzAccountDAO.loadSelfProductList(myOrg,pname,pbar,busType,page,pageSize);
            long totals = tzAccountDAO.loadSelfProductListTotals(myOrg,pname,pbar,busType);
            model.addAttribute("data",lists);
            model.addAttribute("totals",totals);
            return model;
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        }
    }

    /**
     * 企业自己将自己的产品添加到库存
     * @author HY
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Model addSelfProductToStore(Long myOrg,String userName, AccountOutVO accountOut, Model model) throws ServiceException {
        Long busId = null;
        String busType = "";
        try {
        /* 1 获取当前企业的id */
        String[] busT = tzAccountDAO.getBusType(myOrg);
        if(busT!=null){
            busId = Long.valueOf(busT[0]);
            busType = busT[1];
        }

        /* 2 查询库存中是否有该产品存在 */
        List<ReturnProductVO> lists = accountOut.getProList();
        if(lists != null && lists.size() > 0){
            for(ReturnProductVO vo : lists){

                TZStock orig_Stock = tZStockDAO.getByProIdAndSelfBusId(vo.getProductId(),busId,vo.getQsNumber());
                /* 3. 对库存进行操作 */
                String batch = vo.getBatch();
                int reportStatus = 0;
                if(batch!=null&&!"".equals(batch)){
                    reportStatus = tZStockDAO.getReportStatus(batch, vo.getProductId(),busType);
                }
                if(orig_Stock instanceof TZStock){
                    Long count = orig_Stock.getProductNum() + vo.getReturnCount();
                    orig_Stock.setProductNum(count);
                    orig_Stock.setReportStatus(reportStatus);

                }else{
                    orig_Stock = new TZStock();
                    orig_Stock.setBusinessId(busId);
                    orig_Stock.setProductId(vo.getProductId());
                    orig_Stock.setProductNum(vo.getReturnCount());
                    orig_Stock.setQsNo(vo.getQsNumber());
                    orig_Stock.setReportStatus(reportStatus);
                }
                updateStock(orig_Stock,vo,userName);
                model.addAttribute("add",true);
            }

        }} catch (DaoException daoe) {
            model.addAttribute("add",false);
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        }
        return model;
    }

    /**
     * 台帐 在商品入库页面加载已经添加商品入库了的产品
     * @author HY
     */
    @Override
    public Model loadIntakeProductList(Long myOrg, String pname, String pbarcode, int page, int pageSize, Model model) throws ServiceException {
        Long busId = businessUnitService.findIdByOrg(myOrg);
        try {
            List<ReturnProductVO> lists = tZStockDAO.loadIntakeProductList(busId,pname,pbarcode, page, pageSize);
            Long totals = tZStockDAO.loadIntakeProductTotals(busId, pname, pbarcode);
            model.addAttribute("data",lists).addAttribute("totals",totals);
            return model;
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        }

    }

    /**
     * 企业将自己的产品添加到库存时,如果库存已存在就先更新
     * @param orig_stock
     * @param vo
     */
    private void updateStock(TZStock orig_stock, ReturnProductVO vo,String userName) throws ServiceException {
        /* 1. 修改库存主表信息 */
        if(orig_stock instanceof TZStock && orig_stock.getId() != null){
            this.update(orig_stock);
        }else{
            this.create(orig_stock);
        }
        /* 2. 修改库存详情表信息 */
        TZStockInfo stockInfo = new TZStockInfo();
        stockInfo.setStockId(orig_stock.getId());
        stockInfo.setBusinessId(orig_stock.getBusinessId());
        stockInfo.setProductId(orig_stock.getProductId());
        stockInfo.setProductNum(vo.getReturnCount());
        stockInfo.setProductBatch(vo.getBatch());
        stockInfo.setProductFormat(vo.getFormat());
        stockInfo.setInDate(SDF.format(new Date()));
        stockInfo.setQsNo(vo.getQsNumber());
        stockInfo.setType(0);
        stockInfo.setIntake(1);
        stockInfo.setCreateType(1);
        tzStockInfoService.create(stockInfo);
        /* 记录商品入库的日志 */
        upIntakeLog(stockInfo,orig_stock,userName);
    }

    /* 记录商品入库操作日志 */
    private void upIntakeLog(TZStockInfo stockInfo,TZStock orig_stock,String userName) throws ServiceException {

        TZIntakeLog log = new TZIntakeLog();
        log.setBusId(orig_stock.getBusinessId());
        log.setProductId(orig_stock.getProductId());
        log.setOperationDate(SDF.format(new Date()));
        log.setOperator(userName);
        log.setOperationCount(stockInfo.getProductNum());
        log.setStockId(orig_stock.getId());
        log.setStockinfoId(stockInfo.getId());
        log.setUuid(SalesUtil.createGUID());
        TZIntakeLog orig_Log = tzIntakeLogService.getTZIntakeLogForUuid(log.getUuid());
        if(!(orig_Log instanceof TZIntakeLog)){
            tzIntakeLogService.update(log);
        }

    }

}
