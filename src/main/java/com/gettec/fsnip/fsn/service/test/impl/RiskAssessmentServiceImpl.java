package com.gettec.fsnip.fsn.service.test.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.test.RiskAssessmentDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.test.RiskAssessment;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.ProductInstanceService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.test.RiskAssessmentService;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.service.test.TestResultService;
import com.gettec.fsnip.fsn.util.ListSort;
import com.gettec.fsnip.fsn.vo.RiskResultVO;


/**
 * @author ZhaWanNeng<br>
 * 最近更新时间：2015/3/25
 */
@Service("riskAssessmentService")
public class RiskAssessmentServiceImpl extends BaseServiceImpl<RiskAssessment, RiskAssessmentDAO>  implements RiskAssessmentService{
	@Autowired protected RiskAssessmentDAO riskAssessmentDAO;
	@Autowired protected ProductInstanceService productInstanceService;
	@Autowired private ProductService productService;
	@Autowired protected TestPropertyService testPropertyService;
	@Autowired protected TestResultService resultService;
	@Override
	public RiskAssessmentDAO getDAO() {
		return riskAssessmentDAO;
	}
	/**
	 * 根据product计算保存综合污染指数
	 * @param product 产品 
	 * @param userName  用户
	 * @return 
	 * @throws ServiceException
	 * @author ZhaWanNeng<br>
     * 最近更新时间：2015/3/25
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Boolean  calculate(Product product ,String userName) throws ServiceException{
		try {
			/* 获取检测项目*/
			if(product.getBarcode() == null || product.getBarcode()==""|| product.getBusinessBrand()==null){
				saveRisk(null, product.getId(), false, "产品条形码不能为空", userName, new Date(), null);
				return false;
			}
			List<TestProperty> riskAssessment = riskAssessment(product.getBarcode());
			/* 检测项目是否满足三份*/
			if(riskAssessment == null || riskAssessment.size() == 0){
				product.setRisk_succeed(false);
				product.setRiskFailure("报告不足三份");
				productService.update(product);
				saveRisk(null, product.getId(), false, "报告不足三份", userName, new Date(), null);
				return false;
			}
			List<Double> p = new ArrayList<Double>();//用于存放单个风险指数
			String propertyName ="";                 //用于记录检测项目的名称
			
