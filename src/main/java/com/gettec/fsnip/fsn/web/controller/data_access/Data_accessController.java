package com.gettec.fsnip.fsn.web.controller.data_access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.model.business.OrganizingInstitution;
import com.gettec.fsnip.fsn.model.business.TaxRegisterInfo;
import com.gettec.fsnip.fsn.model.data_access.Western_electronic;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.gettec.fsnip.fsn.service.data_access.Western_electronicService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/data_access")
public class Data_accessController {
	@Autowired
	protected Western_electronicService western_electronicService;
	
//	@Autowired
//	private BusinessUnitService businessUnitService;
//	
//	@Autowired
//	private ProductInstanceService productInstanceService;
//	
//	@Autowired
//	private TestReportService testReportService;
//	
//	@Autowired
//	private TraceDataProductNameService traceDataProductNameService;
	
	private JSONArray company_ja = null;
	
	private String operation_user = null;
	
	/**
	 * 进入西部电子商务数据接入操作页面
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/western_electronic_show")
	public String show_western_electronic(Model model,HttpServletRequest req,HttpServletResponse resp) {
		int data_count = 0;
		try {
			data_count = western_electronicService.getCount();
		} catch (Exception e) {
			e.printStackTrace();
			data_count = -1;
		}
		model.addAttribute("data_count", data_count);
		
		if (data_count > 0) {
			Western_electronic western_electronic = null;
			try {
				western_electronic = western_electronicService.getLast();
			} catch (Exception e) {
				e.printStackTrace();
			}
			model.addAttribute("western_electronic", western_electronic);
		}
		
		return "views/data_access/western_electronic";
	}
	
	/**
	 * 接入西部电子商务数据
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/western_electronic_access")
	public String western_electronic_access(Model model,HttpServletRequest req,HttpServletResponse resp) {
		String address = PropertiesUtil.getProperty("western.electronic.address");
		String result = sendPost(address + "company/companyList", null);
		if (result == null || "".equals(result)) {
			model.addAttribute("flg", false);
			model.addAttribute("msg", "未获取到西部电子商务企业数据");
			return "views/data_access/western_electronic";
		}
		company_ja = null;
		try {
			company_ja = JSONArray.fromObject(result);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("flg", false);
			model.addAttribute("msg", "获取到的西部电子商务企业数据不正确");
			return "views/data_access/western_electronic";
		}
		if (company_ja == null || company_ja.isEmpty()) {
			model.addAttribute("flg", false);
			model.addAttribute("msg", "未获取到西部电子商务企业数据");
			return "views/data_access/western_electronic";
		}
		operation_user = AccessUtils.getUserId().toString();
		try {
			Runnable westernRunnable = new WesternRunnable();
			Thread western_thread = new Thread(westernRunnable);
			western_thread.start();
			
			model.addAttribute("flg", true);
			return "views/data_access/western_electronic";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("flg", false);
			model.addAttribute("msg", "处理数据时发生异常，请稍后再试！");
		}
		
		return "views/data_access/western_electronic";
	}
	
	class WesternRunnable implements Runnable {
	    public void run() {
	    	int company_size = company_ja.size();
	    	int complete_num = 0;//完成的企业数量
	    	int company_num = company_size;//总企业数量
//	    	int product_num = 0;//产品数量
//	    	int report_num = 0;//报告数量
//	    	int trace_num = 0;//产品追溯数量
			BigDecimal percent = BigDecimal.ZERO;//进度百分比
			int success_num = 0;//接入成功企业数
			int fail_num = 0;//接入失败企业数
			
			String url = PropertiesUtil.getProperty("western.electronic.address");
    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	for (int i = 0; i < company_size; i++) {
	    		JSONObject company_jo = company_ja.getJSONObject(i);
	    		String company_id = company_jo.getString("id");
	    		String company_name = company_jo.getString("name");
	    		String address = company_jo.getString("address");
	    		String contact = company_jo.getString("contact");
	    		String contactphone = company_jo.getString("contactphone");
	    		
	    		String location = null;
	    		String businessLicense = null;
//	    		String businessLicenseImg = null;
	    		String organizationCode = null;
//	    		String organizationLCodeImg = null;
//	    		String taxRegistrationCertificate = null;
	    		String taxImg = null;
//	    		String productionLicense = null;
//	    		String productionLicenseImg = null;
	    		try {
		    		location = company_jo.getString("location");
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    		try {
		    		businessLicense = company_jo.getString("businessLicense");
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
//	    		try {
//		    		businessLicenseImg = company_jo.getString("businessLicenseImg");
//	    		} catch (Exception e) {
//	    			e.printStackTrace();
//	    		}
	    		try {
		    		organizationCode = company_jo.getString("organizationCode");
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
//	    		try {
//		    		organizationLCodeImg = company_jo.getString("organizationLCodeImg");
//	    		} catch (Exception e) {
//	    			e.printStackTrace();
//	    		}
//	    		try {
//		    		taxRegistrationCertificate = company_jo.getString("taxRegistrationCertificate");
//	    		} catch (Exception e) {
//	    			e.printStackTrace();
//	    		}
	    		try {
		    		taxImg = company_jo.getString("taxImg");
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
//	    		try {
//		    		productionLicense = company_jo.getString("productionLicense");
//	    		} catch (Exception e) {
//	    			e.printStackTrace();
//	    		}
//	    		try {
//		    		productionLicenseImg = company_jo.getString("productionLicenseImg");
//	    		} catch (Exception e) {
//	    			e.printStackTrace();
//	    		}
	    		BusinessUnit businessUnit = new BusinessUnit();
	    		businessUnit.setName(company_name);
	    		businessUnit.setAddress(address);
	    		businessUnit.setContact(contact);
	    		businessUnit.setTelephone(contactphone);
	    		businessUnit.setRegion(location);
	    		
	    		LicenseInfo licenseInfo = new LicenseInfo();//营业执照信息
	    		licenseInfo.setLicenseNo(businessLicense);
	    		licenseInfo.setLicensename(company_name);
	    		
	    		OrganizingInstitution organizingInstitution = new OrganizingInstitution();//组织机构代码信息
	    		organizingInstitution.setOrgCode(organizationCode);
	    		organizingInstitution.setOrgName(company_name);
	    		organizingInstitution.setOrgAddress(address);
	    		
	    		TaxRegisterInfo taxRegisterInfo = new TaxRegisterInfo();//税务登记信息
	    		if (taxImg != null && !"".equals(taxImg)) {
		    		Set<Resource> taxAttachments = new HashSet<Resource>();//税务登记证图片
		    		Resource resource = new Resource();
		    		resource.setUrl(taxImg);
		    		resource.setOrigin(organizationCode);
		    		resource.setBusinessName(company_name);
		    		taxAttachments.add(resource);
		    		taxRegisterInfo.setTaxAttachments(taxAttachments);
	    		}
	    		taxRegisterInfo.setTaxerName(company_name);
	    		taxRegisterInfo.setAddress(address);
	    		
	    		//生产许可证信息不知道存入哪个字段
	    		businessUnit.setLicense(licenseInfo);
	    		businessUnit.setLincesNo(businessLicense);
	    		businessUnit.setOrgInstitution(organizingInstitution);
	    		businessUnit.setTaxRegister(taxRegisterInfo);
	    		
	    		//根据企业信息获取产品信息
	    		String product_result = sendPost(url + "product/productByCompanyId", "companyId="+company_id);
	    		System.out.println("product_result:"+product_result);
	    		ArrayList<ProductInstance> product_list = null;
	    		ArrayList<TestResult> report_list = null;
	    		ArrayList<TraceData> trace_list = null;
	    		int product_size = 0;
	    		int report_size = 0;
	    		if (product_result != null && !"".equals(product_result)) {
	    			//有产品信息
	    			JSONArray product_ja = null;
	    			try {
	    				product_ja = JSONArray.fromObject(product_result);
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	    			product_list = new ArrayList<ProductInstance>();
	    			product_size = product_ja == null || product_ja.isEmpty() ? 0 : product_ja.size();
		    		System.out.println("product_size:"+product_size);
	    			for (int j = 0; j < product_size; j++) {
	    				//获取产品信息
	    	    		JSONObject product_jo = product_ja.getJSONObject(j);
	    	    		String product_id = product_jo.getString("id");//产品id
	    	    		String barcode = null;//产品条形码
	    	    		String product_name = product_jo.getString("name");//产品名称
	    	    		
	    	    		String net_weight = null;//净含量
	    	    		String shelf_life = null;//保质期
	    	    		String description = null;//产品描述
//	    	    		String dishes = null;//口味
	    	    		String ingredient = null;//配料表
	    	    		String batch_number = null;//产品批次编号
//	    	    		String native1 = null;//原产地
	    	    		String specification = null;//规格
	    	    		String state = null;//状态
	    	    		String excutive_standard = null;//执行标准
	    	    		String suitable_crowd = null;//适用人群
//	    	    		String brands = null;//产品所属品牌
//	    	    		String recommended = null;//推荐购买方式
	    	    		String notice = null;//禁忌事项
	    	    		String production_date = null;//产品生产日期
//	    	    		String qualification = null;//产品认证信息
	    	    		String kind = null;//食品分类
//	    	    		String report = null;//产品营养报告
	    	    		String otname = null;//产品别名
	    	    		String logo_name = null;//商标名称
//	    	    		String logo_kind = null;//商标类型
//	    	    		String logo_civilization = null;//商标文化
//	    	    		String logo_valid_period = null;//商标注册有效期限
//	    	    		String logo_registration_no = null;//商标注册证编号
	    	    		String logo_registration_img = null;//商标注册证图片
	    	    		try {
	    	    			barcode = product_jo.getString("barcode");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
	    	    		try {
	    	    			net_weight = product_jo.getString("net_weight");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
	    	    		try {
	    	    			shelf_life = product_jo.getString("shelf_life");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
	    	    		try {
	    	    			description = product_jo.getString("description");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
//	    	    		try {
//	    	    			dishes = product_jo.getString("dishes");
//	    	    		} catch (Exception e) {
//	    	    			e.printStackTrace();
//	    	    		}
	    	    		try {
	    	    			ingredient = product_jo.getString("ingredient");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
	    	    		try {
	    	    			batch_number = product_jo.getString("batch_number");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
//	    	    		try {
//	    	    			native1 = product_jo.getString("native");
//	    	    		} catch (Exception e) {
//	    	    			e.printStackTrace();
//	    	    		}
	    	    		try {
	    	    			specification = product_jo.getString("specification");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
	    	    		try {
	    	    			state = product_jo.getString("state");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
	    	    		try {
	    	    			excutive_standard = product_jo.getString("excutive_standard");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
	    	    		try {
	    	    			suitable_crowd = product_jo.getString("suitable_crowd");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
//	    	    		try {
//	    	    			brands = product_jo.getString("brands");
//	    	    		} catch (Exception e) {
//	    	    			e.printStackTrace();
//	    	    		}
//	    	    		try {
//	    	    			recommended = product_jo.getString("recommended");
//	    	    		} catch (Exception e) {
//	    	    			e.printStackTrace();
//	    	    		}
	    	    		try {
	    	    			notice = product_jo.getString("notice");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
	    	    		try {
	    	    			production_date = product_jo.getString("production_date");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
//	    	    		try {
//	    	    			qualification = product_jo.getString("qualification");
//	    	    		} catch (Exception e) {
//	    	    			e.printStackTrace();
//	    	    		}
	    	    		try {
	    	    			kind = product_jo.getString("kind");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
//	    	    		try {
//	    	    			report = product_jo.getString("report");
//	    	    		} catch (Exception e) {
//	    	    			e.printStackTrace();
//	    	    		}
	    	    		try {
	    	    			otname = product_jo.getString("otname");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
	    	    		try {
	    	    			logo_name = product_jo.getString("logo_name");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
//	    	    		try {
//	    	    			logo_kind = product_jo.getString("logo_kind");
//	    	    		} catch (Exception e) {
//	    	    			e.printStackTrace();
//	    	    		}
//	    	    		try {
//	    	    			logo_civilization = product_jo.getString("logo_civilization");
//	    	    		} catch (Exception e) {
//	    	    			e.printStackTrace();
//	    	    		}
//	    	    		try {
//	    	    			logo_valid_period = product_jo.getString("logo_valid_period");
//	    	    		} catch (Exception e) {
//	    	    			e.printStackTrace();
//	    	    		}
//	    	    		try {
//	    	    			logo_registration_no = product_jo.getString("logo_registration_no");
//	    	    		} catch (Exception e) {
//	    	    			e.printStackTrace();
//	    	    		}
	    	    		try {
	    	    			logo_registration_img = product_jo.getString("logo_registration_img");
	    	    		} catch (Exception e) {
	    	    			e.printStackTrace();
	    	    		}
	    	    		Product product = new Product();
	    	    		product.setName(product_name);
	    	    		product.setOtherName(otname);
	    	    		product.setStatus(state);
	    	    		product.setFormat(specification);
	    	    		product.setNetContent(net_weight);
	    	    		if (excutive_standard != null && !"".equals(excutive_standard)) {
		    	    		Set<ProductCategoryInfo> regularity = new HashSet<ProductCategoryInfo>();  // 执行标准
		    	    		ProductCategoryInfo productCategoryInfo = new ProductCategoryInfo();
		    	    		productCategoryInfo.setCategoryFlag(false);
		    	    		productCategoryInfo.setName(excutive_standard);
		    	    		regularity.add(productCategoryInfo);
		    	    		product.setRegularity(regularity);
	    	    		}
	    	    		product.setBarcode(barcode);
	    	    		product.setNote(notice);
	    	    		product.setSecondCategoryCode(kind);
	    	    		BusinessBrand businessBrand = null;
	    	    		if (logo_name != null && !"".equals(logo_name)) {
		    	    		businessBrand = new BusinessBrand();//商标
		    	    		businessBrand.setName(logo_name);
//		    	    		businessBrand.setRegistrationDate(registrationDate);
		    	    		businessBrand.setBusinessUnit(businessUnit);
		    	    		if (logo_registration_img != null && !"".equals(logo_registration_img)) {
			    	    		Set<Resource> logAttachments = new HashSet<Resource>();
			    	    		Resource log_img = new Resource();
			    	    		log_img.setUrl(logo_registration_img);
			    	    		log_img.setOrigin(organizationCode);
			    	    		log_img.setBusinessName(company_name);
			    	    		logAttachments.add(log_img);
			    	    		businessBrand.setLogAttachments(logAttachments);
		    	    		}
		    	    		
		    	    		product.setBusinessBrand(businessBrand);
	    	    		}
	    	    		product.setProducer(businessUnit);
	    	    		product.setDes(description);
	    	    		product.setCstm(suitable_crowd);
	    	    		product.setIngredient(ingredient);
	    	    		product.setExpiration(shelf_life);
	    	    		
	    	    		ProductInstance productInstance = new ProductInstance();
	    	    		productInstance.setBatchSerialNo(batch_number);
	    	    		if (production_date != null && !"".equals(production_date)) {
		    	    		try {
								productInstance.setProductionDate(format.parse(production_date));
							} catch (ParseException e1) {
								e1.printStackTrace();
			    	    		System.out.println("产品生产日期不正确:"+production_date);
							}
	    	    		}
	    	    		productInstance.setProduct(product);
	    	    		productInstance.setProducer(businessUnit);
	    	    		
	    	    		//根据产品ID获取检测报告信息
	    	    		String report_result = sendPost(url + "report/reportByProductId", "productId="+product_id);
	    	    		System.out.println("report_result:"+report_result);
	    	    		if (report_result != null && !"".equals(report_result)) {
	    	    			//有检测报告信息
	    	    			JSONArray report_ja = null;
	    	    			try {
	    	    				report_ja = JSONArray.fromObject(report_result);
	    	    			} catch (Exception e) {
	    	    				e.printStackTrace();
	    	    			}
	    	    			report_list = new ArrayList<TestResult>();
	    	    			report_size = report_ja == null || report_ja.isEmpty() ? 0 : report_ja.size();
	    	    			for (int k = 0; k < report_size; k++) {
	    	    				//获取检测报告信息
	    	    				TestResult test_result = new TestResult();
	    	    	    		JSONObject report_jo = report_ja.getJSONObject(k);
//	    	    	    		String report_id = report_jo.getString("id");//检测报告id
//	    	    	    		String product_id1 = report_jo.getString("productid");//产品id
//	    	    	    		String product_name1 = report_jo.getString("name");//产品名称
//	    	    	    		String specification1 = report_jo.getString("specification");//产品规格
//	    	    	    		String production_date1 = report_jo.getString("productionDate");//生产日期
//	    	    	    		String report_kind = report_jo.getString("reportKind");//报告类型
	    	    	    		
//	    	    	    		String testee = null;//被检出单位/人
//	    	    	    		String detection_unit = null;//检测单位
//	    	    	    		String report_number = null;//报告编号
	    	    	    		String detection_date = null;//检测日期
//	    	    	    		String detection_batch = null;//检测批次
	    	    	    		String detection_result = null;//检测结果
	    	    	    		String sampling_location = null;//抽样地点
	    	    	    		String sampling_count = null;//抽样量
	    	    	    		String detection_location = null;//检验地点
	    	    	    		String detection_result_describe = null;//检测结论描述
//	    	    	    		String detection_project_name = null;//检测项目名称
//	    	    	    		String detection_project_unit = null;//检测项目单位
//	    	    	    		String detection_project_technique = null;//检测项目技术指标
//	    	    	    		String project_detection_result = null;//项目检测结果
//	    	    	    		String single_evaluation = null;//单项评价
	    	    	    		String detection_criterion = null;//检测依据
//	    	    	    		try {
//	    	    	    			testee = report_jo.getString("testee");
//	    	    	    		} catch (Exception e) {
//	    	    	    			e.printStackTrace();
//	    	    	    		}
//	    	    	    		try {
//	    	    	    			detection_unit = report_jo.getString("detectionUnit");
//	    	    	    		} catch (Exception e) {
//	    	    	    			e.printStackTrace();
//	    	    	    		}
//	    	    	    		try {
//	    	    	    			report_number = report_jo.getString("reportNumber");
//	    	    	    		} catch (Exception e) {
//	    	    	    			e.printStackTrace();
//	    	    	    		}
	    	    	    		try {
	    	    	    			detection_date = report_jo.getString("detectionDate");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
//	    	    	    		try {
//	    	    	    			detection_batch = report_jo.getString("detectionBatch");
//	    	    	    		} catch (Exception e) {
//	    	    	    			e.printStackTrace();
//	    	    	    		}
	    	    	    		try {
	    	    	    			detection_result = report_jo.getString("detectionResult");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
	    	    	    		try {
	    	    	    			sampling_location = report_jo.getString("samplingLocation");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
	    	    	    		try {
	    	    	    			sampling_count = report_jo.getString("samplingCount");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
	    	    	    		try {
	    	    	    			detection_location = report_jo.getString("detectionLocation");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
	    	    	    		try {
	    	    	    			detection_result_describe = report_jo.getString("detectionResultDescribe");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
//	    	    	    		try {
//	    	    	    			detection_project_name = report_jo.getString("detectionProjectName");
//	    	    	    		} catch (Exception e) {
//	    	    	    			e.printStackTrace();
//	    	    	    		}
//	    	    	    		try {
//	    	    	    			detection_project_unit = report_jo.getString("detectionProjectUnit");
//	    	    	    		} catch (Exception e) {
//	    	    	    			e.printStackTrace();
//	    	    	    		}
//	    	    	    		try {
//	    	    	    			detection_project_technique = report_jo.getString("detection_projectTechnique");
//	    	    	    		} catch (Exception e) {
//	    	    	    			e.printStackTrace();
//	    	    	    		}
//	    	    	    		try {
//	    	    	    			project_detection_result = report_jo.getString("projectDetectionResult");
//	    	    	    		} catch (Exception e) {
//	    	    	    			e.printStackTrace();
//	    	    	    		}
//	    	    	    		try {
//	    	    	    			single_evaluation = report_jo.getString("singleEvaluation");
//	    	    	    		} catch (Exception e) {
//	    	    	    			e.printStackTrace();
//	    	    	    		}
	    	    	    		try {
	    	    	    			detection_criterion = report_jo.getString("detectionCriterion");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
	    	    	    		test_result.setTestee(businessUnit);
	    	    	    		test_result.setSample(productInstance);
	    	    	    		test_result.setBrand(businessBrand);
	    	    	    		test_result.setSampleQuantity(sampling_count);
	    	    	    		test_result.setSamplingLocation(sampling_location);
	    	    	    		test_result.setTestPlace(detection_location);
	    	    	    		if (detection_date != null && !"".equals(detection_date)) {
		    	    	    		try {
										test_result.setTestDate(format.parse(detection_date));
									} catch (ParseException e) {
										e.printStackTrace();
					    	    		System.out.println("检测日期不正确:"+detection_date);
									}
	    	    	    		}
	    	    	    		test_result.setStandard(detection_criterion);
	    	    	    		test_result.setResult(detection_result_describe);
	    	    	    		boolean pass = false;
	    	    	    		if ("1".equals(detection_result) || "true".equals(detection_result)) {
	    	    	    			pass = true;
	    	    	    		}
	    	    	    		test_result.setPass(pass);
	    	    	    		
	    	    	    		report_list.add(test_result);
    	    	    		}
	    	    		}
	    	    		
	    	    		//根据产品ID获取产品追溯信息
	    	    		String trace_result = sendPost(url + "trace/traceByProductId", "productId="+product_id);
	    	    		System.out.println("trace_result:"+trace_result);
	    	    		if (trace_result != null && !"".equals(trace_result)) {
	    	    			//有追溯信息
//	    	    			JSONArray trace_ja = null;
//	    	    			try {
//	    	    				trace_ja = JSONArray.fromObject(trace_result);
//	    	    			} catch (Exception e) {
//	    	    				e.printStackTrace();
//	    	    			}
	    	    			trace_list = new ArrayList<TraceData>();
//	    	    			int trace_size = trace_ja == null || trace_ja.isEmpty() ? 0 : trace_ja.size();
//	    	    			trace_num += trace_size;
//	    	    			for (int l = 0; l < trace_size; l++) {
	    	    				//获取追溯信息
	    	    				TraceData traceData = new TraceData();
//	    	    	    		JSONObject trace_jo = trace_ja.getJSONObject(l);
	    	    	    		JSONObject trace_jo = null;
	    	    	    		try {
	    	    	    			trace_jo = JSONObject.fromObject(trace_result);
		    	    			} catch (Exception e) {
		    	    				e.printStackTrace();
		    	    			}
//	    	    	    		String trace_id = trace_jo.getString("id");//追溯id
//	    	    	    		String product_id1 = trace_jo.getString("productid");//产品id
	    	    	    		String product_name1 = null;//产品名称
	    	    	    		try {
		    	    	    		product_name1 = trace_jo.getString("productName");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    			product_name1 = product_name;
	    	    	    		}
	    	    	    		String specification1 = null;//产品规格
	    	    	    		try {
	    	    	    			specification1 = trace_jo.getString("productSpecification");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    			specification1 = specification;
	    	    	    		}
	    	    	    		String package_date = null;//包装日期
	    	    	    		try {
	    	    	    			package_date = trace_jo.getString("packageDate");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
	    	    	    		
	    	    	    		String product_number = null;//育苗\养殖编号
	    	    	    		String product_kind = null;//种植\养殖品种
	    	    	    		String product_date = null;//种植\养殖日期
	    	    	    		String inspection_record = null;//检验检疫记录
	    	    	    		String harvest_date = null;//收割\出栏日期
	    	    	    		String metalwork_date = null;//加工\屠宰日期
	    	    	    		String technology = null;//加工工艺
	    	    	    		String detection_result = null;//检测结果
	    	    	    		String quality = null;//质量等级
//	    	    	    		String logo = null;//品牌商标
	    	    	    		try {
	    	    	    			product_number = trace_jo.getString("productNumber");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
	    	    	    		try {
	    	    	    			product_kind = trace_jo.getString("productKind");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
	    	    	    		try {
	    	    	    			product_date = trace_jo.getString("productDate");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
	    	    	    		try {
	    	    	    			inspection_record = trace_jo.getString("inspectionRecord");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
	    	    	    		try {
	    	    	    			harvest_date = trace_jo.getString("harvestDate");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
	    	    	    		try {
	    	    	    			metalwork_date = trace_jo.getString("metalworkDate");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
	    	    	    		try {
	    	    	    			technology = trace_jo.getString("technology");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
	    	    	    		try {
	    	    	    			detection_result = trace_jo.getString("detectionResult");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
	    	    	    		try {
	    	    	    			quality = trace_jo.getString("quality");
	    	    	    		} catch (Exception e) {
	    	    	    			e.printStackTrace();
	    	    	    		}
//	    	    	    		try {
//	    	    	    			logo = trace_jo.getString("logo");
//	    	    	    		} catch (Exception e) {
//	    	    	    			e.printStackTrace();
//	    	    	    		}
	    	    	    		
	    	    	    		traceData.setAddress(address);
	    	    	    		traceData.setPackageSpec(specification1);
	    	    	    		traceData.setProductName(product_name1);
	    	    	    		StringBuilder timeTrack = new StringBuilder();
	    	    	    		timeTrack.append("[");
	    	    	    		boolean flg = false;
	    	    	    		if (product_date != null && !"".equals(product_date)) {
		    	    	    		timeTrack.append("\"");
		    	    	    		timeTrack.append(product_date);
		    	    	    		timeTrack.append("@种植\\\\养殖");
		    	    	    		if (product_number != null && !"".equals(product_number)) {
			    	    	    		timeTrack.append("（编号：");
			    	    	    		timeTrack.append(product_number);
			    	    	    		timeTrack.append("）");
		    	    	    		}
		    	    	    		if (product_kind != null && !"".equals(product_kind)) {
			    	    	    		timeTrack.append("（品种：");
			    	    	    		timeTrack.append(product_kind);
			    	    	    		timeTrack.append("）");
		    	    	    		}
		    	    	    		flg = true;
		    	    	    		timeTrack.append("\"");
	    	    	    		}
	    	    	    		if (harvest_date != null && !"".equals(harvest_date)) {
		    	    	    		if (flg) {
			    	    	    		timeTrack.append(",");
		    	    	    		}
		    	    	    		timeTrack.append("\"");
		    	    	    		timeTrack.append(harvest_date);
		    	    	    		timeTrack.append("@收割\\\\出栏\"");
		    	    	    		flg = true;
	    	    	    		}
	    	    	    		if (metalwork_date != null && !"".equals(metalwork_date)) {
		    	    	    		if (flg) {
			    	    	    		timeTrack.append(",");
		    	    	    		}
		    	    	    		timeTrack.append("\"");
		    	    	    		timeTrack.append(metalwork_date);
		    	    	    		timeTrack.append("@加工\\\\屠宰");
		    	    	    		if (product_kind != null && !"".equals(product_kind)) {
			    	    	    		timeTrack.append("（加工工艺：");
			    	    	    		timeTrack.append(technology);
			    	    	    		timeTrack.append("）");
		    	    	    		}
		    	    	    		timeTrack.append("\"");
		    	    	    		flg = true;
	    	    	    		}
	    	    	    		if (package_date != null && !"".equals(package_date)) {
		    	    	    		if (flg) {
			    	    	    		timeTrack.append(",");
		    	    	    		}
		    	    	    		SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日");
		    	    	    		timeTrack.append("\"");
		    	    	    		try {
		    	    	    			format1.parse(package_date);
			    	    	    		timeTrack.append(package_date);
			    	    	    		timeTrack.append("@包装\"");
		    	    	    		} catch (ParseException e) {
		    	    	    			//日期不正确
			    	    	    		timeTrack.append("包装日期@");
			    	    	    		timeTrack.append(package_date);
			    	    	    		timeTrack.append("\"");
		    	    	    		}
		    	    	    		flg = true;
	    	    	    		}
	    	    	    		if (inspection_record != null && !"".equals(inspection_record)) {
		    	    	    		if (flg) {
			    	    	    		timeTrack.append(",");
		    	    	    		}
		    	    	    		timeTrack.append("\"");
		    	    	    		timeTrack.append("检验检疫记录");
		    	    	    		timeTrack.append("@");
		    	    	    		timeTrack.append(inspection_record);
		    	    	    		timeTrack.append("\"");
		    	    	    		flg = true;
	    	    	    		}
	    	    	    		if (detection_result != null && !"".equals(detection_result)) {
		    	    	    		if (flg) {
			    	    	    		timeTrack.append(",");
		    	    	    		}
		    	    	    		timeTrack.append("\"");
		    	    	    		timeTrack.append("检测结果");
		    	    	    		timeTrack.append("@");
		    	    	    		timeTrack.append(detection_result);
		    	    	    		timeTrack.append("\"");
		    	    	    		flg = true;
	    	    	    		}
	    	    	    		if (quality != null && !"".equals(quality)) {
		    	    	    		if (flg) {
			    	    	    		timeTrack.append(",");
		    	    	    		}
		    	    	    		timeTrack.append("\"");
		    	    	    		timeTrack.append("质量等级");
		    	    	    		timeTrack.append("@");
		    	    	    		timeTrack.append(quality);
		    	    	    		timeTrack.append("\"");
	    	    	    		}
	    	    	    		timeTrack.append("]");
	    	    	    		traceData.setTimeTrack(timeTrack.toString());
	    	    	    		
	    	    	    		trace_list.add(traceData);
//    	    	    		}
	    	    		}
	    	    		product_list.add(productInstance);
	    			}
	    		}
	    		
	    		int data_count = 0;
	    		try {
	    			data_count = western_electronicService.getCount();
	    		} catch (Exception e) {
	    			e.printStackTrace();
    	    		System.out.println("获取西部电子商务数据接入情况表时发生异常！");
    	    		continue;
	    		}
    			Western_electronic western_electronic = null;
	    		if (data_count > 0) {
	    			try {
	    				western_electronic = western_electronicService.getLast();
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    	    		System.out.println("获取西部电子商务数据接入情况表时发生异常！");
	    	    		continue;
	    			}
	    		}
	    		boolean insert_flg = false;
	    		if (western_electronic == null) {
	    			western_electronic = new Western_electronic();
	    			western_electronic.setId(1);
	    			GregorianCalendar now = new GregorianCalendar();
	    			western_electronic.setOperation_time(format.format(now.getTime()));
	    			insert_flg = true;
	    		}
	    		percent = new BigDecimal(i).divide(new BigDecimal(company_size),2,BigDecimal.ROUND_HALF_UP);
	    		complete_num = i + 1;
	    		if (i < company_size - 1) {
	    			//不是最后一条
	    			western_electronic.setStatus("2");
	    		} else {
	    			western_electronic.setStatus("1");
	    			percent = new BigDecimal(100);
	    			GregorianCalendar now = new GregorianCalendar();
	    			western_electronic.setComplete_time(format.format(now.getTime()));
	    		}
	    		western_electronic.setPercent(percent.toString());
	    		western_electronic.setComplete_num(complete_num);
	    		western_electronic.setCompany_num(company_num);
	    		western_electronic.setOperation_user(operation_user);
	    		
	    		//将企业信息(business_unit)、产品信息(product)、报告信息(test_result)和追溯信息存入数据库
	    		HashMap<String,Integer> result_map = null;
	    		try {
	    			result_map = western_electronicService.save_western_data(businessUnit, product_list, report_list, trace_list);
//	    			product_num += product_size;
//	    			report_num += report_size;
//    				trace_num += 1;
	    			success_num++;
				} catch (Exception e) {
					e.printStackTrace();
					fail_num++;
				}
	    		if (result_map != null) {
		    		western_electronic.setProduct_num(result_map.get("product_num"));
		    		western_electronic.setReport_num(result_map.get("report_num"));
		    		western_electronic.setTrace_num(result_map.get("trace_num"));
	    		}
	    		western_electronic.setSuccess_num(success_num);
	    		western_electronic.setFail_num(fail_num);
	    		if (insert_flg) {
	    			//插入数据
	    			try {
		    			western_electronicService.add_western_electronic(western_electronic);
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    	    		System.out.println("插入西部电子商务数据接入情况数据时发生异常！");
	    			}
	    		} else {
	    			//更新数据
	    			try {
		    			western_electronicService.update_western_electronic(western_electronic);
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    	    		System.out.println("更新西部电子商务数据接入情况数据时发生异常！");
	    			}
	    		}
	    	}
	    }
	}
	
	/**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}