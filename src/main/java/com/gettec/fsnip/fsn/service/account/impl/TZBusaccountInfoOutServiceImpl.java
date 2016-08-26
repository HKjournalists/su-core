package com.gettec.fsnip.fsn.service.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZAccountDAO;
import com.gettec.fsnip.fsn.dao.account.TZBusaccountInfoOutDAO;
import com.gettec.fsnip.fsn.dao.recycle.RecycleDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZBusAccountOut;
import com.gettec.fsnip.fsn.model.account.TZBusaccountInfoOut;
import com.gettec.fsnip.fsn.model.account.TZProductTrail;
import com.gettec.fsnip.fsn.model.product.ProductDestroyRecord;
import com.gettec.fsnip.fsn.recycle.Process_mode;
import com.gettec.fsnip.fsn.recycle.Recycle_reason;
import com.gettec.fsnip.fsn.service.account.TZBusaccountInfoOutService;
import com.gettec.fsnip.fsn.service.account.TZProductTrailService;
import com.gettec.fsnip.fsn.service.account.TZStockService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HY on 2015/5/19.
 * desc:
 */
@Service(value="tzBusaccountInfoOutService")
public class TZBusaccountInfoOutServiceImpl extends BaseServiceImpl<TZBusaccountInfoOut,TZBusaccountInfoOutDAO> implements TZBusaccountInfoOutService {

    @Autowired TZBusaccountInfoOutDAO tzBusaccountInfoOutDAO;
    @Autowired TZStockService tzStockService;
    @Autowired ProductService productService;
    @Autowired TZProductTrailService tzProductTrailService;
    @Autowired TZAccountDAO tZAccountDAO;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //add by ltg 20160815 确定退货时在产品回收/销毁记录表中记录下退货信息 start
    @Autowired
    RecycleDAO recycleDAO;
    //add by ltg 20160815 确定退货时在产品回收/销毁记录表中记录下退货信息 end

    @Override
    public TZBusaccountInfoOutDAO getDAO() {
        return tzBusaccountInfoOutDAO;
    }

