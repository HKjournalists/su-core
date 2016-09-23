package com.gettec.fsnip.fsn.web.controller.rest.procurement;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.procurement.ProcurementDispose;
import com.gettec.fsnip.fsn.model.procurement.ProcurementInfo;
import com.gettec.fsnip.fsn.model.procurement.ProcurementUsageRecord;
import com.gettec.fsnip.fsn.service.procurement.ProcurementDisposeService;
import com.gettec.fsnip.fsn.service.procurement.ProcurementInfoService;
import com.gettec.fsnip.fsn.service.procurement.ProcurementUsageRecordService;
import com.gettec.fsnip.fsn.util.DateUtil;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_MEMBER_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_MEMBER_PATH;
import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

 /**
 * Procurement REST Service API
 * 
 * @author lxz
 */
@Controller
@RequestMapping("/procurement")
public class ProcurementRESTService extends BaseRESTService{
	@Autowired private ProcurementInfoService procurementInfoService;
	@Autowired private ProcurementDisposeService procurementDisposeService;
	@Autowired private ProcurementUsageRecordService procurementUsageRecordService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private final static Logger logger = Logger.getLogger(ProcurementRESTService.class);
	
	/**
	 * 获得采购信息列表
	 * @param name
	 * @param type 采购类型   1：原辅料  2：添加剂  3：包装材料
	 * @param model
	 * @author lxz
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getProcurementList/{page}/{pageSize}")
	public View getListProducer(@PathVariable(value="page")int page,
			@PathVariable(value="pageSize") int pageSize, 
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "type", required = false) int type,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			long total = procurementInfoService.getProcurementTotalByType(name,type , currentUserOrganization);
			List<ProcurementInfo> procurementInfos=procurementInfoService.getProcurementListByType(page,pageSize,name,type , currentUserOrganization);
			model.addAttribute("data",procurementInfos);
			model.addAttribute("total", total);
		} catch (ServiceException e) {
			e.printStackTrace();
		}catch (Exception e1) {
			e1.printStackTrace();
		} 
		return JSON;	
	}

	 /**
	  * 根据采购id获取采购信息
	  * @param pId
	  * @param model
	  * @param req
	  * @param resp
      * @return
      */
	 @RequestMapping(method = RequestMethod.GET, value = "/getProcurementById/{pId}")
	 public View getProcurementById(@PathVariable(value="pId")Long pId,
								 Model model,HttpServletRequest req,HttpServletResponse resp) {
		 try {
			 ProcurementInfo procurementInfo=procurementInfoService.findById(pId);
			 if(procurementInfo==null){
				 model.addAttribute("status",false);
				 return JSON;
			 }
			 model.addAttribute("data",procurementInfo);
			 model.addAttribute("status",true);
		 } catch (ServiceException e) {
			 e.printStackTrace();
			 model.addAttribute("status",false);
		 }catch (Exception e1) {
			 e1.printStackTrace();
			 model.addAttribute("status",false);
		 }
		 return JSON;
	 }
	
	/**
	 * 获得后续处理信息列表
	 * @param name
	 * @param type 采购类型   1：原辅料  2：添加剂  3：包装材料
	 * @param model
	 * @author lxz
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getDisposeList/{page}/{pageSize}")
	public View getDisposeList(@PathVariable(value="page")int page,
			@PathVariable(value="pageSize") int pageSize, 
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "type", required = false) int type,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			long total = procurementDisposeService.getProcurementDisposeTotalByType(name,type , currentUserOrganization);
			List<ProcurementDispose> disposeInfos=procurementDisposeService.getProcurementDisposeListByType(page,pageSize,name,type , currentUserOrganization);
			model.addAttribute("data",disposeInfos);
			model.addAttribute("total", total);
		} catch (ServiceException e) {
			e.printStackTrace();
		}catch (Exception e1) {
			e1.printStackTrace();
		} 
		return JSON;	
	}

	 /**
	  * 根据采购id获取使用记录
	  * @param page
	  * @param pageSize
	  * @param procurementId 采购id
	  * @param useDate 使用日期
	  * @param model
	  * @param req
      * @param resp
      * @return
      */
	 @RequestMapping(method = RequestMethod.GET, value = "/getRecordList/{page}/{pageSize}")
	 public View getRecordList(@PathVariable(value="page")int page,
								@PathVariable(value="pageSize") int pageSize,
							    @RequestParam(value = "procurementId", required = false) Long procurementId,
								@RequestParam(value = "useDate", required = false) String useDate,
								Model model,HttpServletRequest req,HttpServletResponse resp) {
		 try {
			 long total = procurementUsageRecordService.getRecordTotalByPid(useDate , procurementId);
			 List<ProcurementUsageRecord> disposeInfos=procurementUsageRecordService.getRecordListByPid(page,pageSize,useDate , procurementId);
			 model.addAttribute("data",disposeInfos);
			 model.addAttribute("total", total);
		 } catch (ServiceException e) {
			 e.printStackTrace();
		 }catch (Exception e1) {
			 e1.printStackTrace();
		 }
		 return JSON;
	 }
	
