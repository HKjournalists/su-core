package com.gettec.fsnip.fsn.dao.waste.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.waste.WasteDisposaDao;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.waste.WasteDisposa;
import com.gettec.fsnip.fsn.vo.waste.WasteDisposaVO;
import com.lhfs.fsn.vo.business.BussinessUnitVO;

@Repository(value="wasteDisposaDAO")
public class WasteDisposaDaoImpl extends BaseDAOImpl<WasteDisposa>  implements WasteDisposaDao{
	static SimpleDateFormat SDFTIME = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat SDFDATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public List<WasteDisposaVO> loadWasteDisposa(int page, int pageSize,
			String handler,long qiyeId) throws DaoException {
		try {
			StringBuilder condition = new StringBuilder();
			condition.append(" WHERE 1=1 ");
			if (!handler.equals("")) {
				condition.append(" and handler like '%" + handler + "%'");
			}
			if (qiyeId!=0) {
				condition.append(" and qiyeId = "+qiyeId );
			}
			List<WasteDisposa> wasteDisposa = this.getListByPage(page, pageSize, condition.toString());
			return getWasteDisposaVO(wasteDisposa);
		} catch (JPAException jpae) {
			throw new DaoException("获取废弃物处理失败", jpae.getException());
		}
	}
	
	private List<WasteDisposaVO> getWasteDisposaVO(List<WasteDisposa> result) {
		List<WasteDisposaVO> list = new ArrayList<WasteDisposaVO>();
		if (result != null && result.size() > 0) {
			list = new ArrayList<WasteDisposaVO>();
			for (int i = 0; i < result.size(); i++) {
				WasteDisposa objs = result.get(i);
				WasteDisposaVO vo = new WasteDisposaVO();
				vo.setId(objs.getId());
				try {
					vo.setHandleTime(objs.getHandleTime() != null ? SDFTIME.format(SDFTIME.parse(objs.getHandleTime().toString())) : "");
				} catch (ParseException e) {
					e.printStackTrace();
				}
				vo.setParticipation(objs.getParticipation());
				vo.setDestory(objs.getDestory());
				vo.setHandleNumber(objs.getHandleNumber());
				vo.setHandler(objs.getHandler());
				vo.setHandleWay(objs.getHandleWay());
				vo.setQiyeId(objs.getQiyeId());
				list.add(vo);
			}
		}
		return list;
	}
	
	@Override
	public long getWasteTotal(String handler,long qiyeId) throws DaoException {
		try {
			StringBuilder sql = new StringBuilder();
			String sqlStr = " select count(*) from waste_dispoa wd where 1=1";
			sql.append(sqlStr);
			if (!"".equals(handler)) {
				sql.append(" and wd.handler like '%" + handler + "%'");
			}
			if (qiyeId!=0) {
				sql.append(" and wd.qiyeId = " + qiyeId);
			}
			return this.countBySql(sql.toString(), null);
		} catch (JPAException jpae) {
			throw new DaoException("TZAccountDAOImpl.getTZReceiptGoodsTotal  按照当组织机构ID获取进货台账总数时, 出现异常！", jpae.getException());
		}
	}
	
