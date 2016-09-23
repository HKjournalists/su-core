package com.gettec.fsnip.fsn.vo.sales;

import java.util.ArrayList;
import java.util.List;

/**
 * 相册vo
 * @author tangxin 2015-05-05
 *
 */
public class AlbumVO {
	
	private long total; //图片总数
	private List<DetailAlbumVO> detailAlbums; //详细相册图片
	private String name; //名称

	private String describe; //描述

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<DetailAlbumVO> getDetailAlbums() {
		return detailAlbums;
	}
	public void setDetailAlbums(List<DetailAlbumVO> detailAlbums) {
		this.detailAlbums = detailAlbums;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	/**
	 * 如果是手机APP请求数据时，将相册结构化字段设置为空，减少数据量返回
	 * @author tangxin 2015-05-07
	 */
	public void setFiledToNull(){
		if(this.detailAlbums != null){
			List<ReportInfo> ls = new ArrayList<ReportInfo>();
			for(DetailAlbumVO avo : this.detailAlbums){
				if(avo != null){
					
					PhotoFieldVO pFIle = avo.getField();
					if(pFIle != null)
					{
						ReportInfo report = new ReportInfo();
						report.setAllReportNumber(pFIle.getAllReportNumber());
						report.setCensorReportNumber(pFIle.getCensorReportNumber());
						report.setCjReportNumber(pFIle.getCjReportNumber());
						report.setZjReportNumber(pFIle.getZjReportNumber());
						report.setRiskIndex(pFIle.getRiskIndex());
						avo.setReportInfo(report);
					}
					avo.setField(null);
				}
			}
		}
	}
}
