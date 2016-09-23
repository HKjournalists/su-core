package com.gettec.fsnip.fsn.service.erp;

import com.gettec.fsnip.fsn.dao.erp.MerchandiseTypeDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseType;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.fsn.vo.erp.SimpleModelVO;

public interface MerchandiseTypeService extends BaseService<MerchandiseType, MerchandiseTypeDAO>{

	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 */
	public PagingSimpleModelVO<MerchandiseType> getMerchandiseTypefilter(int page, int pageSize,String configure, Long organization);
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 */
	public long countMerchandiseTypefilter(String configure);
	
	public boolean judgeIsUsed(Long id,Long organization) throws ServiceException;

	public boolean updateMerchandiseType(SimpleModelVO vo,
			Long currentUserOrganization) throws ServiceException;

	public boolean add(SimpleModelVO vo, Long currentUserOrganization) throws ServiceException;

	void remove(Long id, Long organization) throws ServiceException;
}
