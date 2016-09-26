/**
 * 
 */
package com.tzquery.fsn.service.impl;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.facility.FacilityInfo;
import com.gettec.fsnip.fsn.model.facility.FacilityMaintenanceRecord;
import com.gettec.fsnip.fsn.model.member.Member;
import com.gettec.fsnip.fsn.model.operate.OperateInfo;
import com.gettec.fsnip.fsn.model.procurement.ProcurementDispose;
import com.gettec.fsnip.fsn.model.procurement.ProcurementInfo;
import com.gettec.fsnip.fsn.model.procurement.ProcurementUsageRecord;
import com.tzquery.fsn.dao.TzQueryDao;
import com.tzquery.fsn.service.TzQueryService;
import com.tzquery.fsn.util.StringUtil;
import com.tzquery.fsn.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

/**
 * @author ChenXiaolin 2015-11-30
 */
@Service("tzQueryService")
public class TzQueryServiceImpl implements TzQueryService{

	@Autowired TzQueryDao tzQueryDao;

	/**
	 * 根据查询条件获取产品信息列表
	 * @author ChenXiaolin 2015-12-1
	 * @param model
	 * @param paramVO
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Model productQuery(Model model, TzQueryRequestParamVO paramVO)throws ServiceException {
		try {
			List<TzQueryResponseProInfoVO> responseList = tzQueryDao.productQuery(paramVO);
			model.addAttribute("total", tzQueryDao.getproductQueryTotal(paramVO));
			model.addAttribute("data", responseList);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->productQuery()根据查询条件分页获取产品信息列表,出现异常！", e);
		}
	}
	
	/**
	 * 根据产品ID获取产品详情
	 * @author ChenXiaolin 2015-12-2
	 * @param model
	 * @param proId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Model getProDetail(Model model, String proId)
			throws ServiceException {
		try {
			TzQueryResponseProInfoVO vo = tzQueryDao.getProDetail(proId);
			model.addAttribute("data", vo);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->getProDetail()根据产品ID获取产品详情,出现异常！", e);
		}
	}

	/**
	 * 根据产品ID获取报告信息列表
	 * @author ChenXiaolin 2015-12-1
	 * @param model
	 * @param paramVO
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Model lookReport(Model model, TzQueryRequestParamVO paramVO)
			throws ServiceException {
		try {
			List<TzQueryResponseReportInfoVO> reportList = tzQueryDao.lookReport(paramVO);
			model.addAttribute("total", tzQueryDao.getLookReportTotal(paramVO));
			model.addAttribute("data", reportList);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->lookReport()根据产品ID获取报告信息列表,出现异常！", e);
		}
	}

	/**
	 * 根据产品ID分页获取销售企业列表
	 * @author ChenXiaolin 2015-12-2
	 * @param model
	 * @param paramVO
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Model getSaleBusiness(Model model, TzQueryRequestParamVO paramVO)
			throws ServiceException {
		try {
			List<TzQueryResponseBusVO> busList = tzQueryDao.getSaleBusiness(paramVO);
			model.addAttribute("total", tzQueryDao.getSaleBusinessTotal(paramVO));
			model.addAttribute("data", busList);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->lookReport()根据产品ID获取报告信息列表,出现异常！", e);
		}
	}

	/**
	 * 根据企业ID和产品ID获取销售企业中的交易明细
	 * @author ChenXiaolin 2015-12-03
	 * @param model
	 * @param paramVO
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Model getProQueryTransDetailList(Model model, TzQueryRequestParamVO paramVO)
			throws ServiceException {
		try {
			List<TzQueryResponseTansDetailVO> transDetailList = tzQueryDao.getProQueryTransDetailList(paramVO);
			model.addAttribute("total", tzQueryDao.getProQueryTransDetailTotal(paramVO));
			model.addAttribute("data", transDetailList);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->getProQueryTransDetailList()根据企业ID和产品ID分页获取销售企业中的交易明细,出现异常！", e);
		}
	}

	/**
	 * 根据企业名称获取台账信息列表
	 * @author ChenXiaolin 2015-12-04
	 * @param model
	 * @param paramVO
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Model getAccountInfo(Model model, TzQueryRequestParamVO paramVO)
			throws ServiceException {
		try {
			List<TzQueryResponseBusVO> accountInfoList = null;
			Long total = 0l;
			String firstBusId = tzQueryDao.getBusIdByBusName(paramVO.getBusName());
			if(StringUtil.isNotEmpty(firstBusId)){
				paramVO.setFirstBusId(firstBusId);
				accountInfoList = tzQueryDao.getAccountInfo(paramVO);
				total = tzQueryDao.getAccountInfoTotal(paramVO);
			}
			model.addAttribute("data", accountInfoList);
			model.addAttribute("total",total );
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->getAccountInfo()根据企业名称获取台账信息列表,出现异常！", e);
		}
	}

	/**
	 * 获取企业查询中台账的交易明细列表
	 * @author ChenXiaolin 2015-12-04
	 * @param model
	 * @param paramVO
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Model getBusQueryTransDetail(Model model, TzQueryRequestParamVO paramVO)
			throws ServiceException {
		try {
			List<TzQueryResponseTansDetailVO> busTransDetailList = null;
			Long total = 0l;
			String firstBusId = tzQueryDao.getBusIdByBusName(paramVO.getFirstBusName());
			if(StringUtil.isNotEmpty(firstBusId)){
				paramVO.setFirstBusId(firstBusId);
				busTransDetailList = tzQueryDao.getBusQueryTransDetail(paramVO);
				total = tzQueryDao.getBusQueryAccountInfoTotal(paramVO);
			}
			model.addAttribute("data", busTransDetailList);
			model.addAttribute("total", total);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->getBusQueryTransDetail()获取企业查询中台账的交易明细列表,出现异常！", e);
		}
	}

	/**
	 * 根据企业名称获取该企业销售的产品列表
	 * @author ChenXiaolin 2015-12-14
	 * @param model
	 * @param paramVO
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Model getBusQueryProList(Model model, TzQueryRequestParamVO paramVO)
			throws ServiceException {
		try {
			List<TzQueryResponseProListVO> proList = null;
			Long total = 0l;
			String busId = tzQueryDao.getBusIdByBusName(paramVO.getBusName());
			if(StringUtil.isNotEmpty(busId)){
				paramVO.setBusId(busId);
				proList = tzQueryDao.getBusQueryProList(paramVO);
				total = tzQueryDao.getBusQueryProListTotal(paramVO);
			}
			model.addAttribute("data", proList);
			model.addAttribute("total", total);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->getBusQueryProList()根据企业名称获取该企业销售的产品列表,出现异常！", e);
		}
	}

	/**
	 * 原材料信息接口--根据企业名称获取原材料信息列表
	 * @param model
	 * @param paramVO
     * @return
     */
	@Override
	public Model getRawMaterialInfo(Model model, TzQueryRequestParamVO paramVO,int type) throws ServiceException{
		try {
			List<ProcurementInfo> rawMaterial = tzQueryDao.getRawMaterialInfoList(paramVO,type);
			Long total = total = tzQueryDao.getRawMaterialInfoTotal(paramVO,type);
			model.addAttribute("data", rawMaterial);
			model.addAttribute("total",total );
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->getRawMaterialInfo()根据企业名称获取原材料信息列表,出现异常！", e);
		}
	}

