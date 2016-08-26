package com.lhfs.fsn.service.base.impl;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.common.Model;
import com.lhfs.fsn.dao.base.BaseDao;
import com.lhfs.fsn.service.base.BaseService;

public abstract class BaseServiceImpl<E extends Model, Dao extends BaseDao<E>> implements BaseService<E, Dao> {

	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public E findById(Object id) throws JPAException {
		return getDao().findById(id);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<E> findAll() throws JPAException {
		return getDao().findAll();
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public E create(E e) throws JPAException {
		getDao().persistent(e);
		return e;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public E update(E e) throws JPAException {
		return getDao().merge(e); // load()
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(E e) throws JPAException {
		getDao().remove(e);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Long id) throws JPAException {
		getDao().remove(getDao().findById(id));
	}

	public abstract Dao getDao();

}
