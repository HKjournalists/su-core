package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.PurchaseorderInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.base.PurchaseorderInfo;

@Repository("purchaseorderInfoDAO")
public class PurchaseorderInfoDAOImpl extends BaseDAOImpl<PurchaseorderInfo> implements PurchaseorderInfoDAO {

	/**
	 * 根据收获单号查找商品列表
	 * @param no
	 * @return
	 */
	@Override
	public List<PurchaseorderInfo> getListByNo(String no) throws DaoException {
		try {
			String sql = "SELECT * FROM t_meta_purchaseorder_info WHERE po_id IN (" +
					 "SELECT contact_id FROM t_meta_receivingnote_to_contact WHERE receivenote_no = ?1)";		
			return this.getListBySQL(PurchaseorderInfo.class, sql, new Object[]{no});
		} catch (JPAException jpae) {
			throw new DaoException("PurchaseorderInfoDAOImpl.getListByNo() 根据收获单号查找商品列表,出现异常！", jpae.getException());
		}
	}

	/**
	 * 根据收获单号查找商品列表(分页)
	 * @param no
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<PurchaseorderInfo> getListByNoPage(String no, int page,
			int pageSize) throws DaoException {
		if(no == null) {return null;}
		try {
			String sql = "SELECT * FROM t_meta_purchaseorder_info WHERE po_id IN (" +
					 "SELECT contact_id FROM t_meta_receivingnote_to_contact WHERE receivenote_no = ?1) " +
					 "LIMIT " + (page-1)*pageSize + "," + pageSize;		
			return this.getListBySQL(PurchaseorderInfo.class, sql, new Object[]{no});
		} catch (JPAException jpae) {
			throw new DaoException("PurchaseorderInfoDAOImpl.getListByNoPage() 根据收获单号查找商品列表(分页),出现异常！", jpae.getException());
		}
	}

	/**
	 * 根据收获单号查找商品总数
	 * @param no
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public long countByNo(String no) throws DaoException {
		if(no == null){return 0L;};
		try {
			String sql = "SELECT count(*) FROM t_meta_purchaseorder_info WHERE po_id IN (" +
					 "SELECT contact_id FROM t_meta_receivingnote_to_contact WHERE receivenote_no = ?1)";		
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, no);
			Object rtn = query.getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (Exception e) {
			throw new DaoException("PurchaseorderInfoDAOImpl.countByNo() 根据收获单号查找商品总数, 出现异常！", e);
		}
	}

	//查询 erp中的对应产品实例
	@Override
	public List<PurchaseorderInfo> getErpProductInstanceByProductId(int page,
			int pageSize, Long proId, String batch,String startTime,String endTime) throws DaoException {
		if(page == 0) {return null;}
		try {
			String condition = "";
			if(!batch.equals("")){
				condition =" AND sample.po_batch LIKE "+ "'%"+batch+"%'";
				
			}
			if(!startTime.equals("")&&!endTime.equals("")){
				condition = "AND t.re_date>= '"+startTime+"' AND t.re_date<= '"+endTime+"'";
				
			}
			String limit = " LIMIT "+(page-1)*pageSize+","+pageSize; 
			String sql = " SELECT sample.* FROM t_meta_purchaseorder_info sample,product pro ,t_meta_receivingnote t ," +
	        		"t_meta_receivingnote_to_contact t1 WHERE pro.id = ?1 AND pro.barcode = sample.po_product_info_id  " +
	        		"AND t1.receivenote_no=t.re_num AND sample.po_id=t1.contact_id  "+condition+
	            	" GROUP BY sample.po_product_info_id, sample.po_batch " + limit;  		
			return this.getListBySQL(PurchaseorderInfo.class, sql, new Object[]{proId});
		} catch (JPAException jpae) {
			throw new DaoException("PurchaseorderInfoDAOImpl.getErpProductInstanceByProductId() 查询 erp中的对应产品实例！", jpae.getException());
		}
	}

	//查询 erp中的对应产品实例 总数
	@Override
	public long countGetErpProductInstance(Long proId, String batch,String startTime,String endTime)throws DaoException {
		try {
			String condition = "";
			if(!batch.equals("")){
				condition =" AND sample.po_batch LIKE "+ "'%"+batch+"%'";
			}
			if(!startTime.equals("")&&!endTime.equals("")){
				condition = "AND t.re_date>= '"+startTime+"' AND t.re_date<= '"+endTime+"'";
			}
			String sql = "SELECT count(DISTINCT sample.po_product_info_id , sample.po_batch) " +
            		" FROM t_meta_purchaseorder_info sample,product pro ,t_meta_receivingnote t ," +
            		" t_meta_receivingnote_to_contact t1 WHERE pro.id = ?1 AND pro.barcode = sample.po_product_info_id " +
            		" AND t1.receivenote_no=t.re_num AND sample.po_id=t1.contact_id "+ condition ; 	
			return this.countBySql(sql, new Object[]{proId});
		} catch (JPAException jpae) {
			throw new DaoException("PurchaseorderInfoDAOImpl.countGetErpProductInstance() 查询 erp中的对应产品实例 总数！", jpae.getException());
		}
	}
	
}
