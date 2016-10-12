package com.gettec.fsnip.fsn.service.procurement;

import java.util.List;

import com.gettec.fsnip.fsn.dao.procurement.OnlineSaleGoodsDao;
import com.gettec.fsnip.fsn.model.procurement.OnlineSaleGoods;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface OnlineSaleGoodsService extends BaseService<OnlineSaleGoods, OnlineSaleGoodsDao> {

	long getOnlineSaleTotal(String name, Long currentUserOrganization);

	List<OnlineSaleGoods> getOnlineSaleList(int page, int pageSize,
			String name, Long currentUserOrganization);

}
