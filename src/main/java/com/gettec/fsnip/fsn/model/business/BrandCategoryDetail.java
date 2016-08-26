package com.gettec.fsnip.fsn.model.business;

import java.io.Serializable;

/**
 * 品牌类型扩展类（用于前台页面品牌树形展示）
 * @author HuiZhang
 */
public class BrandCategoryDetail extends BrandCategory implements Serializable {
	private static final long serialVersionUID = 3418312674298089022L;
	private long childrenNum = 0; // the number of children;
	private long productId;

	public BrandCategoryDetail() {}

	public BrandCategoryDetail(Long id, String name, long childrenNum, long productId) {
		setId(id);
		setName(name);
		setChildrenNum(childrenNum);
		setProductId(productId);
	}
	public long getChildrenNum() {
		return childrenNum;
	}
	public void setChildrenNum(long childrenNum) {
		this.childrenNum = childrenNum;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
}
