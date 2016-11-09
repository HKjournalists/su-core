package com.gettec.fsnip.fsn.service.resort.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.resort.ResortsDao;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.procurement.OnlineSaleGoods;
import com.gettec.fsnip.fsn.model.resort.Resorts;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.resort.ResortsService;

@Service(value = "ResortsService")
public class ResortsServiceImpl extends BaseServiceImpl<Resorts, ResortsDao> implements ResortsService{

	@Autowired ResortsDao resortsDao;
	@Override
	public ResortsDao getDAO() {
		return resortsDao;
	}
	@Override
	public long getResortsTotal(String name,Long currentUserOrganization) {
		try {
			String condition = " WHERE e.currentUserOrganization = ?1 ";
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
	public List<Resorts> getResortsList(int page, int pageSize, String name,Long currentUserOrganization) {
		try {
			String condition = " WHERE e.currentUserOrganization = ?1 ";
			Object[] param = new Object[]{currentUserOrganization};
			if(StringUtils.isNotBlank(name)){
				param = new Object[]{currentUserOrganization,name};
				condition += " and e.name like concat('%',concat(?2,'%'))  ";
				condition+=" order by e.id asc";
			} else{
				condition+=" order by e.id asc";
			}
			List<Resorts> list = this.getDAO().getListByPage(page,pageSize,condition, param);
			return list;
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

}
