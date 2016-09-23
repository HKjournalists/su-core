package com.gettec.fsnip.fsn.service.dishs;

import java.util.List;

import com.gettec.fsnip.fsn.dao.dishs.DishsNoDao;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.dishs.DishsNo;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.dishs.DishsNoShowVO;

public interface DishsNoService extends BaseService<DishsNo, DishsNoDao> {
	List<DishsNo> loadWasteDisposa(int page, int pageSize, String dishsName,long qiyeId)throws DaoException;
	long getWasteTotal(String dishsName,long qiyeId) throws DaoException;
	/**
	 * 根据企业id，查询企业所有菜品信息
	 * @param orgId
	 * @param date
	 * @return
	 * @throws DaoException
	 */
	List<DishsNo> getListDishsNo(String orgId)throws DaoException;
	/**
	 * 获取今日菜品总数
	  * @param showTime 展示日期
	 * @param dishsName  菜品名称
	 * @param fromBusId  当前企业ID
	 * @param flag   true 今日菜品 ；false 添加为展示日期的菜品 
	 * @return
	 */
	long getDishsNoShowTotal(String showTime, String dishsName, Long fromBusId,boolean flag);
	/**
	 * 获取今日菜品列表信息
	 * @param showTime 展示日期
	 * @param dishsName  菜品名称
	 * @param fromBusId  当前企业ID
	 * @param flag
	 * @return
	 */
	List<DishsNoShowVO> loadDishsNoShowVO(int page, int pageSize,
			String showTime, String dishsName, Long fromBusId,boolean flag);
}
