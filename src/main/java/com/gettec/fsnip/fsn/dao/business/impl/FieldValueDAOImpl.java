package com.gettec.fsnip.fsn.dao.business.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.business.FieldValueDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.FieldValue;

/**
 * @author Hui Zhang
 */
@Repository(value="fieldValueDAO")
public class FieldValueDAOImpl extends BaseDAOImpl<FieldValue> implements FieldValueDAO {

	@Override
	public List<FieldValue> getListByBusunitIdAndFieldId(long fieldId, long busunitId) throws DaoException {
		try {
			String condition = " WHERE e.busunitId = ?1 AND e.field.id = ?2";
			return this.getListByCondition(condition, new Object[]{busunitId, fieldId});
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】findByBusunitIdAndFieldId方法，出现异常", jpae.getException());
		}
	}

	@Override
	public List<FieldValue> getListByBusunitId(long busunitId) throws DaoException {
		try {
			String condition = " WHERE e.busunitId = ?1";
			return this.getListByCondition(condition, new Object[]{busunitId});
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】getListByBusunitId方法，出现异常", jpae.getException());
		}
	}

	@Override
	public FieldValue findByBusunitIdAndFieldIdAndValue(long fieldId,
			long busunitId, String fieldValue) throws DaoException {
		try {
			String condition = " WHERE e.field.id = ?1 AND e.busunitId = ?2 AND e.value = ?3";
			List<FieldValue> result = this.getListByCondition(condition, new Object[]{fieldId, busunitId, fieldValue});
			if(result.size() > 0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】findByBusunitIdAndFieldIdAndValue方法，出现异常", jpae.getException());
		}
	}
}