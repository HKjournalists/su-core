package com.gettec.fsnip.fsn.dao.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZStockInfoDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.account.TZStockInfo;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:chenxiaolin
 * 
 */
@Repository(value = "tZStockInfoDAO")
public class TZStockInfoDAOImpl extends BaseDAOImpl<TZStockInfo> implements TZStockInfoDAO {

    /**
     * 根据当前企业的org和用户输入的查询条件加载相应的库存信息
     * @author HY
     */
    @Override
    public List<ReturnProductVO> loadStoreInfoList(Long org, Integer page, Integer pageSize, String condition) throws DaoException {
        String sql = "SELECT tzs.id, p.name,p.barcode,p.format,tzs.qs_no,tzs.product_num FROM product p " +
                        " LEFT JOIN tz_stock tzs ON p.id = tzs.product_id " +
                        " LEFT JOIN business_unit b ON b.id = tzs.business_id " +
                        " WHERE b.organization = ?1 " + condition + " GROUP BY tzs.id ORDER BY tzs.id DESC " ;
        Query query = entityManager.createNativeQuery(sql).setParameter(1,org);
        if(page != null && page > 0){
            query.setFirstResult((page-1)*pageSize);
            query.setMaxResults(pageSize);
        }
        List<Object[]> result = query.getResultList();
        return result!=null && result.size() > 0 ? enclosureStockInfoList(result):null ;
    }

    private List<ReturnProductVO> enclosureStockInfoList(List<Object[]> result) {
        List<ReturnProductVO> lists = null;
        if(result!=null && result.size() > 0){
            lists = new ArrayList<ReturnProductVO>();
            for(int i = 0; i < result.size(); i++){
                Object[] objs = result.get(i);
                ReturnProductVO vo = new ReturnProductVO();
                vo.setId(objs[0] != null ? Long.valueOf(objs[0].toString()) : null);
                vo.setName(objs[1] != null ? objs[1].toString() : "");
                vo.setBarcode(objs[2] != null ? objs[2].toString() : "");
                vo.setFormat(objs[3] != null ? objs[3].toString() : "");
                vo.setQsNumber(objs[4] != null ? objs[4].toString() : "");
                vo.setCount(objs[5]!=null ? Long.valueOf(objs[5].toString()):-1);
                lists.add(vo);
            }
        }
        return lists;
    }

    /**
     * 根据当钱企业的org和用户输入的查询条件加载相应的库存信息总数
     * @author HY
     */
    @Override
    public Long loadStoreInfoListTotals(Long org, String condition) throws DaoException {
        String sql = "SELECT count(DISTINCT tzs.id) FROM product p " +
                " LEFT JOIN tz_stock tzs ON p.id = tzs.product_id " +
                " LEFT JOIN business_unit b ON b.id = tzs.business_id " +
                " WHERE b.organization = ?1 " + condition;
        try {
            return this.countBySql(sql,new Object[]{org});
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(),jpae.getException());
        }
    }

    /**
     * 根据库存ID 加载产品库存明细数据List
     * @author HY
     */
    @Override
    public List<ReturnProductVO> loadStoreDetailList(Integer page, Integer pageSize, Long sid) throws DaoException {
        String sql="SELECT p.name,p.barcode,s.QS_NO,pi.production_date ,s.product_batch,s.product_num,s.type,s.date,s.createType FROM tz_stock_info s " +
                    "RIGHT JOIN product p ON p.id = s.product_id " +
                    "LEFT JOIN product_instance pi ON pi.PRODUCT_ID = p.id " +
                    "WHERE s.stock_id = ?1 GROUP BY s.id ORDER BY s.id DESC ";
        try {
            Query query = entityManager.createNativeQuery(sql).setParameter(1,sid);
            if(page!=null && page>0){
                query.setFirstResult((page-1)*pageSize);
                query.setMaxResults(pageSize);
            }
            List<Object[]> result = query.getResultList();
            return result!=null && result.size()>0?enclosureStockDetailList(result):null;
        } catch (Exception e) {
            throw new DaoException("TZStockInfoDAOImpl.loadStoreDetailList()根据库存ID 加载产品库存明细数据List,异常！",e);
        }
    }

    private List<ReturnProductVO> enclosureStockDetailList(List<Object[]> result) throws ParseException {
        List<ReturnProductVO> lists = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(result!=null&&result.size()>0){
            lists = new ArrayList<ReturnProductVO>();
            for(int i=0;i<result.size();i++){
                Object[] objs = result.get(i);
                ReturnProductVO vo = new ReturnProductVO();
                vo.setName(objs[0] != null ? objs[0].toString() : "");
                vo.setBarcode(objs[1] != null ? objs[1].toString() : "");
                vo.setQsNumber(objs[2] != null ? objs[2].toString() : "");
                vo.setProductionDate(objs[3] != null ? objs[3].toString() : "");
                vo.setBatch(objs[4] != null ? objs[4].toString() : "");
                vo.setCount(objs[5] != null ? Long.valueOf(objs[5].toString()) : 0);
                vo.setType(objs[6] != null ? Integer.valueOf(objs[6].toString()):0);
                vo.setOverDate(objs[7]!=null?objs[7].toString():"");
                vo.setCreateType(objs[8]!=null?objs[8].toString():"");
                lists.add(vo);
            }
        }
        return lists;
    }

    /**
     * 根据库存ID 加载产品库存明细数据总数
     * @author HY
     */
    @Override
    public Long loadStoreDetailListTotals(Long sid) throws DaoException {
        String sql = "SELECT count(DISTINCT s.id ) FROM tz_stock_info s " +
                "RIGHT JOIN product p ON p.id = s.product_id " +
                "LEFT JOIN product_instance pi ON pi.PRODUCT_ID = p.id WHERE s.stock_id = ?1 ";
        try {
            return this.countBySql(sql,new Object[]{sid});
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(), jpae.getException());
        }
    }

	@Override
	public List<TZStockInfo> getTZStockInfoList(long stockId, Long inBusId)
			throws DaoException {
		String condition = " WHERE e.businessId =?1 AND e.stockId = ?2 ";
		try {
			return this.getListByCondition(condition, new Object[]{inBusId, stockId});
		} catch (JPAException jpae) {
			throw new DaoException("TZStockDAOImpl.getTZStockListByCurBusAndProId() 获取库存商品详情列表,出现异常！", jpae);
		}
	}

    @Override
    public Long getStockCountByStockId(Long stockId,String batch) throws DaoException {

        String sql = " SELECT sum(sinfo.product_num) from tz_stock_info sinfo " +
                    " WHERE sinfo.stock_id = ?1 AND sinfo.intake = 1 AND sinfo.product_batch = ?2 ";

        try {
            if(stockId!=-1 && !(Long.valueOf("-1")).equals(stockId) ){
                return this.countBySql(sql,new Object[]{stockId,batch});
            }else{
                return 0L;
            }

        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(),jpae.getException());
        }
    }

	@Override
	public void deleteListByStockId(Long stockId) throws DaoException {
		  String sql = " delete from tz_stock_info where account_id = ?1 ";
      try {
    	  Query query = entityManager.createNativeQuery(sql).setParameter(1, stockId);
    	  query.executeUpdate();
      } catch (Exception jpae) {
          throw new DaoException(jpae.getMessage(),jpae);
      }
		
	}
}
