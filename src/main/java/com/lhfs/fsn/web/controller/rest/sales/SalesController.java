package com.lhfs.fsn.web.controller.rest.sales;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.sales.BusinessSalesInfoService;
import com.gettec.fsnip.fsn.service.sales.ContractService;
import com.gettec.fsnip.fsn.service.sales.ElectronicDataService;
import com.gettec.fsnip.fsn.service.sales.PhotosAlbumsService;
import com.gettec.fsnip.fsn.service.sales.SalesBranchService;
import com.gettec.fsnip.fsn.service.sales.SalesResourceService;
import com.gettec.fsnip.fsn.util.sales.AnnexDownLoad;
import com.gettec.fsnip.fsn.util.sales.BaseMailDefined;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.AlbumVO;
import com.gettec.fsnip.fsn.vo.sales.ContractVOAPP;
import com.gettec.fsnip.fsn.vo.sales.DataCenterVO;
import com.gettec.fsnip.fsn.vo.sales.MaterialsVO;
import com.gettec.fsnip.fsn.vo.sales.ReportAppVO;
import com.gettec.fsnip.fsn.vo.sales.SalesBranchVO;
import com.gettec.fsnip.fsn.vo.sales.ViewAlbumVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.vo.sales.EnterpriseIntroductionVO;
import com.lhfs.fsn.vo.sales.EnterpriseViewImgVO;

/**
 * 销售网点 and 推荐购买方式 Controller
 * @author tangxin 2015/04/24
 *
 */
@Controller
@RequestMapping("/sales/fsnApp")
public class SalesController {

	@Autowired private BusinessSalesInfoService businessSalesInfoService;
	@Autowired private PhotosAlbumsService photosAlbumsService;
	@Autowired private ElectronicDataService electronicDataService;
	@Autowired private ContractService contractService;
	@Autowired private SalesResourceService salesResourceService;
	@Autowired private SalesBranchService salesBranchService;
	@Autowired private BusinessUnitService businessUnitService;
	
