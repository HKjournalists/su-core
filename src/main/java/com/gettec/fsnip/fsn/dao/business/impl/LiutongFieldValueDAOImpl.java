package com.gettec.fsnip.fsn.dao.business.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.business.LiutongFieldValueDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.LiutongFieldValue;

@Repository(value="liutongFieldValueDAO")
public class LiutongFieldValueDAOImpl extends BaseDAOImpl<LiutongFieldValue> 
		implements LiutongFieldValueDAO {

	/**
	 * 根据生产企业id，组织机构代码(营业执照号),查找资源id
	 * @author TangXin 
	 */
	@Override
	public List<Long> getResourceIds(Long producerId, String value,
			String disaply) throws DaoException {
		try {
			String sql = "SELECT tr.resource_id from t_liutong_field_to_resource tr RIGHT JOIN t_liutong_field_value tv " +
					"ON tr.liutong_field_id = tv.id WHERE tv.produce_bus_id = ?1 AND tv.`value` = ?2 and tv.display = ?3";
			return this.getListBySQLWithoutType(Long.class, sql, new Object[]{producerId, value, disaply});
		} catch (JPAException e) {
			throw new DaoException(e.getMessage(), e);
		}
	}

}
