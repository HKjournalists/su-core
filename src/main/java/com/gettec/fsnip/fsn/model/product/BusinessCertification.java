package com.gettec.fsnip.fsn.model.product;

import java.util.Date;

import javax.persistence.*;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;


@Entity(name="business_certification")
public class BusinessCertification extends Model{
	private static final long serialVersionUID = 7100327203340801253L;
	@Id @GeneratedValue
	private Long id;
	
	@Column(name="business_id", nullable=true, length=255)
	private Long businessId;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST})
	@JoinColumn(name="cert_id")
	private Certification cert;
	
	@Column(name="enddate", nullable=true, length=255)
	private Date endDate; //截止日期,永久有效使用固定日期2200-01-01
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="resource_id", nullable=true)
	private Resource certResource;  //  证书图片资源(实例)

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getEndDate() {
		return endDate;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Certification getCert() {
		return cert;
	}

	public void setCert(Certification cert) {
		this.cert = cert;
	}

	public Resource getCertResource() {
		return certResource;
	}

	public void setCertResource(Resource certResource) {
		this.certResource = certResource;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	
}
