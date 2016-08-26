package com.gettec.fsnip.fsn.service.market.impl;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_BRAND_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_CERT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_DIS_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_LICENSE_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_LIQUOR_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_LOGO_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_ORG_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_PRODEP_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_PRODUCT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_QS_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_REPORT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_BRAND_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_CERT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_DIS_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_LICENSE_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_LIQUOR_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_LOGO_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_ORG_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PRODEP_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PRODUCT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_QS_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_REPORT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WASTE_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_WASTE_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_DISHSNO_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_DISHSNO_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_DESTROY_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_DESTROY_PATH;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.gettec.fsnip.fsn.dao.market.MkTestResourceDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;
import com.gettec.fsnip.fsn.model.business.LiutongFieldValue;
import com.gettec.fsnip.fsn.model.business.ProducingDepartment;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.model.dishs.DishsNo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.market.ResourceType;
import com.gettec.fsnip.fsn.model.product.BusinessCertification;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductDestroyRecord;
import com.gettec.fsnip.fsn.model.test.ImportedProductTestResult;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.waste.WasteDisposa;
import com.gettec.fsnip.fsn.service.business.BusinessBrandService;
import com.gettec.fsnip.fsn.service.business.EnterpriseRegisteService;
import com.gettec.fsnip.fsn.service.business.LiutongFieldValueService;
import com.gettec.fsnip.fsn.service.business.ProducingDepartmentService;
import com.gettec.fsnip.fsn.service.business.ProductionLicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.dishs.DishsNoService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.market.ResourceTypeService;
import com.gettec.fsnip.fsn.service.product.ImportedProductService;
import com.gettec.fsnip.fsn.service.product.ProductDestroyRecordService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.test.ImportedProductTestResultService;
import com.gettec.fsnip.fsn.service.waste.WasteDisposaService;
import com.gettec.fsnip.fsn.util.FileUtils;
import com.gettec.fsnip.fsn.util.MKReportNOUtils;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.lhfs.fsn.service.testReport.TestReportService;

