package com.gettec.fsnip.fsn.model.product;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 产品类型
 * @author Hui Zhang
 */
@Entity
@Table(name="product_category")
public class ProductCategory extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7437379787200229791L;
	@Id 
	@GeneratedValue
	private Long id;
	@Column(name="name", nullable=true, length=200)
	private String name;
	@Column(name="code", nullable=true, length=20) 
	private String code;
	@Column(name="display", nullable=true, length=100) 
	private String display;
	@Column(name="imgUrl", nullable=true, length=255) 
	private String imgUrl;
	
	@Transient
	private ProductCategory fristCategory;//一级产品类别

	@Transient
	private List<ProductCategory> children = new ArrayList<ProductCategory>();
	
	public List<ProductCategory> getChildren() {
		return children;
	}

	public void setChildren(List<ProductCategory> children) {
		this.children = children;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "ProductCategory [id=" + id + ", name=" + name + ", code="
				+ code + ", display=" + display + ", imgUrl=" + imgUrl + "]";
	}

	public ProductCategory() {
		super();
	}

	public ProductCategory(String name) {
		super();
		this.name = name;
	}

	public ProductCategory(String name, String code, String display,
			String imgUrl) {
		super();
		this.name = name;
		this.code = code;
		this.display = display;
		this.imgUrl = imgUrl;
	}
	
    public ProductCategory(Long id, String name, String code, String display) {
        super();
        this.id = id;
        this.name = name;
        this.code = code;
        this.display = display;
    }

    public ProductCategory getFristCategory() {
        return fristCategory;
    }

    public void setFristCategory(ProductCategory fristCategory) {
        this.fristCategory = fristCategory;
    }
}
