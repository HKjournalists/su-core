package com.gettec.fsnip.fsn.service.data_access;

import java.util.ArrayList;
import java.util.HashMap;
import com.gettec.fsnip.fsn.dao.data_access.Western_electronicDAO;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.data_access.Western_electronic;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.gettec.fsnip.fsn.service.common.BaseService;

/**
 * Created by Litg on 2016/08/01
 */
public interface Western_electronicService extends BaseService<Western_electronic, Western_electronicDAO> {
	public int getCount() throws Exception;
	
	public Western_electronic getLast() throws Exception;
	
	public HashMap<String,Integer> save_western_data(BusinessUnit businessUnit,ArrayList<ProductInstance> product_list,
			ArrayList<TestResult> report_list,ArrayList<TraceData> trace_list) throws Exception;
	
	public void add_western_electronic(Western_electronic western_electronic) throws Exception;
	
	public void update_western_electronic(Western_electronic western_electronic) throws Exception;
}