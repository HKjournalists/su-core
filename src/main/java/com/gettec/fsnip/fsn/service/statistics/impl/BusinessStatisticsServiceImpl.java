package com.gettec.fsnip.fsn.service.statistics.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.gettec.fsnip.fsn.dao.business.BusinessUnitDAO;
import com.gettec.fsnip.fsn.dao.product.ProductDAO;
import com.gettec.fsnip.fsn.dao.test.TestResultDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.statistics.BusinessStatisticsService;
import com.gettec.fsnip.fsn.util.ExportExcel;
import com.gettec.fsnip.fsn.vo.BusinessStaVO;
import com.gettec.fsnip.fsn.vo.ProductStaVO;

/**
 * ProductPoll service implementation
 * 
 * @author 
 */
@Service(value="businessStatisticsService")
public class BusinessStatisticsServiceImpl implements BusinessStatisticsService{

	@SuppressWarnings("unused")
	private final static Logger logger = Logger.getLogger(BusinessStatisticsServiceImpl.class);
	
	@Autowired
	protected TestResultDAO testResultDAO;
	
	@Autowired
	protected ProductDAO productDAO;
	
	@Autowired
	protected BusinessUnitDAO businessUnitDAO;
	
	/**
	 * 根据条件（企业名称、企业类型、注册时间）分页查询企业集合
	 * @param page 第几页
	 * @param pageSize 每页显示条数
	 * @param businessName 企业名称
	 * @param businessType 企业类型
	 * @param startDate 企业注册起始时间 
	 * @param endDate 企业注册结束时间
	 * @return List<BusinessStaVO>
	 * @throws ServiceException
	 * @author LongXianZhen
	 */
	@Override
	public List<BusinessStaVO> getbusinessStaListByConfigure(int page, int pageSize,
			String businessName, String businessType, String startDate,
			String endDate) throws ServiceException {
		try {
			 boolean nemaFlag=!businessName.equals("")&&businessName!=null;
			 boolean typeFlag=!businessType.equals("")&&businessType!=null;
			 boolean dateFlag=!startDate.equals("")&&!endDate.equals("");
			 //判断是否填写查询条件，没有则不用查询直接返回
			 if(!(nemaFlag||typeFlag||dateFlag)){
				 return new ArrayList<BusinessStaVO>();	
			 }
			List<BusinessStaVO> bus=businessUnitDAO.findBusinessIdByNameType(page,pageSize,businessName,businessType,startDate,endDate);
			for(BusinessStaVO bu:bus){
				//查询已发布报告的产品数
				Long productQuantity=testResultDAO.getPublishProCountByOrganizationId(bu.getOrganization());
				//查询已发布的报告数
				Long reportQuantity=testResultDAO.getRepoCountByOrganizationId(bu.getOrganization());
				//没有发布报告的产品数=该企业所有产品数-已发布报告产品数
				Long notPublishProQuantity=productDAO.getAllProCountByOrganization(bu.getOrganization())-productQuantity;
				bu.setProductQuantity(productQuantity);
				bu.setNotPublishProQuantity(notPublishProQuantity);
				bu.setReportQuantity(reportQuantity);
			}
			return bus;
		} catch (DaoException e) {
			throw new ServiceException("BusinessStatisticsServiceImpl.getbusinessStaListByConfigure()-->"+e.getMessage(),e.getException());
		}
	}

