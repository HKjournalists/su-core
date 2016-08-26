package com.lhfs.fsn.service.testReport;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.ui.Model;

import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.test.TestRptJson;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.report.ReportBackVO;
import com.gettec.fsnip.fsn.vo.report.ReportOfMarketVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.dao.testReport.TestReportDao;
import com.lhfs.fsn.vo.BusinessTestResultVO;
import com.lhfs.fsn.vo.ProductInfoVO;
import com.lhfs.fsn.vo.ProductJGVO;
import com.lhfs.fsn.vo.report.ReportVO;
import com.lhfs.fsn.vo.report.ResultToShianjianVO;

public interface TestReportService  extends BaseService<TestResult, TestReportDao>{

	TestRptJson getReportJson(long product_id, String test_report_type, int sn, String date, boolean bTr, List<TestResult> trList,boolean portalFlag) throws ServiceException;
     
	String addRpt(JSONObject testResults) throws ServiceException;
	
	TestRptJson getReportJson(long id, String test_report_type, String date) throws ServiceException;
	
	String getIDsByProductInstanceID(Long id);

	long countByOrgIdAndUserRealName(Long organizationId, String userRealName,
			char pubFlag, String configure) throws ServiceException;

	List<TestResult> getReportsByOrgIdAndUserRealNameWithPage(
			Long organizationId, String userRealName, int page, int pageSize,
			char pubFlag, String configure) throws ServiceException;

	TestResult editTips(Long reportId, String tipTextValue) throws ServiceException;

	void deleteTestReport(ResultVO resultVO, Long id) throws ServiceException;

	Long countByOrgNameAndUserRealNameAndBackFlag(Long organizationId, String userRealName,
			char pubFlag, String condition) throws ServiceException;

	long countByIsCanPublish(Long organizationId, char pubFlag, 
			String condition) throws ServiceException;

	List<TestResult> getListByIsCanPublish(Long organizationId, int page,
			int pageSize, char pubFlag, String condition) throws ServiceException;

	List<Resource> addTestResources(Collection<Resource> resources) throws ServiceException;

	List<String> autoTestItems(int colName,String keyword,int page,int pageSize) throws ServiceException;

	void goBack(Long reportId,ReportBackVO reportBackVO) throws ServiceException;

	boolean validatHaveProJpg(Long reportId) throws ServiceException;

	boolean getSignFlag(Long reportId) throws ServiceException;

	TestResult changeSignFlag(Long id, boolean signFlag) throws ServiceException;

	void updatePdfUrl(Long id, String pdfUrl) throws ServiceException;

	long countBySampleId(Long sampleId) throws ServiceException;
	
	long countByProductIdAndTestType(Long productId,String testType) throws ServiceException;

	Long getBusIdBytestReportId(Long id) throws ServiceException;
	
	ResultToShianjianVO findByProductIdAndproductionDate(Long proId, String date) throws ServiceException;

	//hy
	Model getReportBySampleId(Long id,String batch, Model model) throws ServiceException;

	/**
	 * 功能描述：报告唯一性检查（报告编号、产品barcode、批次）
	 * @return  true  代表 当前报告是唯一的
	 * 			false 代表 已经存在类似的报告
	 * @throws DaoException
	 * @author ZhangHui 2015/6/5
	 */
	public boolean checkUniquenessOfReport(Long reportId, String serviceOrder, String barcode, String batchNo)
			throws ServiceException;

	Map<String, List<ReportVO>> getInstanceReport(Long id, String batch)throws ServiceException;

	/**
	 * 根据产品id 查找该产品的报告总数
	 * @param id 产品id
	 * @return	Long
	 * @throws ServiceException
	 */
	Long getReportCountForProductId(Long id)throws ServiceException;

	boolean goBack(Long reportId) throws ServiceException;

	void publishToTestLab(Long reportId, String tempProductUrl,
			AuthenticateInfo info) throws ServiceException;

	void saveAfterSign(Long reportId, ByteArrayInputStream input)
			throws ServiceException;

	List<String> getSelfReportPdfUrlsByBarcode(String barcode,String batch)throws ServiceException;

	/**
	 * 更新报告发布状态
	 * @author ZhangHui 2015/4/10
	 */
	void updatePublishFlag(char publishFlag, long reportId, String msg)
			throws ServiceException;

	/**
	 * 查找商超所有已经通过商超审核的报告数量
	 * @author ZhangHui 2015/4/14
	 */
	long countOfDealerAllPass(String organizationName,
			String userRealName, char pubFlag, String configure)
			throws ServiceException;

	/**
	 * 查找商超所有已经通过商超审核的报告
	 * @author ZhangHui 2015/4/14
	 */
	List<TestResult> getReportsOfDealerAllPassWithPage(
			String organizationName, String userName, int page,
			int pageSize, char pubFlag, String configure) throws ServiceException;
	
