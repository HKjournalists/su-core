package com.gettec.fsnip.fsn.nutrition.label;

import java.util.ArrayList;
import java.util.List;

import com.gettec.fsnip.fsn.model.product.ProductNutrition;

/**
 * 营养标签VO，计算营养成分标签时，各责任链间传递的参数
 * @author TangXin
 *
 */
public class NutritionVO {

	private ProductNutrition priNutrition; //商品营养标签的实体
	private String secondCategory; //商品所属的二级分类
	//计算结果标签
	List<String> resultLabel = new ArrayList<String>();
	private ProductNutrition energy; //能量
	private ProductNutrition cholesterol;//胆固醇
	private ProductNutrition fat; //脂肪
	private ProductNutrition saturationFat;//饱和脂肪
	private ProductNutrition transFat; //反式脂肪
	private ProductNutrition dietaryFiber; //膳食纤维
	private float dietaryFiberNvrTotal = 0F;//膳食纤维Nvr累加值,当值为-1时，说明完全匹配 膳食纤维，则其他膳食纤维蒋不再参与计算。
	private int cycle = 0; //商品营养标签循环标志，0：第一次还未循环完，1:已循环完成一次
	private int conformVitamNumber = 0; //NRV 大于等于15 的维生素 数量
	private int conformMineralsNumber = 0; //NRV 大于等于15 的矿物质 数量
	
	public ProductNutrition getPriNutrition() {
		return priNutrition;
	}
	public void setPriNutrition(ProductNutrition priNutrition) {
		this.priNutrition = priNutrition;
	}
	public String getSecondCategory() {
		return secondCategory;
	}
	public void setSecondCategory(String secondCategory) {
		this.secondCategory = secondCategory;
	}
	public List<String> getResultLabel() {
		return resultLabel;
	}
	public void setResultLabel(List<String> resultLabel) {
		this.resultLabel = resultLabel;
	}
	public ProductNutrition getFat() {
		return fat;
	}
	public void setFat(ProductNutrition fat) {
		this.fat = fat;
	}
	public ProductNutrition getSaturationFat() {
		return saturationFat;
	}
	public void setSaturationFat(ProductNutrition saturationFat) {
		this.saturationFat = saturationFat;
	}
	public ProductNutrition getTransFat() {
		return transFat;
	}
	public void setTransFat(ProductNutrition transFat) {
		this.transFat = transFat;
	}
	public float getDietaryFiberNvrTotal() {
		return dietaryFiberNvrTotal;
	}
	public void setDietaryFiberNvrTotal(float dietaryFiberNvrTotal) {
		this.dietaryFiberNvrTotal = dietaryFiberNvrTotal;
	}
	public ProductNutrition getEnergy() {
		return energy;
	}
	public void setEnergy(ProductNutrition energy) {
		this.energy = energy;
	}
	public ProductNutrition getCholesterol() {
		return cholesterol;
	}
	public void setCholesterol(ProductNutrition cholesterol) {
		this.cholesterol = cholesterol;
	}
	public int getCycle() {
		return cycle;
	}
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	public ProductNutrition getDietaryFiber() {
		return dietaryFiber;
	}
	public void setDietaryFiber(ProductNutrition dietaryFiber) {
		this.dietaryFiber = dietaryFiber;
	}
	public int getConformVitamNumber() {
		return conformVitamNumber;
	}
	public void setConformVitamNumber(int conformVitamNumber) {
		this.conformVitamNumber = conformVitamNumber;
	}
	public int getConformMineralsNumber() {
		return conformMineralsNumber;
	}
	public void setConformMineralsNumber(int conformMineralsNumber) {
		this.conformMineralsNumber = conformMineralsNumber;
	}
	
	/**
	 * 处理暂存 营养成分 的方法，（在整个营养成分List遍历完后调用）
	 */
	public void doProducess(){
		this.cycle = 1;
		if(this.energy != null) {
			this.priNutrition = this.energy;
			new EnergyProcessor().doProcess(this);
		}
		if(this.fat != null) {
			this.priNutrition = this.fat;
			new FatProcessor().doProcess(this);
		}
		if(this.dietaryFiber != null) {
			this.priNutrition = this.dietaryFiber;
			new DietaryFiberProcessor().doProcess(this);
		}
		if(this.conformVitamNumber >= 3) {
			this.addNutritionLabel("含多种维生素");
		}
		if(this.conformMineralsNumber >= 3) {
			this.addNutritionLabel("含多种矿物质");
		}
		if(this.cholesterol != null) {
			this.priNutrition = this.cholesterol;
			new CholesterolProcessor().doProcess(this);
		}
	}
	
	/**
	 * 添加标签
	 * @param label 根据营养成分换算的标签值
	 * @author TangXin
	 */
	public void addNutritionLabel(String label) {
		if(label == null || "".equals(label)) {
			return;
		}
		if(resultLabel == null) {
			resultLabel = new ArrayList<String>();
		} else if(!resultLabel.contains(label)) {
			resultLabel.add(label);
		}
	}
	
	/**
	 * 获取标签
	 * @author ChenXiaoLin<br>
     * 最后更新者：TangXin
	 */
	public String labelToString(){
		String nutriStr = "";
		if (resultLabel != null && resultLabel.size() > 0) {
			for (int i = 0; i < resultLabel.size(); i++) {
				nutriStr += resultLabel.get(i) != null ? resultLabel.get(i).toString() + ";" : "";
			}
		}
		return nutriStr;
	}
}