    /**
     * 保存供销关系中的产品信息
     * @author HY
     */
    @Override
    public List<TZBusaccountInfoOut> save(AccountOutVO accountOut,  TZBusAccountOut out) throws ServiceException {
        try {
            List<TZBusaccountInfoOut> list = new ArrayList<TZBusaccountInfoOut>();
            List<ReturnProductVO> pVOs = accountOut.getProList();
            List<TZBusaccountInfoOut> orig_infoOuts = tzBusaccountInfoOutDAO.findListByOutId(out.getId());
            //add by ltg 20160815 确定退货时在产品回收/销毁记录表中记录下退货信息 start
            String outBusName = accountOut.getOutBusName();//退货商
            String inBusName = accountOut.getInBusName();//供应商
            //add by ltg 20160815 确定退货时在产品回收/销毁记录表中记录下退货信息 end
            
            for (ReturnProductVO vo : pVOs) {
                TZBusaccountInfoOut tzAccountInfoOut = new TZBusaccountInfoOut(vo);//没有设AccountId 的值
                TZBusaccountInfoOut orig_InfoOut = null;
                if (tzAccountInfoOut.getId() != null&& tzAccountInfoOut.getId() != -1) {//跟新的情况
                    orig_InfoOut = tzBusaccountInfoOutDAO.findById(tzAccountInfoOut.getId());
                    if(orig_InfoOut instanceof TZBusaccountInfoOut){
                        setReturnProduct(orig_InfoOut, tzAccountInfoOut);
                        orig_InfoOut.setProductNum(vo.getReturnCount());
                        orig_InfoOut.setBusAccountId(out.getId());
                        orig_InfoOut.setProblem_describe(vo.getProblem_describe());
                        update(orig_InfoOut);
                        /* 更新庫存信息 */
                        if(accountOut.getOutStatus()==1){
                            /* 将该订单对应的产从orig_infoOuts记录中移除 */
                            removeOrigOutInfo(orig_infoOuts,orig_InfoOut);
                            tzStockService.updateTZStock(accountOut, orig_InfoOut);
                            /* 创建台账轨迹 */
                            tzProductTrailService.create(setTZProductTrail(orig_InfoOut,out));
                            //add by ltg 20160815 确定退货时在产品回收/销毁记录表中记录下退货信息 start
                            ProductDestroyRecord productDestroyRecord = new ProductDestroyRecord();
                            productDestroyRecord.setName(vo.getName());
                            productDestroyRecord.setBarcode(vo.getBarcode());
                            productDestroyRecord.setBatch(vo.getBatch());
                            productDestroyRecord.setNumber(vo.getReturnCount().toString());
                            for (Recycle_reason recycle_reason : Recycle_reason.values()) {
                            	if (recycle_reason.getValue().equals(vo.getProblem_describe())) {
                                    productDestroyRecord.setProblem_describe(recycle_reason);
                                    break;
                            	}
                            }
                            productDestroyRecord.setProcess_time(accountOut.getOutDate());
                            productDestroyRecord.setProcess_mode(Process_mode.RETURN_GOODS);
                            productDestroyRecord.setHandle_name(outBusName);
                            productDestroyRecord.setRecieve_name(inBusName);
                            productDestroyRecord.setOperation_user(AccessUtils.getUserName().toString());
                            recycleDAO.insert_recycle_record(productDestroyRecord);
                            //add by ltg 20160815 确定退货时在产品回收/销毁记录表中记录下退货信息 end
                        }
                        list.add(orig_InfoOut);
                    }
                }else{//新建的情况
                    orig_InfoOut = new TZBusaccountInfoOut();
                    setReturnProduct(orig_InfoOut, tzAccountInfoOut);
                    orig_InfoOut.setProductNum(vo.getReturnCount());
                    orig_InfoOut.setBusAccountId(out.getId());
                    orig_InfoOut.setProblem_describe(vo.getProblem_describe());
                    create(orig_InfoOut);
                    /* 更新庫存信息 */
                    if(accountOut.getOutStatus()==1){
                        tzStockService.updateTZStock(accountOut, orig_InfoOut);
                        /* 创建台账轨迹 */
                        tzProductTrailService.create(setTZProductTrail(orig_InfoOut, out));
                        //add by ltg 20160815 确定退货时在产品回收/销毁记录表中记录下退货信息 start
                        ProductDestroyRecord productDestroyRecord = new ProductDestroyRecord();
                        productDestroyRecord.setName(vo.getName());
                        productDestroyRecord.setBarcode(vo.getBarcode());
                        productDestroyRecord.setBatch(vo.getBatch());
                        productDestroyRecord.setNumber(vo.getReturnCount().toString());
                        for (Recycle_reason recycle_reason : Recycle_reason.values()) {
                        	if (recycle_reason.getValue().equals(vo.getProblem_describe())) {
                                productDestroyRecord.setProblem_describe(recycle_reason);
                                break;
                        	}
                        }
                        productDestroyRecord.setProcess_time(accountOut.getOutDate());
                        productDestroyRecord.setProcess_mode(Process_mode.RETURN_GOODS);
                        productDestroyRecord.setHandle_name(outBusName);
                        productDestroyRecord.setRecieve_name(inBusName);
                        productDestroyRecord.setOperation_user(AccessUtils.getUserName().toString());
                        recycleDAO.insert_recycle_record(productDestroyRecord);
                        //add by ltg 20160815 确定退货时在产品回收/销毁记录表中记录下退货信息 end
                    }
                    list.add(orig_InfoOut);
                }
            }
            /*删除对应的退货产品信息*/
            if(accountOut.getOutStatus()==1){
                removeOutInfo(orig_infoOuts);
            }
            return list;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(),e);
        }
    }

    /* 删除对应的退货产品信息 */
    private void removeOutInfo(List<TZBusaccountInfoOut> orig_infoOuts) throws ServiceException {
        /* 删除操作 */
        if(orig_infoOuts!=null&&orig_infoOuts.size()>0){
            for (int i = 0; i < orig_infoOuts.size(); i++){
                delete(orig_infoOuts.get(i).getId());
            }
        }
    }

    /**
     * 将该订单对应的产从orig_infoOuts记录中移除
     * 最后将记录中存在的产品信息从退货产品中删除
     * @author HY
     */
    private void removeOrigOutInfo(List<TZBusaccountInfoOut> orig_infoOuts, TZBusaccountInfoOut orig_infoOut) {
        if(orig_infoOuts!=null&&orig_infoOuts.size()>0){
            for(int i = 0; i < orig_infoOuts.size(); i++){
                if(orig_infoOuts.get(i).getId()==orig_infoOut.getId()){
                    orig_infoOuts.remove(i);
                }
            }
        }
    }

    private TZProductTrail setTZProductTrail(TZBusaccountInfoOut orig_infoOut, TZBusAccountOut out) throws ServiceException {
        TZProductTrail trail = new TZProductTrail();
        trail.setProductId(orig_infoOut.getProductId());
        trail.setProductNum(orig_infoOut.getProductNum());
        trail.setInBusId(out.getInBusId());
        trail.setProductBatch(orig_infoOut.getProductBatch());
        trail.setAccountDate(sdf.format(new Date()));
        trail.setOutBusId(out.getOutBusId());
        trail.setType(1); // 退货台账轨迹
        trail.setAccountId(out.getId());
        try {
            String barcode = tZAccountDAO.getBarcodeByProductId(orig_infoOut.getProductId());
            trail.setBarcode(barcode);
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        }
        trail.setProductionDate(orig_infoOut.getProductionDate());
        return trail;
    }

    /**
     * 根据退货代号初始化确认收到退货信息中的数据
     * Create Author HY Date 2015-05-17
     */
    @Override
    public List<ReturnProductVO> loadTZReturnInProductByOutId(Long outId, int page, int pageSize) throws ServiceException {
        try {
            return tzBusaccountInfoOutDAO.loadTZReturnInProductByOutId(outId, page, pageSize);
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        }
    }

    @Override
    public List<TZBusaccountInfoOut> getListByOutId(Long outId) throws ServiceException {
        String condition = "WHERE e.busAccountId =?1 ";
        try {
            return tzBusaccountInfoOutDAO.getListByCondition(condition, new Object[]{outId});
        } catch (JPAException JPAE) {
            throw new ServiceException();
        }
    }

    /* 根号退货单id删除退货产品信息
    * @Author HY
    */
    @Override
    public void deleteReturnOutInfoByOutId(Long outId) throws ServiceException {
        String condition = "WHERE e.busAccountId = ?1 ";
        try {
            List<TZBusaccountInfoOut> orig_infos = tzBusaccountInfoOutDAO.getListByCondition(condition,new Object[]{outId});
            /* 删除操作 */
            removeOutInfo(orig_infos);
        } catch (JPAException jpae) {
            throw new ServiceException(jpae.getMessage(),jpae.getException());
        }
    }

    private void setReturnProduct(TZBusaccountInfoOut orig_infoOut, TZBusaccountInfoOut tzAccountInfoOut) {
        orig_infoOut.setProductionDate(tzAccountInfoOut.getProductionDate());
        orig_infoOut.setOverDate(tzAccountInfoOut.getOverDate());
        orig_infoOut.setProductBatch(tzAccountInfoOut.getProductBatch());
        orig_infoOut.setProductId(tzAccountInfoOut.getProductId());
        orig_infoOut.setQsNo(tzAccountInfoOut.getQsNo());
        orig_infoOut.setProductNum(tzAccountInfoOut.getProductNum()!=null?tzAccountInfoOut.getProductNum():0);
        orig_infoOut.setProductPrice(tzAccountInfoOut.getProductPrice() != null ? tzAccountInfoOut.getProductPrice() : BigDecimal.valueOf(0.00));
    }
}
