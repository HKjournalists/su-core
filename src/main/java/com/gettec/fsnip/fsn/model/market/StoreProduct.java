package com.gettec.fsnip.fsn.model.market;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.product.Product;

/**
 *  大众门户我的收藏
 * TestResult Entity<br>
 * @author zhaWanNeng
 */
@SuppressWarnings("serial")
@Entity(name = "store_product")
public class StoreProduct extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="product_id")
	private Product productid;  // 关联产品
	
	@Column(name = "productImg")
	private String  productImg;   //产品的第一张图片（如无则返回默认的logo图片）
	
	@Column(name = "userId")
	private Long  userId;   //用户id
	
	@Column(name = "productName")
	private String  productName;   //产品名称
	
	@Column(name = "add_date")
	private Date  addDate;   //产品收藏时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProductid() {
		return productid;
	}

	public void setProductid(Product productid) {
		this.productid = productid;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

//	public TestResult getTestId() {
//		return testId;
//	}
//
//	public void setTestId(TestResult testId) {
//		this.testId = testId;
//	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public String getProductImg() {
		return productImg;
	}

	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	
}
