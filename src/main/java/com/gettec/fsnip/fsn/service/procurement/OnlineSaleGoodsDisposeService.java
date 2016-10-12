package com.gettec.fsnip.fsn.service.procurement;

import java.util.List;

import com.gettec.fsnip.fsn.dao.procurement.OnlineSaleGoodsDisposeDao;
import com.gettec.fsnip.fsn.model.procurement.OnlineSaleGoodsDispose;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface OnlineSaleGoodsDisposeService extends BaseService<OnlineSaleGoodsDispose, OnlineSaleGoodsDisposeDao>{

	long getProcurementDisposeTotal(String name, Long currentUserOrganization);

	List<OnlineSaleGoodsDispose> getProcurementDisposeList(int page,
			int pageSize, String name, Long currentUserOrganization);

}
