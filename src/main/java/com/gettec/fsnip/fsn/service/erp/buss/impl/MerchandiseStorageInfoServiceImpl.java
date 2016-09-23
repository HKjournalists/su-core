package com.gettec.fsnip.fsn.service.erp.buss.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.gettec.fsnip.fsn.dao.erp.StorageInfoDAO;
import com.gettec.fsnip.fsn.dao.erp.buss.MerchandiseInstanceDAO;
import com.gettec.fsnip.fsn.dao.erp.buss.MerchandiseStorageInfoDAO;
import com.gettec.fsnip.fsn.dao.product.ProductDAO;
import com.gettec.fsnip.fsn.enums.BusinessTypeEnums;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.model.erp.base.StorageInfo;
import com.gettec.fsnip.fsn.model.erp.buss.MerchandiseStorageInfo;
import com.gettec.fsnip.fsn.model.erp.buss.MerchandiseStorageInfoPK;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.StorageInfoService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseInstanceService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseStorageInfoService;
import com.gettec.fsnip.fsn.service.erp.buss.StorageChangeLogService;
import com.gettec.fsnip.fsn.transfer.ProductTransfer;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("merchandiseStorageInfoService")
public class MerchandiseStorageInfoServiceImpl extends BaseServiceImpl<MerchandiseStorageInfo, MerchandiseStorageInfoDAO>
		implements MerchandiseStorageInfoService {

	@Autowired private MerchandiseStorageInfoDAO merchandiseStorageInfoDAO;
	@Autowired private StorageChangeLogService storageChangeLogService;
	@Autowired private StorageInfoDAO storageInfoDAO;
	@Autowired private ProductDAO productDAO;
	@Autowired private StorageInfoService storageInfoService;
	@Autowired private MerchandiseInstanceService merchandiseInstanceService;
	@Autowired private MerchandiseInstanceDAO merchandiseInstanceDAO;

	/**
	 * 商品入库/出库/调拨
	 * @param instance     产品实例
	 * @param count        入库/出库/调拨数量
	 * @param targetStore  入库/出库/调拨仓库
	 * @param no           入库/出库/调拨单号
	 * @param log          库存日志是否需要变动
	 * @param type         入库/出库/调拨类型
	 * @param userName     操作员
	 * @param organization 
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean addStorage(MerchandiseInstance instance, int count, StorageInfo targetStore,
			 String no, boolean logFlag, String type,String userName, Long organization) throws ServiceException {
		boolean success = false;
		try {
			/* 1. 增加库存数量 */
			MerchandiseStorageInfoPK id = new MerchandiseStorageInfoPK(targetStore.getNo(), instance.getInstanceID());
			MerchandiseStorageInfo orig_storeInfo = merchandiseStorageInfoDAO.findById(id);
			if(orig_storeInfo == null){
				MerchandiseStorageInfo storeInfo = new MerchandiseStorageInfo(id, count);
				storeInfo.setOrganization(organization);
				storeInfo.setReserves(0);
				create(storeInfo);
			}else{
				orig_storeInfo.setCount(orig_storeInfo.getCount() + count);
				update(orig_storeInfo);
			}
			/* 2. 记录库存变动日志  */
			if(logFlag){
				storageChangeLogService.log(instance, targetStore.getName(), "无", count, type, no,userName, organization);
			}
			success = true;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]MerchandiseStorageInfoServiceImpl.addStorage()-->", jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]MerchandiseStorageInfoServiceImpl.addStorage()-->", sex.getException());
		}
		return success;
	}

	/**
	 * 商品出库
	 * @param instance    产品实例
	 * @param count       出库数量
	 * @param targetStore 出库仓库
	 * @param no          出库单号
	 * @param logFlag     库存日志变动标识
	 * @param type        出库类型
	 * @param userName    操作员
	 * @param organization
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean reduceStorage(MerchandiseInstance instance, int count, StorageInfo targetStore, String no, 
			boolean logFlag, String type, String userName, Long organization) throws ServiceException{
		boolean success = false;
		try {
			MerchandiseStorageInfoPK id = new MerchandiseStorageInfoPK(targetStore.getNo(),instance.getInstanceID());
			MerchandiseStorageInfo storeInfo = merchandiseStorageInfoDAO.findById(id);
			if(storeInfo != null){
				if(storeInfo.getCount() > 0 && storeInfo.getCount()>=count){ // 现有库存量大于出库数量
					storeInfo.setCount(storeInfo.getCount() - count);
					if(logFlag){
						storageChangeLogService.log(instance, "无", targetStore.getName(), count, type, no,userName, organization);
					}
					success = true;
				}else{
					success = false;
				}
			}
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]MerchandiseStorageInfoServiceImpl.reduceStorage()-->", jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]MerchandiseStorageInfoServiceImpl.reduceStorage()-->", sex.getException());
		}
		return success;
	}

	/**
	 * 商品调拨库存日志变动
	 * @param instance    商品实例
	 * @param count       调拨数量
	 * @param fromStore   调出仓库
	 * @param toStore     调入仓库
	 * @param no          调拨单号
	 * @param type        调拨类型
	 * @param userName    操作员
	 * @param organization
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean flittingStorage(MerchandiseInstance instance, int count, StorageInfo fromStore,
			StorageInfo toStore, String no, String type, String userName, Long organization) throws ServiceException {
		boolean success = false;
		try {
			/* 1. 出库 */
			success = reduceStorage(instance, count, fromStore, no, false,type,userName, organization);
			/* 2. 入库 */
			success = success && addStorage(instance, count, toStore, no, false,type,userName, organization);
			if(success){
				storageChangeLogService.log(instance, toStore.getName(), fromStore.getName(), count, 
						BusinessTypeEnums.FLITTING_ORDER.getTypeName(), "",userName, organization);
			}
		} catch (ServiceException sex) {
			throw new ServiceException("MerchandiseStorageInfoServiceImpl.flittingStorage()-->", sex.getException());
		}
		return success;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<MerchandiseStorageInfo> getPaging(int page,
			int pageSize, String configure, Long organization) {
		try {
			PagingSimpleModelVO<MerchandiseStorageInfo> result = new PagingSimpleModelVO<MerchandiseStorageInfo>();
			String condition = " WHERE e.organization=?1";
			Object[] params = new Object[]{organization}; 
			result.setCount(merchandiseStorageInfoDAO.count(condition, params));
			
			if(configure != null && configure.trim()!="") {
				condition = condition + " and e.name like '%" + configure + "%'";
			}
			
			List<MerchandiseStorageInfo> infos = merchandiseStorageInfoDAO.getListByPage(page, pageSize, condition, params);
			
			Map<String, StorageInfo> storageMap = new HashMap<String, StorageInfo>();
			Map<Long, MerchandiseInstance> merchandiseMap = new HashMap<Long, MerchandiseInstance>();
			
			if(!CollectionUtils.isEmpty(infos)){
				for(MerchandiseStorageInfo info : infos){
					
					String storageID = info.getId().getNo_1();
					Long instanceID = info.getId().getNo_2();
					
					if(merchandiseMap.containsKey(instanceID)){
						info.setInstance(merchandiseMap.get(instanceID));
					}else{
						MerchandiseInstance instance = null;
						instance = merchandiseInstanceService.findById(instanceID);
						info.setInstance(instance);
						
						merchandiseMap.put(instance.getInstanceID(), instance);
					}
					
					if(storageMap.containsKey(storageID)){
						info.setStorage(storageMap.get(storageID));
					}else{
						StorageInfo storage = storageInfoDAO.findById(storageID);
						info.setStorage(storage);
						
						storageMap.put(storageID, storage);
					}
				}
			}
			
			result.setListOfModel(infos);
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	public PagingSimpleModelVO<MerchandiseStorageInfo> getMerchandiseStorageInfofilter(
			int page, int pageSize, String configure, Long organization)throws ServiceException {
		try {
			PagingSimpleModelVO<MerchandiseStorageInfo> result = new PagingSimpleModelVO<MerchandiseStorageInfo>();
			String realConfigure = getConfigure(configure, organization);
			realConfigure += " and e.organization = " + organization;
			long count = merchandiseStorageInfoDAO.countMerchandiseStorageInfo(realConfigure);
			result.setCount(count);
			
			List<MerchandiseStorageInfo> infos = merchandiseStorageInfoDAO.getMerchandiseStorageInfofilter_(page, pageSize, realConfigure);
			Map<String, StorageInfo> storageMap = new HashMap<String, StorageInfo>();
			Map<Long, MerchandiseInstance> merchandiseMap = new HashMap<Long, MerchandiseInstance>();

			if (!CollectionUtils.isEmpty(infos)) {
				for (MerchandiseStorageInfo info : infos) {

					String storageID = info.getId().getNo_1();
					Long instanceID = info.getId().getNo_2();

					if (merchandiseMap.containsKey(instanceID)) {
						info.setInstance(merchandiseMap.get(instanceID));
					} else {
						MerchandiseInstance instance = null;
						instance = merchandiseInstanceService.findById(instanceID);
						info.setInstance(instance);

						merchandiseMap.put(instance.getInstanceID(), instance);
					}

					if (storageMap.containsKey(storageID)) {
						info.setStorage(storageMap.get(storageID));
					} else {
						StorageInfo storage = storageInfoDAO.findById(storageID);
						info.setStorage(storage);

						storageMap.put(storageID, storage);
					}
				}
			}

			result.setListOfModel(infos);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public long countMerchandiseStorageInfofilter(String configure) {
		String realConfigure = getConfigure(configure, 0l);
		return merchandiseStorageInfoDAO.countMerchandiseStorageInfo(realConfigure);
	}
	
	//describe:将请求转换成语句
	//start
	private String getConfigure(String configure, Long organization) {
		if(configure == null){
			return null;
		}
	    String new_configure = "where ";
	    String filter[] = configure.split("@@");
	    for(int i=0;i<filter.length;i++){
	    	String filters[] = filter[i].split("@");
	    	try {
	    		if(i==0){
	    			new_configure = new_configure + splitJointConfigure(filters[0],filters[1],filters[2], organization);
	    		}else{
	    			new_configure = new_configure +" AND " + splitJointConfigure(filters[0],filters[1],filters[2], organization);
	    		}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    return new_configure;
	}
	
	private String splitJointConfigure (String field,String mark,String value, Long organization){
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if(field.equals("storage_no")){
			if(mark.equals("eq")){
				return getStorageNo(merchandiseStorageInfoDAO.getfilter("eq", value, "e.id.no_1"));
			}else if(mark.equals("neq")){
				return getStorageNo(merchandiseStorageInfoDAO.getfilter("neq",value,"e.id.no_1"));
			}else if(mark.equals("startswith")){
				return getStorageNo(merchandiseStorageInfoDAO.getfilter("startswith",value,"e.id.no_1"));
			}else if(mark.equals("endswith")){
				return getStorageNo(merchandiseStorageInfoDAO.getfilter("endswith",value,"e.id.no_1"));
			}else if(mark.equals("contains")){
				return getStorageNo(merchandiseStorageInfoDAO.getfilter("contains",value,"e.id.no_1"));
			}else if(mark.equals("doesnotcontain")){
				return getStorageNo(merchandiseStorageInfoDAO.getfilter("doesnotcontain",value,"e.id.no_1"));
			}
		}
		if(field.equals("storage_name")){
			if(mark.equals("eq")){
				return getStorageName("eq", value, "e.id.no_1");
			}else if(mark.equals("neq")){
				return getStorageName("neq",value,"e.id.no_1");
			}else if(mark.equals("startswith")){
				return getStorageName("startswith",value,"e.id.no_1");
			}else if(mark.equals("endswith")){
				return getStorageName("endswith",value,"e.id.no_1");
			}else if(mark.equals("contains")){
				return getStorageName("contains",value,"e.id.no_1");
			}else if(mark.equals("doesnotcontain")){
				return getStorageName("doesnotcontain",value,"e.id.no_1");
			}
		}
		if(field.equals("instance_batch_number")){
			if(mark.equals("eq")){
				return getInstanceBatchNumber("eq", value, "e.batchSerialNo");
			}else if(mark.equals("neq")){
				return getInstanceBatchNumber("neq",value,"e.batchSerialNo");
			}else if(mark.equals("startswith")){
				return getInstanceBatchNumber("startswith",value,"e.batchSerialNo");
			}else if(mark.equals("endswith")){
				return getInstanceBatchNumber("endswith",value,"e.batchSerialNo");
			}else if(mark.equals("contains")){
				return getInstanceBatchNumber("contains",value,"e.id.no_2");
			}else if(mark.equals("doesnotcontain")){
				return getInstanceBatchNumber("doesnotcontain",value,"e.batchSerialNo");
			}
		}
		if(field.equals("instance_product_barcode")){
			if(mark.equals("eq")){
				return getInfoNo("eq", value, "e.id.no_2");
			}else if(mark.equals("neq")){
				return getInfoNo("neq",value,"e.id.no_2");
			}else if(mark.equals("startswith")){
				return getInfoNo("startswith",value,"e.id.no_2");
			}else if(mark.equals("endswith")){
				return getInfoNo("endswith",value,"e.id.no_2");
			}else if(mark.equals("contains")){
				return getInfoNo("contains",value,"e.id.no_2");
			}else if(mark.equals("doesnotcontain")){
				return getInfoNo("doesnotcontain",value,"e.id.no_2");
			}
		}
		if(field.equals("instance_product_name")){
			if(mark.equals("eq")){
				return getInfoName("eq", value, "e.id.no_2", organization);
			}else if(mark.equals("neq")){
				return getInfoName("neq", value, "e.id.no_2", organization);
			}else if(mark.equals("startswith")){
				return getInfoName("startswith",value,"e.id.no_2", organization);
			}else if(mark.equals("endswith")){
				return getInfoName("endswith",value,"e.id.no_2", organization);
			}else if(mark.equals("contains")){
				return getInfoName("contains",value,"e.id.no_2", organization);
			}else if(mark.equals("doesnotcontain")){
				return getInfoName("doesnotcontain",value,"e.id.no_2", organization);
			}
		}
		if(field.equals("instance_product_unit_name")){
			if(mark.equals("eq")){
				return getInfoUnitName("eq", value, "e.id.no_2", organization);
			}else if(mark.equals("neq")){
				return getInfoUnitName("neq", value, "e.id.no_2", organization);
			}else if(mark.equals("startswith")){
				return getInfoUnitName("startswith", value, "e.id.no_2", organization);
			}else if(mark.equals("endswith")){
				return getInfoUnitName("endswith", value, "e.id.no_2", organization);
			}else if(mark.equals("contains")){
				return getInfoUnitName("contains", value, "e.id.no_2", organization);
			}else if(mark.equals("doesnotcontain")){
				return getInfoUnitName("doesnotcontain", value, "e.id.no_2", organization);
			}
		}
		if(field.equals("count")){
			return FilterUtils.getConditionStr("e.count",mark,value);
		}
		return null;
	}
	
	private String getInfoUnitName(String filter, String value,
			String fieldName, Long organization) {
		List<MerchandiseInstance> storages = merchandiseInstanceDAO.getInfoUnitNamefilter(filter, value, fieldName, organization);
		String sql = fieldName+" IN(";
		if(storages.size() <= 0) {
			sql += "0)";
		} else {
			for(int i = 0; i<storages.size(); i++){
				if(i<(storages.size()-1)){
					sql += storages.get(i).getInstanceID() + ",";
				}else{
					sql += storages.get(i).getInstanceID() + ")";
				}
			}
		}
		return sql;
	}
	
	private String getInfoName(String filter, String value,
			String fieldName, Long organization) {
		List<MerchandiseInstance> storages = merchandiseInstanceDAO.getInfoNamefilter(filter, value, fieldName, organization);
		String sql = fieldName+" IN(";
		if(storages.size() <= 0) {
			sql += "0)";
		} else {
			for(int i = 0; i<storages.size(); i++){
				if(i<(storages.size()-1)){
					sql += storages.get(i).getInstanceID() + ",";
				}else{
					sql += storages.get(i).getInstanceID() + ")";
				}
			}
		}
		return sql;
	}
	
	private String getInfoNo(String filter, String value,
			String fieldName) {
		List<MerchandiseInstance> storages = merchandiseInstanceDAO.getInfoNofilter(filter, value, fieldName);
		String sql = fieldName+" IN(";
		if(storages.size() <= 0) {
			sql += "0)";
		} else {
			for(int i = 0; i<storages.size(); i++){
				if(i<(storages.size()-1)){
					sql += storages.get(i).getInstanceID() + ",";
				}else{
					sql += storages.get(i).getInstanceID() + ")";
				}
			}
		}
		return sql;
	}
	
	private String getInstanceBatchNumber(String filter, String value,
			String fieldName) {
		List<MerchandiseInstance> storages = merchandiseInstanceDAO.getfilter(filter, value, "e.id.no_2");
		String sql = fieldName+" IN(";
		if(storages.size() <= 0) {
			sql += "0)";
		} else {
			for(int i = 0; i<storages.size(); i++){
				if(i<(storages.size()-1)){
					sql += storages.get(i).getInstanceID() + ",";
				}else{
					sql += storages.get(i).getInstanceID() + ")";
				}
			}
		}
		return sql;
	}

	private String getStorageName(String filter, String value, String fieldName) {
		List<StorageInfo> storages = storageInfoDAO.getStoragefilter(filter, value, fieldName);
		String sql = fieldName+" IN(";
		if(storages.size() <= 0) {
			sql += "0)";
		} else {
			for(int i = 0; i<storages.size(); i++){
				if(i<(storages.size()-1)){
					sql += storages.get(i).getNo() + ",";
				}else{
					sql += storages.get(i).getNo() + ")";
				}
			}
		}
		return sql;
	}

	private String getStorageNo(List<MerchandiseStorageInfo> serviceProviderId){
		List<String> searchId = new ArrayList<String>();
		for(int i=0;i<serviceProviderId.size();i++){
			searchId.add(new String(serviceProviderId.get(i).getId().getNo_1()));
		}
		return FilterUtils.FieldConfigureStr(searchId,"e.id.no_1");
	}
	//end
	/**
	 * 根据库存获取商品信息
	 * Author 郝
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Product> getProductByStorageInfo(Long organization) throws ServiceException{	
		List<Product> products = null;
		try {
			products = ProductTransfer.transfer(productDAO.getListByStorageInfo(organization));
		} catch (DaoException e) {
			throw new ServiceException("MerchandiseStorageInfoServiceImpl.getProductByStorageInfo()-->", e.getException());
		}
		return products;
	}
	
	/**
	 * 商品实例ID获取库存中的仓库
	 * @param productId
	 * @param storageId
	 * @param model
	 * @return 商品实例集合
	 * Author 郝圆彬
	 * 2014-10-27
	 * 修改
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<MerchandiseInstance> getBatchNumByStorageInfo(Long productId,String storageId,Long organization){	
		
			return merchandiseInstanceService.getProductInstancesByStorageInfoAndStorage(productId,storageId,organization);
		
	}
	
	/**
	 * 商品实例ID或者产品ID获取库存中的仓库
	 * @param productId
	 * @param storageId
	 * @param model
	 * @return 商品实例集合
	 * Author 郝圆彬
	 * 2014-10-27
	 * 创建
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<StorageInfo> getStorageByProductIdOrInstanceId(Long productId,Long instanceId,Long organization){	
			return storageInfoService.getStorageByProductIdOrInstanceId(productId, instanceId, organization);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public MerchandiseStorageInfo getNumByStorage(String storageId,Long instanceId,Long organization) throws ServiceException{	
		try {
			MerchandiseStorageInfoPK id = new MerchandiseStorageInfoPK(storageId,instanceId);
			MerchandiseStorageInfo info = merchandiseStorageInfoDAO.findById(id);
			return info;
		} catch (JPAException jpae) {
			throw new ServiceException("MerchandiseStorageInfoServiceImpl.getNumByStorage()-->", jpae.getException());
		}
	}

	@Override
	public MerchandiseStorageInfoDAO getDAO() {
		return merchandiseStorageInfoDAO;
	}
	/**
	 * 出货单添加储备数量
	 * @param MerchandiseInstance 
	 * @param count 
	 * @param StorageInfo 
	 * @param type (add为增加操作，reduce为减少操作)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void changeReserves(MerchandiseInstance instance, int count, StorageInfo targetStore,String type)throws ServiceException{
		try {
			MerchandiseStorageInfoPK id = new MerchandiseStorageInfoPK(targetStore.getNo(), instance.getInstanceID());
			MerchandiseStorageInfo orig_storeInfo = merchandiseStorageInfoDAO.findById(id);
			if(type=="add"){
				/*1.增操作*/
				orig_storeInfo.setCount(orig_storeInfo.getCount()-count);
				orig_storeInfo.setReserves(orig_storeInfo.getReserves()+count);
			}else if(type=="reduce"){
				/*2.减少操作*/
				orig_storeInfo.setCount(orig_storeInfo.getCount()+count);
				orig_storeInfo.setReserves(orig_storeInfo.getReserves()-count);
			}
			update(orig_storeInfo);
		}catch (JPAException jpae) {
			throw new ServiceException("[JPAException]MerchandiseStorageInfoServiceImpl.addReserves()-->"+jpae.getMessage(), jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]MerchandiseStorageInfoServiceImpl.addReserves()-->"+sex.getMessage(), sex.getException());
		}
	}
}
