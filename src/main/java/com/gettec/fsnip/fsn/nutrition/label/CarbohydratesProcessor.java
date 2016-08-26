package com.gettec.fsnip.fsn.nutrition.label;

import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.nutrition.NutritionProcessor;
import com.gettec.fsnip.fsn.util.NutritionUtil;

/**
 * 营养成分-碳水化合物
 * @author TangXin
 */
public class CarbohydratesProcessor extends NutritionProcessor {

	@Override
	public NutritionVO doProcess(NutritionVO nutritionVO) {
		if(nutritionVO == null || nutritionVO.getPriNutrition() == null){
			return nutritionVO;
		}
		/* 碳水化合物 实体 */
		ProductNutrition carbohydrat = nutritionVO.getPriNutrition();
		String name = carbohydrat.getName();
		name = (name == null ? "" : name);
		if("碳水化合物".equals(name)){
			/**
			 * 实现对碳水化合物处理的业务逻辑
			 */
			int unitStatus = NutritionUtil.judgeUnit(carbohydrat.getPer());
			float nrv = NutritionUtil.formatFloat(carbohydrat.getNrv());
			/**
			 * 如果 营养成分的 单位 和 NRV 解析失败，直接返回
			 */
			if(unitStatus == -1 || nrv == -1f) {
				return nutritionVO;
			}
			/**
			 * 营养成分为固体或者液体
			 */
			if(NutritionUtil.STATUS_SOLID == unitStatus || NutritionUtil.STATUS_LIQUID == unitStatus){
				if(nrv <= 0.2f) {
					nutritionVO.addNutritionLabel(NutritionUtil.WU_TANG);
				}else if(0.2f < nrv && nrv <= 2f){
					nutritionVO.addNutritionLabel(NutritionUtil.DI_TANG);
				}
				/**
				 * 如果商品类别属于 乳及乳制品 （01）,判断是否含乳糖
				 */
				String secondCategory = nutritionVO.getSecondCategory(); //商品的二级分类
				if(secondCategory != null && secondCategory.startsWith("01")){
					if(nrv <= 0.2f) {
						nutritionVO.addNutritionLabel(NutritionUtil.WU_RU_TANG);
					}else if (0.2f < nrv && nrv <=0.7f){
						nutritionVO.addNutritionLabel(NutritionUtil.DI_RU_TANG);
					}
				}
			}
		} else if(this.getProcessor() != null){
			/**
			 * 当前营养成分不是 碳水化合物，交给下一级处理
			 */
			nutritionVO = this.getProcessor().doProcess(nutritionVO);
		}
		return nutritionVO;
	}

}
