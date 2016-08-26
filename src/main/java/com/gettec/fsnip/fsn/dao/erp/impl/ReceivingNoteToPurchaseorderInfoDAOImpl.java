package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.ReceivingNoteToPurchaseorderInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNoteToContactinfo;

@Repository("receivingNoteToPurchaseorderInfoDAO")
public class ReceivingNoteToPurchaseorderInfoDAOImpl extends BaseDAOImpl<ReceivingNoteToContactinfo> 
		implements ReceivingNoteToPurchaseorderInfoDAO{

	/**
	 * 根据收货单号获取商品关联信息
	 * @param no
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<ReceivingNoteToContactinfo> getListByNo(String no) throws DaoException {
		try {
			String condition = " WHERE e.id.receivenote_no = ?1";
			return this.getListByCondition(condition, new Object[]{no});
		} catch (JPAException jpae) {
			throw new DaoException("ReceivingNoteToPurchaseorderInfoDAOImpl.getListByNo() ", jpae.getException());
		}
	}
}
