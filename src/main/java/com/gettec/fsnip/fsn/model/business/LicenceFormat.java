package com.gettec.fsnip.fsn.model.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 用于封装生产许可证的填写规则
 * 
 * @author HuangYog
 * 
 */
@Entity(name = "licenceno_format")
public class LicenceFormat extends Model {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    
    @Column(name = "format_name")
    private String formetName; // 生产许可证的样例
    
    @Column(name = "format_type")
    private String formetType; // 生产许可证的分隔符
    
    @Column(name = "format_value")
    private String formetValue; // 生产许可证的前部分值
    
    @Column(name = "format_length")
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

    public LicenceFormat(){}

    public LicenceFormat(int id) {
    	this.id = id;
	}
    
    public LicenceFormat(LicenceFormat format){
    	if(format==null){return;}
    	this.id = format.getId();
    	this.formetName = format.getFormetName();
    	this.formetType = format.getFormetType();
    	this.formetValue = format.getFormetValue();
    	this.formetLength = format.getFormetLength();
    }
}
