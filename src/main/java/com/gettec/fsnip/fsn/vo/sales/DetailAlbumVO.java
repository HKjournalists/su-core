package com.gettec.fsnip.fsn.vo.sales;

/**
 * 详细相册
 * @author tangxin 2015-05-05
 *
 */
public class DetailAlbumVO {

	private Long id; //id
	private String imgName; //图片名字
	private String thumbnailUrl; //缩略图地址
	private String imgUrl; //图片原图
	private Long productId; //产品标识
	private PhotoFieldVO field; //图片对应的结构话字段
	private String describ; //图片描述
	
	private ReportInfo reportInfo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public PhotoFieldVO getField() {
		return field;
	}
	public void setField(PhotoFieldVO field) {
		this.field = field;
	}
	public String getDescrib() {
		return describ;
	}
	public void setDescrib(String describ) {
		this.describ = describ;
	}
	
	public ReportInfo getReportInfo() {
		return reportInfo;
	}
	public void setReportInfo(ReportInfo reportInfo) {
		this.reportInfo = reportInfo;
	}
	@Override
	public String toString() {
		return "DetailAlbumVO [id=" + id + ", imgName=" + imgName + ", thumbnailUrl=" + thumbnailUrl + ", imgUrl="
				+ imgUrl + ", productId=" + productId + ", field=" + field + ", describ=" + describ + ", reportInfo="
				+ reportInfo + "]";
	}
	
}
