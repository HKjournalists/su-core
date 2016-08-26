package com.gettec.fsnip.fsn.service.business;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.business.EnterpriseRegisteDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.vo.business.BussinessUnitVO;


public interface EnterpriseRegisteService extends BaseService<EnterpriseRegiste, EnterpriseRegisteDAO>{

	void save(BusinessUnit businessUnit, AuthenticateInfo info) throws ServiceException;

	void update(BusinessUnit businessUnit) throws ServiceException;

	String findLogoByName(String name) throws ServiceException;
	
	/**
	 * 根据企业id加载企业信息  包括：企业名称、企业法人、企业证照、主营商品
	 * @param id
	 * @return BussinessUnitVO
	 * @throws ServiceException
	 */
	public BussinessUnitVO loadBusinessUnit(String name) throws ServiceException;

	/**
	 * 获取企业类型（行业分类）
	 * @return List<String>
	 * @throws ServiceException
	 */
	List<String> getBuseinssUnitType() throws ServiceException;

	/**
	 * 根据企业类型查找相关的所有企业 的名称
	 * @param type  企业类型
	 * @param page  当前页
	 * @param pageSize  
	 * @return List<String> 企业名称
	 * @throws ServiceException
	 */
    List<String> getBuseinssUnitByType(int page,int pageSize , String type) throws ServiceException;

    /**
     * 根据企业类型查找相关的所有企业 的名称
     * @param type 企业类型
     * @return Object
     * @throws ServiceException
     */
    Object getBuseinssUnitCountByType(String type)throws ServiceException;

	List<EnterpriseRegiste> getEnRegisteListByPage(int page, int pageSize,
			Map<String, Object> configure) throws ServiceException;

	Long getAllCount(Map<String, Object> configureEn) throws ServiceException;

	EnterpriseRegiste findbyEnterpriteName(String name) throws ServiceException;

	boolean verificationEnName(String name) throws ServiceException;

	boolean verificationEnUserName(String userName) throws ServiceException;

	/**
	 * 查询相关企业类型下面的 与模糊名称匹配的企业名称
	 * @param page
	 * @param pageSize
	 * @param type 企业类型
	 * @param name 用户输入企业名称的关键字
	 * @return List<String> 企业名称
	 * @throws ServiceException
	 */
	List<String> getBuseinssUnitNameByTypeAndName(int page, int pageSize,
			String type, String name)throws ServiceException;

	/**
	 * 查询相关企业类型下面的 与模糊名称匹配的企业名称 的总数
	 * @param type 企业类型
	 * @param name 用户输入企业名称的关键字
	 * @return Object 总数
	 * @throws ServiceException
	 */
	Object getBuseinssUnitNameCountByType(String type, String name)throws ServiceException;

	/**
     * 根据营业执照号，获取已注册企业数量
     * @param licenseNo 营业执照号
     * @return 已经使用该营业执照号注册的已注册企业数量<br>
     * @author ZhangHui 2015/4/29
     */
	public long countByLicenseNo(String licenseNo) throws ServiceException;
	/**
	 * 根据餐饮服务许可证号，获取已注册企业数量
	 * @param serviceNo 餐饮服务许可证号
	 * @return 已经使用该营业执照号注册的已注册企业数量<br>
	 * @author HCJ 2015/5/17
	 */
	public long countByServiceNo(String serviceNo) throws ServiceException;
	/**
	 * 根据食品流通许可证号，获取已注册企业数量
	 * @param passNo 食品流通许可证号
	 * @return 已经使用该营业执照号注册的已注册企业数量<br>
	 * @author HCJ 2015/5/17
	 */
	public long countByPassNo(String passNo) throws ServiceException;
	/**
	 * 根据s生产许可证号，获取已注册企业数量
	 * @param productNo 生产许可证号
	 * @return 已经使用该营业执照号注册的已注册企业数量<br>
	 * @author HCJ 2015/5/17
	 */
	public long countByProductNo(String productNo) throws ServiceException;

	/**
	 * 根据企业名称 营业执照 组织机构代码查找注册信息
	 * @param licNo 营业执照
	 * @param orgCode 组织机构代码
	 * @param name 企业名称
	 * @return EnterpriseRegiste
	 * @author HY
	 */
	EnterpriseRegiste getEnteryByLicNoAndOrgCodeAndBName(String licNo, String orgCode, String name)throws ServiceException;
}
