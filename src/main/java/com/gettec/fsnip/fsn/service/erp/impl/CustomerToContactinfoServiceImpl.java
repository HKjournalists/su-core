package com.gettec.fsnip.fsn.service.erp.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.CustomerToContactinfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.CustomerToContactinfo;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.CustomerToContactinfoService;

@Service("customerToContactinfoService")
public class CustomerToContactinfoServiceImpl extends BaseServiceImpl<CustomerToContactinfo, CustomerToContactinfoDAO>
		implements CustomerToContactinfoService {

	@Autowired
	private CustomerToContactinfoDAO customerToContactinfoDAO;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CustomerToContactinfo> addRelationShips(
			List<CustomerToContactinfo> infos) {
		try {
			for(CustomerToContactinfo info : infos){
				if(null != info.getId() && info.getId().getContactID() != null &&
						String.valueOf(info.getId().getCustomerNo()) != null){
					customerToContactinfoDAO.persistent(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}
	
	/**
	 * 批量新增客户的联系人关系
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(List<CustomerToContactinfo> adds) throws ServiceException {
		try {
			for(CustomerToContactinfo info : adds){
				create(info);
			}
		} catch (ServiceException sex) {
			throw new ServiceException("CustomerToContactinfoServiceImpl.save()-->", sex.getException());
		}
	}

	/**
	 * 根据企业id和所说类型查询该企业的联系人关系列表
	 * @param id
	 * @param type
	 * @return
	 * @throws DaoException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CustomerToContactinfo> getListByIdAndType(Long id,int type) throws ServiceException {
		try {
			return customerToContactinfoDAO.getListByIdAndType(id,type);
		} catch (DaoException dex) {
			throw new ServiceException("CustomerToContactinfoServiceImpl.getListByIdAndType()-->", dex.getException());
		}
	}

	@Override
	public CustomerToContactinfoDAO getDAO() {
		return customerToContactinfoDAO;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void deleteByCIdAndOrgId(Long cId, Long organization)
			throws ServiceException {
		try {
			 customerToContactinfoDAO.deleteByCIdAndOrgId(cId,organization);
		} catch (DaoException dex) {
			throw new ServiceException("CustomerToContactinfoServiceImpl.getListByIdAndType()-->", dex.getException());
		}
	}
}
