/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gettec.fsnip.fsn.service.sales;

import java.io.File;
import java.util.List;

import org.springframework.ui.Model;

import com.gettec.fsnip.fsn.dao.sales.SalesResourceDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * Create Date 2015-04-24
 * @author HY
 */
public interface SalesResourceService extends BaseService<SalesResource,SalesResourceDAO>{

	SalesResource saveContractResource(SalesResource salesRes, AuthenticateInfo info) throws ServiceException;

	List<SalesResource> getListResourceByGUID(String guid) throws ServiceException;

	ResultVO removeResourceById(Long id, AuthenticateInfo info) throws ServiceException;

	ResultVO removeResourceByGUID(String guid, AuthenticateInfo info) throws ServiceException;

	ResultVO saveSalesCaseResource(List<SalesResource> salesResList, String guid,
								   AuthenticateInfo info)throws ServiceException;

	ResultVO saveListResourceByFtppath(List<SalesResource> listSalesRes,
			String guid, AuthenticateInfo info, String ftpPath) throws ServiceException;

	Model getListContractsAndElectMater(AuthenticateInfo info, Model model)throws ServiceException;

	File downLoadElectData(String resId, String enterName)throws ServiceException;

	ResultVO updateSortResource(List<SalesResource> listResource) throws ServiceException;
}
