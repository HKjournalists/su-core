/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gettec.fsnip.fsn.dao.sales;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.sales.PromotionCase;
import com.gettec.fsnip.fsn.vo.sales.PromotionCaseVO;

/**
 * Create Date 2015-04-24
 * @author HY
 */
public interface PromotionCaseDAO extends BaseDAO<PromotionCase>{

	long countByConfigure(Long organization, String configure) throws DaoException;

	long countByName(String name, Long organization, Long id) throws DaoException;

	List<PromotionCaseVO> getListByOrganizationWithPage(Long organization,
			String condition, Integer page, Integer pageSize) throws DaoException;
    
}
