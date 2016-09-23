package com.lhfs.fsn.vo.product;


/**
 * 产品条形码与二维码对应vo
 * @author liuyuanjing 2015-12-30
 *
 */
public class ProductBarcodeToQRcodeVO {

	private Long id;
	private String barcode;
	private String product_id;
	private int QRstart_num;
	private int QRend_num;
	
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	
	public int getQRstart_num() {
		return QRstart_num;
	}
	public void setQRstart_num(int qRstart_num) {
		QRstart_num = qRstart_num;
	}
	public int getQRend_num() {
		return QRend_num;
	}
	public void setQRend_num(int qRend_num) {
		QRend_num = qRend_num;
	}
	public ProductBarcodeToQRcodeVO(Long id, String barcode,
			String product_id, int QRstart_num, int QRend_num) {
		this.id=id;
		this.barcode=barcode;
		this.product_id=product_id;
		this.QRstart_num=QRstart_num;
		this.QRend_num=QRend_num;
		
		
	}
	
	
	
}
