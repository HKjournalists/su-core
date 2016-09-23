package com.gettec.fsnip.fsn.dao.test;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.test.TestResultHandler;
import com.gettec.fsnip.fsn.vo.report.ToBeStructuredReportVO;
/**
 * LimsNotFindProduct customized operation implementation
 * 
 * @author LongXianZhen
 * 2015-04-28
 */
 public interface TestResultHandlerDAO extends BaseDAO<TestResultHandler>{

	/**
	 * 更新可结构化报告数量
	 * @author ZhangHui 2015/5/6
	 */
	public long countByCanEdit(Long test_result_id) throws DaoException;

	/**
	 * 根据报告id和结构化状态查找报告
	 * @author ZhangHui 2015/5/6
	 */
	public TestResultHandler findByTestResultIdCanEdit(Long test_result_id) throws DaoException;

	/**
	 * 根据用户名查询已退回的结构化报告
	 * @author ZhangHui 2015/5/7
	 */
	public List<ToBeStructuredReportVO> getStructuredsByPage(String handler,
			int status, int page, int pageSize, String configure) throws DaoException;

	/**
	 * 根据用户名查询已结构化报告数量
	 * @author ZhangHui 2015/5/7
	 */
	public long countOfStructured(String handler, String configure);

	/**
	 * 根据用户名查询已结构化报告
	 * @author ZhangHui 2015/5/7
	 */
	public List<ToBeStructuredReportVO> getHasStructuredsByPage(String handler,
			int page, int pageSize, String configure) throws DaoException;

    /**
	 * 根据用户名查询结构化报告数量
	 * @author ZhangHui 2015/5/7
	 */
	public long count(String handler, int status, String configure);

	/**
	 * 根据用户名查询已退回的结构化报告数量
	 * @author ZhangHui 2015/5/7
	 */
	public long countOfBack(String handler, String configure);

	/**
	 * 根据用户名查询已退回的结构化报告
	 * @author ZhangHui 2015/5/7
	 */
	public List<ToBeStructuredReportVO> getBackStructuredsByPage(
			String handler, int page, int pageSize, String configure) throws DaoException;

	/**
	 * 根据报告id判断该报告是否为完整的报告
	 * @author ZhangHui 2015/5/8
	 */
	public boolean isCanViewAllInfo(long test_result_id);

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
    
	/**
	 * 返回处理报告最小数量的人员信息
	 * @author wubiao 2016/4/18
	 * @return
	 */
	public TestResultHandler getMinTestResultHandler(String user_handler);
}