package com.lhfs.fsn.vo.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.product.BusinessCertification;

/**
 * 食安监产品VO
 * @author YongHuang
 */
public class ProductInfosVO {
	private String proId; //产品id
	private String product_name;//产品名称
	private String product_barcode;	//产品条码
	private int annual_yield;	//预计年产量
	private String min_pack_unit;	//最小包装单位
	private int self_check_num;	//自检报告份数
	private int sampling_num;	//抽检报告份数
	private int inspection_num;	//送检报告份数
	private int third_party_num;	//第三方检测报告份数
	private String status;	//第三方检测报告份数
	private String format;	//第三方检测报告份数
	
	
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
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
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_barcode() {
		return product_barcode;
	}
	public void setProduct_barcode(String product_barcode) {
		this.product_barcode = product_barcode;
	}
	public int getAnnual_yield() {
		return annual_yield;
	}
	public void setAnnual_yield(int annual_yield) {
		this.annual_yield = annual_yield;
	}
	public String getMin_pack_unit() {
		return min_pack_unit;
	}
	public void setMin_pack_unit(String min_pack_unit) {
		this.min_pack_unit = min_pack_unit;
	}
	public int getSelf_check_num() {
		return self_check_num;
	}
	public void setSelf_check_num(int self_check_num) {
		this.self_check_num = self_check_num;
	}
	public int getSampling_num() {
		return sampling_num;
	}
	public void setSampling_num(int sampling_num) {
		this.sampling_num = sampling_num;
	}
	public int getInspection_num() {
		return inspection_num;
	}
	public void setInspection_num(int inspection_num) {
		this.inspection_num = inspection_num;
	}
	public int getThird_party_num() {
		return third_party_num;
	}
	public void setThird_party_num(int third_party_num) {
		this.third_party_num = third_party_num;
	}

}
