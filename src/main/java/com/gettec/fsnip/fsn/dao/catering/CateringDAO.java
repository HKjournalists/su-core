package com.gettec.fsnip.fsn.dao.catering;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.business.Catering;

public interface CateringDAO extends BaseDAO<Catering>{


    Catering findByBusinessId(Long businessId);
}
