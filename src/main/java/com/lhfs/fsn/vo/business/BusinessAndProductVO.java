package com.lhfs.fsn.vo.business;


/**
 * 大众门户企业信息VO
 * @author YongHuang
 */
public class BusinessAndProductVO {
	private Long id;
	private String proIds;  // 产品id集合
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProIds() {
		return proIds;
	}
	public void setProIds(String proIds) {
		this.proIds = proIds;
	}
	@Override
	public String toString() {
		return "BusinessAndProductVO [id=" + id + ", proIds=" + proIds + "]";
	}
}
