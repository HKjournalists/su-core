package com.gettec.fsnip.fsn.model.receive;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 泊银等外部系统的检测项目和结论
 * @author ZhangHui 2015/4/24
 */
@SuppressWarnings("serial")
@Entity(name = "receive_test_property")
public class ReceiveTestProperty extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	/**
	 * 检测项目名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 单位
	 */
	@Column(name = "unit")
	private String unit;

	/**
	 * 技术指标
	 */
	@Column(name = "indicant")
	private String indicant;

	/**
	 * 检测结果
	 */
	@Column(name = "result")
	private String result;

	/**
	 * 单项评价
	 */
	@Column(name = "conclusion")
	private String conclusion;

	/**
	 * 执行标准
	 */
	@Column(name = "standard")
	private String standard;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getIndicant() {
		return indicant;
	}

	public void setIndicant(String indicant) {
		this.indicant = indicant;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public Long getRec_test_result_id() {
		return rec_test_result_id;
	}

	public void setRec_test_result_id(Long rec_test_result_id) {
		this.rec_test_result_id = rec_test_result_id;
	}
}
