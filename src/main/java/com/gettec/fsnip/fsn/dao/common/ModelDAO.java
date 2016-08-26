package com.gettec.fsnip.fsn.dao.common;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.vo.page.PageVO;
/**
 * 
 * @author Sam Zhou
 *	<br/>Date: 2013/02/28
 * @param <E>
 */
public interface ModelDAO<E> {
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
	
	long countBySql(String className, String condition, Object[] params)
			throws JPAException;

	long countBySql(String sql, Object[] params) throws JPAException;
	
	long count(String condition) throws JPAException;

	long count(String countTarget, String className, String condition,
			Object[] params) throws JPAException;

	<U> List<U> getListBySQL(String target, String sql, String indexRefs,
			Class<U> returnType, int page, int pagesize) throws JPAException;

	<U> List<U> getListBySQLWithoutType(Class<U> returnType, String sql,
			Object[] params) throws JPAException;
	
	public <U> List<U> getListBySQLWithPage(String sql, Object[] params, int page,
			int pageSize) throws JPAException;
	
	/**
	 * 分页查询
	 * @author Liang Zhou
	 * 2014-10-23
	 * @param page
	 * @param size
	 * @param condition
	 * @return
	 * @throws JPAException
	 */
	public List<E> getPaging(int page, int size, String condition) throws JPAException;
	
	/**
	 * 查询所有记录
	 * @author Liang Zhou
	 * 2014-10-23
	 * @param organization
	 * @param configure
	 * @return
	 * @throws JPAException
	 */
	public List<E> findAll(Long organization, String configure) throws JPAException;
	
	/**
	 * 过滤
	 * @author Liang Zhou
	 * 2014-10-23
	 * @param filter
	 * @param name
	 * @param fieldName
	 * @return
	 */
	public List<E> getfilter(String filter,String name,String fieldName);

	public List<E> getPaging(int page, int size, String condition, Long organization, List<Long> ids) throws JPAException;
	List<E> getPaging(int page, int size, String condition, List<Long> ids) throws JPAException;
	
	
	/**
	 * qbc 查询接口
	 * 
	 * <p> @创建人：        武国庆		    </p>
	 * <p> @创建时间：   2015-6-15 下午1:48:09	</p>
	 * <p> @修改时间：   2015-6-15 下午1:48:09	</p>
	 * <p> @个性备注：    无				</p>
	 */
	
	public Map<String, Object> findQueryPage(PageVO pageVo,Criteria criteria);
	
	 /**
	  * 离线查询接口
	  * 
	  */
	public Map<String, Object> findQueryPage(PageVO pageVo,DetachedCriteria detachedCriteria);
	
	public Session getSession();
}
