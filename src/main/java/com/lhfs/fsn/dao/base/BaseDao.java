package com.lhfs.fsn.dao.base;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.exception.JPAException;

public interface BaseDao<E> {
	
	void persistent(E entity) throws JPAException;

	void remove(E entity) throws JPAException;

	E merge(E entity) throws JPAException;

	void refresh(E entity) throws JPAException;

	E findById(Object id) throws JPAException;

	List<E> findAll() throws JPAException;

	List<E> getListByCondition(String condition, Object[] params)
			throws JPAException;

	List<E> getListByPage(int page, int pageSize, String condition)
			throws JPAException;

	List<E> getListByPage(int page, int pageSize, String condition,
			Object[] params) throws JPAException;

	List<E> getListByPageQuery(String queryString, int page, int pageSize)
			throws JPAException;

	List<E> getListByPageQuery(String queryString, int page, int pageSize,
			Object[] params) throws JPAException;

	<U> U getEntityByJPQL(Class<U> returnType, String jpql, Object[] params)
			throws JPAException;

	<U> List<U> getListByJPQL(Class<U> returnType, String jpql,
			Map<String, Object> params) throws JPAException;

	<U> List<U> getListByJPQL(Class<U> returnType, String jpql, Object[] params)
			throws JPAException;

	<U> List<U> getListBySQL(Class<U> returnType, String sql, Object[] params)
			throws JPAException;

	long count(String condition, Object[] params) throws JPAException;

	long count(String className, String condition, Object[] params)
			throws JPAException;

	long count(String condition) throws JPAException;

	long count(String countTarget, String className, String condition,
			Object[] params) throws JPAException;

	<U> List<U> getListBySQL(String target, String sql, String indexRefs,
			Class<U> returnType, int page, int pagesize) throws JPAException;

	<U> List<U> getListBySQLWithoutType(Class<U> returnType, String sql,
			Object[] params) throws JPAException;
	/**
	 * 根据条件查询某对象是否存在
	 * @param condition
	 * @param params
	 * @return
	 */
	E isExist(String condition,Object[] params);
	BaseDao<E> field(String field);
	BaseDao<E> where(String where);
	BaseDao<E> where(String where,Object []params);
	BaseDao<E> order(String order);
	BaseDao<E> limit(Integer pageSize);
	BaseDao<E> limit(Integer page,Integer pageSize);
	BaseDao<E> group(String group);
	E find();
	List<E> select();
	List<String> selectOneList(String field);
	
	long getMaxId();
	
	void delete();
	/**
	 * 查询数据库总数（条件查询，第三个参数传null，代表不是条件查询）
	 * @param className
	 * @param condition
	 * @param params
	 * @return
	 */
	public long count();
}
