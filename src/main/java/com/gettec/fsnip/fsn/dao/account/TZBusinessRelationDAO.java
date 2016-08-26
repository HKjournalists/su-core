package com.gettec.fsnip.fsn.dao.account;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.account.TZBusinessRelation;
import com.gettec.fsnip.fsn.vo.account.BusRelationVO;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;

/**
 * 台账系统
 * @author HuangYog 
 * @email HuangYong@fsnip.com
 */
public interface TZBusinessRelationDAO extends BaseDAO<TZBusinessRelation> {

    List<BusRelationVO> loadTZBusRelation(Long myOrg, int type, String busName, String busLic, int page, int pageSize) throws DaoException;

    Long loadTZBusRelationToatl(Long myOrg, int type, String busName, String busLic)throws DaoException;

    List<ReturnProductVO> loadTZReturnProduct(Long org, String proName, String proBar, int page, int pageSize)throws DaoException;

    Long getTZReturnProductTotal(Long org, String proName, String proBar)throws DaoException;

    BusRelationVO getBusinessRelationById(Long outBusId, Long inBusId)throws DaoException;

    List<ReturnProductVO> loadTZReturnProductByOutId(Long outId,Integer page,Integer pageSize)throws DaoException;

    Long loadTZReturnProductTotalsById(Long outId)throws DaoException;
}