	/**
	 * 新增采购信息
	 * @param procurementInfo 采购信息实体
	 * @author lxz 2016/9/13
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/add")
	public View addProcurement(
			@RequestBody ProcurementInfo procurementInfo,
			HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			String userName=AccessUtils.getUserName().toString();
			procurementInfo.setOrganizationId(currentUserOrganization);
			procurementInfo.setCreator(userName);
			procurementInfo.setSurplusNum(procurementInfo.getProcurementNum());
			procurementInfo.setCreateDate(new Date());
			procurementInfo.setExpireDate(DateUtil.addDays(procurementInfo.getProductionDate(),procurementInfo.getExpiration())) ;
			UploadUtil uploadUtil = new UploadUtil();
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_MEMBER_PATH);
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_MEMBER_PATH); 
			//保存证件照
			for (Resource resource : procurementInfo.getHgAttachments()) {
				if (resource.getFile() != null) {
					String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
					String name = randomStr + "." + resource.getType().getRtDesc();
					boolean isSuccess = uploadUtil.uploadFile(resource.getFile(), ftpPath, name);
					if(isSuccess){
						String url;
						if(UploadUtil.IsOss()){
							url=uploadUtil.getOssSignUrl(ftpPath+"/"+name);
						}else{
							url = webUrl + "/" + name;
						}
						resource.setUrl(url);
						resource.setName(name);
					}else{
						throw new Exception("证件照图片上传失败");
					}
				}
			}
			procurementInfoService.create(procurementInfo);
			model.addAttribute("status",true);
		} catch (ServiceException e) {
			e.printStackTrace();
			model.addAttribute("status",false);
		}catch (Exception e1) {
			e1.printStackTrace();
			model.addAttribute("status",false);
		} 
		return JSON;	
		
	}
	
	/**
	 * 新增后续处理信息
	 * @param procurementDispose 后续处理实体
	 * @author lxz 2016/9/13
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addDispose")
	public View addDispose(
			@RequestBody ProcurementDispose procurementDispose,
			HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			String userName=AccessUtils.getUserName().toString();
			procurementDispose.setOrganizationId(currentUserOrganization);
			procurementDispose.setCreator(userName);
			procurementDispose.setCreateDate(new Date());
			UploadUtil uploadUtil = new UploadUtil();
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_MEMBER_PATH);
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_MEMBER_PATH); 
			//保存证件照
			for (Resource resource : procurementDispose.getDisposeAttachments()) {
				if (resource.getFile() != null) {
					String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
					String name = randomStr + "." + resource.getType().getRtDesc();
					boolean isSuccess = uploadUtil.uploadFile(resource.getFile(), ftpPath, name);
					if(isSuccess){
						String url;
						if(UploadUtil.IsOss()){
							url=uploadUtil.getOssSignUrl(ftpPath+"/"+name);
						}else{
							url = webUrl + "/" + name;
						}
						resource.setUrl(url);
						resource.setName(name);
					}else{
						throw new Exception("证件照图片上传失败");
					}
				}
			}
			procurementDisposeService.create(procurementDispose);
			//更新剩余库存数量
			ProcurementInfo procurementInfo=procurementInfoService.findById(procurementDispose.getProcurementId());
			if(procurementInfo!=null){
				procurementInfo.setSurplusNum(procurementInfo.getSurplusNum()-procurementDispose.getDisposeNum());
			}
			procurementInfoService.update(procurementInfo);
			
			model.addAttribute("status",true);
		} catch (ServiceException e) {
			e.printStackTrace();
			model.addAttribute("status",false);
		}catch (Exception e1) {
			e1.printStackTrace();
			model.addAttribute("status",false);
		} 
		return JSON;	
		
	}

	 /**
	  * 添加使用记录
	  * @param procurementUsageRecord
	  * @param req
	  * @param resp
	  * @param model
      * @return
      */
	 @RequestMapping(method = RequestMethod.POST, value = "/addRecord")
	 public View addRecord(
			 @RequestBody ProcurementUsageRecord procurementUsageRecord,
			 HttpServletRequest req,
			 HttpServletResponse resp, Model model) {
		 try {
			 String userName=AccessUtils.getUserName().toString();
			 procurementUsageRecord.setCreator(userName);
			 procurementUsageRecord.setCreateDate(new Date());

			 procurementUsageRecordService.create(procurementUsageRecord);
			 //更新剩余库存数量
			 ProcurementInfo procurementInfo=procurementInfoService.findById(procurementUsageRecord.getProcurementId());
			 if(procurementInfo!=null){
				 procurementInfo.setSurplusNum(procurementInfo.getSurplusNum()-procurementUsageRecord.getUseNum());
			 }
			 procurementInfoService.update(procurementInfo);

			 model.addAttribute("status",true);
			 model.addAttribute("procurementInfo",procurementInfo);
		 } catch (ServiceException e) {
			 e.printStackTrace();
			 model.addAttribute("status",false);
		 }catch (Exception e1) {
			 e1.printStackTrace();
			 model.addAttribute("status",false);
		 }
		 return JSON;

	 }

	 /**
	  * 修改使用记录备注
	  * @param id
	  * @param remark
	  * @param req
	  * @param resp
	  * @param model
      * @return
      */
	 @RequestMapping(method = RequestMethod.POST, value = "/updateRecordRemark/{id}")
	 public View updateRecordRemark(
			 @PathVariable(value="id") Long id,
			 @RequestParam(value = "remark") String remark,
			 HttpServletRequest req,
			 HttpServletResponse resp, Model model) {
		 try {

			 ProcurementUsageRecord remarkVo = procurementUsageRecordService.findById(id);
			 if(remarkVo!=null){
				 remarkVo.setRemark(remark);
				 procurementUsageRecordService.update(remarkVo);
				 model.addAttribute("status",true);
			 }else{
				 model.addAttribute("status",false);
			 }
		 } catch (ServiceException e) {
			 e.printStackTrace();
			 model.addAttribute("status",false);
		 }catch (Exception e1) {
			 e1.printStackTrace();
			 model.addAttribute("status",false);
		 }
		 return JSON;

	 }
}