package com.lhfs.fsn.web.controller.rest.externalapi;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_ATGOO_PRODUCT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import net.sf.ehcache.Element;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.api.AtgooService;
import com.gettec.fsnip.fsn.api.AtgooService2;
import com.gettec.fsnip.fsn.vo.receive.ResultVO;
import com.lhfs.fsn.cache.EhCacheFactory;
import com.lhfs.fsn.service.product.ProductService;
import com.lhfs.fsn.util.StringUtil;
import com.lhfs.fsn.vo.atgoo.ProductVO;

/**
 * 外部系统调用FSN API 控制层
 * @author tangxin 2015/6/26
 *
 */
@Controller
@RequestMapping("/api/external")
public class ExternalAPIController {

	@Autowired 
	private AtgooService atgooService;
	
	@Autowired 
	private AtgooService2 atgooService2;
	
	@Autowired 
	private ProductService productService;
	
	/**
	 * 爱特购系统查询产品信息
	 * @author tangxin 2015/6/26
	 */
	@RequestMapping(method = RequestMethod.POST, value="/product")
	public View receiveReport(
			@RequestParam String data, 
            @RequestParam String sign,
            @RequestParam String appkey,
            Model model){
		ResultVO resultVO = new ResultVO();
		model.addAttribute("result", resultVO);
		try {
			//参数校验
			if("".equals(data) || "".equals(sign) || "".equals(appkey)){
				resultVO.setSuccess(false);
				resultVO.setMessage("参数错误！");
				return JSON;
			}
			Element result = (Element) EhCacheFactory.getCacheElement(CACHE_ATGOO_PRODUCT + "_" + sign);
			ProductVO productVO = null;
			if(result == null) {
				//校验签名并保存接收信息
				productVO = atgooService.verifyAndDoPassHandle(appkey, data, sign);
				EhCacheFactory.put(CACHE_ATGOO_PRODUCT + "_" + sign, productVO);
			} else {
				productVO = (ProductVO) result.getObjectValue();
			}
			model.addAttribute("product", productVO);
		} catch (Exception e) {
			resultVO.setSuccess(false);
			resultVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	 * 爱特购系统查询产品信息，扩展product接口
	 * 
	 * @see receiveReport method
	 * @author liuwx
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST, value="/productex")
	public View receiveReportEx(@RequestParam String data,Model model){
		ResultVO resultVO = new ResultVO();
		model.addAttribute("result", resultVO);
		try {
			//参数校验
			if("".equals(data)){
				resultVO.setSuccess(false);
				resultVO.setMessage("参数错误！");
				return JSON;
			}
			Element result = (Element) EhCacheFactory.getCacheElement(CACHE_ATGOO_PRODUCT + "_" + data);
			ProductVO productVO = null;
			if(result == null) {
				productVO = atgooService.getProductInfo(data);
				EhCacheFactory.put(CACHE_ATGOO_PRODUCT + "_" + data, productVO);
			} else {
				productVO = (ProductVO) result.getObjectValue();
			}
			model.addAttribute("product", productVO);
		} catch (Exception e) {
			resultVO.setSuccess(false);
			resultVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	 * 爱特购系统查询产品信息校验接口
	 * @author liuweixiang 2015/7/14
	 * @param data 经过DES加密过的请求信息
	 *        sign 标识符atigoo的标志符统一为 atigoo
	 *        appkey 校验key，校验key的算法为md5(data+sign)
	 * @return 如果错误返回富出错信息，否则返回产品详细信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value="/products")
	public View receiveReports( @RequestParam String data,
                                Model model)
	{
		List<String> resultList = new ArrayList<String>();
		//如果参数错误，返回错误信息
		if(StringUtil.isBlank(data))
		{
			model.addAttribute("results", resultList);
			model.addAttribute("isok", false);
			model.addAttribute("errorMsg", "request param error");
			return JSON;
		}
		
		//解析json信息，获取产品id或者产品条码
		try {
			JSONObject json = JSONObject.fromObject(data);
			String type = json.getString("type");
			if(type == null || type.equals(""))
			{
				model.addAttribute("results", resultList);
				model.addAttribute("isok", false);
				model.addAttribute("message", "request param error");
				return JSON;
			}
			JSONArray results = json.getJSONArray("values");
			if(results.size()<=0)
			{
				model.addAttribute("results", resultList);
				model.addAttribute("isok", false);
				model.addAttribute("message", "request param error");
				return JSON;
			}
			if(type.equalsIgnoreCase("PRODUCT_ID"))
			{
				Iterator<String> it = results.iterator();
				List<String> list = new ArrayList<String>();
				while(it.hasNext())
				{
					list.add((String)it.next());
				}
				List<Object>productIds = productService.findTestResultByProductIds(list);
				if(productIds == null)productIds = new ArrayList<Object>();
				list.clear();
				System.out.println(productIds.size());
				//for(Object obj:productIds)list.add(((Integer)obj).toString());
				model.addAttribute("results", productIds);
				model.addAttribute("isok", true);
				model.addAttribute("message", "success");
				return JSON;
			}else if(type.equalsIgnoreCase("BARCODE"))
			{
				Iterator <String>it = results.iterator();
				List<String> list = new ArrayList<String>();
				while(it.hasNext())
				{
					list.add(it.next());
				}
				List <String>barcodes = productService.findTestResultByBarcode(list);
				if(barcodes == null)barcodes = new ArrayList<String>();
				model.addAttribute("results", barcodes);
				model.addAttribute("isok", true);
				model.addAttribute("message", "success");
				return JSON;
			}else{
				model.addAttribute("results", resultList);
				model.addAttribute("isok", false);
				model.addAttribute("message", "server exception");
				return JSON;
			}
		} catch (Exception e) {
			model.addAttribute("results", resultList);
			model.addAttribute("isok", false);
			model.addAttribute("message", "request param error");
			return JSON;
		}
	}
	
	
	/**
	 * 爱特购系统查询产品信息
	 * @author liuwx 2015/08/12
	 */
	@RequestMapping(method = RequestMethod.POST, value="/productsEx")
	public View receiveReportsEx(
			@RequestParam String data, 
            @RequestParam String sign,
            @RequestParam String appkey,
            Model model){
		//参数校验
		if("".equals(data) || "".equals(sign) || "".equals(appkey)){
			    model.addAttribute("results", null);
				model.addAttribute("isok", false);
				model.addAttribute("message", "params error");
			return JSON;
		}
		try {
			//校验签名并保存接收信息
		    ProductVO pv = atgooService2.verifyAndDoPassHandle(appkey, data, sign);
		    model.addAttribute("results", pv.getTestList());
			model.addAttribute("isok", true);
			model.addAttribute("message", "success");
		} catch (Exception e) {
			model.addAttribute("results", null);
			model.addAttribute("isok", false);
			model.addAttribute("message", e.getMessage());
		}
	    return JSON;
	}
}
