package com.gettec.fsnip.fsn.service.business;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.springframework.ui.Model;

import com.gettec.fsnip.fsn.dao.business.BusinessUnitDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.business.AccountBusinessVO;
import com.gettec.fsnip.fsn.vo.business.BusinessTreeNode;
import com.gettec.fsnip.fsn.vo.business.ExlVO;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.vo.BusinessUnitVO;
import com.lhfs.fsn.vo.business.BussinessUnitVOToPortal;
import com.lhfs.fsn.vo.business.LightBusUnitVO;

public interface BusinessUnitService extends BaseService<BusinessUnit, BusinessUnitDAO>{
	public boolean checkUniqueName(String name) throws ServiceException;
	
	public int getProductCount();
	
	public List<BusinessUnit> findByName_(String name) throws ServiceException;

	public List<String> getListOfQsNo(String firstpart,Long formatId) throws ServiceException;

	/**
	 * 功能描述：报告录入页面，保存被检单位
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/7
	 */
	public BusinessUnit saveTestee(String testee) throws ServiceException;

	public List<BusinessUnit> getListByName(String name, String type, int page,
			int pageSize) throws ServiceException;

	public List<BusinessUnit> getSubsidiaryListByOrgPage(int page,
			int pageSize, String configure, Long org)throws ServiceException;

	public Long getCountByOrg(Long organization, String configure)throws ServiceException;

	public void addSubsidiary(BusinessUnit businessUnit, Long org)throws ServiceException;

	public boolean verificationNameOrLic(String val,String type)throws ServiceException;

	public void editSubsidiary(BusinessUnit businessUnit)throws ServiceException;

	public void saveEnterpriseRegisteInfo(EnterpriseRegiste enRegiste)throws ServiceException;

	public List<EnterpriseRegiste> getEnRegisteListByPage(int page,
			int pageSize, String configure)throws ServiceException;

	public Long getAllCount(String configure)throws ServiceException;

	public EnterpriseRegiste spprove(Long id ,boolean signFlag)throws ServiceException;

	public BusinessUnit findByIdOfLigth(Long busId) throws ServiceException;

	public Model batchAddSubsidiary(ExlVO exlVO, Long currentUserOrganization,Model model)throws ServiceException;

	public boolean verificationEnName(String name)throws ServiceException;

	public boolean verificationEnUserName(String userName)throws ServiceException;

	public void noPassReturn(String returnMes, Long id)throws ServiceException;
	
	public List<String> getAllBusUnitName(String name, int page, int pageSize) throws ServiceException;
	
	public List<String> getAllBusUnitLicenseNoAndId() throws ServiceException;
	
	public List<String> getAllAddressAndId() throws ServiceException;
	
	public BusinessUnit findByInfo(AuthenticateInfo info, boolean isLoadImg, boolean isCompatibilityNotRegiste) throws ServiceException;
	
	public BusinessUnit findByInfo2(AuthenticateInfo info, BusinessUnit businessUnit, boolean isLoadImg, boolean isCompatibilityNotRegiste) throws ServiceException;

	void updateBusinessUnit(BusinessUnit businessUnit) throws ServiceException;

	public JSONObject updateBusInfoInSSO(BusinessUnit businessUnit,
			BusinessUnit orig_businessUnit) throws ServiceException;

	public void updateBusinessUnit(BusinessUnit businessUnit, String step) throws ServiceException;
	
	List<Resource> getBusinessPdfsByBusUnitIdWithPage(Long busId,int page,int pageSize)throws ServiceException;
	
	boolean removeBusUnitPdf(Long resId,Long busId) throws ServiceException;

	Long countBusPdfByBusId(Long busId) throws ServiceException;

	boolean wdaBackBusUnitById(Long busId, String backMsg)
			throws ServiceException;
	
	Long countProductByBusinessCertificationId(Long busCertId) throws ServiceException;
	
	BusinessUnit findByOrganization(Long organization) throws ServiceException;
	
	public boolean validateBusUnitOrgCode(String orgCode, Long orgId) throws ServiceException;

	public String findLicenseByName(String businessUnitName) throws ServiceException;

	public BusinessUnit findByName(String name) throws ServiceException;

	boolean updateSignStatus(String busName, boolean signFlag,boolean passFlag) throws ServiceException;

	boolean findSignFlagByName(String busName) throws ServiceException;

	public long countMarketByOrganization(Long organization, String configure)throws ServiceException;

	BusinessUnit save(BusinessUnit busUnit) throws ServiceException;
	
	void create(JSONObject testeeVO);

	/**
	 * 保存被检测人或生产企业
	 * @param bu
	 * @return Map
	 * @author LongXianZhen
	 */
	public Map<String, Object> saveBusinessUnit(BusinessUnitVO bu);

	
	public String getMarketNameByOrganization(Long organization) throws ServiceException;
	
	public Long unitCount() throws ServiceException;

	/**
	 * 根据组织结构获取企业id
     * @author ZhangHui 2015/4/10
     */
	public Long findIdByOrg(Long organization) throws ServiceException;

