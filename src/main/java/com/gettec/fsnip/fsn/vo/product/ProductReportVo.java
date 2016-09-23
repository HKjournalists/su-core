package com.gettec.fsnip.fsn.vo.product;


/**
 * 大众门户VO企业专栏接口
 * @author ZhaWanNeng
 */
public class ProductReportVo {
	private Long id;
	private String name;//产品名称
	private String imgUrl;//产品图片，多张用“|”分隔
	private int countProReport;//产品报告数量
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getCountProReport() {
		return countProReport;
	}
	public void setCountProReport(int countProReport) {
		this.countProReport = countProReport;
	}
	
	public ProductReportVo() {
		super();
	}
	public ProductReportVo(Long id, String name, String imgUrl, int countProReport) {
		super();
		this.id = id;
		this.name = name;
		this.imgUrl = imgUrl;
		this.countProReport = countProReport;
	}
	
}
