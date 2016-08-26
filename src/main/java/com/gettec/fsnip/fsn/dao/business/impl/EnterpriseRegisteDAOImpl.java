package com.gettec.fsnip.fsn.dao.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.business.EnterpriseRegisteDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;


/**
 * BusinessType customized operation implementation
 * 
 * @author 
 */
@Repository(value="enterpriseRegisteDAO")
public class EnterpriseRegisteDAOImpl extends BaseDAOImpl<EnterpriseRegiste>
		implements EnterpriseRegisteDAO {

	
	/**
	 * dao层获取所有注册企业集合有分页处理
	 */
	@Override
	public List<EnterpriseRegiste> getEnRegisteListByPage(int page,
			int pageSize, Map<String,Object> map) throws DaoException {
		try {
			String condition ="";
			Object[] params=null;
			if(map!=null){
				condition = (String) map.get("condition") + " and e.password!=null ";
	            params = (Object[]) map.get("params");
			}else{
				condition = " where e.password!=null ";
			}		
            condition += " order by e.id desc";
            return this.getListByPage(page, pageSize, condition, params);
		} catch (Exception e) {
			throw new DaoException("dao层获取所有注册企业集合出错", e);
		}
	}

	
	/**
	 * dao层查询所有注册企业个数
	 */
	@Override
	public Long getAllCount(Map<String,Object> map) throws DaoException {
		try {
			String condition ="";
			Object[] params=null;
			if(map!=null){
				condition = (String) map.get("condition") + " and e.password!=null ";
	            params = (Object[]) map.get("params");
			}else{
				condition = " where e.password!=null ";
			}
	        return this.count(condition, params);
		} catch (Exception e) {
			throw new DaoException("dao层查询所有注册企业个数出错", e);
		}
	}

	/**
	 * 验证注册企业名称唯一性
	 */
	@Override
	public boolean verificationEnName(String name) throws DaoException {
		int count = 0;
		try {
			String jpql = "SELECT count(*) FROM enterprise_registe WHERE enterpriteName=?1";
			Query query = entityManager.createNativeQuery(jpql).setParameter(1, name);
			Number result=(Number)query.getSingleResult();
			count=result.intValue();			
		} catch (Exception e) {
			throw new DaoException("dao层验证企业名称唯一性出错", e);
		}
		return count>0?true:false;
	}

	/**
	 * 验证注册企业用户名唯一性
	 */
	@Override
	public boolean verificationEnUserName(String userName) throws DaoException {
		int count = 0;
		try {
			String jpql = "SELECT count(*) FROM enterprise_registe WHERE userName=?1";
			Query query = entityManager.createNativeQuery(jpql).setParameter(1, userName);
			Number result=(Number)query.getSingleResult();
			count=result.intValue();			
		} catch (Exception e) {
			throw new DaoException("dao层验证企业名称唯一性出错", e);
		}
		return count>0?true:false;
	}

	/**
	 * 按企业名称查找一条企业信息
	 * @throws DaoException 
	 */
	@Override
	public EnterpriseRegiste findbyEnterpriteName(String enterpriteName) throws DaoException {
		try{
			String condition=" where enterpriteName = ?1";
			Object[] params = new Object[]{enterpriteName};
			List<EnterpriseRegiste> result = this.getListByCondition(condition, params);
			if(result.size() > 0){
				return result.get(0);
			}
			return null;
		}catch(JPAException jpae){
			throw new DaoException("【dao-error】验证当前品牌是否已经存在时出现异常！",jpae.getException());
		}
	}
	
	/**
	 * 按企业名称查找一条企业logo
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findLogoByEnterpriteName(String enterpriteName) throws DaoException {
		try{
			String sql = "SELECT URL FROM t_test_resource WHERE RESOURCE_ID =( " +
                         "SELECT RESOURCE_ID FROM t_business_logo_to_resource " +
                         "WHERE ENTERPRISE_REGISTE_ID = (SELECT id FROM " +
                         "enterprise_registe WHERE enterpriteName=?1));";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, enterpriteName);
			List<Object> result = query.getResultList();
			if(result.size()>0){
				return result.get(0).toString();
			}
			return null;
		}catch(Exception e){
			throw new DaoException("按企业名称查找一条企业logo, 出现异常！", e);
		}
	}
	
	/**
	 * 获取企业类型（行业分类）
	 * @return String[]
	 */
    @Override
    public List<String> getBuseinssUnitType() throws DaoException {
        List<String> list = new ArrayList<String>();
        try {
            String sql = "SELECT DISTINCT ent.enterpriteType FROM enterprise_registe ent WHERE ent.enterpriteType IS NOT NULL ";
            list = this.getListBySQLWithoutType(String.class, sql, null);
        } catch (JPAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据企业类型查询相关的企业，分页显示
     * @param page
     * @param pageSize
     * @param type 
     * @return List<EnterpriseRegiste>
     */
    @Override
    public List<String> getBuseinssUnitByType(int page, int pageSize,String type) throws DaoException {
        List<String> list = new ArrayList<String>();
        try {
        	if(page < 1){
    			return null;
    		}
            String sql = "SELECT ent.enterpriteName FROM enterprise_registe ent " +
                    "WHERE ent.enterpriteType = ?1 ORDER BY ent.id LIMIT ?2,?3";
            list = this.getListBySQLWithoutType(String.class, sql, new Object[] {type , (page-1)*pageSize , pageSize});
        } catch (JPAException e) {
            e.printStackTrace();
        }
        return list.size() > 0 ? list:null;
    }

    /**
     * 根据企业类型查询相关的企业 的总数
     * @param type 企业类型 
     * @return Object
     * @throws DaoException
     */
    @Override
    public Object getBuseinssUnitCountByType(String type) throws DaoException {
        try {
            String sql = "SELECT count(ent.enterpriteName) FROM enterprise_registe ent WHERE ent.enterpriteType = ?1 ";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, type);
            Object count = query.getSingleResult();
            return count;
        } catch (Exception e) {
            throw new DaoException("EnterpriseRegisteDAOImpl.getBuseinssUnitCountByType() "+e.getMessage(),e);
        }
        
    }

    ////* 查询相关企业类型下面的 与模糊名称匹配的企业名称 的总数
	@Override
	public Object getBuseinssUnitCountByTypeAndName(String type, String name)
			throws DaoException {
		try {
            String sql = "SELECT count(ent.enterpriteName) FROM enterprise_registe ent WHERE ent.enterpriteType = ?1 AND ent.enterpriteName LIKE ?2 ";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, type);
            query.setParameter(2, "%"+name+"%");
            Object count = query.getSingleResult();
            return count;
        } catch (Exception e) {
            throw new DaoException("EnterpriseRegisteDAOImpl.getBuseinssUnitCountByTypeAndName() "+e.getMessage(),e);
        }
	}

	//* 查询相关企业类型下面的 与模糊名称匹配的企业名称
	@Override
	public List<String> getBuseinssUnitByTypeAndName(int page, int pageSize,
			String type, String name) throws DaoException {
		List<String> list = new ArrayList<String>();
		if(page < 1){
			return null;
		}
		try {
			String sql =" SELECT ent.enterpriteName FROM enterprise_registe ent "+
	                "WHERE ent.enterpriteType = ?1 AND ent.enterpriteName LIKE ?2  ORDER BY ent.id LIMIT ?3,?4 ";
            list = this.getListBySQLWithoutType(String.class, sql, new Object[] {type ,"%"+name+"%", (page-1)*pageSize , pageSize});
        } catch (JPAException e) {
        	throw new DaoException("EnterpriseRegisteDAOImpl.getBuseinssUnitByTypeAndName() "+e.getMessage(),e);
        }
        return list.size() > 0 ? list:null;
	}
	
	/**
     * 根据营业执照号，获取已注册企业数量
     * @param licenseNo 营业执照号
     * @return 已经使用该营业执照号注册的已注册企业数量<br>
     * @author ZhangHui 2015/4/29
     */
	@Override
	public long countByLicenseNo(String licenseNo) throws DaoException {
		try{
			String condition = " WHERE e.licenseNo = ?1";
			return this.count(condition, new Object[]{licenseNo});
		}catch(JPAException e){
			throw new DaoException("BusinessUnitDAOImpl.countByLicenseNo()-->" + e.getMessage(), e.getException());
		}
	}
	/**
	 * 根据餐饮服务许可证号，获取已注册企业数量
	 * @param serviceNo 餐饮服务许可证号
	 * @return 已经使用该营业执照号注册的已注册企业数量<br>
	 * @author HCJ 2015/5/17
	 */
	public long countByServiceNo(String serviceNo) throws DaoException {
		try{
			String condition = " WHERE e.serviceNo = ?1";
			return this.count(condition, new Object[]{serviceNo});
		}catch(JPAException e){
			throw new DaoException("BusinessUnitDAOImpl.countByServiceNo()-->" + e.getMessage(), e.getException());
		}
	}
	/**
	 * 根据食品流通许可证号，获取已注册企业数量
	 * @param passNo 食品流通许可证号
	 * @return 已经使用该营业执照号注册的已注册企业数量<br>
	 * @author HCJ 2015/5/17
	 */
	public long countByPassNo(String passNo) throws DaoException {
		try{
			String condition = " WHERE e.passNo = ?1";
			return this.count(condition, new Object[]{passNo});
		}catch(JPAException e){
			throw new DaoException("BusinessUnitDAOImpl.countByPassNo()-->" + e.getMessage(), e.getException());
		}
	}
	/**
	 * 根据s生产许可证号，获取已注册企业数量
	 * @param productNo 生产许可证号
	 * @return 已经使用该营业执照号注册的已注册企业数量<br>
	 * @author HCJ 2015/5/17
	 */
	public long countByProductNo(String productNo) throws DaoException {
		try{
			String condition = " WHERE e.productNo = ?1";
			return this.count(condition, new Object[]{productNo});
		}catch(JPAException e){
			throw new DaoException("BusinessUnitDAOImpl.countByProductNo()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 根据企业名称 营业执照 组织机构代码查找注册信息
	 * @author HY
	 */
	@Override
	public EnterpriseRegiste getEnteryByLicNoAndOrgCodeAndBName(String licNo, String orgCode, String bName) throws DaoException {
		String condition = " WHERE e.enterpriteName = ?1 AND e.licenseNo = ?2 AND e.organizationNo = ?3 ".toString();
		List<EnterpriseRegiste> er = null;
		try {
			er = this.getListByCondition(condition, new Object[]{bName, licNo, orgCode});
			return er!=null && er.size() > 0 ? er.get(0):null;
		} catch (JPAException e) {
			throw new DaoException("BusinessUnitDAOImpl.countByLicenseNo()-->"+e.getMessage(),e.getException());
		}
	}
}