package com.gettec.fsnip.fsn.dao.product.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductInstanceDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.product.ProductInstance;

/**
 * ProductInstance customized operation implementation
 * 
 * @author Ryan Wang
 */
@Repository(value = "productInstanceDAO")
public class ProductInstanceDAOImpl extends
		BaseDAOImpl<ProductInstance> implements ProductInstanceDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public ProductInstance findLastBySP(String serial, Long productId) {
		ProductInstance productInstance = null;
		if (productId != null) {
			StringBuilder sql = new StringBuilder();
			sql.append("select * from product_instance ");
			sql.append("where product_id = :product_id ");
			if(StringUtils.isNotBlank(serial)){
				sql.append("and serial = :serial ");
			}
			sql.append("order by production_date desc ");
			Query query = entityManager.createNativeQuery(sql.toString(), ProductInstance.class);
			query.setParameter("product_id", productId);
			if(StringUtils.isNotBlank(serial)){
				query.setParameter("serial", serial);
			}
			List<ProductInstance> result = query.getResultList();
			if (result != null && result.size() > 0) {
				productInstance = result.get(0);
			}
		}
		return productInstance;
	}

	@SuppressWarnings("unchecked")
	public ProductInstance findByBSP(String batchSerialNo, String serial,
			Long productId) {
		ProductInstance productInstance = null;
		if (StringUtils.isNotBlank(batchSerialNo)
				&& StringUtils.isNotBlank(serial) && productId != null) {
			StringBuilder sql = new StringBuilder();
			sql.append("select * from product_instance ");
			sql.append("where serial = :serial ");
			sql.append("and product_id = :product_id ");
			sql.append("and batch_serial_no = :batch_serial_no ");
			sql.append("order by production_date desc, serial");
			List<ProductInstance> result = entityManager
					.createNativeQuery(sql.toString(), ProductInstance.class)
					.setParameter("serial", serial)
					.setParameter("product_id", productId)
					.setParameter("batch_serial_no", batchSerialNo)
					.getResultList();
			if (result != null && result.size() > 0) {
				productInstance = result.get(0);
			}
		}
		return productInstance;
	}
	
	@SuppressWarnings("unchecked")
	public ProductInstance findByBSB(String batchSerialNo, String serial,
			String barcode) {
		ProductInstance productInstance = null;
		if (StringUtils.isNotBlank(batchSerialNo)
				&& StringUtils.isNotBlank(serial) && StringUtils.isNotBlank(barcode)) {
			StringBuilder sql = new StringBuilder();
			sql.append("select * from product_instance pi ");
			sql.append("inner join product p on pi.product_id = p.id ");
			sql.append("where pi.serial = :serial ");
			sql.append("and p.barcode = :barcode ");
			sql.append("and pi.batch_serial_no = :batch_serial_no ");
			sql.append("order by pi.production_date desc, pi.serial");
			List<ProductInstance> result = entityManager
					.createNativeQuery(sql.toString(), ProductInstance.class)
					.setParameter("serial", serial)
					.setParameter("barcode", barcode)
					.setParameter("batch_serial_no", batchSerialNo)
					.getResultList();
			if (result != null && result.size() > 0) {
				productInstance = result.get(0);
			}
		}
		return productInstance;
	}

	@SuppressWarnings("unchecked")
	public ProductInstance findByBatchAndProductId(String batchSerialNo, Long productId) {
		ProductInstance productInstance = null;
		if (StringUtils.isNotBlank(batchSerialNo)&& productId != null) {
			StringBuilder sql = new StringBuilder();
			sql.append("select * from product_instance ");
			sql.append("where product_id = :product_id ");
			sql.append("and batch_serial_no = :batch_serial_no ");
			sql.append("order by product_id desc");
			List<ProductInstance> result = entityManager
					.createNativeQuery(sql.toString(), ProductInstance.class)
					.setParameter("product_id", productId)
					.setParameter("batch_serial_no", batchSerialNo)
					.getResultList();
			if (result != null && result.size() > 0) {
				productInstance = result.get(0);
			}
		}
		return productInstance;
	}
	
	@Override
	public List<ProductInstance> findProductInstancesByPID(Long productId) {
		return this.findProductInstances(null, null, productId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductInstance> findProductInstances(String batchSerialNo,
			String serial, Long productId) {
		
		List<ProductInstance> result = null;
		StringBuilder sql = new StringBuilder();
		sql.append("select * from product_instance ");
		sql.append("where 1=1 ");
		if(StringUtils.isNotBlank(batchSerialNo)){
			sql.append("and batch_serial_no like :batch_serial_no ");
		}
		if(StringUtils.isNotBlank(serial)){
			sql.append("and serial like :serial ");
		}
		if(productId != null){
			sql.append("and product_id = :product_id ");
		}
		
		sql.append("order by batch_serial_no desc");

		Query query = entityManager.createNativeQuery(sql.toString(), ProductInstance.class);
		
		if(StringUtils.isNotBlank(batchSerialNo)){
			query.setParameter("batch_serial_no", batchSerialNo);
		}
		if(StringUtils.isNotBlank(serial)){
			query.setParameter("serial", serial);
		}
		if(productId != null){
			query.setParameter("product_id", productId);
		}
		
		result = query.getResultList();
		return result;
	}

	/**
	 * 按产品条形码、批次获取产品实例
	 */
	@Override
	public ProductInstance getListByBarcodeAndBatchSerialNo(
			String barcode, String batchSerialNo) throws DaoException {
		try {
			String condition = " WHERE e.product.barcode = ?1 AND e.batchSerialNo = ?2";
			Object[] params = new Object[]{barcode, batchSerialNo};
			List<ProductInstance> result = this.getListByCondition(condition, params);
			if(result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【dao-error】按产品条形码、批次获取产品实例，出现异常！", jpae.getException());
		}
	}
	/**
	 * 根据产品ID和仓库ID获取商品库存中的商品实例，主要是获取批次
	 * @param productId
	 * @param storageId
	 * @param organization
	 * @return 产品实例集合
	 * Author 郝圆彬
	 * 2014-10-23
	 * 新增
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductInstance> getProductInstancesByStorageInfoAndStorage(
			Long productId, String storageId, Long organization) {
				String sql="";
				if(storageId==null){
						sql="SELECT DISTINCT p.* FROM t_meta_merchandise_info_instance as p "
							+"inner JOIN t_buss_merchandise_storage_info as s on p.INSTANCE_ID=s.NO_2 "
							+"where s.SYS_COUNT>0 and p.product_id="+productId+" AND s.organization="+organization+" ";
				}else{
					sql="SELECT p.* FROM t_meta_merchandise_info_instance as p "
							+"inner JOIN t_buss_merchandise_storage_info as s on p.INSTANCE_ID=s.NO_2 "
							+"where s.SYS_COUNT>0 and p.product_id="+productId+" AND s.NO_1="+storageId+" AND s.organization="+organization+" ";
				}	
					Query query = entityManager.createNativeQuery(sql.toString(),ProductInstance.class);
					List<ProductInstance> result = query.getResultList();
					return result;					
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductInstance> getInfoNofilter(String filter, String name,String fieldName) {
		try {
			String sql = "select e from product_instance e where e.product in ( " +
					" select id from product where barcode ";
			Query query = null;
			if(filter.equals("eq")){
				sql += " = :name";
				sql += ")";
				query = entityManager.createQuery(sql);
				query.setParameter("name", name);
			}else if(filter.equals("neq")){
				sql += " <> :name";
				sql += ")";
				query = entityManager.createQuery(sql);
				query.setParameter("name", name);
			}else if(filter.equals("startswith")){
				sql += " like :name";
				sql += ")";
				query = entityManager.createQuery(sql);
				query.setParameter("name", name + "%");
			}else if(filter.equals("endswith")){
				sql += " like :name";
				sql += ")";
				query = entityManager.createQuery(sql);
				query.setParameter("name", "%" + name + "");
			}else if(filter.equals("contains")){
				sql += " like :name";
				sql += ")";
				query = entityManager.createQuery(sql);
				query.setParameter("name", "%" + name + "%");
			}else if(filter.equals("doesnotcontain")){
				sql += " not like :name";
				sql += ")";
				query = entityManager.createQuery(sql);
				query.setParameter("name", "%" + name + "%");
			}
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductInstance> getInfoNamefilter(String filter, String name,String fieldName, Long organization) {
		try {
			String sql = "select e from product_instance e where e.product in ( " +
					" select id from product where organization = " + organization + " and name ";
			Query query = null;
			if(filter.equals("eq")){
				sql += " = :name";
				sql += ")";
				query = entityManager.createQuery(sql);
				query.setParameter("name", name);
			}else if(filter.equals("neq")){
				sql += " <> :name";
				sql += ")";
				query = entityManager.createQuery(sql);
				query.setParameter("name", name);
			}else if(filter.equals("startswith")){
				sql += " like :name";
				sql += ")";
				query = entityManager.createQuery(sql);
				query.setParameter("name", name + "%");
			}else if(filter.equals("endswith")){
				sql += " like :name";
				sql += ")";
				query = entityManager.createQuery(sql);
				query.setParameter("name", "%" + name + "");
			}else if(filter.equals("contains")){
				sql += " like :name";
				sql += ")";
				query = entityManager.createQuery(sql);
				query.setParameter("name", "%" + name + "%");
			}else if(filter.equals("doesnotcontain")){
				sql += " not like :name";
				sql += ")";
				query = entityManager.createQuery(sql);
				query.setParameter("name", "%" + name + "%");
			}
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductInstance> getInfoUnitNamefilter(String filter,String name, String fieldName, Long organization) {
		try {
			String sql = "select e from product_instance e where e.product in ( " +
					" select id from product where organization = " + organization + " and unit in (" +
					" select id from T_META_UNIT where name ";
			Query query = null;
			if(filter.equals("eq")){
				sql += " = :name";
				sql += "))";
				query = entityManager.createQuery(sql);
				query.setParameter("name", name);
			}else if(filter.equals("neq")){
				sql += " <> :name";
				sql += "))";
				query = entityManager.createQuery(sql);
				query.setParameter("name", name);
			}else if(filter.equals("startswith")){
				sql += " like :name";
				sql += "))";
				query = entityManager.createQuery(sql);
				query.setParameter("name", name + "%");
			}else if(filter.equals("endswith")){
				sql += " like :name";
				sql += "))";
				query = entityManager.createQuery(sql);
				query.setParameter("name", "%" + name + "");
			}else if(filter.equals("contains")){
				sql += " like :name";
				sql += "))";
				query = entityManager.createQuery(sql);
				query.setParameter("name", "%" + name + "%");
			}else if(filter.equals("doesnotcontain")){
				sql += " not like :name";
				sql += "))";
				query = entityManager.createQuery(sql);
				query.setParameter("name", "%" + name + "%");
			}
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 根据产品barcode和当前登录企业ID查找是否有质检报告
	 * @param barcode 条形码
	 * @param producerId 企业ID
	 * @return 产品实例
	 */
	@Override
	public ProductInstance findByBarcodeAndProducerId(String barcode,
			Long producerId) throws DaoException {
		try{
			String condition = " WHERE product.barcode = ?1 AND producer.id = ?2 ";	
			List<ProductInstance> result = this.getListByCondition(condition, new Object[]{barcode, producerId});
			if(result != null && result.size()>0){
				return result.get(0);
			}
			return null;
		}catch(JPAException jpae){
			throw new DaoException("ProductInstanceDAOImpl-->findByBarcodeAndProducerId"+jpae.getMessage(),jpae);
		}
	}
	/**
	 * 根据产品条形码和批次号查找产品实例
	 * @param barcode 条形码
	 * @param batchSeriaNo 批次号
	 * @author ZhaWanNeng
	 * 更新时间2015/3/17
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProductInstance findInstance(String barcode, String batchSeriaNo) {
		List<ProductInstance> result = entityManager
				.createNativeQuery(
						" select * from product_instance where "+
						" product_instance.product_id in (select product.id from product where product.barcode = ?1) and "+
						" product_instance.batch_serial_no = ?2 ",
						ProductInstance.class)
				.setParameter(1, barcode)
				.setParameter(2, batchSeriaNo)
				.getResultList();
		if(result != null && result.size() > 0){
			return result.get(0);
		}
		return null;
	}
	
	/**
	 * 根据产品id获取样品id
	 * @param productId
	 * @author ZhaWanNeng
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> findInstancebyProductId(Long productId)throws DaoException {
		try {
            String sql = "select e.id from product_instance e where e.product_id=?1";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, productId);
            List<Long> productInstanceId=new ArrayList<Long>();
            List<Object> list = query.getResultList();
            for (Object objects : list) {
            	productInstanceId.add(Long.valueOf(objects.toString()));
		    }
		     return productInstanceId;
		} catch (Exception e) {
	            throw new DaoException("ProductInstanceDAOImpl.findInstancebyProductId()-->"+"dao层按条件查询检测报告出错", e);
	    }
	}
}
