package com.gettec.fsnip.fsn.dataSwitchFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import com.gettec.fsnip.fsn.model.receive.ReceiveSpecimendata;
import com.gettec.fsnip.fsn.model.receive.ReceiveTestProperty;
import com.gettec.fsnip.fsn.model.receive.ReceiveTestResult;
import com.gettec.fsnip.fsn.vo.receive.ResultVO;

/**
 * json转换成ReceiveTestResult
 * @author ZhangHui 2015/4/24
 */
public class JSONObjectToReceiveTestResultSwitch {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	
	/**
	 * 执行转换操作
	 * @author ZhangHui 2015/4/24
	 * @param resultVO 
	 */
	@SuppressWarnings("unchecked")
	public static ReceiveTestResult execute(JSONObject jsonObj, ResultVO resultVO){
		/**
		 * 第一步：封装 ReceiveTestResult
		 */
		ReceiveTestResult recResult = firstPackage(jsonObj, resultVO);
		if(!resultVO.isSuccess()){
			return null;
		}
		/**
		 * 第二步：封装ReceiveSpecimendata
		 */
		List<JSONObject> specimenJsonObjs = jsonObj.getJSONArray("specimendata");
		recResult = secondPackage(specimenJsonObjs, recResult, resultVO);
		if(!resultVO.isSuccess()){
			return null;
		}
		/**
		 * 第三步：封装ReceiveTestProperty
		 */
		List<JSONObject> recTestPropertiesJsonObjs = jsonObj.getJSONArray("testdata");
		recResult = thirdPackage(recTestPropertiesJsonObjs, recResult, resultVO);
		/**
		 * 返回
		 */
		return recResult;
	}

