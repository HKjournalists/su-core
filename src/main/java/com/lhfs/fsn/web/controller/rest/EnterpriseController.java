package com.lhfs.fsn.web.controller.rest;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_GET_JG_CREDIT_LEVEL_URL;
import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.ledger.LedgerPrepackNo;
import com.gettec.fsnip.fsn.model.video.Enterprise_video;
import com.gettec.fsnip.fsn.model.waste.WasteDisposa;
import com.gettec.fsnip.fsn.service.business.BusinessMarketService;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.dishs.DishsNoService;
import com.gettec.fsnip.fsn.service.ledger.LedgerPrepackNoService;
import com.gettec.fsnip.fsn.service.video.EnterpriseVideoService;
import com.gettec.fsnip.fsn.service.waste.WasteDisposaService;
import com.gettec.fsnip.fsn.util.HttpUtils;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.business.BusinessMarketVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.fsn.vo.ledger.LedgerPrepackNoVO;
import com.gettec.fsnip.fsn.vo.waste.WasteDisposaVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.vo.ProductJGVO;
import com.lhfs.fsn.vo.business.BussinessUnitVO;

@Controller
@RequestMapping("/portal/enterprise")
public class EnterpriseController {
	static SimpleDateFormat SDFTIME = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired protected BusinessUnitService businessUnitService;
	@Autowired protected BusinessMarketService businessMarketService;
	@Autowired protected WasteDisposaService wasteDisposaService;
	@Autowired protected DishsNoService dishsNoService;
	@Autowired protected LedgerPrepackNoService ledgerPrepackNoService;
	
	@Autowired protected EnterpriseVideoService enterpriseVideoService;
	
