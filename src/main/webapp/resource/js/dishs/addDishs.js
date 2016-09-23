$(function(){
  	var fsn = window.fsn = window.fsn || {};
    var dishsno= fsn.dishsno = fsn.dishsno ||{};
    var portal = fsn.portal = fsn.portal ||{}; // portal命名空间
    var upload = window.fsn.upload = window.fsn.upload ||{};
    var ID = null;
    var qiyeId = null;
    portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    dishsno.id = null;
    dishsno.name=null;
	dishsno.preview_id=null;//查看详细businessId
    dishsno.dishsNofiles = new Array();
	
	dishsno.initialize = function(){
		/* 初始化上传控件 */
    	$("#upload_distribution_div").html("<input id='upload_distribution_files' type='file' />");
    	upload.buildUpload("upload_distribution_files",dishsno.dishsNofiles,"dishsnoDistribution","上传菜品图片");
    	$("#btn_clearDishsnoFiles").bind("click",dishsno.clearDishsNoFiles);
    	$("#btn_clearDishsnoFiles").hide();
		lookLoadDishsNo();
    	dishsno.initKendoWindow("k_window","保存状态","300px","60px",false);
    	/* 保存 */
    	$("#save").bind("click", dishsno.save);
	};
	dishsno.initKendoWindow=function(winId,title,width,height,isVisible){
    	$("#"+winId).kendoWindow({
			title:title,
			width:width,
			height:height,
			modal:true,
			visible:isVisible,
			actions:["Close"]
		});
    };
    
	function lookLoadDishsNo(){
    	var haveId = window.location.href;
    	if(haveId.indexOf("id=")>0){
    		var id = haveId.split("id=")[1];
    		$.ajax({
    			url:portal.HTTP_PREFIX  + "/dishsNo/findDishsNo/"+id,
    			type:"GET",
    			dataType: "json",
    			async:false,
    			success:function(returnValue){
    				$("#dishsName").val(returnValue.data.dishsName);
    				$("#baching").val(returnValue.data.baching);
    				$("#alias").val(returnValue.data.alias);
    				$("#remark").val(returnValue.data.remark);
//    				var _showFlag = $("input[name='showFlag']");
//    				for(var k in _showFlag){
//    					if(_showFlag[k].value==returnValue.data.showFlag){
//    						_showFlag[k].checked=true;
//    						break;
//    					}
//    				}
    				ID = returnValue.data.id;
    				qiyeId = returnValue.data.qiyeId;
    				upload.setAttachments(returnValue.data.dishsnoFile,"btn_clearDishsnoFiles","disDishsNOFileListView","uploadedFilesTemplate");
    			}
    		});
    	}else{
    		$("#dishsName").val();
    		$("#baching").val();
    		$("#alias").val();
    		//默认值为显示菜品信息为（是 值 为 1）
//    		var showFlag = $("input[name='showFlag']");
//       		for(var k in showFlag){
//    			if(showFlag[k].value==1){
//    				showFlag[k].checked = true;
//    			}
//    		}
    	};
    }
    dishsno.removeRes = function(id){
    	upload.removeRes(dishsno.dishsNofiles,id,"disDishsNOFileListView","btn_clearDishsnoFiles");
    };
   	dishsno.goBack = function(){
   		window.location.href="dishsno_prepackNo.html";
   	};
   	
   	dishsno.createInstance = function(){
//   		var showFlag = $("input[name='showFlag']");
//		var showValue = "1";
//   		for(var k in showFlag){
//			if(showFlag[k].checked==true){
//				showValue = showFlag[k].value;
//				break;
//			}
//		}
   		var distribution = {
   			 id:ID,
   			 dishsName:$("#dishsName").val().trim(),
   			 baching:$("#baching").val().trim(),
   			 alias:$("#alias").val().trim(),
   			 dishsnoFile:dishsno.dishsNofiles,
   			 qiyeId:qiyeId,
   			 remark:$("#remark").val().trim()
//   		     showFlag:showValue
   		};
   		return distribution;
   	};
   	 dishsno.goBack = function(){
   		window.location.href="dishs_prepackNo.html";
   	};
   	/* 保存 */
   dishsno.save = function(){
    	 // 1.校验数据的有效性
    	fsn.clearErrorStyle();
    	if(!dishsno.validateFormat()) return;
    	
    	// 2.数据封装
    	var subBusiness = dishsno.createInstance();
    	 // 3.保存
        $("#winMsg").html("正在保存数据，请稍候....");
        $("#k_window").data("kendoWindow").open().center();
        
        $.ajax({
            url: portal.HTTP_PREFIX + "/dishsNo/dishsNoSave",
            type: "PUT",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data:JSON.stringify(subBusiness),
            success: function(returnVal){
            	$("#k_window").data("kendoWindow").close();
            	if(returnVal.result.status == "true"){
			 		fsn.initNotificationMes("菜品信息保存成功", true);
	                window.setTimeout("window.location.href = 'dishs_prepackNo.html'", 1000); 
                }
                else {
			 		lims.initNotificationMes("菜品信息保存失败", false);
                }
            }
        });
        
    };
    
     /* 校验数据的有效性 */
   dishsno.validateFormat = function() {
    	if(!dishsno.validateMyDate()){ return false; }
        return true;
    };
    dishsno.validateMyDate = function(){
     	if(!$("#dishsName").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('菜品名称不能为空！',false);
    		return false;
    	}
    	if(!$("#alias").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('别名不能为空！',false);
    		return false;
    	}
    	if(!$("#baching").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('配料不能为空！',false);
    		return false;
    	}
		if(dishsno.dishsNofiles.length<1){
        	lims.initNotificationMes('请上传菜品图片！',false);
			dishsno.wrong("upload_distribution_files","select");
        	return false;
        }
        if(dishsno.dishsNofiles.length>3){
        	lims.initNotificationMes('菜品图片最多只能上传3张！',false);
			dishsno.wrong("upload_distribution_files","select");
        	return false;
        }
    	return true;
     };
     
     dishsno.wrong = function(id,type){
		if (type == "text") {
			$("#" + id).focus();
			$("#" + id).css("border-color", "red");
			$("#" + id).bind("click", function(){
				$("#" + id).css("border-color", "");
			});
		}else if(type == "select"){
			$("#" + id).focus();
		}
		//控制页面向上滑动10
		var scrollTop=document.body.scrollTop||document.documentElement.scrollTop ;
		document.documentElement.scrollTop = scrollTop - 150;
	};
	dishsno.initialize();
});
