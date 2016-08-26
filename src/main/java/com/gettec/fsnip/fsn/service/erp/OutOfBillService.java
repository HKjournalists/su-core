package com.gettec.fsnip.fsn.service.erp;

import java.math.BigInteger;
import java.util.List;
import com.gettec.fsnip.fsn.dao.erp.OutOfBillDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.OutOfBill;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface OutOfBillService  extends BaseService<OutOfBill, OutOfBillDAO> {
	/*
	 * author:cgw
	 * @parameter： num 供货商
	 * describe： 得到出货单信息*/
	public List<String> getOurOfBillByProviderNum(Long num,Long organization) throws ServiceException;
	
	public boolean checkOne(String outOfBillNo,String userName, Long organization) throws ServiceException;
	
	public boolean checkTwo(String outOfBillNo,String userName, Long organization);

	public PagingSimpleModelVO<OutOfBill> getPagingSureOutofgood(int page, int size,
			String keywords,String userName, Long organization) throws ServiceException;
	//根据出货单号查询联系人id
	public List<BigInteger> getCidByOutOrder(String num);
	//查询一条数据
	public OutOfBill getOutOrderInfoByOutOrder (String num) throws ServiceException;
	
	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	public PagingSimpleModelVO<OutOfBill> getOutOfBillfilter(int page, int pageSize,String configure, Long organization, String userName) throws ServiceException;
	
	public PagingSimpleModelVO<OutOfBill> getPagingByUserName(int page, int size,
			String keywords, String userName, Long organization) throws ServiceException;

	OutOfBill add(OutOfBill outOfBill, Long organization) throws ServiceException;

	boolean remove(OutOfBill outOfBill);

	PagingSimpleModelVO<OutOfBill> getOutOfBillCheckfilter(int page,
			int pageSize, String configure, Long organization, String userName) throws ServiceException;

	OutOfBill updateOutOfBill(OutOfBill outOfBill);
}
