package com.gettec.fsnip.fsn.model.account;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;

/**
 * Account Entity<br>
 * @author:chenxiaolin
 */

@Entity(name = "tz_product_trail")
public class TZProductTrail extends Model{
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private Long productId; //产品id

    @Column(name = "bar_code")
    private String barcode;//条形码

    @Column(name = "product_batch")
    private String productBatch; //商品批次
    
    @Column(name = "date")
    private String accountDate;//台账日期
    
    @Column(name = "product_num")
    private Long productNum;//商品数量
    
    @Column(name = "out_business_id")
    private Long outBusId;//发货企业ID

    @Column(name = "in_business_id")
    private Long inBusId;//进货企业ID

	@Column(name = "qs_no")
	private String qsNo; //商品qs号

   @Column(name = "over_date")
    private String overDate;//过期日期

	@Column(name = "type")
	private Integer type;//轨迹类型 0表示进货台账，1位退货台账

	@Column(name = "account_id")
	private Long accountId;//台账id

	@Column(name = "production_date")
	private String productionDate;//生产日期

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getProductBatch() {
		return productBatch;
	}

	public void setProductBatch(String productBatch) {
		this.productBatch = productBatch;
	}

	public String getAccountDate() {
		return accountDate;
	}

	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}

	public Long getProductNum() {
		return productNum;
	}

	public void setProductNum(Long productNum) {
		this.productNum = productNum;
	}

	public Long getOutBusId() {
		return outBusId;
	}

	public void setOutBusId(Long outBusId) {
		this.outBusId = outBusId;
	}

	public Long getInBusId() {
		return inBusId;
	}

	public void setInBusId(Long inBusId) {
		this.inBusId = inBusId;
	}

	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	public String getOverDate() {
		return overDate;
	}

	public void setOverDate(String overDate) {
		this.overDate = overDate;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getQsNo() {
		return qsNo;
	}

	public void setQsNo(String qsNo) {
		this.qsNo = qsNo;
	}

	public TZProductTrail() {
			
	}

	 public TZProductTrail(ReturnProductVO vo) throws ParseException {
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        this.productId = vo.getProductId();
	        this.barcode = vo.getBarcode()!=null?vo.getBarcode():"";
	        this.productBatch = vo.getBatch()!=null?vo.getBatch():"";
	        this.productNum = vo.getReturnCount()!=null?vo.getReturnCount():0;
	        this.productionDate = vo.getProductionDate();
	        this.overDate = vo.getOverDate();
	        this.accountDate = sdf.format(new Date());
	        this.qsNo = vo.getQsNumber()!=null?vo.getQsNumber():"";
	    }
}
