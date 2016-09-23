package com.gettec.fsnip.fsn.service.test;

import com.gettec.fsnip.fsn.dao.test.ImportedProductTestResultDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.test.ImportedProductTestResult;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.report.ReportOfMarketVO;
/**
 * ImportedProductTestResultService customized operation implementation
 * 
 * @author LongXianZhen
 * 2015-05-26
 */
public interface ImportedProductTestResultService extends 
				BaseService<ImportedProductTestResult, ImportedProductTestResultDAO>{
	/**
	 * 保存进口食品报告信息
	 * @author longxianzhen 2015/05/27
	 */
	void save(ReportOfMarketVO testReport)throws ServiceException;

	/**
	 * 根据报告id查找进口食品报告信息
	 * @author longxianzhen 2015/05/27
	 */
	ImportedProductTestResult findByReportId(Long reportId)throws ServiceException;

	/**
	 * 根据报告id删除进口食品报告信息（假删除）
	 * @author longxianzhen 2015/05/27
	 */
	void deleteByTRId(Long reportId)throws ServiceException;
}