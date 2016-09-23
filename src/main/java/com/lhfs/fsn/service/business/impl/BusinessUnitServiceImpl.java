package com.lhfs.fsn.service.business.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.test.TestResultDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.CirculationPermitInfo;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.business.BusinessUnitToLimsService;
import com.gettec.fsnip.fsn.service.business.CirculationPermitService;
import com.gettec.fsnip.fsn.service.business.EnterpriseRegisteService;
import com.gettec.fsnip.fsn.service.business.LicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.ProductStdCertificationService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.business.BusinessUnitLIMSVO;
import com.lhfs.fsn.dao.business.BusinessUnitDao;
import com.lhfs.fsn.dao.product.ProductDao;
import com.lhfs.fsn.service.business.BusinessUnitService;
import com.lhfs.fsn.service.testReport.TestReportService;
import com.lhfs.fsn.util.PageUtil;
import com.lhfs.fsn.util.StringUtil;
import com.lhfs.fsn.vo.PlusInfoVO;
import com.lhfs.fsn.vo.ResourceVO;
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
import com.lhfs.fsn.vo.product.ProductSortVo;
import com.lhfs.fsn.vo.product.SajTraceabilityVO;
import com.lhfs.fsn.web.controller.RESTResult;

/**
 * 
 * @author Kxo
 */
