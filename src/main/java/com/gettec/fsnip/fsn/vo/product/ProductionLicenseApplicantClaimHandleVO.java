package com.gettec.fsnip.fsn.vo.product;

/**
 * 生产许可证认领申请/处理
 * @author ZhangHui 2015/6/1
 */
public class ProductionLicenseApplicantClaimHandleVO {

	private Long id;
	
	/**
	 * 外键：production_license_info_applicant_claim_backup id
	 */
	private Long qsId_applicant;
	
	/**
	 * 外键：production_license_info id
	 */
	private Long qsId;
	
	/**
	 * 申请许可证编号
	 */
	private String qsno;
	
	/**
	 * 提出认领申请的企业名称
	 */
	private String bussinessName;
	
	/**
	 * 申请人
	 */
	private String applicant;

	/**
	 * 申请时间
	 */
	private String applicant_time;
	
	/**
	 * 处理时间
	 */
	private String handle_time;
	
	/**
	 * 处理结果 
	 * 	1 代表待审核
	 * 	2 代表审核通过
	 *  4 代表未通过审核
	 *  8 代表该申请已经过期（审核通过后，企业主动解除与qs号的认领关系）
	 */
	private int handle_result;
	
	/**
	 * 备注
	 */
	private String note;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQsId_applicant() {
		return qsId_applicant;
	}

	public void setQsId_applicant(Long qsId_applicant) {
		this.qsId_applicant = qsId_applicant;
	}

	public Long getQsId() {
		return qsId;
	}

	public void setQsId(Long qsId) {
		this.qsId = qsId;
	}

	public String getQsno() {
		return qsno;
	}

	public void setQsno(String qsno) {
		this.qsno = qsno;
	}

	public String getBussinessName() {
		return bussinessName;
	}

	public void setBussinessName(String bussinessName) {
		this.bussinessName = bussinessName;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getApplicant_time() {
		return applicant_time;
	}

	public void setApplicant_time(String applicant_time) {
		this.applicant_time = applicant_time;
	}

	public String getHandle_time() {
		return handle_time;
	}

	public void setHandle_time(String handle_time) {
		this.handle_time = handle_time;
	}

	public int getHandle_result() {
		return handle_result;
	}

	public void setHandle_result(int handle_result) {
		this.handle_result = handle_result;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public ProductionLicenseApplicantClaimHandleVO() {
		super();
	}

	public ProductionLicenseApplicantClaimHandleVO(Long id, String applicant, String applicant_time,
				String handle_time, int handle_result, String note) {
		super();
		this.id = id;
		this.applicant = applicant;
		this.applicant_time = applicant_time;
		this.handle_time = handle_time;
		this.handle_result = handle_result;
		this.note = note;
	}
	
	public ProductionLicenseApplicantClaimHandleVO(Long id, Long qsId_applicant, Long qsId, String qsno, String bussinessName,
			String applicant, String applicant_time, int handle_result, String handle_time,String note){
		super();
		this.id = id;
		this.qsId_applicant = qsId_applicant;
		this.qsId = qsId;
		this.qsno = qsno;
		this.bussinessName = bussinessName;
		this.applicant = applicant;
		this.applicant_time = applicant_time;
		this.handle_time = handle_time;
		this.handle_result = handle_result;
		this.note = note;
	}

	public ProductionLicenseApplicantClaimHandleVO(long id, String qsno, String applicant, String applicant_time,
			String handle_time, int handle_result, String note) {
		super();
		this.id = id;
		this.qsno = qsno;
		this.applicant = applicant;
		this.applicant_time = applicant_time;
		this.handle_time = handle_time;
		this.handle_result = handle_result;
		this.note = note;
	}
}
