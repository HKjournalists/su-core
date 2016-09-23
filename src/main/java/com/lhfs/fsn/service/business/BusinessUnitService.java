package com.lhfs.fsn.service.business;

import java.util.List;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.business.BusinessUnitLIMSVO;
import com.lhfs.fsn.dao.business.BusinessUnitDao;
import com.lhfs.fsn.vo.PlusInfoVO;
import com.lhfs.fsn.vo.ScoreInfoVO;
import com.lhfs.fsn.vo.TraceabilityVO;
import com.lhfs.fsn.vo.business.Business2PortalVO;
import com.lhfs.fsn.vo.business.BusinessAndProductVO;
import com.lhfs.fsn.vo.business.BusinessAndPros2PortalVO;
import com.lhfs.fsn.vo.business.BusinessResultVO;
import com.lhfs.fsn.vo.business.BusinessVOWda;
import com.lhfs.fsn.vo.product.Product2EnterpriseColumnVO;
import com.lhfs.fsn.vo.product.ProductInfoVO;
import com.lhfs.fsn.vo.product.ProductInfosVO;
import com.lhfs.fsn.vo.product.ProductSimpleVO;
import com.lhfs.fsn.vo.product.SajTraceabilityVO;
import com.lhfs.fsn.web.controller.RESTResult;

public interface BusinessUnitService extends BaseService<BusinessUnit, BusinessUnitDao>{
	List<Long> getListOfHotBusinessUnitWithPage(int page, int pageSize,
			Long busId, String busIds) throws ServiceException;

	long countOfHotBusinessUnit() throws ServiceException;

    /**
     * 根据企业名称查找 主营商品
     * @param name
     * @return List<ProductSimpleVO>
     * @throws ServiceException
     */
    List<ProductSimpleVO> loadProductInfoByName(String name)throws ServiceException;
	
	ResultVO saveLimsBusUnitInfo(BusinessUnitLIMSVO busUnitLimsVO) throws ServiceException;
	
	//监管系统详细溯源
	public List<List<TraceabilityVO>> productDetailTraceability(String barcode ,String batch )throws ServiceException;

	/**
	 * 根据模糊的企业名称查询企业的名字
	 * @param name 模糊的名称
	 * @return List<String>
	 * @throws ServiceException
	 */
    List<String> loadBusinessUnitListForName(String name) throws ServiceException;
    /**
     * 根据模糊的企业名称查询企业的名字  分页显示
     * @param name 模糊的名称
     * @return List<String>
     * @throws ServiceException
     */
    List<String> loadBusinessUnitListForName(String name,int page,int pageSize) throws ServiceException;
    //根据模糊的企业名称查询企业的名字 总条数
    Object loadBusinessUnitListForNameCount(String name)throws ServiceException;

    /**
     * 根据模糊的企业营业执照号查询企业的企业营业执照号
     * @param licenseNo
     * @return List<String>
     * @throws ServiceException
     */
    List<String> loadBusinessUnitListForlicenseNo(String licenseNo)throws ServiceException;

	List<BusinessVOWda> getListOfMarketByMarketIdWithPage(Long busId, int page,
			int pageSize) throws ServiceException;
	
	
	List<ProductInfoVO> getBusinessUnitListByName(int page, int pageSize, String name, String licenseNo) throws ServiceException;

	List<ReceivingNote> getProductTraceabilityList(String barcode, String batch) throws ServiceException;
	List<BusinessVOWda> getListEnterpriseByTypeWithPage(String type, int page,
			int pageSize,String name,String organization) throws ServiceException;
	
	List<ReceivingNote>getReceivingNoteByBatchAndBarcode(String batch,String barcode)throws ServiceException;

	long countByType(String type,String name,String organization) throws ServiceException;

	BusinessUnit findBusinessByOrg(Long organization)throws ServiceException;

	Long getTransactionNum(String re_num, Long organization, String batch,
			String barcode)throws ServiceException;
	
	/**
	 * 加载相关企业类型下的企业和产品，按报告数量多到少排序
	 * @param type 企业类型
	 * @param page
	 * @param pagesize
	 * @return List<Business2PortalVO>
	 * @throws ServiceException
	 */
	public List<Business2PortalVO> loadBusinessUnit(String type, int page, int pagesize,String keyword)throws ServiceException;

	/**
	 * 加载相关企业类型下的企业和产品，按报告数量多到少排序 的企业总数
	 * @param type 企业类型 
	 * @return Long
	 * @throws ServiceException
	 */
	public Long loadBusinessUnitCount(String type,String keyword)throws ServiceException;

	/**
	 * 根据企业id查找企业下面的产品
	 * @param id
	 * @return List<ProductSimpleVO>
	 * @throws ServiceException
	 */
	public List<ProductSimpleVO> loadProductInfoByOrganization(Long productCount,Long organization)throws ServiceException;

	/**
	 * 为食安监提供的溯源接口
	 * @param barcode 产品条码
	 * @param batch 相关批次
	 * @return List<List<SajTraceabilityVO>>
	 * @throws ServiceException
	 * @author hy
	 */
	List<List<SajTraceabilityVO>> sajTraceability(String barcode, String batch)throws ServiceException;

	RESTResult<BusinessUnit> uploadSearch2(String keyword, Integer sn);

	List<Product2EnterpriseColumnVO> getProductInfoByOrganization(Long long1,
			Long organization)throws ServiceException;

	List<BusinessAndPros2PortalVO> getBusinessAndProductByBuPros(
			List<BusinessAndProductVO> buPros)throws ServiceException;

	/**
	 * 描述:根据企业组织机构获取企业基本信息,和资质信息
	 * @author wb 
	 * @ date : 2016.5.9
	 * @param organization
	 * @return
	 */
	BusinessResultVO getOrganizationBussinessData(Long organization);

	/**
	 * 描述 ：根据当前机构，产品名称，产品条形码，获取信息
	 * @author wb 
	 * @date 2016.5.9
	 * @param organization
	 * @param product_name
	 * @param product_barcode
	 * @return
	 */
	List<ProductInfosVO> getLikeProductInfosVO(String organization,
			String proName, String proBarcode);

	/**
	 * 获取评分指标数据(score_info数据)
	 * @param organization
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	ScoreInfoVO getScoreInfoData(String product_barcode, String start_date,
			String end_date);

	/**
	 * 获取评分指标数据(plus_info数据)
	 * @param organization
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	PlusInfoVO getPlusInfoData(String productBarcode, String startDate,
			String endDate);
}
