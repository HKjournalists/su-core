package com.gettec.fsnip.fsn.vo.member;

import java.util.List;

import javax.persistence.Column;

import com.gettec.fsnip.fsn.model.market.Resource;


/**
 * 用于封装人员管理界面展示的人员字段（轻量级封装）
 * @author ZhangHui 2015/4/11
 */
public class MemberManageViewVO {
	/**
	 * id
	 */
	private long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 头像图片
	 */
	private String headImgUrl;
	/**
	 * 岗位
	 */
	private String position;
	/**
	 * 身份证号
	 */
	private String identificationNo;
	
	/**
	 * 健康证图片地址
	 */
	private String healthImgUrl;  
	
	/**
	 * 从业资格证图片地址
	 */
	private String qualificationImgUrl;  
	
	/**
	 * 荣誉证书图片地址
	 */
	private String honorImgUrl;   
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getIdentificationNo() {
		return identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public String getHealthImgUrl() {
		return healthImgUrl;
	}

	public void setHealthImgUrl(String healthImgUrl) {
		this.healthImgUrl = healthImgUrl;
	}

	public String getQualificationImgUrl() {
		return qualificationImgUrl;
	}

	public void setQualificationImgUrl(String qualificationImgUrl) {
		this.qualificationImgUrl = qualificationImgUrl;
	}

	public String getHonorImgUrl() {
		return honorImgUrl;
	}

	public void setHonorImgUrl(String honorImgUrl) {
		this.honorImgUrl = honorImgUrl;
	}
	
}
