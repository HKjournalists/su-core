/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gettec.fsnip.fsn.service.sales;

import java.util.List;

import com.gettec.fsnip.fsn.dao.sales.PromotionCaseDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.PromotionCase;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.PromotionCaseVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * Create Date 2015-04-24
 * @author HY
 */
public interface PromotionCaseService extends BaseService<PromotionCase,PromotionCaseDAO>{

	long countByConfigure(Long organization, String configure) throws ServiceException;

	long countByName(String name, Long organization, Long id) throws ServiceException;

	List<PromotionCaseVO> getListByOrganizationWithPage(Long organization, String configure, Integer page, Integer pageSize)
			throws ServiceException;

	ResultVO removeById(Long id, AuthenticateInfo info) throws ServiceException;

	ResultVO save(PromotionCaseVO promotionVO, AuthenticateInfo info, boolean isNew) throws ServiceException;
    
}
