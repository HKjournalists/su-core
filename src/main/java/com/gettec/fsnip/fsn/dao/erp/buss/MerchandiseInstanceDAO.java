package com.gettec.fsnip.fsn.dao.erp.buss;

import java.util.List;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;

public interface MerchandiseInstanceDAO extends BaseDAO<MerchandiseInstance> {

	public MerchandiseInstance getMerchandiseInstanceByNoAndBatchNumber(Long no, String batchNumber) throws DaoException;
	
	public List<MerchandiseInstance> getInfoNofilter(String filter, String name,String fieldName);

	public List<MerchandiseInstance> getInfoNamefilter(String filter, String name,String fieldName, Long organization);

	public List<MerchandiseInstance> getInfoUnitNamefilter(String filter,String name, String fieldName, Long organization);

	public List<MerchandiseInstance> getProductInstancesByStorageInfoAndStorage(
			Long productId, String storageId, Long organization);
}
