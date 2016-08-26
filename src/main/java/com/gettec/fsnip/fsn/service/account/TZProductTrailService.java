package com.gettec.fsnip.fsn.service.account;

import com.gettec.fsnip.fsn.dao.account.TZProductTrailDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.account.TZProductTrail;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import com.gettec.fsnip.fsn.vo.account.TZOriginVO;
import org.springframework.ui.Model;

import java.util.List;

/**
 *
 * 
 */
public interface TZProductTrailService extends BaseService<TZProductTrail, TZProductTrailDAO> {

   boolean save(AccountOutVO accountOut, Long org) throws ServiceException;
   
   boolean save(TZProductTrail tZProductTrail);

boolean updateProductTrail(TZProductTrail tZProductTrail);

   Model loadAllGoodsList(Long organization, int page, int pageSize,String condition, Model model)throws ServiceException;

   List<TZOriginVO> loadGoodsOrigin(String barcode,String batch)throws ServiceException;

   List<TZOriginVO> loadGoodsOrigin(String barcode,String idAndBatch,String batch)throws ServiceException;

   void deleteTrainByTzId(long tzId, Long valueOf)throws ServiceException;
}
