package com.gettec.fsnip.fsn.service.statistics.impl;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.ProductDAO;
import com.gettec.fsnip.fsn.dao.statistics.ProductVisitStatisticsDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.statistics.ProductVisitStatistics;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.statistics.ProductVisitStatisticsService;
import com.gettec.fsnip.fsn.util.ExportExcel;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.web.IWebUtils;




/**
 * ProductPoll service implementation
 * 
 * @author 
 */
@Service(value="productVisitStatisticsService")
public class ProductVisitStatisticsServiceImpl implements ProductVisitStatisticsService{

	@SuppressWarnings("unused")
	private final static Logger logger = Logger.getLogger(ProductVisitStatisticsServiceImpl.class);
	
	@Autowired
	protected ProductVisitStatisticsDAO productVisitStatisticsDAO;
	@Autowired
	protected ProductDAO productDAO;
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void statisticalVsits(String data) {
		String[] d=data.split("_");
		Long productId=Long.parseLong(d[0]);
		String type=d[1];
		try {
			Product pro=productDAO.findById(productId);
			if(pro!=null){
				ProductVisitStatistics oProVi=productVisitStatisticsDAO.findByProductId(productId);
				if(oProVi==null){
					ProductVisitStatistics proVi=new ProductVisitStatistics();
					if(type.equals(IWebUtils.PORTAL_FLAG)){
						proVi.setPortalStatistics(1L);
						proVi.setAppStatistics(0L);
					}else{
						proVi.setAppStatistics(1L);	
						proVi.setPortalStatistics(0L);
					}
					proVi.setProduct(pro);
					productVisitStatisticsDAO.persistent(proVi);
				}else{
					if(type.equals(IWebUtils.PORTAL_FLAG)){
						oProVi.setPortalStatistics(oProVi.getPortalStatistics()+1);					
					}else{
						oProVi.setAppStatistics(oProVi.getAppStatistics()+1);									
					}
					productVisitStatisticsDAO.merge(oProVi);
				}
			}
		} catch (JPAException e) {
			e.printStackTrace();
		} catch (DaoException e) {
			((Throwable) e.getException()).printStackTrace();
		}
		
	}


	@Override
	public List<ProductVisitStatistics> getAllProViStaListByPage(int page,
			int pageSize, String configure) throws ServiceException {
		List<ProductVisitStatistics> productSales=null;
		try {
			productSales = productVisitStatisticsDAO.getAllProViStaListByPage(page,pageSize,getConfigure(configure));			
		} catch (DaoException dex) {
             throw new ServiceException(dex.getMessage(),dex.getException());
		}
		return productSales;
	}


