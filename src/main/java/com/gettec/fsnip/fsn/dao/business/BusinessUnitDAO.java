package com.gettec.fsnip.fsn.dao.business;

import java.util.List;
import java.util.Map;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.vo.BusinessStaVO;
import com.gettec.fsnip.fsn.vo.business.AccountBusinessVO;
import com.gettec.fsnip.fsn.vo.business.BusinessTreeDetail;
import com.lhfs.fsn.vo.business.BussinessUnitVOToPortal;
import com.lhfs.fsn.vo.business.LightBusUnitVO;

public interface BusinessUnitDAO extends BaseDAO<BusinessUnit>{

	public int getProductCount();

	public List<BusinessUnit> findByName_(String name) throws DaoException;

	public List<String> getListOfQsNo(String firstpart,Long formatId) throws DaoException;

	public List<BusinessUnit> getListByName(int page, int pageSize, String name,String type) throws DaoException;

	public List<BusinessUnit> getSubsidiaryListByOrgPage(Map<String, Object> map, int page, int pageSize )throws DaoException;

	public Long getCountByOrg(Map<String, Object> map)throws DaoException;

	public boolean verificationNameOrLic(String val,String type)throws DaoException;

	public BusinessUnit findByOrgnizationId(Long orgnizationId) throws DaoException;

	public BusinessUnit getBusinessByOrganizationIdOfLigth(Long busId) throws DaoException;

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
	public List<BusinessStaVO> findBusinessIdByNameType(int page, int pageSize,String businessName,
			String businessType,String startDate,String endDate)throws DaoException;
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
	public Long getBusinessStaCountByConfigure(String businessName,
			String businessType, String startDate, String endDate)throws DaoException;
	
	public List<String> getAllBusUnitName(String name, int page, int pageSize) throws DaoException;
	
	public List<String> getAllLicenseNoAndId() throws DaoException;
	
	public List<String> getAllBusUnitAddressAndId() throws DaoException;
	/**
	 * 获取总企业id根据该企业的组织机构
	 * @param organization 
	 * @return Long
	 * @throws DaoException
	 * 
	 * @author HuangYog 
	 */
	public Long getIdByOrganization(Long organization) throws DaoException;
	
	/**
	 * 根据企业名称获取企业id
	 * @author ZhangHui 2015/6/2
	 */
	public Long getIdByName(String name) throws DaoException;

	/**
	 * 根据 企业的id号获取总企业的前半部分为
	 * ‘firstpart’的所有qs号（包括旗下所有的子企业QS号）
	 * @param businessUnitId
	 * @param firstpart
	 * @return List<String>
	 * @throws DaoException
	 * 
	 * @author HuangYog
	 */
    public List<String> getSonQsList4BussUnitId(Long businessUnitId,String firstpart,Long formatId)throws DaoException;

    /**
     * 根据 企业的parentOrganization号获取当前登录子公司之外，
     * 已经被其他子公司绑定过的且前半部分是‘firstpart’ 的 qs号 
     * @param parentOrganization 付组织机构
     * @param myOrganization 
     * @param firstpart qs号的前部分
     * @return List<String> 
     * @throws DaoException
     * 
     * @author HuangYog
     */
    public List<String> getSonBussUnitIdByParentOrganizationId(Long parentOrganization,
            Long myOrganization,String firstpart,Long formatId) throws DaoException;

    /**
	 * 获取被总企业绑定过的且前半部分是‘firstpart’ 的qs号（不包括子企业） 
	 * @param businessUnitId 
	 * @param firstpart qs号的前部分
	 * @return List<String>
	 * @throws DaoException
	 * 
	 * @author HuangYog
	 */
	public List<String> getproToBusQsListByBusId(Long businessUnitId,String firstpart,Long formatId)throws DaoException;
    
    public boolean validateBusUnitOrgCode(String orgCode, Long orgId) throws DaoException;

	public String findLicenseByName(String name) throws DaoException;

	public BusinessUnit findByName(String name) throws DaoException;

	boolean findSignFlagByName(String busName) throws DaoException;

	public long countMarketByOrganization(Map<String, Object> map, Long organization)throws DaoException;
	
	public String getMarketNameByOrganization(Long organization) throws DaoException;
	
	public Long unitCount() throws DaoException;
	/**
	 * 客户管理新增客户中的按关键字并分页自动加载客户名称
	 * @author HuangYog
	 * Create date 2015/04/13
	 */
	public Object getAllBusUnitName(Integer page,Integer pageSize, String keyword,String busType)throws DaoException;