	@Override
	public List<WasteDisposaVO> getListWaste(String orgId,String date) throws DaoException {
		try {
			List<WasteDisposa> wasteDisposa = null;
			StringBuilder condition = new StringBuilder();
			boolean flag = false;
			if(date!=null&&!"".equals(date)){
				String preDate = date + " 00:00:00";
				String endDate = date + " 23:59:59";
				condition.append(" WHERE qiyeId = ?1 AND handle_time >= ?2 AND handle_time <=?3 " );
				wasteDisposa = this.getListByCondition(condition.toString(), new Object[]{Long.parseLong(orgId),preDate,endDate});
				flag  = true;
			}else{
				String[] dataStr = this.getDateMax(orgId);
				if(dataStr != null&&dataStr.length==2){
				condition.append(" WHERE qiyeId = ?1  AND handle_time >?2 AND handle_time <?3 " );
				wasteDisposa = this.getListByCondition(condition.toString(), new Object[]{Long.parseLong(orgId),dataStr[0],dataStr[1]});
				}
			}
			return transferVo(wasteDisposa,flag);
		} catch (JPAException jpae) {
			throw new DaoException("获取废弃物处理失败", jpae.getException());
		} catch (ParseException e) {
			throw new DaoException("获取废弃物处理失败", e);
		}
	}
	@SuppressWarnings("unchecked")
	private String[] getDateMax(String orgId) {
		String sql = "SELECT id,MAX(DATE_FORMAT(DATE_ADD(t.handle_time,INTERVAL -1 DAY),'%Y-%c-%d')) preDate,MAX(DATE_FORMAT(DATE_ADD(t.handle_time,INTERVAL 1 DAY),'%Y-%c-%d')) endDate FROM  waste_dispoa t  WHERE t.qiyeId=?1";
		Query query = entityManager.createNativeQuery(sql);
	       query.setParameter(1, orgId);
	       List<Object[]> objs = query.getResultList();
	       String[] dateStr = null;;  
	       if(objs.size()>0){
	    	 for (Object[] obj : objs) {
	    		 if(obj[1]!=null){
	    			 dateStr = new String[2]; 
	    			 dateStr[0] = obj[1].toString();
	    			 dateStr[1] = obj[2].toString();
	    		 }
				break;
			 }
	       }
		return dateStr;
	}
	private List<WasteDisposaVO> transferVo(List<WasteDisposa> result,boolean flag) throws ParseException {
		List<WasteDisposaVO> list = new ArrayList<WasteDisposaVO>();
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				if(i==1&&!flag){ break;}
				WasteDisposa objs = result.get(i);
				WasteDisposaVO vo = new WasteDisposaVO();
				vo.setId(objs.getId());
				vo.setHandleTime(objs.getHandleTime() != null ? SDFDATETIME.format(SDFDATETIME.parse(objs.getHandleTime().toString())) : "");
				vo.setDestory(objs.getDestory());
				vo.setHandleNumber(objs.getHandleNumber());
				vo.setHandler(objs.getHandler());
				vo.setHandleWay(objs.getHandleWay());
				vo.setQiyeId(objs.getQiyeId());
				list.add(vo);
			}
		}
		return list;
	}

	@Override
	public BussinessUnitVO getBussinessUnitVO(String licenseNo, String qsNo,
			String businessName) {
		String  sql = "SELECT DISTINCT bus.id,bus.license_no,bus.name,pli.qs_no FROM  business_unit bus ";
				sql +=" LEFT JOIN  product_to_businessunit ptb ON ptb.business_id = bus.id ";
				sql +=" LEFT JOIN  production_license_info pli ON pli.id = ptb.qs_id ";
				sql +=" WHERE 1= 1 ";
				boolean flag = false;
				if(licenseNo!=null&&!"".equals(licenseNo)){
					sql +=" AND bus.license_no = '"+licenseNo+"' ";
					flag =true;
				}
				if(qsNo!=null&&!"".equals(qsNo)&&flag){
					sql +=" OR pli.qs_no = '"+qsNo+"' ";
				}else if(qsNo!=null&&!"".equals(qsNo)){
					sql +=" AND pli.qs_no = '"+qsNo+"' ";
					flag =true;
				}
				if(businessName!=null&&!"".equals(businessName)&&flag){
					sql +=" OR bus.name='"+businessName+"' ";
				}else if(businessName!=null&&!"".equals(businessName)){
					sql +=" AND bus.name = '"+businessName+"' ";
				}
		   Query query = entityManager.createNativeQuery(sql);
	       @SuppressWarnings("unchecked")
		List<Object[]> objs = query.getResultList();
	       BussinessUnitVO vo = null;
	       if(objs.size()>0){
	    	  for (Object[] obj : objs) {
	    		   vo = new BussinessUnitVO();
	    		   vo.setId(obj[0]==null?null:Long.parseLong(obj[0].toString())) ;
	    		   vo.setLicenseNo(obj[1]==null?null:obj[1].toString());
	    		   vo.setName(obj[2]==null?null:obj[2].toString());
	    		   break;
			 }
	       }
		return vo;
	}

}
