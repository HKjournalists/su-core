package com.gettec.fsnip.fsn.web.controller.rest;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.District;
import com.gettec.fsnip.fsn.model.base.Office;
import com.gettec.fsnip.fsn.model.base.SysArea;
import com.gettec.fsnip.fsn.service.base.DistrictService;
import com.gettec.fsnip.fsn.service.base.OfficeService;
import com.gettec.fsnip.fsn.service.base.SysAreaService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.BaseDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.List;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

@Controller
@RequestMapping("/sys")
public class SysRESTService {

	@Autowired protected SysAreaService sysAreaService;
	@Autowired protected DistrictService districtService;
	@Autowired protected OfficeService OfficeService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/areaTree")
	@ResponseBody
	public List<SysArea> getListArea(@RequestParam(value = "id", required = false) String id) {
		try{
			if (id == null) {
				id = "0_null";
			} else {
				id = URLDecoder.decode(id, "UTF-8");
			}
			String[] array = id.split("_");
			List<SysArea> listArea = sysAreaService.getListByParentId(Long.parseLong(array[0]));
			return listArea;
		} catch (ServiceException sex) {
			((Throwable)sex.getException()).printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getListDistrictByDescription/{description}")
	public View getListDistrict(@PathVariable String description, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			List<District> listDist = districtService.getListByDescription(description);
			model.addAttribute("data", listDist);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/officeTree")
	@ResponseBody
	public List<Office> getListOffice(@RequestParam(value = "id", required = false) String id) {
		try{
			if (id == null) {
				id = "0_null";
			} else {
				id = URLDecoder.decode(id, "UTF-8");
			}
			String[] array = id.split("_");
			List<Office> listOffice = OfficeService.getListByParnetId(Long.parseLong(array[0]));
			return listOffice;
		} catch (ServiceException sex) {
			((Throwable)sex.getException()).printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据数据类型获取基础值域数据集
	 * @author lxz 2016/08/10
	 */
	@RequestMapping( method = RequestMethod.GET , value = "/getDataSet")
	public View getDataSet(
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model) {

		ResultVO resultVO = new ResultVO();
		try {
			List<BaseDataVO> data = sysAreaService.getDataSet();
			model.addAttribute("data", data);
		}catch(Exception e){
			resultVO.setMessage(e.getMessage());
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
}
