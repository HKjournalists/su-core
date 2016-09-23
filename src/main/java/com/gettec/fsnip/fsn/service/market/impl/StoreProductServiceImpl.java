package com.gettec.fsnip.fsn.service.market.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.market.StoreProductDao;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.StoreProduct;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.StoreProductService;
import com.gettec.fsnip.fsn.vo.StoreProductVO;

@Service(value = "storeProductService")
public class StoreProductServiceImpl extends
		BaseServiceImpl<StoreProduct, StoreProductDao> implements
		StoreProductService {
	@Autowired private StoreProductDao StoreProductDAO ;

	@Override
	public StoreProductDao getDAO() {
		return StoreProductDAO;
	}
	/**
	 * 根据产品id和用户id判断产品是否被此用户搜藏
	 * @param productId 产品id
	 * @param userId  用户id
	 * @author ZhaWanNeng
	 */
	public Long findStorebyUser4ProductId(Long productId,Long userId)throws ServiceException{
		Long isStore = null ;
		try {
			 isStore = getDAO().findStorebyUser4ProductId(productId, userId);
		} catch (DaoException e) {
			throw new ServiceException("StoreProductServiceImpl.findStorebyUser4ProductId()-->" + e.getMessage(),e.getException());
		}
		return isStore;
	}
	/**
	 * 高级查询我的搜藏
	 * @param size 每页的条数
	 * @param page 第几页
	 * @param productId 产品id
	 * @param userId 用户id
	 * @param startDate 查询报告更新时间的起始时间
	 * @param endDate 查询报告更新时间的最后时间
	 * @param nutrilabel 营养成分
	 * @param category 一级分类
	 * @return
	 * @throws DaoException
	 * @author ZhaWanNeng
	 */
	@Override
	public List<StoreProductVO> findStoreProduc(int size, int page,
			Long userId, String startDate, String endDate,
			String nutrilabel, String category) throws ServiceException {
		List<StoreProductVO> categorys = new ArrayList<StoreProductVO>();
		try {
			StringBuilder condition = conditon(userId, startDate, endDate, nutrilabel, category);
			categorys = getDAO().findStoreProduc(size, page,condition);
		} catch (DaoException e) {
			throw new ServiceException("StoreProductServiceImpl.findStoreProduc()-->" + e.getMessage(),e.getException());
		}
		return categorys;
	}
	/**
	 * 我的搜藏拼接条件
	 * @throws ServiceException
	 */
	private StringBuilder conditon(
			Long userId, String startDate, String endDate,
			String nutriLabel, String code) throws ServiceException {
		
		StringBuilder condition = new StringBuilder(" WHERE 1=1 ");
		
		if(userId != null){
			condition = condition.append(" and sp.userId = " +userId);
		}
		
		if(startDate != null && StringUtils.isNotEmpty(startDate)){
			condition = condition.append(" and tr.test_date  >= '" +startDate+"'" );
		}
		if(endDate != null && StringUtils.isNotEmpty(endDate)){
			condition = condition.append(" and tr.test_date  <= '" + endDate+"'");
		}
		if(nutriLabel != null && StringUtils.isNotEmpty(nutriLabel)){
			condition = condition.append(" and pro.nutri_label LIKE '% " + nutriLabel+"%'");
		}
		if(code != null && StringUtils.isNotEmpty(code)){
			condition = condition.append(" and pro.category LIKE '%" + code+"_%'");
		}
		return condition;
	}
	/**
	 * 高级查询我的搜藏收藏产品的总量
	 * @param size 每页的条数
	 * @param page 第几页
	 * @param productId 产品id
	 * @param userId 用户id
	 * @param startDate 查询报告更新时间的起始时间
	 * @param endDate 查询报告更新时间的最后时间
	 * @param nutrilabel 营养成分
	 * @param category 一级分类
	 * @return int 总条数
	 * @throws DaoException
	 * @author ZhaWanNeng
	 */
	@Override
	public int StoreCount(int size,int page,Long userId,String startDate,String endDate,String nutriLabel ,String category)throws ServiceException{
		try {
			StringBuilder condition = conditon(userId, startDate, endDate, nutriLabel, category);
			int storeCount = getDAO().StoreCount(size, page,condition);
			return storeCount;
		} catch (DaoException e) {
			throw new ServiceException("StoreProductServiceImpl.StoreCount()-->" + e.getMessage(),e.getException());
		}
		
	}
	/**
	 * 批量取消收藏产品接口
	 * @author LongXianZhen 2015/06/18
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void batchDelete(String proIds, Long userId) throws ServiceException {
		try {
			getDAO().batchDelete(proIds,userId);
		} catch (DaoException e) {
			throw new ServiceException("StoreProductServiceImpl.batchDelete()-->" + e.getMessage(),e.getException());
		}
	}

}