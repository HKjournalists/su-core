package com.gettec.fsnip.fsn.service.erp.buss;

import java.util.List;
import com.gettec.fsnip.fsn.dao.erp.buss.BussToMerchandisesDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.buss.BussToMerchandises;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface BussToMerchandisesService extends BaseService<BussToMerchandises, BussToMerchandisesDAO>{

	boolean addRelationShips(List<BussToMerchandises> list);
	boolean save(List<BussToMerchandises> merchandises, String autoNo, Long busTypeId) throws ServiceException;
	List<BussToMerchandises> getMerInfoByNoPage(int page, int pageSize, String no, int type) throws ServiceException;
    long getMerInfoCountByNoPage(int page, int pageSize, String num, int type) throws ServiceException;
}
