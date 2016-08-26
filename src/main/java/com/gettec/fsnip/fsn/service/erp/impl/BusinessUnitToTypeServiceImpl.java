package com.gettec.fsnip.fsn.service.erp.impl;

import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.erp.BusinessUnitToTypeDAO;
import com.gettec.fsnip.fsn.dao.erp.CustomerTypeDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.erp.base.BusinessUnitToType;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.BusinessUnitToTypeService;
import com.gettec.fsnip.fsn.vo.ResultVO;

@Service("businessUnitToTypeService")
public class BusinessUnitToTypeServiceImpl extends BaseServiceImpl<BusinessUnitToType, BusinessUnitToTypeDAO> 
		implements BusinessUnitToTypeService{

	@Autowired
	private BusinessUnitToTypeDAO businessUnitToTypeDAO;
	@Autowired 
	private CustomerTypeDAO customerTypeDAO;
	@Override
	public BusinessUnitToTypeDAO getDAO() {
		return businessUnitToTypeDAO;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(BusinessUnitToType buToType) throws ServiceException {
		try {
			businessUnitToTypeDAO.persistent(buToType);
		} catch (JPAException e) {
			throw new ServiceException("BusinessUnitToTypeServiceImpl.save()", e.getException());
		}
		
	}

	/**
	 * 保存客户类型
	 * @param customer
	 * @param organization
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultVO saveType(BusinessUnit customer, Long organization) throws ServiceException{
		try{
			ResultVO resultVO = new ResultVO();
			resultVO.setObject(customer);
			// 供应商 OR 客户类型
			CustomerAndProviderType new_type = customer.getDiyType();
			if(new_type != null && new_type.getId() != null) {
				// 获取当成登陆用户所属企业 客户类型
				CustomerAndProviderType orig_type = customerTypeDAO.findById(new_type.getId());
				if(orig_type != null){
					/* 保存客户与客户类型的关联 */
					customer.addRelationShipBusinessUnitToType(orig_type);
					customer.getBuToType().setOrganization(organization);
					customer.getBuToType().setType(orig_type.getType() == 1 ? 1L : 2L);
					save(customer.getBuToType());
				}else{
					resultVO.setErrorMessage("PROVIDER_TYTPE_NULL");
					resultVO.setStatus(SERVER_STATUS_FAILED);
					return resultVO;
				}
			}else if(new_type != null && new_type.getType() != null) {
				/*
				 * 客户类型是新增状态，查询同名的客户类型，如果不存在则需要新增。
				 */
				CustomerAndProviderType orig_type = customerTypeDAO.findByCustomerAndOrganization(organization, new_type.getType(), new_type.getName());
				if(orig_type == null){
					// 新增客户类型
					new_type.setOrganization(organization);
					customerTypeDAO.persistent(new_type);
					orig_type = new_type;
				}
				// 添加客户类型 和 客户 以及当前供应商之间的关系
				customer.addRelationShipBusinessUnitToType(orig_type);
				customer.getBuToType().setOrganization(organization);
				customer.getBuToType().setType(orig_type.getType() == 1 ? 1L : 2L);//客户类型，1代表产品销往客户，2代表产品来源客户
				// 获取当前登陆企业 已添加的 客户类型关系，如果为null，则需要新增，否则更新
				BusinessUnitToType orig_ctye = getDAO().findByBusIdAndOrgId(customer.getId(), organization);
				if(orig_ctye == null) {
					orig_ctye = customer.getBuToType();
					save(orig_ctye);
				} else {
					getDAO().updateBusinessUnitType(customer.getId(), orig_type.getId(),organization,customer.getBuToType().getType());
				}
			}
			return resultVO;
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	/**
	 * 按客户/供应商id和组织机构id查找一条记录
	 * type:1代表客户，2代表供应商
	 * @return 
	 */

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BusinessUnitToType findByBusIdAndOrgIdAndType(Long busunitId,
			Long organization, Long type) {
		return businessUnitToTypeDAO.findByBusIdAndOrgId(busunitId, organization,type);
	}
}