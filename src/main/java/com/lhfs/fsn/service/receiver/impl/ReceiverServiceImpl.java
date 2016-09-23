package com.lhfs.fsn.service.receiver.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.ReportFlowLog;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.product.ProductInstanceService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.service.test.TestResultService;
import com.gettec.fsnip.fsn.util.MKReportNOUtils;
import com.gettec.fsnip.fsn.util.StatisticsClient;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.lhfs.fsn.service.receiver.ReceiverService;
import com.lhfs.fsn.vo.ErrorMessageVO;
import com.lhfs.fsn.vo.ResultVO;
import com.lhfs.fsn.vo.SampleVO;
import com.lhfs.fsn.vo.TestPropertieVO;
import com.lhfs.fsn.vo.TestResultVO;
import com.lhfs.fsn.vo.TestResultsVO;

@Service(value="receiverService")
public class ReceiverServiceImpl implements ReceiverService {
	
	@Autowired private ProductInstanceService productInstanceService;
	@Autowired private TestResultService testResultService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private ProductService productService;
	@Autowired private TestPropertyService testPropertyService;
	@Autowired private StatisticsClient staClient;
	
	/**
	 * 接收LIMS传过来的报告
	 * @param testResultVO
	 * @return ResultVO
	 * @author LongXianZhen
	 */
	public ResultVO receiverReportFromLims(TestResultsVO testResultsVO,String json) {
		ResultVO resultVO=new ResultVO();
		List<SampleVO> samples=testResultsVO.getSamples();
		List<ErrorMessageVO> errors=new ArrayList<ErrorMessageVO>();
		for(SampleVO sample:samples){
			/* 验证报告是否发布过 */
			if(sample.getReportStatus()==1){
				continue;
			}
			/* 验证报告是否存在  存在则跳过取下一个*/
			boolean isExist=testResultService.verifyReportExist(testResultsVO.getServiceOrder(),
					testResultsVO.getOrganizationID(),sample.getSampleId(),sample.getSampleNO());
			if(isExist){
				continue;
			}
			
			/* 验证该报告是否能够匹配上产品 */
			resultVO=validationProduct(sample,testResultsVO.isBatch(),json);
			if(resultVO.getStatus().equals("false")){ //匹配不上产品则跳过取下一个并记录原因和样品ID
				errors.add(new ErrorMessageVO(resultVO.getMessage(), sample.getSampleId()));
				continue;
			}
			
			/* 下载检测报告json文件  */
			UploadUtil uploadUtil = new UploadUtil();
			JSONObject testResult = uploadUtil.getJSON(sample.getJsonURL());
			if(testResult==null){
				errors.add(new ErrorMessageVO("指定目录下找不到检测报告json文件", sample.getSampleId()));
				continue;
			}
			testResult = testResult.getJSONObject("testResult");
			ObjectMapper objectMapper=new ObjectMapper();
			TestResultVO testResultVO=null;
			/* 把json数据转换成 TestResultVO对象 */
			try {
				testResultVO=objectMapper.readValue(testResult.toString(), TestResultVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				errors.add(new ErrorMessageVO("检测报告json格式不正确-->"+e.getMessage(), sample.getSampleId()));
				continue;
			}
			
			/* 上传pdf */
			String serviceOrderEng = MKReportNOUtils.convertCharacter(testResultsVO.getServiceOrder(), "");
			Map<String,String> map = uploadUtil.uploadReportPdf(testResultsVO.getEdition(), sample.getPdfURL(), 
					serviceOrderEng, testResultsVO.getTestType());
			if(map!=null&&"nullPdf".equals(map.get("msg"))){
				errors.add(new ErrorMessageVO("指定目录下找不到pdf文件", sample.getSampleId()));
				continue;
			}
			if(map==null){
				errors.add(new ErrorMessageVO("pdf文件上传失败", sample.getSampleId()));
				continue;
			}
			/* 完整的没有截取的报告路径 */ 
			String fullPdfPath = map.get("pdfPath");
			/* 政府抽检的截取前两页pdf路径；企业自检和企业送检均与fullPdfPath的值一致 */
			String interceptionPdfPath=map.get("interceptionPdf");
			
			/* 保存检测报告 */
			resultVO=saveReport(testResultsVO,sample,testResultVO,fullPdfPath,interceptionPdfPath);
			
			if(resultVO.getStatus().equals("false")){
				errors.add(new ErrorMessageVO(resultVO.getMessage(), sample.getSampleId()));
				continue;
			}
			
		}
		resultVO.setError(errors);
		return resultVO;
	}
	
	/**
	 * 保存报告信息
	 * @param testResultsVO
	 * @param sample
	 * @param testResultVO
	 * @param fullPdfPath
	 * @param interceptionPdfPath
	 * @return ResultVO
	 * @author LongXianZhen
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private ResultVO saveReport(TestResultsVO testResultsVO, SampleVO sample,
			TestResultVO testResultVO,String fullPdfPath,String interceptionPdfPath) {
		ResultVO resultVO=new ResultVO();
		/* 保存产品实例 */
		Map<String,Object> proInstanceMap=productInstanceService.saveProductInstance(sample,testResultsVO.isBatch(),testResultsVO.getOrganizationID());
		if(proInstanceMap.get("status").equals("false")){ //判断产品实例是否保存成功
			resultVO.setMessage(proInstanceMap.get("msg").toString());
			return resultVO;
		}
		ProductInstance productInstance=(ProductInstance)proInstanceMap.get("productInstance");
		/* 保存被检查人 */
		BusinessUnit testee=null;
		if(testResultsVO.isBatch()){
			if(sample.getProducer().getName().trim().equals(sample.getTestee().getName().trim())){
				testee=productInstance.getProducer();//如果被检测人等于生产企业 直接把生产企业给被检测人
			}else{
				Map<String,Object> testeeMap=businessUnitService.saveBusinessUnit(sample.getTestee());
				if(testeeMap.get("status").equals("false")&&testResultsVO.isBatch()){ //判断被检测人是否保存成功
					resultVO.setMessage(testeeMap.get("msg").toString());
					return resultVO;
				}
				testee=(BusinessUnit)testeeMap.get("business");
			}
		}
		/* 保存检测报告 */
		Map<String,Object> testResMap=testResultService.saveTestResult(testResultsVO,testResultVO,productInstance,
								testee,fullPdfPath,interceptionPdfPath,sample);
		if(testResMap.get("status").equals("false")){ //判断检测报告是否保存成功
			resultVO.setMessage(testResMap.get("msg").toString());
			return resultVO;
		}
		TestResult testResult=(TestResult)testResMap.get("testResult");
		/* 保存检测项目 */
		List<TestPropertieVO> tps=testResultVO.getTestProperties();
		for(TestPropertieVO tp:tps){
			TestProperty item = new TestProperty();
			item.setName(tp.getName()); // 检测名称
			item.setTechIndicator(tp.getTechIndicator()); // 检测依据
			item.setResult(tp.getResult()); // 检测结果
			item.setAssessment(tp.getAssessment());
			item.setUnit(tp.getUnit());
			item.setStandard(tp.getStandard());
			item.setTestResultId(testResult.getId());
			try {
				testPropertyService.create(item);
			} catch (ServiceException e) {
				((Throwable) e.getException()).printStackTrace();
				resultVO.setMessage("保存检查项目失败-->"+((Throwable) e.getException()).getMessage());
				return resultVO;
			}
		}
		//记录报告日志
		ReportFlowLog rfl=new ReportFlowLog(testResult.getId(),"LIMS人员",testResult.getSample().getProduct().getBarcode()
				,testResult.getSample().getBatchSerialNo(),testResult.getServiceOrder(),"完成 接收LIMS发布的报告 操作");
		staClient.offer(rfl);//记录报告日志为异步处理
		resultVO.setStatus("true");
		return resultVO;
	}

