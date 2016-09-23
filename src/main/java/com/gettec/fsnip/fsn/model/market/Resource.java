package com.gettec.fsnip.fsn.model.market;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;

@SuppressWarnings("serial")
@Entity(name="T_TEST_RESOURCE")
public class Resource extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="RESOURCE_ID")
	private Long id;
	
	@Column(name="FILE_NAME", unique=true, nullable=false)
	private String fileName;
	
	@Column(name="RESOURCE_NAME")
	private String name;
	
	@Column(name="URL")
	private String url;
	
	@Column(name="origin")
	private String origin;
	
	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.DETACH}, optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name="RESOURCE_TYPE_ID")
	private ResourceType type;
	
	@Column(name="UPLOAD_DATE")
	private Date uploadDate;
	
	@Transient
	private byte[] file;

	@Transient
	private String fileBase64;
	
	@Transient
	String interceptionPdfPath;  // 政府抽检的截取前两页pdf路径；合成pdf的时候传到前台

	@Transient
	private Long businessToId; //企业id
	@Transient
	private String businessName; //企业名称
	
	public byte[] getFile() {
		return file;
	}
	
	public void setFile(byte[] file) {
		this.file = file;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getUploadDate() {
		return uploadDate;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getInterceptionPdfPath() {
		return interceptionPdfPath;
	}

	public void setInterceptionPdfPath(String interceptionPdfPath) {
		this.interceptionPdfPath = interceptionPdfPath;
	}
	
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Resource(){}
	
	public Resource(Resource res){
		this.id = res.getId();
		this.fileName = res.getFileName();
		this.name = res.getName();
		this.url = res.getUrl();
		this.type = res.getType();
		this.uploadDate = res.getUploadDate();
		this.file=res.getFile();
		this.origin=res.getOrigin();
	}

	@Override
	public String toString() {
		return "Resource [id=" + id + ", fileName=" + fileName + ", name=" + name + ", url=" + url + ", type=" + type
				+ ", uploadDate=" + uploadDate + ", file=" + Arrays.toString(file) + ", interceptionPdfPath="
				+ interceptionPdfPath + ", origin=" + origin+ "]";
	}

	public Long getBusinessToId() {
		return businessToId;
	}

	public void setBusinessToId(Long businessToId) {
		this.businessToId = businessToId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getFileBase64() {
		return fileBase64;
	}

	public void setFileBase64(String fileBase64) {
		this.fileBase64 = fileBase64;
	}
}
