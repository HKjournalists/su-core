package com.gettec.fsnip.fsn.web;

import java.io.Serializable;
import java.util.List;

/**
 * Menu bar structure
 * 
 * @author Yun-Long Xi (Cloud): shallowlong@gmail.com
 */
public class UIMenuBar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4287010396883140414L;

	private String menuId;
	private String text = "";
	private String cssClass = "";
	private String url = "#";
	private Boolean encoded = false;
	private String content = "";
	private String imageUrl = "";
	List<UIMenuBar> items;
	private String spriteCssClass = "";
	private UIMenuBar parent;

	/**
	 * Default constructor
	 */
	public UIMenuBar() {
	}

	/**
	 * @param text
	 *            the display name
	 * @param url
	 *            the corresponding url
	 */
	public UIMenuBar(String text, String url) {
		this.text = text;
		this.url = url;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the cssClass
	 */
	public String getCssClass() {
		return cssClass;
	}

	/**
	 * @param cssClass
	 *            the cssClass to set
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the encoded
	 */
	public Boolean getEncoded() {
		return encoded;
	}

	/**
	 * @param encoded
	 *            the encoded to set
	 */
	public void setEncoded(Boolean encoded) {
		this.encoded = encoded;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl
	 *            the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the items
	 */
	public List<UIMenuBar> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<UIMenuBar> items) {
		this.items = items;
	}

	/**
	 * @return the spriteCssClass
	 */
	public String getSpriteCssClass() {
		return spriteCssClass;
	}

	/**
	 * @param spriteCssClass
	 *            the spriteCssClass to set
	 */
	public void setSpriteCssClass(String spriteCssClass) {
		this.spriteCssClass = spriteCssClass;
	}

	public UIMenuBar getParent() {
		return parent;
	}

	public void setParent(UIMenuBar parent) {
		this.parent = parent;
	}
	

}
