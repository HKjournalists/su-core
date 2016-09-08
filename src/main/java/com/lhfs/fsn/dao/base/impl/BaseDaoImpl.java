package com.lhfs.fsn.dao.base.impl;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.gettec.fsnip.fsn.exception.JPAException;
import com.lhfs.fsn.dao.base.BaseDao;

import net.sf.json.JSONArray;

public class BaseDaoImpl<E> implements BaseDao<E> {
	
	// 持久化上下文
	
	@PersistenceContext
	protected EntityManager entityManager;
	protected Class<E> entityClass;
	protected String aliseTableName="e";
	protected StringBuilder where=new StringBuilder();
	protected StringBuilder field=new StringBuilder(aliseTableName);
	protected StringBuilder order=new StringBuilder();
	protected StringBuilder group=new StringBuilder();
	protected Integer page;
	protected Integer pageSize;
	protected StringBuilder sql=new StringBuilder();
	protected StringBuilder tableName=new StringBuilder(aliseTableName);
	protected Object[] params;
	private boolean useParams=false;
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
	
	
	private void initVar(){
		where.setLength(0);
		field.setLength(0);
		field.append(aliseTableName);
		order.setLength(0);
		group.setLength(0);
		page=null;
		pageSize=null;
		sql.setLength(0);
		tableName.setLength(0);
		tableName.append(aliseTableName);
		useParams=false;
	}

	public BaseDao<E> field(String field){
		this.field.setLength(0);
		this.field.append(field);
		return this;
	}
	
	public BaseDao<E> field(String field,boolean returnOneList){
		this.field.setLength(0);
		this.field.append(field);
		return this;
	}

	public BaseDao<E> where(String where){
		this.where.append(where);
		return this;
	}

	public BaseDao<E> where(String where,Object []params){
		this.params=params;
		this.where.append(where);
		useParams=true;
		return this;
	}
	public BaseDao<E> order(String order){
		this.order.append(order);
		return this;
	}
	public BaseDao<E> limit(Integer pageSize){
		this.page=1;
		this.pageSize=pageSize;
		return this;
	}
	public BaseDao<E> limit(Integer page,Integer pageSize){
		this.page=page;
		this.pageSize=pageSize;
		return this;
	}
	public BaseDao<E> group(String group){
		this.group.append(group);
		return this;
	}
	public long count(){
		StringBuilder jpql=new StringBuilder();
		jpql.append("select count(*) from "+entityClass.getName()+" "+this.tableName);
		if(this.where.length()!=0){
			jpql.append(" where "+this.where);
		}
		Object rtn=null;
		try{
			Query query = entityManager.createQuery(jpql.toString());
			if(this.useParams){
				setQueryParameters(query, this.params);
			}
			rtn = query.getSingleResult();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			initVar();
		}
		return rtn == null ? 0 : Long.parseLong(rtn.toString());
	}
	@SuppressWarnings("unchecked")
	public E find(){
		StringBuilder jpql=new StringBuilder();
		jpql.append("select "+this.field+" from "+entityClass.getName()+" "+this.tableName);
		if(this.where.length()!=0){
			jpql.append(" where "+this.where);
		}
		if(this.group.length()!=0){
			jpql.append(" group by "+this.group);
		}
		if(this.order.length()!=0){
			jpql.append(" order by "+this.order);
		}
		try{
			Query query = entityManager.createQuery(jpql.toString());
			if(this.useParams){
				setQueryParameters(query, this.params);
			}
			query.setMaxResults(1);
			List<E> list=query.getResultList();
			initVar();
			if(list.size()>0){
				return list.get(0);
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			initVar();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<E> select(){
		StringBuilder jpql=new StringBuilder();
		jpql.append("select "+this.field+" from "+entityClass.getName()+" "+this.tableName);
		if(this.where.length()!=0){
			jpql.append(" where "+this.where);
		}
		if(this.group.length()!=0){
			jpql.append(" group by "+this.group);
		}
		if(this.order.length()!=0){
			jpql.append(" order by "+this.order);
		}

		List<E> list=new ArrayList<E>();
		try{
			Query query = entityManager.createQuery(jpql.toString());
			if(this.useParams){
				setQueryParameters(query, this.params);
			}
			if (this.pageSize!=null) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			list=query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			initVar();
		}
		return list;
	}
	
	public void delete(){
		StringBuilder jpql=new StringBuilder();
		jpql.append("delete from "+entityClass.getName()+" "+this.tableName);
		if(this.where.length()!=0){
			jpql.append(" where "+this.where);
		}
		try{
			Query query = entityManager.createQuery(jpql.toString());
			if(this.useParams){
				setQueryParameters(query, this.params);
			}
			query.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			initVar();
		}
	}
	
	public List<String> selectOneList(String field){
		JSONArray list=JSONArray.fromObject(this.select());
		List<String> strList=new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			strList.add(list.getJSONObject(i).getString(field));
		}
		return strList;
	}

	public E isExist(String condition,Object[] params){
		StringBuffer sql=new StringBuffer("select e from "+entityClass.getName()+" e");
		if(condition!=null&&!condition.equals("")){
			sql.append(" where "+condition);
		}
		Query query = entityManager.createQuery(sql.toString());
		setQueryParameters(query,params);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		List<E> list=query.getResultList();
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	protected void setQueryParameters(Query query, Object[] params) {
		int length = (params != null ? params.length : 0);
		for (int i = 0; i < length; i++) {
			query.setParameter(i + 1, params[i]);
		}
	}
	
	public long getMaxId(){
		StringBuffer sql=new StringBuffer("select max(id) from "+entityClass.getName()+" e");
		Query query = entityManager.createQuery(sql.toString());
		return Long.parseLong(query.getSingleResult()+"") ;
	}
}