package com.gettec.fsnip.fsn.dao.market.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.market.MkCategoryDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.vo.product.ProductCategoryVO;

@Repository(value="categoryDAO")
public class MKCategoryDAOImpl extends BaseDAOImpl<ProductCategory> 
					implements MkCategoryDAO{
    
    @PersistenceContext private EntityManager entityManager;
    
    /**
	 * 按一级分类的code查找一级分类下的二级分类
	 * @param order 二级分类的code位数
	 * @param uporder 一级分类的code
	 * @throws DaoException 
	 * @author 郝圆彬
	 */
	@Override
	public List<ProductCategory> getListOfCategory(Long level,String parentCode) throws DaoException {
		try {
			String condition = " WHERE LENGTH(e.code) = ?1 ";
			Object[] param = null;
			if(parentCode!=null){
				condition+="AND e.code LIKE ?2 ";
				 param=new Object[]{level.intValue()*2,parentCode+"%"};
			}else{
				param=new Object[]{level.intValue()*2};
			}			
			return this.getListByCondition(condition,param);
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按code为6位查找产品类型集合，出现异常", jpae.getException());
		}
	}
    
	/**
	 * 查找一级分类、二级分类
	 * @author ZhangHui 2015/4/27
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductCategoryVO> getListOfCategory(int level,String parentCode) throws DaoException {
		try {
			String sql = "SELECT e.name, e.code FROM product_category e WHERE LENGTH(e.code) = ?1 ";
			if(level == 2){
				sql += "AND e.code LIKE '" + parentCode + "%'";
			}
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, level * 2);
			List<Object[]> result = query.getResultList();
			List<ProductCategoryVO> vos = new ArrayList<ProductCategoryVO>();
			if(result != null){
				
				for(Object[] obj : result){
					ProductCategoryVO vo = new ProductCategoryVO(obj[0]==null?"":obj[0].toString(),
							obj[1]==null?"":obj[1].toString(), parentCode, level);
					vos.add(vo);
				}
			}
			return vos;
		} catch (Exception e) {
			throw new DaoException("MKCategoryDAOImpl.getListOfCategory() 出现异常！", e);
		}
	}
	
	/**
	 * 按code查找一条产品类别
	 */
	@Override
	public ProductCategory findByCode(String code) throws DaoException {
		try {
			String condition = " WHERE e.code= ?1";
			List<ProductCategory> resultList = this.getListByCondition(condition, new Object[]{code});
			if(resultList.size() > 0){
				return resultList.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按code查找产品类别，出现异常", jpae.getException());
		}
	}

	/**
	 * 通过产品类别名称来验证类别是否存在
	 * @throws DaoException
	 */
	@Override
	public ProductCategory getCategoryByName(String categoryName)
			throws DaoException {
		try{
			String condition = " WHERE e.name= ?1";
			List<ProductCategory> resultList = this.getListByCondition(condition, new Object[]{categoryName});
			if(resultList.size() > 0){
				return resultList.get(0);
			}
			return null;
		}catch (JPAException jpae) {
			throw new DaoException("【DAO-error】通过产品类别名称来验证类别是否存在时出现异常！", jpae.getException());
		}
	}

	//加载产品分类的第一大类
	//HuangYog
    @SuppressWarnings("unchecked")
	@Override
    public List<ProductCategory> loadProduCtcategory() throws DaoException {
        try {
            String sql = " SELECT * from product_category pc WHERE pc.`code` LIKE '__' ";
            Query query = entityManager.createNativeQuery(sql,ProductCategory.class);
            return query.getResultList();
        } catch (Exception daoe) {
            throw new DaoException("【DAO-error】加载产品分类的第一大类时出现异常！" + daoe.getMessage(), daoe);
        }
    }
	
	
}