	/**
	 * 根据条件查询某个企业下产品发布报告数的统计
	 * @param page 第几页
	 * @param pageSize 每页显示条数
	 * @param businessId 企业ID
	 * @param productName 产品名称
	 * @param barcode 条形码
	 * @param startDate 报告发布起始时间
	 * @param endDate 报告发布结束时间
	 * @return List<ProductStaVO>
	 * @throws ServiceException
	 * @author LongXianZhen
	 */
	@Override
	public List<ProductStaVO> getProductStaListByConfigure(int page,
			int pageSize, Long businessId, String productName, String barcode,
			String startDate, String endDate) throws ServiceException {
		try {
			List<ProductStaVO> proStas=new ArrayList<ProductStaVO>();
			BusinessUnit bu= businessUnitDAO.findById(businessId);
			List<Product> pros=productDAO.getProListByBusiness(bu,page,pageSize,productName,barcode);
			//封装ProductStaVO
			for(Product pro:pros){
				ProductStaVO proSta= new ProductStaVO();
				//查询该产品已发布报告数量
				Long publishReportQuantity=testResultDAO.getReportQuantityByProBu(bu.getOrganization(),pro.getId(),startDate,endDate,"1");
				//查询该产品未发布报告数量
				Long notPublishReportQuantity=testResultDAO.getReportQuantityByProBu(bu.getOrganization(),pro.getId(),startDate,endDate,"0");
				//查询该产品最后发布报告时间
				Date lastPubDate=testResultDAO.getLastPubDateByProIdBu(pro.getId(), bu.getOrganization(),startDate,endDate);
				proSta.setLastPubDate(lastPubDate);
				proSta.setBusinessName(bu.getName());
				proSta.setProductName(pro.getName());
				proSta.setBarcode(pro.getBarcode());
				proSta.setReportQuantity(publishReportQuantity);
				proSta.setNotPublishReportQuantity(notPublishReportQuantity);
				proStas.add(proSta);
			}
			return proStas;
		} catch (DaoException ex) {
			throw new ServiceException("BusinessStatisticsServiceImpl.getProductStaListByConfigure()-->"+ex.getMessage(),ex.getException());
		}catch (JPAException e) {
			throw new ServiceException("BusinessStatisticsServiceImpl.getProductStaListByConfigure()-->"+e.getMessage(),e.getException());
		}
	}
	
	/**
	 * 根据条件查询某个企业下产品总数
	 * @param businessId 企业ID
	 * @param productName 产品名称
	 * @param barcode 条形码
	 * @return Long
	 * @throws ServiceException
	 * @author LongXianZhen
	 */
	@Override
	public Long getProductStaCountByConfigure(Long businessId,
			String productName, String barcode)
			throws ServiceException {
		try {
			BusinessUnit bu= businessUnitDAO.findById(businessId);
			return productDAO.getProductStaCountByConfigure(bu.getOrganization(),productName,barcode);
		} catch (DaoException ex) {
			throw new ServiceException("BusinessStatisticsServiceImpl.getProductStaCountByConfigure()-->"+ex.getMessage(),ex.getException());
		}catch (JPAException e) {
			throw new ServiceException("BusinessStatisticsServiceImpl.getProductStaCountByConfigure()-->"+e.getMessage(),e.getException());
		}
	}

	 /**
     * 根据条件（企业名称、企业类型、注册时间）查询企业总数
     * @param businessName 企业名称
     * @param businessType 企业类型
     * @param startDate 企业注册起始时间
     * @param endDate 企业注册结束时间
     * @return Long
     * @throws ServiceException
     * @author LongXianZhen
     */
	@Override
	public Long getBusinessStaCountByConfigure(String businessName,
			String businessType, String startDate, String endDate)
			throws ServiceException {
		try {
			 boolean nemaFlag=!businessName.equals("")&&businessName!=null;
			 boolean typeFlag=!businessType.equals("")&&businessType!=null;
			 boolean dateFlag=!startDate.equals("")&&!endDate.equals("");
			 ////判断是否填写查询条件，没有则不用查询直接返回0
			 if(!(nemaFlag||typeFlag||dateFlag)){
				 return 0L;	
			 }
			return businessUnitDAO.getBusinessStaCountByConfigure(businessName,businessType,startDate,endDate);
		} catch (DaoException ex) {
			throw new ServiceException("BusinessStatisticsServiceImpl.getBusinessStaCountByConfigure()-->"+ex.getMessage(),ex.getException());
		}
	}

