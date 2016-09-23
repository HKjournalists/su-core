/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gettec.fsnip.fsn.dao.sales;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.sales.SalesCase;
import com.gettec.fsnip.fsn.vo.sales.SalesCaseVO;

/**
 * Create Date 2015-04-24
 * @author HY
 */
public interface SalesCaseDAO extends BaseDAO<SalesCase>{

    long countByConfigure(Long organization, String configure) throws DaoException;

    List<SalesCaseVO> getListByOrganizationWithPage(Long organization, String configure,
                    Integer page, Integer pageSize)throws DaoException;

    long countByName(String name, Long organization, Long id)throws DaoException;

    List<SalesCaseVO> getListEntityByBusId(Long busId)throws DaoException;

    List<SalesCase> getListEntityByOrg(Long org)throws DaoException;



}
