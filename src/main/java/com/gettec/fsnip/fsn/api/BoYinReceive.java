package com.gettec.fsnip.fsn.api;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.api.base.AbstractSignVerify;
import com.gettec.fsnip.fsn.dataSwitchFactory.JSONObjectToReceiveTestResultSwitch;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.MkTestTemplate;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.receive.ReceiveTestResult;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.market.MkTestTemplateService;
import com.gettec.fsnip.fsn.service.product.ProductInstanceService;
import com.gettec.fsnip.fsn.service.receive.ReceiveBusKeyConfigService;
import com.gettec.fsnip.fsn.service.receive.ReceiveTestResultService;
import com.gettec.fsnip.fsn.util.HttpUtils;
import com.gettec.fsnip.fsn.vo.receive.ResultVO;
import com.lhfs.fsn.service.testReport.TestReportService;

/**
 * 
 * @author tangxin 2015/6/24
 * 
 */
@Service(value="boYinReceive")
public class BoYinReceive extends AbstractSignVerify<String>{
	
	@Autowired private ReceiveBusKeyConfigService receiveBusKeyConfigService;
	@Autowired private ReceiveTestResultService receiveTestResultService;
	@Autowired private TestReportService testReportService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private ProductInstanceService productInstanceService;
	//@Autowired private TestPropertyService testPropertyService;
	@Autowired private MkTestTemplateService templateService;
	//@Autowired private FtpService ftpService;
	
	/**
	 * 得到 ReceiveBusKeyConfigService，用来查询签名的校验信息.
	 */
	@Override
	protected ReceiveBusKeyConfigService getReceiveBusKeyConfigService() {
		return receiveBusKeyConfigService;
	}

	
	/**
	 * 获取签名的字段
	 */
	@Override
	protected String getSignField() {
		return getData();
	}

	/**
	 * AES解密，泊银的数据没有做加密，此处为覆盖父类方法，直接返回原始数据。
	 * @param content 待解密内容
	 * @param key 解密的密钥
	 * @return
	 */
	@Override
	protected String decrypt(String content, String key) throws Exception {
		return content;
	}

	/**
	 * 接收数据持久化
	 * @param data 解密后的接收数据
	 */
	@Override
	protected String doPassHandle(String data) throws Exception{
		if(data == null) {
			return null;
		}
		/**
		 * 第一步：数据持久化之数据备份
		 */
		ResultVO resultVO = new ResultVO();
		JSONObject jsonObj = JSONObject.fromObject(data);
		ReceiveTestResult recResult = JSONObjectToReceiveTestResultSwitch.execute(jsonObj, resultVO);
		if(!resultVO.isSuccess()){
			throw new Exception("JSON数据解析失败。");
		}
		/* 将网络图片另存到ftp */
		String ftp_attachments = "";
		String[] fileUrls = recResult.getAttachments().split("\\|");
		for(int i=0; i<fileUrls.length; i++){
			if("".equals(fileUrls[i])){
				continue;
			}
			ftp_attachments += HttpUtils.getPictureFtpUrl(fileUrls[i], recResult.getBarcode()) + "|";
		}
		recResult.setFtp_attachments(ftp_attachments);
		// 持久化数据
		receiveTestResultService.save(recResult, resultVO);
		/**
		 * 第二步：数据持久化之生成报告
		 */
		//TestResult result = ReceiveTestResultToTestResultSwitch.execute(recResult);
		//saveTestResult(result, 1L, "boyin201504241705"); //虚拟帐号
		if(!resultVO.isSuccess()){
			throw new Exception(resultVO.getMessage());
		}
		return null;
	}

	/**
	 * 保存报告 
	 * @author ZhangHui 2015/4/24
	 */
	@SuppressWarnings("unused")
	private void saveTestResult(TestResult test_result, Long orgnization, String userName) throws ServiceException {
		try {
			/* 1.判断该报告是否已经存在 */
			long count = testReportService.countByServiceorderAndEdition(test_result.getServiceOrder(), test_result.getEdition());
			if(count > 0){
				return;
			}
			ProductInstance sample = test_result.getSample();
			Product product = sample.getProduct();
			/* 2.保存生产企业信息 */
			boolean isSuccess_bus = businessUnitService.saveBYProducer(test_result.getSample(), orgnization);
			/* 3.保存产品信息 */
			boolean isSuccess_pro = productInstanceService.saveBYProductIns(test_result, orgnization);
			if(isSuccess_pro){
				try {
					/* 
					 * 4. template更新 
					 * 保证在只保存了产品的前提下，也能自动加载产品信息
					 */
					if(templateService.findTemplateByBarCode(product.getBarcode(), orgnization) == null){
						MkTestTemplate testTemplate = new MkTestTemplate();
						testTemplate.setBarCode(product.getBarcode());
						testTemplate.setOrignizatonId(orgnization);
						templateService.save(testTemplate, true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			/**
			 * 6. 保存被检人信息 ？？？？？？？？？？？？？？？？？？？？？？？
			 */
			//boolean isSuccess_testee = businessUnitService.saveTestee(test_result.getTestee());
			if(isSuccess_pro && isSuccess_bus){
				/* 7. 新增报告 */
				boolean isSuccess_saveReport = testReportService.createBYReport(test_result, orgnization, userName);
				if(isSuccess_saveReport){
					/* 8. 保存检测项目  */
					//testPropertyService.save(test_result, true);
					/**
					 * 9. 自动生成pdf ??????????????????? 空值校验
					 */
					//ftpService.mkUploadReportPdf(test_result);
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException("TestReportServiceImpl.saveTestResult()-->" + e.getMessage(), e.getException());
		}
	}
	
}
