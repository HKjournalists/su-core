package com.gettec.fsnip.fsn.dao.erp.buss.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.buss.FlittingOrderDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.buss.FlittingOrder;

@Repository
public class FlittingOrderDAOImpl extends BaseDAOImpl<FlittingOrder> 
		implements FlittingOrderDAO{
	
	/**
	 * 获取调拨已有的最大编号
	 * @param noStart
	 * @return
	 * @throws DaoException 
	 */
	@Override
	public String findNoMaxByNoStart(String noStart) throws DaoException {
		try {
			/* 按降序排序，获取已存在最大编号 */
			String sql = "SELECT NO FROM t_buss_flitting_order where NO like ?1 ORDER BY NO DESC LIMIT 1";
			List<String> result = this.getListBySQLWithoutType(String.class, sql, new Object[]{noStart+"%"});
			if(result != null && result.size()>0){
				return result.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new DaoException("FlittingOrderDAOImpl.findNoMaxByNoStart() 获取调拨已有的最大编号，出现异常！", e);
		}
	}
}
