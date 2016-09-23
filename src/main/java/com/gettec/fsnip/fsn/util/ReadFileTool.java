package com.gettec.fsnip.fsn.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.vo.product.ProductVO;
import com.gettec.fsnip.fsn.vo.sampling.ExcelFileVO;
import com.gettec.fsnip.fsn.vo.sampling.SheetVO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lhfs.fsn.util.StringUtil;

public class ReadFileTool {
	private static final String START_TITLE = "序号";  //开始读取行的标识
	private static final Integer START_TEST_COLUMNNUM = 9; //检测项目起始列
	private static final Integer ON_COLUMNNUM =12;  //不合格表格列数
	private static String[] PROPERTY_ARRAY = {"orderNumber","companyName","companyAddress","sampleCompanyName","sampleCompanyAddress","productName","format","businessName","productionDate"};
	private static String[] TEST_PROPERTY_ARRAY = {"name","result","techIndicator"};
	private static String[] OK_PROPERTY_ARRAY = {"orderNumber","companyName","companyAddress","sampleCompanyName","sampleCompanyAddress","productName","format","productionDate"};
	private static String[] DATE_STR = {"yyyy-MM-dd","yyyy-MM-d","yyyy-M-dd","yyyy-M-d","yyyy/MM/dd","yyyy年MM月dd日","yyyy年MM月d日","yyyy年M月dd日","yyyy年M月d日","yyyy/MM/d","yyyy/M/dd","yyyy/M/d","yyyyMMdd"};
	private static String[] COLUMN_NAME ={"序号","标称生产单位名称#标识生产企业名称#标称生产企业名称","标称生产单位地址#标称生产企业地址#标识生产企业地址","被抽样单位#被抽样单位名称#被抽检单位名称","被抽检单位单位地址#被抽样单位地址#被抽样单位所在省份#被抽样单位所在省","食品名称#样品名称","样品规格#规格型号#规格","生产日期#生产日期/批号#生产/购进日期"};
	private static String[] COLUMN_NAME_NO ={"序号","标称生产单位名称#标注生产单位名称#标称生产企业名称#标识生产企业名称#标识生产单位名称","标称生产单位地址#标称生产企业地址#标称生产单位地址#标识生产单位地址#标识生产企业地址","被抽样单位#被抽样单位名称#被抽检单位名称","被抽检单位单位地址#被抽样单位地址#抽样地点#被抽样单位所在省份#被抽样单位所在省","样品名称#食品名称","规格型号#样品规格#规格","商标","批号#生产日期#生产日期/批号#生产/购进日期","不合格项目#检验项目#不合格项目（标准值/实测值）","检验结果","标准值"};
	private static Gson gson;
	
