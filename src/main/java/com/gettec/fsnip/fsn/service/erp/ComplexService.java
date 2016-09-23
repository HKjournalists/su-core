package com.gettec.fsnip.fsn.service.erp;

import java.util.List;

import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface ComplexService<E> {
	E add(E e, Long organization);
	E add(E e, String orgName ,Long organization);
	E update(E e);
	boolean remove(E e);
	List<E> getAll(Long organization);
	boolean remove(E e,String orgName,Long organization);
	public PagingSimpleModelVO<E> getPaging(int page, int size, String keywords, Long organization);
	public PagingSimpleModelVO<E> getPaging(int page, int size, String keywords, String orgName,Long organization);
}
