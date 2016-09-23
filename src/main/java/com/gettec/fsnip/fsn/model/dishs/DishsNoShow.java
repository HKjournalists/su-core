package com.gettec.fsnip.fsn.model.dishs;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.gettec.fsnip.fsn.model.common.Model;

@Entity(name = "dishs_no_show")
public class DishsNoShow extends Model{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "show_id")
	private Long showId;
	
	@Column(name = "dishs_no_id")
	private Long dishsNoId;     //菜品id

	
	@Column(name = "show_flag") //是否显示:0表示否;1表示是
	private String showFlag;
	
	@Column(name = "sample_flag") //已留样:0否;1是
	private String sampleFlag;
	
	@Column(name = "show_time") //备注信息
	private String showTime;

	
	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public Long getDishsNoId() {
		return dishsNoId;
	}

	public void setDishsNoId(Long dishsNoId) {
		this.dishsNoId = dishsNoId;
	}

	public String getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(String showFlag) {
		this.showFlag = showFlag;
	}

	public String getSampleFlag() {
		return sampleFlag;
	}

	public void setSampleFlag(String sampleFlag) {
		this.sampleFlag = sampleFlag;
	}

	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}

	

}
