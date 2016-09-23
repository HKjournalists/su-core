package com.gettec.fsnip.fsn.service.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZBusinessRelationDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZBusAccountOut;
import com.gettec.fsnip.fsn.model.account.TZBusinessRelation;
import com.gettec.fsnip.fsn.model.account.TZStock;
import com.gettec.fsnip.fsn.model.account.TZStockInfo;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.service.account.*;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import com.gettec.fsnip.fsn.vo.account.BusRelationVO;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
/**
 * 台账系统
 * @author HuangYog 
 * @email HuangYong@fsnip.com
 */
@Service(value="tzBusinessRelationService")
public class TZBusinessRelationServiceImpl extends BaseServiceImpl<TZBusinessRelation,TZBusinessRelationDAO> implements TZBusinessRelationService {

    @Autowired private TZBusinessRelationDAO tzBusinessRelationDAO;
    @Autowired private BusinessUnitService businessUnitService;
    @Autowired private TZBusAccountOutService tZBusAccountOutService;
    @Autowired private TZBusaccountInfoOutService tZBusaccountInfoOutService;
    @Autowired private TZStockInfoService tzStockInfoService;
    @Autowired private TZStockService tzStockService;


    
    @Override
    public TZBusinessRelationDAO getDAO() {
        // TODO Auto-generated method stub
        return tzBusinessRelationDAO;
    }

    /**
     * 根据企业关系加载对应的企业信息List
     * @param type 企业关系 0：供应，1：销售
     * Create Author HY Date 2015-05-17
     */
    @Override
    public Model loadTZBusRelation(Long org,int type, String busName, String busLic, int page, int pageSize, Model model)throws ServiceException {
        try {
            /* 获取与该企业对应的供销关系企业信息 */
            List<BusRelationVO> busRelations = tzBusinessRelationDAO.loadTZBusRelation(org,type, busName, busLic,page,pageSize);
            Long total = tzBusinessRelationDAO.loadTZBusRelationToatl(org,type, busName, busLic);
            model.addAttribute("data", busRelations);
            model.addAttribute("total", total);
            return model;
            } catch (DaoException daoe) {
                throw new ServiceException(daoe.getMessage(), daoe.getException());
            }
    }
    
    /**
     *查询该企业下可以退货的产品
     *@author HY
     */
    @Override
    public Model loadTZReturnProduct(Long org, String proName, String proBar, int page, int pageSize,
            Model model) throws ServiceException {
        try {
            Long mybusId = businessUnitService.findIdByOrg(org);
            List<ReturnProductVO> lists = tzBusinessRelationDAO.loadTZReturnProduct(mybusId,proName,proBar, page, pageSize);
            Long totals = tzBusinessRelationDAO.getTZReturnProductTotal(mybusId,proName,proBar);
            model.addAttribute("data",lists);
            model.addAttribute("totals",totals);
            return model;
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(), daoe.getException());
        }
    }

    /**
     * 加载企业的基本信息
     * @param org 组织机构ID
     * @author HY
     */
    @Override
    public Model loadBusinessUnitBasicInfo(Long org, Model model) throws ServiceException {
        try{
            BusinessUnit bu = businessUnitService.findByOrganization(org);
            if(bu instanceof BusinessUnit){
                BusRelationVO brVO = new BusRelationVO();
                brVO.setId(bu.getId());
                brVO.setBusName(bu.getName());
                brVO.setLicNo(bu.getLicense() instanceof LicenseInfo ? bu.getLicense().getLicenseNo() : "");
                brVO.setContact(bu.getTelephone());
                brVO.setAddress(bu.getAddress());
                model.addAttribute("basicInfo",brVO);
            }
            return model;
        }catch (Exception e){
            throw new ServiceException("TZBusinessRelationServiceImpl.loadBusinessUnitBasicInfo()台账退货加载企业的基本信息，异常！", e);
        }
    }

    /**
     * 保存用户提交的退货信息
     * @author HY
     */
    @Override
    public Model submitTZReturnProduct(Long org, AccountOutVO accountOut,Model model) throws ServiceException {
        TZBusAccountOut out = new TZBusAccountOut();
        out = out.setTZBusAccountOut(accountOut);
        boolean savePro = false;
        TZBusAccountOut saveBus = null;
        if(out instanceof TZBusAccountOut){
            if (accountOut.getProList()!=null && accountOut.getProList().size() > 0){
                saveBus = tZBusAccountOutService.save(out,accountOut,org);
                savePro = true;
            }
        }
        model.addAttribute("saveBus",saveBus);
        model.addAttribute("save",savePro);
        return model;
    }

    /**
     * 根据企业关系的id加载销往企业
     * @author HY
     */
    @Override
    public BusRelationVO getBusinessRelationById(Long myBusId, Long busId) throws ServiceException {
        try {
            return tzBusinessRelationDAO.getBusinessRelationById(myBusId, busId);
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(), daoe.getException());
        }
    }

    /**
     * 根据订单编号加载相应的退货产品
     * @author HY
     */
    @Override
    public List<ReturnProductVO> loadTZReturnProductByOutId(Long outId,Integer page,Integer pageSize) throws ServiceException {
        try {
            return tzBusinessRelationDAO.loadTZReturnProductByOutId(outId,page,pageSize);
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(), daoe.getException());
        }

    }

    @Override
    public Long loadTZReturnProductTotalsById(Long outId) throws ServiceException {
        try {
            return tzBusinessRelationDAO.loadTZReturnProductTotalsById(outId);
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(),daoe.getException());
        }
    }

}
