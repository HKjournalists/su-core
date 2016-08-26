package com.gettec.fsnip.fsn.service.product.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.ProductNutriDAO;
import com.gettec.fsnip.fsn.dao.product.ProductToBusinessUnitDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.LicenceFormat;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.model.product.ProductToBusinessUnit;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.ProductionLicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.ProductTobusinessUnitService;
import com.gettec.fsnip.fsn.vo.business.QsNoAndFormatVo;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;

/**
 * Product service implementation
 * 
 * @author Ryan Wang
 */
@Service(value="productToBusinessUnitService")
public class ProductToBusinessUnitServiceImpl
		extends BaseServiceImpl<ProductToBusinessUnit, ProductToBusinessUnitDAO> 
		implements ProductTobusinessUnitService{

    @Autowired private ProductToBusinessUnitDAO productToBusinessUnitDAO;
	@Autowired private ProductNutriDAO productNutriDAO;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private ProductionLicenseService productionLicenseService;
	
	@Override
	public ProductToBusinessUnitDAO getDAO() {
		return productToBusinessUnitDAO;
	}
	
	/**
	 * 功能描述：新增/更新 企业-产品-qs号 关系
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/4
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(Long product_id, Long bus_id, boolean qs_bind, QsNoAndFormatVo qs_info) throws ServiceException {
		try {
			if(product_id==null || bus_id==null || qs_info==null){
				return;
			}
			
			Long qs_id = qs_info.getQsid();
			String qs_no = qs_info.getQsNo();
			
			if(qs_id==null && qs_no!=null && !"".equals(qs_no)){
				ProductionLicenseInfo orig_qs = productionLicenseService.findByQsno(qs_no);
				if(orig_qs == null){
					// 新增生产许可证
					ProductionLicenseInfo prolic = new ProductionLicenseInfo();
					prolic.setQsNo(qs_no);
					prolic.setQsnoFormat(new LicenceFormat(qs_info.getLicenceFormat()==null?1:qs_info.getLicenceFormat().getId()));
					productionLicenseService.createNewQsInfo(prolic);
					
					// 获取id
					qs_id = productionLicenseService.findByQsno(qs_no).getId();
				}else{
					qs_id = orig_qs.getId();
				}
				
				qs_info.setQsid(qs_id);
			}
			
			/* 2. 保存产品与生产许可证的对应关系 */
			ProductToBusinessUnit orig_pro2bus = getDAO().find(product_id, bus_id);
			if(orig_pro2bus == null){
				// 新增 企业-产品-qs号 关系
				ProductToBusinessUnit pro2bus = new ProductToBusinessUnit();
				pro2bus.setProduct_id(product_id);
				pro2bus.setBusiness_id(bus_id);
				pro2bus.setQs_id(qs_id);
				
				if(qs_id == null){
					qs_bind = false;
				}
				
				pro2bus.setBind(qs_bind?1:0);
				create(pro2bus);
			}else{
				// 当生产企业需要更新产品的qs号时，此时允许更新
				if(qs_bind){
					if(qs_id==null){
						if(orig_pro2bus.getBind()==1 && orig_pro2bus.getEffect()==1){
							orig_pro2bus.setBind(0);
							update(orig_pro2bus);
						}
					}else{
						orig_pro2bus.setQs_id(qs_id);
						orig_pro2bus.setBind(1);
						orig_pro2bus.setEffect(1);
						update(orig_pro2bus);
					}
				}
				
				// 当qs号并未被生产企业绑定或者绑定已失效，此时允许更新
				if(!qs_bind && (orig_pro2bus.getBind()==0 || orig_pro2bus.getEffect()==0)){
					if(qs_id != null){
						orig_pro2bus.setQs_id(qs_id);
						orig_pro2bus.setBind(0);
						orig_pro2bus.setEffect(1);
						update(orig_pro2bus);
					}
				}
			}
		} catch (DaoException e) {
			throw new ServiceException("[DaoException]ProductToBusinessUnitServiceImpl.save()-->" + e.getMessage(), e.getException());
		} catch (Exception e) {
			throw new ServiceException("[Exception]ProductToBusinessUnitServiceImpl.save()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：根据产品id和企业id查找一条  产品-企业-qs 关系
	 * @author ZhangHui 2015/6/9
	 * @throws ServiceException
	 */
	@Override
	public ProductToBusinessUnit find(Long product_id, Long bus_id) throws ServiceException{
		try {
			return getDAO().find(product_id, bus_id);
		} catch (DaoException e) {
			throw new ServiceException("ProductToBusinessUnitServiceImpl.find()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 背景：生产企业 报告录入界面，产品条形码下拉选择
	 * 功能描述：查找当前登录的生产企业的所有产品条形码
	 * @author ZhangHui 2015/6/5
	 */
	@Override
	public List<ProductOfMarketVO> getListBarcodeByOrganization(Long organization)
			throws ServiceException {
		try{
			return getDAO().getListBarcodeByOrganization(organization);
		}catch(DaoException e){
			throw new ServiceException("ProductToBusinessUnitService.getListBarcodeByOrganization()-->" + e.getMessage(),e.getException());
		}
	}
	
	/**
     * 根据商品ID得到ProductNutrition 的List
     * @param myRealOrgnizationId
     * @param productId
     * @param page
     * @param pageSize
     * @return List
     * @throws ServiceException
     */
	@Override
    public List<ProductNutrition> getListOfProductNutritionByProductIdWithPage( Long productId, int page, int pageSize)
            throws ServiceException {
	        try {
	            return productNutriDAO.getListOfProductNutritionByProductIdWithPage(productId, page, pageSize);
	        } catch (DaoException dex) {
	            throw new ServiceException("【service-error】按当前登录子企业的组织机构id查找产品分页列表，出现异常！", dex.getException());
	        }
    }

	/**
	 * 按产品id获取营养报告总条数
	 */
    @Override
    public long countByproductId(Long productId) throws ServiceException {
        try {
            return productNutriDAO.countByproductId(productId);
        } catch (DaoException dex) {
            throw new ServiceException("【service-error】按组织机构Id获取当前登录企业的商标总数，出现异常！", dex.getException());
        }
    }
    
	/**
	 * 功能描述：根据产品 barcode 查找 企业-产品-qs 集合
	 * @author ZhangHui 2015/6/5
	 * @throws DaoException
	 */
	@Override
	public List<BusinessUnitOfReportVO> getListByBarcode(String barcode) throws ServiceException {
		try{
			return getDAO().getListByBarcode(barcode);
		}catch(DaoException dex){
			throw new ServiceException("ProductToBusinessUnitServiceImpl.findByBarcode()-->" + dex.getMessage(),dex.getException());
		}
	}
	
	/**
	 * 背景：在报告录入界面
	 * 功能描述：根据产品条形码 和 生产许可证编码，查找一条生产企业信息
	 * @author ZhangHui 2015/6/4
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BusinessUnitOfReportVO getBusUnitOfReportVOByQsno(String barcode, String qsNo) throws ServiceException {
		try {
			return getDAO().getBusUnitOfReportVOByQsno(barcode, qsNo);
		} catch (DaoException e) {
			throw new ServiceException("BusinessUnitServiceImpl.getBusUnitOfReportVOByQsno()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 背景：在报告录入界面
	 * 功能描述：根据营业执照号，查找一条生产企业信息
	 * @author ZhangHui 2015/6/4
	 * @throws ServiceException 
	 */
	@Override
	public BusinessUnitOfReportVO getBusUnitOfReportVOLicenseNo(String licenseNo, String barcode) throws ServiceException {
		try{
			if(barcode==null || "".equals(barcode)){
				return getDAO().getBusUnitOfReportVOLicenseNo(licenseNo);
			}
			
			BusinessUnitOfReportVO vo = getDAO().getBusUnitOfReportVOLicenseNo(licenseNo, barcode);
			if(vo == null){
				return getDAO().getBusUnitOfReportVOLicenseNo(licenseNo);
			}
			
			return vo;
		}catch(DaoException e){
			e.printStackTrace();
			throw new ServiceException("BusinessUnitServiceImpl.getBusUnitOfReportVOLicenseNo()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 背景：在报告录入界面
	 * 功能描述：根据生产企业名称和产品条形码，查找一条生产企业信息（包括生产许可证信息）
	 * @author ZhangHui 2015/6/4
	 * @throws ServiceException 
	 */
	@Override
	public BusinessUnitOfReportVO getBusUnitOfReportVOByBusname(String bus_name, String barcode) throws ServiceException {
		try {
			if(barcode==null || "".equals(barcode)){
				return getDAO().getBusUnitOfReportVOByBusname(bus_name);
			}
			
			BusinessUnitOfReportVO vo =  getDAO().getBusUnitOfReportVOByBusname(bus_name, barcode);
			if(vo == null){
				return getDAO().getBusUnitOfReportVOByBusname(bus_name);
			}
			
			return vo;
		} catch (DaoException e) {
			throw new ServiceException("BusinessUnitServiceImpl.getBusUnitOfReportVOByBusname()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
     * 加载该企业可以绑定的所有qs号
     * @param organization
     * @param firstpart
     * @return List<Object>
     * @author HuangYog
     */
	@Override
    public List<String> loadSonqsno(Long parentOrganization,Long myOrganization,
            String firstpart,Long formatId) throws ServiceException {
	    try{
	    	/* 1. 获取总公司企业id */
	    	Long businessUnitId  = businessUnitService.getDAO().getIdByOrganization(parentOrganization);
	    	/* 2. 获取总企业的前半部分为‘firstpart’的所有qs号（包括旗下所有的子企业QS号） */
	        List<String> countQsList = businessUnitService.getDAO().getSonQsList4BussUnitId(businessUnitId,firstpart,formatId);
	    	/* 3. 获取当前登录子公司之外，已经被其他子公司绑定过的且前半部分是‘firstpart’ 的 qs号  */
	        List<String> otherSonBussUnitBandQsNO = businessUnitService.getDAO().getSonBussUnitIdByParentOrganizationId(parentOrganization,myOrganization,firstpart,formatId);
	        /* 4. 获取被总企业绑定过的且前半部分是‘firstpart’ 的qs号（不包括子企业） */
	        if(!parentOrganization.equals(myOrganization)){
	        	List<String> parentBusQsList = businessUnitService.getDAO().getproToBusQsListByBusId(businessUnitId,firstpart,formatId);
	        	countQsList.removeAll(parentBusQsList);
	        }
	        /* 5. 获取当前登录企业可以使用的qs号 */
	        countQsList.removeAll(otherSonBussUnitBandQsNO);
	        /* 6. 返回 */
	        return countQsList;
	    }catch(DaoException dex){
            throw new ServiceException("ProductToBusinessUnitServiceImpl.findByBarcodeAndBusunitId()-->" + dex.getMessage(),dex.getException());
        }
    }

	/**
	 * 根据产品ID查询所有ProductToBusinessUnit
	 * @author LongXianZhen 2015/05/13
	 */
	@Override
	public List<ProductToBusinessUnit> getByProductId(Long proId)
			throws ServiceException {
		try {
			String condition = " where e.product.id = ?1 ";
			return getDAO().getListByCondition(condition, new Object[]{proId});
		} catch (JPAException jpae) {
			throw new ServiceException("ProductToBusinessUnitServiceImpl.getByProductId()-->" + jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 功能描述：根据产品id和企业组织机构，查找qs号
	 * @author ZhangHui 2015/6/5
	 * @throws ServiceException 
	 */
	@Override
	public Long getQsId(Long product_id, Long organization) throws ServiceException {
		try {
			return getDAO().getQsId(product_id, organization);
		} catch (DaoException e) {
			throw new ServiceException("ProductToBusinessUnitServiceImpl.getQsno()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 功能描述：将企业-产品-qs号 设置为有效/无效
	 * @param effect
	 *          0 代表设置为无效
	 *          1 代表设置为有效
	 * @author Zhanghui 2015/6/15
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateByEffect(long qs_id, long business_id, int effect) throws ServiceException {
		try {
			getDAO().updateByEffect(qs_id, business_id, effect);
		} catch (DaoException e) {
			throw new ServiceException("ProductToBusinessUnitServiceImpl.updateByEffect()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 功能描述：除了当前qs号的主企业，将其他被授权企业-产品-qs号 设置为有效/无效
	 * @param effect
	 *          0 代表设置为无效
	 *          1 代表设置为有效
	 * @author Zhanghui 2015/6/15
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateExOwnerbusByEffect(Long qs_id, Long owner_bus_id, int effect) throws ServiceException {
		try {
			getDAO().updateExOwnerbusByEffect(qs_id, owner_bus_id, effect);
		} catch (DaoException e) {
			throw new ServiceException("ProductToBusinessUnitServiceImpl.updateExOwnerbusByEffect()-->" + e.getMessage(), e.getException());
		}
	}
}