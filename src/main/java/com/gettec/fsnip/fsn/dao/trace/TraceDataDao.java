package com.gettec.fsnip.fsn.dao.trace;


import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.trace.TraceData;

import java.util.List;

public interface TraceDataDao extends BaseDAO<TraceData> {
	public boolean check(Long productID,String batchCode);
	TraceData findPagebyproductID(Long productID,int page);
	long count (Long productID);
	TraceData findPagebyproductIDandproductDate(Long productID,int page,String productDate);
	TraceData findByKeyWord(String keyword);
	List<TraceData> getbyOrgId(Long org,int page,int pageSize);
	long countbyOrg(long org);

    void updataNameByproductId(Long productID,String name);
}
