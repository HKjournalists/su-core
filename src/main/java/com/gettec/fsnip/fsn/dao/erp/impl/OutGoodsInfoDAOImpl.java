package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.OutGoodsInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.OutGoodsInfo;

@Repository("outOfGoodsDAO")
public class OutGoodsInfoDAOImpl extends BaseDAOImpl<OutGoodsInfo> implements
		OutGoodsInfoDAO {

	/**
	 * 根据出货单号查找商品列表信息
	 * @param no 出货单号
	 * @return
	 * @throws DaoException 
	 */
	public List<OutGoodsInfo> getListByNo(String no) throws DaoException {
		try {
			String sql = "SELECT * FROM t_meta_out_goods_info WHERE ID IN (" +
					"SELECT OUTGOODS_ID FROM t_meta_outbill_to_outgoods WHERE OUTOFBILL_NO = ?1)";
			return this.getListBySQL(OutGoodsInfo.class, sql, new Object[]{no});
		} catch (Exception e) {
			throw new DaoException("OutGoodsInfoDAOImpl.getListByNo() 根据出货单号查找商品列表信息，出现异常！", e);
		}
	}

	/**
	 * 根据出货单号查找商品列表信息(分页)
	 * @param no 出货单号
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<OutGoodsInfo> getListByNoPage(int page, int pageSize, String no) throws DaoException {
		try {
			String sql = "SELECT * FROM t_meta_out_goods_info WHERE ID IN (" +
					"SELECT OUTGOODS_ID FROM t_meta_outbill_to_outgoods WHERE OUTOFBILL_NO = ?1)" +
					"LIMIT " + (page-1)*pageSize + "," + pageSize;
			return this.getListBySQL(OutGoodsInfo.class, sql, new Object[]{no});
		} catch (Exception e) {
			throw new DaoException("OutGoodsInfoDAOImpl.getListByNoPage() 根据出货单号分页查找商品列表信息，出现异常！", e);
		}
	}

	/**
	 * 根据出货单号查找商品总数
	 * @param no 出货单号
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public Long countByNo(String no) throws DaoException {
		try {
			String sql = "SELECT count(*) FROM t_meta_out_goods_info WHERE ID IN (" +
					"SELECT OUTGOODS_ID FROM t_meta_outbill_to_outgoods WHERE OUTOFBILL_NO = ?1)";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, no);
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("OutGoodsInfoDAOImpl.countByNo() 根据出货单号查找商品总数，出现异常！", e);
		}
	}
}
