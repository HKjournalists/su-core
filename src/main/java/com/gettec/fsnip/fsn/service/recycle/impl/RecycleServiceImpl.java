package com.gettec.fsnip.fsn.service.recycle.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gettec.fsnip.fsn.dao.recycle.RecycleDAO;
import com.gettec.fsnip.fsn.model.product.ProductDestroyRecord;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.recycle.RecycleService;

@Service(value="recycleService")
public class RecycleServiceImpl extends BaseServiceImpl<ProductDestroyRecord, RecycleDAO> implements RecycleService{
	@Autowired
	private RecycleDAO recycleDAO;

	@Override
	public RecycleDAO getDAO() {
		return recycleDAO;
	}
	
	public Integer getRecords_count(HashMap<String,String> param) throws Exception {
		return recycleDAO.getRecords_count(param);
	}
	
	@SuppressWarnings("rawtypes")
	public List getRecords(HashMap<String,String> param) throws Exception {
		return recycleDAO.getRecords(param);
	}
}