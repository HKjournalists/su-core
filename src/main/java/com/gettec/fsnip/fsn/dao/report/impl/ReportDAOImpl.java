package com.gettec.fsnip.fsn.dao.report.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.report.ReportDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.vo.report.ReviewReportOfSuperVO;

@Repository(value="reportDAO")
public class ReportDAOImpl implements ReportDAO{
	
	@PersistenceContext private EntityManager entityManager;
	
	/**
	 * 获取报告总数(不包括商超退回的报告)
	 * @author Zhanghui 2015/4/9
	 */
	@Override
	public long countOfReports(Long productId, Long fromBusId, Long toBusId, String testType) {
		try {
			if(toBusId == null){
				return 0;
			}
			
			String sql = "SELECT COUNT(*) FROM test_result tr WHERE tr.publish_flag <> 5 AND tr.del = 0 ";
			
			if(testType!=null && !"".equals(testType)){
				sql += "AND tr.test_type = ?1 ";
			}
			
			sql += "AND EXISTS ( " +
						"SELECT  1  FROM product_instance  pri " +
						"INNER JOIN t_meta_from_to_business tb ON pri.product_id=tb.pro_id " +
						"INNER JOIN t_meta_enterprise_to_provider tc ON tc.`business_id`= tb.to_bus_id  AND tc.`provider_id`=tb.`from_bus_id` " +
						"WHERE tb.del = 0 AND tb.to_bus_id = ?2 AND  tr.`sample_id` = pri.`id`";
			
			if(productId != null){
				sql += "AND tb.pro_id = ?3 ";
			}
			
			if(fromBusId != null){
				sql += "AND tb.from_bus_id = ?4 ";
			}
			
			sql += ")";
			
			Query query = entityManager.createNativeQuery(sql);
			
			if(testType!=null && !"".equals(testType)){
				query.setParameter(1, testType);
			}
			
			query.setParameter(2, toBusId);
			
			if(productId != null){
				query.setParameter(3, productId);
			}
			
			if(fromBusId != null){
				query.setParameter(4, fromBusId);
			}
			
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 功能描述：获取商超下供应商录入的报告数量
	 * @author ZhangHui 2015/7/6
	 */
	@Override
	public long countOfReportsOfMyDealer(Long fromBusId, Long toBusId) {
		try {
//			if(toBusId == null){
//				return 0;
//			}
			
			String sql = "SELECT COUNT(*) FROM test_result tr WHERE tr.publish_flag NOT IN('5','7') AND tr.del = 0 ";
			
			sql += "AND EXISTS ( " +
						"SELECT  1  FROM product_instance  pri " +
						"INNER JOIN t_meta_from_to_business tb ON pri.product_id=tb.pro_id " +
						"INNER JOIN business_unit bus ON bus.id = tb.from_bus_id " +
						"INNER JOIN t_meta_enterprise_to_provider tc ON tc.`business_id`= tb.to_bus_id  AND tc.`provider_id`=tb.`from_bus_id` " +
						"WHERE DATEDIFF(pri.expiration_date , SYSDATE()) > 0 AND tb.del = 0  " ;
						if(toBusId!=null){
							sql +=" AND tb.to_bus_id = ?1 ";
						}
						sql +=" AND  tr.`sample_id` = pri.`id` AND bus.organization = tr.organization ";
			
			if(fromBusId != null){
				sql += "AND tb.from_bus_id = ?2 ";
			}
			
			sql += ")";
			
			Query query = entityManager.createNativeQuery(sql);
			if(toBusId!=null){
			query.setParameter(1, toBusId);
			}
			if(fromBusId != null){
				query.setParameter(2, fromBusId);
			}
			
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取报告发布标记
	 * @author Zhanghui 2015/4/11
	 */
	@Override
	public String getPubFlagById(Long reportId) throws DaoException {
		try {
			String sql ="SELECT publish_flag FROM test_result WHERE id = ?1 AND del = 0";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, reportId);
			return query.getSingleResult().toString();
		} catch (Exception e) {
			throw new DaoException("ReportDAOImpl.getPubFlagById() 出现异常！", e);
		}
	}

	/**
	 * 获取待处理报告数量
	 * @author Zhanghui 2015/4/11
	 */
	@Override
	public long countOfOnHandleReports(Long productId, Long fromBusId,
			Long toBusId, String testType) {
		try {
//			if(toBusId == null){
//				return 0;
//			}
			
			String condition_0 = "WHERE publish_flag = '4' ";
			if(testType != null && !"".equals(testType)){
				condition_0 += "AND test_type = ?1 ";
			}
			
			String condition_1 = "";
			if(toBusId != null){
				condition_1 += "AND tb.to_bus_id = ?2 ";
			}
			if(productId != null){
				condition_1 += "AND tb.pro_id=?3 ";
			}
			if(fromBusId != null){
				condition_1 += "AND tb.from_bus_id = ?4 ";
			}
			
			String sql ="SELECT COUNT(*) FROM test_result tr ";
			
			if(fromBusId != null){
				sql += "INNER JOIN business_unit bus ON bus.organization = tr.organization AND bus.id = ?5 ";
			}
			
			sql += condition_0 + " AND tr.del = 0 " +
				   "AND EXISTS ( " +
							"SELECT  1  FROM product_instance  pri " +
							"INNER JOIN t_meta_from_to_business tb ON pri.product_id=tb.pro_id " +
							"INNER JOIN t_meta_enterprise_to_provider tc ON tc.`business_id`= tb.to_bus_id  AND tc.`provider_id`=tb.`from_bus_id` " +
							"INNER JOIN business_unit bus ON bus.id = tc.provider_id " +
							"WHERE DATEDIFF(pri.expiration_date , SYSDATE()) > 0 AND tb.del = 0 " + condition_1 + " AND  tr.`sample_id`=pri.`id`  AND bus.organization = tr.organization)";
			
			Query query = entityManager.createNativeQuery(sql);
			
			if(testType != null && !"".equals(testType)){
				query.setParameter(1, testType);
			}
			if(toBusId != null){
				query.setParameter(2, toBusId);
			}
			if(productId != null){
				query.setParameter(3, productId);
			}
			if(fromBusId != null){
				query.setParameter(4, fromBusId);
				query.setParameter(5, fromBusId);
			}
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取所有报告总数（不包括被商超退回的报告）
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public long countAllOfReports(Long productId, String testType) {
		try {
			if(productId==null && (testType==null || "".equals(testType))){
				return 0;
			}
			
			String sql ="SELECT COUNT(*) FROM test_result tr " +
						"LEFT JOIN product_instance pri ON tr.sample_id = pri.id AND pri.product_id = ?1 " +
						"WHERE DATEDIFF(pri.expiration_date , SYSDATE()) > 0 AND  tr.del = 0 AND tr.test_type = ?2 AND tr.publish_flag NOT IN ('5','7') AND pri.product_id = ?3";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, productId);
			query.setParameter(2, testType);
			query.setParameter(3, productId);
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取所有待处理报告
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public long countOfAllOnHandleReports(Long productId, String testType, Long fromBusId, Long toBusId) {
		try {
			if(productId==null && (testType==null || "".equals(testType))){
				return 0;
			}
			
			String sql ="SELECT COUNT(*) FROM test_result tr " +
						"LEFT JOIN product_instance pri ON tr.sample_id = pri.id AND pri.product_id = ?1 ";
			
			if(fromBusId != null){
				sql += "INNER JOIN business_unit but ON but.organization = tr.organization AND but.id = ?2 ";
			}
			
			sql +=	"WHERE DATEDIFF(pri.expiration_date , SYSDATE()) > 0 AND   tr.del = 0 AND tr.test_type = ?3 AND tr.publish_flag = '4' AND pri.product_id = ?4 " +
					"AND EXISTS ( " +
						"SELECT 1 FROM t_meta_from_to_business f2b " +
						"INNER JOIN business_unit bus ON f2b.from_bus_id = bus.id " +
						"WHERE f2b.pro_id = ?5 AND ";
			if (toBusId != null){
			sql+="f2b.to_bus_id = ?6 AND ";
			}
			sql +="bus.organization = tr.organization AND f2b.del = 0" +
					")";
			
			Query query = entityManager.createNativeQuery(sql);
			
			query.setParameter(1, productId);
			
			if(fromBusId != null){
				query.setParameter(2, fromBusId);
			}
			
			query.setParameter(3, testType);
			query.setParameter(4, productId);
			query.setParameter(5, productId);
			if (toBusId != null){
			  query.setParameter(6, toBusId);
			}
			
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取所有报告（不包括被商超退回的报告）
	 * @author ZhangHui 2015/5/7
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReviewReportOfSuperVO> getListOfAllReportByPage(Long productId, Long from_bus_org,
			String testType, int page, int pageSize) throws DaoException {
		try {
			if(productId==null && (testType==null || "".equals(testType))){
				return null;
			}
			
			int begin = (page-1)*pageSize;
			
			String sql ="SELECT tr.id,tr.service_order,pin.batch_serial_no,bui.`name`,pin.production_date,tr.publish_flag,tr.fullPdfPath,tr.organization,";
			
			if(from_bus_org != null){
				sql += "(CASE tr.organization WHEN ?1 THEN 0 ELSE 1 END) ord1, ";
			}else{
				sql += "1,";
			}
			
			sql += "(CASE tr.publish_flag WHEN '4' THEN 0 ELSE 1 END) ord2,tr.check_org_name " +
				"FROM test_result tr " +
				"LEFT JOIN product_instance pin ON tr.sample_id = pin.id AND pin.product_id = ?2 " +
				"INNER JOIN business_unit bui ON pin.producer_id = bui.id " +
				"WHERE DATEDIFF(pin.expiration_date , SYSDATE()) > 0 AND tr.del = 0 AND tr.test_type = ?3 AND tr.publish_flag NOT IN ('5','7') AND pin.product_id = ?4 " +
				"ORDER BY ";
			
			if(from_bus_org != null){
				sql += "ord1,";
			}
				
			sql += "ord2 ASC LIMIT " + begin + "," + pageSize;
			
			Query query = entityManager.createNativeQuery(sql);
			
			if(from_bus_org != null){
				query.setParameter(1, from_bus_org);
			}
			
			query.setParameter(2, productId);
			query.setParameter(3, testType);
			query.setParameter(4, productId);
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<ReviewReportOfSuperVO> vos = new ArrayList<ReviewReportOfSuperVO>();
			if(result != null){
				for(Object[] obj : result){
					ReviewReportOfSuperVO vo = new ReviewReportOfSuperVO(((BigInteger)obj[0]).longValue(), 
							obj[1]==null?"":obj[1].toString(), obj[2]==null?"":obj[2].toString(),
							obj[3]==null?"":obj[3].toString(), obj[4]==null?null:(Date)obj[4], 
							obj[5]==null?"":obj[5].toString(), obj[6]==null?"":obj[6].toString(),
							obj[7]==null?null:((BigInteger)obj[7]).longValue(),
							Integer.parseInt(String.valueOf(obj[8].toString())),obj[10]==null?"":obj[10].toString());
					vos.add(vo);
				}
			}
			return vos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ReportDAOImpl.getListOfReport() 出现异常！", e);
		}
	}
}
