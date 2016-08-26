package com.gettec.fsnip.fsn.service.thirdreport;

import java.util.List;

import com.gettec.fsnip.fsn.dao.thirdreport.ThirdReportDao;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.thirdReport.Thirdreport;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.thirdreport.ThirdreportVo;

public interface ThirdReportService extends BaseService<Thirdreport, ThirdReportDao>{

	List<ThirdreportVo> getReportNo(long currrntUserId);

	List<TestResult> getReportDetail(long currrntUserId, String testType);

	long getReportCount(long currrntUserId, String testType);

	void save(Thirdreport thirdreport) throws Exception;

	List<Long> getReportCounts(long currrntUserId, String testType);

	List<String> getStandards(long currrntUserId);

	long getStandardCount(long productId, String testType);

}
