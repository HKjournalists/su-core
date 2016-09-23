package com.gettec.fsnip.fsn.dao.deal.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.deal.DealToProblemDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.deal.DealToProblem;

@Repository(value="dealToProblemDAO")
public class DealToProblemDAOImpl  extends BaseDAOImpl<DealToProblem> implements DealToProblemDAO{

	@Override
	public long getdealToProblemTotal(String barcode,long businessId) {
		try {
			StringBuilder sql = new StringBuilder();
			    sql.append("SELECT COUNT(*) FROM deal_to_problem e WHERE e.business_id =?1 ");
		
			if (barcode !=null&&!"".equals(barcode)) {
				sql.append(" AND e.barcode LIKE '%" + barcode + "%'");
			}
			return this.countBySql(sql.toString(), new Object[]{businessId});
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public List<DealToProblem> getdealToProblemList(int page, int pageSize,String barcode,long businessId) {
			try {
				StringBuilder condition = new StringBuilder();
				condition.append(" WHERE  e.businessId = ?1 ");
				if (barcode!=null&&!barcode.equals("")) {
					condition.append(" AND e.barcode LIKE '%" + barcode + "%'");
				}
				condition.append(" ORDER BY e.dealStatus ASC,e.createTime DESC");
				return this.getListByPage(page, pageSize, condition.toString(), new Object[]{businessId});
			} catch (JPAException e) {
				e.printStackTrace();
			}
			return new ArrayList<DealToProblem>();
	}
	@Override
	public DealToProblem getFindPcodeOrScode(String pcode) {
		DealToProblem dealVo = null;
		try {
			StringBuilder condition = new StringBuilder();
			condition.append(" WHERE  e.scode = ?1 ");
			List<DealToProblem> dealList = this.getListByCondition(condition.toString(),new Object[]{pcode});
			if(dealList.size()>0){
				dealVo = dealList.get(0);	
			}
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return dealVo;
	}
}
