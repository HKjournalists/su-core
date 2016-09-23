package com.gettec.fsnip.fsn.dao.sales.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.sales.SalesCaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.sales.SalesCase;
import com.gettec.fsnip.fsn.vo.sales.SalesCaseVO;

@Repository(value = "salesCaseDAO")
public class SalesCaseDAOImpl extends BaseDAOImpl<SalesCase> implements SalesCaseDAO {

    /**
     * 销售案例管理中的总数
     * Create Date 2015-05-04
     * @author HY
     */
    @Override
    public long countByConfigure(Long organization, String configure) throws DaoException {
        try {
        	// 如果筛选的条件为null，将configure 赋值为 WHERE 1=1 ，当configure 不等于null时，必定包含 WHERE 条件
            configure = (configure == null ? " WHERE 1=1 " : configure);
            configure = configure + " and salescase.organization = ?1 and salescase.del_status = 0 ";
            String sql = "select count(salescase.id) from t_bus_sales_case salescase " + configure;
            return this.countBySql(sql, new Object[]{organization});
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(), jpae.getException());
        }
    }

    /**
     * 查询相关企业的所有的销售案例
     * Create Date 20150-05-04
     * @author HY
     */
    @Override
    public List<SalesCaseVO> getListByOrganizationWithPage(Long organization, String condition,
                           Integer page, Integer pageSize) throws DaoException {
        try {
        	// 如果筛选的条件为null，将configure 赋值为 WHERE 1=1 ，当configure 不等于null时，必定包含 WHERE 条件
            condition = (condition == null ? " WHERE 1=1 " : condition);
            condition += " and salescase.organization = ?1 and salescase.delStatus = 0 ";
            String jpql = " select new com.gettec.fsnip.fsn.vo.sales.SalesCaseVO(salescase) from " +
                    "com.gettec.fsnip.fsn.model.sales.SalesCase salescase " + condition;
            return this.getListByJPQL(SalesCaseVO.class, jpql, page, pageSize, new Object[]{organization});
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(), jpae);
        }
    }

    /**
     * 验证销售案例名称是否重复
     * @author HY
     * Created Date 2015-05-05
     */
    @Override
    public long countByName(String name, Long organization, Long id) throws DaoException {
        try {
            String condition = "";
            Object[] params = null;
            if (id != null && id > 0) {
            	// 参数id存在时，表示编辑时的验证，验证名称是否重复需要不包括自己
                condition = " and salescase.id != ?3 ";
                params = new Object[]{name, organization, id};
            } else {
                params = new Object[]{name, organization};
            }
            String sql = "select count(salescase.id) from t_bus_sales_case salescase where salescase.name = ?1 " +
                    "and salescase.organization = ?2 and salescase.del_status = 0 " + condition;
            return this.countBySql(sql, params);
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(), jpae.getException());
        }
    }

    /**
     * 获取指定企业下的销售案例集合，包含销售案例的一张封面图片。
     * author HY
     * Create Date 2015-05-05
     */
    @SuppressWarnings("unchecked")
	@Override
    public List<SalesCaseVO> getListEntityByBusId(Long busId) throws DaoException {
        try {
            String sql = "SELECT temp.* from (SELECT sc.id sid,sc.`name`,sc.description,sres.url FROM t_bus_sales_case sc " +
                    " LEFT JOIN t_sales_resource sres ON sc.guid = sres.guid " +
                    " WHERE sc.business_id = ?1 AND sres.del_status = 0 AND sc.del_status = 0 " +
                    " ORDER BY sres.cover DESC , sres.id ASC ) temp GROUP BY temp.sid ";
            Query query=entityManager.createNativeQuery(sql);
            query.setParameter(1, busId);
            List<Object[]> result = query.getResultList();
            List<SalesCaseVO> lists = null;
            // 将Entity 转换为 VO
            if(result!=null && result.size()>0){
                lists = new ArrayList<SalesCaseVO>();
                for(Object[] obj : result){
                    SalesCaseVO vo = new SalesCaseVO();
                    vo.setId(obj[0] != null ? Long.valueOf(obj[0].toString()) : -1);
                    vo.setSalesCaseName(obj[1] != null ? obj[1].toString() : "");
                    vo.setSalesDetails(obj[2] != null ? obj[2].toString() : "");
                    vo.setUrl(obj[3] != null ? obj[3].toString() : "");
                    lists.add(vo);
                }
            }
            return lists; //返回VO数据
        } catch (Exception e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    /**
     * 根据组织机构获取企业销售案例
     * @author HY
     * Create Date 2015-05-07
     */
    @Override
    public List<SalesCase> getListEntityByOrg(Long org) throws DaoException {
        String condition = " EHERE e.organization = ?1 ";
        try {
            return this.getListByCondition(condition,new Object[]{org});
        } catch (JPAException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

}
