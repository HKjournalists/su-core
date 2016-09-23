package com.gettec.fsnip.fsn.service.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZBusAccountOutDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZBusAccountOut;
import com.gettec.fsnip.fsn.model.account.TZBusaccountInfoOut;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.service.account.TZBusAccountOutService;
import com.gettec.fsnip.fsn.service.account.TZBusaccountInfoOutService;
import com.gettec.fsnip.fsn.service.account.TZBusinessRelationService;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.vo.account.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HY on 2015/5/18.
 * desc:
 */
@Service(value="tzBusAccountOutService")
public class TZBusAccountOutServiceImpl extends BaseServiceImpl<TZBusAccountOut,TZBusAccountOutDAO> implements TZBusAccountOutService {
    @Autowired TZBusAccountOutDAO accountOutDAO;
    @Autowired TZBusinessRelationService tzBusinessRelationService;
    @Autowired BusinessUnitService businessUnitService;
    @Autowired private TZBusaccountInfoOutService tZBusaccountInfoOutService;
    SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public TZBusAccountOutDAO getDAO() {
        return accountOutDAO;
    }

    /**
     * 保存用户提交的退货信息
     * @author HY
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public TZBusAccountOut save(TZBusAccountOut out,AccountOutVO accountOut, Long org) throws ServiceException {
        boolean isSuccess = false;
        TZBusAccountOut orig_out = null;
        try {
            /* 更新的情况 */
            if (out.getId() != null) {
                orig_out = accountOutDAO.findById(out.getId());
                /* 判断是否已经确认退货了 */
                if (orig_out instanceof TZBusAccountOut) {
                    if (orig_out.getOutStatus() != 1) {
                        setBusAccountOut(orig_out, out);
                        update(orig_out);
                        /* 更新產品退貨信息 */
                        List<TZBusaccountInfoOut> outInfs = tZBusaccountInfoOutService.save(accountOut, orig_out);
                    }
                }
            }else{/* 新建的情况 */
                orig_out = new TZBusAccountOut();
                setBusAccountOut(orig_out, out);
                create(orig_out);
                /* 保存產品庫存信息 */
                tZBusaccountInfoOutService.save(accountOut, orig_out);
                isSuccess = true;
            }
            return orig_out;
        } catch (JPAException jpae) {
            throw new ServiceException(jpae.getMessage(), jpae.getException());
        }
    }

    /**
     * 加载企业退货单信息
     * @author HY
     */
    @Override
    public Model loadReturnAccountList(String type,Long org, String accountNo, String nameAndLic,
                                       int page, int pageSize, Model model) throws ServiceException {
        try {
            BusinessUnit bus = businessUnitService.findByOrganization(org);
            if (bus instanceof BusinessUnit) {
                String condition = "";
                if(accountNo!=null&&accountNo!=""){
                    condition += " AND tzao.account_no = '" + accountNo+"' ";
                }
                if(nameAndLic!=null&&nameAndLic!=""){
                    condition += " AND (bt.name LIKE '%"+ nameAndLic +"%' OR bt.license_no LIKE '%"+ nameAndLic +"%' ) ";
                }
                String licNo = bus.getLicense() instanceof LicenseInfo ? bus.getLicense().getLicenseNo():"";
                List<RetrunAccountVO> lists = null;
                Long totals = null;
                if("in".equals(type)){// 表示确认收货的
                    lists = accountOutDAO.loadReturnInAccountList(bus.getId(), bus.getName(), licNo, page, pageSize, condition+" ORDER BY tzao.in_status ASC,tzao.create_time DESC");
                    totals = accountOutDAO.loadReturnInAccountListTotal(bus.getId(), condition);
                }else if("out".equals(type)){
                    lists = accountOutDAO.loadReturnAccountList(bus.getId(),bus.getName(),licNo, page, pageSize,condition+" ORDER BY tzao.out_status ASC,tzao.create_time DESC");
                    totals = accountOutDAO.loadReturnAccountListTotal(bus.getId(), condition);
                }

                model.addAttribute("data", lists);
                model.addAttribute("totals",totals);
            }
            return model;
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        }
    }

    /**
     * 台账退货管理编辑到台账退货页面时根据订单编号加载退货信息
     * @author HY
     */
    @Override
    public Model initReturnGoods(Long id, Model model) throws ServiceException {
        try {
            /*1 加载退货企业关系 */
            TZBusAccountOut orig_accountOut = accountOutDAO.findById(id);
            if(orig_accountOut instanceof TZBusAccountOut){
                ReturnGoodsVO vo = new ReturnGoodsVO();
                vo.setBusAccountId(orig_accountOut.getId());
                vo.setOutStatus(orig_accountOut.getOutStatus());
                vo.setInStatus(orig_accountOut.getInStatus());
                if(orig_accountOut.getOutDate()!=null) {
                    vo.setOutTime(orig_accountOut.getOutDate());
                }
                if(orig_accountOut.getInDate()!=null) {
                    vo.setInTime(orig_accountOut.getInDate());
                }
                /* 用户选择的销往企业 */
                BusRelationVO busRelationVO = tzBusinessRelationService.getBusinessRelationById(orig_accountOut.getInBusId(),orig_accountOut.getOutBusId());
                BusRelationVO outBusVO = tzBusinessRelationService.getBusinessRelationById(orig_accountOut.getOutBusId(),orig_accountOut.getInBusId());
                vo.setBusRelationVO(busRelationVO);
                vo.setOutRelationVO(outBusVO);
                /*2 加载退货产品信息 */
                List<ReturnProductVO> lists =tzBusinessRelationService.loadTZReturnProductByOutId(orig_accountOut.getId(),0,0);
                vo.setReturnProductVOList(lists);
                model.addAttribute("data",vo);
            }
            return model;
        } catch (JPAException jpae) {
            throw new ServiceException(jpae.getMessage(),jpae.getException());
        }
    }

    /**
     * 加载当前企业退货的产品详情
     * Create Author HY Date 2015-05-17
     */
    @Override
    public Model loadReturnGoodsDetail(Long outId, Integer page, Integer pageSize, Model model) throws ServiceException {
        List<ReturnProductVO> lists = new ArrayList<ReturnProductVO>();
        Long totals = Long.valueOf(0);
        if(outId!=null&&outId!=-1L){
            lists = tzBusinessRelationService.loadTZReturnProductByOutId(outId,page,pageSize);
            totals = tzBusinessRelationService.loadTZReturnProductTotalsById(outId);
        }
        model.addAttribute("data",lists);
        model.addAttribute("totals",totals);
        return model;
    }

    /**
     * 根据退货代号初始化确认收到退货信息中的数据
     * Create Author HY Date 2015-05-17
     */
    @Override
    public Model initReturnInGoods(Long id, Model model) throws ServiceException {
        /*1 加载退货企业关系 */
        TZBusAccountOut orig_accountOut = null;
        try {
            orig_accountOut = accountOutDAO.findById(id);
            if(orig_accountOut instanceof TZBusAccountOut){
                ReturnGoodsVO vo = new ReturnGoodsVO();
                vo.setBusAccountId(orig_accountOut.getId());
                vo.setOutStatus(orig_accountOut.getOutStatus());
                vo.setInStatus(orig_accountOut.getInStatus());
                if(orig_accountOut.getOutDate()!=null) {
                    vo.setOutTime(orig_accountOut.getOutDate());
                }
                if(orig_accountOut.getInDate()!=null) {
                    vo.setInTime(orig_accountOut.getInDate());
                }
                    /* 用户选择的销往企业 */
                BusRelationVO busRelationVO = tzBusinessRelationService.getBusinessRelationById(orig_accountOut.getOutBusId(),orig_accountOut.getInBusId());
                vo.setBusRelationVO(busRelationVO);
                    /*2 加载退货产品信息 */
                List<ReturnProductVO> lists =tZBusaccountInfoOutService.loadTZReturnInProductByOutId(orig_accountOut.getId(),0,0);
                vo.setReturnProductVOList(lists);
                model.addAttribute("data",vo);
            }
            return model;
        } catch (Exception e) {
            throw new ServiceException("根据退货代号初始化确认收到退货信息中的数据,异常",e);
        }
    }

    /* 在用户还未确认退货的情况下可以删除该条退货信息 */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Model deleteReturnInfo(Long organization, Long outId, Model model) throws ServiceException {
        try {
            boolean delete = false;
            TZBusAccountOut orig_out = accountOutDAO.findById(outId);
            if(orig_out!=null&&orig_out.getOutStatus()!=1){
                /* 先删除infoOut中的数据 */
                tZBusaccountInfoOutService.deleteReturnOutInfoByOutId(outId);
                /* 删除退货信息 */
                delete(orig_out.getId());
                delete = true;
            }
            model.addAttribute("delete",delete);
            return model;
        } catch (JPAException jpae) {
            throw new ServiceException(jpae.getMessage(),jpae.getException());
        }
    }

    /**
     * 政府查看加载所有的退货台帐
     * author HY
     */
    @Override
    public Model loadAllReturnAccount(String nameOrLic, int page, int pageSize, Model model) throws ServiceException {
        try {
            List<RetrunAccountVO> lists =accountOutDAO.loadAllReturnAccount(nameOrLic,page,pageSize);
            Long totals =accountOutDAO.loadAllReturnAccountTotals(nameOrLic);
            model.addAttribute("data",lists).addAttribute("totals",totals);
            return model;
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        }
    }

    private void setBusAccountOut(TZBusAccountOut orig_out, TZBusAccountOut out) {
        if (orig_out.getId()==null){
            orig_out.setCreateDate(SDF.format(new Date()));
        }
        orig_out.setInBusId(out.getInBusId());
        orig_out.setOutBusId(out.getOutBusId());
        orig_out.setAccountNo(out.getAccountNo());
        /* 退货时判断是否是确认要退货 */
        orig_out.setOutStatus(out.getOutStatus());
        orig_out.setOutDate(out.getOutDate());
        /* 进货时判断是否是确认进货 */
        orig_out.setInStatus(out.getInStatus());
        orig_out.setInDate(out.getInDate());

    }
}
