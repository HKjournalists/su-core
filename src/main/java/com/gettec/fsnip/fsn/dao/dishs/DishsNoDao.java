package com.gettec.fsnip.fsn.dao.dishs;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.dishs.DishsNo;
import com.gettec.fsnip.fsn.vo.dishs.DishsNoShowVO;

public interface DishsNoDao extends BaseDAO<DishsNo> {
	List<DishsNo> loadWasteDisposa(int page, int pageSize, String dishsName,long qiyeId)throws DaoException;
	long getDishsNoTotal(String dishsName,long qiyeId) throws DaoException;
	/**
	 * 根据企业id，查询企业所有菜品信息
	 * @param orgId
	 * @param date
	 * @return
	 * @throws DaoException
	 */
	List<DishsNo> getListDishsNo(String orgId)throws DaoException;
	List<DishsNoShowVO> loadDishsNoShowVO(int page, int pageSize,
			String showTime, String dishsName, Long fromBusId,boolean flag);
	
	long getDishsNoShowTotal(String showTime, String dishsName, Long fromBusId,
			boolean flag);
}
