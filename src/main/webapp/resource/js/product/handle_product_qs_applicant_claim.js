$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var upload = fsn.upload = fsn.upload || {};
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    
	portal.initialize = function(){
		upload.buildGridWioutToolBar("qs_applicant_claim_not_process_grid", portal.templatecolumns, portal.templateDS, 350);
		upload.buildGridWioutToolBar("qs_applicant_claim_back_grid", portal.templatecolumns, portal.backDS, 350);
		upload.buildGridWioutToolBar("qs_applicant_claim_pass_grid", portal.templatecolumns, portal.passDS, 350);
		
		fsn.initKendoWindow("process_qs_applicant_window","企业申请处理","1300px","400px",false, ["Close"]);
		fsn.initKendoWindow("send_back_warn_window","退回原因","400px","240px",false, ["Close"]);
		
		$("#send_back_yes_btn").bind("click", portal.back_yes);
        $("#send_back_no_btn").bind("click", portal.back_no);
	};
	
	portal.templatecolumns = [
			{ field: "qsno", title:"生产许可证编号", width: 35 , filterable: false},
			{ field: "bussinessName",title: "申请企业",width: 40, filterable: false},
			{ field: "applicant", title:"申请认领人",width: 30, filterable: false},
			{ field: "applicant_time", title:"申请认领时间", width: 35, filterable: false},
			{ field: "handle_result", title:"系统处理状态", width: 25, filterable: false, template: function(dataItem) {
			    var statue = "";
			    switch (dataItem.handle_result){
			        case 1: statue = "待处理" ;break;
			        case 2: statue = "审核通过" ;break;
			        case 4: statue = "审核退回" ;break;
			    }
				  return statue;                    		 
			}},
			{ field: "handle_time", title:"处理时间", filterable: false, width: 30},
			{ field: "note", title:"处理意见", width: 35, filterable: false, template: function(dataItem) {
			    var msg = "";
			    switch (dataItem.handle_result){
			        case 1: msg = "请您尽快处理！" ;break;
			        case 2: msg = "已经通过审核！" ;break;
			        case 4: msg = "申请被退回。原因：" + dataItem.note;break;
			    }
				  return msg;
			}},
			{width:20,title: "操作",
			      template:function(e){
			    	var tag = "";
		    		tag += "<a  onclick='return fsn.portal.openProcessWidow(" + e.handle_result + "," + e.id + "," + e.qsId +  "," + e.qsId_applicant + ",\"" + e.qsno + "\",\"" + e.note + "\",\"" + e.bussinessName + "\")' ";
		    		
		    		if(e.handle_result == 1){
		    			tag += "class='k-button'><span class='k-icon k-edit'></span>处理</a>";
		    		}else{
		    			tag += "class='k-button'><span class='k-icon k-i-search k-search'></span>预览</a>";
		    		}
		    		
					return tag;
			      }
		    }];
	
	portal.templateDS = new kendo.data.DataSource({
   			transport: {
   	            read : {
   	                type : "GET",
   	                async:false,
   	                url : function(options){
   	                	return portal.HTTP_PREFIX + "/product/qsno/applicantClaimHandle/list/NotProcess/" + options.page + "/" + options.pageSize;
   	                },
   	                dataType : "json",
   	                contentType : "application/json"
   	            },
   	        },
   	        schema: {
   	        	 data : function(returnValue) {
   	        		 return returnValue.data.list;
   	             },
   	             total : function(returnValue) {
   	            	 return returnValue.data.counts;
   	             }
   	        },
   	        batch : true,
   	        page:1,
   	        pageSize : 5,
   	        serverPaging : true,
   	        serverFiltering : true,
   	        serverSorting : true
   		});
	
		portal.backDS = new kendo.data.DataSource({
	 			transport: {
	 	            read : {
	 	                type : "GET",
	 	                async:false,
	 	                url : function(options){
	 	                	return portal.HTTP_PREFIX + "/product/qsno/applicantClaimHandle/list/back/" + options.page + "/" + options.pageSize;
	 	                },
	 	                dataType : "json",
	 	                contentType : "application/json"
	 	            },
	 	        },
	 	        schema: {
	 	        	 data : function(returnValue) {
	 	        		 return returnValue.data.list;
	 	             },
	 	             total : function(returnValue) {
	 	            	 return returnValue.data.counts;
	 	             }
	 	        },
	 	        batch : true,
	 	        page:1,
	 	        pageSize : 5,
	 	        serverPaging : true,
	 	        serverFiltering : true,
	 	        serverSorting : true
	 		});
		
		portal.passDS = new kendo.data.DataSource({
 			transport: {
 	            read : {
 	                type : "GET",
 	                async:false,
 	                url : function(options){
 	                	return portal.HTTP_PREFIX + "/product/qsno/applicantClaimHandle/list/pass/" + options.page + "/" + options.pageSize;
 	                },
 	                dataType : "json",
 	                contentType : "application/json"
 	            },
 	        },
 	        schema: {
 	        	 data : function(returnValue) {
 	        		 return returnValue.data.list;
 	             },
 	             total : function(returnValue) {
 	            	 return returnValue.data.counts;
 	             }
 	        },
 	        batch : true,
 	        page:1,
 	        pageSize : 5,
 	        serverPaging : true,
 	        serverFiltering : true,
 	        serverSorting : true
 		});
	
	/**
	 * 背景：点击企业申请处理操作
	 * 功能描述：打开处理窗口
	 * @author ZhangHui 2015/6/2
	 */
	portal.openProcessWidow = function(handle_result, current_process_applicant_claim_id, current_process_qs_id, qsId_applicant, qsno, back_msg, applicant_bus_name){
		portal.current_process_applicant_claim_id = current_process_applicant_claim_id; // 记录当前正在处理的企业认领申请记录id
		portal.current_process_qs_id = current_process_qs_id; // 记录原生产许可证信息的qsId（审核通过后，此qs信息会被申请时qs信息覆盖）
		portal.applicant_bus_name = applicant_bus_name;       // 记录申请企业名称
		
		var process_qsInfo = null;
		$.ajax({
			url:portal.HTTP_PREFIX + "/product/qsno/applicantClaimHandle/findApplicantQsInfo/" + qsId_applicant,
			type:"GET",
	        async:false,
			success:function(returnValue){
				if(returnValue.result.success){
					process_qsInfo = returnValue.data;
				}
			}
		});
		
		if(!process_qsInfo){
			fsn.initNotificationMes("未找到qs号信息！", false);
			return;
		}
		
        $("#qsNo").text(qsno);
        $("#busunitName").text(process_qsInfo.busunitName);
        $("#productName").text(process_qsInfo.productName);
        $("#qsStartTime").text(process_qsInfo.startTime==null?"":process_qsInfo.startTime.substr(0, 10));
        $("#qsEndTime").text(process_qsInfo.endTime==null?"":process_qsInfo.endTime.substr(0, 10));
        
        setAddressValueWithHtmlType(process_qsInfo.accommodation, process_qsInfo.accOtherAddress, "accommodation_mainAddr", "accommodation_streetAddress", "text");
        setAddressValueWithHtmlType(process_qsInfo.productionAddress, process_qsInfo.proOtherAddress, "qs_mainAddr", "qs_streetAddress", "text");
        
        $("#checkType").text(process_qsInfo.checkType);
        portal.setQsAttachments(process_qsInfo.qsAttachments);
		
        $("#back_msg_div").hide();
        
        if(handle_result == 1){
        	$("#float_banner").show();
        }else{
        	$("#float_banner").hide();
        	
        	if(handle_result == 4){
        		$("#back_msg_div").show();
        		$("#back_msg_div").html("退回原因：" + back_msg);
        	}
        }
        
		$("#process_qs_applicant_window").data("kendoWindow").open().center();
	};
	
	portal.setQsAttachments = function(qsAttachments){
		 var dataSource = new kendo.data.DataSource();
		 if(qsAttachments.length>0){
			 for(var i=0;i<qsAttachments.length;i++){
				 dataSource.add({attachments:qsAttachments[i]});
			 }
		 }
		 $("#qsAttachmentsListView").kendoListView({
			 dataSource: dataSource,
			 template:kendo.template($("#uploadedFilesTemplate").html()),
		 });
	};
	
	portal.operate = function(operate){
		if(operate == "pass"){
			portal.executeProcess(true);
			return;
		}
		
		if(operate == "notpass"){
			portal.clearBackWin();
			
			$("#send_back_warn_window").data("kendoWindow").open().center();
			return;
		}
	};
	
	/**
	 * 功能描述：企业申请认领生产许可证，审核通过/退回
	 * @author ZhangHui 2015/6/2
	 */
	portal.executeProcess = function(pass){
		var url = portal.HTTP_PREFIX + "/product/qsno/applicantClaimHandle/process/" + portal.current_process_applicant_claim_id + "/" 
						+ portal.current_process_qs_id + "/" + pass + "/" + portal.applicant_bus_name;
		if(!pass){
			url += "?back_msg=" + (portal.send_back_msg?encodeURI(encodeURI(portal.send_back_msg)):"");
		}
		
 		$.ajax({
 	        url: url,
 	        type: "GET",
 	        dataType: "json",
 	        contentType: "application/json; charset=utf-8",
 	        success: function(returnValue) {
 	            if (returnValue.result.success) {
 	            	portal.templateDS.read();
 	            	portal.backDS.read();
 	            	
 	            	if(pass){
 	            		portal.passDS.read();
 	            	}
 	            	
 	            	fsn.initNotificationMes("审核" + (pass?"通过":"退回") + "执行成功！", true);
 	            }else{
 	            	fsn.initNotificationMes("审核" + (pass?"通过":"退回") + "执行失败！", false);
 	            }
 	            
 	            $("#send_back_warn_window").data("kendoWindow").close();
 	   			$("#process_qs_applicant_window").data("kendoWindow").close();
 	        }
 	    });
	};
	
	/**
 	 * 点击退回提示框的确定按钮
 	 * @author ZhangHui 2015/6/2
 	 */
 	portal.back_yes = function(){
 		// 获取退回原因msg
 		var str = document.getElementsByName("send_back_msg");
 		var objarray = str.length;
 		portal.send_back_msg = "";
 		for (var i=0; i<objarray; i++){
 			if(str[i].checked == true){
 				portal.send_back_msg += str[i].value+";";
 			}
 		}
 		portal.send_back_msg += $("#send_back_msg_other").val().trim();
 		if(portal.send_back_msg == ""){
 			fsn.initNotificationMes("请选择退回原因，或者填写其他原因！", false);
 			return;
 		}
 		
 		portal.executeProcess(false);
 	};
 	
 	/**
 	 * 点击退回提示框的取消按钮
 	 * @author zhangHui 2015/6/2
 	 */
 	portal.back_no = function(){
 		$("#send_back_warn_window").data("kendoWindow").close();
 		$("#process_qs_applicant_window").data("kendoWindow").close();
 	};
 	
 	/**
 	 * 清空退回窗口
 	 */
 	portal.clearBackWin = function(){
 		$("#send_back_msg_other").val("");
 		var inputs = document.getElementsByName("send_back_msg");   
 		for(var i=0;i<inputs.length;i++){   
 		    inputs[i].checked = false;   
 		}   
 		$("#send_back_report_warn_window input").val('');
 	};
	
	portal.initialize();
});