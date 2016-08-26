package com.gettec.fsnip.fsn.service.sample;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.SampleBusinessBrand;
import com.gettec.fsnip.fsn.model.product.SampleProduct;
import com.gettec.fsnip.fsn.vo.product.ProductVO;
import com.gettec.fsnip.fsn.vo.sampling.ExcelFileVO;
import com.gettec.fsnip.fsn.vo.sampling.SheetVO;


public interface ImportExcelService {
	
	public List<ExcelFileVO> getWorkbook(MultipartFile[] excelFiles);
	
	public void saveProduct(SheetVO sheetVO,BusinessUnit defaultBusinessUnit,SampleBusinessBrand defaultBusinessBrand);
	
	//取默认企业
	public BusinessUnit getDefaultBusinessUnit();
	
	//取默认商标
	public SampleBusinessBrand getDefaultSampleBusinessBrand(BusinessUnit defaultBusinessUnit);
	
	/**
	  * 取商标信息,有就返回没有保存后返回
	  * getBusinessBrand
	  * @param @param product  
	  * @param @param businessUnitName 企业对象
	  * @param @return
	  * @return BusinessBrand    返回类型
	  * @throws
	 */
   public SampleBusinessBrand saveOrGetSampleBusinessBrand(ProductVO product,BusinessUnit businessUnit);
   
   
   /**
     * 取企业和被抽样单位,有就返回没有保存后返回
     * saveOrGetBusinessUnit
     * @param @param product
     * @param @return
     * @return BusinessUnit    返回类型
     * @throws
    */
   public Map<String,BusinessUnit> saveOrGetBusinessUnit(ProductVO product);
   
   
   /**
     * 取产品信息,有就返回没有保存后返回
     * saveOrGetProduct
     * @param @param product
     * @param @param businessUnit
     * @param @return
     * @return Product    返回类型
     * @throws
    */
   public SampleProduct saveOrGetSampleProduct(ProductVO product, BusinessUnit businessUnit,SampleBusinessBrand defaultBusinessBrand);
		
	
}
