package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;

public interface ProviderDAO extends BaseDAO<BusinessUnit> {
	
	public long countProvider(String configure);
	public List<BusinessUnit> getListByConditionAndOrgId(String configure,
			Long organization, int page, int pageSize) throws DaoException;
	public Long getCountByConditionAndOrgId(String configure, Long organization) throws DaoException;
	
	public long count(Long organization, Long businessId) throws DaoException;
	
	/**
	 * 报告待处理供应商查询
	 * @author ZhangHui 2015/5/4
	 */
	public List<BusinessUnit> getListOfOnHandProducer(int page, int size, Long toBusId,String configure) throws DaoException;
	/**
	 * 获取报告待处理供应商总数
	 * @author ZhangHui 2015/5/4
	 */
	public Long getCountOfOnHandProducer(Long toBusId,String configure) throws DaoException;
}
