package com.gettec.fsnip.fsn.nutrition.label;

import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.nutrition.NutritionProcessor;
import com.gettec.fsnip.fsn.util.NutritionUtil;

/**
 * 营养成分-膳食纤维
 * @author TangXin
 *
 */
public class DietaryFiberProcessor extends NutritionProcessor{

	@Override
	public NutritionVO doProcess(NutritionVO nutritionVO) {
		if(nutritionVO == null || nutritionVO.getPriNutrition() == null){
			return nutritionVO;
		}
		/**
		 * 膳食纤维 实体
		 */
		ProductNutrition dietartFiber = nutritionVO.getPriNutrition();
		String name = dietartFiber.getName();
		name = (name == null ? "" : name);
		int unitStatus = NutritionUtil.judgeUnit(dietartFiber.getPer());
		float nrv = NutritionUtil.formatFloat(dietartFiber.getNrv());
		/**
		 * 如果 营养成分的 单位 和  NRV 解析失败，直接返回
		 */
		if(unitStatus == -1 || nrv == -1f) {
			return nutritionVO;
		}
		/**
		 * 如果是模糊匹配膳食纤维，待整个营养成分列表循环完后，根据累加值计算 膳食纤维 的标签
		 */
		if(name.contains("膳食纤维")){
			if(nutritionVO.getCycle() == 1 && nutritionVO.getDietaryFiberNvrTotal() != -1f 
					&& !"膳食纤维".equals(name)){
				nrv = nutritionVO.getDietaryFiberNvrTotal();
				nutritionVO.addNutritionLabel(getNutritionLabel(unitStatus,nrv));
			} else if(nutritionVO.getCycle() != 1){
				/**
				 * 完全匹配，直接使用NRV值计算
				 */
				if("膳食纤维".equals(name)) {
					/* 当dietaryFiberNvrTotal的值为-1时，说明完全匹配 膳食纤维 ，其他膳食纤维将不参与计算 */
					nutritionVO.setDietaryFiberNvrTotal(-1f);
					nutritionVO.setDietaryFiber(dietartFiber);
					nutritionVO.addNutritionLabel(getNutritionLabel(unitStatus,nrv));
				} else if(name.contains("膳食纤维") && nutritionVO.getDietaryFiberNvrTotal() != -1f){
					/**
					 * 模糊匹配，累加其NRV值后计算
					 */
					nutritionVO.setDietaryFiber(dietartFiber);
					float dfNvrTotal = nutritionVO.getDietaryFiberNvrTotal();
					dfNvrTotal = dfNvrTotal + nrv;
					/* 暂存累加的值 */
					nutritionVO.setDietaryFiberNvrTotal(dfNvrTotal);
				}
			}
		} else if(this.getProcessor() != null) {
			/**
			 * 当前营养成分不是 膳食纤维，交给下一级处理
			 */
			nutritionVO = this.getProcessor().doProcess(nutritionVO);
		}
		return nutritionVO;
	}

	/**
	 * 膳食纤维处理的业务逻辑
	 * @param unit
	 * @param nrv
	 * @return
	 */
	private String getNutritionLabel(int unitStatus, float nrv){
		String result = null;
		if(NutritionUtil.STATUS_SOLID == unitStatus){
			if(12f <= nrv && nrv < 24f){
				result = NutritionUtil.HAN_SHAN_SHI_XIAN_WEI;
			} else if(nrv >= 24f){
				result = NutritionUtil.GAO_SHAN_SHI_XIAN_WEI;
			}
		}else if(NutritionUtil.STATUS_LIQUID ==unitStatus || NutritionUtil.STATUS_KJ == unitStatus){
			if(6f <= nrv && nrv < 12f){
				result = NutritionUtil.HAN_SHAN_SHI_XIAN_WEI;
			} else if(nrv >= 12f){
				result = NutritionUtil.GAO_SHAN_SHI_XIAN_WEI;
			}
		}
		return result;
	}
}
