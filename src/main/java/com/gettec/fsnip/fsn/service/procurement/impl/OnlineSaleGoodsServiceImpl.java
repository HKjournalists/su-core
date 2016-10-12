package com.gettec.fsnip.fsn.service.procurement.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.procurement.OnlineSaleGoodsDao;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.procurement.OnlineSaleGoods;
import com.gettec.fsnip.fsn.model.procurement.ProcurementInfo;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.procurement.OnlineSaleGoodsService;

/**
 * OnlineSaleGoods service implementation
 * 
 * @author suxiang
 */
@Service(value = "OnlineSaleGoodsService")
public class OnlineSaleGoodsServiceImpl extends
		BaseServiceImpl<OnlineSaleGoods, OnlineSaleGoodsDao> implements
		OnlineSaleGoodsService {

	@Autowired
	private OnlineSaleGoodsDao onlineSaleGoodsDao;

	@Override
	public OnlineSaleGoodsDao getDAO() {
		return onlineSaleGoodsDao;
	}

	@Override
	public long getOnlineSaleTotal(String name, Long currentUserOrganization) {
		try {
			String condition = " WHERE e.organizationId = ?1 ";
			Object[] param = new Object[]{currentUserOrganization};
			if(StringUtils.isNotBlank(name)){
				param = new Object[]{currentUserOrganization,name};
				condition += " and e.name like concat('%',concat(?2,'%'))  ";
			}
			return getDAO().count(condition,param);
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<OnlineSaleGoods> getOnlineSaleList(int page, int pageSize,
			String name, Long currentUserOrganization) {
		try {
			String condition = " WHERE e.organizationId = ?1 ";
			Object[] param = new Object[]{currentUserOrganization};
			if(StringUtils.isNotBlank(name)){
				param = new Object[]{currentUserOrganization,name};
				condition += " and e.name like concat('%',concat(?2,'%'))  ";
				condition+=" order by e.procurementDate desc";
			} else{
				condition+=" order by e.createDate desc";
			}
			List<OnlineSaleGoods> list = this.getDAO().getListByPage(page,pageSize,condition, param);
			return list;
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}

}
