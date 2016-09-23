/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gettec.fsnip.fsn.dao.sales;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.vo.sales.ContractResVO;

/**
 * Create Date 2015-04-24
 * @author HY
 */
public interface SalesResourceDAO extends BaseDAO<SalesResource>{

	List<SalesResource> getListByGUID(String guid) throws DaoException;

	List<ContractResVO> getContResIdList(Long organization)throws DaoException;

	ContractResVO getElectDataAndRes(Long organization) throws DaoException;

	List<SalesResource> getListByResIds(String resId) throws DaoException;
}
