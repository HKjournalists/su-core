package com.gettec.fsnip.fsn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
/**
 * excel导出和导入公共类
 * @author wb
 *
 */
public class UtilImportOrExportExcel {
	
	private static  HSSFWorkbook wb2003 =  null;
	private static  HSSFSheet sheet2003 = null;
	
	private static  XSSFWorkbook wb2007 = null;
	private static  XSSFSheet sheet2007 = null;
	

	private static FileOutputStream out = null;
	private static InputStream in = null;
	private static String fileName = null;
	private static Logger LOG = Logger.getLogger(UtilImportOrExportExcel.class);
	/**
	 * 导入功能
	 * @param excel
	 * @return
	 * @throws IOException 
	 */
	public static  Map<String, Object> importExcelFile(
			MultipartFile excel) throws IOException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		TreeMap <String,TreeMap <String,TreeMap <String,Object>>> sheetMap = new TreeMap<String, TreeMap<String,TreeMap<String,Object>>>(); 
		try {
			String importName = excel.getOriginalFilename();
			String excelVersion = "";	
			if(importName.length()>0){
					int len = importName.lastIndexOf(".");
					excelVersion = importName.substring(len, importName.length());
					in = excel.getInputStream();  	
				}
				sheetMap.clear();
//				判断是否office2003
				if(".xls".equalsIgnoreCase(excelVersion)){
				if (in != null) {
//					 创建Excel2003文件对象
	                	wb2003 = new HSSFWorkbook(in);
	                }
	                HSSFSheet sheet2003 = null;
	                map = importExcel2003(sheetMap,wb2003,sheet2003);
				    
//				  判断是否office2003
				}else if(".xlsx".equalsIgnoreCase(excelVersion)){
					 if(in!=null){
//					 创建Excel2007文件对象
		                	wb2007 = new XSSFWorkbook(in);  
		                }
					XSSFSheet sheet2007 = null;
					map = importExcel2007(sheetMap,wb2007,sheet2007);
				}else{
					sheetMap.clear();
					map.put("restul", sheetMap);
					map.put("message", "您导入的不文件不是office2003版,也不是office2007版,不支持其他文件导入!");
					return map;
				}
		    } catch (FileNotFoundException e) {
		    	sheetMap.clear();
				map.put("restul", sheetMap);
		    	map.put("message", "找不到文件！");
			} catch (IOException e) {
				sheetMap.clear();
				map.put("restul", sheetMap);
				map.put("message", "文件读取错误！");
			}
	return map;
	}

	/**
	 * 导出Excel
	 * @param treeMap
	 * @param filename 
	 * @param resp 
	 * @param pathExport
	 * @return
	 * @throws IOException 
	 */
   public static String exportFile(TreeMap<String, TreeMap<Integer, Map<Integer, Object>>> treeMap, String path, String filename, HttpServletResponse resp) throws IOException {
	   
	   int len = filename.lastIndexOf(".");
	   String excelVersion = filename.substring(len, filename.length());
			
	   //判断是否office2003
	   if(".xls".equals(excelVersion)||".XLS".equals(excelVersion)){
		   HSSFWorkbook wb2003 =  new HSSFWorkbook();
		   HSSFSheet sheet2003 = null;
		   fileName = exportExcel2003(wb2003,sheet2003,path,filename,treeMap,resp);
           //判断是否office2007
	    }else if(".xlsx".equals(excelVersion)||".XLSX".equals(excelVersion)){
	    	XSSFWorkbook wb2007 = new XSSFWorkbook();
            XSSFSheet sheet2007 = null;
			fileName = exportExcel2007(wb2007,sheet2007,path,filename,treeMap,resp);
		}else{
			return fileName;
		}
		treeMap.clear();
		return fileName;
	}
   
   /**
    * 导出Excel office2007
 * @param wb2007 
 * @param sheet2007 
    * @param exportName
 * @param filename2 
    * @param treeMap
 * @param resp 
    * @return
    */
	@SuppressWarnings("rawtypes")
	private static String exportExcel2007(XSSFWorkbook wb2007, XSSFSheet sheet2007, String path,String filename, TreeMap<String, TreeMap<Integer, Map<Integer, Object>>> treeMap, HttpServletResponse resp) {
		try {
			Iterator sit = treeMap.keySet().iterator();
				while(sit.hasNext()){
					
					String sheetkey = (String) sit.next();	
					if(!"".equals(sheetkey)){
						sheet2007 = wb2007.createSheet(sheetkey.replace("1", "").replace("2", "").replace("3", ""));
					}
					TreeMap<Integer,Map<Integer,Object>> rowMap = treeMap.get(sheetkey);
				    Iterator rit = rowMap.keySet().iterator();
				
				 int rowNum=0;
				while(rit.hasNext()){	
					Integer rkey = (Integer) rit.next();	
					//创建每一行
					XSSFRow row = sheet2007.createRow((short) rowNum);
					Map<Integer,Object> colMap = rowMap.get(rkey);
					Iterator cit = colMap.keySet().iterator();
					int colSum =0;
					while(cit.hasNext()){
						//定义每一列的宽度
						if(colSum == 1 || colSum == 4){
							sheet2007.setColumnWidth(colSum, 4000);
						}else{
							sheet2007.setColumnWidth(colSum, 8000);
						}
						int ckey = (Integer) cit.next();
						String obj = (String)colMap.get(ckey);
						String value = "";
						if(obj!=null&&!obj.equals("")){
							value = obj.toString();
						}
						//创建数据存放位置（值）
						XSSFCell cell = row.createCell(colSum);
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(new XSSFRichTextString(value));
						//指定合并区域 参数：1，起始行；2，起始列；3，结束行；4，结束列
						//sheet2003.addMergedRegion(new Region(sheet2003.getLastRowNum(), (short) 0,sheet2003.getLastRowNum(), (short) colSum));
						if(rowNum==0){
							//创建单元格样式
							XSSFCellStyle cellStyle = getCellStyle2007(wb2007);
							cell.setCellStyle(cellStyle);
						}else{
							// 创建单元格样式
							XSSFCellStyle cellStyle = wb2007.createCellStyle();
							//对其方式
							cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER_SELECTION);
							XSSFDataFormat format= wb2007.createDataFormat();   
							cellStyle.setDataFormat(format.getFormat("@"));  
							
							//下边框
							cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); 
							//左边框
							cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
							//上边框
							cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
							//右边框
							cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
							cell.setCellStyle(cellStyle);
					    }
						colSum++;
						}
					rowNum++;
					}
					if("2检测结果".equals(sheetkey)){//隐藏ID列
						sheet2007.setColumnHidden(0,true);
					}
				}
				fileName = outputExcel(path,filename,wb2003,wb2007,"office2007",resp);
		} catch (Exception e) {
			LOG.error("错误消息 - Exception - "+e.getMessage());
			e.printStackTrace();
		}
		return fileName;
	}
	
	/**
	 * 导出excel2003
	 * @param wb2003 
	 * @param sheet2003 
	 * @param filename2 
	 * @param treeMap
	 * @param resp 
	 * @throws IOException 
	 */
	@SuppressWarnings("rawtypes")
	private static String exportExcel2003(HSSFWorkbook wb2003, HSSFSheet sheet2003, String path,String filename, TreeMap<String, TreeMap<Integer, Map<Integer, Object>>> treeMap, HttpServletResponse resp) throws IOException {
		
		   try {
			   //创建sheet对象集合
			   	Iterator sit = treeMap.keySet().iterator();
				while(sit.hasNext()){
					String sheetkey = (String) sit.next();	
					if(!"".equals(sheetkey)){
					sheet2003 = wb2003.createSheet(sheetkey.replace("1", "").replace("2", "").replace("3", ""));
					}
//					创建每一个sheet下的对象集合
					TreeMap<Integer,Map<Integer,Object>> rowMap = treeMap.get(sheetkey);
				    Iterator rit = rowMap.keySet().iterator();
				    int rowNum = 0;
					while(rit.hasNext()){	
						int rkey = (Integer) rit.next();	
				        
						//创建每一行
						HSSFRow row = sheet2003.createRow((short) rowNum);
						
						
						Map<Integer,Object> colMap = rowMap.get(rkey);
						Iterator cit = colMap.keySet().iterator();
						int colSum =0;
						while(cit.hasNext()){
							if(colSum == 1 || colSum == 4){
								sheet2003.setColumnWidth(colSum, 4000);
							}else{
								sheet2003.setColumnWidth(colSum, 8000);
							}
							int ckey = (Integer) cit.next();
							String obj = (String)colMap.get(ckey);
						
							String value = "";
							if(obj!=null&&!obj.equals("")){
								value = obj.toString();
							}
							//创建数据存放位置（值）
							HSSFCell cell = row.createCell(colSum);
							cell.setCellType(HSSFCell.ENCODING_UTF_16);
							cell.setCellValue(new HSSFRichTextString(value));
							
							//指定合并区域 参数：1，起始行；2，起始列；3，结束行；4，结束列
							//sheet2003.addMergedRegion(new Region(sheet2003.getLastRowNum(), (short) 0,sheet2003.getLastRowNum(), (short) colSum));
							if(rowNum==0){
							//创建单元格样式@Deprecated
							HSSFCellStyle cellStyle = getCellStyle2003(wb2003);
							cell.setCellStyle(cellStyle);
							}else{
								// 创建单元格样式
								HSSFCellStyle cellStyle = wb2003.createCellStyle();
								//对其方式,（居左:ALIGN_LEFT,居中:ALIGN_CENTER_SELECTION,居右:ALIGN_RIGHT）
								cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
								HSSFDataFormat format = wb2003.createDataFormat();   
								cellStyle.setDataFormat(format.getFormat("@"));
								
								if("2检测结果".equals(sheetkey) && colSum==0){
									cellStyle.setHidden(true);
								}
								//自动换行
								cellStyle.setWrapText(true);  
								
								//下边框
								cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); 
								//左边框
								cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
								//上边框
								cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
								//右边框
								cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
								cell.setCellStyle(cellStyle);
						    }
							//如果是标准从第1行开始，对excel样式处理，其他域进入，表示从弟2行开始做样式处理。
							
							colSum++;
						}
						rowNum++;
					}
					if("2检测结果".equals(sheetkey)){//隐藏ID列
						sheet2003.setColumnHidden(0, true);
					}
				}
				fileName = outputExcel(path,filename,wb2003,wb2007,"office2003",resp);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return  fileName;
		
	}
	/**
	 * 导入Excel
	 * @param fileName
	 * @return
	 */  
	public static Map<String, Object> importFile(String importName) {
		Map<String,Object> map = new HashMap<String, Object>();  	
		TreeMap <String,TreeMap <String,TreeMap <String,Object>>> sheetMap = new TreeMap<String, TreeMap<String,TreeMap<String,Object>>>(); 
		try {
				int len = importName.lastIndexOf(".");
				String excelVersion = importName.substring(len, importName.length());
				in = new FileInputStream(importName);  
				
				sheetMap.clear();
//				判断是否office2003
				if(".xls".equalsIgnoreCase(excelVersion)){
					HSSFWorkbook workbook2003 =  new HSSFWorkbook();
					HSSFSheet sheet2003 = null;
	
//					 创建Excel2003文件对象
					workbook2003 = new HSSFWorkbook(in);
					map = importExcel2003(sheetMap,workbook2003,sheet2003);
				    
//				  判断是否office2003
				}else if(".xlsx".equalsIgnoreCase(excelVersion)){
					
					XSSFWorkbook workbook2007 = new XSSFWorkbook();
					XSSFSheet sheet2007 = null;
//					 创建Excel2007文件对象
					workbook2007 = new XSSFWorkbook(in);  
					map = importExcel2007(sheetMap,workbook2007,sheet2007);
				}else{
					sheetMap.clear();
					map.put("result", sheetMap);
					map.put("message", "您导入的不文件不是office2003版,也不是office2007版,不支持其他文件导入!");
					return map;
				}
			
		    } catch (FileNotFoundException e) {
		    	sheetMap.clear();
				map.put("restul", sheetMap);
		    	map.put("message", "找不到文件！");
			} catch (IOException e) {
				sheetMap.clear();
				map.put("restul", sheetMap);
		    	map.put("message", "找不到文件！");
			}	
	
	return map;
	}
	/**
	 * 导入excel2007
	 * @param sheetMap
	 * @param wb2007
	 * @param sheet2007 
	 * @return
	 */
	private static Map<String, Object>  importExcel2007(TreeMap<String, TreeMap<String, TreeMap<String, Object>>> sheetMap, XSSFWorkbook wb2007, XSSFSheet sheet2007) {
		
		Map<String,Object> map = new HashMap<String, Object>();  	
		// 读取多个sheet的数据
		for (int numSheet = 0; numSheet < wb2007.getNumberOfSheets(); numSheet++) {
			
			// 每一个sheet
			sheet2007 = wb2007.getSheetAt(numSheet);
			if (sheet2007 == null)
				break;
			// 每一个sheet则是一个对象(即：excel面板)
			TreeMap<String, TreeMap<String, Object>> rowMap = new TreeMap<String, TreeMap<String, Object>>();
			
			if(!"标准曲线".equals(sheet2007.getSheetName())){
			// 循环每一个sheet的数据
				for (int rowNum = 1; rowNum < sheet2007.getLastRowNum()+1-sheet2007.getFirstRowNum(); rowNum++) {
	
					// 每一行
					XSSFRow row0 = sheet2007.getRow(0);
					XSSFRow row = sheet2007.getRow(rowNum);
					if("基本信息".equals(sheet2007.getSheetName())&&rowNum>1){
						break;
					}
					if (row == null)
						break;
					
					String ID = "";
					// 获取每一行
					TreeMap<String, Object> cellMap = new TreeMap<String, Object>();
					for (int colSum = 0; colSum < row.getLastCellNum(); colSum++) {
						try {
							// 获取值
							XSSFCell cell = row.getCell(colSum);
							if(cell==null){
								continue;
							}
							
							ID = row.getCell(0)+"";
							if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
								if (HSSFDateUtil.isCellDateFormatted(cell)) {  
							        double d = cell.getNumericCellValue();  
							        Date date = HSSFDateUtil.getJavaDate(d);  
							        cellMap.put(row0.getCell(colSum)+"", date);
							    }else{
							    	DecimalFormat df = new DecimalFormat("#");
							    	cellMap.put(row0.getCell(colSum)+"", df.format(cell.getNumericCellValue()));
//			                System.out.println("type666=="+df.format(cell.getNumericCellValue()));
//							    	cell.setCellType (XSSFCell.CELL_TYPE_STRING);
//							    	cellMap.put(row0.getCell(colSum)+"", cell.toString());
							    }  
							}else{
								  cellMap.put(row0.getCell(colSum)+"", cell.toString());
							}
							
//						String value = "";
//						if(cell!=null&&!cell.equals("")&&cell.getCellType()==cell.CELL_TYPE_STRING){
//							if(cell.toString().indexOf("月")==-1){
//								cell.setCellType (cell.CELL_TYPE_STRING);
//								}
//							value = cell.toString ();
//						}
//					  cellMap.put(row0.getCell(colSum)+"", value);
							if("检测结果".equals(sheet2007.getSheetName())){
								rowMap.put(ID, cellMap);
							}else{
							rowMap.put("sheet_" + numSheet + "_row_" + rowNum, cellMap);
							}
						} catch (Exception e) {
							sheetMap.clear();
							map.put("result", sheetMap);
							map.put("message", "导入"+sheet2007.getSheetName()+"时,列表中第"+rowNum+"行,第"+colSum+"列信息填写错误,执行终止！");
						}
						
					}
				 }
				
			}else{
				// 循环每一个sheet的数据
				for (int rowNum = 0; rowNum < sheet2007.getLastRowNum()+1; rowNum++) {
					
					XSSFRow row = sheet2007.getRow(rowNum);
					if (row == null)
						break;
					// 获取每一行
					TreeMap<String, Object> cellMap = new TreeMap<String, Object>();
					for (int colSum = 0; colSum < row.getLastCellNum(); colSum++) {
						try {
							if(row.getCell(colSum)==null){
								cellMap.put(colSum+"_"+row.getCell(0)+"", "");
								continue;
							}
							row.getCell(colSum).setCellType (row.getCell(colSum).CELL_TYPE_STRING);
							 cellMap.put(colSum+"_"+row.getCell(0)+"", row.getCell(colSum));
						} catch (Exception e) {
							sheetMap.clear();
							map.put("result", sheetMap);
							map.put("message", "导入"+sheet2007.getSheetName()+"时,列表中第"+rowNum+"行,第"+colSum+"列信息填写错误,执行终止！");
						}
					}
					rowMap.put(row.getCell(0)+"", cellMap);
				}
			}
			//sheetMap.put("s_" + numSheet, rowMap);
			sheetMap.put(sheet2007.getSheetName(), rowMap);
		}
		map.put("result", sheetMap);
		return map;
	}

	/**
	 * 导入excel2003
	 * @param sheetMap
	 * @param sheet20032 
	 * @param wb2003
	 * @return
	 */
	@SuppressWarnings("static-access")
	private static Map<String, Object> importExcel2003(TreeMap<String, TreeMap<String, TreeMap<String, Object>>> sheetMap, HSSFWorkbook wb2003, HSSFSheet sheet2003) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 读取多个sheet的数据
			for (int numSheet = 0; numSheet < wb2003.getNumberOfSheets(); numSheet++) {
					// 每一个sheet
				sheet2003 = wb2003.getSheetAt(numSheet);
				if (sheet2003.equals("null")){
					break;
				}
				// 每一个sheet则是一个对象(即：excel面板)
				TreeMap<String, TreeMap<String, Object>> rowMap = new TreeMap<String, TreeMap<String, Object>>();
				String sheetName = sheet2003.getSheetName();
				if(sheetName!=null ){
						if(!"标准曲线".equals(sheetName)){
							for (int rowNum = 1; rowNum < sheet2003.getLastRowNum()+1-sheet2003.getFirstRowNum(); rowNum++) {
								// 每一行
								HSSFRow row0 = sheet2003.getRow(0);
								HSSFRow row = sheet2003.getRow(rowNum);
								
								if("基本信息".equals(sheet2003.getSheetName())&&rowNum>1){
									break;
								}
								if (row == null)
									break;
								
								String ID = "";
								// 获取每一行
								TreeMap<String, Object> cellMap = new TreeMap<String, Object>();
								for (int colSum = 0; colSum < row.getLastCellNum(); colSum++) {
									
									try {
										ID = rowNum+"";//自定义Map的KEY  id
										// 获取值
										HSSFCell cell = row.getCell(colSum);
										if(cell==null){
											continue;
										}
										if(cell.getCellType()==cell.CELL_TYPE_NUMERIC){
											if (HSSFDateUtil.isCellDateFormatted(cell)) {  
												double d = cell.getNumericCellValue();  
												Date date = HSSFDateUtil.getJavaDate(d);  
												cellMap.put(row0.getCell(colSum)+"", date);
											}else{
												cell.setCellType (cell.CELL_TYPE_STRING);
												cellMap.put(row0.getCell(colSum)+"", cell.toString());
											}  
										}else{
											cellMap.put(row0.getCell(colSum)+"", cell.toString());
										}
										
										if("检测结果".equals(sheet2003.getSheetName())){
											rowMap.put(ID, cellMap);
										}else{
											rowMap.put("sheet_" + numSheet + "_row_" + rowNum, cellMap);
										}
									} catch (Exception e) {
										sheetMap.clear();
										map.put("result", sheetMap);
										map.put("message", "导入"+sheetName+"时,列表中第"+rowNum+"行,第"+colSum+"列信息填写错误,执行终止！");
									}
								}
							}
						}else{
							for (int rowNum = 0; rowNum < sheet2003.getLastRowNum()+1; rowNum++) {
								
								HSSFRow row = sheet2003.getRow(rowNum);
								if (row == null)
									break;
								// 获取每一行
								TreeMap<String, Object> cellMap = new TreeMap<String, Object>();
								for (int colSum = 0; colSum < row.getLastCellNum(); colSum++) {
									try {
										if(row.getCell(colSum)==null){
											cellMap.put(colSum+"_"+row.getCell(0)+"", "");
											continue;
										}
										row.getCell(colSum).setCellType (row.getCell(colSum).CELL_TYPE_STRING);
										cellMap.put(colSum+"_"+row.getCell(0)+"", row.getCell(colSum));
									} catch (Exception e) {
										sheetMap.clear();
										map.put("result", sheetMap);
										map.put("message", "导入"+sheetName+"时,列表中第"+rowNum+"行,第"+colSum+"列信息填写错误,执行终止！");
										e.printStackTrace();
									}
								}
								rowMap.put(row.getCell(0)+"", cellMap);
							}
						}
				}
				sheetMap.put(sheet2003.getSheetName(), rowMap);
			}
		} catch (RuntimeException e) {
			sheetMap.clear();
			map.put("result", sheetMap);
			map.put("message", "导入文件超时！");
			e.printStackTrace();
		}
		map.put("result", sheetMap);
	return map;
    }
	
	private static XSSFCellStyle getCellStyle2007(XSSFWorkbook wb) {
       
		XSSFCellStyle cellStyle = wb.createCellStyle();
        
		//指定单元格居中对齐
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       
		//指定单元格垂直居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		// 指定单元格自动换行
		cellStyle.setWrapText(true);
       
		//设置单元格的背景颜色． 
		cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		// 设置单元格字体
		XSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);
		
		//下边框
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); 
		//左边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		//上边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		//右边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		
		return cellStyle;
	}
	/**
	 * 创建文字和背景样式
	 * @param wb
	 * @return
	 */
	private static HSSFCellStyle getCellStyle2003(HSSFWorkbook wb) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
        
		//指定单元格居中对齐
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       
		//指定单元格垂直居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		// 指定单元格自动换行
		cellStyle.setWrapText(true);
       
		//设置单元格的背景颜色． 
		cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		// 设置单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);
		
		//下边框
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); 
		//左边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		//上边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		//右边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}
	
	/**
	 * 输入EXCEL文件 ===
	 * 
	 * @param fileName
	 *            文件名
	 * @param filename 
	 * @param resp 
	 * @throws IOException 
	 */
	public static  String outputExcel(String path,String filename, HSSFWorkbook wb03 ,XSSFWorkbook wb07,String office, HttpServletResponse resp) throws IOException {
		
		try {
			File f = new File(path);
			//判定文件夹是否存在，不存在则创建文件夹
			if (!f.exists() && !f.isDirectory()) {
				f.mkdir();
				//文件夹下的文件删除
			}
			String fList[] = f.list();
			for (int i = 0; i < fList.length; i++) {
				File fs = new File(path+"/"+fList[i]);
				if (!fs.isDirectory()) {
					//文件夹下的文件删除
					fs.delete();
				}
			}
			fileName = path+"/"+filename;
			FileOutputStream out = new FileOutputStream(new File(fileName));
			if(office.equals("office2007")){
				wb07.write(out);
			}else if(office.equals("office2003")){
				wb03.write(out);
			}else{
				LOG.error("outputExcel - 错误消息 - 不支持此文件！");
			}
			out.flush();   //不可少
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}
		return fileName;
	}

	/**
	 * @param wb
	 * @param sheet
	 */
	public UtilImportOrExportExcel(HSSFWorkbook wb, HSSFSheet sheet) {
		super();
		this.wb2003 = wb;
		this.sheet2003 = sheet;
	}
	public UtilImportOrExportExcel() {
		super();
	}

	/**
	 * @param wb
	 * @param sheet
	 */
	public UtilImportOrExportExcel(XSSFWorkbook wb, XSSFSheet sheet) {
		super();
		this.wb2007 = wb;
		this.sheet2007 = sheet;
	}
	public static InputStream getIn() {
		return in;
	}

	public static void setIn(InputStream in) {
		UtilImportOrExportExcel.in = in;
	}

	public static FileOutputStream getOut() {
		return out;
	}

	public static void setOut(FileOutputStream out) {
		UtilImportOrExportExcel.out = out;
	}

	public static HSSFSheet getSheet2003() {
		return sheet2003;
	}

	public static void setSheet2003(HSSFSheet sheet2003) {
		UtilImportOrExportExcel.sheet2003 = sheet2003;
	}

	public static XSSFSheet getSheet2007() {
		return sheet2007;
	}

	public static void setSheet2007(XSSFSheet sheet2007) {
		UtilImportOrExportExcel.sheet2007 = sheet2007;
	}

	public static HSSFWorkbook getWb2003() {
		return wb2003;
	}

	public static void setWb2003(HSSFWorkbook wb2003) {
		UtilImportOrExportExcel.wb2003 = wb2003;
	}

	public static XSSFWorkbook getWb2007() {
		return wb2007;
	}

	public static void setWb2007(XSSFWorkbook wb2007) {
		UtilImportOrExportExcel.wb2007 = wb2007;
	}
	public static String getFileName() {
		return fileName;
	}
	public static void setFileName(String fileName) {
		UtilImportOrExportExcel.fileName = fileName;
	}
}
