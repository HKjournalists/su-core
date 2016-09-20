package com.lhfs.fsn.service.testReport.impl;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_REPORT_BACK_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_REPORT_BACK_PATH;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.gettec.fsnip.fsn.dao.market.ResourceTypeDAO;
import com.gettec.fsnip.fsn.dao.product.ProductDAO;
import com.gettec.fsnip.fsn.dao.test.TestResultDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.market.ResourceType;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.ImportedProductTestResult;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.test.TestResultHandler;
import com.gettec.fsnip.fsn.model.test.TestRptJson;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.EnterpriseRegisteService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.FtpService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.market.UpdataReportService;
import com.gettec.fsnip.fsn.service.product.ProductInstanceService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.test.ImportedProductTestResultService;
import com.gettec.fsnip.fsn.service.test.RiskAssessmentService;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.service.test.TestResultHandlerService;
import com.gettec.fsnip.fsn.transfer.TestResultTransfer;
import com.gettec.fsnip.fsn.util.FileUtils;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.util.MKReportNOUtils;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;
import com.gettec.fsnip.fsn.vo.report.ReportBackVO;
import com.gettec.fsnip.fsn.vo.report.ReportOfMarketVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.dao.product.ProductDao;
import com.lhfs.fsn.dao.testReport.TestReportDao;
import com.lhfs.fsn.service.testReport.TestReportService;
import com.lhfs.fsn.util.DateUtil;
import com.lhfs.fsn.vo.BusinessTestResultVO;
import com.lhfs.fsn.vo.ProductInfoVO;
import com.lhfs.fsn.vo.ProductJGVO;
import com.lhfs.fsn.vo.report.ReportVO;
import com.lhfs.fsn.vo.report.ResultToShianjianVO;
@Service
@Transactional
public class TestReportServiceImpl extends BaseServiceImpl<TestResult, TestReportDao> 
		implements TestReportService {
	@Autowired private TestReportDao testReportDao;
	@Autowired protected TestResultDAO testResultDAO;
	@Autowired private ProductDao productDao;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private ProductInstanceService productInstanceService;
	@Autowired protected FtpService ftpService;
	@Autowired private ResourceService testResourceService;
	@Autowired private ResourceTypeDAO resourceTypeDAO;
	@Autowired private ProductDAO productDAO;
	@Autowired private TestPropertyService testPropertyService;
	@Autowired private UpdataReportService updataReportService;
	@Autowired private RiskAssessmentService riskAssessmentService;
	@Autowired private TestResultHandlerService testResultHandlerService;
	@Autowired private ImportedProductTestResultService importedProductTestResultService;
	@Autowired private ProductService productService;
	//@Autowired private ReportService reportService;
	
	private final static Logger log = Logger.getLogger(TestReportServiceImpl.class);
	
	/**
	 * @param product_id 产品ID，不能为空
	 * @param test_report_type 产品类型
	 * @param sn 产品序号，不能为空
	 * @param date 日期，如20130801
	 * 
	 * 修改：添加参数
	 * @param bTr {boolean} true-不需要再去数据库查找检测报告结果testResultList,false-需要去查
	 * @param trList {List} 传入的已经存在检测报告集合
	 * @throws ServiceException 
	 */
	public TestRptJson getReportJson(long product_id, String test_report_type, int sn, String date, boolean bTr, List<TestResult> trList,boolean portalFlag) throws ServiceException {
		try {
			
			TestResult tr = testReportDao.getResultByIdAndType(product_id, test_report_type ,sn,null,portalFlag);
			TestRptJson trj=null;
			if(tr!=null){
				try {
					TestResult _tr=testReportDao.findById(tr.getId());
					tr.setReportImgList(_tr.getReportImgList());
					tr.setCheckImgList(_tr.getCheckImgList());
					tr.setBuyImgList(_tr.getBuyImgList());
				} catch (JPAException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				trj=getReportJson(tr);
				Integer trNum=testReportDao.countByIdAndType(product_id, test_report_type,null,portalFlag);
				trj.setTotal(trNum);
			}
			return trj;
			// 如果已经传入了testResultList，那么就不再去请求数据库了
			/*List<TestResult> testResultList = bTr ? trList : testReportDao.getResultListByIdAndType(product_id, test_report_type);
			TestResult dateTestResult = date == null ? null : testReportDao.getRptByIdAndTypeAndDate(product_id, test_report_type, date);
			
			if(date == null)
				date = "";
			if(test_report_type == null)
				return null;
			if (testResultList == null)
				return null;
			
			if(date != ""){
				if(dateTestResult == null)
					return null;
				sn = testResultList.indexOf(dateTestResult)+1;
			}
			if(sn < 1 || sn > testResultList.size())
				return null;
			TestResult testResult = testResultList.get(sn-1);
			
			*//**
			 * 未结构化检测报告无需返回报告中未结构化的数据<br>
			 * @authorzhangHui 2015/5/8<br>
			 *//*
			String result = "";           // 检测结果描述
			String testDate = "";         // 检验日期
			String sampleQuantity = "";   // 抽样量
			String testee = "--";         // 被检测单位/人
			String samplingLocation = ""; // 抽样地点
			String standard = "";         // 判定依据
			String samplingDate = null;   // 抽样日期
			
			String remark = "";           // 备注
			String instrument = "";       // 仪器
			List<TestProperty> testPropertyList = null;  // 检测项目列表
			boolean isCanViewAllInfo = testResultHandlerService.isCanViewAllInfo(testResult.getId());
			if(isCanViewAllInfo){
				result = testResult.getResult();
				testDate = testResult.getTestDate()==null?null:DateUtil.formatDateToString(testResult.getTestDate());
				sampleQuantity = testResult.getSampleQuantity();
				if(testResult.getTestee() != null){
					testee = testResult.getTestee().getName();
				}
				samplingLocation = testResult.getSamplingLocation();
				standard = testResult.getStandard();
				if(testResult.getSamplingDate() != null){
					
				}
				if(testResult.getSamplingDate() != null){
					samplingDate = DateUtil.formatDateToString(testResult.getSamplingDate());
				}
				//batchSN = 
				remark = testResult.getComment();
				instrument = testResult.getEquipment();
				testPropertyList = testPropertyService.findByReportId(testResult.getId());
			}
			String batchSN = testResult.getSample().getBatchSerialNo();          // 批号/日期
			String sampleName = productDao.findById(product_id).getName();  //样品名称
			String serial = testResult.getSampleNO();   //样品编号
			String tradeMark = testResult.getSample().getProduct().getBusinessBrand().getName(); // 商标
			String enterprise = testResult.getSample().getProducer().getName(); // 生产企业
			String format = testResult.getSample().getProduct().getFormat();    // 规格型号
			String testType = testResult.getTestType(); //检验类别
			String status = testResult.getSample().getProduct().getStatus();    //样品状态
			
			
           若报告是测试院的账号上传的，不显示
			String organizationName = "";
			Long organization = testResult.getOrganization();
			获取交易市场的名称
			String orig_marketName = businessUnitService.getMarketNameByOrganization(organization);
			if(organization!=null && organization!=1L){
				organizationName = orig_marketName + testResult.getOrganizationName(); //所属企业名称
			}
			String pdfUrl = "";
			if(testResult.getFullPdfPath() != null && !testResult.getFullPdfPath().equals(""))
				pdfUrl = testResult.getFullPdfPath();
			String interceptionPdf = "";
			if(testResult.getInterceptionPdfPath()!= null && !testResult.getInterceptionPdfPath().equals(""))
				interceptionPdf = testResult.getInterceptionPdfPath();
			String productionDate ="";
			if(testResult.getSample().getProductionDate()!=null){
				productionDate = DateUtil.formatDateToString(testResult.getSample().getProductionDate());
			}
			int total = testResultList.size();
			*//**
			 * 处理进口食品报告信息
			 *//*
			ImportedProductTestResult impProTestResult=importedProductTestResultService.findByReportId(testResult.getId());
			TestRptJson testRptJson = new TestRptJson(result, testDate, sampleName,
					serial, tradeMark, enterprise,
					null, format, sampleQuantity,
					testee, samplingLocation, standard,
					testType, samplingDate, batchSN, status,
					instrument, remark,
					testPropertyList, pdfUrl, total, sn,interceptionPdf,organizationName);
			testRptJson.setProductionDate(productionDate);
			testRptJson.setImpProTestResult(impProTestResult);
			return testRptJson;*/
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("", e.getException());
		}
	}
    /**
     * lims传来的testResults（检查结果）上传报告
     * @param testResults
     * @return String
     * @author 未知<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/3/12
     */
	@SuppressWarnings("unchecked")
	public String addRpt(JSONObject obj) throws ServiceException {
		try {
			Boolean isBatch = obj.getBoolean("batch");
			JSONObject producerVO = obj.getJSONObject("product").getJSONObject("producer");
			
			List<JSONObject> samples = obj.getJSONArray("samples");
			for(JSONObject sample : samples){
				String sampleNO = sample.getString("sampleNO");
				boolean isExist = getDAO().findBySampleNO(sampleNO);
				if(isExist){
					continue;
				}
				sample = JSONObject.fromObject(sample);
				/* 获取json文件 */
				UploadUtil uploadUtil = new UploadUtil();
				JSONObject itemsJSON = uploadUtil.getJSON(sample.getString("jsonURL")); // 获取该样品的检测项目
				if(itemsJSON == null){
					return "检测项目json文件缺失";
				}
				/* 1. 处理产品实例 */
				ProductInstance sampleProduct = productInstanceService.addSampleProduct(sample, producerVO, isBatch);
				/* 2. 处理被检人信息 */
				JSONObject testeeVO = JSONObject.fromObject(obj.getJSONObject("testee"));
				BusinessUnit testee = getTestee(testeeVO, isBatch, sampleProduct);
				/* 3. 处理报告 */
				createTestResult(obj, itemsJSON, testee, sample, sampleProduct);
			}
			log.info("解析数据成功");
			return "success";
		} catch (ServiceException sex) {
			throw new ServiceException("TestReportServiceImpl.addRpt()-->" + sex.getMessage(), sex.getException());
		}
	}
	/**
	 * 创建报告
	 * @param obj
	 * @param itemsJSON
	 * @param testee
	 * @param sample
	 * @param sampleProduct
	 * @throws ServiceException
	 * @author ZhangHui<br>
	 * 最后更新者：ZhangHui<br>
	 * 最后更新时间：2015/3/13
	 */
	@SuppressWarnings("deprecation")
	
	private void createTestResult(JSONObject obj, JSONObject itemsJSON, BusinessUnit testee, 
				JSONObject sample, ProductInstance sampleProduct) throws ServiceException {
		try {
			TestResult report = new TestResult();
			String edition = obj.getString("edition");
			report.setEdition(edition);
			report.setOrganization(obj.getLong("organizationID"));
			
			JSONObject testResult = itemsJSON.getJSONObject("testResult");
			/* 抽样日期 */
			Date samplingDate = null; 
			String sampStr = testResult.getString("samplingDate");
			if(!sampStr.equals("") && !sampStr.equals("null") ){
				samplingDate = new Date(sampStr.replace('-', '/').replace('T', ' ').replace(".000Z", ""));					
			}
			report.setServiceOrder(obj.getString("serviceOrder"));
			report.setSamplingDate(samplingDate);
			/* 检测日期 */
			Date testDate = null;
			String testStr = testResult.getString("testDate");
			if(!testStr.equals("") && !testStr.equals("null")){
				testDate = new Date(testStr.replace('-', '/').replace('T', ' ').replace(".000Z", ""));					
			}
			report.setTestDate(testDate);
			/* 抽样数量 */
			report.setSampleQuantity(testResult.getString("sampleQuantity")); 
			/* 抽样地点 */
			report.setSamplingLocation(testResult.getString("samplingLocation"));
			/* 检验类别 */
			String testType = obj.getString("testType");
			if(testType.equals("云平台委托检测")){
				report.setTestType("第三方检测");
			}else{
				report.setTestType(testType);
			}
			report.setPublishFlag('0');
			/* 主要仪器 */
			report.setEquipment(testResult.getString("equipment"));
			/* 执行标准 */
			report.setStandard(testResult.getString("standard"));
			/* 检测结果描述 */
			report.setResult(testResult.getString("result"));
			/* 检验结论 */
			report.setPass(testResult.getString("comment").equals("Pass"));
			report.setComment(testResult.getString("comment")); //  备注
			report.setApproveBy(testResult.getString("approveBy"));
			report.setAuditBy(testResult.getString("auditBy"));
			report.setKeyTester(testResult.getString("keyTester"));
			report.setSample(sampleProduct);
			report.setTestee(testee);
			String pdfURL = sample.getString("pdfURL");
			/* 报告编号不允许包含中文 */
			String serviceOrder = obj.getString("serviceOrder");
			String serviceOrderEng = MKReportNOUtils.convertCharacter(serviceOrder, "");
			/* 上传pdf */
			UploadUtil uploadUtil = new UploadUtil();
			Map<String,String> map = uploadUtil.uploadReportPdf(edition, pdfURL, serviceOrderEng, testType);
			if(map == null||"nullPdf".equals(map.get("msg"))){
				throw new ServiceException();
			}
			
			pdfURL = map.get("pdfPath");
			/* 政府抽检的截取前两页pdf路径；企业自检和企业送检均与fullPdfPath的值一致 */
			report.setInterceptionPdfPath(map.get("interceptionPdf"));
			/* 完整的没有截取的报告路径 */ 
			report.setFullPdfPath(pdfURL);
			report.setReceiveDate(new Date());
			report.setEdition(edition);
			report.setDbflag("receive_from_lims");
			report.setSampleNO(sample.getString("sampleNO"));
			create(report);
			/* 处理检测项目 */  
			Long testResultId = report.getId();
			createTestProperties(testResultId, testResult);
		} catch (ServiceException sex) {
			throw new ServiceException("TestReportServiceImpl.createTestResult()-->" + sex.getMessage(), sex.getException());
		}
	}
	/**
	 * 获取被检测单位
	 * @param isBatch 是否为批次
	 * @param sampleProduct 产品实例
	 * @return BusinessUnit(不可能为空)
	 * @throws ServiceException
	 * @author ZhangHui<br>
	 * 最后更新者：ZhangHui<br>
	 * 最后更新时间：2015/3/13
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private BusinessUnit getTestee(JSONObject testeeVO, Boolean isBatch, ProductInstance sampleProduct) throws ServiceException {
		BusinessUnit testee = null;
		if(isBatch){
			/* 1.1是批次就保存lims传来得被检人信息*/
			String testeeName = testeeVO.getString("name");
			if(!testeeName.trim().equals("")){
				BusinessUnit orig_testee = businessUnitService.findByName(testeeName);
				testee = orig_testee;
				if(orig_testee == null){
					testee=new BusinessUnit(testeeVO.getString("name"));
					businessUnitService.create(testee);
				}
			}
		}else if(sampleProduct!=null && sampleProduct.getProduct()!=null && sampleProduct.getProduct().getBusinessBrand()!=null){
			/* 1.2不是批次就从产品实例中获取被检人*/
			testee = sampleProduct.getProduct().getBusinessBrand().getBusinessUnit();
		}
		/* 默认值 */
		if(testee == null){
			testee = businessUnitService.findByName("--");
		}
		return testee;
	}
	
	public TestRptJson getReportJson(long id, String test_report_type,
			String date) throws ServiceException {
		try {
			TestResult testResult = testReportDao.getRptByIdAndTypeAndDate(id, test_report_type, date);
			if(testResult == null)
				return null;
			String result = testResult.getResult(); //检测结果
//			Date testDate = testResult.getTestDate(); //检测日期
			String testDate = testResult.getTestDate()==null?null:DateUtil.formatDateToString(testResult.getTestDate());
			String sampleName = productDao.findById(id).getName(); //样品名称
			String serial = testReportDao.getSerial(testResult.getSample().getId()); //样品编号
			//String tradeMark = testReportDao.getTradeMark(testResult.getBrand().getId()); //商标
			String tradeMark=testResult.getSample().getProduct().getBusinessBrand().getName();//null==testResult.getBrand()?"--":testResult.getBrand().getName();
			String enterprise = testResult.getSample().getProducer().getName(); //生产企业
			String samplingUnit = null; //抽样单位
			String format = testReportDao.getFormat(testResult.getSample().getId()); //规格型号
			String sampleQuantity = testResult.getSampleQuantity(); //抽样量
			//String testee = testReportDao.getTestee(testResult.getTestee().getId()); //受检单位
			String testee=null==testResult.getTestee()?"--":testResult.getTestee().getName();
			String samplingLocation = testResult.getSamplingLocation(); //抽样地点
			String standard = testResult.getStandard(); //判定依据
			String testType = testResult.getTestType(); //抽验类型
//			Date samplingDate = testResult.getSamplingDate(); //抽样日期
			String samplingDate = testResult.getSamplingDate()==null?null:DateUtil.formatDateToString(testResult.getSamplingDate()); //抽样日期
			String batchSN = testResult.getSample().getBatchSerialNo(); //批号/日期
			String status = testReportDao.getStatus(testResult.getSample().getId()); //样品状态
			String instrument = testResult.getEquipment(); //仪器
			String remark = testResult.getComment(); //备注
			/*若报告是测试院的账号上传的，不显示*/
			String organizationName = "";
			Long organization = testResult.getOrganization();
			/*获取交易市场的名称*/
			String orig_marketName = businessUnitService.getMarketNameByOrganization(organization);
			if(organization!=null && organization!=1l){
				organizationName = orig_marketName + testResult.getOrganizationName(); //所属企业名称		
			}
			String productionDate = testResult.getSample().getProductionDate().toString().substring(0, 10);
			String pdfUrl = "";
			if(testResult.getFullPdfPath()!=null)
				pdfUrl = testResult.getFullPdfPath();
			String interceptionPdf = "";
			if(testResult.getInterceptionPdfPath() != null && !testResult.getInterceptionPdfPath().equals(""))
				interceptionPdf = testResult.getInterceptionPdfPath();
			List<TestProperty> testPropertyList = testReportDao.getPropertyListByID(testResult.getId());	
			TestRptJson testRptJson = new TestRptJson(result, testDate, sampleName,
					serial, tradeMark, enterprise,
					samplingUnit, format, sampleQuantity,
					testee, samplingLocation, standard,
					testType, samplingDate, batchSN, status,
					instrument, remark,
					testPropertyList, pdfUrl, 0, 0,interceptionPdf,organizationName);
			testRptJson.setProductionDate(productionDate);
			return testRptJson;
		} catch (JPAException jpae) {
			throw new ServiceException("", jpae.getException());
		}
	}

	@Override
	public String getIDsByProductInstanceID(Long id) {
		String ids = "";
		List<BigInteger> longIDs = testReportDao.getIDsByProductInstanceID(id);
		if(longIDs != null && longIDs.size()>0){
			for(BigInteger tmpid : longIDs){
				ids = ids.concat(":").concat(tmpid.toString());
			}
		}
		return ids;
	}
	
	
	
	/**
	 * 按产品实例id查找报告条数
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public long countBySampleId(Long sampleId) throws ServiceException {
		try {	
			String condition = " WHERE e.sample.id = ?1 AND e.del = 0 ";
			Object[] params = new Object[]{sampleId};
			return testReportDao.count(condition, params);
		} catch (JPAException jpae) {
			throw new ServiceException("TestReportServiceImpl.countBySampleId()-->" + jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 按组织机构和用户名查找报告总条数（已发布或未发布）
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public long countByOrgIdAndUserRealName(Long organizationId, String userRealName, 
			char pubFlag, String configure) throws ServiceException {
		try {
			Map<String, Object> map;
			map = getConfigure(organizationId, userRealName, pubFlag, '0', configure, false, null);
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			return testReportDao.count(condition, params);
		} catch (JPAException jpae) {
			throw new ServiceException("TestReportServiceImpl.countByOrgIdAndUserRealName()-->" + jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 查找商超所有已经通过商超审核的报告数量
	 * @author ZhangHui 2015/4/14
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public long countOfDealerAllPass(String organizationName, String userRealName, 
			char pubFlag, String configure) throws ServiceException {
		try {
			/*Map<String, Object> map;
			map = getConfigure(null, null, pubFlag, '0', configure, true, organizationName);
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");*/
			 String new_configure=" WHERE DATEDIFF(ip.expiration_date , SYSDATE()) > 0 AND tr.del = 0  AND tr.publish_flag IN ('0','1','2','3','6') AND tr.organization_name = '"+organizationName+"' ";
			 String config = getSCPassConfigure(configure,new_configure);
			return testReportDao.countOfDealerAllPass(config);
		} catch (DaoException jpae) {
			throw new ServiceException("TestReportServiceImpl.countByOrgIdAndUserRealName()-->" + jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 查找商超所有已经通过商超审核的报告
	 * @author ZhangHui 2015/4/14
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<TestResult> getReportsOfDealerAllPassWithPage(String organizationName, String userName, int page,
			int pageSize, char pubFlag, String configure) throws ServiceException {
		try {
			 List<TestResult> reports = null;
			 String new_configure=" WHERE DATEDIFF(ip.expiration_date , SYSDATE()) > 0 AND tr.del = 0  AND tr.publish_flag IN ('0','1','2','3','6') AND tr.organization_name = '"+organizationName+"' ";
			 String config = getSCPassConfigure(configure,new_configure);
			 config+=" order by tr.last_modify_time desc " ;
			//reports =  getDAO().getListByConditionWithPage(pubFlag, page, pageSize, map);
			reports=getDAO().getReportsOfDealerAllPassWithPage(config, page, pageSize);
			if(reports == null){return null;}
			return reports;
		}catch(DaoException dex){
			throw new ServiceException("TestReportServiceImpl.getReportsByOrgIdAndUserRealNameWithPage()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	
	/**
	 * 供应商查看商超已审核通过的报告过滤专用方法
	 * @author longxianzhen 20150807
	 */
	private String getSCPassConfigure( String configure,String new_configure) {	
		
		if(configure != null && !"null".equals(configure)){
			String filter[] = configure.split("@@");
			for(int i=0;i<filter.length;i++){
				String filters[] = filter[i].split("@");
				String config = splitSCJointConfigure(filters[0],filters[1],filters[2]);
				if(config==null){
					continue;
				}
				new_configure = new_configure + " AND " + config;
			}
		}
	    return new_configure;
	}
	
	private String splitSCJointConfigure(String field,String mark,String value) {
		try {
			if(field.equals("serviceOrder")){
				return FilterUtils.getConditionStr(" tr.service_order ",mark,value,true);
			}
			if(field.equals("sample_batchSerialNo")){
				return FilterUtils.getConditionStr(" ip.batch_serial_no ",mark,value,true);
			}
			if(field.equals("sample_product_name")){
				return FilterUtils.getConditionStr(" p.name ",mark,value,true);
			}
			if(field.equals("sample_product_barcode")){
				return FilterUtils.getConditionStr(" p.barcode ",mark,value,true);
			}
			if(field.equals("backResult")){
				return FilterUtils.getConditionStr(" tr.back_result ",mark,value,true);
			}
			if(field.equals("lastModifyUserName")){
				return FilterUtils.getConditionStr(" tr.last_modify_user ",mark,value,true);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 按组织机构和用户名查找报告列表（已发布或未发布）
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<TestResult> getReportsByOrgIdAndUserRealNameWithPage(Long organizationId, String userRealName,
			int page, int pageSize, char pubFlag, String condition) throws ServiceException{
		try {
			List<TestResult> reports = null;
			Map<String, Object> map;
			map = getConfigure(organizationId, userRealName, pubFlag, '0', condition, false, null);
			reports =  testReportDao.getListByConditionWithPage(pubFlag, page, pageSize, map);
			if(reports == null){return null;}
			TestResultTransfer.transfer(reports);
			return reports;
		}catch(DaoException dex){
			throw new ServiceException("TestReportServiceImpl.getReportsByOrgIdAndUserRealNameWithPage()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 对报告更改消息提示
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TestResult editTips(Long repId,String tips) throws ServiceException{
		try {
			TestResult report = findById(repId);
			report.setTips(tips);
			return update(report);
		} catch (ServiceException sex) {
			throw new ServiceException("TestReportServiceImpl.editTips()-->" + sex.getMessage(), sex.getException());
		}
	}
	
	/**
	 * 功能描述：删除报告<br>
	 * 最后更新者：Zhanghui 2015/6/17<br>
	 * 最后更新内容：报告删除用假删除
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteTestReport(ResultVO resultVO, Long reportId) throws ServiceException {
		try {
			TestResult orig_report = findById(reportId);
			if(orig_report.getPublishFlag()=='0' || orig_report.getPublishFlag()=='1'){
				resultVO.setShow(true);
				resultVO.setErrorMessage("此报告已经发布，不允许删除！");
				return;
			}
			
			/* 删除此报告的检测项目计算出风险指数的历史数据 */
			riskAssessmentService.deletRiskAssess(reportId);
			
			/* 3. 删除该报告 */
			getDAO().updateByDel(reportId, 1);
		} catch (DaoException e) {
			throw new ServiceException("[DaoException]TestReportServiceImpl.deleteTestReport()-->" + e.getMessage(), e.getException());
		} catch (ServiceException e) {
			throw new ServiceException("[ServiceException]TestReportServiceImpl.deleteTestReport()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 按当前用户的组织机构名称或用户真实姓名，获取所有退回报告总条数。
	 * @throws ServiceException 
	 */
	public Long countByOrgNameAndUserRealNameAndBackFlag(Long organizationId, String userRealName,
			char pubFlag, String configure) throws ServiceException{
		try {
			Map<String, Object> map = getConfigure(organizationId, userRealName, pubFlag, '0', configure, false, null);
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			return testReportDao.count(condition, params);
		} catch (JPAException jpae) {
			throw new ServiceException("TestReportServiceImpl.countByOrgNameAndUserRealNameAndBackFlag()-->" + jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 获取有pdf的可发布的报告总条数
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public long countByIsCanPublish(Long organizationId, char pubFlag, String condition) throws ServiceException {
		try {
			return testReportDao.countByConditionIsCanPublish(getConfigure(organizationId, null, pubFlag, '0', condition, false, null));
		} catch (DaoException dex) {
			throw new ServiceException("TestReportServiceImpl.countByIsCanPublish()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 获取可以发布的报告列表（此报告必须有pdf）
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<TestResult> getListByIsCanPublish(Long organizationId, int page, int pageSize, 
			char pubFlag, String condition) throws ServiceException{
		try {
			List<TestResult> testResultList= testReportDao.getListByIsHavePdfWithPage(page, pageSize, getConfigure(organizationId,
						null, pubFlag, '0', condition, false, null));
			TestResultTransfer.transfer(testResultList);
			return testResultList;
		} catch(DaoException dex) {
			throw new ServiceException("TestReportServiceImpl.getListByIsCanPublish()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 功能描述：报告唯一性检查（报告编号、产品barcode、批次）
	 * @return  true  代表 当前报告是唯一的
	 * 			false 代表 已经存在类似的报告
	 * @throws DaoException
	 * @author ZhangHui 2015/6/5
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public boolean checkUniquenessOfReport(Long reportId, String serviceOrder,String barcode,String batchNo) throws ServiceException{
		try{
			return testReportDao.checkUniquenessOfReport(reportId, serviceOrder,barcode,batchNo);
		}catch(DaoException e){
			throw new ServiceException("TestReportService.checkUniquenessOfReport()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * add files（用于页面图片上传控件）
	 * @throws ServiceException 
	 */
	@Override
	public List<Resource> addTestResources(Collection<Resource> resources) throws ServiceException{
		try {
			List<Resource> resource = new ArrayList<Resource>();
			if(resources != null){
				for(Resource rs : resources){
					this.updateResource(rs);
					if(rs.getType() == null){
						continue;
					}
					resource.add(rs);
				}
			}
			return resource;
		} catch (ServiceException sex) {
			throw new ServiceException("TestReportServiceImpl.addTestResources()-->" + sex.getMessage(), sex.getException());
		}
	}
	
	/**
	 * 用于页面上传图片
	 * @param rs
	 * @throws ServiceException 
	 */
	private void updateResource( Resource rs) throws ServiceException{
		try {
			if(rs == null) return;
			ResourceType type = this.resourceTypeDAO.findByName(FileUtils.getExtension(rs.getFileName()));
			if(type != null){
				rs.setType(type);
			}
		} catch (DaoException dex) {
			throw new ServiceException("TestReportServiceImpl.updateResource()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 按检测项目的不同列自动加载数据
	 * @throws ServiceException 
	 */
	@Override
	public List<String> autoTestItems(int columeId,String keyword,int page,int pageSize) throws ServiceException{
		try {
			return testPropertyService.getListOfColumeValue(columeId,keyword,page,pageSize);
		} catch (ServiceException sex) {
			throw new ServiceException("TestReportServiceImpl.autoTestItems()-->" + sex.getMessage(), sex.getException());
		}
	}
	
	/**
	 * 发布人员将报告发布至testlab成功后，更改报告publishFlag为'0'。
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void publishToTestLab(Long reportId, String tempProductUrl, AuthenticateInfo info) throws ServiceException {
		try {
			TestResult orig_report = testReportDao.findById(reportId);
			orig_report.getSample().getProduct().setImgUrl(tempProductUrl);
			orig_report.setPublishFlag('0');
			orig_report.setReceiveDate(new Date());
			orig_report.setPubUserName(info.getUserName());
			//orig_report.setOrganizationName(info.getUserOrgName());
			testReportDao.merge(orig_report);
		} catch (JPAException jpae) {
			throw new ServiceException("TestReportServiceImpl.publishToTestLab()-->" + jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * testlab退回到发布人员：将publishFlag改为'2'
	 * @param reportId
	 * @param backMessage
	 * @return void
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void goBack(Long reportId, ReportBackVO reportBackVO) throws ServiceException {
		try {
			TestResult orig_report = testReportDao.findById(reportId);
			orig_report.setPublishFlag('2');
			
			/**
			 * 如果是供应商的进口食品则直接退回到供应商
			 * @author longxianzhen 2015/06/05
			 */
			ImportedProductTestResult impTest=importedProductTestResultService.findByReportId(reportId);
			if("dealer".equals(orig_report.getDbflag())&&impTest!=null){
				orig_report.setPublishFlag('5');
			}
			orig_report.setBackTime(new Date());
			orig_report.setBackResult(reportBackVO.getReturnMes());
			UploadUtil uploadUtil=new UploadUtil();
			for(Resource r:reportBackVO.getRepBackAttachments()){
				r.setFileName(uploadUtil.createFileNameByDate(r.getFileName()));
				r.setName(r.getFileName());
				uploadUtil.uploadFile(r.getFile(), PropertiesUtil.getProperty(FSN_FTP_UPLOAD_REPORT_BACK_PATH), r.getFileName());
				if(UploadUtil.IsOss()){
					r.setUrl(uploadUtil.getOssSignUrl( PropertiesUtil.getProperty(FSN_FTP_UPLOAD_REPORT_BACK_PATH)+"/"+ r.getFileName()));
				}else{
					r.setUrl(PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_REPORT_BACK_PATH)+"/"+r.getFileName());
				}
			}
			orig_report.setRepBackAttachments(reportBackVO.getRepBackAttachments());
			testReportDao.merge(orig_report);
			updataReportService.changeApplyReportStatus(orig_report.getSample().getProduct().getBarcode(),orig_report.getTestType(),1);
		} catch (JPAException jpae) {
			throw new ServiceException("TestReportServiceImpl.goBack()-->" + jpae.getMessage(), jpae);
		}
	}
	
	/**
	 * testlab从portal撤回：将publishFlag改为'0'
	 * @param reportId
	 * @return boolean
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean goBack(Long reportId) throws ServiceException {
		try {
			TestResult orig_report = testReportDao.findById(reportId);
			orig_report.setPublishFlag('0');
			testReportDao.merge(orig_report);
			return true;
		} catch (JPAException jpae) {
			return false;
		}
	}
	
	/**
	 * 在发布报告之前，验证此报告的产品是否包含产品图片
	 * @throws ServiceException
	 */
	@Override
	public boolean validatHaveProJpg(Long reportId) throws ServiceException {
		try {
			TestResult orig_report = testReportDao.findById(reportId);
			return orig_report.getSample().getProduct().getProAttachments().size()>0;
		} catch (JPAException jpae) {
			throw new ServiceException("TestReportServiceImpl.validatHaveProJpg()-->" + jpae.getMessage(), jpae);
		}
	}
	
	/**
	 * 按报告id查找该报告是否已经签名
	 */
	@Override
	public boolean getSignFlag(Long reportId) throws ServiceException {
		try {
			return testReportDao.findById(reportId).isSignFlag();
		} catch (JPAException jpae) {
			throw new ServiceException("TestReportServiceImpl.getSignFlag()-->" + jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 按报告id，签名后更新pdf url
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updatePdfUrl(Long id, String pdfUrl) throws ServiceException {
		try {
			TestResult orig_report = testReportDao.findById(id);
			orig_report.setFullPdfPath(pdfUrl);
			orig_report.setInterceptionPdfPath(pdfUrl);
			testReportDao.merge(orig_report);
		} catch (JPAException jpae) {
			throw new ServiceException("TestReportServiceImpl.updatePdfUrl()-->" + jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 按报告id更改签名标识
	 * @param reportId 报告id
	 * @param input pdf签名成功后的输入流
	 * @return void
	 * @throws ServiceException
	 * @author Zhanghui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveAfterSign(Long reportId, ByteArrayInputStream input) throws ServiceException {
		try {
			TestResult report = findById(reportId);
			report.getRepAttachments().clear();
			/* 1. 将签名后的pdf与报告的关联保存到数据库中 */
			testResourceService.saveAutoPdfResource(input, report);
			/* 2. 更改报告的签名状态为“已签名” */
			changeSignFlag(reportId, true);
		} catch (ServiceException sex) {
			throw new ServiceException("TestReportServiceImpl.saveAfterSign()-->" + sex.getMessage(), sex.getException());
		}
	}
	
	/**
	 * 按报告id更改签名标识
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TestResult changeSignFlag(Long reportId, boolean signFlag) throws ServiceException {
		try {
			TestResult report = testReportDao.findById(reportId);
			report.setSignFlag(signFlag);
			testReportDao.merge(report);
			return report;
		} catch (JPAException jpae) {
			throw new ServiceException("TestReportServiceImpl.changeSignFlag()-->" + jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 功能描述：查找当前登录用户通过解析pdf增加的报告数量。
	 * @throws ServiceException 
	 * @author ZhangHui 2015/6/18
	 */
	@Override
	public long countOfPasePdf(String userName, String configure) throws ServiceException {
		try {
			return testReportDao.countOfPasePdf(userName, getConfigure(null, null, '3', '4', configure, false, null));
		} catch (DaoException dex) {
			throw new ServiceException("TestReportServiceImpl.countByUserNameAndMkdbflag()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 功能描述：查找用户通过解析pdf生成的报告
	 * @author ZhangHui 2015/6/18
	 * @throws ServiceException 
	 */
	@Override
	public List<TestResult> getListOfPasePdfByPage(String userName, int page, int pageSize, String configure) 
			throws ServiceException {
		try {
			return testReportDao.getListOfPasePdfByPage(userName, 
					page, pageSize, getConfigure(null, null, '3', '4', configure, false, null));
		} catch (DaoException dex) {
			throw new ServiceException("TestReportServiceImpl.getListByByUserNameAndMkdbflagWithPage()-->" + dex.getMessage(),dex.getException());
		}
	}
	
	/**
	 * 按以下四个字段信息拼接where字符串
	 * @param realUserName 
	 * @param configure 页面过滤条件
	 * @param mkPubFlag 是否发布
	 * @param backFlag 是否退回
	 * @param dbFlag 数据源
	 * @param flag 内部开关标记
	 * @return
	 */
	private Map<String, Object> getConfigure(Long organizationId, String realUserName, 
			char pubFlag, char dbFlag, String condition, boolean isLoadAllDealerOfPass, String organizationName) throws ServiceException{
		try {
			String new_configure = getConfigure(condition);
			
			Object[] params = null;
			params = new Object[3];
			if(pubFlag == '1'){
				new_configure += " AND publishFlag IN ('0',?1)";
			}else if(isLoadAllDealerOfPass && pubFlag == '6'){
				new_configure += " AND publishFlag IN ('0','1','2','3',?1)";
			}else{
				new_configure += " AND publishFlag = ?1 AND  DATEDIFF(e.sample.expirationDate, SYSDATE()) > 0 ";
			}
			params[0] = pubFlag;
			
			if(!isLoadAllDealerOfPass && pubFlag == '6'){
				organizationId = null;
				realUserName = null;
			}
			
			int index = 2;
			if(dbFlag == '2'){
				new_configure += " AND mkDbFlag = ?2";
				params[1] = dbFlag;
				index = 3;
			}
			
			if(organizationId !=null){
				new_configure += " AND organization = ?" + index;
				params[index-1] = organizationId;
				index += 1; 
			}
			if(realUserName !=null){
				new_configure += " AND lastModifyUserName = ?" + index;
				params[index-1] = realUserName;
				index += 1; 
			}
			if(organizationName != null){
				new_configure += " AND organizationName = ?" + index;
				params[index-1] = organizationName;
			}
			
		    Map<String, Object> map = new HashMap<String, Object>();
			map.put("condition", new_configure);
			map.put("params", params);
		    return map;
		} catch (ServiceException sex) {
			throw new ServiceException("TestReportServiceImpl.getConfigure()-->" + sex.getMessage(), sex.getException());
		} catch (Exception e) {
			throw new ServiceException("TestReportServiceImpl.getConfigure() 出现异常！",e);
		}
	}
	
	private String getConfigure(String condition) throws ServiceException{
		try {
			String new_configure = " WHERE del = 0 ";
			
			if(condition != null && !"null".equals(condition)){
				String filter[] = condition.split("@@");
				for(int i=0;i<filter.length;i++){
					String filters[] = filter[i].split("@");
					String config = splitJointConfigure(filters[0],filters[1],filters[2]);
					if(config==null){
						continue;
					}
					new_configure = new_configure + " AND " + config;
				}
			}
			
		    return new_configure;
		} catch (Exception e) {
			throw new ServiceException("TestReportServiceImpl.getConfigure() 出现异常！",e);
		}
	}
	
	private String splitJointConfigure(String field,String mark,String value) throws ServiceException{
		try {
			//value = URLDecoder.decode(value, "utf-8");
			if(field.equals("id")){
				return FilterUtils.getConditionStr("id",mark,value);
			}
			if(field.equals("serviceOrder")){
				return FilterUtils.getConditionStr("serviceOrder",mark,value,true);
			}
			if(field.equals("sample_productionDate")){
				return FilterUtils.getConditionTime("sample.productionDate",mark,value);
			}
			if(field.equals("sample_batchSerialNo")){
				return FilterUtils.getConditionStr("sample.batchSerialNo",mark,value,true);
			}
			if(field.equals("testOrgnization")){
				return FilterUtils.getConditionStr("testOrgnization",mark,value,true);
			}
			if(field.equals("lastModifyUserName")){
				return FilterUtils.getConditionStr("lastModifyUserName",mark,value,true);
			}
			if(field.equals("backResult")){
				return FilterUtils.getConditionStr("backResult",mark,value,true);
			}
			if(field.equals("tips")){
				return FilterUtils.getConditionStr("tips",mark,value,true);
			}
			if(field.equals("sample_product_name")){
				return FilterUtils.getConditionStr("sample.product.name",mark,value);
			}
			if(field.equals("testType")){
				return FilterUtils.getConditionStr("testType",mark,value);
			}
			if(field.equals("mkPublishFlag")){
				if(value.equals("发布")){
					value = "1";
				}else if(value.equals("未发布")){
					value = "0";
				}
				return FilterUtils.getConditionStr("mkPublishFlag",mark,value);
			}
			return null;
		/*} catch (UnsupportedEncodingException e) {
			throw new ServiceException(" TestReportServiceImpl.splitJointConfigure()中 URLDecoder.decode执行出现异常！",e);*/
		} catch (Exception e){
			throw new ServiceException(" TestReportServiceImpl.splitJointConfigure() 出现异常！", e);
		}
	}
	
	/**
	 * 根据产品id和检测类别统计产品的报告数量
	 */
	@Override
	public long countByProductIdAndTestType(Long productId, String testType)
			throws ServiceException {
		try{
			return testReportDao.countByProductIdAndTestType(productId, testType);
		}catch(DaoException dex){
			throw new  ServiceException("TestReportServiceImpl.countByProductIdAndTestType()-->" + dex.getMessage(), dex.getException());
		}
	}
	/**
	 * 根据产品id和检测类别统计产品的报告数量
	 */
	@Override
	public long countByProductIdAndTestTypeWithProductdate(Long productId, String testType,String productDate)
			throws ServiceException {
		try{
			return testReportDao.countByProductIdAndTestTypeWithProductdate(productId, testType, productDate);
		}catch(DaoException dex){
			throw new  ServiceException("TestReportServiceImpl.countByProductIdAndTestTypeWithProductdate()-->" + dex.getMessage(), dex.getException());
		}
	}
	public List<TestRptJson> getThirdList(long productId,int year,String type){
		return testReportDao.getThirdList(productId, year,type);
	}

	/**
	 * 根据报告id查找businessId
	 */
	@Override
	public Long getBusIdBytestReportId(Long id) throws ServiceException {
		try{
			return testReportDao.getBusIdBytestReportId(id);
		}catch(DaoException dex){
			throw new  ServiceException("TestReportServiceImpl.getBusIdBytestReportId()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 根据产品id查找距离指定生产日期最近的报告
	 */
	@Override
	public ResultToShianjianVO findByProductIdAndproductionDate(Long proId, String date)
			throws ServiceException {
		try{
		    ResultToShianjianVO resultVO =  new ResultToShianjianVO();
			TestResult selfReport = testReportDao.findByProductIdAndproductionDate(proId, date, "企业自检");// 自检报告
			TestResult inspectReport = testReportDao.findByProductIdAndproductionDate(proId, date,"企业送检");// 送检报告
			TestResult casualReport = testReportDao.findByProductIdAndproductionDate(proId, date,"政府抽检");// 抽检报告
			if(selfReport!=null){
			    resultVO.setSelfReport(new ReportVO(selfReport));
			}
			if(inspectReport!=null){
			    //inspectReportVO =new ReportVO(inspectReport);
			    resultVO.setInspectReport(new ReportVO(inspectReport));
			}
			if(casualReport!=null){
			    resultVO.setCasualReport(new ReportVO(casualReport));
			}
			
			return resultVO;
		}catch(DaoException dex){
			throw new  ServiceException("TestReportServiceImpl.findByProductIdAndproductionDate()-->" + dex.getMessage(), dex.getException());
		}
	}

	@Override
	public TestReportDao getDAO() {
		return testReportDao;
	}

	//hy
	@Override
	public Model getReportBySampleId(Long id,String batch, Model model) throws ServiceException {
		try{
			Product product = productDAO.findById(id);
			List<Long> instanceIds = productInstanceService.getInstanceIdForProductIdAndbatch(batch, id);
			List<ReportVO> reportVOSelfList = new ArrayList<ReportVO>();
	        List<ReportVO> reportVOSampleList = new ArrayList<ReportVO>();
	        List<ReportVO> reportVOcensorList = new ArrayList<ReportVO>();
			for(int i= 0 ; i < instanceIds.size();i++){//
				Object iId = instanceIds.get(i);
				List<TestResult> testResults = testReportDao.getReportBySampleId(iId);
				if(testResults!=null){
					for(TestResult testResult:testResults){
						ReportVO reportVO = new ReportVO();
						if(testResult != null && testResult.getTestType().equals("企业自检")){
								reportVO.setId(testResult.getId());
								reportVO.setTestType(testResult.getTestType());
								reportVO.setServiceOrder(testResult.getServiceOrder());
								reportVO.setPdfUrl(testResult.getFullPdfPath());
								reportVO.setTestDate(testResult.getTestDate());//报告检查时间
								reportVO.setExpireDate(testResult.getEndDate());// 报告过期时间
								reportVOSelfList.add(reportVO);
			           	  }else if(testResult != null && testResult.getTestType().equals("政府抽检")){
			           		reportVO.setId(testResult.getId());
			           		reportVO.setTestType(testResult.getTestType());
			           		reportVO.setServiceOrder(testResult.getServiceOrder());
			           		reportVO.setPdfUrl(testResult.getFullPdfPath());
			           		reportVO.setTestDate(testResult.getTestDate());//报告检查时间
							reportVO.setExpireDate(testResult.getEndDate());// 报告过期时间
			           		reportVOSampleList.add(reportVO);
			           	  }else if(testResult != null && testResult.getTestType().equals("企业送检")){
			           		reportVO.setId(testResult.getId());
			           		reportVO.setTestType(testResult.getTestType());
			           		reportVO.setServiceOrder(testResult.getServiceOrder());
			           		reportVO.setPdfUrl(testResult.getFullPdfPath());
			           		reportVO.setTestDate(testResult.getTestDate());//报告检查时间
							reportVO.setExpireDate(testResult.getEndDate());// 报告过期时间
			           		reportVOcensorList.add(reportVO);
			           	  }
						}
				}
				
			}
			model.addAttribute("product", product==null?new Product():product);
			model.addAttribute("selfData", reportVOSelfList==null?"":reportVOSelfList);
            model.addAttribute("sampleData", reportVOSampleList==null?"":reportVOSampleList);
            model.addAttribute("censorData", reportVOcensorList==null?"":reportVOcensorList);
			return model;
		}catch(DaoException dex){
			throw new  ServiceException("TestReportServiceImpl.getReportBySampleId()-->" + dex.getMessage(), dex.getException());
		}catch(JPAException dex){
			throw new  ServiceException("TestReportServiceImpl.getReportBySampleId()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 获取产品实例的对应报告
	 */
	@Override
	public Map<String, List<ReportVO>> getInstanceReport(Long id, String batch) throws ServiceException {
		try{
			List<Long> instanceIds = productInstanceService.getInstanceIdForProductIdAndbatch(batch, id);
			List<ReportVO> reportVOSelfList = new ArrayList<ReportVO>();
	        List<ReportVO> reportVOSampleList = new ArrayList<ReportVO>();
	        List<ReportVO> reportVOcensorList = new ArrayList<ReportVO>();
			for(int i= 0 ; i < instanceIds.size();i++){//
				Object iId = instanceIds.get(i);
				List<TestResult> testResults = testReportDao.getReportBySampleId(iId);
				if(testResults!=null){
					for(TestResult testResult:testResults){
						ReportVO reportVO = new ReportVO();
						if(testResult != null && testResult.getTestType().equals("企业自检")){
								reportVO.setId(testResult.getId());
								reportVO.setTestType(testResult.getTestType());
								reportVO.setServiceOrder(testResult.getServiceOrder());
								reportVO.setPdfUrl(testResult.getFullPdfPath());
								reportVO.setTestDate(testResult.getTestDate());//报告检查时间
								reportVO.setExpireDate(getReportExprieDate(testResult.getEndDate()));// 报告过期时间
								reportVOSelfList.add(reportVO);
			           	  }else if(testResult != null && testResult.getTestType().equals("政府抽检")){
			           		reportVO.setId(testResult.getId());
			           		reportVO.setTestType(testResult.getTestType());
			           		reportVO.setServiceOrder(testResult.getServiceOrder());
			           		reportVO.setPdfUrl(testResult.getFullPdfPath());
			           		reportVO.setTestDate(testResult.getTestDate());//报告检查时间
							reportVO.setExpireDate(getReportExprieDate(testResult.getEndDate()));// 报告过期时间
			           		reportVOSampleList.add(reportVO);
			           	  }else if(testResult != null && testResult.getTestType().equals("企业送检")){
			           		reportVO.setId(testResult.getId());
			           		reportVO.setTestType(testResult.getTestType());
			           		reportVO.setServiceOrder(testResult.getServiceOrder());
			           		reportVO.setPdfUrl(testResult.getFullPdfPath());
			           		reportVO.setTestDate(testResult.getTestDate());//报告检查时间
							reportVO.setExpireDate(getReportExprieDate(testResult.getEndDate()));// 报告过期时间
			           		reportVOcensorList.add(reportVO);
			           	  }
						}
					}
				
			}
			
			Map<String, List<ReportVO>> map = new HashMap<String, List<ReportVO>>();
			map.put("reportVOSelfList", reportVOSelfList);
			map.put("reportVOSampleList", reportVOSampleList);
			map.put("reportVOcensorList", reportVOcensorList);
			return map;
		}catch(DaoException dex){
			throw new  ServiceException("TestReportServiceImpl.getInstanceReport()-->" + dex.getMessage(), dex.getException());
		}
	}

	//根据产品id 查找该产品的报告总数
	@Override
	public Long getReportCountForProductId(Long id) throws ServiceException {
		try{
			return testReportDao.getReportCountForProductId(id);
		}catch(DaoException dex){
			throw new  ServiceException("TestReportServiceImpl.getReportCountForProductId()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 获取相应日期后+1天
	 * @param endDate 报告过期时间
	 * @return
	 */
	private Date getReportExprieDate(Date endDate){
		Calendar cc = Calendar.getInstance();
		if(endDate!=null){
			cc.setTime(endDate);
			int day = cc.get(Calendar.DATE);
			cc.set(Calendar.DATE, day + 1);
			return cc.getTime();
		}
		return null;
	}

	/**
	 * 根据条形码barcode获取所有自检报告的pdfUrl
	 * @param barcode 条形码
	 * @author 郝圆彬
	 */
	@Override
	public List<String> getSelfReportPdfUrlsByBarcode(String barcode,String batch)
			throws ServiceException {
		try{
			return testReportDao.getSelfReportPdfUrlsByBarcode(barcode,batch);
		}catch(DaoException dex){
			throw new  ServiceException("TestReportServiceImpl.getSelfReportPdfUrlsByBarcode()-->" + dex.getMessage(), dex.getException());
		}
	}
	/**
	 * 通过检测结果id和样品的json对象创建检测项目
	 * @param testResultId
	 * @param obj
	 * @throws ServiceException
	 * @author ZhaWanNeng<br>
	 * 最近更新时间：2015/3/12
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void createTestProperties(long testResultId,JSONObject obj) throws ServiceException {
		List<JSONObject> propList = obj.getJSONArray("testProperties");
		for(JSONObject prop : propList){
			TestProperty tp = new TestProperty();
			tp.setName(prop.getString("name"));
			tp.setResult(prop.getString("result"));
			tp.setAssessment(prop.getString("assessment"));
			tp.setStandard(prop.getString("standard"));
			tp.setTechIndicator(prop.getString("techIndicator"));
			tp.setUnit(prop.getString("unit"));
			tp.setTestResultId(testResultId);	
			testPropertyService.create(tp);
		}
	}
	
	/**
	 * 更改报告发布状态
	 * @author ZhangHui 2015/4/9
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updatePublishFlag(char publishFlag, long reportId, String msg) throws ServiceException {
		try {
			//商超审核通过时 如果是进口食品报告 则不到testleb审核 直接发到大众门户
			if(publishFlag=='6'){
				ImportedProductTestResult impProTest=importedProductTestResultService.findByReportId(reportId);
				if(impProTest!=null){ //判断是否是进口食品报告
					publishFlag='1';
				}
			}
			getDAO().updatePublishFlag(publishFlag, reportId, msg,AccessUtils.getUserOrgName().toString());
			// 根据报告ID新建一条需要格式化的报告，并根据算法自动分配
			if(publishFlag=='6'){
				testResultHandlerService.createByReportId(reportId);
			}
		} catch (DaoException e) {
			throw new ServiceException("TestReportServiceImpl.updatePublishFlag()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 根据报告编号和来源标识，获取报告数量
	 * @author ZhangHui 2015/4/24
	 */
	public long countByServiceorderAndEdition(String serviceOrder, String edition) throws ServiceException{
		try{
			return getDAO().countByServiceorderAndEdition(serviceOrder, edition);
		}catch(DaoException e){
			throw new  ServiceException("TestReportServiceImpl.countByServiceorderAndEdition()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 接受泊银等其他系统数据，本地新增一条报告
	 * @author ZhangHui 2015/4/24
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean createBYReport(TestResult test_result, Long organization, String userName) {
		try {
			  if(test_result==null || organization==null){return false;}
			  test_result.setOrganization(organization);
			  test_result.setLastModifyUserName(userName);
			  test_result.setLastModifyTime(new Date());
			  test_result.setAutoReportFlag(true);
			  
			  Date endDate = null;
			  if(test_result.getTestDate()!=null){
				  Calendar c = Calendar.getInstance();
				  c.setTime(test_result.getTestDate());   //设置当前日期 
			      c.add(Calendar.MONTH, 6); //日期加6个月  
			      endDate = c.getTime(); 
			  }
			  test_result.setEndDate(endDate);
			  create(test_result);
			  return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 功能描述：报告录入页面，报告报告
	 * @throws Exception 
	 * @author ZhangHui 2015/6/7
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveReport(ReportOfMarketVO report_vo, Long current_business_id, 
			AuthenticateInfo info, boolean isStructed) throws Exception {
		try {
			if(report_vo==null){
				throw new Exception("参数为空");
			}
			
			ProductOfMarketVO product_vo = report_vo.getProduct_vo();
			
			/* 2.保存生产企业信息 */
			BusinessUnitOfReportVO bus_vo = report_vo.getBus_vo();
			bus_vo = businessUnitService.saveProducer(bus_vo);
			
			/* 3.保存产品信息 */
			product_vo.setProducer_id(bus_vo.getId());
			product_vo = productService.saveProduct(product_vo, current_business_id, info.getOrganization(), bus_vo.isCan_edit_qs());
			
			/* 4.保存被检人信息 */
			BusinessUnit orig_testee = businessUnitService.saveTestee(report_vo.getTestee());
			
			// 5.保存报告
			if(report_vo.isNew_flag()){
				/* 新增报告 */
				createReport(report_vo, info);
				
				// 关联报告的被检单位/人
				if(orig_testee != null){
					updateRecordOfTestee(report_vo.getId(), orig_testee.getId());
				}
			}else{
				/**
				 * 必须先关联报告的被检单位/人，然后再执行merge方法
				 * 注意：在一个事务中，当执行了merge方法后，事务未结束前，其他任何update方法执行无效
				 */
				if(orig_testee != null){
					updateRecordOfTestee(report_vo.getId(), orig_testee.getId());
				}
				
				updateReport(report_vo, info, isStructed);
			}
		} catch (ServiceException e) {
			throw new ServiceException("很抱歉，报告" + (report_vo.isNew_flag()?"新增":"更新") + "失败！", e);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void updateRecordOfTestee(Long report_id, Long testee_id) throws DaoException {
		getDAO().updateRecordOfTestee(report_id, testee_id);
	}
	
	/**
	 * 功能描述：新增报告 
	 * @throws ServiceException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void createReport(ReportOfMarketVO report_vo, AuthenticateInfo info) throws ServiceException {
		try {
			  if(report_vo == null){
				  throw new Exception("参数为空");
			  }
			  
			  // 1. 新增报告
			  TestResult report = new TestResult();
			  setReportValue(report, report_vo, info);
			  report.setOrganization(info.getOrganization()); // 报告组织机构
			  report.setTips(report_vo.getTip());        // 兼容：蒙牛解析pdf时，消息提示问题
			  report.setCreate_time(new Date());         // 报告创建时间
			  report.setCreate_user(info.getUserName()); // 报告创建用户
			  report.setOrganizationName(info.getUserOrgName());  // 报告创建企业组织机构名称
			  report.setPublishFlag(report_vo.getPublishFlag());//设置报告状态 编辑时不需要改该状态 所以提到新增这里
			  report.setCheckOrgName("");
			  report.setReportImgList(report_vo.getReportImgList());
			  report.setCheckImgList(report_vo.getCheckImgList());
			  report.setBuyImgList(report_vo.getBuyImgList());
			  create(report);
			  report_vo.setId(report.getId());
			  
			  // 2. 新增检测项目
			  testPropertyService.save(report.getId(), report_vo.getTestProperties(), true);
			  
			  // 3. 如果是进口食品保存进口食品报告信息
			  if(report_vo.getImpProTestResult()!=null){
				  importedProductTestResultService.save(report_vo); 
			  }
			  
			  // 6. 新增报告实例
			  ProductOfMarketVO product_vo = report_vo.getProduct_vo();
			  
			  ProductInstance pro_ins = new ProductInstance();
			  pro_ins.setBatchSerialNo(product_vo.getBatchSerialNo());
			 
			  //生存日期
			  pro_ins.setProductionDate(product_vo.getProductionDate());
			  //结束日期（即：过期日期）
			  pro_ins.setExpirationDate(product_vo.getExpireDate());
			  
			  pro_ins.setProduct(productService.findById(product_vo.getId()));
			  pro_ins.setProducer(businessUnitService.findById(report_vo.getBus_vo().getId()));
			  pro_ins.setQs_no(product_vo.getQs_info()==null?"":product_vo.getQs_info().getQsNo());
			  productInstanceService.create(pro_ins);
			  this.updateRecordOfSample(report_vo.getId(), pro_ins.getId());
		} catch (Exception e) {
			throw new ServiceException("TestReportServiceImpl.createReport()-->" + e.getMessage(), e);
		}
	}
	public void updateRecordOfSample(long reportId,long productInstanceId) throws DaoException{
		getDAO().updateRecordOfSample(reportId, productInstanceId);
	}

	/**
	 * 更新报告
	 * @throws ServiceException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void updateReport(ReportOfMarketVO report_vo, AuthenticateInfo info, boolean isStructed) 
				throws ServiceException {
		try {
			if(report_vo == null){
				  throw new Exception("参数为空");
			}
			
			TestResult orig_report = findById(report_vo.getId());
			testResourceService.deleteResourceByResultId(report_vo.getId());
			orig_report.setReportImgList(report_vo.getReportImgList());
			orig_report.setCheckImgList(report_vo.getCheckImgList());
			orig_report.setBuyImgList(report_vo.getBuyImgList());
			// 1. 更新结构化状态
			if(isStructed){
				TestResultHandler orig_handle = testResultHandlerService.findByTestResultIdCanEdit(report_vo.getId());
				if(orig_handle != null){
					if(orig_report.getPublishFlag() == '7'){
						//status=100表示从待完善报告退回来的
						if(orig_handle.getStatus()==100||orig_handle.getStatus()==12){
							orig_handle.setStatus(4);
						}else{
							orig_handle.setStatus(1);
						}
					}else{
						orig_handle.setStatus(2);
					}
					testResultHandlerService.update(orig_handle);
				}
			}
			
			/* 2. 更新检测项目 */
			if(orig_report.getPublishFlag()!='7' || report_vo.getMap()!=null){
				testPropertyService.save(report_vo.getId(), report_vo.getTestProperties(), false);
			}
			
			/* 3. 更新报告 */
			setReportValue(orig_report, report_vo, info, isStructed);
			update(orig_report);
			
			// 4. 跟新报告实例
			ProductOfMarketVO product_vo = report_vo.getProduct_vo();
			ProductInstance orig_sample = orig_report.getSample();
			  
			orig_sample.setBatchSerialNo(product_vo.getBatchSerialNo());
			//生产日期
			orig_sample.setProductionDate(product_vo.getProductionDate());
			//结束日期（即：过期日期）
			orig_sample.setExpirationDate(product_vo.getExpireDate());
			//orig_sample.setProduct(productService.findById(product_vo.getId()));
			//orig_sample.setProducer(businessUnitService.findById(report_vo.getBus_vo().getId()));
			productInstanceService.update(orig_sample);
			
			/* 5. 如果是进口食品保存进口食品报告信息 */
			if(report_vo.getImpProTestResult()!=null){
				 importedProductTestResultService.save(report_vo); 
			}
		} catch (Exception e) {
			throw new ServiceException("TestReportServiceImpl.updateReport()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 新增报告前，赋值操作
	 */
	private void setReportValue(TestResult report, ReportOfMarketVO report_vo, AuthenticateInfo info) {
		report.setDbflag(report_vo.getDbflag());
		report.setEdition("easy");
		
		report.setLastModifyUserName(info.getUserName());
		report.setLastModifyTime(new Date());
		
		report.setUploadPath(report_vo.getReportNOEng());
		report.setPass(report_vo.isPass());
		report.setServiceOrder(report_vo.getServiceOrder());
		report.setTestDate(report_vo.getTestDate());
		report.setTestOrgnization(report_vo.getTestOrgnization());
		report.setTestType(report_vo.getTestType());
		report.setAutoReportFlag(report_vo.isAutoReportFlag());
		report.setStandard(report_vo.getStandard());
		report.setComment(report_vo.getComment());
		report.setSampleQuantity(report_vo.getSampleQuantity());
		report.setResult(report_vo.getResult());
		report.setSamplingLocation(report_vo.getSamplingLocation());
		report.setTestPlace(report_vo.getTestPlace());
		
		Date endDate = null;
		if(report.getTestDate()!=null){
			 Calendar c = Calendar.getInstance();  
			 c.setTime(report.getTestDate());
		     c.add(Calendar.MONTH, 6); // 日期加6个月  
		     endDate = c.getTime(); 
		}
		report.setEndDate(endDate);
		
		setPdfResourceValue(report, report_vo);
	}
	
	private void setPdfResourceValue(TestResult report, ReportOfMarketVO report_vo) {
		Map<String,String> map = report_vo.getMap();
		if(map != null){
			report.setRepAttachments(report_vo.getRepAttachments());
			
			report.setFullPdfPath(map.get("fullPdfPath").toString());
			report.setInterceptionPdfPath(map.get("interceptionPdfPath").toString());
			
			// 当pdf重新上传后，签名标识更新为：未签名状态
			report.setSignFlag(false);
		}else{
			/**
			 * 当是图片合成pdf时 给报告路径赋值
			 * @author LongxianZhen 2015/06/09
			 */
			Set<Resource> PdfAtta= new HashSet<Resource>();
			if(report_vo.getImpProTestResult()!=null){//进口食品则取卫生许可证pdf资源
				PdfAtta=report_vo.getImpProTestResult().getSanitaryPdfAttachments();
			}else{
				PdfAtta=report_vo.getRepAttachments();//国产食品则取报告pdf资源
				report.setRepAttachments(PdfAtta);
			}
			if(PdfAtta.size()>0){
				String url=PdfAtta.iterator().next().getUrl();
				report.setFullPdfPath(url);
				report.setInterceptionPdfPath(url);
			}
		}
	}
	/**
	 * 更新报告前，赋值操作
	 */
	private void setReportValue(TestResult orig_report, ReportOfMarketVO report_vo, AuthenticateInfo info, boolean isStructed) {
		/**
		 * 当编辑商超已退回报告时，需要将发布标记从5 --> 4
		 * 		5 代表 商超退回
		 * 		4 代表 经销商提交但为通过商超审核
		 * @author ZhangHui 2015/5/8
		 */
		if(orig_report.getPublishFlag() == '5'){
			orig_report.setPublishFlag('4');
		}
		
		if(isStructed){
			if(orig_report.getPublishFlag() == '2'){
				/**
				 * 当编辑teslab退回至结构化人员的报告时，需要将发布标识从2 --> 6
				 * 		2 代表 testlab退回
				 * 		6 代表 商超通过，但未被结构化
				 */
				orig_report.setPublishFlag('6');
			}else if(orig_report.getPublishFlag() == '7'){
				/**
				 * 当编辑食安云退回至供应商的报告时，需要将发布标识从7 --> 6
				 * 		7 代表 食安云退回至供应商
				 * 		6 代表 商超通过，但未被结构化
				 */
				orig_report.setPublishFlag('6');
				
				// 赋值操作
				orig_report.setServiceOrder(report_vo.getServiceOrder()); // 报告编号
				orig_report.setTestType(report_vo.getTestType());         // 检验类别
				orig_report.setPass(report_vo.isPass());                  // 检验结论
				
				setPdfResourceValue(orig_report, report_vo);              // 报告pdf赋值
				return;
			}
		}
		
		setReportValue(orig_report, report_vo, info);
	}
	
	/**
	 * 大众门户按日期查找报告
	 *@author LongXianZhen 2015/06/02
	 */
	@Override
	public TestRptJson getReportJsonByDate(long proId, String test_type, int sn,
			String date,boolean portalFlag) throws ServiceException {
		try {
			// 如果已经传入了testResultList，那么就不再去请求数据库了
			//List<TestResult> testResultList = bTr ? trList : testReportDao.getResultListByIdAndType(product_id, test_report_type);
			//List<TestResult> testResults = date == null ? null : testReportDao.getRptListByIdAndTypeAndDate(proId, test_type, date,sn);
			TestResult tr = date == null ? null : testReportDao.getRptListByIdAndTypeAndDate(proId, test_type, date,sn,portalFlag);
			if(tr==null){
				return null;
			}
			TestResult _tr=testReportDao.findById(tr.getId());
			tr.setReportImgList(_tr.getReportImgList());
			tr.setCheckImgList(_tr.getCheckImgList());
			tr.setBuyImgList(_tr.getBuyImgList());
			Date proDate=tr.getSample().getProductionDate();
			String pDate=DateUtil.dateFormatYYYYMMDD(proDate);
			TestRptJson trj=getReportJson(tr);
			Integer trNum=testReportDao.countByIdAndType(proId, test_type,pDate,portalFlag);
			trj.setTotal(trNum);
			return trj;
		/*	TestResult testResult=testResults.get(sn-1);
			*//**
			 * 未结构化检测报告无需返回报告中未结构化的数据<br>
			 * @author zhangHui 2015/5/8<br>
			 *//*
			String result = "";           // 检测结果描述
			String testDate = "";         // 检验日期
			String sampleQuantity = "";   // 抽样量
			String testee = "--";         // 被检测单位/人
			String samplingLocation = ""; // 抽样地点
			String standard = "";         // 判定依据
			String samplingDate = null;   // 抽样日期
			
			String remark = "";           // 备注
			String instrument = "";       // 仪器
			List<TestProperty> testPropertyList = null;  // 检测项目列表
			boolean isCanViewAllInfo = testResultHandlerService.isCanViewAllInfo(testResult.getId());
			if(isCanViewAllInfo){
				result = testResult.getResult();
				testDate = testResult.getTestDate()==null?null:DateUtil.formatDateToString(testResult.getTestDate());
				sampleQuantity = testResult.getSampleQuantity();
				if(testResult.getTestee() != null){
					testee = testResult.getTestee().getName();
				}
				samplingLocation = testResult.getSamplingLocation();
				standard = testResult.getStandard();
				if(testResult.getSamplingDate() != null){
					
				}
				if(testResult.getSamplingDate() != null){
					samplingDate = DateUtil.formatDateToString(testResult.getSamplingDate());
				}
				//batchSN = 
				remark = testResult.getComment();
				instrument = testResult.getEquipment();
				testPropertyList = testPropertyService.findByReportId(testResult.getId());
			}
			String batchSN = testResult.getSample().getBatchSerialNo();          // 批号/日期
			String sampleName = productDao.findById(proId).getName();  //样品名称
			String serial = testResult.getSampleNO();   //样品编号
			String tradeMark = testResult.getSample().getProduct().getBusinessBrand().getName(); // 商标
			String enterprise = testResult.getSample().getProducer().getName(); // 生产企业
			String format = testResult.getSample().getProduct().getFormat();    // 规格型号
			String testType = testResult.getTestType(); //检验类别
			String status = testResult.getSample().getProduct().getStatus();    //样品状态
			
			
           若报告是测试院的账号上传的，不显示
			String organizationName = "";
			Long organization = testResult.getOrganization();
			获取交易市场的名称
			String orig_marketName = businessUnitService.getMarketNameByOrganization(organization);
			if(organization!=null && organization!=1L){
				organizationName = orig_marketName + testResult.getOrganizationName(); //所属企业名称
			}
			String pdfUrl = "";
			if(testResult.getFullPdfPath() != null && !testResult.getFullPdfPath().equals(""))
				pdfUrl = testResult.getFullPdfPath();
			String interceptionPdf = "";
			if(testResult.getInterceptionPdfPath()!= null && !testResult.getInterceptionPdfPath().equals(""))
				interceptionPdf = testResult.getInterceptionPdfPath();
			String productionDate ="";
			if(testResult.getSample().getProductionDate()!=null){
				productionDate = DateUtil.formatDateToString(testResult.getSample().getProductionDate());
			}
			int total = testResults.size();
			*//**
			 * 处理进口食品报告信息
			 *//*
			ImportedProductTestResult impProTestResult=importedProductTestResultService.findByReportId(testResult.getId());
			TestRptJson testRptJson = new TestRptJson(result, testDate, sampleName,
					serial, tradeMark, enterprise,
					null, format, sampleQuantity,
					testee, samplingLocation, standard,
					testType, samplingDate, batchSN, status,
					instrument, remark,
					testPropertyList, pdfUrl, total, sn,interceptionPdf,organizationName);
			testRptJson.setProductionDate(productionDate);
			testRptJson.setImpProTestResult(impProTestResult);
			System.out.println(new Date());
			return testRptJson;*/
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("", e.getException());
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("", e);
		}
	}
	
	/**
	 * 功能描述：获取所有退回给当前登陆供应商的报告数量
	 * @author ZhangHui 2015/6/29
	 * @throws ServiceException 
	 */
	@Override
	public long countOfBackToGYS(Long organization, String configure) throws ServiceException {
		try {
			if(organization == null){
				throw new Exception("参数为空");
			}
			
			String condition = getConfigure(configure) + " AND  DATEDIFF(e.sample.expirationDate, SYSDATE()) > 0 AND organization = ?1 AND publishFlag IN ('5', '7')";;
			
			return testReportDao.count(condition, new Object[]{organization});
		} catch (Exception e) {
			throw new ServiceException("TestReportServiceImpl.countOfBackToGYS()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：分页查询退回给当前登陆供应商的报告
	 * @author ZhangHui 2015/6/29
	 * @throws ServiceException 
	 */
	@Override
	public List<TestResult> getReportsOfBackToGYSByPage(Long organization, int page, int pageSize, String configure) throws ServiceException {
		try {
			
			 List<TestResult> reports = null;
			 String new_configure=" WHERE  DATEDIFF(ip.expiration_date , SYSDATE()) > 0 AND  tr.del = 0  AND tr.publish_flag IN ('5', '7') AND tr.organization = "+organization+"  ";
			 String config = getSCPassConfigure(configure,new_configure);
			 config+=" order by tr.back_time desc " ;
			 reports=getDAO().getReportsOfDealerAllPassWithPage(config, page, pageSize);
			 if(reports == null){return null;}
			 return reports;	
			
			/*if(organization == null){
				throw new Exception("参数为空");
			}
			
			String condition = getConfigure(configure) + " AND organization = " + organization + " AND publishFlag IN ('5', '7')";
			
			return getDAO().getListByPage(page, pageSize, condition);*/
		}catch(Exception e){
			throw new ServiceException("TestReportServiceImpl.getReportsOfBackToGYSByPage()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：封装TestRptJson报告
	 * @author longxianzhen 2015/6/29
	 */
	@Override
	public TestRptJson getReportJson(TestResult testResult) throws ServiceException {
		try {
			if(testResult==null){
				return null;
			}
			/**
			 * 未结构化检测报告无需返回报告中未结构化的数据<br>
			 * @authorzhangHui 2015/5/8<br>
			 */
			String result = "";           // 检测结果描述
			String testDate = "";         // 检验日期
			String sampleQuantity = "";   // 抽样量
			String samplingLocation = ""; // 抽样地点
			String standard = "";         // 判定依据
			String samplingDate = null;   // 抽样日期
			String remark = "";           // 备注
			List<TestProperty> testPropertyList = null;  // 检测项目列表
			boolean isCanViewAllInfo = testResultHandlerService.isCanViewAllInfo(testResult.getId());
			if(isCanViewAllInfo){
				result = testResult.getResult();
				testDate = testResult.getTestDate()==null?null:DateUtil.formatDateToString(testResult.getTestDate());
				sampleQuantity = testResult.getSampleQuantity();
				samplingLocation = testResult.getSamplingLocation();
				standard = testResult.getStandard();
				if(testResult.getSamplingDate() != null){
					samplingDate = DateUtil.formatDateToString(testResult.getSamplingDate());
				}
				remark = testResult.getComment();
				testPropertyList = testPropertyService.findByReportId(testResult.getId());
			}
			String batchSN = testResult.getSample().getBatchSerialNo();          // 批号/日期
			String serial = testResult.getSampleNO();   //样品编号
			String testType = testResult.getTestType(); //检验类别
			
           /*若报告是测试院的账号上传的，不显示*/
			String organizationName = "";
			Long organization = testResult.getOrganization();
			/*获取交易市场的名称*/
			String orig_marketName = businessUnitService.getMarketNameByOrganization(organization);
			if(organization!=null && organization!=1L){
				organizationName = orig_marketName + testResult.getOrganizationName(); //所属企业名称
			}
			String pdfUrl = "";
			if(testResult.getFullPdfPath() != null && !testResult.getFullPdfPath().equals(""))
				pdfUrl = testResult.getFullPdfPath();
			String interceptionPdf = "";
			if(testResult.getInterceptionPdfPath()!= null && !testResult.getInterceptionPdfPath().equals(""))
				interceptionPdf = testResult.getInterceptionPdfPath();
			String productionDate ="";
			if(testResult.getSample().getProductionDate()!=null){
				productionDate = DateUtil.formatDateToString(testResult.getSample().getProductionDate());
			}
			//过期日期
			String expirationDate ="";
			if(testResult.getSample().getExpirationDate()!=null){
				expirationDate = DateUtil.formatDateToString(testResult.getSample().getExpirationDate());
			}
			String edition ="";
			if(testResult.getEdition()!=null&&!testResult.getEdition().equals("")){
				edition = testResult.getEdition();
			}
			/**
			 * 处理进口食品报告信息
			 */
			ImportedProductTestResult impProTestResult=importedProductTestResultService.findByReportId(testResult.getId());
			TestRptJson testRptJson = new TestRptJson(result, testDate, "",
					serial, "", "",
					null, "", sampleQuantity,
					"", samplingLocation, standard,
					testType, samplingDate, batchSN, "",
					"", remark,
					testPropertyList, pdfUrl, 0, 0,interceptionPdf,organizationName,edition);
			testRptJson.setProductionDate(productionDate);
			testRptJson.setExpirationDate(expirationDate);
			testRptJson.setImpProTestResult(impProTestResult);
			testRptJson.setReportImgList(testResult.getReportImgList());
			testRptJson.setCheckImgList(testResult.getCheckImgList());
			testRptJson.setBuyImgList(testResult.getBuyImgList());
			testRptJson.setTestOrgnization(testResult.getTestOrgnization());
			return testRptJson;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("", e);
		}
	}
	
	/**
	 * 按组织机构和用户名查找报告列表（商超供应商）
	 * @author longxianzhen 2015/08/10
	 */
	@Override
	public List<TestResult> getReportsByOrgIdAndUserNameFromSCWithPage(
			Long organizationId, String userRealName, int page,
			int pageSize, char pubFlag, String configure)throws ServiceException {
		try {
			List<TestResult> reports = null;
			 String new_configure=" WHERE  DATEDIFF(ip.expiration_date , SYSDATE()) > 0 AND tr.del = 0  AND tr.publish_flag ='"+pubFlag+"' AND tr.organization = "+organizationId+" AND tr.last_modify_user= '"+userRealName+"' ";
			 String config = getSCPassConfigure(configure,new_configure);
			 config+=" order by tr.last_modify_time desc " ;
			 reports=getDAO().getReportsOfDealerAllPassWithPage(config, page, pageSize);
			 if(reports == null){return null;}
			return reports;
		}catch(DaoException dex){
			throw new ServiceException("TestReportServiceImpl.getReportsByOrgIdAndUserRealNameWithPage()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 根据barcode验证该产品是否有已退回的报告没有处理
	 * @param barcode
	 * @return
	 */
	@Override
	public boolean verifyBackReportByBarcode(String barcode)
			throws ServiceException {
		try {
			return getDAO().verifyBackReportByBarcode(barcode);
		} catch (DaoException e) {
			throw new ServiceException("TestPropertyServiceImpl.verifyBackReportByBarcode()-->" + e.getMessage(), e.getException());
		}
	}
	@Override
	public void saveTestProperty(List<TestProperty> items) {
		testPropertyService.saveTestProperty(items);
	}
	@Override
	public TestRptJson getTestResult(String barcode, long buId,
			long product_id, String type, String date) {
		TestRptJson trj= null;
		try {
			TestResult tr = testReportDao.getTestResult(barcode, buId, product_id ,type,date);
			if(tr==null){
				return null;
			}
			Date proDate=tr.getSample().getProductionDate();
			String pDate=DateUtil.dateFormatYYYYMMDD(proDate);
			trj=getReportJson(tr);
			Integer trNum=testReportDao.countByIdAndTypeOrg(product_id, type,pDate,tr.getOrganization());
			trj.setTotal(trNum);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return trj;
	}
	@Override
	public List<TestRptJson> getTestResultList(String barcode, long buId,
			Long productId, String type, String date) {
		List<TestRptJson> trjList =  new ArrayList<TestRptJson>();
		try {
			String types[] = {"企业自检","企业送检","政府抽检"};
			for(int i=0;i<types.length;i++){
				TestResult tr = testReportDao.getTestResult(barcode, buId, productId ,types[i],date);
				Integer trNum=0;
				if(tr!=null){
					TestRptJson trj= new TestRptJson();
					Date proDate=tr.getSample().getProductionDate();
					String pDate=DateUtil.dateFormatYYYYMMDD(proDate);
					trj=getReportJson(tr);
					  trNum=testReportDao.countByIdAndTypeOrg(productId, types[i],pDate,tr.getOrganization());
					  trj.setTotal(trNum);
					  trjList.add(trj);
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return trjList;
	}
	/**
	 * 根据ID查询报告状态，是否已经发布
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TestResult findByIdPublishFlag(Long id) {
		
		return testReportDao.findByIdPublishFlag(id);
	}
	/**
	 * 根据营业执照查询所有报告
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<BusinessTestResultVO> getByLicenseNoTistResult(String licenseNo,
			String type, Integer page, Integer pageSize) {
		List<TestResult> trs = null;
		try {
			String str = "";
			if (type != null && !"".equals(type)) {str = " AND e.testType = ?2 ";}
			trs = testResultDAO.getListByPage(page, pageSize," WHERE e.testee.license.licenseNo = ?1 "+str,new Object[]{licenseNo,type});
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return this.getBusinessTestResult(trs);
	}
	private List<BusinessTestResultVO> getBusinessTestResult(List<TestResult> trs) {
		List<BusinessTestResultVO> testList = new ArrayList<BusinessTestResultVO>();
		for (TestResult tr : trs) {
			BusinessTestResultVO vo = new BusinessTestResultVO();
			vo.setId(tr.getId());
			vo.setLicenseNo(tr.getTestee().getLicense().getLicenseNo());
			vo.setBusId(tr.getTestee().getId());
			vo.setBusinessName(tr.getTestee().getName());
			vo.setBatchId(tr.getSample().getId());
			vo.setBatchSerialNo(tr.getSample().getBatchSerialNo());
			vo.setBrandId(tr.getBrand().getId());
			vo.setBranName(tr.getBrand().getName());
		    vo.setSampleQuantity(tr.getSampleQuantity());
		    vo.setSampleNO(tr.getSampleNO());
		    vo.setSamplingLocation(tr.getSamplingLocation());
		    vo.setSamplingDate(tr.getSamplingDate());
		    vo.setApproveBy(tr.getApproveBy());
		    vo.setAuditBy(tr.getAuditBy());
		    vo.setBackLimsURL(tr.getBackLimsURL());
		    vo.setBackTime(tr.getBackTime());
			vo.setCheckOrgName(tr.getCheckOrgName());
			vo.setComment(tr.getComment());
			vo.setCreate_time(tr.getCreate_time());
			vo.setCreate_user(tr.getCreate_user());
			vo.setDbflag(tr.getDbflag());
			vo.setEdition(tr.getEdition());
			vo.setEndDate(tr.getEndDate());
			vo.setEquipment(tr.getEquipment());
			vo.setFullPdfPath(tr.getFullPdfPath());
			vo.setServiceOrder(tr.getServiceOrder());
			vo.setStandard(tr.getStandard());
			vo.setTestType(tr.getTestType());
			vo.setOrganization(tr.getOrganization());
			vo.setOrganizationName(tr.getOrganizationName());
			vo.setKeyTester(tr.getKeyTester());
			vo.setTestPlace(tr.getTestPlace());
			vo.setTips(tr.getTips());
			vo.setReceiveDate(tr.getReceiveDate());
			vo.setInterceptionPdfPath(tr.getInterceptionPdfPath());
			vo.setPublishFlag(tr.getPublishFlag());
			vo.setPubUserName(tr.getPubUserName());
			vo.setSampleNO(tr.getSampleNO());
			vo.setLastModifyTime(tr.getLastModifyTime());
			testList.add(vo);
		}
		return testList;
	}
	@Override
	public TestRptJson getReportJsonByPreciseDate(long proId, String test_type, int sn,
			String date,boolean portalFlag) throws ServiceException {
		try {
			// 如果已经传入了testResultList，那么就不再去请求数据库了
			//List<TestResult> testResultList = bTr ? trList : testReportDao.getResultListByIdAndType(product_id, test_report_type);
			//List<TestResult> testResults = date == null ? null : testReportDao.getRptListByIdAndTypeAndDate(proId, test_type, date,sn);
			TestResult tr = date == null ? null : testReportDao.getResultByIdAndType(proId, test_type,sn, date,portalFlag);
			if(tr==null){
				return null;
			}
			Date proDate=tr.getSample().getProductionDate();
			String pDate=DateUtil.dateFormatYYYYMMDD(proDate);
			TestRptJson trj=getReportJson(tr);
			Integer trNum=testReportDao.countByIdAndType(proId, test_type,pDate,portalFlag);
			trj.setTotal(trNum);
			return trj;
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("", e.getException());
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("", e);
		}
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<ProductInfoVO> getByLicenseNoProduct(String licenseNo,
			Integer page, Integer pageSize) {
		return testReportDao.getByLicenseNoProduct(licenseNo,page, pageSize);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Long getByLicenseNoProductCount(String licenseNo) {
		return testReportDao.getByLicenseNoProductCount(licenseNo);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<ProductJGVO> getFindProduct(BusinessUnit businessUnit,String type, int page, int pageSize) {
//		BusinessUnit businessUnit=this.businessUnitService.getBusinessUnitByCondition(businessName, qsNo, buslicenseNo);
//		if(businessUnit==null){
//			return new ArrayList<ProductJGVO>();
//		}
		try{
			List<ProductJGVO> proList = null;
			
			if(type.equals("生产企业")){
				proList = testReportDao.getProductByProducerBusinessUnit(businessUnit,page,pageSize);
			}else{
				proList = testReportDao.getProductByCirculationBusinessUnit(businessUnit,page,pageSize);
			}
			for (ProductJGVO vo : proList) {
				vo.setRegularity(this.getByIdRegularity(vo.getId()));
			}
			return proList;
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<ProductJGVO>();
		}
	}
	private String getByIdRegularity(long id) {
		Map<String,String> map = testReportDao.getByIdRegularity(id);
		String regularity = "";
		int k = 1;
		for (String key : map.keySet()) {
			   System.out.println("key= "+ key + " and value= " + map.get(key));
			   regularity +=map.get(key);
			   if(k<=map.size()-1){
				   regularity+=";"; 
			   }
			   k++;
	    }
		return regularity;
	}
	@Override
	public Long getByProductCount(BusinessUnit businessUnit,String type) {
		return testReportDao.getByProductCount(businessUnit,type);
	}
	
	public long getThirdCountByProductId(long productId){
		try {
			return testReportDao.countBySql("select count(*) from test_result tr left join product_instance pi on pi.id=tr.sample_id where tr.test_type=?1 and pi.product_id=?2",new Object[]{"第三方检测",productId});
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
}