package com.gettec.fsnip.fsn.service.procurement.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.procurement.OnlineSaleGoodsDisposeDao;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.procurement.OnlineSaleGoodsDispose;
import com.gettec.fsnip.fsn.model.procurement.ProcurementDispose;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.procurement.OnlineSaleGoodsDisposeService;

/**
 * OnlineSaleGoodsDispose service implementation
 * 
 * @author suxiang
 */
@Service(value = "OnlineSaleGoodsDisposeService")
public class OnlineSaleGoodsDisposeServiceImpl extends
		BaseServiceImpl<OnlineSaleGoodsDispose, OnlineSaleGoodsDisposeDao>
		implements OnlineSaleGoodsDisposeService {

	@Autowired
	private OnlineSaleGoodsDisposeDao disposeDao;

	@Override
	public OnlineSaleGoodsDisposeDao getDAO() {
		return disposeDao;
	}

	@Override
	public long getProcurementDisposeTotal(String name,
			Long currentUserOrganization) {
		try {
			String condition = " WHERE e.organizationId = ?1 ";
			Object[] param = new Object[]{currentUserOrganization};
			if(StringUtils.isNotBlank(name)){
				param = new Object[]{currentUserOrganization,name};
				condition += " and e.onlineSaleName like concat('%',concat(?2,'%'))  ";
			}
			return getDAO().count(condition,param);
				
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<OnlineSaleGoodsDispose> getProcurementDisposeList(int page,
			int pageSize, String name, Long currentUserOrganization) {
		try {
			String condition = " WHERE e.organizationId = ?1  ";
			Object[] param = new Object[]{currentUserOrganization};
			if(StringUtils.isNotBlank(name)){
				param = new Object[]{currentUserOrganization,name};
				condition += " and e.onlineSaleName like concat('%',concat(?2,'%'))  ";
			}
			condition+=" order by e.createDate desc";
			List<OnlineSaleGoodsDispose> list = this.getDAO().getListByPage(page,pageSize,condition, param);
			return list;
				
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}

}
