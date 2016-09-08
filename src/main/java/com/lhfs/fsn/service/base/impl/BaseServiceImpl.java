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
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public long count(){
		return getDao().count();
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BaseDao<E> field(String field){
		return this.getDao().field(field);
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BaseDao<E> where(String where){
		return getDao().where(where);
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BaseDao<E> where(String where,Object []params){
		return getDao().where(where, params);
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BaseDao<E> order(String order){
		return getDao().order(order);
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BaseDao<E> limit(Integer pageSize){
		return getDao().limit(pageSize);
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BaseDao<E> limit(Integer page,Integer pageSize){
		return getDao().limit(page, pageSize);
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BaseDao<E> group(String group){
		return getDao().group(group);
	}	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public E find(){
		return getDao().find();
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<E> select(){
		return getDao().select();
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public long getMaxId() {
		return getDao().getMaxId();
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<String> selectOneList(String field){
		return getDao().selectOneList(field);
	}
	public abstract Dao getDao();

}
