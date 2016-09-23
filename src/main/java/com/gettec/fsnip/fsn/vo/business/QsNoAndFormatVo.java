package com.gettec.fsnip.fsn.vo.business;



/**
 * 用于在流通企业报告录入时传递qs号和qs号的填入规则
 * @author HuangYog
 */
public class QsNoAndFormatVo {
    private Long qsid;
    private String qsNo;
    private LicenceFormatVO licenceFormat;
    
	public Long getQsid() {
		return qsid;
	}

	public void setQsid(Long qsid) {
		this.qsid = qsid;
	}

	public String getQsNo() {
        return qsNo;
    }

    public LicenceFormatVO getLicenceFormat() {
        return licenceFormat;
    }

    public void setLicenceFormat(LicenceFormatVO licenceFormat) {
        this.licenceFormat = licenceFormat;
    }

    public void setQsNo(String qsNo) {
        this.qsNo = qsNo;
    }

    public QsNoAndFormatVo(){
    	super();
    }
    
	public QsNoAndFormatVo(Long qsid) {
		super();
		this.qsid = qsid;
	}
    
	public QsNoAndFormatVo(String qsNo) {
		super();
		this.qsNo = qsNo;
	}
	
    public QsNoAndFormatVo(String qsNo, Integer qsformat_id) {
		super();
		this.qsNo = qsNo;
		
		if(qsformat_id == null){
			qsformat_id = 1;
		}
		this.licenceFormat = new LicenceFormatVO(qsformat_id);
	}
}
