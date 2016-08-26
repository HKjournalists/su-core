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

	public abstract DAO getDAO();
}
