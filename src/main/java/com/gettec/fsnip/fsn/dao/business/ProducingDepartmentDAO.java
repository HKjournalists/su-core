package com.gettec.fsnip.fsn.dao.business;

import java.util.List;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.ProducingDepartment;

public interface ProducingDepartmentDAO extends BaseDAO<ProducingDepartment>{

	List<ProducingDepartment> getListByBusunitIdAndDepartFlag(long busunitId, boolean DepartFlag) throws DaoException;
	
}