package com.gettec.fsnip.fsn.web.controller.rest.product;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_QS_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_QS_PATH;
import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import com.gettec.fsnip.fsn.dataSwitchFactory.JSONObjectToProductQSVOSwitch;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.ProductionLicenseInfoApplicantClaim;
import com.gettec.fsnip.fsn.model.resource.ResourceOfProlicinfoApplicantClaim;
import com.gettec.fsnip.fsn.service.business.ProductionLicenseService;
import com.gettec.fsnip.fsn.service.product.Business2QsRelationService;
import com.gettec.fsnip.fsn.service.product.ProductLicenseOfApplicantClaimHandleService;
import com.gettec.fsnip.fsn.service.product.ProductTobusinessUnitService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.product.ProductQSVO;
import com.gettec.fsnip.fsn.vo.product.ProductionLicenseApplicantClaimHandleVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 查询产品信息
 * @author Zhanghui 2015/4/10
 */
@Controller
@RequestMapping("/product/qsno")
public class ProductQsNoRESTService {
	@Autowired private Business2QsRelationService business2QsRelationService;
	@Autowired private ProductionLicenseService productionLicenseService;
	@Autowired private ProductLicenseOfApplicantClaimHandleService productLicenseOfApplicantClaimHandleService;
	@Autowired private ProductTobusinessUnitService productToBusinessUnitService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * 功能描述：分页查询当前登录企业下的qs号，用于在qs权限设置页面展示
	 * @author Zhanghui 2015/5/15
	 */
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	@RequestMapping(method=RequestMethod.GET, value="/list/myown/{page}/{pageSize}")
	public View getListOfQs(
			@PathVariable(value="page") int page, 
			@PathVariable(value="pageSize") int pageSize,
			HttpServletRequest req, 
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			
			long total = business2QsRelationService.countOfMyOwn(currentUserOrganization);
			List<ProductQSVO> vos = business2QsRelationService.getListOfQsByPage(currentUserOrganization, 1, page, pageSize);
			
			Map map = new HashMap();
			map.put("listOfQs", vos);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (Exception e) {
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：分页查询当前登录企业下的qs号，用于在qs权限设置页面展示
	 * @author Zhanghui 2015/5/15
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/getBus2QsOfHaveRight/{qsId}")
	public void getBus2QsOfHaveRight(
			@PathVariable(value="qsId") Long qsId, 
			@RequestParam String callback,
			HttpServletRequest req, 
			HttpServletResponse resp,
			Model model){
		
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			List<ProductQSVO> vos = business2QsRelationService.getListOfQs(qsId, 0);
			String result = callback + "([";
			for(int i=0; i<vos.size(); i++){
				if(i == (vos.size()-1)){
					result += JSONObject.fromObject(vos.get(i)).toString();
					break;
				}
				result += JSONObject.fromObject(vos.get(i)).toString() + ",";
			}
			result += "])";
			resp.setContentType("application/x-javascript; charset=utf-8");
			resp.getWriter().write(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 功能描述：新增一条qs授权许可
	 * @author Zhanghui 2015/5/18
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/bus2QsCreate/{qsId}/{bus2qsId}/{owerBusName}")
	public void bus2QsCreate(
			@PathVariable(value="qsId") Long qsId,
			@PathVariable(value="bus2qsId") Long bus2qsId,
			@PathVariable(value="owerBusName") String owerBusName,
			@RequestParam String callback,
			@RequestParam String models,
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model){
		
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			JSONObject jsonObj = JSONObject.fromObject(models.replace("[", "").replace("]", ""));
			ProductQSVO vo = JSONObjectToProductQSVOSwitch.execute(jsonObj);
			vo.setQsId(qsId);
			
			// 2 新增
			boolean isSuccess = business2QsRelationService.create(vo, owerBusName);
			// 3 新增成功
			if(isSuccess){
				if(vo.getId() == null){
					// 3.1 获取id
					Long id = business2QsRelationService.findIdByVO(vo);
					vo.setId(id);
				}
				/**
				 *  3.2 更新 qs号-主人企业 关系
				 *     can_use  更新为 true
				 *     can_edit 更新为 true
				 */
				business2QsRelationService.updateOfRight(bus2qsId, 4, 4);
				
				// 4 将 qs号-被授权企业-产品 设置为有效
				productToBusinessUnitService.updateByEffect(vo.getQsId(), vo.getBusinessId(), 1);
				
				// 5 返回
				String result = callback + "([" + JSONObject.fromObject(vo).toString();
				result += "])";
				resp.setContentType("application/x-javascript; charset=utf-8");
				resp.getWriter().write(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 功能描述：编辑一条qs授权许可
	 * @author Zhanghui 2015/5/18
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/bus2QsUpdate")
	public void bus2QsUpdate(
			@RequestParam String callback,
			@RequestParam String models,
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model){
		
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			JSONObject jsonObj = JSONObject.fromObject(models.replace("[", "").replace("]", ""));
			ProductQSVO vo = JSONObjectToProductQSVOSwitch.execute(jsonObj);
			boolean isSuccess = business2QsRelationService.updateOfRight(vo.getId(), vo.isCan_use()?1:2, vo.isCan_eidt()?1:2);
			if(isSuccess){
				String result = callback + "([" + JSONObject.fromObject(vo).toString();
				result += "])";
				resp.setContentType("application/x-javascript; charset=utf-8");
				resp.getWriter().write(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 功能描述：编辑一条 qs-主人企业 关系
	 * @param owner_bus_id qs的主企业id
	 * @param ownerId  qs-主企业 关系 id
	 * @param qsId     qs号id
	 * @param can_use_mychoice
	 * 				0 代表仅自己
	 * 				1 代表指定企业
	 * @author Zhanghui 2015/5/18
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/update/{owner_bus_id}/{ownerId}/{qsId}/{can_use_mychoice}")
	public View update(
			@PathVariable(value="owner_bus_id") Long owner_bus_id,
			@PathVariable(value="ownerId") Long ownerId,
			@PathVariable(value="qsId") Long qsId,
			@PathVariable(value="can_use_mychoice") int can_use_mychoice,
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			
			if(owner_bus_id==null || ownerId==null || qsId==null){
				throw new Exception("参数为空");
			}
			
			boolean isSuccess = business2QsRelationService.updateOfRight(ownerId, can_use_mychoice==0?1:4, can_use_mychoice==0?1:4);
			if(isSuccess && can_use_mychoice==0){
				// 删除该qs号所有的授权记录
				business2QsRelationService.deleteOfBeAuthorized(qsId, owner_bus_id);
			}
		}catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：删除一条qs授权许可
	 * @author Zhanghui 2015/5/19
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/bus2QsDestroy/{qsId}")
	public void bus2QsDestroy(
			@PathVariable(value="qsId") Long qsId, 
			@RequestParam String callback,
			@RequestParam String models,
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model){
		
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			JSONObject jsonObj = JSONObject.fromObject(models.replace("[", "").replace("]", ""));
			ProductQSVO vo = JSONObjectToProductQSVOSwitch.execute(jsonObj);
			
			if(vo==null || vo.getId()==null || vo.getId().equals(0L) || qsId==null){
				throw new Exception("参数异常");
			}
			
			vo.setQsId(qsId);
			business2QsRelationService.delete(vo);
			
			String result = callback + "([" + JSONObject.fromObject(vo).toString();
			result += "])";
			resp.setContentType("application/x-javascript; charset=utf-8");
			resp.getWriter().write(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 功能描述：验证当前qs号有无给该企业授权
	 * @author Zhanghui 2015/5/19
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/isHaveAuthorized/{businessId}/{qsId}")
	public View isHaveAuthorized(
			@PathVariable(value="businessId") Long businessId,
			@PathVariable(value="qsId") Long qsId,
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long count = business2QsRelationService.count(qsId, businessId);
			if(count > 0){
				model.addAttribute("data", true);
			}else{
				model.addAttribute("data", false);
			}
		} catch (Exception e) {
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：验证除了自己的主人外，当前qs号有无给其他企业授权
	 * @author Zhanghui 2015/5/19
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/isHaveToOtherAuthorized/{qsId}")
	public View isHaveToOtherAuthorized(
			@PathVariable(value="qsId") Long qsId,
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long count = business2QsRelationService.count(qsId, 0);
			if(count > 0){
				model.addAttribute("data", true);
			}else{
				model.addAttribute("data", false);
			}
		} catch (Exception e) {
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：分页查询当前登录企业下的qs号，用于在qs权限设置页面展示
	 * @author Zhanghui 2015/5/15
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/getQsDetailInfo/{qsId}")
	public View getQsDetailInfo(
			@PathVariable(value="qsId") Long qsId,
			HttpServletRequest req, 
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			ProductionLicenseInfo proLic = productionLicenseService.findById(qsId);
			model.addAttribute("data", proLic);
		} catch (Exception e) {
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：企业基本信息界面，获取qs list
	 * @author Zhanghui 2015/5/21
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	@RequestMapping(method=RequestMethod.GET, value="/list/myall/{page}/{pageSize}")
	public View getListOfAllQs(
			@PathVariable(value="page") int page, 
			@PathVariable(value="pageSize") int pageSize,
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			long total = business2QsRelationService.countOfMyAll(currentUserOrganization);
			List<ProductQSVO> vos = business2QsRelationService.getListOfQsByPage(currentUserOrganization, page, pageSize);
			// 当qs号为当前登录企业的引进qs号时，需要获悉该qs号有无被其他企业认领。如果已经认领，则没有删除权；如果没有认领，则可以删除。
			for(ProductQSVO vo : vos){
				if(!vo.isLocal()){
					boolean claimed = business2QsRelationService.getClaimedOfQs(vo.getQsId());
					vo.setClaimed(claimed);
				}else{
					vo.setClaimed(true);
				}
			}
			
			Map map = new HashMap();
			map.put("listOfQs", vos);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (Exception e) {
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：企业基本信息界面，新增/更新 生产许可证信息（如果判断当前企业是该qs号的主人，则会直接认领）
	 * @author Zhanghui 2015/5/21
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.PUT, value="/saveQsInfo/{currentBusName}")
	public View saveQsInfo(
			@RequestBody ProductionLicenseInfo proLicInfo,
			@PathVariable(value="currentBusName") String currentBusName,
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			
			// 上传图片
			UploadUtil uploadUtil = new UploadUtil();
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_QS_PATH);
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_QS_PATH);
			for (Resource resource : proLicInfo.getQsAttachments()) {
				if (resource.getFile() != null) {
					String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
					String name = randomStr + "-qsimg." + resource.getType().getRtDesc();
					boolean isSuccess = uploadUtil.uploadFile(resource.getFile(), ftpPath, name);
					if(isSuccess){
						String url;
						if(UploadUtil.IsOss()){
							url = uploadUtil.getOssSignUrl(ftpPath+"/"+name);
						}else{
							url = webUrl + "/" + name;
						}
						resource.setName(name);
						resource.setUrl(url);
					}else{
						throw new Exception("图片上传失败");
					}
				}
			}
			
			// 保存qs基本信息
			productionLicenseService.save(proLicInfo, currentBusName);
		} catch (Exception e) {
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：企业基本信息界面，删除一条生产许可证信息
	 * @author Zhanghui 2015/5/22
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/delQsInfo/{bussinessId}/{qsId}")
	public View delQsInfo(
			@PathVariable(value="bussinessId") Long bussinessId, 
			@PathVariable(value="qsId") Long qsId, 
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			business2QsRelationService.delete(bussinessId, qsId);
		} catch (Exception e) {
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：当前登录企业有无使用或编辑该qs号的权限
	 * @author Zhanghui 2015/5/22
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/getRightOfQs/{qsno}")
	public View getRightOfQs(
			@PathVariable(value="qsno") String qsno, 
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			// 获取当前qs号的可操作权限
			ProductQSVO right_vo = business2QsRelationService.getRightOfQs(qsno, currentUserOrganization);
			model.addAttribute("right_vo", right_vo);
			
			// 则获取qs号的详细信息
			ProductionLicenseInfo qs_info = productionLicenseService.findByQsno(qsno);
			model.addAttribute("qs_info", qs_info);
		} catch (Exception e) {
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：发送企业认领qs号的申请
	 * @author Zhanghui 2015/5/29
	 */
	@RequestMapping(method=RequestMethod.PUT, value="/sendApplicantOfClaimQs/{currentBusId}")
	public View sendApplicantOfClaimQs(
			@RequestBody ProductionLicenseInfo proLicInfo,
			@PathVariable(value="currentBusId") Long currentBusId, 
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			
			ProductionLicenseInfoApplicantClaim proLic_applicant = new ProductionLicenseInfoApplicantClaim(proLicInfo);
			
			// 判断此次申请是否有效
			if(proLic_applicant.getQs_id() != null){
				ProductionLicenseApplicantClaimHandleVO vo = productLicenseOfApplicantClaimHandleService.findOfNotOverdue(
						proLic_applicant.getQs_id(), currentBusId);
				// 如果之前申请被退回，可继续申请
				if(vo != null && vo.getHandle_result()!=4){
					model.addAttribute("data", vo);
					model.addAttribute("result", resultVO);
					return JSON;
				}
			}
			
			// 上传图片
			List<ResourceOfProlicinfoApplicantClaim> qsAttachments = new ArrayList<ResourceOfProlicinfoApplicantClaim>();
			UploadUtil uploadUtil = new UploadUtil();
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_QS_PATH);
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_QS_PATH);
			for (Resource resource : proLicInfo.getQsAttachments()) {
				if (resource.getFile() != null) {
					String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
					String name = randomStr + "-qsimg." + resource.getType().getRtDesc();
					boolean isSuccess = uploadUtil.uploadFile(resource.getFile(), ftpPath, name);
					if(isSuccess){
						String url;
						if(UploadUtil.IsOss()){
							url = uploadUtil.getOssSignUrl(ftpPath +"/"+ name);
						}else{
							url = webUrl +"/"+ name;
						}
						resource.setName(name);
						resource.setUrl(url);
					}else{
						throw new Exception("图片上传失败");
					}
				}
				
				ResourceOfProlicinfoApplicantClaim resource_applicant = new ResourceOfProlicinfoApplicantClaim(resource);
				qsAttachments.add(resource_applicant);
			}
			proLic_applicant.setQsAttachments(qsAttachments);
			
			// 如果当前qs号在系统中不存在，则需要新增
			productionLicenseService.createNewQsInfo(proLicInfo);
			proLic_applicant.setQs_id(proLicInfo.getId());
			
			// 备份当前待申请认领qs号的详细信息
			productLicenseOfApplicantClaimHandleService.createNewRecord(proLic_applicant, currentBusId, info.getUserName());
		} catch (Exception e) {
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 获取指定企业下可以使用的QS号
	 * @author tanxin 2015-06-01
	 */
	@RequestMapping(method=RequestMethod.GET, value="/getListCanUseByBusId")
	public View getListCanUseByBusId(HttpServletRequest req, HttpServletResponse resp, Model model){
		ResultVO resultVO = new ResultVO();
		try{
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			List<ProductQSVO> listQsVO = business2QsRelationService.getListCanUseByOrganization(currentUserOrganization);
			model.addAttribute("listOfQs", listQsVO);
		} catch (Exception e) {
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
}
