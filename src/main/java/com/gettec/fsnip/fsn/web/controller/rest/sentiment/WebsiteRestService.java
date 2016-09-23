package com.gettec.fsnip.fsn.web.controller.rest.sentiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

import com.fsnip.SentimentInvokeUtils;
import com.gettec.fsnip.fsn.common.BaseController;
import com.gettec.fsnip.fsn.model.sentiment.SentimentWebsite;
import com.gettec.fsnip.fsn.service.sentiment.SentimentWebsiteService;
import com.gettec.fsnip.fsn.vo.page.PageVO;
import com.gettec.fsnip.fsn.vo.sentiment.CategoryVO;
import com.gettec.fsnip.fsn.vo.sentiment.SubjectVO;
import com.gettec.fsnip.fsn.vo.sentiment.WebsiteVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lhfs.fsn.util.StringUtil;


@Controller
@RequestMapping(value="/website")
public class WebsiteRestService extends BaseController {

	@Autowired
	private SentimentWebsiteService sentimentWebsiteService;
	
	@RequestMapping(value="", method = RequestMethod.POST)
	public View create(@RequestBody SentimentWebsite sentimentWebsite, Model model){

		if(sentimentWebsite.getWebsiteName() != null && sentimentWebsite.getWebsiteUrl() != null){
			try{
				sentimentWebsite = sentimentWebsiteService.create(sentimentWebsite);
			}catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("status", false);
				model.addAttribute("msg", "系统异常");
				return JSON;
			}
			model.addAttribute("status", true);
			model.addAttribute("result", sentimentWebsite);
			return JSON;
		}
		
