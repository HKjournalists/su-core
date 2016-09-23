package com.gettec.fsnip.fsn.dao.recycle.impl;

import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.recycle.RecycleDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.ProductDestroyRecord;
import com.gettec.fsnip.fsn.recycle.Process_mode;
import com.gettec.fsnip.fsn.recycle.Recycle_reason;

@Repository(value="recycleDAO")
public class RecycleDAOImpl extends BaseDAOImpl<ProductDestroyRecord> implements RecycleDAO{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void insert_recycle_record(ProductDestroyRecord productDestroyRecord) throws DaoException {
		entityManager.persist(productDestroyRecord);
	}
	
	@SuppressWarnings("unchecked")
	public Integer getRecords_count(HashMap<String,String> param) throws Exception {
		StringBuilder sql = new StringBuilder();
		String type = param.get("type");
		String batch = param.get("batch");
		String product_name = param.get("product_name");
		String product_code = param.get("product_code");
		String problem_describe = param.get("problem_describe");
		String start_date = param.get("start_date");
		String end_date = param.get("end_date");
		String enterprise_name = param.get("enterprise_name");
		
		sql.append("select count(0) from product_recycle_destroy_record ");
		boolean condition_flg = false;
		if ("1".equals(type)) {
			//查询退货记录
			sql.append("where process_mode = '");
			sql.append(Process_mode.RETURN_GOODS.getValue());
			sql.append("'");
			condition_flg = true;
		} else if ("2".equals(type)) {
			//查询销毁记录
			sql.append("where process_mode = '");
			sql.append(Process_mode.DESTROY.getValue());
			sql.append("'");
			condition_flg = true;
		}
		if (batch != null && !"".equals(batch)) {
			//过滤批次
			if (condition_flg) {
				sql.append(" and ");
			} else {
				sql.append(" where ");
			}
			condition_flg = true;
			sql.append(" batch = '");
			sql.append(batch);
			sql.append("'");
		}
		if (product_name != null && !"".equals(product_name)) {
			//过滤产品名称
			if (condition_flg) {
				sql.append(" and ");
			} else {
				sql.append(" where ");
			}
			condition_flg = true;
			sql.append(" product_name like '%");
			sql.append(product_name);
			sql.append("%'");
		}
		if (product_code != null && !"".equals(product_code)) {
			//过滤产品条码
			if (condition_flg) {
				sql.append(" and ");
			} else {
				sql.append(" where ");
			}
			condition_flg = true;
			sql.append(" product_code = '");
			sql.append(product_code);
			sql.append("'");
		}
		if (problem_describe != null && !"".equals(problem_describe)) {
			//过滤问题描述
			if (condition_flg) {
				sql.append(" and ");
			} else {
				sql.append(" where ");
			}
			condition_flg = true;
			sql.append(" problem_describe = '");
			for (Recycle_reason recycle_reason : Recycle_reason.values()) {
				if (recycle_reason.getName().equals(problem_describe)) {
					sql.append(recycle_reason.getValue());
					break;
				}
			}
			sql.append("'");
		}
		if (start_date != null && !"".equals(start_date)) {
			//过滤起始日期
			if (condition_flg) {
				sql.append(" and ");
			} else {
				sql.append(" where ");
			}
			condition_flg = true;
			sql.append(" process_time >= '");
			sql.append(start_date);
			sql.append("'");
		}
		if (end_date != null && !"".equals(end_date)) {
			//过滤截止日期
			if (condition_flg) {
				sql.append(" and ");
			} else {
				sql.append(" where ");
			}
			condition_flg = true;
			sql.append(" process_time <= '");
			sql.append(end_date);
			sql.append("'");
		}
		if (enterprise_name != null && !"".equals(enterprise_name)) {
			//过滤企业名称
			if (condition_flg) {
				sql.append(" and ");
			} else {
				sql.append(" where ");
			}
			condition_flg = true;
			sql.append(" handle_name like '%");
			sql.append(enterprise_name);
			sql.append("%'");
		}
		Query query = entityManager.createNativeQuery(sql.toString());
		List<Object> results = query.getResultList();
		Integer count = 0;
		if (results != null && !results.isEmpty()) {
			String s_count = results.get(0).toString();
			count = Integer.parseInt(s_count);
		}
		return count;
	}
	
