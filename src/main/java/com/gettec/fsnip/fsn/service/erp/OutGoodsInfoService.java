package com.gettec.fsnip.fsn.service.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.erp.OutGoodsInfoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.OutGoodsInfo;
import com.gettec.fsnip.fsn.model.erp.base.OutOfBill;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface OutGoodsInfoService extends BaseService<OutGoodsInfo, OutGoodsInfoDAO> {

	List<OutGoodsInfo> getListByNo(String no) throws ServiceException;
	public List<OutGoodsInfo> getAllProductByNum(String num) throws ServiceException;
	void save(OutOfBill outOfBill) throws ServiceException;
	PagingSimpleModelVO<OutGoodsInfo> getListByNoPage(int page, int pageSize,
			String no) throws ServiceException;
}