		model.addAttribute("status", false);
		model.addAttribute("msg", "站点名称或url不能为空");
		return JSON;
		
	}
	
	@RequestMapping(value="/{page}/{pageSize}", method=RequestMethod.GET)
	public View getWebsites(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize, @RequestParam(value="configure", required=false) String configure, Model model){
		List<SentimentWebsite> websites = null;
		Long count = 0L;
		Long organizationId = Long.valueOf(AccessUtils.getUserOrg().toString());
		try{
			websites = sentimentWebsiteService.getWebsitesByOrg(page, pageSize, organizationId);
			count = sentimentWebsiteService.countByOrg(organizationId);
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("status", false);
			model.addAttribute("msg", "系统异常");
			return JSON;
		}
		
		if(websites != null){
			model.addAttribute("status", true);
			model.addAttribute("result", websites);
			model.addAttribute("count", count);
			return JSON;
		}
		model.addAttribute("status", false);
		model.addAttribute("msg", "该机构未添加监测网站");
		return JSON;
		
	}
	
	@RequestMapping(value="/checkUnique", method=RequestMethod.GET)
	public View checkUnique(@RequestParam(value="url", required = true) String websiteUrl,@RequestParam(value="website_id", required=true) Long websiteId, Model model){
	    boolean flag = false;
	    try{
	    	flag = sentimentWebsiteService.checkUnique(websiteId, websiteUrl);
	    }catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("status", false);
			model.addAttribute("msg", "系统异常");
			return JSON;
		}
	    
	    model.addAttribute("status", true);
	    model.addAttribute("result", flag);
		return JSON;
		
	}
	
	@RequestMapping(value="", method=RequestMethod.PUT)
	public View updateWebsite(@RequestBody SentimentWebsite sentimentWebsite, Model model){
		try{
			sentimentWebsite = sentimentWebsiteService.update(sentimentWebsite);
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("status", false);
			model.addAttribute("msg", "系统异常");
			return JSON;
		}
		
		model.addAttribute("status", true);
		model.addAttribute("result", sentimentWebsite);
		return JSON;
		
	}
	
	/**
	 * 取当前组织机构下的网站列表
	 */
	@RequestMapping(method=RequestMethod.GET, value="/website_list")
	public View websiteList(HttpServletRequest req, HttpServletResponse resp,Model model,HttpSession session){
		String myGroupId = (String) session.getAttribute("myGroupID");
		if(StringUtil.isBlank(myGroupId)){
			Map<String,String> map = getWebstGroupID();
			if(!"true".equals(map.get("status"))){
				model.addAttribute("status", false);
				model.addAttribute("msg", map.get("msg"));
				model.addAttribute("result", new ArrayList<WebsiteVO>());
				return JSON;
			}
		    myGroupId = map.get("myGroupID");
		    session.setAttribute("myGroupID", myGroupId);
		}
		
		Gson gson = new Gson();
		String webSiteJson  = SentimentInvokeUtils.getWebsiteList(myGroupId);
		System.out.println(webSiteJson);
		//webSiteJson="[]";
		List<WebsiteVO> websiteList = gson.fromJson(webSiteJson, new TypeToken<List<WebsiteVO>>(){}.getType());
		model.addAttribute("status", "true");
		model.addAttribute("result", websiteList);
		return JSON;
	}
	
	public Map<String,String> getWebstGroupID(){
		/**取主题分类类型**/
		Map<String,String> map = new HashMap<String, String>();
		String orgName = AccessUtils.getUserOrgName().toString();
		String topicNamePrefix = orgName.split("\\.")[0] + "_";
		String result = SentimentInvokeUtils.getStrTopics(topicNamePrefix);
		Gson gson = new Gson();
		List<SubjectVO> subjectList = gson.fromJson(result, new TypeToken<List<SubjectVO>>(){}.getType());
		long subject_category_id = -1L;
		for(SubjectVO s:subjectList){
			if(null!=s.getSubject_Category_ID()&&s.getParent_Subject_Category_ID()==null){
				subject_category_id = s.getSubject_Category_ID();
				break;
			}
		}
		if(subject_category_id == -1L){
			map.put("status", "false");
			map.put("msg", "主题没有设置一级分类，请联系管理员");
			return map;
		}
		/*********************/
		
		/**取主题分类网站组ID**/
		String categoryJson = SentimentInvokeUtils.getJsonLevel1Category();
		List<CategoryVO> categoryList = gson.fromJson(categoryJson, new TypeToken<List<CategoryVO>>(){}.getType());
		long my_website_group_id = -2L;
		for(CategoryVO c:categoryList){
			if(null!=c.getMy_Website_Group_ID()&&subject_category_id ==c.getSubject_Category_ID()){
				my_website_group_id = c.getMy_Website_Group_ID();
				break;
			}
		}
		if(my_website_group_id == -2L){
			map.put("status", "false");
			map.put("msg", "主题一级分类没有绑定网站组，请联系管理员");
			return map;
		}
		map.put("status", "true");
		map.put("myGroupID", String.valueOf(my_website_group_id));
		return map;
	}
	
	
	/*
	 * 获取目标网站分页数据
	 * 
	 */
	@RequestMapping(method=RequestMethod.POST, value="/list")
	public View websiteList(@RequestBody PageVO pageVo,HttpServletRequest req,HttpServletResponse resp, Model model){
		SentimentWebsite sentimentWebsite = new SentimentWebsite();
		sentimentWebsite.setOrganizationId(this.getUserOrganizationId());
	    Map<String,Object> map = sentimentWebsiteService.getPageWebsite(pageVo, sentimentWebsite);
	    readResult(map,model);
		return JSON;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/create")
	public View create(@RequestBody SentimentWebsite sentimentWebsite,HttpServletRequest req,HttpServletResponse resp, Model model){
		try{
			sentimentWebsite = sentimentWebsiteService.create(sentimentWebsite);
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("status", false);
			model.addAttribute("message", "系统异常");
			return JSON;
		}
		createResult(true, model);
		return JSON;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/update")
	public View update(@RequestBody SentimentWebsite sentimentWebsite,HttpServletRequest req,HttpServletResponse resp, Model model){
		boolean flag = sentimentWebsiteService.updateWebsite(sentimentWebsite);
		updateResult(flag, model);;
		return JSON;
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/delete")
	public View delete(@RequestParam("id") Long id,HttpServletRequest req,HttpServletResponse resp, Model model){
		
		boolean flag = sentimentWebsiteService.deleteWebsite(id);
		deleteResult(flag, model);
		return JSON;
	}
	
	/**
	 * 判断同版本下类名是否重复
	 * 
	 */
	@RequestMapping(method={RequestMethod.POST}, value="/unique")
	public View unique(@RequestBody SentimentWebsite sentimentWebsite,HttpServletRequest req,HttpServletResponse resp,Model model,HttpSession session){
		String myGroupId = (String) session.getAttribute("myGroupID");
		if(!StringUtil.isBlank(myGroupId)&&!StringUtil.isBlank(sentimentWebsite)&&!StringUtil.isBlank(sentimentWebsite.getWebsiteUrl())){
			boolean flag = SentimentInvokeUtils.uniqueUrl(myGroupId,sentimentWebsite.getWebsiteUrl());
			if(flag){
				model.addAttribute("status",true);	
				model.addAttribute("message", "该网站舆情系统已存在!");
				return JSON;
			}
		}
		
		boolean flag = sentimentWebsiteService.unique(sentimentWebsite);
		model.addAttribute("status", flag);
		return JSON;
		
	}
	
	
	public static void main(String args[]){
		String url = "http://baidu.com/entry/4545/0/";  
        Pattern p =Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);  
        Matcher matcher = p.matcher(url);  
        matcher.find();  
        System.out.println(matcher.group() + "123"); 
	}
	
}
