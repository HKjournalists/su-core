package com.gettec.fsnip.fsn.dao.sales;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.sales.BusinessSalesInfo;
import com.lhfs.fsn.vo.sales.EnterpriseIntroductionVO;
import com.lhfs.fsn.vo.sales.EnterpriseViewImgVO;

/**
 * Create Date 2015-04-24
 * @author HY
 */
public interface BusinessSalesInfoDAO extends BaseDAO<BusinessSalesInfo>{

	EnterpriseViewImgVO findEnterpriseViewImgByOrganization(Long organization, boolean description) throws DaoException;

	EnterpriseIntroductionVO findEnterpriseIntroductionByOrganization(Long organization) throws DaoException;

	BusinessSalesInfo findByBusId(Long id) throws DaoException;
}
