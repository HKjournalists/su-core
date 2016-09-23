package com.gettec.fsnip.fsn.model.operate;

import com.gettec.fsnip.fsn.model.common.Model;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by wb on 2016/9/17.
 * @author wb  2016.9.17
 */
@Entity(name="operate_info")
public class OperateInfo extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;  //主键ID

    @Column(name = "business_id")
    private Long businessId; //企业ID

    @Column(name = "operate_type")
    private String operateType; //经营类型

    @Column(name = "operate_scope")
    private  String operateScope; //经营规模

    @Column(name = "person_count")
    private  int personCount = 0; //企业人数

    @Column(name = "floor_area")
    private String floorArea;  //占地面具

    @Column(name = "create_time")
    private Date createTime ; //创建日期

    @Column(name = "update_time")
    private  Date updateTime ; // 修改日期

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

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateScope() {
        return operateScope;
    }

    public void setOperateScope(String operateScope) {
        this.operateScope = operateScope;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public String getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(String floorArea) {
        this.floorArea = floorArea;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
