package com.gettec.fsnip.fsn.service.product.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.ProductCategoryInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.MkCategoryService;
import com.gettec.fsnip.fsn.service.product.ProductCategoryInfoService;
import com.gettec.fsnip.fsn.vo.product.ProductCategoryVO;

/**
 * Product service implementation
 * @author  ZhaWanNeng
 */
@Service(value="productCategoryInfoService")
public class ProductCategoryInfoServiceImpl extends BaseServiceImpl<ProductCategoryInfo, ProductCategoryInfoDAO> 
		implements ProductCategoryInfoService{
	@Autowired private ProductCategoryInfoDAO productCategoryInfoDAO;
	@Autowired private MkCategoryService categoryService;
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ProductCategoryInfoDAO getDAO() {
		return productCategoryInfoDAO;
	}
	
	/**
	 * 将执行标准字符串转换为Set集合
	 * @return
	 * @author 郝圆彬
	 */
	 public Set<ProductCategoryInfo> getRegularityByString(String regularityStr, ProductCategory category2)throws ServiceException{
		if(regularityStr==null||regularityStr.equals("")){ return null; }
		try{
			Set<ProductCategoryInfo> regularitys = new HashSet<ProductCategoryInfo>();
			String[] regularity = regularityStr.split(";");
			for(int i=0;i<regularity.length;i++){
				if(!regularity[i].equals("")){
					ProductCategoryInfo reg = new ProductCategoryInfo();
					reg.setName(regularity[i]);
					reg.setCategory(category2);
					ProductCategoryInfo orig_regularity = saveCategoryInfo(reg);
					regularitys.add(orig_regularity);
				}
			}
			return regularitys;
		}catch (Exception e) {
			throw new ServiceException("TestReportServiceImpl.countBySampleId()-->" + e.getMessage(), e);
		}
	}
	 
	/**
	 * 根据二级分类ID查找二级分类下的所有的三级分类或者执行标准
	 * @param upId 二级分类ID
	 * @param categoryFlag true：查找三级三类，false：查找执行标准
	 * @author 郝圆彬
	 */
	@Override
	public List<ProductCategoryInfo> getListOfUpId(Long upId,boolean categoryFlag)
			throws ServiceException {
		try{
			return productCategoryInfoDAO.getListOfUpId(upId,categoryFlag);
		}catch(Exception e){
			throw new ServiceException(e.getMessage(),e);
		}	
	}
	 
	 /**
	 * 获取三级分类
	 * @author ZhangHui 2015/4/27
	 */
	@Override
	public List<ProductCategoryVO> getListVOByParentcode(String parentcode, boolean iscategory) throws ServiceException {
		try{
			return productCategoryInfoDAO.getListByParentcode(parentcode, iscategory);
		}catch(DaoException e){
			throw new ServiceException("TestReportServiceImpl.getListByParentcode()-->" + e.getMessage(), e);
		}	
	}
	
	/**
	 * 新增三级分类或者执行标准，如果已经存在，则返回null
	 * @param categoryInfo 基本信息
	 * @author 郝圆彬
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ProductCategoryInfo saveCategoryInfo(ProductCategoryInfo categoryInfo)
			throws ServiceException {
		try{
			/*true:三级分类，false：执行标准*/
			boolean categoryFlag = categoryInfo.getCategoryFlag();
			//1、验证执行标准或者三级分类是否已经存在
			ProductCategoryInfo orig_categoryInfo = findByNameAndCategoryIdAndFlag(categoryInfo.getName(),categoryInfo.getCategory().getId(),categoryFlag);
			if(orig_categoryInfo!=null){
				if(orig_categoryInfo.isDel()){
					orig_categoryInfo.setDel(false);
					update(orig_categoryInfo);
				}
				return orig_categoryInfo;
			}
			//2、新增三级分类或者执行标准
			ProductCategoryInfo new_categoryInfo = new ProductCategoryInfo();
			new_categoryInfo.setName(categoryInfo.getName());
			if(!categoryFlag){
				new_categoryInfo.setDisplay(null);//执行标准display为null
			}else{
				new_categoryInfo.setDisplay(categoryInfo.getName());//三级分类display和name默认为一样
			}
			Long c_id = categoryInfo.getCategory().getId();
			ProductCategory categoryInfo2 = categoryService.findById(c_id);
			new_categoryInfo.setCategory(categoryInfo2);
			new_categoryInfo.setCategoryFlag(categoryFlag);
			new_categoryInfo.setAddition(true);
			create(new_categoryInfo);
			return new_categoryInfo;
		}catch(Exception daoe){
			throw new ServiceException(""+daoe.getMessage(),daoe);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ProductCategoryInfo findByNameAndCategoryIdAndFlag(String name, Long categoryId,boolean categoryFlag)
			throws ServiceException {
		try {
			return getDAO().findByNameAndCategoryIdAndFlag(name, categoryId, categoryFlag);
		} catch (DaoException e) {
			throw new ServiceException("ProductCategoryInfo.findByNameAndCategoryIdAndFlag()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 根据产品id获取产品执行标准
	 * @author longxianzhen 2015-08-06
	 */
	@Override
	public List<ProductCategoryInfo> getRegularityByProId(Long proId)
			throws ServiceException {
		try {
			return getDAO().getRegularityByProId(proId);
		} catch (DaoException e) {
			throw new ServiceException("ProductCategoryInfo.getRegularityByProId()-->" + e.getMessage(), e.getException());
		}
	}

	@Override
	public ProductCategoryInfo findByCategoryId(Long categoryId)
			throws ServiceException {
		try {
			return getDAO().findByCategoryId( categoryId);
		} catch (DaoException e) {
			throw new ServiceException("ProductCategoryInfo.findByCategoryId-->" + e.getMessage(), e.getException());
		}
		
	}
}