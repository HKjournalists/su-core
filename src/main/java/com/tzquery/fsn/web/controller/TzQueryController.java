package com.tzquery.fsn.web.controller;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import com.tzquery.fsn.service.TzQueryService;
import com.tzquery.fsn.vo.TzQueryRequestParamVO;
import com.tzquery.fsn.vo.TzQueryResultVO;


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
	 * @param proId
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
	 * @param busName
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
	
}
