package com.gettec.fsnip.fsn.vo.product;

/**
 * 封装用于生产二维码产品信息的VO
 * @author TangXin
 */
public class QRCodeVO {

	private Long productId; //产品ID
	private String barcode; //条形码
	private String productName;  //产品名称
	private String productAddress;	//产地
	private String supplier;  //供应商
	private String productArea; //产品所属区域
	private String message;  //信息
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductAddress() {
		return productAddress;
	}
	public void setProductAddress(String productAddress) {
		this.productAddress = productAddress;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getProductArea() {
		return productArea;
	}
	public void setProductArea(String productArea) {
		this.productArea = productArea;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
