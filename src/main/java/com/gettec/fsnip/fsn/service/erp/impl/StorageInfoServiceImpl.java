package com.gettec.fsnip.fsn.service.erp.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.InitializeProductDAO;
import com.gettec.fsnip.fsn.dao.erp.ReceivingNoteDAO;
import com.gettec.fsnip.fsn.dao.erp.StorageInfoDAO;
import com.gettec.fsnip.fsn.dao.erp.buss.MerchandiseStorageInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.StorageInfo;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.StorageInfoService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("storageInfoService")
public class StorageInfoServiceImpl extends BaseServiceImpl<StorageInfo, StorageInfoDAO> 
		implements StorageInfoService {

	@Autowired private StorageInfoDAO storageInfoDAO;
	@Autowired private MerchandiseStorageInfoDAO merchandiseStorageInfoDAO;
	@Autowired private InitializeProductDAO initializeProductDAO;
	@Autowired private ReceivingNoteDAO receivingNoteDAO;

	/**
	 * 新增仓库信息
	 * @param storageInfo
	 * @param organization
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean add(StorageInfo storageInfo, Long organization) throws ServiceException {
		try {
			/* 1. 查看仓库名称和管理员名称是否重复 */
			String condition = " WHERE e.organization = ?1 AND e.name= ?2";
			long count = getDAO().count(condition, new Object[]{organization,storageInfo.getName()});
			if (count > 0) {
				return false;
			}
			/* 2. 查看最大编号 */
			String no = storageInfoDAO.findLastNoByOrg(organization);
			if(no == null){
				no = organization + "0000";
			}else{
				/* 3. 构建新编号 */
				String StartNo = no.substring(0,(no.length()-4));
				int initNum = Integer.parseInt(no.substring(no.length()-4)); // 1
				initNum = initNum+1; // 2
				String Snum = "0000" +initNum; // 00002
				String EndNum = Snum.substring(Snum.length()-4); // 0002
				no= StartNo + EndNum;
			}
			/* 4. 新增仓库信息 */
			storageInfo.setNo(no);
			create(storageInfo);
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]StorageInfoServiceImpl.add()-->" + jpae.getException(), jpae.getException());
		} catch (DaoException dex) {
			throw new ServiceException("[DaoException]StorageInfoServiceImpl.add()-->" + dex.getException(), dex.getException());
		}
	}

	/**
	 * 更新仓库信息
	 * @param storageInfo
	 * @param organization
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean update(StorageInfo storageInfo, Long organization) throws ServiceException {
		try {
			Long count = getDAO().count("where e.name = ?1 and e.organization = ?2 and e.no != ?3", 
					new Object[]{storageInfo.getName(),organization,storageInfo.getNo()});
			if(count > 0){return false;}
			StorageInfo orig_storage = getDAO().findById(storageInfo.getNo());
			orig_storage.setManager(storageInfo.getManager());
			orig_storage.setName(storageInfo.getName());
			update(orig_storage);
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]StorageInfoServiceImpl.update()-->" + jpae.getMessage(), jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]StorageInfoServiceImpl.update()-->" + sex.getMessage(), sex.getException());
		}
		return true;
	}

	/**
	 * 删除仓库信息
	 * @param storageInfo
	 * @param organization
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(StorageInfo storageInfo, Long organization) throws ServiceException {
		try {
			StorageInfo orig_storage = getDAO().findById(storageInfo.getNo());
			delete(orig_storage);
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]StorageInfoServiceImpl.remove()-->" + jpae.getMessage(), jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]StorageInfoServiceImpl.remove()-->" + sex.getMessage(), sex.getException());
		}
	}

	public StorageInfo findByName(String name,Long organization) {
		return storageInfoDAO.findByName(name,organization);
	}

	/**
	 * 查找仓库信息
	 * @param page
	 * @param pageSize
	 * @param keywords
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public PagingSimpleModelVO<StorageInfo> getPaging(int page, int pageSize,
			String keywords, Long organization) throws ServiceException {
		PagingSimpleModelVO<StorageInfo> result = new PagingSimpleModelVO<StorageInfo>();
		try {
			String condition = " WHERE e.organization = ?1";
			String condition1 = " WHERE e.organization ="+organization;
			result.setCount(getDAO().count(condition, new Object[]{organization}));
			result.setListOfModel(getDAO().getPaging(page, pageSize, condition1));
		} catch (JPAException jpae) {
			throw new ServiceException("StorageInfoServiceImpl.getPaging()-->" + jpae.getMessage(), jpae.getException());
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public StorageInfo findById(String no) throws ServiceException {
		try {
			return storageInfoDAO.findById(no);
		} catch (JPAException jpae) {
			throw new ServiceException("StorageInfoServiceImpl.findById-->" + jpae.getMessage(), jpae);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public PagingSimpleModelVO<StorageInfo> getAllStorageInfo(Long organization) {
		PagingSimpleModelVO<StorageInfo> result = new PagingSimpleModelVO<StorageInfo>();
		String configure = " WHERE e.organization = " + organization;
		try {
			result.setListOfModel(storageInfoDAO.findAll(organization, configure));
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return result;
	}

	public PagingSimpleModelVO<StorageInfo> getStorageInfofilter(int page,
			int pageSize, String configure, Long organization) {
		PagingSimpleModelVO<StorageInfo> result = new PagingSimpleModelVO<StorageInfo>();
		String realConfigure = getConfigure(configure);
		realConfigure += " and e.organization = " + organization;
		long count = storageInfoDAO.countStorageInfo(realConfigure);
		result.setCount(count);
		result.setListOfModel(storageInfoDAO.getStorageInfofilter_(page,
				pageSize, realConfigure));
		return result;
	}

	public long countStorageInfofilter(String configure) {
		String realConfigure = getConfigure(configure);
		return storageInfoDAO.countStorageInfo(realConfigure);
	}

	// describe:将请求转换成语句
	// start
	private String getConfigure(String configure) {
		if (configure == null) {
			return null;
		}
		String new_configure = "where ";
		String filter[] = configure.split("@@");
		for (int i = 0; i < filter.length; i++) {
			String filters[] = filter[i].split("@");
			try {
				if (i == 0) {
					new_configure = new_configure
							+ splitJointConfigure(filters[0], filters[1],
									filters[2]);
				} else {
					new_configure = new_configure
							+ " AND "
							+ splitJointConfigure(filters[0], filters[1],
									filters[2]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new_configure;
	}

	private String splitJointConfigure(String field, String mark, String value) {
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (field.equals("name")) {
			if (mark.equals("eq")) {
				return getProviderTypeId(storageInfoDAO.getfilter("eq", value,
						"name"));
			} else if (mark.equals("neq")) {
				return getProviderTypeId(storageInfoDAO.getfilter("neq", value,
						"name"));
			} else if (mark.equals("startswith")) {
				return getProviderTypeId(storageInfoDAO.getfilter("startswith",
						value, "name"));
			} else if (mark.equals("endswith")) {
				return getProviderTypeId(storageInfoDAO.getfilter("endswith",
						value, "name"));
			} else if (mark.equals("contains")) {
				return getProviderTypeId(storageInfoDAO.getfilter("contains",
						value, "name"));
			} else if (mark.equals("doesnotcontain")) {
				return getProviderTypeId(storageInfoDAO.getfilter(
						"doesnotcontain", value, "name"));
			}
		}
		if (field.equals("manager")) {
			if (mark.equals("eq")) {
				return getProviderTypeId(storageInfoDAO.getfilter("eq", value,
						"manager"));
			} else if (mark.equals("neq")) {
				return getProviderTypeId(storageInfoDAO.getfilter("neq", value,
						"manager"));
			} else if (mark.equals("startswith")) {
				return getProviderTypeId(storageInfoDAO.getfilter("startswith",
						value, "manager"));
			} else if (mark.equals("endswith")) {
				return getProviderTypeId(storageInfoDAO.getfilter("endswith",
						value, "manager"));
			} else if (mark.equals("contains")) {
				return getProviderTypeId(storageInfoDAO.getfilter("contains",
						value, "manager"));
			} else if (mark.equals("doesnotcontain")) {
				return getProviderTypeId(storageInfoDAO.getfilter(
						"doesnotcontain", value, "manager"));
			}
		}
		if (field.equals("no")) {
			return FilterUtils.getConditionStr("e.no", mark, value);
		}
		return null;
	}

	private String getProviderTypeId(List<StorageInfo> serviceProviderId) {
		List<String> searchId = new ArrayList<String>();
		for (int i = 0; i < serviceProviderId.size(); i++) {
			searchId.add(new String(serviceProviderId.get(i).getNo()));
		}
		return FilterUtils.FieldConfigureStr(searchId, "e.no");
	}
	// end
	
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
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<StorageInfo> getStorageByProductIdOrInstanceId(Long productId,Long instanceId,Long organization) {	
		return storageInfoDAO.getStorageByProductIdOrInstanceId(productId, instanceId, organization);
	}

	@Override
	public StorageInfoDAO getDAO() {
		return storageInfoDAO;
	}
	/**
	 * 校验仓库是否使用
	 * @param storage
	 * @param organization
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean judgeIsUsed(StorageInfo storage, Long organization)
			throws ServiceException {
		boolean flag =  true;
		try {
			StorageInfo storageInfo = storageInfoDAO.findById(storage.getNo());
			String condition = " WHERE e.organization = ?1 AND e.firstStorage.no = ?2";
			String condition1 = " WHERE e.organization = ?1 AND  e.id.no_1 = ?2";	
			Object[] params = new Object[]{organization,storageInfo.getNo()};
			long countInitializeProduct = initializeProductDAO.count(condition, params);
			if(countInitializeProduct>0){
				return false;
			}
			long countMerchandiseStorageInfo = merchandiseStorageInfoDAO.count(condition1, params);
			if(countMerchandiseStorageInfo>0){
				return false;
			}
			long countReceivingNote = receivingNoteDAO.countByStorageUsed(storageInfo.getName(), organization);
			if(countReceivingNote>0){
				return false;
			}
		} catch (JPAException jpae) {
			throw new ServiceException("[JPA]StorageInfoServiceImpl.judgeIsUsed()"+jpae.getMessage(),jpae);
		} catch (DaoException e) {
			throw new ServiceException("[DAO]StorageInfoServiceImpl.judgeIsUsed()"+e.getMessage(),e);
		}
		return flag;
	}
}
