package com.gettec.fsnip.fsn.service.account;

import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import com.gettec.fsnip.fsn.vo.account.BusRelationVO;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;
import org.springframework.ui.Model;

import com.gettec.fsnip.fsn.dao.account.TZBusinessRelationDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZBusinessRelation;
import com.gettec.fsnip.fsn.service.common.BaseService;

import java.util.List;

/**
 * 台账系统
 * @author HuangYog 
 * @email HuangYong@fsnip.com
 */
public interface TZBusinessRelationService extends BaseService<TZBusinessRelation, TZBusinessRelationDAO>{

    Model loadTZBusRelation(Long org,int type, String busName, String busLic, int page, int pageSize, Model model) throws ServiceException;

    Model loadTZReturnProduct(Long org, String proName, String proBar, int page, int pageSize,
            Model model)throws ServiceException;

    Model loadBusinessUnitBasicInfo(Long organization, Model model)throws ServiceException;

    Model submitTZReturnProduct(Long org, AccountOutVO accountOut, Model model)throws ServiceException;

    BusRelationVO getBusinessRelationById(Long outBusId, Long inBusId)throws ServiceException;

    List<ReturnProductVO> loadTZReturnProductByOutId(Long outId,Integer page,Integer pageSize)throws ServiceException;

    Long loadTZReturnProductTotalsById(Long outId)throws ServiceException;
}
