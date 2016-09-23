$(function(){
  	var fsn = window.fsn = window.fsn || {};
    var business_unit = fsn.business_unit = fsn.business_unit ||{};
    var portal = fsn.portal = fsn.portal ||{}; // portal命名空间
    var ID = null;
    var qiyeId = null;
    portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    business_unit.id = null;
    business_unit.name=null;
    fsn.business_send = false;
	business_unit.preview_id=null;//查看详细businessId
    business_unit.aryDisAttachments = new Array();
	fsn.imgMax = 5;//页面引导图片id
	
	business_unit.initialize = function(){
		/* 初始化上传控件 */
    	$("#upload_distribution_div").html("<input id='upload_distribution_files' type='file' />");
    	business_unit.buildUpload("upload_distribution_files",business_unit.aryDisAttachments,"proFileMsgDistribution");
    	$("#btn_clearDisFiles").bind("click",business_unit.clearDisFiles);
    	$("#btn_clearDisFiles").hide();
		lookLoadOutBusInfo();
    	business_unit.initKendoWindow("k_window","保存状态","300px","60px",false);
    	/* 保存 */
    	$("#save").bind("click", business_unit.save);
	}
	function lookLoadOutBusInfo(){
    	var haveId = window.location.href;
    	if(haveId.indexOf("id=")>0){
    		var id = haveId.split("id=")[1];
    		$.ajax({
    			url:portal.HTTP_PREFIX  + "/wasteDisposa/findWasteDisposa/"+id,
    			type:"GET",
    			dataType: "json",
    			async:false,
    			success:function(returnValue){
    				$("#handler").val(returnValue.data.handler);
    				$("#handleTime").val(returnValue.data.handleTime);
    				$("#handleWay").val(returnValue.data.handleWay);
    				$("#handleNumber").val(returnValue.data.handleNumber);
    				$("#destory").val(returnValue.data.destory);
    				$("#participation").val(returnValue.data.participation);
    				ID = returnValue.data.id;
    				qiyeId = returnValue.data.qiyeId;
    				business_unit.setDisAttachments(returnValue.data.piceFile);
    			}
    		});
    	}else{
    		$("#handler").val();
    		$("#handleTime").val();
    		$("#handleWay").val();
    		$("#handleNumber").val();
    	};
    }
	 /* 初始化时间控件  */
	$("#handleTime").kendoDatePicker({
   	 format: "yyyy-MM-dd", 
   	 height:30,
   	 culture : "zh-CN",
   	 animation: {
   	   close: {
   	     effects: "fadeOut zoom:out",
   	     duration: 300
   	   },
   	   open: {
   	     effects: "fadeIn zoom:in",
   	     duration: 300
   	   }
   	  }
   	});
   	business_unit.goBack = function(){
   		window.location.href="waste_disposa_dealer.html";
   	}
   	business_unit.createInstance = function(){
   		var distribution = {
   			 id:ID,
   			 handler:$("#handler").val().trim(),
   			 handleTime:$("#handleTime").val().trim(),
   			 handleNumber:$("#handleNumber").val().trim(),
   			 handleWay:$("#handleWay").val().trim(),
   			 participation:$("#participation").val().trim(),
   			 destory:$("#destory").val().trim(),
   			 qiyeId:qiyeId,
   			 piceFile: business_unit.aryDisAttachments,
   		};
   		return distribution;
   	}
   	/* 保存 */
    business_unit.save = function(){
    	 // 1.校验数据的有效性
    	fsn.clearErrorStyle();
    	if(!business_unit.validateFormat()) return;
    	
    	// 2.数据封装
    	var subBusiness = business_unit.createInstance();
    	 // 3.保存
        $("#winMsg").html("正在保存数据，请稍候....");
        $("#k_window").data("kendoWindow").open().center();
        
        $.ajax({
            url: portal.HTTP_PREFIX + "/wasteDisposa/wasteSave",
            type: "PUT",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data:JSON.stringify(subBusiness),
            success: function(returnVal){
            	$("#k_window").data("kendoWindow").close();
            	if(returnVal.result.status == "true"){
			 		fsn.initNotificationMes("废弃物信息保存成功", true);
	                window.setTimeout("window.location.href = 'waste_disposa_dealer.html'", 2000); 
                }
                else {
			 		lims.initNotificationMes("废弃物信息保存失败", false);
                }
            }
        });
        
    }
    
    /*
     * 格式化时间控件获取的时间*/
    function FormatDate (strTime) {
        var date = new Date(strTime);
        var dateee = date.format("YYYY-MM-dd");
        return dateee;//date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
    }
    
     /* 校验数据的有效性 */
    business_unit.validateFormat = function() {
    	if(!business_unit.validateMyDate()){ return false; }
        return true;
    };
    business_unit.validateMyDate = function(){
     	if(!$("#handler").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('【废弃物处理】中的处理人名称不能为空！',false);
    		return false;
    	}
    	if(!$("#handleNumber").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('【废弃物处理】中的处理数量名称不能为空！',false);
    		return false;
    	}
    	if(!$("#handleWay").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('【废弃物处理】中的处理方式名称不能为空！',false);
    		return false;
    	}
    	if(!$("#participation").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('【废弃物处理】中的参与单位名称不能为空！',false);
    		return false;
    	}
    	if(!$("#destory").kendoValidator().data("kendoValidator").validate()){
    		fsn.initNotificationMes('【废弃物处理】中的销毁地点名称不能为空！',false);
    		return false;
    	}
    	if(!fsn.validateMustDate("handleTime","处理时间不能为空")){return false;}
    	if(fsn.validateDateFormat("handleTime","处理时间的日期格式不对")){fsn.initNotificationMes('【废弃物处理】中的处理时间的日期格式不对！',false); return false;}
    	if( business_unit.aryDisAttachments.length<1){
        	lims.initNotificationMes('请上传废弃物处理图片！',false);
			business_unit.wrong("upload_distribution_files","select");
        	return false;
        }
        if(business_unit.aryDisAttachments.length>3){
        	lims.initNotificationMes('废弃物处理图片最多只能上传3张！',false);
			business_unit.wrong("upload_distribution_files","select");
        	return false;
        }
    	return true;
     }
	business_unit.initialize();
})
