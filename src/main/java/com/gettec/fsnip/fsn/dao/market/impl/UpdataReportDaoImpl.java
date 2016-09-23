package com.gettec.fsnip.fsn.dao.market.impl;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.market.UpdataReportDao;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.market.UpdataReport;
import org.springframework.stereotype.Repository;

/**
 * Create Time 2015-03-25
 * @author HuangYog
 * @email huangyong@fsnip.com
 */
@Repository(value="updataReportDao")
public class UpdataReportDaoImpl extends BaseDAOImpl<UpdataReport> implements UpdataReportDao {
    
    /**
     * 加载所有符合条件的报告更新记录
     * @author HuangYog
     */
    @Override
    public List<UpdataReport> findAllForCondition(Integer page,
            Integer pageSize, Map<String, Object> configure)throws DaoException {
        try {
            String condition = (String) configure.get("condition");
            Object[] params = (Object[]) configure.get("params");
            List<UpdataReport> lists= this.getListByPage(page, pageSize, condition, params);
            return lists;
        } catch (JPAException jpae) {
            throw new DaoException("UpdataReportDaoImpl--> findAllForCondition 按条件分页查找不同状态的报告申请更新记录时 《 出现异常》",jpae.getException());
        }
    }
    /**
     * 按条件查询申请更新报告的总数
     * @author HuangYog
     */
    @Override
    public Long getApplyReportTimesCount( Map<String, Object> configure) throws DaoException {
        try {
            String condition = (String) configure.get("condition");
            Object[] params = (Object[]) configure.get("params");
            return this.count(condition, params);
        } catch (JPAException jpae) {
            throw new DaoException("UpdataReportDaoImpl--> getApplyReportTimesCount 跟新申请 《 出现异常》",jpae.getException());
        }
    }
    
    /**
     *  根据barcode和报告类型查找是否有portal申请跟新记录
     *  @author HuangYog
     */
    @Override
    public UpdataReport findByBarcodeAndReportType(String barcode,
            String testType) throws DaoException {
        String jpql = "WHERE e.productBarcode = ?1 AND e.reportType = ?2 " ;
        try {
            List<UpdataReport> list = this.getListByCondition( jpql, new Object[] {barcode,testType});
            return list !=null && list.size() > 0 ? list.get(0): null ;
        } catch (JPAException JPAE) {
            throw new DaoException("根据barcode和报告类型查找是否有portal申请跟新记录  《 出现异常》",JPAE.getException());
        }
    }
    
    /**
     * 根据产品id查找是否存在相关报告申请更新记录
     * @author HuangYog
     */
    @Override
    public List<UpdataReport> findByProductId(Long productId) throws DaoException {
        String jpql = " WHERE e.productId = ?1 ";
        try {
            return this.getListByCondition(jpql, new Object[] {productId});
        } catch (JPAException JPAE) {
            throw new DaoException("根据产品id查找是否存在相关报告申请更新记录    《出现异常》",JPAE.getException());
        }
    }
    
    /**
     * 根据产品id和报告类型查找是否已存在对应的记录
     * @param pid  产品id
     * @param reportType 报告类型
     * @author HuangYog
     */
    @Override
    public UpdataReport checkToProIdAandreportType(Long pid, String reportType)throws DaoException {
        String jpql = " WHERE e.productId = ?1 AND e.reportType = ?2 ";
        try {
            List<UpdataReport> lists = this.getListByCondition(jpql, new Object[]{pid,reportType});
           return lists!= null && lists.size() >0 ? lists.get(0):null;
        } catch (JPAException jpae) {
            throw new DaoException("UpdataReportDaoImpl-->checkToProIdAandreportType 根据产品id和报告类型查找是否已存在对应的记录 《异常》",jpae);
        }
    }
}
