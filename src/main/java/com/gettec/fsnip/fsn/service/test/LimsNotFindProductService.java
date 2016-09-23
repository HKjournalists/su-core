package com.gettec.fsnip.fsn.service.test;

import com.gettec.fsnip.fsn.dao.test.LimsNotFindProductDAO;
import com.gettec.fsnip.fsn.model.test.LimsNotFindProduct;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface LimsNotFindProductService extends BaseService<LimsNotFindProduct, LimsNotFindProductDAO>{

	void save(String data);
	
}