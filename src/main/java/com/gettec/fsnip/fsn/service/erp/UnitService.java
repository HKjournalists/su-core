package com.gettec.fsnip.fsn.service.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.erp.UnitDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.Unit;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface UnitService extends BaseService<Unit, UnitDAO>{

	/**
	 * 根据唯一名字查询单位
	 * @author Liang Zhou
	 * 2014-10-29
	 * @param unitName
	 * @return
	 * @throws ServiceException
	 */
	public Unit findByBusunitName(String unitName) throws ServiceException;
	
	public PagingSimpleModelVO<Unit> getPaging(int page, int size, String keywords, Long organization);
	
	public PagingSimpleModelVO<Unit> getAll(Long organization);
	
	/**
	 * 根据条件查询出这个条件的单位数量
	 * @author Liang Zhou
	 * 2014-10-29
	 * @param configure
	 * @return
	 * @throws ServiceException
	 */
	public long countUnit(String configure) throws ServiceException;
	
	List<String> getAllUnitName()throws ServiceException;
}
