package com.lhfs.fsn.vo.product;

/**
 * 用于portal搜索中产品认证类别的封装
 * @author HuangYog
 */
public class CertificationVO {
    private Long id;
    private String name;
    private String imgUrl; //认证图片
    
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
    public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public CertificationVO(Long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.imgUrl = url;
    }
    
    public CertificationVO() {
    }
    
    
}