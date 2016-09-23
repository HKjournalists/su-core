package com.gettec.fsnip.fsn.service.report.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gettec.fsnip.fsn.dao.report.ReportDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.service.report.ReportService;
import com.gettec.fsnip.fsn.vo.report.ReviewReportOfSuperVO;

/**
 *  该service不同于testReportService的是，它只做报告相关的查询，不做任何的增删改
 *  @author ZhangHui 2015/4/24
 */
@Service(value="reportService")
public class ReportServiceImpl implements ReportService{
	@Autowired private ReportDAO reportDAO;
	
	public ReportDAO getDAO() {
		return reportDAO;
	}

	/**
	 * 获取报告总数
	 * @author ZhangHui 2015/4/9
	 */
	@Override
	public long countOfReports(Long productId, Long fromBusId, Long toBusId, String testType) {
		return getDAO().countOfReports(productId, fromBusId, toBusId, testType);
	}
	
	/**
	 * 功能描述：获取商超下供应商录入的报告数量
	 * @author ZhangHui 2015/7/6
	 */
	@Override
	public long countOfReportsOfMyDealer(Long fromBusId, Long toBusId) {
		return getDAO().countOfReportsOfMyDealer(fromBusId, toBusId);
	}
	
	/**
	 * 获取报告发布状态
	 * @author ZhangHui 2015/4/10
	 */
	@Override
	public String getPubFlagById(Long reportId) throws ServiceException {
		try {
			return getDAO().getPubFlagById(reportId);
		} catch (DaoException e) {
			throw new ServiceException("ReportServiceImpl.countOfReports()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 获取待处理报告数量
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public long countOfOnHandleReports(Long productId, Long fromBusId,
			Long toBusId, String testType) {
		return getDAO().countOfOnHandleReports(productId, fromBusId, toBusId, testType);
	}

	/**
	 * 获取所有报告总数（不包括被商超退回的报告）
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public long countAllOfReports(Long productId, String testType) {
		return getDAO().countAllOfReports(productId, testType);
	}

	/**
	 * 获取所有报告（不包括被商超退回的报告）
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public List<ReviewReportOfSuperVO> getListOfAllReportByPage(Long productId, Long from_bus_org,
			String testType, int page, int pageSize) throws ServiceException {
		try {
			return getDAO().getListOfAllReportByPage(productId, from_bus_org, testType, page, pageSize);
		} catch (DaoException e) {
			throw new ServiceException("ReportServiceImpl.getListOfAllReportByPage()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 获取所有待处理报告
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public long countOfAllOnHandleReports(Long productId, String testType, Long fromBusId, Long toBusId) {
		return getDAO().countOfAllOnHandleReports(productId, testType, fromBusId, toBusId);
	}
}