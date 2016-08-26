package com.gettec.fsnip.fsn.service.erp.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.OutBillToOutGoodsDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.OutBillToOutGoods;
import com.gettec.fsnip.fsn.model.erp.base.OutBillToOutGoodsPK;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.OutBillToOutGoodsService;

@Service("outBillToOutGoodsService")
public class OutBillToOutGoodsServiceImpl extends BaseServiceImpl<OutBillToOutGoods, OutBillToOutGoodsDAO>
		implements OutBillToOutGoodsService {
	@Autowired private OutBillToOutGoodsDAO outBillToOutGoodsDAO;
	
	/**
	 * 新增出货单和商品的关系
	 * @param infos
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<OutBillToOutGoods> addRelationShips(List<OutBillToOutGoods> infos) {
		try {
			for(OutBillToOutGoods info : infos){
				if(null != info.getId() && info.getId().getOutGoodsId() != null &&
						info.getId().getOutOfBillNo() != null){
					outBillToOutGoodsDAO.persistent(info);
				}
			}
		} catch (Exception e) {
			
		}
		return infos;
	}

	/**
	 * 根据出货单号查找上关联的商品信息
	 * @param no
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<OutBillToOutGoods> getRelationShipByNo(String no) throws ServiceException {
		try {
			return outBillToOutGoodsDAO.getRelationShipByNo(no);
		} catch (DaoException dex) {
			throw new ServiceException("OutBillToOutGoodsServiceImpl.getRelationShipByNo()-->", dex.getException());
		}
	}

	/**
	 * 根据联合主键（出货单号、商品id）查找出货单关联表
	 * @param OutBillToOutGoodsPK
	 * @returnOutBillToOutGoods
	 * @throws ServiceException 
	 */
	@Override
	public OutBillToOutGoods findByPk(OutBillToOutGoodsPK pk) throws ServiceException {
		try{
			return outBillToOutGoodsDAO.findById(pk);
		}catch(JPAException jpae){
			throw new ServiceException("OutBillToOutGoodsServiceImpl-->findByPk"+jpae.getMessage(),jpae);
		}
	}
	
	@Override
	public OutBillToOutGoodsDAO getDAO() {
		return outBillToOutGoodsDAO;
	}
}
