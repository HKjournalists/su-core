package com.gettec.fsnip.fsn.dao.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZProductTrailDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.account.TZProductTrail;
import com.gettec.fsnip.fsn.vo.account.TZOriginGoodsInfoVO;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HY on 2015/5/19.
 * desc:
 */
@Repository(value = "tZProductTrailDAO")
public class TZProductTrailDAOImpl extends BaseDAOImpl<TZProductTrail> implements TZProductTrailDAO {

    /**
     * 溯源列表，加载产品详情
     * Created By HY On 2015-06-04
     */
    @Override
    public List<TZOriginGoodsInfoVO> loadAllGoodsList(int page, int pageSize, String condition,String cond) throws DaoException {
        String sql = "SELECT * FROM " +
                "(SELECT  p.id pid,p.`name` pname, p.barcode pbar, qs.qs_no qsno,bb.`name` bbname," +
                " p.format format, b.address baddr,b.id bid FROM  product p " +
                " LEFT JOIN product_to_businessunit ptb ON p.id = ptb.PRODUCT_ID " +
                " LEFT JOIN production_license_info qs ON qs.id = ptb.qs_id " +
                " LEFT JOIN business_unit b ON ptb.business_id = b.id " +
                " LEFT JOIN business_brand bb ON b.id = bb.business_unit_id " +
                " WHERE p.id IS NOT NULL AND p.name IS NOT NULL  AND b.id IS NOT NULL AND b.name IS NOT NULL" +
                condition+" GROUP BY p.id,qs.qs_no ) temp ";
        Query query = entityManager.createNativeQuery(sql.toString());
        if(cond != null && !"".equals(cond)){
            query.setParameter(1,cond);
            query.setParameter(2,"%"+cond+"%");
        }
        if(page > 0){
            query.setFirstResult((page-1)*pageSize);
            query.setMaxResults(pageSize);
        }
        List<Object[]> result = query.getResultList();
        return result!=null && result.size() > 0? setTZOriginGoodsInfoList(result):null;
    }

