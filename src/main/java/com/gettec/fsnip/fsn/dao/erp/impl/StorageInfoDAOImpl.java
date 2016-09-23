package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.StorageInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.base.StorageInfo;

@Repository("storageInfoDAO")
public class StorageInfoDAOImpl extends BaseDAOImpl<StorageInfo> 
		implements StorageInfoDAO{

	@SuppressWarnings("unchecked")
	public List<StorageInfo> getStorageInfofilter_(int from,int size,String configure) {
		String jpql="select e from T_META_STORAGE_INFO e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
//		query.setFirstResult(from);  
//		query.setMaxResults(size); 
//		System.out.println(query);
		List<StorageInfo> result = query.getResultList();
		return result;
	}
	
	public StorageInfo findByName(String name,Long organization) {
		String sql = "select e from T_META_STORAGE_INFO e where e.organization="+organization+" and e.name=:name";
		return (StorageInfo) entityManager.createQuery(sql).setParameter("name", name).getResultList().get(0);
	}
	
	public long countStorageInfo(String configure) {
		String jpql = "SELECT count(*) FROM T_META_STORAGE_INFO e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
		Object rtn = query.getSingleResult();
    	return rtn == null ? 0 : Long.parseLong(rtn.toString());
	}
	
	@SuppressWarnings("unchecked")
	public List<StorageInfo> getStoragefilter(String filter,
			String name,String fieldName) {
		try {
			String sql = "SELECT e FROM T_META_STORAGE_INFO e WHERE e.name";
			Query query = null;
			if(filter.equals("eq")){
				sql += " = :name";
				query = entityManager.createQuery(sql);
				query.setParameter("name", name);
			}else if(filter.equals("neq")){
				sql += " <> :name";
				query = entityManager.createQuery(sql);
				query.setParameter("name", name);
			}else if(filter.equals("startswith")){
				sql += " like :name";
				query = entityManager.createQuery(sql);
				query.setParameter("name", name + "%");
			}else if(filter.equals("endswith")){
				sql += " like :name";
				query = entityManager.createQuery(sql);
				query.setParameter("name", "%" + name);
			}else if(filter.equals("contains")){
				sql += " like :name";
				query = entityManager.createQuery(sql);
				query.setParameter("name", "%" + name + "%");
			}else if(filter.equals("doesnotcontain")){
				sql += " not like :name";
				query = entityManager.createQuery(sql);
				query.setParameter("name", "%" + name + "%");
			}
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 商品实例ID或者产品ID获取库存中的仓库且库存大于0 
	 * @param productId
	 * @param storageId
	 * @param model
	 * @return 商品实例集合
	 * Author 郝圆彬
	 * 2014-10-28
	 * 修改
	 */
	@SuppressWarnings("unchecked")
	public List<StorageInfo> getStorageByProductIdOrInstanceId(Long productId,Long instanceId,Long organization) {
		String sql="";
		if(productId!=null){
				sql="SELECT distinct  t.* FROM t_meta_storage_info t "
						+"INNER JOIN t_buss_merchandise_storage_info s ON t.`NO`=s.NO_1 "
						+"INNER JOIN t_meta_merchandise_info_instance p ON s.NO_2=p.INSTANCE_ID "
						+"WHERE s.SYS_COUNT>0 and s.organization="+organization+" AND p.product_id="+productId;
		}else{
			sql="SELECT t.* FROM t_meta_storage_info t "
					+"INNER JOIN t_buss_merchandise_storage_info s ON t.`NO`=s.NO_1 "
					+"WHERE s.SYS_COUNT>0 and s.NO_2="+instanceId+" AND  s.organization="+organization;
		}	
			Query query = entityManager.createNativeQuery(sql.toString(),StorageInfo.class);
			List<StorageInfo> result = query.getResultList();
			return result;		
	}

	/**
	 * 根据组织机构查找仓库最大编号
	 * @param organization
	 * @return
	 */
	@Override
	public String findLastNoByOrg(Long organization) throws DaoException{
		try{
			String sql = "SELECT NO FROM t_meta_storage_info t where t.`NO` LIKE ?1 and t.organization=?2 ORDER BY t.`NO` DESC";
			List<String> result = this.getListBySQLWithoutType(String.class, sql, new Object[]{organization+"%",organization});
			if(result != null && result.size()>0){
				return result.get(0);
			}
			return null;
		}catch (JPAException jpae) {
			throw new DaoException("StorageInfoDAOImpl.findNoMaxByNoStart() 获取仓库已有的最大编号，出现异常！", jpae.getException());
		}
	}
}
