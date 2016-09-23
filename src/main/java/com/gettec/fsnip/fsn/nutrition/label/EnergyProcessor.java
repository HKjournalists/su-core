package com.gettec.fsnip.fsn.nutrition.label;

import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.nutrition.NutritionProcessor;
import com.gettec.fsnip.fsn.util.NutritionUtil;

/**
 * 营养成分-能量
 * @author TangXin
 *
 */
public class EnergyProcessor extends NutritionProcessor{

	@Override
	public NutritionVO doProcess(NutritionVO nutritionVO) {
		if(nutritionVO == null || nutritionVO.getPriNutrition() == null){
			return nutritionVO;
		}
		/**
		 * 能量 实体
		 */
		ProductNutrition energy = nutritionVO.getPriNutrition();
		String name = energy.getName();
		name = (name == null ? "" : name);
		if("能量".equals(name)){
			/**
			 * 暂存能量，计算其他营养成分依赖该实体
			 */
			nutritionVO.setEnergy(energy);
			/**
			 * 能量 的计算依赖 脂肪
			 */
			if(nutritionVO.getCycle() == 0 || nutritionVO.getFat() == null){
				return nutritionVO;
			}
			int unitStatus = NutritionUtil.judgeUnit(energy.getPer());
			float nrv = NutritionUtil.formatFloat(energy.getNrv());
			float fatVal = NutritionUtil.formatFloat(nutritionVO.getFat().getValue());
			/* 单位换算 */
			fatVal = NutritionUtil.transferValueByUnit(fatVal, energy.getUnit());
			float enertyValue = NutritionUtil.formatFloat(energy.getValue());
			/**
			 * 如果 营养成分的 单位 和  NRV 解析失败，直接返回
			 */
			if(unitStatus == -1 || nrv == -1f || fatVal == -1f || enertyValue == -1f) {
				return nutritionVO;
			}
			/**
			 * 脂肪的含量*37/能量的值要小于等于0.5
			 */
			if((fatVal*37)/enertyValue > 0.5f){
				return nutritionVO;
			}
			/**
			 * 营养成分-为固体
			 */
			if(NutritionUtil.STATUS_SOLID == unitStatus){
				if(0.2f >= nrv){
					nutritionVO.addNutritionLabel(NutritionUtil.WU_NENG_LIANG);
				} else if(0.2f < nrv && nrv <= 2f) {
					nutritionVO.addNutritionLabel(NutritionUtil.DI_NENG_LIANG);
				}
			} else if(NutritionUtil.STATUS_LIQUID == unitStatus){
				/**
				 * 营养成分-为液体
				 */
				if(0.2f >= nrv){
					nutritionVO.addNutritionLabel(NutritionUtil.WU_NENG_LIANG);
				} else if(0.2f < nrv && nrv <= 1f) {
					nutritionVO.addNutritionLabel(NutritionUtil.DI_NENG_LIANG);
				}
			}
		} else if(this.getProcessor() != null) {
			/**
			 * 当前营养成分不是 能量，交给下一级处理
			 */
			nutritionVO = this.getProcessor().doProcess(nutritionVO);
		}
		return nutritionVO;
	}

}