	/**
	 * 验证该报告是否能匹配上产品
	 * @param sample
	 * @param batch 
	 * @return ResultVO
	 * @author LongXianZhen
	 */
	private ResultVO validationProduct(SampleVO sample,boolean batch,String json) {
		ResultVO resultVO=new ResultVO();
		String barcode=sample.getBarCode().replaceAll(" ", "");
		if("".equals(barcode)||barcode==null){
			resultVO.setMessage("该报告产品条形码为空");
			return resultVO;
		}
		if("".equals(sample.getJsonURL().trim())||sample.getJsonURL().trim()==null){
			resultVO.setMessage("检测报告json文件路径为空");
			return resultVO;
		}
		if("".equals(sample.getPdfURL().trim())||sample.getPdfURL().trim()==null){
			resultVO.setMessage("pdf文件路径为空");
			return resultVO;
		}
		if("".equals(sample.getProDate().trim())||sample.getProDate().trim()==null){
			resultVO.setMessage("生产日期为空");
			return resultVO;
		}
		if("".equals(sample.getSampleNO().trim())||sample.getSampleNO().trim()==null){
			resultVO.setMessage("样品编号为空");
			return resultVO;
		}
		if(sample.getSampleId()==null){
			resultVO.setMessage("样品ID为空");
			return resultVO;
		}
		if(!batch){
			Product pro=null;
			try {
				 pro=productService.findByBarcode(barcode);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			if(pro==null){
				String data=sample.getName()+"&&&"+sample.getBarCode()+"&&&"+json;
				staClient.offer(data);
				resultVO.setMessage("该报告匹配不到产品");
				return resultVO;
			}
		}else{
			if(sample.getProducer()==null){
				resultVO.setMessage("该报告生产企业为空");
				return resultVO;
			}else if("".equals(sample.getProducer().getName().trim())||sample.getProducer().getName().trim()==null){
				resultVO.setMessage("该报告生产企业为空");
				return resultVO;
			}
			if(sample.getTestee()==null){
				resultVO.setMessage("该报受检单位为空");
				return resultVO;
			}else if("".equals(sample.getTestee().getName().trim())||sample.getTestee().getName().trim()==null){
				resultVO.setMessage("该报受检单位为空");
				return resultVO;
			}
		}
		resultVO.setStatus("true");
		return resultVO;
	}
	
}