	/**
	 * 封装 ReceiveTestResult
	 * @param resultVO 
	 */
	private static ReceiveTestResult firstPackage(JSONObject jsonObj, ResultVO resultVO) {
		ReceiveTestResult recResult = new ReceiveTestResult();
		
		// 检测报告ID（非空）
		String receive_id = jsonObj.getString("id");
		if("".equals(receive_id)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检测报告ID不能为空！");
			return null;
		}
		recResult.setReceive_id(receive_id);
		
		// 检测设备序列号
		String device_sn = jsonObj.getString("device_sn");
		recResult.setDevice_sn(device_sn);
		
		// 检测设备名称（非空）
		String device_name = jsonObj.getString("device_name");
		if("".equals(device_name)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检测设备名称不能为空！");
			return null;
		}
		recResult.setDevice_name(device_name);
		
		// 检测用户名
		String username = jsonObj.getString("username");
		recResult.setUsername(username);
		
		// 检测部门（非空）
		String deptname = jsonObj.getString("deptname");
		if("".equals(deptname)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检测部门不能为空！");
			return null;
		}
		recResult.setDeptname(deptname);
		
		// 检测样品名称（非空）
		String name = jsonObj.getString("name");
		if("".equals(name)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检测样品名称不能为空！");
			return null;
		}
		recResult.setName(name);
		
		// 检测样品类型代码（非空）
		String type = jsonObj.getString("type");
		if("".equals(type)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检测样品类型代码不能为空！");
			return null;
		}
		recResult.setType(type);
		
		// 检测样品条码（非空）
		String barcode = jsonObj.getString("barcode");
		if("".equals(barcode)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检测样品条码不能为空！");
			return null;
		}
		recResult.setBarcode(barcode);
		
		// 检测样品生产厂商唯一标识（非空）
		String manufacturer_id = jsonObj.getString("manufacturer_id");
		if("".equals(manufacturer_id)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检测样品生产厂商唯一标识不能为空！");
			return null;
		}
		recResult.setManufacturer_id(manufacturer_id);
		
		// 检测样品生产厂商名称（非空）
		String manufacturer_name = jsonObj.getString("manufacturer_name");
		if("".equals(manufacturer_name)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检测样品生产厂商名称不能为空！");
			return null;
		}
		recResult.setManufacturer_name(manufacturer_name);
		
		// 检测样品生产厂商营业执照
		String manufacturer_licenseno = jsonObj.getString("manufacturer_licenseno");
		recResult.setManufacturer_licenseno(manufacturer_licenseno);
		
		// 检测样品商标
		String brand = jsonObj.getString("brand");
		recResult.setBrand(brand);
		
		// 检测样品规格（非空）
		String spec = jsonObj.getString("spec");
		if("".equals(spec)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检测样品规格不能为空！");
			return null;
		}
		recResult.setSpec(spec);
		
		// 检测样品批号（非空）
		String batch = jsonObj.getString("batch");
		if("".equals(batch)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检测样品批号不能为空！");
			return null;
		}
		recResult.setBatch(batch);
		
		try {
			// 检测样品生产日期（非空）
			String manufacture_date_str = jsonObj.getString("manufacture_date");
			if("".equals(manufacture_date_str)){
				resultVO.setSuccess(false);
				resultVO.setMessage("检测样品生产日期不能为空！");
				return null;
			}
			Date manufacture_date = sdf.parse(manufacture_date_str);
			recResult.setManufacture_date(manufacture_date);
		} catch (ParseException e) {
			resultVO.setSuccess(false);
			resultVO.setMessage("检测样品生产日期格式不正确！（yyyy-MM-dd）");
			return null;
		}
		try {
			// 检测时间（非空）
			String test_date_str = jsonObj.getString("test_date");
			if("".equals(test_date_str)){
				resultVO.setSuccess(false);
				resultVO.setMessage("检测时间不能为空！");
				return null;
			}
			Date test_date = sdf.parse(test_date_str);
			recResult.setTest_date(test_date);
		} catch (ParseException e) {
			resultVO.setSuccess(false);
			resultVO.setMessage("检测时间格式不正确！（yyyy-MM-dd）");
			return null;
		}
		
		// 检测样品零售商唯一标识
		String retailer_id = jsonObj.getString("retailer_id");
		recResult.setRetailer_id(retailer_id);
		
		// 检测样品零售商名称
		String retailer_name = jsonObj.getString("retailer_name");
		recResult.setRetailer_name(retailer_name);
		
		// 检测样品零售商营业执照号
		String retailer_licenseno = jsonObj.getString("retailer_licenseno");
		recResult.setRetailer_licenseno(retailer_licenseno);
		
		// 检测样品零售商负责人名称
		String person_in_charge = jsonObj.getString("person_in_charge");
		recResult.setPerson_in_charge(person_in_charge);
		
		// 检测样品零售商电话
		String phone_in_charge = jsonObj.getString("phone_in_charge");
		recResult.setPhone_in_charge(phone_in_charge);
		
		// 检测地点（非空）
		String address = jsonObj.getString("address");
		if("".equals(address)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检测地点不能为空！");
			return null;
		}
		recResult.setAddress(address);
		
		// 检测环境信息
		String env = jsonObj.getString("env");
		recResult.setEnv(env);
		
		// 检测数量（非空）
		String amount = jsonObj.getString("amount");
		if("".equals(amount)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检测数量不能为空！");
			return null;
		}
		recResult.setAmount(amount);
		
		// 备注
		String memo = jsonObj.getString("memo");
		recResult.setMemo(memo);
		
		// 检测报告合格标记（非空）
		String pass_str = jsonObj.getString("pass");
		if("".equals(pass_str)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检测报告合格标记不能为空！");
			return null;
		}
		try {
			int pass = Integer.parseInt(pass_str);
			recResult.setPass(pass);
		} catch (Exception e) {
			resultVO.setSuccess(false);
			resultVO.setMessage("检测报告合格标记格式不正确，必须为数字！");
			return null;
		}
		
		// 报告来源标识（非空）
		String edition = jsonObj.getString("edition");
		if("".equals(edition)){
			resultVO.setSuccess(false);
			resultVO.setMessage("报告来源标识不能为空！");
			return null;
		}
		recResult.setEdition(edition);
		
		// 原始检测样品照片文件名（非空）
		String attachments = jsonObj.getString("attachments");
		if("".equals(attachments)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检测样品照片不能为空！");
			return null;
		}
		recResult.setAttachments(attachments);
		
		// 检验类别（非空）
		String test_type = jsonObj.getString("test_type");
		if("".equals(test_type)){
			resultVO.setSuccess(false);
			resultVO.setMessage("检验类别不能为空！");
			return null;
		}
		recResult.setTest_type(test_type);
		
		try {
			// 样品过期时间
			String deadline_str = jsonObj.getString("deadline");
			Date deadline = sdf.parse(deadline_str);
			recResult.setDeadline(deadline);
		} catch (ParseException e) {
			resultVO.setSuccess(false);
			resultVO.setMessage("样品过期时间格式不正确！（yyyy-MM-dd）");
			return null;
		}
		
		// 样品生产许可证
		String qs_no = jsonObj.getString("qs_no");
		recResult.setQs_no(qs_no);
		
		recResult.setTest_type(test_type);
		
		// 返回
		return recResult;
	}
	
