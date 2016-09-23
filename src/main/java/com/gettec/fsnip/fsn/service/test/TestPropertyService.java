package com.gettec.fsnip.fsn.service.test;

import java.util.List;
import com.gettec.fsnip.fsn.dao.test.TestPropertyDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface TestPropertyService extends BaseService<TestProperty, TestPropertyDAO>{
	Long getCountByReportId(Long reportId) throws ServiceException;
	
	List<TestProperty> getListByReportIdWithPage(Long repId, int page, 
			int pageSize) throws ServiceException;

	public List<TestProperty> findByReportId(Long reportId) throws ServiceException;

	void deleByTRID(Long id) throws ServiceException;

	List<String> getListOfColumeValue(int columeId, String keyword, int page,
			int pageSize) throws ServiceException;

	/**
	 * 获取所有报告（不包括被商超退回的报告）
	 * @author ZhangHui 2015/5/7
	 */
	public List<TestProperty> findByReportIdWithoutId(long reportId) throws ServiceException;

	/**
	 * 功能描述：报告录入页面，保存检测项目
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/7
	 */
	public void save(Long report_id, List<TestProperty> properties, boolean isNew)
			throws ServiceException;

	boolean deleteTestProperty(long id);

	void saveTestProperty(List<TestProperty> items);
	
	List<TestProperty> getTestPropertyListByProductId(long productId);
}