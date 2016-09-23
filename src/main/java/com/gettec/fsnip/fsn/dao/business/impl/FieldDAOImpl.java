package com.gettec.fsnip.fsn.dao.business.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.business.FieldDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.Field;

/**
 * @author Hui Zhang
 */
@Repository(value="fieldDAO")
public class FieldDAOImpl extends BaseDAOImpl<Field> implements FieldDAO {

	@Override
	public Field findByName(String fieldName) throws DaoException {
		try {
			String condition = " WHERE e.fieldName = ?1";
			List<Field> result = this.getListByCondition(condition, new Object[]{fieldName});
			if(result.size() > 0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】findByName方法，出现异常", jpae.getException());
		}
	}
}