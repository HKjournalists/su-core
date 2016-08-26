package com.gettec.fsnip.fsn.service.trace.impl;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_REPORT_PATH;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.trace.TraceDataDao;
import com.gettec.fsnip.fsn.dao.trace.TraceDataProductNameDao;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.market.ResourceType;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.gettec.fsnip.fsn.model.trace.TraceDataProductName;
import com.gettec.fsnip.fsn.service.business.BusinessBrandService;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.impl.BusinessBrandServiceImpl;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.UnitService;
import com.gettec.fsnip.fsn.service.market.MkCategoryService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.market.ResourceTypeService;
import com.gettec.fsnip.fsn.service.product.ProductCategoryInfoService;
import com.gettec.fsnip.fsn.service.product.ProductInstanceService;
import com.gettec.fsnip.fsn.service.test.TestResultService;
import com.gettec.fsnip.fsn.service.trace.TraceDataProductNameService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.SystemDefaultInterface;
import com.gettec.fsnip.fsn.util.ToPdfUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.web.controller.rest.business.BusinessBrandRESTService;
import com.gettec.fsnip.fsn.web.controller.rest.product.ProductCategoryRESTService;
import com.gettec.fsnip.fsn.web.controller.rest.test.TestResultRESTService;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.lhfs.fsn.service.product.ProductService;
@Service
public class TraceDataProductNameServiceImpl extends BaseServiceImpl<TraceDataProductName, TraceDataProductNameDao>
implements TraceDataProductNameService {
	@Autowired private TraceDataProductNameDao traceDataProductNameDao;
	@Autowired private TraceDataDao traceDataDao;
	@Autowired private ProductService productService;
	@Autowired private BusinessUnitService  businessUnitService;
	@Autowired private ProductInstanceService  productInstanceService;
	@Autowired private TestResultService  testResultService;
	@Autowired private BusinessBrandService  businessBrandService;
	@Autowired private ProductCategoryInfoService  productCategoryInfoService;
	@Autowired private MkCategoryService categoryService;
	@Autowired private UnitService unitService;
	@Autowired private ResourceTypeService resourceTypeService;
	@Override
	public TraceDataProductNameDao getDAO() {
		return traceDataProductNameDao;
	}
	public TraceDataProductName findByProductName(String productName){
		try{
			return this.traceDataProductNameDao.findByProductName(productName);
		}catch(Exception e){
			return null;
		}
	}

	public TraceData isKeywordTraceData(String keyword){
		boolean isTraceData=false;
		StringBuilder code=new StringBuilder();
		JSONObject traceDataJson = null;
		if(keyword.contains("qc110.com")){//如果是茶叶云数据
			isTraceData=true;
			String a[]=keyword.split("=");
			code.append(a[1]);
		}else if(keyword.contains("fw.guotaiworld.com")){//国台数据
			isTraceData=true;
			try{
				URL url=new URL(keyword);
				code.append(url.getPath().substring(1));
			}catch(Exception e){
				e.printStackTrace();
				code.append(keyword);
			}
		}else if(keyword.contains("www.mnzlzs.com")){//蒙牛优益C数据
			isTraceData=true;
			String a[]=keyword.split("=");
			code.append(a[1]);
		}else if(keyword.length()==16||keyword.length()==17||keyword.length()==18){//16是茅台,17,18是蒙牛数据
			isTraceData=true;
			code.append(keyword);
		}else if(keyword.length()==39){//三维码
			isTraceData=true;
			code.append(keyword);
		}
		TraceData traceData=new TraceData();
		String _productDate=null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFormat1= new SimpleDateFormat("yyyyMMdd");
		if (isTraceData) {
			String _bcode = null;
			traceData = traceDataDao.findByKeyWord(keyword);
			if (traceData == null) {
				traceData = new TraceData();
				String appkey = PropertiesUtil.getProperty(SystemDefaultInterface.OPEN_API_KEY);
				String secretkey = PropertiesUtil.getProperty(SystemDefaultInterface.OPEN_API_SECRETKEY);
				String accesstoken = PropertiesUtil.getProperty(SystemDefaultInterface.OPEN_API_ACCESSTOKEN);
				String queryString = "method=getTraceData&version=1&format=json&code="+ code+ "&accesstoken="+ accesstoken+ "&appkey="+ appkey + "&secretkey=" + secretkey;
				String rs = SSOClientUtil.send(PropertiesUtil.getProperty(SystemDefaultInterface.OPEN_API_URL),SSOClientUtil.GET, queryString);
				rs = rs.replace("\\", "").replace("\"{", "{").replace("}\"", "}").replace("\"[", "[").replace("]\"", "]");
				JSONObject json = JSONObject.fromObject(rs);
				if (json.getString("parseError").equals("true")) {
					isTraceData = false;
				} else {
					traceDataJson = json.getJSONObject("result");
					if(keyword.length()==39){
					String _keyword = keyword.substring(26, 39);
					UploadUtil uploadUtil = new UploadUtil();
					try {
						if(businessUnitService.findByName(traceDataJson.getString("departmentName"))==null){
							BusinessUnit businessUnit=new BusinessUnit();
							businessUnit.setName(traceDataJson.getString("departmentName"));
							businessUnit.setAddress(traceDataJson.getString("address"));
							businessUnitService.create(businessUnit);
						}
						BusinessBrand businessBrand=businessBrandService.findByName(traceDataJson.getString("brand"));
						if(businessBrand==null){
							businessBrand=new BusinessBrand();
							businessBrand.setName(traceDataJson.getString("brand"));
							businessBrand.setBusinessUnit(businessUnitService.findByName(traceDataJson.getString("departmentName")));
							businessBrandService.create(businessBrand);
						}
						if(productService.findProductByBarcode(_keyword)==null){
							Product pro=new Product();
							pro.setBarcode(_keyword);
							pro.setName(traceDataJson.getString("productName"));
							pro.setCategory(productCategoryInfoService.findByCategoryId(categoryService.findCategoryByCode(traceDataJson.getString("productType")).getId()));
							pro.setFormat(traceDataJson.getString("packageSpec"));
							pro.setExpiration("24月");
							pro.setExpirationDate("720天");
							pro.setUnit(unitService.findByBusunitName("g"));
						    BusinessUnit businessUnit=businessUnitService.findByName(traceDataJson.getString("departmentName"));
							pro.setProducer(businessUnit);
							pro.setBusinessBrand(businessBrand);
							pro.setAreaID("");
							pro.setCityID("");
							pro.setProvinceID("");
							if(businessUnit.getOrganization()==null){
								pro.setOrganization(Long.valueOf(1));
							}else{
								pro.setOrganization(businessUnit.getOrganization());
							}
							for(int i=0;i<traceDataJson.getJSONArray("prodPics").size();i++){
								String _fileName=traceDataJson.getJSONArray("prodPics").getString(i);
								String suffix=_fileName.substring(_fileName.lastIndexOf(".")+1);
								String fileName=keyword+i+"."+suffix;
								Resource resource=new Resource();
								resource.setFileName(fileName);
								resource.setOrigin("三维码导入");
								resource.setName(fileName);
								ResourceType resourceType=resourceTypeService.findByName(suffix);
								resource.setType(resourceType);
								uploadUtil.uploadFile(ToPdfUtil.getImageFromURL(traceDataJson.getJSONArray("prodPics").getString(i)),PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_PRODUCT_PATH),fileName);
								if(UploadUtil.IsOss()){
									resource.setUrl(uploadUtil.getOssSignUrl(PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_PRODUCT_PATH)+"/"+fileName));
								}else{
									resource.setUrl(SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PRODUCT_PATH+"/"+fileName);
								}
								pro.getProAttachments().add(resource);
							}
							productService.create(pro);
						} 
						if (traceDataJson.getString("reportPics") != null) {
							ProductInstance productInstance = new ProductInstance();
							TestResult testResult=new TestResult();								
							String pdfPath = uploadUtil.uploadReportImg(traceDataJson.getJSONArray("reportPics"),PropertiesUtil.getProperty(FSN_FTP_UPLOAD_REPORT_PATH),keyword + ".pdf");
							productInstance.setBatchSerialNo(keyword);
							if (productService.getProductInfoByBarcode(_keyword) != null) {
								productInstance.setProduct(productService.findProductByBarcode(_keyword));
							}else{
								productInstance.setProducer(businessUnitService.findByName(traceDataJson.getString("departmentName")));
							}
							productInstanceService.create(productInstance);
							testResult.setFtpPath(pdfPath);
							testResult.setPublishFlag('1');
							testResult.setFullPdfPath(pdfPath);
							testResult.setInterceptionPdfPath(pdfPath);
							testResult.setSample(productInstance);
							testResult.setTestType("政府抽检");
							testResult.setSampleNO(keyword);
							testResult.setServiceOrder(keyword);
							testResult.setCreate_user("threeCode");
							testResult.setBrand(productService.findProductByBarcode(_keyword).getBusinessBrand());
							testResultService.create(testResult);
						}
					
					}catch (Exception e) {
						e.printStackTrace();
					}
					}
					try {
					if (keyword.length()==39) {
						if(traceDataJson.getString("productDate") .equals("")){
						    _productDate = keyword.substring(0, 8);
							traceData.setProductDate(dateFormat1.parse(_productDate));
							}
						if(traceDataJson.getString("batchCode") .equals("")){
							_bcode = keyword.substring(0, 8);
						}
					} else {
						traceData.setProductDate(dateFormat.parse(traceDataJson.getString("productDate")));
						_bcode = traceDataJson.getString("batchCode");
					}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					traceData.setBatchCode(_bcode);
					traceData.setAddress(traceDataJson.getString("address"));
					traceData.setAreaCode(traceDataJson.getString("areaCode"));
					traceData.setClassName(traceDataJson.getString("className"));
					traceData.setDegree(traceDataJson.getString("degree"));
					traceData.setDepartmentID(traceDataJson.getString("departmentID"));
					traceData.setDescription(traceDataJson.getString("description"));
					traceData.setExpireDate(traceDataJson.getString("expireDate"));
					traceData.setFactoryName(traceDataJson.getString("factoryName"));
					traceData.setNetContent(traceDataJson.getString("netContent"));
					traceData.setOrgCode(traceDataJson.getString("orgCode"));
					traceData.setPackageSpec(traceDataJson.getString("packageSpec"));
					traceData	.setProdLevel(traceDataJson.getString("prodLevel"));
					traceData.setProdLine(traceDataJson.getString("prodLine"));
					traceData.setProdMixture(traceDataJson.getString("prodMixture"));
					traceData.setProdPics(traceDataJson.getString("prodPics"));
					traceData.setProductName(traceDataJson.getString("productName"));
					traceData.setTimeTrack(traceDataJson.getString("timeTrack"));
					traceData.setVolume(traceDataJson.getString("volume"));
	//				traceData.setGpsList(traceDataJson.getString("gpsList"));//茅台GPS追溯信息
				}
			}
		}
		traceData.setTraceData(isTraceData);
		return traceData;
	}
	@Override
	public TraceDataProductName getBarcodeByProductName(String productName) {
		try{
			List<TraceDataProductName> traceDataProductNameList=  traceDataProductNameDao.getListByCondition("where e.productName=?1",new Object[]{productName});
			if(traceDataProductNameList.size()==0){
				return null;
			}else{
				return traceDataProductNameList.get(0);
			}
		}catch(Exception e){
			return null;
		}
	}
}
