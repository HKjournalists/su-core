package com.gettec.fsnip.fsn.service.market;

import java.util.List;

import com.gettec.fsnip.fsn.dao.market.StoreProductDao;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.StoreProduct;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.StoreProductVO;

/**
 * Create Time 2015-03-31
 * @author zhaWanNeng
 */
public interface StoreProductService extends BaseService<StoreProduct, StoreProductDao> {

	public Long findStorebyUser4ProductId(Long productId,Long userId)throws ServiceException;
	
	public List<StoreProductVO> findStoreProduc(int size,int page,Long userId,String startDate,String endDate,String nutri_label ,String code)throws ServiceException;

	public int StoreCount(int size,int page,Long userId,String startDate,String endDate,String nutri_label ,String code)throws ServiceException;

	/**
	 * 批量取消收藏产品接口
	 * @author LongXianZhen 2015/06/18
	 */
	public void batchDelete(String proIds, Long userId)throws ServiceException;

	
}