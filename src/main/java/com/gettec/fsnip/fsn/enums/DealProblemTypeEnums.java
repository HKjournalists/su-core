package com.gettec.fsnip.fsn.enums;

public enum DealProblemTypeEnums {
	ZERO(0,"无产品信息","未处理","监管","新增投诉","提交监管"), 
	ONE(1,"产品信息有错","已处理","食安测","已提交投诉","提交企业"),
	TWO(2,"有产品信息，无报告信息",null,"终端","已忽略投诉","忽略"),
	THREE(3,"报告过期，无上传新的报告",null,null,null,null),
	FOUR(4,"无生产企业证照",null,null,null,null),
	FIVE(5,"生产企业证照错误",null,null,null,null),
	OTHER(-1,"其他",null,null,null,null);
	
	private DealProblemTypeEnums(int id,String problemType,String dealStatus,String origin,String complainStatus,String commitStatus){
		this.id=id;
		this.problemType = problemType;
		this.dealStatus = dealStatus;
		this.origin = origin;
		this.complainStatus = complainStatus;
		this.commitStatus = commitStatus;
	}
	private int id;
	private String problemType; //问题类型
	private String dealStatus; //企业处理
	private String origin;     //信息来源
	private String complainStatus;//投诉处理
	private String commitStatus; //管理员处理
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProblemType() {
		return problemType;
	}
	public void setProblemType(String problemType) {
		this.problemType = problemType;
	}
	public String getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getComplainStatus() {
		return complainStatus;
	}
	public void setComplainStatus(String complainStatus) {
		this.complainStatus = complainStatus;
	}
	public String getCommitStatus() {
		return commitStatus;
	}
	public void setCommitStatus(String commitStatus) {
		this.commitStatus = commitStatus;
	}
	public static DealProblemTypeEnums typeEnumeId(int id){
		DealProblemTypeEnums[] values = DealProblemTypeEnums.values();
		for(DealProblemTypeEnums item : values){
			if(item.getId() == id)
				return item;
		}
		return null;
	}
}
