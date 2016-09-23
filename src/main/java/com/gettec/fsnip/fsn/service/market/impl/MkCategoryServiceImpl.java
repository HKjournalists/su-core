package com.gettec.fsnip.fsn.service.market.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.market.MkCategoryDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.service.market.MkCategoryService;
import com.gettec.fsnip.fsn.vo.product.ProductCategoryVO;

@Service(value="categoryService")
public class MkCategoryServiceImpl implements MkCategoryService{
	@Autowired MkCategoryDAO categoryDAO;
	
	/**
	 * 获取产品类型树形结构末级类型列表，
	 * 用于展示在页面下拉列表中，供用户选择。
	 * @param order 二级分类的code的位数
	 * @param uporder 一级分类的code
	 * @throws ServiceException 
	 * @author 郝圆彬
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<ProductCategory> getListOfCategory(Long level,String parentCode) throws ServiceException{
		try {
			return categoryDAO.getListOfCategory(level,parentCode);
		} catch (DaoException daoe) {
			throw new ServiceException("MkCategoryServiceImpl-->getListOfCategory"+daoe.getMessage(),daoe);
		}
	}

	/**
	 * 获取一级分类、二级分类
	 * @author ZhangHui 2015/4/27
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<ProductCategoryVO> getListOfCategoryVO(int level, String parentCode) throws ServiceException{
		try {
			return categoryDAO.getListOfCategory(level, parentCode);
		} catch (DaoException e) {
			throw new ServiceException("MkCategoryServiceImpl.getListOfCategory()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 报告保存之前，校验产品类型是否为用户自定义。若是用户自定义的，则提醒用户应从已有的列表中选择。
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ProductCategory findCategoryByCode(String category) throws ServiceException {
		try {
			return categoryDAO.findByCode(category);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 通过产品类别名称来获取产品列表实体
	 * @throws ServiceException
	 */
	@Override
	public ProductCategory getCategoryByName(String categoryName) throws ServiceException {
		try{
			return categoryDAO.getCategoryByName(categoryName);
		}catch(DaoException doe){
			throw new ServiceException("【service-error】通过产品类别名称来获取产品列表实体时出现异常！",doe.getException());
		}
	}

	//加载产品分类的第一大类
	//HuangYog
    @Override
    public List<ProductCategory> loadProduCtcategory() throws ServiceException {
        try {
        return categoryDAO.loadProduCtcategory();
        } catch (DaoException doe) {
            throw new ServiceException("【service-error】加载产品分类的第一大类时出现异常！",doe.getException());
        }
    }
	
	/**
	 * 通过ID查找分类信息 一般用于二级分类
	 * @throws ServiceException
	 * @author 郝圆彬
	 */
	@Override
	public ProductCategory findById(Long id) throws ServiceException {
		try{
			return categoryDAO.findById(id);
		}catch(JPAException jpae){
			throw new ServiceException("MkCategoryServiceImpl-->findById！"+jpae.getMessage(),jpae);
		}
	}
	
}