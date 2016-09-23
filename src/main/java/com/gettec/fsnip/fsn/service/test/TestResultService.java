package com.gettec.fsnip.fsn.service.test;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.test.TestResultDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.RiskResultVO;
import com.gettec.fsnip.fsn.vo.TestResultSearchCriteria;
import com.gettec.fsnip.fsn.vo.product.EnterpriseVo;
import com.gettec.fsnip.fsn.vo.report.ReportBackVO;
import com.gettec.fsnip.fsn.vo.report.ReportVO;
import com.gettec.fsnip.fsn.vo.report.StructuredReportOfTestlabVO;
import com.lhfs.fsn.vo.BusinessUnitVO;
import com.lhfs.fsn.vo.SampleVO;
import com.lhfs.fsn.vo.TestBusinessUnitVo;
import com.lhfs.fsn.vo.TestResultVO;
import com.lhfs.fsn.vo.TestResultsVO;

public interface TestResultService extends BaseService<TestResult, TestResultDAO>{
	public List<TestResult> getProReportListByConfigure(int page, int pageSize,
			String configure)throws ServiceException;

	public Long getCountProReport(String configure)throws ServiceException;

	List<TestResult> findTestResults(TestResultSearchCriteria criteria) throws ServiceException;

	long getCount(TestResultSearchCriteria criteria) throws ServiceException;
	
	long getThirdCount(TestResultSearchCriteria criteria) throws ServiceException;

	public List<RiskResultVO> findTRByProductInstance(Long id,Boolean isInspect)throws ServiceException;

    ReportVO getNewestReportForPIdAndReportType(Long organization,
            Long proId, String reportType, Integer status)throws ServiceException;


    /**
     * 验证报告是否存在 
     * @param serviceOrder 委托单号
     * @param organizationID 所属组织机构ID
     * @param sampleId LIMS处样品ID
     * @param sampleNO LIMS样品编号
     * @return boolean true 存在  false 失败
     * @author LongXianZhen
     */
	public boolean verifyReportExist(String serviceOrder, Long organizationID,
			Long sampleId, String sampleNO);
	/**
	 * 保存检查报告
	 * @param testResultsVO
	 * @param testResultVO
	 * @param productInstance
	 * @param testee
	 * @param fullPdfPath
	 * @param interceptionPdfPath
	 * @param sample
	 * @return Map<String, Object>
	 * @author LongXianZhen
	 */
	public Map<String, Object> saveTestResult(TestResultsVO testResultsVO,
			TestResultVO testResultVO, ProductInstance productInstance,
			BusinessUnit testee, String fullPdfPath,
			String interceptionPdfPath, SampleVO sample);

	public TestResult getLimsReportByConditions(Long sampleId, String sampleNo,
			String serviceOrder, Long organizationID)throws ServiceException;

	public void deleteById(Long id)throws ServiceException;


    public Long testResultCount() throws ServiceException;
    
    public List<EnterpriseVo> getEnterprise(int proSize,String busIds) throws ServiceException;
    
    public int countProductEnterprise() throws ServiceException;
    
    public List<EnterpriseVo> getBusinessTeaUnit(int pageSize,int page,int proSize,String keyword) throws ServiceException;
    
    public int countBusinessTeaUnit(String keyword) throws ServiceException;

    /**
     * 获取需审核的结构化报告集合
	 * @author ZhangHui 2015/5/6
     */
	public List<StructuredReportOfTestlabVO> findTestResultsOfStructureds(
			TestResultSearchCriteria criteria) throws ServiceException;

	/**
     * 获取需审核的结构化报告数量
	 * @author ZhangHui 2015/5/6
     */
	public long getCountOfStructureds(TestResultSearchCriteria criteria);

	/**
     * 审核通过结构化报告
	 * @author ZhangHui 2015/5/6
     */
	public void publishStructured(Long id) throws ServiceException;

	/**
	 * teslab审核退回结构化报告
	 * @param isSendBackToGYS
	 * 			true  代表 将报告直接退回至供应商
	 * 			false 代表 将报告退回至结构化人员 
	 * @author ZhangHui 2015/5/6
	 * 最近更新：ZhangHui 2015/6/29<br>
	 * 更新内容：增加status参数，根据报告有无被结构化，来将报告退回至结构化人员还是直接退回至供应商<br>
	 */
	public void sendBackStructured(Long test_result_id, ReportBackVO reportBackVO, boolean isSendBackToGYS) throws ServiceException;

	/**
	 * 从portal撤回报告
	 * @author ZhangHui 2015/5/6
	 */
	public void goBackFromPortal(Long test_result_id) throws ServiceException;
	
	/**
	 * 根据报告id获取检测报告 （没有级联）
	 * @param id
	 * @author lxz 2015/5/6
	 */
	public TestResult findByTestResultId(Long id)throws ServiceException;

	/**
	 * 根据条形码查询生产企业
	 * @param barcode  条形码
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<TestBusinessUnitVo> getBusinessUnitVOList(String barcode,
			Integer page, Integer pageSize);

	/**
	 * 根据条形码和生产企业id查询生产企业基本信息
	 * @param barcode   条形码
	 * @param id    生产企业id
	 * @return  返回生产企业基本信息
	 */
	public TestBusinessUnitVo getBusinessUnitVO(String barcode, long id,String date);
	
	public List<TestResult> findTestResultsByThird(TestResultSearchCriteria criteria) throws ServiceException;
}