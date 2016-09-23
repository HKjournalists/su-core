/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gettec.fsnip.fsn.service.sales;

import com.gettec.fsnip.fsn.dao.sales.BusinessSalesInfoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.sales.BusinessSalesInfo;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.business.BusinessSalesVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.vo.sales.EnterpriseIntroductionVO;
import com.lhfs.fsn.vo.sales.EnterpriseViewImgVO;

/**
 * Create Date 2015-04-24
 * @author HY
 */
public interface BusinessSalesInfoService extends BaseService<BusinessSalesInfo, BusinessSalesInfoDAO>{

	EnterpriseViewImgVO findEnterpriseViewImgByOrganization(Long organization, boolean description) throws ServiceException;

	EnterpriseIntroductionVO findEnterpriseIntroductionByOrganization(Long organization) throws ServiceException;

	void save(BusinessUnit businessUnit, AuthenticateInfo info)throws ServiceException;

	BusinessSalesInfo findByBusId(Long id)throws ServiceException;

	BusinessSalesVO findByOrgId(Long organization)throws ServiceException;
}
