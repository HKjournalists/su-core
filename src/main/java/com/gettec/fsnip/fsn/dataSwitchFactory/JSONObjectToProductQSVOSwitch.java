package com.gettec.fsnip.fsn.dataSwitchFactory;

import net.sf.json.JSONObject;
import com.gettec.fsnip.fsn.vo.product.ProductQSVO;

/**
 * json转换成ProductQSVO
 * @author ZhangHui 2015/5/18
 */
public class JSONObjectToProductQSVOSwitch {
	
	/**
	 * 执行转换操作
	 * @author ZhangHui 2015/5/18
	 */
	public static ProductQSVO execute(JSONObject jsonObj){
		ProductQSVO vo = new ProductQSVO();
		
		// id
		String id = jsonObj.getString("id");
		if(!"null".equals(id)){
			vo.setId(Long.parseLong(id));
		}
		
		// can_use
		boolean can_use = jsonObj.getBoolean("can_use");
		vo.setCan_use(can_use);
		
		// can_eidt
		boolean can_eidt = jsonObj.getBoolean("can_eidt");
		vo.setCan_eidt(can_eidt);
		
		// businessName
		String businessName = jsonObj.getString("businessName");
		vo.setBusinessName(businessName);
		
		// businessId
		String businessId = jsonObj.getString("businessId");
		if(!"null".equals(businessId)){
			vo.setBusinessId(Long.parseLong(businessId));
		}
		
		return vo;
	}

}
