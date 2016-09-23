package com.gettec.fsnip.fsn.dao.sales;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.sales.Contract;
import com.gettec.fsnip.fsn.vo.sales.ContractVO;
import com.gettec.fsnip.fsn.vo.sales.ContractVOAPP;

/**
 * 电子合同DAO
 * @author tangxin 2015/04/24
 *
 */
public interface ContractDAO extends BaseDAO<Contract> {

	long countByConfigure(Long organization, String configure) throws DaoException;

	long countByName(String name, Long organization, Long id) throws DaoException;

	List<ContractVO> getListByOrganizationWithPage(Long organization, String condition, Integer page, Integer pageSize)
			throws DaoException;

	List<ContractVOAPP> getListForAPPWithPage(Long organization, String configure, Integer page, Integer pageSize)
			throws DaoException;
}
