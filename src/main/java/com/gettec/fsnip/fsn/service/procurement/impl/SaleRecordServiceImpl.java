package com.gettec.fsnip.fsn.service.procurement.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.procurement.SaleRecordDao;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.procurement.ProcurementUsageRecord;
import com.gettec.fsnip.fsn.model.procurement.SaleRecord;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.procurement.SaleRecordService;

/**
 * SaleRecord service implementation
 * 
 * @author suxiang
 */
@Service(value = "SaleRecordService")
public class SaleRecordServiceImpl extends
		BaseServiceImpl<SaleRecord, SaleRecordDao> implements SaleRecordService {

	@Autowired
	private SaleRecordDao saleRecordDao;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	public SaleRecordDao getDAO() {
		return saleRecordDao;
	}

	@Override
	public long getRecordTotalByPid(String saleDate, Long onlineSaleId) {
		try {
			String condition = " WHERE e.onlineSaleId = ?1 ";
			Object[] param = new Object[]{onlineSaleId};
			if(StringUtils.isNotBlank(saleDate)){
				try {
					Date date = sdf.parse(saleDate);
					param = new Object[]{onlineSaleId,date};
					condition += " and e.saleDate=?2 ";
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
	public List<SaleRecord> getRecordListByPid(int page, int pageSize,
			String saleDate, Long onlineSaleId) {
		try {
			String condition = " WHERE e.onlineSaleId = ?1 ";
			Object[] param = new Object[]{onlineSaleId};
			if(StringUtils.isNotBlank(saleDate)){
				try {
					Date date=sdf.parse(saleDate);
					param = new Object[]{onlineSaleId,date};
					condition += " and e.saleDate=?2 ";
				} catch (ParseException e) {
					e.printStackTrace();
					return null;
				}
			}
			condition+=" order by e.saleDate desc";
			List<SaleRecord> list = this.getDAO().getListByPage(page,pageSize,condition, param);
		
			return list;

		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}

}
