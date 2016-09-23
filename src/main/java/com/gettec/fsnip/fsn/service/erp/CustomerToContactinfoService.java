package com.gettec.fsnip.fsn.service.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.erp.CustomerToContactinfoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.CustomerToContactinfo;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface CustomerToContactinfoService extends BaseService<CustomerToContactinfo, CustomerToContactinfoDAO>{

	/**
	 * 批量增加联系人关系
	 * @param infos
	 * @return
	 */
	List<CustomerToContactinfo> addRelationShips(List<CustomerToContactinfo> infos);
	
	void save(List<CustomerToContactinfo> adds) throws ServiceException;

	List<CustomerToContactinfo> getListByIdAndType(Long id, int type) throws ServiceException;

	void deleteByCIdAndOrgId(Long cId, Long organization)throws ServiceException;
}
