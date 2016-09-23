package com.gettec.fsnip.fsn.web.controller.rest.erp;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import com.gettec.fsnip.fsn.enums.BusinessTypeEnums;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.service.erp.buss.BussToMerchandisesService;
import com.gettec.fsnip.fsn.service.erp.buss.FlittingOrderService;
import com.gettec.fsnip.fsn.service.erp.buss.InStorageRecordService;
import com.gettec.fsnip.fsn.service.erp.buss.OutStorageRecordService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.erp.BusinessOrderVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Controller
@RequestMapping("/erp/buss/{type}")
public class BusinessOperationRESTService {
	@Autowired private InStorageRecordService inStorageRecordService;
	@Autowired private OutStorageRecordService outStorageRecordService;
	@Autowired private FlittingOrderService flittingOrderService;
	@Autowired private BussToMerchandisesService bussToMerchandisesService;
	
	/**
	 * 根据type类型，加载入库/出库/调拨的数据源信息
	 * @param req
	 * @param resp
	 * @param type
	 * @param vo
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"","/"}, method=RequestMethod.POST)
	public View addBusinessOrder(HttpServletRequest req, HttpServletResponse resp, 
			@PathVariable int type, @RequestBody BusinessOrderVO vo, Model model){
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			String userName = info.getUserName();
			Object obj = null;
			switch (getType(type)) {
			case IN_STORAGE:  // 入库
				obj = inStorageRecordService.addBusinessOrder(vo, currentUserOrganization,userName);
				model.addAttribute("result", obj);
				break;
			case OUT_STORAGE: // 出库
				outStorageRecordService.addBusinessOrder(vo, currentUserOrganization,userName,resultVO);
				model.addAttribute("result", vo.getResult());
				break; 
			case FLITTING_ORDER: // 调拨
				flittingOrderService.addBusinessOrder(vo, currentUserOrganization,userName);
				model.addAttribute("result", vo.getResult1());
				break;
			default:
				break;
			}
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 入库/出库/调拨 分页查询
	 * @param req
	 * @param resp
	 * @param type
	 * @param page
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value={"/list"}, method=RequestMethod.GET)
	public View search(HttpServletRequest req, HttpServletResponse resp, 
			@PathVariable int type, @RequestParam int page, 
			@RequestParam int pageSize, Model model){
		ResultVO resultVO = new ResultVO();
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			switch (getType(type)) {
			case IN_STORAGE: // 入库
				model.addAttribute("result",inStorageRecordService.getPaging(page, pageSize, null, currentUserOrganization));
				break;
			case OUT_STORAGE: // 出库
				model.addAttribute("result",outStorageRecordService.getPaging(page, pageSize, null, currentUserOrganization));
				break;
			case FLITTING_ORDER:  // 调拨
				model.addAttribute("result",flittingOrderService.getPaging(page, pageSize, null, currentUserOrganization));
				break;
			default:
				break;
			}
		} catch (ServiceException sex) {
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable)sex.getException()).printStackTrace();
		}
		model.addAttribute("resultVO", resultVO);
		return JSON;
	}
	
	/**
	 * 根据单号和类型获取入库/出库/调拨的商品列表
	 * @param req
	 * @param resp
	 * @param type
	 * @param num
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/listinfo"}, method=RequestMethod.GET)
	public View getInOutFlittingInfo(HttpServletRequest req, HttpServletResponse resp, 
			@PathVariable int type, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize, 
			@RequestParam(value = "num") String num, Model model){
		ResultVO resultVo = new ResultVO();
		try {
		    model.addAttribute("count", bussToMerchandisesService.getMerInfoCountByNoPage(page, pageSize, num, type)); 
			model.addAttribute("result", bussToMerchandisesService.getMerInfoByNoPage(page, pageSize, num, type)); 
		} catch (ServiceException sex) {
			resultVo.setStatus(SERVER_STATUS_FAILED);
			resultVo.setMessage(sex.getMessage());
		}
		model.addAttribute("resultVO", resultVo);
		return JSON;
	}
	
	/**
	 * 判断出库数量是否大于现有库存量
	 * @param req
	 * @param resp
	 * @param type
	 * @param storage
	 * @param batch
	 * @param value
	 * @param productId
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/JudgeStorage"}, method=RequestMethod.GET)
	public View JudgeOutNum(HttpServletRequest req, HttpServletResponse resp, 
			@PathVariable int type,@RequestParam(value = "storage") String storage, 
			@RequestParam(value = "batch") String batch,@RequestParam(value = "value") String value,
			@RequestParam(value = "productId") Long productId, Model model){
		boolean flag = false;
		try {
			flag = outStorageRecordService.JudgeOutNum(productId, value, batch, storage);
		} catch (Exception e) {
			
		}
		model.addAttribute("flag", flag);
		return JSON;
	}
	
	@RequestMapping(value={"/JudgeFlitting"}, method=RequestMethod.GET)
	public View JudgeFlittingNum(HttpServletRequest req, HttpServletResponse resp, 
			@PathVariable int type,@RequestParam(value = "storage") String storage, @RequestParam(value = "batch") String batch,@RequestParam(value = "value") String value,@RequestParam(value = "mid") Long mid, Model model){
		boolean flag = false;
		ResultVO resultVo = new ResultVO();
		try {
			flag = flittingOrderService.JudgeflittingNum(mid, value, batch, storage);
		} catch (Exception e) {
			resultVo.setStatus("false");
			resultVo.setSuccess(false);
			resultVo.setErrorMessage(e.getMessage());
		}
		model.addAttribute("resultVO", resultVo);
		model.addAttribute("flag", flag);
		return JSON;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/reglist/{page}/{pageSize}/{configure}" })
	public View getReqList(HttpServletRequest req,
			HttpServletResponse resp,
			@PathVariable(value = "page") int page,
			@PathVariable(value = "pageSize") int pageSize,
			@PathVariable(value = "configure") String configure,
			@PathVariable int type, Model model) {
		ResultVO resultVo = new ResultVO();
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			switch (getType(type)) {
			case IN_STORAGE:
				model.addAttribute("result",inStorageRecordService.getInStorageRecordfilter(page, pageSize, configure, currentUserOrganization));
				break;
			case OUT_STORAGE:
				model.addAttribute("result",outStorageRecordService.getOutStorageRecordfilter(page, pageSize, configure, currentUserOrganization));
				break;
			case FLITTING_ORDER:
				model.addAttribute("result",flittingOrderService.getFlittingOrderfilter(page, pageSize, configure, currentUserOrganization));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			resultVo.setStatus("false");
			resultVo.setSuccess(false);
			resultVo.setErrorMessage(e.getMessage());
			e.printStackTrace();
		}
		model.addAttribute("resultVO", resultVo);
		return JSON;
	}
	
	private BusinessTypeEnums getType(int type){
		return BusinessTypeEnums.getEnumByID(type);
	}
	
	/*@RequestMapping(value={"/search"}, method=RequestMethod.GET)
	public View searchKeywords(HttpServletRequest req, HttpServletResponse resp, @RequestParam String keywords,
			@PathVariable int type, @RequestParam int page, 
			@RequestParam int pageSize, Model model){
		ResultVO resultVo = new ResultVO();
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			switch (getType(type)) {
			case IN_STORAGE:
				model.addAttribute("result",inStorageRecordService.getPaging(page, pageSize, keywords, currentUserOrganization));
				break;
			case OUT_STORAGE:
				model.addAttribute("result",outStorageRecordService.getPaging(page, pageSize, keywords, currentUserOrganization));
				break;
			case FLITTING_ORDER:
				model.addAttribute("result",flittingOrderService.getPaging(page, pageSize, keywords, currentUserOrganization));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			resultVo.setStatus("false");
			resultVo.setSuccess(false);
			resultVo.setMessage(e.getMessage());
		}
		model.addAttribute("resultVO", resultVo);
		return JSON;
	}*/
}
