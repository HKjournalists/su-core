package com.gettec.fsnip.fsn.service.sales.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.sales.ContractDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.Contract;
import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.sales.ContractService;
import com.gettec.fsnip.fsn.service.sales.SalesResourceService;
import com.gettec.fsnip.fsn.util.FileUtils;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.ContractVO;
import com.gettec.fsnip.fsn.vo.sales.ContractVOAPP;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 电子合同service层
 * @author tangxin 2015/04/24
 *
 */
@Service(value = "contractService")
public class ContractServiceImpl extends
		BaseServiceImpl<Contract, ContractDAO> implements ContractService {
	
	@Autowired private ContractDAO contractDAO;
	@Autowired private SalesResourceService salesResourceService;
	@Autowired private BusinessUnitService businessUnitService;

	/**
     * 新增 or 更新 电子合同 信息
     * @author tangxin 2015-04-29
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ContractVO save(ContractVO contractVO, AuthenticateInfo info, boolean isNew) throws ServiceException{
		try{
			if(contractVO == null) {
				return contractVO;
			}
			Contract contract = null;
			if(!isNew) {
				contract = getDAO().findById(contractVO.getId());
			}
			contract = contractVO.toEntity(contract);
			contract.setUpdateTime(new Date());
			contract.setUpdateUser(info.getUserName());
			if(isNew) {
				Long busId = businessUnitService.findIdByOrg(info.getOrganization());
				contract.setCreateTime(new Date());
				contract.setCreateUser(info.getUserName());
				contract.setGuid(SalesUtil.createGUID());
				contract.setBusinessId(busId);
				contract.setOrganization(info.getOrganization());
				contract.setDelStatus(0);
				create(contract);
				contractVO.setId(contract.getId());
			}else{
				update(contract);
			}
			SalesResource salesRes = contractVO.getResource();
			if(salesRes != null) {
				/**
				 * 保存和更正电子合同时，都将资源名称更改为同名名称
				 */
				String ext = FileUtils.getExtension(salesRes.getFileName());
				salesRes.setFileName(contract.getName() + "." + ext);
				salesRes.setGuid(contract.getGuid());
				salesRes = salesResourceService.saveContractResource(salesRes, info);
			} else {
				/* 删除合同资源 */
				salesResourceService.removeResourceByGUID(contract.getGuid(), info);
			}
			contractVO.setResource(salesRes);
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
		return contractVO;
	}
	
    /**
   	 * 根据筛选条件统计企业合同数量
   	 * @author tangxin 2015-04-29
   	 */
   	@Override
   	public long countByConfigure(Long organization, String configure) throws ServiceException {
   		try{
   			return this.getDAO().countByConfigure(organization, getConfigure(configure));
   		}catch(DaoException daoe){
   			throw new ServiceException(daoe.getMessage(), daoe.getException());
   		}
   	}

   	/**
   	 * 根据企业合同名称统计数量，验证名称时候重复用
   	 * @author tangxin 2015-04-29
   	 */
   	@Override
   	public long countByName(String name, Long organization, Long id) throws ServiceException {
   		try{
   			return this.getDAO().countByName(name, organization, id);
   		}catch(DaoException daoe){
   			throw new ServiceException(daoe.getMessage(), daoe.getException());
   		}
   	}
   	
   	/**
   	 * 根据筛选条件分页查电子合同信息
   	 * @author tangxin 2015-04-29
   	 */
   	@Override
   	public List<ContractVO> getListByOrganizationWithPage(Long organization, String configure, Integer page, Integer pageSize)
   			throws ServiceException {
   		try{
   			return this.getDAO().getListByOrganizationWithPage(organization, getConfigure(configure), page, pageSize);
   		}catch(DaoException daoe){
   			throw new ServiceException(daoe.getMessage(), daoe.getException());
   		}
   	}

   	/**
   	 * 企业APP 获取企业电子合同信息
   	 * @author tangxin 2015-05-10
   	 */
   	@Override
   	public List<ContractVOAPP> getListForAPPWithPage(Long organization, String configure, Integer page, Integer pageSize)
   			throws ServiceException {
   		try{
   			return this.getDAO().getListForAPPWithPage(organization, getConfigure(configure), page, pageSize);
   		}catch(DaoException daoe){
   			throw new ServiceException(daoe.getMessage(), daoe.getException());
   		}
   	}
   	
   	
   	
   	/**
   	 * 根据id删除电子合同信息(假删除)
   	 * @author tangxin 2015-04-29
   	 */
   	@Override
   	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
   	public ResultVO removeById(Long contractId, AuthenticateInfo info) throws ServiceException {
   		ResultVO resultVO = new ResultVO();
   		if(contractId == null) {
   			resultVO.setStatus("false");
   			resultVO.setErrorMessage("删除失败,被删除网点的id为空！");
   			return resultVO;
   		}
   		Contract orig_contract = findById(contractId);
   		if(orig_contract != null) {
   			orig_contract.setDelStatus(1);
   			orig_contract.setUpdateTime(new Date());
   			orig_contract.setUpdateUser(info.getUserName());
   			resultVO = salesResourceService.removeResourceByGUID(orig_contract.getGuid(), info);
   			update(orig_contract);
   		} else {
   			resultVO.setStatus("false");
   			resultVO.setErrorMessage("删除失败,被删除的合同信息不存在！");
   		}
   		return resultVO;
   	}

	/**
	 * 拼接configure条件（纯sql语句）
	 * @author tangxin 2015-04-29
	 */
	private String getConfigure(String configure) throws ServiceException{
		String new_configure = "";
		if(configure == null) {
			return " WHERE 1=1 ";
		}
		String filter[] = configure.split("@@");
		for(int i=0;i<filter.length;i++){
			String filters[] = filter[i].split("@");
			if(filters.length > 3){
				try {
					String config = splitJointConfigure(filters[0],filters[1],filters[2], true);
					if(config==null){
						continue;
					}
					if(i==0){
						new_configure = new_configure + " WHERE " + config;
					}else{
						new_configure = new_configure +" AND " + config;
					}
				} catch (Exception e) {
				e.printStackTrace();
				}
			}
		}
		new_configure = (new_configure.equals("") ? " WHERE 1=1 " : new_configure);
	    return new_configure;
	}
	
	/**
	 * 分割页面的过滤信息
	 * @author tangxin 2015-04-29
	 */
	private String splitJointConfigure(String field, String mark, String value, boolean isIgnoreBrand) throws ServiceException{
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException("【Service-Error】数据筛选时，利用utf-8解码筛选条件时出现异常！",e);
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("contract.id",mark,value);
		}
		if(field.equals("name")){
			return FilterUtils.getConditionStr("contract.name",mark,value);
		}
		if(field.equals("remark")){
			return FilterUtils.getConditionStr("contract.remark",mark,value);
		}
		if(field.equals("updateTime")){
			return FilterUtils.getConditionStr("contract.update_time",mark,value);
		}
		return null;
	}
    
	@Override
	public ContractDAO getDAO() {
		return contractDAO;
	}

}
