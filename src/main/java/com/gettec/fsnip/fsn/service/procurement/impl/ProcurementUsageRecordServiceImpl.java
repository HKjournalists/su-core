package com.gettec.fsnip.fsn.service.procurement.impl;

import com.gettec.fsnip.fsn.dao.procurement.ProcurementUsageRecordDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.procurement.ProcurementUsageRecord;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.procurement.ProcurementUsageRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * ProcurementUsageRecord service implementation
 * 
 * @author lxz
 */
@Service(value="procurementUsageRecordService")
public class ProcurementUsageRecordServiceImpl extends BaseServiceImpl<ProcurementUsageRecord, ProcurementUsageRecordDAO> 
		implements ProcurementUsageRecordService{
	@Autowired private ProcurementUsageRecordDAO procurementUsageRecordDAO;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ProcurementUsageRecordDAO getDAO() {
		return procurementUsageRecordDAO;
	}


	@Override
	public long getRecordTotalByPid(String useDate, Long procurementId) throws ServiceException{
		try {
			String condition = " WHERE e.procurementId = ?1 ";
			Object[] param = new Object[]{procurementId};
			if(StringUtils.isNotBlank(useDate)){
				try {
					Date date=sdf.parse(useDate);
					param = new Object[]{procurementId,date};
					condition += " and e.useDate=?2 ";
				} catch (ParseException e) {
					e.printStackTrace();
					return 0;
				}
			}
			return getDAO().count(condition,param);

		} catch (JPAException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<ProcurementUsageRecord> getRecordListByPid(int page, int pageSize, String useDate, Long procurementId) throws ServiceException {
		try {
			String condition = " WHERE e.procurementId = ?1 ";
			Object[] param = new Object[]{procurementId};
			if(StringUtils.isNotBlank(useDate)){
				try {
					Date date=sdf.parse(useDate);
					param = new Object[]{procurementId,date};
					condition += " and e.useDate=?2 ";
				} catch (ParseException e) {
					e.printStackTrace();
					return null;
				}
			}
			condition+=" order by e.useDate desc";
			List<ProcurementUsageRecord> list = this.getDAO().getListByPage(page,pageSize,condition, param);
			return list;

		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}
}