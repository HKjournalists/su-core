package com.gettec.fsnip.fsn.dao.erp.buss;

import java.util.List;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.erp.buss.MerchandiseStorageInfo;

public interface MerchandiseStorageInfoDAO extends BaseDAO<MerchandiseStorageInfo> {

	public List<MerchandiseStorageInfo> getMerchandiseStorageInfofilter_(int from,
			int size, String configure);

	public long countMerchandiseStorageInfo(String configure);
}
