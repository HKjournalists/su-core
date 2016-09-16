package com.gettec.fsnip.fsn.dao.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.vo.RiskResultVO;
import com.gettec.fsnip.fsn.vo.product.EnterpriseVo;
import com.gettec.fsnip.fsn.vo.report.ReportVO;
import com.gettec.fsnip.fsn.vo.report.StructuredReportOfTestlabVO;
/**
 * TestResult customized operation implementation
 * 
 * @author Ryan Wang
 */
 public interface TestResultDAO extends BaseDAO<TestResult>{

	 /**
	  * 根据条件查询某个产品最后发布报告的时间
	  * @param proId 产品ID
	  * @param organizationId 企业组织机构ID
	  * @param startDate 报告发布起始时间
	  * @param endDate 报告发布结束时间
	  * @return Date
	  * @throws DaoException
	  * @author LongXianZhen
	  */
	public Date getLastPubDateByProIdBu(Long proId, Long organizationId,String startDate ,String endDate)throws DaoException;
	/**
	 * 根据企业组织机构ID查询该企业已发布报告的产品数量
	 * @param organizationId 企业组织机构ID
	 * @return Long
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	public Long getPublishProCountByOrganizationId(Long organizationId)throws DaoException;

	/**
	 * 根据企业组织机构ID查询该企业已发布报告数量
	 * @param organizationId 企业组织机构ID
	 * @return Long
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	public Long getRepoCountByOrganizationId(Long organizationId)throws DaoException;
	
	/**
	 * 根据条件查询某个产品已发布或未发布报告的数量
	 * @param organizationId 企业组织机构ID
	 * @param proId 产品ID
	 * @param startDate 报告发布起始时间
	 * @param endDate 报告发布结束时间
	 * @param publishFlag 报告发布标志为'1'查询已发布的报告数量，为'0'查询未发布的报告数量
	 * @return Long
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	public Long getReportQuantityByProBu(Long organizationId, Long proId,String startDate,String endDate,String publishFlag)throws DaoException;

	public List<TestResult> getProReportListByPage(int page, int pageSize,
			String configure)throws DaoException;

	public Long getCountProReport(String configure)throws DaoException;

	List<TestResult> findTestResults(boolean publishFlag, int page,
			int pageSize, String configure) throws DaoException;

	long getTestResultCount(boolean publishFlag, String configure) throws DaoException;
	
	long getTestResultThirdCount(String configure) throws DaoException;

    public List<TestResult> getProEXECLReportList(
            Map<String, Object> configSQLString)throws DaoException;

    public List<RiskResultVO> findTRByProductInstance(Long id,Boolean isInspect)throws DaoException;

    public ReportVO getNewestReportForPIdAndReportType(Long organization,
            Long proId, String reportType,Integer status)throws DaoException;
	
    public Long testResultCount() throws DaoException;
	
    public List<EnterpriseVo> getEnterprise(int proSize,String busIds) throws DaoException;
    
    public int countProductEnterprise() throws DaoException;
    
    public List<EnterpriseVo> getBusinessTeaUnit(int pageSize,int page,int proSize,String keyword) throws DaoException;
    
    public int countBusinessTeaUnit(String keyword) throws DaoException;
    
    /**
	 * 获取需审核的结构化报告集合
	 * @author ZhangHui 2015/5/6
	 */
	public List<StructuredReportOfTestlabVO> findTestResultsOfStructureds(int page, int pageSize,String configure) throws DaoException;
	
	/**
	 * 获取需审核的结构化报告数量
	 * @author ZhangHui 2015/5/6
	 */
	public long getCountOfStructureds(String configure);
	
	/**
     * 审核通过报告
	 * @author ZhangHui 2015/5/6
     */
	public void publishTestResult(Long result_id) throws DaoException;
	
	/**
	 * 审核通过结构化报告
	 * @author ZhangHui 2015/5/6
     */
	public void publishTestResultOfStructured(Long result_id) throws DaoException;
	
	/**
	 * 从portal撤回不是来自经销商的报告
	 * @author ZhangHui 2015/5/6
	 * @return 
	 */
	public int goBackTestResult(Long test_result_id) throws DaoException;
	
	/**
	 * 从portal撤回结构化报告
	 * @author ZhangHui 2015/5/6
	 */
	public void goBackTestResultOfStructured(Long test_result_id) throws DaoException;
	
	/**
	 * 从portal撤回来自经销商的报告_0
	 * @author ZhangHui 2015/5/6
	 * @return 
	 */
	public int goBackTestResultOfDealer_0(Long test_result_id);
	
	/**
	 * 从portal撤回来自经销商的报告_1
	 * @author ZhangHui 2015/5/6
	 * @return 
	 */
	public int goBackTestResultOfDealer_1(Long test_result_id);
	
	/**
	 * 根据组织机构id获取产品数量
	 * @author LongxianZhen 20150625
	 */
	public Integer productCountByOrg(Integer organization)throws DaoException;
	
	/**
	 * 功能描述：teslab将已结构化报告退回至结构化人员
	 * @author ZhangHui 2015/6/29
     */
	public void sendBackToJGH(Long test_result_id, String returnMes)
			throws DaoException;
	
	/**
	 * 功能描述：teslab将已结构化报告退回至结构化人员
	 * @author ZhangHui 2015/6/29
     */
	public void sendBackOfStructuredToJGH(Long test_result_id) throws DaoException;
	
	/**
	 * 功能描述：teslab将已结构化报告退回至供应商
	 * @author ZhangHui 2015/6/29
     */
	public void sendBackToGYS(Long test_result_id, String returnMes)
			throws DaoException;
	
	/**
	 * 功能描述：teslab将已结构化报告退回至供应商
	 * @author ZhangHui 2015/6/29
     */
	public void sendBackOfStructuredToGYS(Long test_result_id,String sql) throws DaoException;
	/**
	 * 根据报告id获取检测报告 （没有级联）
	 * @param id
	 * @author lxz 2015/5/6
	 */
	public TestResult findByTestResultId(Long id)throws DaoException;
	
	public List<TestResult> findTestResults(int page, int pageSize, String configure) throws DaoException;
}