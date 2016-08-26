package com.gettec.fsnip.fsn.vo.business.report;

import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.market.MkTempBusUnit;
import com.gettec.fsnip.fsn.vo.business.QsNoAndFormatVo;

/**
 * 用于封装有关报告中的生产企业字段信息
 * @author ZhangHui 2015/6/4
 */
public class BusinessUnitOfReportVO {

	/**
	 * 企业 id
	 */
	private Long id;
	
	/**
	 * 企业名称
	 */
	private String name;
	
	/**
	 * 企业营业执照号
	 */
	private String licenseno;

	/**
	 * 企业地址
	 */
	private String address;
	
	/**
	 * 产品条形码
	 */
	private String barcode;
	
	/**
	 * 生产许可证
	 */
	private QsNoAndFormatVo qs_vo;
	
	/**
	 * 流通企业在报告录入界面，能否修改qs号
	 *  		true  可以修改
	 *  		false 不可以修改
	 */
	private boolean can_edit_qs = true;
	
	/**
	 * 流通企业在报告录入界面，能否修改生产企业信息
	 * 			true  可以修改
	 * 			false 不可以修改
	 */
	private boolean can_edit_bus = true;
	
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
	
	public String getLicenseno() {
		return licenseno;
	}

	public void setLicenseno(String licenseno) {
		this.licenseno = licenseno;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public QsNoAndFormatVo getQs_vo() {
		return qs_vo;
	}

	public void setQs_vo(QsNoAndFormatVo qs_vo) {
		this.qs_vo = qs_vo;
	}

	public boolean isCan_edit_qs() {
		return can_edit_qs;
	}

	public void setCan_edit_qs(boolean can_edit_qs) {
		this.can_edit_qs = can_edit_qs;
	}

	public boolean isCan_edit_bus() {
		return can_edit_bus;
	}

	public void setCan_edit_bus(boolean can_edit_bus) {
		this.can_edit_bus = can_edit_bus;
	}

	public BusinessUnitOfReportVO() {
		super();
	}

	public BusinessUnitOfReportVO(Long id, String name, String licenseno, String address, Long orgnization) {
		super();
		this.id = id;
		this.name = name;
		this.licenseno = licenseno;
		this.address = address;
		
		if(orgnization!=null && !orgnization.equals(0L)){
			this.can_edit_bus = false;
		}
	}
	
	public BusinessUnitOfReportVO(Long id, String name, String licenseno, String address, 
			int bind, int effect, Long orgnization) {
		super();
		this.id = id;
		this.name = name;
		this.licenseno = licenseno;
		this.address = address;
		
		if(bind==1 && effect==1){
			this.can_edit_qs = false;
		}
		
		if(orgnization!=null && !orgnization.equals(0L)){
			this.can_edit_bus = false;
		}
	}
	
	public BusinessUnitOfReportVO(Long id, String name, String licenseno, String address, 
			int bind, int effect, Long orgnization, String qsno, int qsformat_id) {
		super();
		this.id = id;
		this.name = name;
		this.licenseno = licenseno;
		this.address = address;
		
		if(bind==1 && effect==1){
			this.can_edit_qs = false;
		}
		
		if(orgnization!=null && !orgnization.equals(0L)){
			this.can_edit_bus = false;
		}
		
		this.qs_vo = new QsNoAndFormatVo(qsno, qsformat_id); 
	}

	public BusinessUnitOfReportVO(MkTempBusUnit tempBusUnit) {
		super();
		this.name = tempBusUnit.getName();
		this.address = tempBusUnit.getAddress();
		this.licenseno = tempBusUnit.getLicenseNo();
		
		this.qs_vo = new QsNoAndFormatVo(tempBusUnit.getQsNo(), tempBusUnit.getQsnoFormatId());
	}

	public BusinessUnitOfReportVO(BusinessUnit producer) {
		if(producer == null){
			return;
		}
		
		this.id = producer.getId();
		this.name = producer.getName();
		
		if(producer.getLicense() != null){
			this.licenseno = producer.getLicense().getLicenseNo();
		}
		
		this.address = producer.getAddress();
		
		if(producer.getOrganization()!=null && !producer.getOrganization().equals(0L)){
			this.can_edit_bus = false;
		}
	}
}