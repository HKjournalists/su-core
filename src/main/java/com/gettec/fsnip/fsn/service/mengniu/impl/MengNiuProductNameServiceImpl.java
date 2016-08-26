package com.gettec.fsnip.fsn.service.mengniu.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.mengniu.MengNiuProductNameDAO;
import com.gettec.fsnip.fsn.model.mengniu.MengNiuProductName;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.mengniu.MengNiuProductNameService;
@Service
public class MengNiuProductNameServiceImpl extends BaseServiceImpl<MengNiuProductName, MengNiuProductNameDAO>
		implements MengNiuProductNameService {
	@Autowired
	private MengNiuProductNameDAO mengniuProductNameDAO;
	
	@Override
	public MengNiuProductNameDAO getDAO() {
		return mengniuProductNameDAO;
	}

	public boolean isProductNameExist(String product_name) {
		if(this.mengniuProductNameDAO.getfilter("eq", product_name, "e.productName").size()==0){
			return false;
		}else{
			return true;
		}
	}

}
