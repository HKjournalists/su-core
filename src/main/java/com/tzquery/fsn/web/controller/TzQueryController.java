package com.tzquery.fsn.web.controller;

import com.tzquery.fsn.service.TzQueryService;
import com.tzquery.fsn.vo.TzQueryRequestParamVO;
import com.tzquery.fsn.vo.TzQueryResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;


/**
 * FSN提供给监管内网的综合查询接口的Controller层
 * @author ChenXiaolin 2015-11-30
 */
@Controller
@RequestMapping("/tzQuery")
public class TzQueryController {

	Logger logger = LoggerFactory.getLogger(TzQueryController.class);
	
	@Autowired TzQueryService tzQueryService;
	
	/**
	 * 产品信息列表接口--根据查询条件分页获取产品信息列表
	 * @author ChenXiaolin 2015-12-01
	 * @param model
	 * @param paramVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/proQuery/query")
	public View productQuery(Model model,@RequestBody TzQueryRequestParamVO paramVO) {
		
		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.productQuery(model,paramVO);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 产品详情接口--根据产品ID获取产品详情
	 * @author ChenXiaolin 2015-12-02
	 * @param model
	 * @param proId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/proQuery/proDetail")
	public View getProDetail(Model model,@RequestParam String proId) {
		
		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.getProDetail(model,proId);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 查看报告接口--根据产品ID分页获取报告信息列表
	 * @author ChenXiaolin 2015-12-01
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/proQuery/lookReport")
	public View lookReport(Model model,@RequestBody TzQueryRequestParamVO paramVO) {
		
		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.lookReport(model,paramVO);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 销售企业接口--根据产品ID分页获取销售企业列表
	 * @author ChenXiaolin 2015-12-02
	 * @param model
	 * @param paramVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/proQuery/saleBusiness")
	public View getSaleBusiness(Model model,@RequestBody TzQueryRequestParamVO paramVO) {
		
		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.getSaleBusiness(model,paramVO);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 销售企业明细中的交易明细接口--根据企业ID和产品ID分页获取销售企业中的交易明细
	 * @author ChenXiaolin 2015-12-03
	 * @param model
	 * @param paramVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/proQuery/transDetail")
	public View getProQueryTransDetailList(Model model,@RequestBody TzQueryRequestParamVO paramVO) {
		
		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.getProQueryTransDetailList(model,paramVO);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 台账信息接口--根据企业名称获取台账信息列表
	 * @author ChenXiaolin 2015-12-04
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/busQuery/accountInfo")
	public View getAccountInfo(Model model,@RequestBody TzQueryRequestParamVO paramVO) {
		
		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.getAccountInfo(model,paramVO);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 交易明细接口--获取企业查询中台账的交易明细列表
	 * @author ChenXiaolin 2015-12-04
	 * @param model
	 * @param paramVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/busQuery/transDetail")
	public View getBusQueryTransDetail(Model model,@RequestBody TzQueryRequestParamVO paramVO) {
		
		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.getBusQueryTransDetail(model,paramVO);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 综合查询-->企业查询-->企业详情-->产品信息-->根据企业名称和营业执照号获取该企业销售的产品列表
	 * @author ChenXiaolin 2015-12-14
	 * @param model
	 * @param paramVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/busQuery/getproList")
	public View getBusQueryProList(Model model,@RequestBody TzQueryRequestParamVO paramVO) {
		
		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.getBusQueryProList(model,paramVO);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 原材料信息接口--根据企业名称获取原材料信息列表
	 * @author lxz 2016-9-22
	 * @param model
	 * @param paramVO
	 * @param type  原材料类型  1：原辅料   2：添加剂   3：包装材料
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/busQuery/rawMaterialInfo/{type}")
	public View getRawMaterialInfo(Model model, @PathVariable(value = "type") int type,
								   @RequestBody TzQueryRequestParamVO paramVO) {

		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.getRawMaterialInfo(model,paramVO,type);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 原材料使用记录接口--根据id获取原材料使用记录信息列表
	 * @author lxz 2016-9-22
	 * @param model
	 * @param paramVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/busQuery/rawMaterial/usageRecord/{rId}")
	public View getProcurementUsageRecordInfo(Model model,@PathVariable(value = "rId") Long rId, @RequestBody TzQueryRequestParamVO paramVO) {

		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.getProcurementUsageRecordInfo(model,paramVO,rId);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 原材料后续处理接口--根据id获取原材料后续处理信息列表
	 * @author lxz 2016-9-22
	 * @param model
	 * @param paramVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/busQuery/rawMaterial/dispose/{type}")
	public View getProcurementDisposeInfo(Model model, @PathVariable(value = "type") int type,
										   @RequestBody TzQueryRequestParamVO paramVO) {
		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.getProcurementDisposeInfo(model,paramVO,type);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}


	/**
	 * 人员信息接口--根据企业名称获取人员信息列表
	 * @author lxz 2016-9-22
	 * @param model
	 * @param paramVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/busQuery/memberInfo")
	public View getMemberInfo(Model model,@RequestBody TzQueryRequestParamVO paramVO) {

		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.getMemberInfo(model,paramVO);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 设备信息接口--根据企业名称获取设备信息列表
	 * @author lxz 2016-9-22
	 * @param model
	 * @param paramVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/busQuery/facilityInfo")
	public View getFacilityInfo(Model model,@RequestBody TzQueryRequestParamVO paramVO) {

		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.getFacilityInfo(model,paramVO);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 设备养护记录信息接口--根据设备id获取设备养护记录信息列表
	 * @author lxz 2016-9-22
	 * @param model
	 * @param paramVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/busQuery/facility/maintenanceRecord/{fId}")
	public View getFacilityMaintenanceRecord(Model model,@PathVariable(value = "fId") Long fId,@RequestBody TzQueryRequestParamVO paramVO) {

		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.getFacilityMaintenanceRecord(model,paramVO,fId);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 规模信息接口--根据企业名称获取规模信息列表
	 * @author lxz 2016-9-22
	 * @param model
	 * @param paramVO
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/busQuery/operateInfo")
	public View getOperateInfo(Model model,@RequestBody TzQueryRequestParamVO paramVO) {

		TzQueryResultVO resultVO = new TzQueryResultVO();
		try {
			model = tzQueryService.getOperateInfo(model,paramVO);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			logger.error(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
}