	/**
	 * 封装ReceiveSpecimendata
	 */
	private static ReceiveTestResult secondPackage(List<JSONObject> jsonObjs, ReceiveTestResult recResult, ResultVO resultVO) {
		List<ReceiveSpecimendata> recSpecimens = new ArrayList<ReceiveSpecimendata>();
		
		for(JSONObject jsonObj : jsonObjs){
			ReceiveSpecimendata recSpecimen = new ReceiveSpecimendata();
			
			// 检测条码（非空）
			String barcode = jsonObj.getString("barcode");
			if("".equals(barcode)){
				continue;
			}
			recSpecimen.setBarcode(barcode);
			
			// 检测物名称（非空）
			
			String material = jsonObj.getString("material");
			if("".equals(material)){
				continue;
			}
			recSpecimen.setMaterial(material);
			
			// 检测浓度
			String density_str = jsonObj.getString("density");
			if(!"".equals(density_str)){
				try {
					int density = Integer.parseInt(density_str);
					recSpecimen.setDensity(density);
				} catch (Exception e) {
					resultVO.setSuccess(false);
					resultVO.setMessage("检测浓度格式不正确，必须为数字！");
					return null;
				}
			}

			// 检测温度
			String temperature_str = jsonObj.getString("temperature");
			if(!"".equals(temperature_str)){
				try {
					int temperature = Integer.parseInt(temperature_str);
					recSpecimen.setTemperature(temperature);
				} catch (Exception e) {
					resultVO.setSuccess(false);
					resultVO.setMessage("检测温度格式不正确，必须为数字！");
					return null;
				}
			}
			
			// 检测湿度
			String humidity_str = jsonObj.getString("humidity");
			if(!"".equals(humidity_str)){
				try {
					int humidity = Integer.parseInt(humidity_str);
					recSpecimen.setHumidity(humidity);
				} catch (Exception e) {
					resultVO.setSuccess(false);
					resultVO.setMessage("检测湿度格式不正确，必须为数字！");
					return null;
				}
			}

			try {
				// 检测时间（非空）
				String test_date_str = jsonObj.getString("test_date");
				if("".equals(test_date_str)){
					continue;
				}
				Date test_date = sdf.parse(test_date_str);
				recSpecimen.setTest_date(test_date);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// 检测人
			String tester = jsonObj.getString("tester");
			recSpecimen.setTester(tester);
			
			recSpecimens.add(recSpecimen);
		}
		recResult.setRecSpecimens(recSpecimens);
		return recResult;
	}
	
	/**
	 * 封装ReceiveTestProperty
	 */
	private static ReceiveTestResult thirdPackage(List<JSONObject> jsonObjs, ReceiveTestResult recResult, ResultVO resultVO) {
		List<ReceiveTestProperty> recTestProperties = new ArrayList<ReceiveTestProperty>();
		
		for(JSONObject jsonObj : jsonObjs){
			ReceiveTestProperty recTestProperty = new ReceiveTestProperty();
			
			// 检测项目名称（非空）
			String name = jsonObj.getString("name");
			if("".equals(name)){
				continue;
			}
			recTestProperty.setName(name);

			// 单位
			String unit = jsonObj.getString("unit");
			recTestProperty.setUnit(unit);

			// 技术指标
			String indicant = jsonObj.getString("indicant");
			recTestProperty.setIndicant(indicant);

			// 检测结果
			String result = jsonObj.getString("result");
			recTestProperty.setResult(result);

			// 单项评价（非空）
			String conclusion = jsonObj.getString("conclusion");
			if("".equals(conclusion)){
				continue;
			}
			recTestProperty.setConclusion(conclusion);

			// 执行标准
			String standard = jsonObj.getString("standard");
			recTestProperty.setStandard(standard);
			
			recTestProperties.add(recTestProperty);
		}
		recResult.setRecTestProperties(recTestProperties);
		return recResult;
	}
}
