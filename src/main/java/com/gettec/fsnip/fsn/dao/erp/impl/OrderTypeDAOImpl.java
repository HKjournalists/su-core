package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.OrderTypeDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.base.OrderType;

//单别cgw
@Repository("orderTypeDAO")
public class OrderTypeDAOImpl extends BaseDAOImpl<OrderType> implements OrderTypeDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderType> getOrdrTypeByModuleAndOrder(String module,
			String order, String configure) {
		String sql = "SELECT ot_id,ot_type FROM t_meta_order_type WHERE ot_belong_module =:module AND ot_belong_order =:order";
		if(configure != null){
			sql = sql+configure;
		}
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("module", module);
		query.setParameter("order", order);
		List<OrderType> result = new ArrayList<OrderType>();
		List<Object[]> objs = query.getResultList();
		if(objs != null && objs.size() >0){
			for(Object[] obj :objs){
				try {
					OrderType info = new OrderType(Long.parseLong(obj[0].toString()), obj[1].toString());
					result.add(info);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 根据所属模块/单据/组织机构id 查找单别数量
	 * @param belongModule  所属模块
	 * @param belongOrder   所属单据
	 * @param organization  组织机构id
	 * @param noStart       单别前缀
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public long count(String belongModule, String belongOrder, Long organization, String noStart) throws DaoException {
		try {
			/* 按降序排序，获取已存在最大编号 */
			String condition = " WHERE e.ot_belong_module = ?1"
					+ " AND e.ot_belong_order = ?2 AND e.organization = ?3 AND e.ot_type = ?4";
			return this.count(condition, new Object[]{belongModule, belongOrder, organization, noStart});
		} catch (JPAException jpae) {
			throw new DaoException("OrderTypeDAOImpl.count() 根据所属模块/单据/组织机构id 查找单别数量，出现异常！", jpae.getException());
		}
	}
}
