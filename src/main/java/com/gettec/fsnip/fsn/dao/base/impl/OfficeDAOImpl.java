package com.gettec.fsnip.fsn.dao.base.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.base.OfficeDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.base.Office;

@Repository(value = "officeDAO")
public class OfficeDAOImpl extends BaseDAOImpl<Office>
		implements OfficeDAO {
	/**
	 * 
	 * @param parentId
	 * @return List<Office>
	 * @throws DaoException
	 */
	@Override
	public List<Office> getListByParentId(Long parentId) throws DaoException {
		try{
			String condition = " where e.parentId = ?1";
			return this.getListByCondition(condition, new Object[]{parentId});
		}catch(JPAException jpae){
			throw new DaoException("OfficeDAOImpl.getListByParentId()-->" + jpae.getMessage(),jpae.getException());
		}
	}
}
