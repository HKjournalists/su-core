package com.gettec.fsnip.fsn.dao.trace;


import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.trace.TraceData;

public interface TraceDataDao extends BaseDAO<TraceData> {
	public boolean check(Long productID,String batchCode);
	TraceData findPagebyproductID(Long productID,int page);
	long count (Long productID);
	TraceData findPagebyproductIDandproductDate(Long productID,int page,String productDate);
	TraceData findByKeyWord(String keyword);
	List<TraceData> getbyOrgId(Long org,int page,int pageSize);
	long countbyOrg(long org);
}
