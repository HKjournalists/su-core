package com.gettec.fsnip.fsn.service.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.business.BusinessMarketDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessMarket;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.business.BusinessMarketVO;

public interface BusinessMarketService extends BaseService<BusinessMarket, BusinessMarketDAO>{
	public BusinessMarket findByOrg(Long organization)throws ServiceException ;
	
	public List<BusinessMarket> getListByPublishFlag(boolean publishFlag) throws ServiceException;
	
	public BusinessMarketVO findVOByBusinessId(Long businessId) throws ServiceException;
}
