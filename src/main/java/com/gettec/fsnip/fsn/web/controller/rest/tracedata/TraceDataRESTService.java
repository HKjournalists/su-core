package com.gettec.fsnip.fsn.web.controller.rest.tracedata;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.gettec.fsnip.fsn.service.trace.TraceDataService;
import com.gettec.fsnip.sso.client.util.AccessUtils;

@Controller
@RequestMapping("/traceData")
public class TraceDataRESTService{
	@Autowired private TraceDataService traceDataService;
	@RequestMapping("/getListTracedata")
	public View  getListTracedata(Model model,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize){
		long org=Long.valueOf(AccessUtils.getUserOrg().toString());
		List<TraceData> list=traceDataService.getbyOrgId(org,page,pageSize);
		model.addAttribute("list",list);
		model.addAttribute("count",traceDataService.countbyOrg(org));
		return JSON;
	}
	@RequestMapping(value="/delete",method=RequestMethod.DELETE)
	public View addTracedata(Model model,@RequestParam("id") long id){
		try {
			this.traceDataService.delete(id);
			model.addAttribute("status",true);
		} catch (ServiceException e) {
			e.printStackTrace();
			model.addAttribute("status",false);
		}
		return null;
	}
	@RequestMapping(value="/save",method = {RequestMethod.POST,RequestMethod.PUT})
	public View save(Model model,@RequestBody TraceData traceData){
		try {
			traceData.setOrganization(Long.valueOf(AccessUtils.getUserOrg().toString()));
			traceDataService.update(traceData);
			model.addAttribute("status",true);
		} catch (ServiceException e) {
			e.printStackTrace();
			model.addAttribute("status",false);
		}
		return JSON;
	}

	@RequestMapping(value="/getTraceDataById",method = RequestMethod.GET)
	public View save(Model model,@RequestParam("id") long id){
		try {
			model.addAttribute("traceData",this.traceDataService.findById(id));
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSON;
	}
}
