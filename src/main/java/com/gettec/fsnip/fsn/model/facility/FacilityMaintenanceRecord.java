package com.gettec.fsnip.fsn.model.facility;

/**
 * Created by wb on 2016/9/13.
 */

import com.gettec.fsnip.fsn.model.common.Model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "facility_maintenance_record")
public class FacilityMaintenanceRecord extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;                     //主键ID

    @Column(name = "facility_id")
    private Long facilityId;             //设备ID

    @Column(name = "maintenance_name")
    private String maintenanceName;      //养护人

    @Column(name = "maintenance_time")
    private Date maintenanceTime ;    //养护时间

    @Column(name = "maintenance_content")
    private String maintenanceContent;  //养护内容

    @Column(name = "remark")
    private String remark;              //备注

    @Column(name = "create_time")
    private Date createTime;            //创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public String getMaintenanceName() {
        return maintenanceName;
    }

    public void setMaintenanceName(String maintenanceName) {
        this.maintenanceName = maintenanceName;
    }

    public Date getMaintenanceTime() {
        return maintenanceTime;
    }

    public void setMaintenanceTime(Date maintenanceTime) {
        this.maintenanceTime = maintenanceTime;
    }

    public String getMaintenanceContent() {
        return maintenanceContent;
    }

    public void setMaintenanceContent(String maintenanceContent) {
        this.maintenanceContent = maintenanceContent;
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
}
