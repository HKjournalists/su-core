package com.gettec.fsnip.fsn.dao.market;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.market.StoreProduct;
import com.gettec.fsnip.fsn.vo.StoreProductVO;

/**
 * Create Time 2015-03-31
 * @author zhaWanNeng
 */
public interface StoreProductDao extends BaseDAO<StoreProduct>{

	public Long findStorebyUser4ProductId(Long productId,Long userId)throws DaoException;
    
	public List<StoreProductVO> findStoreProduc(int size,int page,StringBuilder condition)throws DaoException;
	
	public int StoreCount(int size,int page,StringBuilder condition)throws DaoException;

	/**
	 * 批量取消收藏产品接口
	 * @author LongXianZhen 2015/06/18
	 */
	public void batchDelete(String proIds, Long userId)throws DaoException;
}
