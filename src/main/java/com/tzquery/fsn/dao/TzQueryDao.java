/**
 * 
 */
package com.tzquery.fsn.dao;

import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.facility.FacilityInfo;
import com.gettec.fsnip.fsn.model.facility.FacilityMaintenanceRecord;
import com.gettec.fsnip.fsn.model.member.Member;
import com.gettec.fsnip.fsn.model.operate.OperateInfo;
import com.gettec.fsnip.fsn.model.procurement.ProcurementDispose;
import com.gettec.fsnip.fsn.model.procurement.ProcurementInfo;
import com.gettec.fsnip.fsn.model.procurement.ProcurementUsageRecord;
import com.tzquery.fsn.vo.*;

import java.util.List;

/**
 * @author ChenXiaolin 2015-11-30
 */
public interface TzQueryDao {

	/**
	 * 根据查询条件获取产品信息列表
	 * @author ChenXiaolin 2015-11-30
	 * @param paramVO 
	 */
	List<TzQueryResponseProInfoVO> productQuery(TzQueryRequestParamVO paramVO) throws DaoException;
	
	/**
	 * 获取查询产品列表总记录数
	 * @author ChenXiaolin 2015-12-01
	 * @param paramVO
	 * @return
	 */
	Long getproductQueryTotal(TzQueryRequestParamVO paramVO)throws DaoException;
	
	/**
	 * 根据产品ID获取产品详情
	 * @author ChenXiaolin 2015-12-02
	 * @param proId
	 * @return
	 */
	TzQueryResponseProInfoVO getProDetail(String proId)throws DaoException;
	
	/**
	 * 根据产品ID获取报告信息列表
	 * @author ChenXiaolin 2015-12-01
	 * @param paramVO
	 * @return
	 */
	List<TzQueryResponseReportInfoVO> lookReport(TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 获取报告信息列表总记录数
	 * @author ChenXiaolin 2015-12-02
	 * @param paramVO
	 * @return
	 */
	Long getLookReportTotal(TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 根据产品ID分页获取销售企业列表
	 * @author ChenXiaolin 2015-12-02
	 * @param paramVO
	 * @return
	 */
	List<TzQueryResponseBusVO> getSaleBusiness(TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 获取销售企业列表总记录
	 * @author ChenXiaolin 2015-12-02
	 * @param paramVO
	 * @return
	 */
	Long getSaleBusinessTotal(TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 根据企业ID和产品ID获取销售企业中的交易明细
	 * @author ChenXiaolin 2015-12-03
	 * @param paramVO
	 * @return
	 */
	List<TzQueryResponseTansDetailVO> getProQueryTransDetailList(
			TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 获取销售企业中交易明细的总记录数
	 * @author ChenXiaolin 2015-12-03
	 * @param paramVO
	 * @return
	 */
	Long getProQueryTransDetailTotal(TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 根据企业名称获取台账信息列表
	 * @author ChenXiaolin 2015-12-04
	 * @param paramVO
	 * @return
	 */
	List<TzQueryResponseBusVO> getAccountInfo(TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 根据企业名称获取台账信息列表
	 * @author ChenXiaolin 2015-12-04
	 * @param paramVO
	 * @return
	 */
	Long getAccountInfoTotal(TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 获取企业查询中台账的交易明细列表
	 * @author ChenXiaolin 2015-12-04
	 * @param paramVO
	 * @return
	 */
	List<TzQueryResponseTansDetailVO> getBusQueryTransDetail(
			TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 获取企业查询中台账的交易明细列表总记录数
	 * @author ChenXiaolin 2015-12-04
	 * @param paramVO
	 * @return
	 */
	Long getBusQueryAccountInfoTotal(TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 根据企业名称获取企业ID
	 * @author ChenXiaolin 2015-12-05
	 * @param busName
	 * @return
	 */
	String getBusIdByBusName(String busName)throws DaoException;

	/**
	 * 根据企业名称获取该企业销售的产品列表
	 * @author ChenXiaolin 2015-12-14
	 * @param paramVO
	 * @return
	 */
	List<TzQueryResponseProListVO> getBusQueryProList(
			TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 *根据企业名称获取该企业销售的产品列表总数
	 * @author ChenXiaolin 2015-12-14
	 * @param paramVO
	 * @return
	 */
	Long getBusQueryProListTotal(TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 根据企业名称获取原材料信息列表
	 * @param paramVO
	 * @return
	 * @throws DaoException
     */
	List<ProcurementInfo> getRawMaterialInfoList(TzQueryRequestParamVO paramVO,int type)throws DaoException;

	/**
	 * 根据企业名称获取原材料信息数量
	 * @param paramVO
	 * @return
	 * @throws DaoException
     */
	Long getRawMaterialInfoTotal(TzQueryRequestParamVO paramVO,int type)throws DaoException;

	/**
	 * 根据企业名称获取人员信息列表
	 * @param paramVO
	 * @return
     */
	List<Member> getMemberInfoList(TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 根据企业名称获取人员信息数量
	 * @param paramVO
	 * @return
     */
	Long getMemberInfoTotal(TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 根据企业名称获取设备信息列表
	 * @param paramVO
	 * @return
     */
	List<FacilityInfo> getFacilityInfoList(TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 根据企业名称获取设备信息数量
	 * @param paramVO
	 * @return
     */
	Long getFacilityInfoTotal(TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 根据企业名称获取规模信息列表
	 * @param paramVO
	 * @return
     */
	OperateInfo getOperateInfo(TzQueryRequestParamVO paramVO)throws DaoException;

	/**
	 * 根据id获取原材料使用记录信息列表
	 * @param paramVO
	 * @param rId
     * @return
     */
	List<ProcurementUsageRecord> getProcurementUsageRecordList(TzQueryRequestParamVO paramVO, Long rId)throws DaoException;

	/**
	 * 根据id获取原材料使用记录信息数量
	 * @param paramVO
	 * @param rId
     * @return
     */
	Long getProcurementUsageRecordTotal(TzQueryRequestParamVO paramVO, Long rId)throws DaoException;

	/**
	 * 根据id获取原材料后续处理信息列表
	 * @param paramVO
	 * @param rId
	 * @return
     */
	List<ProcurementDispose> getProcurementDisposeList(TzQueryRequestParamVO paramVO, int type)throws DaoException;

	/**
	 * 根据id获取原材料后续处理信息数量
	 * @param paramVO
	 * @param rId
     * @return
     */
	Long getProcurementDisposeTotal(TzQueryRequestParamVO paramVO, int type)throws DaoException;

	/**
	 * 根据设备id获取设备养护记录信息列表
	 * @param paramVO
	 * @param fId
     * @return
     */
	List<FacilityMaintenanceRecord> getFacilityMaintenanceRecordList(TzQueryRequestParamVO paramVO, Long fId)throws DaoException;

	/**
	 * 根据设备id获取设备养护记录信息数量
	 * @param paramVO
	 * @param fId
	 * @return
     */
	Long getFacilityMaintenanceRecordTotal(TzQueryRequestParamVO paramVO, Long fId)throws DaoException;
}
