package com.gettec.fsnip.fsn.service.sample.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.SampleBusinessBrand;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.product.SampleProduct;
import com.gettec.fsnip.fsn.model.product.SampleProductInstance;
import com.gettec.fsnip.fsn.model.test.SampleTestProperty;
import com.gettec.fsnip.fsn.model.test.SampleTestResult;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.SampleBusinessBrandService;
import com.gettec.fsnip.fsn.service.product.ProductInstanceService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.product.SampleProductInstanceService;
import com.gettec.fsnip.fsn.service.product.SampleProductService;
import com.gettec.fsnip.fsn.service.sample.ImportExcelService;
import com.gettec.fsnip.fsn.service.test.SampleTestPropertyService;
import com.gettec.fsnip.fsn.service.test.SampleTestResultService;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.service.test.TestResultService;
import com.gettec.fsnip.fsn.util.ReadFileTool;
import com.gettec.fsnip.fsn.vo.product.ProductVO;
import com.gettec.fsnip.fsn.vo.sampling.ExcelFileVO;
import com.gettec.fsnip.fsn.vo.sampling.SheetVO;

@Service(value="importExcelService")
public class ImportExcelServiceImpl implements ImportExcelService {
	 private final String XLSX_CONTENTTYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	 private final String XLS_CONTENTTYPE = "application/vnd.ms-excel";
	 
	 private final String DEFAULT_BUSINESSUNIT ="--"; //默认企业名称
	 
	 
	 @Autowired 
	 private BusinessUnitService businessUnitService;  //企业信息
	 @Autowired 
	 private ProductService productService;//产品信息
	 @Autowired 
	 private ProductInstanceService productInstanceService;
	 @Autowired 
	 private TestResultService testResultService;
	 @Autowired 
	 private TestPropertyService testPropertyService;
   
	 
	 @Autowired 
	 private SampleProductService sampleProductService;//抽样产品信息
	 
	 @Autowired 
	 private SampleBusinessBrandService sampleBusinessBrandService;//抽样商标信息
	 
	 @Autowired 
	 private SampleProductInstanceService sampleProductInstanceService;
	 
	 @Autowired 
	 private SampleTestPropertyService sampleTestPropertyService;
	 
