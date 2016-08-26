package com.gettec.fsnip.fsn.api;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.PORTAL_PRODUCCT_DETAIL_URL;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.api.base.AbstractSignVerify;
import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.base.Nutrition;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.BusinessCertification;
import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.service.product.BusinessCertificationService;
import com.gettec.fsnip.fsn.service.product.ProductNutriService;
import com.gettec.fsnip.fsn.service.receive.ReceiveBusKeyConfigService;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.lhfs.fsn.service.product.ProductService;
import com.lhfs.fsn.vo.atgoo.CertificationVO;
import com.lhfs.fsn.vo.atgoo.NutritionVO;
import com.lhfs.fsn.vo.atgoo.ProductVO;
import com.lhfs.fsn.vo.atgoo.TestPropertyVO;

/**
 * 爱特够系统获取产品信息的服务
 * @author tangxin
 *
 */
@Service(value="atgooService")
public class AtgooService extends AbstractSignVerify<ProductVO>{

	@Autowired 
	private ReceiveBusKeyConfigService receiveBusKeyConfigService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductNutriService productNutriService;
	@Autowired
	private BusinessCertificationService businessCertificationService;
	@Autowired
	private TestPropertyService testPropertyService;
	
	/**
	 * 接口请求的最大并发量，父类的默认为null，
	 * 如果该变量的值为null，则不做接口过载处理，
	 * 如果该变量的值不null，并且值大于0，则需要验证接口并发访问量不能超过该值
	 */
	private Integer maxVisitNumber = 50;
	
	public Integer getMaxVisitNumber() {
		return maxVisitNumber;
	}

	public void setMaxVisitNumber(Integer maxVisitNumber) {
		this.maxVisitNumber = maxVisitNumber;
	}
	
	/**
	 * 得到 ReceiveBusKeyConfigService，用来查询签名的校验信息,由实现类提供具体的方法
	 */
	@Override
	protected ReceiveBusKeyConfigService getReceiveBusKeyConfigService() {
		return receiveBusKeyConfigService;
	}

	/**
	 * 将实体ProductNutrition 转换为 NutritionVO
	 * @param listNutrition 产品营养报告的实体集合，被转换的对象
	 * @return
	 */
	private List<NutritionVO> transferNutritionVO(List<ProductNutrition> listNutrition) {
		List<NutritionVO> listNutriVO = new ArrayList<NutritionVO>();
		if(listNutrition == null || listNutrition.size() < 1) {
			return listNutriVO;
		}
		//遍历实体，封装为vo
		for(ProductNutrition nutri : listNutrition) {
			if(nutri == null) continue;
			NutritionVO vo = new NutritionVO();
			vo.setName(nutri.getName());   //项目
			vo.setValue(nutri.getValue()); //含量
			vo.setUnit(nutri.getUnit());   //单位
			vo.setPer(nutri.getPer());     //条件
			vo.setNrv(nutri.getNrv());     //每日所需营养参考值
			Nutrition nt = nutri.getNutrition();
			if(nt!= null){
				vo.setDailyIntake(nt.getDailyIntake()); //日摄入量
			}
			listNutriVO.add(vo);
		}
		return listNutriVO;
	}
	
	/**
	 * 将实体BusinessCertification 转换为 CertificationVO
	 * @param listCertModel 产品认证信息的实体集合，被转换的对象
	 * @return
	 */
	private List<TestPropertyVO> transferTestPropertieVO(List<TestProperty> tps) {
		List<TestPropertyVO> tpVOs = new ArrayList<TestPropertyVO>();
		if(tps == null || tps.size() < 1) {
			return tpVOs;
		}
		//遍历实体，封装为vo
		for(TestProperty tp : tps){
			if(tp == null) continue;
			TestPropertyVO vo = new TestPropertyVO();
			vo.setAssessment(tp.getAssessment());
			vo.setId(tp.getId());
			vo.setName(tp.getName());
			vo.setResult(tp.getResult());
			vo.setStandard(tp.getStandard());
			vo.setTechIndicator(tp.getTechIndicator());
			vo.setUnit(tp.getUnit());
			tpVOs.add(vo);
		}
		return tpVOs;
	}
	
	/**
	 * 将实体BusinessCertification 转换为 CertificationVO
	 * @param listCertModel 产品认证信息的实体集合，被转换的对象
	 * @return
	 */
	private List<CertificationVO> transferCertificationVO(List<BusinessCertification> listCertModel) {
		List<CertificationVO> listCertVO = new ArrayList<CertificationVO>();
		if(listCertModel == null || listCertModel.size() < 1) {
			return listCertVO;
		}
		//遍历实体，封装为vo
		for(BusinessCertification busCert : listCertModel){
			if(busCert == null) continue;
			Certification cert = busCert.getCert();
			if(cert == null) continue;
			CertificationVO vo = new CertificationVO();
			vo.setName(cert.getName());       //认证名称
			vo.setIconUrl(cert.getImgUrl());  //证书图标Url
			Resource res = busCert.getCertResource();
			if(res != null) {
				vo.setCertUrl(res.getUrl());  //证书Url
			}
			listCertVO.add(vo);
		}
		return listCertVO;
	}
	
