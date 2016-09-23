package com.gettec.fsnip.fsn.dao.test.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.test.RiskAssessmentDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.test.RiskAssessment;

/**
 * RiskAssessment customized operation implementation
 * 
 * @author ZhaWanNeng
 */
@Repository(value = "riskAssessmentDAO")
public class RiskAssessmentDAOImpl extends BaseDAOImpl<RiskAssessment>
		implements RiskAssessmentDAO {
	 /**
     * 根据检测项目id获取风险指数的历史数据 
     * @author ZhaWanNeng
     */
    @SuppressWarnings("unchecked")
	public Long getRiskAssessmentId(Long propertyid) throws DaoException {
        String sql = "SELECT DISTINCT ra.id FROM risk_assessment ra " +
        		     "LEFT JOIN risk_to_property rtp ON rtp.risk_id = ra.id WHERE rtp.property_id = ?1 ;";
        try {
            List<Object> result = entityManager.createNativeQuery(sql)
                    .setParameter(1, propertyid)
                    .getResultList();
            Object obj = result != null && result.size() > 0 ? result.get(0): null;
            return obj == null ? null : Long.valueOf(obj.toString());
        } catch (Exception e) {
            throw new DaoException("TestResultDAOImpl.getRiskAssessmentId()-->"+"根据条件查询产品报告出错", e);
        }
    }

}