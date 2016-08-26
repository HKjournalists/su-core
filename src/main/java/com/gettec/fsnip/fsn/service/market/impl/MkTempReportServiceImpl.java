package com.gettec.fsnip.fsn.service.market.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.gettec.fsnip.fsn.dao.market.MkTempBusUnitDAO;
import com.gettec.fsnip.fsn.dao.market.MkTempProductDAO;
import com.gettec.fsnip.fsn.dao.market.MkTempReportDAO;
import com.gettec.fsnip.fsn.dao.market.impl.MKTempItemDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.MkTempBusUnit;
import com.gettec.fsnip.fsn.model.market.MkTempProduct;
import com.gettec.fsnip.fsn.model.market.MkTempReport;
import com.gettec.fsnip.fsn.model.market.MkTempReportItem;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.service.market.MkTempReportService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.product.ProductTobusinessUnitService;
import com.gettec.fsnip.fsn.vo.business.QsNoAndFormatVo;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;
import com.gettec.fsnip.fsn.vo.report.ReportOfMarketVO;
import com.lhfs.fsn.dao.product.ProductDao;
import com.lhfs.fsn.service.testReport.TestReportService;

@Service(value="tempReportService")
public class MkTempReportServiceImpl implements MkTempReportService{
	@Autowired private MkTempReportDAO tempReportDAO;
	@Autowired private MkTempProductDAO tempProductDAO;
	@Autowired private MkTempBusUnitDAO tempBusUnitDAO;
	@Autowired private MKTempItemDAOImpl tempItemDAO;
	@Autowired private ProductDao productDao;
	@Autowired private ProductTobusinessUnitService productToBusinessUnitService;
	@Autowired private ProductService productService;
	@Autowired private TestReportService testReportService;
	
	/**
	 * 功能描述：保存报告信息
	 * @throws ServiceException 
	 * @author ZhangHui 2015/6/9
	 * @return 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ReportOfMarketVO save(ReportOfMarketVO report_vo, String realUserName, Long myOrgnizationId) throws ServiceException {
		try {
			/* 1.保存报告信息  */
			report_vo = saveTempReport(report_vo, realUserName, myOrgnizationId);
//			testReportService.saveTestProperty(report_vo.getTestProperties());
			/* 2.保存产品信息  */
			MkTempProduct tempProduct = null;
			ProductOfMarketVO product_vo = report_vo.getProduct_vo();
			
			// 如果当前产品前台不允许编辑（即已经被生产企业认领）
			if(!product_vo.isCan_edit_pro() && product_vo.getBarcode()!=null && "".equals(product_vo.getBarcode())){
				Product orig_pro = productDao.findByBarcode(product_vo.getBarcode());
				
				if(orig_pro != null){
					tempProduct = new MkTempProduct(orig_pro);
					tempProduct.setProDate(product_vo.getProductionDateStr());
					tempProduct.setBatchNo(product_vo.getBatchSerialNo());
				}else{
					// 前台默认此产品已经被生产企业认领，但后台无法查询到此产品，这属于异常操作；不允许缓存这样的数据。
					return report_vo;
				}
			}
			
			if(tempProduct == null){
				tempProduct = new MkTempProduct(report_vo.getProduct_vo());
			}
			
			saveTempProduct(tempProduct, realUserName, myOrgnizationId);
			
			/* 3.保存生产商信息  */
			MkTempBusUnit tempBusUnit = new MkTempBusUnit(report_vo.getBus_vo());
			saveTempBusUnit(tempBusUnit, product_vo.getQs_info(), realUserName, myOrgnizationId);
			