	/**
	 * Portal 接口:根据企业ID获取企业所有信息
	 * @author HCJ 2016-5-24
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getBusinessById")
	public View getBusinessByOrg(@RequestParam long orgId,Model model,
			HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			BusinessUnit businessUnit = businessUnitService.findById(orgId);
			if (businessUnit == null) {
				resultVO.setStatus(SERVER_STATUS_FAILED);
				resultVO.setSuccess(false);
				model.addAttribute("result", resultVO);
				return JSON;
			}
			AuthenticateInfo info = new AuthenticateInfo();
			info.setOrganization(businessUnit.getOrganization());
			BusinessUnit orig_businessUnit = businessUnitService.findByInfo2(info,businessUnit, true, false);
			String type = orig_businessUnit.getType();
			String address = orig_businessUnit.getAddress();
			/*如果是贵州流通企业，需要查询所选交易市场*/
			if(type!=null&&type.equals("流通企业")&&address!=null&&address.contains("贵州省")){
				BusinessMarketVO marketVO = businessMarketService.findVOByBusinessId(orig_businessUnit.getId());
				if(marketVO != null) orig_businessUnit.setMarketOrg(marketVO.getOrganization());
			}
			if(orig_businessUnit!=null&&orig_businessUnit.getLicense()!=null&&orig_businessUnit.getLicense().getLicenseNo()!=null&&!"".equals(orig_businessUnit.getLicense().getLicenseNo())){
				//信用等级评分（获取监管系统的评分信息的url）
				String url=PropertiesUtil.getProperty(FSN_GET_JG_CREDIT_LEVEL_URL);
				url +="/fsn/getEnScore?lic="+orig_businessUnit.getLicense().getLicenseNo();
				String fsnResult = null;
				try {
					fsnResult = HttpUtils.send(url,"GET",null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (fsnResult != null && !"".equals(fsnResult)) {
					JSONObject creditList=JSONObject.fromObject(fsnResult);
					if(creditList.getString("data")!=null&&!creditList.getString("data").equals("null")){
						JSONObject scoreJson=creditList.getJSONObject("data");
						//信用等级(分数)
						String regularity = scoreJson.getString("score");
						orig_businessUnit.setRegularity(regularity);
					}
				}
			}
			model.addAttribute("data", orig_businessUnit);
		} catch(ServiceException sex){
			sex.printStackTrace();
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JSON.setObjectMapper(objectMapper);
		return JSON;
	}
	
//	/**
//	 * Portal 接口:根据企业ID获取企业所有信息
//	 * 另生成一个方法，餐饮调用该方法避免企业注册问题
//	 * @author LTG 2016-7-27
//	 */
//	@RequestMapping(method = RequestMethod.GET, value = "/getBusinessById2")
//	public View getBusinessById2(@RequestParam long orgId,Model model,
//			HttpServletRequest req,HttpServletResponse resp) {
//		ResultVO resultVO = new ResultVO();
//		try {
//			BusinessUnit businessUnit = businessUnitService.findById(orgId);
//			if (businessUnit == null) {
//				resultVO.setStatus(SERVER_STATUS_FAILED);
//				resultVO.setSuccess(false);
//				model.addAttribute("result", resultVO);
//				return JSON;
//			}
//			AuthenticateInfo info = new AuthenticateInfo();
//			info.setOrganization(businessUnit.getOrganization());
//			BusinessUnit orig_businessUnit = businessUnitService.findByInfo2(info,businessUnit, true, false);
//			String type = orig_businessUnit.getType();
//			String address = orig_businessUnit.getAddress();
//			/*如果是贵州流通企业，需要查询所选交易市场*/
//			if(type!=null&&type.equals("流通企业")&&address!=null&&address.contains("贵州省")){
//				BusinessMarketVO marketVO = businessMarketService.findVOByBusinessId(orig_businessUnit.getId());
//				if(marketVO != null) orig_businessUnit.setMarketOrg(marketVO.getOrganization());
//			}
//			if(orig_businessUnit!=null&&orig_businessUnit.getLicense()!=null&&orig_businessUnit.getLicense().getLicenseNo()!=null&&!"".equals(orig_businessUnit.getLicense().getLicenseNo())){
//				//信用等级评分（获取监管系统的评分信息的url）
//				String url=PropertiesUtil.getProperty(FSN_GET_JG_CREDIT_LEVEL_URL);
//				url +="/fsn/getEnScore?lic="+orig_businessUnit.getLicense().getLicenseNo();
//				String fsnResult = null;
//				try {
//					fsnResult = HttpUtils.send(url,"GET",null);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//				if (fsnResult != null && !"".equals(fsnResult)) {
//					JSONObject creditList=JSONObject.fromObject(fsnResult);
//					if(creditList.getString("data")!=null&&!creditList.getString("data").equals("null")){
//						JSONObject scoreJson=creditList.getJSONObject("data");
//						//信用等级(分数)
//						String regularity = scoreJson.getString("score");
//						orig_businessUnit.setRegularity(regularity);
//					}
//				}
//			}
//			model.addAttribute("data", orig_businessUnit);
//		} catch(ServiceException sex){
//			sex.printStackTrace();
//			resultVO.setStatus(SERVER_STATUS_FAILED);
//			resultVO.setSuccess(false);
//			resultVO.setErrorMessage(sex.getMessage());
//			((Throwable) sex.getException()).printStackTrace();
//		}catch(Exception e){
//			e.printStackTrace();
//			resultVO.setStatus(SERVER_STATUS_FAILED);
//			resultVO.setSuccess(false);
//		}
//		model.addAttribute("result", resultVO);
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.configure(org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
//		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		JSON.setObjectMapper(objectMapper);
//		return JSON;
//	}
	
	/**
	 * Portal 接口:根据企业ID获取企业视频信息
	 * @author HCJ 2016-5-24
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getVideoById")
	public View getVideoById(@RequestParam String orgId,@RequestParam int page,@RequestParam int page_size,Model model,
			HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			PagingSimpleModelVO<Enterprise_video> video_list = enterpriseVideoService.getPage(page, page_size, orgId);
			JSONObject jo = new JSONObject();
			jo.put("count", video_list.getCount());
			jo.put("page", page);
			jo.put("page_size", page_size);
			JSONArray datas = new JSONArray();
			List<Enterprise_video> data_list = video_list.getListOfModel();
			if (data_list != null && !data_list.isEmpty()) {
				for (Enterprise_video video_ : data_list) {
					JSONObject video_jo = new JSONObject();
					video_jo.put("video_url", video_.getVideo_url());
					video_jo.put("video_type", video_.getVideo_type());
					video_jo.put("video_desc", video_.getVideo_desc());
					
					datas.add(video_jo);
				}
			}
			jo.put("videos", datas);
			model.addAttribute("data", jo.toString());
		} catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 根据企业id查询废物处理信息
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListWaste")
	public View getListWaste(@RequestParam String orgId,@RequestParam String date,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		try {
			model.addAttribute("data",wasteDisposaService.getListWaste(orgId, date));
		} catch (DaoException e1) {
			e1.printStackTrace();
		}
		return JSON;	
	}
	
	/**
	 * 获得当前企业菜品
	 * @param orgId 企业id
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListDishsNo")
	public View getListDishsNo(@RequestParam String orgId,@RequestParam(value = "showTime", required = false) String showTime,Model model,HttpServletRequest req,HttpServletResponse resp) {
			
        try {
			model.addAttribute("data",dishsNoService.loadDishsNoShowVO(0, 0, showTime,null,Long.parseLong(orgId),true));
			//model.addAttribute("data",dishsNoService.getListDishsNo(orgId));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return JSON;	
	}
	
	/**
	 * 获得当前企业采购信息
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListLedgerPrepackNo")
	public View getListLedgerPrepackNo(@RequestParam String orgId,@RequestParam String date,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		try {
			model.addAttribute("data",ledgerPrepackNoService.getListLedgerPrepackNo(orgId, date));
		} catch (DaoException e1) {
			e1.printStackTrace();
		}
		return JSON;	
	}
	
	/**
	 * 获得当前废弃物列表
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getJgListWaste")
	public View get_jgListWaste(
			@RequestParam(value="page",required = true ,defaultValue="1")String page,
			@RequestParam(value="pageSize",required = true ,defaultValue="10") String pageSize, 
			@RequestParam(value = "licenseNo", required = false) String licenseNo,
			@RequestParam(value = "qsNo", required = false) String qsNo,
			@RequestParam(value = "businessName", required = false) String businessName,
			@RequestParam(value = "buslicenseNo", required = false) String buslicenseNo,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
			
				try {

					List<WasteDisposaVO> loadWasteDisposa = null;
					long total = 0l;
					BusinessUnit busVo=this.businessUnitService.getBusinessUnitByCondition(businessName, qsNo, buslicenseNo);
//					BussinessUnitVO busVo = wasteDisposaService.getBussinessUnitVO(licenseNo,qsNo,businessName);
					if(busVo == null){
						loadWasteDisposa = new ArrayList<WasteDisposaVO>();
					}else{
						int start = 1;
						int end = 10;
						//为了防止page乱填,因此做了如下处理(比如:填入的不是数字)
						if(page!=null&&!"".equals(page)){
							try {
								start = Integer.parseInt(page);
							} catch (Exception e) {
								start = 1;
							}
						}
						//为了防止pageSize乱填,因此做了如下处理(比如:填入的不是数字)
						if(pageSize!=null&&!"".equals(pageSize)){
						 try {
								end = Integer.parseInt(pageSize);
							} catch (Exception e) {
								end = 10;
							}
						}
						loadWasteDisposa = wasteDisposaService.loadWasteDisposa(start,end, "",busVo.getId());
						total = wasteDisposaService.getWasteTotal("",busVo.getId());
					}
					model.addAttribute("data",loadWasteDisposa);
					model.addAttribute("total", total);
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		return JSON;	
	}
	/**
     * 查看
     */
    @RequestMapping(method = RequestMethod.GET, value = "/findWasteDisposa/{id}")
    public View findWasteDisposa(Model model,@PathVariable(value="id") long id) {
    	ResultVO resultVO = new ResultVO();
    	try {
    		WasteDisposa wasteDisposa = wasteDisposaService.findById(id);
    		WasteDisposaVO wasteDisposaVO  = new WasteDisposaVO();
    		wasteDisposaVO.setId(wasteDisposa.getId());
    		wasteDisposaVO.setHandler(wasteDisposa.getHandler());
    		wasteDisposaVO.setQiyeId(wasteDisposa.getQiyeId());
    		try {
				wasteDisposaVO.setHandleTime(wasteDisposa.getHandleTime() != null ? SDFTIME.format(SDFTIME.parse(wasteDisposa.getHandleTime().toString())) : "");
			} catch (ParseException e) {
				e.printStackTrace();
			}
    		wasteDisposaVO.setHandleWay(wasteDisposa.getHandleWay());
    		wasteDisposaVO.setHandleNumber(wasteDisposa.getHandleNumber());
    		wasteDisposaVO.setDestory(wasteDisposa.getDestory());
    		wasteDisposaVO.setParticipation(wasteDisposa.getParticipation());
    		wasteDisposaVO.setPiceFile(wasteDisposa.getPiceFile());
			model.addAttribute("data",wasteDisposaVO);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	model.addAttribute("result", resultVO);
		return JSON;
    }
	
	/**
	 * 获得当前采购列表
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getJgListLedgerPrepackNo")
	public View getListProducer(
			@RequestParam(value="page",required = true ,defaultValue="1")String page,
			@RequestParam(value="pageSize",required = true ,defaultValue="10") String pageSize, 
			@RequestParam(value = "licenseNo", required = false) String licenseNo,
			@RequestParam(value = "qsNo", required = false) String qsNo,
			@RequestParam(value = "businessName", required = false) String businessName,
			@RequestParam(value = "buslicenseNo", required = false) String buslicenseNo,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
				try {
					long total = 0l;
					List<LedgerPrepackNoVO> ledgerList = null;
					 BusinessUnit busVo=this.businessUnitService.getBusinessUnitByCondition(businessName, qsNo, buslicenseNo);
//					 BussinessUnitVO busVo = wasteDisposaService.getBussinessUnitVO(licenseNo,qsNo,businessName);
					if(busVo == null){
						ledgerList = new ArrayList<LedgerPrepackNoVO>();
					}else{
						int start = 1;
						int end = 10;
						//为了防止page乱填,因此做了如下处理(比如:填入的不是数字)
						if(page!=null&&!"".equals(page)){
							try {
								start = Integer.parseInt(page);
							} catch (Exception e) {
								start = 1;
							}
						}
						//为了防止pageSize乱填,因此做了如下处理(比如:填入的不是数字)
						if(pageSize!=null&&!"".equals(pageSize)){
						 try {
								end = Integer.parseInt(pageSize);
							} catch (Exception e) {
								end = 10;
							}
						}
						total = ledgerPrepackNoService.getLedgerPrepackNoTotal("", "", "",busVo.getId());
						ledgerList = ledgerPrepackNoService.loadLedgerPrepackNo(start, end, "", "", "",busVo.getId());
					}
					model.addAttribute("data",ledgerList);
					model.addAttribute("total", total);
				} catch (DaoException e) {
					e.printStackTrace();
				}
		return JSON;	
	}
	 /**
     * 查看
     */
    @RequestMapping(method = RequestMethod.GET, value = "/findLedgerPrepackNo/{id}")
    public View findLedgerPrepackNo(Model model,@PathVariable(value="id") long id) {
    	ResultVO resultVO = new ResultVO();
    	try {
    		LedgerPrepackNo ledgerPrepackNo = ledgerPrepackNoService.findById(id);
    		LedgerPrepackNoVO vo  = new LedgerPrepackNoVO();
    		vo.setId(ledgerPrepackNo.getId());
			vo.setProductName(ledgerPrepackNo.getProductName());
			vo.setQiyeId(ledgerPrepackNo.getQiyeId());
			try {
				vo.setPurchaseTime(ledgerPrepackNo.getPurchaseTime() != null ? SDFTIME.format(SDFTIME.parse(ledgerPrepackNo.getPurchaseTime().toString())) : "");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			vo.setAlias(ledgerPrepackNo.getAlias());
			vo.setStandard(ledgerPrepackNo.getStandard());
			vo.setNumber(ledgerPrepackNo.getNumber());
			vo.setCompanyName(ledgerPrepackNo.getCompanyName());
			vo.setCompanyPhone(ledgerPrepackNo.getCompanyPhone());
			vo.setCompanyAddress(ledgerPrepackNo.getCompanyAddress());
			vo.setSupplier(ledgerPrepackNo.getSupplier());
			model.addAttribute("data",vo);
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	model.addAttribute("result", resultVO);
    	return JSON;
    }
}

















