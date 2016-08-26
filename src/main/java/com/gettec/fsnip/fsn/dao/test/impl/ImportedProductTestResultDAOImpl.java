package com.gettec.fsnip.fsn.dao.test.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.test.ImportedProductTestResultDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.test.ImportedProductTestResult;

/**
 * ImportedProductTestResultDAO customized operation implementation
 * 
 * @author LongXianZhen
 * 2015-05-26
 */
@Repository(value="importedProductTestResultDAO")
public class ImportedProductTestResultDAOImpl extends BaseDAOImpl<ImportedProductTestResult>
		implements ImportedProductTestResultDAO {

	 /**
	  * 根据报告id删除进口食品报告信息（假删除）
	  * @author longxianzhen 2015/05/27
	  */
	@Override
	public void deleteByTRId(Long reportId) throws DaoException {
		try{
			String sql = " UPDATE test_result_imported_product e SET e.del=1 WHERE e.test_result_id = ?1 ";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, reportId);
			query.executeUpdate();
		}catch(Exception jpae){
			throw new DaoException("ImportedProductTestResultDAOImpl.deleteByTRId()->"+jpae.getMessage(),jpae);
		}
	}
	
	
}