package com.gettec.fsnip.fsn.dao.base;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.base.Office;

public interface OfficeDAO extends BaseDAO<Office>{

	List<Office> getListByParentId(Long parentId) throws DaoException;
}
