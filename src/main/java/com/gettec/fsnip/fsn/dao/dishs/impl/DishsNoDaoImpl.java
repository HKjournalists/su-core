package com.gettec.fsnip.fsn.dao.dishs.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.dishs.DishsNoDao;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.dishs.DishsNo;
import com.gettec.fsnip.fsn.vo.dishs.DishsNoShowVO;

@Repository(value="dishsNoDAO")
public class DishsNoDaoImpl extends BaseDAOImpl<DishsNo> implements DishsNoDao {

	@Override
	public List<DishsNo> loadWasteDisposa(int page, int pageSize,
			String dishsName,long qiyeId) throws DaoException {
		try {
			StringBuilder condition = new StringBuilder();
			condition.append(" WHERE 1=1 ");
			if (!dishsName.equals("")) {
				condition.append(" and dishsName like '%" + dishsName + "%'");
			}
			if (qiyeId!=0) {
				condition.append(" and qiyeId = "+qiyeId);
			}
			condition.append(" ORDER BY id DESC");
			return this.getListByPage(page, pageSize, condition.toString());
		} catch (JPAException jpae) {
			throw new DaoException("获取菜品失败", jpae.getException());
		} 
	}

	@Override
	public long getDishsNoTotal(String dishsName,long qiyeId) throws DaoException {
		try {
			StringBuilder sql = new StringBuilder();
			String sqlStr = " select count(*) from dishs_no dn where 1=1 ";
			sql.append(sqlStr);
			if (!"".equals(dishsName)) {
				sql.append(" and dn.dishs_name like '%" + dishsName + "%'");
			}
			if (qiyeId!=0) {
				sql.append(" and dn.qiyeId =" + qiyeId);
			}
			return this.countBySql(sql.toString(), null);
		} catch (JPAException jpae) {
			throw new DaoException("TZAccountDAOImpl.getTZReceiptGoodsTotal  获取非预包装菜品总数失败！", jpae.getException());
		}
	}
	
