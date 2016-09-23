package com.gettec.fsnip.fsn.service.market.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.market.MkTempItemDAO;
import com.gettec.fsnip.fsn.dao.market.MkTestTemplateDAO;
import com.gettec.fsnip.fsn.dao.market.impl.MKTempItemDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.market.MkTempReportItem;
import com.gettec.fsnip.fsn.model.market.MkTestTemplate;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.market.MkTestTemplateService;
import com.gettec.fsnip.fsn.service.product.ImportedProductService;
import com.gettec.fsnip.fsn.service.product.ProductTobusinessUnitService;
import com.gettec.fsnip.fsn.service.product.impl.ProductServiceImpl;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;
import com.gettec.fsnip.fsn.vo.report.ReportOfMarketVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.dao.product.ProductDao;

@Service(value="templateService")
public class MkTestTemplateServiceImpl implements MkTestTemplateService{
	@Autowired private MkTestTemplateDAO templateDAO;
	@Autowired private ProductDao productDao;
	@Autowired private MkTempItemDAO mkTempItemDAO;
	@Autowired private MKTempItemDAOImpl tempItemDAO;
	@Autowired private TestPropertyService testPropertyService;
	@Autowired private ProductTobusinessUnitService productTobusinessUnitService;
	@Autowired private ProductServiceImpl productService;
	@Autowired private ImportedProductService importedProductService;

	/**
	 * 功能描述：按barcode查找一条产品-生产企业-报告信息
	 * @author ZhangHui 2015/6/5
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ReportOfMarketVO findReportByBarCode(String barcode, AuthenticateInfo info,Long currentUserOrganization) throws ServiceException{
		try {
			/* 条件1. 从template中：条形码、组织机构、用户名称 */
			MkTestTemplate template =  templateDAO.findByBarCode(barcode, currentUserOrganization, info.getUserName());
			if((template!=null && template.getReport()==null) || template==null){
				/* 条件2. 从template表中：条形码、组织机构  */
				List<MkTestTemplate> list =  templateDAO.findByBarCode(barcode, currentUserOrganization);
				for(MkTestTemplate temp : list){
					if(temp.getReport() != null){
						template = temp;
						break;
					}
				}
				if(template == null){
					/* 条件3. 从template表中：条形码  */
					template = templateDAO.findByBarCode(barcode);
				}
			}
			
			String orig_barcode = "";
			ReportOfMarketVO report_vo = null;
			TestResult report = null;
			if(template != null){
				report = template.getReport();
				if(report != null){
					report_vo = new ReportOfMarketVO(report);
					
					// 获取报告的检测项目
					report_vo.setTestProperties(testPropertyService.findByReportId(report.getId()));
					
					// 获取报告对应产品的条形码
					if(report.getSample()!=null && report.getSample().getProduct()!=null){
						orig_barcode = report.getSample().getProduct().getBarcode();
					}
				}
			}
			
			if(report_vo==null || !orig_barcode.equals(barcode)){
				report_vo = new ReportOfMarketVO();
				
				/* 条件4. 从product表中：条形码  */
				Product product = productDao.findByBarcode(barcode);
				if(product != null){
					ProductOfMarketVO product_vo = new ProductOfMarketVO(product);
					BusinessUnitOfReportVO bus_vo = new BusinessUnitOfReportVO(product.getProducer());
					report_vo.setProduct_vo(product_vo);
					report_vo.setBus_vo(bus_vo);
				}
			}
			
			// 判断产品有无被生产企业认领
			ProductOfMarketVO product_vo = report_vo.getProduct_vo();
			if(product_vo==null || product_vo.getId()==null){
				throw new Exception("产品为空");
			}
			/* 查找该产品进口食品信息 */
			ImportedProduct impPro=importedProductService.findByProductId(product_vo.getId());
			product_vo.setImportedProduct(impPro);
			boolean claim = productService.checkClaimOfProduct(product_vo.getId());
			product_vo.setCan_edit_pro(claim?false:true); // 如果该产品已经被生产企业认领，则不允许修改
			
			/* 步骤一：根据条形码获取，产品-生产企业-qs号 关系 */
			List<BusinessUnitOfReportVO> pro2Buss = productTobusinessUnitService.getListByBarcode(barcode);
			
