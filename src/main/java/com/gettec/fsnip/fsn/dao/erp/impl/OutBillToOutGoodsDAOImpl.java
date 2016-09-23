package com.gettec.fsnip.fsn.dao.erp.impl;

import java.math.BigInteger;
import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.OutBillToOutGoodsDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.base.OutBillToOutGoods;

@Repository("outBillToOutGoodsDAO")
public class OutBillToOutGoodsDAOImpl extends BaseDAOImpl<OutBillToOutGoods>
		implements OutBillToOutGoodsDAO {

	/**
	 * 根据出货单号查找上关联的商品信息
	 * @param no
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<OutBillToOutGoods> getRelationShipByNo(String no) throws DaoException {
		try {
			String condition = " WHERE e.id.outOfBillNo = ?1";
			return this.getListByCondition(condition, new Object[]{no});
		} catch (JPAException jpae) {
			throw new DaoException("OutBillToOutGoodsDAOImpl.getRelationShipByNo() 根据出货单号查找上关联的商品信息，出现异常！", jpae.getException());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BigInteger> getIdbyOutToOG(String num) {
		String sql = "SELECT outgoods_id FROM t_meta_outbill_to_outgoods where OUTOFBILL_NO =:num";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("num", num);
		List<BigInteger> o_id = query.getResultList();
		return o_id;
	}
}
