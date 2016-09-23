package com.gettec.fsnip.fsn.service.erp.buss;

import java.util.List;
import com.gettec.fsnip.fsn.dao.erp.buss.MerchandiseInstanceDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.InitializeProduct;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface MerchandiseInstanceService extends BaseService<MerchandiseInstance,MerchandiseInstanceDAO>{

	MerchandiseInstance getInstanceByNoAndBatchNumber(Long no, String batchNumber) throws ServiceException;
	List<MerchandiseInstance> getProductInstancesByStorageInfoAndStorage(
			Long productId, String storageId, Long organization);
	MerchandiseInstance saveInstance(InitializeProduct initializeProduct,
			String batchNo) throws ServiceException;
}
