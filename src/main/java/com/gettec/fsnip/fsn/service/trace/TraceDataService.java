package com.gettec.fsnip.fsn.service.trace;



import com.gettec.fsnip.fsn.dao.trace.TraceDataDao;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;

import java.util.List;
import java.util.Set;

public interface TraceDataService extends BaseService<TraceData, TraceDataDao> {
	boolean checkbyproductIDandbatchCode(Long productID,String batchCode);
	TraceData findPagebyproductID(Long productID,int page);
	long count (Long productID);
	TraceData findPagebyproductIDandproductDate(Long productID,int page,String productDate);
	String CheckCode(String fsnCode);
	List<TraceData> getbyOrgId(Long org,int page,int pageSize);
	long countbyOrg(long org);
	
	Set<Resource> addResource(TraceData traceData);
	public List<ProductOfMarketVO> getListOfBuylink();
	
}