	@Override
	public Long getCount(String configure) throws ServiceException {
		Long count=0L;
		try {
			count=productVisitStatisticsDAO.getCountByproId(getConfigure(configure));
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
		return count;
	}

	  /**
     * 根据kendoGrid中数据筛选条件拼接查询的sql语句
     * @param organizationId 
     * @param configure
     * @return
     * @throws ServiceException 
     */
    private Map<String, Object> getConfigure( String configure) throws ServiceException{
        Object[] params = null;
        String new_configure = "";
        String filter[] = configure.split("@@");
        for(int i=0;i<filter.length;i++){
            String filters[] = filter[i].split("@");
            if(filters.length > 3){
                try {
                    String config = splitJointConfigure(filters[0],filters[1],filters[2]);
                    if(config==null){
                        continue;
                    }
                    if(i==0){
                        new_configure = new_configure + " WHERE " + config;
                    }else{
                        new_configure = new_configure +" AND " + config;
                    }
                } catch (Exception e) {
                e.printStackTrace();
                }
            }
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("condition", new_configure);
        map.put("params", params);
        return map;
    }
    
    /**
     * 分割页面的过滤信息
     * @param field
     * @param mark
     * @param value
     * @param isSon 
     * @return
     * @throws ServiceException
     */
    private String splitJointConfigure(String field, String mark, String value) throws ServiceException{
        try {
            value = URLDecoder.decode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("【Service-Error】数据筛选时，利用utf-8解码筛选条件时出现异常！",e);
        }
        if(field.equals("id")){
            return FilterUtils.getConditionStr("id",mark,value);
        }
        if(field.equals("product_name")){
            return FilterUtils.getConditionStr("product.name",mark,value);
        }
        if(field.equals("product_barcode")){
            return FilterUtils.getConditionStr("product.barcode",mark,value);
        }
        if(field.equals("appStatistics")){
            return FilterUtils.getConditionStr("appStatistics",mark,value);
        }
        if(field.equals("portalStatistics")){
            return FilterUtils.getConditionStr("portalStatistics",mark,value);
        }
        return null;
    }
    

    /**
     * 生成excel的HSSFWorkbook
     */
	@Override
	public HSSFWorkbook downExcel(List<TestResult> tes, String configure) throws ServiceException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		// 定义单元格报头				
			String worksheetTitle = "产品报告查询";
			
			HSSFWorkbook wb = new HSSFWorkbook();
			// 创建单元格样式
			HSSFCellStyle cellStyleTitle = wb.createCellStyle();
			// 指定单元格居中对齐
			cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 指定单元格垂直居中对齐
			cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 指定当单元格内容显示不下时自动换行
			cellStyleTitle.setWrapText(true);
			// ------------------------------------------------------------------
			HSSFCellStyle cellStyle = wb.createCellStyle();
			// 指定单元格居中对齐
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 指定单元格垂直居中对齐
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 指定当单元格内容显示不下时自动换行
			cellStyle.setWrapText(true);
			// ------------------------------------------------------------------
			// 设置单元格字体
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontName("宋体");
			font.setFontHeight((short) 200);
			cellStyleTitle.setFont(font);

			// 工作表名
			String reportNO = "报告编号";
			String proName="产品名称";
			String barcode = "条形码";
			String testType = "检测类型";
			String format = "规格";
			String producerName = "生产企业";
			String businessBrand = "商标";
			String batchSerialNo = "批次号";
			String productionDate = "生产日期";
			String testDate = "检测日期";
			String publishFlag = "发布状态";

			HSSFSheet sheet = wb.createSheet();
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 7000);
			sheet.setColumnWidth(3, 3000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 7000);
			sheet.setColumnWidth(6, 5000);
			sheet.setColumnWidth(7, 5000);
			sheet.setColumnWidth(8, 5000);
			sheet.setColumnWidth(9, 5000);
			sheet.setColumnWidth(10, 3000);
			ExportExcel exportExcel = new ExportExcel(wb, sheet);
			// 创建报表头部
			exportExcel.createNormalHead(worksheetTitle, 10);
			// 定义第一行
			/*HSSFRow row1 = sheet.createRow(1);
			HSSFCell cell1 = row1.createCell(0);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString("查询条件"));
			cell1 = row1.createCell(1);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString("产品名称"));
			cell1 = row1.createCell(2);
			cell1.setCellStyle(cellStyle);
			cell1.setCellValue(new HSSFRichTextString(productName));
		
			HSSFRow row2 = sheet.createRow(2);
			cell1 = row2.createCell(1);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString("条形码"));
			cell1 = row2.createCell(2);
			cell1.setCellStyle(cellStyle);
			cell1.setCellValue(new HSSFRichTextString(barcode));
		
			HSSFRow row3 = sheet.createRow(3);
			cell1 = row3.createCell(1);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString("报告发布时间范围"));
			cell1 = row3.createCell(2);
			cell1.setCellStyle(cellStyle);
			cell1.setCellValue(new HSSFRichTextString((startDate.equals("")||endDate.equals(""))?"":startDate+"到"+endDate));							
		*/
			HSSFRow row1 = sheet.createRow(1);


			//第一行第一列
			HSSFCell cell1 = row1.createCell(0);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(reportNO));
			//第一行第er列
			cell1 = row1.createCell(1);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(barcode));

			//第一行第si列
			cell1 = row1.createCell(2);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(proName));

			//第一行第wu列
			cell1 = row1.createCell(3);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(testType));
			
			cell1 = row1.createCell(4);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(format));
			
			cell1 = row1.createCell(5);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(producerName));
			
			cell1 = row1.createCell(6);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(businessBrand));
			
			cell1 = row1.createCell(7);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(batchSerialNo));
			
			cell1 = row1.createCell(8);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(productionDate));
			
			cell1 = row1.createCell(9);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(testDate));
			
			cell1 = row1.createCell(10);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(publishFlag));
		
			//定义第二行
			HSSFRow row = sheet.createRow(2);
			HSSFCell cell = row.createCell(1);
			for (int i = 0; i < tes.size(); i++) {
				TestResult te = tes.get(i);
				row = sheet.createRow(i + 2);

				cell = row.createCell(0);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString((te.getServiceOrder()==null||te.getServiceOrder().equals(""))?"":te.getServiceOrder() + ""));
			
				cell = row.createCell(1);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString((te.getSample().getProduct().getBarcode()==null||te.getSample().getProduct().getBarcode().equals(""))?"":te.getSample().getProduct().getBarcode()+""));
			
				cell = row.createCell(2);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString((te.getSample().getProduct().getName()==null||te.getSample().getProduct().getName().equals(""))?"":te.getSample().getProduct().getName()+""));
			
				cell = row.createCell(3);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString((te.getTestType()==null||te.getTestType().equals(""))?"":te.getTestType()+""));
				
				cell = row.createCell(4);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString((te.getSample().getProduct().getFormat()==null||te.getSample().getProduct().getFormat().equals(""))?"":te.getSample().getProduct().getFormat()+""));
				
				cell = row.createCell(5);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString((te.getSample().getProducer().getName()==null||te.getSample().getProducer().getName().equals(""))?"":te.getSample().getProducer().getName()+""));
				
				cell = row.createCell(6);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString((te.getSample().getProduct().getBusinessBrand().getName()==null||te.getSample().getProduct().getBusinessBrand().getName().equals(""))?"":te.getSample().getProduct().getBusinessBrand().getName()+""));
				
				cell = row.createCell(7);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString((te.getSample().getBatchSerialNo()==null||te.getSample().getBatchSerialNo().equals(""))?"":te.getSample().getBatchSerialNo()+""));
				
				cell = row.createCell(8);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString((te.getSample().getProductionDate()==null||te.getSample().getProductionDate().equals(""))?"":sdf.format(te.getSample().getProductionDate())+""));
				
				cell = row.createCell(9);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString((te.getTestDate()==null||te.getTestDate().equals(""))?"":sdf.format(te.getTestDate())+""));
				
				cell = row.createCell(10);
				cell.setCellStyle(cellStyle);
				String publish=null;
				if(te.getPublishFlag()=='0'){
					publish="未发布";
				}else if(te.getPublishFlag()=='1'){
					publish="已发布";
				}
				cell.setCellValue(new HSSFRichTextString(publish));
			}
			return wb;	
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("系统异常",e);
		}
	}
}