package com.lhfs.fsn.vo.business;


public class BussinessUnitVOToPortal {

	private Long id;
	
	private String name;  // 企业名称
	
	private String address;	 //地址
	
	private String qs_no;   //qs号
	
	private Long qsId;
	
	private String licImg;  //营业执照图片路径
	
	private String qsImg;   //生产许可证图片路径
	
	private String disImg;  //流通许可证图片

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getQs_no() {
		return qs_no;
	}

	public void setQs_no(String qs_no) {
		this.qs_no = qs_no;
	}

	public String getLicImg() {
		return licImg;
	}

	public void setLicImg(String licImg) {
		this.licImg = licImg;
	}

	public String getQsImg() {
		return qsImg;
	}

	public void setQsImg(String qsImg) {
		this.qsImg = qsImg;
	}

	public String getDisImg() {
		return disImg;
	}

	public void setDisImg(String disImg) {
		this.disImg = disImg;
	}

	public Long getQsId() {
		return qsId;
	}

	public void setQsId(Long qsId) {
		this.qsId = qsId;
	}
	
	
}
