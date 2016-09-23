package com.gettec.fsnip.fsn.vo.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.gettec.fsnip.fsn.model.market.MkTempReport;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.test.ImportedProductTestResult;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;

/**
 * 用于封装报告录入页面的报告信息
 * @author ZhangHui 2015/6/4 
 */
public class ReportOfMarketVO {
	
	/**
	 * id
	 */
	private Long id;
	
	/**
	 * 报告编号
	 */
	private String serviceOrder;

	/**
	 * 被检测单位/人
	 */
	private String testee;

	/**
	 * 抽样数量
	 */
	private String sampleQuantity;
	
	/**
	 * 抽样地点
	 */
	private String samplingLocation;
	
	/**
	 * 检验地点
	 */
	private String testPlace;
	
	/**
	 * 检验日期字符串
	 */
	private String testDateStr;
	
	/**
	 * 检验日期
	 */
	private Date testDate;
	
	/**
	 * 报告有效期至
	 */
	private String endDateStr;
	
	/**
	 * 检验类别
	 */
	private String testType;
	
	/**
	 * 执行标准
	 */
	private String standard;
	
	/**
	 * 检测结果描述
	 */
	private String result;
	
	/**
	 * 检验结论
	 */
	private boolean pass;
	
	/**
	 * 备注
	 */
	private String comment;
	
	/**
	 * 进口食品报告信息
	 */
	private ImportedProductTestResult impProTestResult;
	
	/**
	 * 当前报告是否为退回报告
	 *		true  代表 此报告是退回报告
	 *		false 代表 此报告不是退回报告
	 */
	private boolean send_back;
	
	/**
	 * 退回原因
	 */
	private String backResult;

	/**
	 * 是否为自动生成报告标识
	 * 		true  是
	 * 		false 否
	 */
	private boolean autoReportFlag;
	
	/**
	 * 检验机关
	 */
	private String testOrgnization;

	
	/**
	 * 生产企业
	 */
	private BusinessUnitOfReportVO bus_vo;
	
	/**
	 * 产品
	 */
	private ProductOfMarketVO product_vo;
	
	/**
	 * 检测项目
	 */
	private List<TestProperty> testProperties = new ArrayList<TestProperty>();
	
	/**
	 * pdf
	 */
	private Set<Resource> repAttachments = new HashSet<Resource>();
	
	/**
	 * 当前报告对应产品关联的qs号列表
	 */
	private List<BusinessUnitOfReportVO> pro2Bus;
	
	/**
	 * 当前报告对应产品已绑定qs号的企业列表
	 */
	private List<Map<Object,String>> listOfBusunitName;
	
	/**
	 * true  代表 新增报告
	 * false 代表 编辑报告
	 */
	private boolean new_flag;
	
	/**
	 * dealer             代表 供应商
	 * icb                代表 工商数据
	 * lims2                                   代表 lims2.0 数据
	 * parse_pdf          代表 解析pdf数据(目前只给蒙牛提供此功能)
	 * receive_from_lims  代表 从lims接收
	 */
	private String dbflag;
	
	/**
	 * 报告发布标记
	 * 		0 代表 testlab正在审核;
	 * 		1 代表 testlab通过审核;
	 * 		2 代表 testlab退回至企业;
	 * 		3 代表 发布人员未发布到testlab
	 * 		4 代表 经销商提交，但未通过商超审核
	 * 		5 代表 商超退回至供应商
	 * 		6 代表 商超审核通过
	 * 		7 代表 [食安云]将报告直接退回至供应商
	 */
	private char publishFlag;
	
	/**
	 * 总共两个值：
	 * 		fullPdfPath         表示 完整的pdf路径
	 * 		interceptionPdfPath 表示 截取前两页后的pdf路径
	 */
	private Map<String, String> map;
	
	/**
	 * 当报告编号不包括中文时，当前字段的值等于报告编号
	 * 当报告编号包含中文时，当前字段会自动生成一串不包含中文的编号
	 * （用于报告上传路径中使用）
	 */
	private String reportNOEng;
	
	/**
	 * 关于报告的用户提示消息
	 */
	private String tip;
	
	
	private long count; 
	
	private Set<Resource> repBackAttachments = new HashSet<Resource>();
	
	private Set<Resource> reportImgList = new HashSet<Resource>();//报告原件
	private Set<Resource> checkImgList = new HashSet<Resource>();//检测报告
	private Set<Resource> buyImgList = new HashSet<Resource>();//购买凭证
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceOrder() {
		return serviceOrder;
	}

	public void setServiceOrder(String serviceOrder) {
		this.serviceOrder = serviceOrder;
	}

	public String getTestee() {
		return testee;
	}

	public void setTestee(String testee) {
		this.testee = testee;
	}

	public String getSampleQuantity() {
		return sampleQuantity;
	}

	public void setSampleQuantity(String sampleQuantity) {
		this.sampleQuantity = sampleQuantity;
	}

	public String getSamplingLocation() {
		return samplingLocation;
	}

	public void setSamplingLocation(String samplingLocation) {
		this.samplingLocation = samplingLocation;
	}

	public String getTestPlace() {
		return testPlace;
	}

	public void setTestPlace(String testPlace) {
		this.testPlace = testPlace;
	}

	public String getTestDateStr() {
		return testDateStr;
	}

