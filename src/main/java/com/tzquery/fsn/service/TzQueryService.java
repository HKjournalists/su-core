/**
 * 
 */
package com.tzquery.fsn.service;

import org.springframework.ui.Model;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.tzquery.fsn.vo.TzQueryRequestParamVO;

/**
 * @author ChenXiaolin 2015-11-30
 */
public interface TzQueryService {

	/**
	 * 根据查询条件获取产品信息列表
	 * @author ChenXiaolin 2015-11-30
	 * @param paramVO 
	 * @param model 
	 */
	Model productQuery(Model model, TzQueryRequestParamVO paramVO) throws ServiceException;
	
	/**
	 * 根据产品ID获取产品详情
	 * @author ChenXiaolin 2015-12-02
	 * @param model
	 * @param proId
	 * @return
	 */
	Model getProDetail(Model model, String proId) throws ServiceException;

	/**
	 * 根据产品ID获取报告信息列表
	 * @author ChenXiaolin 2015-12-01
	 * @param model
	 * @param paramVO
	 * @return
	 */
	Model lookReport(Model model, TzQueryRequestParamVO paramVO)throws ServiceException;

	/**
	 * 根据产品ID分页获取销售企业列表
	 * @author ChenXiaolin 2015-12-02
	 * @param model
	 * @param paramVO
	 * @return
	 */
	Model getSaleBusiness(Model model, TzQueryRequestParamVO paramVO)throws ServiceException;

	/**
	 * 根据企业ID和产品ID获取销售企业中的交易明细
	 * @author ChenXiaolin 2015-12-03
	 * @param model
	 * @param paramVO
	 * @return
	 */
	Model getProQueryTransDetailList(Model model, TzQueryRequestParamVO paramVO)throws ServiceException;

	/**
	 * 根据企业名称获取台账信息列表
	 * @author ChenXiaolin 2015-12-04
	 * @param model
	 * @param paramVO
	 * @return
	 */
	Model getAccountInfo(Model model, TzQueryRequestParamVO paramVO)throws ServiceException;

	/**
	 * 获取企业查询中台账的交易明细列表
	 * @author ChenXiaolin 2015-12-04
	 * @param model
	 * @param paramVO
	 * @return
	 */
	Model getBusQueryTransDetail(Model model, TzQueryRequestParamVO paramVO)throws ServiceException;

	/**
	 * 根据企业名称获取该企业销售的产品列表
	 * @author ChenXiaolin 2015-12-14
	 * @param model
	 * @param paramVO
	 * @return
	 */
	Model getBusQueryProList(Model model, TzQueryRequestParamVO paramVO)throws ServiceException;

	/**
	 * 原材料信息接口--根据企业名称获取原材料信息列表
	 * @param model
	 * @param paramVO
	 * @return
     */
	Model getRawMaterialInfo(Model model, TzQueryRequestParamVO paramVO,int type)throws ServiceException;

	/**
	 * 根据企业名称获取人员信息列表
	 * @param model
	 * @param paramVO
     * @return
     */
	Model getMemberInfo(Model model, TzQueryRequestParamVO paramVO)throws ServiceException;

	/**
	 * 根据企业名称获取设备信息列表
	 * @param model
	 * @param paramVO
     * @return
     */
	Model getFacilityInfo(Model model, TzQueryRequestParamVO paramVO)throws ServiceException;

	/**
	 * 根据企业名称获取规模信息列表
	 * @param model
	 * @param paramVO
	 * @return
     */
	Model getOperateInfo(Model model, TzQueryRequestParamVO paramVO)throws ServiceException;

	/**
	 * 根据id获取原材料使用记录信息列表
	 * @param model
	 * @param paramVO
	 * @param rId
     * @return
     */
	Model getProcurementUsageRecordInfo(Model model, TzQueryRequestParamVO paramVO, Long rId)throws ServiceException;

	/**
	 * 根据id获取原材料后续处理信息列表
	 * @param model
	 * @param paramVO
	 * @param rId
     * @return
     */
	Model getProcurementDisposeInfo(Model model, TzQueryRequestParamVO paramVO,int type)throws ServiceException;

	/**
	 * 根据设备id获取设备养护记录信息列表
	 * @param model
	 * @param paramVO
	 * @param fId
     * @return
     */
	Model getFacilityMaintenanceRecord(Model model, TzQueryRequestParamVO paramVO, Long fId)throws ServiceException;
}
