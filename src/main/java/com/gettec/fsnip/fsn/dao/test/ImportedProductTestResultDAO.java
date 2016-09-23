package com.gettec.fsnip.fsn.dao.test;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.test.ImportedProductTestResult;
/**
 * ImportedProductTestResult customized operation implementation
 * 
 * @author LongXianZhen
 * 2015-05-26
 */
 public interface ImportedProductTestResultDAO extends BaseDAO<ImportedProductTestResult>{

	
	 /**
	  * 根据报告id删除进口食品报告信息（假删除）
	  * @author longxianzhen 2015/05/27
	  */
	void deleteByTRId(Long reportId) throws DaoException;

	
}