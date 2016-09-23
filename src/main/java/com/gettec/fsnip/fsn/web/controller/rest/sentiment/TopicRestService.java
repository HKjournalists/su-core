package com.gettec.fsnip.fsn.web.controller.rest.sentiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

import com.fsnip.SentimentInvokeUtils;
import com.gettec.fsnip.fsn.model.sentiment.OrganizationTopic;
import com.gettec.fsnip.fsn.service.sentiment.OrgToTopicService;
import com.gettec.fsnip.sso.client.util.AccessUtils;

@Controller
@RequestMapping("/setimentTopic")
public class TopicRestService {

	@Autowired
	OrgToTopicService orgToTopicService;
	
	@RequestMapping(value="/topices", method=RequestMethod.GET)
	public View getTopices(HttpServletRequest request, Model model){
		System.out.println("1312312");
		return JSON;
		
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public View createTopic(@RequestBody OrganizationTopic organizationTopic, Model model){
		if(organizationTopic == null || organizationTopic.getTopicName() == null || "".equals(organizationTopic.getTopicName())){
			model.addAttribute("status", false);
			model.addAttribute("msg", "输入参数有误");
			return JSON;
		}
		
		String orgName = AccessUtils.getUserOrgName().toString();
		String topicName = orgName.split("\\.")[0]+ "_" + organizationTopic.getTopicName();
		organizationTopic.setTopicName(topicName);
		
		try{
			organizationTopic = orgToTopicService.add(organizationTopic);
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("status", false);
			model.addAttribute("msg", e.getMessage());
			return JSON;
		}
		
		if(organizationTopic != null){
			model.addAttribute("status", true);
			model.addAttribute("result", organizationTopic);
			return JSON;
		}
		
		model.addAttribute("status", false);
		model.addAttribute("msg", "系统异常，添加主题失败");
		return JSON;
		
	}
	
	@RequestMapping(value="/getTopicsByOrg", method=RequestMethod.GET)
	public View getTopicsByOrg(Model model){
		List<HashMap<String, String>> topics = null;
		String orgName = AccessUtils.getUserOrgName().toString();
		String topicNamePrefix = orgName.split("\\.")[0] + "_";
		try{
			topics = SentimentInvokeUtils.getTopics(topicNamePrefix);
		}catch (Exception e) {
			model.addAttribute("status", false);
			model.addAttribute("msg", "系统异常");
			return JSON;
		}
		
		if(topics != null){
			for(int i = 0; i <topics.size(); i++){
				Map<String, String> map = topics.get(i);
				map.put("Subject_Name", map.get("Subject_Name").split("_")[1]);
				map.put("id", String.valueOf(i + 1));
			}
		}
		model.addAttribute("status", true);
		model.addAttribute("result", topics);
		return JSON;
		
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public View deleteTopic(@PathVariable("id") String id, Model model){
		boolean result = false;
		
		try{
			result = SentimentInvokeUtils.deleteTopic(id);
		}catch (Exception e) {
			model.addAttribute("status", false);
			model.addAttribute("msg", "系统异常");
			return JSON;
		}
		
		if(result){
			model.addAttribute("status", true);
		}else{
			model.addAttribute("status", false);
			model.addAttribute("msg", "删除失败");
		}
		return JSON;
		
	}
	
	@RequestMapping(value="", method=RequestMethod.PUT)
	public View updateTopic(@RequestBody OrganizationTopic organizationTopic, Model model){
		String orgName = AccessUtils.getUserOrgName().toString();
		String topicNamePrefix = orgName.split("\\.")[0] + "_";
		boolean flag = false;
		try{
			flag = SentimentInvokeUtils.editTopicName(organizationTopic.getTopicId(), topicNamePrefix + organizationTopic.getTopicName());
			SentimentInvokeUtils.editKeywords(organizationTopic.getTopicId(), organizationTopic.getKeyword().replaceAll(" ", "\r\n"));
			SentimentInvokeUtils.editSearchWords(organizationTopic.getTopicId(), organizationTopic.getSearchWords());
		}catch (Exception e) {
			model.addAttribute("status", false);
			model.addAttribute("msg", "系统异常");
			return JSON;
		}
		
		if(flag){
			model.addAttribute("status", true);
			return JSON;
		}
		
		model.addAttribute("status", false);
		model.addAttribute("msg", "修改主题失败");
		return JSON;
	}
	
	@RequestMapping(value="findAll", method=RequestMethod.GET)
	public View findAll(Model model){
		List<OrganizationTopic> orgTopics = new ArrayList<OrganizationTopic>();
		
		try{
			orgTopics = orgToTopicService.findAll();
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("status", false);
			model.addAttribute("msg", "系统异常");
			return JSON;
		}
		
		if(orgTopics != null && orgTopics.size() > 0){
			Map<String, Long> topicMapOrg = new HashMap<String, Long>();
			for(int i=0; i< orgTopics.size(); i++){
				topicMapOrg.put(orgTopics.get(i).getTopicId(), orgTopics.get(i).getOrganizationId());
			}
			model.addAttribute("status", true);
			model.addAttribute("result", topicMapOrg);
			return JSON;
		}
		
		model.addAttribute("status", false);
		model.addAttribute("msg", "未获取到主题信息");
		return JSON;
		
	}

}
