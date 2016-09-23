package com.gettec.fsnip.fsn.dao.market.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.market.StoreProductDao;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.market.StoreProduct;
import com.gettec.fsnip.fsn.vo.StoreProductVO;

/**
 * Create Time 2015-03-31
 * @author zhaWanNeng
 */
@Repository(value="StoreProductDAO")
public class StoreProductDAOImpl extends BaseDAOImpl<StoreProduct> implements StoreProductDao {
    
	/**
	 * 根据产品id和用户id判断产品是否被此用户搜藏
	 * @param productId 产品id
	 * @param userId  用户id
	 * @author ZhaWanNeng
	 */
	@SuppressWarnings("unchecked")
	public Long findStorebyUser4ProductId(Long productId,Long userId)throws DaoException {
		try {
            String sql = "select e.id from store_product e where e.product_id=?1 and e.userId =?2";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, productId);
            query.setParameter(2, userId);
           
            List<Object> list = query.getResultList();
            List<Long> Id=new ArrayList<Long>();
            if(list.size()>0){
            	for (Object objects : list) {
            		Id.add(Long.valueOf(objects.toString()));
            	}
            	return Id.get(0);
            }else {
            	return 0l;
			}
		} catch (Exception e) {
	            throw new DaoException("StoreProductImpl.findStorebyUser4ProductId()-->"+"dao层按条件查询检测报告出错", e);
	    }
	
	}
	/**
	 * 高级查询我的搜藏
	 * @param size 每页的条数
	 * @param page 第几页
	 * @param condition 条件
	 * @return
	 * @throws DaoException
	 * @author ZhaWanNeng
	 */
	@SuppressWarnings("unchecked")
	public List<StoreProductVO> findStoreProduc(int size,int page,StringBuilder condition)throws DaoException {
		try {
			String sql = "SELECT DISTINCT sp.product_id,sp.productImg,sp.productName,max(tr.test_date),pro.riskIndex,pro.nutri_label,bus.name as businessUnitName FROM store_product sp  " +
					"LEFT JOIN product_instance  pi ON  pi.product_id = sp.product_id " +
					"LEFT JOIN product pro ON   pro.id = sp.product_id " +
					"LEFT JOIN business_unit bus on bus.id=pro.producer_id "+
					"LEFT JOIN test_result tr  ON   tr.sample_id = pi.id AND tr.del = 0 " + condition+
					" GROUP BY sp.id order by sp.id desc ";
			Query query = entityManager.createNativeQuery(sql);
			query.setFirstResult((page - 1)*size);
			query.setMaxResults(size);
			List<Object[]> result = query.getResultList();
			List<StoreProductVO> categorys = new ArrayList<StoreProductVO>();
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(result.size() <= 0){
				return categorys;
			}
			for (Object[] obj : result) {
					Long productid = Long.valueOf(obj[0].toString());
					String productImg = obj[1] !=null ? obj[1].toString() : "";
					String productName = obj[2] !=null ? obj[2].toString() : "";
					Date updateDate = null;
					if(obj[3]!=null){
						updateDate = sdf.parse(obj[3].toString());
					}
					Double riskIndex = Double.valueOf(obj[4] !=null ? obj[4].toString() : "-1.0");
					String nutriLabel = obj[5] !=null ? obj[5].toString() : "";
					int reportNum = reportNum(productid);
					StoreProductVO storeProductVO = new StoreProductVO(productid, productImg, productName, riskIndex, nutriLabel, reportNum, updateDate,obj[6].toString());
					categorys.add(storeProductVO);
			}
			return categorys;
		} catch (Exception e) {
			throw new DaoException("StoreProductImpl.findStoreProduc()-->"+"dao层高级查询我的搜藏出错", e);
		}
		
	}
	/**
	 * 高级查询我的收藏数量
	* @param size 每页的条数
	 * @param page 第几页
	 * @param condition 条件
	 * @return
	 * @throws DaoException
	 * @author ZhaWanNeng
	 */
	
	@SuppressWarnings("unchecked")
	public int StoreCount(int size,int page,StringBuilder condition)throws DaoException {
		try {
			String sql = "SELECT DISTINCT sp.product_id FROM store_product sp  " +
					"LEFT JOIN product_instance  pi ON  pi.product_id = sp.product_id " +
					"LEFT JOIN product pro ON   pro.id = sp.product_id " +
					"LEFT JOIN test_result tr  ON   tr.sample_id = pi.id  AND tr.del = 0 " + condition+
					" GROUP BY sp.id  ";
			Query query = entityManager.createNativeQuery(sql);
			List<Object> result = query.getResultList();
			if(result.size() <= 0){
				return 0;
			}
			return result.size();
		} catch (Exception e) {
			throw new DaoException("StoreProductImpl.StoreCount()-->"+"dao层按条件高级查询我的收藏数量出错", e);
		}
		
	}
	/**
	 * 报告数量
	 * @param size 每页的条数
	 * @param page 第几页
	 * @param condition 条件
	 * @return
	 * @throws DaoException
	 * @author ZhaWanNeng
	 */
	
	@SuppressWarnings("unchecked")
	public int reportNum(Long productid)throws DaoException {
		try {
			
			String sql = "SELECT DISTINCT COUNT(tr.id) FROM test_result tr  " +
						 "LEFT JOIN product_instance  pi ON tr.sample_id = pi.id  " +
						 "LEFT JOIN product pro ON pi.product_id = pro.id  " +
						 "WHERE pro.id=?1 and tr.publish_flag = 1 AND tr.del = 0";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, productid);
			List<Object> result = query.getResultList();
			if(result.size() <= 0){
				return 0;
			}
			return Integer.valueOf(result.get(0).toString());
		} catch (Exception e) {
			throw new DaoException("StoreProductImpl.StoreCount()-->"+"dao层按条件查询报告数量出错", e);
		}
		
	}
	/**
	 * 批量取消收藏产品接口
	 * @author LongXianZhen 2015/06/18
	 */
	@Override
	public void batchDelete(String proIds, Long userId) throws DaoException {
		try {
			String sql = "DELETE FROM store_product WHERE store_product.product_id IN("+proIds+") AND store_product.userId=?2";
			Query query = entityManager.createNativeQuery(sql);
			//query.setParameter(1, proIds);
			query.setParameter(2, userId);
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException("StoreProductImpl.batchDelete()-->"+"dao层批量删除收藏产品出错", e);
		}
	}
}
