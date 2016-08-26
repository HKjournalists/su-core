package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseType;

public interface MerchandiseTypeDAO extends BaseDAO<MerchandiseType> {

	public List<MerchandiseType> getMerchandiseTypefilter_(int from,int size,String configure);
	public long countMerchandiseType(String configure);
}
