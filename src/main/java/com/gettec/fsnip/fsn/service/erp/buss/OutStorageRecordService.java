package com.gettec.fsnip.fsn.service.erp.buss;

import com.gettec.fsnip.fsn.dao.erp.buss.OutStorageRecordDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.buss.OutStorageRecord;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.erp.BusinessOrderVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface OutStorageRecordService extends BaseService<OutStorageRecord, OutStorageRecordDAO> {
	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 */
	public PagingSimpleModelVO<OutStorageRecord> getOutStorageRecordfilter(int page, int pageSize,String configure, Long organization)throws ServiceException;
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 */
	public long countOutStorageRecordfilter(String configure)throws ServiceException;
	
	/*
	 * author:cgw
	 * @param mid
	 * @param value
	 * @param batch
	 * */
	public boolean JudgeOutNum(Long productId,String value,String batch,String storage) throws ServiceException;

	public OutStorageRecord addBusinessOrder(BusinessOrderVO vo,
			Long currentUserOrganization, String userName, ResultVO resultVO) throws ServiceException;
}
