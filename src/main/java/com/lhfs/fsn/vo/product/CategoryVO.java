package com.lhfs.fsn.vo.product;

import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;

/**
 * 提供给Portal 用于封装产品的类别
 * @author HuangYog
 *
 */
public class CategoryVO {
    
    private Long id; //
    private String code;//类别编号
    private String name;//类别名称
    private String display;//
    
    
    public CategoryVO() {}
    
    public CategoryVO(Long id, String code, String name, String display) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.display = display;
    }

    /**
     * 构造函数
     * @author tangxin 2015/03/31
     */
    public CategoryVO(ProductCategory pc) {
    	 this.id = pc.getId();
         this.code = pc.getCode();
         this.name = pc.getName();
         this.display = pc.getDisplay();
    }
    
    /**
     * 构造函数
     * @author tangxin 2015/04/22
     */
    public CategoryVO(ProductCategoryInfo pci) {
    	 this.id = pci.getId();
         this.code = "";
         this.name = pci.getName();
         String display =  pci.getDisplay();
         this.display = (display == null ? this.name : display);
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDisplay() {
        return display;
    }
    public void setDisplay(String display) {
        this.display = display;
    }

}