@Service(value="businessUnitLFService")
@Transactional
public class BusinessUnitServiceImpl extends BaseServiceImpl<BusinessUnit, BusinessUnitDao> 
		implements BusinessUnitService{
	@Autowired private BusinessUnitDao businessUnitLFDao;
	@Autowired private EnterpriseRegisteService enterpriseService;
	@Autowired private ProductDao productDao;
	@Autowired private BusinessUnitToLimsService businessUnitToLimsService;
	@Autowired private LicenseService licenseService;
	@Autowired private CirculationPermitService circulationPermitService;
	@Autowired private TestReportService reportService;
	@Autowired private ProductStdCertificationService productStdCertificationService;
	@Autowired private ResourceService resourceService;
	@Autowired protected TestResultDAO testResultDAO;
	
	@Override
	public BusinessUnitDao getDAO() {
		return businessUnitLFDao;
	}

	/**
     * 获取热点企业，按报告数量排序
     * @param page
     * @param pageSize
     * @return
	 * @throws ServiceException 
     */
	@Override
	public List<Long> getListOfHotBusinessUnitWithPage(int page, int pageSize,
			Long busId, String busIds) throws ServiceException {
		try {
			List<Long> listOfHotBusUnit = getDAO().getListOfHotBusinessUnitWithPage(page, pageSize, busId, busIds);
			/*if(listOfHotBusUnit!=null && listOfHotBusUnit.size()>0){
				for(BusinessUnitVO2 busunit : listOfHotBusUnit){
					busunit.setLogo(enterpriseService.findLogoByName(busunit.getName()));
				}
			}*/
			return listOfHotBusUnit;
		} catch (DaoException dex) {
			throw new ServiceException("【service-error】 获取热门企业，出现异常！", dex);
		}
	}

	/**
     * 获取热门企业总数
	 * @throws ServiceException 
     */
	@Override
	public long countOfHotBusinessUnit() throws ServiceException {
		try {
			return getDAO().countOfHotBusinessUnit();
		} catch (DaoException dex) {
			throw new ServiceException("【service-error】 获取热门企业总数，出现异常！", dex.getException());
		}
	}

	/**
	 * 根据企业名称查找 主营商品
	 * @param name
	 * @return List<ProductInfoVO>
	 * @throws ServiceException
	 */
	@Override
    public List<ProductSimpleVO> loadProductInfoByName(String name) throws ServiceException {
        List<ProductSimpleVO>  infoVOList = new ArrayList<ProductSimpleVO>();
		try {
			BusinessUnit businessUnit = getDAO().findByName(name);
			if(businessUnit != null){
					List<Product> list = productDao.getProductByOrg(businessUnit.getOrganization());
					for(Product product : list){
						if (product != null) {
						    ProductSimpleVO productInfoVO = new ProductSimpleVO();
                            productInfoVO.setId(product.getId()); 
                            productInfoVO.setName(product.getName());
                            productInfoVO.setBarcode(product.getBarcode());
                            infoVOList.add(productInfoVO);
		                }
					}
			}
		} catch (DaoException daoe) {
		    throw new  ServiceException("BusinessUnitServiceImpl.loadProductInfoByName()-->" + daoe.getMessage(), daoe.getException());
		}
		return infoVOList;
	}

	/**
	 * 保存来自lims的企业信息
	 * @param BusinessUnitLIMSVO
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultVO saveLimsBusUnitInfo(BusinessUnitLIMSVO busUnitLimsVO)
			throws ServiceException {
		ResultVO resultVO = new ResultVO();
		try{
			if(busUnitLimsVO==null){
				resultVO.setSuccess(false);
				resultVO.setErrorMessage("无效的存储对象！");
				return resultVO;
			}
			BusinessUnit orgBusUnit = getDAO().findByName(busUnitLimsVO.getName());
			if(orgBusUnit!=null&&orgBusUnit.getBusinessUnitToLims()==null){
				resultVO.setSuccess(false);
				resultVO.setErrorMessage("保存失败，该企业是企业门户所属企业，此处不允许修改。");
				return resultVO;
			}
			if(orgBusUnit!=null&&!busUnitLimsVO.getEdition().equals(orgBusUnit.getBusinessUnitToLims().getEdition())){
				resultVO.setSuccess(false);
				resultVO.setErrorMessage("保存失败，该企业已经存在，并且不是当前LIMS用户所创建，不允许修改。");
				return resultVO;
			}
			orgBusUnit = busUnitLimsVO.transformEntity(orgBusUnit);
			/*1.处理营业执照信息*/
			if(busUnitLimsVO.getLicenseNo()!=null){
				LicenseInfo orig_licen = licenseService.getDAO().findById(busUnitLimsVO.getLicenseNo());
				if(orig_licen==null){
					orig_licen = new LicenseInfo();
					orig_licen.setLicenseNo(busUnitLimsVO.getLicenseNo());
					orgBusUnit.setLicense(orig_licen);
					licenseService.save(orgBusUnit.getLicense());
				}else{
					orgBusUnit.setLicense(orig_licen);
				}
			}
			/*2.处理流通许可证*/
			if(busUnitLimsVO.getDistributionNo()!=null){
				CirculationPermitInfo orig_cpInfo = circulationPermitService.findByDistributionNo(busUnitLimsVO.getDistributionNo());
				if(orig_cpInfo==null){
					orig_cpInfo = new CirculationPermitInfo();
					orig_cpInfo.setDistributionNo(busUnitLimsVO.getDistributionNo());
					orgBusUnit.setDistribution(orig_cpInfo);
					circulationPermitService.save(orgBusUnit.getDistribution());
				}else{
					orgBusUnit.setDistribution(orig_cpInfo);
				}
			}
			/*3.处理扩展信息*/
			businessUnitToLimsService.save(orgBusUnit.getBusinessUnitToLims());
			/*4.处理企业基本信息*/
			if(orgBusUnit.getId()==null){
				create(orgBusUnit);
			}else{
				update(orgBusUnit);
			}
			resultVO.setSuccess(true);
		}catch(Exception e){
			throw new ServiceException("BusinessUnitServiceImpl.saveLimsBusUnitInfo() "+e.getMessage(),e);
		}
		return resultVO;
	}
	
	/**
	 * 监管系统 查询产品详细溯源
	 * @param barcode
	 * @param batch
	 * @author YongHuang
	 */
    @Override
	public List<List<TraceabilityVO>> productDetailTraceability(String barcode,String batch) throws ServiceException {
    	try {
        	List<ReceivingNote> list = getDAO().getProductTraceabilityList(barcode ,batch);
            List<List<TraceabilityVO>> traceability = new ArrayList<List<TraceabilityVO>>();
            for (int i = 0; i < list.size(); i++) {
                List<TraceabilityVO> businessUnitList = new ArrayList<TraceabilityVO>();
                List<TraceabilityVO> newList = new ArrayList<TraceabilityVO>();
                boolean count = false;
                for (int j = 0; j < list.size(); j++) {
                    count = false;
                    if (i != j && list.get(i).getOrganization().equals(list.get(j).getRe_provide_num().getOrganization())) {
                        break;
                    }
                    count = true;
                }
                if (count ) {
                    BusinessUnit receiveBus = getDAO().findByOrgnizationId(list.get(i).getOrganization());//收货企业
                    BusinessUnit sourceBus = list.get(i).getRe_provide_num();//来源企业
                    TraceabilityVO tVO = new TraceabilityVO();
                    tVO.setResourceBusName(sourceBus == null?"":sourceBus.getName());
                    tVO.setDirectionBusName(receiveBus == null?"":receiveBus.getName());
                    tVO.setReceiveDate(list.get(i).getRe_date());//交易时间（收货时间）
                    
	        		Long total = getTransactionNum(list.get(i).getRe_num(),list.get(i).getOrganization(),batch,barcode);//.获取交易数量
	        		tVO.setReceiveNum(total);
                    newList.add(tVO);
                    List<TraceabilityVO> bussUnitList = loopDetailCompare(list,list.get(i).getRe_provide_num().getOrganization() , i,barcode,batch);
                    businessUnitList.addAll(newList);
                    businessUnitList.addAll(bussUnitList);
                    traceability.add(businessUnitList);
                }
            }
            return traceability;
        } catch (DaoException dex) {
        	throw new  ServiceException("BusinessUnitServiceImpl.productDetailTraceability()-->" + dex.getMessage(), dex.getException());
        }
	}
    
    /**
     * 循环遍历查询企业信息
     * @param list
     * @param providerOrgId
     * @param j
     * @param barcode
     * @param batch
     * @return
     * @throws ServiceException
     * @author YongHuang
     */
    public List<TraceabilityVO> loopDetailCompare(List<ReceivingNote> list,Long providerOrgId ,int j,String barcode,String batch) throws ServiceException {
        List<TraceabilityVO> listUnit = new ArrayList<TraceabilityVO>();
        try {
            for (int i = 0; i < list.size(); i++) {
                if (i != j && providerOrgId.equals(list.get(i).getOrganization())) {
                    List<TraceabilityVO> listUnits = new ArrayList<TraceabilityVO>();
                    BusinessUnit receiveBus = getDAO().findByOrgnizationId(list.get(i).getOrganization());
                    BusinessUnit sourceBus = list.get(i).getRe_provide_num();
                    TraceabilityVO tVO = new TraceabilityVO();
                    tVO.setResourceBusName(sourceBus == null?"":sourceBus.getName());
                    tVO.setDirectionBusName(receiveBus == null?"":receiveBus.getName());
                    tVO.setReceiveDate(list.get(i).getRe_date());//交易时间（收货时间）
                    
	        		Long total = getTransactionNum(list.get(i).getRe_num(),list.get(i).getOrganization(),batch,barcode);//.获取交易数量
	        		tVO.setReceiveNum(total);
	        		listUnits.add(tVO);
                    List<TraceabilityVO> bussUnitList = loopDetailCompare(list,list.get(i).getRe_provide_num().getOrganization(),i,barcode,batch);
                    listUnit.addAll(listUnits);
                    listUnit.addAll(bussUnitList);
                }
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return listUnit;
    }

    /**
     * 根据企业名字动态加载企业名称，模糊查询
     * @param name
     * @author YongHuang
     */
    @Override
    public List<String> loadBusinessUnitListForName(String name) throws ServiceException {
        try {
            return getDAO().loadBusinessUnitListForName(name);
        } catch (DaoException dex) {
            throw new  ServiceException("BusinessUnitServiceImpl.loadBusinessUnitListForName()-->" + dex.getMessage(), dex.getException());
        }
    }
    
    /**
     * 分页显示
     * @param name
     * @param page
     * @param pageSize
     */
    @Override
    public List<String> loadBusinessUnitListForName(String name,int page,int pageSize) 
    		throws ServiceException {
        try {
            return getDAO().loadBusinessUnitListForName(name,page,pageSize);
        } catch (DaoException dex) {
            throw new  ServiceException("BusinessUnitServiceImpl.loadBusinessUnitListForName(String name,int page,int pageSize)-->" + dex.getMessage(), dex.getException());
        }
    }
    
    @Override
    public Object loadBusinessUnitListForNameCount(String name)throws ServiceException {
        try {
            return getDAO().loadBusinessUnitListForNameCount(name);
        } catch (DaoException dex) {
            throw new  ServiceException("BusinessUnitServiceImpl.loadBusinessUnitListForNameCount()-->" + dex.getMessage(), dex.getException());
        }
    }

    /**
     * 根据模糊的企业营业执照号查询企业的企业营业执照号
     * @param licenseNo
     * @return List<String>
     */
    @Override
    public List<String> loadBusinessUnitListForlicenseNo(String licenseNo) throws ServiceException {
        try {
            return getDAO().loadBusinessUnitListForlicenseNo(licenseNo);
        } catch (DaoException dex) {
            throw new  ServiceException("BusinessUnitServiceImpl.loadBusinessUnitListForlicenseNo()-->" + dex.getMessage(), dex.getException());
        }
    }

    /**
	 * 监管系统获取来源于超市没有认证的企业信息
	 */
	@Override
	public List<BusinessVOWda> getListOfMarketByMarketIdWithPage(Long busId,
			int page, int pageSize) throws ServiceException {
		try{
			return getDAO().getListOfMarketByMarketIdWithPage(busId, page, pageSize);
		}catch(DaoException daoe){
			throw new ServiceException("BusinessUnitServiceImpl.getListOfMarketByMarketIdWithPage() "+daoe.getMessage(),daoe.getException());
		}
	}

	@Override
	public List<ProductInfoVO> getBusinessUnitListByName(int page,int pageSize,String name,
			String licenseNo) throws ServiceException {
		try{
			return getDAO().getBusinessUnitListByName(page,pageSize,name,licenseNo);
		}catch(DaoException jpae){
			throw new ServiceException("BusinessUnitServiceImpl.getBusinessUnitListByName() "+jpae.getMessage(),jpae.getException());
		}
	}

	@Override
	public List<ReceivingNote> getProductTraceabilityList(String barcode,
			String batch) throws ServiceException {
		try{
			return getDAO().getProductTraceabilityList(barcode, batch);
		}catch(Exception jpae){
			throw new ServiceException("BusinessUnitServiceImpl.findByOrgnizationId() "+jpae.getMessage(),jpae.getMessage());
		}
	}
	
	/**
	 * 统计指定类型的企业数量
	 * @param type
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public long countByType(String type,String name,String organization) throws ServiceException{
		try{
			return getDAO().countByType(type,name,organization);
		}catch(DaoException daoe){
			throw new ServiceException("BusinessUnitServiceImpl.countByType() "+daoe.getMessage(),daoe);
		}
	}
	
	/**
	 * 查询指定类型的企业类别
	 */
	@Override
	public List<BusinessVOWda> getListEnterpriseByTypeWithPage(String type,int page,
			int pageSize,String name,String organization) throws ServiceException {
		try{
			return getDAO().getListEnterpriseByTypeWithPage(type, page, pageSize,name,organization);
		}catch(DaoException daoe){
			throw new ServiceException("BusinessUnitServiceImpl.getListEnterpriseByTypeWithPage() "+daoe.getMessage(),daoe.getException());
		}
	}
	
	/**
     * 根据批次和条形码获取收货单集合
     * @param batch
     * @param barcode
     * @return List<ReceivingNote>
     */
    @Override
    public List<ReceivingNote> getReceivingNoteByBatchAndBarcode(String batch,String barcode)throws ServiceException {
		try{
			return getDAO().getProductTraceabilityList(barcode ,batch);
		}catch(DaoException daoe){
			throw new ServiceException("BusinessUnitServiceImpl.getReceivingNoteByBatchAndBarcode() "+daoe.getMessage(),daoe.getException());
		}
    }
    /**
     * 根据organization查询企业
     * @param organization
     * @return BusinessUnit
     */
    @Override
    public BusinessUnit findBusinessByOrg(Long organization)throws ServiceException{
    	try{
    		return getDAO().findByOrgnizationId(organization);
    	}catch(DaoException daoe){
    		throw new ServiceException("BusinessUnitServiceImpl.findBusinessByOrg() "+daoe.getMessage(),daoe.getException());
    	}
    }
	/**
	 * 根据交易单号、组织结构ID、批次、条形码获取交易总数量
	 * @param re_num
	 * @param organization
	 * @param batch
	 * @param barcode
	 * @return 交易总数量
	 */
	@Override
	public Long getTransactionNum(String re_num, Long organization,
			String batch, String barcode) throws ServiceException {
		try{
			return getDAO().getTransactionNum(re_num,organization,batch,barcode);
		}catch(DaoException daoe){
    		throw new ServiceException("BusinessUnitServiceImpl.getTransactionNum() "+daoe.getMessage(),daoe.getException());
    	}
	}
	
	/**
	 * 加载相关企业类型下的企业和产品，按报告数量多到少排序
	 * @param type 企业类型
	 * @param page
	 * @param pagesize
	 * @return List<BussinessUnitVO>
	 */
	@Override
	public List<Business2PortalVO> loadBusinessUnit(String type, int page,
			int pagesize,String keyword) throws ServiceException {
		try{
			List<Object[]> busu= getDAO().loadBusinessUnit(type,page,pagesize,keyword);
			List<Business2PortalVO> list = new ArrayList<Business2PortalVO>();
			for(int i = 0 ; i < busu.size() ; i++){
			    Object[] obj = (Object[]) busu.get(i);
				Business2PortalVO business2PortalVO = new Business2PortalVO();
				business2PortalVO.setId(Long.parseLong(obj[0]!=null?obj[0].toString():"-1L"));
				business2PortalVO.setAbout(obj[1]!=null?obj[1].toString():"");
				business2PortalVO.setName(obj[2]!=null?obj[2].toString():"");
				business2PortalVO.setWebsite(obj[3]!=null?obj[3].toString():"");
				business2PortalVO.setType(obj[4]!=null?obj[4].toString():"");
				business2PortalVO.setOrganization(Long.parseLong(obj[5]!=null?obj[5].toString():"-1L"));
				EnterpriseRegiste orig_enterprise = enterpriseService.findbyEnterpriteName(business2PortalVO.getName());
				Set<Resource> logoUrls = orig_enterprise!=null?orig_enterprise.getLogoAttachments():null;
				business2PortalVO.setLogo(logoUrls!=null&&logoUrls.size()>0?(logoUrls.iterator().next().getUrl()):"");
				Integer organization = Integer.valueOf(obj[5] !=null ? obj[5].toString() : "-1");
				int countOfProduct =testResultDAO.productCountByOrg(organization);
				Long countOfReport =testResultDAO.getRepoCountByOrganizationId(Integer.valueOf(organization).longValue());
				business2PortalVO.setCountOfProduct(countOfProduct);
				business2PortalVO.setCountOfReport(countOfReport!=null?countOfReport.intValue():0);
				list.add(business2PortalVO);
			}
			return list;
		}catch(DaoException daoe){
			throw new ServiceException("BusinessUnitServiceImpl.loadBusinessUnit() " + daoe.getMessage(), daoe);
		}
	}

	/**
	 * 加载相关企业类型下的企业和产品，按报告数量多到少排序 的企业总数
	 * @param type 企业类型 
	 * @return Long
	 */
	@Override
	public Long loadBusinessUnitCount(String type,String keyword) throws ServiceException {
		try{
			return getDAO().loadBusinessUnitCount(type,keyword);
		}catch(DaoException daoe){
			throw new ServiceException("BusinessUnitServiceImpl.loadBusinessUnitCount() " + daoe.getMessage(), daoe);
		}
	}

	/**
	 * 根据企业组织机构查找企业下面的产品
	 * @param organization
	 * @return List<ProductSimpleVO>
	 */
	@Override
	public List<ProductSimpleVO> loadProductInfoByOrganization(Long productCount,Long organization)
			throws ServiceException {
		try {
			List<Product> list = productDao.getReportMaxProductByOrganization(productCount,organization);
			List<ProductSimpleVO>  infoVOList = new ArrayList<ProductSimpleVO>();
			for(Product product : list){
				if (product != null) {
					ProductSimpleVO productInfoVO = new ProductSimpleVO();
					productInfoVO.setId(product.getId()); 
					productInfoVO.setName(product.getName());
					//productInfoVO.setBarcode(product.getBarcode());
					productInfoVO.setProductCertification(productStdCertificationService.getListOfStandCertificationByProductId(product.getId()));
					productInfoVO.setReportCount(reportService.getReportCountForProductId(product.getId()));
					 /*处理产品图片*/
			        List<Resource> imgList = resourceService.getProductImgListByproId(product.getId());//查找产品图片集合
					//为了兼容老数据如果资源表里没有产品图片则从产品表imgUrl字段中取值
					if(imgList==null||imgList.size()==0){
						if(product.getImgUrl() != null){
							String[] imgUrlListArray = product.getImgUrl().split("\\|");
							for(String url:imgUrlListArray){
								Resource re=new Resource();
								re.setUrl(url);
								imgList.add(re);
							}
						}
					}
					if(imgList.size()>0){
						productInfoVO.setImage(imgList.get(0).getUrl());
					}
					infoVOList.add(productInfoVO);
				}
			}
			return infoVOList;
		} catch (DaoException daoe) {
			throw new ServiceException("BusinessUnitServiceImpl.loadProductInfoByOrganization() " + daoe.getMessage(), daoe);
		}
	}

	/**
	 * * 为食安监提供的溯源接口
	 * @param barcode 产品条码
	 * @param batch 相关批次
	 * @return List<List<SajTraceabilityVO>>
	 */
	@Override
	public List<List<SajTraceabilityVO>> sajTraceability(String barcode,
			String batch) throws ServiceException {
		try {
        	List<ReceivingNote> list = getDAO().getProductTraceabilityList(barcode ,batch);
            List<List<SajTraceabilityVO>> traceability = new ArrayList<List<SajTraceabilityVO>>();
            for (int i = 0; i < list.size(); i++) {
                List<SajTraceabilityVO> businessUnitList = new ArrayList<SajTraceabilityVO>();
                List<SajTraceabilityVO> newList = new ArrayList<SajTraceabilityVO>();
                boolean count = false;
                for (int j = 0; j < list.size(); j++) {
                    count = false;
                    if (i != j && list.get(i).getOrganization().equals(list.get(j).getRe_provide_num().getOrganization())) {
                        break;
                    }
                    count = true;
                }
                if (count ) {
                    BusinessUnit receiveBus = getDAO().findByOrgnizationId(list.get(i).getOrganization());//收货企业
                    BusinessUnit sourceBus = list.get(i).getRe_provide_num();//来源企业
                    SajTraceabilityVO tVO = new SajTraceabilityVO();
                    tVO.setResourceBusName(sourceBus == null?"":sourceBus.getName());
                    tVO.setDirectionBusName(receiveBus == null?"":receiveBus.getName());
                    tVO.setReceiveDate(list.get(i).getRe_date());//交易时间（收货时间）
                    
	        		Long total = getTransactionNum(list.get(i).getRe_num(),list.get(i).getOrganization(),batch,barcode);//.获取交易数量
	        		tVO.setReceiveNum(total);
                    newList.add(tVO);
                    List<SajTraceabilityVO> bussUnitList = loopSajCompare(list,list.get(i).getRe_provide_num().getOrganization() , i,barcode,batch);
                    businessUnitList.addAll(newList);
                    businessUnitList.addAll(bussUnitList);
                    traceability.add(businessUnitList);
                }
            }
            return traceability;
        } catch (DaoException dex) {
        	throw new  ServiceException("BusinessUnitServiceImpl.productDetailTraceability()-->" + dex.getMessage(), dex.getException());
        }
	}
	
	public List<SajTraceabilityVO> loopSajCompare(List<ReceivingNote> list,Long providerOrgId ,int j,String barcode,String batch) throws ServiceException {
        List<SajTraceabilityVO> listUnit = new ArrayList<SajTraceabilityVO>();
        try {
            for (int i = 0; i < list.size(); i++) {
                if (i != j && providerOrgId.equals(list.get(i).getOrganization())) {
                    List<SajTraceabilityVO> listUnits = new ArrayList<SajTraceabilityVO>();
                    BusinessUnit receiveBus = getDAO().findByOrgnizationId(list.get(i).getOrganization());
                    BusinessUnit sourceBus = list.get(i).getRe_provide_num();
                    SajTraceabilityVO tVO = new SajTraceabilityVO();
                    tVO.setResourceBusName(sourceBus == null?"":sourceBus.getName());
                    tVO.setDirectionBusName(receiveBus == null?"":receiveBus.getName());
                    tVO.setReceiveDate(list.get(i).getRe_date());//交易时间（收货时间）
                    
	        		Long total = getTransactionNum(list.get(i).getRe_num(),list.get(i).getOrganization(),batch,barcode);//.获取交易数量
	        		tVO.setReceiveNum(total);
	        		listUnits.add(tVO);
                    List<SajTraceabilityVO> bussUnitList = loopSajCompare(list,list.get(i).getRe_provide_num().getOrganization(),i,barcode,batch);
                    listUnit.addAll(listUnits);
                    listUnit.addAll(bussUnitList);
                }
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return listUnit;
    }
	
	/**
	 *  通过关键字搜索企业
	 * @param keyword
	 * @param sn
	 * @return
	 */
	@Override
	public RESTResult<BusinessUnit> uploadSearch2(String keyword,Integer sn) {
		int count = businessUnitLFDao.getCount(keyword);
		PageUtil page = new PageUtil(count, sn.intValue(), 15);
		List<BusinessUnit> businessUnitList = businessUnitLFDao.getUnitByKeyword(keyword, page.getStartindex(), page.getPagesize());
		page.setList(businessUnitList);
		return new RESTResult<BusinessUnit>(1, businessUnitList);
	}

	@Override
	public List<Product2EnterpriseColumnVO> getProductInfoByOrganization(
			Long productCount, Long organization) throws ServiceException {
		try {
			List<Product> list = productDao.getReportMaxProductByOrganization(productCount,organization);
			List<Product2EnterpriseColumnVO>  infoVOList = new ArrayList<Product2EnterpriseColumnVO>();
			for(Product product : list){
				if (product != null) {
					 /*处理产品图片*/
			        List<Resource> imgList = resourceService.getProductImgListByproId(product.getId());//查找产品图片集合
					//为了兼容老数据如果资源表里没有产品图片则从产品表imgUrl字段中取值
					if(imgList==null||imgList.size()==0){
						if(product.getImgUrl() != null){
							String[] imgUrlListArray = product.getImgUrl().split("\\|");
							for(String url:imgUrlListArray){
								Resource re=new Resource();
								re.setUrl(url);
								imgList.add(re);
							}
						}
					}
					Product2EnterpriseColumnVO p2EnVO=new Product2EnterpriseColumnVO(
							product.getId(),product.getName(),"",
							reportService.getReportCountForProductId(product.getId()),
							productStdCertificationService.getListOfStandCertificationByProductId(product.getId()));
					if(imgList.size()>0){
						p2EnVO.setImgUrl(imgList.get(0).getUrl());
					}
					infoVOList.add(p2EnVO);
				}
			}
			return infoVOList;
		} catch (DaoException daoe) {
			throw new ServiceException("BusinessUnitServiceImpl.loadProductInfoByOrganization() " + daoe.getMessage(), daoe);
		}
	}

	@Override
	public List<BusinessAndPros2PortalVO> getBusinessAndProductByBuPros(
			List<BusinessAndProductVO> buPros) throws ServiceException {
		try {
			Map<Long,String> map = new HashMap<Long, String>();
			String buIds="";
			for(int i=0;i<buPros.size();i++){
				if(i==0){
					buIds=buIds+buPros.get(i).getId().toString();
				}else{
					buIds=buIds+","+buPros.get(i).getId().toString();
				}
				map.put(buPros.get(i).getId(), buPros.get(i).getProIds());
			}
			List<BusinessAndPros2PortalVO> list = businessUnitLFDao.getBusinessByBuIds(buIds);
			
			for(BusinessAndPros2PortalVO buPro : list){
				String proIds=map.get(buPro.getId());
				if(!StringUtil.isBlank(proIds)){
					List<ProductSortVo> proSorts=productDao.getProductSortVoByProIds(proIds);
					for(ProductSortVo psVo:proSorts){
						 /*处理产品图片*/
				        List<Resource> imgList = resourceService.getProductImgListByproId(psVo.getId());//查找产品图片集合
						//为了兼容老数据如果资源表里没有产品图片则从产品表imgUrl字段中取值
						if(imgList==null||imgList.size()==0){
							if(psVo.getImgUrl() != null){
								String[] imgUrlListArray = psVo.getImgUrl().split("\\|");
								for(String url:imgUrlListArray){
									Resource re=new Resource();
									re.setUrl(url);
									imgList.add(re);
								}
							}
						}
						psVo.setImgUrl(imgList.get(0).getUrl());
					}
					buPro.setProductList(proSorts);
				}
			}
			return list;
		} catch (DaoException daoe) {
			throw new ServiceException("BusinessUnitServiceImpl.getBusinessAndProductByBuPros() " + daoe.getMessage(), daoe);
		}catch (Exception e) {
			throw new ServiceException("BusinessUnitServiceImpl.getBusinessAndProductByBuPros() " + e .getMessage(), e);
		}
	}
	/**
	 * 根据企业组织机构获取企业基本信息,和资质信息
	 * @author wb 
	 * @ date : 2016.5.9
	 * @param organization
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BusinessResultVO getOrganizationBussinessData(Long organization) {
		BusinessResultVO result = businessUnitLFDao.getOrganizationBussinessInfo(organization);
	     List<ResourceVO> licenseImg = businessUnitLFDao.getOrganiaztionQSImg(organization);
	    result.setLicenseImg(licenseImg);
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<ProductInfosVO> getLikeProductInfosVO(String organization,
			String proName, String proBarcode) {
		
		return businessUnitLFDao.getLikeProductInfosVO(organization,proName,proBarcode);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ScoreInfoVO getScoreInfoData(String productBarcode, String startDate,String endDate) {
		
		ScoreInfoVO  scoreInfo = new ScoreInfoVO();
		
		//上传自检批次数量
		String self_num = businessUnitLFDao.getSelfNum(productBarcode, startDate,endDate);
		scoreInfo.setSelf_num(self_num);
		
		//报告操作活跃度
		String activeDegreeInt = businessUnitLFDao.getActiveDegree(productBarcode, startDate,endDate);
		int dayInt = this.getDaysSum(startDate,endDate);
		String active_degree = this.getActive_degree(Integer.parseInt(activeDegreeInt),dayInt);
		scoreInfo.setActive_degree(active_degree);
	
		//送检频次(1：月、2：季度、3：半年)
		int monthInt = businessUnitLFDao.getInspectionFrequency(productBarcode, startDate,endDate);
		int sunInt = this.getMonthSum(startDate,endDate);
		String inspection_frequency = this.getinspection_frequencyInt(monthInt,sunInt);
		scoreInfo.setInspection_frequency(inspection_frequency);
		
		//结果比对合格率
		scoreInfo.setCompare_qualified_rate("100");
		//自检报告合格率
		scoreInfo.setSelf_qualified_rate("100");
		//检测指标覆盖率
		scoreInfo.setCoverage_rate("100");
		return scoreInfo;
	}
	/**
	 * 计算报告操作活跃度
	 * @param parseInt 报告在查询日期范围内的报告总数
	 * @param dayInt  日期范围计算的天数
	 * @return //返回（天/份）
	 */
	private String getActive_degree(int parseInt, int dayInt) {
		String active_degree = ""+dayInt;
		if (parseInt > 0 && dayInt > 0) {
			//天数除以报告总数小于登录3给3分，3到10给2分默认给1分
			int sum = Math.round((float)dayInt / parseInt);
			if (sum < 1) {
				active_degree = "1";
			}else{
				active_degree = sum+"";	
			}
		}
		return active_degree;
	}

	/**
	 * 计算送检频次
	 * @param monthInt 月份数量（几个月）
	 * @param sunInt 报告总数
	 * @return (1：月、2：季度、3：半年)
	 */
	private String getinspection_frequencyInt(int monthInt, int sunInt) {
		String inspection_frequency = "";
		if(sunInt<=4){
			if(monthInt == sunInt){
				inspection_frequency = "1";	
			}else{
				inspection_frequency = "2";	
			}
		}else if(sunInt > 4){
			if(monthInt == sunInt){
				inspection_frequency = "1";	
			}else{
				inspection_frequency = "3";	
			}
		}
		return inspection_frequency;
	}

	/**
	 * 日期计算月数方法
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private int getMonthSum(String startDate, String endDate) {
		 int monthInt = 0;
		try {
		       String d1 = endDate;
			    String d2 = startDate;
			    SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
			    Calendar c = Calendar.getInstance();
			    c.setTime(sdf.parse(d1));
			    int year1 = c.get(Calendar.YEAR);
			    int month1 = c.get(Calendar.MONTH);
			     
			    c.setTime(sdf.parse(d2));
			    int year2 = c.get(Calendar.YEAR);
			    int month2 = c.get(Calendar.MONTH);
			    if(year1 == year2) {
			    	monthInt = month1 - month2;
			    } else {
			    	monthInt = 12*(year1 - year2) + month1 - month2;
			    }
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return monthInt;
	}
	/**
	 * 日期计算月数方法
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private int getDaysSum(String startDate, String endDate) {
		int dayInt = 0;
		try {
			 //时间转换类
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1s = sdf.parse(endDate);
            Date date2s = sdf.parse(startDate);
            //将转换的两个时间对象转换成Calendard对象
            Calendar can1 = Calendar.getInstance();
            can1.setTime(date1s);
            Calendar can2 = Calendar.getInstance();
            can2.setTime(date2s);
            //拿出两个年份
            int year1 = can1.get(Calendar.YEAR);
            int year2 = can2.get(Calendar.YEAR);
            //天数
            int days = 0;
            Calendar can = null;
            //如果can1 < can2
            //减去小的时间在这一年已经过了的天数
            //加上大的时间已过的天数
            if(can1.before(can2)){
                days -= can1.get(Calendar.DAY_OF_YEAR);
                days += can2.get(Calendar.DAY_OF_YEAR);
                can = can1;
            }else{
                days -= can2.get(Calendar.DAY_OF_YEAR);
                days += can1.get(Calendar.DAY_OF_YEAR);
                can = can2;
            }
            for (int i = 0; i < Math.abs(year2-year1); i++) {
                //获取小的时间当前年的总天数
                days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
                //再计算下一年。
                can.add(Calendar.YEAR, 1);
            }
            dayInt = days;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dayInt;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PlusInfoVO getPlusInfoData(String productBarcode, String startDate,String endDate) {
		
		return businessUnitLFDao.getPlusInfoData(productBarcode, startDate,endDate);
	}
}
