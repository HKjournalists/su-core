package com.gettec.fsnip.fsn.model.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 国家标准营养摄入量信息表
 * @author Administrator
 */
@Entity(name="nutri_std")
public class Nutrition extends Model {
	private static final long serialVersionUID = 3083982734808924919L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name", length=30)
	private String name;
	
	@Column(name="daily_intake", length=50)
	private String dailyIntake; // 日摄入量
	
	@Column(name="unit", length=255)
	private String unit;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDailyIntake() {
		return dailyIntake;
	}

	public void setDailyIntake(String dailyIntake) {
		this.dailyIntake = dailyIntake;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Nutrition(){}
	
	public Nutrition(Long id, String name, String dailyIntake, String unit){
		this.id = id;
		this.name = name;
		this.dailyIntake = dailyIntake;
		this.unit = unit;
	}
}
