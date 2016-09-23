package com.gettec.fsnip.fsn.dao.erp.impl;

import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.OutOfBillDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.OutOfBill;

@Repository("outOfBillDAO")
public class OutOfBillDAOImpl extends BaseDAOImpl<OutOfBill> implements
		OutOfBillDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getOurOfBillByProviderNum(Long organization,Long cus_no) {
		String sql = "SELECT OUTOFBILL_NO FROM t_meta_out_of_bill where organization =:organization and CUSTOMER_NO =:cus_no and OUT_OF_BILL_STATE = '已发货'";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("organization", organization);
		query.setParameter("cus_no", cus_no);
		List<String> Num = null;
		try {
			Num = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Num;
	}
	
	@Override
	public boolean checkOne(String outOfBillNo) {
		boolean flag = false;
		String sql = "UPDATE T_META_OUT_OF_BILL SET OUT_OF_BILL_STATE = :outOfBillState WHERE OUTOFBILL_NO = :outOfBillNo";
		try {
			entityManager.createNativeQuery(sql)
				.setParameter("outOfBillState", "已发货")
				.setParameter("outOfBillNo", outOfBillNo)
				.executeUpdate();
			flag = true;
		} catch(Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	@Override
	public boolean checkTwo(String outOfBillNo) {
		boolean flag = false;
		String sql = "UPDATE T_META_OUT_OF_BILL SET OUT_OF_BILL_STATE = :outOfBillState WHERE OUTOFBILL_NO = :outOfBillNo";
		try {
			entityManager.createNativeQuery(sql)
				.setParameter("outOfBillState", "已交付")
				.setParameter("outOfBillNo", outOfBillNo)
				.executeUpdate();
			flag = true;
		} catch(Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BigInteger> getCidByOutOrder(String num) {
		String sql = "SELECT outgoods_id FROM T_META_OUTBILL_TO_OUTGOODS where OUTOFBILL_NO =:num";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("num", num);
		List<BigInteger> con_id = null;
		try {
			con_id = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return con_id;
	}
	
	/**
	 * 获取出货单已有的最大编号
	 * @param noStart
	 * @return
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findNoMaxByNoStart(String noStart) throws DaoException {
		try {
			/* 按降序排序，获取已存在最大编号 */

			String sql = "SELECT OUTOFBILL_NO FROM t_meta_out_of_bill where OUTOFBILL_NO like ?1 ORDER BY OUTOFBILL_NO DESC LIMIT 1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, noStart + "%");
			List<Object> result = query.getResultList();
			if(result != null&&result.size()>0){
				return result.get(0).toString();
			}
			return null;
		} catch (Exception e) {
			throw new DaoException("OutOfBillDAOImpl.findNoMaxByNoStart() 获取出货单已有的最大编号，出现异常！", e);
		}
	}
}
