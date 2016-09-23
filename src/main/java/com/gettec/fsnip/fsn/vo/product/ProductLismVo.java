package com.gettec.fsnip.fsn.vo.product;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.vo.business.BusinessLicenseLismVo;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;

@SuppressWarnings("unused")
public class ProductLismVo {
	/**
	 * id
	 */
	private Long id;
	
	/**
	 * 产品名称
	 */
	private String proName;
	
	/**
	 * 条形码
	 */
	private String barcode;
	
	/**
	 * 商品状态
	 */
	private String status;
	
	/**
	 * 规格
	 */
	private String format;
	
    List<BusinessLicenseLismVo> bussPro;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public List<BusinessLicenseLismVo> getBussPro() {
		return bussPro;
	}

	public void setBussPro(List<BusinessLicenseLismVo> bussPro) {
		this.bussPro = bussPro;
	}
	
}
