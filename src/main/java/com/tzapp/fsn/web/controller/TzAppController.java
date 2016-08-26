package com.tzapp.fsn.web.controller;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import com.gettec.fsnip.fsn.service.account.TZAccountService;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.tzapp.fsn.service.TzAppService;
import com.tzapp.fsn.util.StringUtil;
import com.tzapp.fsn.vo.ConfirmReceiptVO;
import com.tzapp.fsn.vo.TzAppRequestParamVO;
import com.tzapp.fsn.vo.TzAppResultVO;
import com.tzapp.fsn.vo.TzAppSearchAndScanVO;

/**
 * 提供给台账App的接口
 * @author chenxiaolin 2015-10-10
 */
@Controller
@RequestMapping("/tzApp")
public class TzAppController {
	
	@Autowired TzAppService tzAppService;
	@Autowired TZAccountService tZAccountService;
	
/************************************************成功登录后所要调用的接口*****************************************************/	
	/**
	 * 所有搜索及扫描接口调用（处审核功能中的搜索及扫描之外）
	 * @param model
	 * @param vo
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/searchScan")
	public View loadReceipt(Model model,@RequestBody TzAppSearchAndScanVO vo) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		try {
			TzAppSearchAndScanVO paramVo = tzAppService.getBusNameByOrg(vo);
			vo.setCurBusId(paramVo.getCurBusId());
			vo.setCurBusName(paramVo.getCurBusName());
			if(paramVo!=null){
				model = tzAppService.loadReceipt(model,vo);
			}
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 审核搜索和审核扫描接口
	 * @param model
	 * @param vo
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/searchReport")
	public View searchReport(Model model,@RequestBody TzAppSearchAndScanVO vo) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		try {
			TzAppSearchAndScanVO paramVo = tzAppService.getBusNameByOrg(vo);//获取当前登录的企业ID和名称
			vo.setCurBusId(paramVo.getCurBusId());
			vo.setCurBusName(paramVo.getCurBusName());
			if(paramVo!=null){
				model = tzAppService.searchReport(model,vo);
			}
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 初始化台账相关总数
	 * @param org当前组织机构
	 * @author ChenXiaolin 2015-11-24
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/total/loadTotal")
	public View loadTotal(Model model,@RequestParam(value = "org") Long org) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		try {
			TzAppSearchAndScanVO vo = new TzAppSearchAndScanVO();
			vo.setOrg(org);
			TzAppSearchAndScanVO paramVo = tzAppService.getBusNameByOrg(vo);//获取当前登录的企业ID和名称
			vo.setCurBusId(paramVo.getCurBusId());
			vo.setCurBusName(paramVo.getCurBusName());
			if(paramVo!=null){
				model = tzAppService.loadTotal(model,vo);
			}
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 收货单详情
	 * @param model
	 * @param id 收货单ID
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @param page 第几页
	 * @param pageSize 每页显示记录数
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/detail/loadReceipt")
	public View loadReceiptDetail(Model model,@RequestParam(value = "id") Long id,
										@RequestParam(value = "width",required = false) Integer width,
										@RequestParam(value = "height",required = false) Integer height,
										@RequestParam(value = "page") int page,
										@RequestParam(value = "pageSize") int pageSize) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		try {
			TzAppRequestParamVO busInfo = new TzAppRequestParamVO();
			busInfo.setId(id);
			busInfo.setPage(page);
			busInfo.setPageSize(pageSize);
			busInfo.setStatus(0);//收货详情标识
			busInfo.setWidth(width==null?0:width);
			busInfo.setHeight(height==null?0:height);
			model = tzAppService.loadReceiptDetail(model,busInfo);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 退货单详情
	 * @param model
	 * @param id 退货单ID
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @param page 第几页
	 * @param pageSize 每页显示记录数
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/detail/returnSingle")
	public View returnSingle(Model model,@RequestParam(value="id") Long id,
										@RequestParam(value = "width",required = false) Integer width,
										@RequestParam(value = "height",required = false) Integer height,
										@RequestParam(value = "page") int page,
										@RequestParam(value = "pageSize") int pageSize) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		try {
			TzAppRequestParamVO busInfo = new TzAppRequestParamVO();
			busInfo.setId(id);
			busInfo.setPage(page);
			busInfo.setPageSize(pageSize);
			busInfo.setStatus(1);//退货详情标识
			busInfo.setWidth(width==null?0:width);
			busInfo.setHeight(height==null?0:height);
			model = tzAppService.loadReceiptDetail(model,busInfo);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 *  根据台账信息ID加载产品详情
	 * @param model
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @param page 第几页
	 * @param pageSize 每页显示记录数
	 * @param proId 台账信息ID
	 * @param status 0:收货单详情->查看产品详情  1：退货单详情->查看产品详情
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/lookProductDetail")
	public View lookProductDetail(Model model,@RequestParam(value = "width",required = false) Integer width,
											  @RequestParam(value = "height",required = false) Integer height,
											  @RequestParam(value = "page") int page,
											  @RequestParam(value = "pageSize") int pageSize,
											  @RequestParam(value = "acInfoId") Long acInfoId,
											  @RequestParam(value = "status") int status) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		try {
			TzAppRequestParamVO busInfo = new TzAppRequestParamVO();
			if(busInfo!=null){
				busInfo.setPage(page);
				busInfo.setPageSize(pageSize);
				busInfo.setAcInfoId(acInfoId);//台账信息ID
				busInfo.setStatus(status);
				busInfo.setWidth(width==null?0:width);
				busInfo.setHeight(height==null?0:height);
				model = tzAppService.lookProductDetail(model,busInfo);
			}
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 供应商详情
	 * @param model
	 * @param id 收货单ID
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/lookBusInfo")
	public View lookBusInfo(Model model,HttpServletRequest request,@RequestParam(value = "busId") Long busId,
																   @RequestParam(value = "width" ,required = false) Integer width,
																   @RequestParam(value = "height",required = false) Integer height) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		try {
			TzAppRequestParamVO busInfo = new TzAppRequestParamVO();
			if(busInfo!=null){
				busInfo.setBusId(busId.toString());//企业ID
				busInfo.setRequset(request);
				busInfo.setWidth(width==null?0:width);
				busInfo.setHeight(height==null?0:height);
				model = tzAppService.lookBusInfo(model,busInfo);
			}
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 确认收货
	 * @param model
	 * @param vo 前台封装实际收货数量、详情ID、收货单ID、当前组织机构ID的Vo对象
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/confirmReceipt")
	public View confirmReceipt(Model model,@RequestBody ConfirmReceiptVO vo) {
		TzAppResultVO resultVO = new TzAppResultVO();
		try {
			tzAppService.confirmReceipt(vo);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 *  拒收
	 * @param model
	 * @param org 当前组织机构ID
	 * @param id 收货单ID
	 * @param refuseReason 拒收原因
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/refuseReceipt/{org}")
	public View refuseReceipt(Model model,@PathVariable(value = "org") Long org,
										@RequestParam(value="id") long id,
										@RequestParam(value="refuseReason",required = false) String refuseReason) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		try {
			tZAccountService.returnOfGoods(id,org,refuseReason);//直接调用台账网页版接口
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 确认退货
	 * @param model
	 * @param id 收货单ID
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/confirm/returnOfGoods")
	public View confirmReturnOfGoods(Model model,@RequestParam(value="id") Long id) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		try {
			tzAppService.confirmReturnOfGoods(id);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 加载未审核的报告列表
	 * @param model
	 * @param org 当前组织机构ID
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @param page 第几页
	 * @param pageSize 每页显示记录数
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/load/noCheck/{org}")
	public View noCheck(Model model,@PathVariable(value = "org") Long org,
									@RequestParam(value="width",required = false) Integer width,
									@RequestParam(value="height",required = false) Integer height,
									@RequestParam(value="page") int page,
									@RequestParam(value="pageSize") int pageSize) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		TzAppSearchAndScanVO vo = new TzAppSearchAndScanVO();
		try {
			vo.setOrg(org);
			TzAppSearchAndScanVO paramVo = tzAppService.getBusNameByOrg(vo);//获取当前登录的企业ID和名称
			paramVo.setPage(page);
			paramVo.setPageSize(pageSize);
			paramVo.setWidth(width==null?0:width);
			paramVo.setHeight(height==null?0:height);
			if(paramVo!=null){
				model = tzAppService.noCheck(model,paramVo);
			}
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 根据报告ID获取审核报告详情
	 * @param model
	 * @param id 报告ID
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/detail/lookReportl")
	public View reportlDetail(Model model,@RequestParam(value="id") Long id,
										  @RequestParam(value="width",required = false) Integer width,
										  @RequestParam(value="height",required = false) Integer height) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		try {
			TzAppRequestParamVO busInfo = new TzAppRequestParamVO();
			busInfo.setId(id);//报告ID
			busInfo.setWidth(width==null?0:width);
			busInfo.setHeight(height==null?0:height);
			model = tzAppService.reportlDetail(model,busInfo);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 确认报告审核通过
	 * @param model
	 * @param id 报告ID
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/confirm/reportPass/{org}")
	public View reportPass(Model model,@PathVariable(value="org") Long org,
									   @RequestParam(value="id") Long id) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		TzAppSearchAndScanVO vo = new TzAppSearchAndScanVO();
		vo.setOrg(org);
		try {
			TzAppSearchAndScanVO paramVo = tzAppService.getBusNameByOrg(vo);
			paramVo.setReportId(id);//报告ID
			paramVo.setPublishFlag(6);//设置商超审核通过状态
			paramVo.setPass(true);//审核通过标识
			tzAppService.reportPass(paramVo);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 报告退回
	 * @param model
	 * @param id 报告ID
	 * @param returnReason 退回原因
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/confirm/returnOfReport/{org}")
	public View returnOfReport(Model model,@PathVariable(value="org") Long org,
										   @RequestParam(value="id") Long id,
										   @RequestParam(value="returnReason",required = false) String returnReason) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		TzAppSearchAndScanVO vo = new TzAppSearchAndScanVO();
		vo.setOrg(org);
		try {
			TzAppSearchAndScanVO paramVo = tzAppService.getBusNameByOrg(vo);
			paramVo.setReportId(id);//报告ID
			paramVo.setPublishFlag(5);//设置商超退回至供应商状态
			paramVo.setReturnReason(returnReason!=null?returnReason:"");//t退回原因
			paramVo.setPass(false);//审核退回标识
			tzAppService.reportPass(paramVo);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 获取快捷原因列表
	 * @param model
	 * @param type 收货单拒收或报告退回 1：报告退回  2：收货单拒收
	 * @param page 第几页
	 * @param pageSize 每页显示记录数
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/back/results/{type}")
	public View backResults(Model model,@PathVariable(value="type") int type,
										@RequestParam(value="page") int page,
										@RequestParam(value="pageSize") int pageSize) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		try {
			TzAppRequestParamVO busInfo = new TzAppRequestParamVO();
			busInfo.setType(type);
			busInfo.setPage(page);
			busInfo.setPageSize(pageSize);
			model = tzAppService.backResults(model,busInfo);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 处理图片大小
	 * @param fullUrl 图片的原路径
	 * @param width 剪切后的宽度
	 * @param height 剪切后的高度
	 * @author ChenXiaolin 2015-11-25
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/deal/image")
	public View dealImage(Model model,@RequestParam(value="fullUrl") String fullUrl,
									  @RequestParam(value="width") int width,
									  @RequestParam(value="height") int height) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		String cutUrl = "";
		try {
			cutUrl = StringUtil.getImgPath(fullUrl, width, height);
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("newUrl", cutUrl);
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
/***************************************************用户登录及密码找回接口*********************************************************/
	/**
	 * 台账APP验证用户登录
	 * @param model
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/login")
	public View login(Model model,@RequestParam(value = "username") String username,
								  @RequestParam(value = "password") String password) {
		

		TzAppResultVO resultVO = new TzAppResultVO();
		TzAppSearchAndScanVO vo = null;
		TzAppSearchAndScanVO busInfo = null;
		try {
			String casServiceURL = SSOClientUtil.getServiceURLOfCurrentCAS().replace("/service", "")+"/webservice/users/login";
			StringBuffer sb = new StringBuffer();
			sb.append("userName=").append(URLEncoder.encode(username, "utf-8"));
			sb.append("&password=").append(URLEncoder.encode(password, "utf-8"));
			String result = SSOClientUtil.send(casServiceURL, SSOClientUtil.POST, sb.toString());//send to sso 
			if(result!=null&&!"".equals(result)){
				JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
				Object code = jsonResult.get("code");
				Object status = jsonResult.get("status");
				String nameStr = jsonResult.get("userBaseInfo").toString();
				JSONObject jsonName = (JSONObject)JSONSerializer.toJSON(nameStr);
				Long myOrg = Long.parseLong(jsonName.get("organizationId").toString());
				if(myOrg!=null&&myOrg>0){
					//根据当前组织机构ID获取企业名称
					vo = new TzAppSearchAndScanVO();
					vo.setOrg(myOrg);
					busInfo = tzAppService.getBusNameByOrg(vo);
				}
				if(busInfo!=null){
					busInfo.setOrg(myOrg);
				}
				if(code!=null&&status!=null){
					resultVO.setStatus((Boolean)status);
					if("1000".equals(code)){
						resultVO.setMessage("返回成功");
					}else if("1005".equals(code)){
						resultVO.setMessage("服务器内部异常");
					}else if("1009".equals(code)){
						resultVO.setMessage("验证用户失败");
					}else if("1010".equals(code)){
						resultVO.setMessage("该用户已被停用");
					}
				}else{
					resultVO.setStatus(false);
					resultVO.setMessage("服务器异常");
				}
			}else{
				resultVO.setStatus(false);
				resultVO.setMessage("服务器返回空值");
			}
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("org", busInfo!=null?busInfo.getOrg():0);
		model.addAttribute("busName", busInfo!=null?busInfo.getCurBusName():"");
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 忘记密码时验证用户名的正确与否
	 * 正确：发送验证码到该用户的注册手机号码   错误：不发送验证码、提示用户不正确的信息(如：用户不存在等等)
	 * @param model
	 * @param username 用户名
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/checkUserName")
	public View checkUsername(Model model,@RequestParam(value = "username") String username) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		try {
			String casServiceURL = SSOClientUtil.getServiceURLOfCurrentCAS()+"/portal/user/checkUserNameSendCode";
			StringBuffer sb = new StringBuffer();
			sb.append("name=").append(URLEncoder.encode(username, "utf-8"));
			String result = SSOClientUtil.send(casServiceURL, SSOClientUtil.GET, sb.toString());//send to sso 
			if(result!=null&&!"".equals(result)){
				JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
				Object status = jsonResult.get("status");
				if(status!=null){
					boolean flag = (Boolean) status;
					resultVO.setStatus(flag);
					if(flag){
						boolean phone = (Boolean) jsonResult.get("sendToPhone");
						boolean email = (Boolean)jsonResult.get("sendToEmail");
						if(phone||email){
							resultVO.setMessage("验证码已发送至你的手机号或者邮箱");
						}else{
							resultVO.setMessage("您在注册时未填写手机号或邮箱");
						}
					}else{
						resultVO.setMessage(jsonResult.get("msg")!=null?jsonResult.get("msg").toString():"服务器异常");
					}
				}else{
					resultVO.setStatus(false);
					resultVO.setMessage("服务器异常");
				}
			}else{
				resultVO.setStatus(false);
				resultVO.setMessage("服务器返回空值");
			}
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 根据用户名、新密码、验证码去设置新的密码
	 * @param model
	 * @param username 用户名
	 * @param password 新密码
	 * @param code 验证码
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/setNewPassword")
	public View setNewpassword(Model model,@RequestParam(value = "username") String username,
										@RequestParam(value = "password") String password,
										@RequestParam(value = "code") String code) {
		
		TzAppResultVO resultVO = new TzAppResultVO();
		try {
			String casServiceURL = SSOClientUtil.getServiceURLOfCurrentCAS()+"/portal/user/checkVerifyCode";
			StringBuffer sb = new StringBuffer();
			sb.append("name=").append(URLEncoder.encode(username, "utf-8"));
			sb.append("&code=").append(URLEncoder.encode(code, "utf-8"));
			sb.append("&password=").append(URLEncoder.encode(password, "utf-8"));
			String result = SSOClientUtil.send(casServiceURL, SSOClientUtil.GET, sb.toString());//send to sso 
			if(result!=null&&!"".equals(result)){
				JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
				Object status = jsonResult.get("status");
				if(status!=null){
					resultVO.setStatus("true".equals(status.toString())?true:false);
					resultVO.setMessage(jsonResult.get("msg")!=null?jsonResult.get("msg").toString():"服务器异常");
				}else{
					resultVO.setStatus(false);
					resultVO.setMessage("服务器异常");
				}
			}else{
				resultVO.setStatus(false);
				resultVO.setMessage("服务器返回空值");
			}
		} catch (Exception sex) {
			resultVO.setStatus(false);
			resultVO.setMessage("failure");
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
}
