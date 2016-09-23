package com.gettec.fsnip.fsn.dao.facility.impl;


import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.facility.FacilityMaintenanceRecordDao;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.facility.FacilityMaintenanceRecord;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by wb on 2016/9/13.
 *
 */
@Repository("facilityMaintenanceRecordDAO")
public class FacilityMaintenanceRecordDAOImpl extends BaseDAOImpl<FacilityMaintenanceRecord> implements FacilityMaintenanceRecordDao {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<FacilityMaintenanceRecord> getByFacilityIdList(Long facilityId, int page, int pageSize, String maintenanceTime) {
        try {
            String condition = " WHERE e.facilityId = ?1 ";
            if(maintenanceTime !=null&&!"".equals(maintenanceTime)){
                String start = maintenanceTime + " 00:00:00";
                String end = maintenanceTime + " 23:59:59";
                condition += "  AND e.maintenanceTime between '"+start+"' and '"+end+"' " ;
            }
            condition+=" ORDER BY e.createTime desc";
            return this.getListByPage(page,pageSize,condition,new Object[]{facilityId});
        } catch (JPAException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long getByFacilityIdCount(Long facilityId, String maintenanceTime) {
        try {
            String condition = " WHERE e.facilityId = ?1 ";
            if(maintenanceTime !=null&&!"".equals(maintenanceTime)){
                String start = maintenanceTime + " 00:00:00";
                String end = maintenanceTime + " 23:59:59";
                condition += "  AND e.maintenanceTime between '"+start+"' and '"+end+"' "  ;
            }
            return this.count(condition,new Object[]{facilityId});
        } catch (JPAException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteMaintenance(Long facilityId) {
        try {
            String sql = "DELETE FROM facility_maintenance_record WHERE facility_id = ?1 ";
            Query query = this.entityManager.createNativeQuery(sql);
            query.setParameter(1,facilityId);
            query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
