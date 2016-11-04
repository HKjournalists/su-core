package com.gettec.fsnip.fsn.service.carering.impl;

import com.gettec.fsnip.fsn.dao.catering.CateringDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.Catering;
import com.gettec.fsnip.fsn.service.carering.CateringService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.vo.catering.CateringVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CateringServiceImpl extends BaseServiceImpl<Catering, CateringDAO>
									implements CateringService {

	@Autowired
	private CateringDAO cateringDAO;
	@Override
	public CateringDAO getDAO() {
		return cateringDAO;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveOrUpdate(CateringVO cateringvo) {
		try {
			if(cateringvo != null){
				if(cateringvo.getId() != null&&cateringvo.getId()>0){
					Catering enety = cateringDAO.findById(cateringvo.getId());
					enety.setConsume(cateringvo.getConsume());
					enety.setStoreType(cateringvo.getStoreType());
					enety.setTelephone(cateringvo.getTelephone());
					enety.setLongitude(cateringvo.getLongitude());
					enety.setLatitude(cateringvo.getLatitude());
					enety.setPlaceName(cateringvo.getPlaceName());
				}else{
					Catering enety = new Catering();
					enety.setBusinessId(cateringvo.getBusinessId());
					enety.setConsume(cateringvo.getConsume());
					enety.setStoreType(cateringvo.getStoreType());
					enety.setTelephone(cateringvo.getTelephone());
					enety.setLongitude(cateringvo.getLongitude());
					enety.setLatitude(cateringvo.getLatitude());
					enety.setPlaceName(cateringvo.getPlaceName());
					cateringDAO.persistent(enety);
				}
			}
		} catch (JPAException e) {
			e.printStackTrace();
		}
	}

	@Override
	public CateringVO getCateringVOBusinessId(Long businessId) {

		Catering enety = cateringDAO.findByBusinessId(businessId);
        if(enety == null){
			return null;
		}
		CateringVO cateringVO = new CateringVO();
		cateringVO.setId(enety.getId());
		cateringVO.setBusinessId(enety.getBusinessId());
		cateringVO.setConsume(enety.getConsume());
		cateringVO.setStoreType(enety.getStoreType());
		cateringVO.setTelephone(enety.getTelephone());
		cateringVO.setLongitude(enety.getLongitude());
		cateringVO.setLatitude(enety.getLatitude());
		cateringVO.setPlaceName(enety.getPlaceName());
		return cateringVO;
	}
}


	
