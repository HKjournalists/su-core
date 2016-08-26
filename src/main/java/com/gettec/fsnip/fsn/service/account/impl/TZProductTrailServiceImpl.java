package com.gettec.fsnip.fsn.service.account.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.vo.account.TZOriginGoodsInfoVO;
import com.gettec.fsnip.fsn.vo.account.TZOriginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.account.TZProductTrailDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZProductTrail;
import com.gettec.fsnip.fsn.service.account.TZProductTrailService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;
import org.springframework.ui.Model;

/**
 * Created by HY on 2015/5/19.
 * Author:chenxiaolin
 */
@Service(value="tZProductTrailService")
public class TZProductTrailServiceImpl extends BaseServiceImpl<TZProductTrail,TZProductTrailDAO> implements TZProductTrailService {

    @Autowired TZProductTrailDAO tZProductTrailDAO;

    @Override
    public TZProductTrailDAO getDAO() {
        return tZProductTrailDAO;
    }

    /**
     * 
     * @author chenxiaolin
     */
    @Override
    public boolean save(AccountOutVO accountOut, Long org) throws ServiceException {
        try {
            boolean isSuccess = false;
            List<ReturnProductVO> pVOs = accountOut.getProList();//获取商品列表
            for (ReturnProductVO vo : pVOs) {
            	TZProductTrail tzAccountInfo = new TZProductTrail(vo);//没有设AccountId 的值
            	TZProductTrail orig_Info = null;
                if (tzAccountInfo.getId() != null) {//跟新的情况
                    orig_Info = tZProductTrailDAO.findById(tzAccountInfo.getId());
                    if(orig_Info instanceof TZProductTrail){
                        setReturnProduct(orig_Info, orig_Info);
                        update(orig_Info);
                        isSuccess = true;
                    }
                }else{//新建的情况
                    orig_Info = new TZProductTrail();
                    orig_Info.setOutBusId(accountOut.getOutBusId());//供应商ID
                    orig_Info.setInBusId(accountOut.getInBusId());//当前企业ID
                    setReturnProduct(orig_Info, tzAccountInfo);
                    create(orig_Info);
                    isSuccess = true;
                }
            }
            return isSuccess;
        } catch (JPAException jpae) {
            throw new ServiceException(jpae.getMessage(),jpae.getException());
        } catch (ParseException pe) {
            throw new ServiceException(pe.getMessage(),pe);
        }
    }

    //设置进货商品
    private void setReturnProduct(TZProductTrail orig_info, TZProductTrail tzAccountInfo) {
    	orig_info.setProductId(tzAccountInfo.getProductId());
    	orig_info.setBarcode(tzAccountInfo.getBarcode());
    	orig_info.setProductBatch(tzAccountInfo.getProductBatch());
    	orig_info.setProductNum(tzAccountInfo.getProductNum()!=null?tzAccountInfo.getProductNum():0);
    	orig_info.setProductionDate(tzAccountInfo.getProductionDate());
    	orig_info.setOverDate(tzAccountInfo.getOverDate());
    }

