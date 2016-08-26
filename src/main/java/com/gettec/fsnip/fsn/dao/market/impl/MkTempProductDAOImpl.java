package com.gettec.fsnip.fsn.dao.market.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.market.MkTempProductDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.market.MkTempProduct;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
@Repository(value="tempProductDAO")
public class MkTempProductDAOImpl extends BaseDAOImpl<MkTempProduct> 
					implements MkTempProductDAO{
	/**
	 * 按用户真实名称和组织机构名称查找一条临时产品信息
	 * @throws DaoException 
	 */
	@Override
	public MkTempProduct findByUserRealNameAndOrganization(String userRealName, Long organization) throws DaoException {
		try {
			MkTempProduct tempProduct=null;
			String condition = " WHERE e.createUserRealName = ?1 AND e.organization = ?2";
			List<MkTempProduct> resultList = this.getListByCondition(condition, new Object[]{userRealName, organization});
			condition="SELECT * FROM product_category WHERE code=?1";
			if( resultList.size() > 0){
				tempProduct = resultList.get(0);
				List<ProductCategory> categoryNames=this.getListBySQL(ProductCategory.class, condition, new Object[]{tempProduct.getCategory()});
				if(categoryNames!=null&&categoryNames.size()>0){
					tempProduct.setCategoryName(categoryNames.get(0).getName());
				}
			}
			return tempProduct;
		} catch (JPAException e) {
			e.printStackTrace();
			throw new DaoException("【DAO-error】按用户真实名称和组织机构名称查找一条临时产品信息，出现异常", e.getException());
		}
	}
	
}
