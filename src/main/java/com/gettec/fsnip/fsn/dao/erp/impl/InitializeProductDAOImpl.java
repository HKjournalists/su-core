package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.InitializeProductDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.base.InitializeProduct;

@Repository(value = "IntroduceProductsDAO")
public class InitializeProductDAOImpl extends BaseDAOImpl<InitializeProduct> 
implements InitializeProductDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<InitializeProduct> getAllOutInitializeProduct(Long organization) {
		String sql="SELECT DISTINCT	t.*  FROM t_meta_initialize_product t "
						+"INNER JOIN t_meta_merchandise_info_instance t1 ON t1.product_id=t.id "
						+"INNER JOIN t_buss_merchandise_storage_info t2 ON t2.NO_2=t1.INSTANCE_ID "
						+"WHERE t2.SYS_COUNT>0 AND t.first_storage_id IS NOT NULL and t2.organization="+organization;
		Query query = entityManager.createNativeQuery(sql.toString(),InitializeProduct.class);
		List<InitializeProduct> result = query.getResultList();
		return result;	
	}

	/**
	 * 按产品id和组织机构查找一条产品初始化信息
	 * @param productId
	 * @param organization
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public InitializeProduct findByProIdAndOrgId(Long productId, Long organization) throws DaoException {
		try {
			String condition = " WHERE e.product.id = ?1 and e.organization = ?2";
			List<InitializeProduct> result = this.getListByCondition(condition, new Object[]{productId, organization});
			if(result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("InitializeProductDAOImpl.findByProIdAndOrgId() 按产品id和组织机构查找一条产品初始化信息，出现异常！", jpae.getException());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InitializeProduct> getAllInitializeProduct(int page,
			int pageSize, Long organization) {
		String sql="SELECT e.* FROM t_meta_initialize_product e "
				+" WHERE e.organization="+organization+" AND e.first_storage_id is not null";
		Query query = entityManager.createNativeQuery(sql.toString(),InitializeProduct.class);
		if (page > 0) {
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
		}
		List<InitializeProduct> result = query.getResultList();
		return result;	
	}

	@Override
	public Long getCountInitializeProduct(Long organization) {
		String sql="SELECT count(*) FROM t_meta_initialize_product e "
				+" WHERE e.organization="+organization+" AND e.first_storage_id is not null";
		Query query = entityManager.createNativeQuery(sql.toString());
		return ((Number)query.getSingleResult()).longValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<InitializeProduct> getInitializeProducts(Long organization) {
		String sql="SELECT e.* FROM t_meta_initialize_product e "
				+" WHERE e.organization="+organization+" AND e.first_storage_id is not null";
		Query query = entityManager.createNativeQuery(sql.toString(),InitializeProduct.class);
		List<InitializeProduct> reslut = query.getResultList();
		return reslut;
	}

	/**
	 * 按产品条形码和组织机构查找一条产品初始化信息
	 * @param barcode
	 * @param organization
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public InitializeProduct findByBarcodeAndOrgId(String barcode,
			Long organization) throws DaoException {
		try {
			String condition = " WHERE e.product.barcode = ?1 and e.organization = ?2";
			List<InitializeProduct> result = this.getListByCondition(condition, new Object[]{barcode, organization});
			if(result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("InitializeProductDAOImpl.findByBarcodeAndOrgId() 按产品条形码和组织机构查找一条产品初始化信息，出现异常！", jpae.getException());
		}
	}

	/**
	 * 根据condition统计商品条数
	 */
	@Override
	public long countConditon(Map<String, Object> map) throws DaoException {
		try{
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			return count(condition, params);
		}catch(JPAException jpae){
			throw new DaoException("InitializeProductDAOImpl.countByOrganizationAndLocal()"+jpae.getMessage(),jpae);
		}
	}

	/**
	 * 根据condition获取商品列表
	 */
	@Override
	public List<InitializeProduct> getByConditon(Map<String, Object> map, int page, int pageSize)
			throws DaoException {
		try{
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			return getListByPage(page, pageSize, condition, params);
		}catch(JPAException jpae){
			throw new DaoException("InitializeProductDAOImpl.getProductByConditon() " + jpae.getMessage(), jpae);
		}
	}

	/**
	 * 删除/恢复 产品
	 * @param proId 产品id
	 * @param organization 组织机构id
	 * @param isDel<br>
	 * true: 已删除<br>
	 * false:未删除<br>
	 * @author ZhangHui 2015/4/14
	 */
	@Override
	public void updateDelFlag(Long proId, Long organization, boolean isDel) throws DaoException {
		try {
			String sql = "UPDATE t_meta_initialize_product SET del = ?1 WHERE product_id = ?2 AND organization = ?3";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, isDel);
			query.setParameter(2, proId);
			query.setParameter(3, organization);
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException("InitializeProductDAOImpl.updateDelFlag() 出现异常！" , e);
		}
	}
}
