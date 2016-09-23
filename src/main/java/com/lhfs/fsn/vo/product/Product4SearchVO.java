package com.lhfs.fsn.vo.product;

import java.util.List;

import com.gettec.fsnip.fsn.model.market.Resource;

/**
 * 用于portal搜索加载时产品信息的封装
 * @author HuangYog
 * 
 */
public class Product4SearchVO {
    private Long id;
    private String name;// 名称
    private String imgUrl;//图片地址
    private String cstm;//适宜人群
    private Float qscoreSelf; //自荐评分
    private Float qscoreCensor; //送检评分
    private Float qscoreSample; //抽检评分
    private List<CertificationVO> productCertification; // portal加载时需要用到
	private Double riskIndex;   //风险指数
	private Boolean riskSucceed ;   //风险指数的计算成功:0：失败、1：成功
	private String testPropertyName;   //风险指数计算相关的检测项目名称的字符串
	private String nutriLabel; //营养指数
	private Long reportNum; //报告数量
	private String businessUnitName;
	private List<Resource> imgList;
	private boolean isImportedProduct;// 是否是进口食品 true:是 false:不是
	
    public Product4SearchVO() {}

    public Product4SearchVO(Long id, String name, String imgUrl, String cstm,
            Float qscoreSelf, Float qscoreCensor, Float qscoreSample,
            List<CertificationVO> productCertification,Double riskIndex,
            Boolean riskSucceed,String  testPropertyName, String nutriLabel,Long reportNum,List<Resource> imgList) {
        super();
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.cstm = cstm;
        this.qscoreSelf = qscoreSelf;
        this.qscoreCensor = qscoreCensor;
        this.qscoreSample = qscoreSample;
        this.riskIndex = riskIndex;
        this.riskSucceed = riskSucceed;
        this.testPropertyName = testPropertyName;
        this.nutriLabel = nutriLabel;
        this.reportNum = reportNum;
        this.setProductCertification(productCertification);
        this.imgList=imgList;
    }

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCstm() {
        return cstm;
    }

    public void setCstm(String cstm) {
        this.cstm = cstm;
    }

    public Float getQscoreSelf() {
        return qscoreSelf;
    }

    public void setQscoreSelf(Float qscoreSelf) {
        this.qscoreSelf = qscoreSelf;
    }

    public Float getQscoreCensor() {
        return qscoreCensor;
    }

    public void setQscoreCensor(Float qscoreCensor) {
        this.qscoreCensor = qscoreCensor;
    }

    public Float getQscoreSample() {
        return qscoreSample;
    }

    public void setQscoreSample(Float qscoreSample) {
        this.qscoreSample = qscoreSample;
    }

    public List<CertificationVO> getProductCertification() {
        return productCertification;
    }

    public void setProductCertification(List<CertificationVO> productCertification) {
        this.productCertification = productCertification;
    }

	public Double getRiskIndex() {
		return riskIndex;
	}

	public void setRiskIndex(Double riskIndex) {
		this.riskIndex = riskIndex;
	}

	public Boolean getRiskSucceed() {
		return riskSucceed;
	}

	public void setRiskSucceed(Boolean riskSucceed) {
		this.riskSucceed = riskSucceed;
	}

	public String getTestPropertyName() {
		return testPropertyName;
	}

	public void setTestPropertyName(String testPropertyName) {
		this.testPropertyName = testPropertyName;
	}

	public String getNutriLabel() {
		return nutriLabel;
	}

	public void setNutriLabel(String nutriLabel) {
		this.nutriLabel = nutriLabel;
	}

	public Long getReportNum() {
		return reportNum;
	}

	public void setReportNum(Long reportNum) {
		this.reportNum = reportNum;
	}

	public List<Resource> getImgList() {
		return imgList;
	}

	public void setImgList(List<Resource> imgList) {
		this.imgList = imgList;
	}

	public boolean isImportedProduct() {
		return isImportedProduct;
	}

	public void setImportedProduct(boolean isImportedProduct) {
		this.isImportedProduct = isImportedProduct;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}
	
}