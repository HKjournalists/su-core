package com.gettec.fsnip.fsn.dao.business.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.business.BusinessUnitDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.CirculationPermitInfo;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.model.business.OrganizingInstitution;
import com.gettec.fsnip.fsn.model.business.TaxRegisterInfo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.market.ResourceType;
import com.gettec.fsnip.fsn.util.DateUtil;
import com.gettec.fsnip.fsn.vo.BusinessStaVO;
import com.gettec.fsnip.fsn.vo.business.AccountBusinessVO;
import com.gettec.fsnip.fsn.vo.business.BusinessTreeDetail;
import com.gettec.fsnip.fsn.vo.erp.BusinessNameVO;
import com.lhfs.fsn.vo.business.BussinessUnitVOToPortal;
import com.lhfs.fsn.vo.business.LightBusUnitVO;

/**
 * BusinessUnit customized operation implementation
 * 
 * @author Ryan Wang
 */
@Repository(value="businessUnitDAO")
public class BusinessUnitDAOImpl extends BaseDAOImpl<BusinessUnit>
implements BusinessUnitDAO {

	/**
	 * 根据企业名称查找企业营业执照
	 * @param name
	 * @return
	 */
	@Override
	public String findLicenseByName(String name) throws DaoException {
		try {
			if (StringUtils.isNotBlank(name)) {
				String sql = "select license_no from business_unit where name = ?1";
				List<String> result = this.getListBySQLWithoutType(String.class, sql, new Object[]{name});
				if(result.size() > 0){
					return result.get(0);
				}
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("BusinessUnitDAOImpl.findLicenseByName() 根据企业名称查找企业营业执照,出现异常！", jpae.getException());
		}
	}

	/**
	 * 根据企业组织机构id获取企业名称
	 * @param organization
	 * @author tangxin 2015-05-18
	 */
	@Override
	public String findNameByOrganization(Long organization) throws DaoException {
		try {
			if(organization == null) {
				return null;
			}
			String sql = "select name from  business_unit where organization = ?1";
			List<String> result = this.getListBySQLWithoutType(String.class, sql, new Object[]{organization});
			if(result != null && result.size() > 0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 功能描述：根据企业id查找企业名称
	 * @author ZhangHui 2015/7/3
	 * @throws DaoException 
	 */
	@Override
	public String findNameById(Long id) throws DaoException {
		try {
			if(id == null) {
				return null;
			}
			String sql = "select name from  business_unit where id = ?1";
			List<String> result = this.getListBySQLWithoutType(String.class, sql, new Object[]{id});
			if(result != null && result.size() > 0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae.getException());
		}
	}


	@Override
	public int getProductCount() {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from business_unit");
		Query query=entityManager.createNativeQuery(sql.toString());
		Number result=(Number) query.getSingleResult();		
		return result.intValue();
	}

	/**
	 * 按企业名称模糊查询企业信息
	 * @throws DaoException 
	 */
	@Override
	public List<BusinessUnit> findByName_(String name) throws DaoException {
		try {
			if (!StringUtils.isNotBlank(name)) {
				return null;
			}	
			String condition = " WHERE name like ?1";
			return this.getListByCondition(condition, new Object[]{"%"+name+"%"});
		} catch (JPAException jpae) {
			throw new DaoException("BusinessUnitDAOImpl.findByName_ 按企业名称模糊查询企业信息, 出现异常！", jpae.getException());
		}
	}

	/**
	 * 从生产企业信息表中获取所有qs号集合（并去重）
	 * 抹掉qs号前面的QS字母
	 * @param firstpart qs号的第一部分
	 * @param formatId qs号规则的id
	 * @return List<String>
	 * @throws DaoException 
	 * 
	 * @author HuangYog 
	 */
	@Override
	public List<String> getListOfQsNo(String firstpart,Long formatId) throws DaoException {
		try {
			String sql = "SELECT DISTINCT REPLACE(pli.qs_no,?1,'') FROM product_to_businessunit p2b " + 
					" LEFT JOIN production_license_info pli ON p2b.qs_id = pli.id " + 
					" WHERE p2b.qs_id IS NOT NULL AND pli.qs_no like ?2 AND pli.qsformat_id = ?3";
			return this.getListBySQLWithoutType(String.class, sql, new Object[]{firstpart, firstpart + "%", formatId});
		} catch (Exception e) {
			throw new DaoException("【DAO-error】从生产企业信息表中获取所有qs号集合，出现异常", e);
		}
	}

	@Override
	public List<BusinessUnit> getListByName(int page, int pageSize, String name,String type) throws DaoException {
		try {
			String condition = " WHERE e.name like ?1";
			Object[] params = new Object[]{"%"+name+"%"};

			if(type.equals("business")){
				condition = " WHERE e.name like ?1";
				params = new Object[]{"%"+name+"%"};
			}else{
				condition = " WHERE e.name LIKE ?1 OR e.license.licenseNo LIKE ?2";
				params = new Object[]{"%"+name+"%", "%"+name+"%"};
			}
			return this.getListByPage(page, pageSize, condition, params);
		} catch (JPAException jpae) {
			throw new DaoException("【dao-error】按组织机构Id获取商标列表，出现异常！", jpae.getException());
		}
	}


	/**
	 * dao层根据组织机构ID查询该组织机构下所有子企业
	 */
	@Override
	public List<BusinessUnit> getSubsidiaryListByOrgPage(Map<String, Object> map, int page, int pageSize) throws DaoException {
		try {
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			condition += " order by e.id desc";
			return this.getListByPage(page, pageSize, condition, params);
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-Error】根据筛选条件condition(筛选BusinessUnit)分页查询品牌信息时出现异常",jpae.getException());
		}
	}

	/**
	 * dao层根据组织机构ID获取该组织机构下所有子企业的个数
	 */
	@Override
	public Long getCountByOrg(Map<String, Object> map) throws DaoException {
		try {
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			return this.count(condition, params);
		} catch (JPAException jpae) {
			throw new DaoException("【dao-error】BusinessUnit获取过滤后的总条数，出现异常！", jpae.getException());
		}
	}

	/**
	 * dao层验证企业名称唯一性
	 */
	@Override
	public boolean verificationNameOrLic(String val,String type) throws DaoException {
		int count = 0;
		try {
			String jpql="";
			if(type.equals("name")){				
				jpql = "SELECT count(*) FROM business_unit WHERE name=?1";
			}else{
				jpql = "SELECT count(*) FROM business_unit WHERE license_no=?1";
			}
			Query query = entityManager.createNativeQuery(jpql).setParameter(1, val);
			Number result=(Number)query.getSingleResult();
			count=result.intValue();			
		} catch (Exception e) {
			throw new DaoException("dao层验证企业名称唯一性出错", e);
		}
		return count>0?true:false;
	}


	/**
	 * 按组织机构Id查找一条生产商信息
	 * @throws DaoException 
	 */
	@Override
	public BusinessUnit findByOrgnizationId(Long organizationId) throws DaoException {
		String hql = "select e from  "+entityClass.getName() + " e WHERE e.organization = ?1";
		Query query = entityManager.createQuery(hql);
		query.setParameter(1,organizationId);
		try{
			return (BusinessUnit) query.getSingleResult();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据组织机构Id获取该生产企业的信息
	 * 
	 */
	@Override
	public BusinessUnit getBusinessByOrganizationIdOfLigth(Long busId)
			throws DaoException {
		try{
			BusinessUnit orig_bus = this.findById(busId);
			BusinessUnit bus=null;
			if(orig_bus!=null){
				bus=new BusinessUnit();
				bus.setLicense(new LicenseInfo());
				bus.getLicense().setLicenseNo(orig_bus.getLicense()!=null?orig_bus.getLicense().getLicenseNo():"");
				bus.setName(orig_bus.getName());
				bus.setAddress(orig_bus.getAddress());
				bus.setOtherAddress(orig_bus.getOtherAddress());
				bus.setType(orig_bus.getType());
				bus.setId(orig_bus.getId());
				bus.setPersonInCharge(orig_bus.getPersonInCharge());
				bus.setTelephone(orig_bus.getTelephone());
				bus.setSignFlag(orig_bus.isSignFlag());
			}
			return bus;
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl.getBusinessByOrganizationIdOfLigth() " + e.getMessage(),e);
		}
	}

	/**
	 * 根据条件（企业名称、企业类型、注册时间）分页查询企业集合
	 * @param page 第几页
	 * @param pageSize 每页显示条数
	 * @param businessName 企业名称
	 * @param businessType 企业类型
	 * @param startDate 企业注册起始时间 
	 * @param endDate 企业注册结束时间
	 * @return List<BusinessStaVO>
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessStaVO> findBusinessIdByNameType(int page, int pageSize,String businessName,
			String businessType,String startDate,String endDate)throws DaoException {
		try {	
			String new_configure = " WHERE organization IS NOT NULL AND organization!=0 ";
			//如果企业名称不为空则加上查询企业名称的条件
			if(!businessName.equals("")&&businessName!=null){
				new_configure=new_configure+" AND  e.name like "+"'%"+businessName+"%' ";
			}
			//如果企业类型不为空则加上查询企业类型的条件
			if(!businessType.equals("")&&businessType!=null){
				new_configure=new_configure+" AND e.type like "+"'%"+businessType+"%' ";
			}
			////如果企业注册时间范围不为空则加上查询企业注册时间范围的条件
			if(!startDate.equals("")&&!endDate.equals("")){
				endDate=DateUtil.addDays(endDate, 1);
				new_configure=new_configure+" AND e.enterpriteDate BETWEEN '"+startDate+"' AND '"+endDate+"' ";
			}
			String jpql = "SELECT e.id,e.name,e.type,e.organization,enterpriteDate FROM business_unit e ";
			if (new_configure != null&&!new_configure.equals("")) {
				jpql = jpql + " " + new_configure;
			}
			Query query = entityManager.createNativeQuery(jpql);
			if(page!=0&&pageSize!=0){
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> objs=query.getResultList();
			List<BusinessStaVO> businessSta=new ArrayList<BusinessStaVO>();
			for(Object[] obj:objs){
				BusinessStaVO buSta=new BusinessStaVO();
				buSta.setBusinessId(Long.valueOf(obj[0].toString()));
				buSta.setBusinessName(obj[1]==null?null:obj[1].toString());
				buSta.setBusinessType(obj[2]==null?null:obj[2].toString());
				buSta.setOrganization(obj[3]==null?null:Long.valueOf(obj[3].toString()));
				buSta.setEnterpriteDate(obj[4]==null?null:(Date)obj[4]);
				businessSta.add(buSta);
			}
			return businessSta;
		} catch (Exception e) {
			throw new DaoException("BusinessUnitDAOImpl.findBusinessIdByNameType()-->根据条件（企业名称、企业类型、注册时间）分页查询企业集合出错",e);
		}
	}

	/**
	 * 根据条件（企业名称、企业类型、注册时间）查询企业总数
	 * @param businessName 企业名称
	 * @param businessType 企业类型
	 * @param startDate 企业注册起始时间
	 * @param endDate 企业注册结束时间
	 * @return Long
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	@Override
	public Long getBusinessStaCountByConfigure(String businessName,
			String businessType, String startDate, String endDate)
					throws DaoException {
		try{
			String new_configure = " WHERE organization IS NOT NULL AND organization!=0 ";
			//如果企业名称不为空则加上查询企业名称的条件
			if(!businessName.equals("")&&businessName!=null){
				new_configure=new_configure+" AND  e.name like "+"'%"+businessName+"%' ";
			}
			//如果企业类型不为空则加上查询企业类型的条件
			if(!businessType.equals("")&&businessType!=null){
				new_configure=new_configure+" AND e.type like "+"'%"+businessType+"%' ";
			}
			////如果企业注册时间范围不为空则加上查询企业注册时间范围的条件
			if(!startDate.equals("")&&!endDate.equals("")){
				endDate=DateUtil.addDays(endDate, 1);
				new_configure=new_configure+" AND e.enterpriteDate BETWEEN '"+startDate+"' AND '"+endDate+"' ";
			}
			String jpql = "SELECT count(*) FROM " + entityClass.getName() + " e ";
			if (new_configure != null&&!new_configure.equals("")) {
				jpql = jpql + " " + new_configure;
			}
			Object rtn = entityManager.createQuery(jpql).getSingleResult();
			return rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (Exception e) {
			throw new DaoException("BusinessUnitDAOImpl.getBusinessStaCountByConfigure()-->根据条件（企业名称、企业类型、注册时间）查询企业总数出错",e);
		}
	}

	/**
	 * 获取所有的生产企业名称，提供自动加载使用。 
	 */
	@Override
	public List<String> getAllBusUnitName(String name, int page, int pageSize) throws DaoException {
		try{
			String nameCond = "";
			String pageCond = "";
			if(name != null && !"".equals(name)){
				nameCond = " AND name LIKE '" + name + "%' ";
			}
			if(page > 0){
				page = (page - 1) * pageSize;
				pageCond = " LIMIT " + page + "," + pageSize;
			}
			String sql = "SELECT DISTINCT name FROM business_unit WHERE name IS NOT NULL and name!=''" + nameCond + pageCond;
			return this.getListBySQLWithoutType(String.class, sql, null);
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl.getAllBusUnitName()-->"+e.getMessage(),e);
		}
	}

	/**
	 * 获取所有的生产企业营业执照号，提供自动加载使用。 
	 */
	@Override
	public List<String> getAllLicenseNoAndId() throws DaoException {
		try{
			String sql = "SELECT DISTINCT license_no FROM business_unit WHERE license_no IS NOT NULL";
			return this.getListBySQLWithoutType(String.class, sql, null);
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl.getAllLicenseNoAndId()-->"+e.getMessage(),e);
		}
	}

	/**
	 * 获取所有的生产企业地址信息，提供自动加载使用。 
	 */
	@Override
	public List<String> getAllBusUnitAddressAndId() throws DaoException {
		try{
			String sql = "SELECT DISTINCT address FROM business_unit WHERE address IS NOT NULL";
			return this.getListBySQLWithoutType(String.class, sql, null);
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl",e);
		}
	}

	/**
	 * 获取总企业id根据该企业的组织机构
	 * @param organizationId
	 * @return Long
	 * @author HuangYog
	 */
	@Override
	public Long getIdByOrganization(Long organizationId) throws DaoException {
		try{
			String sql="select MAX(id) from business_unit where organization= ?1";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, organizationId);
			Object obj=query.getSingleResult();
			return obj!=null?Long.parseLong(obj.toString()):null;
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl.getIdByOrganization()-->"+e.getMessage(),e);
		}
	}

	/**
	 * 功能描述：根据企业id查找企业组织机构
	 * @author ZhangHui 2015/7/1
	 * @throws DaoException 
	 */
	@Override
	public Long findOrgById(Long organizationId) throws DaoException {
		try{
			String sql = "select organization from business_unit where id = ?1";

			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organizationId);
			Object obj = query.getSingleResult();

			return obj!=null?Long.parseLong(obj.toString()):null;
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl.getIdByOrganization()-->"+e.getMessage(),e);
		}
	}

	/**
	 * 根据企业名称获取企业id
	 * @author ZhangHui 2015/6/2
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long getIdByName(String name) throws DaoException {
		try{
			String sql = "select id from business_unit where name = ?1";

			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, name);

			List<Object> result = query.getResultList();

			if(result.size() > 1){
				throw new Exception("一个企业名称不能有多条记录");
			}

			if(result.size() == 1){
				Object obj = result.get(0);
				return Long.parseLong(obj.toString());
			}

			return null;
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl.findIdByName()-->"+e.getMessage(),e);
		}
	}

	/**
	 * 根据 企业的id号获取总企业的前半部分为‘firstpart’的所有qs号（包括旗下所有的子企业QS号）
	 * @param businessUnitId
	 * @param firstpart
	 * @return List<String>
	 * 
	 * @author HuangYog
	 */
	@Override
	public List<String> getSonQsList4BussUnitId(Long businessUnitId,String firstpart,Long formatId)
			throws DaoException {
		try{
			//将查询出来的QSNO 去掉开头的QS字母 和空格
			String sql="select REPLACE(pi.qs_no,?1,'') from businessunit_to_prolicinfo e " + 
					"RIGHT JOIN production_license_info pi ON e.qs_id = pi.id " + 
					"where e.business_id = ?2 AND e.del = 0 AND pi.qs_no like ?3 And pi.qsFormat_id = ?4";
			return this.getListBySQLWithoutType(String.class, sql, new Object[] {firstpart,businessUnitId,firstpart+"%",formatId});
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl.getBusinessUnitIdByOrganizationId()-->"+e.getMessage(),e);
		}
	}

	/**
	 * 根据 企业的parentOrganization号获取当前登录子公司之外，
	 * 已经被其他子公司绑定过的且前半部分是‘firstpart’ 的 qs号 
	 * @param organization 付组织机构
	 * @param myOrganization 
	 * @param firstpart qs号的前部分
	 * @return List<String> 
	 * 
	 * @author HuangYog
	 */
	@Override
	public List<String> getSonBussUnitIdByParentOrganizationId(Long organization,
			Long myOrganization,String firstpart,Long formatId)throws DaoException {
		try{
			String sql="SELECT DISTINCT REPLACE(p.QS_NO,?1,'') FROM product_to_businessunit p " + 
					" LEFT JOIN business_unit b ON  p.business_id = b.id " + 
					" RIGHT JOIN production_license_info pl ON p.QS_NO = pl.qs_no " + 
					" WHERE p.BARCORD IS NOT NULL AND b.organization <> ?2 AND b.parentOrgnization = ?3 " + 
					" AND p.qs_no like ?4 AND pl.qsformat_id = ?5";
			return this.getListBySQLWithoutType(String.class, sql, new Object[] {firstpart,myOrganization,organization,firstpart+"%",formatId});
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl.getBusinessUnitIdByOrganizationId()-->"+e.getMessage(),e);
		}
	}

	/**
	 * 验证企业组织机构代码是否重复
	 * @param orgCode
	 * * @param orgId
	 * @return boolean：ture表示重复
	 */
	@Override
	public boolean validateBusUnitOrgCode(String orgCode, Long orgId) throws DaoException {
		try{
			String condition="";
			Object[] params = null;
			if(orgId==null){
				condition=" where e.organization!=null and e.orgInstitution!=null and e.orgInstitution.orgCode= ?1";
				params = new Object[]{orgCode};
			}else{
				condition=" where e.orgInstitution!=null and e.orgInstitution.orgCode= ?1 and e.organization!=null and e.organization != ?2";
				params = new Object[]{orgCode,orgId};
			}
			Long cont = this.count(condition, params);
			return (cont>0?true:false);
		}catch(JPAException jpae){
			throw new DaoException("BusinessUnitDAOImpl.validateBusUnitOrgCode() "+jpae.getMessage(),jpae.getException());
		}
	}

	/**
	 * 获取被总企业绑定过的且前半部分是‘firstpart’ 的qs号（不包括子企业）
	 * @param businessUnitId
	 * @param firstpart
	 * @return List<String>
	 * 
	 * @author HuangYog 
	 */
	@Override
	public List<String> getproToBusQsListByBusId(Long businessUnitId,String firstpart,Long formatId)
			throws DaoException {
		try{
			String sql="SELECT DISTINCT REPLACE(QS_NO,?1,'') FROM product_to_businessunit p2b " + 
					" RIGHT JOIN production_license_info pl ON  p2b.qs_NO = pl.qs_no " + 
					" WHERE p2b.business_id = ?2 AND p2b.QS_NO like ?3 AND pl.qsformat_id = ?4 ";
			return this.getListBySQLWithoutType(String.class, sql, new Object[] {firstpart,businessUnitId,firstpart+"%",formatId});
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl.getproToBusQsListBussUnitId()-->"+e.getMessage(),e);
		}
	}

	/**
	 * 根据 企业名称 查找一条企业信息
	 * @param name
	 * @return
	 * @throws DaoException
	 */
	@Override
	public BusinessUnit findByName(String name) throws DaoException {
		try {
			String condition = " WHERE e.name = ?1";
			List<BusinessUnit> result = this.getListByCondition(condition, new Object[]{name});
			if(result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("BusinessUnitDAOImpl.findByName() 根据 企业名称 查找一条企业信息,出现异常！", jpae.getException());
		}
	}

	/**
	 * 获取企业的签名状态
	 * @param busName
	 * @return
	 * @throws DaoException
	 */
	@Override
	public boolean findSignFlagByName(String busName) throws DaoException{
		try{
			BusinessUnit orig_bus = this.findByName(busName);
			if(orig_bus != null) return orig_bus.isSignFlag();
			return false;
		}catch(DaoException daoe){
			throw new DaoException("BusinessUnitDAOImpl.findSignFlagByName() " + daoe.getMessage(), daoe);
		}
	}

	/**
	 * 根据当前登录组织机构ID获取交易市场下的商户数量
	 */
	@Override
	public long countMarketByOrganization(Map<String, Object> map,Long organization)throws DaoException {
		try{
			String condition="";
			if(map!=null){
				condition = (String) map.get("condition")+" AND ";			  
			}else{
				condition = " WHERE ";
			}	
			String sql = "SELECT count(*) FROM business_unit ";
			sql += condition;
			sql += " id IN (SELECT business_id FROM business_market_to_business WHERE marketBusiness_id = ("+
					"SELECT id FROM business_market WHERE business_id = ("+
					"SELECT id FROM business_unit WHERE organization = ?1)))";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			return Long.parseLong(query.getSingleResult().toString());
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl.countMarketByOrganization() 根据过滤条件查询当前登录企业的客户列表信息，出现异常！",e);
		}
	}

	/**
	 * 根据当前登录组织机构ID获取交易市场的名称
	 * @author ZhaWanNeng
	 */

	@SuppressWarnings("unchecked")
	public String getMarketNameByOrganization(Long organization) throws DaoException {
		try{
			String sql="SELECT buu.name FROM business_unit buu WHERE buu.organization=(" +
					"SELECT bmb.organization FROM business_unit bu " +
					"LEFT JOIN business_market_to_business bmb ON bu.id=bmb.business_id " +
					"WHERE bu.organization=?1 and  bu.type='流通企业' ORDER BY bmb.id DESC limit 0,1 )";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			List<Object> result = query.getResultList();
			String string = "";
			if(result.size() > 0){
				string = result.get(0).toString();
			}
			return string;
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl.getMarketNameByOrganization() 根据过滤条件查询当前登录企业的客户列表信息，出现异常！",e);
		}
	}
	/**
	 * 企业总数
	 * @return Long
	 * @author ZhaWanNeng
	 */
	@SuppressWarnings("unchecked")
	public Long unitCount() throws DaoException {
		try {
			String sql = "SELECT count(t.id) FROM business_unit t ";
			Query query = entityManager.createNativeQuery(sql);
			List<Object> list = query.getResultList();
			return Long.valueOf(list.get(0).toString());
		} catch (Exception e) {
			throw new DaoException("BusinessUnitDAOImpl.unitCount() ", e);
		}
	}

	/**
	 * 客户管理新增客户中的按关键字并分页自动加载客户名称
	 * @author HuangYog
	 * Create date 2015/04/13
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAllBusUnitName(Integer page,Integer pageSize, String keyword,String busType)
			throws DaoException {
		try{
			if(page-1 < 0){
				return null;
			}
			String sql = "SELECT DISTINCT name,id FROM business_unit WHERE name IS NOT NULL AND name LIKE ?1 AND type LIKE ?2 LIMIT ?3,?4 ";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, "%"+keyword+"%");
			query.setParameter(2, busType);
			query.setParameter(3, (page-1)*20);
			query.setParameter(4, pageSize);
			List<Object[]> result = query.getResultList();
			List<BusinessNameVO> lists = null;
			if(result!=null && result.size()>0){
				lists = new ArrayList<BusinessNameVO>();
				for(Object[] obj : result){
					BusinessNameVO vo = new BusinessNameVO();
					vo.setId(obj[1]!= null?Long.valueOf(obj[1].toString()):-1);
					vo.setName(obj[0]!= null?obj[0].toString():"");
					lists.add(vo);
				}
			}
			return lists;
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl.getAllBusUnitName()-->"+e.getMessage(),e);
		}
	}

	/**
	 * 根据企业guid，查找一条企业信息
	 * @author ZhangHui 2015/4/24
	 */
	@Override
	public BusinessUnit findByGuid(String bus_guid) throws DaoException {
		try {
			String condition = " WHERE e.guid = ?1";
			List<BusinessUnit> result = this.getListByCondition(condition, new Object[]{bus_guid});
			if(result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (JPAException e) {
			throw new DaoException("BusinessUnitDAOImpl.findByGuid()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 功能描述：根据组织机构id获取企业信息<br>
	 * 作用于判断某产品有没有被生产企业接管。
	 * @author ZhangHui 2015/5/1
	 */
	@SuppressWarnings("unchecked")
	@Override
	public LightBusUnitVO findBusVOByOrg(Long organization) throws DaoException {
		try{		
			String sql = "SELECT id,`name`,address,other_address,type,license_no,organization,sign_flag,pass_flag " +
					"FROM business_unit WHERE organization = ?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			List<Object[]> results = query.getResultList();
			if(results!=null && results.size()>0){
				Object[] obj = results.get(0);
				LightBusUnitVO vo = new LightBusUnitVO(((BigInteger)obj[0]).longValue(),
						obj[1]==null?"":obj[1].toString(),obj[2]==null?"":obj[2].toString(),
								obj[3]==null?"":obj[3].toString(),obj[4]==null?"":obj[4].toString(),
										obj[5]==null?"":obj[5].toString(),((BigInteger)obj[6]).longValue(),
												(Boolean)obj[7]);
				vo.setPassFlag((Boolean)obj[8]);
				return vo;
			}
			return null;
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl.findVOByName() 根据企业名称查找企业轻量级信息，出现异常！",e);
		}
	}

	/********************************************台帐系统政府专员查看企业*****************************************************/
	@Override
	public List<AccountBusinessVO> getAccountEnRegisteList(int page, int pageSize, String province,
			String city, String area,String nameOrLicNo,String btype) throws DaoException{
		try{
			Map<String ,Map<String,String>> map = setParam(province, city, area, nameOrLicNo,btype);
			Object condition = "";
			Map<String,String> param = null;
			if(map!=null && map.size() > 0){
				condition = map.keySet().iterator().next();
				param = map.get(condition);
			}

			String sql = "SELECT e.id,e.name,e.license_no,e.person_in_charge,e.type,e.enterpriteDate, " +
					"e.address,e.contact,e.telephone FROM business_unit e WHERE e.`name` IS NOT NULL " +
					"AND e.`name` <> '' AND e.organization IS NOT NULL AND e.organization <> '' " + condition.toString() +" ORDER BY e.id ";
			Query query = entityManager.createNativeQuery(sql.toString());
			getParam(param, query);
			if(page > 0){
				query.setFirstResult((page-1) * pageSize).setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return result != null && result.size() > 0 ? setListAccountBusinessVO(result):null;
		}catch (Exception e){
			throw new DaoException("BusinessUnitDAOImpl.getAccountEnRegisteList()出现异常",e);
		}
	}

	/**
	 * 将参数设置到Query中
	 * @author HY
	 */
	private void getParam(Map<String, String> param, Query query) {
		if(param!=null && param.size() > 0){
			Set keySet = param.keySet();
			Iterator it = keySet.iterator();
			while(it.hasNext()){
				Object key = it.next();
				String value = param.get(key);
				query.setParameter((String)key,value);
			}
		}
	}

	/**
	 * 将参数进行封装，
	 * 拼接sql语句
	 * @return Map<String ,Map<String,String>>
	 * @author HY
	 */
	private Map<String ,Map<String,String>> setParam(String province, String city, String area, String nameOrLicNo,String btype) {
		String condition = "";
		Map<String, String> param = new HashMap<String, String>();
		if(province!=null && !"".equals(province)){
			condition = "AND e.other_address LIKE :address";
			String parameter = province+"%";
			if(city!=null && !"".equals(city)){
				parameter = province+"-"+city+"%";
				if(area!=null && !"".equals(area)){
					parameter = province+"-"+city+"-"+area+"%";
				}
			}
			param.put("address", parameter);
		}
		if(nameOrLicNo!=null && !"".equals(nameOrLicNo)){
			condition += " AND (e.name LIKE :bname OR e.license_no LIKE :licno) ";
			param.put("bname", "%" + nameOrLicNo + "%");
			param.put("licno", "%" + nameOrLicNo + "%");
		}
		if(btype!=null && !"".equals(btype)){
			condition += " AND e.type LIKE :btype ";
			param.put("btype","%" + btype +"%");
		}
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		map.put(condition,param);
		return map;
	}

	/**
	 * 封装查询出来的结果集
	 * @auhtor HY
	 */
	private List<AccountBusinessVO> setListAccountBusinessVO(List<Object[]> result) {
		List<AccountBusinessVO> lists = new ArrayList<AccountBusinessVO>();
		for(int i = 0; i < result.size(); i++){
			Object[] objs = result.get(i);
			AccountBusinessVO vo = new AccountBusinessVO();
			vo.setId(objs[0] != null ? objs[0].toString():"-1");
			vo.setName(objs[1] != null ? objs[1].toString() : "");
			vo.setLicNo(objs[2] != null ? objs[2].toString() : "");
			vo.setPersonInCharge(objs[3] != null ? objs[3].toString() : "");
			vo.setBusType(objs[4] != null ? objs[4].toString() : "");
			vo.setRegDate(objs[5] != null ? objs[5].toString() : "");
			vo.setRegAddr(objs[6] != null ? objs[6].toString() : "");
			vo.setLinkMan(objs[7] != null ? objs[7].toString() : "");
			vo.setLinkTel(objs[8] != null ? objs[8].toString() : "");
			lists.add(vo);
		}
		return lists;
	}

	@Override
	public Long getAccountEnRegisteListTotal(String province, String city, String area,String nameOrLicNo,String btype)throws DaoException {
		try{
			Map<String ,Map<String,String>> map = setParam(province, city, area, nameOrLicNo,btype);
			Object condition = "";
			Map<String,String> param = null;
			if(map!=null && map.size() > 0){
				condition = map.keySet().iterator().next();
				param = map.get(condition);
			}
			String sql = "SELECT count(*) FROM business_unit e WHERE e.`name` IS NOT NULL " +
					"AND e.`name` <> '' AND e.organization IS NOT NULL AND e.organization <> '' " + condition;
			Query query = entityManager.createNativeQuery(sql);
			getParam(param, query);
			Object result = query.getSingleResult();
			return result != null ? Long.parseLong(result.toString()):0;
		}catch (Exception e){
			throw new DaoException("BusinessUnitDAOImpl.getAccountEnRegisteListTotal()出现异常",e);
		}
	}

	/**
	 * 根据企业id查找企业信息
	 * @param busId
	 * @throws DaoException
	 */
	@Override
	public AccountBusinessVO getAccountBusinessById(Long busId) throws DaoException {
		try{
			String sql = "SELECT b.id, b.name,b.person_in_charge,b.org_code,b.type,b.address, " +
					"b.enterpriteDate,b.email,b.contact,b.telephone,b.license_no FROM business_unit b " +
					"WHERE b.id = ?1";
			Query query = entityManager.createNativeQuery(sql.toString()).setParameter(1,busId);
			Object[] result = (Object[]) query.getSingleResult();
			AccountBusinessVO vo = null;
			if(result != null && result.length > 0){
				vo = new AccountBusinessVO();
				vo.setId(result[0]!=null?result[0].toString():"");
				vo.setName(result[1] != null ? result[1].toString() : "");
				vo.setPersonInCharge(result[2] != null ? result[2].toString() : "");
				vo.setOrgCode(result[3] != null ? result[3].toString() : "");
				vo.setBusType(result[4] != null ? result[4].toString() : "");
				vo.setRegAddr(result[5] != null ? result[5].toString() : "");
				vo.setRegDate(result[6] != null ? result[6].toString() : "");
				vo.setEmail(result[7] != null ? result[7].toString() : "");
				vo.setLinkMan(result[8] != null ? result[8].toString() : "");
				vo.setLinkTel(result[9] != null ? result[9].toString() : "");
				vo.setLicNo(result[10] != null ? result[10].toString():"");
			}
			return vo;
		}catch (Exception e){
			throw new DaoException("BusinessUnitDAOImpl.getAccountBusinessById()出现异常",e);
		}
	}
	/*************************************************************************************************/

	/**
	 * 功能描述：根据企业名称查找企业轻量级信息
	 * @author ZhangHui 2015/5/14
	 */
	@SuppressWarnings("unchecked")
	@Override
	public LightBusUnitVO findVOByName(String name) throws DaoException {
		try{		
			String sql = "SELECT id,`name`,address,other_address,type,license_no,organization,sign_flag " +
					"FROM business_unit WHERE `name` = ?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, name);
			List<Object[]> results = query.getResultList();
			if(results!=null && results.size()>0){
				Object[] obj = results.get(0);
				LightBusUnitVO vo = new LightBusUnitVO(((BigInteger)obj[0]).longValue(),
						obj[1]==null?"":obj[1].toString(),obj[2]==null?"":obj[2].toString(),
								obj[3]==null?"":obj[3].toString(),obj[4]==null?"":obj[4].toString(),
										obj[5]==null?"":obj[5].toString(),((BigInteger)obj[6]).longValue(),
												(obj[7]!=null||"1".equals(obj[7].toString()))?true:false);
				return vo;
			}
			return null;
		}catch(Exception e){
			throw new DaoException("BusinessUnitDAOImpl.findVOByName() 根据企业名称查找企业轻量级信息，出现异常！",e);
		}
	}

	/**
	 * 功能描述：获取当前企业的母企业、子企业、兄弟企业<br>
	 * 获取business TreeNode集合
	 * @author ZhangHui 2015/5/18
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessTreeDetail> getRelativesOfTreeNodes(int level,
			String keyword, Long organization) throws DaoException {
		try {
			List<BusinessTreeDetail> details = new ArrayList<BusinessTreeDetail>();
			if (level <= 1) {
				level = 1;
				String jpql = "SELECT p.name as name,COUNT(*) AS childrenNum,p.id AS id " +
						"FROM business_unit p " +
						"INNER JOIN business_unit p2 " +
						"ON p.organization = p2.parentOrgnization " +
						"WHERE p.organization = ?1 " +
						"GROUP BY p.id";
				Query query =  entityManager.createNativeQuery(jpql);
				query.setParameter(1, organization);
				List<Object[]> result = query.getResultList();
				for(Object[] obj : result){
					BusinessTreeDetail brandDetail = new BusinessTreeDetail(
							-1L,obj[0].toString(),
							Long.parseLong(obj[1].toString()),Long.parseLong(obj[2].toString()));
					details.add(brandDetail);
				}
			} else {
				String jpql = "SELECT p.`name`," +
						"SUM(CASE WHEN p3.id IS NOT NULL THEN 1 ELSE 0 END) childrenNum," +
						"p.id AS id " +
						"FROM business_unit p " +
						"INNER JOIN business_unit p2 " +
						"ON p.parentOrgnization = p2.organization AND p2.`name` = ?1 " +
						"LEFT JOIN business_unit p3 " +
						"ON p.organization = p3.parentOrgnization " +
						"GROUP BY p.id";
				Query query =  entityManager.createNativeQuery(jpql);
				query.setParameter(1, keyword);
				List<Object[]> result = query.getResultList();
				for(Object[] obj : result){
					BusinessTreeDetail brandDetail = new BusinessTreeDetail(
							-1L,obj[0].toString(),
							Long.parseLong(obj[1].toString()),Long.parseLong(obj[2].toString()));
					details.add(brandDetail);
				}
			}
			return details;
		} catch (Exception e) {
			throw new DaoException("BrandCategoryDAOImpl.getRelativesOfTreeNodes() 出现异常!", e);
		}
	}

	/**
	 * 背景：报告录入页面
	 * 功能描述：新增一条企业信息
	 * @author ZhangHui 2015/6/5
	 * @throws DaoException 
	 */
	@Override
	public void createNewRecord(String bus_name, String bus_address, String licenseno) throws DaoException {
		try {
			if(bus_name != null){
				bus_name = bus_name.replace(" ", "");
			}

			if(bus_name==null || "".equals(bus_name)){
				throw new Exception("参数为空");
			}

			String sql ="INSERT INTO business_unit(name,address,license_no) VALUES(?1,?2,?3)";

			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, bus_name);
			query.setParameter(2, bus_address);
			query.setParameter(3, licenseno);

			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("BusinessUnitDAOimpl.createNewRecord()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 背景：报告录入页面
	 * 功能描述：更新一条企业信息
	 * @author ZhangHui 2015/6/5
	 * @throws DaoException 
	 */
	@Override
	public void updateRecord(BusinessUnit buss, String licenseno) throws DaoException {
		try {
			if(buss==null || buss.getId()==null || buss.getName()==null ||
					"".equals(buss.getName())){
				throw new Exception("参数为空");
			}

			String sql ="UPDATE business_unit SET name = ?1, address = ?2, license_no = ?3 WHERE id = ?4";

			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, buss.getName());
			query.setParameter(2, buss.getAddress());
			query.setParameter(3, licenseno);
			query.setParameter(4, buss.getId());

			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("BusinessUnitDAOimpl.updateRecord()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 根据产品id查询该产品的所有生产企业
	 * @author longxianzhen 2015-08-06
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BussinessUnitVOToPortal> getBuVOToPortalByProId(Long proId)
			throws DaoException {
		try {	
			String sql ="SELECT bus.bId,bus.name,bus.address,bus.jg_qs_no,bus.jg_lic_url,bus.jg_dis_url,bus.jg_qs_url FROM ( "+
					"SELECT bu.id AS bId,bu.`name`,bu.address,ptb.jg_qs_no,bu.jg_lic_url,bu.jg_dis_url,bu.jg_qs_url FROM product_to_businessunit ptb "+
					"LEFT JOIN business_unit bu ON ptb.business_id=bu.id "+
					"WHERE ptb.PRODUCT_ID=?1 AND ptb.effect=1 GROUP BY ptb.jg_qs_no ) AS bus";

			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, proId);
			List<Object[]> objs = query.getResultList();
			List<BussinessUnitVOToPortal> bus = new ArrayList<BussinessUnitVOToPortal>();
			if(objs != null && objs.size() > 0) {
				for(Object[] obj:objs){
					BussinessUnitVOToPortal bu= new BussinessUnitVOToPortal();
					bu.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
					bu.setName(obj[1] != null ? obj[1].toString() : "");
					bu.setAddress(obj[2] != null ? obj[2].toString() : "");
					bu.setQs_no(obj[3] != null ? obj[3].toString() : "");
					bu.setLicImg(obj[4] != null ? obj[4].toString() : "");
					bu.setDisImg(obj[5] != null ? obj[5].toString() : "");
					bu.setQsImg(obj[6] != null ? obj[6].toString() : "");
					bus.add(bu);
				}
			}
			return bus;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("BusinessUnitDAOimpl.getBuVOToPortalByProId()-->" + e.getMessage(), e);
		}
	}

	public BusinessUnit finUnitSanZenInfo(long orgId) throws DaoException
	{
		String sql = "select bu.name,oi.org_code,oi.org_name,oi.start_time,oi.end_time,"
				+"li.license_no,li.license_name,li.start_time,li.end_time,"
				+"li.registration_time,li.legal_name,trc.id,trc.taxer_name"
				+" from business_unit bu"
				+" LEFT JOIN organizing_institution oi ON bu.org_code = oi.org_code"
				+" LEFT JOIN license_info li ON bu.license_no = li.license_no "
				+" LEFT JOIN tax_register_cert trc ON bu.tax_register_id = trc.id "
				+" where bu.organization = ?1";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1,orgId);
		List<Object[]> result = query.getResultList();
		if(result!=null && result.size()>0){
			LicenseInfo linces = new LicenseInfo();
			TaxRegisterInfo tax = new TaxRegisterInfo();
			OrganizingInstitution org = new OrganizingInstitution();
			BusinessUnit businessUnit = new BusinessUnit();
			Object[] obj = result.get(0);
			String name = obj[0]==null?null:obj[0].toString();
			String orgcode = obj[1]==null?null:obj[1].toString();
			String orgname = obj[2]==null?null:obj[2].toString();
			String orgStartTime = obj[3]==null?null:obj[3].toString();
			String orgEndTime = obj[4]==null?null:obj[4].toString();
			String licenseNo = obj[5]==null?null:obj[5].toString();
			String licenseName = obj[6]==null?null:obj[6].toString();
			String licenseStartTime = obj[7]==null?null:obj[7].toString();
			String licenseEndTime = obj[8]==null?null:obj[8].toString();
			String linceRegisterTime = obj[9]==null?null:obj[9].toString();
			String linceLeageName = obj[10]==null?null:obj[10].toString();
			String taxId = obj[11]==null?null:obj[11].toString();
			String taxName = obj[12]==null?null:obj[12].toString();

			businessUnit.setName(name);
			org.setOrgName(orgname);
			org.setOrgCode(orgcode);
			org.setStartTime(DateUtil.str2Date(orgStartTime));
			org.setEndTime(DateUtil.str2Date(orgEndTime));
			businessUnit.setOrgInstitution(org);
			linces.setLicenseNo(licenseNo);
			linces.setLicensename(licenseName);
			linces.setStartTime(DateUtil.str2Date(licenseStartTime));
			linces.setEndTime(DateUtil.str2Date(licenseEndTime));
			linces.setRegistrationTime(DateUtil.str2Date(linceRegisterTime));
			linces.setLegalName(linceLeageName);
			businessUnit.setLicense(linces);
			tax.setId(Long.parseLong(taxId));
			tax.setTaxerName(taxName);
			businessUnit.setTaxRegister(tax);
			return businessUnit;
		}
		return null;

	}

	/**
	 * 根据用户信息查找商超或供应商的企业信息
	 * @author longxiaznhen 2015/08/07
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BusinessUnit findSCByOrgnizationId(Long organization)
			throws DaoException {
		try {	
			String sql ="SELECT bu.id,bu.`name`,bu.address, "+
					"bu.other_address,bu.website,bu.person_in_charge, "+
					"bu.contact,bu.email,bu.postal_code,bu.telephone, "+
					"bu.fax,bu.license_no,bu.org_code,bu.distribution_no, "+
					"bu.organization ,bu.about,bu.type "+
					"FROM business_unit bu WHERE bu.organization=?1";

			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			List<Object[]> objs = query.getResultList();
			BusinessUnit bu = null;
			if(objs != null && objs.size() > 0) {
				Object[] obj=objs.get(0);
				bu= new BusinessUnit();
				bu.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
				bu.setName(obj[1] != null ? obj[1].toString() : "");
				bu.setAddress(obj[2] != null ? obj[2].toString() : "");
				bu.setOtherAddress(obj[3] != null ? obj[3].toString() : "");
				bu.setWebsite(obj[4] != null ? obj[4].toString() : "");
				bu.setPersonInCharge(obj[5] != null ? obj[5].toString() : "");
				bu.setContact(obj[6] != null ? obj[6].toString() : "");
				bu.setEmail(obj[7] != null ? obj[7].toString() : "");
				bu.setPostalCode(obj[8] != null ? obj[8].toString() : "");
				bu.setTelephone(obj[9] != null ? obj[9].toString() : "");
				bu.setFax(obj[10] != null ? obj[10].toString() : "");
				bu.setLicense(new LicenseInfo(obj[11] != null ? obj[11].toString() : ""));
				bu.setOrgInstitution(new OrganizingInstitution(obj[12] != null ? obj[12].toString() : ""));
				bu.setDistribution(new CirculationPermitInfo(obj[13] != null ? obj[13].toString() : ""));
				bu.setOrganization(obj[14] != null ? Long.parseLong(obj[14].toString()) : -1L);
				bu.setAbout(obj[15] != null ? obj[15].toString() : "");
				bu.setType(obj[16] != null ? obj[16].toString() : "");
			}
			return bu;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("BusinessUnitDAOimpl.getBuVOToPortalByProId()-->" + e.getMessage(), e);
		}
	}

	@Override
	public boolean updateSignStatus(String busName, boolean signFlag,
			boolean passFlag) throws DaoException {
		try {
			String sql ="UPDATE business_unit SET sign_flag = ?1, pass_flag = ?2 WHERE name = ?3";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, signFlag);
			query.setParameter(2, passFlag);
			query.setParameter(3, busName);
			query.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("BusinessUnitDAOimpl.updateRecord()-->" + e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public BusinessUnit findByNameOrganization(String name) {
		String sql = "SELECT id,organization FROM business_unit WHERE NAME=?1";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, name);
		List<Object[]> objs = query.getResultList();
		BusinessUnit bu = null;
		if (objs != null && objs.size() > 0) {
			Object[] obj = objs.get(0);
			bu = new BusinessUnit();
			bu.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
			bu.setOrganization(obj[1] != null ? Long.parseLong(obj[1].toString()) : -1L);
		}
		return bu;
	}

	@Override
	public void updateBusinessUnit(String strImg,Long id, String url) {
		try {
			String sql ="UPDATE business_unit SET "+strImg+"=?1";
			 sql+=" WHERE id = ?2 ";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, url);
			query.setParameter(2, id);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Resource> getByIdResourceList(Long id,String strColumn) {
		List<Resource> resList = new ArrayList<Resource>();
		try {
			String sql = " SELECT pbl.id,pbl.business_name,";
			sql += " ttr.RESOURCE_ID,ttr.FILE_NAME,ttr.RESOURCE_NAME,ttr.URL,ttr.UPLOAD_DATE,tsrt.RESOURCE_TYPE_ID,";
			sql += " tsrt.CONTENT_TYPE,tsrt.RESOURCE_TYPE_DESC,tsrt.RESOURCE_TYPE_NAME FROM product_business_license pbl ";
			sql += " LEFT JOIN t_test_resource ttr ON  ttr.RESOURCE_ID = " + strColumn;
			sql += " LEFT JOIN T_SYS_RESOURCE_TYPE tsrt ON tsrt.RESOURCE_TYPE_ID = ttr.RESOURCE_TYPE_ID ";
			sql += " WHERE product_id = ?1 ORDER BY pbl.id ASC ";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, id);
			List<Object[]> objs = query.getResultList();
			for (Object[] obj : objs) {
				Resource res = new Resource();
				res.setBusinessToId(obj[0]==null?null:Long.parseLong(obj[0].toString()));
				res.setBusinessName(obj[1]==null?null:obj[1].toString());
				res.setId(obj[2]==null?null:Long.parseLong(obj[2].toString()));
				res.setFileName(obj[3]==null?null:obj[3].toString());
				res.setName(obj[4]==null?null:obj[4].toString());
				res.setUrl(obj[5]==null?null:obj[5].toString());
				res.setUploadDate(obj[6]==null?null:(Date)obj[6]);
				ResourceType rt = new ResourceType();
				rt.setRtId(obj[7]==null?null:Long.parseLong(obj[7].toString()));
				rt.setContentType(obj[8]==null?null:obj[8].toString());
				rt.setRtDesc(obj[9]==null?null:obj[9].toString());
				rt.setRtName(obj[10]==null?null:obj[10].toString());
				res.setType(rt);
				resList.add(res);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    return resList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BussinessUnitVOToPortal> getBuVOToPortalByBarcode(String barcode) {
		 List<BussinessUnitVOToPortal> bus = new ArrayList<BussinessUnitVOToPortal>();
		try {
			String sql ="SELECT DISTINCT t.id,t.name,t.address,t.jg_lic_url,t.jg_dis_url,t.jg_qs_url,t.qs_no " ;
			        sql +=" FROM (" ;
			        sql +="SELECT  bu.id,bu.name,bu.address,bu.jg_lic_url,bu.jg_dis_url,bu.jg_qs_url, " ;
				    sql+="(SELECT MAX(pis.qs_no)   FROM product_instance pis WHERE pis.product_id=p.id) qs_no ";
					sql+="FROM business_unit  bu "; 
					sql+="INNER JOIN product  p ON bu.id = p.producer_id ";
					sql+="WHERE  p.barcode = ?1 ";
					sql+="UNION ALL "; 
					sql+="SELECT bus.id,bus.name,bus.address,bus.jg_lic_url,bus.jg_dis_url,bus.jg_qs_url, ";
					sql+="(SELECT MAX(pis.qs_no)  FROM product_instance pis WHERE pis.product_id=ps.id) qs_no ";
					sql+="FROM business_unit bus "; 
					sql+="INNER JOIN product_business_license pbl ON bus.id = pbl.business_id ";
					sql+="INNER JOIN product ps ON pbl.product_id = ps.id "; 
					sql+="WHERE ps.barcode = ?1 ) t ";
					Query query = entityManager.createNativeQuery(sql);
					query.setParameter(1, barcode);
					List<Object[]> objs = query.getResultList();
					
					if(objs != null && objs.size() > 0) {
						for(Object[] obj:objs){
							BussinessUnitVOToPortal bu= new BussinessUnitVOToPortal();
							bu.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
							bu.setName(obj[1] != null ? obj[1].toString() : "");
							bu.setAddress(obj[2] != null ? obj[2].toString() : "");
							bu.setLicImg(obj[3] != null ? obj[3].toString() : "");
							bu.setDisImg(obj[4] != null ? obj[4].toString() : "");
							bu.setQsImg(obj[5] != null ? obj[5].toString() : "");
							bu.setQs_no(obj[6] != null ? obj[6].toString() : "");
							bus.add(bu);
						}
					}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return bus;
	}
	/**
	 * 根据企业名称.营业执照,生产许可证号查询企业相关情况
	 */
	public BusinessUnit getBusinessUnitByCondition(String businessName,String qsNo,String licenseNo){
		
		if(licenseNo!=null&&!licenseNo.equals("")){
			String sql="select e from "+this.entityClass.getName()+" e where e.license.licenseNo=?1";
			Query query = entityManager.createQuery(sql);
			query.setParameter(1, licenseNo);
			List<BusinessUnit> businessUnitList=query.getResultList();
			if(businessUnitList.size()>0){
				return businessUnitList.get(0);
			}
		}
		if(businessName!=null&&!businessName.equals("")){
			String sql="select e from "+this.entityClass.getName()+" e where e.name=?1";
			Query query = entityManager.createQuery(sql);
			query.setParameter(1, businessName);
			List<BusinessUnit> businessUnitList=query.getResultList();
			if(businessUnitList.size()>0){
				return businessUnitList.get(0);
			}
		}
		if(qsNo!=null&&!qsNo.equals("")){
			String sql = "SELECT bu.id,bu.name FROM business_unit bu ";
			sql +=" inner JOIN product_to_businessunit ptb  ON bu.id = ptb.business_id ";
			sql +=" inner JOIN production_license_info pli ON pli.id=ptb.qs_id ";
			sql +=" WHERE pli.qs_no=?1 ";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, qsNo);
			List<BusinessUnit> businessUnitList=query.getResultList();
			if(businessUnitList.size()>0){
				return businessUnitList.get(0);
			}
		}
		return null;
	}
}