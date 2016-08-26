$(function(){
	var lims = window.lims = window.lims || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
    var upload = fsn.upload = fsn.upload || {};
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	fsn.initialize=function(){
		fsn.initComponentCommon();
		// 初始化待完善报告Grid
		upload.buildGridByBoolbar("report_no_perfect_grid", fsn.reportColumn, fsn.reportNoDS, "350px", "");
		// 初始化已退回报告Grid
		upload.buildGridByBoolbar("send_back_report_grid", fsn.reportBackColumn, fsn.reportBackDS, "350px", "");
		// 初始化已完善报告Grid
		upload.buildGridByBoolbar("report_yes_perfect_grid", fsn.reportYesColumn, fsn.reportYesDS, "350px", "");
		
		lims.initEasyItemGrid("testItem_grid");
		
		fsn.initKendoWindow("send_back_report_warn_window","退回原因","950px","690px",false,null);
		$("#send_back_report_yes_btn").bind("click", portal.back_yes);
        $("#send_back_report_no_btn").bind("click", portal.back_no);
	};
	fsn.reportNoDS = new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		var configure = "0";
					if(options.filter!=undefined){
					   configure = filter.configure(options.filter);
					}
            		return portal.HTTP_PREFIX + "/report/getToBeStructured/" + options.page + "/" + options.pageSize + "/" + configure;
            	},
                dataType : "json",
                contentType : "application/json"
            }
        },
        batch : true,
        page:1,
        pageSize: 5,
        schema: {
            data : function(returnValue) {
                return returnValue.data;
            },
            total : function(returnValue) {       
                return returnValue.counts;
            }     
        },
        change: function(e) {
            
        },
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	
	fsn.reportBackDS = new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		var configure = "0";
            		if(options.filter!=undefined){
 					   configure = filter.configure(options.filter);
 					}
            		return portal.HTTP_PREFIX + "/report/getBackStructureds/" + options.page + "/" + options.pageSize + "/" + configure;
            	},
                dataType : "json",
                contentType : "application/json"
            }
        },
        batch : true,
        page:1,
        pageSize: 5,
        schema: {
            data : function(returnValue) {
                return returnValue.data;
            },
            total : function(returnValue) {       
                return returnValue.counts;
            }     
        },
        change: function(e) {
            
        },
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	
	fsn.reportYesDS = new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		var configure = "0";
            		if(options.filter!=undefined){
 					   configure = filter.configure(options.filter);
 					}
            		return portal.HTTP_PREFIX + "/report/getHasStructureds/" + options.page + "/" + options.pageSize + "/" + configure;
            	},
                dataType : "json",
                contentType : "application/json"
            }
        },
        batch : true,
        page:1,
        pageSize: 5,
        schema: {
            data : function(returnValue) {
            	for(var i=0;i<returnValue.data.length;i++){
            		returnValue.data[i].status=returnValue.data[i].status==2?"审核中":"已发布";
            	}
                return returnValue.data;
            },
            total : function(returnValue) {       
                return returnValue.counts;
            }     
        },
        change: function(e) {
            
        },
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	
	fsn.reportColumn = [
           { field: "id", title:"ID", width: 35 , filterable: false},
           { field: "serviceOrder", title:"报告编号", width: 70 , filterable: true},
           { field: "proName", title:"食品名称",width: 70 , filterable: true},
           { field: "producerName", title:"生产企业名称",width: 70 , filterable: true},
           { field: "barcode", title:"条形码",width: 70 , filterable: true},
           { field: "batchSerialNo", title:"批次", width: 50, filterable: false},
           { field: "lastModifyUserName",title:"最后更新者",width:45, filterable: false},
           { field: "lastModifyTime",title:"最后更新时间",width:55,template:'#= new Date(lastModifyTime).format("YYYY-MM-dd hh:mm:ss")#', filterable: false},
               { command: [{name:"edit",
               	    text:"<span class='k-edit'></span>" + "编辑", 
               	    click:function(e){
               	    	var url = '/fsn-core/views/market/add_testreport.html';
               	    	fsn.edit(this.dataItem($(e.currentTarget).closest("tr")), url, "manage_report_perfect_shianyun");
                 }},
                 {name:"review",
               	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
               	    click:function(e){
               	    	e.preventDefault();
               	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));
               	    	viewReportInfo(currentItem.id);
                 }},
                 {   name:"Return",
              	   text:"<span class='k-icon k-cancel'></span>"+fsn.l("Return"),  
              	   click:function(e){
              		      e.preventDefault();
							  var ds = this;
							  var tr = $(e.target).closest("tr");
							  fsn.returnData=ds.dataItem(tr);
                      		   fsn.operate_report_id = fsn.returnData.id;
							  // 清空上次的退回原因
							  $("#send_back_msg_other").val("");
							  var inputs = document.getElementsByName("send_back_msg");   
							  for(var i=0;i<inputs.length;i++){   
						 		    inputs[i].checked = false;   
							  }
							  $("#send_back_report_warn_window input").val('');
							  $("#status").val("1");
							  // 打开退回window
							  $("#send_back_report_warn_window").data("kendoWindow").open().center();
              	   }
                 },
                 ], title: "操作", width: 120 }];
	
	fsn.reportBackColumn = [
	                    { field: "id", title:"ID", width: 35 , filterable: false},
	                    { field: "serviceOrder", title:"报告编号", width: 70 , filterable: true},
	                    { field: "proName", title:"食品名称",width: 70 , filterable: true},
	                    { field: "producerName", title:"生产企业名称",width: 70 , filterable: true},
	                    { field: "barcode", title:"条形码",width: 70 , filterable: true},
	                    { field: "batchSerialNo", title:"批次", width: 50, filterable: false},
	                    { field: "lastModifyUserName",title:"最后更新者",width:45, filterable: false},
	                    { field: "lastModifyTime",title:"最后更新时间",width:55,template:'#= new Date(lastModifyTime).format("YYYY-MM-dd hh:mm:ss")#', filterable: false},
	                        { command: [{name:"edit",
	                        	    text:"<span class='k-edit'></span>" + "编辑", 
	                        	    click:function(e){
	                        	    	var url = '/fsn-core/views/market/add_testreport.html';
	                        	    	fsn.edit(this.dataItem($(e.currentTarget).closest("tr")), url, "manage_report_perfect_shianyun");
	                          }},
	                          {name:"review",
	                        	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
	                        	    click:function(e){
	                        	    	e.preventDefault();
	                        	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));
	                        	    	viewReportInfo(currentItem.id);
	                          }},
	                          {name:"Return",
	                        	  text:"<span class='k-icon k-cancel'></span>"+fsn.l("Return"),  
	                       	   click:function(e){
	                       		   e.preventDefault();
	                       		   var currentItem = this.dataItem($(e.currentTarget).closest("tr"));
	                       		   fsn.operate_report_id = currentItem.id;
	                       		 
	                       		   // 清空上次的退回原因
	                       		   $("#send_back_msg_other").val("");
								   var inputs = document.getElementsByName("send_back_msg");   
								   for(var i=0;i<inputs.length;i++){   
								 		 inputs[i].checked = false;   
								   }
								   $("#send_back_report_warn_window input").val('');
								   $("#status").val("4");
									// 打开退回window
	                       		   $("#send_back_report_warn_window").data("kendoWindow").open().center();
	         				}},
	                          ], title: "操作", width: 120 }];
	
	fsn.reportYesColumn = [
	                    { field: "id", title:"ID", width: 35 , filterable: false},
	                    { field: "serviceOrder", title:"报告编号", width: 70 , filterable: true},
	                    { field: "proName", title:"食品名称",width: 70 , filterable: true},
	                    { field: "producerName", title:"生产企业名称",width: 70 , filterable: true},
	                    { field: "barcode", title:"条形码",width: 70 , filterable: true},
	                    { field: "batchSerialNo", title:"批次", width: 50, filterable: false},
	                    { field: "lastModifyUserName",title:"最后更新者",width:45, filterable: false},
	                    { field: "lastModifyTime",title:"最后更新时间",width:55,template:'#= new Date(lastModifyTime).format("YYYY-MM-dd hh:mm:ss")#', filterable: false},
	                    { field: "status",title:"审核状态",width:45,
	                    	filterable: {
	                    		ui: function(element) {
	                    	        element.kendoDropDownList({
	                    	        	dataSource:[{name:'审核中',id:2},{name:'已发布',id:8}],
	                    	        	dataTextField:"name",
	                    	        	dataValueField:"id"
	                    	        }); 
	                    	      }
	                        }
	                    },
	                    { command: [
	                          {name:"review",
	                        	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
	                        	    click:function(e){
	                        	    	e.preventDefault();
			                   	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));
			                   	    	viewReportInfo(currentItem.id);
	                          }},
	                          ], title: "操作", width: 80 }];
	
	/**
 	 * 点击退回提示框的确定按钮
 	 * @author ZhangHui 2015/5/5
 	 */
 	portal.back_yes = function(){
 		var  status = $("#status").val()
 		// 获取退回原因msg
 		var send_back_msg = "";
 		var index=1;
 		$("input[name='send_back_msg']:checked").each(function(i){
 			index=i+1;
 			send_back_msg+=index+"、"+$(this).attr('alt');
 			if($(this).next().is("input")||$(this).next().is("select")){
 				send_back_msg+=":"+$(this).next().val()+";";
 			}
 		});
 		if($.trim($("#send_back_msg_other").val())!=""){
 			++index;
 			send_back_msg+=index+":"+$.trim($("#send_back_msg_other").val());
 		}
 		if(send_back_msg == ""){
 			fsn.initNotificationMes("请选择退回原因，或者填写其他原因！", false);
 			return;
 		}
 		
 		var data = {"returnMes":encodeURIComponent(send_back_msg),repBackAttachments:[],status:status};
 		
 		// 将报告直接退回至供应商
 		$.ajax({
 	        url: portal.HTTP_PREFIX + "/test/sendBack/structured/" + fsn.operate_report_id + "/true",
 	        type: "POST",
 	        dataType: "json",
 	        contentType: "application/json; charset=utf-8",
 	        data: JSON.stringify(data),
 	        success: function(returnValue) {
 	        	// 关闭退回原因window
				$("#send_back_report_warn_window").data("kendoWindow").close();
				
 	            if (returnValue.result.status == "true") {
 	                fsn.reportBackDS.read();
 	                fsn.reportNoDS.read();
 	            	fsn.initNotificationMes("ID=" + fsn.operate_report_id + ",退回成功！", true);
 	            }else {
 	            	fsn.initNotificationMes("ID=" + fsn.operate_report_id + ",退回失败", false);
 	            }
 	        }
 	    });
 	};
 	
 	/**
 	 * 点击退回提示框的取消按钮
 	 * @author zhangHui 2015/5/5
 	 */
 	portal.back_no = function(){
 		$("#send_back_report_warn_window").data("kendoWindow").close();
 	};
 	/**
 	 * 导出excel文档
 	 */
 	portal.exportExel_Win = function(isStructured){
 		var data = $("#testItem_grid").data("kendoGrid").dataSource.data();
 		 if(data.length>0){
 			var orderNo = $("#tri_reportNo").val().trim();
 			var barcodeId = $("#barcodeId").val().trim();
 			var testResultId = data[0].testResultId;
 			$.ajax({
 				url:portal.HTTP_PREFIX +"/tempReport/exportExcel",
 				type:"GET",
 				dataType:"json",
 				contentType: "application/json; charset=utf-8",
 				data:{"orderNo":orderNo,"barcode":barcodeId,"isStructured":isStructured,"testResultId":testResultId},
 				success:function(data){
 					if(data.status=="true"){
 						var hiddenIFrameID = 'hiddenDownloader',
 			            iframe = document.getElementById(hiddenIFrameID);
 			            if(iframe === null) {
 			                iframe = document.createElement('iframe');
 			                iframe.id = hiddenIFrameID;
 			                iframe.style.display = 'none';
 			                document.body.appendChild(iframe);
 			            }
 			            iframe.src = portal.HTTP_PREFIX +"/tempReport/downLoadExcel";
 					}else{
 						 lims.initNotificationMes(lims.l("您还没有保存该检测项目或者系统异常!") + "!", false);
 					}
 				}
 			});
 		 }else{
 			 lims.initNotificationMes(lims.l("目前没有检测项目!") + "!", false); 
 		 }
 	};
 	
	fsn.initialize();
});
