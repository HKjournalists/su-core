package com.gettec.fsnip.fsn.model.account;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 台账系统Model 
 * @author HuangYog 
 * @email HuangYong@fsnip.com
 */
@Entity(name = "tz_business_relation")
public class TZBusinessRelation extends Model{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "self_business_id")
    private Long selfBusinessId;  // 自身企业ID
    
    @Column(name = "business_id")
    private Long businessId; //企业ID
    
    @Column(name = "relation_type")
    private int type;   //企业关系，0：供应，1：销售
    
    @Column(name = "create_time")
    private String createDate;   //创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSelfBusinessId() {
        return selfBusinessId;
    }

    public void setSelfBusinessId(Long selfBusinessId) {
        this.selfBusinessId = selfBusinessId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    
    public TZBusinessRelation() {    }

    public TZBusinessRelation(Long id, Long selfBusinessId, Long businessId,
            int type,String createDate) {
        super();
        this.id = id;
        this.selfBusinessId = selfBusinessId;
        this.businessId = businessId;
        this.type = type;
        this.createDate = createDate;
    }
}
