package com.gettec.fsnip.fsn.dao.deal;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.enums.DealProblemTypeEnums;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.deal.DealProblem;

public interface DealProblemDAO extends BaseDAO<DealProblem>{

	List<DealProblem> getProblemList(int page, int pageSize, String businewssName, String licenseNo,
			String barcode,long businessId,DealProblemTypeEnums complainStatus) throws DaoException;

	long getTotalRecord(String businewssName, String licenseNo, String barcode,long businessId,int complainStatus) throws DaoException;

}
