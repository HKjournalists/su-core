package com.gettec.fsnip.fsn.service.test.impl;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_REPORT_BACK_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_REPORT_BACK_PATH;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.ProductInstanceDAO;
import com.gettec.fsnip.fsn.dao.test.TestPropertyDAO;
import com.gettec.fsnip.fsn.dao.test.TestResultDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.test.TestRptJson;
import com.gettec.fsnip.fsn.service.business.EnterpriseRegisteService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.ProductCategoryInfoService;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.service.test.TestResultService;
import com.gettec.fsnip.fsn.transfer.TestResultTransfer;
import com.gettec.fsnip.fsn.util.DateUtil;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.vo.RiskResultVO;
import com.gettec.fsnip.fsn.vo.TestResultSearchCriteria;
import com.gettec.fsnip.fsn.vo.product.EnterpriseVo;
import com.gettec.fsnip.fsn.vo.product.ProductReportVo;
import com.gettec.fsnip.fsn.vo.report.ReportBackVO;
import com.gettec.fsnip.fsn.vo.report.ReportVO;
import com.gettec.fsnip.fsn.vo.report.StructuredReportOfTestlabVO;
import com.lhfs.fsn.dao.product.ProductDao;
import com.lhfs.fsn.service.testReport.TestReportService;
import com.lhfs.fsn.vo.SampleVO;
import com.lhfs.fsn.vo.TestBusinessUnitVo;
import com.lhfs.fsn.vo.TestResultVO;
import com.lhfs.fsn.vo.TestResultsVO;

/**
 * TestResult service implementation
 * @author Ryan Wang
 */
