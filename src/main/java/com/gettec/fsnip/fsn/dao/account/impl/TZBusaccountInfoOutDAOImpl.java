package com.gettec.fsnip.fsn.dao.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZBusaccountInfoOutDAO;
import com.gettec.fsnip.fsn.dao.account.TZStockDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.account.TZBusAccountOut;
import com.gettec.fsnip.fsn.model.account.TZBusaccountInfoOut;
import com.gettec.fsnip.fsn.model.account.TZStock;
import com.gettec.fsnip.fsn.service.account.TZBusAccountOutService;
import com.gettec.fsnip.fsn.vo.account.ProductionDateVO;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HY on 2015/5/19.
 * desc:
 */
@Repository(value = "tzBusaccountInfoOutDAO")
public class TZBusaccountInfoOutDAOImpl extends
        BaseDAOImpl<TZBusaccountInfoOut> implements
        TZBusaccountInfoOutDAO {

    @Autowired
    TZBusAccountOutService tzBusAccountOutService;
    @Autowired
    TZStockDAO tzStockDAO;
    /**
     * 根据退货代号初始化确认收到退货信息中的数据
     * Create Author HY Date 2015-05-17
     */
    @Override
    public List<ReturnProductVO> loadTZReturnInProductByOutId(Long outId, int page, int pageSize) throws DaoException {
        String sql = "SELECT temp.* FROM  (SELECT p.id pid,p.name pname,p.barcode pbarcode,p.format pformat, " +
                " iout.id outId,iout.qs_no,iout.product_num,iout.product_price, " +
                " iout.production_date,iout.product_batch,iout.over_date,iout.problem_describe "+
                " FROM tz_business_account_info_out iout LEFT JOIN product p ON p.id = iout.product_id  " +
                " WHERE iout.business_account_id = ?1 ) temp ";
        try {
            Query query = entityManager.createNativeQuery(sql).setParameter(1, outId);
            if(page > 0){
                query.setFirstResult((page-1)*pageSize);
                query.setMaxResults(pageSize);
            }
            List<Object[]> result = query.getResultList();
            return encapsulationResultToReturnProductVO(result,outId);
        }catch (Exception e){
            throw new DaoException("TZBusinessRelationDAOImpl.loadTZReturnProductByAccountNo()根据订单编号加载可以退货的产品,出现异常！",e);
        }
    }

    /**
     * 根据退货单号加载退货的产品信息
     */
    @Override
    public List<TZBusaccountInfoOut> findListByOutId(Long id) throws DaoException {
        String condition = " WHERE e.busAccountId = ?1 ";
        try {
            return this.getListByCondition(condition,new Object[]{id});
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(),jpae.getException());
        }
    }

    /**
     * 封装可以退货的产品
     * @author HY
     */
    private List<ReturnProductVO> encapsulationResultToReturnProductVO(
            List<Object[]> result,Long outId) throws Exception {
        List<ReturnProductVO> lists = null;
        if(result!=null&&result.size()>0) {
            lists = new ArrayList<ReturnProductVO>();
            for(int i = 0; i < result.size(); i++) {
                Object[] objs = result.get(i);
                ReturnProductVO vo = new ReturnProductVO();
                vo.setProductId(objs[0] != null ? Long.valueOf(objs[0].toString()) : -1);
                vo.setName(objs[1] != null ? objs[1].toString() : "");
                vo.setBarcode(objs[2] != null ? objs[2].toString() : "");
                vo.setFormat(objs[3] != null ? objs[3].toString() : "");
                vo.setId(objs[4] != null && !"".equals(objs[4].toString()) ? Long.valueOf(objs[4].toString()) : -1);
                vo.setQsNumber(objs[5] != null ? objs[5].toString() : "");
                vo.setPrice(new BigDecimal(objs[7] != null && !"".equals(objs[7].toString()) ? objs[7].toString() : 0 + ""));
                vo.setProductionDate(objs[8] != null ? objs[8].toString().substring(0,10) :"");
                vo.setBatch(objs[9] != null ? objs[9].toString() :"");
                vo.setOverDate(objs[10] != null ? objs[10].toString() :"");
                vo.setProblem_describe(objs[11] != null ? objs[11].toString() :"");
                if(outId!=null){
                    vo.setReturnCount(objs[6] != null ? Long.valueOf(objs[6].toString()) : 0);
                    TZBusAccountOut accountOut = tzBusAccountOutService.findById(outId);
                    if(accountOut!=null){
                        TZStock tzStock = tzStockDAO.getByProIdAndSelfBusId(vo.getProductId(),accountOut.getInBusId(),vo.getQsNumber());
                        if(tzStock!=null){
                            vo.setCount(tzStock.getProductNum());
                        }
                    }else{vo.setCount(Long.valueOf(0));}
                }
                lists.add(vo);
            }
        }
        return lists;
    }
}
