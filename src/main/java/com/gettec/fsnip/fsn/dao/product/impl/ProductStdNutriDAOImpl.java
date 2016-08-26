package com.gettec.fsnip.fsn.dao.product.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductStdNutriDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.base.Nutrition;

/**
 * Product customized operation implementation
 * 
 * @author Ryan Wang
 */
@Repository(value="productStdNutriDAO")
public class ProductStdNutriDAOImpl extends BaseDAOImpl<Nutrition>
		implements ProductStdNutriDAO {

	/**
	 * 获取标准的营养列表
	 * @throws DaoException 
	 */
	@Override
	public List<Nutrition> getListOfStandNutri() throws DaoException {
		try {
			return this.getListByCondition("", null);
		} catch (JPAException jpae) {
			throw new DaoException("【dao-error】获取标准的营养列表，出现异常！", jpae.getException());
		}
	}

	/**
	 * 按名称获取一条标准营养信息
	 * @throws DaoException 
	 */
	@Override
	public Nutrition findByName(String name) throws DaoException {
		try {
			String condition = " WHERE e.name=?1";
			Object[] params = new Object[]{name};
			List<Nutrition> result = this.getListByCondition(condition, params);
			if(result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【dao-error】按名称获取一条标准营养信息，出现异常！", jpae.getException());
		}
	}
}