	/**
     * 签名验证通过后需要执行的具体操作
     * @param data 解密后的数据
     */
	@Override
	protected ProductVO doPassHandle(String data) throws Exception {
		if(data == null) {
			return null;
		}
		//解析 data 参数， 转换为 JSONObject
		String type = null, value = null;
		try {
			JSONObject json = JSONObject.fromObject(data);
			type = (String) json.get("type");
			value = (String) json.get("value");
		} catch (Exception e) {
			throw new Exception("调用API的参数data解密后不是一个有效的JSON数据，detailMessage：" + e.getMessage());
		}
		
		//参数有效性校验
		if(type == null || "".equals(type) ||
				value == null || "".equals(value)){
			throw new IllegalArgumentException("调用API的参数data解密后不是一个有效的JSON数据,不能正常解析。");
		}
		
		// 返回数据 productVO
		ProductVO productVO = null;
		if("PRODUCT_ID".equals(type)) {
			//type 为 PRODUCT_ID 时，根据产品id查询产品VO信息
			productVO = productService.findById_(Long.parseLong(value));
		} else if("BARCODE".equals(type)) {
			//type 为 BARCODE 时，根据产品barcode查询产品VO信息
			productVO = productService.findByBarcode_(value);
		}
		
		if(productVO == null) {
			throw new ClassNotFoundException("产品信息获取失败，该产品不存在或者已经被删除。");
		}
		
		//大众门户产品详情的地址
		String proDetailUrl = PropertiesUtil.getProperty(PORTAL_PRODUCCT_DETAIL_URL) + productVO.getId();
		productVO.setDetailUrl(proDetailUrl);
		
		//根据产品id 获取产品营养报告实体
		List<ProductNutrition> listNutri = productNutriService.getListOfNutrisByProductId(productVO.getId());
		//将实体ProductNutrition 转换为 NutritionVO
		List<NutritionVO> listNutriVO = transferNutritionVO(listNutri);
		productVO.setNutritionReport(listNutriVO);
		
		//根据产品id 查询产品认证信息
		List<BusinessCertification> listCertModel = businessCertificationService.getListOfCertificationByProductId(productVO.getId());
		//将实体BusinessCertification 转换为 CertificationVO
		List<CertificationVO> listCertVO = transferCertificationVO(listCertModel);
		productVO.setCertification(listCertVO);
		
		//根据报告id 获取报告检测项目实体
		List<TestProperty> tps=testPropertyService.findByReportId(productVO.getTestResultId());
		//将实体TestProperty 转换为 TestPropertyVO
		List<TestPropertyVO> tpVOs = transferTestPropertieVO(tps);
		productVO.setTestProperty(tpVOs);
		
		return productVO;
	}
	
	/**
     * 签名验证通过后需要执行的具体操作
     * @param data 解密后的数据
     */
	public ProductVO getProductInfo(String data) throws Exception {
		if(data == null) {
			return null;
		}
		//解析 data 参数， 转换为 JSONObject
		String type = null, value = null;
		try {
			JSONObject json = JSONObject.fromObject(data);
			type = (String) json.get("type");
			value = (String) json.get("value");
		} catch (Exception e) {
			throw new Exception("调用API的参数data解密后不是一个有效的JSON数据，detailMessage：" + e.getMessage());
		}
		
		//参数有效性校验
		if(type == null || "".equals(type) ||
				value == null || "".equals(value)){
			throw new IllegalArgumentException("调用API的参数data解密后不是一个有效的JSON数据,不能正常解析。");
		}
		
		// 返回数据 productVO
		ProductVO productVO = null;
		if("PRODUCT_ID".equals(type)) {
			//type 为 PRODUCT_ID 时，根据产品id查询产品VO信息
			productVO = productService.findById_(Long.parseLong(value));
		} else if("BARCODE".equals(type)) {
			//type 为 BARCODE 时，根据产品barcode查询产品VO信息
			productVO = productService.findByBarcode_(value);
		}
		
		if(productVO == null) {
			throw new ClassNotFoundException("产品信息获取失败，该产品不存在或者已经被删除。");
		}
		
		//大众门户产品详情的地址
		String proDetailUrl = PropertiesUtil.getProperty(PORTAL_PRODUCCT_DETAIL_URL) + productVO.getId();
		productVO.setDetailUrl(proDetailUrl);
		
		//根据产品id 获取产品营养报告实体
		List<ProductNutrition> listNutri = productNutriService.getListOfNutrisByProductId(productVO.getId());
		//将实体ProductNutrition 转换为 NutritionVO
		List<NutritionVO> listNutriVO = transferNutritionVO(listNutri);
		productVO.setNutritionReport(listNutriVO);
		
		//根据产品id 查询产品认证信息
		List<BusinessCertification> listCertModel = businessCertificationService.getListOfCertificationByProductId(productVO.getId());
		//将实体BusinessCertification 转换为 CertificationVO
		List<CertificationVO> listCertVO = transferCertificationVO(listCertModel);
		productVO.setCertification(listCertVO);
		
		//根据报告id 获取报告检测项目实体
		List<TestProperty> tps=testPropertyService.findByReportId(productVO.getTestResultId());
		//将实体TestProperty 转换为 TestPropertyVO
		List<TestPropertyVO> tpVOs = transferTestPropertieVO(tps);
		productVO.setTestProperty(tpVOs);
				
		return productVO;
	}
	
	
	

}
