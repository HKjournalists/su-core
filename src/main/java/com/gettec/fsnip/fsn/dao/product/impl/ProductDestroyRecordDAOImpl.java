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
	public List<ProductDestroyRecord> getbyOrgId(String orgname,int page,int pageSize,String keyword) {
		String jpql = "SELECT e FROM " + entityClass.getName() + " e"+" where e.operation_user=?1";
		if(!keyword.equals("")&&keyword!=null){
		jpql +=" and e.name=?2 or e.barcode=?2";
		}
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1,orgname);
		if(!keyword.equals("")&&keyword!=null){
			query.setParameter(2,keyword);
			}
		query.setFirstResult((page-1)*pageSize);
		query.setMaxResults(pageSize);
		try{
			return (List<ProductDestroyRecord>) query.getResultList();
		}catch(Exception e){
			return null;
		}
	}
	
	/*@Override
	public List<ProductDestroyRecord> getbyOrgIdandKeyword(String orgname,int page,int pageSize,String keyword) {
		String jpql = "SELECT e FROM " + entityClass.getName() + " e"+" where e.operation_user=?1 and e.name=?4 or e.barcode=?4";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1,orgname);
		query.setParameter(4,keyword);
		query.setFirstResult((page-1)*pageSize);
		query.setMaxResults(pageSize);
		try{
			return (List<ProductDestroyRecord>) query.getResultList();
		}catch(Exception e){
			return null;
		}
	}*/

	@Override
	public long countbyOrg(String orgname,String keyword) {
		String jpql = "SELECT count(*) FROM " + entityClass.getName() + " e"+" where e.operation_user=?1";
		if(!keyword.equals("")&&keyword!=null){
			jpql +=" and e.name=?2 or e.barcode=?2";
			}
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1,orgname);
		if(!keyword.equals("")&&keyword!=null){
			query.setParameter(2,keyword);
			}
		
		try{
			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		}catch(Exception e){
			return 0;
		}
	
	}
}