	/**
	 * 解析Microsoft Excel 2007/2010文件(*.xlsx)工作簿
	  * xlsx2007Workbook
	  * @param @param xssfWorkbook  工作簿
	  * @param @return
	  * @return Map<String,Object> 返回类型
	  * @throws
	 */
	public static ExcelFileVO analyticalWorkbook(XSSFWorkbook xssfWorkbook){
		ExcelFileVO excelFileVO = new ExcelFileVO();
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++){
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			//总行数
			int rowCount = xssfSheet.getLastRowNum();
			if(rowCount==0){
				continue;
			}
			SheetVO sheetVO = new SheetVO();
			sheetVO.setSheetName(xssfSheet.getSheetName());
			//取得读取数据起始行
			Map<String,Object> map = stratNum(xssfSheet);
			if((Boolean)map.get("flag")){
				sheetVO.setMessage("工作表格式错误，没有找到表头！");
				excelFileVO.setFlag(false);
				excelFileVO.getSheetList().add(sheetVO);
				excelFileVO.setMessage("导入失败");
				continue;
			}
			int startNum = (Integer)map.get("stratNum");
			int columnCount = (Integer)map.get("lastCallNum");
			if(!(columnCount==8||columnCount==12)){
				sheetVO.setMessage("工作表格式错误，表格列数不对应为12或8列！");
				excelFileVO.setFlag(false);
				excelFileVO.getSheetList().add(sheetVO);
				excelFileVO.setMessage("导入失败");
				continue;
			}
			
			boolean flag = isValidTitle(xssfSheet,startNum,columnCount);
			if(!flag){
				sheetVO.setMessage("工作表表头顺序不对");
				excelFileVO.setFlag(false);
				excelFileVO.getSheetList().add(sheetVO);
				excelFileVO.setMessage("导入失败");
				continue;
			}
			
			//列数为8则认为合格
			if(columnCount==8){
				sheetVO.setPass(Boolean.TRUE);
			}
			
			List<ProductVO> list = new ArrayList<ProductVO>();
			List<ProductVO> errorList = new ArrayList<ProductVO>();
			//从表头的下行遍历
			for(int rowNum = startNum+1;rowNum<=rowCount;rowNum++){
				Map<String,Object> mergedRegionMap = isMergedRegion(xssfSheet,rowNum);
				if((Boolean) mergedRegionMap.get("isMergedRegion")){
					CellRangeAddress cellRangeAddress = (CellRangeAddress) mergedRegionMap.get("cellRangeAddress");
					//如果为合并多列单元格则跳过不解析
					if(cellRangeAddress.getLastColumn()-cellRangeAddress.getFirstColumn()>1){
						continue;
					}
					ProductVO productVO = getCallValue(xssfSheet,cellRangeAddress,columnCount);
					if(null!=productVO){
						String validMessag="";
						productVO = isValidDate(productVO);
					/*	if(isValidDate(productVO)==null){
							validMessag+="日期不能识别，如果包含批号请用#分割,日期在前,如：2015年1月5日#T20150105/2015年1月5日";
						}*/
						if(StringUtil.isBlank(productVO.getProductName())){
							validMessag+="|样品名称不能为空";
						}
						if(!sheetVO.getPass()&&productVO.getTestProperty().size()>0){
							for(TestProperty t:productVO.getTestProperty()){
								if(StringUtil.isBlank(t.getName())){
									validMessag+="|不合格项目不能为空";
									break;
								}
								if(StringUtil.isBlank(t.getResult())){
									validMessag+="|检验结果不能为空";
									break;
								}
							}
						}
						if("".equals(validMessag)){
							list.add(productVO);
						}else{
							productVO.setValidMessage(validMessag);
							errorList.add(productVO);
						}
						
					}
					rowNum = cellRangeAddress.getLastRow();
					continue;
				}
				CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum,rowNum,0, 0);
				ProductVO productVO = getCallValue(xssfSheet,cellRangeAddress,columnCount);
				if(null!=productVO){
					String validMessag="";
					productVO = isValidDate(productVO);
				/*	if(isValidDate(productVO)==null){
						validMessag+="日期不能识别，如果包含批号请用#分割,日期在前,如：2015年1月5日#T20150105/2015年1月5日";
					}*/
					if(!StringUtil.isBlank(productVO.getProduction_Date())){
						  Calendar calendar = Calendar.getInstance();
						  calendar.setTime(productVO.getProduction_Date());
						  int year = calendar.get(Calendar.YEAR);//获取年份
						  if(year<1900||year>2099){
								validMessag+="日期年份必需在1900~2099之间";
						  }
					}
					if(StringUtil.isBlank(productVO.getProductName())){
						validMessag+="|样品名称不能为空";
					}
					if(!sheetVO.getPass()&&productVO.getTestProperty().size()>0){
						for(TestProperty t:productVO.getTestProperty()){
							if(StringUtil.isBlank(t.getName())){
								validMessag+="|不合格项目不能为空";
								break;
							}
							if(StringUtil.isBlank(t.getResult())){
								validMessag+="|检验结果不能为空";
								break;
							}
						}
					}
					if("".equals(validMessag)){
						list.add(productVO);
					}else{
						productVO.setValidMessage(validMessag);
						errorList.add(productVO);
					}
				}
			}
			sheetVO.setList(list);
			if(errorList.size()>0){
				sheetVO.setErrorList(errorList);
				sheetVO.setMessage("工作表中有没能识别的行！");
				excelFileVO.setFlag(false);
				excelFileVO.setMessage("导入失败");
			}
			excelFileVO.getSheetList().add(sheetVO);
		}
	
		return excelFileVO;
	}
	
	public static boolean isValidTitle(XSSFSheet xssfSheet,Integer startNum,Integer columnCount){
		String[] titleArr = COLUMN_NAME;
		if(columnCount==12){
			titleArr = COLUMN_NAME_NO;
		}
		XSSFRow xssfRow = xssfSheet.getRow(startNum);
		for(int num=0;num<columnCount;num++){
			XSSFCell xssfCell = xssfRow.getCell(num);
			String value = getFormatValue(xssfCell);
			String columnName = titleArr[num];
			String[] arry = columnName.split("#");
			if(!Arrays.asList(arry).contains(value)){
				return false;
			}
		}
		return true;
	}
	
	/**
	  *  解析Microsoft Excel 2003文件(*.xls)工作簿
	  * xlsx2003Workbook 
	  * @param @param hssfWorkbook 工作簿
	  * @param @return
	  * @return Map<String,Object>    返回类型
	  * @throws
	 */
	
	public static ExcelFileVO analyticalWorkbook(HSSFWorkbook hssfWorkbook){
		ExcelFileVO excelFileVO = new ExcelFileVO();
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++){
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			//总行数
			int rowCount = hssfSheet.getLastRowNum();
			if(rowCount==0){
				continue;
			}
			SheetVO sheetVO = new SheetVO();
			sheetVO.setSheetName(hssfSheet.getSheetName());
			//取得读取数据起始行
			Map<String,Object> map = stratNum(hssfSheet);
			if((Boolean)map.get("flag")){
				sheetVO.setMessage("工作表格式错误，没有找到表头！");
				excelFileVO.setFlag(false);
				excelFileVO.getSheetList().add(sheetVO);
				excelFileVO.setMessage("导入失败");
				continue;
			}
			int startNum = (Integer)map.get("stratNum");
			int columnCount = (Integer)map.get("lastCallNum");
			if(!(columnCount==8||columnCount==12)){
				sheetVO.setMessage("工作表格式错误，表格列数不对应为12或8列！");
				excelFileVO.setFlag(false);
				excelFileVO.getSheetList().add(sheetVO);
				excelFileVO.setMessage("导入失败");
				continue;
			}
			
			boolean flag = isValidTitle(hssfSheet,startNum,columnCount);
			if(!flag){
				sheetVO.setMessage("工作表表头顺序不对");
				excelFileVO.setFlag(false);
				excelFileVO.getSheetList().add(sheetVO);
				excelFileVO.setMessage("导入失败");
				continue;
			}
		
			//列数为8则认为合格
			if(columnCount==8){
				sheetVO.setPass(Boolean.TRUE);
			}
			
			List<ProductVO> list = new ArrayList<ProductVO>();
			List<ProductVO> errorList = new ArrayList<ProductVO>();
			//从表头的下行遍历
			for(int rowNum = startNum+1;rowNum<=rowCount;rowNum++){
				Map<String,Object> mergedRegionMap = isMergedRegion(hssfSheet,rowNum);
				//如果为合并单元格
				if((Boolean) mergedRegionMap.get("isMergedRegion")){
					CellRangeAddress cellRangeAddress = (CellRangeAddress) mergedRegionMap.get("cellRangeAddress");
					//如果为合并多列单元格则跳过不解析
					if(cellRangeAddress.getLastColumn()-cellRangeAddress.getFirstColumn()>1){
						continue;
					}
					ProductVO productVO = getCallValue(hssfSheet,cellRangeAddress,columnCount);
					if(null!=productVO){
						String validMessag="";
						productVO = isValidDate(productVO);
					/*	if(isValidDate(productVO)==null){
							validMessag+="日期不能识别，如果包含批号请用#分割,日期在前,如：2015年1月5日#T20150105/2015年1月5日";
						}*/
						if(StringUtil.isBlank(productVO.getProductName())){
							validMessag+="|样品名称不能为空";
						}
						if(!sheetVO.getPass()&&productVO.getTestProperty().size()>0){
							for(TestProperty t:productVO.getTestProperty()){
								if(StringUtil.isBlank(t.getName())){
									validMessag+="|不合格项目不能为空";
									break;
								}
								if(StringUtil.isBlank(t.getResult())){
									validMessag+="|检验结果不能为空";
									break;
								}
							}
						}
						if("".equals(validMessag)){
							list.add(productVO);
						}else{
							productVO.setValidMessage(validMessag);
							errorList.add(productVO);
						}
					}
					rowNum = cellRangeAddress.getLastRow();
					continue;
				}
				
				CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum,rowNum,0, 0);
				ProductVO productVO = getCallValue(hssfSheet,cellRangeAddress,columnCount);
				if(null!=productVO){
					String validMessag="";
					productVO = isValidDate(productVO);
					/*if(isValidDate(productVO)==null){
						validMessag+="日期不能识别，如果包含批号请用#分割,日期在前,如：2015年1月5日#T20150105/2015年1月5日";
					}*/
					if(!StringUtil.isBlank(productVO.getProduction_Date())){
						  Calendar calendar = Calendar.getInstance();
						  calendar.setTime(productVO.getProduction_Date());
						  int year = calendar.get(Calendar.YEAR);//获取年份
						  if(year<1900||year>2099){
								validMessag+="日期年份必需在1900~2099之间";
								break;
						  }
					}
					if(StringUtil.isBlank(productVO.getProductName())){
						validMessag+="|样品名称不能为空";
					}
					if(!sheetVO.getPass()&&productVO.getTestProperty().size()>0){
						for(TestProperty t:productVO.getTestProperty()){
							if(StringUtil.isBlank(t.getName())){
								validMessag+="|不合格项目不能为空";
								break;
							}
							if(StringUtil.isBlank(t.getResult())){
								validMessag+="|检验结果不能为空";
								break;
							}
						}
					}
					if("".equals(validMessag)){
						list.add(productVO);
					}else{
						productVO.setValidMessage(validMessag);
						errorList.add(productVO);
					}
				}
			}
			sheetVO.setList(list);
			if(errorList.size()>0){
				sheetVO.setErrorList(errorList);
				sheetVO.setMessage("工作表中有没能识别的行！");
				excelFileVO.setFlag(false);
				excelFileVO.setMessage("导入失败");
			}
		
			excelFileVO.getSheetList().add(sheetVO);
		}
	
		return excelFileVO;
	}
	
	public static boolean isValidTitle(HSSFSheet hssfSheet,Integer startNum,Integer columnCount){
		String[] titleArr = COLUMN_NAME;
		if(columnCount==12){
			titleArr = COLUMN_NAME_NO;
		}
		HSSFRow hssfRow = hssfSheet.getRow(startNum);
		for(int num=0;num<columnCount;num++){
			HSSFCell hssfCell = hssfRow.getCell(num);
			String value = getFormatValue(hssfCell);
			value = value.replaceAll(" ", "");
			String columnName = titleArr[num];
			String[] arry = columnName.split("#");
			if(!Arrays.asList(arry).contains(value)){
				return false;
			}
		}
		return true;
	}
	
	 public static ProductVO isValidDate(ProductVO productVO){
		 String dateStr = productVO.getProductionDate();
		 SimpleDateFormat sdf = null;
		  if(null==dateStr){
			  return productVO;
		  }
		  if("".equals(dateStr)){
			  return productVO; 
		  }
		  if("/".equals(dateStr)){
			  return productVO; 
		  }
		  
		  String[] arry = dateStr.split("#");
		  String[] arry_1 = dateStr.split("\\.");
		  if(arry.length==2){
			  dateStr = arry[0];
			  productVO.setProductionDate(arry[1]);
		  }else if(arry_1.length==3&&dateStr.length()<=10){
			  dateStr = dateStr.replace(".","-");
		  }
	
		  for(String s:DATE_STR){
			  try {
				  sdf = new SimpleDateFormat(s);
				  sdf.setLenient(false);
				  Date date = sdf.parse(dateStr);
				  productVO.setProduction_Date(date);
				  return productVO;
			  } catch (ParseException e) {
			  }
		  }
		  return productVO;
	 }
	
	
	/**
	  * 取单元格内容 xlsx
	  * getMergedRegionCallValue
	  * @param @param xssfSheet
	  * @param @param cellRangeAddress
	  * @param @return
	  * @return ProductVO    返回类型
	  * @throws
	 */
	public static ProductVO getCallValue(XSSFSheet xssfSheet,CellRangeAddress cellRangeAddress,int columnCount){
		XSSFRow xssfRow = xssfSheet.getRow(cellRangeAddress.getFirstRow());
		if(xssfRow==null){
			return null;
		}
		String[] propertyArray = OK_PROPERTY_ARRAY;  //合格对象顺序属性
		int endColumnCount = columnCount;
		//不合格单元格
		if(columnCount==ON_COLUMNNUM){
			endColumnCount = ON_COLUMNNUM-3;
			propertyArray = PROPERTY_ARRAY;  //不合格对象顺序属性
		}
		int columnNum =0;
		JsonObject jsonObject = new JsonObject();
		while (columnNum < endColumnCount){
			XSSFCell xssfCell = xssfRow.getCell(columnNum);
			String value = getFormatValue(xssfCell);
			if(!"".equals(value)){
				jsonObject.addProperty(propertyArray[columnNum], value);
			}
			columnNum++;
	    }
		ProductVO productVO = null;
		//空行不解析
		if(!"{}".equals(jsonObject.toString())){
			productVO = getGson().fromJson(jsonObject.toString(), ProductVO.class);
			//不合格单元格
			if(columnCount==ON_COLUMNNUM){
				List<TestProperty> list = getTestProperty(xssfSheet,cellRangeAddress.getFirstRow() ,cellRangeAddress.getLastRow(),columnCount);
				productVO.setTestProperty(list);
			}
		}
	
		return productVO;
	}
	
	/**
	  * 取单元格内容 xls
	  * getCallValue
	  * @param @param hssfSheet
	  * @param @param cellRangeAddress
	  * @param @return
	  * @return ProductVO    返回类型
	  * @throws
	 */
	public static ProductVO getCallValue(HSSFSheet hssfSheet,CellRangeAddress cellRangeAddress,int columnCount){
		HSSFRow hssfRow = hssfSheet.getRow(cellRangeAddress.getFirstRow());
		if(hssfRow==null){
			return null;
		}
		String[] propertyArray = OK_PROPERTY_ARRAY;  //合格对象顺序属性
		int endColumnCount = columnCount;
		//不合格单元格
		if(columnCount==ON_COLUMNNUM){
			endColumnCount = ON_COLUMNNUM-3;
			propertyArray = PROPERTY_ARRAY;  //不合格对象顺序属性
		}
		int columnNum =0;
		JsonObject jsonObject = new JsonObject();
		while (columnNum < endColumnCount){
			HSSFCell hssfCell = hssfRow.getCell(columnNum);
			String value = getFormatValue(hssfCell);
			if(!"".equals(value)){
				jsonObject.addProperty(propertyArray[columnNum], value);
			}
			columnNum++;
	    }
		ProductVO productVO = null;
		//空行不解析
		if(!"{}".equals(jsonObject.toString())){
			productVO = getGson().fromJson(jsonObject.toString(), ProductVO.class);
			
			//不合格单元格
			if(columnCount==ON_COLUMNNUM){
				List<TestProperty> list = getTestProperty(hssfSheet,cellRangeAddress.getFirstRow() ,cellRangeAddress.getLastRow(),columnCount);
				productVO.setTestProperty(list);
			}
		}
		return productVO;
	}
	
	//取检测项目 xlsx
	public static List<TestProperty> getTestProperty(XSSFSheet xssfSheet,int firstRow ,int lastRow,int columnCount){
		List<TestProperty> list = new ArrayList<TestProperty>();
		JsonArray jsonArray = new JsonArray();
		for(int rowNum =firstRow;rowNum <=lastRow;rowNum++){
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			//int columnCount = xssfRow.getPhysicalNumberOfCells();
			int columnNum = START_TEST_COLUMNNUM;
			int test_columnnum  = 0;
			JsonObject jsonObject = new JsonObject();
			while (columnNum < columnCount){
				XSSFCell xssfCell = xssfRow.getCell(columnNum);
				String value = getFormatValue(xssfCell);
				jsonObject.addProperty(TEST_PROPERTY_ARRAY[test_columnnum], value);
				columnNum++;
				test_columnnum++;
		    }
			jsonArray.add(jsonObject);
		}
		list = getGson().fromJson(jsonArray.toString(), new TypeToken<List<TestProperty>>(){}.getType());
		return list;
	}
	
	//取检测项目 xls
	public static List<TestProperty> getTestProperty(HSSFSheet hssfSheet,int firstRow ,int lastRow,int columnCount){
		List<TestProperty> list = new ArrayList<TestProperty>();
		JsonArray jsonArray = new JsonArray();
		for(int rowNum =firstRow;rowNum <=lastRow;rowNum++){
			HSSFRow hssfRow = hssfSheet.getRow(rowNum);
			//int columnCount = hssfRow.getPhysicalNumberOfCells();
			int columnNum = START_TEST_COLUMNNUM;
			int test_columnnum  = 0;
			JsonObject jsonObject = new JsonObject();
			while (columnNum < columnCount){
				HSSFCell hssfCell = hssfRow.getCell(columnNum);
				String value = getFormatValue(hssfCell);
				jsonObject.addProperty(TEST_PROPERTY_ARRAY[test_columnnum], value);
				columnNum++;
				test_columnnum++;
		    }
			jsonArray.add(jsonObject);
		}
		list = getGson().fromJson(jsonArray.toString(), new TypeToken<List<TestProperty>>(){}.getType());
		return list;
	}
	
	/**
	 * 获取Xlsx读取数据起始行号
	  * stratNum
	  * @param @param xssfSheet
	  * @param @return
	  * @return Integer    返回类型
	  * @throws
	 */
	public static Map<String,Object> stratNum(XSSFSheet xssfSheet){
		 Map<String,Object> map = new HashMap<String,Object>();
		 map.put("flag", false);
		//总行数
		int rowCount = xssfSheet.getLastRowNum();
/*		if(rowCount<1){
			map.put("flag", true);
			return map;
		}*/
		int stratNum =-1;
		//得到读数据起始行
		for(int rowNum = 0;rowNum<=rowCount;rowNum++){
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			if(xssfRow==null){
				continue;
			}
			XSSFCell xssfCell = xssfRow.getCell(0);
			String value = getFormatValue(xssfCell);
			if(START_TITLE.equals(value)){
				stratNum = rowNum; 
				break;
			}
		}
		if(stratNum==-1){
			map.put("flag", true);
			return map;
		}
		map.put("stratNum", stratNum);
		
		XSSFRow xssfRow = xssfSheet.getRow((Integer)map.get("stratNum"));
		int columnCount = xssfRow.getPhysicalNumberOfCells();
		int columnNum =0;
		while (columnNum < columnCount){
			XSSFCell xssfCell = xssfRow.getCell(columnNum);
			String value = getFormatValue(xssfCell);
			if("".equals(value)){
				columnCount = columnNum;
				break;
			}
			columnNum++;
	    }
		map.put("lastCallNum", columnCount);
		return map;
	}
	
	/**
	 * 获取Xls读取数据起始行号
	  * stratNum
	  * @param @param hssfSheet
	  * @param @return
	  * @return Integer    返回类型
	  * @throws
	 */
	public static Map<String,Object> stratNum(HSSFSheet hssfSheet){
		 Map<String,Object> map = new HashMap<String,Object>();
		 map.put("flag", false);
		//总行数
		int rowCount = hssfSheet.getLastRowNum();
	/*	if(rowCount<3){
			map.put("flag", true);
			return map;
		}*/
		int stratNum =-1;
		for(int rowNum = 0;rowNum<=rowCount;rowNum++){
			HSSFRow hssfRow = hssfSheet.getRow(rowNum);
			if(hssfRow==null){
				continue;
			}
			HSSFCell hssfCell = hssfRow.getCell(0);
			String value = getFormatValue(hssfCell);
			if(START_TITLE.equals(value)){
				stratNum = rowNum; 
				break;
			}
		}
		if(stratNum==-1){
			map.put("flag", true);
			return map;
		}
		map.put("stratNum", stratNum);
		
		HSSFRow hssfRow = hssfSheet.getRow((Integer)map.get("stratNum"));
		int columnCount =hssfRow.getPhysicalNumberOfCells();
		int columnNum =0;
		while (columnNum < columnCount){
			HSSFCell hssfCell = hssfRow.getCell(columnNum);
			String value = getFormatValue(hssfCell);
			if("".equals(value)){
				columnCount = columnNum;
				break;
			}
			columnNum++;
	    }
		map.put("lastCallNum", columnNum);
		return map;
	}
	
	/**
	 * 判断是否为合并单元格
	  * isMergedRegion
	  * @param @param xssfSheet
	  * @param @param row  当前行
	  * @param @return
	  * @return Map<String,Object>    返回类型
	  * @throws
	 */
	 public static Map<String,Object> isMergedRegion(XSSFSheet xssfSheet,int row){
		 Map<String,Object> map = new HashMap<String, Object>();
		 map.put("isMergedRegion", false);
		 int sheetMergeCount = xssfSheet.getNumMergedRegions(); 
		 for (int i = 0; i < sheetMergeCount; i++){
			 CellRangeAddress cellRangeAddress = xssfSheet.getMergedRegion(i);
		      int firstRow = cellRangeAddress.getFirstRow();
		      int lastRow = cellRangeAddress.getLastRow();
		      if(row >= firstRow && row <= lastRow){
		    	  map.put("cellRangeAddress", cellRangeAddress);
		    	  map.put("isMergedRegion", true);
		    	  return map;
		      }
		 }
		 return map;
	 }
	 
	 public static Map<String,Object> isMergedRegion(HSSFSheet hssfSheet,int row){
		 Map<String,Object> map = new HashMap<String, Object>();
		 map.put("isMergedRegion", false);
		 int sheetMergeCount = hssfSheet.getNumMergedRegions(); 
		 for (int i = 0; i < sheetMergeCount; i++){
			  CellRangeAddress cellRangeAddress = hssfSheet.getMergedRegion(i);
		      int firstRow = cellRangeAddress.getFirstRow();
		      int lastRow = cellRangeAddress.getLastRow();
		      if(row >= firstRow && row <= lastRow){
		    	  map.put("cellRangeAddress", cellRangeAddress);
		    	  map.put("isMergedRegion", true);
		    	  return map;
		      }
		 }
		 return map;
	 }
	
	 /**
	  * word2007/2010 excel取值
	  * getXlsxFormatValue
	  * @param @param xssfCell
	  * @param @return
	  * @return String    返回类型
	  * @throws
	 */
	public static String getFormatValue(XSSFCell xssfCell){
		String cellvalue = "";
		if (xssfCell != null) {
			 switch (xssfCell.getCellType()) {
				 case Cell.CELL_TYPE_BOOLEAN:
					 cellvalue = String.valueOf( xssfCell.getBooleanCellValue());
					  break;
				 case Cell.CELL_TYPE_NUMERIC:
					  if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(xssfCell)) {
						  Date date = xssfCell.getDateCellValue();
						  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		                  cellvalue = sdf.format(date);
					  }else {
						    xssfCell.setCellType (XSSFCell.CELL_TYPE_STRING);
		                    // 取得当前Cell的数值
		                    cellvalue = String.valueOf(xssfCell.getStringCellValue());
		              }
					 break;
				 case Cell.CELL_TYPE_STRING:
					 cellvalue = String.valueOf( xssfCell.getStringCellValue());
					 break;
				 case Cell.CELL_TYPE_FORMULA:
					/*  // 判断当前的cell是否为Date
					  if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(xssfCell)) {
						  Date date = xssfCell.getDateCellValue();
						  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		                  cellvalue = sdf.format(date);
					  }else {
						    xssfCell.setCellType (XSSFCell.CELL_TYPE_STRING);
		                    // 取得当前Cell的数值
		                    cellvalue = String.valueOf(xssfCell.getStringCellValue());
		              }*/
					   xssfCell.setCellType (XSSFCell.CELL_TYPE_STRING);
	                    // 取得当前Cell的数值
	                    cellvalue = String.valueOf(xssfCell.getStringCellValue());
		              break;
				 default:
					 xssfCell.setCellType (XSSFCell.CELL_TYPE_STRING);
	                 cellvalue = String.valueOf(xssfCell.getStringCellValue());
			 }
		}
		return cellvalue.trim();
	}
	
	//xls取值
	public static String getFormatValue(HSSFCell cell){
		String cellvalue = "";
		if (cell != null) {
			 switch (cell.getCellType()) {
			    case Cell.CELL_TYPE_NUMERIC:
			    	  if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
						  Date date = cell.getDateCellValue();
						  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		                  cellvalue = sdf.format(date);
					  }else {
		                    // 取得当前Cell的数值
						  	cell.setCellType (HSSFCell.CELL_TYPE_STRING);
		                    cellvalue = String.valueOf(cell.getStringCellValue());
		              }
			    	  break;
	            case HSSFCell.CELL_TYPE_FORMULA: {
	             /*   // 判断当前的cell是否为Date
	                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
	                    // 如果是Date类型则，转化为Data格式
	                    Date date = cell.getDateCellValue();
	                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                    cellvalue = sdf.format(date);
	                }
	                // 如果是纯数字
	                else {
	                    // 取得当前Cell的数值
	             		cell.setCellType (HSSFCell.CELL_TYPE_STRING);
	                    cellvalue = String.valueOf(cell.getStringCellValue());
	                }*/
	            	   // 取得当前Cell的数值
             		cell.setCellType (HSSFCell.CELL_TYPE_STRING);
                    cellvalue = String.valueOf(cell.getStringCellValue());
	                break;
	            }
	           case HSSFCell.CELL_TYPE_STRING:
	                // 取得当前的Cell字符串
	                cellvalue = cell.getRichStringCellValue().getString();
	                break;
	           default:
	         		cell.setCellType (HSSFCell.CELL_TYPE_STRING);
                    cellvalue = String.valueOf(cell.getStringCellValue());
			 }
		}
		return cellvalue.trim();
	}
	
	
	public static Gson getGson() {
		return gson==null?new Gson():gson;
	}
}
