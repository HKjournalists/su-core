/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gettec.fsnip.fsn.service.sales;

import java.util.List;

import com.gettec.fsnip.fsn.dao.sales.SalesBranchDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.SalesBranch;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.SalesBranchVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * Create Date 2015-04-24
 * @author HY
 */
public interface SalesBranchService extends BaseService<SalesBranch,SalesBranchDAO>{
    
	public SalesBranchVO save(SalesBranchVO salesBranchVO, AuthenticateInfo info, boolean isNew) throws ServiceException;
	
	public long countByConfigure(Long organization, String configure) throws ServiceException;
	
	public List<SalesBranchVO> getListByOrganizationWithPage(Long organization, String configure,
			Integer page, Integer pageSize) throws ServiceException;
	
	public ResultVO removeBranchById(Long branchId, AuthenticateInfo info) throws ServiceException;

	List<String> getListAddrByType(Long organization, String keyword, String type) throws ServiceException;

	long countByName(String name, Long organization, Long id) throws ServiceException;

	List<SalesBranchVO> getListAddrByProvince(Long organization, String province) throws ServiceException;
	
}
