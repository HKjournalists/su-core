package com.gettec.fsnip.fsn.web.controller.rest.sales;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.util.ArrayList;
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
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.service.sales.ElectronicDataService;
import com.gettec.fsnip.fsn.service.sales.SalesDataSortService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.DataSortVO;
import com.gettec.fsnip.fsn.vo.sales.SortFieldValueVO;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 资料排序 Controller
 * @author tangxin 2015-05-07
 *
 */
@Controller
@RequestMapping("/sales/sort")
public class DataSortRESTService {

	@Autowired 
	private SalesDataSortService salesDataSortService;
	@Autowired 
	private ElectronicDataService electronicDataService;
	
	/**
	 * 分页获取排序的产品资源 （包含已排序或未排序的）
	 * 如果用户已经执行过自定义排序操作，则返回用户自定义的排序数据，同时返回未排序的产品信息。
	 * @author tangxin 2015-05-07
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getSortProductAlbum/{page}/{pageSize}")
	public View getSortProductAlbum(@PathVariable int page, @PathVariable int pageSize, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			// 已排序的产品信息集合
			List<SortFieldValueVO> sortProductVO = salesDataSortService.getSortProductAlbum(info.getOrganization(), true, page, pageSize);
			// 未排序的产品信息集合
			List<SortFieldValueVO> notSortProductVO = salesDataSortService.getSortProductAlbum(info.getOrganization(), false, page, pageSize);
			sortProductVO = (sortProductVO != null ? sortProductVO : new ArrayList<SortFieldValueVO>());
			// 如果已排序的集合为空，则响应数据增加字段 selfSort ，值说明，selfSort=true,表示已经自定义排序，selfSort=false 表示未自定义排序
			if(sortProductVO.size() < 1){
				// 未自定义排序
				model.addAttribute("selfSort", false);
			} else {
				// 已经自定义排序
				model.addAttribute("selfSort", true);
			} 
			// 将未自定义排序的集合加入到 已排序的集合当中
			if(notSortProductVO != null && notSortProductVO.size() > 0){
				sortProductVO.addAll(notSortProductVO);
			}
			model.addAttribute("listSortProductVO", sortProductVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 分页获取排序的企业掠影信息（包含已排序或未排序的）
	 * @author tangxin 2015-05-07
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getSortBusinesAlbum/{page}/{pageSize}")
	public View getSortBusinesAlbum(@PathVariable int page, @PathVariable int pageSize, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			List<SortFieldValueVO> listSortBusinesAlbumVO = salesDataSortService.getSortBusinesAlbum(info.getOrganization(), page, pageSize);
			model.addAttribute("listSorBusinesAlbumVO", listSortBusinesAlbumVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 分页获取排序的企业掠影相册（包含已排序或未排序的）
	 * @author tangxin 2015-05-09
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getSortSalesCase/{page}/{pageSize}")
	public View getSortSalesCase(@PathVariable int page, @PathVariable int pageSize, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			List<SortFieldValueVO> listSortSalesCaseAlbumVO = salesDataSortService.getSortSalesCase(info.getOrganization(), page, pageSize);
			model.addAttribute("listSortSalesCaseAlbumVO", listSortSalesCaseAlbumVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 分页查询企业三证（组织机构证、营业执照、税务登记证）、生产许可证、荣誉证排序信息
	 * 返回的数据报告 已排序的 和 为排序的信息
	 * @author tangxin 2015-05-09
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getSanZhengPhotos/{page}/{pageSize}")
	public View getSanZhengPhotos(@PathVariable int page, @PathVariable int pageSize, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			// 查询已排序的认证信息
			List<SortFieldValueVO> listSanZhengPhotosVO = salesDataSortService.getSanZhengWithPage(info.getOrganization(), page, pageSize);
			// 如果已排序的集合为空，则响应数据增加字段 selfSort ，值说明，selfSort=true,表示已经自定义排序，selfSort=false 表示未自定义排序
			if(listSanZhengPhotosVO == null || listSanZhengPhotosVO.size() < 1){
				model.addAttribute("selfSort", false); // 未自定义排序
			}else{
				model.addAttribute("selfSort", true); // 已经自定义排序
			}
			// 对已经排序和未排序的信息进行筛选
			salesDataSortService.filterSanZhengVO(listSanZhengPhotosVO, info.getOrganization());
			model.addAttribute("listSanZhengPhotosVO", listSanZhengPhotosVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 电子资料排序功能，保存用户的排序信息
	 * @author tangxin 2015-05-10
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/saveSortData")
	public View saveSortData(@RequestBody DataSortVO dataSortVO, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			dataSortVO = salesDataSortService.saveSortData(dataSortVO, info);
			model.addAttribute("dataSortVO", dataSortVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 使用排序信息生成企业宣传资料pdf
	 * @author tangxin 2015-05-10
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/createElectData")
	public View createElectData(@RequestBody DataSortVO dataSortVO, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			resultVO = electronicDataService.createElectDate(dataSortVO, info);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
}