	@Override
	public HSSFWorkbook downBusinessExcel(List<BusinessStaVO> buSta,String businessName,
			String businessType, String startDate, String endDate) throws ServiceException {
		try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				// 定义单元格报头
				String worksheetTitle = "企业发布报告统计";

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
				String name = "企业名称";
				String type = "企业类型";
				String proQuantity = "已发布产品数";
				String notProQuantity = "未发布报告产品数";
				String repQuantity = "发布报告数";
				String enDate = "注册时间";

				HSSFSheet sheet = wb.createSheet();
				sheet.setColumnWidth(0, 5000);
				sheet.setColumnWidth(1, 5000);
				sheet.setColumnWidth(2, 5000);
				sheet.setColumnWidth(3, 3000);
				sheet.setColumnWidth(4, 3000);
				sheet.setColumnWidth(5, 3000);
				ExportExcel exportExcel = new ExportExcel(wb, sheet);
				// 创建报表头部
				exportExcel.createNormalHead(worksheetTitle, 4);
				// 定义第一行
				HSSFRow row1 = sheet.createRow(1);
				HSSFCell cell1 = row1.createCell(0);
				cell1.setCellStyle(cellStyleTitle);
				cell1.setCellValue(new HSSFRichTextString("查询条件"));
				cell1 = row1.createCell(1);
				cell1.setCellStyle(cellStyleTitle);
				cell1.setCellValue(new HSSFRichTextString("企业名称"));
				cell1 = row1.createCell(2);
				cell1.setCellStyle(cellStyle);
				cell1.setCellValue(new HSSFRichTextString(businessName));
				
				HSSFRow row2 = sheet.createRow(2);
				cell1 = row2.createCell(1);
				cell1.setCellStyle(cellStyleTitle);
				cell1.setCellValue(new HSSFRichTextString("企业类型"));
				cell1 = row2.createCell(2);
				cell1.setCellStyle(cellStyle);
				cell1.setCellValue(new HSSFRichTextString(businessType));
				
				HSSFRow row3 = sheet.createRow(3);
				cell1 = row3.createCell(1);
				cell1.setCellStyle(cellStyleTitle);
				cell1.setCellValue(new HSSFRichTextString("注册时间范围"));
				cell1 = row3.createCell(2);
				cell1.setCellStyle(cellStyle);
				cell1.setCellValue(new HSSFRichTextString((startDate.equals("")||endDate.equals(""))?"":startDate+"到"+endDate));							
				
				HSSFRow row4 = sheet.createRow(4);


				//第一行第一列
				cell1 = row4.createCell(0);
				cell1.setCellStyle(cellStyleTitle);
				cell1.setCellValue(new HSSFRichTextString(name));
				//第一行第er列
				cell1 = row4.createCell(1);
				cell1.setCellStyle(cellStyleTitle);
				cell1.setCellValue(new HSSFRichTextString(type));

				//第一行第san列
				cell1 = row4.createCell(2);
				cell1.setCellStyle(cellStyleTitle);
				cell1.setCellValue(new HSSFRichTextString(proQuantity));
				
				cell1 = row4.createCell(3);
				cell1.setCellStyle(cellStyleTitle);
				cell1.setCellValue(new HSSFRichTextString(notProQuantity));
				

				//第一行第si列
				cell1 = row4.createCell(4);
				cell1.setCellStyle(cellStyleTitle);
				cell1.setCellValue(new HSSFRichTextString(repQuantity));

				//第一行第wu列
				cell1 = row4.createCell(5);
				cell1.setCellStyle(cellStyleTitle);
				cell1.setCellValue(new HSSFRichTextString(enDate));
				
				//定义第二行
				HSSFRow row = sheet.createRow(5);
				HSSFCell cell = row.createCell(1);
				for (int i = 0; i < buSta.size(); i++) {
					BusinessStaVO bu = buSta.get(i);
					row = sheet.createRow(i + 5);

					cell = row.createCell(0);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString((bu.getBusinessName()==null||bu.getBusinessName().equals(""))?"":bu.getBusinessName() + ""));
					
					cell = row.createCell(1);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString((bu.getBusinessType()==null||bu.getBusinessType().equals(""))?"":bu.getBusinessType()+""));
					
