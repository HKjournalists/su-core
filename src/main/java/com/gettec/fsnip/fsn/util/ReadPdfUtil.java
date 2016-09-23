package com.gettec.fsnip.fsn.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;
import com.gettec.fsnip.fsn.vo.report.ReportOfMarketVO;
import com.lhfs.fsn.service.testReport.TestReportService;

public class ReadPdfUtil {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	/**
	 * @param productService 
	 * @param testReportService 
	 * @param testReportService 
	 * @param 读取PDF文件
	 */
	/*public static void main(String[] args) {
		ReadPdfUtil pdf = new ReadPdfUtil();
		String pdfName = "D:\\myPDF.pdf";
		pdf.readFileOfPDF(pdfName);

	}*/

	/**
	 * 功能描述；读取指定的PDF文件的内
	 * @author ZhangHui 2015/6/18
	 */
	public static Map<String, Object> readFileOfPDF(byte[] bs, ProductService productService, TestReportService testReportService) {
		Map<String, Object> map = new HashMap<String, Object>();
		ByteArrayInputStream  in = new ByteArrayInputStream(bs);
		
		try {
			// 新建一个PDF解析器对象
			PDFParser parser = new PDFParser(in);
			// 对PDF文件进行解析
			parser.parse();
			// 获取解析后得到的PDF文档对象
			PDDocument pdfdocument = parser.getPDDocument();
			// 新建一个PDF文本剥离器
			PDFTextStripper stripper = new PDFTextStripper();
			// 从PDF文档对象中剥离文本
			String context = stripper.getText(pdfdocument);
			
			String[] datas = context.split("\r\n");
			if(datas.length<2){
				datas = context.split("\n");
			}
			
			// 验证pdf有效性
			if(datas.length<11 || !datas[4].equals("生产日期： 报告日期：") || !datas[3].equals("产品批次：") 
					|| !datas[1].equals("产品名称：") || !datas[0].equals("生产单位：")){
				map.put("report", null);
				map.put("message", "mismatching template");
				return map;
			}
			
			/**
			 * 1. 封装产品信息
			 */
			ProductOfMarketVO product_vo = new ProductOfMarketVO();
			
			Date productionDate = null;
			int i = 0;
			try {
				productionDate = sdf.parse(datas[8]);
				i = 8;
			} catch (ParseException e) {
				productionDate = sdf.parse(datas[7]);
				i = 7;
			}
			
			product_vo.setProductionDate(productionDate);// 产品生产日期   4:"生产日期： 报告日期："
			product_vo.setBatchSerialNo(datas[i+1]);     // 产品批次  3:"产品批次："
			
			String format = datas[2].substring(6).replace(" 产地：", "");
			product_vo.setFormat(format); // 产品规格
			
			product_vo.setName(datas[6]); // 产品名称  1:"产品名称："
			
			/**
			 * 2. 封装生产企业信息
			 */
			BusinessUnitOfReportVO bus_vo = new BusinessUnitOfReportVO();
			bus_vo.setName(datas[5].replace("事业部", "")); // 生产企业名称  0:"生产单位："
			
			/**
			 * 3. 封装报告信息
			 */
			ReportOfMarketVO report_vo = new ReportOfMarketVO();
			
			Date testDate = sdf.parse(datas[i+2]); 
			report_vo.setTestDate(testDate);         // 报告检测日期
			
			String serviceOrder = datas[i+3].substring(5);
			report_vo.setServiceOrder(serviceOrder); // 报告编号
			
			/**
			 * 匹配产品：按产品别名和规格匹配产品
			 */
			List<Product> products = productService.getListByOtherNameAndPDFformat(
					product_vo.getName(), product_vo.getFormat(), false);
			if(products.size() < 1){
				products = productService.getListByOtherNameAndPDFformat(
						product_vo.getName() + "/", product_vo.getFormat(), true);
				if(products.size() < 1){
					products = productService.getListByOtherNameAndPDFformat(
							"/" + product_vo.getName(), product_vo.getFormat(),true);
				}
			}
			
			if(products.size() == 0){
				map.put("report", null);
				map.put("message", "mismatching product");
				return map;
			}
			
			/**
			 * 匹配报告：按报告编号、产品条形码、批次查找报告
			 */
			List<Boolean> list = new ArrayList<Boolean>();
			for(Product pro : products){
				boolean isExist = testReportService.checkUniquenessOfReport(null, report_vo.getServiceOrder(),
						pro.getBarcode(), product_vo.getBatchSerialNo());
				list.add(isExist);
			}
			
			for(i=0; i<list.size(); i++){
				if(!list.get(i)){
					break;
				}
			}
			
			if(i == list.size()){
				map.put("report", null);
				map.put("message", "has exist");
				return map;
			}
			
			/**
			 * 4. 封装检测项目
			 */
			int index;
			for(index = 5; index<datas.length; index++){
				if(datas[index].equals("检验项目 判断标准 检验结果 结果判定")){
					break;
				}
			}
			
			List<TestProperty> testProperties = new ArrayList<TestProperty>();
			for(int j=index+1; j<datas.length; j++){    // 15:"检验项目 判断标准 检验结果 结果判定"
				String[] items = datas[j].split(" ");
				TestProperty item = new TestProperty();
				item.setName(items[0]);           // 检测名称
				item.setTechIndicator(items[1]);  // 检测依据
				item.setResult(items[2]);         // 检测结果
				item.setAssessment(items[3]);     // 单项评价
				testProperties.add(item);
			}
			report_vo.setTestProperties(testProperties);
			
			report_vo.setProduct_vo(product_vo);
			report_vo.setBus_vo(bus_vo);
			
			// 返回
			map.put("message", "can save");
			map.put("report", report_vo);
			return map;
		} catch (Exception e) {
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
	}
}
