package com.gettec.fsnip.fsn.service.common.impl;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public abstract class BaseServiceImpl<E extends Model, DAO extends BaseDAO<E>> implements BaseService<E, DAO> {

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public E findById(Object id) throws ServiceException {
		try {
			return getDAO().findById(id);
		} catch (JPAException jpae) {
			throw new ServiceException("", jpae.getException());
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<E> findAll() throws ServiceException {
		try {
			return getDAO().findAll();
		} catch (JPAException jpae) {
			throw new ServiceException("", jpae.getException());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public E create(E e) throws ServiceException {
		try {
			getDAO().persistent(e);
			return e;
		} catch (JPAException jpae) {
			throw new ServiceException("", jpae.getException());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public E update(E e) throws ServiceException {
		try {
			return getDAO().merge(e);
		} catch (JPAException jpae) {
			throw new ServiceException("", jpae.getException());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(E e) throws ServiceException {
		try {
			getDAO().remove(e);
		} catch (JPAException jpae) {
			throw new ServiceException("", jpae.getException());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Long id) throws ServiceException {
		try {
			getDAO().remove(getDAO().findById(id));
		} catch (JPAException jpae) {
			throw new ServiceException("", jpae.getException());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete()  {
		getDAO().delete();
	}

	@SuppressWarnings("unused")
	public PagingSimpleModelVO<E> getPaging(int page, int size, String keywords, Long organization) throws ServiceException {
		PagingSimpleModelVO<E> result = new PagingSimpleModelVO<E>();
		String condition = " where e.organization = " + organization;
		String configure = null;
		if(keywords != null && keywords.trim()!="") {
			condition = condition + " and e.ot_type like '%" + keywords + "%' order by id ASC";
			configure = " and name like '%" + keywords + "%'";
		}else{
			condition = condition + " order by id ASC";
		}
		Long count = 0l;
		try {
			count = getDAO().count(condition);
			result.setCount(count);
			result.setListOfModel(getDAO().getPaging(page, size, condition));
		} catch (JPAException jpae) {
			throw new ServiceException("", jpae.getException());
		}
		return result;
	}

	public PagingSimpleModelVO<E> getAll(Long organization) throws ServiceException {
		PagingSimpleModelVO<E> result = new PagingSimpleModelVO<E>();
		String configure = " WHERE e.organization = " + organization;
		try {
			result.setListOfModel(getDAO().findAll(organization, configure));
		} catch (JPAException jpae) {
			throw new ServiceException("", jpae.getException());
		}
		return result;
	}

	public long countUniqueName(String condition) throws ServiceException {
		try {
			return getDAO().count(condition);
		} catch (JPAException jpae) {
			throw new ServiceException("", jpae.getException());
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public long count(){
		return getDAO().count();
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BaseDAO<E> field(String field){
		return this.getDAO().field(field);
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BaseDAO<E> where(String where){
		return getDAO().where(where);
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BaseDAO<E> where(String where,Object []params){
		return getDAO().where(where, params);
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BaseDAO<E> order(String order){
		return getDAO().order(order);
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BaseDAO<E> limit(Integer pageSize){
		return getDAO().limit(pageSize);
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BaseDAO<E> limit(Integer page,Integer pageSize){
		return getDAO().limit(page, pageSize);
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BaseDAO<E> group(String group){
		return getDAO().group(group);
	}	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public E find(){
		return getDAO().find();
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<E> select(){
		return getDAO().select();
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public long getMaxId() {
		return getDAO().getMaxId();
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<String> selectOneList(String field){
		return getDAO().selectOneList(field);
	}
	public abstract DAO getDAO();
}