	@Override
	public List<DishsNo> getListDishsNo(String orgId) throws DaoException {
		try {
			StringBuilder condition = new StringBuilder();
			condition.append(" WHERE showFlag =1 AND qiyeId = ?1  ORDER BY id DESC" );
			return  this.getListByCondition(condition.toString(), new Object[]{Long.parseLong(orgId)});
		} catch (JPAException jpae) {
			throw new DaoException("获取菜品失败", jpae.getException());
		} 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DishsNoShowVO> loadDishsNoShowVO(int page, int pageSize,
			String showTime, String dishsName, Long fromBusId,boolean flag) {
		
		String sql = " ";
			if(flag){
				sql +="SELECT dn.id,dn.dishs_name,dn.alias,dn.baching,dn.qiyeId,dn.remark,dns.show_id,dns.show_flag,dns.sample_flag,dns.show_time,  ";
				sql +=" (SELECT MAX(resource_id) FROM dishsno_to_resource dtr WHERE dtr.dishsno_id=dn.id)  RESOURCE_ID ";	
				sql +="  FROM  dishs_no  dn  ";	
			    sql +="  INNER JOIN ";
			    sql+=" dishs_no_show dns ON dn.id = dns.dishs_no_id ";
			}else{
				sql += "SELECT dn.id,dn.dishs_name,dn.alias,dn.baching,dn.qiyeId,dn.remark,";
				sql +=" (SELECT MAX(dns.show_id) FROM dishs_no_show dns WHERE  dns.dishs_no_id =dn.id   ";	
				sql += this.getConfing(showTime)+") show_id,";	
				sql +=" (SELECT MAX(dns.show_flag) FROM dishs_no_show dns WHERE  dns.dishs_no_id =dn.id   ";	
				sql += this.getConfing(showTime)+") show_flag,";	
				sql +=" (SELECT MAX(dns.sample_flag) FROM dishs_no_show dns WHERE  dns.dishs_no_id =dn.id   ";	
				sql += this.getConfing(showTime)+") sample_flag,";	
				sql +=" (SELECT MAX(dns.show_time) FROM dishs_no_show dns WHERE  dns.dishs_no_id =dn.id   ";	
				sql += this.getConfing(showTime)+") show_time,";	
				sql +=" (SELECT MAX(resource_id) FROM dishsno_to_resource dtr WHERE dtr.dishsno_id=dn.id)  RESOURCE_ID ";	
				sql +=" FROM  dishs_no  dn  ";	
			}
			sql+=" WHERE  dn.qiyeId = ?1  ";
			if(flag){
				sql += this.getConfing(showTime);
				sql+=" AND  dns.show_flag = 1  ";
			}
			if(dishsName!=null&&!"".equals(dishsName)){
				sql+=" AND dn.dishs_name LIKE '%"+dishsName+"%'" ;
			}
		Query query = entityManager.createNativeQuery(sql);
		      query.setParameter(1, fromBusId);
		      // 判断是否分页查询
		      if (page > 0) {
		    	  query.setFirstResult((page - 1) * pageSize);
		    	  query.setMaxResults(pageSize);
		      }
		      List<Object[]> objs = query.getResultList();
		      List<DishsNoShowVO> dishsList = new ArrayList<DishsNoShowVO>();
		      for (Object[] obj : objs) {
		    	  DishsNoShowVO e = new DishsNoShowVO();
		    	  e.setId(obj[0]==null?null:Long.parseLong(obj[0].toString()));
		    	  e.setDishsName(obj[1]==null?null:obj[1].toString());
		    	  e.setAlias(obj[2]==null?null:obj[2].toString());
		    	  e.setBaching(obj[3]==null?null:obj[3].toString());
		    	  e.setQiyeId(obj[4]==null?null:Long.parseLong(obj[4].toString()));
		    	  e.setRemark(obj[5]==null?null:obj[5].toString());
		    	  e.setShowId(obj[6]==null?null:Long.parseLong(obj[6].toString()));
		    	  e.setShowFlag(obj[7]==null?null:obj[7].toString());
		    	  e.setSampleFlag(obj[8]==null?null:obj[8].toString());
		    	  e.setShowTime(obj[9]==null?null:obj[9].toString());
		    	  e.setResourceId(obj[10]==null?null:Long.parseLong(obj[10].toString()));
		    	  dishsList.add(e);
				
			 }
		return dishsList;
	}

	private String getConfing(String showTime) {
		String sql = "";
		if(showTime!=null&&!"".equals(showTime)){
			sql+=" AND dns.show_time = '"+showTime+"'" ;
		}else{
			sql+=" AND dns.show_time = DATE_FORMAT(NOW(),'%Y-%m-%d') " ;
		}
		return sql;
	}

	@Override
	public long getDishsNoShowTotal(String showTime, String dishsName,
			Long fromBusId, boolean flag) {
		String sql = "";
			if(flag){
				sql+=" SELECT COUNT(dns.show_id)  FROM  dishs_no  dn  ";	
			    sql+="  INNER JOIN ";
			    sql+=" dishs_no_show dns ON dn.id = dns.dishs_no_id ";	
			}else{
				sql+=" SELECT COUNT(dn.id) FROM  dishs_no  dn  ";	
			}
			sql+=" WHERE  dn.qiyeId = ?1  ";
			if(flag){
				sql += this.getConfing(showTime);
				sql+=" AND  dns.show_flag = 1  ";
			}
			if(dishsName!=null&&!"".equals(dishsName)){
				sql+=" AND dn.dishs_name LIKE '%"+dishsName+"%'" ;
			}
		Query query = entityManager.createNativeQuery(sql);
		      query.setParameter(1, fromBusId);
		Object rtn = query.getSingleResult();
		return rtn == null ? 0 : Long.parseLong(rtn.toString());
	}

}
