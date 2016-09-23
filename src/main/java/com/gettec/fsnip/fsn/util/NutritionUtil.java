package com.gettec.fsnip.fsn.util;
import java.util.ArrayList;
import java.util.List;

import com.gettec.fsnip.fsn.nutrition.NutritionProcessor;
import com.gettec.fsnip.fsn.nutrition.label.CarbohydratesProcessor;
import com.gettec.fsnip.fsn.nutrition.label.CholesterolProcessor;
import com.gettec.fsnip.fsn.nutrition.label.DietaryFiberProcessor;
import com.gettec.fsnip.fsn.nutrition.label.EnergyProcessor;
import com.gettec.fsnip.fsn.nutrition.label.FatProcessor;
import com.gettec.fsnip.fsn.nutrition.label.MineralsProcesor;
import com.gettec.fsnip.fsn.nutrition.label.ProteinProcessor;
import com.gettec.fsnip.fsn.nutrition.label.SodiumProcessor;
import com.gettec.fsnip.fsn.nutrition.label.VitaminsProcessor;

/**
 * 处理营养成分的公共类
 * @author TangXin
 */
public class NutritionUtil {

	/**
	 * 营养成分的状态
	 */
	public static final int STATUS_SOLID = 0; //状态为固体
	public static final int STATUS_LIQUID = 1;//状态为液体
	public static final int STATUS_KJ = 2; //千焦
	
	/**
	 * 可显示的标签
	 */
	public static final String WU_NENG_LIANG = "无能量";
	public static final String DI_NENG_LIANG = "低能量";
	public static final String HAN_DAN_BAI = "含蛋白";
	public static final String GAO_DAN_BAI = "高蛋白";
	public static final String WU_ZHI = "无脂";
	public static final String DI_ZHI = "低脂";
	public static final String SHOU = "瘦";
	public static final String TUO_ZHI_NAI = "脱脂.液体奶和酸奶";
	public static final String TUO_ZHI_FEN = "脱脂.奶粉";
	public static final String WU_BAO_HE_ZHI_FANG = "无饱和脂肪";
	public static final String DI_BAO_HE_ZHI_FANG = "低饱和脂肪";
	public static final String BU_HAN_FAN_SHI_ZHI_FANG_SUAN = "不含反式脂肪酸";
	public static final String WU_DAN_GU_CHUN = "无胆固醇";
	public static final String DI_DAN_GU_CHUN = "低胆固醇";
	public static final String WU_TANG = "无糖";
	public static final String DI_TANG = "低糖";
	public static final String WU_RU_TANG = "无乳糖";
	public static final String DI_RU_TANG = "低乳糖";
	public static final String HAN_SHAN_SHI_XIAN_WEI = "含膳食纤维";
	public static final String GAO_SHAN_SHI_XIAN_WEI = "高膳食纤维";
	public static final String WU_NA = "无钠";
	public static final String JI_DI_NA = "极低钠";
	public static final String DI_NA = "低钠";
	
	/**
	 * 营养指数计算状态
	 */
	public static final char NOT_CALCULATED = '0'; //未计算状态
	public static final char SUCCESS = '1'; //计算成功
	public static final char FAILED = '2'; //计算失败
	
	/**
	 * 获取责任链
	 * @return
	 */
	public static NutritionProcessor getNutritionProcessor(){
		/* 蛋白质 */
		ProteinProcessor protein = new ProteinProcessor();
		/* 能量 */
		EnergyProcessor energy = new EnergyProcessor();
		/* 脂肪 */
		FatProcessor fat = new FatProcessor();
		/* 碳水化合物  */
		CarbohydratesProcessor Carbohydrates = new CarbohydratesProcessor();
		/* 胆固醇  */
		CholesterolProcessor chop = new CholesterolProcessor();
		/* 膳食纤维  */
		DietaryFiberProcessor diet = new DietaryFiberProcessor();
		/* 钠 */
		SodiumProcessor sodium = new SodiumProcessor();
		/* 维生素系列 */
		VitaminsProcessor vitam = new VitaminsProcessor();
		/* 矿物质系列 */
		MineralsProcesor minerals = new MineralsProcesor();
		/* 将各个处理器串联起来 */
		protein.setProcessor(energy);
		energy.setProcessor(fat);
		fat.setProcessor(Carbohydrates);
		Carbohydrates.setProcessor(chop);
		chop.setProcessor(diet);
		diet.setProcessor(sodium);
		sodium.setProcessor(vitam);
		vitam.setProcessor(minerals);
		
		/* 返回责任链头 */
		return protein;
	}
	
