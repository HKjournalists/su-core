package com.gettec.fsnip.fsn.dao.recycle;

import java.util.HashMap;
import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.ProductDestroyRecord;

public interface RecycleDAO extends BaseDAO<ProductDestroyRecord> {

	/**
	 * 增加产品回收记录
	 */
	public void insert_recycle_record (ProductDestroyRecord productDestroyRecord) throws DaoException;
	
	public Integer getRecords_count(HashMap<String,String> param) throws Exception;
	
	/**
	 * 查询产品回收/销毁记录
	 * @param param
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getRecords(HashMap<String,String> param) throws Exception;
}