package com.gettec.fsnip.fsn.dao.erp.impl;


import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.CustomerAndProviderTypeDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;

@Repository("CustomerAndProviderTypeDAO")
public class CustomerAndProviderTypeDAOImpl extends BaseDAOImpl<CustomerAndProviderType> implements CustomerAndProviderTypeDAO{

	/**
	 * 获取客户自定义类型
	 * @param bid   客户企业id
	 * @param type  客户类型
	 * @param organization
	 * @return
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public CustomerAndProviderType findByBid(Long bid, Long type, Long organization) {
		String contype = "";
		if(type != null) {
			contype = " and b.type =:type ";
		}
		String sql = "SELECT t.id,t.name,t.type FROM t_meta_customer_and_provider_type t LEFT JOIN t_meta_business_diy_type b" +
				" on t.ID = b.type_id where b.organization = t.organization and b.business_id =:bid  " + contype ;
		if (null != organization) {
		sql+="and b.organization=:organization";
		}
		Query query  = entityManager.createNativeQuery(sql);
		query.setParameter("bid", bid);
		if(type != null) {
			query.setParameter("type", type);	
		}
		if (null != organization) {
			query.setParameter("organization", organization);
		}
		CustomerAndProviderType cpt = null;
		List<Object[]> objs = query.getResultList();
		if(objs != null && objs.size() > 0){
			for(Object[] obj : objs){
				int typeId = (obj[2] != null ? Integer.parseInt(obj[2].toString()) : 0);
				cpt = new CustomerAndProviderType(Long.parseLong(obj[0].toString()),obj[1].toString(), typeId, null);
			}
		}
		return cpt;
	}

}