	/**
	 * 根据企业名称获取企业id
     * @author ZhangHui 2015/4/10
     */
	public Long findIdByName(String busunitName) throws ServiceException;
	
	/**
	 * 客户管理新增客户中的按关键字并分页自动加载客户名称
	 * @author HuangYog
	 * Create date 2015/04/13
	 */
	public Object getAllBusUnitName(Integer page,Integer pageSize, String keyword,String busType)throws ServiceException;

	/**
	 * 功能描述：保存生产企业信息<br>
	 * 作用于保存泊银等其他外部系统的生产企业数据。
	 * @author ZhangHui 2015/4/24
	 */
	public boolean saveBYProducer(ProductInstance sample, Long organization);

	/**
	 * 功能描述：根据组织机构id获取企业信息<br>
	 * 作用于判断某产品有没有被生产企业接管。
	 * @author ZhangHui 2015/5/1
	 */
	public LightBusUnitVO findBusVOByOrg(Long organization) throws ServiceException;

	/**
	 * 功能描述：根据企业名称获取轻量级企业信息
	 * @author ZhangHui 2015/5/14
	 */
	public LightBusUnitVO findVOByName(String name) throws ServiceException;

	/**
	 * 功能描述：获取当前企业的母企业、子企业、兄弟企业<br>
	 * 获取business TreeNode集合
	 * @author ZhangHui 2015/5/18
	 */
	public List<BusinessTreeNode> getRelativesOfTreeNodes(int level, String keyword, Long organization) throws ServiceException;


	/**
	 * 功能描述：修改企业信息保存为全部事务模式<br>
	 * 把原来在Reset控制层的保存企业信息修改，修改到Service层，保证事务完整性
	 * @author weihongyou 2015/05/18
	 */
	public BusinessUnit updateBusinessUnitAll(BusinessUnit businessUnit, AuthenticateInfo info) throws ServiceException;
	
	String findNameByOrganization(Long organization) throws ServiceException;

	/**
	 * 政府专员查看企业信息，根据企业地址过滤企业
	 * @param province 省
	 * @param city 市
	 * @param area 县（区域）
	 * @return List<EnterpriseRegiste>
	 * @author HY
	 */
	List<AccountBusinessVO> getAccountEnRegisteList(int page, int pageSize, String province, String city, String area,String nameOrLicNo,String btype)throws ServiceException;

	/**
	 * 政府专员查看企业信息，根据企业地址过滤企业 加载出的总数
	 * @return Long
	 * @author HY
	 */
	Long getAccountEnRegisteListTotal(String province, String city, String area,String nameOrLicNo,String btype)throws ServiceException;

	/**
	 * 根据企业id查看企业详情
	 * @param busId
	 * @return AccountBusinessVO
	 * @author HY
	 */
	AccountBusinessVO getAccountBusinessById(Long busId)throws ServiceException;

	/**
	 * 背景：报告录入页面
	 * 功能描述：保存生产企业信息
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/5
	 * @return 
	 */
	public BusinessUnitOfReportVO saveProducer(BusinessUnitOfReportVO bus_vo) throws ServiceException;

	/**
	 * 背景：流通企业产品新增/编辑页面
	 * 功能描述：保存产品所属的企业名称
	 * @param business_name  企业名称
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/3
	 */
	public BusinessUnit saveProducerName(String business_name) throws ServiceException;

	/**
	 * 功能描述：根据企业id查找企业组织机构
	 * @throws ServiceException
	 * @author ZhangHui 2015/7/1
	 */
	public Long findOrgById(Long id) throws ServiceException;

	/**
	 * 功能描述：根据企业id查找企业名称
	 * @throws ServiceException
	 * @author ZhangHui 2015/7/3
	 */
	public String findNameById(Long orig_fromBusId) throws ServiceException;
	
	/**
	 * 查找企业名称和对应的三证信息
	 * @param orgId
	 * @return
	 */
	public BusinessUnit findUnitNameSanZhengInfo(long orgId)throws ServiceException;

	/**
	 * 根据产品id查询该产品的所有生产企业
	 * @author longxianzhen 2015-08-06
	 */
	public List<BussinessUnitVOToPortal> getBuVOToPortalByProId(Long id)throws ServiceException;
	
	/**
	 * 根据用户信息查找商超或供应商的企业信息
	 * @author longxiaznhen 2015/08/07
	 */
	public BusinessUnit findSCBusinessByInfo(AuthenticateInfo info)throws ServiceException;

	/**
	 * 根据企业信息,添加营业执照
	 * @param id 企业ID
	 * @param url 营业执照图片地址
	 */
	public void updateBusinessUnit(String strImg,Long id, String url);

	public List<Resource> getByIdResource(Long id,String strImg);
    /**
     * 根据产品条形码获取所有的生产企业
     * @param barcode
     * @return
     */
	public List<BussinessUnitVOToPortal> getBuVOToPortalByBarcode(String barcode);
	
	public BusinessUnit getBusinessUnitByCondition(String businessName,String qsNo,String licenseNo);
	

}