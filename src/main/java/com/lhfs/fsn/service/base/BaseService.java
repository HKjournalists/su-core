package com.lhfs.fsn.service.base;

import java.util.List;

import com.gettec.fsnip.fsn.exception.JPAException;

public interface BaseService<E, Dao> {
	
	public E findById(Object id) throws JPAException;

	public List<E> findAll() throws JPAException;
	
	public E create(E e) throws JPAException;
	
	public E update(E e) throws JPAException;
	
	public void delete(E e) throws JPAException;
	
	public void delete(Long id) throws JPAException;
	
	public Dao getDao();
}
