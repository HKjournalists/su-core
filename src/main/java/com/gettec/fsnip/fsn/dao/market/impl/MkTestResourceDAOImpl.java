package com.gettec.fsnip.fsn.dao.market.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.market.MkTestResourceDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.market.Resource;

@Repository(value="testResourceDAO")
public class MkTestResourceDAOImpl extends BaseDAOImpl<Resource> 
					implements MkTestResourceDAO{
	
	@PersistenceContext private EntityManager entityManager;
	
	/**
	 * 按文件名称查询一条资源
	 * @throws DaoException 
	 */
	@Override
	public Resource getResourceByFileName(String fileName) throws DaoException{
		try {
			String condition = " WHERE e.fileName = ?1";
			List<Resource> resultList = this.getListByCondition(condition, new Object[]{fileName});
			if(resultList.size() > 0){
				return resultList.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("MkTestResourceDAOImpl.getResourceByFileName() "+jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 按资源id到t_test_product_to_resource关系表中查找记录条数
	 * @throws DaoException 
	 */
	@Override
	public long getRelationCountByResourceId(Long resourceId)
			throws DaoException {
		try {
			Object rtn = entityManager
					.createNativeQuery(
							"SELECT COUNT(*) FROM t_test_product_to_resource WHERE RESOURCE_ID = :id")
							.setParameter("id", resourceId)
							.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (Exception e) {
			throw new DaoException("MkTestResourceDAOImpl.getRelationCountByResourceId() "+e.getMessage(), e);
		}
	}

	/**
	 * 根据企业id返回企业上传的pdf，仁怀酒业系统用
	 */
	@Override
	public List<Resource> getListBusPdfByBusUnitIdWithPage(int page,
			int pageSize,Long busUnitId) throws DaoException {
		try{
			String sql="select * from t_test_resource where RESOURCE_ID in (select resource_id from business_pdf_to_resource where business_id= ?1) order by UPLOAD_DATE desc";
			return this.getListBySQL(Resource.class, sql, new Object[]{busUnitId});
		}catch(JPAException jpae){
			throw new DaoException("MkTestResourceDAOImpl.getListBusPdfByBusUnitIdWithPage() "+jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 根据产品id查找产品图片集合按上传时间排序
	 * @author longxianzhen 2015/06/10
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Resource> getProductImgListByproId(Long proId)
			throws DaoException {
		try {
			String sql="SELECT tr.* FROM t_test_product_to_resource ttr LEFT JOIN t_test_resource tr ON tr.RESOURCE_ID=ttr.RESOURCE_ID WHERE ttr.PRODUCT_ID=?1 ORDER BY tr.UPLOAD_DATE,tr.RESOURCE_ID";
			Query query=entityManager.createNativeQuery(sql,Resource.class);
			query.setParameter(1, proId);
			List<Resource> list=query.getResultList();
			return list;
		} catch (Exception e) {
			throw new DaoException("MkTestResourceDAOImpl.getProductImgListByproId() "+e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Resource> getRebackImgListByreportId(Long reportId)
			throws DaoException {
		try {
			String sql="SELECT tr.* FROM t_test_report_back_to_resource ttr LEFT JOIN t_test_resource tr ON tr.RESOURCE_ID=ttr.RESOURCE_ID WHERE ttr.REPORT_ID=?1";
			Query query=entityManager.createNativeQuery(sql,Resource.class);
			query.setParameter(1, reportId);
			List<Resource> list=query.getResultList();
			return list;
		} catch (Exception e) {
			throw new DaoException("MkTestResourceDAOImpl.getProductImgListByproId() "+e.getMessage(), e);
		}
	}

	@Override
	public Map<String, String> getBusinessUnitCertById(Long buId)
			throws DaoException {
		try {
			Map<String,String> map=new HashMap<String, String>();
			String sql="SELECT ttr.URL FROM business_unit bu "+
						"LEFT JOIN enterprise_registe er ON bu.`name`=er.enterpriteName "+
						"LEFT JOIN t_business_license_to_resource  tlr ON tlr.ENTERPRISE_REGISTE_ID=er.id "+
						"LEFT JOIN t_test_resource ttr ON ttr.RESOURCE_ID=tlr.RESOURCE_ID "+
						"WHERE bu.id=?1 ORDER BY ttr.RESOURCE_ID DESC LIMIT 0,1";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, buId);
			String licUrl=null;
			try {
				Object obj=query.getSingleResult();
				licUrl=obj!=null?obj.toString():null;
			} catch (NoResultException e) {
				licUrl=null;
			}
			map.put("licUrl", licUrl);
			
			sql="SELECT ttr.URL FROM business_unit bu "+
					"LEFT JOIN enterprise_registe er ON bu.`name`=er.enterpriteName "+
					"LEFT JOIN t_org_license_to_resource  tlr ON tlr.ENTERPRISE_REGISTE_ID=er.id "+
					"LEFT JOIN t_test_resource ttr ON ttr.RESOURCE_ID=tlr.RESOURCE_ID "+
					"WHERE bu.id=?2 ORDER BY ttr.RESOURCE_ID DESC LIMIT 0,1";
			query=entityManager.createNativeQuery(sql);
			query.setParameter(2, buId);
			String orgUrl=null;
			try {
				Object obj=query.getSingleResult();
				orgUrl=obj!=null?obj.toString():null;
			} catch (Exception e) {
				orgUrl=null;
			}
			map.put("orgUrl", orgUrl);
			
			sql="SELECT ttr.URL FROM business_unit bu "+
					"LEFT JOIN enterprise_registe er ON bu.`name`=er.enterpriteName "+
					"LEFT JOIN t_business_distribution_to_resource  tlr ON tlr.ENTERPRISE_REGISTE_ID=er.id "+
					"LEFT JOIN t_test_resource ttr ON ttr.RESOURCE_ID=tlr.RESOURCE_ID "+
					"WHERE bu.id=?3 ORDER BY ttr.RESOURCE_ID DESC LIMIT 0,1";
			query=entityManager.createNativeQuery(sql);
			query.setParameter(3, buId);
			String disUrl=null;
			try {
				Object obj=query.getSingleResult();
				disUrl=obj!=null?obj.toString():null;
			} catch (Exception e) {
				disUrl=null;
			}
			map.put("disUrl", disUrl);
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("MkTestResourceDAOImpl.getProductImgListByproId() "+e.getMessage(), e);
		}
	}

	/**
	 * 根据qs号id查询qs图片资源
	 * @author longxianzhen 2015/08/06
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Resource> getQsResourceByQsId(Long qsId) throws DaoException {
		try {
			String sql="SELECT ts.* FROM  productionlicenseinfo_to_resource ppr "+
				"LEFT JOIN t_test_resource ts ON ts.RESOURCE_ID=ppr.resource_id WHERE ppr.qs_id=?1";
			Query query=entityManager.createNativeQuery(sql,Resource.class);
			query.setParameter(1, qsId);
			List<Resource> re=query.getResultList();
			return re;
		}catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("MkTestResourceDAOImpl.getQsResourceByQsId() "+e.getMessage(), e);
		}
	}
	
	public void deleteResourceByResultId(long resultId){
		String sql="delete from t_test_result_pic_to_resource where test_result_id=?1";
		Query query=entityManager.createNativeQuery(sql,Resource.class);
		query.setParameter(1, resultId);
		query.executeUpdate();
		sql="delete from t_test_result_check_to_resource where test_result_id=?1";
		query=entityManager.createNativeQuery(sql,Resource.class);
		query.setParameter(1, resultId);
		query.executeUpdate();
		sql="delete from t_test_result_buy_to_resource where test_result_id=?1";
		query=entityManager.createNativeQuery(sql,Resource.class);
		query.setParameter(1, resultId);
		query.executeUpdate();
	}
}
