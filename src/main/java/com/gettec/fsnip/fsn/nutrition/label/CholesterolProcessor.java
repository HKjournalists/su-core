package com.gettec.fsnip.fsn.nutrition.label;

import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.nutrition.NutritionProcessor;
import com.gettec.fsnip.fsn.util.NutritionUtil;

/**
 * 营养成分-胆固醇
 * @author TangXin
 *
 */
public class CholesterolProcessor extends NutritionProcessor{

	@Override
	public NutritionVO doProcess(NutritionVO nutritionVO) {
		if(nutritionVO == null || nutritionVO.getPriNutrition() == null){
			return nutritionVO;
		}
		/**
		 * 胆固醇 实体
		 */
		ProductNutrition cholesterol = nutritionVO.getPriNutrition();
		String name = cholesterol.getName();
		name = (name == null ? "" : name);
		if("胆固醇".equals(name)){
			/**
			 * 暂存胆固醇，计算其他营养成分依赖该实体
			 */
			nutritionVO.setCholesterol(cholesterol);
			/**
			 * 计算胆固醇标签时，依赖 低饱和脂肪，没有则不计算
			 */
			if(nutritionVO.getCycle() == 1 && nutritionVO.getResultLabel().contains(NutritionUtil.DI_BAO_HE_ZHI_FANG)){
				int unitStatus = NutritionUtil.judgeUnit(cholesterol.getPer());
				float nrv = NutritionUtil.formatFloat(cholesterol.getNrv());
				/**
				 * 如果 营养成分的 单位 和 NRV 解析失败，直接返回
				 */
				if(unitStatus == -1 || nrv == -1f) {
					return nutritionVO;
				}
				/**
				 * 营养成分为固体
				 */
				if(NutritionUtil.STATUS_SOLID == unitStatus){
					if(nrv <= 2f){
						nutritionVO.addNutritionLabel(NutritionUtil.WU_DAN_GU_CHUN);
					}else if(2f < nrv && nrv <= 7f){
						nutritionVO.addNutritionLabel(NutritionUtil.DI_DAN_GU_CHUN);
					}
				}else if(NutritionUtil.STATUS_LIQUID == unitStatus){
					/**
					 * 营养成分为液体
					 */
					if(nrv <= 2f){
						nutritionVO.addNutritionLabel(NutritionUtil.WU_DAN_GU_CHUN);
					}else if(2f < nrv && nrv <= 4f){
						nutritionVO.addNutritionLabel(NutritionUtil.DI_DAN_GU_CHUN);
					}
				}
			}
		}else if(this.getProcessor() != null){
			/**
			 * 当前营养成分不是 胆固醇，交给下一级处理
			 */
			nutritionVO = this.getProcessor().doProcess(nutritionVO);
		}
		return nutritionVO;
	}

}
