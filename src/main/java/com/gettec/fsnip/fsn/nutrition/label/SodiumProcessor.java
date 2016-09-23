package com.gettec.fsnip.fsn.nutrition.label;

import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.nutrition.NutritionProcessor;
import com.gettec.fsnip.fsn.util.NutritionUtil;

/**
 * 钠
 * @author TangXin
 *
 */
public class SodiumProcessor extends NutritionProcessor {

	@Override
	public NutritionVO doProcess(NutritionVO nutritionVO) {
		if(nutritionVO == null || nutritionVO.getPriNutrition() == null){
			return nutritionVO;
		}
		/**
		 * 钠 实体
		 */
		ProductNutrition sodium = nutritionVO.getPriNutrition();
		String name = sodium.getName();
		name = (name == null ? "" : name);
		if("钠".equals(name)){
			int unitStatus = NutritionUtil.judgeUnit(sodium.getPer());
			float nrv = NutritionUtil.formatFloat(sodium.getNrv());
			/**
			 * 如果 营养成分的 单位 和  NRV 解析失败，直接返回
			 */
			if(unitStatus == -1 || nrv == -1f) {
				return nutritionVO;
			}
			/**
			 * 营养成分状态-固态、液态、kj 计算方式都一致
			 */
			if(NutritionUtil.STATUS_SOLID == unitStatus || NutritionUtil.STATUS_LIQUID == unitStatus){
				if(nrv <= 0.3f){
					nutritionVO.addNutritionLabel(NutritionUtil.WU_NA);
				} else if(0.3f < nrv && nrv <= 2f) {
					nutritionVO.addNutritionLabel(NutritionUtil.JI_DI_NA);
				} else if(2f < nrv && nrv <= 6f){
					nutritionVO.addNutritionLabel(NutritionUtil.DI_NA);
				}
			}
		} else if(this.getProcessor() != null) {
			/**
			 * 当前营养成分不是 钠，交给下一级处理
			 */
			nutritionVO = this.getProcessor().doProcess(nutritionVO);
		}
		return nutritionVO;
	}

}
