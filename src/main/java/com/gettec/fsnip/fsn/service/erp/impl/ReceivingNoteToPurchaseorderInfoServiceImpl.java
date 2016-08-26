package com.gettec.fsnip.fsn.service.erp.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gettec.fsnip.fsn.dao.erp.ReceivingNoteToPurchaseorderInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNoteToContactinfo;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNoteToContactinfoPK;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.ReceivingNoteToPurchaseorderInfoService;

@Service("receivingNoteToPurchaseorderInfoService")
public class ReceivingNoteToPurchaseorderInfoServiceImpl 
		extends BaseServiceImpl<ReceivingNoteToContactinfo, ReceivingNoteToPurchaseorderInfoDAO>
		implements ReceivingNoteToPurchaseorderInfoService{
	@Autowired private ReceivingNoteToPurchaseorderInfoDAO receivingNoteToPurchaseorderInfoDAO;

	/**
	 * 根据收货单号获取商品关联信息
	 * @param no
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public List<ReceivingNoteToContactinfo> getListByNo(String no) throws ServiceException {
		try {
			return receivingNoteToPurchaseorderInfoDAO.getListByNo(no);
		} catch (DaoException dex) {
			throw new ServiceException("ReceivingNoteToPurchaseorderInfoServiceImpl.getListByNo()-->", dex.getException());
		}
	}

	/**
	 * 根据收货单号和商品id获取一条商品关联信息
	 * @param pk
	 * @return 
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public ReceivingNoteToContactinfo findByPk(ReceivingNoteToContactinfoPK pk) throws ServiceException {
		try {
			return getDAO().findById(pk);
		} catch (JPAException jpae) {
			throw new ServiceException("ReceivingNoteToPurchaseorderInfoServiceImpl.findByPk()-->", jpae.getException());
		}
	}
	
	@Override
	public ReceivingNoteToPurchaseorderInfoDAO getDAO() {
		return receivingNoteToPurchaseorderInfoDAO;
	}
}
