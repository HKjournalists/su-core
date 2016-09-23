package com.gettec.fsnip.fsn.nutrition.label;

import java.util.List;

import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.nutrition.NutritionProcessor;
import com.gettec.fsnip.fsn.util.NutritionUtil;

/**
 * 营养成分-维生素
 * @author TangXin
 * date 2015/03/19
 */
public class VitaminsProcessor extends NutritionProcessor{

	/**
	 * 维生素系列
	 */
	List<String> listVitam = null;
	
	@Override
	public NutritionVO doProcess(NutritionVO nutritionVO) {
		if(nutritionVO == null || nutritionVO.getPriNutrition() == null || listVitam == null){
			return null;
		}
		ProductNutrition vitamins = nutritionVO.getPriNutrition();
		String name = vitamins.getName();
		name = (name == null ? "" : name);
		/**
		 * 判断当前营养成分是否属于维生素系列
		 */
		int index = listVitam.indexOf(name.trim().toUpperCase());
		if(index > -1) {
			float nrv = NutritionUtil.formatFloat(vitamins.getNrv());
			int unitStatus = NutritionUtil.judgeUnit(vitamins.getPer());
			float nrvCompareVal = getNrvCompareVal(unitStatus);
			/**
			 * 如果 营养成分的 单位 和  NRV 解析失败，直接返回
			 */
			if(unitStatus == -1 || nrv == -1f) {
				return nutritionVO;
			}
			/**
			 * 当前维生素的 nrv >= nrvCompareVal,商品的维生素种类加1。（当某个产品所含的维生素种类超过3种时，需要添加“含多种维生素标签”）
			 * nrvCompareVal的取值：当nrv为固体时为15，液体时为7.5，kj时为5 
			 */
			if(nrvCompareVal != -1 && nrv >= nrvCompareVal) {
				int total = nutritionVO.getConformVitamNumber();
				nutritionVO.setConformVitamNumber(total + 1);
			}
			if(NutritionUtil.STATUS_SOLID == unitStatus) {
				/**
				 * 营养成分-固体
				 */
				if(15f <= nrv && nrv < 30f) {
					nutritionVO.addNutritionLabel("含" + listVitam.get(index));
				} else if(nrv >= 30f) {
					nutritionVO.addNutritionLabel("高" + listVitam.get(index));
				}
			} else if(NutritionUtil.STATUS_LIQUID == unitStatus){
				/**
				 * 营养成分-液体
				 */
				if(7.5f <= nrv && nrv < 15f) {
					nutritionVO.addNutritionLabel("含" + listVitam.get(index));
				} else if(nrv >= 15f) {
					nutritionVO.addNutritionLabel("高" + listVitam.get(index));
				}
			} else if(NutritionUtil.STATUS_KJ == unitStatus) {
				/**
				 * 营养成分-千焦
				 */
				if(5f <= nrv && nrv < 10f) {
					nutritionVO.addNutritionLabel("含" + listVitam.get(index));
				} else if(nrv >= 10f) {
					nutritionVO.addNutritionLabel("高" + listVitam.get(index));
				}
			}
		} else if(getProcessor() != null) {
			/**
			 * 当前营养成分不是 维生素，交给下一级处理
			 */
			nutritionVO = getProcessor().doProcess(nutritionVO);
		}
		return nutritionVO;
	}

	/**
	 * 判断多种维生素的条件值
	 * @author TangXin 
	 */
	private float getNrvCompareVal(int unitStatus) {
		float val = -1f;
		if(NutritionUtil.STATUS_SOLID == unitStatus) {
			val = 15f;
		} else if(NutritionUtil.STATUS_LIQUID == unitStatus) {
			val = 7.5f;
		} else if(NutritionUtil.STATUS_KJ == unitStatus) {
			val = 5f;
		}
		return val;
	}
	
	/**
	 * 构造器
	 */
	public VitaminsProcessor() {
		if(listVitam == null){
			listVitam = NutritionUtil.getListVitamins();
		}
	}
}
