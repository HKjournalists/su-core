package com.gettec.fsnip.fsn.dao.erp;

import java.math.BigInteger;
import java.util.List;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.OutBillToOutGoods;

public interface OutBillToOutGoodsDAO extends BaseDAO<OutBillToOutGoods> {

	List<OutBillToOutGoods> getRelationShipByNo(String no) throws DaoException;
	//根据出货单号获取对应的商品id
	public List<BigInteger> getIdbyOutToOG(String num);
}
