package com.gettec.fsnip.fsn.service.report;

import java.util.List;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.vo.report.ReviewReportOfSuperVO;

public interface ReportService {

	/**
	 * 获取报告总数（不包括被商超退回的报告）
	 * @author ZhangHui 2015/5/7
	 */
	public long countOfReports(Long productId, Long fromBusId, Long toBusId, String testType);

	public String getPubFlagById(Long reportId) throws ServiceException;

	/**
	 * 获取待处理报告总数
	 * @author ZhangHui 2015/5/7
	 */
	public long countOfOnHandleReports(Long productId, Long fromBusId, Long toBusId, String testType);

	/**
	 * 获取所有报告总数（不包括被商超退回的报告）
	 * @author ZhangHui 2015/5/7
	 */
	public long countAllOfReports(Long productId, String testType);

	/**
	 * 获取所有报告（不包括被商超退回的报告）
	 * @author ZhangHui 2015/5/7
	 */
	public List<ReviewReportOfSuperVO> getListOfAllReportByPage(Long productId,
			Long from_bus_org, String testType, int page, int pageSize) throws ServiceException;

	/**
	 * 获取所有待处理报告
	 * @author ZhangHui 2015/5/7
	 */
	public long countOfAllOnHandleReports(Long productId, String testType, Long fromBusId, Long toBusId);

	/**
	 * 功能描述：获取商超下供应商录入的报告数量
	 * @author ZhangHui 2015/7/6
	 */
	public long countOfReportsOfMyDealer(Long fromBusId, Long toBusId);
}
