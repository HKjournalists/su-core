package com.gettec.fsnip.fsn.dao.erp.impl;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.FromToBussinessDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.buss.FromToBussiness;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.util.FilterUtils;

@Repository("fromToBusDAO")
public class FromToBussinessDAOImpl extends BaseDAOImpl<FromToBussiness> implements FromToBussinessDAO {

	/**
	 * 获取所有的销往企业id
	 * @author Zhanghui 2015/4/8
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> finAllToBusIds(Long proId, Long fromBusId) throws DaoException {
		try {
			String sql = "SELECT to_bus_id FROM t_meta_from_to_business WHERE pro_id = ?1 AND from_bus_id = ?2";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, proId);
			query.setParameter(2, fromBusId);
			List<Object> objs = query.getResultList();
			List<Long> result = new ArrayList<Long>();
			for(Object obj : objs){
				Long toBusId = ((BigInteger)obj).longValue();
				result.add(toBusId);
			}
			return result;
		} catch (Exception e) {
			throw new DaoException("FromToBussinessDAOImpl.finAllToBusIds() 出现异常！" , e);
		}
	}
	
	/**
	 * 获取销往企业id<br>
	 * isDel:true  已删除<br>
	 * isDel:false 未删除
	 * @author Zhanghui 2015/4/8
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> finAllToBusIds(Long proId, Long fromBusId, boolean isDel) throws DaoException {
		try {
			String sql = "SELECT DISTINCT to_bus_id FROM t_meta_from_to_business WHERE pro_id = ?1 AND from_bus_id = ?2 AND del = ?3";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, proId);
			query.setParameter(2, fromBusId);
			query.setParameter(3, isDel);
			List<Object> objs = query.getResultList();
			List<Long> result = new ArrayList<Long>();
			for(Object obj : objs){
				Long toBusId = ((BigInteger)obj).longValue();
				result.add(toBusId);
			}
			return result;
		} catch (Exception e) {
			throw new DaoException("FromToBussinessDAOImpl.finAllToBusIds() 出现异常！" , e);
		}
	}

	/**
	 * 获取一条销往企业信息
	 * @author Zhanghui 2015/4/8
	 */
	@Override
	public FromToBussiness findById(Long proId, Long fromBusId, Long toBusId) throws DaoException {
		try {
			String condition = " WHERE e.proId = ?1 AND e.fromBusId = ?2 AND e.toBusId = ?3";
			List<FromToBussiness> resultList = this.getListByCondition(condition, new Object[]{proId, fromBusId, toBusId});
			if(resultList.size() > 0){
				return resultList.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("FromToBussinessDAOImpl.findById()-->" + jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 获取销往企业名称
	 * isDel:true  已删除<br>
	 * isDel:false 未删除
	 * @author Zhanghui 2015/4/8
	 * @throws DaoException 
	 */
	@Override
	public String findNamestrs(Long proId, Long fromBusId, boolean isDel) throws DaoException {
		try {
			String sql = "SELECT  GROUP_CONCAT(name) FROM business_unit WHERE id in" +
					"(SELECT to_bus_id FROM t_meta_from_to_business WHERE pro_id = ?1 AND from_bus_id = ?2 AND del = ?3)";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, proId);
			query.setParameter(2, fromBusId);
			query.setParameter(3, isDel);
			Object result = query.getSingleResult();
			return result==null?"":result.toString();
		} catch (Exception e) {
			throw new DaoException("FromToBussinessDAOImpl.findNamestrs() 出现异常！" , e);
		}
	}

	/**
	 * 根据供应商和收货商id，获取产品id集合<br>
	 * isDel:true  已删除<br>
	 * isDel:false 未删除
	 * @author Zhanghui 2015/4/9
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> finAllProIdsByPage(Long fromBusId, Long toBusId, boolean isDel, int page, int pageSize,String configure) throws DaoException {
		try {
//			int begin = (page-1)*pageSize;
			String new_configure=" ";
			new_configure = getConfigure(configure,new_configure);
			String sql = "SELECT DISTINCT pro_id FROM t_meta_from_to_business tfb " +
						 "LEFT JOIN product p ON p.id=tfb.pro_id " +
                          "LEFT JOIN (select pi.product_id,max(tr.last_modify_time) last_modify_time " +
                          "from product_instance pi inner join test_result tr on tr.sample_id=pi.id group by pi.product_id) pth " +
                          "on pth.product_id=p.id "+
						 "WHERE tfb.from_bus_id = ?1 AND ";
			if (toBusId != null) {
			        sql+="tfb.to_bus_id = ?2 AND ";
			}
	        sql+=" tfb.del = ?3 " +new_configure+"ORDER BY pro_id ASC ";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, fromBusId);
			if (toBusId != null) {
			    query.setParameter(2, toBusId);
			}
			query.setParameter(3, isDel);
			if (page > 0 && pageSize > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object> objs = query.getResultList();
			List<Long> result = new ArrayList<Long>();
			for(Object obj : objs){
				Long proId = ((BigInteger)obj).longValue();
				result.add(proId);
			}
			return result;
		} catch (Exception e) {
			throw new DaoException("FromToBussinessDAOImpl.finAllProIds() 出现异常！" , e);
		}
	}

	/**
	 * 更新删除标记<br>
	 * isDel:true  已删除<br>
	 * isDel:false 未删除
	 * @author Zhanghui 2015/4/10
	 */
	@Override
	public void updateDelFlag(Long proId, Long fromBusId, Long toBusId, boolean isDel) throws DaoException {
		try {
			if(fromBusId == null){
				return;
			}
			String sql = "UPDATE t_meta_from_to_business SET del = ?1 WHERE from_bus_id = ?2";
			if(proId != null){
				sql += " AND pro_id = " + proId;
			}
			if(toBusId != null){
				sql += " AND to_bus_id = " + toBusId;
			}
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, isDel);
			query.setParameter(2, fromBusId);
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException("FromToBussinessDAOImpl.updateDelFlag() 出现异常！" , e);
		}
	}

	/**
	 * 查找数量<br>
	 * isDel:true  已删除<br>
	 * isDel:false 未删除
	 * @author Zhanghui 2015/4/10
	 */
	@Override
	public long counts(Long fromBusId, Long toBusId, boolean isDel) throws DaoException {
		try {
//			if(toBusId == null){
//				return 0;
//			}
			
			String condition = "";
			if(fromBusId != null){
				condition += "AND tb.from_bus_id = ?4";
			}
			
			String sql = "SELECT count( DISTINCT tb.pro_id ) FROM t_meta_from_to_business tb " +
					     "INNER JOIN t_meta_enterprise_to_provider tp ON tp.provider_id = tb.from_bus_id ";
			if (toBusId != null) {
				sql+="AND tp.business_id = ?1 ";
			}	
			sql+=" WHERE ";
			if (toBusId != null) {
				sql+=" tb.to_bus_id = ?2   AND ";
			}		    
			 sql+=" tb.del = ?3 " + condition;
			Query query = entityManager.createNativeQuery(sql);
			if(toBusId!=null){
			query.setParameter(1, toBusId);
			query.setParameter(2, toBusId);
			}
			query.setParameter(3, isDel);
			if(fromBusId != null){
				query.setParameter(4, fromBusId);
			}
			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (Exception e) {
			throw new DaoException("FromToBussinessDAOImpl.counts() 出现异常！" , e);
		}
	}
	
	/**
	 * 获取某供应商提供给当前登录的商超的所有产品总数<br>
	 * isDel:true  已删除<br>
	 * isDel:false 未删除
	 * @author Zhanghui 2015/6/17
	 */
	@Override
	public long countsOfProduct2Super(Long fromBusId, Long toBusId, boolean isDel,String configure) throws DaoException {
		try {
			if(fromBusId==null){
				throw new Exception("参数为空");
			}
			String new_configure=" ";
			new_configure = getConfigure(configure,new_configure);
			String sql = "SELECT count( DISTINCT tb.pro_id ) FROM t_meta_from_to_business tb " +
            "LEFT JOIN (select pi.product_id,max(tr.last_modify_time) last_modify_time " +
            "from product_instance pi inner join test_result tr on tr.sample_id=pi.id group by pi.product_id) pth " +
            "on pth.product_id=tb.pro_id "+
			 "LEFT JOIN product p ON p.id=tb.pro_id "+
		 	 "WHERE ";
			if (toBusId != null) {
			sql+="tb.to_bus_id = ?1 AND ";
			}
			sql+=" tb.del = ?2 AND tb.from_bus_id = ?3 "+new_configure;
			
			Query query = entityManager.createNativeQuery(sql);
			if (toBusId != null) {
				query.setParameter(1, toBusId);
			}
			query.setParameter(2, isDel);
			query.setParameter(3, fromBusId);
			
			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (Exception e) {
			throw new DaoException("FromToBussinessDAOImpl.countsOfProduct2Super() 出现异常！" , e);
		}
	}

	/**
	 * 获取过滤条件方法
	 * @author longxianzhen 20150815
	 */
	private String getConfigure( String configure,String new_configure) {	
		
		if(configure != null && !"null".equals(configure)){
			String filter[] = configure.split("@@");
			for(int i=0;i<filter.length;i++){
				String filters[] = filter[i].split("@");
				String config = splitSCJointConfigure(filters[0],filters[1],filters[2]);
				if(config==null){
					continue;
				}
				new_configure = new_configure + " AND " + config;
			}
		}
	    return new_configure;
	}
	
	/**
	 * 过滤条件拼接方法
	 * @author longxianzhen 20150815
	 */
	private String splitSCJointConfigure(String field,String mark,String value) {
		try {
			if(field.equals("name")){
				return FilterUtils.getConditionStr(" p.name ",mark,value);
			}
			if(field.equals("barcode")){
				return FilterUtils.getConditionStr(" p.barcode ",mark,value);
			}
			if(field.equals("lastModifyTime")){
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
			    DateFormat DateFormat_US = new SimpleDateFormat(
						"EEE MMM dd yyyy HH:mm:ss", Locale.US);
				return FilterUtils.getConditionStr("pth.last_modify_time ",mark,formatter.format(DateFormat_US.parse(value)));
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取某供应商提供给当前登录的商超的待处理报告的产品数量<br>
	 * isDel:true  已删除<br>
	 * isDel:false 未删除
	 * @author ZhangHui 2015/5/4
	 * 最后修改人：ZhangHui 2015/5/5
	 */
	@Override
	public long countsOfOnHandle(Long fromBusId, Long toBusId, boolean isDel,String configure) throws DaoException {
		try {
			if(fromBusId==null){ return 0;}
			String new_configure=" ";
			new_configure = getConfigure(configure,new_configure);
			String sql = "SELECT COUNT(*) FROM ( " +
							"SELECT DISTINCT tb.pro_id  FROM product_instance  pri " +
							"INNER JOIN test_result tr ON tr.sample_id = pri.id AND tr.publish_flag='4' AND tr.del = 0 " +
							"INNER JOIN business_unit bus ON bus.id = ?1 AND bus.organization = tr.organization " +
									"INNER JOIN t_meta_from_to_business tb ON tb.pro_id = pri.product_id AND ";
					if (toBusId != null) {
					sql+="tb.to_bus_id = ?2 AND "; 
					}
					sql+="tb.from_bus_id =?3  AND tb.del = ?4 " +
							"INNER JOIN product p ON p.id=pri.product_id "+new_configure+" "+
							"INNER JOIN t_meta_enterprise_to_provider tc ON ";
					if (toBusId != null) {
					sql+="tc.`business_id`=?5 AND ";
					}
					sql+="tc.provider_id = tb.from_bus_id AND tc.provider_id =?6 WHERE DATEDIFF(pri.expiration_date , SYSDATE()) > 0) t";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, fromBusId);
			if (toBusId != null) {
			query.setParameter(2, toBusId);
			query.setParameter(5, toBusId);
			}
			query.setParameter(3, fromBusId);
			query.setParameter(4, isDel);
			query.setParameter(6, fromBusId);
			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("FromToBussinessDAOImpl.countsOfOnHandle() 出现异常！" , e);
		}
	}

	/**
	 * 获取某供应商提供给当前登录的商超的待处理报告的产品ids
	 * @author Zhanghui 2015/5/4
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> findAllProIdStrsOfOnHandle(Long fromBusId, Long toBusId,
			boolean isDel, int page, int pageSize,String configure) throws DaoException {
		try {
			List<Long> result = new ArrayList<Long>();
			if(fromBusId==null){ return result;}
			
			
			String new_configure=" ";
			new_configure = getConfigure(configure,new_configure);
			String sql = "SELECT DISTINCT tb.pro_id  FROM product_instance  pri " +
							"INNER JOIN test_result tr ON tr.sample_id = pri.id AND tr.publish_flag='4' AND tr.del = 0 " +
							"INNER JOIN business_unit bus ON bus.id = ?1 AND bus.organization = tr.organization " +
							"INNER JOIN t_meta_from_to_business tb ON tb.pro_id = pri.product_id AND ";
					if (toBusId != null) {        
					sql+="tb.to_bus_id = ?2 AND " ;
					}
					sql+="tb.from_bus_id =?3 AND tb.del = ?4 " +
							"INNER JOIN product p ON p.id=pri.product_id "+new_configure+" "+
							"INNER JOIN t_meta_enterprise_to_provider tc ON ";
					if (toBusId != null) {      
					sql+="tc.`business_id`=?5 AND ";
					}
					sql+="tc.provider_id = tb.from_bus_id AND tc.provider_id =?6  ";
					sql+="WHERE DATEDIFF(pri.expiration_date , SYSDATE()) > 0 ORDER BY tb.pro_id " ;
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, fromBusId);
			if (toBusId != null) {
			query.setParameter(2, toBusId);
			query.setParameter(5, toBusId);
			}
			query.setParameter(3, fromBusId);
			query.setParameter(4, isDel);
			query.setParameter(6, fromBusId);
			if (page > 0 && pageSize > 0) {
			query.setFirstResult((page-1)*pageSize);
			query.setMaxResults(pageSize);
			}
			List<Object> objs = query.getResultList();
			for(Object obj : objs){
				Long proId = ((BigInteger)obj).longValue();
				result.add(proId);
			}
			return result;
		} catch (Exception e) {
			throw new DaoException("FromToBussinessDAOImpl.finAllProIds() 出现异常！" , e);
		}
	}

	/**
	 * 根据产品id和商超ID获取供应商
	 * @author LongXianZhen 2015/5/6
	 */
	@Override
	public FromToBussiness getFromBuByproIdAndToBuId(Long productId, Long toBusId)
			throws DaoException {
		try {
			String condition = " WHERE e.proId = ?1 AND e.del=?2 AND e.toBusId = ?3";
			List<FromToBussiness> resultList = this.getListByCondition(condition, new Object[]{productId, false, toBusId});
			if(resultList.size() > 0){
				return resultList.get(0);
				
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("FromToBussinessDAOImpl.getFromBuByproIdAndToBuId()-->" + jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 功能描述：获取某供应商提供给某商超的产品的记录
	 * @author Zhanghui 2015/6/23
	 * @throws DaoException 
	 */
	@Override
	public long count(Long productId, Long fromBusId, Long toBusId) throws DaoException {
		try {
			if(productId==null || fromBusId==null || toBusId==null){
				return 0;
			}
			
			String condition = " WHERE e.proId = ?1 AND e.fromBusId = ?2 AND e.toBusId = ?3 AND e.del = 0";
			
			return this.count(condition, new Object[]{productId, fromBusId, toBusId});
		} catch (JPAException e) {
			throw new DaoException("[JPAException]FromToBussinessDAOImpl.find()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 功能描述：获取供货给当前商超的供应商数量
	 * @author Zhanghui 2015/7/7
	 * @throws DaoException 
	 */
	@Override
	public long count(Long productId, Long toBusId) throws DaoException {
		try {
			if(productId==null || toBusId==null){
				return 0;
			}
			
			String condition = " WHERE e.proId = ?1 AND e.toBusId = ?3 AND e.del = 0";
			
			return this.count(condition, new Object[]{productId, toBusId});
		} catch (JPAException e) {
			throw new DaoException("[JPAException]FromToBussinessDAOImpl.count()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 功能描述：根据产品id，商超企业id查找供应商企业id
	 * @author Zhanghui 2015/7/1
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long findFromBusId(Long productId, Long toBusId) throws DaoException {
		try {
			if(productId==null ){
				throw new Exception("参数为空");
			}
			
			String sql = "SELECT from_bus_id FROM t_meta_from_to_business WHERE pro_id = ?1 AND ";
			if (toBusId != null) {
				sql += "to_bus_id = ?2 AND ";
			}
			sql+="del = 0";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, productId);
			if (toBusId != null) {
			 query.setParameter(2, toBusId);
			}
			
			List<Object> result = query.getResultList();
			if(result!=null && result.size()>0){
				return Long.parseLong(result.get(0).toString());
			}
			
			return null;
		} catch (JPAException e) {
			throw new DaoException("[JPAException]FromToBussinessDAOImpl.find()-->" + e.getMessage(), e.getException());
		} catch (Exception e) {
			throw new DaoException("[Exception]FromToBussinessDAOImpl.find()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：获取供货给当前商超的供应商名称
	 * @author Zhanghui 2015/7/7
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getListOfFromBusName(Long productId, Long toBusId)
			throws DaoException {
		try {
			if(productId==null || toBusId==null){
				return null;
			}
			
			String sql = "SELECT bus.`name` FROM t_meta_from_to_business f2b " +
						 "INNER JOIN business_unit bus ON bus.id = f2b.from_bus_id " +
						 "WHERE f2b.pro_id = ?1 AND f2b.to_bus_id = ?2 AND f2b.del = 0";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, productId);
			query.setParameter(2, toBusId);
			
			return query.getResultList();
		}catch (Exception e) {
			throw new DaoException("[Exception]FromToBussinessDAOImpl.find()-->" + e.getMessage(), e);
		}
	}

	@Override
	public Product getProbyId(Long fromBusId, Long toBusId, Long proId,
			boolean isDel) throws DaoException {

		try {
			String condition = " AND tb.pro_id=?5";
			if(fromBusId != null){
				condition += "AND tb.from_bus_id = ?4";
			}
			
			String sql = "select * from product e where e.id=(select DISTINCT tb.pro_id  FROM t_meta_from_to_business tb " +
					     "INNER JOIN t_meta_enterprise_to_provider tp ON tp.provider_id = tb.from_bus_id";
			if (toBusId != null) {
				sql+=" AND tp.business_id = ?1 ";
			}	
			sql+=" WHERE ";
			if (toBusId != null) {
				sql+=" tb.to_bus_id = ?2   AND ";
			}		    
			 sql+=" tb.del = ?3 " + condition+")";
			Query query = entityManager.createNativeQuery(sql, Product.class);
			if(toBusId!=null){
			query.setParameter(1, toBusId);
			query.setParameter(2, toBusId);
			}
			query.setParameter(3, isDel);
			query.setParameter(5, proId);
			if(fromBusId != null){
				query.setParameter(4, fromBusId);
			}
			List<Product> productList = query.getResultList();
			if(productList.size()==0){
				return null;
			}else{
				return productList.get(0);
			}
		} catch (Exception e) {
			throw new DaoException("FromToBussinessDAOImpl.getProbyId() 出现异常！" , e);
		}
	
	}
}
