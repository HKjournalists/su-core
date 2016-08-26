package com.gettec.fsnip.fsn.service.erp;

import java.util.List;
import com.gettec.fsnip.fsn.dao.erp.ProviderDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface ProviderService extends BaseService<BusinessUnit, ProviderDAO> {
	public PagingSimpleModelVO<BusinessUnit> getAllprovider(Long organization);
	
	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	public PagingSimpleModelVO<BusinessUnit> getProviderfilter(int page, int pageSize,String configure, Long organization) throws ServiceException;
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 */
	public long countProviderfilter(String configure);
	/*
	 * author：cgw
	 * describe:获取供货商信息*/
	public List<BusinessUnit> getProvider(Long organization);
	ResultVO add(ResultVO resultVO, Long organization);
	public boolean judgeIsUsed(BusinessUnit businessUnit, Long organization) throws ServiceException;

	boolean remove(BusinessUnit e,Long organization) throws ServiceException;

	ResultVO updateProvider(ResultVO resultVO);

	long count(Long organization, Long businessId) throws ServiceException;

	/**
	 * 报告待处理供应商查询
	 * @author ZhangHui 2015/5/4
	 */
	PagingSimpleModelVO<BusinessUnit> getPagingOfOnHandProducer(int page,
			int size, Long organization,String configure) throws ServiceException;
}
