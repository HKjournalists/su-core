package com.gettec.fsnip.fsn.web.controller.rest.waste;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WASTE_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_WASTE_PATH;
import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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

import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.waste.WasteDisposa;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.waste.WasteDisposaService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.waste.WasteDisposaVO;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;


@Controller
@RequestMapping("/wasteDisposa")
public class WasteDisposaRESTService extends BaseRESTService {
	
	static SimpleDateFormat SDFTIME = new SimpleDateFormat("yyyy-MM-dd");
	 
	@Autowired
	private WasteDisposaService wasteDisposaService;
	
	@Autowired 
	private ResourceService testResourceService;
	
	@Autowired 
	private BusinessUnitService businessUnitService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 获得当前废弃物列表
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListWaste/{page}/{pageSize}")
	public View getListProducer(@PathVariable(value="page")int page,
			@PathVariable(value="pageSize") int pageSize, 
			@RequestParam(value = "handler", required = false) String handler,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
			long total = wasteDisposaService.getWasteTotal(handler,fromBusId);
			model.addAttribute("data",wasteDisposaService.loadWasteDisposa(page, pageSize, handler,fromBusId));
			model.addAttribute("total", total);
		} catch (DaoException e1) {
			e1.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return JSON;	
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/wasteSave")
	public View save(@RequestBody WasteDisposa wasteDisposa,Model model,HttpServletRequest req,HttpServletResponse resp) throws Exception{
		ResultVO resultVO = new ResultVO();
		try {
			//testResourceService.savePiceFile(wasteDisposa);
			UploadUtil uploadUtil = new UploadUtil();
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WASTE_PATH);
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_WASTE_PATH);
			//保存废弃物处理图片
			for (Resource resource : wasteDisposa.getPiceFile()) {
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
						throw new Exception("废弃物处理图片上传失败");
					}
				}
			}
			if(wasteDisposa.getId()!=null)
			{
				wasteDisposaService.update(wasteDisposa);
			}else{
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
				wasteDisposa.setQiyeId(fromBusId);
				wasteDisposaService.create(wasteDisposa);
			}
			model.addAttribute("data", wasteDisposa);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
     * 确认删除
     */
    @RequestMapping(method = RequestMethod.GET, value = "/deleteRow/{id}")
    public View deleteGoods(Model model,@PathVariable(value="id") long tZId) {
        ResultVO resultVO = new ResultVO();
        try {
        	wasteDisposaService.delete(tZId);
        	Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
			long total = wasteDisposaService.getWasteTotal("",fromBusId);
			model.addAttribute("total", total);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        } catch (DaoException e) {
			e.printStackTrace();
		}
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**
     * 查看
     */
    @RequestMapping(method = RequestMethod.GET, value = "/findWasteDisposa/{id}")
    public View findWasteDisposa(Model model,@PathVariable(value="id") long id) {
    	ResultVO resultVO = new ResultVO();
    	try {
    		WasteDisposa wasteDisposa = wasteDisposaService.findById(id);
    		WasteDisposaVO wasteDisposaVO  = new WasteDisposaVO();
    		wasteDisposaVO.setId(wasteDisposa.getId());
    		wasteDisposaVO.setHandler(wasteDisposa.getHandler());
    		wasteDisposaVO.setQiyeId(wasteDisposa.getQiyeId());
    		try {
				wasteDisposaVO.setHandleTime(wasteDisposa.getHandleTime() != null ? SDFTIME.format(SDFTIME.parse(wasteDisposa.getHandleTime().toString())) : "");
			} catch (ParseException e) {
				e.printStackTrace();
			}
    		wasteDisposaVO.setHandleWay(wasteDisposa.getHandleWay());
    		wasteDisposaVO.setHandleNumber(wasteDisposa.getHandleNumber());
    		wasteDisposaVO.setDestory(wasteDisposa.getDestory());
    		wasteDisposaVO.setParticipation(wasteDisposa.getParticipation());
    		wasteDisposaVO.setPiceFile(wasteDisposa.getPiceFile());
			model.addAttribute("data",wasteDisposaVO);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	model.addAttribute("result", resultVO);
		return JSON;
    }
	
}
