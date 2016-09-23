package com.gettec.fsnip.fsn.vo.sales;

/**
 * Created by HY on 2015/5/9.
 * desc:
 */
public class ContractResVO {

    private Long cId; // 电子合同id
    private String contractName; //电子合同名称
    private Long resId;//电子合同的资源id
    private String url;

    public ContractResVO(Long cId, String contractName, Long resId) {
        this.cId = cId;
        this.contractName = contractName;
        this.resId = resId;
    }

    public ContractResVO() {

    }

    public Long getCId() {

        return cId;
    }

    public void setCId(Long cId) {
        this.cId = cId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Long getResId() {
        return resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
    
}
