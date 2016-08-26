package com.gettec.fsnip.fsn.service.dishs.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.dishs.DishsNoShowDao;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.dishs.DishsNoShow;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;

import com.gettec.fsnip.fsn.service.dishs.DishsNoShowService;
import com.gettec.fsnip.fsn.vo.dishs.DishsNoShowVO;

@Service(value = "dishsNoShowService")
public class DishsNoShowServiceImpl extends BaseServiceImpl<DishsNoShow, DishsNoShowDao> implements DishsNoShowService{
	
	@Autowired 
	private DishsNoShowDao dishsNoShowDao ;
	@Override
	public DishsNoShowDao getDAO() {
		return dishsNoShowDao;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveDishsNoShow(List<DishsNoShowVO> voList) {
			try {
				for (DishsNoShowVO vo : voList) {
					DishsNoShow showVo = dishsNoShowDao.getDishsNoShowVo(vo);
					if(showVo == null){
						DishsNoShow  entity = new DishsNoShow();
						entity.setDishsNoId(vo.getId());
						entity.setSampleFlag(vo.getSampleFlag());
						entity.setShowFlag(vo.getShowFlag());
						entity.setShowTime(vo.getShowTime());
						dishsNoShowDao.persistent(entity);
					}else{
						DishsNoShow e = dishsNoShowDao.findById(vo.getShowId());
						e.setSampleFlag(vo.getSampleFlag());
						e.setShowFlag(vo.getShowFlag());
					}
				}
				return true;
			} catch (JPAException e) {
				e.printStackTrace();
			}
		return false;
	}
}
