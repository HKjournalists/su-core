package com.gettec.fsnip.fsn.vo.home;

import java.util.List;

public class NewsColumnVO {
    
    private Long id; 
    private String name; // 专栏名称
    private String imageUrl; // 专栏对应的图片地址
    private List<NewsInformationVO> newsLists; // 专栏下的新闻
    
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
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public List<NewsInformationVO> getNewsLists() {
        return newsLists;
    }
    public void setNewsLists(List<NewsInformationVO> newsLists) {
        this.newsLists = newsLists;
    }
    
    
    

}
