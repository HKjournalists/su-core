package com.gettec.fsnip.fsn.dao.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZBusAccountOutDAO;
import com.gettec.fsnip.fsn.dao.account.TZProductTrailDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.account.TZBusAccountOut;
import com.gettec.fsnip.fsn.vo.account.RetrunAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HY on 2015/5/18.
 * desc:
 */
@Repository(value = "tzBusAccountOutDAO")
public class TZBusAccountOutDAOImpl extends
        BaseDAOImpl<TZBusAccountOut> implements
        TZBusAccountOutDAO {

    @Autowired TZProductTrailDAO tZProductTrailDAO;
    
    SimpleDateFormat SDFTIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 加载企业退货单别信息
     * @author HY
     */
    @Override
    public List<RetrunAccountVO> loadReturnAccountList(Long busId, String busName,String busLic, int page,
                                                       int pageSize, String condition) throws DaoException {
        try {
            String sql = "SELECT tzao.id,tzao.account_no,tzao.out_status,tzao.create_time,bt.`name`,bt.license_no,tzao.in_status FROM tz_business_account_out tzao " +
                    "LEFT JOIN business_unit bt ON tzao.in_business_id = bt.id WHERE tzao.out_business_id = ?1 "+condition;
            Query query = entityManager.createNativeQuery(sql.toString()).setParameter(1, busId);
            if (page > 0) {
                query.setFirstResult((page - 1) * pageSize);
                query.setMaxResults(pageSize);
            }
            List<Object[]> result = query.getResultList();
            return enclosureRetrunAccountVOs(result, busName, null);
        }catch (Exception e){
            throw new DaoException("TZBusAccountOutDAOImpl.loadReturnAccountList()加载企业退货单别信息，异常",e);
        }

    }

    private List<RetrunAccountVO> enclosureRetrunAccountVOs(List<Object[]> result, String busName, String busLic) throws ParseException {
        List<RetrunAccountVO> lists = null;
        if(result!=null && result.size() > 0){
            lists = new ArrayList<RetrunAccountVO>();
            for (int i = 0; i < result.size(); i++){
                Object[] objs = result.get(i);
                RetrunAccountVO vo = new RetrunAccountVO();
                vo.setId(objs[0] != null ? Long.valueOf(objs[0].toString()) : -1);
                vo.setAccountNo(objs[1] != null ? objs[1].toString() : "");
                vo.setOutStatus(objs[2] != null ? Integer.valueOf(objs[2].toString()) : null);
                vo.setAccountDate(objs[3] != null ? SDFTIME.format(SDFTIME.parse(objs[3].toString())) : null);
                vo.setInBusName(objs[4] != null ? objs[4].toString() : "");
                vo.setInStatus(objs[6] != null ? Integer.valueOf(objs[6].toString()) : null);
                vo.setOutBusLic(busLic);
                if(busLic!=null){
                    vo.setOutBusLic(busLic);
                }else {
                    vo.setOutBusLic(objs[5] != null ? objs[5].toString() : "");
                }
                vo.setOutBusName(busName);
                lists.add(vo);
            }
        }
        return lists;
    }

    /**
     * 加载企业退货单别总数
     * @author HY
     */
    @Override
    public Long loadReturnAccountListTotal(Long busId, String condition) throws DaoException {
        try {
            String sql = "SELECT count(*) FROM tz_business_account_out tzao " +
                    "LEFT JOIN business_unit bt ON tzao.in_business_id = bt.id WHERE tzao.out_business_id = ?1 " + condition;
            return this.countBySql(sql, new Object[]{busId});
        }catch (Exception e){
            throw new DaoException("TZBusAccountOutDAOImpl.loadReturnAccountList()加载企业退货单别总数，异常",e);
        }
    }

    /**
     * 查询别人退货过来的退货信息总数
     * @author HY
     */
    @Override
    public Long loadReturnInAccountListTotal(Long id, String condition) throws DaoException {
        String sql = "SELECT count(*) FROM tz_business_account_out tzao " +
                "LEFT JOIN business_unit bt ON tzao.out_business_id = bt.id WHERE tzao.in_business_id = ?1 "+
                "AND tzao.out_status = 1 " + condition;
        try {
            return this.countBySql(sql, new Object[]{id});
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(),jpae.getException());
        }
    }

    /**
     * 查询别人退货过来的退货信息
     * @author HY
     */
    @Override
    public List<RetrunAccountVO> loadReturnInAccountList(Long id, String name, String licNo, int page, int pageSize, String condition) throws DaoException {
        try{
            String sql = "SELECT tzao.id,tzao.account_no,tzao.out_status,tzao.create_time,bt.`name`,bt.license_no,tzao.in_status FROM tz_business_account_out tzao " +
                    "LEFT JOIN business_unit bt ON tzao.out_business_id = bt.id WHERE tzao.in_business_id = ?1 "+
                    "AND tzao.out_status = 1 " + condition;
            Query query = entityManager.createNativeQuery(sql.toString()).setParameter(1, id);
            if (page > 0) {
                query.setFirstResult((page - 1) * pageSize);
                query.setMaxResults(pageSize);
            }
            List<Object[]> result = query.getResultList();
            return enclosureRetrunAccountVOs(result, name, null);
        }catch (Exception e){
            throw new DaoException("TZBusAccountOutDAOImpl.loadReturnInAccountList()查询别人退货过来的退货信息，异常",e);
        }
    }

    @Override
    public List<RetrunAccountVO> loadAllReturnAccount(String nameOrLic, int page, int pageSize) throws DaoException {
        try {

            String sql = "SELECT o.id, o.out_business_id,o.in_business_id,o.create_time,o.in_status FROM tz_business_account_out o " +
                    " LEFT JOIN business_unit b ON b.id = o.out_business_id  WHERE 1 = 1 ";
            Query query = null;
            if(nameOrLic!=null&&nameOrLic!=""){
                sql  += " AND (b.name LIKE ?1 OR b.license_no LIKE ?2 ) ORDER BY o.in_status DESC,o.create_time DESC";
                query = entityManager.createNativeQuery(sql.toString());
                query.setParameter(1,"%"+ nameOrLic +"%").setParameter(2,"%"+ nameOrLic +"%");
            }else{
                sql += " ORDER BY o.in_status DESC,o.create_time DESC";
                query = entityManager.createNativeQuery(sql.toString());
            }
            if (page  > 0) {
                query.setFirstResult((page - 1) * pageSize);
                query.setMaxResults(pageSize);
            }
            List<Object[]> result = query.getResultList();
            return enclosureAccountVOs(result);
        }catch (Exception e){
            throw new DaoException("TZBusAccountOutDAOImpl.loadAllReturnAccount()加载所有退货单别信息，异常",e);
        }
    }

    private List<RetrunAccountVO> enclosureAccountVOs(List<Object[]> result) throws DaoException, ParseException {
        List<RetrunAccountVO> lists = null;
        if(result != null && result.size() > 0){
            lists = new ArrayList<RetrunAccountVO>();
            for(int i = 0 ; i < result.size(); i++){
                Object[] objs = result.get(i);
                RetrunAccountVO vo = new RetrunAccountVO();
                vo.setId(objs[0] != null ? Long.valueOf(objs[0].toString()) : -1);
                Object []outBusinfo = tZProductTrailDAO.getBusInfoById(objs[1] != null ? Long.valueOf(objs[1].toString()) : -1);
                if(outBusinfo!=null&&outBusinfo.length>0){
                    vo.setOutBusName(outBusinfo[1] != null ? outBusinfo[1].toString() : "");
                    vo.setOutBusLic(outBusinfo[2] != null ? outBusinfo[2].toString() : "");
                }
                Object []inBusinfo = tZProductTrailDAO.getBusInfoById(objs[2] != null ? Long.valueOf(objs[2].toString()) : -1);
                if(inBusinfo!=null&&inBusinfo.length>0){
                    vo.setInBusName(inBusinfo[1] != null ? inBusinfo[1].toString() : "");
                    vo.setInBusLic(inBusinfo[2] != null ? inBusinfo[2].toString() : "");
                }
                vo.setAccountDate(objs[3] != null ? SDFTIME.format(SDFTIME.parse(objs[3].toString())) : "");
                vo.setInStatus(objs[4] != null ? Integer.valueOf(objs[4].toString()) : null);
                lists.add(vo);
            }
        }
        return lists;
    }

    @Override
    public Long loadAllReturnAccountTotals(String nameOrLic) throws DaoException {
        String sql = "SELECT COUNT(*) FROM tz_business_account_out o " +
                " LEFT JOIN business_unit b ON b.id = o.out_business_id  WHERE 1 = 1 ";
        Object[] objs = null;
        if(nameOrLic != null && nameOrLic != ""){
            sql  += " AND (b.name LIKE ?1 OR b.license_no LIKE ?2 ) ";
            objs = new Object[]{"%"+ nameOrLic +"%","%" + nameOrLic + "%"};
        }
        try {
            return this.countBySql(sql.toString(),objs);
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(),jpae.getException());
        }
    }
}
