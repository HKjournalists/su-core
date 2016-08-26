package com.lhfs.fsn.dao.base.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.gettec.fsnip.fsn.exception.JPAException;
import com.lhfs.fsn.dao.base.BaseDao;

public class BaseDaoImpl<E> implements BaseDao<E> {
	
	// 持久化上下文
	
	@PersistenceContext
	protected EntityManager entityManager;
	protected Class<E> entityClass;
	
	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		try {
			ParameterizedType type = (ParameterizedType) getClass()
					.getGenericSuperclass();
			this.entityClass = (Class<E>) type.getActualTypeArguments()[0];
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 新增保存
	 */
	public void persistent(E entity) throws JPAException {
		try {
			entityManager.persist(entity);
		} catch (Exception e) {
			throw new JPAException("", e);
		}
	}

	/**
	 * 删除
	 */
	public void remove(E entity) throws JPAException {
		try {
			entityManager.remove(entity);
		} catch (Exception e) {
			throw new JPAException("", e);
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
			throw new JPAException("", e);
		}
	}

	/**
	 * 刷新
	 */
	public void refresh(E entity) throws JPAException {
		try {
			entityManager.refresh(entity);
		} catch (Exception e) {
			throw new JPAException("", e);
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
			throw new JPAException("", e);
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
			throw new JPAException("", e);
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
			throw new JPAException("", e);
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
			throw new JPAException("", e);
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
			throw new JPAException("", e);
		}
	}

	@SuppressWarnings("unchecked")
	public <U> List<U> getListBySQL(Class<U> returnType, String sql,
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
			throw new JPAException("", e);
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
			throw new JPAException("", e);
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
			throw new JPAException("", e);
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
			throw new JPAException("", e);
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
			throw new JPAException("", e);
		}
	}
	
	public long count(String condition) throws JPAException {
		try {
			return count(entityClass.getName(), condition, null);
		} catch (Exception e) {
			throw new JPAException("", e);
		}
	}

	public long count(String condition, Object[] params) throws JPAException {
		try {
			return count(entityClass.getName(), condition, params);
		} catch (Exception e) {
			throw new JPAException("", e);
		}
	}
	
	public long count(String className, String condition, Object[] params)
			throws JPAException {
		try {
			return count("*", className, condition, params);
		} catch (Exception e) {
			throw new JPAException("", e);
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
			throw new JPAException("", e);
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
			throw new JPAException("", e);
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
			throw new JPAException("", e);
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
			throw new JPAException("", e);
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
}