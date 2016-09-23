package com.gettec.fsnip.fsn.nutrition.label;

import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.nutrition.NutritionProcessor;
import com.gettec.fsnip.fsn.util.NutritionUtil;

/**
 * 营养成分-脂肪
 * @author TangXin
 */
public class FatProcessor extends NutritionProcessor {

	@Override
	public NutritionVO doProcess(NutritionVO nutritionVO) {
		if(nutritionVO == null || nutritionVO.getPriNutrition() == null){
			return nutritionVO;
		}
		/**
		 * 脂肪 实体
		 */
		ProductNutrition fat = nutritionVO.getPriNutrition();
		String name = fat.getName();
		name = (name == null ? "" : name);
		if("脂肪".equals(name)){
			/**
			 * 暂存脂肪，计算其他营养成分依赖该实体
			 */
			nutritionVO.setFat(fat);
			float nrv = NutritionUtil.formatFloat(fat.getNrv());
			float value = NutritionUtil.formatFloat(fat.getValue());
			/* 单位换算 */
			String unit = fat.getUnit();
			value = NutritionUtil.transferValueByUnit(value, unit);
			int unitStatus = NutritionUtil.judgeUnit(fat.getPer());
			/**
			 * 如果 营养成分的 单位 和  NRV 解析失败，直接返回
			 */
			if(unitStatus == -1 || nrv == -1f || value == -1f) {
				return nutritionVO;
			}
			/**
			 * 营养成分-为固体
			 */
			if(NutritionUtil.STATUS_SOLID == unitStatus){
				/**
				 * cycle=1：表示营养成分列表已经遍历完成
				 */
				if(nutritionVO.getCycle() == 0){
					if(nrv <= 1f){
						nutritionVO.addNutritionLabel(NutritionUtil.WU_ZHI);
					}else if(1f < nrv && nrv <= 5f) {
						nutritionVO.addNutritionLabel(NutritionUtil.DI_ZHI);
					}
					if("g&克&mg&毫克&G&KG&kg&千克".indexOf(unit) > -1) {
						calculateLabelWithCategory(nutritionVO, value, 10f, 0.5f, 1.5f);
					}
				}else {
					nutritionVO = selfProcess(nutritionVO, value, unitStatus);
				}
			} else if(NutritionUtil.STATUS_LIQUID == unitStatus){
				/**
				 * 营养成分-为液体 或者 千焦
				 */
				if(nutritionVO.getCycle() == 0){
					if(nrv <= 1f){
						nutritionVO.addNutritionLabel(NutritionUtil.WU_ZHI);
					}
					if(1f < nrv && nrv <= 3f) {
						nutritionVO.addNutritionLabel(NutritionUtil.DI_ZHI);
					}
					if("ml&ML&毫升&l&L&升".indexOf(unit) > -1) {
						calculateLabelWithCategory(nutritionVO, value, 10f, 0.5f, 1.5f);
					}
				}else{
					nutritionVO = selfProcess(nutritionVO, value, unitStatus);
				}
			} else if(NutritionUtil.STATUS_KJ == unitStatus) {
				if("kj&KJ&千焦".indexOf(unit) > -1) {
					calculateLabelWithCategory(nutritionVO, value, 42f, 2.1f, 6.3f);
				}
			}
		} else if(fat.getName().contains("饱和脂肪")) {
			/**
			 * 暂存 饱和脂肪 ，其他项依赖
			 */
			nutritionVO.setSaturationFat(fat);
		} else if(fat.getName().contains("反式脂肪")) {
			/**
			 * 暂存 反式脂肪 ，其他项依赖
			 */
			nutritionVO.setTransFat(fat);
		} else if(this.getProcessor() != null){
			/**
			 * 当前营养成分不是 脂肪，交给下一级处理
			 */
			nutritionVO = this.getProcessor().doProcess(nutritionVO);
		}
		return nutritionVO;
	}
	
	/**
	 * 脂肪 标签的计算步骤
	 * @param nutritionVO
	 * @return
	 */
	private NutritionVO selfProcess(NutritionVO nutritionVO, float fatVal, int unitStatus) {
		if(nutritionVO.getTransFat() == null && nutritionVO.getSaturationFat() == null) {
			return nutritionVO;
		}
		/* 反式脂肪 的NVR值 */
		float tfatNrv = NutritionUtil.formatFloat(nutritionVO.getTransFat() != null ? nutritionVO.getTransFat().getNrv() : "0");
		/* 饱和脂肪 的NVR值 */
		float sfatNrv = NutritionUtil.formatFloat(nutritionVO.getSaturationFat() != null ? nutritionVO.getSaturationFat().getNrv() : "0");
		float sumNrv = tfatNrv + sfatNrv;
		if(sumNrv <= 0.1f){
			nutritionVO.addNutritionLabel(NutritionUtil.WU_BAO_HE_ZHI_FANG);
		}
		if(nutritionVO.getEnergy() != null) {
			/* 营养成分中 能量 的值 */
			float energyVal = NutritionUtil.formatFloat(nutritionVO.getEnergy().getValue());
			float result = fatVal*17/energyVal;
			if(result <= 0.1f){
				if(NutritionUtil.STATUS_SOLID ==unitStatus && 0.1f < sumNrv && sumNrv <= 3f){
					nutritionVO.addNutritionLabel(NutritionUtil.DI_BAO_HE_ZHI_FANG);
				} else if(NutritionUtil.STATUS_LIQUID ==unitStatus && 0.1f < sumNrv && sumNrv <= 1.3f){
					nutritionVO.addNutritionLabel(NutritionUtil.DI_BAO_HE_ZHI_FANG);
				}
			}
		}
		if(nutritionVO.getTransFat() != null) {
			/* 包含 反式脂肪 营养成分，根据含量计算标签 */
			if(tfatNrv <= 0.5f){
				nutritionVO.addNutritionLabel(NutritionUtil.BU_HAN_FAN_SHI_ZHI_FANG_SUAN);
			}
		}
		return nutritionVO;
	}
	
	/**
	 * 计算脂肪的 瘦、脱脂 营养指数（依赖产品类别）
	 * @author tangxin
	 */
	private void calculateLabelWithCategory(NutritionVO nutritionVO, float fatVal, float shouCompVal, float tuoZhiNaiCompVal, float tuoZhiFenCompVal) {
		if(fatVal <= shouCompVal && nutritionVO.getSecondCategory().startsWith("08")){
			nutritionVO.addNutritionLabel(NutritionUtil.SHOU);
		}
		if(fatVal <= tuoZhiNaiCompVal && (nutritionVO.getSecondCategory().startsWith("0101") ||
				nutritionVO.getSecondCategory().startsWith("0102"))){
			nutritionVO.addNutritionLabel(NutritionUtil.TUO_ZHI_NAI);
		}
		if(fatVal <= tuoZhiFenCompVal && nutritionVO.getSecondCategory().startsWith("0103")){
			nutritionVO.addNutritionLabel(NutritionUtil.TUO_ZHI_FEN);
		}
	}
}
