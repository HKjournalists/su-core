/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gettec.fsnip.fsn.service.sales;

import java.util.List;

import com.gettec.fsnip.fsn.dao.sales.SalesCaseDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.SalesCase;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.AlbumVO;
import com.gettec.fsnip.fsn.vo.sales.SalesCaseVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * Create Date 2015-04-24
 * @author HY
 */
public interface SalesCaseService extends BaseService<SalesCase,SalesCaseDAO>{

    long countByConfigure(Long organization, String configure) throws ServiceException;

    List<SalesCaseVO> getListByOrganizationWithPage(Long organization, String configure, Integer page,
                                                    Integer pageSize)throws ServiceException;

    SalesCaseVO save(SalesCaseVO salesCaseVO, AuthenticateInfo info, boolean b)throws ServiceException;

    long countByName(String name, Long organization, Long id)throws ServiceException;

    ResultVO removeById(Long id, AuthenticateInfo info)throws ServiceException;

    List<SalesCaseVO> getListEntityByBusId(Long busId)throws ServiceException;

    AlbumVO getSalesCaseAlbum(Long id, String cut)throws ServiceException;

	ResultVO updateSortCase(List<SalesCase> listCase) throws ServiceException;
}
