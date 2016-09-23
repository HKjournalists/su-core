package com.gettec.fsnip.fsn.dao.sales.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.sales.PromotionCaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.sales.PromotionCase;
import com.gettec.fsnip.fsn.vo.sales.PromotionCaseVO;

@Repository(value="promotionCaseDAO")
public class PromotionCaseDAOImpl extends BaseDAOImpl<PromotionCase> implements PromotionCaseDAO {

	/**
     * 根据筛选条件统计销售活动数量
     * @author tangxin 2015-05-03
     */
	@Override
	public long countByConfigure(Long organization, String configure) throws DaoException {
		try{
			configure = (configure == null ? " WHERE 1=1 " : configure);
			configure = configure.replace("startDate", "start_date");
			configure = configure + " and promotion.organization = ?1 and promotion.del_status = 0 ";
			String sql = "select count(promotion.id) from t_bus_promotion_case promotion " + configure;
			return this.countBySql(sql, new Object[]{organization});
		}catch(JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 根据销售活动名称统计数量，验证名称是否重复用
	 * @author tangxin 2015-05-03
	 */
	@Override
	public long countByName(String name, Long organization, Long id) throws DaoException {
		try{
			String condition = "";
			Object[] params = null;
			if(id != null && id > 0) {
				condition = " and promotion.id != ?3 ";
				params = new Object[]{name, organization, id};
			} else {
				params = new Object[]{name, organization};
			}
			String sql = "select count(promotion.id) from t_bus_promotion_case promotion where promotion.name = ?1 " +
					"and promotion.organization = ?2 and promotion.del_status = 0 " + condition;
			return this.countBySql(sql, params);
		}catch(JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 根据筛选条件分页查询促销活动信息
	 * @author tangxin 2015-05-03
	 */
	@Override
	public List<PromotionCaseVO> getListByOrganizationWithPage(Long organization, String condition, 
			Integer page, Integer pageSize) throws DaoException {
		try{
			condition = (condition == null ? " WHERE 1=1 " : condition);
			condition += " and promotion.organization = ?1 and promotion.delStatus = 0 ";
			String jpql = " select new com.gettec.fsnip.fsn.vo.sales.PromotionCaseVO(promotion) from " +
					"com.gettec.fsnip.fsn.model.sales.PromotionCase promotion " + condition;
			return this.getListByJPQL(PromotionCaseVO.class, jpql, page, pageSize, new Object[]{organization});
		}catch(JPAException jpae){
			throw new DaoException(jpae.getMessage(), jpae);
		}
	}
}
