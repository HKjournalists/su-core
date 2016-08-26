package com.gettec.fsnip.fsn.web.controller.rest.deal;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.deal.DealProblem;
import com.gettec.fsnip.fsn.model.deal.DealToProblem;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.deal.DealProblemService;
import com.gettec.fsnip.fsn.service.deal.DealToProblemService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.RabbitMQUtil;
import com.gettec.fsnip.fsn.util.SystemDefaultInterface;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;


@Controller
@RequestMapping("/deal")
public class DealProblemRESTService extends BaseRESTService{
	
	@Autowired 
	private BusinessUnitService businessUnitService;
	@Autowired 
	private DealToProblemService dealToProblemService;
	@Autowired 
	private DealProblemService dealProblemService;
	
	final static String SUPERVISE_JG=PropertiesUtil.getProperty(SystemDefaultInterface.RABBIT_MQ_SUPERVISE_JG);
	final static String SUPERVISE_KEY=PropertiesUtil.getProperty(SystemDefaultInterface.RABBIT_MQ_SUPERVISE_KEY);
	final static String SUPERVISE_DIRECT=PropertiesUtil.getProperty(SystemDefaultInterface.RABBIT_MQ_SUPERVISE_DIRECT);
	/**
	 * 获得当前问题列表数据
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getDealToProblemList/{page}/{pageSize}")
	public View getListProducer(@PathVariable(value="page")int page,
			@PathVariable(value="pageSize") int pageSize, 
			@RequestParam(value = "barcode", required = false) String barcode,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
			try {
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
				long total = dealToProblemService.getdealToProblemTotal(barcode,fromBusId);
				List<DealToProblem>   dealToProblemList = dealToProblemService.getdealToProblemList(page, pageSize, barcode,fromBusId);
				model.addAttribute("data",dealToProblemList);
				model.addAttribute("total", total);
			    model.addAttribute("status", true);
			} catch (Exception e) {
				model.addAttribute("status", false);
				e.printStackTrace();
			}
		return JSON;	
	}
	/**
	 * 获得问题处理并且通知监管
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/editDealToProblem/{id}")
	public View editDealToProblem(@PathVariable(value="id") long id,Model model,HttpServletRequest req,HttpServletResponse resp) {
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			RabbitMQUtil rabbitMQUtil=new RabbitMQUtil();
				Channel channel=rabbitMQUtil.connect();
				channel.exchangeDeclare(SUPERVISE_JG, SUPERVISE_DIRECT,true);
				channel.queueDeclare(SUPERVISE_JG, true, false, false, null);  
				channel.queueBind(SUPERVISE_JG,SUPERVISE_JG,SUPERVISE_KEY);
				JSONObject jsonObject = dealToProblemService.getDealProblemLogVO(id,info);  
				channel.basicPublish("",SUPERVISE_JG, MessageProperties.PERSISTENT_TEXT_PLAIN, jsonObject.toString().getBytes("UTF-8"));  
			model.addAttribute("status", true);
		} catch (Exception e) {
			model.addAttribute("status", false);
			e.printStackTrace();
		}
		return JSON;	
	}
	
	@RequestMapping(method = RequestMethod.GET,value = "/getDealList/{page}/{pageSize}/{status}")
	public View getDealList(
			@PathVariable(value = "page")int page,
			@PathVariable(value = "pageSize") int pageSize,
			@PathVariable(value = "status") int status,
			@RequestParam(value = "businessName", required = false) String businessName,
			@RequestParam(value = "licenseNo", required = false) String licenseNo,
			@RequestParam(value = "barcode", required = false) String barcode,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response
			){
	
			try {
				//获取当前登录用户的ID
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
				
				long totalRecord = dealProblemService.getTotalRecord(businessName,licenseNo,barcode,fromBusId,status);
				
				List<DealProblem> dealProblemsList = dealProblemService.getProblemList(page,pageSize,businessName,licenseNo,barcode,fromBusId,status);
				model.addAttribute("data", dealProblemsList);
				model.addAttribute("total", totalRecord);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (DaoException e) {
				e.printStackTrace();
			}
			
		
		return JSON;
	}
	

	//根据ID和提交状态来，通知监管，通知企业，忽略，三个按钮都掉这个方法
	@RequestMapping(method = RequestMethod.GET,value = "/notice/{id}/{status}/{pageStatus}")
	public View notice(
			@PathVariable("id") long id,
			@PathVariable("status") long status,
			@PathVariable("pageStatus") long pageStatus,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response){
		
		try {
			DealProblem sendproblem =  dealProblemService.noticeComplainById(id,status);
			
			if (status == 0) {
				//通知监管要发RabbitMQ,
				RabbitMQUtil rabbitMQUtil=new RabbitMQUtil();
				Channel channel=rabbitMQUtil.connect();
				channel.exchangeDeclare(SUPERVISE_JG, SUPERVISE_DIRECT,true);
				channel.queueDeclare(SUPERVISE_JG, true, false, false, null);  
				channel.queueBind(SUPERVISE_JG,SUPERVISE_JG,SUPERVISE_KEY);
				JSONObject jsonObject = dealToProblemService.getJsonDealToProblem(sendproblem,pageStatus);  
				channel.basicPublish("",SUPERVISE_JG, MessageProperties.PERSISTENT_TEXT_PLAIN, jsonObject.toString().getBytes("UTF-8"));   
			} else if (status == 1) {
				//通知企业
				dealToProblemService.getNoticeDealToProblem(sendproblem);   
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON;
	}
	
	//删除
	@RequestMapping(method = RequestMethod.DELETE,value = "/delete/{id}")
	public View delete(@PathVariable("id") long id,Model model){
		try {
			dealProblemService.deleteDealProblemById(id);
			model.addAttribute("result", "1");
		} catch (Exception e) {
			model.addAttribute("result", "2");
			e.printStackTrace();
		}
		
		return JSON;
	}
	//恢复
	@RequestMapping(method = RequestMethod.GET,value = "/recover/{id}")
	public View recover(@PathVariable("id") long id,Model model){
			try {
				dealProblemService.backComplain(id);
				model.addAttribute("result", "1");
			} catch (Exception e) {
				model.addAttribute("result", "2");
				e.printStackTrace();
			}
			
		return JSON;
	}
	
}