	/**
	 * 根据报告编号和报告来源查找报告数量
	 * @author ZhangHui 2015/4/24
	 */
	public long countByServiceorderAndEdition(String serviceOrder, String edition) throws ServiceException;

	/**
	 * 接受泊银等其他系统数据，本地新增一条报告
	 * @author ZhangHui 2015/4/24
	 */
	public boolean createBYReport(TestResult testReport, Long organization,
			String userName);

	/**
	 * 功能描述：报告录入页面，保存报告
	 * @throws Exception 
	 * @author ZhangHui 2015/6/7
	 */
	public void saveReport(ReportOfMarketVO report_vo, Long current_business_id, AuthenticateInfo info, boolean isStructed) throws Exception;

	/**
	 * 大众门户按日期查找报告
	 *@author LongXianZhen 2015/06/02
	 */
	TestRptJson getReportJsonByDate(long id, String type, int sn, String date,boolean portalFlag)throws ServiceException;
	/**
	 * 大众门户按精确日期查找报告
	 *@author LongXianZhen 2015/06/02
	 */
	TestRptJson getReportJsonByPreciseDate(long id, String type, int sn, String date,boolean portalFlag)throws ServiceException;

	/**
	 * 功能描述：查找用户通过解析pdf生成的报告数量
	 * @author ZhangHui 2015/6/18
	 * @throws ServiceException 
	 */
	public long countOfPasePdf(String userName, String configure) throws ServiceException;

	/**
	 * 功能描述：查找用户通过解析pdf生成的报告
	 * @author ZhangHui 2015/6/18
	 * @throws ServiceException 
	 */
	public List<TestResult> getListOfPasePdfByPage(String userName, int page,
			int pageSize, String configure) throws ServiceException;

	/**
	 * 功能描述：获取所有退回给当前登陆供应商的报告数量
	 * @author ZhangHui 2015/6/29
	 * @throws ServiceException 
	 */
	public long countOfBackToGYS(Long organization, String configure) throws ServiceException;

	/**
	 * 功能描述：分页查询退回给当前登陆供应商的报告
	 * @author ZhangHui 2015/6/29
	 * @throws ServiceException 
	 */
	public List<TestResult> getReportsOfBackToGYSByPage(Long organization, int page, int pageSize, String configure) throws ServiceException;

	/**
	 * 功能描述：封装TestRptJson报告
	 * @author longxianzhen 2015/6/29
	 */
	TestRptJson getReportJson(TestResult tr)throws ServiceException;

	/**
	 * 按组织机构和用户名查找报告列表（商超供应商）
	 * @author longxianzhen 2015/08/10
	 */
	List<TestResult> getReportsByOrgIdAndUserNameFromSCWithPage(
			Long currentUserOrganization, String userName, int page,
			int pageSize, char pubFlag, String configure)throws ServiceException;
	
	/**
	 * 根据barcode验证该产品是否有已退回的报告没有处理
	 * @param barcode
	 * @return
	 */
	boolean verifyBackReportByBarcode(String barcode)throws ServiceException;
    /**
     *保存检测项目
     * @param items
     * @return
     */
	void saveTestProperty(List<TestProperty> items);

	/**
	 * 获取生产企业报告
	 * @param barcode 条形码
 	 * @param buId  企业id
	 * @param product_id 产品id
	 * @param type type 报告类型：{"self","censor","sample"}
	 * @param data 查询条件日期
	 * @return
	 */
	TestRptJson getTestResult(String barcode, long buId, long product_id,
			String type, String date);
	void updateRecordOfSample(long reportId,long productInstanceId) throws DaoException;

	List<TestRptJson> getTestResultList(String barcode, long buId,
			Long productId, String type, String date);

	/**
	 * 更加报告ID查询报告是否已经发布，查询PublishFlag状态值
	 * authar : wubiao   
	 * date :2016.2.18 9:45
	 * @param id 报告ID
	 * @return
	 */
	TestResult findByIdPublishFlag(Long id);

	List<BusinessTestResultVO> getByLicenseNoTistResult(String licenseNo, String type,
			Integer page, Integer pageSize);

	List<ProductInfoVO> getByLicenseNoProduct(String licenseNo, Integer page,
			Integer pageSize);

	Long getByLicenseNoProductCount(String licenseNo);


	List<ProductJGVO> getFindProduct(BusinessUnit businessUnit,String type, int page, int pageSize);

	Long getByProductCount(BusinessUnit businessUnit,String type);


	long countByProductIdAndTestTypeWithProductdate(Long productId,
			String testType, String productDate) throws ServiceException;
	
	List<TestRptJson> getThirdList(long productId,int date,String type);
	
	long getThirdCountByProductId(long productId);
}