package com.gettec.fsnip.fsn.model.business;

import com.gettec.fsnip.fsn.model.common.Model;

import javax.persistence.*;

/**
 * Created by wb on 2016/11/2.
 */
@Entity(name = "business_unit_to_catering")
public class Catering extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id ;// 主键

    @Column(name = "business_id")
    private Long businessId ;// 主键

    @Column(name = "consume")
    private String consume ;//人均消费

    @Column(name = "telephone")
    private String telephone;// 订餐电话,

    @Column(name = "store_type")
    private String storeType;//店铺类型,

    @Column(name = "longitude")
    private String longitude;//经度,

    @Column(name = "latitude")
    private String latitude;//纬度,

    @Column(name = "placeName")
    private String placeName;//所在位置


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

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
