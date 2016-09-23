package com.gettec.fsnip.fsn.service.market;

import com.gettec.fsnip.fsn.dao.market.MkTestResourceDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.*;
import com.gettec.fsnip.fsn.model.dishs.DishsNo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductDestroyRecord;
import com.gettec.fsnip.fsn.model.test.ImportedProductTestResult;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.waste.WasteDisposa;
import com.gettec.fsnip.fsn.service.common.BaseService;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ResourceService extends BaseService<Resource, MkTestResourceDAO>{

	void saveProResource(Product product) throws ServiceException;

	boolean savePdfResource(TestResult report) throws ServiceException;

	Resource saveAutoPdfResource(InputStream input, TestResult report) throws ServiceException;

	void saveLicAndOrgResource(EnterpriseRegiste enRegiste)throws ServiceException;
	

	void saveCertificationResources(BusinessUnit business) throws ServiceException;

	void saveBusinessBrandResources(BusinessBrand businessBrand) throws ServiceException;

	void updateResourceUrl(Long resId, String pdfUrl) throws ServiceException;

	void saveOrgnizationResource(Set<Resource> orgAttachments, BusinessUnit name) throws ServiceException;
	
	Set<Resource> saverecResource(Set<Resource> recAttachments, ProductDestroyRecord productDestroyRecord) throws ServiceException;
	
	void saveLicenseResource(Set<Resource> licAttachments, String name) throws ServiceException;

	void saveDisResource(Set<Resource> disAttachments, String name) throws ServiceException;

	void saveQsResource(Set<Resource> qsAttachments, String name) throws ServiceException;

	void saveLogoResource(Set<Resource> LogoAttachments, String name) throws ServiceException;
	
	void savePiceFile(WasteDisposa wasteDisposa)throws ServiceException;
	
	void saveDishsnoFile(DishsNo dishsNo)throws ServiceException;
	
	Long countBusPdfByBusId(Long id)throws ServiceException;
	
	List<Resource> getListBusPdfWithPage(Long busId,int page,int pageSize)throws ServiceException;

	void saveTaxRegResource(Set<Resource> taxRegAttachments, String name)
			throws ServiceException;

	void saveLiquorResource(Set<Resource> liquorAttachments, String name)
			throws ServiceException;

	void saveProDepResource(Set<Resource> proDepAttachments, Long proDepId)
			throws ServiceException;

	List<Resource> addResourcesKendoUI(
			Collection<Resource> resources) throws ServiceException;
	
	List<TestProperty> paseExcelByResource(Resource resource) throws ServiceException;
	
	Set<Resource> getListOfRemoves(
			Collection<Resource> origAttachments,
			Collection<Resource> nowAttachments);
	
	Resource getPdfByPcitures(List<Resource> pictures, 
			String reportNo, String testType) throws ServiceException;

	LiutongFieldValue saveResouce(LiutongFieldValue fieldValue) throws ServiceException;

	ProductionLicenseInfo saveQsResource(ProductionLicenseInfo productionLicense)
			throws ServiceException;
	Product setImgToProduct(String productImg,String barcode) throws ServiceException;

	void savePropagandaResource(Resource resource, String name)throws ServiceException;

	/**
	 * 保存进口食品中文标签图片资源
	 * @author longxianzhen 2015/05/27
	 */
	void saveLabResource(ImportedProduct impProduct)throws ServiceException;

	/**
	 * 保存进口食品卫生证书图片资源
	 * @author longxianzhen 2015/05/27
	 */
	void saveSanResource(ImportedProductTestResult impProductTestResult)throws ServiceException;

	/**
	 * 保存进口食品卫生证书PDF资源
	 * @author longxianzhen 2015/05/27
	 */
	void saveSanPdfResource(ImportedProductTestResult impProductTestResult)
			throws ServiceException;

	/**
	 * 获取新增resource的集合
	 * @author ZhangHui 2015/6/3
	 */
	public Set<Resource> getListOfAdds(Collection<Resource> nowAttachments);

	/**
	 * 根据产品id查找产品图片集合按上传时间排序
	 * @author longxianzhen 2015/06/10
	 */
	List<Resource> getProductImgListByproId(Long proId)throws ServiceException;
	
	List<Resource> getRebackImgListByreportId(Long reportId) throws ServiceException;

	/**
	 * 根据企业id获取企业的证照图片地址（营业执照，组织机构代码证，流通许可证）
	 * @author longxianzhen 2015/08/03
	 */
	Map<String, String> getBusinessUnitCertById(Long buId)throws ServiceException;

	/**
	 * 根据qs号id查询qs图片资源
	 * @author longxianzhen 2015/08/06
	 */
	List<Resource> getQsResourceByQsId(Long qsId)throws ServiceException;
	
	void deleteResourceByResultId(long resultId);

	void saveBusinessCert(BusinessUnit businessUnit)throws ServiceException;

}