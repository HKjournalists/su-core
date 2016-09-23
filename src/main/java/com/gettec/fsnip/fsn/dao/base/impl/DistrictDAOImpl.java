package com.gettec.fsnip.fsn.dao.base.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.base.DistrictDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.base.District;

@Repository(value="districtDAO")
public class DistrictDAOImpl extends BaseDAOImpl<District>
		implements DistrictDAO{

	/**
	 * 通过属性description查询District集合
	 * @param description
	 * @return List<District>
	 * @throws DaoException
	 */
	@Override
	public List<District> getListByDescription(String description)
			throws DaoException {
		try{
			String condition = " WHERE e.description = ?1";
			return this.getListByCondition(condition, new Object[]{description});
		}catch(JPAException jpae){
			throw new DaoException("DistrictDAOImpl.getListByDescription()-->" + jpae.getMessage(), jpae.getException());
		}
	}
}
