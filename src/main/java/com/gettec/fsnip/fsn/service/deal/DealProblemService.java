package com.gettec.fsnip.fsn.service.deal;

import java.util.List;

import com.gettec.fsnip.fsn.dao.deal.DealProblemDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.deal.DealProblem;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface DealProblemService extends BaseService<DealProblem,DealProblemDAO>{

	void save(DealProblem dealProblem) throws ServiceException;

	long getTotalRecord(String businewssName, String licenseNo, String barcode,long businessId,int status) throws DaoException;

	List<DealProblem> getProblemList(int page, int pageSize,
			String businewssName, String licenseNo, String barcode,long businessId,int status) throws DaoException;

	DealProblem  noticeComplainById(long id,long status);

	void deleteDealProblemById(long id);

	void backComplain(long id);


}
