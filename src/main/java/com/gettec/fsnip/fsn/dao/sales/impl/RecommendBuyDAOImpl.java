package com.gettec.fsnip.fsn.dao.sales.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.sales.RecommendBuyDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.sales.RecommendBuy;
import com.gettec.fsnip.fsn.vo.sales.RecommendBuyVO;

/**
 * 推荐购买方式DAOImpl
 * @author tangxin 2015/04/24
 *
 */
@Repository(value = "recommendBuyDAO")
public class RecommendBuyDAOImpl extends BaseDAOImpl<RecommendBuy>
		implements RecommendBuyDAO {

	/**
	 * 根据筛选条件统计 推荐购买方式 数量
	 * @author tangxin 2015-04-27
	 */
	@Override
	public long countByConfigure(Long organization, String configure) throws DaoException {
		try{
			configure = (configure == null ? " WHERE 1=1 " : configure);
			configure = configure + " and buyway.organization = ?1 and buyway.del_status = 0 ";
			String sql = "select count(buyway.id) from t_bus_recommend_buy buyway " + configure;
			return this.countBySql(sql, new Object[]{organization});
		}catch(JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 根据推荐购买方式名称统计 数量 验证名称是否重复使用
	 * @author tangxin 2015-04-29
	 */
	@Override
	public long countByName(String name, Long organization, Long id) throws DaoException {
		try{
			String condition = "";
			Object[] params = null;
			if(id != null && id > 0) {
				condition = " and buyway.id != ?3 ";
				params = new Object[]{name, organization, id};
			} else {
				params = new Object[]{name, organization};
			}
			String sql = "select count(buyway.id) from t_bus_recommend_buy buyway where buyway.name = ?1 " +
					"and buyway.organization = ?2 and buyway.del_status = 0 " + condition;
			return this.countBySql(sql, params);
		}catch(JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 根据企业id获取对应的推荐购买方式
	 * @author HY
	 * Create Date 2015-05-05
	 */
	@Override
	public List<RecommendBuyVO> getEntityByBusId(Long busId) throws DaoException {
		String sql = " select new com.gettec.fsnip.fsn.vo.sales.RecommendBuyVO(buyway) from " +
				"com.gettec.fsnip.fsn.model.sales.RecommendBuy buyway WHERE buyway.businessId = ?1 and buyway.delStatus = 0";
		try {
			return this.getListByJPQL(RecommendBuyVO.class , sql,new Object[]{busId});
		} catch (JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 根据筛选条件分页查询 推荐购买方式
	 * @author tangxin 2015-04-27
	 */
	@Override
	public List<RecommendBuyVO> getListByOrganizationWithPage(Long organization, String condition, 
			Integer page, Integer pageSize) throws DaoException {
		try{
			condition = (condition == null ? " WHERE 1=1 " : condition);
			condition += " and buyway.organization = ?1 and buyway.delStatus = 0 ";
			String jpql = " select new com.gettec.fsnip.fsn.vo.sales.RecommendBuyVO(buyway) from " +
					"com.gettec.fsnip.fsn.model.sales.RecommendBuy buyway ";
			jpql = jpql + condition;
			return this.getListByJPQL(RecommendBuyVO.class, jpql, page, pageSize, new Object[]{organization});
		}catch(JPAException jpae){
			throw new DaoException(jpae.getMessage(), jpae);
		}
	}

}
