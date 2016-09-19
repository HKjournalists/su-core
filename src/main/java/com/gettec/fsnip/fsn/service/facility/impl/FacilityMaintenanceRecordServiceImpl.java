package com.gettec.fsnip.fsn.service.facility.impl;

import com.gettec.fsnip.fsn.dao.facility.FacilityMaintenanceRecordDao;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.facility.FacilityMaintenanceRecord;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.facility.FacilityMaintenanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by wb on 2016/9/16.
 */
@Service("facilityMaintenanceRecordService")
public class FacilityMaintenanceRecordServiceImpl extends BaseServiceImpl<FacilityMaintenanceRecord, FacilityMaintenanceRecordDao>
        implements FacilityMaintenanceRecordService {

    @Autowired
    private FacilityMaintenanceRecordDao facilityMaintenanceRecordDAO;
    @Override
    public FacilityMaintenanceRecordDao getDAO() {
        return facilityMaintenanceRecordDAO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<FacilityMaintenanceRecord> getByFacilityIdList(Long facilityId, int page, int pageSize, String maintenanceTime) {

        List<FacilityMaintenanceRecord> amrList = null;
        try {
            amrList = facilityMaintenanceRecordDAO.getByFacilityIdList(facilityId, page, pageSize, maintenanceTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amrList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Long getByFacilityIdCount(Long facilityId, String maintenanceTime) {
        Long total = null;
        try {
            total = facilityMaintenanceRecordDAO.getByFacilityIdCount(facilityId,maintenanceTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteMaintenance(Long facilityId) {
        facilityMaintenanceRecordDAO.deleteMaintenance(facilityId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean maintenanceSaveOrEdit(FacilityMaintenanceRecord maintenance) {
        try {
            if(maintenance.getId()==null||"".equals(maintenance.getId())){
                maintenance.setCreateTime(new Date());
                facilityMaintenanceRecordDAO.persistent(maintenance);
            }else{
                FacilityMaintenanceRecord record = facilityMaintenanceRecordDAO.findById(maintenance.getId());
                record.setMaintenanceName(maintenance.getMaintenanceName());
                record.setMaintenanceTime(maintenance.getMaintenanceTime());
                record.setMaintenanceContent(maintenance.getMaintenanceContent());
                record.setRemark(maintenance.getRemark());
            }
            return true;
        } catch (JPAException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delMaintenanceInfo(Long id) {
        try {
            FacilityMaintenanceRecord record = facilityMaintenanceRecordDAO.findById(id);
            facilityMaintenanceRecordDAO.remove(record);
            return true;
        } catch (JPAException e) {
            e.printStackTrace();
        }
        return false;
    }
}
