package com.gettec.fsnip.fsn.dao.erp.buss.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.buss.MerchandiseInstanceDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;

@Repository
public class MerchandiseInstanceDAOImpl extends BaseDAOImpl<MerchandiseInstance> 
		implements MerchandiseInstanceDAO{

	/**
	 * 按产品实例id和批次查找一条商品实例信息
	 * @throws DaoException 
	 */
	@Override
	public MerchandiseInstance getMerchandiseInstanceByNoAndBatchNumber(
			Long initProId, String batchNo) throws DaoException {
		try {
			String condition = " WHERE e.initializeProduct.id = ?1 AND e.batch_number = ?2 ";
			List<MerchandiseInstance> result = this.getListByCondition(condition, new Object[]{initProId, batchNo});
			if(result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("MerchandiseInstanceDAOImpl.getMerchandiseInstanceByNoAndBatchNumber() 按产品实例id和批次查找一条商品实例信息，出现异常", jpae.getException());
		}
	}

	@SuppressWarnings("unchecked")
	public List<MerchandiseInstance> getInfoNofilter(String filter, String name,String fieldName) {
		try {
			String sql = "select e from T_META_MERCHANDISE_INFO_INSTANCE e where e.product in ( " +
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
	public List<MerchandiseInstance> getInfoNamefilter(String filter, String name,String fieldName, Long organization) {
		try {
			String sql = "select e from T_META_MERCHANDISE_INFO_INSTANCE e where e.product in ( " +
					" select id from product where name ";
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
	public List<MerchandiseInstance> getInfoUnitNamefilter(String filter,String name, String fieldName, Long organization) {
		try {
			String sql = "select e from T_META_MERCHANDISE_INFO_INSTANCE e where e.product in ( " +
					" select id from product where unit in (" +
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

	@SuppressWarnings("unchecked")
	@Override
	public List<MerchandiseInstance> getProductInstancesByStorageInfoAndStorage(
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
			Query query = entityManager.createNativeQuery(sql.toString(),MerchandiseInstance.class);
			List<MerchandiseInstance> result = query.getResultList();
			return result;		
	}
}
