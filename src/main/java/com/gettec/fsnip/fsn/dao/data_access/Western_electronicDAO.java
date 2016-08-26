package com.gettec.fsnip.fsn.dao.data_access;

import java.util.ArrayList;
import java.util.HashMap;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.data_access.Western_electronic;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.trace.TraceData;

/**
 * 
 * @author litg
 *
 */
public interface Western_electronicDAO extends BaseDAO<Western_electronic>{
	public int getCount() throws Exception;
	
	public Western_electronic getLast() throws Exception;
	
	public HashMap<String,Integer> save_western_data(BusinessUnit businessUnit,ArrayList<ProductInstance> product_list,
			ArrayList<TestResult> report_list,ArrayList<TraceData> trace_list) throws Exception;
	
	public void add_western_electronic(Western_electronic western_electronic) throws Exception;
	
	public void update_western_electronic(Western_electronic western_electronic) throws Exception;
}