package com.gettec.fsnip.fsn.service.facility;

import com.gettec.fsnip.fsn.dao.facility.FacilityMaintenanceRecordDao;
import com.gettec.fsnip.fsn.model.facility.FacilityMaintenanceRecord;
import com.gettec.fsnip.fsn.service.common.BaseService;

import java.util.List;

/**
 * Created by wb on 2016/9/13.
 * 养护记录service层
 */
public interface FacilityMaintenanceRecordService extends BaseService<FacilityMaintenanceRecord,FacilityMaintenanceRecordDao>{

    /**
     * 根据设备信息ID获取养护记录信息
     * @param facilityId 设备信息ID
     * @param page    页码
     * @param pageSize 每页显示总条数
     * @param maintenanceTime  养护时间
     * @return  返回养护记录List集合
     */
    List<FacilityMaintenanceRecord> getByFacilityIdList(Long facilityId, int page, int pageSize, String maintenanceTime);

    /**
     *根据设备信息ID获取养护记录信息
     * @author wb 2016.9.16
     * @param facilityId  设备信息ID
     * @param maintenanceTime  养护时间
     * @return 返回养护记录总数
     */
    Long getByFacilityIdCount(Long facilityId, String maintenanceTime);

    /**
     *根据设备信息ID删除养护记录信息
     * @author wb 2016.9.14
     * @param facilityId  设备信息ID
     * @return 返回养护记录总数
     */
    void deleteMaintenance(Long facilityId);

    /**
     *根据设备信息ID获取养护记录信息
     * @author wb 2016.9.16
     * @param maintenance  养护记录对象
     * @return
     */
    boolean maintenanceSaveOrEdit(FacilityMaintenanceRecord maintenance);

    /**
     * 删除养护记录信息
     *  @authar wb
     * @date 2016.9.16
     * @param id
     * @return
     */
    boolean delMaintenanceInfo(Long id);

}
