package com.gettec.fsnip.fsn.service.market;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.vo.report.ReportOfMarketVO;

public interface FtpService {
	
	public Resource mkUploadBusinessPdf(BusinessUnit busUnit, List<ProductionLicenseInfo> ListproLicense) throws Exception;

	/**
	 * 用XML传值方式自动生成pdf
	 * @author LongXianZhen
	 */
	public ByteArrayInputStream mkUploadReportPdf(ReportOfMarketVO report_vo);

}
