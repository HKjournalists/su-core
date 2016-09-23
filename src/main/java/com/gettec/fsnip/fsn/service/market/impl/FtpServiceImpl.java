package com.gettec.fsnip.fsn.service.market.impl;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.xml.rpc.ServiceException;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.FieldValue;
import com.gettec.fsnip.fsn.model.business.ProducingDepartment;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.service.market.FtpService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.market.ResourceTypeService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;
import com.gettec.fsnip.fsn.vo.report.ReportOfMarketVO;

@Service(value="ftpService")
public class FtpServiceImpl implements FtpService{
	@Autowired protected ResourceService testResourceService;
	@Autowired private ResourceTypeService resourceTypeService; 
	
	@Autowired ServletContext servletContext;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 字符串处理，替换‘&’ 符号
	 * @author tangxin 2015-05-26
	 */
	private String formatStrVal(String value){
		return value == null ? "" : value.replace("&", "﹠");
	}
	
	/**
	 * 用XML传值方式自动生成pdf
	 * @throws ServiceException
	 * @author LongXianZhen
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ByteArrayInputStream mkUploadReportPdf(ReportOfMarketVO report_vo){
		try {
			if(report_vo==null || report_vo.getProduct_vo()==null){
				throw new Exception("参数为空");
			}
			
			ProductOfMarketVO product_vo = report_vo.getProduct_vo();
			
			File f = new File(servletContext.getRealPath("/WEB-INF/Com_lims.jrxml"));
			InputStream JRxml = new FileInputStream(f); 
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			HashMap<String, String> headMap = new HashMap<String, String>();		
			headMap.put("报告编号",formatStrVal(report_vo.getServiceOrder()));
			headMap.put("产品名称",formatStrVal(product_vo.getName()));
			headMap.put("批次号",formatStrVal(product_vo.getBatchSerialNo()));
			headMap.put("规格",formatStrVal(product_vo.getFormat()==null||product_vo.getFormat().equals("")?" ":product_vo.getFormat()));
			headMap.put("产品状态",formatStrVal(product_vo.getStatus()==null||product_vo.getStatus().equals("")?" ":product_vo.getStatus()));
			headMap.put("商标",formatStrVal(product_vo.getBrand_name()));
			Date proDate = product_vo.getProductionDate();
			headMap.put("生产日期",(proDate==null?"0000-00-00":formatter.format(proDate)));
			headMap.put("抽样数量",formatStrVal(report_vo.getSampleQuantity()==null||report_vo.getSampleQuantity().equals("")?" ":report_vo.getSampleQuantity()));
			headMap.put("抽样基数"," ");
			headMap.put("检测日期",formatStrVal(report_vo.getTestDate()==null?" ":formatter.format(report_vo.getTestDate())));
			headMap.put("抽样地点",formatStrVal(report_vo.getSamplingLocation()==null||report_vo.getSamplingLocation().equals("")?" ":report_vo.getSamplingLocation()));
			headMap.put("判定依据",formatStrVal(report_vo.getStandard()==null||report_vo.getStandard().equals("")?" ":report_vo.getStandard()));
			headMap.put("主要仪器",formatStrVal(" "));
			headMap.put("检验结论",formatStrVal(report_vo.getResult()==null||report_vo.getResult().equals("")?" ":report_vo.getResult()));
			headMap.put("备注",formatStrVal(report_vo.getComment()==null||report_vo.getComment().equals("")?" ":report_vo.getComment()));
			headMap.put("主检",formatStrVal(" "));
			headMap.put("审核",formatStrVal(" "));
			List<TestProperty> tps = report_vo.getTestProperties();
			List<HashMap<String, String>> bodyMaps=new ArrayList<HashMap<String,String>>();
			for(TestProperty tp:tps){
				HashMap<String, String> bodyMap = new HashMap<String, String>();				
				bodyMap.put("检测项目",tp.getName()==null?"":"<![CDATA["+tp.getName()+"]]>"/*.replace("<", "&lt;").replace(">", "&gt;")*/
						.replace("₀", "0").replace("₁","1").replace("₂","2")
						.replace("₃","3").replace("₄","4").replace("₅","5")
						.replace("₆","6").replace("₇","7").replace("₈","8")
						.replace("₉","9").replace("⁴","4").replace("⁵","5")
						.replace("⁶","6").replace("⁷","7").replace("⁸","8")
						.replace("⁹","9").replace("&", "﹠"));
				bodyMap.put("计量单位",tp.getUnit()==null?"":"<![CDATA["+tp.getUnit()+"]]>"/*.replace("<", "&lt;").replace(">", "&gt;")*/
						.replace("₀", "0").replace("₁","1").replace("₂","2")
						.replace("₃","3").replace("₄","4").replace("₅","5")
						.replace("₆","6").replace("₇","7").replace("₈","8")
						.replace("₉","9").replace("⁴","4").replace("⁵","5")
						.replace("⁶","6").replace("⁷","7").replace("⁸","8")
						.replace("⁹","9").replace("&", "﹠"));
				bodyMap.put("标准要求",tp.getTechIndicator()==null?"":"<![CDATA["+tp.getTechIndicator()+"]]>"/*.replace("<", "&lt;").replace(">", "&gt;")*/
						.replace("₀", "0").replace("₁","1").replace("₂","2")
						.replace("₃","3").replace("₄","4").replace("₅","5")
						.replace("₆","6").replace("₇","7").replace("₈","8")
						.replace("₉","9").replace("⁴","4").replace("⁵","5")
						.replace("⁶","6").replace("⁷","7").replace("⁸","8")
						.replace("⁹","9").replace("&", "﹠"));
				bodyMap.put("检测结果",tp.getResult()==null?"":"<![CDATA["+tp.getResult()+"]]>"/*.replace("<", "&lt;").replace(">", "&gt;")*/
						.replace("₀", "0").replace("₁","1").replace("₂","2")
						.replace("₃","3").replace("₄","4").replace("₅","5")
						.replace("₆","6").replace("₇","7").replace("₈","8")
						.replace("₉","9").replace("⁴","4").replace("⁵","5")
						.replace("⁶","6").replace("⁷","7").replace("⁸","8")
						.replace("⁹","9").replace("&", "﹠"));
				bodyMap.put("结论",tp.getAssessment()==null?"":"<![CDATA["+tp.getAssessment()+"]]>"/*.replace("<", "&lt;").replace(">", "&gt;")*/
						.replace("₀", "0").replace("₁","1").replace("₂","2")
						.replace("₃","3").replace("₄","4").replace("₅","5")
						.replace("₆","6").replace("₇","7").replace("₈","8")
						.replace("₉","9").replace("⁴","4").replace("⁵","5")
						.replace("⁶","6").replace("⁷","7").replace("⁸","8")
						.replace("⁹","9").replace("&", "﹠"));
				bodyMaps.add(bodyMap);
			}
			String reportstr=getXMLstring(headMap,bodyMaps);
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			InputStream in_nocode = new ByteArrayInputStream(reportstr.getBytes("UTF-8"));
			JRXmlDataSource xmlDataSource = new JRXmlDataSource(in_nocode);
			JasperReport jasperReport = JasperCompileManager.compileReport(JRxml);
			byte[] bytes = null;
			bytes = JasperRunManager.runReportToPdf(jasperReport, parameterMap, xmlDataSource);
			return new ByteArrayInputStream(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private String getXMLstring(HashMap<String, String> headMap,
			List<HashMap<String, String>> bodyMaps) {
		String reportstr = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
		reportstr = reportstr + " <检测报告> <表头>";
		Iterator it = headMap.entrySet().iterator(); 
		while(it.hasNext()){
			Map.Entry entry = (Map.Entry) it.next(); 
			String xmlRow = " <"+entry.getKey()+">"+entry.getValue()+"</"+entry.getKey()+"> ";
			reportstr = reportstr + xmlRow;
		}
		reportstr = reportstr + " </表头> <表体>";
		for(HashMap<String, String> bodyMap:bodyMaps){
			reportstr = reportstr +"<项目>";
			Iterator ite = bodyMap.entrySet().iterator(); 
			while(ite.hasNext()){
				Map.Entry entry = (Map.Entry) ite.next(); 
				String xmlRow = " <"+entry.getKey()+">"+entry.getValue().toString()+"</"+entry.getKey()+"> ";
				reportstr = reportstr + xmlRow;
			}
			reportstr = reportstr +"</项目>";
		}
		reportstr = reportstr + "</表体> </检测报告> ";
		return reportstr;
	}
	
	private String getFieldValByFieldId(List<FieldValue> fieldValues,Long id) throws Exception{
		String result="";
		if(fieldValues==null||fieldValues.size()<1){return "";}
		for(FieldValue fv:fieldValues){
			if(fv.getField().getId().equals(id)){
				if(fv.getValue()!=null&&fv.getValue().length()>0){
					result+=fv.getValue()+",";
				}
				if(!id.equals(32L)){break;}
			}
		}
		return result.length()==0?"":result.substring(0,result.length()-1);
	}
	
	private String getStr(String value) throws Exception{
		return value==null?"":value;
	}
	
	private String formatDateToStr(Date date) throws Exception{
		return date!=null?sdf.format(date):"";
	}
	
	/**
	 * 用XML传值方式将企业信息生成pdf
	 * @param busUnit
	 * @return Resource
	 * @author Long Xian Zhen
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Resource mkUploadBusinessPdf(BusinessUnit busUnit, List<ProductionLicenseInfo> ListproLicense) {
		try {
//			InputStream JRxml = FtpServiceImpl.class.getResourceAsStream("rhjy_busUnit.jrxml");
			File f = new File(servletContext.getRealPath("/WEB-INF/rhjy_busUnit.jrxml"));
			InputStream JRxml = new FileInputStream(f); 
			StringBuilder busUnitStr=new StringBuilder();
			busUnitStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			//企业基本信息1
			busUnitStr.append("<仁怀白酒生产企业><企业基本信息1><表头>");
			busUnitStr.append("<企业名称>"+getStr(busUnit.getName())+"</企业名称>");
			busUnitStr.append("<法人代表>"+getStr(busUnit.getPersonInCharge())+"</法人代表>");
			busUnitStr.append("<联系电话>"+getStr(busUnit.getTelephone())+"</联系电话>");
			busUnitStr.append("<公司办公地址>"+getStr(busUnit.getAddress())+"</公司办公地址>");
			busUnitStr.append("<办公电话>"+getFieldValByFieldId(busUnit.getFieldValues(),37L)+"</办公电话>");
			busUnitStr.append("<主任电话>"+getFieldValByFieldId(busUnit.getFieldValues(),1L)+"</主任电话>");
			busUnitStr.append("<主要股东>"+getFieldValByFieldId(busUnit.getFieldValues(),32L)+"</主要股东>");
			//生产车间布点情况
			busUnitStr.append("</表头><表体>");
			List<ProducingDepartment> subDep=busUnit.getProDepartments();
			int len=(subDep!=null&&subDep.size()<13)?13:subDep.size();
			for(int i=0;i<len;i++){
				if(subDep!=null&&i<subDep.size()){
					busUnitStr.append("<项目><名称>"+getStr(subDep.get(i).getName())+"</名称>");
					busUnitStr.append("<详细地址>"+getStr(subDep.get(i).getAddress())+"</详细地址>");
					busUnitStr.append("<负责人>"+getStr(subDep.get(i).getLegalName())+"</负责人>");
					busUnitStr.append("<联系电话>"+getStr(subDep.get(i).getTelephone())+"</联系电话></项目>");
				}else{
					busUnitStr.append("<项目><名称> </名称>");
					busUnitStr.append("<详细地址> </详细地址>");
					busUnitStr.append("<负责人> </负责人>");
					busUnitStr.append("<联系电话> </联系电话></项目>");
				}
			}
			busUnitStr.append("</表体></企业基本信息1>");
			//企业基本情况表2
			busUnitStr.append("<企业基本信息2><表头>");
			List<FieldValue> fieldValues=busUnit.getFieldValues();
			busUnitStr.append("<企业占地面积平方米>"+getFieldValByFieldId(fieldValues,2L)+"</企业占地面积平方米>");
			busUnitStr.append("<总建筑面积平方米>"+getFieldValByFieldId(fieldValues,3L)+"</总建筑面积平方米>");
			busUnitStr.append("<制酒车间数个>"+getFieldValByFieldId(fieldValues,4L)+"</制酒车间数个>");
			busUnitStr.append("<制酒车间建筑面积平方米>"+getFieldValByFieldId(fieldValues,5L)+"</制酒车间建筑面积平方米>");
			busUnitStr.append("<窖池2013建成数>"+getFieldValByFieldId(fieldValues,33L)+"</窖池2013建成数>");
			busUnitStr.append("<窖池2013年底投产数>"+getFieldValByFieldId(fieldValues,34L)+"</窖池2013年底投产数>");
			busUnitStr.append("<窖池2014在建窖池数>"+getFieldValByFieldId(fieldValues,35L)+"</窖池2014在建窖池数>");
			busUnitStr.append("<窖池2014年计划投产数>"+getFieldValByFieldId(fieldValues,36L)+"</窖池2014年计划投产数>");
			busUnitStr.append("<生产能力大曲酱香酒>"+getFieldValByFieldId(fieldValues,25L)+"</生产能力大曲酱香酒>");
			busUnitStr.append("<生产能力碎沙酱香酒>"+getFieldValByFieldId(fieldValues,26L)+"</生产能力碎沙酱香酒>");
			busUnitStr.append("<生产能力翻沙酱香酒>"+getFieldValByFieldId(fieldValues,27L)+"</生产能力翻沙酱香酒>");
			busUnitStr.append("<生产能力浓香酒>"+getFieldValByFieldId(fieldValues,28L)+"</生产能力浓香酒>");
			busUnitStr.append("<生产能力其他>"+getFieldValByFieldId(fieldValues,38L)+"</生产能力其他>");
			busUnitStr.append("<制曲车间数个>"+getFieldValByFieldId(fieldValues,6L)+"</制曲车间数个>");
			busUnitStr.append("<制曲车间建筑面积平方米>"+getFieldValByFieldId(fieldValues,7L)+"</制曲车间建筑面积平方米>");
			busUnitStr.append("<包装车间数个>"+getFieldValByFieldId(fieldValues,8L)+"</包装车间数个>");
			busUnitStr.append("<包装车间建筑面积平方米>"+getFieldValByFieldId(fieldValues,9L)+"</包装车间建筑面积平方米>");
			busUnitStr.append("<酒库数栋>"+getFieldValByFieldId(fieldValues,10L)+"</酒库数栋>");
			busUnitStr.append("<酒库建筑面积平方米>"+getFieldValByFieldId(fieldValues,11L)+"</酒库建筑面积平方米>");
			busUnitStr.append("<办公楼建筑面积平方米>"+getFieldValByFieldId(fieldValues,12L)+"</办公楼建筑面积平方米>");
			busUnitStr.append("<职工宿舍建筑面积平方米>"+getFieldValByFieldId(fieldValues,13L)+"</职工宿舍建筑面积平方米>");
			busUnitStr.append("<其他用房建筑面积平方米>"+getFieldValByFieldId(fieldValues,14L)+"</其他用房建筑面积平方米>");
			busUnitStr.append("<实际资产总额>"+getFieldValByFieldId(fieldValues,15L)+"</实际资产总额>");
			busUnitStr.append("<库存白酒量大曲酱香酒吨>"+getFieldValByFieldId(fieldValues,29L)+"</库存白酒量大曲酱香酒吨>");
			busUnitStr.append("<库存白酒量碎沙翻沙酒>"+getFieldValByFieldId(fieldValues,30L)+"</库存白酒量碎沙翻沙酒>");
			busUnitStr.append("<库存白酒量浓香>"+getFieldValByFieldId(fieldValues,39L)+"</库存白酒量浓香>");
			busUnitStr.append("<库存白酒量其他酒吨>"+getFieldValByFieldId(fieldValues,31L)+"</库存白酒量其他酒吨>");
			busUnitStr.append("<锅炉台>"+getFieldValByFieldId(fieldValues,16L)+"</锅炉台>");
			busUnitStr.append("<行车台>"+getFieldValByFieldId(fieldValues,17L)+"</行车台>");
			//品牌
			List<BusinessBrand> brands=busUnit.getBrands();
			String brandStr="";
			for(BusinessBrand bd:brands){
				brandStr=brandStr+bd.getName()+",";
			}
			brandStr=brandStr.length()==0?"":brandStr.substring(0, brandStr.length()-1);
			busUnitStr.append("<主要白酒品牌>"+brandStr+"</主要白酒品牌>");
			busUnitStr.append("<企业总人数>"+getFieldValByFieldId(fieldValues,18L)+"</企业总人数>");
			busUnitStr.append("<管理人员总数量>"+getFieldValByFieldId(fieldValues,19L)+"</管理人员总数量>");
			busUnitStr.append("<酿酒师人数>"+getFieldValByFieldId(fieldValues,20L)+"</酿酒师人数>");
			busUnitStr.append("<勾调师人数>"+getFieldValByFieldId(fieldValues,21L)+"</勾调师人数>");
			busUnitStr.append("<营销人员数>"+getFieldValByFieldId(fieldValues,22L)+"</营销人员数>");
			busUnitStr.append("<科研人员数>"+getFieldValByFieldId(fieldValues,23L)+"</科研人员数>");
			busUnitStr.append("<其他人员数>"+getFieldValByFieldId(fieldValues,24L)+"</其他人员数>");
			busUnitStr.append("</表头></企业基本信息2>");
			//企业法人营业执照注册情况
			busUnitStr.append("<企业法人营业执照注册情况><表头>");
			busUnitStr.append("<注册名称>"+getStr(busUnit.getLicense()!=null?busUnit.getLicense().getLicensename():"")+"</注册名称>");
			busUnitStr.append("<住所>"+getStr(busUnit.getLicense()!=null?busUnit.getLicense().getBusinessAddress():"")+"</住所>");
			busUnitStr.append("<法人代表>"+getStr(busUnit.getLicense()!=null?busUnit.getLicense().getLegalName():"")+"</法人代表>");
			busUnitStr.append("<注册资本>"+getStr(busUnit.getLicense()!=null?busUnit.getLicense().getRegisteredCapital():"")+"</注册资本>");
			busUnitStr.append("<实收资本>"+getStr(busUnit.getLicense()!=null?busUnit.getLicense().getPracticalCapital():"")+"</实收资本>");
			busUnitStr.append("<公司类型>"+getStr(busUnit.getLicense()!=null?busUnit.getLicense().getSubjectType():"")+"</公司类型>");
			busUnitStr.append("<经营范围>"+getStr(busUnit.getLicense()!=null?busUnit.getLicense().getToleranceRange():"")+"</经营范围>");
			busUnitStr.append("<注册号>"+getStr(busUnit.getLicense()!=null?busUnit.getLicense().getLicenseNo():"")+"</注册号>");
			String tempStr = (busUnit.getLicense()!=null?formatDateToStr(busUnit.getLicense().getEstablishTime()):"");
			busUnitStr.append("<成立时间>"+tempStr+"</成立时间>");
			tempStr = (busUnit.getLicense()!=null?formatDateToStr(busUnit.getLicense().getRegistrationTime()):"");
			busUnitStr.append("<注册时间>"+tempStr+"</注册时间>");
			busUnitStr.append("</表头></企业法人营业执照注册情况>");
			//全国工业产品生产许可证登记情况
			if(ListproLicense!=null&&ListproLicense.size()>0){
				ProductionLicenseInfo proLicense = ListproLicense.iterator().next();
				busUnitStr.append("<全国工业产品生产许可证登记情况><表头>");
				busUnitStr.append("<企业名称>"+getStr(proLicense.getBusunitName())+"</企业名称>");
				busUnitStr.append("<产品名称>"+getStr(proLicense.getProductName())+"</产品名称>");
				busUnitStr.append("<住所>"+getStr(proLicense.getAccommodation())+"</住所>");
				busUnitStr.append("<生产地址>"+getStr(proLicense.getProductionAddress())+"</生产地址>");
				busUnitStr.append("<检验方式>"+getStr(proLicense.getCheckType())+"</检验方式>");
				busUnitStr.append("<证书编号>"+getStr(proLicense.getQsNo())+"</证书编号>");
				tempStr = (formatDateToStr(proLicense.getEndTime()));
				busUnitStr.append("<有效期至>"+tempStr+"</有效期至>");
				tempStr = (formatDateToStr(proLicense.getStartTime()));
				busUnitStr.append("<获证时间>"+tempStr+"</获证时间>");
				busUnitStr.append("</表头></全国工业产品生产许可证登记情况>");
			}
			//组织机构代码证
			busUnitStr.append("<组织机构代码证><表头>");
			busUnitStr.append("<代码>"+getStr(busUnit.getOrgInstitution()!=null?busUnit.getOrgInstitution().getOrgCode():"")+"</代码>");
			busUnitStr.append("<机构名称>"+getStr(busUnit.getOrgInstitution()!=null?busUnit.getOrgInstitution().getOrgName():"")+"</机构名称>");
			busUnitStr.append("<机构类型>"+getStr(busUnit.getOrgInstitution()!=null?busUnit.getOrgInstitution().getOrgType():"")+"</机构类型>");
			busUnitStr.append("<地址>"+getStr(busUnit.getOrgInstitution()!=null?busUnit.getOrgInstitution().getOrgType():"")+"</地址>");
			String startDate = (busUnit.getOrgInstitution()!=null?formatDateToStr(busUnit.getOrgInstitution().getStartTime()):"");
			String endDate = (busUnit.getOrgInstitution()!=null?formatDateToStr(busUnit.getOrgInstitution().getEndTime()):"");
			busUnitStr.append("<有效期>"+startDate+"~"+endDate+"</有效期>");
			busUnitStr.append("<颁发单位>"+getStr(busUnit.getOrgInstitution()!=null?busUnit.getOrgInstitution().getUnitsAwarded():"")+"</颁发单位>");
			busUnitStr.append("<登记号>"+getStr(busUnit.getOrgInstitution()!=null?busUnit.getOrgInstitution().getRegisterNo():"")+"</登记号>");
			busUnitStr.append("</表头></组织机构代码证>");
			//税务登记证
			busUnitStr.append("<税务登记证><表头>");
			busUnitStr.append("<纳税人名称>"+getStr(busUnit.getTaxRegister()!=null?busUnit.getTaxRegister().getTaxerName():"")+"</纳税人名称>");
			busUnitStr.append("<法定代表人>"+getStr(busUnit.getTaxRegister()!=null?busUnit.getTaxRegister().getLegalName():"")+"</法定代表人>");
			busUnitStr.append("<地址>"+getStr(busUnit.getTaxRegister()!=null?busUnit.getTaxRegister().getAddress():"")+"</地址>");
			busUnitStr.append("<登记注册类型>"+getStr(busUnit.getTaxRegister()!=null?busUnit.getTaxRegister().getRegisterType():"")+"</登记注册类型>");
			busUnitStr.append("<经营范围>"+getStr(busUnit.getTaxRegister()!=null?busUnit.getTaxRegister().getBusinessScope():"")+"</经营范围>");
			busUnitStr.append("<批准设立机关>"+getStr(busUnit.getTaxRegister()!=null?busUnit.getTaxRegister().getApproveSetUpAuthority():"")+"</批准设立机关>");
			busUnitStr.append("<扣缴义务>"+getStr(busUnit.getTaxRegister()!=null?busUnit.getTaxRegister().getWithholdingObligations():"")+"</扣缴义务>");
			busUnitStr.append("<发证机关>"+getStr(busUnit.getTaxRegister()!=null?busUnit.getTaxRegister().getIssuingAuthority():"")+"</发证机关>");
			busUnitStr.append("</表头></税务登记证>");
			//酒类销售许可证
			busUnitStr.append("<酒类销售许可证><表头>");
			busUnitStr.append("<编号>"+getStr(busUnit.getLiquorSalesLicense()!=null?busUnit.getLiquorSalesLicense().getCertificateNo():"")+"</编号>");
			busUnitStr.append("<法定代表人>"+getStr(busUnit.getLiquorSalesLicense()!=null?busUnit.getLiquorSalesLicense().getLegalName():"")+"</法定代表人>");
			busUnitStr.append("<地址>"+getStr(busUnit.getLiquorSalesLicense()!=null?busUnit.getLiquorSalesLicense().getAddress():"")+"</地址>");
			busUnitStr.append("<经营类型>"+getStr(busUnit.getLiquorSalesLicense()!=null?busUnit.getLiquorSalesLicense().getBusinessType():"")+"</经营类型>");
			busUnitStr.append("<经营范围>"+getStr(busUnit.getLiquorSalesLicense()!=null?busUnit.getLiquorSalesLicense().getBusinessScope():"")+"</经营范围>");
			tempStr = (busUnit.getLiquorSalesLicense()!=null?formatDateToStr(busUnit.getLiquorSalesLicense().getEndTime()):"");
			busUnitStr.append("<有效期至>"+tempStr+"</有效期至>");
			busUnitStr.append("<发证机关>"+getStr(busUnit.getLiquorSalesLicense()!=null?busUnit.getLiquorSalesLicense().getIssuingAuthority():"")+"</发证机关>");
			tempStr = (busUnit.getLiquorSalesLicense()!=null?formatDateToStr(busUnit.getLiquorSalesLicense().getStartTime()):"");
			busUnitStr.append("<获证时间>"+tempStr+"</获证时间>");
			busUnitStr.append("</表头></酒类销售许可证>");
			//“挂靠”酒厂基本情况登记表
			busUnitStr.append("<挂靠酒厂基本情况登记表><表体>");
			List<ProducingDepartment> subDepartments=busUnit.getSubDepartments();
			int leng=(subDepartments!=null&&subDepartments.size()<20)?20:subDepartments.size();
			for(int j=0;j<leng;j++){
				if(subDepartments!=null&&j<subDepartments.size()){
					busUnitStr.append("<项目><名称>"+getStr(subDepartments.get(j).getName())+"</名称>");
					busUnitStr.append("<详细地址>"+getStr(subDepartments.get(j).getAddress())+"</详细地址>");
					busUnitStr.append("<负责人>"+getStr(subDepartments.get(j).getLegalName())+"</负责人>");
					busUnitStr.append("<投产窖池数2013年底>"+getStr(subDepartments.get(j).getInCommissionNum())+"</投产窖池数2013年底>");
					busUnitStr.append("<联系电话>"+getStr(subDepartments.get(j).getTelephone())+"</联系电话></项目>");
				}else{
					busUnitStr.append("<项目><名称> </名称>");
					busUnitStr.append("<详细地址> </详细地址>");
					busUnitStr.append("<负责人> </负责人>");
					busUnitStr.append("<投产窖池数2013年底> </投产窖池数2013年底>");
					busUnitStr.append("<联系电话> </联系电话></项目>");
				}
			}
			busUnitStr.append("</表体></挂靠酒厂基本情况登记表></仁怀白酒生产企业>");
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			InputStream in_nocode = new ByteArrayInputStream(busUnitStr.toString().getBytes("UTF-8"));
			JRXmlDataSource xmlDataSource = new JRXmlDataSource(in_nocode);
			JasperReport jasperReport = JasperCompileManager.compileReport(JRxml);
			byte[] bytes = JasperRunManager.runReportToPdf(jasperReport, parameterMap, xmlDataSource);
			/* 上传 */
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_ENTERPRISE_PATH);
			String fileName = System.currentTimeMillis()+".pdf";
			UploadUtil uploadUtil = new UploadUtil();
			boolean success_upload = uploadUtil.uploadFile(bytes, ftpPath, fileName);
			Resource res = null;
			if(success_upload){
				String webUrl;
				if(UploadUtil.IsOss()){
					webUrl=uploadUtil.getOssSignUrl(fileName);
				}else{
					webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_ENTERPRISE_PATH) + "/" + fileName;
				}
				res = new Resource();
				res.setFileName(fileName);
				res.setName(fileName);
				res.setUrl(webUrl);
				res.setUploadDate(new Date());
				res.setType(resourceTypeService.findById(3L));
				res = testResourceService.create(res);
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
