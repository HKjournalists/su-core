package com.gettec.fsnip.fsn.nutrition;

import com.gettec.fsnip.fsn.nutrition.label.NutritionVO;

/**
 * 商品营养成分的处理器
 * @author TangXin
 *
 */
public abstract class NutritionProcessor {
	
	NutritionProcessor processor = null;
	
	public NutritionProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(NutritionProcessor processor) {
		this.processor = processor;
	}

	/**
	 * 商品营养成分的处理方法
	 * @param nutritionVO
	 * @return
	 * @author TangXin
	 */
	public abstract NutritionVO doProcess(NutritionVO nutritionVO);
	
}
