package com.gettec.fsnip.fsn.web.controller.rest.report;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.test.ReportFlowLog;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.report.ReportService;
import com.gettec.fsnip.fsn.util.StatisticsClient;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.service.testReport.TestReportService;

@Controller
@RequestMapping("/report/operation")
public class operationReportRESTService {
	@Autowired private TestReportService testReportService;
	@Autowired private BusinessUnitService businessUnitService;
	/**
	 *  该service不同于testReportService的是，它只做报告相关的查询，不做任何的增删改
	 *  @author ZhangHui 2015/4/10
	 */
	@Autowired private ReportService reportService;
	@Autowired private StatisticsClient staClient;
	/**
	 * 商超审核报告<br>
	 * pass:true 审核通过<br>
	 * pass:fasle 退回<br>
	 * @author Zhanghui 2015/4/9
	 */
	@RequestMapping(method=RequestMethod.GET, value="/busSuperCheckReport/{pass}/{reportId}")
	public View getReports(
			@PathVariable Boolean pass,
			@PathVariable long reportId,
			@RequestParam(required=false) String msg, 
	        HttpServletRequest req, 
	        HttpServletResponse resp, Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			
			if(msg != null){
				msg = URLDecoder.decode(msg, "UTF-8");
			}
			String pubFlag = reportService.getPubFlagById(reportId);
			if(pass && "5".equals(pubFlag)){
				/**
				 * 商超审核通过，但该报告已经被其他商超退回
				 */
				resultVO.setShow(true); // 错误消息需要在前台展示
				throw new Exception("该报告已经被退回！");
			}else if(!pass && !"4".equals(pubFlag) && !"5".equals(pubFlag)){
				/**
				 * 商超审核退回，但该报告已经被其他商超审核通过
				 */
				resultVO.setShow(true); // 错误消息需要在前台展示
				throw new Exception("该报告已经审核通过！");
			}else if(pass&&"6".equals(pubFlag)){
				/**
				 * 商超审核通过，但该报告已经被其他商超审核通过
				 */
				resultVO.setShow(true); // 错误消息需要在前台展示
				throw new Exception("该报告已经审核通过！请刷新页面");
			}
			
			Long myOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			String bus_name = businessUnitService.findNameByOrganization(myOrganization);
			if(!pass){
				if(bus_name==null||"".equals(bus_name)){
					msg = "商超超级用户【super】退回，原因：" + msg;
				}else{
					msg = "商超【" + bus_name + "】退回，原因：" + msg;
				}
			}
			testReportService.updatePublishFlag(pass?'6':'5', reportId, msg);
			
			/* 报告日志处理 */ 
			TestResult tr=testReportService.findById(reportId);
			if(pass){
				ReportFlowLog rfl=new ReportFlowLog(tr.getId(),info.getUserName(),tr.getSample().getProduct().getBarcode()
							,tr.getSample().getBatchSerialNo(),tr.getServiceOrder(),"完成商超报告审核通过操作");
				staClient.offer(rfl);//记录报告日志为异步处理
			}else{
				ReportFlowLog rfl=new ReportFlowLog(tr.getId(),info.getUserName(),tr.getSample().getProduct().getBarcode()
						,tr.getSample().getBatchSerialNo(),tr.getServiceOrder(),"完成商超报告退回操作 退回原因："+msg);
				staClient.offer(rfl);
			}
		}catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 按当前用户的组织机构或用户姓名，获取报告列表。
	 * @author Zhanghui 2015/4/9
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping( method = RequestMethod.GET , value = "/byuser/getPassReportOfDealer/{configure}/{page}/{pageSize}")
	public View getTestReportListByUser(
			@PathVariable(value="configure") String configure,
			@PathVariable(value="page") int page, 
			@PathVariable(value="pageSize") int pageSize, 
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			/* 1 获取当前登录用户的组织机构id */
			long total;
			List<TestResult> listOfReport;
			
			String userName = info.getUserName();
			total = testReportService.countOfDealerAllPass(info.getUserOrgName(), userName, '6', configure);
			listOfReport = testReportService.getReportsOfDealerAllPassWithPage(info.getUserOrgName(),
					userName, page, pageSize, '6', configure);

			Map map = new HashMap();
			map.put("listOfReport", listOfReport);
			map.put("counts", total);
			model.addAttribute("data", map);
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
}
