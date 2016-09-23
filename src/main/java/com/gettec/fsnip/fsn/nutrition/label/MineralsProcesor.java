package com.gettec.fsnip.fsn.nutrition.label;

import java.util.List;

import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.nutrition.NutritionProcessor;
import com.gettec.fsnip.fsn.util.NutritionUtil;

/**
 * 营养成分-矿物质
 * @author TangXin
 * date 2015/03/19
 */
public class MineralsProcesor extends NutritionProcessor {

	/**
	 * 矿物质系列
	 */
	private List<String> listMinerals = null;
	
	@Override
	public NutritionVO doProcess(NutritionVO nutritionVO) {
		if(nutritionVO == null || nutritionVO.getPriNutrition() == null || listMinerals == null){
			return nutritionVO;
		}
		/**
		 * 矿物质 实体
		 */
		ProductNutrition minerals = nutritionVO.getPriNutrition();
		String name = minerals.getName();
		name = (name == null ? "" : name);
		/**
		 * 判断当前营养成分是否属于矿物质系列
		 */
		int index = listMinerals.indexOf(name.trim().toUpperCase());
		if(index > -1) {
			float nrv = NutritionUtil.formatFloat(minerals.getNrv());
			int unitStatus = NutritionUtil.judgeUnit(minerals.getPer());
			float nrvCompareVal = getNrvCompareVal(unitStatus);
			/**
			 * 如果 营养成分的 单位 和  NRV 解析失败，直接返回
			 */
			if(unitStatus == -1 || nrv == -1f) {
				return nutritionVO;
			}
			/**
			 * 当前矿物质的 nrv >= nrvCompareVal,商品的矿物质种类加1。（当某个产品所含的矿物质种类超过3种时，需要添加“含多种矿物质标签”）
			 * nrvCompareVal的取值：当nrv为固体时为15，液体时为7.5，kj时为5
			 */
			if(nrvCompareVal != -1 && nrv >= nrvCompareVal) {
				int total = nutritionVO.getConformMineralsNumber();
				nutritionVO.setConformMineralsNumber(total + 1);
			}
			if(NutritionUtil.STATUS_SOLID == unitStatus) {
				/**
				 * 营养成分-固体
				 */
				if(15f <= nrv && nrv < 30f) {
					nutritionVO.addNutritionLabel("含" + listMinerals.get(index));
				} else if(nrv >= 30f) {
					nutritionVO.addNutritionLabel("高" + listMinerals.get(index));
				}
			} else if(NutritionUtil.STATUS_LIQUID == unitStatus){
				/**
				 * 营养成分-液体
				 */
				if(7.5f <= nrv && nrv < 15f) {
					nutritionVO.addNutritionLabel("含" + listMinerals.get(index));
				} else if(nrv >= 15) {
					nutritionVO.addNutritionLabel("高" + listMinerals.get(index));
				}
			} else if(NutritionUtil.STATUS_KJ == unitStatus) {
				/**
				 * 营养成分-千焦
				 */
				if(5f <= nrv && nrv < 10f) {
					nutritionVO.addNutritionLabel("含" + listMinerals.get(index));
				} else if(nrv >= 10) {
					nutritionVO.addNutritionLabel("高" + listMinerals.get(index));
				}
			}
		} else if(getProcessor() != null) {
			/**
			 * 当前营养成分不是 矿物质，交给下一级处理
			 */
			nutritionVO = getProcessor().doProcess(nutritionVO);
		}
		return nutritionVO;
	}

	/**
	 * 判断多种矿物质的条件值
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
	public MineralsProcesor() {
		if(listMinerals == null) {
			listMinerals = NutritionUtil.getListMinerals();
		}
	}
}
