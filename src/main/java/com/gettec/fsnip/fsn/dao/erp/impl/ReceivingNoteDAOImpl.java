package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.List;

import javax.persistence.Query;
import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.ReceivingNoteDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote;

@Repository("receivingNoteDAO")
public class ReceivingNoteDAOImpl extends BaseDAOImpl<ReceivingNote> implements ReceivingNoteDAO{

	/**
	 * 根据收货单号，删除商品的关联
	 * @param no
	 * @throws DaoException 
	 */
	@Override
	public void removeByNo(String no) throws DaoException {
		try {
			String sql = "DELETE FROM t_meta_receivingnote_to_contact where receivenote_no =?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, no);
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException("ReceivingNoteDAOImpl.removeByNo() 根据收货单号，删除商品的关联,出现异常！", e);
		}
	}

	@Override
	public boolean checkReceivingnote(String Re_num) {
		boolean flag = false;
		String sql = "UPDATE t_meta_receivingnote SET re_purchase_check = :re_purchase_check WHERE re_num = :re_num";
		try {
			entityManager.createNativeQuery(sql)
				.setParameter("re_purchase_check", "已收货")
				.setParameter("re_num", Re_num)
				.executeUpdate();
			flag = true;
		} catch(Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 获取收货单已有的最大编号
	 * @param noStart
	 * @return
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findNoMaxByNoStart(String noStart) throws DaoException {
		try {
			/* 按降序排序，获取已存在最大编号 */
			String sql = "SELECT re_num FROM t_meta_receivingnote where re_num like ?1 ORDER BY re_num DESC LIMIT 1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, noStart + "%");
			List<Object> result = query.getResultList();
			if(result != null&&result.size()>0){
				return result.get(0).toString();
			}
			return null;
		} catch (Exception e) {
			throw new DaoException("ReceivingNoteDAOImpl.findNoMaxByNoStart() 获取收货单已有的最大编号，出现异常！", e);
		}
	}
	/**
	 * 校验仓库是否使用
	 * @param name
	 * @param organization
	 */
	@Override
	public long countByStorageUsed(String name, Long organization)
			throws DaoException {
		try{
			String sql="SELECT COUNT(*) FROM t_meta_purchaseorder_info t "+
						"INNER JOIN t_meta_receivingnote_to_contact t1 ON t1.contact_id=t.po_id "+
						"INNER JOIN t_meta_receivingnote t2 ON t2.re_num=t1.receivenote_no "+
						"WHERE t2.organization=?1 AND t.po_storage_address=?2 ";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1,organization);				
			query.setParameter(2,name);
			Object result = query.getSingleResult();
			return result == null ? 0 : Long.parseLong(result.toString());
		} catch (Exception e) {
			throw new DaoException("ReceivingNoteDAOImpl.countByStorageUsed() 获取收货单使用仓库数量！", e);
		}
	}
}
