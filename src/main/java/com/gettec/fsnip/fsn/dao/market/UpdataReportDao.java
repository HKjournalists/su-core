package com.gettec.fsnip.fsn.dao.market;

import java.util.List;
import java.util.Map;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.market.UpdataReport;

/**
 * Create Time 2015-03-25
 * @author HuangYog
 * @email HuangYong@fsnip.com
 */
public interface UpdataReportDao extends BaseDAO<UpdataReport>{

    List<UpdataReport> findAllForCondition(Integer page, Integer pageSize,
            Map<String, Object> configure) throws DaoException;

    Long getApplyReportTimesCount( Map<String, Object> configure) throws DaoException;

    UpdataReport findByBarcodeAndReportType(String barcode, String testType)throws DaoException;

    List<UpdataReport> findByProductId(Long id)throws DaoException;

    public UpdataReport checkToProIdAandreportType(Long pid, String reportType) throws DaoException;
}
