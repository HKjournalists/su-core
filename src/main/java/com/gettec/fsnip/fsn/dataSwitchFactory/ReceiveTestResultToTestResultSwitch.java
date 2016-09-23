package com.gettec.fsnip.fsn.dataSwitchFactory;

import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.receive.ReceiveTestResult;
import com.gettec.fsnip.fsn.model.test.TestResult;

/**
 * ReceiveTestResult 转换成 TestResult
 * @author ZhangHui 2015/4/24
 */
public class ReceiveTestResultToTestResultSwitch {
	
	/**
	 * 执行转换操作
	 * @author ZhangHui 2015/4/24
	 */
	public static TestResult execute(ReceiveTestResult recResult){
		/**
		 * 第一步：封装 TestResult
		 */
		TestResult result = firstPackage(recResult);
		/**
		 * 第二步：封装 BusinessUnit-testee
		 */
		// result = secondPackage(result, recResult);
		/**
		 * 第三步：封装 ProductInstance-sample
		 */
		result = thirdPackage(result, recResult);
		
		return result;
	}

	/**
	 * 封装 TestResult
	 * @author ZhangHui 2015/4/24
	 */
	private static TestResult firstPackage(ReceiveTestResult recResult) {
		TestResult result = new TestResult();
		
		// 检测数量
		result.setSampleQuantity(recResult.getAmount());
		
		// 检验地点
		result.setTestPlace(recResult.getAddress());
		
		// 检验日期
		result.setTestDate(recResult.getTest_date());
		
		// 检验类别
		result.setTestType("企业自检");
		
		// 执行标准
		result.setStandard("/");
		
		// 检测结论描述
		result.setResult("/");
		
		// 备注
		result.setComment(recResult.getMemo());
		
		// 报告状态
		result.setPublishFlag('3');
		
		// 报告编号
		result.setServiceOrder(recResult.getEdition() + recResult.getReceive_id());
		
		// 样品编号
		result.setSampleNO(result.getServiceOrder() + "-1");
		
		// 报告来源标识
		result.setEdition(result.getEdition());
		
		// 检验机关
		result.setTestOrgnization(recResult.getDeptname());
		
		return result;
	}
	
	/**
	 * 封装 BusinessUnit-testee
	 * @author ZhangHui 2015/4/24
	 */
	@SuppressWarnings("unused")
	private static TestResult secondPackage(TestResult result, ReceiveTestResult recResult) {
		BusinessUnit testee = new BusinessUnit();
		
		result.setTestee(testee);
		return result;
	}
	
	/**
	 * 封装 ProductInstance-sample
	 */
	private static TestResult thirdPackage(TestResult result, ReceiveTestResult recResult) {
		ProductInstance proIns = new ProductInstance();
		// 批次
		proIns.setBatchSerialNo(recResult.getBatch());
		// 生产日期
		proIns.setProductionDate(recResult.getManufacture_date());
		
		/**
		 *  1. 封装产品信息
		 */
		Product product = new Product();
		product.setName(recResult.getName());          // 产品名称
		product.setFormat(recResult.getSpec());        // 产品规格
		product.setBarcode(recResult.getBarcode());    // 产品条形码
		product.setImgUrl(recResult.getAttachments()); // 产品图片
		//??????? 保质期、保质天数、产品包装方式
		/**
		 * 封装产品类别
		 */
		// ??????????????
		/**
		 * 封装商标信息
		 */
		BusinessBrand brand = new BusinessBrand();
		brand.setName(recResult.getBrand()); // 商标名称
		product.setBusinessBrand(brand);
		
		proIns.setProduct(product);  // 产品信息封装 END
		
		/**
		 * 2. 封装生产企业信息
		 */
		BusinessUnit producer = new BusinessUnit();
		producer.setGuid(recResult.getManufacturer_id());    // 生产企业唯一标识
		producer.setName(recResult.getManufacturer_name());  // 生产企业名称
		producer.setLicense(new LicenseInfo(recResult.getManufacturer_licenseno())); // 生产企业营业执照
		
		proIns.setProducer(producer);  // 生产企业信息封装 END
		result.setSample(proIns);      // 产品实例信息封装 END
		return result;
	}
}
