package com.lhfs.fsn.web.controller.rest.deal;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import bsh.ParseException;

import com.aliyun.oss.ServiceException;
import com.gettec.fsnip.fsn.enums.DealProblemTypeEnums;
import com.gettec.fsnip.fsn.model.deal.DealProblem;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.deal.DealProblemService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.ws.SmsUtils;
import com.gettec.fsnip.fsn.ws.vo.Message;
@Controller
@RequestMapping("/deal/problem")
public class DealProblemController {
	
	@Autowired
	private DealProblemService dealProblemService;
	
	@Autowired
	private ProductService productService;
		
	static SimpleDateFormat SDFTIME = new SimpleDateFormat("yyyy-MM-dd");
	
	
    /**
     * 查看
     */
    @RequestMapping(method = RequestMethod.GET, value = "/appSendInfo")
    public View appSendInfo(Model model,
    		@RequestParam(value = "telephone", required = false) String telephone,
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "businessName", required = false) String businessName
    		) {
    	try {
    		String st = Math.random()+"";
    		String code = st.substring(2, 8);
    		/* 2.发送短信 */
    		Message message = new Message();
    		message.setMobile(telephone);
    		message.setContent("验证码:"+code+",您正在通过食安测绑定账户手机,请于30分钟之内输入验证码,工作人员不会向您索要,请勿泄露.");
    		SmsUtils smsUtils =new SmsUtils(message);
    		//异步短信发送
    		smsUtils.SyncSendSms();
    		model.addAttribute("code", code);
    		model.addAttribute("status", true);
    	} catch (Exception e) {
    		model.addAttribute("status", false);
    		e.printStackTrace();
    	}
    	return JSON;
    }

	
	/**
	 * 大众门户调用保存
	 * @param businewssName
	 * @param licenseNo
	 * @param barcode
	 * @param model
	 * @param request
	 * @param response
	 * @throws com.gettec.fsnip.fsn.exception.ServiceException 
	 * @throws ServiceException 
	 * @throws ParseException 
	 */
	@RequestMapping(method = RequestMethod.GET,value = "/saveDealProblem")
	public View save(
			@RequestParam(value = "province", required = false) String province,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "counties", required = false) String counties,
			@RequestParam(value = "businessName", required = false) String businessName,
			@RequestParam(value = "licenseNo", required = false) String licenseNo,
			@RequestParam(value = "barcode", required = false) String barcode,
			@RequestParam(value = "productTime", required = false) String productTime,
			@RequestParam(value = "problemType", required = false,defaultValue="-1") int problemType,
			@RequestParam(value = "origin", required = false,defaultValue="-1") int origin,
			@RequestParam(value = "longitude", required = false) String longitude,
			@RequestParam(value = "latitude", required = false) String latitude,
			@RequestParam(value = "userId", required = false) String userId,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response
			) {
		
		//将请求参数封装为dealProblem对象保存
		DealProblem dealProblem = new DealProblem();
		dealProblem.setProvince(province);
		dealProblem.setCity(city);
		dealProblem.setCounties(counties);
		dealProblem.setBusinessName(businessName);
		dealProblem.setLicenseNo(licenseNo);
		dealProblem.setBarcode(barcode);
		dealProblem.setCreateTime(new Date());
		dealProblem.setCommitStatus(DealProblemTypeEnums.ZERO);
		dealProblem.setComplainStatus(DealProblemTypeEnums.ZERO);
		//设置问题类型。用枚举来指定传过来的是那种问题类型
		dealProblem.setProblemType(DealProblemTypeEnums.typeEnumeId(problemType));
//		if (problemType == 0) {
//			dealProblem.setProblemType(DealProblemTypeEnums.ZERO);
//		}else if (problemType == 1) {
//			dealProblem.setProblemType(DealProblemTypeEnums.ONE);
//		}else if (problemType == 2) {
//			dealProblem.setProblemType(DealProblemTypeEnums.TWO);
//		}else if (problemType == 3) {
//			dealProblem.setProblemType(DealProblemTypeEnums.THREE);
//		}else if (problemType == 4) {
//			dealProblem.setProblemType(DealProblemTypeEnums.FOUR);
//		}else if (problemType == 5) {
//			dealProblem.setProblemType(DealProblemTypeEnums.FIVE);
//		}else {
//			dealProblem.setProblemType(DealProblemTypeEnums.OTHER);
//		}
		
		dealProblem.setOrigin(DealProblemTypeEnums.typeEnumeId(origin));
//		if (origin == 0) {
//		}else if (origin == 1) {
//			dealProblem.setOrigin(DealProblemTypeEnums.ONE);
//		}else if (origin == 2) {
//			dealProblem.setOrigin(DealProblemTypeEnums.TWO);
//		}else {
//			dealProblem.setOrigin(DealProblemTypeEnums.OTHER);
//		}
		
		if (productTime!=null && !productTime.equals("null") && productTime != "") {
			try {
				dealProblem.setProductTime(SDFTIME.parse(productTime));
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}
		
		if (latitude != null && !"".equals(latitude) && !(latitude.equals("null"))) {
			dealProblem.setLatitude(Float.parseFloat(latitude));
		}
		
		if (longitude != null && !longitude.equals("null") && !"".equals(longitude)) {
			dealProblem.setLongitude(Float.parseFloat(longitude));
		}
		
		if (userId != null && !userId.equals("null") && !"".equals(userId)) {
			dealProblem.setUserId(Long.parseLong(userId));
		}
		
		try {
			//根据条形码获取产品名称，保存到问题表内
				Product product = productService.findByBarcode(barcode);
				String productName = "";
				if (product != null) {
					productName = product.getName();
				} 
				dealProblem.setProductName(productName);
				
			dealProblemService.save(dealProblem);
			 model.addAttribute("status", true);
		}  catch (com.gettec.fsnip.fsn.exception.ServiceException e) {
			 model.addAttribute("status", false);
			e.printStackTrace();
		} 
		 return JSON;
	}
    
}
