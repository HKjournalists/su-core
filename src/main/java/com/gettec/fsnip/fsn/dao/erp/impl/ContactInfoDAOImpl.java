package com.gettec.fsnip.fsn.dao.erp.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.ContactInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.ContactInfo;

@Repository("contactInfoDAO")
public class ContactInfoDAOImpl extends BaseDAOImpl<ContactInfo> 
		implements ContactInfoDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<ContactInfo> getContactsByTypeAndNo(int type, Long no,Long organization) {
		String sql = "SELECT `T1`.ID,T1.NAME,T1.`TEL_1`,T1.`TEL_2`,T1.`EMAIL`,"
				    + "T1.`IM_ACCOUNT`,T1.`ZIP_CODE`,T1.`ADDR`, T2.`IS_DIRECT`, "
				    + "T1.`PROVINCE`,T1.`CITY`,T1.`AREA` "
					+ "FROM `t_meta_contact_info` T1 LEFT JOIN "
					+ "`t_meta_customer_to_contact` T2 ON T1.ID = T2.CONTACT_ID " 
					+ "WHERE  T2.CUSTOMER_TYPE = :type AND T2.CUSTOMER_NO =:no AND T1.organization =:organization";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("type", type);
		query.setParameter("no", no);
		query.setParameter("organization", organization);
		List<ContactInfo> result = new ArrayList<ContactInfo>();
		List<Object[]> objs = query.getResultList();
		if(objs != null && objs.size() >0){
			for(Object[] obj : objs){
				try {
					ContactInfo info = new ContactInfo(((BigInteger)obj[0]).longValue(),obj[1].toString() , obj[2].toString(), obj[3].toString(),
							obj[4].toString(), obj[5].toString(), obj[6].toString(), obj[7].toString(), ((Boolean)obj[8]).booleanValue(),
							obj[9].toString(), obj[10].toString(), obj[11].toString());
					result.add(info);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		return result;
	}

	/**
	 * 根据收货单号，删除所有商品信息
	 * @param no
	 * @throws DaoException 
	 */
	@Override
	public void removeByNo(String no) throws DaoException {
		try {
			String sql = "DELETE FROM t_meta_purchaseorder_info where po_id IN ( " +
						"select contact_id from t_meta_receivingnote_to_contact where receivenote_no =?1)";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, no);
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException("ContactInfoDAOImpl.removeByNo() 据收货单号，删除所有商品信息,出现异常！", e);
		}
	}

	/**
	 * 根据企业id和企业类型获取联系人列表
	 * @param bussId
	 * @param type
	 * @throws DaoException 
	 */
	@Override
	public List<ContactInfo> getListByBusIdAndType(int page, int pageSize, 
			Long bussId, int type, Long organization) throws ServiceException {
		try {
			String sql = "SELECT * FROM T_META_CONTACT_INFO WHERE " ;
			if(null != organization){
				sql+="organization=?3 AND " ;
			}
			sql+=" ID IN(" +
				"SELECT CONTACT_ID FROM T_META_CUSTOMER_TO_CONTACT WHERE CUSTOMER_NO =?1 AND CUSTOMER_TYPE =?2 )" +
				"LIMIT " + (page-1)*pageSize + "," + pageSize;
			Object[] params = new Object[]{};
			if(null != organization){
				params = new Object[]{bussId,type,organization};
			}else{
				params = new Object[]{bussId,type};
			}
			return this.getListBySQL(ContactInfo.class, sql, params);
		} catch (JPAException jpae) {
			throw new ServiceException("ContactInfoDAOImpl.getListByBusIdAndType()-->", jpae.getException());
		}
	}

	/**
	 * 根据企业id和企业类型获取联系人总数
	 * @param bussId
	 * @param type
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	@Override
	public Long countByBusIdAndTypeAndOrgId(Long bussId, int type, Long organization) throws ServiceException {
		try {
			String sql = "SELECT count(*) FROM T_META_CONTACT_INFO WHERE organization=?1 AND ID IN(" +
							"SELECT CONTACT_ID FROM T_META_CUSTOMER_TO_CONTACT WHERE CUSTOMER_NO =?2 AND CUSTOMER_TYPE =?3 )";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			query.setParameter(2, bussId);
			query.setParameter(3, type);
			Object rtn = query.getSingleResult();
	    	return rtn == null ? 0 : Long.parseLong(rtn.toString());
		} catch (Exception e) {
			throw new ServiceException("ContactInfoDAOImpl.countByBusIdAndTypeAndOrgId() 根据企业id和企业类型获取联系人总数，出现异常！", e);
		}
	}
}
