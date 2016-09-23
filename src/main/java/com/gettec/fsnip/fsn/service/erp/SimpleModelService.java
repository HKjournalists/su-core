package com.gettec.fsnip.fsn.service.erp;


import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface SimpleModelService<E> {

	E add(String name, Long organization);
	boolean remove(String name, Long organization);
	boolean update(String value1, String value2, Long organization);
	
	public PagingSimpleModelVO<E> getPaging(int page, int size, String keywords, Long organization);

	public PagingSimpleModelVO<E> getAll(Long organization);
	
}
