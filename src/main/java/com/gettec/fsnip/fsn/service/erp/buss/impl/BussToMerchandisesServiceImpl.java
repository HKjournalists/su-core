package com.gettec.fsnip.fsn.service.erp.buss.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.buss.BussToMerchandisesDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.BusinessType;
import com.gettec.fsnip.fsn.model.erp.base.InitializeProduct;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.model.erp.base.StorageInfo;
import com.gettec.fsnip.fsn.model.erp.buss.BussToMerchandises;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.BusinessTypeService;
import com.gettec.fsnip.fsn.service.erp.InitializeProductService;
import com.gettec.fsnip.fsn.service.erp.StorageInfoService;
import com.gettec.fsnip.fsn.service.erp.buss.BussToMerchandisesService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseInstanceService;

@Service
public class BussToMerchandisesServiceImpl extends BaseServiceImpl<BussToMerchandises, BussToMerchandisesDAO>
		implements BussToMerchandisesService {
	@Autowired private BussToMerchandisesDAO bussToMerchandisesDAO;
	@Autowired private StorageInfoService storageInfoService;
	@Autowired private BusinessTypeService businessTypeService;
	@Autowired private InitializeProductService initializeProductService;
	@Autowired private MerchandiseInstanceService merchandiseInstanceService;
	
	/**
	 * 保存商品列表信息(入库/出库/调拨)
	 * @param merchandises
	 * @return
	 * @throws ServiceException 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean addRelationShips(List<BussToMerchandises> merchandises) {
		try {
			Map<String, StorageInfo> storageMap = new HashMap<String, StorageInfo>();
			for(BussToMerchandises item : merchandises){
				/* 1. 入库仓库 */
				if(item.getStorage_1() != null){
					if(storageMap.containsKey(item.getStorage_1().getNo())){
						item.setStorage_1(storageMap.get(item.getStorage_1().getNo()));
					}else{
						StorageInfo storeInfo = storageInfoService.findById(item.getStorage_1().getNo());
						item.setStorage_1(storeInfo);
						storageMap.put(storeInfo.getNo(), storeInfo);
					}
				}
				/* 2. 出库仓库 */
				if(item.getStorage_2() != null){
					if(storageMap.containsKey(item.getStorage_2().getNo())){
						item.setStorage_2(storageMap.get(item.getStorage_2().getNo()));
					}else{
						StorageInfo storeInfo = storageInfoService.findById(item.getStorage_2().getNo());
						item.setStorage_2(storeInfo);
						storageMap.put(storeInfo.getNo(), storeInfo);
					}
				}
				bussToMerchandisesDAO.persistent(item);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 保存商品列表信息
	 * @param merchandises
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean save(List<BussToMerchandises> merchandises, String autoNo, Long busTypeId) throws ServiceException {
		try {
			BusinessType orig_busType = businessTypeService.findById(busTypeId);
			for(BussToMerchandises item : merchandises){
				MerchandiseInstance instance = null;
				if(busTypeId==5){  // 入库
					InitializeProduct orig_initProduct = initializeProductService.findById(item.getMerchandiseNo());
					instance = merchandiseInstanceService.saveInstance(orig_initProduct, item.getBatch_number());
				}else { // 出库、调拨
					instance = merchandiseInstanceService.getInstanceByNoAndBatchNumber(item.getMerchandiseNo(), item.getBatch_number());
				}
				if(instance == null){ return false;};
				item.setNo_1(autoNo);
				item.setNo_2(instance.getInstanceID());
				item.setType(orig_busType);
				item.setUnit(instance.getInitializeProduct().getProduct().getUnit());
				item.setInstance(instance);
			}
			return addRelationShips(merchandises);
		} catch (ServiceException sex) {
			throw new ServiceException("BussToMerchandisesServiceImpl.save()-->", sex);
		}
	}
	
	/**
	 * 根据单号和类型获取入库/出库/调拨的商品列表
	 * @param page
	 * @param pageSize
	 * @param no
	 * @param type
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public List<BussToMerchandises> getMerInfoByNoPage(int page, int pageSize, String no, int type) throws ServiceException {
		try {
			return getDAO().getMerInfoByNoPage(page, pageSize, no, type);
		} catch (DaoException dex) {
			throw new ServiceException("BussToMerchandisesServiceImpl.getMerInfoByNoPage()-->", dex);
		}
	}

	@Override
	public BussToMerchandisesDAO getDAO() {
		return bussToMerchandisesDAO;
	}

    @Override
    public long getMerInfoCountByNoPage(int page, int pageSize, String num,int type) throws ServiceException {
        try {
            return bussToMerchandisesDAO.countOutStorageRecord(page, pageSize,num,type);
        } catch (DaoException e) {
            throw new ServiceException("BussToMerchandisesServiceImpl.getMerInfoByNoPage()-->", e);
        }
    }
	
	
}
