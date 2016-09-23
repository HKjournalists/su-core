package com.gettec.fsnip.fsn.service.erp.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.erp.UnitDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.Unit;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.UnitService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("unitService")
public class UnitServiceImpl extends BaseServiceImpl<Unit, UnitDAO> 
		implements UnitService {
	@Autowired private UnitDAO unitDAO;
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<Unit> getAll(Long organization) {
		PagingSimpleModelVO<Unit> result = new PagingSimpleModelVO<Unit>();
		String configure = null;
		result.setListOfModel(unitDAO.findAll(organization, configure));
		return result;
	}
	
	public PagingSimpleModelVO<Unit> getPaging(int page, int size, String keywords, Long organization) {
		PagingSimpleModelVO<Unit> result = new PagingSimpleModelVO<Unit>();
		String condition = " ";
		String configure = null;
		if(keywords != null && keywords.trim()!="") {
			condition = condition + "where e.name like '%" + keywords + "%'";
			configure = "where name like '%" + keywords + "%'";
		}else{
			condition = condition + " order by id ASC";
		}
		Long count = unitDAO.countUnit(configure);
		result.setCount(count);
		result.setListOfModel(unitDAO.getPaging(page, size, condition));
		return result;
	}
	
	public Unit findByBusunitName(String unitName) throws ServiceException {
		try {
			return unitDAO.findByBusunitName(unitName);
		} catch (DaoException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public long countUnit(String configure) throws ServiceException {
		try {
			return unitDAO.count(configure);
		} catch (JPAException e) {
			throw new ServiceException("【service-error】按条件查询单位数量！", e.getException());
		}
	}
		
	/**
	 * 获取所有产品单位名称
	 */
	@Override
	public List<String> getAllUnitName() throws ServiceException {
		try{
			return unitDAO.getAllUnitName();
		}catch(DaoException daoe){
			throw new ServiceException("UnitServiceImpl.getAllUnitName()-->"+daoe.getMessage(),daoe.getException());
		}
	}

	public UnitDAO getDAO() {
		return unitDAO;
	}
}
