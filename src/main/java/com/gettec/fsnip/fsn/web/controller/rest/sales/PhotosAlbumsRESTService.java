package com.gettec.fsnip.fsn.web.controller.rest.sales;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.util.List;

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

import com.gettec.fsnip.fsn.service.sales.PhotosAlbumsService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.AlbumVO;
import com.gettec.fsnip.fsn.vo.sales.PhotosAlbumVO;
import com.gettec.fsnip.fsn.vo.sales.ViewAlbumVO;
import com.gettec.fsnip.fsn.vo.sales.ViewReportVO;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 相册
 * @author tangxin 2015/04/24
 *
 */
@Controller
@RequestMapping("/sales/photos")
public class PhotosAlbumsRESTService {

	@Autowired PhotosAlbumsService photosAlbumsService;
	
	/**
	 * 获取企业所有的相册
	 * @author tangxin 2015-05-05
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListAlbums")
	public View getListBranch(@RequestParam(value="cut",required=false) String cut, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			List<ViewAlbumVO> albumList = photosAlbumsService.getAlbumsByOrgId(info.getOrganization(), cut, "WEB");
			model.addAttribute("data", albumList);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 获取单个相册详细信息
	 * @author tangxin 2015-05-05
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getDetailAlbums/{albumID}/{page}/{pageSize}")
	public View getDetailAlbums(@PathVariable String albumID, @PathVariable(value = "page") int page, 
			@PathVariable(value = "pageSize") int pageSize,
			@RequestParam(value = "cut", required = false) String cut, 
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			AlbumVO albumVO = photosAlbumsService.getDetailAblumByPage(info.getOrganization(), albumID, cut, page, pageSize);
			model.addAttribute("albumVO", albumVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 获取产品最新送检报告
	 * @author tangxin 2015-05-05
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getSelfReport/{productId}")
	public View getSelfReport(@PathVariable Long productId, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			ViewReportVO reportVO = photosAlbumsService.getViewReport(productId);
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
	 * 创建企业掠影信息
	 * @author tangxin 2015-05-06
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/create")
	public View create(@RequestBody PhotosAlbumVO albumVO,HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			albumVO = photosAlbumsService.save(albumVO, info, true);
			model.addAttribute("albumVO", albumVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 创建企业掠影信息
	 * @author tangxin 2015-05-06
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/update")
	public View update(@RequestBody PhotosAlbumVO albumVO,HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			albumVO = photosAlbumsService.save(albumVO, info, false);
			model.addAttribute("albumVO", albumVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 根据企业组织机构和相册名称查询
	 * @author tangxin 2015-05-06
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/findAlbumByName/{name}")
	public View findAlbumByName(@PathVariable String name, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			PhotosAlbumVO albumVO = photosAlbumsService.findByOrganizationAndName(info.getOrganization(), name);
			model.addAttribute("albumVO", albumVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
}
