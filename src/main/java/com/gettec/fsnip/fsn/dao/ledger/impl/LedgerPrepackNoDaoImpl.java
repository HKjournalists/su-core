package com.gettec.fsnip.fsn.dao.ledger.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.ledger.LedgerPrepackNoDao;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.ledger.LedgerPrepackNo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.vo.ledger.LedgerPrepackNoVO;

@Repository(value="ledgerPrepackNoDAO")
public class LedgerPrepackNoDaoImpl extends BaseDAOImpl<LedgerPrepackNo> implements LedgerPrepackNoDao{
	
	static SimpleDateFormat SDFTIME = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat SDFDATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public List<LedgerPrepackNoVO> loadLedgerPrepackNo(int page, int pageSize,
			String productName, String companyName, String companyPhone,long qiyeId)
			throws DaoException {
		try {
			StringBuilder condition = new StringBuilder();
			condition.append(" WHERE 1=1 ");
			if (!productName.equals("")&&productName!=null) {
				condition.append(" and productName like '%" + productName + "%'");
			}
			if (!companyName.equals("")&&companyName!=null) {
				condition.append(" and companyName like '%" + companyName + "%'");
			}
			if (!companyPhone.equals("")&&companyPhone!=null) {
				condition.append(" and companyPhone like '%" + companyPhone + "%'");
			}
			if (qiyeId!=0) {
				condition.append(" and qiyeId = " + qiyeId);
			}
			condition.append(" ORDER BY purchaseTime DESC");
			List<LedgerPrepackNo> ledgerPrepackNo = this.getListByPage(page, pageSize, condition.toString());
			return getLedgerPrepackNoVO(ledgerPrepackNo);
		} catch (JPAException jpae) {
			throw new DaoException("获取非预包装台账失败", jpae.getException());
		} 
	}
	