    /**
     * 溯源列表，加载产品详情 总数
     * Created By HY On 2015-06-04
     */
    @Override
    public Long loadAllGoodsListTotals(String condition,String cond) throws DaoException {
        String sql ="SELECT count(*) from (SELECT  COUNT(p.id) FROM  product p "+
                " LEFT JOIN product_to_businessunit ptb ON p.id = ptb.PRODUCT_ID "+
                " LEFT JOIN production_license_info qs ON qs.id = ptb.qs_id "+
                " LEFT JOIN business_unit b ON ptb.business_id = b.id "+
                " LEFT JOIN business_brand bb ON b.id = bb.business_unit_id "+
                " WHERE p.id IS NOT NULL AND p.name IS NOT NULL "+
                " AND b.id IS NOT NULL  AND b.name IS NOT NULL "+condition+" GROUP BY p.id,qs.qs_no) temp ";
        try {
            Object[] objs = null;
            if (cond != null && !"".equals(cond)) {
                objs = new Object[]{cond, "%" + cond + "%"};
            }
            return this.countBySql(sql.toString(), objs);
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(), jpae.getException());
        }
    }

    /**
     * 根据产品id qs号和台账类型加载台账溯源的根目录
     * Create By HY On 2015-06-05
     */
    @Override
    public TZProductTrail loadGoodsOrigin(String barcode, int type,String batch) throws DaoException {
        String condition = " WHERE e.barcode = ?1 AND e.type = ?2";
        Object []objs = new Object[]{barcode,type};
        if(batch!=null && !"".equals(batch)){
            condition = condition+" AND e.productBatch = ?3 ";
            objs = new Object[]{barcode,type,batch};
        }
        condition = condition+" GROUP BY e.outBusId ORDER BY e.id ASC LIMIT 1 ";
        try {
            List<TZProductTrail> result = this.getListByCondition(condition,objs);
            return result!=null&&result.size()>0?result.get(0):null;
        } catch (JPAException jpae) {
            throw new DaoException("TZProductTrailDAOImpl.loadGoodsOrigin()加载台账溯源的根目录,异常!",jpae.getException());
        }
    }

    /**
     * 根据企业id 加载部分企业信息
     */
    @Override
    public Object[] getBusInfoById(Long busId) throws DaoException {
        try{
            String sql = "SELECT b.id,b.name,b.license_no FROM business_unit b WHERE b.id = ?1 ";
            Query query = entityManager.createNativeQuery(sql.toString()).setParameter(1,busId);
            Object[] result = (Object[]) query.getSingleResult();
            return result;
        }catch (Exception e){
            throw new DaoException("getBusInfoById台账溯源根据企业id 加载部分企业信息",e);
        }
    }

    /**
     * 根据产品id，企业id,qs号 加载轨迹
     */
    @Override
    public List<TZProductTrail> getOriginChildren(Long proId, String qs, Long outBusId) throws DaoException {
        String condition = " WHERE e.productId = ?1 " +
                " AND e.qsNo = ?2 AND e.outBusId = ?3 ";
        try {
            return this.getListByCondition(condition,new Object[]{proId,qs,outBusId});
        } catch (JPAException jpae) {
            throw new DaoException("根据产品id，企业id,qs号 加载轨迹"+jpae.getMessage(),jpae.getException());
        }
    }

    /*
    * 查找该产品的起点轨迹
    * @auhtor HY
    * */
    @Override
    public List<TZProductTrail> getOriginChildrens(Long outBusId,Long trialId, String barcode,String batch) throws DaoException {
        String condition =  " WHERE e.outBusId = ?1 AND e.barcode = ?2 AND e.type = 0 AND e.id > ?3";
        Object []objs = new Object[]{outBusId,barcode,trialId};
        if(batch!=null&&!"".equals(batch)&&!" ".equals(batch)){
            condition = condition + " AND e.productBatch = ?4 ";
            objs = new Object[]{outBusId,barcode,trialId,batch};
        }
       condition = condition + " GROUP BY e.productBatch,e.inBusId ";
        try {
            return this.getListByCondition(condition,objs);
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(),jpae.getException());
        }
    }

    /**
     * 根据产品id查找生产企业id
     * Author HY
     */
    @Override
    public String findByproductId(Long productId) throws DaoException {
        String sql = "SELECT b.name FROM business_unit b RIGHT JOIN product p  ON b.id = p.producer_id " +
                "WHERE p.id= ?1";
        Query query = entityManager.createNativeQuery(sql).setParameter(1,productId);
        Object result = query.getSingleResult();
        return result!=null ?result.toString():"";
    }

    @Override
    public List<TZProductTrail> getOriginChildrensByBatch(Long outBusId, String barcode,
                                                          String productBatch,Long tid) throws DaoException {
        String condition =  " WHERE e.outBusId = ?1 AND e.barcode = ?2 AND e.type = 0 " +
                        " AND e.productBatch = ?3 AND e.id > ?4 GROUP BY e.inBusId ";
        try {
            return this.getListByCondition(condition,new Object[]{outBusId,barcode,productBatch,tid});
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(),jpae.getException());
        }
    }

    @Override
    public Long getProductNum(String barcode, Long outBusId, String productBatch,Long inId) throws DaoException {
        String sql = "SELECT SUM(product_num) FROM tz_product_trail WHERE bar_code = ?1 " +
                    " AND out_business_id = ?2 AND product_batch =?3 AND in_business_id = ?4 ";
        try {
            Long sum = this.countBySql(sql,new Object[]{barcode,outBusId,productBatch,inId});
            return sum;
        } catch (JPAException jpae) {
            throw new DaoException(jpae.getMessage(),jpae.getException());
        }
    }

    /**
     * 封装商品溯源中的产品详情信息
     * Created By HY On 2015-06-04
     */
    private List<TZOriginGoodsInfoVO> setTZOriginGoodsInfoList(List<Object[]> result) {
        List<TZOriginGoodsInfoVO> lists = new ArrayList<TZOriginGoodsInfoVO>();
        for(int i = 0; i < result.size(); i++){
            Object[] objs = result.get(i);
            TZOriginGoodsInfoVO vo = new TZOriginGoodsInfoVO();
            vo.setId(Long.valueOf(i+1));
            vo.setProductId(objs[0] != null ? Long.valueOf(objs[0].toString()) : -1);
            vo.setProductName(objs[1] != null ? objs[1].toString() : "");
            vo.setBarcode(objs[2] != null ? objs[2].toString() : "");
            vo.setQsNo(objs[3] != null ? objs[3].toString() : "");
            vo.setProductBand(objs[4] != null ? objs[4].toString() : "");
            vo.setFormat(objs[5] != null ? objs[5].toString() : "");
            vo.setPlace(objs[6] != null ? objs[6].toString() : "");
            vo.setBusId(objs[7] != null ? Long.valueOf(objs[7].toString()) : -1);
            lists.add(vo);
        }
        return lists;
    }

	@Override
	public void deleteTrainByTzId(long tzId, Long busId) throws DaoException {
		String sql = "delete from tz_product_trail WHERE account_id = ?1 and in_business_id = ?2 ";
		try {
			Query query = entityManager.createNativeQuery(sql).setParameter(1, tzId).setParameter(2, busId);
			query.executeUpdate();
		} catch (Exception jpae) {
			throw new DaoException(jpae.getMessage(), jpae);
		}

	}
}
