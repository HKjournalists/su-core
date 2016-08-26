package com.gettec.fsnip.fsn.dao.market.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.market.MkTempReportDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.market.MkTempReport;

@Repository(value="tempReportDAO")
public class MkTempReportDAOImpl extends BaseDAOImpl<MkTempReport> 
					implements MkTempReportDAO{
	/**
	 * 按用户真实名称和组织机构名称查找一条临时报告信息
	 * @throws DaoException 
	 */
	@Override
	public MkTempReport findByUserRealNameAndOrganization(String userRealName, Long organization) throws DaoException {
		try {
			String condition = " WHERE e.createUserRealName = ?1 AND e.organization = ?2";
			List<MkTempReport> resultList = this.getListByCondition(condition, new Object[]{userRealName, organization});
			if(resultList.size() > 0){
				return resultList.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按用户真实名称和组织机构名称查找一条临时报告信息", jpae.getException());
		}
	}

	@Override
	public MkTempReport getTempReportOrderNo(String serviceOrder) {
		try {
			String condition = " WHERE e.reportNo = ?1 ";
			List<MkTempReport> resultList = this.getListByCondition(condition, new Object[]{serviceOrder});
			if(resultList.size() > 0){
				return resultList.get(0);
			}
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}