	private List<LedgerPrepackNoVO> getLedgerPrepackNoVO(List<LedgerPrepackNo> result)  {
		List<LedgerPrepackNoVO> list = new ArrayList<LedgerPrepackNoVO>();
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				LedgerPrepackNo objs = result.get(i);
				LedgerPrepackNoVO vo = new LedgerPrepackNoVO();
				vo.setId(objs.getId());
				vo.setProductName(objs.getProductName());
				try {
					vo.setPurchaseTime(objs.getPurchaseTime() != null ? SDFTIME.format(SDFTIME.parse(objs.getPurchaseTime().toString())) : "");
				} catch (ParseException e) {
					e.printStackTrace();
				}
				vo.setAlias(objs.getAlias());
				vo.setStandard(objs.getStandard());
				vo.setNumber(objs.getNumber());
				vo.setCompanyName(objs.getCompanyName());
				vo.setCompanyPhone(objs.getCompanyPhone());
				vo.setCompanyAddress(objs.getCompanyAddress());
				vo.setSupplier(objs.getSupplier());
				vo.setQiyeId(objs.getQiyeId());
				list.add(vo);
			}
		}
		return list;
	}

	@Override
	public long getLedgerPrepackNoTotal(String productName, String companyName,
			String companyPhone,long qiyeId) throws DaoException {
		try {
			StringBuilder sql = new StringBuilder();
			String sqlStr = " select count(*) from ledger_prepackno lp where 1=1";
			sql.append(sqlStr);
			if (!"".equals(productName)&&productName!=null) {
				sql.append(" and lp.product_name like '%" + productName + "%'");
			}
			if (!"".equals(companyName)&&companyName!=null) {
				sql.append(" and lp.company_name like '%" + companyName + "%'");
			}
			if (!"".equals(companyPhone)&&companyPhone!=null) {
				sql.append(" and lp.company_phone like '%" + companyPhone + "%'");
			}
			if (qiyeId!=0) {
				sql.append(" and lp.qiyeId =" + qiyeId);
			}
			return this.countBySql(sql.toString(), null);
		} catch (JPAException jpae) {
			throw new DaoException("TZAccountDAOImpl.getTZReceiptGoodsTotal  查询非预包装台账总数时, 出现异常！", jpae.getException());
		}
	}
	
	@Override
	public List<LedgerPrepackNoVO> getListLedgerPrepackNo(String orgId,String date)throws DaoException {
		try {
			StringBuilder condition = new StringBuilder();
//			String preDate = date + " 00:00:00";
//			String endDate = date + " 23:59:59";
//			condition.append(" WHERE qiyeId = ?1 and purchase_time >= ?2 and purchase_time <=?3 " );
//			List<LedgerPrepackNo> ledgerPrepackNo = this.getListByCondition( condition.toString(), new Object[]{Long.parseLong(orgId),preDate,endDate});
//			return getLedgerPrepackNoVO(ledgerPrepackNo);
			return this.getListByOrgId(orgId,date);
		} catch (Exception jpae) {
			throw new DaoException("获取非预包装台账失败", jpae.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private List<LedgerPrepackNoVO> getListByOrgId(String orgId, String date) {
		List<LedgerPrepackNoVO> voList = new ArrayList<LedgerPrepackNoVO>();
		try {
			String sql = "SELECT DISTINCT lp.id,lp.product_name,lp.alias,lp.standard,lp.number,lp.purchase_time,lp.company_name,lp.company_phone,lp.supplier,lp.company_address,lp.qiyeId, ";
			       sql+="lp.LIC_RESOURCE_ID,(SELECT ttr.url FROM t_test_resource ttr WHERE ttr.RESOURCE_ID=lp.LIC_RESOURCE_ID) LIC_URL, ";
			       sql+="lp.DIS_RESOURCE_ID,(SELECT ttr.url FROM t_test_resource ttr WHERE ttr.RESOURCE_ID=lp.DIS_RESOURCE_ID) DIS_URL ";
			       sql+="FROM ledger_prepackno lp WHERE lp.qiyeId = ?1 ";
			      if(date != null && !"".equals(date)){
			    	  String preDate = date + " 00:00:00 ";
			    	  String endDate = date + " 23:59:59";
			    	  sql +=" AND lp.purchase_time >= '"+preDate+"' AND lp.purchase_time <='"+endDate+"'  ";
			      }else{
			    	  sql +=" AND lp.purchase_time < (SELECT  MAX(DATE_ADD(t.purchase_time,INTERVAL 1 DAY)) FROM  ledger_prepackno t  WHERE t.qiyeId=?1) " ;
			    	  sql+="  AND lp.purchase_time > (SELECT  MAX(DATE_ADD(t.purchase_time,INTERVAL -1 DAY)) FROM  ledger_prepackno t  WHERE t.qiyeId=?1)";
			      } 
			      sql +=" ORDER BY lp.purchase_time desc";
			       Query query = entityManager.createNativeQuery(sql);
			       query.setParameter(1, orgId);
			       List<Object[]> objs = query.getResultList();
			      for (Object[] obj : objs) {
			    	  LedgerPrepackNoVO vo = new LedgerPrepackNoVO();
			    	  vo.setId(obj[0]==null?null:Long.parseLong(obj[0].toString()));
			    	  vo.setProductName(obj[1]==null?null:obj[1].toString());
			    	  vo.setAlias(obj[2]==null?null:obj[2].toString());
			    	  vo.setStandard(obj[3]==null?null:obj[3].toString());
			    	  vo.setNumber(obj[4]==null?null:obj[4].toString());
			    	  vo.setPurchaseTime(obj[5]==null?null:obj[5].toString());
			    	  vo.setCompanyName(obj[6]==null?null:obj[6].toString());
			    	  vo.setCompanyPhone(obj[7]==null?null:obj[7].toString());
			    	  vo.setSupplier(obj[8]==null?null:obj[8].toString());
			    	  vo.setCompanyAddress(obj[9]==null?null:obj[9].toString());
			    	  vo.setQiyeId(obj[10]==null?null:Long.parseLong(obj[10].toString()));
			    	  vo.getLicResource().setId(obj[11]==null?null:Long.parseLong(obj[11].toString()));
			    	  vo.getLicResource().setUrl(obj[12]==null?null:obj[12].toString());
			    	  vo.getDisResource().setId(obj[13]==null?null:Long.parseLong(obj[13].toString()));
			    	  vo.getDisResource().setUrl(obj[14]==null?null:obj[14].toString());
			    	  voList.add(vo);
				}
			       
		} catch (Exception e) {
			e.printStackTrace();
		}
		return voList;
	}

	private List<LedgerPrepackNoVO> transferVo(List<LedgerPrepackNo> result) throws ParseException {
		List<LedgerPrepackNoVO> list = new ArrayList<LedgerPrepackNoVO>();
		if (result != null && result.size() > 0) {
			list = new ArrayList<LedgerPrepackNoVO>();
			for (int i = 0; i < result.size(); i++) {
				LedgerPrepackNo objs = result.get(i);
				LedgerPrepackNoVO vo = new LedgerPrepackNoVO();
				vo.setId(objs.getId());
				vo.setProductName(objs.getCompanyName());
				vo.setPurchaseTime(objs.getPurchaseTime() != null ? SDFDATETIME.format(SDFDATETIME.parse(objs.getPurchaseTime().toString())) : "");
				vo.setAlias(objs.getAlias());
				vo.setStandard(objs.getStandard());
				vo.setNumber(objs.getNumber());
				vo.setCompanyName(objs.getCompanyName());
				vo.setCompanyPhone(objs.getCompanyPhone());
				vo.setCompanyAddress(objs.getCompanyAddress());
				vo.setSupplier(objs.getSupplier());
				vo.setQiyeId(objs.getQiyeId());
				list.add(vo);
			}
		}
		return list;
	}

	@Override
	public void deleteResource(String sqlColumn, LedgerPrepackNo emty) {
         String sql ="UPDATE ledger_prepackno SET "+sqlColumn+" = NULL WHERE id = ?1 ";		
		 Query query = entityManager.createNativeQuery(sql);
	       query.setParameter(1, emty.getId());
	       query.executeUpdate();
	}

	@Override
	public void deleteResource(Long id) {
		String sql = "DELETE FROM t_test_resource  WHERE resource_id = ?1 ";		
		 Query query = entityManager.createNativeQuery(sql);
	       query.setParameter(1, id);
	       query.executeUpdate();
	}
}