	/**
	 * 企业封面图片接口 报告logo和宣传照
	 * @author tangxin 2015-04-27
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/findEnterpriseViewImgs")
	public View findEnterpriseViewImgs(@RequestParam(value = "organization") Long organization, HttpServletRequest request,Model model) {
		ResultVO resultVO = new ResultVO();
		
		//构造发送邮件地址
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"
		                  +request.getServerName()+":"
		                  +request.getServerPort()+path+"/"
		                  +"resource/img/";
		
		try{
			EnterpriseViewImgVO viewImgVO = businessSalesInfoService.findEnterpriseViewImgByOrganization(organization,false);
			String url = viewImgVO.getEnterpriseImgUrl();
			if(url!=null && !url.equals(""))
			{
				String [] urls = url.split("\\|");
				for(int i=0;i<urls.length;i++)
					viewImgVO.addEnterseImgUrls(urls[i]);
				viewImgVO.setEnterpriseImgUrl(urls[0]);
			}else{
				String defaultUrl = basePath+"enterprise_default_pic.png";
				viewImgVO.setEnterpriseImgUrl(defaultUrl);
				viewImgVO.addEnterseImgUrls(defaultUrl);
			}
			model.addAttribute("enterprise", viewImgVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	
	/**
	 * 企业封面图片接口 报告logo和宣传照
	 * @author liuwx
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/findEnterpriseViewImgsEx")
	public View findEnterpriseViewImgsEx(@RequestParam(value = "organization") Long organization,HttpServletRequest request, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			EnterpriseViewImgVO viewImgVO = businessSalesInfoService.findEnterpriseViewImgByOrganization(organization,false);
			String url = viewImgVO.getEnterpriseImgUrl();
			if(url!=null && !url.equals(""))
			{
				String [] urls = url.split("\\|");
				for(int i=0;i<urls.length;i++)
					viewImgVO.addEnterseImgUrls(urls[i]);
			}else{
				//构造发送邮件地址
				String path = request.getContextPath();
				String basePath = request.getScheme()+"://"
				                  +request.getServerName()+":"
				                  +request.getServerPort()+path+"/"
				                  +"resource/img/";
				String defaultUrl = basePath+"enterprise_default_pic.png";
				viewImgVO.setEnterpriseImgUrl(defaultUrl);
				viewImgVO.addEnterseImgUrls(defaultUrl);
			}
			model.addAttribute("enterprise", viewImgVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	
	
	
	/**
	 * 获取企业简介信息
	 * @author tangxin 2015-04-27
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/findIntroduction")
	public View findIntroductionByOrganization(@RequestParam(value = "organization") Long organization, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			EnterpriseIntroductionVO  enterpriseVO = businessSalesInfoService.findEnterpriseIntroductionByOrganization(organization);
			model.addAttribute("introduction", enterpriseVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 企业APP 相册接口
	 * @author tangxin 2015-05-05
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getAlbums")
	public View getAlbums(@RequestParam(value = "organization") Long organization,
			@RequestParam(value = "cut", required = false) String cut, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			List<ViewAlbumVO> viewAlbum = photosAlbumsService.getAlbumsByOrgId(organization, cut, "APP");
			model.addAttribute("viewAlbum", viewAlbum);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 企业APP 单个相册接口
	 * @author tangxin 2015-05-05
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getDetailAlbums")
	public View getDetailAlbums(@RequestParam(value = "organization") Long organization, 
			@RequestParam(value = "albumID") String albumID, @RequestParam(value = "cut", required = false) String cut,
			@RequestParam(value = "page") int page, @RequestParam(value = "pageSize") int pageSize, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AlbumVO albumVO = photosAlbumsService.getDetailAblumByPage(organization, albumID, cut, page, pageSize);
			albumVO.setFiledToNull(); /* 简化返回的数据量 */
			model.addAttribute("album", albumVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 获取产品最新送检报告 企业APP
	 * @author tangxin 2015-05-07
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getSelfReport/{productId}")
	public View getSelfReport(@PathVariable Long productId, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			ReportAppVO reportVO = photosAlbumsService.findReportByBusAPP(productId);
			model.addAttribute("reportVO", reportVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	
	/**
	 * 获取产品最新送检报告 企业APP
	 * @author tangxin 2015-05-07
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getReportDetail")
	public View getReportDetail(@RequestParam Long productId,@RequestParam int type, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			ReportAppVO reportVO = photosAlbumsService.findReportByBusAPP(productId,type);
			model.addAttribute("reportVO", reportVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	
	
	
	/**
	 * 发送电子邮件 (企业APP接口)
	 * @author tangxin 2015-05-10
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/sendMail")
	public View getSelfReport(@RequestParam(value="to") String to, @RequestParam(value="subject") String subject,
			@RequestParam(value="text") String text, @RequestParam(value="attachments", required=false) String attachments,
			@RequestParam(value="organization", required=false) Long organization,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = null;
		try{
			List<Long> ids = null;
			if(attachments != null) {
				JSONArray array = JSONArray.fromObject(attachments);
				if(array != null){
					ids = new ArrayList<Long>();
					for(Object obj : array){
						if(obj != null){
							ids.add(Long.parseLong(obj.toString()));	
						}
					}
				}
			}
			BaseMailDefined baseMail = new BaseMailDefined(to,null,subject,text,ids);
			AuthenticateInfo info = new AuthenticateInfo();
			info.setOrganization(organization);
			resultVO = electronicDataService.sendMail(baseMail, info);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 企业资料中心 (企业APP接口)
	 * @author tangxin 2015-05-10
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getDataCenter")
	public View getDataCenter(@RequestParam(value = "organization") Long organization, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			List<MaterialsVO> materials = electronicDataService.getListMaterials(organization);
			materials = (materials == null ? new ArrayList<MaterialsVO>() : materials);
			List<ContractVOAPP> contracts = contractService.getListForAPPWithPage(organization, null, -1, 0);
			contracts = (contracts == null ? new ArrayList<ContractVOAPP>() :contracts);
			DataCenterVO dataCenter = new DataCenterVO(contracts,materials);
			model.addAttribute("dataCenter", dataCenter);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 企业APP 打包下载用户勾选的电子合同和电子资料
	 * @param attachmentId 用户勾选附件的id集合 格式：attachmentId=1,2,3(兼容 attachmentId=1,2,3,)
	 * @author tangxin 2015-04-30
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/downLoadAttachments")
	public void downLoadElectData(@RequestParam(value="attachmentId") String attachmentId,
			@RequestParam(value="organization", required = false) Long organization,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		File file = null;
		try{
			if(attachmentId != null && !"".equals(attachmentId)){
				String lastChar = attachmentId.substring(attachmentId.length()-1);
				attachmentId = (",".equals(lastChar) ? attachmentId : attachmentId+",");
				String enterName = businessUnitService.findNameByOrganization(organization);
				file = salesResourceService.downLoadElectData(attachmentId, enterName);
				resp.reset();
				resp = AnnexDownLoad.downloadZip(file, resp);
				resp.flushBuffer();
			}
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		} finally{
            try {
               if(file != null) {
            	   AnnexDownLoad.delFolder(file.getParentFile().getPath());
               }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * 企业APP 分页获取销售网点信息
	 * @author tangxin 2015-05-13
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getSalesBranch")
	public View getSalesBranch(@RequestParam(value = "organization") Long organization, @RequestParam(value = "page") int page, 
			@RequestParam(value = "pageSize") int pageSize, Model model)  {
		ResultVO resultVO = new ResultVO();
		try{
			long totals = salesBranchService.countByConfigure(organization, null);
			List<SalesBranchVO> listBranch = salesBranchService.getListByOrganizationWithPage(organization, null, page, pageSize);
			model.addAttribute("totals", totals);
			model.addAttribute("listBranch", listBranch);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
}