@Service(value = "testResourceService")
public class ResourceServiceImpl extends
		BaseServiceImpl<Resource, MkTestResourceDAO> implements
		ResourceService {
	@Autowired private MkTestResourceDAO testResourceDAO;
	@Autowired private ResourceTypeService resourceTypeService;
	@Autowired private ProductService productService;
	@Autowired private TestReportService testReportService;
	@Autowired private EnterpriseRegisteService enterpriseService;
	@Autowired private BusinessBrandService businessBrandService;
	@Autowired private ProducingDepartmentService producingDepartmentService;
	@Autowired private ProductionLicenseService productionLicenseService;
	@Autowired private LiutongFieldValueService liutongFieldValueService;
	@Autowired private ImportedProductService importedProductService;
	@Autowired private ImportedProductTestResultService importedProductTestResultService;
	@Autowired private WasteDisposaService wasteDisposaService;
	@Autowired private DishsNoService dishsNoService;
	@Autowired private ProductDestroyRecordService productDestroyRecordService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 按资源id更新url路径
	 * 
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateResourceUrl(Long resId, String pdfUrl)
			throws ServiceException {
		try {
			Resource resource = findById(resId);
			resource.setUrl(pdfUrl);
			update(resource);
		} catch (ServiceException sex) {
			throw new ServiceException("MkTestResourceServiceImpl"
					+ sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 自动生成pdf
	 * @param input
	 * @param report
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Resource saveAutoPdfResource(InputStream input, TestResult report)
			throws ServiceException {
		try {
			if (input == null) { return null; }
			TestResult orig_report = testReportService.findById(report.getId());
			Resource resource = operationResourcesOfPdf(orig_report, input,
					null, report.getRepAttachments(), orig_report.getTestType());
			return resource;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 保存报告图片
	 * 
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean savePdfResource(TestResult report) {
		try {
			if (report == null) {return false;}
			if (report.isAutoReportFlag()) {return true;}
			Set<Resource> repAttachments = report.getRepAttachments();
			TestResult orig_report = testReportService.findById(report.getId());
			for (Resource resource : repAttachments) {
				if (!report.isNewFlag()) {
					if (resource.getId() != null) {
						/* 如果当前pdf的resource对象的id不为空，则表示是原有的pdf，无需上传到ftp */
						return true;
					}
				}
				if (resource.getFile() != null) {
					/* 上传pdf */
					ByteArrayInputStream in_fullPdf = new ByteArrayInputStream(resource.getFile());
					ByteArrayInputStream in_interceptionPdf = new ByteArrayInputStream(resource.getFile());
					resource = operationResourcesOfPdf(orig_report, in_fullPdf,in_interceptionPdf, 
							repAttachments,report.getTestType());
				} else if (resource.getUrl()!=null && !resource.getUrl().equals("")) {
					/* 多张图片合成pdf */
					create(resource);
					Set<Resource> removes = getListOfRemoves(orig_report.getRepAttachments(), repAttachments);
					if (!CollectionUtils.isEmpty(removes)) {
						orig_report.removeResources(removes);
						for (Resource res : removes) {
							delete(res);
						}
					}
					orig_report.setInterceptionPdfPath(resource.getInterceptionPdfPath());
					orig_report.setFullPdfPath(resource.getUrl());
				}
				if (resource != null && resource.getId() != null) {
					orig_report.getRepAttachments().clear();
					orig_report.getRepAttachments().add(resource);
					orig_report.setFullPdfPath(resource.getUrl());
					orig_report.setSignFlag(false);
					testReportService.update(orig_report);
					report.setRepAttachments(orig_report.getRepAttachments());
					break;
				} else {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @param orig_report
	 * @param in_fullPdf
	 * @param in_interceptionPdf
	 * @param repAttachments
	 * @param testType
	 * @return
	 */
	private Resource operationResourcesOfPdf(TestResult orig_report,
			InputStream in_full, ByteArrayInputStream in_cut,
			Set<Resource> repAttachments, String testType) {
		try {
			/* 1.对报告编号进行处理（避免因包含特殊字符导致上传或下载失败） */
			String reportNOEng = MKReportNOUtils.convertCharacter(orig_report.getServiceOrder(), orig_report.getUploadPath());
			orig_report.setUploadPath(reportNOEng);
			/* 2.数据准备 */
			Set<Resource> adds = new HashSet<Resource>();
			Map<String, String> map = null;
			String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
			String fileName = reportNOEng + "-" + randomStr + "-report.pdf";
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_REPORT_PATH) + "/" + reportNOEng;
			/* 3.上传pdf到ftp服务器 */
			UploadUtil uploadUtil = new UploadUtil();
			map = uploadUtil.uploadReportPdf(in_full, in_cut, ftpPath, fileName, reportNOEng, testType);
			if (map != null) {
				Resource resource = new Resource();
				if(UploadUtil.IsOss()){
					resource.setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+fileName));
				}else{
					resource.setUrl(map.get("fullPdfPath"));
				}
				resource.setFileName(fileName);
				resource.setName(fileName);
				resource.setType(resourceTypeService.findById(3L));
				create(resource);
				adds.add(resource);
				/* 4.将报告与pdf关联 */
				orig_report.setFullPdfPath(map.get("fullPdfPath"));
				if (map != null) {
					orig_report.setInterceptionPdfPath(map.get("interceptionPdfPath"));
				}
				Set<Resource> removes = getListOfRemoves(
						orig_report.getRepAttachments(), repAttachments);
				if (!CollectionUtils.isEmpty(removes)) {
					orig_report.removeResources(removes);
					for (Resource res : removes) {
						delete(res);
					}
				}
				if (!CollectionUtils.isEmpty(adds)) {
					orig_report.addResources(adds);
				}
				testReportService.update(orig_report); // 报告pdf关联
				return resource;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 保存商标的图片
	 * @param businessBrand
	 * @return void
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveBusinessBrandResources(BusinessBrand businessBrand)
			throws ServiceException {
		try {
			Set<Resource> nowLogAttachments = businessBrand.getLogAttachments();
			Set<Resource> adds = new HashSet<Resource>();
			String randomStr = "";
			String fileName = "";
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_BRAND_PATH);
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_BRAND_PATH);
			UploadUtil uploadUtil = new UploadUtil();
			for (Resource resource : nowLogAttachments) {
				randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
				if (resource.getId() != null) {
					resource = findById(resource.getId());
					continue;
				}
				if (resource.getId() == null && resource.getFile() != null) {
					fileName = randomStr + "." + resource.getType().getRtDesc();
					boolean success_upload = uploadUtil.uploadFile(resource.getFile(), ftpPath, fileName);
					if(success_upload){
						if(UploadUtil.IsOss()){
							resource.setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+fileName));
						}else{
							resource.setUrl(webUrl + "/" + fileName);
						}
						resource.setFileName(fileName);
						resource.setName(fileName);
						create(resource);
						adds.add(resource);
					}
				}
			}

			/* 在编辑商标时：需要从数据库中删除在页面已经被删除的图片资源 */
			if (businessBrand.getId() != null) {
				BusinessBrand origBrand = businessBrandService.findById(businessBrand.getId());
				Set<Resource> removes = getListOfRemoves(origBrand.getLogAttachments(), nowLogAttachments);
				if (!CollectionUtils.isEmpty(removes)) {
					origBrand.removeResources(removes);
					for (Resource resource : removes) {
						this.delete(resource.getId());
					}
				}
				if (!CollectionUtils.isEmpty(adds)) {
					origBrand.addResources(adds);
				}
				businessBrandService.update(origBrand);
			}
		} catch (ServiceException ioe) {
			throw new ServiceException("MkTestResourceServiceImpl.saveBusinessBrandResources()-->" + ioe.getMessage(), ioe);
		}
	}

	/**
	 * 保存产品的认证图片列表
	 * @param business
	 * @return void
	 * @throws ServiceException
	 * @author LongXianZhen
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveCertificationResources(BusinessUnit business)
			throws ServiceException {
		try {
			List<BusinessCertification> listOfCertification = business.getListOfCertification();
			String randomStr = "";
			String fileName = "";
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_CERT_PATH);
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_CERT_PATH);
			UploadUtil uploadUtil = new UploadUtil();
			for (BusinessCertification certification : listOfCertification) {
				Resource certResource = certification.getCertResource(); //证书图片
				Resource certIconRes = certification.getCert().getCertIconResource();//荣誉证书小图
				/*上传图片认证*/
				if (certResource.getId() == null && certResource.getFile() != null) {
					randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
					fileName = business.getId() + "-" + randomStr + ".jpg";
					boolean success_upload = uploadUtil.uploadFile(certResource.getFile(), ftpPath, fileName);
					if(success_upload){
						if(UploadUtil.IsOss()){
							certResource.setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+fileName));
						}else{
							certResource.setUrl(webUrl + "/" + fileName);
						}
						certResource.setFileName(certResource.getName());
						certResource.setName(certResource.getName());
						create(certResource);
					}
				}
				/*上传荣誉图标*/
				if (certification.getCert().getStdStatus()!=null && certification.getCert().getStdStatus()==1 ) {
					if(certIconRes != null && certIconRes.getUrl() == null && certIconRes.getFile() != null){
						randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
						fileName = business.getId() + "-" + randomStr + ".jpg";
						boolean success_upload_Icon = uploadUtil.uploadFile(certIconRes.getFile(), ftpPath, fileName);
						if(success_upload_Icon){
							if(UploadUtil.IsOss()){
								certIconRes.setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+fileName));
							}else{
								certIconRes.setUrl(webUrl + "/" + fileName);
							}
							
							certIconRes.setFileName(certIconRes.getName());
							certIconRes.setName(certIconRes.getName());
						}
					}
				}

			}
		} catch (ServiceException sex) {
			throw new ServiceException("【service-error】上传产品的认证图片，出现异常！", sex.getException());
		}
	}

	/**
	 * 保存产品图片
	 * @product
	 * @return void
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveProResource(Product product) throws ServiceException {
		try {
			Set<Resource> proAttachments = product.getProAttachments();
			Product origProduct = productService.getDAO().findByBarcode(
					product.getBarcode());
			/* 1.获取删除的产品图片列表 */
			Set<Resource> removes = null;
			if (origProduct != null) {
				removes = getListOfRemoves(origProduct.getProAttachments(),
						proAttachments);
			}
			Set<Resource> adds = new HashSet<Resource>();
			String randomStr = "";
			String fileName = "";
			String imgUrls = "";
			/* 2.对产品图片集合进行循环：（根据resource的id是否为null，来判断该图片是否是新增图片） */
			for (Resource resource : proAttachments) {
				randomStr = sdf.format(new Date())
						+ (new Random().nextInt(1000) + 1);
				fileName = product.getBarcode() + randomStr + "."
						+ resource.getType().getRtDesc();
				if (resource.getId() != null && origProduct != null) {
					/* 2.1 产品图片已存在，且产品表中barcode已存在 */
					resource = findById(resource.getId());
					if (removes.size() > 0) {
						for (Resource rs : removes) {
							if (rs.getId() != resource.getId()) {
								imgUrls += resource.getUrl() + "|";
								break;
							}
						}
					} else {
						imgUrls += resource.getUrl() + "|";
					}
					continue;
				} else if (resource.getId() != null && origProduct == null) {
					/* 2.2 产品图片已存在，且产品表中barcode不存在 */
					resource.setId(null);
					imgUrls += resource.getUrl() + "|";
					continue;
				}
				if (resource.getFile() != null) {
					/* 2.3 产品图片是页面新增图片 */
					String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_PRODUCT_PATH) + "/" + product.getBarcode();
					UploadUtil uploadUtil = new UploadUtil();
					boolean success_upload = uploadUtil.uploadFile(resource.getFile(), ftpPath, fileName);
					if(success_upload){
						String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PRODUCT_PATH) + "/" + product.getBarcode();
						if(UploadUtil.IsOss()){
							resource.setUrl(uploadUtil.getOssSignUrl(ftpPath + "/" + fileName));
						}else{
							resource.setUrl(webUrl + "/" + fileName);
						}
						resource.setFileName(fileName);
						resource.setName(fileName);
						imgUrls = webUrl + "/" + fileName + "|" + imgUrls;
						if (origProduct != null) {
							create(resource);
							adds.add(resource);
						}
					}
				}
			}
			/* 3.更新产品信息 */
			if (origProduct != null) {
				/* 3.1 删除产品图片与产品的关系 */
				if (!CollectionUtils.isEmpty(removes)) {
					origProduct.removeResources(removes);
					for (Resource resource : removes) {
						long count = getDAO().getRelationCountByResourceId(
								resource.getId());
						if (count == 1) {
							delete(resource);
						}
					}
				}
				/* 3.2 增加产品图片与产品的关系 */
				if (!CollectionUtils.isEmpty(adds)) {
					origProduct.addResources(adds);
				}
				/* 3.3 当前产品没有产品图片时，用一张临时图片代替 */
				if (imgUrls.equals("")) {
					imgUrls = "http://qa.fsnrec.com/portal/img/product/temp/temp.jpg";
				}
				origProduct.setImgUrl(imgUrls);
				origProduct.setExpirationDate(product.getExpirationDate());
				productService.update(origProduct);
			} else {
				if (product.getProAttachments().size() == 0) {
					imgUrls = "http://qa.fsnrec.com/portal/img/product/temp/temp.jpg";
				}
			}
			product.setImgUrl(imgUrls);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("删除资源，出现异常", e);
		}
	}

	/**
	 * 保存组织机构代码证件
	 * 
	 * @param orgAttachments
	 * @param businessUnit
	 * @return void
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveOrgnizationResource(Set<Resource> orgAttachments,BusinessUnit busUnit) throws ServiceException {
		try {
			if (orgAttachments == null || orgAttachments.size() < 1) {
				return;
			}
			EnterpriseRegiste orig_enterprise = enterpriseService.findbyEnterpriteName(busUnit.getName());
			/* 流通企业上传的组织机构信息 */
			LiutongFieldValue ltOrgan_orig = liutongFieldValueService.findByProducerIdAndValueAndDisplay(busUnit.getId(), orig_enterprise.getOrganizationNo(), "组织机构代码");
			List<Long> liutongOrgResId = null;
			if(ltOrgan_orig != null) {
				/* 流通企业-生产企业管理页面，组织机构关联的图片id */
				liutongOrgResId = liutongFieldValueService.getResourceIds(busUnit.getId(), orig_enterprise.getOrganizationNo(), "组织机构代码");
			}
			/* 1.获取已删除的[组织机构代码证件]资源列表 */
			Set<Resource> removes = getListOfRemoves(orig_enterprise == null ? null : orig_enterprise.getOrgAttachments(),orgAttachments);
			/* 2.对图片集合进行循环 */
			Set<Resource> adds = operationResources(orgAttachments, "org", null);
			/* 3.保存资源 */
			if (!CollectionUtils.isEmpty(removes)) {
				orig_enterprise.removeOrgResources(removes);
				for (Resource resource : removes) {
					BigInteger resId = BigInteger.valueOf(resource.getId());
					/* 同步删除流通企业-生产企业管理页面相同的组织机构图片  */
					if(liutongOrgResId != null && liutongOrgResId.contains(resId)){
						ltOrgan_orig.removeResources(resource);
					}
					delete(resource);
				}
			}
			if (!CollectionUtils.isEmpty(adds)) {
				orig_enterprise.addOrgResources(adds);
				if(ltOrgan_orig != null) {
					ltOrgan_orig.addResources(adds);
				}
			}
			enterpriseService.update(orig_enterprise);
			if(ltOrgan_orig != null) {
				liutongFieldValueService.update(ltOrgan_orig);
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("删除资源，出现异常", e);
		}
	}

	/**
	 * 保存营业执照图片
	 * 
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveLicenseResource(Set<Resource> licAttachments, String name)
			throws ServiceException {
		try {
			if (licAttachments == null || licAttachments.size() < 1) {
				return;
			}
			EnterpriseRegiste orig_enterprise = enterpriseService
					.findbyEnterpriteName(name);
			if (orig_enterprise != null
					&& orig_enterprise.getStatus().equals("等待审核")) {
				licAttachments = orig_enterprise.getLicAttachments();
				return;
			}
			/* 1.获取已删除的[营业执照]资源列表 */
			Set<Resource> removes = getListOfRemoves(
					orig_enterprise.getLicAttachments(), licAttachments);
			/* 2.对图片集合进行循环 */
			Set<Resource> adds = operationResources(licAttachments, "license",
					null);
			/* 3.保存资源 */
			if (!CollectionUtils.isEmpty(removes)) {
				orig_enterprise.removeLicenseResources(removes);
				for (Resource resource : removes) {
					delete(resource);
				}
			}
			if (!CollectionUtils.isEmpty(adds)) {
				orig_enterprise.addLicResources(adds);
			}
			enterpriseService.update(orig_enterprise);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("保存营业执照图片，出现异常", e);
		}
	}

	/**
	 * 保存流通许可证图片
	 * 
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveDisResource(Set<Resource> disAttachments, String name)
			throws ServiceException {
		try {
			EnterpriseRegiste orig_enterprise = enterpriseService
					.findbyEnterpriteName(name);
			/* 1.获取已删除的[流通许可证]资源列表 */
			Set<Resource> removes = getListOfRemoves(
					orig_enterprise.getDisAttachments(), disAttachments);
			/* 2.对图片集合进行循环 */
			Set<Resource> adds = operationResources(disAttachments, "dis", null);
			/* 3.保存资源 */
			if (!CollectionUtils.isEmpty(removes)&&adds.size()>0) {
				orig_enterprise.removeDisResources(removes);
				for (Resource resource : removes) {
					delete(resource);
				}
			}
			if (!CollectionUtils.isEmpty(adds)&&adds.size()>0) {
				orig_enterprise.addDisResources(adds);
			}else{
				orig_enterprise.addDisResources(removes);
			}
			enterpriseService.update(orig_enterprise);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("删除资源，出现异常", e);
		}
	}

	/**
	 * 保存流通许可证图片
	 * 
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveQsResource(Set<Resource> qsAttachments, String name)
			throws ServiceException {
		try {
			if (qsAttachments == null || qsAttachments.size() < 1) {
				return;
			}
			EnterpriseRegiste orig_enterprise = enterpriseService
					.findbyEnterpriteName(name);
			/* 1.获取已删除的[流通许可证]资源列表 */
			Set<Resource> removes = getListOfRemoves(
					orig_enterprise.getQsAttachments(), qsAttachments);
			/* 2.对图片集合进行循环 */
			Set<Resource> adds = operationResources(qsAttachments, "qs", null);
			/* 3.保存资源 */
			if (!CollectionUtils.isEmpty(removes)) {
				orig_enterprise.removeQsResources(removes);
				for (Resource resource : removes) {
					delete(resource);
				}
			}
			if (!CollectionUtils.isEmpty(adds)) {
				orig_enterprise.addQsResources(adds);
			}
			enterpriseService.update(orig_enterprise);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("删除资源，出现异常", e);
		}
	}

	/**
	 * 保存企业Logo图片
	 * 
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveLogoResource(Set<Resource> logoAttachments, String name)
			throws ServiceException {
		try {
			if (logoAttachments == null || logoAttachments.size() < 1) {
				return;
			}
			EnterpriseRegiste orig_enterprise = enterpriseService
					.findbyEnterpriteName(name);
			/* 1.获取已删除的[企业Logo]资源列表 */
			Set<Resource> removes = getListOfRemoves(
					orig_enterprise.getLogoAttachments(), logoAttachments);
			/* 2.对图片集合进行循环 */
			Set<Resource> adds = operationResources(logoAttachments, "logo",
					null);
			/* 3.保存资源 */
			if (!CollectionUtils.isEmpty(removes)) {
				orig_enterprise.removeLogoResources(removes);
				for (Resource resource : removes) {
					delete(resource);
				}
			}
			if (!CollectionUtils.isEmpty(adds)) {
				orig_enterprise.addLogoResources(adds);
			}
			enterpriseService.update(orig_enterprise);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("删除资源，出现异常", e);
		}
	}

	/**
	 * 保存企业税务登记证信息图片
	 * 
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveTaxRegResource(Set<Resource> taxRegAttachments, String name)
			throws ServiceException {
		try {
			if (taxRegAttachments == null) {
				return;
			}
			EnterpriseRegiste orig_enterprise = enterpriseService
					.findbyEnterpriteName(name);
			/* 1.获取已删除的[税务登记证信息]资源列表 */
			Set<Resource> removes = getListOfRemoves(
					orig_enterprise.getTaxRegAttachments(), taxRegAttachments);
			/* 2.对图片集合进行循环 */
			Set<Resource> adds = operationResources(taxRegAttachments,
					"taxReg", null);
			/* 3.保存资源 */
			if (!CollectionUtils.isEmpty(removes)) {
				orig_enterprise.removeTaxRegResources(removes);
				for (Resource resource : removes) {
					delete(resource);
				}
			}
			if (!CollectionUtils.isEmpty(adds)) {
				orig_enterprise.addTaxRegResources(adds);
			}
			enterpriseService.update(orig_enterprise);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("删除资源，出现异常", e);
		}
	}

	/**
	 * 保存企业酒类销售许可证信息图片
	 * 
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveLiquorResource(Set<Resource> liquorAttachments, String name)
			throws ServiceException {
		try {
			if (liquorAttachments == null) {
				return;
			}
			EnterpriseRegiste orig_enterprise = enterpriseService
					.findbyEnterpriteName(name);
			/* 1.获取已删除的[酒类销售许可证]资源列表 */
			Set<Resource> removes = getListOfRemoves(
					orig_enterprise.getLiquorAttachments(), liquorAttachments);
			/* 2.对图片集合进行循环 */
			Set<Resource> adds = operationResources(liquorAttachments,
					"liquor", null);
			/* 3.保存资源 */
			if (!CollectionUtils.isEmpty(removes)) {
				orig_enterprise.removeLiquorResources(removes);
				for (Resource resource : removes) {
					delete(resource);
				}
			}
			if (!CollectionUtils.isEmpty(adds)) {
				orig_enterprise.addLiquorResources(adds);
			}
			enterpriseService.update(orig_enterprise);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("删除资源，出现异常", e);
		}
	}

	/**
	 * 保存企业生产车间图片
	 * 
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveProDepResource(Set<Resource> proDepAttachments,
			Long proDepId) throws ServiceException {
		try {
			ProducingDepartment orig_proDep = producingDepartmentService
					.findById(proDepId);
			/* 1.获取已删除的[酒类销售许可证]资源列表 */
			Set<Resource> removes = getListOfRemoves(
					orig_proDep.getDepAttachments(), proDepAttachments);
			/* 2.对图片集合进行循环 */
			Set<Resource> adds = operationResources(proDepAttachments,
					"proDep", null);
			/* 3.保存资源 */
			if (!CollectionUtils.isEmpty(removes)) {
				orig_proDep.removeDepResources(removes);
				for (Resource resource : removes) {
					delete(resource);
				}
			}
			if (!CollectionUtils.isEmpty(adds)) {
				orig_proDep.addDepResources(adds);
			}
			producingDepartmentService.update(orig_proDep);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("删除资源，出现异常", e);
		}
	}

	/**
	 * 企业注册时，保存组织机构代码证件和营业执照图片
	 * @param enRegiste
	 * @return void
	 * @throws ServiceException
	 * @author LongXianZhen
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveLicAndOrgResource(EnterpriseRegiste enRegiste)
			throws ServiceException {
		try {
			Set<Resource> licAttachments = enRegiste.getLicAttachments();
			Set<Resource> addLics = new HashSet<Resource>();
			String randomStr = "";
			String fileName = ""; // pdf名称
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_LICENSE_PATH);
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_LICENSE_PATH);
			UploadUtil uploadUtil = new UploadUtil();
			EnterpriseRegiste enReg = enterpriseService.findById(enRegiste
					.getId());
			for (Resource resource : licAttachments) {
				randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
				fileName = randomStr + "." + resource.getType().getRtDesc();
				if (resource.getFile() != null) {
					boolean success_upload = uploadUtil.uploadFile(resource.getFile(), ftpPath, fileName);
					if(success_upload){
						if(UploadUtil.IsOss()){
							resource.setUrl(uploadUtil.getOssSignUrl(ftpPath + "/" + fileName));
						}else{
							resource.setUrl(webUrl + "/" + fileName);
						}
						resource.setFileName(fileName);
						resource.setName(fileName);
						create(resource);
						addLics.add(resource);
					}
				}
			}
			if(StringUtils.isNotBlank(enRegiste.getProductNo())){
				Set<Resource> addService= new HashSet<Resource>();
				Set<Resource> qsAttachments = enRegiste.getQsAttachments();
				ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_QS_PATH);
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_QS_PATH);
				for (Resource resource : qsAttachments) {
					randomStr = sdf.format(new Date())
							+ (new Random().nextInt(1000) + 1);
					fileName = randomStr + "."
							+ resource.getType().getRtDesc();
					if (resource.getFile() != null) {
						boolean success_upload = uploadUtil.uploadFile(resource.getFile(), ftpPath, fileName);
						if(success_upload){
							if(UploadUtil.IsOss()){
								resource.setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+fileName));
							}else{
								resource.setUrl(webUrl + "/" + fileName);
							}
							resource.setFileName(fileName);
							resource.setName(fileName);
							create(resource);
							addService.add(resource);
						}
					}
				}
				enReg.addQsResources(addService);
			}
			if(StringUtils.isNotBlank(enRegiste.getPassNo())){
				Set<Resource> addPass = new HashSet<Resource>();
				Set<Resource> disAttachments = enRegiste.getDisAttachments();
				ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_DIS_PATH);
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_DIS_PATH);
				for (Resource resource : disAttachments) {
					randomStr = sdf.format(new Date())
							+ (new Random().nextInt(1000) + 1);
					fileName = randomStr + "."
							+ resource.getType().getRtDesc();
					if (resource.getFile() != null) {
						boolean success_upload = uploadUtil.uploadFile(resource.getFile(), ftpPath, fileName);
						if(success_upload){
							if(UploadUtil.IsOss()){
								resource.setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+fileName));
							}else{
								resource.setUrl(webUrl + "/" + fileName);
							}
							resource.setFileName(fileName);
							resource.setName(fileName);
							create(resource);
							addPass.add(resource);
						}
					}
				}
				enReg.addDisResources(addPass);
			}
			if(StringUtils.isNotBlank(enRegiste.getServiceNo())){
				Set<Resource> addPro= new HashSet<Resource>();
				Set<Resource> liquorAttachments = enRegiste.getLiquorAttachments();
				ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_LIQUOR_PATH);
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_LIQUOR_PATH);
				for (Resource resource : liquorAttachments) {
					randomStr = sdf.format(new Date())
							+ (new Random().nextInt(1000) + 1);
					fileName = randomStr + "."
							+ resource.getType().getRtDesc();
					if (resource.getFile() != null) {
						boolean success_upload = uploadUtil.uploadFile(resource.getFile(), ftpPath, fileName);
						if(success_upload){
							if(UploadUtil.IsOss()){
								resource.setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+fileName));
							}else{
								resource.setUrl(webUrl + "/" + fileName);
							}
							resource.setFileName(fileName);
							resource.setName(fileName);
							create(resource);
							addPro.add(resource);
						}
					}
					enReg.setLiquorAttachments(addPro);
				}
			}
			
			enReg.addLicResources(addLics);
//			enReg.addOrgResources(addOrgs);
			enterpriseService.update(enReg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Long countBusPdfByBusId(Long id) throws ServiceException {
		try {
			return getDAO().countBySql("business_pdf_to_resource",
					" where business_id= ?1", new Object[] { id });
		} catch (JPAException jpae) {
			throw new ServiceException(
					"MkTestResourceServiceImpl.countBusPdfByBusId() "
							+ jpae.getMessage(), jpae);
		}
	}

	@Override
	public List<Resource> getListBusPdfWithPage(Long busId, int page,
			int pageSize) throws ServiceException {
		try {
			return getDAO().getListBusPdfByBusUnitIdWithPage(page, pageSize,
					busId);
		} catch (DaoException daoe) {
			throw new ServiceException(
					"MkTestResourceServiceImpl.getListBusPdfWithPage() "
							+ daoe.getMessage(), daoe.getException());
		}
	}

	/**
	 * 将新增的图片上传至ftp
	 * 
	 * @param attachments
	 * @param type
	 * @param barcode
	 * @return Set<MkTestResource> 返回新增的图片集合
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	private Set<Resource> operationResources(Set<Resource> attachments,
			String type, String barcode) throws ServiceException {
		try {
			String ftpPath = "";
			String webUrl = "";
			if (type.equals("license")) { // 营业执照号
				ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_LICENSE_PATH);
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_LICENSE_PATH);
			} else if (type.equals("org")) { // 组织机构代码证件
				ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_ORG_PATH);
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_ORG_PATH);
			} else if (type.equals("dis")) { // 流通许可证图片
				ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_DIS_PATH);
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_DIS_PATH);
			} else if (type.equals("qs")) { // 生产许可证图片
				ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_QS_PATH);
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_QS_PATH);
			} else if (type.equals("logo")) {// 企业logo图片
				ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_LOGO_PATH);
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_LOGO_PATH);
			} else if (type.equals("taxReg")) {// 上传税务登记证图片
				ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_LICENSE_PATH);
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_LICENSE_PATH);
			} else if (type.equals("liquor")) { // 上传酒类销售许可证图片
				ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_LIQUOR_PATH);
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_LIQUOR_PATH);
			} else if (type.equals("proDep")) { // 上传企业生产车间图片
				ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_PRODEP_PATH);
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PRODEP_PATH);
			} else if (type.equals("product")) { // 上传产品图片
				ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_PRODUCT_PATH) + "/" + barcode;
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PRODUCT_PATH) + "/" + barcode;
			}
			 else if (type.equals("record")) { // 上传销毁记录图片
					ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_DESTROY_PATH) ;
					webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_DESTROY_PATH) ;
				}
			
			Set<Resource> adds = new HashSet<Resource>();
			for (Resource resource : attachments) {
				/* 1.图片已存在 */
				if (resource.getId() != null && resource.getUrl() != null) {
					continue;
				}
				/*2.url不为空的资源，不再上传ftp*/
				if(resource.getUrl()!=null && resource.getUrl().length()>0){
					create(resource);
					adds.add(resource);
					continue;
				}
				/* 3.图片是页面新增图片 */
				if (resource.getFile() != null) {
					String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
					String urlFileName = randomStr + "." + resource.getType().getRtDesc();
					UploadUtil uploadUtil = new UploadUtil();
					boolean success_upload = uploadUtil.uploadFile(resource.getFile(), ftpPath, urlFileName);
					if(success_upload){
						if(UploadUtil.IsOss()){
							resource.setUrl(uploadUtil.getOssSignUrl(ftpPath + "/" + urlFileName));
						}else{
							resource.setUrl(webUrl + "/" + urlFileName);
						}
						resource.setFileName(resource.getFileName());
						resource.setName(resource.getFileName());
						if (resource.getId() == null) {
							create(resource);
							adds.add(resource);
						} else {
							// 资源已保存到数据库，但为上传的ftp
							Resource org_res = findById(resource.getId());
							org_res.setUrl(resource.getUrl());
							org_res.setFileName(resource.getFileName());
							org_res.setName(resource.getFileName());
							update(org_res);
						}
					}
				}
			}
			return adds;
		} catch (ServiceException sex) {
			throw new ServiceException("按id查找一条资源，出现异常", sex);
		}
	}

	/**
	 * 用于页面增加一条资源
	 */
	@Override
	public List<Resource> addResourcesKendoUI(Collection<Resource> resources)
			throws ServiceException {
		List<Resource> rsss = new ArrayList<Resource>();
		if (resources != null) {
			for (Resource rs : resources) {
				//this.updateResource(rs);
				ResourceType type = resourceTypeService.findByName(FileUtils.getExtension(rs.getFileName()));
				if (type != null) {
					rs.setType(type);
				}
				/*if (rs.getType() == null) {
					System.out.println((rs.getType() == null) + " is null");
					continue;
				}*/
				rsss.add(rs);
				//System.out.println("add files");
			}
		}
		return rsss;
	}

	/**
	 * 保存生产许可证图片
	 * 
	 * @param productionLicense
	 * @param new_qsAttachments
	 * @return void
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ProductionLicenseInfo saveQsResource(ProductionLicenseInfo proLicense)
			throws ServiceException {
		try {
			if (proLicense == null || proLicense.getId() == null) {
				return null;
			}
			ProductionLicenseInfo orig_proLicense = productionLicenseService.findById(proLicense.getId());
			Set<Resource> qsAttachments = proLicense.getQsAttachments();
			/* 1.获取已删除的[生产许可证]资源列表 */
			Set<Resource> removes = getListOfRemoves(
					orig_proLicense.getQsAttachments(), qsAttachments);
			/* 2.对图片集合进行循环 */
			Set<Resource> adds = operationResources(qsAttachments, "qs", null);
			/* 3.保存资源 */
			if (!CollectionUtils.isEmpty(removes)) {
				orig_proLicense.removeQsResources(removes);
				for (Resource resource : removes) {
					long count = productionLicenseService.getDAO()
							.countRelationshipByResIdAndQsNo(resource.getId(),
									proLicense.getId());
					if (count == 0) {
						delete(resource);
					}
				}
			}
			if (!CollectionUtils.isEmpty(adds)) {
				orig_proLicense.addQsResources(adds);
			}
			productionLicenseService.update(orig_proLicense);
			return orig_proLicense;
		} catch (DaoException dao) {
			throw new ServiceException(
					"[DaoException]MkTestResourceServiceImpl.saveQsResource()-->"
							+ dao.getMessage(), dao.getException());
		} catch (ServiceException sex) {
			throw new ServiceException(
					"[ServiceException]MkTestResourceServiceImpl.saveQsResource()-->"
							+ sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 对比前后的资源列表，获取删除的集合
	 */
	@Override
	public Set<Resource> getListOfRemoves(Collection<Resource> origAttachments,
			Collection<Resource> nowAttachments) {
		if (origAttachments == null) {
			return null;
		}
		Set<Resource> removes = new HashSet<Resource>();
		List<Long> currentId = new ArrayList<Long>();
		for (Resource resource : nowAttachments) {
			Long id = resource.getId();
			if (id != null) {
				currentId.add(id);
			}
		}
		for (Resource resource : origAttachments) {
			if (resource.getId() != null
					&& !currentId.contains(resource.getId())) {
				removes.add(resource);
			}
		}
		return removes;
	}
	
	/**
	 * 获取新增resource的集合
	 * @author ZhangHui 2015/6/3
	 */
	@Override
	public Set<Resource> getListOfAdds(Collection<Resource> nowAttachments) {
		if (nowAttachments == null) {
			return null;
		}
		
		Set<Resource> adds = new HashSet<Resource>();
		for (Resource resource : nowAttachments) {
			if (resource.getFile() != null) {
				adds.add(resource);
			}
		}
		
		return adds;
	}

	/**
	 * 通过excel文件批量导入检测项目
	 */
	@Override
	public List<TestProperty> paseExcelByResource(Resource resource)
			throws ServiceException {
		if (resource == null) {
			return null;
		}
		List<TestProperty> items = null;
		try {
			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("iso-8859-1");
			Workbook wb = Workbook.getWorkbook(new ByteArrayInputStream(
					resource.getFile()), ws);
			Sheet sheet = wb.getSheet(0);
			items = new ArrayList<TestProperty>();
			if (sheet.getRows() < 1) {
				return null;
			}
			for (int i = 1; i < sheet.getRows(); i++) {
				if (sheet.getCell(0, i).getContents().trim().length() < 1
						|| sheet.getCell(3, i).getContents().trim().length() < 1) {
					continue;
				}
				TestProperty itm = new TestProperty();
				itm.setName(sheet.getCell(0, i).getContents().trim());
				itm.setUnit(sheet.getCell(1, i).getContents().trim());
				itm.setTechIndicator(sheet.getCell(2, i).getContents().trim());
				itm.setResult(sheet.getCell(3, i).getContents().trim());
				itm.setAssessment("合格");
				if (sheet.getCell(4, i).getContents().trim().contains("不")) {
					itm.setAssessment("不合格");
				}
				itm.setStandard(sheet.getCell(5, i).getContents().trim());
				items.add(itm);
			}
			if (wb != null) {
				wb.close();
			}
		} catch (Exception e) {
			throw new ServiceException(
					"MkTestResourceServiceImpl.paseExcelByResource() "
							+ e.getMessage(), e);
		}
		return items;
	}

	/**
	 * 保存营业执照图片或组织机构图片
	 * 
	 * @param fieldValue
	 * @return Set<MkTestResource>
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public LiutongFieldValue saveResouce(LiutongFieldValue fieldValue)
			throws ServiceException {
		try {
			if (fieldValue == null || fieldValue.getId() == null) {
				return null;
			}
			/* 1.判定当前type类型 */
			String type = "";
			if (fieldValue.getDisplay().equals("组织机构代码")) {
				type = "org";
			} else if (fieldValue.getDisplay().equals("营业执照号")) {
				type = "license";
			} else {
				return null;
			}
			LiutongFieldValue orig_fieldValue = null;
			orig_fieldValue = liutongFieldValueService.findById(fieldValue
					.getId());
			/* 2.获取已删除的资源列表 */
			Set<Resource> removes = getListOfRemoves(
					orig_fieldValue != null ? orig_fieldValue.getAttachments()
							: null, fieldValue.getAttachments());
			/* 3.对页面新增的图片上传至ftp */
			Set<Resource> adds = operationResources(
					fieldValue.getAttachments(), type, null);
			/* 3.更新资源 */
			if (!CollectionUtils.isEmpty(removes)) {
				orig_fieldValue.removeResources(removes);
				for (Resource resource : removes) {
					delete(resource);
				}
			}
			if (!CollectionUtils.isEmpty(adds)) {
				orig_fieldValue.addResources(adds);
			}
			setFieldValue(orig_fieldValue, fieldValue);
			liutongFieldValueService.update(orig_fieldValue);
			return orig_fieldValue;
		} catch (ServiceException sex) {
			throw new ServiceException(
					"MkTestResourceServiceImpl.saveResouce()-->"
							+ sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 更新前的复制操作
	 * 
	 * @param orig_fieldValue
	 * @param fieldValue
	 * @return void
	 * @author ZhangHui
	 */
	private void setFieldValue(LiutongFieldValue orig_fieldValue,
			LiutongFieldValue fileValue) {
		orig_fieldValue.setFullFlag(fileValue.isFullFlag());
		orig_fieldValue.setMsg(fileValue.getMsg());
		orig_fieldValue.setValue(fileValue.getValue());
		orig_fieldValue.setPassFlag(fileValue.getPassFlag());
	}

	/**
	 * 将多个图片合成pdf 返回一个 MkTestResource 对象。
	 * @param pictures
	 * @param reportNo
	 * @param testType
	 * @return Resource
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	public Resource getPdfByPcitures(List<Resource> pictures, String reportNo,
			String testType) throws ServiceException {
		try {
			if (pictures == null || pictures.size() < 1){return null;}
			if (reportNo == null || reportNo.equals("")) {
				reportNo = "" + System.currentTimeMillis();
			}
			Resource result = new Resource();
			String reportNOEng = MKReportNOUtils.convertCharacter(reportNo, null);
			String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
			String uploadFileName = reportNOEng + "-" + randomStr + "-report.pdf";
			UploadUtil uploadUtil = new UploadUtil();
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_REPORT_PATH) + "/" +reportNOEng;
			Map<String, String> map = uploadUtil.uploadReportPdf(pictures,
					ftpPath, uploadFileName, reportNOEng);
			result.setFileName(reportNo + ".pdf");
			result.setName(reportNo + ".pdf");
			result.setUrl(map.get("fullPdfPath"));
			result.setInterceptionPdfPath(map.get("interceptionPdfPath"));
			result.setType(resourceTypeService.findById(3L));
			return result;
		} catch (Exception e) {
			throw new ServiceException("MkTestResourceServiceImpl.getPdfByPcitures() " + e.getMessage(), e);
		}
	}

	@Override
	public MkTestResourceDAO getDAO() {
		return testResourceDAO;
	}
	/**
	 *  根据图片地址和条形码保存图片
	 * @author zhawaneng
	 *  2015-03-06
	 * @param productImg
	 * @param barcode
	 * @return
	 */
	@Override
	public Product setImgToProduct(String productImg,String barcode) {
		Product product = new Product();
		String[] split = productImg.split("\\|");
		Set<Resource> resources = new HashSet<Resource>();
		String randomStr = "";
		String fileName = "";
		String imgUrls = "http://qa.fsnrec.com/portal/img/product/temp/temp.jpg";
		String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PRODUCT_PATH) + "/" + barcode;
		for (String imgurl : split) {
			
			if(imgurl==null||"".equals(imgurl)||"null".equals(imgurl))
			{
			  continue;
			}
			
			Resource resource = new Resource();
			randomStr = sdf.format(new Date())+ (new Random().nextInt(1000) + 1);
			/* 获取图片的格式  */
			String imgType = FileUtils.getExtension(imgurl);
			fileName = barcode + randomStr + "."+ imgType;
			UploadUtil uploadUtil = new UploadUtil();
			/* 图片的上传路径  */
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_PRODUCT_PATH)+"/"+barcode;
			/* 下载图片得到图片的InputStream  */
			InputStream downloadFileStream = uploadUtil.downloadFileStream(imgurl);
			/* 通过InputStream 上传图片 */
			boolean success_upload = uploadUtil.uploadFile(downloadFileStream, ftpPath, fileName);
			/* 上传图片成功后，保存图片上传的地址 */
			if(success_upload){
				try {
					if(UploadUtil.IsOss()){
						resource.setUrl(uploadUtil.getOssSignUrl(ftpPath + "/" + fileName));
					}else{
						resource.setUrl(webUrl + "/" + fileName);
					}
					resource.setFileName(fileName);
					resource.setName(fileName);
					ResourceType type =resourceTypeService.findByName(imgType); 
					resource.setType(type);
					create(resource);
				} catch (Exception e) {
					e.printStackTrace();
				}
				resources.add(resource);
			}
			imgUrls += webUrl + "/" + fileName + "|";
		}
		if (!CollectionUtils.isEmpty(resources)) {
			product.addResources(resources);
		}
		if(StringUtils.isNotEmpty(imgUrls)){
			product.setImgUrl(imgUrls);
		}
		return product;
		
	}

	/**
	 * 存存业业传传照片片
	 * @author HY
	 */
	@Override
	public void savePropagandaResource(Resource resource, String name) throws ServiceException {
		String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_LOGO_PATH);
		String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_LOGO_PATH);
		/* 1.图片已存在 */
		if (resource.getFile() == null && resource.getName() != null && resource.getUrl() != null) {
			return;
		}else if (resource.getFile() != null) { /* 3.图片是页面新增图片 */
			String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
			String urlFileName = randomStr + "." + resource.getType().getRtDesc();
			UploadUtil uploadUtil = new UploadUtil();
			boolean success_upload = uploadUtil.uploadFile(resource.getFile(), ftpPath, urlFileName);
			if(success_upload){
				if(UploadUtil.IsOss()){
					resource.setUrl(uploadUtil.getOssSignUrl(ftpPath + "/" + urlFileName));
				}else{
					resource.setUrl(webUrl + "/" + urlFileName);
				}
				resource.setFileName(resource.getFileName());
				resource.setName(resource.getFileName());
			}
		}
	}
	
	/**
	 * 保存进口食品中文标签图片资源
	 * @author longxianzhen 2015/05/27
	 */
	@Override
	public void saveLabResource(ImportedProduct impProduct)
			throws ServiceException {
		try {
			Set<Resource> labAttachments = impProduct.getLabelAttachments();
			Product pro=productService.findById(impProduct.getProductId());
			Set<Resource> adds = new HashSet<Resource>();
			String randomStr = "";
			String fileName = "";
			//String imgUrls = "";
			/* 2.对中文标签图片集合进行循环：（根据resource的id是否为null，来判断该图片是否是新增图片） */
			for (Resource resource : labAttachments) {
				randomStr = sdf.format(new Date())
						+ (new Random().nextInt(1000) + 1);
				fileName = pro.getBarcode() + randomStr + "."
						+ resource.getType().getRtDesc();
				if (resource.getId() != null) {
					continue;
				}
					/* 2.3 中文标签图片是页面新增图片 */
					String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_PRODUCT_PATH) + "/" + pro.getBarcode()+"/lab";
					UploadUtil uploadUtil = new UploadUtil();
					boolean success_upload = uploadUtil.uploadFile(resource.getFile(), ftpPath, fileName);
					if(success_upload){
						String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PRODUCT_PATH) + "/" + pro.getBarcode()+"/lab";
						if(UploadUtil.IsOss()){
							resource.setUrl(uploadUtil.getOssSignUrl(ftpPath + "/" + fileName));
						}else{
							resource.setUrl(webUrl + "/" + fileName);
						}
						//resource.setFileName(fileName);
						//resource.setName(fileName);
						create(resource);
						adds.add(resource);
					}
			}
			/* 3.更新进口产品信息 */
			if (impProduct.getId() != null) {
				ImportedProduct origImpProduct = importedProductService.getDAO().findById(
						impProduct.getId());
				/* 1.获取删除的中文标签图片列表 */
				Set<Resource> removes = null;
				if (origImpProduct != null) {
					removes = getListOfRemoves(origImpProduct.getLabelAttachments(),
							labAttachments);
				}
				/* 3.1 删除中文标签图片与进口产品的关系 */
				if (!CollectionUtils.isEmpty(removes)) {
					origImpProduct.removeResources(removes);
					for (Resource resource : removes) {
						long count = getDAO().getRelationCountByResourceId(
								resource.getId());
						if (count == 1) {
							delete(resource);
						}
					}
				}
				/* 3.2 增加中文标签图片与进口产品的关系 */
				if (!CollectionUtils.isEmpty(adds)) {
					origImpProduct.addResources(adds);
				}
	
				importedProductService.update(origImpProduct);
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("删除资源，出现异常", e);
		}
	}

	/**
	 * 保存进口食品卫生证书图片资源
	 * @author longxianzhen 2015/05/27
	 */
	@Override
	public void saveSanResource(ImportedProductTestResult impProductTestResult)
			throws ServiceException {
		try {
			Set<Resource> sanAttachments = impProductTestResult.getSanitaryAttachments();
			Set<Resource> adds = new HashSet<Resource>();
			String randomStr = "";
			String fileName = "";
			//String imgUrls = "";
			/* 2.对进口食品卫生证书图片集合进行循环：（根据resource的id是否为null，来判断该图片是否是新增图片） */
			for (Resource resource : sanAttachments) {
				randomStr = sdf.format(new Date())
						+ (new Random().nextInt(1000) + 1);
				fileName = impProductTestResult.getSanitaryCertNo() + randomStr + "."
						+ resource.getType().getRtDesc();
				if (resource.getId() != null) {
					continue;
				}
					/* 2.3 进口食品卫生证书图片是页面新增图片 */
					String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_REPORT_PATH) + "/" + impProductTestResult.getSanitaryCertNo();
					UploadUtil uploadUtil = new UploadUtil();
					boolean success_upload = uploadUtil.uploadFile(resource.getFile(), ftpPath, fileName);
					if(success_upload){
						if(UploadUtil.IsOss()){
							resource.setUrl(uploadUtil.getOssSignUrl(ftpPath + "/" + fileName));
						}else{
							String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_REPORT_PATH) + "/" + impProductTestResult.getSanitaryCertNo();
							resource.setUrl(webUrl + "/" + fileName);
						}
						//resource.setFileName(fileName);
						//resource.setName(fileName);
						create(resource);
						adds.add(resource);
					}
			}
			/* 3.更新进口产品报告信息 */
			if (impProductTestResult.getId() != null) {
				ImportedProductTestResult origImpProTset = importedProductTestResultService.getDAO().findById(
						impProductTestResult.getId());
				/* 1.获取删除的进口食品卫生证书图片列表 */
				Set<Resource> removes = null;
				if (origImpProTset != null) {
					removes = getListOfRemoves(origImpProTset.getSanitaryAttachments(),
							sanAttachments);
				}
				/* 3.1 删除进口食品卫生证书图片与进口产品报告的关系 */
				if (!CollectionUtils.isEmpty(removes)) {
					origImpProTset.removeResources(removes);
					for (Resource resource : removes) {
						long count = getDAO().getRelationCountByResourceId(
								resource.getId());
						if (count == 1) {
							delete(resource);
						}
					}
				}
				/* 3.2 增加进口食品卫生证书图片与进口产品报告的关系 */
				if (!CollectionUtils.isEmpty(adds)) {
					origImpProTset.addResources(adds);
				}
	
				importedProductTestResultService.update(origImpProTset);
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("删除资源，出现异常", e);
		}
	}
	
	/**
	 * 保存进口食品卫生证书PDF资源
	 * @author longxianzhen 2015/05/27
	 */
	@Override
	public void saveSanPdfResource(ImportedProductTestResult impProductTestResult)
			throws ServiceException {
		try {
			Set<Resource> sanAttachments = impProductTestResult.getSanitaryPdfAttachments();
			Set<Resource> adds = new HashSet<Resource>();
			/* 2.对进口食品卫生证书图片集合进行循环：（根据resource的id是否为null，来判断该图片是否是新增图片） */
			for (Resource resource : sanAttachments) {
				if (resource.getId() != null) {
					continue;
				}else{
					adds.add(resource);
				}
			}
			/* 3.更新进口产品报告信息 */
			if (impProductTestResult.getId() != null) {
				ImportedProductTestResult origImpProTset = importedProductTestResultService.getDAO().findById(
						impProductTestResult.getId());
				/* 1.获取删除的进口食品卫生证书图片列表 */
				Set<Resource> removes = null;
				if (origImpProTset != null) {
					removes = getListOfRemoves(origImpProTset.getSanitaryPdfAttachments(),
							sanAttachments);
				}
				/* 3.1 删除进口食品卫生证书图片与进口产品报告的关系 */
				if (!CollectionUtils.isEmpty(removes)) {
					origImpProTset.removePdfResources(removes);
					for (Resource resource : removes) {
						long count = getDAO().getRelationCountByResourceId(
								resource.getId());
						if (count == 1) {
							delete(resource);
						}
					}
				}
				/* 3.2 增加进口食品卫生证书图片与进口产品报告的关系 */
				if (!CollectionUtils.isEmpty(adds)) {
					origImpProTset.addPdfResources(adds);
				}
	
				importedProductTestResultService.update(origImpProTset);
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("删除资源，出现异常", e);
		}
	}

	/**
	 * 根据产品id查找产品图片集合按上传时间排序
	 * @author longxianzhen 2015/06/10
	 */
	@Override
	public List<Resource> getProductImgListByproId(Long proId)
			throws ServiceException {
		try {
			return testResourceDAO.getProductImgListByproId(proId);
		}catch (DaoException e) {
			throw new ServiceException(e.getMessage()+"删除资源，出现异常", e.getException());
		}
	}
	
	public List<Resource> getRebackImgListByreportId(Long reportId)
			throws ServiceException {
		try {
			return testResourceDAO.getRebackImgListByreportId(reportId);
		}catch (DaoException e) {
			throw new ServiceException(e.getMessage()+"删除资源，出现异常", e.getException());
		}
	}

	/**
	 * 根据企业id获取企业的证照图片地址（营业执照，组织机构代码证，流通许可证）
	 * @author longxianzhen 2015/08/03
	 */
	@Override
	public Map<String, String> getBusinessUnitCertById(Long buId) throws ServiceException {
		try {
			return testResourceDAO.getBusinessUnitCertById(buId);
		}catch (DaoException e) {
			throw new ServiceException(e.getMessage()+"根据企业id获取企业的证照图片地址", e.getException());
		}
	}

	/**
	 * 根据qs号id查询qs图片资源
	 * @author longxianzhen 2015/08/06
	 */
	@Override
	public List<Resource> getQsResourceByQsId(Long qsId)
			throws ServiceException {
		try {
			return testResourceDAO.getQsResourceByQsId(qsId);
		}catch (DaoException e) {
			throw new ServiceException(e.getMessage()+"", e.getException());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void savePiceFile(WasteDisposa wasteDisposa)
			throws ServiceException {
		try {
			Set<Resource> piceFiles = wasteDisposa.getPiceFile();
			Set<Resource> adds = new HashSet<Resource>();
			String randomStr = "";
			String fileName = "";
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WASTE_PATH);
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_WASTE_PATH);
			UploadUtil uploadUtil = new UploadUtil();
			for (Resource resource : piceFiles) {
				randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
				if (resource.getId() != null) {
					resource = findById(resource.getId());
					continue;
				}
				if (resource.getId() == null && resource.getFile() != null) {
					fileName = randomStr + "." + resource.getType().getRtDesc();
					boolean success_upload = uploadUtil.uploadFile(resource.getFile(), ftpPath, fileName);
					if(success_upload){
						if(UploadUtil.IsOss()){
							resource.setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+fileName));
						}else{
							resource.setUrl(webUrl + "/" + fileName);
						}
						resource.setFileName(fileName);
						resource.setName(fileName);
						create(resource);
						adds.add(resource);
					}
				}
			}

			/* 在编辑时：需要从数据库中删除在页面已经被删除的图片资源 */
			if (wasteDisposa.getId() != null) {
				WasteDisposa origwasteDisposa = wasteDisposaService.findById(wasteDisposa.getId());
				Set<Resource> removes = getListOfRemoves(origwasteDisposa.getPiceFile(), piceFiles);
				if (!CollectionUtils.isEmpty(removes)) {
					origwasteDisposa.removeResources(removes);
					for (Resource resource : removes) {
						delete(resource);
					}
				}
				if (!CollectionUtils.isEmpty(adds)) {
					origwasteDisposa.addResources(adds);
				}
				wasteDisposaService.update(origwasteDisposa);
			}
		} catch (ServiceException ioe) {
			throw new ServiceException("MkTestResourceServiceImpl.saveBusinessBrandResources()-->" + ioe.getMessage(), ioe);
		}
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveDishsnoFile(DishsNo dishsNo)
			throws ServiceException {
		try {
			Set<Resource> dishsnoFile = dishsNo.getDishsnoFile();
			Set<Resource> adds = new HashSet<Resource>();
			String randomStr = "";
			String fileName = "";
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_DISHSNO_PATH);
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_DISHSNO_PATH);
			UploadUtil uploadUtil = new UploadUtil();
			for (Resource resource : dishsnoFile) {
				randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
				if (resource.getId() != null) {
					resource = findById(resource.getId());
					continue;
				}
				if (resource.getId() == null && resource.getFile() != null) {
					fileName = randomStr + "." + resource.getType().getRtDesc();
					boolean success_upload = uploadUtil.uploadFile(resource.getFile(), ftpPath, fileName);
					if(success_upload){
						if(UploadUtil.IsOss()){
							resource.setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+fileName));
						}else{
							resource.setUrl(webUrl + "/" + fileName);
						}
						resource.setFileName(fileName);
						resource.setName(fileName);
						create(resource);
						adds.add(resource);
					}
				}
			}

			/* 在编辑时：需要从数据库中删除在页面已经被删除的图片资源 */
			if (dishsNo.getId() != null) {
				DishsNo origndishsNo = dishsNoService.findById(dishsNo.getId());
				Set<Resource> removes = getListOfRemoves(origndishsNo.getDishsnoFile(),dishsnoFile);
				if (!CollectionUtils.isEmpty(removes)) {
					origndishsNo.removeResources(removes);
					for (Resource resource : removes) {
						delete(resource);
					}
				}
				if (!CollectionUtils.isEmpty(adds)) {
					origndishsNo.addResources(adds);
				}
				dishsNoService.update(origndishsNo);
			}
		} catch (ServiceException ioe) {
			throw new ServiceException("MkTestResourceServiceImpl.saveBusinessBrandResources()-->" + ioe.getMessage(), ioe);
		}
	}
	/**
	 * 保存组织机构代码证件
	 * 
	 * @param orgAttachments
	 * @param businessUnit
	 * @return void
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Set<Resource> saverecResource(Set<Resource> recAttachments, ProductDestroyRecord productDestroyRecord) throws ServiceException {
		try {
			if (recAttachments == null || recAttachments.size() < 1) {
				return null;
			}
			Set<Resource> removes = getListOfRemoves(productDestroyRecord == null ? null : productDestroyRecord.getRecAttachments(),recAttachments);
			/* 2.对图片集合进行循环 */
			Set<Resource> adds = operationResources(recAttachments, "record", null);
			/* 3.保存资源 */
			if (!CollectionUtils.isEmpty(removes)) {
				productDestroyRecord.removerecResources(removes);
				for (Resource resource : removes) {
	//				BigInteger resId = BigInteger.valueOf(resource.getId());
					delete(resource);
				}
			}
			if (!CollectionUtils.isEmpty(adds)) {
			//	productDestroyRecord.addrecResources(adds);
				recAttachments= adds;
			}
		//	productDestroyRecordService.update(productDestroyRecord);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("删除资源，出现异常", e);
		}
		return recAttachments;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteResourceByResultId(long resultId){
		this.getDAO().deleteResourceByResultId(resultId);
	}

}