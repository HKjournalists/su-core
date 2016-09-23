package com.gettec.fsnip.fsn.service.procurement;

import com.gettec.fsnip.fsn.dao.procurement.ProcurementDisposeDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.procurement.ProcurementDispose;
import com.gettec.fsnip.fsn.service.common.BaseService;

import java.util.List;

/**
 * ProcurementDispose Service
 * @author lxz
 *
 */
public interface ProcurementDisposeService extends BaseService<ProcurementDispose, ProcurementDisposeDAO>{

	long getProcurementDisposeTotalByType(String name, int type,
			Long currentUserOrganization)throws ServiceException;

	List<ProcurementDispose> getProcurementDisposeListByType(int page, int pageSize,
			String name, int type, Long currentUserOrganization)throws ServiceException;

}