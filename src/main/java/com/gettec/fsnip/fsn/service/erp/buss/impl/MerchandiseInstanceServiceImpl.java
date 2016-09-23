package com.gettec.fsnip.fsn.service.erp.buss.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.buss.MerchandiseInstanceDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.InitializeProduct;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseInstanceService;

@Service(value="merchandiseInstanceService")
public class MerchandiseInstanceServiceImpl extends BaseServiceImpl<MerchandiseInstance, MerchandiseInstanceDAO>
			implements MerchandiseInstanceService {
	@Autowired private MerchandiseInstanceDAO merchandiseInstanceDAO;
	
	public MerchandiseInstance getInstanceByNoAndBatchNumber(Long no,
			String batchNumber) throws ServiceException {
		try {
			return merchandiseInstanceDAO.getMerchandiseInstanceByNoAndBatchNumber(no, batchNumber);
		} catch (DaoException dex) {
			throw new ServiceException("MerchandiseInstanceServiceImpl.getInstanceByNoAndBatchNumber()-->", dex.getException());
		}
	}

	/**
	 * 商品入库
	 * @param initializeProduct
	 * @param batchNo
	 * @param userName
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public MerchandiseInstance saveInstance(InitializeProduct initializeProduct,
			String batchNo) throws ServiceException {
		MerchandiseInstance instance = null;
		try {
			instance = getInstanceByNoAndBatchNumber(initializeProduct.getId(),batchNo);
			if(instance == null){
				instance = new MerchandiseInstance();
				instance.setBatch_number(batchNo);
				instance.setInitializeProduct(initializeProduct);
				merchandiseInstanceDAO.persistent(instance);
			}
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]MerchandiseInstanceServiceImpl.saveInstance()-->", jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]MerchandiseInstanceServiceImpl.saveInstance()-->", sex.getException());
		}
		return instance;
	}

	@Override
	public List<MerchandiseInstance> getProductInstancesByStorageInfoAndStorage(
			Long productId, String storageId, Long organization) {
		return merchandiseInstanceDAO.getProductInstancesByStorageInfoAndStorage(productId,storageId,organization);
	}

	@Override
	public MerchandiseInstanceDAO getDAO() {
		return merchandiseInstanceDAO;
	}
}