	/**
	 * 根据企业名称获取人员信息列表
	 * @param model
	 * @param paramVO
	 * @return
	 * @throws ServiceException
     */
	@Override
	public Model getMemberInfo(Model model, TzQueryRequestParamVO paramVO) throws ServiceException {
		try {
			List<Member> member = tzQueryDao.getMemberInfoList(paramVO);
			Long total = total = tzQueryDao.getMemberInfoTotal(paramVO);
			model.addAttribute("data", member);
			model.addAttribute("total",total );
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->getMemberInfo()根据企业名称获取人员信息列表,出现异常！", e);
		}
	}

	/**
	 * 根据企业名称获取设备信息列表
	 * @param model
	 * @param paramVO
	 * @return
	 * @throws ServiceException
     */
	@Override
	public Model getFacilityInfo(Model model, TzQueryRequestParamVO paramVO) throws ServiceException {
		try {
			List<FacilityInfo> facilityInfo = tzQueryDao.getFacilityInfoList(paramVO);
			Long total = total = tzQueryDao.getFacilityInfoTotal(paramVO);
			model.addAttribute("data", facilityInfo);
			model.addAttribute("total",total );
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->getFacilityInfo()根据企业名称获取设备信息列表,出现异常！", e);
		}
	}

	/**
	 * 根据企业名称获取规模信息列表
	 * @param model
	 * @param paramVO
	 * @return
	 * @throws ServiceException
     */
	@Override
	public Model getOperateInfo(Model model, TzQueryRequestParamVO paramVO) throws ServiceException {
		try {
			OperateInfo operateInfo = tzQueryDao.getOperateInfo(paramVO);
			model.addAttribute("data", operateInfo);
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->getOperateInfo()根据企业名称获取规模信息列表,出现异常！", e);
		}
	}

