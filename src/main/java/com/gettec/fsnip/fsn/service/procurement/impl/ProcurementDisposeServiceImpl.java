package com.gettec.fsnip.fsn.service.procurement.impl;

import com.gettec.fsnip.fsn.dao.procurement.ProcurementDisposeDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.procurement.ProcurementDispose;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.procurement.ProcurementDisposeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * ProcurementDispose service implementation
 * 
 * @author lxz
 */
@Service(value="procurementDisposeService")
public class ProcurementDisposeServiceImpl extends BaseServiceImpl<ProcurementDispose, ProcurementDisposeDAO> 
		implements ProcurementDisposeService{
	@Autowired private ProcurementDisposeDAO procurementDisposeDAO;
	
	
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ProcurementDisposeDAO getDAO() {
		return procurementDisposeDAO;
	}



	@Override
	public long getProcurementDisposeTotalByType(String name, int type,
			Long currentUserOrganization) throws ServiceException {
		try {
			String condition = " WHERE e.organizationId = ?1 AND e.type=?2  ";
			Object[] param = new Object[]{currentUserOrganization,type};
			if(StringUtils.isNotBlank(name)){
				param = new Object[]{currentUserOrganization,type,name};
				condition += " and e.procurementName like concat('%',concat(?3,'%'))  ";
			}
			return getDAO().count(condition,param);
				
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return 0;
	}



	@Override
	public List<ProcurementDispose> getProcurementDisposeListByType(int page,
			int pageSize, String name, int type, Long currentUserOrganization)
			throws ServiceException {
		try {
			String condition = " WHERE e.organizationId = ?1 AND e.type=?2  ";
			Object[] param = new Object[]{currentUserOrganization,type};
			if(StringUtils.isNotBlank(name)){
				param = new Object[]{currentUserOrganization,type,name};
				condition += " and e.procurementName like concat('%',concat(?3,'%'))  ";
			}
			condition+=" order by e.createDate desc";
			List<ProcurementDispose> list = this.getDAO().getListByPage(page,pageSize,condition, param);
			return list;
				
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

}