	/**
	 * 判断营养标签的单位属于 液体、固体还是千焦
	 * @param unit
	 * @author TangXin
	 */
	public static int judgeUnit(String nutriPer){
		
		if(nutriPer == null || "".equals(nutriPer)){
			return -1;
		}
		
		if(nutriPer.endsWith("g") || nutriPer.endsWith("克") || nutriPer.endsWith("G")){
			/**
			 * 营养成分-为固体
			 */
			return STATUS_SOLID;
		} else if(nutriPer.endsWith("ml") || nutriPer.endsWith("毫升") || nutriPer.endsWith("ML")){
			/**
			 * 营养成分-为液体
			 */
			return STATUS_LIQUID;
		} else if(nutriPer.endsWith("KJ") || nutriPer.endsWith("kj") || nutriPer.endsWith("千焦")){
			/**
			 * 营养成分-千焦
			 */
			return STATUS_KJ;
		}
		return -1;
	}
	
	/**
	 * 将字符串转换为float
	 * @param value
	 * @return
	 */
	public static float formatFloat(String value) {
		float result = -1f;
		try{
			if(value == null || "".equals(value)) {
				return result;
			}
			result = Float.parseFloat(value);
			return result;
		}catch(NumberFormatException nfe) {
			return -1f;
		}
	}
	
	/**
     * 营养标签的单位换算
     * 标准计算单位，固体单位g，液体单位ml，kj
     * @author TangXin
	 */
	public static float transferValueByUnit(float value, String unit) {
		if(value == -1 || unit == null){
			return value;
		}
		if("mg".equals(unit) || "毫克".equals(unit) || "毫克（mg)".equals(unit)) {
			value = value/1000f;
		} else if("μg".equals(unit) || "ug".equals(unit) || "微克".equals(unit)) {
			value = value/1000000f;
		} else if("kg".equals(unit) || "KG".equals(unit) || "千克".equals(unit)) {
			value = value * 1000f;
		} else if("l".equals(unit) || "L".equals(unit) || "升".equals(unit)) {
			value = value * 1000f;
		} else if(judgeUnit(unit) == -1f) {
			value = -1f;
		}
		return value;
	}
	
	/**
	 * 默认矿物质系列
	 */
	private static  List<String> listMinerals = null;
	
	/**
	 * 获取光物质系列
	 * @return
	 */
	public static List<String> getListMinerals() {
		if(listMinerals == null) {
			listMinerals = new ArrayList<String>();
		}
		listMinerals.add("钙");
		listMinerals.add("磷");
		listMinerals.add("钾");
		listMinerals.add("镁");
		listMinerals.add("铁");
		listMinerals.add("锌");
		listMinerals.add("碘");
		listMinerals.add("硒");
		listMinerals.add("铜");
		listMinerals.add("氟");
		listMinerals.add("锰");
		
		return listMinerals;
	}
	
	/**
	 * 默认维生素系列
	 */
	private static List<String> listVitams = null;
	
	/**
	 * 获取维生素系列
	 * @return
	 */
	public static List<String> getListVitamins() {
		if(listVitams == null) {
			listVitams = new ArrayList<String>();
		}
		
		listVitams.add("维生素A");
		listVitams.add("维生素D");
		listVitams.add("维生素E");
		listVitams.add("维生素K");
		listVitams.add("维生素B1");
		listVitams.add("维生素B2");
		listVitams.add("维生素B6");
		listVitams.add("维生素B12");
		listVitams.add("维生素C");
		listVitams.add("烟酸");
		listVitams.add("叶酸");
		listVitams.add("泛酸");
		listVitams.add("生物素");
		
		return listVitams;
	}
	
	/**
	 * 营养标签模糊查询值，大众门户搜索接口根据营养标签模糊查询产品。
	 * @author tangxin 2015/04/15
	 */
	public static String getNutritionLabel(String labelName) {
		if(labelName == null || "".equals(labelName)) {
			return labelName;
		}
		if("蛋白质".equals(labelName)) {
			return "蛋白";
		} else if("脂肪".equals(labelName)) {
			return "脂";
		} else if("碳水化合物（糖）".equals(labelName)) {
			return "糖";
		} else if(labelName.contains("维生素")) {
			return "维生素";
		} 
		return labelName;
	}
}
