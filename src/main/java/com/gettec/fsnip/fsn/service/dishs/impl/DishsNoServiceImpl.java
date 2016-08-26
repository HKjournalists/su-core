package com.gettec.fsnip.fsn.service.dishs.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.dishs.DishsNoDao;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.dishs.DishsNo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.dishs.DishsNoService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.vo.dishs.DishsNoShowVO;

@Service(value = "dishsNoService")
public class DishsNoServiceImpl extends BaseServiceImpl<DishsNo, DishsNoDao> implements DishsNoService    {

	@Autowired 
	private DishsNoDao dishsNoDao ;
	@Autowired private ResourceService  resourceService;
	@Override
	public DishsNoDao getDAO() {
		return dishsNoDao;
	}

	@Override
	public List<DishsNo> loadWasteDisposa(int page, int pageSize,
			String dishsName,long qiyeId) throws DaoException {
		return this.getDAO().loadWasteDisposa(page, pageSize, dishsName,qiyeId);
	}

	@Override
	public long getWasteTotal(String dishsName,long qiyeId) throws DaoException {
		return this.getDAO().getDishsNoTotal(dishsName,qiyeId);
	}
	
	@Override
	public List<DishsNo>  getListDishsNo(String orgId) throws DaoException {
		return this.getDAO().getListDishsNo(orgId);
	}

	@Override
	public long getDishsNoShowTotal(String showTime, String dishsName,
			Long fromBusId, boolean flag) {
		
		return getDAO().getDishsNoShowTotal(showTime, dishsName,fromBusId,flag);
	}

	@Override
	public List<DishsNoShowVO> loadDishsNoShowVO(int page, int pageSize,
			String showTime, String dishsName, Long fromBusId, boolean flag) {
		try {
			if(page>0){
				return getDAO().loadDishsNoShowVO(page, pageSize, showTime, dishsName,fromBusId,flag);
			}else{
				List<DishsNoShowVO> dishsList = getDAO().loadDishsNoShowVO(page, pageSize, showTime, dishsName,fromBusId,flag);
				for (DishsNoShowVO dishsNoShowVO : dishsList) {
					Resource  res = resourceService.findById(dishsNoShowVO.getResourceId());
					dishsNoShowVO.setDishsnoFile(res);
				}
				return dishsList;
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;
	}
}
