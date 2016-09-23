package com.gettec.fsnip.fsn.service.common;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface BaseService<E, DAO> {
	
	public E findById(Object id) throws ServiceException;

	public List<E> findAll() throws ServiceException;
	
	public E create(E e) throws ServiceException;
	
	public E update(E e) throws ServiceException;
	
	public void delete(E e) throws ServiceException;
	
	public void delete(Long id) throws ServiceException;
	
	/**
	 * 分页查询
	 * @author Liang Zhou
	 * 2014-10-23
	 * @param page
	 * @param size
	 * @param keywords
	 * @param organization
	 * @return
	 * @throws ServiceException
	 */
	public PagingSimpleModelVO<E> getPaging(int page, int size, String keywords, Long organization) throws ServiceException;
	
	/**
	 * 查询所有记录
	 * @author Liang Zhou
	 * 2014-10-23
	 * @param organization
	 * @return
	 * @throws ServiceException
	 */
	public PagingSimpleModelVO<E> getAll(Long organization) throws ServiceException;
	BaseDAO<E> field(String field);
	BaseDAO<E> where(String where);
	BaseDAO<E> where(String where,Object []params);
	BaseDAO<E> order(String order);
	BaseDAO<E> limit(Integer pageSize);
	BaseDAO<E> limit(Integer page,Integer pageSize);
	BaseDAO<E> group(String group);
	E find();
	List<E> select();
	List<String> selectOneList(String field);
	long getMaxId();
	void delete();
	public DAO getDAO();
}
