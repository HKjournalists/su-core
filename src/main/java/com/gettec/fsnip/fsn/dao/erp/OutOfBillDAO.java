package com.gettec.fsnip.fsn.dao.erp;

import java.math.BigInteger;
import java.util.List;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.OutOfBill;

public interface OutOfBillDAO extends BaseDAO<OutOfBill> {
	
	/*
	 * author:cgw
	 * @parameter num 供货商（机构代码）,客户no是否是自己
	 * describe 获取单号*/
	public List<String> getOurOfBillByProviderNum(Long organization,Long cus_no);
	
	public boolean checkOne(String outOfBillNo);
	
	public boolean checkTwo(String outOfBillNo);
	
	//根据出货单号查询联系人id
	public List<BigInteger> getCidByOutOrder(String num);
	
	String findNoMaxByNoStart(String noStart) throws DaoException;
}
