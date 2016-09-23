package com.gettec.fsnip.fsn.dao.market;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.market.Resource;


public interface MkTestResourceDAO extends BaseDAO<Resource>{

	Resource getResourceByFileName(String fileName) throws DaoException;

	long getRelationCountByResourceId(Long resourceId) throws DaoException;
	
	List<Resource> getListBusPdfByBusUnitIdWithPage(int page,
			int pageSize,Long busUnitId) throws DaoException;

	/**
	 * 根据产品id查找产品图片集合按上传时间排序
	 * @author longxianzhen 2015/06/10
	 */
	List<Resource> getProductImgListByproId(Long proId)throws DaoException;
	
	List<Resource> getRebackImgListByreportId(Long reportId) throws DaoException;

	/**
	 * 根据企业id获取企业的证照图片地址（营业执照，组织机构代码证，流通许可证）
	 * @author longxianzhen 2015/08/03
	 */
	Map<String, String> getBusinessUnitCertById(Long buId)throws DaoException;

	/**
	 * 根据qs号id查询qs图片资源
	 * @author longxianzhen 2015/08/06
	 */
	List<Resource> getQsResourceByQsId(Long qsId)throws DaoException;
	
	public void deleteResourceByResultId(long resultId);
}
