package com.lhfs.fsn.vo.product;

import java.util.List;

/**
 * 食安监产品溯源VO:封装溯源链和批次信息
 * @author YongHuang
 */
public class SajBatchAndTraceabilityVO {
    
	 private String batch;//批次
	 private List<SajTraceabilityVO> suyuan;
	 
    public List<SajTraceabilityVO> getSuyuan() {
        return suyuan;
    }
    public void setSuyuan(List<SajTraceabilityVO> suyuan) {
        this.suyuan = suyuan;
    }
    public String getBatch() {
        return batch;
    }
    public void setBatch(String batch) {
        this.batch = batch;
    }

}