					cell = row.createCell(2);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(bu.getProductQuantity() + ""));
					
					cell = row.createCell(3);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(bu.getNotPublishProQuantity() + ""));
					
					cell = row.createCell(4);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString(bu.getReportQuantity() + ""));
					
					cell = row.createCell(5);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new HSSFRichTextString((bu.getEnterpriteDate()==null||bu.getEnterpriteDate().equals(""))?"":sdf.format(bu.getEnterpriteDate())+""));
				}
				return wb;	
		} catch (Exception e) {
			throw new ServiceException("系统异常",e);
		}
	}

	@Override
	public HSSFWorkbook downProductExcel(List<ProductStaVO> productStas,
			String productName, String barcode, String startDate,
			String endDate, Long businessId) throws ServiceException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		// 定义单元格报头		
			BusinessUnit bu = businessUnitDAO.findById(businessId);
		
			String worksheetTitle = "企业发布报告详细统计（"+bu.getName()+"）";
			
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
			String name = "产品名称";
			String barcodeN = "条形码";
			String repQuantity = "发布报告数";
			String notRepQuantity = "未发布报告数";
			String lastDate = "最后发布报告时间";

			HSSFSheet sheet = wb.createSheet();
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 3000);
			sheet.setColumnWidth(4, 3000);
			ExportExcel exportExcel = new ExportExcel(wb, sheet);
			// 创建报表头部
			exportExcel.createNormalHead(worksheetTitle, 4);
			// 定义第一行
			HSSFRow row1 = sheet.createRow(1);
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
		
			HSSFRow row4 = sheet.createRow(4);


			//第一行第一列
			cell1 = row4.createCell(0);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(name));
			//第一行第er列
			cell1 = row4.createCell(1);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(barcodeN));

			//第一行第si列
			cell1 = row4.createCell(2);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(repQuantity));
			
			//第一行第si列
			cell1 = row4.createCell(3);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(notRepQuantity));

			//第一行第wu列
			cell1 = row4.createCell(4);
			cell1.setCellStyle(cellStyleTitle);
			cell1.setCellValue(new HSSFRichTextString(lastDate));
		
			//定义第二行
			HSSFRow row = sheet.createRow(5);
			HSSFCell cell = row.createCell(1);
			for (int i = 0; i < productStas.size(); i++) {
				ProductStaVO pro = productStas.get(i);
				row = sheet.createRow(i + 5);

				cell = row.createCell(0);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString((pro.getProductName()==null||pro.getProductName().equals(""))?"":pro.getProductName() + ""));
			
				cell = row.createCell(1);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString((pro.getBarcode()==null||pro.getBarcode().equals(""))?"":pro.getBarcode()+""));
			
				cell = row.createCell(2);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(pro.getReportQuantity() + ""));
			
				cell = row.createCell(3);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString(pro.getNotPublishReportQuantity() + ""));
				
				cell = row.createCell(4);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(new HSSFRichTextString((pro.getLastPubDate()==null||pro.getLastPubDate().equals(""))?"":sdf.format(pro.getLastPubDate())+""));
			}
			return wb;	
		} catch (JPAException je) {
			throw new ServiceException(je.getMessage(),je.getException());
		}catch (Exception e) {
			throw new ServiceException("系统异常",e);
		}
	}

	@Override
	public List<ProductStaVO> getProductStaListByConfigureData(Long businessId,
			String productName, String barcode, String startDate,
			String endDate, int page, int pageSize) {
		List<ProductStaVO> proStas= productDAO.getProductStaListByConfigureData( businessId,
				 productName,  barcode,  startDate,
				 endDate,  page,  pageSize);
		return proStas;
	}

	@Override
	public Long getProductStaCountByConfigureData(Long businessId,
			String productName, String barcode) {
		Long counts= productDAO.getProductStaCountByConfigureData( businessId,
				 productName,  barcode);
		return counts;
	}
	
	
	
}