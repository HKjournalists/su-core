package com.gettec.fsnip.fsn.service.data_access.impl;

import com.gettec.fsnip.fsn.dao.business.BusinessUnitDAO;
import com.gettec.fsnip.fsn.dao.data_access.Western_electronicDAO;
import com.gettec.fsnip.fsn.dao.product.ProductInstanceDAO;
import com.gettec.fsnip.fsn.dao.trace.TraceDataProductNameDao;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.data_access.Western_electronic;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.data_access.Western_electronicService;
import com.lhfs.fsn.dao.testReport.TestReportDao;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Litg on 2016/08/01
 */
@Service(value = "western_electronicService")
public class Western_electronicServiceImpl extends BaseServiceImpl<Western_electronic, Western_electronicDAO> implements Western_electronicService {
	
	@Autowired
	Western_electronicDAO western_electronicDAO;
	
	@Autowired
	BusinessUnitDAO businessUnitDAO;
	
	@Autowired
	ProductInstanceDAO productInstanceDAO;
	
	@Autowired
	TestReportDao testReportDAO;
	
	@Autowired
	TraceDataProductNameDao traceDataProductNameDAO;
	
	@Override
	public Western_electronicDAO getDAO() {
		return western_electronicDAO;
	}
	
	public int getCount() throws Exception {
		return western_electronicDAO.getCount();
	}
	
	public Western_electronic getLast() throws Exception {
		return western_electronicDAO.getLast();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public HashMap<String,Integer> save_western_data(BusinessUnit businessUnit,ArrayList<ProductInstance> product_list,
			ArrayList<TestResult> report_list,ArrayList<TraceData> trace_list) throws Exception {
		return western_electronicDAO.save_western_data(businessUnit, product_list, report_list, trace_list);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void add_western_electronic(Western_electronic western_electronic) throws Exception {
		western_electronicDAO.add_western_electronic(western_electronic);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update_western_electronic(Western_electronic western_electronic) throws Exception {
		western_electronicDAO.update_western_electronic(western_electronic);
	}
}