package com.gettec.fsnip.fsn.dao.erp.buss.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.buss.OutStorageRecordDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.buss.OutStorageRecord;

@Repository
public class OutStorageRecordDAOImpl extends BaseDAOImpl<OutStorageRecord> 
		implements OutStorageRecordDAO{

	public List<OutStorageRecord> getOutStorageRecordfilter_(int from,int size,String configure) throws DaoException{
		try{
			return this.getListByPage(from, size, configure);
		}catch(JPAException jpae){
			throw new DaoException("OutStorageRecordDAOImpl.getOutStorageRecordfilter_() "+jpae.getMessage(),jpae.getException());
		}
	}
	
	public long countOutStorageRecord(String configure) throws DaoException{
		try{
			String jpql = "SELECT count(*) FROM t_buss_out_storage_record e ";
			this.countBySql("t_buss_out_storage_record e ", configure, null);
			Query query = entityManager.createQuery(jpql);
			Object rtn = query.getSingleResult();
	    	return rtn == null ? 0 : Long.parseLong(rtn.toString());
		}catch(JPAException jpae){
			throw new DaoException("OutStorageRecordDAOImpl.countOutStorageRecord() "+jpae.getMessage(),jpae.getException());
		}
	}
	
	/**
	 * 获取出库已有的最大编号
	 * @param noStart
	 * @return
	 */
	@Override
	public String findNoMaxByNoStart(String noStart) throws DaoException {
		try {
			/* 按降序排序，获取已存在最大编号 */
			String sql = "SELECT NO FROM t_buss_out_storage_record where NO like ?1 ORDER BY NO DESC LIMIT 1";
			List<String> result = this.getListBySQLWithoutType(String.class, sql, new Object[]{noStart+"%"});
			if(result != null && result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new DaoException("OutStorageRecordDAOImpl.findNoMaxByNoStart() 获取出库已有的最大编号，出现异常！", e);
		}
	}
}
