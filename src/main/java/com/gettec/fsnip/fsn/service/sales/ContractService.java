package com.gettec.fsnip.fsn.service.sales;

import java.util.List;

import com.gettec.fsnip.fsn.dao.sales.ContractDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.Contract;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.ContractVO;
import com.gettec.fsnip.fsn.vo.sales.ContractVOAPP;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 电子合同service层
 * @author tangxin 2015/04/24
 *
 */
public interface ContractService extends BaseService<Contract, ContractDAO> {

	ContractVO save(ContractVO contractVO, AuthenticateInfo info, boolean isNew) throws ServiceException;

	long countByConfigure(Long organization, String configure) throws ServiceException;

	long countByName(String name, Long organization, Long id) throws ServiceException;

	List<ContractVO> getListByOrganizationWithPage(Long organization, String configure, Integer page, Integer pageSize)
			throws ServiceException;

	ResultVO removeById(Long contractId, AuthenticateInfo info) throws ServiceException;

	List<ContractVOAPP> getListForAPPWithPage(Long organization,String configure, Integer page, Integer pageSize)
			throws ServiceException;

}
