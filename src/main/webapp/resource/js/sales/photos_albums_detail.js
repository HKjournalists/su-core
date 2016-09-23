$(document).ready(function() {
	var fsn = window.fsn = window.fsn || {};
	var album = window.album = window.album || {};
	var httpPrefix = fsn.getHttpPrefix();
	
	var LIST_DETAIL_ALBUMS = null; //相册集合
	
	var SANZHENG_ALBUM_ID = "1"; //三证相册
	var HONOR_ALBUM_ID = "2"; //荣誉证书
	var CERTIFICATION_ALBUM_ID = "3"; //其他认证相册
	var PRODUCT_ALBUM_ID = "4"; //产品相册
	var BUSPHOTO_ALBUM_ID = "5"; //企业掠影
	
	/* 相册图片展示字段类型 */
	var PHOTO_FIELD_TYPE_ORGANIZATION = 1; //组织机构类型
	var PHOTO_FIELD_TYPE_LICENCE = 2; //营业执照类型
	var PHOTO_FIELD_TYPE_TAXREGISTER = 3; //税务登记证类型
	var PHOTO_FIELD_TYPE_HONORCERT = 4; //荣誉证书类型
	var PHOTO_FIELD_TYPE_CERTIFICATE = 5; //企业认证类型
	var PHOTO_FIELD_TYPE_PRODUCT = 6; //产品类型
	var PHOTO_FIELD_TYPE_BUSALBUM = 7; //企业掠影
	
	try {
		album.ALBUM_ID = $.cookie("cook_album_detail_id").albumId;
		/*$.cookie("cook_edit_promotion", JSON.stringify(null), {
			path : '/'
		});*/
	} catch (e) {}
	
	/**
	 * 页面初始化方法
	 * @author tangxin 2015-05-05
	 */
	function initComponent(){
		renderpic.initPhotosStyleA("picture_listBox",pictureSwitchFun);
		$("#add_btn").bind("click", addFun);
		initDetailAlbum();
	}
	
	/**
	 * 企业掠影 新增/删除事件
	 * @author tangxin 2015-05-07
	 */
	function addFun(){
		window.location.href = "bus_albums_manager.html";
	}
	
	/**
	 * 格式化字符串字段，处理页面显示 null 字段
	 * @author tangxin 2015-05-07
	 */
	function formatFiledStr(value){
		return value == null ? "" : value;
	}
	
	/**
	 * 展示三证相册中组织机构证的结构化信息
	 * @author tangxin 2015-05-06
	 */
	function setOrigPhotoField(photoField){
		if(photoField == null) return;
		var htmlStr = '<div class="div-tr"><span class="mr10 name_sty">组织机构代码：</span><span>' + formatFiledStr(photoField.no) + '</span></div>'+
   		'<div class="div-tr"><span class="mr10 name_sty">机构名称：</span><span>' + formatFiledStr(photoField.name) + '</span></div>'+
   		'<div class="div-tr"><span class="mr10 name_sty">有效期：</span><span>' + fsn.formatGridDate(photoField.startDate) + " 至 " + fsn.formatGridDate(photoField.endDate) + '</span></div>';
   		$("#album_field").html(htmlStr);
	}
	
	/**
	 * 展示三证相册中营业执照的结构化信息
	 * @author tangxin 2015-05-06
	 */
	function setLicenPhotoField(photoField){
		if(photoField == null) return;
		var htmlStr = '<div class="div-tr"><span class="mr10 name_sty">营业执照注册号：</span><span>' + formatFiledStr(photoField.no) + '</span></div>'+
			'<div class="div-tr"><span class="mr10 name_sty">经营主体名称：</span><span>' + formatFiledStr(photoField.name) + '</span></div>'+
   		'<div class="div-tr"><span class="mr10 name_sty">法人代表：</span><span>' + formatFiledStr(photoField.personInCharge) + '</span></div>'+
   		'<div class="div-tr"><span class="mr10 name_sty">有效期：</span><span>'+ fsn.formatGridDate(photoField.startDate) + " 至 " + fsn.formatGridDate(photoField.endDate) + '</span></div>';
   		$("#album_field").html(htmlStr);
	}
	
	/**
	 * 展示三证相册中税务登记证的结构化信息
	 * @author tangxin 2015-05-06
	 */
	function setTaxtPhotoField(photoField){
		if(photoField == null) return;
		var htmlStr = '<div class="div-tr"><span class="mr10 name_sty">纳税人名称：</span><span>' + formatFiledStr(photoField.name) + '</span></div>';
   		$("#album_field").html(htmlStr);
	}
	
	/**
	 * 展示认证信息相册结构化信息
	 * @author tangxin 2015-05-06
	 */
	function setCertificatePhotoField(photoField){
		if(photoField == null) return;
		var endDate = fsn.formatGridDate(photoField.endDate);
		endDate = (endDate != null ? endDate.toString() : "");
		endDate = (endDate.indexOf("2200-01-01") > -1 ? "长期有效" : endDate);
		var htmlStr = '<div class="div-tr"><span class="mr10 name_sty">认证类型：</span><span>' + formatFiledStr(photoField.type) + '</span></div>'+
			'<div class="div-tr"><span class="mr10 name_sty">有效期：</span><span>' + endDate + '</span></div>';
   		$("#album_field").html(htmlStr);
	}
	
	/**
	 * 展示认产品相册结构化信息
	 * @author tangxin 2015-05-06
	 */
	function setProductPhotoField(photoField){
		if(photoField == null) return;
		var disb = '';
		var title = 'title="点击查看报告"';
		if(photoField.censorReportNumber == 0) {
			disb = 'disabled="disabeld"';
			title ='title="当前产品无自检报告"';
		}
		var htmlStr = '<div class="div-tr"><a '+title+'><button class="mr10 k-button" '+
		    'onclick="return album.viewReport('+photoField.id+')" '+disb+
		    '>查看送检报告('+photoField.censorReportNumber+')</span></button></a></div>'+
			'<div class="div-tr"><span class="mr10 name_sty">产品名称：</span><span>' + formatFiledStr(photoField.name) + '</span></div>'+
			'<div class="div-tr"><span class="mr10 name_sty">产品规格：</span><span>' + formatFiledStr(photoField.format) + '</span></div>'+
			'<div class="div-tr"><span class="mr10 name_sty">产品描述：</span><span>' + formatFiledStr(photoField.desc) + '</span></div>';
   		$("#album_field").html(htmlStr);
	}
	
	/**
	 * 展示企业掠影结构化信息
	 * @author tangxin 2015-05-06
	 */
	function setBusPhotoField(photoField){
		if(photoField == null) return;
		$("#album_desc").html(photoField.albumDescribe);
	}
	
	/**
	 * 展示三证相册
	 * @author tangxin 2015-05-06
	 */
	function viewSanZhengAlbums(album){
		if(album == null || album.detailAlbums == null 
				|| album.detailAlbums.length < 1) return;
		renderpic.setStyleADataSource(album.detailAlbums);
		var photoField = album.detailAlbums[0].field;
		if(photoField != null) {
			if(PHOTO_FIELD_TYPE_ORGANIZATION == photoField.fieldType){
				setOrigPhotoField(photoField);
			}else if(PHOTO_FIELD_TYPE_LICENCE == photoField.fieldType){
				setLicenPhotoField(photoField);
			}else if(PHOTO_FIELD_TYPE_TAXREGISTER == photoField.fieldType){
				setTaxtPhotoField(photoField);
			}
		}
		$("#album_desc").html("该相册用于展示企业的营业执照号证件照及详情；组织机构代码证证件照及详情；" +
				"税务登记证证件照及详情。如果信息存在缺失或图片不清晰等情况，请联系公司的企业管理人员进入【我的企业》基本信息】处更新。");
	}
	
	/**
	 * 展示荣誉证书相册
	 * @author tangxin 2015-05-06
	 */
	function viewCertificateAlbums(album){
		if(album == null || album.detailAlbums == null 
				|| album.detailAlbums.length < 1) return;
		renderpic.setStyleADataSource(album.detailAlbums);
		var photoField = album.detailAlbums[0].field;
		if(photoField != null) {
			if(PHOTO_FIELD_TYPE_HONORCERT == photoField.fieldType){
				$("#album_desc").html("该相册用于展示企业荣获的荣誉证书照片。如果信息存在缺失或图片不清晰等情况，请联系公司的企业管理人员进入【我的企业》基本信息】处更新。");
				setCertificatePhotoField(photoField);
			}else if(PHOTO_FIELD_TYPE_CERTIFICATE == photoField.fieldType){
				$("#album_desc").html(" 该相册用于展示企业荣获的认证证照及详情。如果信息存在缺失或图片不清晰等情况，请联系公司的企业管理人员进入【我的企业》基本信息】处更新。");
				setCertificatePhotoField(photoField);
			}
		}
		
	}
	
	/**
	 * 展示企业产品相册
	 * @author tangxin 2015-05-06
	 */
	function viewProductAlbums(album){
		if(album == null || album.detailAlbums == null 
				|| album.detailAlbums.length < 1) return;
		renderpic.setStyleADataSource(album.detailAlbums);
		var photoField = album.detailAlbums[0].field;
		setProductPhotoField(photoField);
		$("#album_desc").html("该相册用于展示企业产品的图片及详情，每个产品仅限一张。如若图片不清晰等情况，请联系公司的企业管理人员进入【我的产品】，选择并编辑对应产品，进行图片的更换。");
	}
	
	/**
	 * 展示企业掠影相册
	 * @author tangxin 2015-05-06
	 */
	function viewBusPhotosAlbums(album){
		$(".mf115").attr("style","display:block;");
		if(album == null || album.detailAlbums == null 
				|| album.detailAlbums.length < 1) return;
		renderpic.setStyleADataSource(album.detailAlbums);
		var photoField = album.detailAlbums[0].field;
		setBusPhotoField(photoField);
	}
	
	/**
	 * 相册图片切换时，对应的字段展示
	 * @author tangxin 2015-05-06
	 */
	function pictureSwitchFun(fieldType,index){
		if(LIST_DETAIL_ALBUMS == null || fieldType == null){
			return;
		}
		fieldType = parseInt(fieldType);
		for(var i=0;i<LIST_DETAIL_ALBUMS.length;i++){
			var filedVO = LIST_DETAIL_ALBUMS[i].field;
			if(filedVO.fieldType == fieldType && filedVO.index == index){
				switch(fieldType){
				case PHOTO_FIELD_TYPE_ORGANIZATION:setOrigPhotoField(filedVO);
					break;
				case PHOTO_FIELD_TYPE_LICENCE:setLicenPhotoField(filedVO);
					break;
				case PHOTO_FIELD_TYPE_TAXREGISTER:setTaxtPhotoField(filedVO);
					break;
				case PHOTO_FIELD_TYPE_HONORCERT:setCertificatePhotoField(filedVO);
					break;
				case PHOTO_FIELD_TYPE_CERTIFICATE:setCertificatePhotoField(filedVO);
					break;
				case PHOTO_FIELD_TYPE_PRODUCT:setProductPhotoField(filedVO);
					break;
				case PHOTO_FIELD_TYPE_BUSALBUM:setBusPhotoField(filedVO);
					break;
				}
			}
		}
	}
	
	/**
	 * 在页面展示相册
	 * @author tangxin 2015-05-05
	 */
	function viewAlbums(albums){
		if(albums == null) return;
		LIST_DETAIL_ALBUMS = albums.detailAlbums;
		switch(album.ALBUM_ID){
		case SANZHENG_ALBUM_ID:$("#navigation_span").html("三证");
			viewSanZhengAlbums(albums);
			break;
		case HONOR_ALBUM_ID:$("#navigation_span").html("荣誉证书");
			viewCertificateAlbums(albums);
			break;
		case CERTIFICATION_ALBUM_ID:$("#navigation_span").html("认证证照");
			viewCertificateAlbums(albums);
			break;
		case PRODUCT_ALBUM_ID:$("#navigation_span").html("产品图片集");
			viewProductAlbums(albums);
			break;
		case BUSPHOTO_ALBUM_ID:$("#navigation_span").html("企业掠影");
			viewBusPhotosAlbums(albums);
			break;
		}
	}
	
	/**
	 * 页面初始化方法
	 * @author tangxin 2015-05-05
	 */
	function initDetailAlbum(){
		if(album.ALBUM_ID == null){
			window.location.href = "photos_albums_list.html";
			return;
		}
		$.ajax({
	        url: httpPrefix + "/sales/photos/getDetailAlbums/" + album.ALBUM_ID + "/-1/0?cut=170x180",
	        type: "GET",
	        dataType: "json",
	        success: function(returnData) {
	           if(returnData.result.status == "true"){
	         	  if(returnData.albumVO != null) {
	         		 if(returnData.albumVO.total < 1){
	         			window.location.href = "photos_albums_list.html";
	        		}else{
	        			viewAlbums(returnData.albumVO);
	        		}
	         	  }
	           }
	       }
		});
	}
	
	/* 查看产品最近送检报告 */
	album.viewReport = function(productId){
		try {
    		$.cookie("cook_viewReport_productId", JSON.stringify({productId:productId}), {
    			path : '/'
    		});
	    } catch (e) {}
	    window.location.href = "sales_view_report.html";
	}
	//隐藏删除和显示删除按钮
	var params = fsn.getUrlVars();
	var status = params[params[0]];
	if(status==1){
	   $("#mf115").hide();
	}else{
	   $("#mf115").show();
	}
	/* 页面初始化 */
	initComponent();
});