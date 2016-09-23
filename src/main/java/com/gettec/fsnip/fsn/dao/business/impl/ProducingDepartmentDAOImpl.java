package com.gettec.fsnip.fsn.dao.business.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.business.ProducingDepartmentDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.ProducingDepartment;

/**
 * producingDepartmentDAO dao implementation
 * @author xianzhen long
 */
@Repository(value="producingDepartmentDAO")
public class ProducingDepartmentDAOImpl extends BaseDAOImpl<ProducingDepartment> 
		implements ProducingDepartmentDAO {

	@Override
	public List<ProducingDepartment> getListByBusunitIdAndDepartFlag(
			long busunitId, boolean departFlag) throws DaoException {
		try {
			String condition = " WHERE e.businessId = ?1 AND e.departmentFlag = ?2";
			return this.getListByCondition(condition, new Object[]{busunitId, departFlag});
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】getListByBusunitIdAndDepartFlag方法，出现异常", jpae.getException());
		}
	}
	
}