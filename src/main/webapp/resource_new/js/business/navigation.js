$(function() {
    var fsn = window.fsn = window.fsn || {}; // 全局命名空间
    var navigation = window.fsn.navigation = window.fsn.navigation ||{};
    var portal = fsn.portal = fsn.portal || {}; // portal命名空间
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
     var List = [];
     var currentBusinessID = null;
     var smallData = [];
     /**
      * 初始化页面
      */
     navigation.initialize = function(){
    	 //弹窗初始化
    	 $("#CONFIRM_COMMON_WIN").kendoWindow({
				width: "480",
				height:"auto",
				title: "请添加快速访问页面",
				visible: false,
				resizable: false,
				draggable:false,
				modal: true
			});
    	 //获取当前登录用户的ID
    	 portal.currentBusiness = getCurrentBusiness();
    	 currentBusinessID = portal.currentBusiness.id;
    	
    	 navigation.initView();
    	 
    	 //获取当前登录用户权限-sso
    	 navigation.getAuthority();
    	 //给弹窗赋值
    	 $("#bigOption").kendoDropDownList({
     		dataTextField: "text",
     		dataValueField: "id",
     		dataSource : List,
     		change: function(e) {
     		   var value = this.value();
     		   smallData  = navigation.getSmallOptionDateSource(value);
     		   $("#smallOption").data("kendoDropDownList").setDataSource(smallData);
     		  }
     	});
    	 $("#smallOption").kendoDropDownList({
	 		    dataTextField: "text",
	 		    dataValueField: "id",
	 		   dataSource : List[0].items
	 		});
    	 //获取当前用户的快速导航
    	navigation.getNavigationList();
    	
     };
     
     /**
      * 获取第二个选框的数据
      */
     navigation.getSmallOptionDateSource=function(id){
		 for(var i=0;i<List.length;i++){
			 if(List[i].id == id){
				 smallData=List[i].items;
			 }
		 }
    	 return smallData;
     };
     
     /**
      * 获取当前企业信息
      */
     navigation.initView = function() {
         $.ajax({
             url: portal.HTTP_PREFIX + "/business/getBusinessByOrg",
             type: "GET",
             dataType: "json",
             async: false,
             success: function(returnValue) {
            	 if(returnValue.data != null){
            		 $("#businessAbout").html(returnValue.data.about);
            		 var logos = returnValue.data.logoAttachments;
            		 $("#businessLogo").attr("src",logos[0].url);
            	 }
             }
         });
     };
     
     
     
     /**
      * 获取当前用户已经创建的快速导航portal.HTTP_PREFIX + "/business/getNavigationList";
      */
     navigation.getNavigationList = function(){
    	 $.ajax({
	 			url : portal.HTTP_PREFIX + "/business/getNavigationList",
	 			type:"GET",
	 			dataType:"json",
	 			async:false,
	 			success:function(result){
	 				var objs = result.navigationList;
	 				if(objs != null){
	 					//将查询到的navigationList，赋值给页面，给页面使用
	 					$.each(objs,function(n,na){
	 						var returnSmallOption = na.smallOption;
	 						var returnUrl = na.navigationURL;
	 						var returnId = na.id;
	 						createButton(returnSmallOption,returnUrl,returnId);
	 					});
	 				}
	 			},
	 		});

    	
    	 };
     /**
      * 获取当前登录的用户sso权限名称
      */
    	 navigation.getAuthority = function(businessID){
	 		$.ajax({
	 			//为弹框准备数据，在sso获取权限名称 http://devfsn.fsnip.com:8080/fsn-core/service/user/menubar
	 			url: portal.HTTP_PREFIX + "/user/menubar",
	 			type:"GET",
	 			dataType:"json",
	 			async:false,
	 			success:function(result){
	 				var objs = result.menubar;
	 				if(objs != undefined && objs != null){
	 					List=objs;
	 				}
	 			},
	 		});
     };
     
     /**
      * 点击添加快速访问页面按钮时，
      */
     $("#addNavigation").click(function(){
    	 $("#CONFIRM_COMMON_WIN").data("kendoWindow").center().open();
     });
     
 	/**
 	 * 添加快速访问页面
 	 */
 	$("#confirm_yes_btn").bind("click",function(){
		var url="";
		var smallOptionName="";
 		var smallOption = $("#smallOption").data("kendoDropDownList").dataItem();
		if(!smallOption){
			url=$("#bigOption").data("kendoDropDownList").dataItem().url;
			smallOptionName= $("#bigOption").data("kendoDropDownList").text();
		} else{
			url= smallOption.url;
			smallOptionName=$("#smallOption").data("kendoDropDownList").text();
		}
		var businessNavigation = {
			bigOption : $("#bigOption").data("kendoDropDownList").text(),
			smallOption : smallOptionName,
			//根据选中的值-动态的设置URL
			navigationURL : url,
			//获取当前登录的企业ID
			businessID : currentBusinessID
		};
 		addNavigation(businessNavigation);
		close();
	});
	
 	/**
 	 * 取消添加，关闭弹窗
 	 */
	$("#confirm_no_btn").bind("click",function(){
		close();
	});
	/**
	 * 关闭弹窗函数
	 */
	function close(){
		$("#CONFIRM_COMMON_WIN").data("kendoWindow").close();
	};
	
 	
	/**
	 * 创建页面BusinessNavigation对象
	 */
	var createNavigation = function(url){
		var businessNavigation = {
				bigOption : $("#bigOption").data("kendoDropDownList").text(),
				smallOption : $("#smallOption").data("kendoDropDownList").text(),
				//根据选中的值-动态的设置URL
				navigationURL : url,
				//获取当前登录的企业ID
				businessID : currentBusinessID,
		};
		return businessNavigation;
	};
	
	/**
	 * 添加企业快速导航函数
	 */
	addNavigation = function(navigation){
		 $.ajax({
	            url: portal.HTTP_PREFIX + "/business/addNavigation",
	            type: "POST",
	            dataType: "json",
	            async: false,
	            contentType: "application/json; charset=utf-8",
	            data: JSON.stringify(navigation),
	            success: function(rusult) {
	            	//添加成功，生成超链接，超链接可删除
	            	if(rusult.newNavigation != null){
	            		var na = rusult.newNavigation;
	            		var returnSmallOption = na.smallOption;
	            		var returnUrl = na.navigationURL;
	            		var returnId = na.id;
	            		createButton(returnSmallOption,returnUrl,returnId);
	            	}
	            }
	        });
	};
	
	/**
	 * 在页面动态的创建超链接
	 */
	var createButton = function(smallOptionName,url,id){
		var btn = $("<div class='item' id='item_"+id+"'><li>" +
				"<a id='speed' href='"+url+"'>"+smallOptionName+"</a></li>" +
				"<span class='close' onclick='return deleteNavigation("+id+");'></span>" +
				"</div>");
        $("#sortable").append(btn);
	};
	
	 deleteNavigation = function(navigationId){
		$.ajax({
			// /fsn-core/service/business/deleteNavigation/43"
            url: portal.HTTP_PREFIX + "/business/deleteNavigation/" + navigationId,
            type: "DELETE",
            dataType: "json",
            async: false,
            contentType: "application/json; charset=utf-8",
            success: function(returnValue) {
            	if(returnValue.data == true){
            		fsn.initNotificationMes("删除成功", true);
            		$("#item_"+navigationId).remove();
            	}else {
            		fsn.initNotificationMes("删除失败", false);
				}
            }
        });
	};
	
	
	
	
	
	
	
	
	
	
	navigation.initialize();
});