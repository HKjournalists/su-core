package com.gettec.fsnip.fsn.dao.dishs.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.dishs.DishsNoShowDao;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.dishs.DishsNoShow;
import com.gettec.fsnip.fsn.vo.dishs.DishsNoShowVO;
@Repository(value="dishsNoShowDao")
public class DishsNoShowDaoImpl extends BaseDAOImpl<DishsNoShow> implements DishsNoShowDao{

	@Override
	public DishsNoShow getDishsNoShowVo(DishsNoShowVO vo) {
		try {
			String condition = " WHERE e.showId = ?1  and e.showTime = ?2";
			List<DishsNoShow> resultList = this.getListByCondition(condition, new Object[]{vo.getShowId(),vo.getShowTime()});
			if(resultList.size() > 0){
				return resultList.get(0);
			}
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}

}