			RiskAssessment riskAss = new RiskAssessment();
			riskAss.setProductid(product.getId());
			/* 根据检测项目把计算的单个风险指数放入List*/
			Set<TestProperty> testPropertys = new HashSet<TestProperty>();
			for (TestProperty testProperty : riskAssessment) {
				testPropertys.add(testProperty);
				Double riskIndex = riskIndex(testProperty);
				if(riskIndex != null){
					p.add(riskIndex);
					if(propertyName.length()<950){
						propertyName=propertyName+testProperty.getName()+"|";
					}
				}
			}
			if(p.size() <= 0){
				product.setRisk_succeed(false);
				product.setRiskFailure("检测项目数据有误");
				productService.update(product);
				saveRisk(null, product.getId(), false, "检测项目数据有误", userName, new Date(), testPropertys);
				return false;
			}
			//综合污染指数
			Double riskIndexs = riskIndexs(p);
			//保存综合污染指数历史数据
			saveRisk(riskIndexs, product.getId(), true, "成功", userName, new Date(), testPropertys);
			//添加综合污染指数到产品中
			product.setRiskIndex(riskIndexs);
			product.setRiskIndexDate(new Date());
			product.setTestPropertyName(propertyName);
			product.setRisk_succeed(true);
			productService.update(product);
			return true;
		} catch (ServiceException e) {
			throw new ServiceException("RiskAssessmentServiceImpl.saveRisk()-->"+e.getMessage(),e.getException());
		}
	}
	/**
	 * 根据产品条形码把检测项目添加到List中
	 * @param barcode 条形码
	 * @return
	 * @throws ServiceException
	 * @author ZhaWanNeng<br>
     * 最近更新时间：2015/3/25
	 */
	private List<TestProperty>  riskAssessment(String barcode) throws ServiceException{
//		ListSort<TestResult> listSort= new ListSort<TestResult>();
		try {
			List<TestProperty> testPropertys = new ArrayList<TestProperty>();
			/*1. 获取产品*/
			if(barcode == null || barcode==""){
				return null;
			}
			Product orig_product = productService.findByBarcode(barcode);
			/*2. 获取样品*/
			List<Long> productInstanceID = productInstanceService.findInstancebyProductId(orig_product.getId());
			//用来存报告id
			Set<Long> reportsId = new HashSet<Long>();
			List<RiskResultVO> list = new ArrayList<RiskResultVO>();
			/*3.1第一次获取送检报告*/
			for (int i = 0; i < productInstanceID.size(); i++) {
				List<RiskResultVO> orig_testResults = resultService.findTRByProductInstance(productInstanceID.get(i),true);
				for (RiskResultVO testResult : orig_testResults) {
					list.add(testResult);
				}
			}
			//list按时间排序
			ListSort<RiskResultVO>  listSort = new ListSort<RiskResultVO>();
			listSort.Sort(list, "getTestDate", "desc");
			int number = addTestPropertyTOList(list, testPropertys,reportsId);//number报告的分数
			/*3.2 再去重新查询出自检报告和抽检报告按照日期从近到远排序，选取日期最近且检验标准为国标的以满足三份报告的要求*/
			if(number<3){
				List<RiskResultVO> listOTH = new ArrayList<RiskResultVO>();
				for (int i = 0; i < productInstanceID.size(); i++) {
					List<RiskResultVO > orig_testResults = resultService.findTRByProductInstance(productInstanceID.get(i),false);
					for (RiskResultVO testResult : orig_testResults) {
						listOTH.add(testResult);
					}
				}
				listSort.Sort(listOTH, "getTestDate", "desc");
				int  OthTestNM = addTestPropertyTOList(listOTH, testPropertys,reportsId);
				if(OthTestNM < 3){
					return null;
				}
				return testPropertys;
			}
			return testPropertys;
		} catch (ServiceException e) {
			throw new ServiceException("RiskAssessmentServiceImpl.riskAssessment()-->"+e.getMessage(),e.getException());
		}
	}
	/**
	 * 从检测报告把检测项目添加到集合中
	 * @param orig_testResults 检测报告
	 * @param testPropertys  检测项目
	 * @author ZhaWanNeng<br>
     * 最近更新时间：2015/3/25
	 */
	private int  addTestPropertyTOList(List<RiskResultVO> testResultList,List<TestProperty> testPropertys,Set<Long> reportsId) throws ServiceException{
//		Set<Long> set1 = new HashSet<Long>();
		try {
			for (RiskResultVO testResults : testResultList) {
				List<TestProperty> findByTestResult = testPropertyService.findByReportId(testResults.getId());
				for (TestProperty testProperty : findByTestResult) {
					String standard = testProperty.getStandard();
	                //判断检验项目的执行标准是否有GB
					if(standard!=null&&standard.contains("GB")){
						reportsId.add(testResults.getId());
						testPropertys.add(testProperty);
					}
	               //先判断检验项目的执行标准是否为空，如果为空在去判断报告的执行标准
					if("".equals(standard)||standard == null||standard == "null"){
						if(testResults.getStandard() != null&&testResults.getStandard().contains("GB")&&!testResults.getStandard().contains("DB")){
							reportsId.add(testResults.getId());
							testPropertys.add(testProperty);
						}
					}
				}
				if(reportsId.size() >= 3){
					return reportsId.size();
				}
			}
			return reportsId.size();
		} catch (ServiceException e) {
			throw new ServiceException("RiskAssessmentServiceImpl.addTestPropertyTOList()-->"+e.getMessage(),e.getException());
		}
	}
	/**
	 * 计算每个检测项目的单项污染指数p
	 * @param property 检测项目
	 * @return 单项污染指数p
	 * @author ZhaWanNeng<br>
     * 最近更新时间：2015/3/25
	 */
	private Double  riskIndex(TestProperty property) throws ServiceException{
		try {
			String result = property.getResult();//实测值
			String techIndicator = property.getTechIndicator();//国家标准中食品安全指标
			techIndicator = techIndicator.replaceAll(" ", "");
			result = result.replaceAll(" ", "");
			boolean matches = false;
			boolean resultmatches = false;
			if(techIndicator!=null && (techIndicator.matches("[<≤≦]?-?[0-9]+(\\.[0-9]+)?")||techIndicator.matches("(<=)?-?[0-9]+(\\.[0-9]+)?")||techIndicator.matches("(<<)?-?[0-9]+(\\.[0-9]+)?"))){
				 matches = true;
			}
			if(result != null && (result.matches("[<≤≦]?-?[0-9]+(\\.[0-9]+)?")||result.matches("(<=)?-?[0-9]+(\\.[0-9]+)?")||result.matches("(<<)?-?[0-9]+(\\.[0-9]+)?"))){
				resultmatches = true;
			}
			Double p = null;
			if(matches && resultmatches==false && "合格".equals(property.getAssessment())){
				p=0.5;
			}
			if(matches&&resultmatches){
				//去掉"<，≤"
				techIndicator =techIndicator.replaceAll("<", "");
				techIndicator = techIndicator.replaceAll("≤", "");
				techIndicator = techIndicator.replaceAll("≦", "");
				techIndicator = techIndicator.replaceAll("=", "");
				result = result.replaceAll("<", "");
				result = result.replaceAll("≤", "");
				result = result.replaceAll("≦", "");
				result = result.replaceAll("=", "");
				double s = Double.parseDouble(techIndicator);
				double c = Double.parseDouble(result);
				if(s<c || s == 0){
					return p;
				}
				p = c/s;
			}
			return p;
		} catch (Exception e) {
			throw new ServiceException("RiskAssessmentServiceImpl.riskIndex()-->"+e.getMessage(),null);
		}
	}
	/**
	 * 计算综合污染指数(p综)
	 * @param p 单个风险指数
	 * @return 综合污染指数(p综)
	 * @throws Exception 
	 * @author ZhaWanNeng<br>
     * 最近更新时间：2015/3/25
	 */
	private  Double  riskIndexs(List<Double> p) throws ServiceException{
		try {
			double average = 0.0;
			double Maxp = 0.0;
			for (Double temporary : p) {
				average = average+temporary;
			}
			for (int i = 0; i < p.size(); i++){
	            if (p.get(i) > Maxp){
	            	Maxp = p.get(i);
	            }
	         }
			average = average/p.size();
			double comP = Math.sqrt((average*average+Maxp*Maxp)/2);
			DecimalFormat  df = new DecimalFormat("#######0.00");
			String comPStr = df.format(comP);
			comP = Double.valueOf(comPStr);
			return comP;
		} catch (Exception e) {
			throw new ServiceException("RiskAssessmentServiceImpl.riskIndexs()-->"+e.getMessage(),null);
		}
		
	}
	/**
	 * 日期加3个月
	 * @author ZhaWanNeng<br>
     * 最近更新时间：2015/3/25
	 */
	@SuppressWarnings("unused")
	private Date subtractDate(Date date) throws Exception {
		try {
	        Calendar rightNow = Calendar.getInstance();
	        rightNow.setTime(date);
	        rightNow.add(Calendar.MONTH,3);//日期加3个月
	        Date dt=rightNow.getTime();
		    return dt;
		} catch (Exception e) {
			throw new Exception("RiskAssessmentServiceImpl.subtractDate()-->"+e.getMessage());
		}
	}
	/**
	 * 保存检测项目历史数据
	 * @param riskIndex 
	 * @param productid
	 * @param risk_succeed
	 * @param riskFailure
	 * @param userName
	 * @param riskDate
	 * @param testPropertys
	 * @author ZhaWanNeng<br>
     * 最近更新时间：2015/3/25
	 */
	private void saveRisk(Double riskIndex, Long productid,
			Boolean risk_succeed, String riskFailure, String userName,
			Date riskDate, Set<TestProperty> testPropertys) throws ServiceException {
		try {
			RiskAssessment riskAss =  new RiskAssessment();
			riskAss.setProductid(productid);
			riskAss.setRisk_succeed(risk_succeed);
			riskAss.setRiskDate(riskDate);
			riskAss.setRiskFailure(riskFailure);
			riskAss.setRiskIndex(riskIndex);
			riskAss.setTestPropertys(testPropertys);
			riskAss.setUserName(userName);
			create(riskAss);
		} catch (ServiceException e) {
			throw new ServiceException("RiskAssessmentServiceImpl.saveRisk()-->"+e.getMessage(),e.getException());
		}
	}
	/**
     * 根据检测项目id获取风险指数的历史数据 
     * @author ZhaWanNeng
     */
	@Override
	public Long getRiskAssessmentId(Long propertyid) throws ServiceException {
		try {
			Long riskAssessmentid = riskAssessmentDAO.getRiskAssessmentId(propertyid);
			return riskAssessmentid;
		} catch (DaoException e) {
			throw new ServiceException("RiskAssessmentServiceImpl.saveRisk()-->"+e.getMessage(),e.getException());
		}
	}
	/**
	 * 根据报告id删除风险指数的历史数据 
	 * @author ZhaWanNeng
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deletRiskAssess(Long reportId) throws ServiceException {
		try {
			List<TestProperty> findByTestResult = testPropertyService.findByReportId(reportId);
			if(findByTestResult != null && findByTestResult.size() > 0){
				Long riskAssessmentId = getRiskAssessmentId(findByTestResult.get(0).getId());
				if(riskAssessmentId == null) {
					return false;
				}
				RiskAssessment findById = findById(riskAssessmentId);
				if(findById != null){
					delete(findById);
				}
			}
			return true;
		} catch (Exception e) {
			throw new ServiceException("deletRiskAssess.saveRisk()-->"+e.getMessage(),null);
		}
	}
	
}
