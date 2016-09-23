package com.gettec.fsnip.fsn.dao.market.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.market.ResourceTypeDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.market.ResourceType;

@Repository(value="resourceTypeDAO")
public class ResourceTypeDAOImpl  extends BaseDAOImpl<ResourceType>
			implements ResourceTypeDAO{
	/**
	 * 按typeName查找一条资源类型信息
	 * @throws DaoException
	 */
	@Override
	public ResourceType findByName(String typeName) throws DaoException {
		try {
			String condition = " WHERE e.contentType = ?1";
			List<ResourceType> resultList = this.getListByCondition(condition, new Object[]{typeName});
			if(resultList.size() > 0){
				return resultList.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按typeName查找一条资源类型信息，出现异常", jpae.getException());
		}
	}
}