	/**
	 * 根据id获取原材料使用记录信息列表
	 * @param model
	 * @param paramVO
	 * @param rId
	 * @return
	 * @throws ServiceException
     */
	@Override
	public Model getProcurementUsageRecordInfo(Model model, TzQueryRequestParamVO paramVO, Long rId) throws ServiceException {
		try {
			List<ProcurementUsageRecord> procurementUsageRecord = tzQueryDao.getProcurementUsageRecordList(paramVO,rId);
			Long total = total = tzQueryDao.getProcurementUsageRecordTotal(paramVO,rId);
			model.addAttribute("data", procurementUsageRecord);
			model.addAttribute("total",total );
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->getProcurementUsageRecordInfo()根据id获取原材料使用记录信息列表,出现异常！", e);
		}
	}

	/**
	 * 根据id获取原材料后续处理信息列表
	 * @param model
	 * @param paramVO
	 * @param rId
	 * @return
	 * @throws ServiceException
     */
	@Override
	public Model getProcurementDisposeInfo(Model model, TzQueryRequestParamVO paramVO,int type) throws ServiceException {
		try {
			List<ProcurementDispose> procurementDispose = tzQueryDao.getProcurementDisposeList(paramVO,type);
			Long total = total = tzQueryDao.getProcurementDisposeTotal(paramVO,type);
			model.addAttribute("data", procurementDispose);
			model.addAttribute("total",total );
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->getProcurementDisposeInfo()根据id获取原材料后续处理信息列表,出现异常！", e);
		}
	}

	/**
	 * 根据设备id获取设备养护记录信息列表
	 * @param model
	 * @param paramVO
	 * @param fId
	 * @return
	 * @throws ServiceException
     */
	@Override
	public Model getFacilityMaintenanceRecord(Model model, TzQueryRequestParamVO paramVO, Long fId) throws ServiceException {
		try {
			List<FacilityMaintenanceRecord> maintenanceRecords = tzQueryDao.getFacilityMaintenanceRecordList(paramVO,fId);
			Long total = total = tzQueryDao.getFacilityMaintenanceRecordTotal(paramVO,fId);
			model.addAttribute("data", maintenanceRecords);
			model.addAttribute("total",total );
			return model;
		} catch (Exception e) {
			throw new ServiceException("TzQueryServiceImpl-->getFacilityMaintenanceRecord()根据设备id获取设备养护记录信息列表,出现异常！", e);
		}
	}


}
