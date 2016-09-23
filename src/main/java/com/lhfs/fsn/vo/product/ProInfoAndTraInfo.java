package com.lhfs.fsn.vo.product;

import java.io.Serializable;
import java.util.List;

import com.lhfs.fsn.vo.TraceabilityVO;

/**
 * 监管溯源VO
 * @author YongHuang
 */
public class ProInfoAndTraInfo implements Serializable{
	private static final long serialVersionUID = 6724624163985454968L;
	
	private List<TraceabilityVO> traceabilityVO;
	private Long pId;
	private String pName;
	private String pBarcode;
	private Long flagTime;
	
	public List<TraceabilityVO> getTraceabilityVO() {
		return traceabilityVO;
	}
	public void setTraceabilityVO(List<TraceabilityVO> traceabilityVO) {
		this.traceabilityVO = traceabilityVO;
	}
	public Long getpId() {
		return pId;
	}
	public void setpId(Long pId) {
		this.pId = pId;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getpBarcode() {
		return pBarcode;
	}
	public void setpBarcode(String pBarcode) {
		this.pBarcode = pBarcode;
	}
	public Long getFlagTime() {
		return flagTime;
	}
	public void setFlagTime(Long flagTime) {
		this.flagTime = flagTime;
	}
	@Override
	public String toString() {
		return "ProInfoAndTraInfo [traceabilityVO=" + traceabilityVO + ", pId="
				+ pId + ", pName=" + pName + ", pBarcode=" + pBarcode
				+ ", flagTime=" + flagTime + "]";
	}
}
