package com.gettec.fsnip.fsn.web.controller.rest.sampl;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.SampleBusinessBrand;
import com.gettec.fsnip.fsn.service.sample.ImportExcelService;
import com.gettec.fsnip.fsn.vo.sampling.ExcelFileVO;
import com.gettec.fsnip.fsn.vo.sampling.SheetVO;
import com.gettec.fsnip.fsn.vo.sampling.SheetVO.Source;
/**
 * 导入抽检excel
 * @author Administrator
 */
@Controller
@RequestMapping("/sampling/import")
public class ImportExcelRESTService {
	private final static Object lock = new Object();
	
	@Autowired
	ImportExcelService importExcelService;
	 @RequestMapping(value = "/uploadExcel",method = RequestMethod.POST)
	  public View uploadExcelFile(@RequestParam(value = "files") MultipartFile[] excelFiles,@RequestParam(value = "source") String source, Model model){
		 List<ExcelFileVO> list = null;
		 synchronized (lock){
		 list = importExcelService.getWorkbook(excelFiles);
		 BusinessUnit defaultBusinessUnit = importExcelService.getDefaultBusinessUnit(); //默认企业
		 SampleBusinessBrand defaultsampleBusinessBrand = importExcelService.getDefaultSampleBusinessBrand(defaultBusinessUnit);//默认商标
			 for(ExcelFileVO f:list){
				 if(f.isFlag()){
					 for(SheetVO sheetVO:f.getSheetList()){
						 if(Source.地方.toString().equals(source)){
							 sheetVO.setSource(Source.地方);
						 }else if(Source.总局.toString().equals(source)){
							 sheetVO.setSource(Source.总局);
						 }else{
							 f.setFlag(false);
							 f.setMessage("导入失败");
							 sheetVO.setMessage("没有设置数据来源");
							 sheetVO.setList(null);
							 sheetVO.setErrorList(null);
							 break;
						 }
						 if(sheetVO.getList().size()>1100){
							 f.setFlag(false);
							 sheetVO.setList(null);
							 sheetVO.setErrorList(null);
							 sheetVO.setMessage("excel数据不能大于1000条，如果数据太多请拆分多个文件导入");
							 f.setMessage("导入失败"); 
							 break;
						 }
						 try {
							 importExcelService.saveProduct(sheetVO,defaultBusinessUnit,defaultsampleBusinessBrand);
						} catch (Exception e) {
							 f.setFlag(false);
							 sheetVO.setList(null);
							 sheetVO.setErrorList(null);
							 sheetVO.setMessage(e.toString());
							 f.setMessage("导入失败");
						}
						
					 }
				 }
			 }
		 }
		model.addAttribute("list", list);
		return JSON;
	 }
	 
	 @RequestMapping(value = "/remove",method = RequestMethod.POST)
	 public View remove(Model model){
		 return JSON;
	 }
}
