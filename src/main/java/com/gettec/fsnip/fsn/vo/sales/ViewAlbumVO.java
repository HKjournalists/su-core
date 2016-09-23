package com.gettec.fsnip.fsn.vo.sales;

public class ViewAlbumVO {

	private String coverPhoto; //封面照片
	private long PhotoNo; //照片总数
	private String albumName; //相册名字
	private String AlbumID; //相册的唯一标识
	public String getCoverPhoto() {
		return coverPhoto;
	}
	public void setCoverPhoto(String coverPhoto) {
		this.coverPhoto = coverPhoto;
	}
	public long getPhotoNo() {
		return PhotoNo;
	}
	public void setPhotoNo(long photoNo) {
		PhotoNo = photoNo;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public String getAlbumID() {
		return AlbumID;
	}
	public void setAlbumID(String albumID) {
		AlbumID = albumID;
	}
	
}
