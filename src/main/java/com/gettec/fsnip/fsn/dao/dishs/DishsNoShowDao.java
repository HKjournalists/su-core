package com.gettec.fsnip.fsn.dao.dishs;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.dishs.DishsNoShow;
import com.gettec.fsnip.fsn.vo.dishs.DishsNoShowVO;

public interface DishsNoShowDao extends BaseDAO<DishsNoShow> {

	DishsNoShow getDishsNoShowVo(DishsNoShowVO vo);

}
