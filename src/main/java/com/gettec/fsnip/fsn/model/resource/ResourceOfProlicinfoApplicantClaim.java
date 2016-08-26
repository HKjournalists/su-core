package com.gettec.fsnip.fsn.model.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

/**
 * 图片资源信息（企业申请认领时，待审核qs图片）
 * @author ZhangHui 2015/5/29
 */
@Entity(name = "resource_of_prolicinfo_qs_applicant_claim")
public class ResourceOfProlicinfoApplicantClaim extends Model{
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	/**
	 * 文件展示名称
	 */
	@Column(name = "file_name")
	private String file_name;
	
	/**
	 * 文件实际名称
	 */
	@Column(name = "resource_name")
	private String resource_name;

	/**
	 * 文件web路径
	 */
	@Column(name = "url")
	private String url;

	/**
	 * 文件类型
	 */
	@Column(name = "resource_type_id")
	private Long resource_type_id;

	/**
	 * 上传时间
	 */
	@Column(name = "upload_time")
	private Date upload_time;

	/**
	 * 上传人
	 */
	@Column(name = "upload_user_name")
	private String upload_user_name;

	/**
	 * 外键：production_license_info_applicant_claim_backup id
	 */
	@Column(name = "qs_id_applicant_claim_back")
	private Long qs_id_applicant_claim_back;

	/**
	 * 是否删除
	 * 		0代表未删除 
	 * 		1代表已经删除
	 */
	@Column(name = "del")
	private int del;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getResource_name() {
		return resource_name;
	}

	public void setResource_name(String resource_name) {
		this.resource_name = resource_name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getResource_type_id() {
		return resource_type_id;
	}

	public void setResource_type_id(Long resource_type_id) {
		this.resource_type_id = resource_type_id;
	}

	public Date getUpload_time() {
		return upload_time;
	}

	public void setUpload_time(Date upload_time) {
		this.upload_time = upload_time;
	}

	public String getUpload_user_name() {
		return upload_user_name;
	}

	public void setUpload_user_name(String upload_user_name) {
		this.upload_user_name = upload_user_name;
	}

	public Long getQs_id_applicant_claim_back() {
		return qs_id_applicant_claim_back;
	}

	public void setQs_id_applicant_claim_back(Long qs_id_applicant_claim_back) {
		this.qs_id_applicant_claim_back = qs_id_applicant_claim_back;
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}
	
	public ResourceOfProlicinfoApplicantClaim(){}
	
	public ResourceOfProlicinfoApplicantClaim(Resource resource){
		this.file_name = resource.getFileName();
		this.resource_name = resource.getName();
		this.url = resource.getUrl();
		this.resource_type_id = resource.getType().getRtId();
	}
}
