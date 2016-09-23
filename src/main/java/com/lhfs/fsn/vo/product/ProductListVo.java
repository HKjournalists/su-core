package com.lhfs.fsn.vo.product;


/**
 * 大众门户VO
 * @author ZhaWanNeng
 */
public class ProductListVo {
	private Long id;
	private String name;
	private String imgUrl;
	private ProductNutritionVO report = new ProductNutritionVO();
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
	public ProductNutritionVO getReport() {
		return report;
	}
	public void setReport(ProductNutritionVO report) {
		this.report = report;
	}
	public ProductListVo(Long id, String name, String imgUrl,
			ProductNutritionVO report) {
		super();
		this.id = id;
		this.name = name;
		this.imgUrl = imgUrl;
		this.report = report;
	}
	
}
