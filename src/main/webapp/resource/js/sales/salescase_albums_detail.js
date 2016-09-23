$(document).ready(function() {
	var fsn = window.fsn = window.fsn || {};
	var album = window.album = window.album || {};
	var httpPrefix = fsn.getHttpPrefix();
	var urlParameter= (window.location.href).split("?")[1]; //当前页面url参数

	/**
	 * 页面初始化方法
	 * @author tangxin 2015-05-05
	 */
	function initComponent(){
		renderpic.initPhotosStyleA("picture_listBox",pictureSwitchFun);
		$("#add_btn").bind("click", backhome);
		initDetailAlbum();
	}

	/**
	 * 相册图片切换时，对应的字段展示
	 * @author tangxin 2015-05-06
	 */
	function pictureSwitchFun(fieldType,index){
		if(fieldType == null){
			return;
		}
		fieldType = parseInt(fieldType);
	}

	/**
	 * 企业掠影 新增/删除事件
	 * @author tangxin 2015-05-07
	 */
	function backhome(){
		window.location.href = "../../../views/sales/sales_home.html";
	}

	/**
	 * 在页面展示相册
	 * @author tangxin 2015-05-05
	 */
	function viewAlbums(album){
		if(album == null) return;
		$(".mf115").attr("style","display:block;");
		if(album == null || album.name == null || album.detailAlbums.length < 1) return;
		renderpic.setStyleADataSource(album.detailAlbums);
	}
	
	/**
	 * 页面初始化方法
	 * @author tangxin 2015-05-05
	 */
	function initDetailAlbum(){
		var urlParameter= (window.location.href).split("?")[1]; //当前页面url参数
		if(!urlParameter){
			return;
		}
		$.ajax({
	        url: httpPrefix + "/sales/case/getcasealbum/"+urlParameter+"?cut=170x180",
	        type: "GET",
	        dataType: "json",
	        success: function(returnData) {
	           if(returnData.result.status == "true"){
	         	  if(returnData.data != null) {
					 $("#salecase_name").html(returnData.data.name);
					 $("#album_desc").html(returnData.data.describe);
	         		 viewAlbums(returnData.data);
	         	  }
	           }
	       }
		});
	}
	
	/* 页面初始化 */
	initComponent();
});