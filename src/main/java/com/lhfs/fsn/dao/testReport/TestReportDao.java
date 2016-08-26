package com.lhfs.fsn.dao.testReport;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.test.TestRptJson;
import com.lhfs.fsn.vo.ProductInfoVO;
import com.lhfs.fsn.vo.ProductJGVO;

public interface TestReportDao extends BaseDAO<TestResult>{

	List<TestResult> getResultListByIdAndType(long product_id,
			String test_report_type);

	String getSerial(long sampleId);

	String getTradeMark(long brandId);

	String getEnterprise(long brandId);

	String getFormat(long sampleId);

	String getTestee(long testeeId);

	String getBatchSN(long sampleId);

	String getStatus(long sampleId);

	List<TestProperty> getPropertyListByID(long id);

	TestRptJson getTestRptJson(long product_id, String test_report_type, int sn);

	BusinessUnit getBusinessUnit(Long long1);

	BusinessBrand getBrand(String barcode);

	BusinessBrand getBrandBySampleID(Long id);

	TestResult getRptByIdAndTypeAndDate(long id, String test_report_type,
			String date);

	List<BigInteger> getIDsByProductInstanceID(Long id);

	boolean findBySampleNO(String serviceOrder);

	boolean verifyExist(String barcode, String batchSeriaNo, String serviceOrder);

	List<TestResult> getListByIsHavePdfWithPage(int page, int pageSize,
			Map<String, Object> map) throws DaoException;

	List<TestResult> getListByConditionWithPage(char pubFlag,
			int page, int pageSize, Map<String, Object> map) throws DaoException;

	long countByConditionIsCanPublish(Map<String, Object> configure) throws DaoException;
	
	long countByProductIdAndTestType(Long productId,String testType) throws DaoException;

	Long getBusIdBytestReportId(Long id) throws DaoException;
	
	TestResult findByProductIdAndproductionDate(Long proId,String date,String type) throws DaoException;

	List<TestResult> getReportBySampleId(Object id) throws DaoException;
	
	public List<TestProperty> getItemsByReportId(Long reportId) throws DaoException;

	/**
	 * 功能描述：报告唯一性检查（报告编号、产品barcode、批次）
	 * @return  true  代表 当前报告是唯一的
	 * 			false 代表 已经存在类似的报告
	 * @throws DaoException
	 * @author ZhangHui 2015/6/5
	 */
	public boolean checkUniquenessOfReport(Long reportId, String serviceOrder,	String barcode, String batchNo) 
			throws DaoException;

	/**
	 * 根据产品id 查找该产品的报告总数
	 * @param id
	 * @return Long
	 * @throws DaoException
	 */
	Long getReportCountForProductId(Long id)throws DaoException;

	List<String> getSelfReportPdfUrlsByBarcode(String barcode,String batch)throws DaoException;

	/**
	 * 更新销报告发布状态
	 * @author ZhangHui 2015/4/10
	 */
	public void updatePublishFlag(char publishFlag, long reportId, String msg,String checkOrgName) throws DaoException;

	/**
	 * 根据报告编号和来源标识，获取报告数量
	 * @author ZhangHui 2015/4/24
	 */
	public long countByServiceorderAndEdition(String serviceOrder, String edition) throws DaoException;

	/**
	 * 功能描述：关联报告的产品实例
	 * @author ZhangHui 2015/6/7
	 * @throws DaoException 
	 */
	public void updateRecordOfSample(Long id, Long sample_id) throws DaoException;

	/**
	 * 功能描述：关联报告的被检单位/人
	 * @author ZhangHui 2015/6/7
	 * @throws DaoException 
	 */
	public void updateRecordOfTestee(Long id, Long testee_id) throws DaoException;

	/**
	 * 大众门户按日期查找报告最近的报告
	 *@author LongXianZhen 2015/06/02
	 */
	TestResult getRptListByIdAndTypeAndDate(long proId, String testType,
			String date,int sn,boolean portalFlag)throws DaoException;

	/**
	 * 功能描述：根据报告id,删除/恢复报告
	 * @param del 
	 * 			0 代表恢复报告
	 * 			1 代表删除报告
	 * @author ZhangHui 2015/6/17
	 * @throws DaoException 
	 */
	public void updateByDel(Long reportId, int del) throws DaoException;

	/**
	 * 功能描述：查找当前登录用户通过解析pdf增加的报告数量。
	 * @author ZhangHui 2015/6/18
	 * @throws DaoException 
	 */
	public long countOfPasePdf(String userName, Map<String, Object> configure) throws DaoException;

	/**
	 * 功能描述：查找用户通过解析pdf生成的报告
	 * @author ZhangHui 2015/6/18
	 * @throws ServiceException 
	 */
	public List<TestResult> getListOfPasePdfByPage(String userName, int page,
			int pageSize, Map<String, Object> configure) throws DaoException;
	/**
	 * 查找供应商的商超已审核通过的报告
	 * @author longxianzhen 20150807
	 */
	List<TestResult> getReportsOfDealerAllPassWithPage(String config, int page,
			int pageSize)throws DaoException;

	/**
	 * 查找供应商的商超已审核通过的报告数量
	 * @author longxianzhen 20150807
	 */
	long countOfDealerAllPass(String config)throws DaoException;
	
	/**
	 * 根据产品id及报告检测类型查找该产品最新的报告
	 * @author longxianzhen 2015/08/07
	 */
	TestResult getResultByIdAndType(Long id, String testType,int sn,String date,boolean portalFlag)throws DaoException;

	/**
	 * 根据产品id及报告检测类型查找该产品报告数量
	 * @author longxianzhen 2015/08/07
	 */
	Integer countByIdAndType(Long id, String testType,String date,boolean portalFlag)throws DaoException;
	
	/**
	 * 根据barcode验证该产品是否有已退回的报告没有处理
	 * @param barcode
	 * @return
	 */
	boolean verifyBackReportByBarcode(String barcode)throws DaoException;
	/**
	 * 获取生产企业报告
	 * @param barcode 条形码
 	 * @param buId  企业id
	 * @param product_id 产品id
	 * @param type type 报告类型：{"self","censor","sample"}
	 * @param data 查询条件日期
	 * @return
	 */
	TestResult getTestResult(String barcode, long buId, long product_id,
			String type, String date);
    
	Integer countByIdAndTypeOrg(long product_id, String type, String pDate,
			Long organization);

	/**
	 * 更加报告ID查询报告是否已经发布，查询PublishFlag状态值
	 * authar : wubiao   
	 * date :2016.2.18 9:45
	 * @param id 报告ID
	 * @return
	 */
	TestResult findByIdPublishFlag(Long id);

	List<TestResult> getByLicenseNoTistResult(String licenseNo, String type,
			Integer page, Integer pageSize);

	List<ProductInfoVO> getByLicenseNoProduct(String licenseNo, Integer page,
			Integer pageSize);

	Long getByLicenseNoProductCount(String licenseNo);


	Map<String, String> getByIdRegularity(long id);

	Long getByProductCount(BusinessUnit businessUnit,String type);


	long countByProductIdAndTestTypeWithProductdate(Long productId,
			String testType, String productDate) throws DaoException ;
	public List<ProductJGVO> getProductByProducerBusinessUnit(BusinessUnit businessUnit, int page, int pageSize);
	List<ProductJGVO> getProductByCirculationBusinessUnit(BusinessUnit businessUnit, int page, int pageSize);
	
	List<TestRptJson> getThirdList(long productId,int year,String type);
	
	
}
