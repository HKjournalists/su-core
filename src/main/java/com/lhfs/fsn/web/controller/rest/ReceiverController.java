package com.lhfs.fsn.web.controller.rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.test.TestRptJson;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.util.UploadUtil;

import com.lhfs.fsn.service.receiver.ReceiverService;
import com.lhfs.fsn.service.testReport.TestReportService;
import com.lhfs.fsn.vo.ResultVO;
import com.lhfs.fsn.vo.SampleVO;
import com.lhfs.fsn.vo.TestResultsVO;
import com.lhfs.fsn.web.controller.RESTResult;

@Controller
@RequestMapping("/portal/receive")
public class ReceiverController {
	@Autowired private TestReportService testReportService;
	@Autowired private ProductService productService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private ResourceService testResourceService;
	@Autowired private ReceiverService receiverService;

	/**
	 * 接收lims传过来的报告
	 * @param json
	 * @return RESTResult<List<TestRptJson>>
	 * @author 未知<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/3/16
	 */
	@RequestMapping(method = RequestMethod.GET, value="")
	public RESTResult<List<TestRptJson>> getRpt(@RequestParam String json){
		RESTResult<List<TestRptJson>> result=new RESTResult<List<TestRptJson>>(0, json);
		try {
			/* 1. 获取json */
			UploadUtil uploadUtil = new UploadUtil();
			JSONObject testResults = uploadUtil.getJSON(json);
			if(testResults==null){
				result.setMessage("指定目录下找不到json文件");
				return result;
			}
			
			testResults = testResults.getJSONObject("testResults");
			Boolean isBatch = testResults.getBoolean("batch");
			if(isBatch){
				/* 2. 如果为批次，则处理产品、生产企业、被检人信息  */
				Long organization=testResults.getLong("organizationID");
				JSONObject productVO=testResults.getJSONObject("product");
				JSONObject testeeVO=testResults.getJSONObject("testee");
				/* 2.1 保存产品信息 */
				Product orig_product = productService.getDAO().findByBarcode(productVO.getString("barcode"));
				if(orig_product == null){
					/* 新增产品 */
					boolean success = productService.createProduct(productVO,organization);
					if(!success){
						result.setMessage("产品新增失败");
						return result;
					}
				}else{
					/* 更新产品信息（保存图片）
					 * 当且仅当已有的产品没有图片时，才接收来自LIMS的图片；否则不接收。
					 *  */
					Set<Resource> proAttachments = orig_product.getProAttachments();
					if(proAttachments.isEmpty()){
						boolean isProductImg = productVO.containsKey("productImg");
						if(isProductImg){
							String productImg = productVO.getString("productImg");
							if(!productImg.equals("")||productImg != null){
								String barcode = productVO.getString("barcode");
								Product saveImgToProduct = testResourceService.setImgToProduct(productImg,barcode);
								orig_product.setProAttachments(saveImgToProduct.getProAttachments());
								orig_product.setImgUrl(saveImgToProduct.getImgUrl());
								productService.update(orig_product);
							}
						}
					}
				}
				/* 2.2 保存生产企业信息 */
				BusinessUnit orig_bussUnit = businessUnitService.findByName(productVO.getJSONObject("producer").getString("name"));
				if(orig_bussUnit==null && !productVO.getJSONObject("producer").getString("name").trim().equals("")){
					businessUnitService.create(productVO.getJSONObject("producer"));
				}
				/* 2.3 保存被检人信息 */
				BusinessUnit orig_testee = businessUnitService.findByName(testeeVO.getString("name"));
				if(orig_testee==null && !testeeVO.getString("name").trim().equals("")){
					businessUnitService.create(testeeVO);
				}
			}
			/* 3. 保存报告信息 */
			String msg = testReportService.addRpt(testResults);
			result.setMessage(msg);
			if(msg.equals("success")){
				result.setSuccess(true);
			}
			return result;
		} catch (DaoException dex) {
			((Throwable)dex.getException()).printStackTrace();
			result.setMessage("发布失败");
			return result;
		} catch (ServiceException sex) {
			((Throwable)sex.getException()).printStackTrace();
			result.setMessage("发布失败");
			return result;
		}
	}
	
	/**
	 * 接收lims传过来的报告
	 * @param json
	 * @return ResultVO
	 * @author LongXianZhen
	 */
	@RequestMapping(method = RequestMethod.GET, value="/report")
	public ResultVO receiveReport(@RequestParam String json){
		
		ResultVO resultVO = new ResultVO();
		/* 1. 获取json */
		UploadUtil uploadUtil = new UploadUtil();
		JSONObject testResults = uploadUtil.getJSON(json);
		if(testResults==null){
			resultVO.setMessage("指定目录下找不到json文件");
			return resultVO;
		}
			
		testResults = testResults.getJSONObject("testResults");
		ObjectMapper objectMapper=new ObjectMapper();
		TestResultsVO testResultsVO=null;
		/* 把json数据转换成 TestResultVO对象 */
		try {
			testResultsVO=objectMapper.readValue(testResults.toString(), TestResultsVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setMessage("json格式不正确-->"+e.getMessage());
			return resultVO;
		}
		/* 接收lims传过来的报告 */
		resultVO=receiverService.receiverReportFromLims(testResultsVO,json);
		if(resultVO.getError()==null||resultVO.getError().size()==0){
			resultVO.setStatus("true");
			resultVO.setMessage("数据解析成功");
		}else{
			resultVO.setStatus("false");
			resultVO.setMessage("数据解析失败");
		}
		System.out.println(resultVO.getMessage());
		return resultVO;
	}
}