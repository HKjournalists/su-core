package com.gettec.fsnip.fsn.service.test;

import java.util.List;

import com.gettec.fsnip.fsn.dao.test.TestResultHandlerDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.test.TestResultHandler;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.report.ToBeStructuredReportVO;
/**
 * TestResultHandlerService customized operation implementation
 * 
 * @author LongXianZhen
 * 2015-04-28
 */
public interface TestResultHandlerService extends BaseService<TestResultHandler, TestResultHandlerDAO>{

	void createByReportId(long reportId)throws ServiceException;

	/**
	 * 根据报告id，查找可结构化报告数量
	 * @author ZhangHui 2015/5/6
	 */
	public long countByCanEdit(Long test_result_id) throws ServiceException;


	/**
	 * 根据报告id和结构化状态查找报告
	 * @author ZhangHui 2015/5/6
	 */
	public TestResultHandler findByTestResultIdCanEdit(Long test_result_id)
			throws ServiceException;

	/**
	 * 根据用户名查询结构化报告
	 * @author ZhangHui 2015/5/7
	 */
	public List<ToBeStructuredReportVO> getStructuredsByPage(String handler, int status,
			int page, int pageSize,String configure) throws ServiceException;

	/**
	 * 根据用户名查询已结构化的报告数量
	 * @author ZhangHui 2015/5/7
	 */
	public long countOfStructured(String handler,String configure);

	/**
	 * 根据用户名查询已结构化的报告
	 * @author ZhangHui 2015/5/7
	 */
	public List<ToBeStructuredReportVO> getHasStructuredsByPage(String handler,
			int page, int pageSize,String configure) throws ServiceException;

	/**
	 * 根据用户名查询结构化报告数量
	 * @author ZhangHui 2015/5/7
	 */
	public long count(String handler, int status,String configure) throws ServiceException;

	/**
	 * 根据用户名查询已退回的结构化报告数量
	 * @author ZhangHui 2015/5/7
	 */
	public long countOfBack(String handler,String configure);

	/**
	 * 根据用户名查询已退回的结构化报告
	 * @author ZhangHui 2015/5/7
	 */
	public List<ToBeStructuredReportVO> getBackStructuredsByPage(String handler,
			int page, int pageSize,String configure) throws ServiceException;

	/**
	 * 根据报告id判断该报告是否为完整的报告
	 * @author ZhangHui 2015/5/8
	 */
	public boolean isCanViewAllInfo(long test_result_id);
	
	/**
	 * 处理之前待结构化的老数据
	 * @author LongXiZhen 2015/5/8
	 */
	void dealWithTheOldData()throws ServiceException;

	/**
	 * 根据产品id查找最近一次已结构化报告id
	 * @author ZhangHui 2015/5/8
	 */
	public long getTestResultIdOfHasStructed(long productId);

	/**
	 * 根据报告id和状态查找结构化报告数量
	 * @author ZhangHui 2015/5/8
	 */
	public long count(long myReportId, int status);

}