package com.gettec.fsnip.fsn.model.facility;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wb on 2016/9/13.
 */
@Entity(name = "facility_info")
public class FacilityInfo extends Model{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id ;                //主键ID

    @Column(name = "business_id")
    private Long businessId;         //企业id

    @Column(name = "facility_name")
    private String facilityName;     //名称

    @Column(name = "manufacturer")
    private String manufacturer;     //生产厂家

    @Column(name = "facility_type")
    private String facilityType;     //型号

    @Column(name = "facility_count")
    private int facilityCount;    //数量

    @Column(name = "buying_time")
    private Date buyingTime;       //采购时间

    @Column(name = "application")
    private String application;      //用途

    @OneToOne(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinColumn(name="RESOURCE_ID")
    private Resource resource;  // 营业执照图片

    @Column(name = "remark")
    private String remark;           //备注

    @Column(name = "create_time")
    private Date createTime;      //创建时间

    @Transient
    private Resource rsImg ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public int getFacilityCount() {
        return facilityCount;
    }

    public void setFacilityCount(int facilityCount) {
        this.facilityCount = facilityCount;
    }

    public Date getBuyingTime() {
        return buyingTime;
    }

    public void setBuyingTime(Date buyingTime) {
        this.buyingTime = buyingTime;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Resource getRsImg() {
        return rsImg;
    }

    public void setRsImg(Resource rsImg) {
        this.rsImg = rsImg;
    }
}