@Service(value = "testResultService")
public class TestResultServiceImpl extends BaseServiceImpl<TestResult, TestResultDAO> 
		implements TestResultService {
	@Autowired private TestReportService testReportService;
	@Autowired protected TestResultDAO testResultDAO;
	@Autowired protected TestPropertyDAO testPropertyDAO;
	@Autowired protected ProductInstanceDAO productInstanceDAO;
	@Autowired protected TestPropertyService testPropertyService;
	@Autowired private EnterpriseRegisteService enterpriseService;
	@Autowired private ResourceService resourceService;
	@Autowired private ProductDao productLFDAO;
	@Autowired private ProductCategoryInfoService productCategoryInfoService;

	@Override
	public TestResultDAO getDAO() {
		return testResultDAO;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<TestResult> findTestResults(TestResultSearchCriteria criteria) throws ServiceException{
		try {
			return getDAO().findTestResults(
					criteria.isPublishFlag(),
					criteria.getPage(),
					criteria.getPageSize(),
					criteria.getConfigure()
			        );
		} catch (DaoException daoe) {
			throw new ServiceException("TestResultServiceImpl.findTestResults()-->" + daoe.getMessage(), daoe.getException());
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<TestResult> findTestResultsByThird(TestResultSearchCriteria criteria) throws ServiceException{
		try {
			return getDAO().findTestResults(
					criteria.getPage(),
					criteria.getPageSize(),
					criteria.getConfigure()
			        );
		} catch (DaoException daoe) {
			throw new ServiceException("TestResultServiceImpl.findTestResults()-->" + daoe.getMessage(), daoe.getException());
		}
	}
	
	/**
	 * 获取需审核的结构化报告集合
	 * @author ZhangHui 2015/5/6
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<StructuredReportOfTestlabVO> findTestResultsOfStructureds(TestResultSearchCriteria criteria) throws ServiceException{
		try {
			return getDAO().findTestResultsOfStructureds(
					criteria.getPage(),
					criteria.getPageSize(),
					criteria.getConfigure()
			        );
		} catch (DaoException daoe) {
			throw new ServiceException("TestResultServiceImpl.findTestResultsOfStructureds()-->" + daoe.getMessage(), daoe.getException());
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public long getCount(TestResultSearchCriteria criteria) throws ServiceException{
		try {
			return getDAO().getTestResultCount(criteria.isPublishFlag(),criteria.getConfigure());
		} catch (DaoException daoe) {
			throw new ServiceException("TestResultServiceImpl.getCount()-->" + daoe.getMessage(), daoe.getException());
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public long getThirdCount(TestResultSearchCriteria criteria) throws ServiceException{
		try {
			return getDAO().getTestResultThirdCount(criteria.getConfigure());
		} catch (DaoException daoe) {
			throw new ServiceException("TestResultServiceImpl.getCount()-->" + daoe.getMessage(), daoe.getException());
		}
	}
	
	/**
	 * 获取需审核的结构化报告数量
	 * @author ZhangHui 2015/5/6
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public long getCountOfStructureds(TestResultSearchCriteria criteria){
		return getDAO().getCountOfStructureds(criteria.getConfigure());
	}
	
	/**
	 * service层根据过滤条件查询产品报告集合，有分页处理
	 */
	@Override
	public List<TestResult> getProReportListByConfigure(int page, int pageSize,
			String configure)throws ServiceException {
		List<TestResult> tes=null;
		try {
		    if(page < 1 && pageSize<1) {
		        tes = testResultDAO.getProEXECLReportList(configSQLString(configure)); //导出EXECL
		    }else {
		        tes = testResultDAO.getProReportListByPage(page,pageSize,getConfigurePro(configure));
		    }
		    TestResultTransfer.transfer(tes);
		} catch (DaoException dex) {
             throw new ServiceException(dex.getMessage(),dex.getException());
		}
		return tes;
	}
	
	//专为EXCEL导出提供
	private Map<String, Object> configSQLString(String configure){
	    if(configure == null){
            return null;
        }
        Object[] params = null;
        String new_configure = "";
        String publishConfig=" AND (e.publish_flag ='0' or e.publish_flag ='1') ";
        String filter[] = configure.split("@@");
        for(int i=0;i<filter.length;i++){
            String filters[] = filter[i].split("@");
            if(filters.length > 3){
                try {
                    if(filters[0].equals("publishFlag")){
                        publishConfig=" ";
                    }
                    String config = splitConfigureSQLPro(filters[0],filters[1],filters[2]);
                    if(config==null){
                        continue;
                    }
                    if(i==0){
                        new_configure = new_configure + " WHERE " + config;
                    }else{
                        new_configure = new_configure +" AND " + config;
                    }
                } catch (Exception e) {
                e.printStackTrace();
                }
            }else{              
                publishConfig=" WHERE e.publish_flag ='0' or e.publish_flag ='1'";  
            }
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        new_configure=new_configure+publishConfig;
        map.put("condition", new_configure);
        map.put("params", params);
        return map;
	}
	
	//专为EXCEL导出提供
	private String splitConfigureSQLPro(String field,String mark,String value) {
	    try {
            value = URLDecoder.decode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(field.equals("serviceOrder")){
            return FilterUtils.getConditionStr(" e.service_order",mark,value,true);
        }
        if(field.equals("sample_product_name")){
            return FilterUtils.getConditionStr(" e.proname",mark,value);
        }
        if(field.equals("sample_producer_name")){
            return FilterUtils.getConditionStr(" e.busname",mark,value);
        }
        if(field.equals("testType")){
            return FilterUtils.getConditionStr(" e.test_type",mark,value);
        }
        if(field.equals("sample_product_barcode")){
            return FilterUtils.getConditionStr(" e.barcode",mark,value);
        }
        if(field.equals("sample_product_format")){
            return FilterUtils.getConditionStr(" e.format",mark,value);
        }
        if(field.equals("testDate")){
            return FilterUtils.getConditionStr(" e.test_date",mark,value);
        }
        if(field.equals("sample_batchSerialNo")){
            return FilterUtils.getConditionStr(" e.batch_serial_no",mark,value);
        }
        if(field.equals("sample_productionDate")){
            return FilterUtils.getConditionStr(" e.production_date",mark,value);
        }
        if(field.equals("sample_product_businessBrand_name")){
            return FilterUtils.getConditionStr(" e.brandname",mark,value);
        }
        if(field.equals("publishFlag")){
            if(value.contains("未")){
                value="0";
            }else if(value.contains("已")){
                value="1";
            }
            return FilterUtils.getConditionStr("e.publish_flag",mark,value);
        }
        return null;
	}
	
	
	
	/**
	 * service层根据过滤条件查询产品报告数量
	 */
	@Override
	public Long getCountProReport(String configure) throws ServiceException {
		Long count=0L;
		try {
			count=testResultDAO.getCountProReport(getConfigurePro(configure));
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
		return count;
	}

	private String getConfigurePro(String configure) {
		if(configure == null){
			return null;
		}
		// Object[] params = null;
        String new_configure = "";
        // String publishConfig=" AND (publishFlag='0' or publishFlag='1') ";
        String filter[] = configure.split("@@");
        for(int i=0;i<filter.length;i++){
            String filters[] = filter[i].split("@");
            if(filters.length > 3){
                try {
                	/*if(filters[0].equals("publishFlag")){
                		publishConfig=" ";
                	}*/
                    String config = splitJointConfigurePro(filters[0],filters[1],filters[2]);
                    if(config==null){
                        continue;
                    }
                    if(i==0){
                        new_configure = new_configure + " WHERE " + config;
                    }else{
                        new_configure = new_configure +" AND " + config;
                    }
                } catch (Exception e) {
                e.printStackTrace();
                }
            }/*else{            	
            	publishConfig=" WHERE publishFlag='0' or publishFlag='1'";  
            }*/
        }
        
        /*Map<String, Object> map = new HashMap<String, Object>();
        new_configure=new_configure+publishConfig;
        map.put("condition", new_configure);
        map.put("params", params);*/
        return new_configure;
	}
	
	private String splitJointConfigurePro(String field,String mark,String value){
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(field.equals("serviceOrder")){
			return FilterUtils.getConditionStr("serviceOrder",mark,value,true);
		}
		if(field.equals("sample_product_name")){
			return FilterUtils.getConditionStr("sample.product.name",mark,value);
		}
		if(field.equals("sample_producer_name")){
			return FilterUtils.getConditionStr("sample.producer.name",mark,value);
		}
		if(field.equals("testType")){
			return FilterUtils.getConditionStr("testType",mark,value);
		}
		if(field.equals("sample_product_barcode")){
			return FilterUtils.getConditionStr("sample.product.barcode",mark,value);
		}
		if(field.equals("sample_product_format")){
			return FilterUtils.getConditionStr("sample.product.format",mark,value);
		}
		if(field.equals("testDate")){
			return FilterUtils.getConditionStr("testDate",mark,value);
		}
		if(field.equals("sample_product_format")){
			return FilterUtils.getConditionStr("sample.product.format",mark,value);
		}
		if(field.equals("sample_batchSerialNo")){
			return FilterUtils.getConditionStr("sample.batchSerialNo",mark,value);
		}
		if(field.equals("sample_productionDate")){
			
			return FilterUtils.getConditionStr("sample.productionDate",mark,value);
		}
		if(field.equals("sample_product_businessBrand_name")){
			return FilterUtils.getConditionStr("sample.product.businessBrand.name",mark,value);
		}
		if(field.equals("organizationName")){
			return FilterUtils.getConditionStr("organizationName",mark,value);
		}
		if(field.equals("pubUserName")){
			return FilterUtils.getConditionStr("pubUserName",mark,value);
		}
		if(field.equals("publishFlag")){
			if(value.contains("未")){
				value="0";
			}else if(value.contains("已")){
				value="1";
			}
			return FilterUtils.getConditionStr("publishFlag",mark,value);
		}
		return null;
	}
	/**
     * 按产品实例id查询检测报告
	 * @author ZhaWanNeng
	 */
	@Override
	public List<RiskResultVO> findTRByProductInstance(Long id,Boolean isInspect)throws ServiceException {
		try {
			List<RiskResultVO> riskResultVOs = testResultDAO.findTRByProductInstance(id,isInspect);
			return riskResultVOs;
		} catch (DaoException e) {
			 throw new ServiceException("TestResultServiceImpl.findTRByProductInstance() 按条件查询检测报告出错 -->" + e.getMessage(), e.getException());
		}
		
	}

	/**
	 * 根据产品和报告类型查找最新一条报告 
	 * @param organization 组织机构
	 * @param proId 产品id
	 * @param reportType 报告类型
	 * @param status status 0表示是在未处理中查找是否最新报告，1,表示处理中
	 * @author HuangYog
	 */
    @Override
    public ReportVO getNewestReportForPIdAndReportType(Long organization,
            Long proId, String reportType, Integer status) throws ServiceException {
        try {
			return  testResultDAO.getNewestReportForPIdAndReportType(organization,proId,reportType,status);
		} catch (DaoException daoe) {
			throw new ServiceException("TestResultServiceImpl.getNewestReportForPIdAndReportType()-->"+daoe.getMessage(),daoe.getException());
		}
	}

    /**
     * 验证报告是否存在 
     * @param serviceOrder 委托单号
     * @param organizationID 所属组织机构ID
     * @param sampleId LIMS处样品ID
     * @param sampleNO LIMS样品编号
     * @return boolean true 存在  false 失败
     * @author LongXianZhen
     */
	@Override
	public boolean verifyReportExist(String serviceOrder, Long organizationID,
			Long sampleId, String sampleNO) {
		try {
			Long count = testResultDAO.count(" WHERE e.serviceOrder=?1 AND e.organization=?2 AND e.sampleNO=?3 AND e.limsSampleId=?4 ", 
						new Object[]{serviceOrder,organizationID,sampleNO,sampleId});
			return count>0?true:false;
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return false;
	}

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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public Map<String, Object> saveTestResult(TestResultsVO testResultsVO,
			TestResultVO testResultVO, ProductInstance productInstance,
			BusinessUnit testee, String fullPdfPath, String interceptionPdfPath,SampleVO sample) {
		Map<String, Object> map=new HashMap<String, Object>();
		TestResult report = new TestResult();
		report.setEdition(testResultsVO.getEdition());
		report.setOrganization(testResultsVO.getOrganizationID());
		report.setServiceOrder(testResultsVO.getServiceOrder());
		/* 抽样日期 */
		try {
			report.setSamplingDate(testResultVO.getSamplingDate().equals("")||testResultVO.getSamplingDate()==null?null:DateUtil.StringToDate(testResultVO.getSamplingDate(), "yyyy-MM-dd"));
		} catch (ParseException e) {
			e.printStackTrace();
			map.put("msg", "抽样日期格式不正确");
			map.put("status", "false");
			return map;
		}
		String testDate=testResultVO.getTestDate();
		Date tDate=null;
		if(testDate!=null&&!testDate.equals("")){
			try {
				tDate=DateUtil.StringToDate(testDate, "yyyy-MM-dd");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		report.setTestDate(tDate);
		/* 抽样数量 */
		report.setSampleQuantity(testResultVO.getSampleQuantity()); 
		/* 抽样地点 */
		report.setSamplingLocation(testResultVO.getSamplingLocation());
		/* 检验类别 */
		String testType = testResultsVO.getTestType();
		if(testType.equals("云平台委托检测")){
			report.setTestType("第三方检测");
			report.setPublishFlag('1');
		}else{
			report.setTestType(testType);
			report.setPublishFlag('0');
		}
		/* 主要仪器 */
		report.setEquipment(testResultVO.getEquipment());
		/* 执行标准 */
		report.setStandard(testResultVO.getStandard());
		/* 检测结果描述 */
		report.setResult(testResultVO.getResult());
		/* 检验结论 */
		report.setPass(testResultVO.isPass());
		report.setComment(testResultVO.getComment()); //  备注
		report.setApproveBy(testResultVO.getApproveBy());
		report.setAuditBy(testResultVO.getAuditBy());
		report.setKeyTester(testResultVO.getKeyTester());
		report.setSample(productInstance);
		report.setTestee(testee);
		/* 政府抽检的截取前两页pdf路径；企业自检和企业送检均与fullPdfPath的值一致 */
		report.setInterceptionPdfPath(interceptionPdfPath);
		/* 完整的没有截取的报告路径 */ 
		report.setFullPdfPath(fullPdfPath);
		report.setReceiveDate(new Date());
		report.setDbflag("receive_from_lims");
		report.setSampleNO(sample.getSampleNO());
		report.setLimsSampleId(sample.getSampleId());
		report.setBackLimsURL(testResultsVO.getBackURL());
		report.setOrganizationName(testResultsVO.getOrgName());
		report.setPubUserName(testResultsVO.getPublisher());
		report.setLimsIdentification(sample.getReportNo());
		report.setCreate_user(testResultsVO.getPublisher());
		report.setCheckOrgName("");
		try {
			testResultDAO.persistent(report);
		} catch (JPAException e) {
			e.printStackTrace();
			map.put("msg", "报告保存失败");
			map.put("status", "false");
			return map;
		}
		map.put("status", "true");
		map.put("testResult", report);
		return map;
	}

	@Override
	public TestResult getLimsReportByConditions(Long sampleId, String sampleNo,
			String serviceOrder, Long organizationID) throws ServiceException {
		try {
			List<TestResult> trs = testResultDAO.getListByCondition(" WHERE e.serviceOrder=?1 AND e.organization=?2 AND e.sampleNO=?3 AND e.limsSampleId=?4 ", 
						new Object[]{serviceOrder,organizationID,sampleNo,sampleId});
			return trs!=null&&trs.size()>0?trs.get(0):null;
		} catch (JPAException e) {
			throw new ServiceException("",e.getException());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void deleteById(Long id) throws ServiceException {
		try {
			TestResult tr=testResultDAO.findById(id);
			testPropertyDAO.deleByTestResultId(id);
			testResultDAO.remove(tr);
			productInstanceDAO.remove(tr.getSample());
		} catch (JPAException je) {
			throw new ServiceException("TestResultServiceImpl.deleteById()-->"+je.getMessage(),je.getException());		
		}catch (DaoException je) {
			throw new ServiceException("TestResultServiceImpl.deleteById()-->"+je.getMessage(),je.getException());
		}
	}
        

    /**
	 * 报告总数
	 * @return Long
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@Override
	public Long testResultCount() throws ServiceException {
		try {
				return getDAO().testResultCount();
			} catch (DaoException dex) {
			   throw new ServiceException("", dex.getException());
			}
	}
    /**
     * 企业专栏
     * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
     */
	@Override
	public List<EnterpriseVo> getEnterprise(int proSize,String busIds)
			throws ServiceException {
		try {
			List<EnterpriseVo> enterprise = getDAO().getEnterprise( proSize, busIds);
			List<EnterpriseVo> enterpriseVos = new ArrayList<EnterpriseVo>();
			for (EnterpriseVo enterpriseVo : enterprise) {
				EnterpriseRegiste orig_enterprise = enterpriseService.findbyEnterpriteName(enterpriseVo.getName());
				Set<Resource> logoUrls = orig_enterprise!=null?orig_enterprise.getLogoAttachments():null;
				enterpriseVo.setLogo(logoUrls!=null&&logoUrls.size()>0?(logoUrls.iterator().next().getUrl()):"");
				if(enterpriseVo.getProductList()!=null&&enterpriseVo.getProductList().size()>0){
					for(ProductReportVo proVO:enterpriseVo.getProductList()){
						 /*处理产品图片*/
				        List<Resource> imgList = resourceService.getProductImgListByproId(proVO.getId());//查找产品图片集合
						//为了兼容老数据如果资源表里没有产品图片则从产品表imgUrl字段中取值
						if(imgList==null||imgList.size()==0){
							if(proVO.getImgUrl() != null){
								String[] imgUrlListArray = proVO.getImgUrl().split("\\|");
								for(String url:imgUrlListArray){
									Resource re=new Resource();
									re.setUrl(url);
									imgList.add(re);
								}
							}
						}
						if(imgList.size()>0){
							proVO.setImgUrl(imgList.get(0).getUrl());
						}
					}
				}
				enterpriseVos.add(enterpriseVo);
			}
			return enterpriseVos;
		} catch (DaoException dex) {
		   throw new ServiceException("", dex.getException());
		}
	}
	/**
     * 企业专栏生产企业总数
     * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/13
     */
	@Override
	public int countProductEnterprise() throws ServiceException {
		try {
			return getDAO().countProductEnterprise();
		} catch (DaoException dex) {
		   throw new ServiceException("", dex.getException());
		}
	}
	/**
     * 茶叶专栏
     * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/14
     */
	@Override
	public List<EnterpriseVo> getBusinessTeaUnit(int pageSize, int page,
			int proSize,String keyword) throws ServiceException {
		try {
			return  testResultDAO. getBusinessTeaUnit(pageSize, page, proSize,keyword);
		} catch (DaoException daoe) {
			throw new ServiceException("TestResultServiceImpl.getBusinessTeaUnit()-->"+daoe.getMessage(),daoe.getException());
		}
	}
   /**
     * 茶叶专栏企业总数
     * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/14
     */
	@Override
	public int countBusinessTeaUnit(String keyword) throws ServiceException {
		try {
			return  testResultDAO.countBusinessTeaUnit(keyword);
		} catch (DaoException daoe) {
			throw new ServiceException("TestResultServiceImpl.countBusinessTeaUnit()-->"+daoe.getMessage(),daoe.getException());
		}
	}

	/**
     * 审核通过结构化报告
	 * @author ZhangHui 2015/5/6
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void publishStructured(Long result_id) throws ServiceException {
		 try {
			getDAO().publishTestResult(result_id);
			getDAO().publishTestResultOfStructured(result_id);
		} catch (DaoException e) {
			throw new ServiceException("TestResultServiceImpl.publishStructured()-->"+e.getMessage(),e.getException());
		}
	}

	/**
	 * teslab审核退回结构化报告
	 * @param isSendBackToGYS
	 * 			true  代表 将报告直接退回至供应商
	 * 			false 代表 将报告退回至结构化人员 
	 * @author ZhangHui 2015/5/6
	 * 最近更新：ZhangHui 2015/6/29<br>
	 * 更新内容：增加status参数，根据报告有无被结构化，来将报告退回至结构化人员还是直接退回至供应商<br>
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void sendBackStructured(Long test_result_id, ReportBackVO reportBackVO, boolean isSendBackToGYS) throws ServiceException {
		try {
			TestResult testResult=this.findById(test_result_id);
			testResult.setBackTime(new Date());
			UploadUtil uploadUtil=new UploadUtil();
			for(Resource r:reportBackVO.getRepBackAttachments()){
				r.setFileName(uploadUtil.createFileNameByDate(r.getFileName()));
				r.setName(r.getFileName());
				uploadUtil.uploadFile(r.getFile(), PropertiesUtil.getProperty(FSN_FTP_UPLOAD_REPORT_BACK_PATH), r.getFileName());
				if(UploadUtil.IsOss()){
					r.setUrl(uploadUtil.getOssSignUrl(PropertiesUtil.getProperty(FSN_FTP_UPLOAD_REPORT_BACK_PATH)+"/"+r.getFileName()));
				}else{
					r.setUrl(PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_REPORT_BACK_PATH)+"/"+r.getFileName());
				}
			}
			testResult.setRepBackAttachments(reportBackVO.getRepBackAttachments());
			if(isSendBackToGYS){
				// 将未结构化报告直接退回至供应商
				testResult.setPublishFlag('7');
				testResult.setSuppliersBackCount(testResult.getSuppliersBackCount()+1);
				testResult.setBackResult( "【食安云退回】原因：" + reportBackVO.getReturnMes());
//				getDAO().sendBackToGYS(test_result_id, "【食安云退回】原因：" + reportBackVO.getReturnMes());
				if(reportBackVO.getStatus()!=null&&!"".equals(reportBackVO.getStatus())&&"1".equals(reportBackVO.getStatus())){
					//当带完善报告退回时改变状态为100
					String sql = "UPDATE test_result_handler SET `status` = 100 WHERE test_result_id = ?1";
					getDAO().sendBackOfStructuredToGYS(test_result_id,sql);
				}else{
					String sql = "UPDATE test_result_handler SET `status` = 12 WHERE test_result_id = ?1";
					getDAO().sendBackOfStructuredToGYS(test_result_id,sql);
				}
			}else{
				// 将已结构化报告退回至结构化人员
				testResult.setPublishFlag('2');
				testResult.setBackCount(testResult.getBackCount()+1);
				testResult.setBackResult( reportBackVO.getReturnMes());
//				getDAO().sendBackToJGH(test_result_id, reportBackVO.getReturnMes());
				getDAO().sendBackOfStructuredToJGH(test_result_id);
			}
			getDAO().merge(testResult);
		} catch (DaoException e) {
			throw new ServiceException("TestResultServiceImpl.sendBackStructured()-->"+e.getMessage(),e.getException());
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 从portal撤回报告
	 * @author ZhangHui 2015/5/6
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void goBackFromPortal(Long test_result_id) throws ServiceException {
		try {
			int count = getDAO().goBackTestResult(test_result_id);
			if(count < 1){
				count = getDAO().goBackTestResultOfDealer_0(test_result_id);
				if(count < 1){
					getDAO().goBackTestResultOfDealer_1(test_result_id);
				}
				getDAO().goBackTestResultOfStructured(test_result_id);
			}
		} catch (DaoException e) {
			throw new ServiceException("TestResultServiceImpl.sendBackStructured()-->"+e.getMessage(),e.getException());
		}
	}

	/**
	 * 根据报告id获取检测报告 （没有级联）
	 * @param id
	 * @author lxz 2015/5/6
	 */
	@Override
	public TestResult findByTestResultId(Long id) throws ServiceException {
		try {
			TestResult tr=getDAO().findByTestResultId(id);
			Product pro=productLFDAO.findProductBasicById(tr.getSample().getProduct().getId());
			/* 处理产品类别 */
			ProductCategoryInfo pc=productCategoryInfoService.findById(pro.getCategory().getId());
			pro.setCategory(pc);
			 /*处理产品图片*/
	        List<Resource> imgList = resourceService.getProductImgListByproId(pro.getId());
	        Set<Resource> set= new HashSet<Resource>();
	        set.addAll(imgList);
			pro.setProAttachments(set);
			tr.getSample().setProduct(pro);
			 /*处理产品图片*/
	        List<Resource> repBackAttachments = resourceService.getRebackImgListByreportId(id);
	        Set<Resource> repBackAttachmentsSet= new HashSet<Resource>();
	        repBackAttachmentsSet.addAll(repBackAttachments);
	        tr.setRepBackAttachments(repBackAttachmentsSet);
			
			return tr;
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("TestResultServiceImpl.sendBackStructured()-->"+e.getMessage(),e.getException());
		}
	}

	/**
	 * 根据条形码查询生产企业
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<TestBusinessUnitVo> getBusinessUnitVOList(String barcode, Integer page,
			Integer pageSize) {
		List<TestBusinessUnitVo> busVo = productLFDAO.getBusinessUnitVOList(barcode, page,pageSize);
		return busVo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TestBusinessUnitVo getBusinessUnitVO(String barcode, long buId,String date) {
		TestBusinessUnitVo vo = productLFDAO.getBusinessUnitVO(barcode, buId);
		if(vo!=null){
			List<TestRptJson> testResultList = testReportService.getTestResultList(barcode,buId,vo.getProductId(),"or",date);
			vo.setTestResultList(testResultList);
		}
		return vo;
	}
}