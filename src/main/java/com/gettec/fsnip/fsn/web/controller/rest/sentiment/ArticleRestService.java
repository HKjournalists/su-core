package com.gettec.fsnip.fsn.web.controller.rest.sentiment;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fsnip.elasticsearch.FsnipElasticSearchJavaClientUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.fsnip.elasticsearch.FsnipElasticSearchHttpUtils;
import com.fsnip.elasticsearch.SearchResult;
import com.gettec.fsnip.fsn.model.sentiment.OrganizationTopic;
import com.gettec.fsnip.fsn.service.sentiment.OrgToTopicService;
import com.gettec.fsnip.sso.client.util.AccessUtils;

@Controller
@RequestMapping(value="/sentimentArticle")
public class ArticleRestService {
	
	@Autowired
	OrgToTopicService orgToTopicService;
	
	@RequestMapping(method = RequestMethod.GET, value="/{type}/{page}/{pageSize}")
	public View search(HttpServletRequest request, HttpServletResponse resp, Model model,
						@RequestParam(value="keywords", required=false) String keywords,
						@RequestParam(value="focuskey", required=false) String focuskey,
						@PathVariable int page,
						@PathVariable int pageSize,
						@PathVariable String type){
		String organizationId = AccessUtils.getUserOrg().toString();
		List<OrganizationTopic> orgTopics = orgToTopicService.findByOrg(Long.valueOf(organizationId));
		
		String index = "fsnip_sentiment_common";
		String index_type = null;
		if(orgTopics == null || orgTopics.size() == 0){
			index_type = null;
		}else{
			index = "fsnip_sentiment_" + organizationId;
			
			if(!"-1".equals(type)){
				index_type = "sentiment_" + type;
			}
			
		}
		
		List<String> keywordList = new ArrayList<String>();
		if(!StringUtils.isEmpty(keywords)){
			try {
				keywordList.add(URLDecoder.decode(keywords, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(!StringUtils.isEmpty(focuskey)){
			try {
				keywordList.add(URLDecoder.decode(focuskey, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		SearchResult searchResult = new SearchResult();
		try {
			searchResult = FsnipElasticSearchHttpUtils.searches(index, index_type, keywordList, page, pageSize);
			model.addAttribute("status", true);
			model.addAttribute("result", searchResult);
		} catch (Exception e){
			e.printStackTrace();
			model.addAttribute("status", false);
			model.addAttribute("msg", "系统异常");
		}
		return JSON;
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="/getArticleById/{id}")
	public View getArticleById(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response, Model model){
		String organizationId = AccessUtils.getUserOrg().toString();
		String index_type = "fsnip_sentiment_" + organizationId;
			
			
		try {
			SearchResult searchResult = new SearchResult();
			searchResult = FsnipElasticSearchJavaClientUtils.searchByArticleId(index_type, null, id);
			model.addAttribute("result", searchResult);
		} catch (Exception e){
			e.printStackTrace();
		}
		return JSON;
		
	}
}
