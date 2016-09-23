package com.gettec.fsnip.fsn.vo.product;

/**
 * 产品qs号VO<br>
 * 用于: 1 qs权限管理界面，qs信息展示
 *      2 企业基本信息界面，qs信息展示
 * @author ZhangHui 2015/5/15
 */
public class ProductQSVO {
	/**
	 * 企业-qs 关系表 businessunit_to_prolicinfo 中的主键id
	 */
	private Long id;
	
	/**
	 * 证件id
	 */
	private Long qsId;
	
	/**
	 * 证件编号
	 */
	private String qsno;
	
	/**
	 * 产品名称
	 */
	private String productName;
	
	/**
	 * 是否允许他人使用
	 * true  代表可以使用
	 * false 代表不可以使用
	 */
	private boolean can_use;
	
	/**
	 * 是否允许他人编辑
	 * true  代表可以编辑
	 * false 代表不可以编辑
	 */
	private boolean can_eidt;
	
	/**
	 * 当前企业是否为qs号的主人
	 * 		false 代表 当前企业不是qs号的主人
	 * 		true  代表 当前企业是qs号的主人
	 */
	private boolean local;
	
	/**
	 * 企业id
	 */
	private Long businessId;
	
	/**
	 * 企业名称
	 */
	private String businessName;
	
	/**
	 * 企业组织机构id
	 */
	private Long organization;
	
	/**
	 * 当前qs号有无被认领
	 */
	private boolean claimed;
	
	/**
	 * qs类型id
	 */
	private int qsnoFormatId;
	
	/**
	 * 当前企业对qs号 未过期认领申请信息
	 */
	ProductionLicenseApplicantClaimHandleVO applicant_vo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public boolean isCan_use() {
		return can_use;
	}

	public void setCan_use(boolean can_use) {
		this.can_use = can_use;
	}

	public boolean isCan_eidt() {
		return can_eidt;
	}

	public void setCan_eidt(boolean can_eidt) {
		this.can_eidt = can_eidt;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public boolean isClaimed() {
		return claimed;
	}

	public void setClaimed(boolean claimed) {
		this.claimed = claimed;
	}

	public int getQsnoFormatId() {
		return qsnoFormatId;
	}

	public void setQsnoFormatId(int qsnoFormatId) {
		this.qsnoFormatId = qsnoFormatId;
	}

	public ProductionLicenseApplicantClaimHandleVO getApplicant_vo() {
		return applicant_vo;
	}

	public void setApplicant_vo(ProductionLicenseApplicantClaimHandleVO applicant_vo) {
		this.applicant_vo = applicant_vo;
	}

	public ProductQSVO() {
		super();
	}

	/**
	 * 功能描述：此方法用于qs号的主人查找该qs号能否被别人使用/编辑<br>
	 * @param can_use 
	 *           1 代表 除了我自己之外，其他企业不可以使用
	 *           4 只有指定企业可以使用
	 * @param can_eidt
	 * 			 1 代表 除了我自己之外，其他企业不可以编辑
	 * 			 4 只有指定企业可以编辑
	 * @author ZhangHui 2015/5/20
	 */
	public ProductQSVO(Long id, Long qsId, String qsno, String productName, int can_use, int can_eidt, String businessName) {
		super();
		this.id = id;
		this.qsId = qsId;
		this.qsno = qsno;
		this.productName = productName;
		this.can_use = (can_use==1?false:true);
		this.can_eidt = (can_eidt==1?false:true);
		this.businessName = businessName;
	}

	/**
	 * 功能描述：此方法用于查找该qs号被其他企业（不是主人）能否使用/编辑<br>
	 * @param can_use 
	 *           1 代表当前企业可以使用
	 *           2 代表当前企业不可以使用
	 * @param can_eidt
	 * 			 1 代表当前企业可以编辑
	 *           2 代表当前企业不可以编辑
	 * @author ZhangHui 2015/5/20
	 */
	public ProductQSVO(Long id, int can_use, int can_eidt, String businessName, Long businessId) {
		super();
		this.id = id;
		this.can_use = (can_use==2?false:true);
		this.can_eidt = (can_eidt==2?false:true);
		this.businessName = businessName;
		this.businessId = businessId;
	}
	
	/**
	 * 功能描述：此方法用于企业基本信息页面，查找所有qs号<br>
	 * @param can_use
	 *           1 代表 除了我自己之外，其他企业不可以使用
	 *           2 代表 我自己不可以使用
	 *           4 代表 只有指定企业可以使用
	 * @param can_eidt
	 * 			 1 代表 除了我自己之外，其他企业不可以编辑
	 * 			 2 代表 我自己不可以编辑
	 * 			 4 代表 只有指定企业可以编辑
	 * @param local
	 *			 0 代表 当前企业不是qs号的主人
	 *   		 1 代表 当前企业是qs号的主人
	 * @author ZhangHui 2015/5/20
	 */
	public ProductQSVO(Long qsId, String qsno, String businessName, String productName, int can_use, int can_eidt, int local, int qsnoFormatId) {
		super();
		this.qsId = qsId;
		this.qsno = qsno;
		this.businessName = businessName;
		this.productName = productName;
		if(local == 0){
			this.local = false;
			this.can_use = ((can_use==2)?false:true);
			this.can_eidt = (can_eidt==2?false:true);
		}else if(local == 1){
			this.local = true;
			this.can_use = true;
			this.can_eidt = true;
		}
		this.qsnoFormatId = qsnoFormatId;
	}

	/**
	 * 
	 * @author ZhangHui 2015/5/25
	 */
	public ProductQSVO(int local, int can_use, int can_eidt) {
		if(local == 0){
			this.local = false;
			this.can_use = ((can_use==2)?false:true);
			this.can_eidt = (can_eidt==2?false:true);
		}else if(local == 1){
			this.local = true;
			this.can_use = true;
			this.can_eidt = true;
		}
	}
}
