package com.gettec.fsnip.fsn.service.erp.buss.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.gettec.fsnip.fsn.dao.erp.buss.StorageChangeLogDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.model.erp.buss.StorageChangeLog;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.buss.StorageChangeLogService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("storageChangeLogService")
public class StorageChangeLogServiceImpl extends BaseServiceImpl<StorageChangeLog, StorageChangeLogDAO>
		implements StorageChangeLogService {

	@Autowired private StorageChangeLogDAO storageChangeLogDAO;
	
	/**
	 * 商品入库/出库/调拨  引起的库存日志变动
	 * @param instance     产品实例
	 * @param inStorage    入库仓库
	 * @param outStorage   出库仓库
	 * @param count        入库/出库/调拨数量
	 * @param type         入库/出库/调拨类型
	 * @param userName     操作员
	 * @param organization 
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void log(MerchandiseInstance instance, String inStorage, String outStorage,
			  int count, String type, String no,String userName, Long organization) throws ServiceException {
		try {
			StorageChangeLog log = new StorageChangeLog(instance.getInitializeProduct().getProduct().getName(),
					instance.getBatch_number(), inStorage, outStorage, type, no, count,userName);
			log.setOrganization(organization);
			create(log);
		} catch (ServiceException sex) {
			throw new ServiceException("StorageChangeLogServiceImpl.log()-->", sex.getException());
		}
		
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<StorageChangeLog> getStorageLogs(int page, 
			int pageSize,String keywords,Long organization) throws ServiceException {
		PagingSimpleModelVO<StorageChangeLog> vo = new PagingSimpleModelVO<StorageChangeLog>(); 
		try {
			String condition = " WHERE e.organization=?1";
			Object[] params = new Object[]{organization};
			vo.setCount(storageChangeLogDAO.count(condition, params));
			List<StorageChangeLog> result = storageChangeLogDAO.getListByPage(page, pageSize, condition, params);
			if(!CollectionUtils.isEmpty(result)){
				for(StorageChangeLog log : result){
					StringBuffer buffer = new StringBuffer();
					buffer.append(log.getUserName());
					buffer.append("\t");
					buffer.append(log.getMerchandiseName());
					buffer.append("\t");
					buffer.append(log.getBatch_number());
					buffer.append("\t");
					buffer.append(log.getBusinessType());
					buffer.append("\t");
					buffer.append(log.getStorage_1());
					if(log.getSourceNo()!= null){
						buffer.append("( 来源单号 : " + log.getSourceNo() + " )");
					}
				}
				vo.setListOfModel(result);
			}
		} catch (JPAException jpae) {
			throw new ServiceException("StorageChangeLogServiceImpl.getStorageLogs()-->", jpae.getException());
		}
		return vo;
	}

	public PagingSimpleModelVO<StorageChangeLog> getStorageChangeLogfilter(int page, int pageSize,String configure, Long organization) {
		PagingSimpleModelVO<StorageChangeLog> result = new PagingSimpleModelVO<StorageChangeLog>();
		String realConfigure = getConfigure(configure);
		realConfigure += " and e.organization = " + organization;
		long count = storageChangeLogDAO.countStorageChangeLog(realConfigure);
		result.setCount(count);
		result.setListOfModel(storageChangeLogDAO.getStorageChangeLogfilter_(page, pageSize, realConfigure));
		return result;
	}
	
	public long countStorageChangeLogfilter(String configure) {
		String realConfigure = getConfigure(configure);
		return storageChangeLogDAO.countStorageChangeLog(realConfigure);
	}
	
	//describe:将请求转换成语句
	//start
	private String getConfigure(String configure) {
		if(configure == null){
			return null;
		}
	    String new_configure = "where ";
	    String filter[] = configure.split("@@");
	    for(int i=0;i<filter.length;i++){
	    	String filters[] = filter[i].split("@");
	    	try {
	    		if(i==0){
	    			new_configure = new_configure + splitJointConfigure(filters[0],filters[1],filters[2]);
	    		}else{
	    			new_configure = new_configure +" AND " + splitJointConfigure(filters[0],filters[1],filters[2]);
	    		}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    return new_configure;
	}
	
	private String splitJointConfigure (String field,String mark,String value){
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if(field.equals("businessType")){
			if(mark.equals("eq")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("eq", value, "businessType"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("neq",value,"businessType"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("startswith",value,"businessType"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("endswith",value,"businessType"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("contains",value,"businessType"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("doesnotcontain",value,"businessType"));
			}
		}
		if(field.equals("merchandiseName")){
			if(mark.equals("eq")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("eq", value, "merchandiseName"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("neq",value,"merchandiseName"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("startswith",value,"merchandiseName"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("endswith",value,"merchandiseName"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("contains",value,"merchandiseName"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("doesnotcontain",value,"merchandiseName"));
			}
		}
		if(field.equals("batch_number")){
			if(mark.equals("eq")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("eq", value, "batch_number"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("neq",value,"batch_number"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("startswith",value,"batch_number"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("endswith",value,"batch_number"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("contains",value,"batch_number"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("doesnotcontain",value,"batch_number"));
			}
		}
		if(field.equals("storage_1")){
			if(mark.equals("eq")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("eq", value, "storage_1"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("neq",value,"storage_1"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("startswith",value,"storage_1"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("endswith",value,"storage_1"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("contains",value,"storage_1"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("doesnotcontain",value,"storage_1"));
			}
		}
		if(field.equals("storage_2")){
			if(mark.equals("eq")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("eq", value, "storage_2"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("neq",value,"storage_2"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("startswith",value,"storage_2"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("endswith",value,"storage_2"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("contains",value,"storage_2"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("doesnotcontain",value,"storage_2"));
			}
		}
		if(field.equals("userName")){
			if(mark.equals("eq")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("eq", value, "userName"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("neq",value,"userName"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("startswith",value,"userName"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("endswith",value,"userName"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("contains",value,"userName"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(storageChangeLogDAO.getfilter("doesnotcontain",value,"userName"));
			}
		}
		if(field.equals("count")){
			return FilterUtils.getConditionStr("e.count",mark,value);
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("e.id",mark,value);
		}
		return null;
	}
	
	private String getProviderTypeId(List<StorageChangeLog> serviceProviderId){
		List<Long> searchId = new ArrayList<Long>();
		for(int i=0;i<serviceProviderId.size();i++){
			searchId.add(new Long(serviceProviderId.get(i).getId()));
		}
		return FilterUtils.FieldConfigure(searchId,"e.id");
	}

	@Override
	public StorageChangeLogDAO getDAO() {
		return storageChangeLogDAO;
	}
}
