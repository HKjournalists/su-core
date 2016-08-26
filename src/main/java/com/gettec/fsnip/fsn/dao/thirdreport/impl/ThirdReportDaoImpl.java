package com.gettec.fsnip.fsn.dao.thirdreport.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.thirdreport.ThirdReportDao;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.thirdReport.Thirdreport;
import com.gettec.fsnip.fsn.vo.thirdreport.ThirdreportVo;

@Repository
public class ThirdReportDaoImpl extends BaseDAOImpl<Thirdreport> implements ThirdReportDao{

	@Override
	public List<ThirdreportVo> getReportNo(long currrntUserId) {
		String testType = "第三方检测";	
		String sql = "select id,service_order as serviceOrder from test_result where organization = ?1 and test_type = ?2";
		
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, currrntUserId);
		query.setParameter(2, testType);
		@SuppressWarnings("unchecked")
		List<Object[]> objs = (List<Object[]>)query.getResultList();
		List<ThirdreportVo> list = new ArrayList<ThirdreportVo>();
		for (Object[] obj : objs) {
			ThirdreportVo vo = new ThirdreportVo();
			vo.setId(obj==null?null:Long.parseLong(obj[0].toString()));
			vo.setServiceOrder(obj==null?"":obj[1].toString());
			list.add(vo);
		}
		return list;
	}

	//查询报告结果，通过产品ID和检测类型为“第三方检测”
	@Override
	public List<TestResult> getReportDetail(long productID, String testType) {
		String sql = "SELECT create_time,pass FROM test_result tr LEFT JOIN product_instance ip ON ip.product_id = tr.sample_id LEFT JOIN product p ON p.id = ip.product_id WHERE p.id = ?1 AND tr.test_type = ?2";
		
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, productID);
		query.setParameter(2, testType);
		
		List<TestResult> resultList = query.getResultList();
		for (TestResult testResult : resultList) {
			System.out.println(testResult.getTestType());
		}
		
		return resultList;
	}

	//查询随机抽取N个样品,
	@Override
	public long getReportCount(long productID, String testType) {
		String sql = "SELECT count(*) FROM test_result tr LEFT JOIN product_instance ip ON ip.id=tr.sample_id WHERE ip.product_id=?1 AND tr.test_type=?2";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, productID);
		query.setParameter(2, testType);
		
		long count = Long.valueOf(query.getSingleResult().toString());
		
		return count;
	}
	
	//查询第三方检测抽检的次数集合
	@Override
	public List<Long> getReportCounts(long productID, String testType) {
		String sql = "SELECT ttt.report_count FROM t_test_third ttt LEFT JOIN test_result tr ON ttt.test_result_id = tr.id LEFT JOIN product_instance ip ON ip.product_id = tr.sample_id LEFT JOIN product p ON p.id = ip.product_id WHERE p.id = ?1 AND tr.test_type = ?2";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, productID);
		query.setParameter(2, testType);
		
		List<Long> countList = query.getResultList();
		
		return countList;
	}

	//查询检测标准的内容
	@Override
	public List<String> getStandards(long productID) {
		String sql = "SELECT DISTINCT tp.standard  FROM test_result tr LEFT JOIN product_instance ip ON ip.id=tr.sample_id LEFT JOIN product p ON p.id=ip.product_id LEFT JOIN test_property tp ON tp.test_result_id=tr.id WHERE p.id=?1 AND tr.test_type='第三方检测'";
		Query query = entityManager.createNativeQuery(sql).setParameter(1, productID);
		List<String> standards = query.getResultList();
		
		return standards;
	}

	//查询检测标准的总条数，----------上面已经拿到了检测标准的集合，检测标准的总数就是集合的长度
	@Override
	public long getStandardCount(long productId, String testType) {
		//SELECT COUNT(*) FROM test_result a WHERE a.standard  IN(SELECT DISTINCT t.standard FROM test_result t WHERE t.test_type = '第三方检测')  AND a.test_type='第三方检测'"
		String sql = "";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, productId);
		query.setParameter(2, testType);
		
		long standardCount = (Long) query.getSingleResult();
		
		return standardCount;
	}

	
	
	
	
	
}
