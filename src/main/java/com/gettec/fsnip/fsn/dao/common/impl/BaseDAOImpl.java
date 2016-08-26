package com.gettec.fsnip.fsn.dao.common.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.vo.page.FilterVO;
import com.gettec.fsnip.fsn.vo.page.PageVO;
import com.gettec.fsnip.fsn.vo.page.SortVO;
import com.lhfs.fsn.util.StringUtil;


/**
 * 
 * @author Sam Zhou <br/>
 *         Date: 02/28/2013
 * @param <E>
 */
public class BaseDAOImpl<E> implements BaseDAO<E> {

	@PersistenceContext
	protected EntityManager entityManager;
	protected Class<E> entityClass;
	
	public enum Operator {
		EQ ("eq"), //等于
		NEQ ("neq"),  //不等
		GTE ("gte"),  //大于或等于
		GT ("gt") ,  //大于
		LTE("lte") ,  //小于或等于
		LT("lt") ,  //小于
		CONTAINS("contains"),   //包含
		DOESNOTCONTAIN("doesnotcontain") , //不包含
		STARTSWITH("startswith") , //以什么开头
		ENDSWITH ("endswith"),  //以什么结尾
		AND("and") ,
		OR("or") ,
		IDIN("idIn");   //包含ID
		private final String name;
		private Operator (String s) {
	        name = s;
	    }
		public boolean equals(String otherName){
			return (otherName == null)? false:name.equals(otherName);
		};
	}

	
	@SuppressWarnings("unchecked")
	public BaseDAOImpl() {
		try {
			ParameterizedType type = (ParameterizedType) getClass()
					.getGenericSuperclass();
			this.entityClass = (Class<E>) type.getActualTypeArguments()[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增保存
	 */
	public void persistent(E entity) throws JPAException {
		try {
			entityManager.persist(entity);
		} catch (Exception e) {
			e.printStackTrace();
//			throw new JPAException("AbstractModelDAOImpl.persistent() 出现异常！", e);
		}
	}

	/**
	 * 删除
	 */
	public void remove(E entity) throws JPAException {
		try {
			entityManager.remove(entity);
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.remove() 出现异常！", e);
		}
	}

	/**
	 * 更新
	 */
	public E merge(E entity) throws JPAException {
		try {
			entityManager.merge(entity);
			return entity;
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.merge() 出现异常！", e);
		}
	}

	/**
	 * 刷新
	 */
	public void refresh(E entity) throws JPAException {
		try {
			entityManager.refresh(entity);
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.refresh() 出现异常！", e);
		}
	}

	/**
	 * 根据主键获取实体
	 * @throws ServiceException 
	 */
	public E findById(Object id) throws JPAException {
		try {
			return entityManager.find(entityClass, id);
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.findById() 出现异常！", e);
		}
	}

	/**
	 * 获取所有对象集合
	 * 
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<E> findAll() throws JPAException {
		try {
			String jpql = "SELECT e FROM " + entityClass.getName() + " e";
			Query query = entityManager.createQuery(jpql);
			return query.getResultList();
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.findAll() 出现异常！", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<E> getListByCondition(String condition, Object[] params) throws JPAException {
		try {
			String jpql = "SELECT e FROM " + entityClass.getName() + " e";
			if (condition != null) {
				jpql = jpql + " " + condition;
			}
			Query query = entityManager.createQuery(jpql);
			// 增加参数
			if (params != null && params.length != 0) {
				Object[] val = params;
				for (int i = 0; i < val.length; i++) {
					query.setParameter(i + 1, val[i]);
				}
			}
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JPAException("AbstractModelDAOImpl.getListByCondition() 出现异常！", e);
		}
	}
	
	/**
	 * 
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @return
	 */
	public List<E> getListByPage(int page, int pageSize, String condition)
			throws JPAException {
		return getListByPage(page, pageSize, condition, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<E> getListByPage(int page, int pageSize, String condition,
			Object[] params) throws JPAException {
		try {

			String jpql = "SELECT e FROM " + entityClass.getName() + " e ";
			if (condition != null) {
				jpql = jpql + " " + condition;
			}
			Query query = entityManager.createQuery(jpql);
			// 增加参数
			if (params != null && params.length != 0) {
				Object[] val = params;
				for (int i = 0; i < val.length; i++) {
					if(val[i] == null){
						break;
					}
					query.setParameter(i + 1, val[i]);
				}
			}
			// 判断是否分页查询
			if (page > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.getListByPage() 出现异常！", e);
		}
	}

	/**
	 * 根据jpql获取指定类型的实体集合
	 * 
	 * @param returnType
	 *            指定的类型
	 * @param jpql
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <U> List<U> getListByJPQL(Class<U> returnType, String jpql,
			Map<String, Object> params) throws JPAException {
		try {
			Query query = entityManager.createQuery(jpql);
			// 参数
			if (params != null && params.size() != 0) {
				for (String name : params.keySet()) {
					query.setParameter(name, params.get(name));
				}
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.getListByJPQL() 出现异常！", e);
		}
	}

	@SuppressWarnings("unchecked")
	public <U> List<U> getListBySQL(Class<U> returnType, String sql,
			Object[] params) throws JPAException {
		try {
			Query query = entityManager.createNativeQuery(sql,
					returnType);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i + 1, params[i]);
				}
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.getListBySQL() 出现异常！", e);
		}
	}
	/**
	 * 根据sql语句获取给定的类型集合
	 * @param returnType
	 * @param sql
	 * @param params
	 * @return
	 * @throws JPAException
	 */
	@SuppressWarnings("unchecked")
	public <U> List<U> getListBySQLWithoutType(Class<U> returnType, String sql,
			Object[] params) throws JPAException {
		try {
			Query query = entityManager.createNativeQuery(sql);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i + 1, params[i]);
				}
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.getListBySQLWithoutType() 出现异常！", e);
		}
	}
	
	/**
	 * 根据sql语句分页获取给定的类型集合
	 * @param returnType
	 * @param sql
	 * @param params
	 * @return
	 * @throws JPAException
	 * @author tangxin 2015-05-07
	 */
	@SuppressWarnings("unchecked")
	public <U> List<U> getListBySQLWithPage(String sql, Object[] params, int page,
			int pageSize) throws JPAException {
		try {
			Query query = entityManager.createNativeQuery(sql);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i + 1, params[i]);
				}
			}
			if (page > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new JPAException(e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <U> List<U> getListByJPQL(Class<U> returnType, String jpql,
			Object[] params) throws JPAException {
		try {
			Query query = entityManager.createQuery(jpql);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i + 1, params[i]);
				}
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.getListByJPQL() 出现异常！", e);
		}
	}

	@SuppressWarnings("unchecked")
	public <U> List<U> getListByJPQL(Class<U> returnType, String jpql,
			int page, int pageSize, Map<String, Object> params) throws JPAException {
		try {
			Query query = entityManager.createQuery(jpql);
			if (params != null && params.size() != 0) {
				for (String name : params.keySet()) {
					query.setParameter(name, params.get(name));
				}
			}
			if (page > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.getListByJPQL() 出现异常！", e);
		}
	}

	@SuppressWarnings("unchecked")
	public <U> List<U> getListByJPQL(Class<U> returnType, String jpql,
			int page, int pageSize, Object[] params) throws JPAException {
		try {
			Query query = entityManager.createQuery(jpql);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i + 1, params[i]);
				}
			}
			if (page > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.getListByJPQL() 出现异常！", e);
		}
	}
	
	public long count(String condition) throws JPAException {
		try {
			return count(entityClass.getName(), condition, null);
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.count() 出现异常！", e);
		}
	}

	public long count(String condition, Object[] params) throws JPAException {
		try {
			return count(entityClass.getName(), condition, params);
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.count() 出现异常！", e);
		}
	}
	
	public long count(String className, String condition, Object[] params)
			throws JPAException {
		try {
			return count("*", className, condition, params);
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.count() 出现异常！", e);
		}
	}

	public long count(String countTarget, String className, String condition,
			Object[] params) throws JPAException {
		try {
			String jpql = "SELECT count(" + countTarget + ") FROM " + className;
			if (condition != null) {
				jpql = jpql + " e " + condition;
			}
			Query query = entityManager.createQuery(jpql);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					if(params[i] == null){break;}
					query.setParameter(i + 1, params[i]);
				}
			}

			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.count() 出现异常！", e);
		}
	}

