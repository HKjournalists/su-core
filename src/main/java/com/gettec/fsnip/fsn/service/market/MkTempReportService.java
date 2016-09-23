package com.gettec.fsnip.fsn.service.market;

import java.util.List;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.MkTempReport;
import com.gettec.fsnip.fsn.model.market.MkTempReportItem;
import com.gettec.fsnip.fsn.vo.report.ReportOfMarketVO;

public interface MkTempReportService {
	
	/**
	 * 功能描述：保存报告信息
	 * @throws ServiceException 
	 * @author ZhangHui 2015/6/9
	 */
	public ReportOfMarketVO save(ReportOfMarketVO report_vo, String realUserName,
			Long myRealOrgnizationId) throws ServiceException;

	/**
	 * 背景：在报告录入页面
	 * 功能描述：按登录用户信息获取一条缓存报告信息
	 * @throws ServiceException 
	 * @author ZhangHui 2015/6/4
	 */
	public ReportOfMarketVO getTempReport(String userName, Long myOrgnization)throws ServiceException;

	void clearTemp(String realUserName, Long myRealOrgnizationId) throws ServiceException;
    /**
     * 背景：数据结构化点击编辑进入页面
     * 描述：获取登录用户下某一报告下的检测项目
     *  authar：wubiao
     * date:2015.10.17 11:07 
     * @param orderNo  报告编号
     * @param userName 登录用户
     * @param page  
     * @param pageSize
     * @return
     */
	public List<MkTempReportItem> getMkTempReportItemList(String orderNo,String userName,int page,int pageSize);
	/**
     * 背景：数据结构化点击编辑进入页面
     * 描述：获取登录用户下某一报告下的检测项目
     * authar：wubiao
     * date:2015.10.17 11:07 
     * @param orderNo  报告编号
     * @param userName 登录用户
     * @return 检测项目总数
     */
	public long getMkTempReportItemCount(String orderNo, String realUserName);

	public boolean deleteMkTempReportItem(long id);

	public MkTempReport getTempReportOrderNo(String serviceOrder);
}