	@Override
	public boolean save(TZProductTrail tZProductTrail) {
		try {
			create(tZProductTrail);
			return true;
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateProductTrail(TZProductTrail tZProductTrail) {
		try {
			update(tZProductTrail);
			return true;
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return false;
	}

    /**
     * 溯源列表，加载产品详情
     * Created By HY On 2015-06-04
     */
    @Override
    public Model loadAllGoodsList(Long organization, int page, int pageSize,
                                  String cond,Model model) throws ServiceException {
        String condition = "";
        if(cond!=null && !"".equals(cond)){
            condition += " AND p.barcode = ?1 OR p.name LIKE ?2 ";
        }
        try {
            List<TZOriginGoodsInfoVO> lists = tZProductTrailDAO.loadAllGoodsList(page, pageSize, condition, cond);
            Long totals = tZProductTrailDAO.loadAllGoodsListTotals(condition, cond);
            model.addAttribute("data",lists).addAttribute("totals", totals);
            return model;
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(), daoe.getException());
        }
    }

    /**
     * 根据条形码和qs号加载产品溯源
     * Create By HY On 2015-06-04
     */
    @Override
    public List<TZOriginVO> loadGoodsOrigin( String barcode,String batch) throws ServiceException {
        try {
            List<TZOriginVO> lists = null;
            TZProductTrail result = tZProductTrailDAO.loadGoodsOrigin(barcode, 0,batch);
            if(result instanceof TZProductTrail){
                lists = new ArrayList<TZOriginVO>();
                /* 1.根目录（主体企业 ）*/
                String rootBusName = tZProductTrailDAO.findByproductId(result.getProductId());
                TZOriginVO vo = setTZOriginVO(result);
                vo.setShowName(rootBusName);
                vo.setId(vo.getTradingBusId() + "#" + result.getId() + "#_# ");
                //List<TZProductTrail> childrenLists = tZProductTrailDAO.getOriginChildrensByBatch(result.getOutBusId(), result.getBarcode(), result.getProductBatch(),result.getId());
                /* 2. 分支（交易记录的企业）*/
                //boolean isChildrens = getOriginChildrenToRoot(childrenLists);
                vo.setHasChildren(true);
                lists.add(vo);
            }
            return lists;
        } catch (DaoException daoe) {
           throw new ServiceException(daoe.getMessage(),daoe.getException());
        }
    }

    private boolean getOriginChildrenToRoot(List<TZProductTrail> lists) throws DaoException {
        /* 查找根节点 */
        if(lists!=null&&lists.size()>0){
            for(int i = 0; i<lists.size(); i++){
                List<TZProductTrail> childrens = tZProductTrailDAO.getOriginChildren(lists.get(i).getProductId(),lists.get(i).getQsNo(),lists.get(i).getInBusId());
                if(childrens!=null && childrens.size()>0){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<TZOriginVO> loadGoodsOrigin(String barcode,String idAndBatch,String batch) throws ServiceException {
        /* 根据轨迹id 加载当前轨迹 */
        List<TZOriginVO> lists = new ArrayList<TZOriginVO>();
        String[] arr = idAndBatch.split("#_#");
        if (arr == null || arr.length == 0) {
            return null;
        }
        Long outBusId = Long.valueOf(arr[0].split("#")[0]);
        Long trialId = Long.valueOf(arr[0].split("#")[1]);
        String batch_new  = arr[1];
        if (batch_new != null && !" ".equals(batch_new)){
            batch = batch_new;
        }
        try {
            List<TZProductTrail> childrenLists = tZProductTrailDAO.getOriginChildrens(outBusId, trialId, barcode, batch);
            if(childrenLists!=null&&childrenLists.size()>0){
                for(int i = 0 ;i<childrenLists.size();i++){
                    List<TZProductTrail> cLists = tZProductTrailDAO.getOriginChildrensByBatch(childrenLists.get(i).getInBusId(), childrenLists.get(i).getBarcode(), childrenLists.get(i).getProductBatch(),childrenLists.get(i).getId());
                    TZOriginVO vo = setTZOriginVO(childrenLists.get(i));
                    vo.setShowName(vo.getTradingBusName()+"||"+vo.getTradingbatch()+"||"+vo.getTradingDate());
                    vo.setId(vo.getTradingBusId() + "#" + vo.getTrailId() + "#_#" + vo.getTradingbatch());
                    vo.setHasChildren(false);
                    if(cLists!=null&&cLists.size()>0){
                        vo.setHasChildren(true);
                    }
                    lists.add(vo);
                }
            }else{
                TZProductTrail orig_trail = tZProductTrailDAO.findById(trialId);
                if(orig_trail instanceof TZProductTrail){
                    TZOriginVO vo = setTZOriginVO(orig_trail);
                    vo.setHasChildren(false);
                    vo.setShowName(vo.getTradingBusName() + "||" + vo.getTradingbatch() + "||" + vo.getTradingDate());
                    vo.setId(vo.getTradingBusId() + "#" + trialId + "#_#" + vo.getTradingbatch());
                    lists.add(vo);
                }
            }

            return lists;
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        } catch (JPAException e) {
            throw new ServiceException(e.getMessage(),e.getException());
        }
    }

    private TZOriginVO setTZOriginVO(TZProductTrail result) throws DaoException {

        TZOriginVO vo = new TZOriginVO();
        vo.setTrailId(result.getId());
        vo.setTradingDate(result.getAccountDate() != null ? result.getAccountDate().toString() : "");
        Long sum = tZProductTrailDAO.getProductNum(result.getBarcode(),result.getOutBusId(),result.getProductBatch(),result.getInBusId());
        vo.setTradingNum(sum);
        vo.setTradingType(result.getType()==0?"进货台账":"退货台账");
        Object[] supplybus = tZProductTrailDAO.getBusInfoById(result.getOutBusId());
        vo.setTradingbatch(result.getProductBatch());
        if(supplybus!=null && supplybus.length > 0){
            vo.setSupplyBusId(supplybus[0] != null ? Long.valueOf(supplybus[0].toString()) : -1);
            vo.setSupplyBusName(supplybus[1] != null ? supplybus[1].toString() : "");
        }
        Object[] tradingBus = tZProductTrailDAO.getBusInfoById(result.getInBusId());
        if(tradingBus!=null&&tradingBus.length>0){
            vo.setTradingBusId(tradingBus[0]!=null?Long.valueOf(tradingBus[0].toString()):-1);
            vo.setTradingBusName(tradingBus[1] != null ? tradingBus[1].toString() : "");
        }
        return vo;
    }

	@Override
	public void deleteTrainByTzId(long tzId, Long busId)
			throws ServiceException {
		try {
			tZProductTrailDAO.deleteTrainByTzId(tzId,busId);
		} catch (Exception e) {
			throw new ServiceException("TZProductTrailServiceImpl-->deleteTrainByTzId",e);
		}
	}
}
