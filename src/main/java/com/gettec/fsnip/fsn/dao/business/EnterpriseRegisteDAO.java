package com.gettec.fsnip.fsn.dao.business;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;

/**
 * enterpriseRegiste customized operation implementation
 * 
 * @author 
 */
 public interface EnterpriseRegisteDAO extends BaseDAO<EnterpriseRegiste>{

	List<EnterpriseRegiste> getEnRegisteListByPage(int page, int pageSize,
			Map<String,Object> map) throws DaoException;

	Long getAllCount(Map<String,Object> map)throws DaoException;

	boolean verificationEnName(String name)throws DaoException;

	boolean verificationEnUserName(String userName)throws DaoException;

	EnterpriseRegiste findbyEnterpriteName(String name) throws DaoException;

	String findLogoByEnterpriteName(String name) throws DaoException;

	/**
	 * 获取企业类型（行业分类）
	 * @return List<String>
	 * @throws DaoException
	 */
	List<String> getBuseinssUnitType() throws DaoException;

	/**
	 * 根据企业类型查询相关的企业，分页显示
	 * @param page
	 * @param pageSize
	 * @param type 
	 * @return List<EnterpriseRegiste>
	 * @throws DaoException
	 */
    List<String> getBuseinssUnitByType(int page, int pageSize, String type) throws DaoException;
/**
 * 根据企业类型查询相关的企业 的总数
 * @param type 企业类型 
 * @return Object
 * @throws DaoException
 */
    Object getBuseinssUnitCountByType(String type) throws DaoException;

    //* 查询相关企业类型下面的 与模糊名称匹配的企业名称 的总数
    Object getBuseinssUnitCountByTypeAndName(String type,String name)throws DaoException;

    //* 查询相关企业类型下面的 与模糊名称匹配的企业名称
	List<String> getBuseinssUnitByTypeAndName(int page, int pageSize, String type,
		String name)throws DaoException;

	/**
     * 根据营业执照号，获取已注册企业数量
     * @param licenseNo 营业执照号
     * @return 已经使用该营业执照号注册的已注册企业数量<br>
     * @author ZhangHui 2015/4/29
     */
	public long countByLicenseNo(String licenseNo) throws DaoException;
	
	/**
	 * 根据餐饮服务许可证号，获取已注册企业数量
	 * @param serviceNo 餐饮服务许可证号
	 * @return 已经使用该营业执照号注册的已注册企业数量<br>
	 * @author HCJ 2015/5/17
	 */
	public long countByServiceNo(String serviceNo) throws DaoException;
	/**
	 * 根据食品流通许可证号，获取已注册企业数量
	 * @param passNo 食品流通许可证号
	 * @return 已经使用该营业执照号注册的已注册企业数量<br>
	 * @author HCJ 2015/5/17
	 */
	public long countByPassNo(String passNo) throws DaoException;
	/**
	 * 根据s生产许可证号，获取已注册企业数量
	 * @param productNo 生产许可证号
	 * @return 已经使用该营业执照号注册的已注册企业数量<br>
	 * @author HCJ 2015/5/17
	 */
	public long countByProductNo(String productNo) throws DaoException;

	/**
	 * 根据企业名称 营业执照 组织机构代码查找注册信息
	 * @param licNo 营业执照
	 * @param orgCode 组织机构代码
	 * @param bName 企业名称
	 * @return EnterpriseRegiste
	 * @author HY
	 */
	EnterpriseRegiste getEnteryByLicNoAndOrgCodeAndBName(String licNo, String orgCode, String bName)throws DaoException;
}