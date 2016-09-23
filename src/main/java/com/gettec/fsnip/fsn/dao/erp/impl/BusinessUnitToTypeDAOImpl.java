package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.BusinessUnitToTypeDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.BusinessUnitToType;

@Repository("businessUnitToTypeDAO")
public class BusinessUnitToTypeDAOImpl extends BaseDAOImpl<BusinessUnitToType> 
				implements BusinessUnitToTypeDAO {

	/**
	 * 删除企业与自定义类型的关系
	 * @param bussid  企业id
	 * @param organization
	 * @param type  企业类型
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public void removeByBussidAndType(Long bussid, Long organization, Long type) throws DaoException {
		try {	
			String sql = "DELETE FROM t_meta_business_diy_type WHERE business_id =?1 and organization =?2 and type =?3";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, bussid);
			query.setParameter(2, organization);
			query.setParameter(3, type);
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException("BusinessUnitToTypeDAOImpl.removeByBussidAndType() 删除企业与自定义类型的关系，出现异常！", e);
		}
	}
	@Override
	public BusinessUnitToType findByBusIdAndOrgId(Long busunitId,
			Long organization, Long type) {
		try {
			String condition = " WHERE e.organization=?1 AND e.id.businessId=?2 AND e.type=?3";
			List<BusinessUnitToType> result = this.getListByCondition(condition, new Object[]{organization, busunitId,type});
			if(result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	public void updateBusinessUnitTypeByBidAndOid(Long busunitId,Long typeId,
			Long organization, Long type) {
		String sql = "update t_meta_business_diy_type SET type_id =:typeId where business_id =:busunitId AND organization =:organization and type =:type";
		Query query = entityManager.createNativeQuery(sql);
		try {
			query.setParameter("busunitId", busunitId);
			query.setParameter("typeId", typeId);
			query.setParameter("organization", organization);
			query.setParameter("type", type);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateBusinessUnitType(Long busunitId,Long typeId, Long organization, Long type) {
		String sql = "update t_meta_business_diy_type SET type_id =:typeId,type=:type where business_id =:busunitId AND organization =:organization";
		Query query = entityManager.createNativeQuery(sql);
		try {
			query.setParameter("busunitId", busunitId);
			query.setParameter("typeId", typeId);
			query.setParameter("organization", organization);
			query.setParameter("type", type);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void createBusinessUnitTypeBySql(Long busunitId,Long typeId, Long organization, Long type) {
		String sql = "insert t_meta_business_diy_type(type_id,type,business_id,organization) values(?1,?2,?3,?4)";
		Query query = entityManager.createNativeQuery(sql);
		try {
			query.setParameter("1", typeId);
			query.setParameter("2", type);
			query.setParameter("3", busunitId);
			query.setParameter("4", organization);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据组织机构和企业id查询 BusinessUnitToType 实体
	 */
	@Override
	public BusinessUnitToType findByBusIdAndOrgId(Long busunitId,Long organization) {
		try {
			String condition = " WHERE e.organization=?1 AND e.id.businessId=?2";
			List<BusinessUnitToType> result = this.getListByCondition(condition, new Object[]{organization, busunitId});
			if(result.size() > 0){
				return result.get(0);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}
