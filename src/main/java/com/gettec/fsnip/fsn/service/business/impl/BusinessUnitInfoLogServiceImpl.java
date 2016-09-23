package com.gettec.fsnip.fsn.service.business.impl;

import java.util.Date;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.business.BusinessUnitInfoLogDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.BusinessUnitInfoLog;
import com.gettec.fsnip.fsn.service.business.BusinessUnitInfoLogService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;

/**
 * businessUnitInfoLogService service implementation
 * 
 * @author LongXianZhen  2015/06/03
 */
@Service(value="businessUnitInfoLogService")
public class BusinessUnitInfoLogServiceImpl extends BaseServiceImpl<BusinessUnitInfoLog, BusinessUnitInfoLogDAO> 
		implements BusinessUnitInfoLogService{
	@Autowired protected BusinessUnitInfoLogDAO businessUnitInfoLogDAO;
	
	@Override
	public BusinessUnitInfoLogDAO getDAO() {
		return businessUnitInfoLogDAO;
	}
	/**
	 * 保存企业信息日志
	 * @param data 
	 * @author LongXianZhen 2015/06/04
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveBusinessUnitInfoLog(BusinessUnitInfoLog data) {
		try {
			BusinessUnit bu=data.getBusinessUnit();
			bu.setCustomers(null);
			bu.setProviders(null);
			bu.setCustomerlist(null);
			try {
				JSON json=JSONSerializer.toJSON(bu);//JSONObject.fromObject(bu);
				data.setOperationData(json.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			data.setBusinessUnitId(bu.getId());
			data.setBusinessUnitName(bu.getName());
			data.setOperationTime(new Date());
			businessUnitInfoLogDAO.persistent(data);
		} catch (JPAException e) {
			((Throwable) e.getException()).printStackTrace();
		}
	}

}