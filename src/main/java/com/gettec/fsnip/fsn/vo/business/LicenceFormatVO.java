package com.gettec.fsnip.fsn.vo.business;

import com.gettec.fsnip.fsn.model.business.LicenceFormat;

/**
 * 用于封装生产许可证的填写规则
 * 
 * @author HuangYog
 * 
 */
public class LicenceFormatVO {

    private int id;

    private String formetName; // 生产许可证的样例

    private String formetType; // 生产许可证的分隔符

    private String formetValue; // 生产许可证的前部分值

    private Integer formetLength; // 生产许可证的后部分长度

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormetName() {
        return formetName;
    }

    public void setFormetName(String formetName) {
        this.formetName = formetName;
    }

    public String getFormetType() {
        return formetType;
    }

    public void setFormetType(String formetType) {
        this.formetType = formetType;
    }

    public String getFormetValue() {
        return formetValue;
    }

    public void setFormetValue(String formetValue) {
        this.formetValue = formetValue;
    }

    public Integer getFormetLength() {
        return formetLength;
    }

    public void setFormetLength(Integer formetLength) {
        this.formetLength = formetLength;
    }
    
    public LicenceFormatVO() {
        super();
    }

    public LicenceFormatVO(int id) {
		super();
		this.id = id;
	}
    
    public LicenceFormatVO(LicenceFormat licenceFormat) {
    	super();
        this.id = licenceFormat.getId();
        this.formetName = licenceFormat.getFormetName();
        this.formetType = licenceFormat.getFormetType();
        this.formetValue = licenceFormat.getFormetValue();
        this.formetLength = licenceFormat.getFormetLength();
    }
}
