package com.gettec.fsnip.fsn.cron;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gettec.fsnip.fsn.enums.TestResultEditionEnum;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.mengniu.MengNiuProductImportLog;
import com.gettec.fsnip.fsn.model.mengniu.MengNiuProductName;
import com.gettec.fsnip.fsn.model.mengniu.MengNiuProductNameToBarcode;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.business.BusinessBrandService;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.mengniu.MengNiuProductImportLogService;
import com.gettec.fsnip.fsn.service.mengniu.MengNiuProductNameService;
import com.gettec.fsnip.fsn.service.mengniu.MengNiuProductNameToBarcodeService;
import com.gettec.fsnip.fsn.service.product.ProductInstanceService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.lhfs.fsn.service.testReport.TestReportService;

@Component
public class MengNiu{
	private static String city;
	private static String startDate;
	private static String endDate;
	private static String[] type;
	private static String url;
	private static String configPath="/mengniu.properties";
	private static long businessBrandId;//商标ID
	private static long producerId;//企业ID
	private static long organization;//蒙牛组织机构ID

	@Autowired
	private ProductService productService;
	@Autowired
	private BusinessUnitService businessUnitService;
	@Autowired
	private ProductInstanceService productInstanceService;
	@Autowired
	private TestPropertyService testPropertyService;
	@Autowired
	private TestReportService testReportService;
	@Autowired
	private BusinessBrandService businessBrandService;
	@Autowired
	private MengNiuProductImportLogService productMengNiuImportLogService;
	@Autowired
	private MengNiuProductNameToBarcodeService mengniuProductNameToBarcodeService;
	@Autowired
	private MengNiuProductNameService mengniuProductNameService;
	static{
		InputStream in = MengNiu.class.getResourceAsStream(configPath);
		Properties properties = new Properties();
		try {
			properties.load(in);
			Enumeration<?> en = properties.keys();  
			while(en.hasMoreElements()){
				String key=en.nextElement().toString();
				if(key.equals("mn.url")){
					url=properties.getProperty(key);
				}else if(key.equals("mn.city")){
					city=properties.getProperty(key);
				}else if(key.equals("mn.type")){
					type=properties.getProperty(key).split(",");
				}
			}
			businessBrandId=Long.valueOf(PropertiesUtil.getProperty("mn.businessBrandId"));
			producerId=Long.valueOf(PropertiesUtil.getProperty("mn.producerId"));
			organization=Long.valueOf(PropertiesUtil.getProperty("mn.organization"));
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Scheduled(cron="0 0 3 * * ? ")   //每天凌晨3点执行
	public void main() throws JPAException, ServiceException, IOException, ParseException, DaoException{
		String userName="蒙牛数据导入";
		BusinessUnit producer=businessUnitService.findById(producerId);
		BusinessBrand businessBrand=businessBrandService.findById(businessBrandId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//获取上一个周的日期
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -14);
		startDate=endDate=sdf.format(calendar.getTime());
		
		List<MengNiuProductNameToBarcode> mnProductNameToBarcodeList=mengniuProductNameToBarcodeService.findAll(); 
		Map<String,String> productNameToBarcodeMap=new HashMap<String,String>();
		for(MengNiuProductNameToBarcode n:mnProductNameToBarcodeList){
			productNameToBarcodeMap.put(n.getProductName(),n.getBarcode());
		}
		for(int i=0;i<type.length;i++){
			String rs=curl(url,getRequestData(city,startDate,endDate,type[i]));
			Document doc =Jsoup.parse(rs);
			Elements elements = doc.select("C_CFDAREPORT_AUDIT");
			for(Element e:elements){
				String productName=e.select("SKUNAME").first().text();
				if(productNameToBarcodeMap.containsKey(productName)){
					Product product=productService.findByBarcode(productNameToBarcodeMap.get(productName));
					TestResult report = new TestResult();
					report.setEdition(TestResultEditionEnum.mn.toString());
					report.setServiceOrder(e.select("ID").first().text());
					report.setSampleNO(e.select("ID").first().text()+"-1");
					report.setCheckOrgName("");
					report.setCreate_time(new Date());
					report.setCreate_user(userName);
					report.setBrand(businessBrand);
					report.setTestee(producer);
					report.setTestType("企业自检");
					report.setPublishFlag('1');
					report.setOrganization(organization);
					report.setPass(true);
					testReportService.create(report);

					//产品实例
					ProductInstance productInstance = new ProductInstance();
					productInstance.setBatchSerialNo(e.select("CHARG").first().text());
					productInstance.setProductionDate(sdf.parse(e.select("PRODUCTIONDATE").first().text()));
					productInstance.setProduct(product);
					productInstance.setProducer(producer);
					productInstance.setQs_no("");

					productInstanceService.create(productInstance);
					testReportService.updateRecordOfSample(report.getId(), productInstance.getId());

					//保存检测项目
					List<TestProperty> propertyList=new ArrayList<TestProperty>();
					Elements propertyElements=e.select("C_CFDAREPORT_ITEM");
					for(Element p:propertyElements){
						TestProperty testProperty=new TestProperty();
						testProperty.setAssessment(p.select("SS").first().text());
						testProperty.setName(p.select("DESCRIPTION").first().text());
						testProperty.setResult(p.select("RESULT").first().text());
						testProperty.setStandard(p.select("ATTR2").first().text());
						testProperty.setTechIndicator(p.select("STANDARD").first().text());
						testProperty.setUnit(p.select("UNIT").first().text());
						testProperty.setTestResultId(report.getId());
						propertyList.add(testProperty);
					}
					testPropertyService.save(report.getId(), propertyList, true);
					//记录同步日志
					MengNiuProductImportLog productMengNiuImportLog=new MengNiuProductImportLog();
					productMengNiuImportLog.setCreateTime(new Date());
					productMengNiuImportLog.setStartDate(sdf.parse(startDate));
					productMengNiuImportLog.setEndDate(sdf.parse(endDate));
					productMengNiuImportLog.setProductId(product.getId());
					productMengNiuImportLog.setTestResultId(report.getId());
					productMengNiuImportLogService.create(productMengNiuImportLog);
				}else{
					if(mengniuProductNameService.isProductNameExist(productName)==false){
						MengNiuProductName MengNiuProductName=new MengNiuProductName();
						MengNiuProductName.setProductName(productName);
						MengNiuProductName.setCreateTime(new Date());
						mengniuProductNameService.create(MengNiuProductName);
						System.out.println(MengNiuProductName.getId());
					}
				}
			}
		}
	}
	public String curl(String url,String data) throws IOException{
		URL _url = new URL(url);
		URLConnection connection = _url.openConnection();     
		/**   
		 * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。   
		 * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：   
		 */
		connection.setDoOutput(true);     
		/**   
		 * 最后，为了得到OutputStream，简单起见，把它约束在Writer并且放入POST信息中，例如： ...   
		 */
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");     
		out.write(data); //向页面传递数据。post的关键所在！     
		// remember to clean up     
		out.flush();     
		out.close();     
		// 一旦发送成功，用以下方法就可以得到服务器的回应：     
		String sCurrentLine="";     
		StringBuffer rsStr=new StringBuffer();     
		InputStream l_urlStream;     
		l_urlStream = connection.getInputStream();     
		// 传说中的三层包装阿！     
		BufferedReader l_reader = new BufferedReader(new InputStreamReader(     
				l_urlStream));     
		while ((sCurrentLine = l_reader.readLine()) != null) {     
			rsStr.append(sCurrentLine);

		}
		return rsStr.toString();
	}
	private String getRequestData(String city,String startDate,String endDate,String type){
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><mnInspectionReport xmlns=\"http://ws.mn.com\"><city>"+city+"</city><type>"+type+"</type><startDate>"+startDate+"</startDate><endDate>"+endDate+"</endDate></mnInspectionReport></soap:Body></soap:Envelope>";
	}
}