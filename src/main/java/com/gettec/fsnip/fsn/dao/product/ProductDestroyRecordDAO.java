package com.gettec.fsnip.fsn.dao.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.product.ProductDestroyRecord;
import com.gettec.fsnip.fsn.model.trace.TraceData;
/**
 * 问题产品销毁记录DAO层接口
 * @author xuetaoyang
 *
 */
public interface ProductDestroyRecordDAO extends BaseDAO<ProductDestroyRecord> {
	List<ProductDestroyRecord> getbyOrgId(String orgname,int page,int pageSize,String keyword);
	//List<ProductDestroyRecord> getbyOrgIdandKeyword(String orgname,int page,int pageSize,String keyword);
	long countbyOrg(String orgname,String keyword);
}
