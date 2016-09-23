package com.gettec.fsnip.fsn.service.test.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.test.ImportedProductTestResultDAO;
import com.gettec.fsnip.fsn.dao.test.TestResultDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.test.ImportedProductTestResult;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.test.ImportedProductTestResultService;
import com.gettec.fsnip.fsn.vo.report.ReportOfMarketVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;

/**
 * ImportedProductTestResultService service implementation
 * 
 * @author LongXianZhen
 * 2015-05-26
 */
@Service(value="importedProductTestResultService")
public class ImportedProductTestResultServiceImpl extends 
		BaseServiceImpl<ImportedProductTestResult, ImportedProductTestResultDAO> 
		implements ImportedProductTestResultService{
	@Autowired protected ImportedProductTestResultDAO importedProductTestResultDAO;
	@Autowired protected TestResultDAO testResultDAO;
	@Autowired protected ResourceService testResourceService;
	
	@Override
	public ImportedProductTestResultDAO getDAO() {
		return importedProductTestResultDAO;
	}

	/**
	 * 保存进口食品报告信息
	 * @author longxianzhen 2015/05/27
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(ReportOfMarketVO testReport) throws ServiceException {
		try {
			
			String userName=AccessUtils.getUserName()!=null?AccessUtils.getUserName().toString():null;
			ImportedProductTestResult impProductTestResult=testReport.getImpProTestResult();
			impProductTestResult.setTestResultId(testReport.getId());
			/* 1. 保存卫生证书图片 */
			testResourceService.saveSanResource(impProductTestResult);
			/* 1. 保存卫生证书PDF */
			testResourceService.saveSanPdfResource(impProductTestResult);
			/* 当id为null时新增 否则更新 */
			if(impProductTestResult.getId()==null){ //新增
				impProductTestResult.setCreateDate(new Date());
				impProductTestResult.setCreator(userName);
				importedProductTestResultDAO.persistent(impProductTestResult);
			}else{  //更新 
				ImportedProductTestResult oldImpProductTset=importedProductTestResultDAO.findById(impProductTestResult.getId());
				oldImpProductTset.setSanitaryCertNo(impProductTestResult.getSanitaryCertNo());
				oldImpProductTset.setLastModifyDate(new Date());
				oldImpProductTset.setLastModifyUser(userName);
				importedProductTestResultDAO.merge(oldImpProductTset);
			}
		}  catch (JPAException je) {
			throw new ServiceException("ImportedProductTestResultServiceImpl.save()-->"+je.getMessage(),je.getException());
		} catch (Exception e) {
			throw new ServiceException("ImportedProductTestResultServiceImpl.save()-->"+e.getMessage(),e);
		}
	}

	/**
	 * 根据报告id查找进口食品报告信息
	 * @author longxianzhen 2015/05/27
	 */
	@Override
	public ImportedProductTestResult findByReportId(Long reportId)
			throws ServiceException {
		try {
			String condition= "  WHERE e.testResultId=?1 AND e.del=0 ";
			List<ImportedProductTestResult> impProductTests=getDAO().getListByCondition(condition, new Object[]{reportId});
			ImportedProductTestResult impProductTest=null;
			if(impProductTests!=null&&impProductTests.size()>0){
				impProductTest=impProductTests.get(0);
				if(impProductTest.getSanitaryPdfAttachments()!=null&&impProductTest.getSanitaryPdfAttachments().size()>0){
					impProductTest.setSanitaryPdfURL(impProductTest.getSanitaryPdfAttachments().iterator().next().getUrl());
				}
			}
			return impProductTest;
		} catch (JPAException dex) {
			throw new ServiceException("ImportedProductTestResultServiceImpl.ImportedProductTestResult()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 根据报告id删除进口食品报告信息（假删除）
	 * @author longxianzhen 2015/05/27
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteByTRId(Long reportId) throws ServiceException {
		try {
			importedProductTestResultDAO.deleteByTRId(reportId);
		} catch (DaoException dex) {
			throw new ServiceException("ImportedProductTestResultServiceImpl.deleteByTRId()-->" + dex.getMessage(), dex.getException());
		}
	}

	
}