			return report_vo;
		} catch (Exception e) {
			throw new ServiceException("MkTempReportServiceImpl.save()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 保存临时报告信息
	 * @return 
	 * @throws ServiceException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private ReportOfMarketVO saveTempReport(ReportOfMarketVO report_vo, String realUserName, Long myRealOrgnizationId) throws ServiceException {
		try {
			MkTempReport tempReport = new MkTempReport(report_vo);
			for(TestProperty testProperty : report_vo.getTestProperties()){
				MkTempReportItem item = new MkTempReportItem(testProperty);
				item.setTempReport(tempReport);
				tempReport.getListOfItems().add(item);
			}
			// 保存报告信息
			MkTempReport orig = tempReportDAO.findByUserRealNameAndOrganization(realUserName, myRealOrgnizationId);
			if(orig == null){
				tempReport.setCreateUserRealName(realUserName);
				tempReport.setOrganization(myRealOrgnizationId);
				tempReport.setLastModifyTime(new Date());
				tempReportDAO.persistent(tempReport);
			}else{
				updateTempItems(orig, tempReport);
				setTempItemValue(orig, tempReport, realUserName, myRealOrgnizationId);
				tempReportDAO.merge(orig);
				tempReport.setListOfItems(orig.getListOfItems());
			}
			
			// 保存检测项目
			List<TestProperty> listOfItems = new ArrayList<TestProperty>();
			for(MkTempReportItem item_temp : tempReport.getListOfItems()){
				TestProperty item = new TestProperty(item_temp);
				listOfItems.add(item);
			}
			
			report_vo.setTestProperties(listOfItems);
			
			return report_vo;
		} catch (Exception e) {
			throw new ServiceException("MkTempReportServiceImpl.saveTempReport()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 保存临时产品信息
	 * @param tempProduct
	 * @param info
	 * @throws ServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	private void saveTempProduct(MkTempProduct tempProduct, String realUserName,
			Long myRealOrgnizationId) throws ServiceException {
		try {
			MkTempProduct orig = tempProductDAO.findByUserRealNameAndOrganization(realUserName,myRealOrgnizationId);
			if(orig == null){
				tempProduct.setCreateUserRealName(realUserName);
				tempProduct.setOrganization(myRealOrgnizationId);
				tempProduct.setLastModifyTime(new Date());
				tempProductDAO.persistent(tempProduct);
			}else{
				setTempProductValue(orig, tempProduct, realUserName, myRealOrgnizationId);
				tempProductDAO.merge(orig);
			}
		} catch (Exception e) {
			throw new ServiceException("【Service-error】保存临时产品信息，出现异常", e);
		}
	}
	
	/**
	 * 保存临时生产商信息
	 * @param qs_vo 
	 * @throws ServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	private void saveTempBusUnit(MkTempBusUnit tempBusUnit, QsNoAndFormatVo qs_vo, String realUserName,
			Long myRealOrgnizationId) throws ServiceException {
		try {
			MkTempBusUnit orig = tempBusUnitDAO.findByUserRealNameAndOrganization(realUserName, myRealOrgnizationId);
			if(orig == null){
				tempBusUnit.setCreateUserRealName(realUserName);
				tempBusUnit.setOrganization(myRealOrgnizationId);
				tempBusUnit.setLastModifyTime(new Date());
				tempBusUnitDAO.persistent(tempBusUnit);
			}else{
				orig.setLicenseNo(tempBusUnit.getLicenseNo());
				if(qs_vo != null){
					orig.setQsNo(qs_vo.getQsNo());
					orig.setQsnoFormatId(qs_vo.getLicenceFormat()==null?1:qs_vo.getLicenceFormat().getId());
				}
				orig.setName(tempBusUnit.getName());
				orig.setAddress(tempBusUnit.getAddress());
				orig.setCreateUserRealName(realUserName);
				orig.setOrganization(myRealOrgnizationId);
				orig.setLastModifyTime(new Date());
				tempBusUnitDAO.merge(orig);
			}
		} catch (Exception e) {
			throw new ServiceException("MkTempReportServiceImpl.saveTempBusUnit()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 更新报告的检测项目列表
	 * @param origTempReport
	 * @param nowTempReport
	 * @throws ServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void updateTempItems(MkTempReport origTempReport, MkTempReport nowTempReport) 
			throws ServiceException {
		try {
			List<MkTempReportItem> origItems = origTempReport.getListOfItems();
			List<MkTempReportItem> nowItems = nowTempReport.getListOfItems();
			
			Set<MkTempReportItem> removes = new HashSet<MkTempReportItem>();
			List<Long> currentId = new ArrayList<Long>();
			
			for(MkTempReportItem item : nowItems){
				if(item.getId() != null){
					currentId.add(item.getId());
				}
			}
			for(MkTempReportItem item : origItems){
				if(!currentId.contains(item.getId())){
					removes.add(item);
				}
			}
			if(!CollectionUtils.isEmpty(removes)){
				origTempReport.removeItems(removes);
				for(MkTempReportItem item : removes){
					tempItemDAO.remove(tempItemDAO.findById(item.getId()));
				}
			}
			for(MkTempReportItem item : nowTempReport.getListOfItems()){
				if(item.getId() == null){
					if(item.getItemName()==null){ continue; }
					tempItemDAO.persistent(item);
				}
				item.setTempReport(origTempReport);
			}
		} catch (JPAException jpae) {
			throw new ServiceException("新增或删除检测项目，出现异常", jpae.getException());
		} catch (Exception e) {
			throw new ServiceException("【Service-error】更新报告的检测项目列表，出现异常", e);
		}
	}

	/**
	 * 背景：在报告录入页面
	 * 功能描述：按登录用户信息获取一条缓存报告信息
	 * @throws ServiceException 
	 * @author ZhangHui 2015/6/4
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ReportOfMarketVO getTempReport(String userName, Long myOrgnization) throws ServiceException{
		try {
			/* 1. tempReport */
			MkTempReport tempReport = tempReportDAO.findByUserRealNameAndOrganization(userName, myOrgnization);
			
			if(tempReport == null){
				return null;
			}
			
			ReportOfMarketVO report = new ReportOfMarketVO(tempReport);
			List<TestProperty> listOfItems = new ArrayList<TestProperty>();
			for(MkTempReportItem item_temp : tempReport.getListOfItems()){
				TestProperty item = new TestProperty(item_temp);
				listOfItems.add(item);
			}
			report.setTestProperties(listOfItems);
			
			/* 2. tempProduct */
			MkTempProduct tempProduct = tempProductDAO.findByUserRealNameAndOrganization(userName, myOrgnization);
			if(tempProduct == null){
				return null;
			}
			
			// 根据产品条形码查看该产品有无被生产企业认领
			ProductOfMarketVO product_vo = null;
			Product orig_product = productService.findByBarcodeOfHasClaim(tempProduct.getBarCode());
			if(orig_product == null){
				// 没有被认领
				product_vo = new ProductOfMarketVO(tempProduct);
				
				// 判断该产品是否已经存在，如果存在，需要加载产品图片。
				Product product = productDao.findByBarcode(tempProduct.getBarCode());
				if(product != null){
					product_vo.setProAttachments(product.getProAttachments());
				}
			}else{
				// 已经被认领
				product_vo = new ProductOfMarketVO(orig_product);
				product_vo.setCan_edit_pro(false); // 不允许编辑产品信息
				// 批次
				product_vo.setBatchSerialNo(tempProduct.getBatchNo());
				// 生产日期
				product_vo.setProductionDateStr(tempProduct.getProDate());
			}
			
			/* 3. tempBusUnit */
			MkTempBusUnit tempBusUnit = tempBusUnitDAO.findByUserRealNameAndOrganization(userName, myOrgnization);
			if(tempBusUnit == null){
				return null;
			}
			
			// 获取最新的qs号，避免使用过期的qs号
			BusinessUnitOfReportVO bus_vo = productToBusinessUnitService.getBusUnitOfReportVOByBusname(
					tempBusUnit.getName(), tempProduct.getBarCode());
			
			if(bus_vo == null){
				bus_vo = new BusinessUnitOfReportVO(tempBusUnit);
			}
			
			report.setProduct_vo(product_vo);
			report.setBus_vo(bus_vo);
				
			return report;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("MkTempReportServiceImpl.getTempReport()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 按登录用户信息清空一条临时报告信息
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void clearTemp(String realUserName, Long myRealOrgnizationId) throws ServiceException{
		clearTempReport(realUserName, myRealOrgnizationId);
		clearTempProduct(realUserName, myRealOrgnizationId);
		clearTempBusUnit(realUserName, myRealOrgnizationId);
	}
	
	/**
	 * 按登录用户信息清空一条临时生产商信息
	 * @param info
	 * @throws ServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	private void clearTempBusUnit(String realUserName, Long myRealOrgnizationId) throws ServiceException {
		try {
			MkTempBusUnit orig = tempBusUnitDAO.findByUserRealNameAndOrganization(realUserName, myRealOrgnizationId);
			if(orig != null){
				orig.setLicenseNo(null);
				orig.setQsNo(null);
				orig.setName(null);
				orig.setAddress(null);
				orig.setQsnoFormatId(1);
				orig.setLastModifyTime(new Date());
				tempBusUnitDAO.merge(orig);
			}
		} catch (JPAException jpae) {
			throw new ServiceException("更新生产商信息，出现异常", jpae.getException());
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		} catch (Exception e) {
			throw new ServiceException("【Service-error】按登录用户信息删除一条临时生产商信息，出现异常", e);
		}
	}

	/**
	 * 按登录用户信息清空一条临时生产企业信息
	 * @param info
	 * @throws ServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	private void clearTempProduct(String realUserName, Long myRealOrgnizationId) throws ServiceException {
		try {
			MkTempProduct orig = tempProductDAO.findByUserRealNameAndOrganization(realUserName, myRealOrgnizationId);
			if(orig != null){
				orig.setBarCode("");
				orig.setName("");
				orig.setSpecification("");
				orig.setProDate("");
				orig.setBatchNo("");
				orig.setSampleAddress("");
				orig.setModelNo("");
				orig.setBrand("");
				orig.setProductStatus("");
				orig.setExpiration("");
				orig.setExpirDay("");
				orig.setCategory("");
				orig.setMinUnit("");
				orig.setStandard("");
				orig.setLastModifyTime(new Date());
				orig.setCategoryName("");
				orig.setCategoryparentcode("");
				orig.setCategoryparentid(null);
				tempProductDAO.merge(orig);
			}
		} catch (JPAException jpae) {
			throw new ServiceException(jpae.getMessage(), jpae.getException());
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		} catch (Exception e) {
			throw new ServiceException("【Service-error】按登录用户信息清空一条临时生产企业信息，出现异常", e);
		}
	}

	/**
	 * 按登录用户信息清空一条报告信息
	 * @param info
	 * @throws ServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	private void clearTempReport(String realUserName, Long myRealOrgnizationId) throws ServiceException {
		try {
			MkTempReport orig = tempReportDAO.findByUserRealNameAndOrganization(realUserName, myRealOrgnizationId);
			if(orig != null){
				MkTempReport nowTempReport = new MkTempReport();
				updateTempItems(orig, nowTempReport);
				orig.setReportNo(null);
				orig.setTesteeName(null);
				orig.setTestOrgnizName(null);
				orig.setTestType(null);
				orig.setConclusion("合格");
				orig.setTestPlace(null);
				orig.setTestDate(null);
				orig.setSampleCount(null);
				orig.setJudgeStandard(null);
				orig.setTestResultDescribe(null);
				orig.setRemark(null);
				orig.setProAddress(null);   //产地
				orig.setLastModifyTime(new Date());
				orig.setSamplePlace(null);
				tempReportDAO.merge(orig);
			}
		} catch (JPAException jpae) {
			throw new ServiceException(jpae.getMessage(), jpae.getException());
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		} catch (Exception e) {
			throw new ServiceException("【Service-error】按登录用户信息清空一条报告信息，出现异常", e);
		}
	}
	
	/**
	 * 更新临时检测项目之前对对象进行赋值操作
	 * @param orig
	 * @param tempReport
	 * @param info
	 */
	private void setTempItemValue(MkTempReport orig, MkTempReport tempReport, String realUserName, Long myRealOrgnizationId) {
		orig.setReportNo(tempReport.getReportNo());
		orig.setTesteeName(tempReport.getTesteeName());
		orig.setTestOrgnizName(tempReport.getTestOrgnizName());
		orig.setTestType(tempReport.getTestType());
		orig.setConclusion(tempReport.getConclusion());
		orig.setTestPlace(tempReport.getTestPlace());
		orig.setTestDate(tempReport.getTestDate());
		orig.setSampleCount(tempReport.getSampleCount());
		orig.setJudgeStandard(tempReport.getJudgeStandard());
		orig.setTestResultDescribe(tempReport.getTestResultDescribe());
		orig.setRemark(tempReport.getRemark());
		orig.setProAddress(tempReport.getProAddress());   //产地
		orig.setCreateUserRealName(realUserName);
		orig.setOrganization(myRealOrgnizationId);
		orig.setLastModifyTime(new Date());
		orig.setSamplePlace(tempReport.getSamplePlace());
		orig.setListOfItems(tempReport.getListOfItems());
	}
	
	/**
	 * 更新临时产品信息之前对对象进行赋值操作
	 * @param orig
	 * @param tempProduct
	 * @param info
	 */
	private void setTempProductValue(MkTempProduct orig, MkTempProduct tempProduct, 
			String realUserName,Long myRealOrgnizationId) {
		orig.setBarCode(tempProduct.getBarCode());
		orig.setName(tempProduct.getName());
		orig.setSpecification(tempProduct.getSpecification());
		orig.setModelNo(tempProduct.getModelNo());
		orig.setBrand(tempProduct.getBrand());
		orig.setProductStatus(tempProduct.getProductStatus());
		orig.setExpiration(tempProduct.getExpiration());
		orig.setExpirDay(tempProduct.getExpirDay());
		orig.setCategory(tempProduct.getCategory());
		orig.setMinUnit(tempProduct.getMinUnit());
		orig.setStandard(tempProduct.getStandard());
		orig.setCreateUserRealName(realUserName);
		orig.setOrganization(myRealOrgnizationId);
		orig.setLastModifyTime(new Date());
		orig.setProDate(tempProduct.getProDate());
		orig.setBatchNo(tempProduct.getBatchNo());
		orig.setSampleAddress(tempProduct.getSampleAddress());
		orig.setCategoryparentid(tempProduct.getCategoryparentid());
		orig.setCategoryparentcode(tempProduct.getCategoryparentcode());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<MkTempReportItem> getMkTempReportItemList(String orderNo, String userName,int page,int pageSize) {
		List<MkTempReportItem>  itemList = tempItemDAO.getMkTempReportItemList(orderNo, userName,page,pageSize);
		
		
		
		
		return itemList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public long getMkTempReportItemCount(String orderNo, String realUserName) {
		return tempItemDAO.getCount(orderNo, realUserName);
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

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public MkTempReport getTempReportOrderNo(String serviceOrder) {
		
		return tempReportDAO.getTempReportOrderNo(serviceOrder);
	}

}