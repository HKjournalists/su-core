package com.gettec.fsnip.fsn.model.product;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.MkTempProduct;

/**
 * 产品二级分类下(三级分类和执行标准)
 * @author 郝圆彬
 */
@Entity
@Table(name="product_category_info")
public class ProductCategoryInfo extends Model{
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="name", nullable=false, length=255)
	private String name;//若category_flag为0则是执行标准，为1则是三级分类
	
	@Column(name="display", nullable=true, length=100) 
	private String display;//三级分类别名,若若category_flag为0则空
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER,targetEntity=ProductCategory.class)
	@JoinColumn(name="category_id", nullable=false)
	private ProductCategory category;//所属二级分类id
	
	@Column(name="category_flag", nullable=false) 
	private boolean categoryFlag;//为0则是执行标准，为1则是三级分类,默认值为1
	
	@Column(name="addition", nullable=false) 
	private boolean addition;
	
	/**
	 * 是否删除的标记
	 * @author ZhangHui 2015/4/27
	 */
	@Column(name="del", nullable=false) 
	private boolean del;
	
	@Transient
	private String categoryOneCode;//所属一级分类
	
	public String getCategoryOneCode() {
		return categoryOneCode;
	}

	public void setCategoryOneCode(String categoryOneCode) {
		this.categoryOneCode = categoryOneCode;
	}

	public boolean isAddition() {
		return addition;
	}

	public void setAddition(boolean addition) {
		this.addition = addition;
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

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	public boolean getCategoryFlag() {
		return categoryFlag;
	}

	public void setCategoryFlag(boolean categoryFlag) {
		this.categoryFlag = categoryFlag;
	}
	
	public boolean isDel() {
		return del;
	}

	public void setDel(boolean del) {
		this.del = del;
	}

	public ProductCategoryInfo(){
		
	};
	
	public ProductCategoryInfo(Long id,String name,String display,ProductCategory category,boolean categoryFlag,boolean addition){
		this.id=id;
		this.name=name;
		this.display=display;
		this.category=category;
		this.categoryFlag=categoryFlag;
		this.addition=addition;
	}

	public ProductCategoryInfo(MkTempProduct tempProduct) {
		if(tempProduct == null){
			return;
		}
		
		this.name = tempProduct.getCategory();                      // 三级分类名称
		
		ProductCategory pro_cat = new ProductCategory();
		pro_cat.setId(tempProduct.getCategoryparentid());           // 二级id
		pro_cat.setCode(tempProduct.getCategoryparentcode());       // 一级code
		
		this.category = pro_cat;
	};	
}
