$(document).ready(function() {
	var fsn = window.fsn = window.fsn || {};
	var sepuence = window.sepuence = window.sepuence || {};
	var httpPrefix = fsn.getHttpPrefix();
	var widget = {}; 
	
	/* 排序状态 */
	var SANZHENG_SELF_SORT_STATUS = false;
	var ALBUMS_SELF_SORT_STATUS = false;
	var PRODUCT_SELF_SORT_STATUS = false;
	var CASE_SELF_SORT_STATUS = false;
	
	/* kendoTabl li id */
	var TAB_LI_CERT_ID = "certId";
	var TAB_LI_ALBUM_ID = "album";
	var TAB_LI_PRUDCT_ID = "product";
	var TAB_LI_CASE_ID = "salesCase";
	var CURRENT_LI_ID = TAB_LI_CERT_ID; //默认为认证id
	
	sepuence.listSortCertVO = null;
	sepuence.listSortAlbumVO = null;
	sepuence.listSortProductVO = null;
	sepuence.listSortSalesCaseVO = null;
	
	/**
	 * 页面初始化方法
	 * @author tangxin 2015-05-07
	 */
	function initComponent(){
		initKendoTab();
		widget.MSGWIN = saleskendoUtil.initKendoWindow("messageWindow","310","65","保存状态",false,
				true,false,[],"messageLabel","正在保存请稍候...");
		$("#concel_btn").bind("click",cancelBtnFun);
		$("#save_btn").bind("click",saveFun);
		$("#sort_btn").bind("click",selfSortFun);
		$("#createData_btn").bind("click",createDataFun);
	}
	
	/**
	 * 相册排序的方法
	 * @author tangxin 2015-05-09
	 */
	function sortFun(fromId){
		if(fromId == null) return;
		var listRes = null;
		if(fromId == "tabstrip-1"){
			listRes = sepuence.listSortCertVO;
		}else if(fromId == "tabstrip-2"){
			listRes = sepuence.listSortAlbumVO;
		}else if(fromId == "tabstrip-3"){
			listRes = sepuence.listSortProductVO;
		}else{
			listRes = sepuence.listSortSalesCaseVO;
		}
		if(listRes == null) return;
		var mos = $("div#"+fromId+" div#dom0 div.mo");
		var oids = [];
		for(var i=0;i<mos.length;i++){
			oids.push(parseInt($(mos[i]).find("a.ico_close_circle").attr("oid")));
			$(mos[i]).attr("id","m"+i);
		}
		for(var j=0;j<listRes.length;j++){
			var vo = listRes[j];
			var index = oids.indexOf(vo.id);
			vo.sort = (index > -1 ? (index+1) : index);
		}
	}
	
	/**
	 * 判断指定的相册是否排序
	 * @author tangxin 2015-05-13
	 */
	function judgeAlbums(listAlbums){
		if(listAlbums == null || listAlbums.length < 1){
			return false;
		}
		var status = false;
		for(var i=0;i<listAlbums.length;i++){
			var alb = listAlbums[i];
			if(alb.sort > 0){
				status = true;
				break;
			}
		}
		return status;
	}
	
	/**
	 * 移除图片的拖动事件
	 * @author tangxin 2015-05-13
	 */
	function unbindImgDrag(status){
		if(status == null){
			return;
		}
		cancelSelfSortFun(status);
	}
	
	/**
	 * 加载排序的企业三证相册信息
	 * @author tangxin 2015-05-09
	 */
	function loadBusCertInfo(){
		drag.initDrag("tabstrip-1",sortFun);
		$("#small_ul").html("");
		sepuence.listSortCertVO = null;
			$.ajax({
		        url: httpPrefix + "/sales/sort/getSanZhengPhotos/-1/20",
		        type: "GET",
		        dataType: "json",
		        success: function(returnData) {
		           if(returnData.result.status == "true"){
		        	   SANZHENG_SELF_SORT_STATUS = returnData.selfSort;
		        	   /* 全局变量 产品排序vo数组 */
		        	   sepuence.listSortCertVO = returnData.listSanZhengPhotosVO;
		        	   /* 在页面显示 产品排序内容 */
		        	   viewBusunitCertContent();
		           }
		       }
			});
	}
	
	/**
	 * 加载排序的企业掠影相册信息
	 * @author tangxin 2015-05-09
	 */
	function loadSortBusinesAlbums(){
		drag.initDrag("tabstrip-2",sortFun);
		$("#small_ul").html("");
		sepuence.listSortAlbumVO = null;
			$.ajax({
		        url: httpPrefix + "/sales/sort/getSortBusinesAlbum/-1/20",
		        type: "GET",
		        dataType: "json",
		        success: function(returnData) {
		           if(returnData.result.status == "true"){
		        	   /* 全局变量 产品排序vo数组 */
		        	   sepuence.listSortAlbumVO = returnData.listSorBusinesAlbumVO;
		        	   ALBUMS_SELF_SORT_STATUS = judgeAlbums(sepuence.listSortAlbumVO);
		        	   /* 在页面显示 产品排序内容 */
		        	   viewBusAblumSortContent();
		           }
		       }
			});
	}
	
	/**
	 * 加载排序的产品相册信息
	 * @author tangxin 2015-05-09
	 */
	function loadSortProductInfo(){
		drag.initDrag("tabstrip-3",sortFun);
		$("#small_ul").html("");
		sepuence.listSortProductVO = null;
			$.ajax({
		        url: httpPrefix + "/sales/sort/getSortProductAlbum/-1/20",
		        type: "GET",
		        dataType: "json",
		        success: function(returnData) {
		           if(returnData.result.status == "true"){
		        	   PRODUCT_SELF_SORT_STATUS = returnData.selfSort;
		        	   /* 全局变量 产品排序vo数组 */
		        	   sepuence.listSortProductVO = returnData.listSortProductVO;
		        	   /* 在页面显示 产品排序内容 */
		        	   viewProductSortContent();
		           }
		       }
			});
	}
	
	/**
	 * 加载排序的企业掠销售案例相册信息
	 * @author tangxin 2015-05-09
	 */
	function loadSortSalesCase(){
		drag.initDrag("tabstrip-4",sortFun);
		$("#small_ul").html("");
        sepuence.listSortSalesCaseVO = null;			
        $.ajax({
		        url: httpPrefix + "/sales/sort/getSortSalesCase/-1/20",
		        type: "GET",
		        dataType: "json",
		        success: function(returnData) {
		           if(returnData.result.status == "true"){
		        	   /* 全局变量 产品排序vo数组 */
		        	   sepuence.listSortSalesCaseVO = returnData.listSortSalesCaseAlbumVO;
		        	   CASE_SELF_SORT_STATUS = judgeAlbums(sepuence.listSortSalesCaseVO);
		        	   /* 在页面显示 产品排序内容 */
		        	   viewSalesCaseSortContent();
		           }
		       }
			});
	}
	
	/**
	 * 将产品图片添加到指定区域
	 * @author tangxin 2015-05-09
	 */
	function addProImgToTarget(proId, sort){
		if(sepuence.listSortProductVO == null || proId == null){
			return;
		}
		for(var i=0;i<sepuence.listSortProductVO.length;i++){
			var vo = sepuence.listSortProductVO[i];
			if(vo.id == proId){
				if(sort){
					drag.addSortAttachment("tabstrip-3", vo, addProImgToTarget);
				}else{
					viewNotSortAlbums("small_ul", vo, addProImgToTarget);
				}
			} 
		}
	}
	
	/**
	 * 将企业掠影图片添加到指定区域
	 * @author tangxin 2015-05-09
	 */
	function addAlbumToTarget(bid, sort){
		if(sepuence.listSortAlbumVO == null || bid == null){
			return;
		}
		for(var i=0;i<sepuence.listSortAlbumVO.length;i++){
			var vo = sepuence.listSortAlbumVO[i];
			if(vo.id == bid){
				if(sort){
					drag.addSortAttachment("tabstrip-2", vo, addAlbumToTarget);
				}else{
					viewNotSortAlbums("small_ul", vo, addAlbumToTarget);
				}
				
			} 
		}
	}
	
	/**
	 * 将销售案例图片添加到指定区域
	 * @author tangxin 2015-05-09
	 */
	function addSalesCaseToTarget(rid, sort){
		if(sepuence.listSortSalesCaseVO == null || rid == null){
			return;
		}
		for(var i=0;i<sepuence.listSortSalesCaseVO.length;i++){
			var vo = sepuence.listSortSalesCaseVO[i];
			if(vo.id == rid){
				if(sort){
					drag.addSortAttachment("tabstrip-4", vo, addSalesCaseToTarget);
				}else{
					viewNotSortAlbums("small_ul", vo, addSalesCaseToTarget);
				}
				
			} 
		}
	}
	
	/**
	 * 将销售企业认证图片添加到指定区域
	 * @author tangxin 2015-05-09
	 */
	function addCertToTarget(cid, sort){
		if(sepuence.listSortCertVO == null || cid == null){
			return;
		}
		for(var i=0;i<sepuence.listSortCertVO.length;i++){
			var vo = sepuence.listSortCertVO[i];
			if(vo.id == cid){
				if(sort){
					drag.addSortAttachment("tabstrip-1", vo, addCertToTarget);
				}else{
					viewNotSortAlbums("small_ul", vo, addCertToTarget);
				}
				
			} 
		}
	}
	
	/**
	 * 判断排序的资料是否达到上限，
	 * return true or false
	 * @author tangxin 2015-05-17
	 */
	function judgeAlbumsOutput(){
		var listDiv = $("div.mo");
	    var len = listDiv.length;
	    var maxLen = 8;
	    if(CURRENT_LI_ID == TAB_LI_CERT_ID || CURRENT_LI_ID == TAB_LI_ALBUM_ID){
	    	maxLen = 8; // 三证 和 企业掠影不能超过 8 张 
	    }else if(CURRENT_LI_ID == TAB_LI_PRUDCT_ID){
	    	maxLen = 50; // 产品资料不能超过 50 张
		}else if(CURRENT_LI_ID == TAB_LI_CASE_ID){
			maxLen = 20; //销售案例不能超过 20 张
		}
	    /* 如果数量达到上限 return false */
	    if(len == maxLen){
       		fsn.initNotificationMes(fsn.l("当前资料最多只能选" + maxLen + "张！"), false);
       		return false;
	    }
	    return true;
	}
	
	/**
	 * 页面展示未排序的产品方法
	 * @author tangxin 2015-05-09
	 */
	function viewNotSortAlbums(formId, productVO, imgClickFun){
		if(formId == null || productVO == null){
			return;
		}
		productVO.sort = -1;
		var url="http://fsnrec.com:8080/portal/img/product/temp/temp.jpg";
		if(productVO.url!=null&&productVO.url!=""){
			url=productVO.url;
		}
		var clo ='<div class="mod-photo-op"><div class="position-absolute" style="display:none;">'+
				'<div class="bg"></div><p class="fav_cover" title="点击添加到排序区域">添加到排序</p></div></div>';
		var aLi = $("<li></li>");
		aLi.html('<i class="arr2"></i><img oid="'+productVO.id+'" title="点击添加到排序区域" class="list_img" width="120px" src="'+url+'"/>'+
				'<span class="name_span">'+productVO.name+'</span>'+clo);
		$("#"+formId).append(aLi);
		$(aLi).mouseover(function(){
    		$(this).find("div.position-absolute").css("display","block");
    	});
    	$(aLi).mouseout(function(){
    		$(this).find("div.position-absolute").css("display","none");
    	});
    	$(aLi).find("div.mod-photo-op").click(function(){
    		if(!judgeAlbumsOutput()) return;
    		var ali = $(this).parent();
    		ali.remove();
    		if(imgClickFun != null) {
    			imgClickFun(ali.find("img.list_img").attr("oid"), true);
    		}
    	});
    	$(aLi).find("img.list_img").click(function(){
    		/* 判断当前排序的资料是否达到上限 */
    		if(!judgeAlbumsOutput()) return;
    		$(this).parent().remove();
    		if(imgClickFun != null) {
    			imgClickFun($(this).attr("oid"), true);
    		}
    	});
	}
	
	/**
	 * 展示页面排序内容的方法
	 * @author tangxin 2015-05-09
	 */
	function viewProductSortContent(){
		if(sepuence.listSortProductVO == null){
			return;
		}
		var sortVO = sepuence.listSortProductVO;
		$("#tabstrip-1 div#dom0").html("");
		$("#tabstrip-2 div#dom0").html("");
		$("#tabstrip-4 div#dom0").html("");
		for(var i=0;i<sortVO.length;i++){
			if(!PRODUCT_SELF_SORT_STATUS && i < 8){
				drag.addSortAttachment("tabstrip-3", sortVO[i], addProImgToTarget);
			}else if(sortVO[i].sort > 0){
				/* sort>0 表示已经排序过的产品，添加到排序内容区域 */
				drag.addSortAttachment("tabstrip-3", sortVO[i], addProImgToTarget);
			} else {
				viewNotSortAlbums("small_ul", sortVO[i], addProImgToTarget);
			}
		}
		unbindImgDrag(PRODUCT_SELF_SORT_STATUS);
	}
	
	/**
	 * 展示页面排序内容的方法
	 * @author tangxin 2015-05-09
	 */
	function viewBusAblumSortContent(){
		if(sepuence.listSortAlbumVO == null){
			return;
		}
		var sortVO = sepuence.listSortAlbumVO;
		$("#tabstrip-1 div#dom0").html("");
		$("#tabstrip-3 div#dom0").html("");
		$("#tabstrip-4 div#dom0").html("");
		for(var i=0;i<sortVO.length;i++){
			if(!ALBUMS_SELF_SORT_STATUS && i < 8) {
				drag.addSortAttachment("tabstrip-2", sortVO[i], addAlbumToTarget);
			} else if(sortVO[i].sort > 0){
				/* sort>0 表示已经排序过的产品，添加到排序内容区域 */
				drag.addSortAttachment("tabstrip-2", sortVO[i], addAlbumToTarget);
			} else {
				viewNotSortAlbums("small_ul", sortVO[i], addAlbumToTarget);
			}
		}
		unbindImgDrag(ALBUMS_SELF_SORT_STATUS);
	}
	
	/**
	 * 展示页面排序内容的方法
	 * @author tangxin 2015-05-09
	 */
	function viewSalesCaseSortContent(){
		if(sepuence.listSortSalesCaseVO == null){
			return;
		}
		var sortVO = sepuence.listSortSalesCaseVO;
		$("#tabstrip-1 div#dom0").html("");
		$("#tabstrip-2 div#dom0").html("");
		$("#tabstrip-3 div#dom0").html("");
		for(var i=0;i<sortVO.length;i++){
			if(!CASE_SELF_SORT_STATUS && i < 8) {
				drag.addSortAttachment("tabstrip-4", sortVO[i], addSalesCaseToTarget);
			}else if(sortVO[i].sort > 0){
				/* sort>0 表示已经排序过的产品，添加到排序内容区域 */
				drag.addSortAttachment("tabstrip-4", sortVO[i], addSalesCaseToTarget);
			} else {
				viewNotSortAlbums("small_ul", sortVO[i], addSalesCaseToTarget);
			}
		}
		unbindImgDrag(CASE_SELF_SORT_STATUS);
	}
	
	/**
	 * 展示页面排序内容的方法
	 * @author tangxin 2015-05-09
	 */
	function viewBusunitCertContent(){
		if(sepuence.listSortCertVO == null){
			return;
		}
		var sortVO = sepuence.listSortCertVO;
		$("#tabstrip-2 div#dom0").html("");
		$("#tabstrip-3 div#dom0").html("");
		$("#tabstrip-4 div#dom0").html("");
		for(var i=0;i<sortVO.length;i++){
			if(!SANZHENG_SELF_SORT_STATUS && i < 8){
				/* 默认排序时选取前 8 张 */
				drag.addSortAttachment("tabstrip-1", sortVO[i], addCertToTarget);
			}else if(sortVO[i].sort > 0){
				/* sort>0 表示已经排序过的产品，添加到排序内容区域 */
				drag.addSortAttachment("tabstrip-1", sortVO[i], addCertToTarget);
			} else {
				viewNotSortAlbums("small_ul", sortVO[i], addCertToTarget);
			}
		}
		unbindImgDrag(SANZHENG_SELF_SORT_STATUS);
	}
	
	/**
	 * 冒泡排序
	 * @author tangxin 2015-05-07
	 */
	function MaoPaoSort(){
		if(CURRENT_LI_ID == null){
			return;
		}
		var tempArry = null;
		/* 根据当前被激活选项卡的id，获取需要排序的集合 */
		switch(CURRENT_LI_ID){
		case TAB_LI_CERT_ID:
			if(SANZHENG_SELF_SORT_STATUS){
				tempArry = sepuence.listSortCertVO;
			}
			break;
		case TAB_LI_ALBUM_ID:
			if(ALBUMS_SELF_SORT_STATUS){
				tempArry = sepuence.listSortAlbumVO;
			}
			break;
		case TAB_LI_PRUDCT_ID:
			if(PRODUCT_SELF_SORT_STATUS){
				tempArry = sepuence.listSortProductVO;
			}
			break;
		case TAB_LI_CASE_ID:
			if(CASE_SELF_SORT_STATUS){
				tempArry = sepuence.listSortSalesCaseVO;
			}
			break;
		}
		if(tempArry == null || tempArry.length < 1){
			return;
		}
		/* 冒泡排序 */
		 for(var i=0;i<tempArry.length-1;i++){
             for(var j=i+1;j<tempArry.length;j++){
                    if((tempArry[i].sort > tempArry[j].sort && tempArry[j].sort != -1) ||
                    		(tempArry[i].sort == -1 && tempArry[i].sort < tempArry[j].sort)){
                    	var mvo = tempArry[j];
                    	tempArry[j] = tempArry[i];
                    	tempArry[i] = mvo;
                    }
              }
		 }
	}
	
	/**
	 * KendoTab onActivate事件
	 * @author tangxin 2015-05-07
	 */
	function onActivate(e){
		var li = e.item;
		CURRENT_LI_ID = $(li).attr("id");
		switch(CURRENT_LI_ID) {
		case TAB_LI_CERT_ID:loadBusCertInfo();
			break;
		case TAB_LI_ALBUM_ID:loadSortBusinesAlbums();
			break;
		case TAB_LI_PRUDCT_ID:loadSortProductInfo();
			break;
		case TAB_LI_CASE_ID:loadSortSalesCase();
			break;
		}
	}
	
	/**
	 * 初始化KendoTab
	 * @author tangxin 2015-05-07
	 */
	 function initKendoTab(){
		 $("#tabstrip").kendoTabStrip({
	         animation:  {
	             open: {
	                 effects: "fadeIn"
	             }
	         },
		    activate: onActivate
	     });
	 }
	 
	 /**
	  * 点击取消按钮时隐藏 自定义排序 区域
	  * @author tangxin 2015-05-13
	  */
	 function cancelSelfSortFun(selfSortStatus){
		 if(!selfSortStatus){
			 document.getElementById("defatul_order").checked = true;
		 } else {
			 document.getElementById("self_order").checked = true;
		 }
		 drag.unBindEvent();
		 $("div.mo a.ico_close_circle").removeClass("ico_close_circle");
		 $("div.con_b").hide();
		 $("div.div_btn_sty").hide();
		 $("div.self_sort_div").show();
	 }
	 
	 /**
	  * 页面取消按钮 点击事件
	  * @author tangxin 2015-05-13
	  */
	 function cancelBtnFun(){
		 switch(CURRENT_LI_ID) {
		 case TAB_LI_CERT_ID:
			cancelSelfSortFun(SANZHENG_SELF_SORT_STATUS);
			sepuence.listSortCertVO = null;
			loadBusCertInfo();
			break;
		 case TAB_LI_ALBUM_ID:
			 cancelSelfSortFun(ALBUMS_SELF_SORT_STATUS);
			 sepuence.listSortAlbumVO = null;
			 loadSortBusinesAlbums();
			break;
		 case TAB_LI_PRUDCT_ID:
			 cancelSelfSortFun(PRODUCT_SELF_SORT_STATUS);
			 sepuence.listSortProductVO = null;
			 loadSortProductInfo();
			break;
		 case TAB_LI_CASE_ID:
			 cancelSelfSortFun(CASE_SELF_SORT_STATUS);
			 sepuence.listSortSalesCaseVO = null;
			 loadSortSalesCase();
			break;
		 }
	 }
	 
	 /**
	  * 换图或排序 点击事件
	  * @author tangxin 2015-05-13
	  */
	 function selfSortFun(){
		 drag.bindEvent();
		 document.getElementById("self_order").checked = true;
		 $("div.mo a").addClass("ico_close_circle");
		 $("div.con_b").slideDown("slow");
		 $("div.div_btn_sty").slideDown("slow");
		 $("div.self_sort_div").hide();
	 }
	 
	 /**
	  * 页面的确定按钮事件
	  * @author tangxin 2015-05-07
	  */
	 function saveFun(){
		 if(widget.MSGWIN != null) {
			 widget.MSGWIN.open().center();
		 }
		 $.ajax({
	           url: httpPrefix + "/sales/sort/saveSortData",
	           type: "POST",
	           dataType: "json",
	           data: JSON.stringify(sepuence),
	           contentType: "application/json; charset=utf-8",
	           success: function(returnData) {
	        	   widget.MSGWIN.close();
	              if(returnData.result.status == "true"){
	            	  sepuence = returnData.dataSortVO;
	            	  fsn.initNotificationMes(fsn.l("保存成功！"), true);
	              }else {
	            	  fsn.initNotificationMes(fsn.l("保存失败！"), false);
	              }
	           },
	           error: function(e) {
	        	   fsn.initNotificationMes(fsn.l("服务器异常！"), false);
	           }
	    });
		 
	 }
	 
	 /**
	  * 生成电子资料方法
	  * @author tangxin 2015-05-07
	  */
	 function createDataFun(){
		 if(widget.MSGWIN != null) {
			 $("#messageLabel").html("创建资料中，请稍后...");
			 widget.MSGWIN.open().center();
		 }
		 $.ajax({
	           url: httpPrefix + "/sales/sort/createElectData",
	           type: "POST",
	           dataType: "json",
	           timeout:600000,//超时等待10分钟
	           data: JSON.stringify(sepuence),
	           contentType: "application/json; charset=utf-8",
	           success: function(returnData) {
	        	   widget.MSGWIN.close();
	        	   $("#messageLabel").html("正在保存请稍候...");
	              if(returnData.result.status == "true"){
	            	  fsn.initNotificationMes(fsn.l("资料创建成功！"), true);
	              }else {
	            	  fsn.initNotificationMes(fsn.l("资料创建失败！"), false);
	              }
	           },
	           error: function(e) {
	        	   $("#messageLabel").html("正在保存请稍候...");
	        	   fsn.initNotificationMes(fsn.l("服务器异常！"), false);
	           }
	    });
	 }
	 
	/* 调用页面初始化方法 */
	initComponent();
});