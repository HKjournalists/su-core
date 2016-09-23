package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.CustomerToContactinfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.base.CustomerToContactinfo;

@Repository("customerToContactinfoDAO")
public class CustomerToContactinfoDAOImpl extends BaseDAOImpl<CustomerToContactinfo> 
		implements CustomerToContactinfoDAO{

	/**
	 * 根据企业id和所说类型查询该企业的联系人关系列表
	 * @param id
	 * @param type
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public List<CustomerToContactinfo> getListByIdAndType(Long id, int type) throws DaoException {
		try {
			String condition = " WHERE e.id.customerNo = ?1 and e.type = ?2";
			return this.getListByCondition(condition, new Object[]{id, type});
		} catch (JPAException jpae) {
			throw new DaoException("CustomerToContactinfoDAOImpl.getListByIdAndType() 根据企业id和所说类型查询该企业的联系人关系列表,出现异常！", jpae.getException());
		}
	}

	@Override
	public void deleteByCIdAndOrgId(Long cId, Long organization)
			throws DaoException {
		try {
			String sql = " delete from t_meta_customer_to_contact where " +
									"CUSTOMER_NO=?1 AND CONTACT_ID IN(" +
									"select id from t_meta_contact_info where organization=?2)";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, cId);
			query.setParameter(2, organization);
			query.executeUpdate();
		} catch (Exception jpae) {
			throw new DaoException("CustomerToContactinfoDAOImpl.deleteByCIdAndOrgId()", jpae);
		}
	}
}
