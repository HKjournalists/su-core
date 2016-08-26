$(document).ready(function() {
	var fsn = window.fsn = window.fsn || {};
	var busAlbum = window.busAlbum = window.busAlbum || {};
	var httpPrefix = fsn.getHttpPrefix();
	var widget = {UPLOAD:null,MSGWIN:null};
	var RES_TYPE = ".jpg;.png;.jpeg;.bmp;.gif;";
	busAlbum.model = {id:null,resource:[]};
	
	/**
	 * 页面初始化方法
	 * @author tangxin 2015-05-06
	 */
	function initComponent(){
		initUplodaWidget();
		widget.MSGWIN = saleskendoUtil.initKendoWindow("messageWindow","300","65","保存状态",false,
				true,false,["Close"],"messageLabel","正在保存请稍候...");
		$("#save_btn").bind("click", saveAlbums);
		$("#concel_btn").bind("click", cancelFun);
		inintBusAlbums();
	}
	
	/**
	 * 文件上传控件的始化方法
	 * @author tangxin 2015-05-06
	 */
	function initUplodaWidget(){
		$("#upload-div").html('<input id="uploadFile" type="file"/>');
		widget.UPLOAD = saleskendoUtil.buildUpload("uploadFile",null,"Upload Picture",RES_TYPE,true,addPicture,null);
	}
	
	
	
	/**
	 * 加载企业掠影相册
	 * @author tangxin 2015-05-06
	 */
	function inintBusAlbums(){
		$.ajax({
	           url: httpPrefix + "/sales/photos/findAlbumByName/企业掠影",
	           type: "GET",
	           dataType: "json",
	           success: function(returnData) {
	              if(returnData.result.status == "true"){
	            	  busAlbum.model = returnData.albumVO;
	            	  if(busAlbum.model != null && busAlbum.model.resource != null) {
	            		  var res = busAlbum.model.resource;
	            		  for(var i=0;i<res.length;i++){
        					createViewPictureHtml(res[i]);
	            		  }
	            		  /* 判断是否隐藏上传按钮 */
	            		  judgeAlbumsSize();
	            	  }
	              }
	          }
		});
	}
	
	/**
	 * 页面取消按钮的click事件方面
	 * @author tangxin 2015-05-07
	 */
	function cancelFun(){
		if(busAlbum.model != null && busAlbum.model.resource != null 
				&& busAlbum.model.resource.length < 1){
			window.location.href = "photos_albums_list.html";
		} else {
			window.location.href = "photos_albums_detail.html";
		}
	}
	
	/**
	 * 判断当前相册大小，图片超过10张则隐藏上传按钮
	 * @author tangxin 2015-05-13
	 */
	function judgeAlbumsSize(){
		if(busAlbum.model.resource == null){
			return 0;
		}
		var size = 0;
		for(var i=0;i<busAlbum.model.resource.length;i++){
			var res = busAlbum.model.resource[i];
			if(res.delStatus == 0) size += 1;
		}
		if(size < 10){
			$("#upload-div div.k-upload-button").removeAttr("disabled","disabled");
			$("#uploadFile").removeAttr("disabled","disabled");
		}else{
			$("#upload-div div.k-upload-button").attr("disabled","disabled");
			$("#uploadFile").attr("disabled","disabled");
		}
		return size;
	}
	
	/**
	 * 从页面删除相册图片资源
	 * @author tangxin 2015-05-06
	 */
	function delAlbumRes(guid,resId){
		if(!guid || !busAlbum.model.resource) {
			return;
		}
		saleskendoUtil.removeResByGUID(busAlbum.model.resource,guid,resId);
		/* 判断是否隐藏上传按钮 */
		judgeAlbumsSize();
	}
	
	/**
	 * 设置封面
	 * @author tangxin 2015-05-06
	 */
	function setCoverByGUID(guid,resId){
		if(!guid || !busAlbum.model.resource) {
			return;
		}
		saleskendoUtil.setCoverByGUID(busAlbum.model.resource,guid,resId);
	}
	
	/**
	 * 创建展示图片的html标签
	 * @author tangxin 2015-05-06
	 */
	function createViewPictureHtml(resource){
		if(resource == null || resource.delStatus == 1) return;
		var src = "";
		if(resource.url){
			src = 'src="'+resource.url+'"';
		}else if(resource.file){
			src = 'src="data:image/jpg;base64,' + resource.file + '"';
		}
		var p_fav_class = (resource.cover == 1 ? "fav_cover" : "fav");
    	var p_fav_title = (resource.cover == 1 ? "当前图片已经设为封面" : "将此照片设置为封面");
    	var p_fav_html = (resource.cover == 1 ? "封面" : "设置为封面");
    	var clo = '<div class="mod-photo-op"><span class="photo-op-tip" guid='+resource.guid+' i='+resource.id+' title="删除此图片">' +
		  				'<i class="icon-m icon-expansion-m"></i></span>'+
		  				'<div class="position-absolute" guid='+resource.guid+' i='+resource.id+' style="display:none;">'+
		  				'<div class="bg"></div><p class="'+p_fav_class+'" title="'+p_fav_title+'">'+p_fav_html+'</p></div></div>';
    	
		var htmlStr = '<div class="bor3 bg"><img '+src+' /><span class="w20">'+resource.fileName+'</span>'+clo+'</div>';
		var aLi = $("<li></li>");
		$(aLi).mouseover(function(){
    		$(this).find("span.photo-op-tip").css("display","block");
    		$(this).find("div.position-absolute").css("display","block");
    	});
    	$(aLi).mouseout(function(){
    		$(this).find("span.photo-op-tip").css("display","none");
    		$(this).find("div.position-absolute").css("display","none");
    	});
		aLi.html(htmlStr);
		$("#picture_listBox").prepend(aLi);
		
		/* 删除图片 */
      	$(aLi).find("span.photo-op-tip").click(function(e){
      		$(aLi).remove();
      		delAlbumRes($(this).attr("guid"),$(this).attr("i"));
    	});
      	
      	/* 设置为封面 */
    	$(aLi).find("div.position-absolute").click(function(e){
    		var coverP = $("p.fav_cover");
    		if(coverP != null) {
    			$(coverP).attr("class","fav");
    			$(coverP).attr("title","将此照片设置为封面");
    			$(coverP).html("设置为封面");
    		}
    		var pfav = $(this).find("p.fav");
    		if(pfav != null) {
    			$(pfav).attr("class","fav_cover");
    			$(pfav).attr("title","当前图片已经设为封面");
    			$(pfav).html("封面");
    		}
    		setCoverByGUID($(this).attr("guid"),$(this).attr("i"));
    	});
	}
	
	/**
	 * 添加文件方法
	 * @author tangxin 2015-05-06
	 */
	function addPicture(listRes){
		initUplodaWidget();
		if(listRes == null || listRes.length < 1) return;
		busAlbum.model = (busAlbum.model == null ? {id:null,resource:[]} : busAlbum.model);
		var res = busAlbum.model.resource;
		res = (res != null ? res : []);
		var size = judgeAlbumsSize();
		var lenn = (size + listRes.length);
		var leng = (lenn > 10 ? 10 - size : listRes.length);
		for(var i=0;i<leng;i++){
			createViewPictureHtml(listRes[i]);
			busAlbum.model.resource.push(listRes[i]);
		}
		if(lenn > 10) fsn.initNotificationMes(fsn.l("最多只能上传10张掠影图片！"), false);
		/* 判断是否隐藏上传按钮 */
		judgeAlbumsSize();
		
	}
	
	var BUSPHOTO_ALBUM_ID = "5"; //企业掠影
	
	/* 跳转到相册详细界面 */
	function goDetailAlbums(){
		try {
    		$.cookie("cook_album_detail_id", JSON.stringify({albumId:BUSPHOTO_ALBUM_ID}), {
    			path : '/'
    		});
	    } catch (e) {}
	    salesUtil.setWindowLocationHref("photos_albums_detail.html");
	}
	
	/**
	 * 保存企业掠影信息
	 * @author tangxin 2015-05-06
	 */
	function saveAlbums(){
		if(busAlbum.model != null && busAlbum.model.resource != null 
				&&  busAlbum.model.resource.length < 1){
			 fsn.initNotificationMes(fsn.l("你没有上传任何图片！"), false);
			 return;
		}
		busAlbum.model.name = "企业掠影";
		busAlbum.model.describe = "该相册用于展示企业风采，最多可保存十张图片。可点击【添加/删除】对图片进行替换。";
		if(widget.MSGWIN != null) {
			widget.MSGWIN.open().center();
		}
		$.ajax({
	           url: httpPrefix + "/sales/photos/" + (busAlbum.model.id == null ? "create" : "update"),
	           type: busAlbum.model.id == null ? "POST" : "PUT",
	           dataType: "json",
	           timeout:6000000,
	           data: JSON.stringify(busAlbum.model),
	           contentType: "application/json; charset=utf-8",
	           success: function(returnData) {
	        	   widget.MSGWIN.close();
	              if(returnData.result.status == "true"){
	            	  fsn.initNotificationMes(fsn.l("保存成功！"), true);
	            	  busAlbum.model = returnData.albumVO;
	            	  /* 跳转到相册详细界面 */
	            	  goDetailAlbums();
	              }else {
	            	  fsn.initNotificationMes(fsn.l("保存失败！"), false);
	              }
	           },
	           error: function(e) {
	        	   fsn.initNotificationMes(fsn.l("服务器异常！"), false);
	           }
	    });
	}
	
	/* 调用页面初始方法 */
	initComponent();
});