package com.gettec.fsnip.fsn.vo.sales;

import java.util.ArrayList;
import java.util.List;

import com.gettec.fsnip.fsn.model.test.TestProperty;

/**
 * 手机app 报告信息VO
 * @author tangxin 2015-06-07
 */
public class ReportAppVO {
	
	private String reportNO; //报告编号
	private String pdfUrl; //pdf路径
	private long total; //报告总数
	private SalesProductVO product;//产品信息
	List<TestItemVO> testItems; //检测项目集合
	
	public String getReportNO() {
		return reportNO;
	}
	public void setReportNO(String reportNO) {
		this.reportNO = reportNO;
	}
	public String getPdfUrl() {
		return pdfUrl;
	}
	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<TestItemVO> getTestItems() {
		return testItems;
	}
	public void setTestItems(List<TestItemVO> testItems) {
		this.testItems = testItems;
	}
	public SalesProductVO getProduct() {
		return product;
	}
	public void setProduct(SalesProductVO product) {
		this.product = product;
	}
	
	public ReportAppVO create(ViewReportVO reportVO){
		if(reportVO == null) {
			return this;
		}
		this.reportNO = reportVO.getReportNo();
		this.pdfUrl = reportVO.getPdfPath();
		long selN = reportVO.getSelfNumber();
		long ceN = reportVO.getCensorNumber();
		long samN = reportVO.getSampleNumber();
		this.total = (selN + ceN + samN);
		//this.total = ceN;
		return this;
	}
	
	public ReportAppVO createTestItem(List<TestProperty> items){
		if(items == null){
			return this;
		}
		this.testItems = new ArrayList<TestItemVO>();
		for(TestProperty item : items){
			TestItemVO itm = new TestItemVO(item.getName(),item.getResult(),item.getStandard());
			this.testItems.add(itm);
		}
		return this;
	}
}
