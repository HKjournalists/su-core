package com.gettec.fsnip.fsn.service.product.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.gettec.fsnip.fsn.dao.product.ProductNutriDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Nutrition;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.ProductNutriService;
import com.gettec.fsnip.fsn.service.product.ProductStdNutriService;

@Service(value="productNutriService")
public class ProductNutriServiceImpl extends BaseServiceImpl<ProductNutrition, ProductNutriDAO> 
		implements ProductNutriService{

	@Autowired protected ProductNutriDAO productNutriDAO;
	@Autowired protected ProductStdNutriService productStdNutriService;
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ProductNutriDAO getDAO() {
		return productNutriDAO;
	}

	/**
	 * 按产品id查找营养报告列表
	 * @throws ServiceException 
	 */
	@Override
	public List<ProductNutrition> getListOfNutrisByProductId(Long productId) throws ServiceException {
		try {
			return productNutriDAO.getListOfNutrisByProductId(productId);
		} catch (DaoException dex) {
			throw new ServiceException("【service-error】按产品id查找营养报告列表，出现异常！", dex.getException());
		}
	}

	/**
	 * 保存产品的营养列表
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(Product product) throws ServiceException {
		try {
			/* 1. 处理新增项   */
			List<ProductNutrition> nowNutritions = product.getListOfNutrition();
			for(ProductNutrition nutrition : nowNutritions){
				if(nutrition.getId() != null){
					ProductNutrition orig_nutrition = productNutriDAO.findById(nutrition.getId());
					Nutrition orig_stgNutrition = productStdNutriService.findByName(nutrition.getName());
					orig_nutrition.setNutrition(orig_stgNutrition);
					orig_nutrition.setName(nutrition.getName());
					orig_nutrition.setNrv(nutrition.getNrv());
					orig_nutrition.setPer(nutrition.getPer());
					orig_nutrition.setUnit(nutrition.getUnit());
					orig_nutrition.setValue(nutrition.getValue());
					update(orig_nutrition);
				}else{
					Nutrition orig_stgNutrition = productStdNutriService.findByName(nutrition.getName());
					nutrition.setNutrition(orig_stgNutrition);
					nutrition.setProductId(product.getId());
					create(nutrition);
				}
			}
			
			/* 2. 处理删除项   */
			List<ProductNutrition> origNutritions = productNutriDAO.getListOfNutrisByProductId(product.getId());
			Set<ProductNutrition> removes = getRemoves(origNutritions, nowNutritions);
			if(!CollectionUtils.isEmpty(removes)){
				for(ProductNutrition nutrition : removes){
					productNutriDAO.remove(productNutriDAO.findById(nutrition.getId()));
				}
			}
		} catch (JPAException jpae) {
			throw new ServiceException("JPAException error", jpae.getException());
		} catch (DaoException dex) {
			throw new ServiceException("DaoException error", dex.getException());
		}
	}
	
	/**
	 * 获取删除的检测项目
	 * @param origItems 原来的检测项目列表
	 * @param nowItems 现在的检测项目列表
	 * @return
	 */
	private Set<ProductNutrition> getRemoves(List<ProductNutrition> origNutritions, List<ProductNutrition> nowNutritions) {
		Set<ProductNutrition> removes = new HashSet<ProductNutrition>();
		List<Long> currentId = new ArrayList<Long>();
		for(ProductNutrition nutrition : nowNutritions){
			if(nutrition.getId() != null){
				currentId.add(nutrition.getId());
			}
		}
		for(ProductNutrition nutrition : origNutritions){
			if(!currentId.contains(nutrition.getId()) && nutrition.getId()!=null){
				removes.add(nutrition);
			}
		}
		return removes;
	}
	
	/**
     * 根据点击的列号得到这一列对应的数据
     * @param colName
     * @return List
     * @throws ServiceException
	 * @throws DaoException 
     */
    @Override
    public List<String> autoNutritionItems(int colNameId) throws ServiceException, DaoException {
        try {
            return productNutriDAO.getListOfColumeValue(colNameId);
        } catch (DaoException sex) {
            throw sex;
        }
    }
}
