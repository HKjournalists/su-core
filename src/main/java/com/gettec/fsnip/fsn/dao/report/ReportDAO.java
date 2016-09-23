package com.gettec.fsnip.fsn.dao.report;

import java.util.List;

import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.vo.report.ReviewReportOfSuperVO;

public interface ReportDAO{

	/**
	 * 获取报告总数（不包括被商超退回的报告）
	 * @author ZhangHui 2015/5/5
	 */
	public long countOfReports(Long productId, Long fromBusId, Long toBusId, String testType);

	public String getPubFlagById(Long reportId) throws DaoException;

	/**
	 * 获取待处理报告总数
	 * @author ZhangHui 2015/5/7
	 */
	public long countOfOnHandleReports(Long productId, Long fromBusId,
			Long toBusId, String testType);

	/**
	 * 获取所有报告总数（不包括被商超退回的报告）
	 * @author ZhangHui 2015/5/7
	 */
	public long countAllOfReports(Long productId, String testType);

	/**
	 * 获取所有待处理报告
	 * @author ZhangHui 2015/5/7
	 */
	public long countOfAllOnHandleReports(Long productId, String testType, Long fromBusId, Long toBusId);

	/**
	 * 获取所有报告（不包括被商超退回的报告）
	 * @author ZhangHui 2015/5/7
	 */
	public List<ReviewReportOfSuperVO> getListOfAllReportByPage(Long productId,
			Long from_bus_org, String testType, int page, int pageSize) throws DaoException;

	/**
	 * 功能描述：获取商超下供应商录入的报告数量
	 * @author ZhangHui 2015/7/6
	 */
	public long countOfReportsOfMyDealer(Long fromBusId, Long toBusId);
}
