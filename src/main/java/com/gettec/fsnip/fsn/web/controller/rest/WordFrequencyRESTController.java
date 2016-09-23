package com.gettec.fsnip.fsn.web.controller.rest;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.wordFrequency.WordFrequencyService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.WordFrequencyVO;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;


/**
 * 
 * @author Sam Zhou
 *
 */
@Controller
@RequestMapping("/bigdata")
public class WordFrequencyRESTController {
	@Autowired private WordFrequencyService wordFrequencyService;
	@Autowired private ProductService productService;
	
	private final static long VALID_TIME = 10 * 60 * 1000; //10 mins
	private final static String CACHE = "FREQUENCY";
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value={"","/"})
	public View getWordFrequency(HttpServletRequest req, HttpServletResponse resp, Model model){
		Long key = 0L;
		HttpSession session = req.getSession(true);
		Map<Long, List<WordFrequencyVO>> cache = new HashMap<Long, List<WordFrequencyVO>>();
		
		if(session.getAttribute(CACHE) != null){
			cache = (Map<Long, List<WordFrequencyVO>>) session.getAttribute(CACHE);
			key = cache.keySet().iterator().next();
			Date now = new Date();
			if(key + VALID_TIME <= now.getTime()){
				cache = wordFrequencyService.getFrequency();
				key = cache.keySet().iterator().next();
			}
		}else{
			cache = wordFrequencyService.getFrequency();
			session.setAttribute(CACHE, cache);
			key = cache.keySet().iterator().next();
		}
		model.addAttribute("result", cache.get(key));
		return JSON;
	}
	
	/**
	 * 按热词获取企业的产品。
	 * @param page
	 * @param pageSize
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping( method = RequestMethod.GET , value = "/getProductByHotWord/{page}/{pageSize}/{hotWord}")
	public View getProductByHotWord(@PathVariable(value="hotWord") String hotWord, 
			@PathVariable(value="page") int page, @PathVariable(value="pageSize") int pageSize, 
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			/* 1.根据热词获取该企业的产品  */
			long total = productService.countByHotWord(info.getOrganization(), hotWord);
			List<Product> listOfProduct = productService.getListByHotWordWithPage(info.getOrganization(), 
					page, pageSize, hotWord);
			Map map = new HashMap();
			map.put("listOfProduct", listOfProduct);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
}
