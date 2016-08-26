package com.gettec.fsnip.fsn.dao.sales.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.sales.SalesResourceDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.vo.sales.ContractResVO;

/**
 * Create Date 2015-04-24
 *
 * @author HY
 */
@Repository(value = "salesResourceDAO")
public class SalesResourceDAOImpl extends BaseDAOImpl<SalesResource> implements SalesResourceDAO {

    /**
     * 通过全局guid 获取资源列表
     * @author tangxin 2015-04-29
     */
    @Override
    public List<SalesResource> getListByGUID(String guid) throws DaoException {
        try {
            if (guid == null && "".equals(guid)) {
                return null;
            }
            String condition = " WHERE e.guid = ?1 and e.delStatus = 0 ";
            return this.getListByCondition(condition, new Object[]{guid});
        } catch (Exception e) {
            throw new DaoException(e.getMessage(), e);
        }

    }

    /**
     * 根据企业Org获取该企业的所有电子合同的资源id
     * @author HY On 2015-05-08
     */
    @SuppressWarnings("unchecked")
	@Override
    public List<ContractResVO> getContResIdList(Long org) throws DaoException {
        try {
            String sql = " SELECT temp.cid,temp.cname,temp.rid,temp.url FROM " +
                    " (SELECT tbc.id cid,tbc.`name` cname,tsr.id rid,tsr.url FROM t_sales_resource tsr " +
                    "LEFT JOIN t_bus_contract tbc  ON tsr.guid = tbc.guid " +
                    " WHERE tbc.organization = ?1 AND tbc.del_status = 0 ) temp ";
            List<Object[]> result = entityManager.createNativeQuery(sql).setParameter(1, org).getResultList();
            return enclContractResVO(result);
        } catch (Exception e) {
            throw new DaoException(e.getMessage(), e);
        }

    }

    /**
     * @author HY
     * Create Date 2015-05-08
     */
    @SuppressWarnings("unchecked")
	@Override
    public ContractResVO getElectDataAndRes(Long org) throws DaoException {
        try {
            String sql = "SELECT temp.cid,temp.cname,temp.rid,temp.url FROM " +
                    " (SELECT tbc.id cid,tbc.`name` cname,tsr.id rid,tsr.url FROM t_sales_resource tsr " +
                    "LEFT JOIN t_bus_electronic_data tbc  ON tsr.guid = tbc.guid " +
                    " WHERE tbc.organization = ?1  and tsr.del_status = 0 order by tsr.id desc) temp ";
            List<Object[]> result = entityManager.createNativeQuery(sql).setParameter(1, org).getResultList();
            return result!=null && result.size() > 0 ? enclContractResVO(result).get(0) : null;
        }catch (Exception e) {
            throw new DaoException(e.getMessage(), e);
        }

    }

    /**
     * 根据资源id获取资源 List
     * @author HY
     * Create Date 2015-05-08
     */
    @Override
    public List<SalesResource> getListByResIds(String resId) throws DaoException {
        try {
            resId = resId.substring(0,resId.length()-1);
            resId ="("+resId+")";
            String sql = "SELECT res.* FROM t_sales_resource res " +
                    "WHERE res.id IN "+resId;
            List<SalesResource> result = this.getListBySQL(SalesResource.class, sql, null);
            return result;
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(), jpae);
        }
    }

    /**
     * 封装电子合同的名称和资源ID
     * @author HY
     * Create Date 2015-05-09
     */
    private List<ContractResVO> enclContractResVO(List<Object[]> result) {
        List<ContractResVO> list = null ;
        if(result != null && result.size() > 0){
            list = new ArrayList<ContractResVO>();
            for(int i= 0 ; i < result.size() ; i++){
                Object[] obj = result.get(i);
                ContractResVO cresVO = new ContractResVO();
                cresVO.setCId(obj[0] != null && !"".equals(obj[0]) ? Long.parseLong(obj[0].toString()):-1);
                cresVO.setContractName(obj[1] != null ? obj[1].toString():"");
                cresVO.setResId(obj[2] != null && !"".equals(obj[2]) ? Long.parseLong(obj[2].toString()):-1);
                cresVO.setUrl(obj[3] != null ? obj[3].toString() : "");
                list.add(cresVO);
            }
        }
        return list;
    }

}
