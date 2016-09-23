package com.gettec.fsnip.fsn.service.mengniu;

import com.gettec.fsnip.fsn.dao.mengniu.MengNiuProductNameDAO;
import com.gettec.fsnip.fsn.model.mengniu.MengNiuProductName;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface MengNiuProductNameService extends BaseService<MengNiuProductName, MengNiuProductNameDAO> {
	boolean isProductNameExist(String product_name);
}
