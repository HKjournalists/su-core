package com.gettec.fsnip.fsn.dao.erp.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.CustomerDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;

@Repository("customerDAO")
public class CustomerDAOImpl extends BaseDAOImpl<BusinessUnit> 
		implements CustomerDAO{
	
	@SuppressWarnings("unchecked")
	public List<BusinessUnit> getfilter(String filter,String name,String fieldName, List<Long> listId) {
		String sql = "SELECT e FROM business_unit e";
		
		Query query = null;
		if(filter.equals("eq")){
			sql = sql + " WHERE "+fieldName+" = :name and e.id in (:ids) ";
			query = entityManager.createQuery(sql);
			query.setParameter("name", name);
			query.setParameter("ids", listId);
		}else if(filter.equals("neq")){
			sql = sql + " WHERE "+fieldName+" <> :name and e.id in (:ids) ";
			query = entityManager.createQuery(sql);
			query.setParameter("name", name);
			query.setParameter("ids", listId);
		}else if(filter.equals("startswith")){
			sql = sql + " WHERE "+fieldName+" like :name and e.id in (:ids) ";
			query = entityManager.createQuery(sql);
			query.setParameter("name", name + "%");
			query.setParameter("ids", listId);
		}else if(filter.equals("endswith")){
			sql = sql + " WHERE "+fieldName+" like :name and e.id in (:ids) ";
			query = entityManager.createQuery(sql);
			query.setParameter("name", "%" + name);
			query.setParameter("ids", listId);
		}else if(filter.equals("contains")){
			sql = sql + " WHERE "+fieldName+" like :name and e.id in (:ids)";
			query = entityManager.createQuery(sql);
			query.setParameter("name", "%" + name +"%");
			query.setParameter("ids", listId);
		}else if(filter.equals("doesnotcontain")){
			sql = sql + " WHERE "+fieldName+" not like :name and e.id in (:ids) ";
			query = entityManager.createQuery(sql);
			query.setParameter("name", "%" + name +"%");
			query.setParameter("ids", listId);
		}
		return query.getResultList();
	}

	/**
	 * 根据过滤条件查询当前登录企业的客户列表信息
	 * @param configure
	 * @param organization
	 * @return
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessUnit> getListByConditionAndOrgId(String configure,
			Long organization, int page, int pageSize) throws DaoException {
		try{
			 String sql ="SELECT buu.id,buu.name,buu.license_no,buu.note,bu.organization,a.id typeId,a.name typeName,a.type";
					sql+=" FROM t_meta_enterprise_to_customer tec ";
					sql+=" LEFT JOIN business_unit bu ";
					sql+=" ON tec.business_id = bu.id ";
					sql+=" LEFT JOIN business_unit buu ";
					sql+=" ON tec.customer_id = buu.id ";
					sql+=" LEFT JOIN (SELECT t.id,t.name,t.type,b.business_id,b.organization FROM ";
					sql+=" t_meta_customer_and_provider_type t LEFT JOIN t_meta_business_diy_type b ";
					sql+="	ON t.ID = b.type_id WHERE b.organization = t.organization ) a ";
					sql+="	ON (a.business_id=buu.id AND a.organization=bu.organization )";
					sql+=" WHERE bu.organization = ?1  "; 
//			String sql="SELECT buu.id,buu.`name`,buu.license_no,buu.note "+
//					"FROM t_meta_enterprise_to_customer tec "+
//					"LEFT JOIN business_unit bu ON tec.business_id=bu.id "+
//					"LEFT JOIN business_unit buu ON tec.customer_id=buu.id "+
//					"WHERE bu.organization=?1";
			if(configure == null || configure.equals("")){
				sql += "  ";
			}else{
				configure=configure.replaceAll("e#Provider#e.", "buu.").replaceAll("WHERE", " ");
				sql +=" AND "+ configure ;
			}
			sql +=" LIMIT " + (page - 1) * pageSize + "," + pageSize;
			Query query=entityManager.createNativeQuery(sql);
//			Query query=entityManager.createNamedQuery(sql);
			
			query.setParameter(1, organization);
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<BusinessUnit> buss = new ArrayList<BusinessUnit>();
			if(result != null&&result.size()>0){
				for(Object[] obj : result){
					BusinessUnit bus = new BusinessUnit();
					
					bus.setId(((BigInteger)obj[0]).longValue());
					bus.setName(obj[1]==null?"":obj[1].toString());
					if(obj[2] != null){
						LicenseInfo license = new LicenseInfo();
						license.setLicenseNo(obj[2].toString());
						bus.setLicense(license);
					}
					bus.setNote(obj[3]==null?"":obj[3].toString());
					bus.getDiyType().setId(obj[5]!=null?Long.parseLong(obj[5].toString()):null);
					bus.getDiyType().setName(obj[6]!=null?obj[6].toString():null);
					bus.getDiyType().setType(obj[7]!=null?Integer.parseInt(obj[7].toString()):null);
					buss.add(bus);
				}
			}
			return buss;
		}catch(Exception e){
			throw new DaoException("CustomerDAOImpl.getListByConditionAndOrgId() 根据过滤条件查询当前登录企业的客户列表信息，出现异常！",e);
		}
	}
	
	/**
	 * 根据过滤条件查询当前登录企业的客户列表总数
	 * @param configure
	 * @param organization
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public Long getCountByConditionAndOrgId(String configure, Long organization) throws DaoException {
		try{
			
			String sql="SELECT count(*) "+
					"FROM t_meta_enterprise_to_customer tec "+
					"LEFT JOIN business_unit bu ON tec.business_id=bu.id "+
					"LEFT JOIN business_unit buu ON tec.customer_id=buu.id "+
					"WHERE bu.organization=?1";
			if(configure == null || configure.equals("")){
				sql += "  ";
			}else{
				configure=configure.replaceAll("e#Provider#e.", "buu.").replaceAll("WHERE", " ");
				sql +=" AND "+ configure ;
			}
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			return Long.parseLong(query.getSingleResult().toString());
			
			/*String sql = "SELECT count(*) FROM business_unit ";
			if(configure == null || configure.equals("")){
				sql += " WHERE ";
			}else{
				sql += configure + " AND ";
			}
			sql += " id IN (SELECT customer_id FROM t_meta_enterprise_to_customer WHERE business_id = ("+
								"SELECT id FROM business_unit WHERE organization = ?1))";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			return Long.parseLong(query.getSingleResult().toString());*/
		}catch(Exception e){
			throw new DaoException("CustomerDAOImpl.getCountByConditionAndOrgId() 根据过滤条件查询当前登录企业的客户列表总数，出现异常！",e);
		}
	}

	/**
	 * 根据组织机构、客户id，查找数量
	 * @author ZhangHui 2015/4/13
	 */
	@Override
	public long count(Long organization, Long businessId) throws DaoException {
		try {
			String sql = "SELECT COUNT(*) FROM t_meta_enterprise_to_customer " +
						 "WHERE customer_id = ("+
						 		"SELECT id FROM business_unit WHERE organization = ?1) " +
						 "AND business_id = ?2";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			query.setParameter(2, businessId);
			
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("CustomerDAOImpl.count() 出现异常！", e);
		}
	}

	/**
	 * 增加一条企业-供应商关系
	 * @author ZhangHui 2015/4/13
	 */
	@Override
	public void addProviderRelation(Long busId, Long providerId) throws DaoException {
		try {
			String sql = "INSERT INTO t_meta_enterprise_to_provider(business_id,provider_id) VALUE(?1,?2)";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, busId);
			query.setParameter(2, providerId);
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException("CustomerDAOImpl.addProviderRelation() 出现异常！", e);
		}
	}

	/**
	 * 查找一条企业-供应商关系
	 * @author ZhangHui 2015/4/13
	 * @throws DaoException 
	 */
	@Override
	public long countProviderRelation(Long busId, Long providerId) throws DaoException {
		try {
			String sql = "SELECT COUNT(*) FROM t_meta_enterprise_to_provider WHERE business_id = ?1 AND provider_id = ?2";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, busId);
			query.setParameter(2, providerId);
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("CustomerDAOImpl.countProviderRelation() 出现异常！", e);
		}
	}
	
	/**
	 * 根据客户类型查询当前登录企业的客户列表信息
	 * @param configure
	 * @param organization
	 * @return
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessUnit> getListByCustomerType(Long organization, int customerType, int page, int pageSize) throws DaoException {
		try{
			String contype="";
//			if(customerType != 0) {
//				contype = " AND b.type=?2  ";
//			}
//			String sql="SELECT buu.id,buu.`name`,buu.license_no,buu.note "+
//					"FROM t_meta_enterprise_to_customer tec "+
//					"LEFT JOIN business_unit bu ON tec.business_id=bu.id "+
//					"LEFT JOIN business_unit buu ON tec.customer_id=buu.id "+
//					"LEFT JOIN t_meta_customer_and_provider_type tp ON tp.organization=bu.organization "+
//					"LEFT JOIN t_meta_business_diy_type b on tp.ID = b.type_id " +
//					"WHERE bu.organization=?1 AND b.business_id =buu.id "+contype;
			String sql = this.getTypesql();
			
			Query query=entityManager.createNativeQuery(sql);
			
			query.setParameter("org", organization);
			if(customerType != 0) {
				query.setParameter("type", customerType);
			}
			if(page > 0 && pageSize > 0){
				query.setFirstResult((page-1)*pageSize);
				query.setMaxResults(pageSize);
			}
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<BusinessUnit> buss = new ArrayList<BusinessUnit>();
			if(result != null&&result.size()>0){
				for(Object[] obj : result){
					BusinessUnit bus = new BusinessUnit();
					bus.setId(((BigInteger)obj[0]).longValue());
					bus.setName(obj[1]==null?"":obj[1].toString());
					if(obj[2] != null){
						LicenseInfo license = new LicenseInfo();
						license.setLicenseNo(obj[2].toString());
						bus.setLicense(license);
					}
					bus.setNote(obj[3]==null?"":obj[3].toString());
					buss.add(bus);
				}
			}
			return buss;
			
			
			/*if(organization == null || customerType < 1){
				return null;
			}
			String limit = "";
			if(page > 0 && pageSize > 0){
				limit = " limit " + (page - 1) + "," + pageSize;
			}
			String sql = "SELECT bus.* FROM business_unit bus " +
					"RIGHT JOIN t_meta_business_diy_type bdt ON bus.id = bdt.business_id " +
					"WHERE bdt.type = ?1 AND bdt.organization = ?2" + limit;
			return this.getListBySQL(BusinessUnit.class, sql, new Object[]{customerType, organization});*/
		}catch(Exception e){
			throw new DaoException("CustomerDAOImpl.getListByCustomerType() 根据客户类型查询当前登录企业的客户列表信息时出现异常！", e);
		}
	}
    /**
     * 客户查询列表
     */
	@Override
	public List<BusinessUnit> searchCustomerList(String keyword,
			Long organization, int page, int pageSize) {
		int  countNum = 0;
		/*String sql ="SELECT b.id,b.name,b.license_no,b.note,b.address,dt.organization,dt.type_id,pt.name AS typeName ,dt.type ";
			   sql +=" FROM (SELECT customer_id AS business_id FROM t_meta_enterprise_to_customer GROUP BY customer_id) t "; 
			   sql +=" INNER JOIN business_unit b ON b.id=t.business_id ";
			   sql +=" LEFT JOIN t_meta_business_diy_type dt ON dt.business_id=t.business_id "; 
			   sql +=" AND dt.organization=:org ";
			   sql +=" LEFT JOIN t_meta_customer_and_provider_type pt ON pt.id=dt.type_id ";
			   sql +=" WHERE  (b.name like '%"+keyword+"%' OR b.license_no like '%"+keyword+"%' OR b.note like '%"+keyword+"%' )"; */
		String sql="SELECT  bu.id,bu.name,bu.license_no,bu.note,bu.address,tc.business_id,s.typeId,s.typeName ,s.TYPE  " +
				"FROM business_unit bu "+
				"LEFT JOIN t_meta_enterprise_to_customer tc ON tc.customer_id = bu.id "+
				"AND tc.business_id =(SELECT b.id FROM business_unit b WHERE b.organization=:org) "+
				"LEFT JOIN (SELECT dt.business_id,dt.organization,tt.id as typeId,tt.`NAME` AS typeName,tt.TYPE "+
					"FROM t_meta_business_diy_type dt ,t_meta_customer_and_provider_type tt "+
				"WHERE tt.id=dt.type_id) s ON s.business_id=tc.customer_id AND s.organization=:org "+
			"WHERE  bu.organization!=0 and bu.organization!=-1 AND bu.organization is not NULL "+
			"AND (bu.name like '%"+keyword+"%' OR bu.license_no like '%"+keyword+"%'  ) ";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter("org", organization);
			if(page > 0 && pageSize > 0){
				countNum = (page - 1) * pageSize;
				query.setFirstResult((page-1)*pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			List<BusinessUnit> buss = new ArrayList<BusinessUnit>();
			if(result != null&&result.size()>0){
				for(Object[] obj : result){
					BusinessUnit bus = new BusinessUnit();
					bus.setId(((BigInteger)obj[0]).longValue());
					bus.setName(obj[1]==null?"":obj[1].toString());
					bus.setLincesNo(obj[2]==null?"":obj[2].toString());
					bus.setNote(obj[3]==null?"":obj[3].toString());
					bus.setAddress(obj[4]==null?"":obj[4].toString());
					Long con_org = obj[5]==null?null:Long.parseLong(obj[5].toString());
					if(con_org!=null){
						bus.setSignFlag(true);
					}else{
				    	bus.setSignFlag(false);
				    }
					bus.getDiyType().setId(obj[6]!=null?Long.parseLong(obj[6].toString()):null);
					bus.getDiyType().setName(obj[7]!=null?obj[7].toString():null);
					bus.getDiyType().setType(obj[8]!=null?Integer.parseInt(obj[8].toString()):null);
					countNum++;
					bus.setCountNum(countNum);
					buss.add(bus);
					
				}
			}
			return buss;
	}

	@Override
	public Long searchCustomerCount(String keyword, Long organization) {
		/*String sql ="SELECT COUNT(b.id) ";
		   sql +=" FROM (SELECT customer_id AS business_id FROM t_meta_enterprise_to_customer GROUP BY customer_id) t "; 
		   sql +=" INNER JOIN business_unit b ON b.id=t.business_id ";
		   sql +=" LEFT JOIN t_meta_business_diy_type dt ON dt.business_id=t.business_id "; 
		   sql +=" AND dt.organization=:org ";
		   sql +=" LEFT JOIN t_meta_customer_and_provider_type pt ON pt.id=dt.type_id ";
		   sql +=" WHERE  (b.name like '%"+keyword+"%' OR b.license_no like '%"+keyword+"%' OR b.note like '%"+keyword+"%' )"; */
		String sql="SELECT COUNT(bu.id) FROM business_unit bu "+
					"LEFT JOIN t_meta_enterprise_to_customer tc ON tc.customer_id = bu.id "+
					"AND tc.business_id =(SELECT b.id FROM business_unit b WHERE b.organization=:org) "+
					"LEFT JOIN (SELECT dt.business_id,dt.organization,tt.id as typeId,tt.`NAME` AS typeName,tt.TYPE "+
						"FROM t_meta_business_diy_type dt ,t_meta_customer_and_provider_type tt "+
					"WHERE tt.id=dt.type_id) s ON s.business_id=tc.customer_id AND s.organization=:org "+
				"WHERE  bu.organization!=0 and bu.organization!=-1 AND bu.organization is not NULL "+
				"AND (bu.name like '%"+keyword+"%' OR bu.license_no like '%"+keyword+"%'  ) ";
				Query query=entityManager.createNativeQuery(sql);
				query.setParameter("org", organization);
				/*query.setParameter("typeName", "'%"+keyword+"%'");
				query.setParameter("licenseNo", "'%"+keyword+"%'");
				query.setParameter("note", "'%"+keyword+"%'");*/
	   return Long.parseLong(query.getSingleResult().toString());
	}

	@Override
	public List<BusinessUnit> sourceOrsoldCustomer(int type,String configure,
			Long organization, int page, int pageSize) {
		String sql = this.getTypesql();
		if(configure!=null&&!"0".equals(configure)){
			sql += this.getConfigureFilter("b",configure);
		}
		Query query=entityManager.createNativeQuery(sql);
		query.setParameter("org", organization);
		query.setParameter("type", type);
		if(page > 0 && pageSize > 0){
			query.setFirstResult((page-1)*pageSize);
			query.setMaxResults(pageSize);
		}
		List<Object[]> result = query.getResultList();
		List<BusinessUnit> buss = new ArrayList<BusinessUnit>();
		if(result != null&&result.size()>0){
			for(Object[] obj : result){
				BusinessUnit bus = new BusinessUnit();
				bus.setId(((BigInteger)obj[0]).longValue());
				bus.setName(obj[1]==null?"":obj[1].toString());
				bus.setLincesNo(obj[2]==null?"":obj[2].toString());
				bus.setNote(obj[3]==null?"":obj[3].toString());
				bus.setAddress(obj[4]==null?"":obj[4].toString());
				bus.setContact(obj[5]==null?"":obj[5].toString());
				bus.setTelephone(obj[6]==null?"":obj[6].toString());
				bus.getDiyType().setId(obj[7]!=null?Long.parseLong(obj[7].toString()):null);
				bus.getDiyType().setName(obj[8]!=null?obj[8].toString():null);
				bus.getDiyType().setType(obj[9]!=null?Integer.parseInt(obj[9].toString()):null);
				Long con_org = obj[10]==null?null:Long.parseLong(obj[10].toString());
				if(con_org!=null){
					bus.setSignFlag(true);
				}else{
			    	bus.setSignFlag(false);
			    }
				buss.add(bus);
			}
		}
		return buss;
	}
    /**
     * 查看销往客户或者来源客户sql
     * @return
     */
	private String getTypesql() {
		String sql ="SELECT b.id,b.name,b.license_no,b.note,b.address,b.contact,b.telephone,dt.type_id,pt.name AS typeName ,dt.type,dt.organization ";
		   sql +=" FROM (SELECT customer_id AS business_id FROM t_meta_enterprise_to_customer GROUP BY customer_id) t "; 
		   sql +=" INNER JOIN business_unit b ON b.id=t.business_id ";
		   sql +=" LEFT JOIN t_meta_business_diy_type dt ON dt.business_id=t.business_id "; 
		   sql +=" AND dt.organization=:org ";
		   sql +=" INNER JOIN t_meta_customer_and_provider_type pt ON pt.id=dt.type_id ";
		   sql +=" WHERE pt.type=:type ";
		return sql;
	}
	@Override
	public Long sourceOrsoldCustomer(int type,String configure, Long organization) {
		String sql ="SELECT COUNT(b.id) ";
		   sql +=" FROM (SELECT customer_id AS business_id FROM t_meta_enterprise_to_customer GROUP BY customer_id) t "; 
		   sql +=" INNER JOIN business_unit b ON b.id=t.business_id ";
		   sql +=" LEFT JOIN t_meta_business_diy_type dt ON dt.business_id=t.business_id "; 
		   sql +=" AND dt.organization=:org ";
		   sql +=" INNER JOIN t_meta_customer_and_provider_type pt ON pt.id=dt.type_id ";
		   sql +=" WHERE pt.type=:type ";
		
		if(configure!=null&&!"0".equals(configure)){
			sql += this.getConfigureFilter("b",configure);
		}
		Query query=entityManager.createNativeQuery(sql);
		query.setParameter("org", organization);
		query.setParameter("type", type);
		return Long.parseLong(query.getSingleResult().toString());
	}
	private String getConfigureFilter(String e, String configure) {
		String sql = "";
		 String filter[] = configure.split("@@");
		    for(int i=0;i<filter.length;i++){
		    	String filters[] = filter[i].split("@");
		    	try {
		    		if(filters[0].equals("name")){
			    		if(filters[1].equals("eq")){
			    			sql +=" and " +	e + "." + filters[0]+"='"+filters[2]+"'";
			    		}else if(filters[0].equals("name")&&filters[1].equals("contains")){
			    			sql +=" and " +	e + "." + filters[0]+"  like '%"+filters[2]+"%' ";
			    		}else if(filters[1].equals("neq")){
			    			sql +=" and " +	e + "." + filters[0]+" != '%"+filters[2]+"%' ";
			    		}
		    		}else if(filters[0].equals("lincesNo")){
			    		if(filters[1].equals("eq")){
			    			sql +=" and " +	e + ".license_no ='"+filters[2]+"'";
			    		}else if(filters[1].equals("contains")){
			    			sql +=" and " +	e + ".license_no  like '%"+filters[2]+"%' ";
			    		}else if(filters[1].equals("neq")){
			    			sql +=" and " +	e + ".license_no != " +filters[2]+ "' ";
			    		}
		    		}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		    }
		return sql;
	}
}
