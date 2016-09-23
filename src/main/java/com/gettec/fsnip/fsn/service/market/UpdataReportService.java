package com.gettec.fsnip.fsn.service.market;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.lhfs.fsn.vo.report.UpdataReportVO;
import com.gettec.fsnip.fsn.model.market.UpdataReport;
import com.gettec.fsnip.fsn.dao.market.UpdataReportDao;

import java.util.List;

/**
 * Create Time 2015-03-25
 * @author HuangYog
 */
public interface UpdataReportService extends BaseService<UpdataReport, UpdataReportDao> {

    List<UpdataReportVO> findAllForCondition(Long org,Integer status, String configure, Integer page, Integer pageSize) throws ServiceException;

    Long getApplyReportTimesCount(Long org,Integer status,String configure)throws ServiceException;

    void changeApplyReportStatus(Long id,Integer status)throws ServiceException;

    void changeApplyReportStatus(String barcode, String testType,Integer status)throws ServiceException;

    void changeApplyReportProductInfo(Product product)throws ServiceException;

    void sendMessageToPortal(String barcode, String testType)throws ServiceException;
    
    void save(UpdataReportVO updataReportVO) throws ServiceException;

}