	 @Autowired 
	 private SampleTestResultService sampleTestResultService;
	 
	
	 
	
	@Override
	public List<ExcelFileVO> getWorkbook(MultipartFile[] excelFiles) {
		  List<ExcelFileVO> list = new ArrayList<ExcelFileVO>();
		 for (MultipartFile file : excelFiles){
			 ExcelFileVO excelFileVO = null;
			 if(XLSX_CONTENTTYPE.equals(file.getContentType())){
				 XSSFWorkbook workbook = null;
				try {
					workbook = new XSSFWorkbook(file.getInputStream());
					excelFileVO = ReadFileTool.analyticalWorkbook(workbook);
					excelFileVO.setFileName(file.getOriginalFilename());
				} catch (IOException e) {
					excelFileVO = new ExcelFileVO();
					excelFileVO.setFlag(false);
					excelFileVO.setMessage("读取文件流异常，请重试！");
				}
			 }else if(XLS_CONTENTTYPE.equals(file.getContentType())){
				 HSSFWorkbook workbook = null;
				 try {
					workbook = new HSSFWorkbook(file.getInputStream());
					excelFileVO = ReadFileTool.analyticalWorkbook(workbook);
					excelFileVO.setFileName(file.getOriginalFilename());
				} catch (IOException e) {
					excelFileVO = new ExcelFileVO();
					excelFileVO.setFlag(false);
					excelFileVO.setMessage("读取文件流异常，请重试！");
				}
			 }else{
				    excelFileVO = new ExcelFileVO();
					excelFileVO.setFlag(false);
					excelFileVO.setMessage("读取文件流异常，请重试！");
			 }
			 list.add(excelFileVO);
		 }
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveProduct(SheetVO sheetVO,BusinessUnit defaultBusinessUnit,SampleBusinessBrand defaultBusinessBrand) {
		List<ProductVO> list = sheetVO.getList();
		for(ProductVO productVO:list){
			try {
				    /*********************保存生产企业和被抽企业**************************/
					Map<String,BusinessUnit> map = saveOrGetBusinessUnit(productVO);
					BusinessUnit businessUnit = map.get("businessUnit");
					businessUnit = businessUnit==null?defaultBusinessUnit:businessUnit;  //生产企业
					BusinessUnit sampleBusinessUnit = map.get("sampleBusinessUnit");  //被抽样企业
					
					
					Product product = new Product();
					product.setName(productVO.getProductName());
					product.setFormat(productVO.getFormat());
					product = productService.checkProduct(product);
					
					if(product!=null){
						//保存匹配产品信息
						saveSample(product,productVO,businessUnit,sampleBusinessUnit,sheetVO);
					}else{
						//保存不匹配产品信息（保存到新的表）
						saveSample(productVO,businessUnit,sampleBusinessUnit,sheetVO,defaultBusinessBrand);
					}
				
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
		}
	}
	
	//保存不匹配产品信息（保存到新的表）
	public void saveSample(ProductVO productVO,BusinessUnit businessUnit,BusinessUnit sampleBusinessUnit,SheetVO sheetVO,SampleBusinessBrand defaultBusinessBrand){
		try {
			/*********************保存产品信息**************************/
			SampleProduct sampleProduct = saveOrGetSampleProduct(productVO,businessUnit,defaultBusinessBrand);
			
			
			/*********************保存产品实例信息**************************/
			SampleProductInstance sampleProductInstance = new SampleProductInstance();
			sampleProductInstance.setBatchSerialNo(productVO.getProductionDate());
			sampleProductInstance.setProductionDate(productVO.getProduction_Date());
			sampleProductInstance.setSampleProduct(sampleProduct);
			sampleProductInstance.setProducer(businessUnit);
			sampleProductInstanceService.create(sampleProductInstance);
			
			/*********************保存报告信息**************************/
			SampleTestResult testResult = new SampleTestResult();
			if(sampleBusinessUnit!=null){
				testResult.setTestee(sampleBusinessUnit);
			}
			testResult.setCheckOrgName("");
			testResult.setPass(sheetVO.getPass());
			testResult.setSample(sampleProductInstance);
			String uuid = UUID.randomUUID().toString();  
			testResult.setServiceOrder(uuid);
			testResult.setCreate_user("");
			testResult.setPublishFlag('1'); //通过审核;
			testResult.setTestType("政府抽检");
			testResult.setEdition(sheetVO.getSource().toString());
			sampleTestResultService.create(testResult);
			
			/*********************保存检测项目信息**************************/
			if(sheetVO.getPass().equals(Boolean.FALSE)){
				List<TestProperty> testList = productVO.getTestProperty();
				for(TestProperty testProperty:testList){
					SampleTestProperty sampleTestProperty = new SampleTestProperty();
					sampleTestProperty.setName(testProperty.getName());
					sampleTestProperty.setResult(testProperty.getResult());
					sampleTestProperty.setTechIndicator(testProperty.getTechIndicator());
					sampleTestProperty.setTestResultId(testResult.getId());
					sampleTestPropertyService.create(sampleTestProperty);
				}
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}
	
	//保存匹配产品信息
	public void saveSample(Product product,ProductVO productVO,BusinessUnit businessUnit,BusinessUnit sampleBusinessUnit,SheetVO sheetVO){
		try {
			/*********************保存产品实例信息**************************/
			ProductInstance productInstance = new ProductInstance();
			productInstance.setBatchSerialNo(productVO.getProductionDate());
			productInstance.setProductionDate(productVO.getProduction_Date());
			productInstance.setProduct(product);
			productInstance.setProducer(businessUnit);
			productInstanceService.create(productInstance);
			
			
			/*********************保存报告信息**************************/
			TestResult testResult = new TestResult();
			if(sampleBusinessUnit!=null){
				testResult.setTestee(sampleBusinessUnit);
			}
			testResult.setCheckOrgName("");
			testResult.setPass(sheetVO.getPass());
			testResult.setSample(productInstance);
			String uuid = UUID.randomUUID().toString();  
			testResult.setServiceOrder(uuid);
			testResult.setCreate_user("");
			testResult.setPublishFlag('1'); //通过审核;
			testResult.setTestType("政府抽检");
			testResult.setEdition(sheetVO.getSource().toString());
			testResultService.create(testResult);
			
			/*********************保存检测项目信息**************************/
			if(sheetVO.getPass().equals(Boolean.FALSE)){
				List<TestProperty> testList = productVO.getTestProperty();
				for(TestProperty testProperty:testList){
					testProperty.setTestResultId(testResult.getId());
					System.out.println(testProperty.getTechIndicator());
					testPropertyService.create(testProperty);
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public SampleProduct saveOrGetSampleProduct(ProductVO product, BusinessUnit businessUnit,SampleBusinessBrand defaultBusinessBrand) {
		SampleProduct producEntity = null;
		try {
			SampleProduct productCheck = new SampleProduct();
			productCheck.setName(product.getProductName());
			productCheck.setFormat(product.getFormat());
		
			producEntity = sampleProductService.checkSampleProduct(productCheck);
			
		    if(producEntity==null){
		    	if(null!=product.getBusinessName()&&!"未标注".equals(product.getBusinessName())&&!"/".equals(product.getBusinessName())&&!"".equals(product.getBusinessName())){
					SampleBusinessBrand sampleBusinessBrand = saveOrGetSampleBusinessBrand(product,businessUnit);
	        		productCheck.setSampleBusinessBrand(sampleBusinessBrand);
	        	}else{
	        		productCheck.setSampleBusinessBrand(defaultBusinessBrand);
	        	}
		    	
		    	productCheck.setProducer(businessUnit);
	        	producEntity = sampleProductService.create(productCheck);
		    }
		    
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return producEntity;
	}

	
	@Override
	public 	Map<String,BusinessUnit> saveOrGetBusinessUnit(ProductVO product) {
		BusinessUnit businessUnit = null;
		BusinessUnit sampleBusinessUnit = null;
		Map<String,BusinessUnit> map = new HashMap<String,BusinessUnit>();
		try {
			if(!"/".equals(product.getCompanyName())&&!"".equals(product.getCompanyName())){
				BusinessUnit businessUnitCheck =  new BusinessUnit();  
				businessUnitCheck.setName(product.getCompanyName());
				businessUnit = businessUnitService.findByName(product.getCompanyName());
				//businessUnit = businessUnitService.checkUniqueEntity(businessUnitCheck);
				if(businessUnit==null){
					if(!"/".equals(product.getCompanyAddress())&&!"".equals(product.getCompanyAddress())){
						businessUnitCheck.setAddress(product.getCompanyAddress());
					}
					businessUnitCheck.setLicense(null);
					businessUnit = businessUnitService.create(businessUnitCheck);
				}
			}
			
			if(!"/".equals(product.getSampleCompanyName())&&!"".equals(product.getSampleCompanyName())){
				BusinessUnit sampleBusinessUnitCheck =  new BusinessUnit();
				sampleBusinessUnitCheck.setName(product.getSampleCompanyName());
				sampleBusinessUnit =  businessUnitService.findByName(product.getSampleCompanyName());
				//sampleBusinessUnit =  businessUnitService.checkUniqueEntity(sampleBusinessUnitCheck);
				if(sampleBusinessUnit==null){
					if(!"/".equals(product.getSampleCompanyName())&&!"".equals(product.getSampleCompanyName())&&product.getSampleCompanyAddress().length()>7){
						sampleBusinessUnitCheck.setAddress(product.getSampleCompanyAddress());
					}
					sampleBusinessUnitCheck.setLicense(null);
					sampleBusinessUnit = businessUnitService.create(sampleBusinessUnitCheck);
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		map.put("businessUnit", businessUnit);
		map.put("sampleBusinessUnit", sampleBusinessUnit);
		return map;
	}
	
	
	public SampleBusinessBrand saveOrGetSampleBusinessBrand(ProductVO product,BusinessUnit businessUnit) {
		SampleBusinessBrand sampleBusinessBrand = null;
		try {
			SampleBusinessBrand businessBrandCheck = new SampleBusinessBrand();
			businessBrandCheck.setName(product.getBusinessName());
			businessBrandCheck.setBusinessUnit(businessUnit);
			sampleBusinessBrand = sampleBusinessBrandService.checkBusinessBrand(businessBrandCheck);
			if(null==sampleBusinessBrand){
				sampleBusinessBrand = sampleBusinessBrandService.create(businessBrandCheck);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return sampleBusinessBrand;
	}
	
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BusinessUnit getDefaultBusinessUnit() {
		BusinessUnit businessUnit = new BusinessUnit();
		businessUnit.setName(DEFAULT_BUSINESSUNIT);
		BusinessUnit defaultBusinessUnit =null;
		try {
			defaultBusinessUnit = businessUnitService.findByName(DEFAULT_BUSINESSUNIT);
			if(null==defaultBusinessUnit){
				defaultBusinessUnit = businessUnitService.create(businessUnit);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return defaultBusinessUnit;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public SampleBusinessBrand getDefaultSampleBusinessBrand(BusinessUnit defaultBusinessUnit) {
		SampleBusinessBrand sampleBusinessBrand = new SampleBusinessBrand();
		sampleBusinessBrand.setName(DEFAULT_BUSINESSUNIT);
		SampleBusinessBrand defaultSampleBusinessBrand = null;
		try {
			sampleBusinessBrand.setBusinessUnit(defaultBusinessUnit);
			defaultSampleBusinessBrand = sampleBusinessBrandService.checkBusinessBrand(sampleBusinessBrand);
			if(null==defaultSampleBusinessBrand){
				defaultSampleBusinessBrand = sampleBusinessBrandService.create(sampleBusinessBrand);
			
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return defaultSampleBusinessBrand;
	}

}
