package com.gettec.fsnip.fsn.vo.home;

import java.util.Date;

/**
 * 用来存放新首页中初始化需要展示的新闻信息
 * @author HuangYog
 */
public class NewsInformationVO {

    private Long id;
    private String title;//标题
    private String outline;//概要
    private String content;//新闻内容
    private String imageUrls; //新闻涉及的图片
    private String date; //新闻涉及的图片

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getOutline() {
        return outline;
    }
    public void setOutline(String outline) {
        this.outline = outline;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getImageUrls() {
        return imageUrls;
    }
    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    
    
}
