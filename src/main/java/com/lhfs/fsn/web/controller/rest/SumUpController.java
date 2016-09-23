package com.lhfs.fsn.web.controller.rest;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_SUCCESS;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.test.RiskAssessmentService;
import com.gettec.fsnip.fsn.service.test.TestResultService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.product.EnterpriseVo;
import com.lhfs.fsn.vo.product.ProductListVo;
import com.lhfs.fsn.vo.product.ProductRiskVo;

@Controller
@RequestMapping("/portal/statistics")
public class SumUpController {
	
	@Autowired private ProductService productService;
	@Autowired private TestResultService testResultService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private RiskAssessmentService riskAssessmentService;
	
	/**
	 * 产品总量汇总信息小喇叭接口
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@RequestMapping(method = RequestMethod.GET, value = "")
	public View getProductById( HttpServletRequest request,Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			Long productCount = productService.productCount();
			Long testResultCount = testResultService.testResultCount();
			Long unitCount = businessUnitService.unitCount();
			model.addAttribute("prodAmount", productCount);
			model.addAttribute("reportAmount", testResultCount);
			model.addAttribute("enterpriseAmount", unitCount);
			resultVO.setStatus(SERVER_STATUS_SUCCESS);
		} catch (Exception sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 营养排行接口
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/nutrition")
	public View nutrition( 
			             @RequestParam Long type,
			             @RequestParam int pageSize,
			             @RequestParam int page,
			HttpServletRequest request,Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			List<ProductListVo> productList = productService.getProductList(type, pageSize, page);
			model.addAttribute("productList", productList);
			resultVO.setStatus(SERVER_STATUS_SUCCESS);
		} catch (Exception sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		
		return JSON;
	}
	/**
	 * 风险排行接口
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/risk")
	public View riskBillboard( 
							@RequestParam String type,
							@RequestParam int pageSize,
							@RequestParam int page,
							HttpServletRequest request,Model model) {
		ResultVO resultVO = new ResultVO();
		try {
//			String type = productService.productCode(name);
			List<ProductRiskVo> riskBillboard = productService.riskBillboard(type, pageSize, page);
			int countriskBill = productService.countriskBill(type);
			model.addAttribute("productList", riskBillboard);
			model.addAttribute("count", countriskBill);
			resultVO.setStatus(SERVER_STATUS_SUCCESS);
		} catch (Exception sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		
		return JSON;
	}
	/**
	 * 企业专栏接口
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/programa")
	public View programa( 
						//@RequestParam int pageSize,
						@RequestParam String enterList,
						@RequestParam int proSize,
						HttpServletRequest request,Model model) {
		ResultVO resultVO = new ResultVO();
		try {
		    List<EnterpriseVo> countriskBills = testResultService.getEnterprise( proSize, enterList);
		    int countProductEnterprise = testResultService.countProductEnterprise();
			model.addAttribute("enterpriseList", countriskBills);
			model.addAttribute("countEnterprise", countProductEnterprise);
			resultVO.setStatus(SERVER_STATUS_SUCCESS);
		} catch (Exception sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	/**
	 * 企业专栏接口
	 * @author ZhaWanNeng<br>
	 * 最近更新人：査万能
	 * 最近更新时间：2015/4/10
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/TeaUnit")
	public View getBusinessTeaUnit( 
			@RequestParam int pageSize,
			@RequestParam int page,
			@RequestParam int proSize,
			HttpServletRequest request,Model model) {
		ResultVO resultVO = new ResultVO();
		try {

			List<EnterpriseVo> businessTeaUnit = testResultService.getBusinessTeaUnit(pageSize, page, proSize,null);
			int countProductEnterprise = testResultService.countBusinessTeaUnit(null);
			resultVO.setStatus(SERVER_STATUS_SUCCESS);
			model.addAttribute("enterpriseList", businessTeaUnit);
			model.addAttribute("countEnterprise", countProductEnterprise);
			resultVO.setStatus(SERVER_STATUS_SUCCESS);
		} catch (Exception sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	/**
	 *  获取没计算风险指数所有的产品
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/17
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/roductrisk")
	public View ProductriskAssessment( HttpServletRequest request,Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			Long productCount = productService.productCount();
			Long in = (productCount+10-1)/10;
			for (int i = 0; i < in; i++) {
				List<Product> findAll = productService.getproductList(10,i+1);
				for (Product product : findAll) {
					if(product.getCategory()!=null){
						riskAssessmentService.calculate(product, "system");
					}
				}
			}
			resultVO.setStatus(SERVER_STATUS_SUCCESS);
		} catch (Exception sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

}

















