package com.gettec.fsnip.fsn.dao.product.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductDestroyRecordDAO;
import com.gettec.fsnip.fsn.model.product.ProductDestroyRecord;
import com.gettec.fsnip.fsn.model.trace.TraceData;
/**
 * 问题产品销毁记录操作，基础数据，没有增、删、改操作
 * @author xuetaoyang 2016/08/11
 *
 */
@Repository
public class ProductDestroyRecordDAOImpl extends BaseDAOImpl<ProductDestroyRecord> 
implements ProductDestroyRecordDAO {
	@Override
	public List<ProductDestroyRecord> getbyOrgId(String orgname,int page,int pageSize) {
		String jpql = "SELECT e FROM " + entityClass.getName() + " e"+" where e.operation_user=?1";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1,orgname);
		query.setFirstResult((page-1)*pageSize);
		query.setMaxResults(pageSize);
		try{
			return (List<ProductDestroyRecord>) query.getResultList();
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public long countbyOrg(String orgname) {
		String jpql = "SELECT count(*) FROM " + entityClass.getName() + " e"+" where e.operation_user=?1";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1,orgname);
		try{
			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		}catch(Exception e){
			return 0;
		}
	
	}
}
