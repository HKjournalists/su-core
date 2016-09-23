package com.gettec.fsnip.fsn.web.controller.rest.receive;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.api.BoYinReceive;
import com.gettec.fsnip.fsn.vo.receive.ResultVO;

/**
 * 接收外部系统传过来的样品检测数据
 * @author Administrator
 */
@Controller
@RequestMapping("/api/import")
public class ImportSpecimenRESTService {
	
	@Autowired private BoYinReceive boYinReceive;
	
	/**
	 * 接收泊银传过来的样品检测数据
	 * @author ZhangHui 2015/4/23
	 */
	@RequestMapping(method = RequestMethod.POST, value="/specimen")
	public View receiveReport(
			@RequestParam String data, 
            @RequestParam String sign,
            @RequestParam String busNo,
            HttpServletRequest req,
            HttpServletResponse resp,
            Model model){
		
		ResultVO resultVO = new ResultVO();
		model.addAttribute("result", resultVO);
		try {
			//参数校验
			if("".equals(data) || "".equals(sign) || "".equals(busNo)){
				resultVO.setSuccess(false);
				resultVO.setMessage("参数错误！");
				return JSON;
			}
			//校验签名并保存接收信息
			String productVO = boYinReceive.verifyAndDoPassHandle(busNo, data, sign);
			model.addAttribute("product", productVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setSuccess(false);
			resultVO.setMessage(e.getMessage());
		}
		return JSON;
	}
}
