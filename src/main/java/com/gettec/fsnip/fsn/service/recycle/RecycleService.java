package com.gettec.fsnip.fsn.service.recycle;

import java.util.HashMap;
import java.util.List;
import com.gettec.fsnip.fsn.dao.recycle.RecycleDAO;
import com.gettec.fsnip.fsn.model.product.ProductDestroyRecord;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface RecycleService extends BaseService<ProductDestroyRecord, RecycleDAO>{
	
	public Integer getRecords_count(HashMap<String,String> param) throws Exception;
	
	@SuppressWarnings("rawtypes")
	public List getRecords(HashMap<String,String> param) throws Exception;
}