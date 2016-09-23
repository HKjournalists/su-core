package com.gettec.fsnip.fsn.vo.product;

import com.gettec.fsnip.fsn.model.product.Product;

/**
 * 热销食品的OV
 */
public class HotProductVO {

    private Product product; //产品对象
    private Long count;   //产品对应的报告总数
    
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public Long getCount() {
        return count;
    }
    public void setCount(Long count) {
        this.count = count;
    }
    
}
