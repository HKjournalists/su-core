package com.gettec.fsnip.fsn.dao.account;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.account.TZProductTrail;
import com.gettec.fsnip.fsn.vo.account.TZOriginGoodsInfoVO;

import java.util.List;

/**
 * 
 * Author:chenxiaolin
 */
public interface TZProductTrailDAO extends BaseDAO<TZProductTrail> {

    List<TZOriginGoodsInfoVO> loadAllGoodsList(int page, int pageSize,
                                               String condition,String cond) throws DaoException;

    Long loadAllGoodsListTotals(String condition,String cond)throws DaoException;

    TZProductTrail loadGoodsOrigin( String barcode, int i,String batch)throws DaoException;

    Object[] getBusInfoById(Long busId)throws DaoException;

    List<TZProductTrail> getOriginChildren(Long proId, String qs, Long inBusId)throws DaoException;

    List<TZProductTrail> getOriginChildrens(Long inBusId,Long trialId, String barcode,String batch)throws DaoException;

    String findByproductId(Long productId)throws DaoException;

    List<TZProductTrail> getOriginChildrensByBatch(Long inBusId, String barcode,
                                                   String productBatch,Long tid)throws DaoException;

    Long getProductNum(String barcode, Long outBusId, String productBatch,Long tid)throws DaoException;

	void deleteTrainByTzId(long tzId, Long busId)throws DaoException;
}
