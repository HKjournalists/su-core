package com.gettec.fsnip.fsn.service.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.business.BusinessMarketDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessMarket;
import com.gettec.fsnip.fsn.service.business.BusinessMarketService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.vo.business.BusinessMarketVO;

@Service(value="businessMarketService")
public class BusinessMarketServiceImpl extends BaseServiceImpl<BusinessMarket, BusinessMarketDAO> 
implements  BusinessMarketService{
	@Autowired private BusinessMarketDAO businessMarketDAO;
	@Override
	public BusinessMarketDAO getDAO() {
		return businessMarketDAO;
	}

	/**
	 * 根据组织机构查询交易市场
	 */
	@Override
	public BusinessMarket findByOrg(Long organization) throws ServiceException {
		try {
			return businessMarketDAO.findByOrg(organization);
		} catch (DaoException daoe) {
			throw new ServiceException("BusniessMarketServiceImpl-->findByOrg()",daoe);
		}
	}

	/**
	 * 根据交易市场信息的上传状态查询交易市场信息
	 */
	@Override
	public List<BusinessMarket> getListByPublishFlag(boolean publishFlag)
			throws ServiceException {
		try{
			String condition = " where e.publishFlag = ?1";
			return businessMarketDAO.getListByCondition(condition, new Object[]{publishFlag});
		}catch(JPAException jpae){
			throw new ServiceException("BusinessMarketServiceImpl-->getListByPublishFlag"+jpae.getMessage(),jpae);
		}
	}

	/**
	 * 通过经营主体id获取交易市场VO
	 */
	@Override
	public BusinessMarketVO findVOByBusinessId(Long businessId)
			throws ServiceException {
		try{
			return businessMarketDAO.findByBusinessId(businessId);
		}catch(DaoException jpae){
			throw new ServiceException("BusinessMarketServiceImpl-->findVOByBusinessId()"+jpae.getMessage(),jpae);
		}
	}
	
}
