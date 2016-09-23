package com.gettec.fsnip.fsn.dao.market.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.market.MkTempBusUnitDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.market.MkTempBusUnit;

@Repository(value="tempBusUnitDAO")
public class MkTempBusUnitDAOImpl extends BaseDAOImpl<MkTempBusUnit> 
				implements MkTempBusUnitDAO{
	/**
	 * 按用户真实名称和组织机构名称查找一条临时生产商信息
	 * @throws DaoException 
	 */
	@Override
	public MkTempBusUnit findByUserRealNameAndOrganization(String userRealName, Long organization) throws DaoException {
		try {
			String condition = " WHERE e.createUserRealName = ?1 AND e.organization = ?2";
			List<MkTempBusUnit> resultList = this.getListByCondition(condition, new Object[]{userRealName, organization});
			if( resultList.size() > 0){
				return resultList.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按用户真实名称和组织机构名称查找一条临时生产商信息，出现异常", jpae.getException());
		}
	}
	
}
