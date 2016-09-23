package com.gettec.fsnip.fsn.dao.erp.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.ProviderDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.util.FilterUtils;

@Repository("providerDAO")
public class ProviderDAOImpl extends BaseDAOImpl<BusinessUnit> implements
		ProviderDAO {

	public long countProvider(String configure) {
		String jpql = "SELECT count(*) FROM business_unit e ";
		if (configure != null) {
			jpql = jpql + configure;
		}
		Query query = entityManager.createQuery(jpql);
		Object rtn = query.getSingleResult();
		return rtn == null ? 0 : Long.parseLong(rtn.toString());
	}

	@SuppressWarnings("unchecked")
	public List<BusinessUnit> getfilter(String filter, String name,
			String fieldName, List<Long> listId) {
		String sql = "SELECT e FROM business_unit e";

		Query query = null;
		if (filter.equals("eq")) {
			sql = sql + " WHERE " + fieldName + " = :name and e.id in (:ids) ";
			query = entityManager.createQuery(sql);
			query.setParameter("name", name);
			query.setParameter("ids", listId);
		} else if (filter.equals("neq")) {
			sql = sql + " WHERE " + fieldName + " <> :name and e.id in (:ids) ";
			query = entityManager.createQuery(sql);
			query.setParameter("name", name);
			query.setParameter("ids", listId);
		} else if (filter.equals("startswith")) {
			sql = sql + " WHERE " + fieldName
					+ " like :name and e.id in (:ids) ";
			query = entityManager.createQuery(sql);
			query.setParameter("name", name + "%");
			query.setParameter("ids", listId);
		} else if (filter.equals("endswith")) {
			sql = sql + " WHERE " + fieldName
					+ " like :name and e.id in (:ids) ";
			query = entityManager.createQuery(sql);
			query.setParameter("name", "%" + name);
			query.setParameter("ids", listId);
		} else if (filter.equals("contains")) {
			sql = sql + " WHERE " + fieldName
					+ " like :name and e.id in (:ids)";
			query = entityManager.createQuery(sql);
			query.setParameter("name", "%" + name + "%");
			query.setParameter("ids", listId);
		} else if (filter.equals("doesnotcontain")) {
			sql = sql + " WHERE " + fieldName
					+ " not like :name and e.id in (:ids) ";
			query = entityManager.createQuery(sql);
			query.setParameter("name", "%" + name + "%");
			query.setParameter("ids", listId);
		}
		return query.getResultList();
	}

	/**
	 * 根据过滤条件查询当前登录企业的供应商列表信息
	 * 
	 * @param configure
	 * @param organization
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessUnit> getListByConditionAndOrgId(String configure,
			Long organization, int page, int pageSize) throws DaoException {

		try {
			String sql = "SELECT DISTINCT buu.id,buu.name,buu.telephone,buu.note,buu.license_no,buu.organization FROM business_unit bu "
					+ "LEFT JOIN t_meta_enterprise_to_provider ep ON bu.id=ep.business_id "
					+ "LEFT JOIN business_unit buu ON ep.provider_id=buu.id "
					+ "WHERE buu.id is not null ";
			if (organization != null) {
				sql += " AND bu.organization=?1 ";
			}
			if (configure == null || configure.equals("")) {
				sql += "  ";
			} else {
				configure = configure.replaceAll("e#Provider#e.", "buu.")
						.replaceAll("WHERE", " ");
				sql += " AND " + configure;
			}
			Query query = entityManager.createNativeQuery(sql);
			if (organization != null) {
				query.setParameter(1, organization);
			}
			if (page > 0 || pageSize > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<BusinessUnit> buss = new ArrayList<BusinessUnit>();
			if (result != null) {
				for (Object[] obj : result) {
					BusinessUnit bus = new BusinessUnit();
					bus.setId(((BigInteger) obj[0]).longValue());
					bus.setName(obj[1] == null ? "" : obj[1].toString());
					bus.setTelephone(obj[2] == null ? "" : obj[2].toString());
					bus.setNote(obj[3] == null ? "" : obj[3].toString());
					if (obj[4] != null) {
						LicenseInfo license = new LicenseInfo();
						license.setLicenseNo(obj[4].toString());
						bus.setLicense(license);
					}
					bus.setOrganization(obj[5] == null ? -1
							: ((BigInteger) obj[5]).longValue());
					buss.add(bus);
				}
			}
			return buss;
		} catch (Exception e) {
			throw new DaoException(
					"ProviderDAOImpl.getListByConditionAndOrgId() 根据过滤条件查询当前登录企业的供应商列表信息，出现异常！",
					e);
		}
	}

	/**
	 * 根据过滤条件查询当前登录企业的供应商列表总数
	 * 
	 * @param configure
	 * @param organization
	 * @return
	 * @throws DaoException
	 */
	@Override
	public Long getCountByConditionAndOrgId(String configure, Long organization)
			throws DaoException {
		try {
			String sql = "SELECT count(DISTINCT buu.id) FROM business_unit bu "
					+ "LEFT JOIN t_meta_enterprise_to_provider ep ON bu.id=ep.business_id "
					+ "LEFT JOIN business_unit buu ON ep.provider_id=buu.id "
					+ "WHERE buu.id is not null  ";
			if (organization != null) {
				sql += " AND bu.organization=?1 ";
			}
			if (configure == null || configure.equals("")) {
				sql += "  ";
			} else {
				configure = configure.replaceAll("e#Provider#e.", "buu.")
						.replaceAll("WHERE", " ");
				sql += " AND " + configure;
			}
			Query query = entityManager.createNativeQuery(sql);
			if (organization != null) {
				query.setParameter(1, organization);
			}
			return Long.parseLong(query.getSingleResult().toString());

			/*
			 * String sql = "SELECT count(*) FROM business_unit "; if(configure
			 * == null || configure.equals("")){ sql += " WHERE "; }else{ sql +=
			 * configure + " AND "; } sql +=
			 * "id IN (SELECT provider_id FROM t_meta_enterprise_to_provider WHERE business_id = ("
			 * + "SELECT id FROM business_unit WHERE organization = ?1))"; Query
			 * query=entityManager.createNativeQuery(sql); query.setParameter(1,
			 * organization); return
			 * Long.parseLong(query.getSingleResult().toString());
			 */
		} catch (Exception e) {
			throw new DaoException(
					"ProviderDAOImpl.getCountByConditionAndOrgId() 根据过滤条件查询当前登录企业的供应商列表总数，出现异常！",
					e);
		}
	}

	/**
	 * 根据组织机构、客户id，查找数量
	 * 
	 * @author ZhangHui 2015/4/13
	 */
	@Override
	public long count(Long organization, Long businessId) throws DaoException {
		try {
			String sql = "SELECT COUNT(*) FROM t_meta_enterprise_to_provider "
					+ "WHERE provider_id = ("
					+ "SELECT id FROM business_unit WHERE organization = ?1) "
					+ "AND business_id = ?2";

			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			query.setParameter(2, businessId);
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取报告过滤条件方法
	 * 
	 * @author longxianzhen 20150815
	 */
	private String getConfigure(String configure, String new_configure) {

		if (configure != null && !"null".equals(configure)) {
			String filter[] = configure.split("@@");
			for (int i = 0; i < filter.length; i++) {
				String filters[] = filter[i].split("@");
				String config = splitSCJointConfigure(filters[0], filters[1],
						filters[2]);
				if (config == null) {
					continue;
				}
				new_configure = new_configure + " AND " + config;
			}
		}
		return new_configure;
	}

	/**
	 * 过滤条件拼接方法
	 * 
	 * @author longxianzhen 20150815
	 */
	private String splitSCJointConfigure(String field, String mark, String value) {
		try {
			if (field.equals("name")) {
				return FilterUtils.getConditionStr(" provider.name ", mark,
						value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 报告待处理供应商查询
	 * 
	 * @author ZhangHui 2015/5/4
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessUnit> getListOfOnHandProducer(int page, int pageSize,
			Long toBusId, String configure) throws DaoException {
		try {
			String new_configure = " ";
			new_configure = getConfigure(configure, new_configure);
			String sql = "SELECT DISTINCT provider.id, provider.name,provider.telephone,provider.note,provider.license_no,provider.organization "
					+ "FROM business_unit provider "
					+ "LEFT JOIN	 t_meta_enterprise_to_provider tc ON ";
			if (toBusId != null) {
				sql += "tc.`business_id`=?1 AND ";
			}
			sql += " tc.`provider_id`=provider.`id`   WHERE 1=1 ";
			if (toBusId != null) {
				sql += " AND tc.`business_id`=?2  ";
			}
			sql += new_configure
					+ "AND  EXISTS ( "
					+ "SELECT 1 FROM t_meta_from_to_business tb "
					+ "INNER JOIN product_instance  pri ON tb.`pro_id`=pri.`product_id` "
					+ "INNER JOIN test_result tr ON pri.`id`=tr.`sample_id`  AND tr.del = 0 AND tr.`publish_flag`='4' "
					+ "INNER JOIN business_unit bus ON bus.organization = tr.organization "
					+ "WHERE DATEDIFF(pri.expiration_date , SYSDATE()) > 0 AND tb.del = '0' AND ";
			if (toBusId != null) {
				sql += "tb.to_bus_id = ?3 AND ";
			}
			sql += "provider.id= tb.`from_bus_id`  AND bus.id = provider.id ) "
					+ " ORDER BY provider.id  ASC ";
			Query query = entityManager.createNativeQuery(sql);
			if (toBusId != null) {
				query.setParameter(1, toBusId);
				query.setParameter(2, toBusId);
				query.setParameter(3, toBusId);
			}
			if (page > 0 && pageSize > 0) {
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<BusinessUnit> buss = new ArrayList<BusinessUnit>();
			if (result != null) {
				for (Object[] obj : result) {
					BusinessUnit bus = new BusinessUnit();
					bus.setId(((BigInteger) obj[0]).longValue());
					bus.setName(obj[1] == null ? "" : obj[1].toString());
					bus.setTelephone(obj[2] == null ? "" : obj[2].toString());
					bus.setNote(obj[3] == null ? "" : obj[3].toString());
					if (obj[4] != null) {
						LicenseInfo license = new LicenseInfo();
						license.setLicenseNo(obj[4].toString());
						bus.setLicense(license);
					}
					bus.setOrganization(((BigInteger) obj[5]).longValue());
					buss.add(bus);
				}
			}
			return buss;
		} catch (Exception e) {
			throw new DaoException(
					"ProviderDAOImpl.getListOfOnHandProducer() 出现异常！", e);
		}
	}

	/**
	 * 获取报告待处理供应商总数
	 * 
	 * @author ZhangHui 2015/5/4
	 */
	@Override
	public Long getCountOfOnHandProducer(Long toBusId, String configure)
			throws DaoException {
		try {
			String new_configure = " ";
			new_configure = getConfigure(configure, new_configure);
			String sql = "SELECT COUNT(DISTINCT provider.id) "
					+ "FROM business_unit provider "
					+ "LEFT JOIN	 t_meta_enterprise_to_provider tc ON ";
			if (toBusId != null) {
			sql+="tc.`business_id`=?1 AND ";
			}
			sql+="tc.`provider_id`=provider.`id` WHERE 1=1 ";
			if (toBusId != null) {
				sql+= " AND tc.`business_id`=?2 ";
			}
			sql+=  new_configure 
				+ " AND EXISTS ( "
				+ "SELECT 1 FROM t_meta_from_to_business tb "
				+ "INNER JOIN product_instance  pri ON tb.`pro_id`=pri.`product_id` "
				+ "INNER JOIN test_result tr ON pri.`id`=tr.`sample_id` AND tr.del = 0 AND tr.`publish_flag`='4' "
				+ "INNER JOIN business_unit bus ON bus.organization = tr.organization "
				+ "WHERE DATEDIFF(pri.expiration_date , SYSDATE()) > 0 AND  tb.del = '0' AND  ";
			if (toBusId != null) {
			sql+=" tb.to_bus_id = ?3 AND " ;
			}
			sql+="provider.id= tb.`from_bus_id` AND bus.id = provider.id)";
			Query query = entityManager.createNativeQuery(sql);
			if (toBusId != null) {
			query.setParameter(1, toBusId);
			query.setParameter(2, toBusId);
			query.setParameter(3, toBusId);
			}
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException(
					"ProviderDAOImpl.getCountOfOnHandProducer() 出现异常！", e);
		}
	}
}