	@SuppressWarnings("rawtypes")
	public List getRecords(HashMap<String,String> param) throws Exception {
		StringBuilder sql = new StringBuilder();
		String type = param.get("type");
		String page = param.get("page");
		String page_number = param.get("page_number");
		String batch = param.get("batch");
		String product_name = param.get("product_name");
		String product_code = param.get("product_code");
		String problem_describe = param.get("problem_describe");
		String start_date = param.get("start_date");
		String end_date = param.get("end_date");
		String enterprise_name = param.get("enterprise_name");
		
		sql.append("select * from product_recycle_destroy_record ");
		boolean condition_flg = false;
		if ("1".equals(type)) {
			//查询退货记录
			sql.append("where process_mode = '");
			sql.append(Process_mode.RETURN_GOODS.getValue());
			sql.append("'");
			condition_flg = true;
		} else if ("2".equals(type)) {
			//查询销毁记录
			sql.append("where process_mode = '");
			sql.append(Process_mode.DESTROY.getValue());
			sql.append("'");
			condition_flg = true;
		}
		if (batch != null && !"".equals(batch)) {
			//过滤批次
			if (condition_flg) {
				sql.append(" and ");
			} else {
				sql.append(" where ");
			}
			condition_flg = true;
			sql.append(" batch = '");
			sql.append(batch);
			sql.append("'");
		}
		if (product_name != null && !"".equals(product_name)) {
			//过滤产品名称
			if (condition_flg) {
				sql.append(" and ");
			} else {
				sql.append(" where ");
			}
			condition_flg = true;
			sql.append(" product_name like '%");
			sql.append(product_name);
			sql.append("%'");
		}
		if (product_code != null && !"".equals(product_code)) {
			//过滤产品条码
			if (condition_flg) {
				sql.append(" and ");
			} else {
				sql.append(" where ");
			}
			condition_flg = true;
			sql.append(" product_code = '");
			sql.append(product_code);
			sql.append("'");
		}
		if (problem_describe!= null && !"".equals(problem_describe)) {
			//过滤问题描述
			if (condition_flg) {
				sql.append(" and ");
			} else {
				sql.append(" where ");
			}
			condition_flg = true;
			sql.append(" problem_describe = '");
			for (Recycle_reason recycle_reason : Recycle_reason.values()) {
				if (recycle_reason.getName().equals(problem_describe)) {
					sql.append(recycle_reason.getValue());
					break;
				}
			}
			sql.append("'");
		}
		if (start_date != null && !"".equals(start_date)) {
			//过滤起始日期
			if (condition_flg) {
				sql.append(" and ");
			} else {
				sql.append(" where ");
			}
			condition_flg = true;
			sql.append(" process_time >= '");
			sql.append(start_date);
			sql.append("'");
		}
		if (end_date != null && !"".equals(end_date)) {
			//过滤截止日期
			if (condition_flg) {
				sql.append(" and ");
			} else {
				sql.append(" where ");
			}
			condition_flg = true;
			sql.append(" process_time <= '");
			sql.append(end_date);
			sql.append("'");
		}
		if (enterprise_name != null && !"".equals(enterprise_name)) {
			//过滤企业名称
			if (condition_flg) {
				sql.append(" and ");
			} else {
				sql.append(" where ");
			}
			condition_flg = true;
			sql.append(" handle_name like '%");
			sql.append(enterprise_name);
			sql.append("%'");
		}
		sql.append(" order by process_time desc,handle_name limit ");
    	int i_page = 1;
    	if (page != null && !"".equals(page)) {
        	try {
        		i_page = Integer.parseInt(page);
        	} catch (NumberFormatException e) {
        		throw new Exception("页码不正确");
        	}
    	}
    	int i_page_number = 10;
    	if (page_number != null && !"".equals(page_number)) {
        	try {
        		i_page_number = Integer.parseInt(page_number);
        	} catch (NumberFormatException e) {
        		throw new Exception("每页数据条数不正确");
        	}
    	}
		int limit_start = (i_page - 1) * i_page_number;
		sql.append(limit_start);
		sql.append(",");
		sql.append(i_page_number);
		
		Query query = entityManager.createNativeQuery(sql.toString());
		return query.getResultList();
	}
}