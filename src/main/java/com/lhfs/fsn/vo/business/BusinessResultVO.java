package com.lhfs.fsn.vo.business;

import java.util.List;

import com.lhfs.fsn.vo.ResourceVO;



/**
 * 
 * @author wb
 *
 */
public class BusinessResultVO {
	/**
	 * 企业基本 信息
	 */
	private BussinessBaseInfoVO base_info ;
	/**
	 * 资源基本信息
	 */
	private List<BussinessCredentialsVO> credentials ;

	private List<ResourceVO> licenseImg ;

	public BussinessBaseInfoVO getBase_info() {
		return base_info;
	}

	public void setBase_info(BussinessBaseInfoVO base_info) {
		this.base_info = base_info;
	}

	public List<BussinessCredentialsVO> getCredentials() {
		return credentials;
	}

	public void setCredentials(List<BussinessCredentialsVO> credentials) {
		this.credentials = credentials;
	}

	public List<ResourceVO> getLicenseImg() {
		return licenseImg;
	}

	public void setLicenseImg(List<ResourceVO> licenseImg) {
		this.licenseImg = licenseImg;
	}
	
	
}
