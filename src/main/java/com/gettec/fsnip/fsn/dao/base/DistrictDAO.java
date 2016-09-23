package com.gettec.fsnip.fsn.dao.base;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.base.District;

public interface DistrictDAO extends BaseDAO<District>{

	List<District> getListByDescription(String description) throws DaoException;
}
