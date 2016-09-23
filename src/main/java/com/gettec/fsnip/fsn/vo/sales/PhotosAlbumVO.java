package com.gettec.fsnip.fsn.vo.sales;

import java.util.List;

import com.gettec.fsnip.fsn.model.sales.PhotosAlbums;
import com.gettec.fsnip.fsn.model.sales.SalesResource;

public class PhotosAlbumVO {
	
	private Long id;
	private String guid;
	private String name; //相册名称
	private String describe; //相册描述
	List<SalesResource> resource;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public List<SalesResource> getResource() {
		return resource;
	}
	public void setResource(List<SalesResource> resource) {
		this.resource = resource;
	}
	
	public PhotosAlbums toEntity(PhotosAlbums album){
		if(album == null) {
			album = new PhotosAlbums();
		}
		album.setName(this.name);
		album.setDescription(this.describe);
		return album;
	}
}
