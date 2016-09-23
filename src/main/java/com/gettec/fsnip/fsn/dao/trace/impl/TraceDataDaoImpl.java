package com.gettec.fsnip.fsn.dao.trace.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.trace.TraceDataDao;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.lhfs.fsn.util.DateUtil;
@Repository
public class TraceDataDaoImpl extends BaseDAOImpl<TraceData> implements TraceDataDao {

	private static final Object formatted = null;
	@Override
	public boolean check(Long productID, String batchCode) {
		try {
			long count=this.count(" where e.productID=?1 and e.batchCode=?2",new Object[]{productID,batchCode});
			if(count>0){
				return false;
			}else{
				return true;
			}
		} catch (JPAException e1) {
			return true;
		}
	}

	@Override
	public TraceData findPagebyproductID(Long productID,int page) {
		String jpql = "SELECT e FROM " + entityClass.getName() + " e"+" where e.productID=?1";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1,productID);
		query.setFirstResult(page-1);
		query.setMaxResults(1);
		try{
			Object obj=query.getSingleResult();
			return (TraceData) obj;
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public long count(Long productID) {
		try {
			long count=this.count("where e.productID=?1",new Object[]{productID});
			return count;
		} catch (JPAException e) {
			e.printStackTrace();
			return 0;
		}
	}
	private TraceData gettraceDatabyproductIDandproductData(Long productID, int page,
			Date productDate){
		String jpql = "SELECT e FROM " + entityClass.getName() + " e"+" where e.productID=?1"+" and e.productDate=?2";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1,productID);
		query.setParameter(2,productDate);
		query.setFirstResult(page-1);
		try{
			return (TraceData) query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	@Override
	public TraceData findPagebyproductIDandproductDate(Long productID, int page,
			String productDate) {
		SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
		Date _productDate = null;
		try {
			_productDate=sim.parse(productDate);
		}catch(Exception e){

		}
		TraceData o=this.gettraceDatabyproductIDandproductData(productID, page, _productDate);
		if(o!=null){
			return (TraceData) o;
		}else{
			//List<TraceData> traceList=new ArrayList<TraceData>();
			List<TraceData> traceData=new ArrayList<TraceData>(); //当前日期之前最近的追溯信息
			String jpql = "SELECT e FROM " + entityClass.getName() + " e"+" where e.productID=?1 and e.productDate<?2 order by e.productDate desc";   
			Query query = entityManager.createQuery(jpql);
			query.setParameter(1,productID);
			query.setParameter(2,_productDate);
			query.setMaxResults(1);
			traceData.addAll( query.getResultList());
			List<TraceData> traceData1=new ArrayList<TraceData>();  //当前日期之后最近的追溯信息
			String jpql1 = "SELECT e FROM " + entityClass.getName() + " e"+" where e.productID=?1 and e.productDate>?2 order by e.productDate asc";    //当前日期之前最近的追溯信息
			Query query1 = entityManager.createQuery(jpql1);
			query1.setParameter(1,productID);
			query1.setParameter(2,_productDate);
			query1.setMaxResults(1);
			traceData1.addAll(query1.getResultList());
			if(traceData.size()>0&&traceData1.size()==0){
				return traceData.get(0);
			}else if(traceData.size()==0&&traceData1.size()>0){
				return traceData1.get(0);
			}else if(traceData.size()>0&&traceData1.size()>0){
				boolean falg;
				falg = DateUtil.compareDate(traceData.get(0).getProductDate(),productDate,traceData1.get(0).getProductDate());
				if(falg){
					return traceData.get(0);
				}else{
					return traceData1.get(0);
				}	
			}
		}
		return null;
	}
	@Override
	public TraceData findByKeyWord(String keyword) {
		String jpql = "SELECT e FROM " + entityClass.getName() + " e"+" where e.keyWord=?1";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1,keyword);
		try{
			return (TraceData) query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<TraceData> getbyOrgId(Long org,int page,int pageSize) {
		String jpql = "SELECT e FROM " + entityClass.getName() + " e"+" where e.organization=?1";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1,org);
		query.setFirstResult((page-1)*pageSize);
		query.setMaxResults(pageSize);
		try{
			return (List<TraceData>) query.getResultList();
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public long countbyOrg(long org) {
		String jpql = "SELECT count(*) FROM " + entityClass.getName() + " e"+" where e.organization=?1";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1,org);
		try{
			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		}catch(Exception e){
			return 0;
		}
	}
}

