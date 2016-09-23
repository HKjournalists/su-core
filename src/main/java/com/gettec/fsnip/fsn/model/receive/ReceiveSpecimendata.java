package com.gettec.fsnip.fsn.model.receive;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 泊银等外部系统的检测数据
 * @author ZhangHui 2015/4/24
 */
@SuppressWarnings("serial")
@Entity(name = "receive_specimendata")
public class ReceiveSpecimendata extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	/**
	 * 检测条码
	 */
	@Column(name = "barcode")
	private String barcode;
	
	/**
	 * 检测物名称
	 */
	@Column(name = "material")
	private String material;
	
	/**
	 * 检测浓度(单位：ppm)
	 */
	@Column(name = "density")
	private int density;
	
	/**
	 * 检测温度(单位：摄氏度)
	 */
	@Column(name = "temperature")
	private int temperature;
	
	/**
	 * 检测湿度(单位：百分比)
	 */
	@Column(name = "humidity")
	private int humidity;
	
	/**
	 * 检测时间
	 */
	@Column(name = "test_date")
	private Date test_date;
	
	/**
	 * 检测人
	 */
	@Column(name = "tester")
	private String tester;
	
	/**
	 * 外键：关联test_result
	 */
	@Column(name = "rec_test_result_id")
	private Long rec_test_result_id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public int getDensity() {
		return density;
	}

	public void setDensity(int density) {
		this.density = density;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public Date getTest_date() {
		return test_date;
	}

	public void setTest_date(Date test_date) {
		this.test_date = test_date;
	}

	public String getTester() {
		return tester;
	}

	public void setTester(String tester) {
		this.tester = tester;
	}

	public Long getRec_test_result_id() {
		return rec_test_result_id;
	}

	public void setRec_test_result_id(Long rec_test_result_id) {
		this.rec_test_result_id = rec_test_result_id;
	}
}