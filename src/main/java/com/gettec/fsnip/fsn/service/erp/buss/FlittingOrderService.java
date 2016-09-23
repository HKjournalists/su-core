package com.gettec.fsnip.fsn.service.erp.buss;

import com.gettec.fsnip.fsn.dao.erp.buss.FlittingOrderDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.buss.FlittingOrder;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.BusinessOrderVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface FlittingOrderService extends BaseService<FlittingOrder,FlittingOrderDAO> {
	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 */
	public PagingSimpleModelVO<FlittingOrder> getFlittingOrderfilter(int page, int pageSize,String configure, Long organization)throws ServiceException ;
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 */
	public long countFlittingOrderfilter(String configure)throws ServiceException ;
	/*
	 * 判读调拨出库数量*/
	public boolean JudgeflittingNum(Long mid,String value,String batch,String storage) throws ServiceException;

	public FlittingOrder addBusinessOrder(BusinessOrderVO vo,
			Long currentUserOrganization, String userName) throws ServiceException;
}
