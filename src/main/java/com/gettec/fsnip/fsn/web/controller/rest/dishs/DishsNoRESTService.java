package com.gettec.fsnip.fsn.web.controller.rest.dishs;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_DISHSNO_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_DISHSNO_PATH;
import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;


import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
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
import com.gettec.fsnip.fsn.model.dishs.DishsNo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.dishs.DishsNoService;
import com.gettec.fsnip.fsn.service.dishs.DishsNoShowService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.dishs.DishsNoShowList;
import com.gettec.fsnip.fsn.vo.dishs.DishsNoShowVO;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;

@Controller
@RequestMapping("/dishsNo")
public class DishsNoRESTService extends BaseRESTService  {

	@Autowired
	private DishsNoService dishsNoService;
	
	@Autowired 
	private ResourceService testResourceService;
	
	@Autowired 
	private BusinessUnitService businessUnitService;
	@Autowired 
	private DishsNoShowService dishsNoShowService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * 获得当前非预包装菜品管理的列表
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListDishsNo/{page}/{pageSize}")
	public View getListProducer(@PathVariable(value="page")int page,
			@PathVariable(value="pageSize") int pageSize, 
			@RequestParam(value = "dishsName", required = false) String dishsName,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
			long total = dishsNoService.getWasteTotal(dishsName,fromBusId);
			model.addAttribute("data",dishsNoService.loadWasteDisposa(page, pageSize, dishsName,fromBusId));
			model.addAttribute("total", total);
		} catch (DaoException e1) {
			e1.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return JSON;	
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/dishsNoSave")
	public View save(@RequestBody DishsNo dishsNo ,Model model,HttpServletRequest req,HttpServletResponse resp) throws Exception{
		ResultVO resultVO = new ResultVO();
		try {
		    //testResourceService.saveDishsnoFile(dishsNo);
			UploadUtil uploadUtil = new UploadUtil();
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_DISHSNO_PATH);
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_DISHSNO_PATH);
			//保存菜品图片
			for (Resource resource : dishsNo.getDishsnoFile()) {
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
						throw new Exception("菜品图片上传失败");
					}
				}
			}
			if(dishsNo.getId()!=null){
				dishsNoService.update(dishsNo);
			}else{
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
				dishsNo.setQiyeId(fromBusId);
				dishsNoService.create(dishsNo);
			}
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
        	dishsNoService.delete(tZId);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**
     * 查看
     */
    @RequestMapping(method = RequestMethod.GET, value = "/findDishsNo/{id}")
    public View findWasteDisposa(Model model,@PathVariable(value="id") long id) {
    	ResultVO resultVO = new ResultVO();
    	try {
			DishsNo dishsno = dishsNoService.findById(id);
			model.addAttribute("data",dishsno);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	model.addAttribute("result", resultVO);
		return JSON;
    }
    /**
	 * 获得当前今日菜品管理的列表
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListDishsNoShow/{page}/{pageSize}")
	public View getListDishsNoShow(@PathVariable(value="page")int page,
			@PathVariable(value="pageSize") int pageSize, 
			@RequestParam(value = "showTime", required = false) String showTime,
			@RequestParam(value = "dishsName", required = false) String dishsName,
			@RequestParam(value = "flag", required = false) boolean flag,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
	
			try {
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
				if(flag){
					
				}
				long total = dishsNoService.getDishsNoShowTotal(showTime,dishsName,fromBusId,flag);
				List<DishsNoShowVO> dishsList = dishsNoService.loadDishsNoShowVO(page, pageSize, showTime,dishsName,fromBusId,flag);
				model.addAttribute("data",dishsList);
				model.addAttribute("total", total);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		
		return JSON;	
	}
	/**
	 * 获得当前今日菜品管理的列表
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/dishsNoShowSave")
	public View dishsNoShowSave(@RequestBody DishsNoShowList voList,Model model,HttpServletRequest req,HttpServletResponse resp) {
	    try {
	    	List<DishsNoShowVO> dishsNoShowList = voList.getVoList();
			boolean  status= dishsNoShowService.saveDishsNoShow(dishsNoShowList);
			model.addAttribute("status",status);
		} catch (Exception e) {
			e.printStackTrace();
		}
	  return JSON;	
	}
}
