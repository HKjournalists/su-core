package com.gettec.fsnip.fsn.service.erp;


import java.util.List;

import com.gettec.fsnip.fsn.dao.erp.CustomerDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface CustomerService extends BaseService<BusinessUnit, CustomerDAO> {

	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	public PagingSimpleModelVO<BusinessUnit> getCustomerfilter(int page, int pageSize,String configure, Long organization) throws ServiceException;
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 * @throws ServiceException 
	 */
	public long countCustomerfilter(String configure) throws ServiceException;
	/*
	 * author：cgw
	 * describe:获取客户信息*/
	public List<BusinessUnit> getCustomer(Long organization);

	List<BusinessUnit> getAllByOrganization(Long organization);

	boolean remove(BusinessUnit e,Long organization) throws ServiceException;
	
	public boolean judgeIsUsed(BusinessUnit businessUnit, Long organization) throws ServiceException;

	String getConfigure(String configure);

	ResultVO updateCustomer(ResultVO resultVO);

	public ResultVO add(ResultVO resultVO, Long currentUserOrganization);

	long count(Long organization, Long businessId) throws ServiceException;

	public void addProviderRelation(BusinessUnit customer, Long providerId) throws ServiceException;

	public PagingSimpleModelVO<BusinessUnit> searchCustomerList(int page,
			int pageSize, String keyword, Long currentUserOrganization);

	public PagingSimpleModelVO<BusinessUnit> sourceOrsoldCustomer(int _type,
			int page, int pageSize, String configure,
			Long organiztion);
}
