package com.gettec.fsnip.fsn.web.controller.rest.market;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PATH;
import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_SUCCESS;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.ledger.LedgerPrepackNo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.ledger.LedgerPrepackNoService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.sales.SalesResourceService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.ReadPdfUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.util.UtilImportOrExportExcel;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.ListResourceVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.lhfs.fsn.service.testReport.TestReportService;

@Controller
@RequestMapping("/resource")
public class ResourceRESTService {
	static SimpleDateFormat SDFTIME = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired private ResourceService  resourceService;
	@Autowired private TestReportService testReportService;
	@Autowired private ProductService productService;
	@Autowired private SalesResourceService salesResourceService;
	 
	@Autowired private LedgerPrepackNoService ledgerPrepackNoService;
	
	@Autowired private BusinessUnitService businessUnitService;
	
	@RequestMapping(value = "/kendoUI/removeResources", method = RequestMethod.POST) // for kendo ui upload
    public View removeResourcesKendoUI(@RequestParam String[] fileNames,  Model model) {
		model.addAttribute("result", fileNames);
		model.addAttribute("status", SERVER_STATUS_SUCCESS);
		return JSON;
    }
	
	/**
	 * 下载
	 * @param id
	 * @param response
	 * @return void
	 * @author TangXin
	 */
	@RequestMapping(method=RequestMethod.GET, value="/download/{id}")
	public void download(@PathVariable("id") Long id, @RequestParam(value="type", required=false) String type,
			HttpServletResponse response){
		try {
			String nameK = null;
			String conType = null;
			String path = null;
			InputStream in = null;
			UploadUtil uploadUtil = new UploadUtil();
			if("sales".equals(type)) {
				SalesResource sres = salesResourceService.findById(id);
				nameK = sres.getFileName().replaceAll(" ", "");
				if(sres.getType() != null){
					conType = sres.getType().getContentType();
				}
				path = "/http" + sres.getUrl().replace(PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PATH), "");
			}else {
				Resource resource = resourceService.findById(id);
				nameK = resource.getFileName().replaceAll(" ", "");
				if(resource.getType() != null){
					conType = resource.getType().getContentType();
				}
				path = "/http" + resource.getUrl().replace(PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PATH), "");
			}
			in = uploadUtil.downloadFileStream(path);
			response.setContentType(conType);
			String name = new String(nameK.getBytes("utf-8"),"ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename="+name);
			if(null != in){
				FileCopyUtils.copy(in, response.getOutputStream());
			}	
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/kendoUI/addResources/{resourceType}") // for kendo ui upload
	public View addResourcesKendoUI(@RequestParam List<MultipartFile> attachments, @PathVariable String resourceType,
				Model model) {
		try{
			Collection<Resource> rs  = getResources(attachments);
			if(resourceType.equals("items")){
				List<TestProperty> items = resourceService.paseExcelByResource(rs!=null?rs.iterator().next():null);
//				testReportService.saveTestProperty(items);
				model.addAttribute("results", items);
				model.addAttribute("status", SERVER_STATUS_SUCCESS);
				return JSON;
			}
			if(resourceType!=null&&"excel".equals(resourceType)&&attachments.size()>0){
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
				Map<String, Object> objMap =  UtilImportOrExportExcel.importExcelFile(attachments.get(0));
               Iterator<String> iter = objMap.keySet().iterator();
               while  (iter.hasNext()) {
            	    String keys = iter.next();
            	    @SuppressWarnings("unchecked")
					TreeMap <String,TreeMap <String,TreeMap <String,Object>>> sheetMap = (TreeMap<String, TreeMap<String, TreeMap<String, Object>>>) objMap.get(keys);
                	   ledgerPrepackNoService.saveledgerPrepackNo(sheetMap,fromBusId);
				}
			}else{
				List<Resource> list  = resourceService.addResourcesKendoUI(rs);
				model.addAttribute("results", list);
			}
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
		}catch(Exception e){
			model.addAttribute("status", SERVER_STATUS_FAILED);
			e.printStackTrace();
			return null;
		}
		return JSON;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/kendoUI/addSalesResources") // for kendo ui upload
	public View addSalesResources(@RequestParam List<MultipartFile> attachments, Model model) {
		try{
			Collection<Resource> rs  = getResources(attachments);
			List<Resource> list  = resourceService.addResourcesKendoUI(rs);
			List<SalesResource> listSalesRes = null;
			if(list != null && list.size() > 0) {
				listSalesRes = new ArrayList<SalesResource>();
				for(Resource res : list) {
					SalesResource salesRes = new SalesResource();
					salesRes.setFileName(res.getFileName());
					salesRes.setFile(res.getFile());
					salesRes.setType(res.getType());
					salesRes.setDelStatus(0);
					salesRes.setSort(-1);
					salesRes.setCover(0);
					salesRes.setGuid(SalesUtil.createGUID());
					listSalesRes.add(salesRes);
				}
			}
			model.addAttribute("results", listSalesRes);
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
		}catch(Exception e){
			model.addAttribute("status", SERVER_STATUS_FAILED);
			e.printStackTrace();
			return null;
		}
		return JSON;
	}
	
	/**
	 * 通过读取并解析蒙牛pdf，生成报告
	 * @param attachments
	 * @param resourceType
	 * @param flag 
	 * true: 蒙牛上传pdf
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/kendoUI/addResources/automnPdf") // for kendo ui upload
	public View addTestResources(@RequestParam List<MultipartFile> attachments, Model model) {
		try{
			Collection<Resource> rs  = getTestResources(attachments);
			String message = "";
			for(Resource ts : rs){
				// 蒙牛解析pdf 
				message = (String) ReadPdfUtil.readFileOfPDF(ts.getFile(), productService, testReportService).get("message");
			}
			if(message.equals("can save")){
				List<Resource> list  = testReportService.addTestResources(rs);
				model.addAttribute("results", list);
				model.addAttribute("status", SERVER_STATUS_SUCCESS);
			}else if(message.equals("mismatching template")){
				model.addAttribute("status", SERVER_STATUS_FAILED);
				model.addAttribute("pdfTypeError", "true");
			}else if(message.equals("mismatching product")){
				model.addAttribute("status", SERVER_STATUS_FAILED);
				model.addAttribute("mismatchProduct", "true");
			}else if(message.equals("has exist")){
				model.addAttribute("status", SERVER_STATUS_FAILED);
				model.addAttribute("hasExist", "true");
			}
		}catch(Exception e){
			model.addAttribute("status", SERVER_STATUS_FAILED);
			e.printStackTrace();
			return null;
		}
		return JSON;
	}
	
	@RequestMapping(value = "/kendoUI/removeTestResources", method = RequestMethod.POST) // for kendo ui upload
    public View removeTestResources(@RequestParam String[] fileNames,  Model model) {
		model.addAttribute("result", fileNames);
		model.addAttribute("status", SERVER_STATUS_SUCCESS);
		return JSON;
    }
	
	/**
	 * 报告录入界面，用户上传图片合成pdf的方法
	 * @param resource
	 * @param reportNo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/picturesToPdf", method = RequestMethod.POST)
    public View downLoadResByUrl(@RequestBody ListResourceVO resVo,@RequestParam String reportNo, Model model) {
		try {
			Resource pdfRes = resourceService.getPdfByPcitures(resVo.getListResource(), reportNo, resVo.getReportTestType());
			model.addAttribute("pdf", pdfRes);
		} catch (ServiceException sex) {
			throw new RuntimeException("IOError writing file to output stream");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
    }
	
	private Collection<Resource> getResources(List<MultipartFile> files){
		List<Resource> attachments = new ArrayList<Resource>();
        for(MultipartFile mpf : files ){
        	Resource resource = new Resource();
            try {
            	byte[] fileISO8559 = mpf.getOriginalFilename().getBytes("UTF-8"); 
            	String fileUTF8 = new String(fileISO8559, "utf-8");
            	resource.setFileName(fileUTF8);
                resource.setName(fileUTF8);
				resource.setFile(mpf.getBytes());
				resource.setUploadDate(new Date());
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
            if(resource.getFile() == null){
            	continue;
            }
            attachments.add(resource);
        }        
        return attachments;
	}
	
	private Collection<Resource> getTestResources(List<MultipartFile> files){
		List<Resource> attachments = new ArrayList<Resource>();
        for(MultipartFile mpf : files ){
        	Resource resource = new Resource();
            try {
            	byte[] fileISO8559 = mpf.getOriginalFilename().getBytes("utf-8");
            	String fileUTF8 = new String(fileISO8559, "utf-8");
            	resource.setFileName(fileUTF8);
                resource.setName(fileUTF8);
				resource.setFile(mpf.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
            if(resource.getFile() == null){
            	continue;
            }
            attachments.add(resource);
        }        
        return attachments;
	}
}
