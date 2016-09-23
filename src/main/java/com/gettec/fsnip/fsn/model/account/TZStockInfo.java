package com.gettec.fsnip.fsn.model.account;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  Entity<br>库存详细信息表
 * @author: chenxiaolin
 */

@Entity(name = "tz_stock_info")
public class TZStockInfo extends Model{

    private static final long serialVersionUID = -2568266125280477434L;

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "stock_id")
	private Long stockId;	 //主库存ID
	
	@Column(name = "account_id")
	private Long accountId;	 //台账ID
	
	@Column(name = "business_id")
	private Long businessId;	 //当前企业ID
	
	@Column(name = "product_id")
	private Long productId;	 //产品ID
	
	@Column(name = "product_num")
	private Long productNum;  // 商品数量
	
	@Column(name = "product_batch")
	private String productBatch;  // 产品批次

	@Column(name = "product_format")
	private String productFormat;  // 产品规格

	@Column(name = "date")
	private String inDate;  //入库日期
	
	@Column(name = "type")
	private int type;  //类型，0：加库存，1：减库存

	@Column(name = "qs_no")
	private String qsNo; //商品qs号

	@Column(name = "createType")
	private int createType;  //标识数据来源 流水库存:0，台账库存:1,2用户手动修改库存数据

	@Column(name = "intake")
	private int intake;  //0表示其他方式入库，1表示通过商品入库添加的

	public TZStockInfo() {
		
	}

	public TZStockInfo(ReturnProductVO vo) {
		this.productId = vo.getProductId();
		this.productNum = vo.getCount()!=null?vo.getCount():0;
		this.productBatch = vo.getBatch()!=null?vo.getBatch():"";
		this.productFormat = vo.getFormat()!=null?vo.getFormat():"";
		this.inDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//入库日期
		this.type = 1;//加库存
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getProductNum() {
		return productNum;
	}

	public void setProductNum(Long productNum) {
		this.productNum = productNum;
	}

	public String getProductBatch() {
		return productBatch;
	}

	public void setProductBatch(String productBatch) {
		this.productBatch = productBatch;
	}

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCreateType() {
		return createType;
	}

	public void setCreateType(int createType) {
		this.createType = createType;
	}

	public String getProductFormat() {
		return productFormat;
	}

	public void setProductFormat(String productFormat) {
		this.productFormat = productFormat;
	}

	public String getQsNo() {
		return qsNo;
	}

	public void setQsNo(String qsNo) {
		this.qsNo = qsNo;
	}

	public int getIntake() {
		return intake;
	}

	public void setIntake(int intake) {
		this.intake = intake;
	}
}
