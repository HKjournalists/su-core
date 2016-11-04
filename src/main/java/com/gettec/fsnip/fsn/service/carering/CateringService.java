package com.gettec.fsnip.fsn.service.carering;

import com.gettec.fsnip.fsn.dao.catering.CateringDAO;
import com.gettec.fsnip.fsn.model.business.Catering;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.catering.CateringVO;

public interface CateringService extends BaseService<Catering,CateringDAO>{


	void saveOrUpdate(CateringVO catering);

	CateringVO getCateringVOBusinessId(Long id);
}