	/**
	 * 根据企业guid，查找一条企业信息
	 * @author ZhangHui 2015/4/24
	 */
	public BusinessUnit findByGuid(String bus_guid) throws DaoException;

	/**
	 * 功能描述：根据组织机构id获取企业信息<br>
	 * 作用于判断某产品有没有被生产企业接管。
	 * @author ZhangHui 2015/5/1
	 */
	public LightBusUnitVO findBusVOByOrg(Long organization) throws DaoException;

	/**
	 * 功能描述：根据企业名称查找企业轻量级信息
	 * @author ZhangHui 2015/5/14
	 */
	public LightBusUnitVO findVOByName(String name) throws DaoException;

	/**
	 * 功能描述：获取当前企业的母企业、子企业、兄弟企业<br>
	 * 获取business TreeNode集合
	 * @author ZhangHui 2015/5/18
	 */
	public List<BusinessTreeDetail> getRelativesOfTreeNodes(int level,
			String keyword, Long organization) throws DaoException;

	
	public String findNameByOrganization(Long organization) throws DaoException;

	/**
	 * 背景：报告录入页面
	 * 功能描述：新增一条企业信息
	 * @author ZhangHui 2015/6/5
	 * @throws DaoException 
	 */
	public void createNewRecord(String bus_name, String bus_address, String licenseNo) throws DaoException;

	/**
	 * 背景：报告录入页面
	 * 功能描述：更新一条企业信息
	 * @author ZhangHui 2015/6/5
	 * @throws DaoException 
	 */
	public void updateRecord(BusinessUnit buss, String licenseNo) throws DaoException;


	/**
	 * 政府专员查看企业信息，根据企业地址过滤企业
	 * @param province 省
	 * @param city 市
	 * @param area 县（区域）
	 * @return List<EnterpriseRegiste>
	 * @author HY
	 */
	List<AccountBusinessVO> getAccountEnRegisteList(int page, int pageSize, String province, String city, String area,String nameOrLicNo,String btype)throws DaoException;

	/**
	 * 政府专员查看企业信息，根据企业地址过滤企业 加载出的总数
	 * @return Long
	 * @author HY
	 */
	Long getAccountEnRegisteListTotal(String province, String city, String area,String nameOrLicNo,String btype)throws DaoException;

	/**
	 * 根据企业id查看企业详情
	 * @param busId
	 * @return AccountBusinessVO
	 * @author HY
	 */
	AccountBusinessVO getAccountBusinessById(Long busId)throws DaoException;

	/**
	 * 功能描述：根据企业id查找企业组织机构
	 * @author ZhangHui 2015/7/1
	 * @throws DaoException 
	 */
	public Long findOrgById(Long organization) throws DaoException;

	/**
	 * 功能描述：根据企业id查找企业名称
	 * @author ZhangHui 2015/7/3
	 * @throws DaoException 
	 */
	public String findNameById(Long id) throws DaoException;

	/**
	 * 根据产品id查询该产品的所有生产企业
	 * @author longxianzhen 2015-08-06
	 */
	public List<BussinessUnitVOToPortal> getBuVOToPortalByProId(Long proId)throws DaoException;

	public BusinessUnit finUnitSanZenInfo(long orgId) throws DaoException;

	/**
	 * 根据用户信息查找商超或供应商的企业信息
	 * @author longxiaznhen 2015/08/07
	 */
	public BusinessUnit findSCByOrgnizationId(Long organization)throws DaoException;

	/**
	 * 更新企业签名状态
	 * @param busName
	 * @param signFlag
	 * @throws ServiceException
	 */
	public boolean updateSignStatus(String busName, boolean signFlag,
			boolean passFlag)throws DaoException;
	
	/**
	 * 根据名称查找该组织机构
	 * @param name
	 * @return
	 */
	public BusinessUnit findByNameOrganization(String name);

	/**
	 * 更新企业信息
	 * @param id
	 * @param url
	 */
	public void updateBusinessUnit(String strImg,Long id, String url);
	/**
	 * 更加产品ID查询所有的企业信息
	 *
	 */
	public List<Resource> getByIdResourceList(Long id,String strImg);

	/**
	 * 根据条形码查询生产企业
	 * @param barcode
	 * @return
	 */
	public List<BussinessUnitVOToPortal> getBuVOToPortalByBarcode(String barcode);
	
	public BusinessUnit getBusinessUnitByCondition(String businessName,String qsNo,String licenseNo);

}