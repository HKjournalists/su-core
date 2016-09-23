package com.gettec.fsnip.fsn.vo.receive;

/**
 * 用于接收泊银等其他外部系统的检测数据
 * @author ZhangHui 2015/4/23
 */
public class SpecimenVO {
	/**
	 * 检测条码
	 */
	private String barcode;
	/**
	 * 检测物名称
	 */
	private String material;
	/**
	 * 检测浓度
	 */
	private String density;
	/**
	 * 检测温度
	 */
	private String temperature;
	/**
	 * 检测湿度
	 */
	private String humidity;
	/**
	 * 检测时间
	 */
	private String test_date;
	/**
	 * 检测人
	 */
	private String tester;
	
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getDensity() {
		return density;
	}
	public void setDensity(String density) {
		this.density = density;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getTest_date() {
		return test_date;
	}
	public void setTest_date(String test_date) {
		this.test_date = test_date;
	}
	public String getTester() {
		return tester;
	}
	public void setTester(String tester) {
		this.tester = tester;
	}
}
