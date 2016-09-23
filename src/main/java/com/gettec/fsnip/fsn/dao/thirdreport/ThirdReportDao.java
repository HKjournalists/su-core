package com.gettec.fsnip.fsn.dao.thirdreport;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.thirdReport.Thirdreport;
import com.gettec.fsnip.fsn.vo.thirdreport.ThirdreportVo;

public interface ThirdReportDao extends BaseDAO<Thirdreport>{

	List<ThirdreportVo> getReportNo(long currrntUserId);

	List<TestResult> getReportDetail(long currrntUserId, String testType);

	long getReportCount(long currrntUserId, String testType);

	List<Long> getReportCounts(long currrntUserId, String testType);

	List<String> getStandards(long currrntUserId);

	long getStandardCount(long productId, String testType);

}
