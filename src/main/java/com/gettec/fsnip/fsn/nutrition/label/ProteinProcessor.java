package com.gettec.fsnip.fsn.nutrition.label;

import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.nutrition.NutritionProcessor;
import com.gettec.fsnip.fsn.util.NutritionUtil;

/**
 * 营养成分-蛋白质
 * @author TangXin
 */
public class ProteinProcessor extends NutritionProcessor{

	@Override
	public NutritionVO doProcess(NutritionVO nutritionVO) {
		if(nutritionVO == null || nutritionVO.getPriNutrition() == null) {
			return nutritionVO;
		}
		/**
		 * 蛋白质 实体
		 */
		ProductNutrition proterin = nutritionVO.getPriNutrition();
		String name = proterin.getName();
		name = (name == null ? "" : name);
		if("蛋白质".equals(name)){
			/**
			 * 实现对蛋白质处理的业务逻辑
			 */
			int unitStatus = NutritionUtil.judgeUnit(proterin.getPer());//判断：固态：0、液态：1、千焦：2
			float nrv = NutritionUtil.formatFloat(proterin.getNrv());// 获取NRV值然后转成float型
			/**
			 * 如果 营养成分的 单位 和  NRV 解析失败，直接返回
			 */
			if(unitStatus == -1 || nrv == -1f) {
				return nutritionVO;
			}
			/**
			 * 营养成分-固体
			 */
			if(NutritionUtil.STATUS_SOLID == unitStatus){
				if(10f <= nrv && nrv < 20f){
					nutritionVO.addNutritionLabel(NutritionUtil.HAN_DAN_BAI);
				} else if(nrv >= 20) {
					nutritionVO.addNutritionLabel(NutritionUtil.GAO_DAN_BAI);
				}
			} else if(NutritionUtil.STATUS_LIQUID == unitStatus || NutritionUtil.STATUS_KJ == unitStatus){
				/**
				 * 营养成分-液体 或者 千焦
				 */
				if(5f <= nrv && nrv < 10f){
					nutritionVO.addNutritionLabel(NutritionUtil.HAN_DAN_BAI);
				} else if(nrv >= 10) {
					nutritionVO.addNutritionLabel(NutritionUtil.GAO_DAN_BAI);
				}
			}
		} else if(this.getProcessor() != null){
			/**
			 * 当前营养成分不是 蛋白质，交给下一级处理
			 */
			return this.getProcessor().doProcess(nutritionVO);
		}
		return nutritionVO;
	}
}
