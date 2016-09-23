package com.gettec.fsnip.fsn.service.statistics;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.vo.BusinessStaVO;
import com.gettec.fsnip.fsn.vo.ProductStaVO;





/**
 * ProductPoll service
 * 
 * @author 
 */
public interface BusinessStatisticsService {

	/**
	 * 根据条件（企业名称、企业类型、注册时间）分页查询企业集合
	 * @param page 第几页
	 * @param pageSize 每页显示条数
	 * @param businessName 企业名称
	 * @param businessType 企业类型
	 * @param startDate 企业注册起始时间 
	 * @param endDate 企业注册结束时间
	 * @return List<BusinessStaVO>
	 * @throws ServiceException
	 * @author LongXianZhen
	 */
	List<BusinessStaVO> getbusinessStaListByConfigure(int page, int pageSize,String businessName,
			String businessType, String startDate, String endDate)throws ServiceException;
	/**
	 * 根据条件查询某个企业下产品发布报告数的统计
	 * @param page 第几页
	 * @param pageSize 每页显示条数
	 * @param businessId 企业ID
	 * @param productName 产品名称
	 * @param barcode 条形码
	 * @param startDate 报告发布起始时间
	 * @param endDate 报告发布结束时间
	 * @return List<ProductStaVO>
	 * @throws ServiceException
	 * @author LongXianZhen
	 */
	List<ProductStaVO> getProductStaListByConfigure(int page, int pageSize,
			Long businessId, String productName, String barcode,
			String startDate, String endDate)throws ServiceException;
	/**
	 * 根据条件查询某个企业下产品总数
	 * @param businessId 企业ID
	 * @param productName 产品名称
	 * @param barcode 条形码
	 * @return Long
	 * @throws ServiceException
	 * @author LongXianZhen
	 */
	Long getProductStaCountByConfigure(Long businessId, String productName,
			String barcode)throws ServiceException;
    /**
     * 根据条件（企业名称、企业类型、注册时间）查询企业总数
     * @param businessName 企业名称
     * @param businessType 企业类型
     * @param startDate 企业注册起始时间
     * @param endDate 企业注册结束时间
     * @return Long
     * @throws ServiceException
     * @author LongXianZhen
     */
	Long getBusinessStaCountByConfigure(String businessName,
			String businessType, String startDate, String endDate)throws ServiceException;

	HSSFWorkbook downBusinessExcel(List<BusinessStaVO> businessSta, String businessName,
			String businessType, String startDate, String endDate)throws ServiceException;

	HSSFWorkbook downProductExcel(List<ProductStaVO> productStas,
			String productName, String barcode, String startDate,
			String endDate, Long businessId)throws ServiceException;
	/**
	 * 查询企业下的产品信息
	 * @param businessId 企业ID
	 * @param productName 产品名称
	 * @param barcode   产品条形码
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @param page     
	 * @param pageSize
	 * author:wubiao
	 * date : 2015.12.17 16:05
	 * @return
	 */
	List<ProductStaVO> getProductStaListByConfigureData(Long businessId,
			String productName, String barcode, String startDate,
			String endDate, int page, int pageSize);
	/**
	 * 查询企业下的产品数量
	 * @param businessId 企业ID
	 * @param productName 产品名称
	 * @param barcode   产品条形码
	 * author:wubiao
	 * date : 2015.12.17 16:05
	 * @return
	 */
	Long getProductStaCountByConfigureData(Long businessId, String productName,
			String barcode);



}