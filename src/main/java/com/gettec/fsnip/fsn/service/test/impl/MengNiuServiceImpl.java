package com.gettec.fsnip.fsn.service.test.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.enums.TestResultEditionEnum;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.model.product.ProductRecommendUrl;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.test.MengNiuService;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.service.test.TestResultService;
import com.lhfs.fsn.service.testReport.TestReportService;
@Service
public class MengNiuServiceImpl implements MengNiuService {
	@Autowired
	private ProductService productService;
	@Autowired
	private BusinessUnitService businessUnitService;
	@Autowired
	private TestResultService testResultService;
	@Autowired
	private TestPropertyService testPropertyService;
	@Autowired
	private TestReportService testReportService;
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void importData(String rs,Map<String,String> config){
		try {
			BusinessUnit producer=businessUnitService.findById(375);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Document doc =Jsoup.parse(rs);
//		Elements elements = doc.select("C_CFDAREPORT_AUDIT");
//		try{
//			BusinessUnit producer=businessUnitService.findById(config.get("mn.producerId"));
//			String opName="蒙牛数据导入";
//			for(Element e:elements){
//				String productName=e.select("SKUNAME").first().text();
//				String [] _proStr=getProductNameAndFormalByPreg(productName);
//				Product product=new Product();
//				product.setName(_proStr[0]);
//				product.setFormat(_proStr[1]);
//				product=productService.checkProduct(product);
//				//如果是新产品时,则新创建一个产品
//				if(product==null){
//					product=new Product();
//					product.setName(_proStr[0]);
//					product.setFormat(_proStr[1]);
//					product.setProducer(producer);
//					BusinessBrand businessBrand=new BusinessBrand();
//					businessBrand.setName(config.get("mn.brandName"));
//					product.setBusinessBrand(businessBrand);
//					ProductCategoryInfo productCategoryInfo=new ProductCategoryInfo();
//					productCategoryInfo.setId(Long.valueOf(config.get("mn.categoryId")));
//					productCategoryInfo.setName(config.get("mn.categoryName"));
//					productCategoryInfo.setCategoryFlag(true);
//					product.setCategory(productCategoryInfo);
//					product.setListOfNutrition(new ArrayList<ProductNutrition>());
//					product.setProUrlList(new ArrayList<ProductRecommendUrl>());
//					productService.saveProduct(product, producer.getName(), producer.getId(), true);
//				}
//				//新增报告
//				TestResult report = new TestResult();
//				report.setEdition(TestResultEditionEnum.mn.toString());
//				report.setServiceOrder(e.select("ID").first().text());
//				report.setCreate_time(new Date());         // 报告创建时间
//				report.setCreate_user(opName); // 报告创建用户
//				report.setOrganizationName(opName);  // 报告创建企业组织机构名称
//				report.setPublishFlag('6');//设置报告状态 编辑时不需要改该状态 所以提到新增这里
//				report.setCheckOrgName("");
//				report.setLastModifyTime(new Date());
//				report.setPass(true);
//				testResultService.create(report);
//				//新增检测项目
//				List<TestProperty> propertyList=new ArrayList<TestProperty>();
//				Elements elementList = elements.select("C_CFDAREPORT_ITEM");
//				for(Element el:elementList){
//					TestProperty testProperty=new TestProperty();
//					testProperty.setName(el.select("DESCRIPTION").first().text());
//					testProperty.setName(el.select("UNIT").first().text());
//					testProperty.setTechIndicator(el.select("STANDARD").first().text());
//					testProperty.setTechIndicator(el.select("SS").first().text());
//					testProperty.setResult(el.select("RESULT").first().text());
//					testProperty.setAssessment(el.select("ATTR2").first().text());
//				}
//				
//				testPropertyService.save(report.getId(),propertyList, true);
//				  
//				ProductInstance productInstance = new ProductInstance();
//				productInstance.setBatchSerialNo(e.select("CHARG").first().text());
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd ");  
//			    Date date = sdf.parse(e.select("PRODUCTIONDATE").first().text());
//				productInstance.setProductionDate(date);
//				productInstance.setProduct(product);
//				productInstance.setProducer(producer);
//				productInstance.setQs_no("");
//				testReportService.updateRecordOfSample(report.getId(), productInstance.getId());
//				break;
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
	/**
	 * 根据名称 划分 名称和规格
	 * @param productName
	 * @return
	 */
	private String[] getProductNameAndFormalByPreg(String productName){
		Pattern p = Pattern.compile("(.*?)([0-9].*)");
		Matcher m = p.matcher(productName);
		while(m.find()){
			return new String[]{m.group(1),m.group(2)};
		}
		return new String[]{productName,""};
	}
}
