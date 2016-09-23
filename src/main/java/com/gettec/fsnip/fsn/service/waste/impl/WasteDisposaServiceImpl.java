package com.gettec.fsnip.fsn.service.waste.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.waste.WasteDisposaDao;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.waste.WasteDisposa;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.waste.WasteDisposaService;
import com.gettec.fsnip.fsn.vo.waste.WasteDisposaVO;
import com.lhfs.fsn.vo.business.BussinessUnitVO;

@Service(value = "wasteDisposaService")
public class WasteDisposaServiceImpl extends BaseServiceImpl<WasteDisposa, WasteDisposaDao> 
implements WasteDisposaService {

	@Autowired 
	private WasteDisposaDao wasteDisposaDao;
	
	@Override
	public WasteDisposaDao getDAO() {
		return wasteDisposaDao;
	}

	@Override
	public List<WasteDisposaVO> loadWasteDisposa(int page, int pageSize,
			String handler,long qiyeId) throws DaoException {
		return getDAO().loadWasteDisposa(page, pageSize, handler,qiyeId);
	}

	@Override
	public long getWasteTotal(String handler,long qiyeId) throws DaoException {
		return getDAO().getWasteTotal(handler,qiyeId);
	}
	
	@Override
	public List<WasteDisposaVO> getListWaste(String orgId,String date) throws DaoException {
		return getDAO().getListWaste(orgId,date);
	}

	@Override
	public BussinessUnitVO getBussinessUnitVO(String licenseNo, String qsNo,
			String businessName) {
		return getDAO().getBussinessUnitVO(licenseNo, qsNo,businessName);
	}
	
}
