package com.gettec.fsnip.fsn.web.controller.rest.resort;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_MEMBER_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_MEMBER_PATH;
import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.procurement.OnlineSaleGoods;
import com.gettec.fsnip.fsn.model.resort.Resorts;
import com.gettec.fsnip.fsn.service.resort.ResortsService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.sso.client.util.AccessUtils;

@Controller
@RequestMapping("/resorts")
public class ResortRESTService {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	@Autowired ResortsService resortsService;
	
	
	
	/**
	 * 获得景区信息列表
	 * @param name
	 * @param model
	 * @author suxiang
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getResortsList/{page}/{pageSize}")
	public View getResortsList(@PathVariable(value="page")int page,
			@PathVariable(value="pageSize") int pageSize, 
			@RequestParam(value = "name", required = false) String name,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			long total = resortsService.getResortsTotal(name,currentUserOrganization);
			List<Resorts> resorts = resortsService.getResortsList(page,pageSize,name,currentUserOrganization);
			
			model.addAttribute("data",resorts);
			model.addAttribute("total", total);
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		return JSON;	
	}
	
	
	
	/**
	 * 新增景区录入
	 * @param  Resorts 景区录入实体
	 * @author suxiang
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/save")
	public View addProcurement(
			@RequestBody Resorts resorts,
			HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			if (resorts != null) {
				saveImage(resorts.getLogoAttachments());
				saveImage(resorts.getInfoAttachments());
				
				String rank = resorts.getRank();
				String[] ranks = rank.split("--");
				String lat = ranks[0];
				String lng = ranks[1];
				
				resorts.setLatitude(Float.parseFloat(lat));
				resorts.setLongitude(Float.parseFloat(lng));
				resorts.setCurrentUserOrganization(currentUserOrganization);
				resortsService.create(resorts);
				model.addAttribute("status",true);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			model.addAttribute("status",false);
		}
		return JSON;	
	}
	
	
	/**
	 * 删除景区
	 * @param  id 景区id
	 * @author suxiang
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/delete/{id}")
	public View delete(
			@PathVariable Long id,
			HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		try {
			resortsService.delete(id);
			model.addAttribute("status",true);
		} catch (ServiceException e) {
			model.addAttribute("status",false);		
			e.printStackTrace();
		}
		return JSON;
	}
	/**
	 * 根据ID获取景区
	 * @param  id 景区id
	 * @author suxiang
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getResortById/{id}")
	public View getResortById(
			@PathVariable Long id,
			HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		try {
			Resorts resorts = resortsService.findById(id);
			model.addAttribute("resorts",resorts);
			model.addAttribute("status",true);
		} catch (ServiceException e) {
			model.addAttribute("status",false);		
			e.printStackTrace();
		}
		return JSON;
	}
	/**
	 * 编辑景区
	 * @param  id 景区id
	 * @author suxiang
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/edit/{id}")
	public View edit(
			@PathVariable Long id,
			@RequestBody Resorts resorts,
			HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		try {
			saveImage(resorts.getLogoAttachments());
			saveImage(resorts.getInfoAttachments());
			Resorts oldResorts = resortsService.findById(id);
			oldResorts.setName(resorts.getName());
			oldResorts.setLogoAttachments(resorts.getLogoAttachments());
			oldResorts.setInfoAttachments(resorts.getInfoAttachments());
			oldResorts.setLevel(resorts.getLevel());
			oldResorts.setRank(resorts.getRank());
			String rank = resorts.getRank();
			String[] ranks = rank.split("--");
			String lat = ranks[0];
			String lng = ranks[1];
			resorts.setLatitude(Float.parseFloat(lat));
			resorts.setLongitude(Float.parseFloat(lng));
			oldResorts.setPlaceName(resorts.getPlaceName());
			oldResorts.setResortType(resorts.getResortType());
			oldResorts.setResortPrice(resorts.getResortPrice());
			oldResorts.setResortInfo(resorts.getResortInfo());
			oldResorts.setReserveTelephone(resorts.getReserveTelephone());
			resortsService.update(oldResorts);
			model.addAttribute("status",true);
		} catch (ServiceException e) {
			model.addAttribute("status",false);		
			e.printStackTrace();
		}
		return JSON;
	}
	
	

	private Resorts setResort(Resorts resorts) {
		if (resorts != null) {
			Resorts returnResorts = new Resorts();
			returnResorts.setId(resorts.getId());
			returnResorts.setName(resorts.getName());
			returnResorts.setLogoAttachments(resorts.getLogoAttachments());
			returnResorts.setInfoAttachments(resorts.getInfoAttachments());
			returnResorts.setLevel(resorts.getLevel());
			returnResorts.setRank(resorts.getRank());
			String rank = resorts.getRank();
			String[] ranks = rank.split("--");
			String lat = ranks[0];
			String lng = ranks[1];
			resorts.setLatitude(Float.parseFloat(lat));
			resorts.setLongitude(Float.parseFloat(lng));
			returnResorts.setPlaceName(resorts.getPlaceName());
			returnResorts.setResortType(resorts.getResortType());
			returnResorts.setResortPrice(resorts.getResortPrice());
			returnResorts.setResortInfo(resorts.getResortInfo());
			returnResorts.setReserveTelephone(resorts.getReserveTelephone());
		}
		
		return null;
	}



	public void saveImage(Set<Resource> imageAttachments){
		try {
			UploadUtil uploadUtil = new UploadUtil();
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_MEMBER_PATH);
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_MEMBER_PATH); 
			//保存景区照
			for (Resource resource : imageAttachments) {
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
						throw new Exception("景区图片上传失败");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
}
