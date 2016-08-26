package com.gettec.fsnip.fsn.service.statistics;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.statistics.ProductVisitStatistics;
import com.gettec.fsnip.fsn.model.test.TestResult;





/**
 * ProductPoll service
 * 
 * @author 
 */
public interface ProductVisitStatisticsService {

	void statisticalVsits(String data);

	List<ProductVisitStatistics> getAllProViStaListByPage(int page,
			int pageSize, String configure)throws ServiceException;

	Long getCount(String configure)throws ServiceException;

	HSSFWorkbook downExcel(List<TestResult> tes, String configure)throws ServiceException;
}