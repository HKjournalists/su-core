package com.gettec.fsnip.fsn.service.procurement;

import com.gettec.fsnip.fsn.dao.procurement.ProcurementUsageRecordDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.procurement.ProcurementUsageRecord;
import com.gettec.fsnip.fsn.service.common.BaseService;

import java.util.List;

/**
 * ProcurementUsageRecord Service
 * @author lxz
 *
 */
public interface ProcurementUsageRecordService extends BaseService<ProcurementUsageRecord, ProcurementUsageRecordDAO>{
    /**
     * 根据采购id获取使用记录数量
     * @param useDate 使用时间
     * @param procurementId 采购id
     * @return
     */
    long getRecordTotalByPid(String useDate, Long procurementId)throws ServiceException;

    /**
     * 根据采购id获取使用记录集合 有分页
     * @param page
     * @param pageSize
     * @param useDate 使用时间
     * @param procurementId 采购id
     * @return
     */
    List<ProcurementUsageRecord> getRecordListByPid(int page, int pageSize, String useDate, Long procurementId)throws ServiceException;
}