package com.gettec.fsnip.fsn.dao.business.impl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.business.ProductBusinessLicenseDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;

import com.gettec.fsnip.fsn.exception.JPAException;


import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.ProductBusinessLicense;
@Repository(value="productBusinessLicenseDAO")
public class ProductBusinessLicenseDAOImpl extends BaseDAOImpl<ProductBusinessLicense> 
implements ProductBusinessLicenseDAO {
	@PersistenceContext private EntityManager entityManager;
	@Override
	public void saveProductBusinessLicense(ProductBusinessLicense entity) {
		//String sql = " INSERT INTO product_business_license "
	}
	@Override
	public ProductBusinessLicense getsetBusinessId(Long businseeId,Long productId) {
			try {
				String condition = " WHERE e.businessId = ?1 AND e.productId = ?2 ";
				List<ProductBusinessLicense> result = this.getListByCondition(condition, new Object[]{businseeId,productId});
				if(result.size()>0){
					return result.get(0);
				}
				return null;
			} catch (JPAException e) {
				e.printStackTrace();
			}
		return null;
	}
	@Override
	public void delResourceIdProductBusinessLicense(Long resourceId) {
		String sql = "DELETE FROM product_business_license WHERE RESOURCE_ID=?1";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, resourceId);
		query.executeUpdate();
	}
	@Override
	public void getsetProductId(Long id) {
		String sql = "DELETE FROM product_business_license WHERE product_id=?1";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, id);
		query.executeUpdate();
	}
	/**
	 * 修改营业执照图片
	 */
	@Override
	public void updateResource(Resource resource) {
		String sql = "UPDATE  T_TEST_RESOURCE SET FILE_NAME = ?1,RESOURCE_NAME = ?2,URL = ?3,RESOURCE_TYPE_ID= ?4 WHERE RESOURCE_ID = ?5";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, resource.getFileName());
		query.setParameter(2, resource.getName());
		query.setParameter(3, resource.getUrl());
		query.setParameter(4, resource.getType().getRtId());
		query.setParameter(5, resource.getId());
		query.executeUpdate();
	}
	@Override
	public void updateProductBusiness(Long id, String stgImg) {
		String sql = "UPDATE product_business_license ";
		boolean flag = false;
		if(id!=null&&id>-1&&"licImg".equals(stgImg)){
			sql+="  SET RESOURCE_ID=NULL   WHERE RESOURCE_ID=?1";
			flag =true;
		}
		if(id!=null&&id>-1&&"qsImg".equals(stgImg)){
			sql+=" SET QS_RESOURCE_ID=NULL   WHERE QS_RESOURCE_ID=?1";
			flag =true;
		}
		if(id!=null&&id>-1&&"disImg".equals(stgImg)){
			sql+=" SET DIS_RESOURCE_ID=NULL   WHERE DIS_RESOURCE_ID=?1";
			flag =true;
		}
		if(flag){
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, id);
		query.executeUpdate();
	  }
	}
	
	public List<ProductBusinessLicense> getProductBusinessLicenseListByProductId(long productId){
		String sql="select e from "+this.entityClass.getName()+" e where e.productId=?1";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, productId);
		return query.getResultList();
	}
}
