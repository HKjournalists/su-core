package com.gettec.fsnip.fsn.dao.deal.impl;

import java.util.List;

import javax.persistence.Id;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.deal.DealProblemDAO;
import com.gettec.fsnip.fsn.enums.DealProblemTypeEnums;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.deal.DealProblem;

@Repository
public class DealProblemDAOImpl  extends BaseDAOImpl<DealProblem> implements DealProblemDAO{


	@Override
	public long getTotalRecord(String businessName, String licenseNo,
			String barcode,long businessId,int complainStatus) throws DaoException {

		try {
			
			StringBuilder sql = new StringBuilder();
			String sqlStr = "select count(*) from deal_problem d where  complain_status = ?1";
			sql.append(sqlStr);
			if (businessName != null && !"".equals(businessName)) {
				sql.append(" and d.business_name like '%" + businessName + "%'");
			}
			if (businessName != null && !"".equals(licenseNo)) {
				sql.append(" and d.license_no like '%" + licenseNo + "%'");
			}
			if (barcode != null && !"".equals(barcode)) {
				sql.append(" and d.barcode like '%" + barcode + "%'");
			}
			if (complainStatus == 2) {
				sql.append(" and d.commit_status = 2 ");
			}else{
				sql.append(" and d.commit_status != 2 ");
			}
			
			return this.countBySql(sql.toString(), new Object[]{complainStatus});
		} catch (JPAException jpae) {
			throw new DaoException("获取总记录数失败",jpae.getException());
		}
	}

	//查询问题集合
	@Override
	public List<DealProblem> getProblemList(
										int page, 
										int pageSize,
										String businewssName, 
										String licenseNo, 
										String barcode,
										long businessId,
										DealProblemTypeEnums complainStatus) throws DaoException {
		
		StringBuilder condition = new StringBuilder();
		condition.append(" where  e.complainStatus=?1");
		if (businewssName != null && !"".equals(businewssName)) {
			condition.append(" and e.businessName like '%" + businewssName + "%'");
		}
		if (businewssName != null && !"".equals(licenseNo)) {
			condition.append(" and e.licenseNo like '%" + licenseNo + "%'");
		}
		if (barcode != null && !"".equals(barcode)) {
			condition.append(" and e.barcode like '%" + barcode + "%'");
		}
		
		if (complainStatus == DealProblemTypeEnums.TWO) {
			condition.append(" and e.commitStatus = 2 ");
		}else{
			condition.append(" and e.commitStatus != 2 ");
		}
		
		condition.append(" order by e.createTime desc" );
		
		try {
			
			return this.getListByPage(page, pageSize, condition.toString(),new Object[]{complainStatus});
		
		} catch (JPAException e) {
			throw new DaoException("获取处理问题数据失败",e.getException());
		}
	}

}
