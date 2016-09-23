package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.OutGoodsInfo;

public interface OutGoodsInfoDAO extends BaseDAO<OutGoodsInfo> {

	List<OutGoodsInfo> getListByNo(String no) throws DaoException;

	List<OutGoodsInfo> getListByNoPage(int page, int pageSize,
			String no) throws DaoException;

	Long countByNo(String no) throws DaoException;
}