	public void setTestDateStr(String testDateStr) {
		this.testDateStr = testDateStr;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ImportedProductTestResult getImpProTestResult() {
		return impProTestResult;
	}

	public void setImpProTestResult(ImportedProductTestResult impProTestResult) {
		this.impProTestResult = impProTestResult;
	}

	public String getBackResult() {
		return backResult;
	}

	public void setBackResult(String backResult) {
		this.backResult = backResult;
	}

	public boolean isAutoReportFlag() {
		return autoReportFlag;
	}

	public void setAutoReportFlag(boolean autoReportFlag) {
		this.autoReportFlag = autoReportFlag;
	}

	public String getTestOrgnization() {
		return testOrgnization;
	}

	public void setTestOrgnization(String testOrgnization) {
		this.testOrgnization = testOrgnization;
	}

	public BusinessUnitOfReportVO getBus_vo() {
		return bus_vo;
	}

	public void setBus_vo(BusinessUnitOfReportVO bus_vo) {
		this.bus_vo = bus_vo;
	}

	public ProductOfMarketVO getProduct_vo() {
		return product_vo;
	}

	public void setProduct_vo(ProductOfMarketVO product_vo) {
		this.product_vo = product_vo;
	}

	public List<TestProperty> getTestProperties() {
		return testProperties;
	}

	public void setTestProperties(List<TestProperty> testProperties) {
		this.testProperties = testProperties;
	}

	public Set<Resource> getRepAttachments() {
		return repAttachments;
	}

	public void setRepAttachments(Set<Resource> repAttachments) {
		this.repAttachments = repAttachments;
	}

	public List<BusinessUnitOfReportVO> getPro2Bus() {
		return pro2Bus;
	}

	public void setPro2Bus(List<BusinessUnitOfReportVO> pro2Bus) {
		this.pro2Bus = pro2Bus;
	}

	public boolean isSend_back() {
		return send_back;
	}

	public void setSend_back(boolean send_back) {
		this.send_back = send_back;
	}

	public List<Map<Object, String>> getListOfBusunitName() {
		return listOfBusunitName;
	}

	public void setListOfBusunitName(List<Map<Object, String>> listOfBusunitName) {
		this.listOfBusunitName = listOfBusunitName;
	}
	
	public boolean isNew_flag() {
		return new_flag;
	}

	public void setNew_flag(boolean new_flag) {
		this.new_flag = new_flag;
	}
	
	public char getPublishFlag() {
		return publishFlag;
	}

	public void setPublishFlag(char publishFlag) {
		this.publishFlag = publishFlag;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public String getReportNOEng() {
		return reportNOEng;
	}

	public void setReportNOEng(String reportNOEng) {
		this.reportNOEng = reportNOEng;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getDbflag() {
		return dbflag;
	}

	public void setDbflag(String dbflag) {
		this.dbflag = dbflag;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public ReportOfMarketVO(){
		super();
	}
	
	public ReportOfMarketVO(MkTempReport tempReport) {
		super();
		
		this.serviceOrder = tempReport.getReportNo();
		this.testOrgnization = tempReport.getTestOrgnizName();
		this.testType = tempReport.getTestType();
		this.pass = tempReport.getConclusion().equals("合格")?true:false;
		this.sampleQuantity = tempReport.getSampleCount();
		this.standard = tempReport.getJudgeStandard();
		this.result = tempReport.getTestResultDescribe();
		this.comment = tempReport.getRemark();
		this.testPlace=tempReport.getTestPlace();
		this.samplingLocation = tempReport.getSamplePlace();
		this.testee = tempReport.getTesteeName();
		
		if(tempReport.getTestDate() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			this.testDateStr = sdf.format(tempReport.getTestDate());
		}
	}

	public ReportOfMarketVO(TestResult report) {
		super();
		
		if(report == null){
			return;
		}
		
		this.id = report.getId();
		this.serviceOrder = report.getServiceOrder();
		
		if(report.getTestee() != null){
			this.testee = report.getTestee().getName();
		}
		this.sampleQuantity = report.getSampleQuantity();
		this.samplingLocation = report.getSamplingLocation();
		this.testPlace = report.getTestPlace();
		this.testType = report.getTestType();
		this.standard = report.getStandard();
		this.result = report.getResult();
		this.pass = report.getPass();
		this.comment = report.getComment();
		this.autoReportFlag = report.isAutoReportFlag();
		this.testOrgnization = report.getTestOrgnization();
		this.send_back = report.getPublishFlag()==2?true:false;
		this.backResult = report.getBackResult();
		this.repAttachments = report.getRepAttachments();
		this.publishFlag = report.getPublishFlag();
		this.repBackAttachments=report.getRepBackAttachments();
		this.reportImgList=report.getReportImgList();
		this.checkImgList=report.getCheckImgList();
		this.buyImgList=report.getBuyImgList();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(report.getTestDate() != null){
			this.testDateStr = sdf.format(report.getTestDate());
		}
		if(report.getEndDate() != null){
			this.endDateStr = sdf.format(report.getEndDate());
		}
		
		this.product_vo = new ProductOfMarketVO(report.getSample());
		
		this.bus_vo = new BusinessUnitOfReportVO(report.getSample().getProducer());
		this.bus_vo.setQs_vo(product_vo.getQs_info());
	}

	public Set<Resource> getRepBackAttachments() {
		return repBackAttachments;
	}

	public void setRepBackAttachments(Set<Resource> repBackAttachments) {
		this.repBackAttachments = repBackAttachments;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Set<Resource> getReportImgList() {
		return reportImgList;
	}

	public void setReportImgList(Set<Resource> reportImgList) {
		this.reportImgList = reportImgList;
	}

	public Set<Resource> getCheckImgList() {
		return checkImgList;
	}

	public void setCheckImgList(Set<Resource> checkImgList) {
		this.checkImgList = checkImgList;
	}

	public Set<Resource> getBuyImgList() {
		return buyImgList;
	}

	public void setBuyImgList(Set<Resource> buyImgList) {
		this.buyImgList = buyImgList;
	}
	
}