			if(pro2Buss!=null && pro2Buss.size() > 0){
				// 该产品对应的已经绑定qs号的生产企业信息（前台用户从可选生产企业中选择一条后，加载生产企业详细信息使用）
				report_vo.setPro2Bus(pro2Buss);
				
				// 返回前台所有该产品绑定的qs号，用于用户下拉选择
				List<Map<Object,String>> listOfBusunitName = getBusNamesFromPro2Bus(pro2Buss);
				report_vo.setListOfBusunitName(listOfBusunitName);
				
				// 默认为下拉选择的第一个生产企业
				report_vo.setBus_vo(pro2Buss.get(0));
				
				if(report!=null && report.getSample()!=null){
					BusinessUnit producerOfReport = report.getSample().getProducer();
					if(producerOfReport != null){
						/* 1.1 该产品已经录过报告 */
						BusinessUnitOfReportVO bus_vo = getPro2BusFromList(producerOfReport.getName(), pro2Buss);
						if(bus_vo!=null){
							report_vo.setBus_vo(bus_vo);
						}
					}
				}
			}
			
			return report_vo;
		} catch (DaoException e) {
			throw new ServiceException("[DaoException]MkTestTemplateServiceImpl.findReportByBarCode()-->" + e.getMessage(), e.getException());
		} catch (Exception e) {
			throw new ServiceException("[Exception]MkTestTemplateServiceImpl.findReportByBarCode()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 根据企业名称，从已有的产品-生产企业-qs关系list中，获取一条信息
	 * @author ZhangHui 2015/5/1
	 */
	@Override
	public BusinessUnitOfReportVO getPro2BusFromList(String name, List<BusinessUnitOfReportVO> list) {
		for(BusinessUnitOfReportVO vo : list){
			if(name.equals(vo.getName())){
				return vo;
			}
		}
		return null;
	}

	/**
	 * 返回前台所有该产品绑定的qs号，用于用户下拉选择
	 */
	@Override
	public List<Map<Object,String>> getBusNamesFromPro2Bus(List<BusinessUnitOfReportVO> pro2Bus) {
		Map<Object,String> map = null;
		List<Map<Object,String>> result=new ArrayList<Map<Object,String>>();
		for(BusinessUnitOfReportVO vo : pro2Bus){
			map = new HashMap<Object,String>();
			String busName = vo.getName();
			if(busName!=null && !"".equals(busName)){
				map.put("name",busName);
				result.add(map);
			}
		}
		return result;
	}

	/**
	 * 按barcode查找一条template模板信息
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public MkTestTemplate findTemplateByBarCode(String barcode, Long organization, String userName) throws ServiceException{
		try {
			return templateDAO.findByBarCode(barcode, organization, userName);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 按barcode查找一条template模板信息
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<MkTestTemplate> findTemplateByBarCode(String barcode, Long organization) throws ServiceException{
		try {
			return templateDAO.findByBarCode(barcode, organization);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 保存保存 template
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(MkTestTemplate template, boolean isNew) throws ServiceException{
		try {
			if(isNew)
				templateDAO.persistent(template);
			else
				templateDAO.merge(template);
		} catch (JPAException jpae) {
			throw new ServiceException("新增或更新模板信息，出现异常", jpae.getException());
		}
	}
	
	/**
	 * 从t_test_template表中查找所有barcode,
	 * 用于页面按产品条形码自动加载数据的功能。
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<String> getListOfBarCode(Long orignizatonId) throws ServiceException {
		try {
			return templateDAO.getListOfBarCode(orignizatonId);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 按reportId查找一条template模板信息
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public MkTestTemplate findByReportId(Long reportId) throws ServiceException{
		try {
			return templateDAO.findByReportId(reportId);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 根据 barcode 和 organization 查找一条template模板信息id
	 * @author tangxin 2015/6/8
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public Long findIdByOrganizationAndUserName(String barcode, Long organization, String userName) throws ServiceException{
		try {
			return templateDAO.findIdByBarCodeAndOrganizationAndUserName(barcode, organization, userName);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 根据 id 更新 template 中的 barcode 和 reportid 字段 
	 * @author tangxin 2015/6/8
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateById(Long id, String barcode, Long reportId) throws ServiceException{
		try {
			return templateDAO.updateById(id, barcode, reportId);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 创建 template 信息 
	 * @author tangxin 2015/6/8
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean createBySql(String barcode, Long reportId, Long organization, String userName) throws ServiceException{
		try {
			return templateDAO.createBySql(barcode, reportId, organization, userName);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 保存 template 信息
	 * @author tangxin 2015/6/8
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveTemplate(ReportOfMarketVO report_vo, AuthenticateInfo info, Long myOrganization) throws ServiceException{
		try {
			if(report_vo == null || report_vo.getProduct_vo() == null 
					|| info == null || myOrganization == null){
				throw new Exception("参数为空");
			}
			
			boolean isSuccess = false;
			ProductOfMarketVO product_vo = report_vo.getProduct_vo();
			
			// 根据当前用户下指定 barcode 的 template 信息
			Long template_id = findIdByOrganizationAndUserName(product_vo.getBarcode(), myOrganization, info.getUserName());
			if(template_id == null) {
				// template 信息不存在 创建
				isSuccess = createBySql(product_vo.getBarcode(), report_vo.getId(), myOrganization, info.getUserName());
			} else {
				// template 信息已经存在 更新
				isSuccess = updateById(template_id, product_vo.getBarcode(), report_vo.getId());
			}
			
			return isSuccess;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ReportOfMarketVO findReportOfMarketByBarCode(String barcode,
			AuthenticateInfo info, Long currentUserOrganization, int page,
			int pageSize) {
		    ReportOfMarketVO report_vo = new ReportOfMarketVO();
				try {
					/* 条件1. 从template中：条形码、组织机构、用户名称 */
					MkTestTemplate template =  templateDAO.findByBarCode(barcode, currentUserOrganization, info.getUserName());
					if((template!=null && template.getReport()==null) || template==null){
						/* 条件2. 从template表中：条形码、组织机构  */
						List<MkTestTemplate> list =  templateDAO.findByBarCode(barcode, currentUserOrganization);
						for(MkTestTemplate temp : list){
							if(temp.getReport() != null){
								template = temp;
								break;
							}
						}
						if(template == null){
							/* 条件3. 从template表中：条形码  */
							template = templateDAO.findByBarCode(barcode);
						}
					}
					
					String orig_barcode = "";
//				ReportOfMarketVO report_vo = null;
					TestResult report = null;
					if(template != null){
						report = template.getReport();
						if(report != null){
							report_vo = new ReportOfMarketVO(report);
							
							// 获取报告的检测项目
							report_vo.setTestProperties(testPropertyService.findByReportId(report.getId()));
							
							// 获取报告对应产品的条形码
							if(report.getSample()!=null && report.getSample().getProduct()!=null){
								orig_barcode = report.getSample().getProduct().getBarcode();
							}
						}
					}
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
//				try {
//				/* 条件1. 从template中：条形码、组织机构、用户名称 */
//				MkTestTemplate template =  templateDAO.findByTemplateBarCode(barcode, currentUserOrganization, info.getUserName());
//				if((template!=null && template.getReport()==null) || template==null){
//					/* 条件2. 从template表中：条形码、组织机构  */
//					 template =  templateDAO.findByTemplateBarCode(barcode, currentUserOrganization);
//					if(template == null){
//						/* 条件3. 从template表中：条形码  */
//						template = templateDAO.findByTemplateBarCode(barcode);
//					}
//				}
//				TestResult report = null;
//				if(template != null){
//					report = template.getReport();
//					if(report != null){
//						List<TestProperty> testProperties = testPropertyService.getListByReportIdWithPage(report.getId(),page,pageSize);
//						report_vo.setCount(testPropertyService.getCountByReportId(report.getId()));
//						// 获取报告的检测项目
//						report_vo.setTestProperties(testProperties);
//					}
//				}
//			} catch (ServiceException e) {
//				e.printStackTrace();
//			}
		return report_vo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteMkTempReportItem(long id) {
		boolean flag =false ;
		try {
			MkTempReportItem entity = tempItemDAO.findById(id);
			tempItemDAO.remove(entity);
			flag = true;
		} catch (JPAException e) {
			flag = false ;
			e.printStackTrace();
		}
		return flag;
	}
}