	public List<E> getListByPageQuery(String queryString, int page, int pageSize)
			throws JPAException {
		return getListByPageQuery(queryString, page, pageSize, null);
	}

	@SuppressWarnings("unchecked")
	public List<E> getListByPageQuery(String queryString, int page,
			int pageSize, Object[] parm) throws JPAException {
		try {
			Query query = entityManager.createQuery(queryString);

			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);

			if (parm != null && parm.length != 0) {
				Object[] val = parm;
				for (int i = 0; i < val.length; i++) {
					query.setParameter(i + 1, val[i]);
				}
			}
			return query.getResultList();
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.getListByPageQuery() 出现异常！", e);
		}
	}

	@SuppressWarnings("unchecked")
	public <U> List<U> getListBySQL(String target, String sql,
			String indexRefs, Class<U> returnType, int start, int pageSize)
			throws JPAException {
		try {
			Query query = entityManager.createNativeQuery(
					"{Call search('" + target + "','" + sql + "','" + indexRefs
							+ "'," + start + "," + pageSize + ")}", returnType);
			return query.getResultList();
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.getListBySQL() 出现异常！", e);
		}
	}

	@SuppressWarnings("unchecked")
	public <U> U getEntityByJPQL(Class<U> returnType, String jpql,
			Object[] params) throws JPAException {
		try {
			Query query = entityManager.createQuery(jpql, returnType);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i + 1, params[i]);
				}
			}
			if (query.getResultList().size() > 0) {
				return (U) query.getResultList().get(0);
			}
			return null;
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.getEntityByJPQL() 出现异常！", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<E> getfilter(String filter,
			String name,String fieldName) {
		String sql = "SELECT e FROM " + entityClass.getName() + " e";
		
		Query query = null;
		if(filter.equals("eq")){
			sql = sql + " WHERE "+fieldName+" = :name ";
			query = entityManager.createQuery(sql);
			query.setParameter("name", name);
		}else if(filter.equals("neq")){
			sql = sql + " WHERE "+fieldName+" <> :name ";
			query = entityManager.createQuery(sql);
			query.setParameter("name", name);
		}else if(filter.equals("startswith")){
			sql = sql + " WHERE "+fieldName+" like :name";
			query = entityManager.createQuery(sql);
			query.setParameter("name", name + "%");
		}else if(filter.equals("endswith")){
			sql = sql + " WHERE "+fieldName+" like :name";
			query = entityManager.createQuery(sql);
			query.setParameter("name", "%" + name);
		}else if(filter.equals("contains")){
			sql = sql + " WHERE "+fieldName+" like :name";
			query = entityManager.createQuery(sql);
			query.setParameter("name", "%" + name +"%");
		}else if(filter.equals("doesnotcontain")){
			sql = sql + " WHERE "+fieldName+" not like :name";
			query = entityManager.createQuery(sql);
			query.setParameter("name", "%" + name +"%");
		}
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<E> getPaging(int page, int size, String condition) throws JPAException {
		List<E> listOfEntity = null;
		try {
			StringBuffer buffer = new StringBuffer("SELECT e FROM " + entityClass.getName() + " e ");
			if(condition != null && condition.trim() != ""){
				buffer.append(condition);
			}
			Query query = entityManager.createQuery(buffer.toString());
			query.setFirstResult((page - 1)*size);
			query.setMaxResults(size);
			
			listOfEntity = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listOfEntity;
	}
	
	@SuppressWarnings("unchecked")
	public List<E> findAll(Long organization, String configure) throws JPAException {
		String jpql = "SELECT e FROM " + entityClass.getName() + " e";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
		return query.getResultList();
	}
	
	/**
	 * 通过sql语句统计元素数量
	 */
	@Override
	public long countBySql(String className, String condition, Object[] params)
			throws JPAException {
		try {
			String sql = "SELECT count(*) FROM " + className;
			if (condition != null) {
				sql = sql + condition;
			}
			Query query = entityManager.createNativeQuery(sql);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					if(params[i] == null){break;}
					query.setParameter(i + 1, params[i]);
				}
			}

			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.countBySql() 出现异常！", e);
		}
	}
	
	/**
	 * 通过sql语句统计元素数量
	 */
	@Override
	public long countBySql(String sql, Object[] params)
			throws JPAException {
		try {
			Query query = entityManager.createNativeQuery(sql);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					if(params[i] == null){break;}
					query.setParameter(i + 1, params[i]);
				}
			}

			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.countBySql() 出现异常！", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<E> getPaging(int page, int size, String condition, Long organization, List<Long> ids) throws JPAException{
		List<E> listOfEntity = null;
		try {
			StringBuffer buffer = new StringBuffer("SELECT e FROM " + entityClass.getName() + " e ");
			if(condition != null && condition.trim() != ""){
				buffer.append(condition);
			}
			Query query = entityManager.createQuery(buffer.toString());
			query.setFirstResult((page - 1)*size);
			query.setMaxResults(size);
			query.setParameter("organization", organization);
			query.setParameter("ids", ids);
			
			listOfEntity = query.getResultList();
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.getPaging() 出现异常！", e);
		}
		return listOfEntity;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<E> getPaging(int page, int size, String condition, List<Long> ids) throws JPAException {
		List<E> listOfEntity = null;
		try {
			StringBuffer buffer = new StringBuffer("SELECT e FROM " + entityClass.getName() + " e ");
			if(condition != null && condition.trim() != ""){
				buffer.append(condition);
			}
			Query query = entityManager.createQuery(buffer.toString());
			query.setFirstResult((page - 1)*size);
			query.setMaxResults(size);
			query.setParameter("ids", ids);
			
			listOfEntity = query.getResultList();
		} catch (Exception e) {
			throw new JPAException("AbstractModelDAOImpl.getPaging() 出现异常！", e);
		}
		return listOfEntity;
	}

	@Override
	public Map<String, Object> findQueryPage(PageVO pageVo, Criteria criteria){
		 Map<String,Object> map = new HashMap<String, Object>();
		 if(pageVo.getFilter()!=null&&!"".equals(pageVo.getFilter())){
			 criteria = this.criteria(pageVo.getFilter().getFilters(),criteria,pageVo.getFilter().getLogic(),pageVo);
		 }

		 Integer totalCount = Integer.valueOf(criteria.setProjection(
				 Projections.rowCount()).uniqueResult().toString());

		 criteria.setProjection(null);

		 criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);// ROOT_ENTITY
		 
		 //pageSize等于-1查全部
		 if(pageVo.getPageSize()!=-1){
			 criteria.setFirstResult((pageVo.getPage()-1)*pageVo.getPageSize());
			 criteria.setMaxResults(pageVo.getPageSize());
		 }
	

		 if(pageVo.getSort()!=null){
			 SortVO stors = pageVo.getSort();
			 //过滤是还是否组拼过级联查询对象
			 if(pageVo.isProperty()){
				 String field = stors.getField();
				 if(field.contains(".")){
					 String property = field.substring(0, field.indexOf("."));
					 criteria.createAlias(property, property);
				 }
			 }
			 criteria = "asc".equals(stors.getDir())?criteria.addOrder(Order.asc(stors.getField())):criteria.addOrder(Order.desc(stors.getField()));
		 }

		 map.put("total", totalCount);
		 map.put("list", criteria.list());
		 return map;
	}

	@Override
	public Map<String, Object> findQueryPage(PageVO pageVo,
			DetachedCriteria detachedCriteria) {
		Session session = entityManager.unwrap(org.hibernate.Session.class);
		Criteria criteria = detachedCriteria.getExecutableCriteria(session);
		 Map<String,Object> map = new HashMap<String, Object>();
		 if(pageVo.getFilter()!=null&&!"".equals(pageVo.getFilter())){
			 criteria = this.criteria(pageVo.getFilter().getFilters(),criteria,pageVo.getFilter().getLogic(),pageVo);
		 }

		 Integer totalCount = Integer.valueOf(criteria.setProjection(
				 Projections.rowCount()).uniqueResult().toString());

		 criteria.setProjection(null);

		 criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);// ROOT_ENTITY
		 //pageSize等于-1查全部
		 if(pageVo.getPageSize()!=-1){
			 criteria.setFirstResult((pageVo.getPage()-1)*pageVo.getPageSize());
			 criteria.setMaxResults(pageVo.getPageSize()); 
		 }
		

		 if(pageVo.getSort()!=null){
			 SortVO stors = pageVo.getSort();
			 //过滤是还是否组拼过级联查询对象
			 if(pageVo.isProperty()){
				 String field = stors.getField();
				 if(field.contains(".")){
					 String property = field.substring(0, field.indexOf("."));
					 criteria.createAlias(property, property);
				 }
			 }
			 criteria = "asc".equals(stors.getDir())?criteria.addOrder(Order.asc(stors.getField())):criteria.addOrder(Order.desc(stors.getField()));
		 }

		 map.put("total", totalCount);
		 map.put("list", criteria.list());
		 return map;
	}
	
	/**
	  * 前台封装的值转换为对应属性的类型
	  * objectToPrimitive
	  * @param @param filter
	  * @return void    返回类型
	  * @throws
	 */
	
	@SuppressWarnings("rawtypes")
	public void objectToPrimitive(FilterVO filter){
		 String fieldStr = filter.getField();
		 Class cls = getField(entityClass,fieldStr,null);
		 if(cls.equals(String.class)){
			 filter.setValue(String.valueOf(filter.getValue()));
		 }
		 if(cls.equals(Integer.class)){
			 filter.setValue(Integer.valueOf(filter.getValue().toString()));
		 }
		 if(cls.equals(Long.class)){
			 filter.setValue(Long.valueOf(filter.getValue().toString()));
		 }
		
	}
	
	/**
	  * 取本类属性和级联类属性的类型
	  * getField
	  * @param @param cls 对应的Class
	  * @param @param str Class对应的属性
	  * @param @return
	  * @return Class    返回类型
	  * @throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class getField(Class cls,String str,Field fields_1){
		if(str.contains(".")){
			 try {
				 String property = str.substring(0, str.indexOf("."));
				 Field fields = cls.getDeclaredField(property);
				 cls = fields.getType();
				 cls = getField(cls,str.substring(str.indexOf(".")+1,str.length()),fields);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}else{
			try {
				//如果查询对象是集合则取出集合泛型的类型
				if(cls.isAssignableFrom(Set.class)||cls.isAssignableFrom(List.class)||cls.isAssignableFrom(Map.class)){
					 Type fc = fields_1.getGenericType(); //得到其Generic的类型
					 if(fc instanceof ParameterizedType){
						 ParameterizedType pt = (ParameterizedType) fc;
						 cls = (Class)pt.getActualTypeArguments()[0];  //得到泛型里的class类型对象。
					 }
				}
				return cls.getDeclaredField(str).getType();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				//没有找到属性就到父类去找
				try {
					cls = cls.getSuperclass();
					return  cls.getDeclaredField(str).getType();
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchFieldException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//e.printStackTrace();
			}
		}
		return cls;
	}
	
	//递归所有过滤条件
	public Criteria criteria(List<FilterVO> list,Criteria criteria,String logic,PageVO pageVo){
			 List<Criterion> criterionList = new ArrayList<Criterion>();
			 for(FilterVO filter:list){
				 if(filter.getLogic()!=null){
					 criteria = criteria(filter.getFilters(),criteria,filter.getLogic(),pageVo);
				 }
				 //组拼查询级联对象
				 if(!StringUtil.isBlank(filter.getField())){
					 String field = filter.getField();
					 if(field.contains(".")){
						 String property = field.substring(0, field.indexOf("."));
						 criteria.createAlias(property, property);
						 //是否是排序字段
						 if(pageVo.getSort()!=null&&field.equals(pageVo.getSort().getField())){
							 pageVo.setProperty(false);
						 }
					 }
				
				 }
				 
				 //转换前台封装的值转换为对应属性的类型
				 objectToPrimitive(filter);
				 
				 //等于
				 if(Operator.EQ.equals(filter.getOperator())){
					 criterionList.add(Restrictions.eq(filter.getField(),filter.getValue()));
				 }
				 //不等
				 if(Operator.NEQ.equals(filter.getOperator())){
					 criterionList.add(Restrictions.not(Restrictions.eq(filter.getField(),filter.getValue())));
				 }
				 //大于或等于
				 if(Operator.GTE.equals(filter.getOperator())){
					 criterionList.add(Restrictions.ge(filter.getField(),filter.getValue()));
				 }
				 //大于
				 if(Operator.GT.equals(filter.getOperator())){
					 criterionList.add(Restrictions.gt(filter.getField(),filter.getValue()));
				 }
				 //小于或等于
				 if(Operator.LTE.equals(filter.getOperator())){
					 criterionList.add(Restrictions.le(filter.getField(),filter.getValue()));
				 }
				 //小于
				 if(Operator.LT.equals(filter.getOperator())){
					 criterionList.add(Restrictions.lt(filter.getField(),filter.getValue()));
				 }
				 //包含
				 if(Operator.CONTAINS.equals(filter.getOperator())){
					 criterionList.add(Restrictions.like(filter.getField(),"%"+filter.getValue()+"%"));
				 }
				 //不包含
				 if(Operator.DOESNOTCONTAIN.equals(filter.getOperator())){
					 criterionList.add(Restrictions.not(Restrictions.like(filter.getField(),"%"+filter.getValue()+"%")));
				 }
				 //以什么开头
				 if(Operator.STARTSWITH.equals(filter.getOperator())){
					 criterionList.add(Restrictions.like(filter.getField(),filter.getValue()+"%"));
				 }
				 //以什么结尾
				 if(Operator.ENDSWITH.equals(filter.getOperator())){
					 criterionList.add(Restrictions.like(filter.getField(),"%"+filter.getValue()));
				 }
				 
				 //包含ID
				 if(Operator.IDIN.equals(filter.getOperator())){
					 String str = (String) filter.getValue();
					 String[] ids = str.split(",");
					 criterionList.add(Restrictions.in(filter.getField(), ids));
				 }

			 }

			 if(Operator.AND.equals(logic)){
				 for(Criterion c:criterionList){
					 criteria.add(c);
				 }
			 }else{
				 Criterion b = criterionList.get(0);
				 for(Criterion c:criterionList){
					 b = Restrictions.or(b,c);
				 }
				 criteria.add(b);
			 }
			 return criteria;
		 }

	@Override
	public Session getSession() {
		Session session = entityManager.unwrap(org.hibernate.Session.class);
		return session;
	}
}