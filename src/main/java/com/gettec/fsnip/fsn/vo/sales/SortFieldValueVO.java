package com.gettec.fsnip.fsn.vo.sales;

/**
 * 三证、荣誉证书、产品相册 排序是返回页面数据 vo
 * @author tangxin 2015-05-08
 */
public class SortFieldValueVO {

	private Long id; //id唯一标识
	private String name; //名称
	
	/**
	 * sortFieldId 排序信息所属的类型
	 * 1  排序的信息为组织机构信息
	 * 2 排序的信息为营业执照证
	 * 3 排序的信息为税务登记证
	 * 4 排序的信息为生产许可证
	 * 5 排序信息为企业认证信息（企业普通认证和荣誉认证） 
	 */
	private Long sortFieldId;
	private Long sortFieldParentId;//排序字段的父及id
	private Long objectId;//排序对象的id
	private int sort; //顺序
	private Long sortId;//排序id
	private String url;
	private String no; //营业执照号、组织机构代码、qs号
	private String format;
	private String desc;
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
	public Long getSortFieldId() {
		return sortFieldId;
	}
	public void setSortFieldId(Long sortFieldId) {
		this.sortFieldId = sortFieldId;
	}
	public Long getSortFieldParentId() {
		return sortFieldParentId;
	}
	public void setSortFieldParentId(Long sortFieldParentId) {
		this.sortFieldParentId = sortFieldParentId;
	}
	public Long getObjectId() {
		return objectId;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getSortId() {
		return sortId;
	}
	public void setSortId(Long sortId) {
		this.sortId = sortId;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public SortFieldValueVO(){}
	
	public SortFieldValueVO(Long id, String name, Long sortFieldId,
			Long sortFieldParentId, Long objectId, int sort, Long sortId,
			String url, String no,String format, String desc) {
		super();
		this.id = id;
		this.name = name;
		this.sortFieldId = sortFieldId;
		this.sortFieldParentId = sortFieldParentId;
		this.objectId = objectId;
		this.sort = sort;
		this.sortId = sortId;
		this.url = url;
		this.no = no;
		this.format = format;
		this.desc = desc;
	}
	
}
