package com.gettec.fsnip.fsn.web.controller.rest.procurement;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.procurement.OnlineSaleGoods;
import com.gettec.fsnip.fsn.model.procurement.OnlineSaleGoodsDispose;
import com.gettec.fsnip.fsn.model.procurement.SaleRecord;
import com.gettec.fsnip.fsn.service.procurement.OnlineSaleGoodsDisposeService;
import com.gettec.fsnip.fsn.service.procurement.OnlineSaleGoodsService;
import com.gettec.fsnip.fsn.service.procurement.SaleRecordService;
import com.gettec.fsnip.fsn.util.DateUtil;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;
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
 * ProcurementOnlineSale REST Service API
 * 
 * @author suxiang
 */
@Controller
@RequestMapping("/procurement")
public class OnlineSaleGoodsRESTService extends BaseRESTService{
	@Autowired private OnlineSaleGoodsDisposeService disposeService;
	@Autowired private OnlineSaleGoodsService onlineSaleGoodsService;
	@Autowired private SaleRecordService saleRecordService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//	private final static Logger logger = Logger.getLogger(OnlineSaleGoodsRESTService.class);

	
	/**
	 * 获得在售销售商品信息列表
	 * @param name
	 * @param model
	 * @author suxiang
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getOnlineSaleList/{page}/{pageSize}")
	public View getOnlineSaleList(@PathVariable(value="page")int page,
			@PathVariable(value="pageSize") int pageSize, 
			@RequestParam(value = "name", required = false) String name,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			long total = onlineSaleGoodsService.getOnlineSaleTotal(name,currentUserOrganization);
			List<OnlineSaleGoods> onlineSaleGoods = onlineSaleGoodsService.getOnlineSaleList(page,pageSize,name,currentUserOrganization);
			
			model.addAttribute("data",onlineSaleGoods);
			model.addAttribute("total", total);
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		return JSON;	
	}

	
	
	/**
	 * 新增在售商品信息
	 * @param  OnlineSaleGoods 在售商品信息实体
	 * @author suxiang
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/create")
	public View addProcurement(
			@RequestBody OnlineSaleGoods onlineSaleGoods,
			HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			String userName=AccessUtils.getUserName().toString();
			onlineSaleGoods.setOrganizationId(currentUserOrganization);
			onlineSaleGoods.setCreator(userName);
			onlineSaleGoods.setSurplusNum(onlineSaleGoods.getProcurementNum());
			onlineSaleGoods.setCreateDate(new Date());
			onlineSaleGoods.setExpireDate(DateUtil.addDays(onlineSaleGoods.getProductionDate(),onlineSaleGoods.getExpiration())) ;
			UploadUtil uploadUtil = new UploadUtil();
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_MEMBER_PATH);
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_MEMBER_PATH); 
			//保存证件照
			for (Resource resource : onlineSaleGoods.getHgAttachments()) {
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
			onlineSaleGoodsService.create(onlineSaleGoods);
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
	  * 根据在售id获取在售信息
	  * @param pId
	  * @param model
	  * @param req
	  * @param resp
     * @return
     */
	 @RequestMapping(method = RequestMethod.GET, value = "/getSaleProcurementById/{pId}")
	 public View getSaleProcurementById(@PathVariable(value="pId")Long pId,
								 Model model,HttpServletRequest req,HttpServletResponse resp) {
		 try {
			 OnlineSaleGoods saleGoods = onlineSaleGoodsService.findById(pId);
			 if(saleGoods==null){
				 model.addAttribute("status",false);
				 return JSON;
			 }
			 model.addAttribute("data",saleGoods);
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
	  * 根据在售id获取销售记录
	  * @param page
	  * @param pageSize
	  * @param onlineSaleId 在售id
	  * @param saleDate 销售日期
	  * @param model
	  * @param req
     * @param resp
     * @return
     */
	 @RequestMapping(method = RequestMethod.GET, value = "/getSaleRecordList/{page}/{pageSize}")
	 public View getSaleRecordList(@PathVariable(value="page")int page,
								@PathVariable(value="pageSize") int pageSize,
							    @RequestParam(value = "onlineSaleId", required = false) Long onlineSaleId,
								@RequestParam(value = "saleDate", required = false) String saleDate,
								Model model,HttpServletRequest req,HttpServletResponse resp) {
		 try {
			 long total = saleRecordService.getRecordTotalByPid(saleDate , onlineSaleId);
			 List<SaleRecord> saleRecords = saleRecordService.getRecordListByPid(page,pageSize,saleDate , onlineSaleId);
			 
			 model.addAttribute("data",saleRecords);
			 model.addAttribute("total", total);
		 } catch (Exception e1) {
			 e1.printStackTrace();
		 }
		 return JSON;
	 }
	
	 
	 /**
	  * 添加销售记录
	  * @param SaleRecord
	  * @param req
	  * @param resp
	  * @param model
      * @return
      */
	 @RequestMapping(method = RequestMethod.POST, value = "/addSaleRecord")
	 public View addRecord(
			 @RequestBody SaleRecord saleRecord,
			 HttpServletRequest req,
			 HttpServletResponse resp, Model model) {
		 try {
			 String userName=AccessUtils.getUserName().toString();
			 saleRecord.setCreator(userName);

			 saleRecordService.create(saleRecord);
			 //更新剩余库存数量
			 OnlineSaleGoods onlineSaleGoods = onlineSaleGoodsService.findById(saleRecord.getOnlineSaleId());
			 if(onlineSaleGoods!=null){
				 onlineSaleGoods.setSurplusNum(onlineSaleGoods.getSurplusNum() - saleRecord.getSaleNum());;
			 }
			 onlineSaleGoodsService.update(onlineSaleGoods);

			 model.addAttribute("status",true);
			 model.addAttribute("onlineSaleGoods",onlineSaleGoods);
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
		 * @param OnlineSaleGoodsDispose 后续处理实体
		 *
		 */
		@RequestMapping(method = RequestMethod.POST, value = "/addSaleDispose")
		public View addDispose(
				@RequestBody OnlineSaleGoodsDispose saleGoodsDispose,
				HttpServletRequest req, 
				HttpServletResponse resp, Model model) {
			try {
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				String userName=AccessUtils.getUserName().toString();
				saleGoodsDispose.setOrganizationId(currentUserOrganization);
				saleGoodsDispose.setCreator(userName);
				saleGoodsDispose.setCreateDate(new Date());
				UploadUtil uploadUtil = new UploadUtil();
				String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_MEMBER_PATH);
				String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_MEMBER_PATH); 
				//保存证件照
				for (Resource resource : saleGoodsDispose.getOnlineDisposeAttachments()) {
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
				disposeService.create(saleGoodsDispose);
				//更新剩余库存数量
				OnlineSaleGoods saleGoods = onlineSaleGoodsService.findById(saleGoodsDispose.getOnlineSaleId());
				if(saleGoods!=null){
					saleGoods.setSurplusNum(saleGoods.getSurplusNum()-saleGoodsDispose.getDisposeNum());
				}
				onlineSaleGoodsService.update(saleGoods);
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
		 * @param model
		 */
		@RequestMapping(method = RequestMethod.GET, value = "/getSaleDisposeList/{page}/{pageSize}")
		public View getSaleDisposeList(@PathVariable(value="page")int page,
				@PathVariable(value="pageSize") int pageSize, 
				@RequestParam(value = "name", required = false) String name,
				Model model,HttpServletRequest req,HttpServletResponse resp) {
			try {
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				long total = disposeService.getProcurementDisposeTotal(name,currentUserOrganization);
				List<OnlineSaleGoodsDispose> disposeInfos=disposeService.getProcurementDisposeList(page,pageSize,name,currentUserOrganization);
				model.addAttribute("data",disposeInfos);
				model.addAttribute("total", total);
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
			return JSON;	
		}

}