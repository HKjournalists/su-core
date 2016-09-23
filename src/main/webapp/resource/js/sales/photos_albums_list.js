$(document).ready(function() {
	var fsn = window.fsn = window.fsn || {};
	var album = window.album = window.album || {};
	var httpPrefix = fsn.getHttpPrefix();
	var SANZHENG_ALBUM_ID = "1"; //三证相册
	var HONOR_ALBUM_ID = "2"; //荣誉证书
	var CERTIFICATION_ALBUM_ID = "3"; //其他认证相册
	var PRODUCT_ALBUM_ID = "4"; //产品相册
	var BUSPHOTO_ALBUM_ID = "5"; //企业掠影
	
	/**
	 * 页面初始化方法
	 * @author tangxin 2015-05-04
	 */
	function initComponent(){
		/* 获取企业相册并在页面展示 */
		getListAlbums();
	}
	
	/**
	 * 三证相册展示
	 * @author tangxin 2015-05-05
	 */
	function viewSanZhengAlbums(album){
		if(album == null) {
			return;
		}
		$("#SANZHENG_IMG_ID").attr("src",album.coverPhoto);
		$("#SANZHENG_NUM_ID").html(album.photoNo);
		$("#SANZHENG_NAME_ID").html(album.albumName);
	}
	
	/**
	 * 荣誉证书相册展示
	 * @author tangxin 2015-05-05
	 */
	function viewHonorAlbums(album){
		if(album == null) {
			return;
		}
		$("#HONOR_IMG_ID").attr("src",album.coverPhoto);
		$("#HONOR_NUM_ID").html(album.photoNo);
		$("#HONOR_NAME_ID").html(album.albumName);
	}
	
	/**
	 * 认证证照相册展示
	 * @author tangxin 2015-05-05
	 */
	function viewCertAlbums(album){
		if(album == null) {
			return;
		}
		$("#CERT_IMG_ID").attr("src",album.coverPhoto);
		$("#CERT_NUM_ID").html(album.photoNo);
		$("#CERT_NAME_ID").html(album.albumName);
	}
	
	/**
	 * 产品相册展示
	 * @author tangxin 2015-05-04
	 */
	function viewProductAlbums(album){
		if(album == null) {
			return;
		}
		$("#PRODUCT_IMG_ID").attr("src",album.coverPhoto);
		$("#PRODUCT_NUM_ID").html(album.photoNo);
		$("#PRODUCT_NAME_ID").html(album.albumName);
	}
	
	/**
	 * 产品相册展示
	 * @author tangxin 2015-05-04
	 */
	function viewBusPhotosAlbums(album){
		if(album == null) {
			return;
		}
		$("#BUSPHOTOS_IMG_ID").attr("src",album.coverPhoto);
		$("#BUSPHOTOS_NUM_ID").html(album.photoNo);
		$("#BUSPHOTOS_NAME_ID").html(album.albumName);
	}
	
	/**
	 * 在页面展示相册
	 * @author tangxin 2015-05-05
	 */
	function viewAlbums(listAlbums){
		if(listAlbums == null || listAlbums.length < 1) {
			return;
		}
		for(var i=0; i< listAlbums.length; i++) {
			var album = listAlbums[i];
			switch(album.albumID){
			case SANZHENG_ALBUM_ID:viewSanZhengAlbums(album);
				break;
			case HONOR_ALBUM_ID:viewHonorAlbums(album);
				break;
			case CERTIFICATION_ALBUM_ID:viewCertAlbums(album);
				break;
			case PRODUCT_ALBUM_ID:viewProductAlbums(album);
				break;
			case BUSPHOTO_ALBUM_ID: viewBusPhotosAlbums(album);
				break;
			}
		}
	}
	
	/**
	 * 获取企业相册
	 * @author tangxin 2015-05-05
	 */
	function getListAlbums(){
		$.ajax({
	           url: httpPrefix + "/sales/photos/getListAlbums?cut=175x205",
	           type: "GET",
	           dataType: "json",
	           success: function(returnData) {
	              if(returnData.result.status == "true"){
	            	  viewAlbums(returnData.data);
	              }
	          }
		});
	}
	
	/**
	 * 验证相册是否为空
	 */
	function validateAlbumsEmpty(type){
		var status = false;
		switch(type){
			case SANZHENG_ALBUM_ID:
				size = parseInt($("#SANZHENG_NUM_ID").html());
				if(size > 0) status = true;
				break;
			case HONOR_ALBUM_ID:
				size = parseInt($("#HONOR_NUM_ID").html());
				if(size > 0) status = true;
				break;
			case CERTIFICATION_ALBUM_ID:
				size = parseInt($("#CERT_NUM_ID").html());
				if(size > 0) status = true;
				break;
			case PRODUCT_ALBUM_ID:
				size = parseInt($("#PRODUCT_NUM_ID").html());
				if(size > 0) status = true;
				break;
			case BUSPHOTO_ALBUM_ID:
				size = parseInt($("#BUSPHOTOS_NUM_ID").html());
				if(size > 0) status = true;
				break;
		}
		return status;
	}
	
	/**
	 * 查看相册相信信息
	 * @author tangxin 2015-05-05
	 */
	album.albumDetail = function(albumId){
		if(albumId == null) return;
		var status = validateAlbumsEmpty(albumId);
		if(!status){
			if(albumId == BUSPHOTO_ALBUM_ID){
				window.open("bus_albums_manager.html");
			}else{
				fsn.initNotificationMes('相册为空！',false);
			}
			return;
		}
		try {
    		$.cookie("cook_album_detail_id", JSON.stringify({albumId:albumId}), {
    			path : '/'
    		});
	    } catch (e) {}
	    window.location.href = "photos_albums_detail.html?status=2";
	}
	
	/* 页面初始化 */
	initComponent();
});