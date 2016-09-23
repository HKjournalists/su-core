package com.lhfs.fsn.dao.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote;
import com.lhfs.fsn.vo.PlusInfoVO;
import com.lhfs.fsn.vo.ResourceVO;
import com.lhfs.fsn.vo.business.BusinessAndPros2PortalVO;
import com.lhfs.fsn.vo.business.BusinessResultVO;
import com.lhfs.fsn.vo.business.BusinessVOWda;
import com.lhfs.fsn.vo.product.ProductInfoVO;
import com.lhfs.fsn.vo.product.ProductInfosVO;
/**
 * 
 * @author Kxo
 */
public interface BusinessUnitDao extends BaseDAO<BusinessUnit>{

	BusinessUnit findByName(String name);

	List<Long> getListOfHotBusinessUnitWithPage(int page, int pageSize,
			Long busId,String busIds) throws DaoException;

	long countOfHotBusinessUnit() throws DaoException;

	   /**
     * 产品溯源
     * @param barcode
     * @param batch
     * @return List<Object>
     */
    List<ReceivingNote> getProductTraceabilityList(String barcode, String batch) throws DaoException;
    public BusinessUnit findByOrgnizationId(Long orgnizationId) throws DaoException;

    /**
     * 根据模糊的企业名称查询出匹配的企业名称
     * @param name 模糊的企业名称
     * @return List<String>
     * @throws DaoException
     */
    List<String> loadBusinessUnitListForName(String name) throws DaoException;
    /**
     * 根据模糊的企业名称查询出匹配的企业名称 分页显示
     * @param name
     * @param page
     * @param pageSize
     * @return List<String>
     * @throws DaoException
     */
    List<String> loadBusinessUnitListForName(String name,int page,int pageSize) throws DaoException;
    Object loadBusinessUnitListForNameCount(String name)throws DaoException;

    /**
     * 根据模糊的企业营业执照号查询企业的企业营业执照号
     * @param licenseNo 模糊的企业营业执照号
     * @return List<String> 
     * @throws DaoException
     */
    List<String> loadBusinessUnitListForlicenseNo(String licenseNo)throws DaoException;

	List<BusinessVOWda> getListOfMarketByMarketIdWithPage(Long busId, int page,
			int pageSize) throws DaoException;

	List<ProductInfoVO> getBusinessUnitListByName(int page, int pageSize, String name, String licenseNo) throws DaoException;

	List<BusinessVOWda> getListEnterpriseByTypeWithPage(String type, int page,int pageSize,String name,String organization) throws DaoException;

	long countByType(String type,String name,String organization) throws DaoException;

	Long getTransactionNum(String re_num, Long organization, String batch,
			String barcode)throws DaoException;
	
	//加载相关企业类型下的企业和产品，按报告数量多到少排序   @author HuangYog
	public List<Object[]> loadBusinessUnit(String type, int page,
			int pagesize,String keyword)throws DaoException;
	//加载相关企业类型下的企业和产品，按报告数量多到少排序 的企业总数     @author HuangYog
	public Long loadBusinessUnitCount(String type,String keyword)throws DaoException;

	List<BusinessUnit> getUnitByKeyword(String keyword, int startindex,
			int pagesize);

	int getCount(String keyword);

	List<BusinessAndPros2PortalVO> getBusinessByBuIds(String buIds)throws DaoException;
	/**
	 * 根据企业组织机构获取企业基本信息,和资质信息
	 * @author wb 
	 * @ date : 2016.5.9
	 * @param organization
	 * @return
	 */
	BusinessResultVO getOrganizationBussinessInfo(Long organization);
	/**
	 * 根据企业组织机构获取企业生产许可证基本信息
	 * @author wb 
	 * @ date : 2016.5.9
	 * @param organization
	 * @return
	 */
	List<ResourceVO> getOrganiaztionQSImg(Long organization);

	List<ProductInfosVO> getLikeProductInfosVO(String organization,
			String proName, String proBarcode);
	
	PlusInfoVO getPlusInfoData(String productBarcode, String startDate,
			String endDate);
	/**
	 * 根据条形码,在条件时间查询范围内获取相关的上传的报告企业自检数量
	 * @author wb     2016.5.11
	 * @param productBarcode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String getSelfNum(String productBarcode, String startDate, String endDate);
	/**
	 * 根据条形码,在条件时间查询范围内获取相关的报告操作活跃度总数
	 * @author wb     2016.5.11
	 * @param productBarcode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String getActiveDegree(String productBarcode, String startDate,
			String endDate);
	/**
	 * 根据条形码,在条件时间查询范围内获取相关的企业送检数量
	 * @author wb     2016.5.11
	 * @param productBarcode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int getInspectionFrequency(String productBarcode, String startDate,
			String endDate);



	

	
}
