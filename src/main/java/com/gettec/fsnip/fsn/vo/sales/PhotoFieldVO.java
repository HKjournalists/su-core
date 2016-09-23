package com.gettec.fsnip.fsn.vo.sales;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;

/**
 * 相册结构话字段VO
 * @author tangxin 2015-05-05
 *
 */
public class PhotoFieldVO {

	private Long id;
	private String no; //代码编号
	private String name;//机构名称 经营主体名称 纳税人名称 产品名称
	private Date startDate;//证照起始日期
	private Date endDate; //证照截止日期 
	private String personInCharge; //法人代表
	private Date registeDate; //注册时间
	private String type; //认证类型 
	private String format; //规格
	private String desc; //描述-字段
	private long censorReportNumber; //送检报告数量
	private long allReportNumber; //总报告数量
	private int fieldType; // 字段类型
	private String albumDescribe; //相册描述
	
	private long zjReportNumber;
	
	private long cjReportNumber;
	
	private double riskIndex;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getStartDate() {
		return startDate;
	}
	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getEndDate() {
		return endDate;
	}
	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getPersonInCharge() {
		return personInCharge;
	}
	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}
	public Date getRegisteDate() {
		return registeDate;
	}
	public void setRegisteDate(Date registeDate) {
		this.registeDate = registeDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public long getCensorReportNumber() {
		return censorReportNumber;
	}
	public void setCensorReportNumber(long censorReportNumber) {
		this.censorReportNumber = censorReportNumber;
	}
	public long getAllReportNumber() {
		return allReportNumber;
	}
	public void setAllReportNumber(long allReportNumber) {
		this.allReportNumber = allReportNumber;
	}
	public int getFieldType() {
		return fieldType;
	}
	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
	public String getAlbumDescribe() {
		return albumDescribe;
	}
	public void setAlbumDescribe(String albumDescribe) {
		this.albumDescribe = albumDescribe;
	}
	
	public long getZjReportNumber() {
		return zjReportNumber;
	}
	public void setZjReportNumber(long zjReportNumber) {
		this.zjReportNumber = zjReportNumber;
	}
	public long getCjReportNumber() {
		return cjReportNumber;
	}
	public void setCjReportNumber(long cjReportNumber) {
		this.cjReportNumber = cjReportNumber;
	}
	public double getRiskIndex() {
		return riskIndex;
	}
	public void setRiskIndex(double riskIndex) {
		this.riskIndex = riskIndex;
	}
	public PhotoFieldVO(){}
	
	public PhotoFieldVO(Long id, String no, String name, Date startDate, Date endDate,
			String personInCharge, Date registeDate, String type,
			String format, String desc, long censorReportNumber,
			long allReportNumber, int fieldType,String describe) {
		super();
		this.id = id;
		this.no = no;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.personInCharge = personInCharge;
		this.registeDate = registeDate;
		this.type = type;
		this.format = format;
		this.desc = desc;
		this.censorReportNumber = censorReportNumber;
		this.allReportNumber = allReportNumber;
		this.fieldType = fieldType;
		this.albumDescribe = describe;
	}
	@Override
	public String toString() {
		return "PhotoFieldVO [id=" + id + ", no=" + no + ", name=" + name + ", startDate=" + startDate + ", endDate="
				+ endDate + ", personInCharge=" + personInCharge + ", registeDate=" + registeDate + ", type=" + type
				+ ", format=" + format + ", desc=" + desc + ", censorReportNumber=" + censorReportNumber
				+ ", allReportNumber=" + allReportNumber + ", fieldType=" + fieldType + ", albumDescribe="
				+ albumDescribe + ", zjReportNumber=" + zjReportNumber + ", cjReportNumber=" + cjReportNumber
				+ ", riskIndex=" + riskIndex + "]";
	}
	
}
