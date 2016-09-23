package com.lhfs.fsn.vo.product;

/**
 * 大众门户品牌VO
 * @author Administrator
 *
 */
public class BrandVO {
	private Long id;
	private String name;
	
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
	
    public BrandVO() {}
    public BrandVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
	
	
}
