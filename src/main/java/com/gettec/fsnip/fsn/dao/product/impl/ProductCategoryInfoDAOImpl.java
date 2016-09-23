package com.gettec.fsnip.fsn.dao.product.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductCategoryInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.vo.product.ProductCategoryVO;

/**
 * productCategoryInfo customized operation implementation
 * 
 * @author zhawanneng
 */
@Repository(value="productCategoryInfoDAO")
public class ProductCategoryInfoDAOImpl extends BaseDAOImpl<ProductCategoryInfo>
		implements ProductCategoryInfoDAO {
	
	/**
	 * 根据二级分类的ID查找二级分类下的所有三级分类或者执行标准
	 * @param upId 二级分类ID
	 * @param categoryFlag true：查找三级分类,false：查找执行标准
	 * @author 郝圆彬
	 */
	@Override
	public List<ProductCategoryInfo> getListOfUpId(Long upId, boolean categoryFlag) throws DaoException {
		try{
			String condition = " WHERE e.category.id=?1 AND e.categoryFlag=?2 AND e.del = ?3";
			return this.getListByCondition(condition, new Object[]{upId, categoryFlag, false}); 
		}catch(JPAException jpae){
			throw new DaoException("MKCategoryInfoDAOImpl-->getListOfUpId"+jpae.getMessage(),jpae);
		}
	}
	
	/**
	 * 获取三级分类
	 * @author ZhangHui 2015/4/27
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductCategoryVO> getListByParentcode(String parentcode, boolean iscategory) throws DaoException {
		try{
			String sql = "SELECT id,`name` FROM product_category_info " +
						 "WHERE category_flag = ?1 AND del = ?2 " +
						 "AND category_id = (" +
						 		"SELECT id FROM product_category WHERE `code` = ?3)";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, iscategory);
			query.setParameter(2, false);
			query.setParameter(3, parentcode);
			List<Object[]> result = query.getResultList();
			List<ProductCategoryVO> vos = new ArrayList<ProductCategoryVO>();
			if(result != null){
				for(Object[] obj : result){
					ProductCategoryVO vo = new ProductCategoryVO(((BigInteger)obj[0]).longValue(),
							obj[1]==null?"":obj[1].toString(), parentcode, 3);
					vos.add(vo);
				}
			}
			return vos;
		}catch(Exception e){
			throw new DaoException("MKCategoryInfoDAOImpl.getListByParentcode()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 根据名称和二级分类ID查找三级分类或者执行标准
	 * @param name 名称
	 * @param categoryId 二级分类的ID
	 * @param categoryFlag true:查找三级分类，false：查找执行标准
	 * @author 郝圆彬
	 */
	@Override
	public ProductCategoryInfo findByNameAndCategoryIdAndFlag(String name,
			Long categoryId, boolean categoryFlag)  throws DaoException{
		try{
			String condition = " WHERE e.name=?1 AND e.category.id=?2 AND e.categoryFlag=?3";
			List<ProductCategoryInfo> result = this.getListByCondition(condition, new Object[]{name,categoryId,categoryFlag});
			if(result!=null&&result.size()>0){
				return result.get(0);
			}
			return null;
		}catch(JPAException jpae){
			throw new DaoException("MKCategoryInfoDAOImpl-->findByNameAndCategoryIdAndFlag"+jpae.getMessage(),jpae);
		}
	}
	@Override
	public ProductCategoryInfo findByCategoryId(Long categoryId)  throws DaoException{
		try{
			String condition = " WHERE e.category.id=?1 ";
			List<ProductCategoryInfo> result = this.getListByCondition(condition, new Object[]{categoryId});
			if(result!=null&&result.size()>0){
				return result.get(0);
			}
			return null;
		}catch(JPAException jpae){
			throw new DaoException("MKCategoryInfoDAOImpl-->findByNameAndCategoryIdAndFlag"+jpae.getMessage(),jpae);
		}
	}
	/**
	 * 根据产品id获取产品执行标准
	 * @author longxianzhen 2015-08-06
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductCategoryInfo> getRegularityByProId(Long proId)
			throws DaoException {
		try{
			String sql = " SELECT pc.* FROM product_to_regularity p LEFT JOIN "+
						"product_category_info pc ON p.regularity_id=pc.id WHERE p.product_id=?1";
			Query query = entityManager.createNativeQuery(sql,ProductCategoryInfo.class);
			query.setParameter(1, proId);
			List<ProductCategoryInfo> result = query.getResultList();
			return result;
		}catch(Exception jpae){
			throw new DaoException("MKCategoryInfoDAOImpl-->getRegularityByProId",jpae);
		}
	}
}