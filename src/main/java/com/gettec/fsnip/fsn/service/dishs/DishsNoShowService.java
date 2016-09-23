package com.gettec.fsnip.fsn.service.dishs;

import java.util.List;

import com.gettec.fsnip.fsn.dao.dishs.DishsNoDao;
import com.gettec.fsnip.fsn.dao.dishs.DishsNoShowDao;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.dishs.DishsNo;
import com.gettec.fsnip.fsn.model.dishs.DishsNoShow;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.dishs.DishsNoShowVO;

public interface DishsNoShowService extends BaseService<DishsNoShow,DishsNoShowDao> {

	boolean saveDishsNoShow(List<DishsNoShowVO> voList);
}
