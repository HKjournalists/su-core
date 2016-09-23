package com.gettec.fsnip.fsn.web.controller.rest.product;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import com.gettec.fsnip.fsn.service.market.MkCategoryService;
import com.gettec.fsnip.fsn.service.product.ProductCategoryInfoService;
import com.gettec.fsnip.fsn.vo.product.ProductCategoryVO;
import com.gettec.fsnip.fsn.vo.receive.ResultVO;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 查询产品信息
 * @author Zhanghui 2015/4/10
 */
@Controller
@RequestMapping("/product/category")
public class ProductCategoryRESTService {
	@Autowired private ProductCategoryInfoService productCategoryInfoService;
	@Autowired private MkCategoryService categoryService;
	
	/**
	 * 泊银接口：查询产品分类信息
	 * @param level 当前需要查询的产品分类级别
	 * @param parentCode 父产品分类的code
	 * @author Zhanghui 2015/4/27
	 */
	@RequestMapping(method=RequestMethod.GET, value="/query/{parentCode}/{level}")
	public View searchLastCategory(
			@PathVariable int level,
			@PathVariable String parentCode,
			HttpServletRequest req, 
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			List<ProductCategoryVO> listOfCategory = null;
			if(level == 1 || level == 2){
				listOfCategory = categoryService.getListOfCategoryVO(level, parentCode);
			}else if(level == 3){
				listOfCategory = productCategoryInfoService.getListVOByParentcode(parentCode, true);
			}
			model.addAttribute("data", listOfCategory);
		} catch (Exception e) {
			resultVO.setSuccess(false);
			resultVO.setMessage("获取产品分类失败！");
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
}
