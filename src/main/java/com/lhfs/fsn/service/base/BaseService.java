package com.lhfs.fsn.service.base;

import java.util.List;

import com.gettec.fsnip.fsn.exception.JPAException;
import com.lhfs.fsn.dao.base.BaseDao;

public interface BaseService<E, Dao> {
	
	public E findById(Object id) throws JPAException;

	public List<E> findAll() throws JPAException;
	
	public E create(E e) throws JPAException;
	
	public E update(E e) throws JPAException;
	
	public void delete(E e) throws JPAException;
	
	public void delete(Long id) throws JPAException;
	
	public Dao getDao();
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
}
