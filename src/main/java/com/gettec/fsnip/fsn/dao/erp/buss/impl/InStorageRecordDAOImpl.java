package com.gettec.fsnip.fsn.dao.erp.buss.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.buss.InStorageRecordDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.buss.InStorageRecord;

@Repository
public class InStorageRecordDAOImpl extends BaseDAOImpl<InStorageRecord> 
		implements InStorageRecordDAO{

	public List<InStorageRecord> getInStorageRecordfilter_(int from,int size,String configure) throws DaoException{
		try{
			return this.getListByPage(from, size, configure);
		}catch(JPAException jpae){
			throw new DaoException("InStorageRecordDAOImpl.getInStorageRecordfilter_() "+jpae.getMessage(),jpae.getException());
		}
	}
	
	public long countInStorageRecord(String configure) throws DaoException {
		try{
			String jpql = "SELECT count(*) FROM T_BUSS_IN_STORAGE_RECORD e ";
			this.countBySql("T_BUSS_IN_STORAGE_RECORD e ", configure, null);
			Query query = entityManager.createQuery(jpql);
			Object rtn = query.getSingleResult();
	    	return rtn == null ? 0 : Long.parseLong(rtn.toString());
		}catch(JPAException jpae){
			throw new DaoException("InStorageRecordDAOImpl.countInStorageRecord() "+jpae.getMessage(),jpae.getException());
		}
	}
	
	/**
	 * 获取入库已有的最大编号
	 * @param noStart
	 * @return
	 */
	@Override
	public String findNoMaxByNoStart(String noStart) throws DaoException {
		try {
			/* 按降序排序，获取已存在最大编号 */
			String sql = "SELECT NO FROM t_buss_in_storage_record where NO like ?1 ORDER BY NO DESC LIMIT 1";
			List<String> result = this.getListBySQLWithoutType(String.class, sql, new Object[]{noStart+"%"});
			if(result != null && result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new DaoException("InStorageRecordDAOImpl.findNoMaxByNoStart() 获取入库已有的最大编号，出现异常！", e);
		}
	}
}
