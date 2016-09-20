$(function(){
	var lims = window.lims = window.lims || {};
	var root = window.lims.root = window.lims.root || {};
	var filter = window.filter = window.filter || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	root.initialize=function(){
		//SSO Integration 1
		lims.uploadSSOJS();
		root.buildGrid("report_publish_grid", root.reportDS, "toolbar_template", root.column);
		root.buildGrid("was_publish_grid_", root.publishedReportDS, "empty", root.hasPubColumn);
		root.buildGrid("report_back_eidt_grid", root.backReportDS, "toolbar_template_back", root.backColumn);
		lims.initEasyItemGrid("testItem_grid");
		
		root.current_bus = getCurrentBusiness();
	};
	
	root.reportDS = new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
					if(options.filter==null){
						options.filter={};
						options.filter.filters=[];
					}
					options.filter.filters.push({"field":"testType","operator":"eq","value":"第三方检测"});
            		var configure = filter.configure(options.filter);
					configure = encodeURIComponent(configure);
            		return portal.HTTP_PREFIX + "/testReport/canPublished/" + options.page + "/" + options.pageSize + "/" + configure + "/3?isThird=true";
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
            	for(var i=0; i<returnValue.data.listOfReport.length; i++){
            		/*returnValue.data.listOfReport[i].status = "未发布";*/
            		if(returnValue.data.listOfReport[i].signFlag){
            			returnValue.data.listOfReport[i].signFlag = "已签名";
            		}else{
            			returnValue.data.listOfReport[i].signFlag = "未签名";
            		}
            	}
                return returnValue.data.listOfReport;
            },
            total : function(returnValue) {       
                return returnValue.data.counts;
            }     
        },
        change: function(e) {
            
          },
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	
	root.backReportDS = new kendo.data.DataSource({
		transport: {
            read: {
            	async:false,
            	url : function(options){
					if(options.filter==null){
						options.filter={};
						options.filter.filters=[];
					}
					options.filter.filters.push({"field":"testType","operator":"eq","value":"第三方检测"});
            		var configure = filter.configure(options.filter);
					configure = encodeURIComponent(configure);
            		return portal.HTTP_PREFIX + "/testReport/getAllBackRep/" + options.page + "/" + options.pageSize + "/" + configure + "/true?isThird=true";
            	},
                dataType : "json",
                contentType : "application/json;charset=utf-8",
            }
        },
        batch : true,
        page:1,
        pageSize: 5,
        schema: {
            data : function(returnValue) {
            	for(var i=0; i<returnValue.data.listOfReport.length; i++){
            		//returnValue.data.listOfReport[i].status = "已退回";
            		if(returnValue.data.listOfReport[i].signFlag){
            			returnValue.data.listOfReport[i].signFlag = "已签名";
            		}else{
            			returnValue.data.listOfReport[i].signFlag = "未签名";
            		}
            	}
                return returnValue.data.listOfReport;
            },
            total : function(returnValue) {       
                return returnValue.data.counts;
            }     
        },
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	
	root.publishedReportDS= new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
					if(options.filter==null){
						options.filter={};
						options.filter.filters=[];
					}
					options.filter.filters.push({"field":"testType","operator":"eq","value":"第三方检测"});
            		var configure = filter.configure(options.filter);
					configure = encodeURIComponent(configure);
            		return portal.HTTP_PREFIX + "/testReport/byuser/" + options.page + "/" + options.pageSize+"/1/"+ configure + "/true?isThird=true";
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
            	for(var i=0; i<returnValue.data.listOfReport.length; i++){
            		returnValue.data.listOfReport[i].status = "已发布";
            	}
                return returnValue.data.listOfReport;
            },
            total : function(returnValue) {       
                return returnValue.data.counts;
            }     
        },
        change: function(e) {
            
          },
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	
	root.buildGrid = function (id,ds,tmpBar,columns,isBack){
		var element = $("#" + id).kendoGrid({
			dataSource: ds == undefined ? []:ds,
			toolbar: [
			    	   {template: kendo.template($("#"+tmpBar).html())}
			    	 ],
			height: 300,
			width: 1000,
			filterable: {
				extra: false,
				messages: lims.gridfilterMessage(),
			 	operators: {
			      string: lims.gridfilterOperators(),
			      date: lims.gridfilterOperatorsDate(),
			      number: lims.gridfilterOperatorsNumber()
			    }
		    },
	        sortable: true,
	        selectable: true,
	        resizable: true,
	        pageable: {
	            refresh: true,
	            pageSizes: 5,
	            buttonCount:5,
	            messages: lims.gridPageMessage(),
	        },
	        columns: columns
		});
	};
	
	root.hasPubColumn = [	
	                       { field: "id", title:"ID", width: 35 },
		                   { field: "serviceOrder", title:"报告编号", width: 70 },
		                   { field: "sample.product.name", title:"食品名称",width: 70 },
		                   { field: "sample.productionDate", title:"生产日期", width: 50,template: '#=sample.productionDate!=null?sample.productionDate.substr(0,10):"0000-00-00"#', filterable: false},
		                   { field: "sample.batchSerialNo", title:"批次", width: 60},
		                   { field: "testOrgnization", title:"检验机关", width: 70},
		                   { field: "receiveDate", title:"发布时间", width: 55, filterable: false},
		                   { field: "status", title:"状态", width: 30, filterable: false, template: function(dataItem) {
                                   var reportStatue="";
                                   switch (dataItem.publishFlag){
                                       case "1": reportStatue = "已发布" ;break;
                                       default: reportStatue = "审核中" ;
                                   }
		                    		return "<strong style='color:#006536;'>" + reportStatue + "</strong>";                    		 
		                     }},
		                   { command: [
		                     {name:"review",
		                   	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
		                   	    click:function(e){
		                   	    	e.preventDefault();
		                   	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));
		                   	    	viewReportInfo(currentItem.id);
		                   	    },
		                   	    	},], title: "操作", width: 40 }];
	
	root.column = [	
                   { field: "id", title:"ID", width: 20 },
                   { field: "serviceOrder", title:"报告编号", width: 50 },
                   { field: "sample.product.name", title:"食品名称",width: 50},
                   { field: "sample.batchSerialNo", title:"批次", width: 40},
                   { field: "testType", title:"检测类型", width: 40, template: function(dataItem) {
                    	 if(dataItem.signFlag=="未签名" && dataItem.testType=="企业自检"){
                      		 return "<strong style='color:#DB7721;'>" + dataItem.testType + "</strong>";
                    	 }else{
                    		 return dataItem.testType;               			 
                    	 }
                     }},
                   { field: "lastModifyUserName",title:"创建者",width:40},
                   { field: "lastModifyTime",title:"最后更新时间",width:38,template: '#=lastModifyTime=fsn.formatGridDate(lastModifyTime)#', filterable: false},
                   { field: "tips",title:"消息提示",width:60},
                   { field: "signFlag", title:"签名状态", width: 25, filterable: false, template: function(dataItem) {
                  	 if(dataItem.signFlag=="已签名"){
                  		 return "<strong style='color:red;'>" + dataItem.signFlag + "</strong>";
                	 }else if(dataItem.testType=="企业自检"){
                		 return "<strong style='color:#DB7721;'>" + dataItem.signFlag + "</strong>";             		 
                	 }else{
                		 return dataItem.signFlag;
                	 }
                 }},
                   { command: [
                   	 {name:"review",
                    	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
                    	    click:function(e){
                    	    	e.preventDefault();
	                   	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));
	                   	    	viewReportInfo(currentItem.id);
                    	    },
                     },{name:"edit",
                 	    text:"<span class='k-edit'></span>" + "编辑", 
                 	    click:function(e){
                    	 var url = '/fsn-core/views/market/manager_addReport.html';
                	    	fsn.edit(this.dataItem($(e.currentTarget).closest("tr")), url, "publish_testreport.html");
                 	    },
                  },], title: "操作", width: 60 }];
	
	
	root.backColumn = [	
	                   { field: "id", title:"ID", width: 30 },
	                   { field: "serviceOrder", title:"报告编号", width: 70 },
	                   { field: "sample.product.name", title:"食品名称",width: 70 },
	                   { field: "sample.batchSerialNo", title:"批次", width: 50},
	                   { field: "testType", title:"检测类型", width: 40, template: function(dataItem) {
	                    	 if(dataItem.signFlag=="未签名" && dataItem.testType=="企业自检"){
	                      		 return "<strong style='color:#DB7721;'>" + dataItem.testType + "</strong>";
	                    	 }else{
	                    		 return dataItem.testType;               		 
	                    	 }
	                     }},
	                   { field: "backResult",title:"退回原因",width:60},
	                   { field: "backTime",title:"退回时间",width:50,template: '#=backTime=fsn.formatGridDate(backTime)#', filterable: false},
	                   { field: "lastModifyUserName",title:"最后更新者",width:45},
	                   { field: "tips",title:"消息提示",width:60},
	                   { field: "signFlag", title:"签名状态", width: 30, filterable: false, template: function(dataItem) {
	                    	 if(dataItem.signFlag=="已签名"){
	                      		 return "<strong style='color:red;'>" + dataItem.signFlag + "</strong>";
	                    	 }else if(dataItem.testType=="企业自检"){
	                    		 return "<strong style='color:#DB7721;'>" + dataItem.signFlag + "</strong>";               		 
	                    	 }else{
	                    		 return dataItem.signFlag;               		 
	                    	 }
	                     }},
		                   { command: [{name:"review",
		                   	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
		                   	    click:function(e){
		                   	    	e.preventDefault();
		                   	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));
		                   	    	viewReportInfo(currentItem.id);
		                   	    },
		                   	 },], title: "操作", width: 40 }];
	
    root.sign = function(){
    	var size = $("#report_publish_grid").data("kendoGrid").select().length;
		if(size == 1){
			var selectItem = $("#report_publish_grid").data("kendoGrid").select()[0];
			root.currentReport = $("#report_publish_grid").data("kendoGrid").dataItem(selectItem);
		}else{
			lims.initNotificationMes("请选择一条未发布的检测报告！", false);
			return;
		}
		if(root.currentReport.testType != "企业自检"){
			lims.initNotificationMes("当前报告不是企业自检类型的报告，无需签名！", false);
			return;
		}
		if(!root.current_bus.signFlag){
			$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
			return;
		}
    	window.lims.system.digitalCert.doLogin("",root.signReport);
	 };
	 
	 root.sign_back = function(){
    	var size = $("#report_back_eidt_grid").data("kendoGrid").select().length;
		if(size == 1){
			var selectItem = $("#report_back_eidt_grid").data("kendoGrid").select()[0];
			root.currentReport = $("#report_back_eidt_grid").data("kendoGrid").dataItem(selectItem);
		}else{
			lims.initNotificationMes("请选择一条已退回的检测报告！", false);
			return;
		}
		if(root.currentReport.testType != "企业自检"){
			lims.initNotificationMes("当前报告不是企业自检类型的报告，无需签名！", false);
			return;
		}
		if(!root.current_bus.signFlag){
			$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
			return;
		}
    	window.lims.system.digitalCert.doLogin("",root.signReport);
	 };
	
	root.signReport = function(){
		window.open("http://" + lims.getHostName() + "/service/archive/sign/" + root.currentReport.id);
	};
	
	/*验证生产企业信息是否完整*/
	root.validateProducer=function(){
		var fullFlag = false;
		$("#publishStatus").html("正在检查企业信息是否完整，请稍候...");
		$("#toPublishWindow").data("kendoWindow").open().center();
		$.ajax({
			url:portal.HTTP_PREFIX + "/liutong/validateProducerByReportId/" +root.currentReport.id,
			type:"GET",
			async:false,
			success:function(data){
				$("#toPublishWindow").data("kendoWindow").close();
                if(data.result.status=="true"){
                	fullFlag = data.producer.fullFlag;
                	if(!data.producer.fullFlag){
                		lims.initNotificationMes("当前报告关联的生产企业【"+data.producer.producerName+"】,信息不完善，请到生产企业管理界面完善信息！", false);
                	}
                }
			},
			error:function(){
				$("#toPublishWindow").data("kendoWindow").close();
				lims.initNotificationMes("系统异常，请联系相关工作人员！", false);
			}
    	});
		return fullFlag;
	};
	
	root.publishToFSN = function(){
		if(root.current_bus.signFlag){
			if(root.currentReport.testType == "企业自检" && root.currentReport.signFlag=="未签名"){
				lims.initNotificationMes("请先对此报告签名！", false);
				return;
			}
		}
		
		/*流通企业发布报告时，验证生产企业信息是否完整*/
		/*if(root.currentReport.dbflag!="dealer" && root.busUnit!=null && root.busUnit.type=="流通企业"){
			if(!root.validateProducer()) return;
		}*/
		$("#publishStatus").html("正在发布，请稍候...");
		$("#toPublishWindow").data("kendoWindow").open().center();
		$.ajax({
			url:portal.HTTP_PREFIX + "/pipeline/getTestResult/" +root.currentReport.id,
			type:"GET",
			async:false,
			success:function(data){
				$("#toPublishWindow").data("kendoWindow").close();
                if(data.status!="false"){
                    if(data.result.status=="true"){
    					lims.initNotificationMes("ID=" + root.currentReport.id + ". " + "恭喜，报告发布成功！", true);
    					root.reportDS.read();
    					root.backReportDS.read();
    					root.publishedReportDS.read();
				    }
                }
				else{
					lims.initNotificationMes("ID=" + root.currentReport.id + ". " + data.error, false);
				}
			},
			error:function(){
				$("#toPublishWindow").data("kendoWindow").close();
				lims.initNotificationMes("系统异常，请联系相关工作人员！", false);
			}
    	});   	 
	};
	
	root.edit = function(e) {
		var size = $("#report_publish_grid").data("kendoGrid").select().length;
		if(size > 0){
			var selectItem = $("#report_publish_grid").data("kendoGrid").select()[0];
			var currentReport = $("#report_publish_grid").data("kendoGrid").dataItem(selectItem);
			root.currentReportID = currentReport.id;
		}
		var testreport = {
			id : root.currentReportID
		};
		$.cookie("user_0_edit_testreport", JSON.stringify(testreport), {
			path : '/'
		});
		window.location.pathname = "/lims-core/views/testreport/add_testreport.html";
	};
	
	$("#viewWindow").kendoWindow({
		width:1000,
		visible: false,
		title:"发布报告预览",
	});
	
	$("#toPublishWindow").kendoWindow({
		actions:[],
		width:500,
		visible:false,
		title:"发布状态",
		modal: true,
		resizable:false,
	});
	
	$("#addRemarkWin").kendoWindow({
		width:500,
		height:200,
		visible: false,
		title:"添加备注",
	});
	
	$("#tsWin").kendoWindow({
		width:500,
		visible:false,
		title:"提示",
	});
	
	root.editTips=function(){
		root.editGrid_id = "report_publish_grid";
		var length=$("#report_publish_grid").data("kendoGrid").select().length;
		if(length<1){
			lims.initNotificationMes("请选择一条未发布的检测报告！", false);
			return;
		}
		$("#remarkText").val("");
		$("#addRemarkWin").data("kendoWindow").open().center();
	};
	
	root.editTips_back=function(){
		root.editGrid_id = "report_back_eidt_grid";
		var length=$("#report_back_eidt_grid").data("kendoGrid").select().length;
		if(length<1){
			lims.initNotificationMes("请选择一条已退回的检测报告！", false);
			return;
		}
		$("#remarkText").val("");
		$("#addRemarkWin").data("kendoWindow").open().center();
	};
	$("#btn_addN").click(function(){
		$("#addRemarkWin").data("kendoWindow").close();
	});
	
	$("#btn_addY").click(function(){
		var tipText=$("#remarkText").val();
		if($.trim(tipText)==""){
			$("#tsWin").html("你没有输入任何备注文本，不能修改提示消息！");
			$("#tsWin").data("kendoWindow").open().center();
			return;
		}
		$("#addRemarkWin").data("kendoWindow").close();
		var selectRow=$("#"+root.editGrid_id).data("kendoGrid").select();
		var seItem=$("#"+root.editGrid_id).data("kendoGrid").dataItem(selectRow);
		$.ajax({
			url:portal.HTTP_PREFIX+"/testReport/editTips/" + tipText + "/" + seItem.id,
    		type:"GET",
    		dataType: "json",
            contentType: "application/json; charset=utf-8",
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					root.reportDS.read();
					root.backReportDS.read();
					lims.initNotificationMes("修改消息成功！", true);
				}else{
					lims.initNotificationMes("修改消息失败！", false);
				}
			},
			
		});
	});
	
	$("#pubWindow_noProJpg").kendoWindow({
   		width:400,
   		visible:false,
   		title:"警告",
   		modal: true,
   		resizable:false,
   	});
	
	 $("#btn_pubOK").click(function(){
		 $("#pubWindow_noProJpg").data("kendoWindow").close();
		 root.publishToFSN();
	 });
	 
	 $("#btn_pubCancel").click(function(){
		 $("#pubWindow_noProJpg").data("kendoWindow").close();
	 });
	 
	 root.publish = function(){
		 var size = $("#report_publish_grid").data("kendoGrid").select().length;
		 if(size == 1){
			var selectItem = $("#report_publish_grid").data("kendoGrid").select()[0];
			root.currentReport = $("#report_publish_grid").data("kendoGrid").dataItem(selectItem);
		 }else{
			lims.initNotificationMes("请选择一条未发布的检测报告！", false);
			return;
		 }
		 
		 if(root.haveProJpg(root.currentReport.id)){
			 root.publishToFSN();
		 }else{
			 $("#pubWindow_noProJpg").data("kendoWindow").open().center();
		 }
	 };
	 
	 root.publish_back = function(){
		 var size = $("#report_back_eidt_grid").data("kendoGrid").select().length;
		 if(size == 1){
			var selectItem = $("#report_back_eidt_grid").data("kendoGrid").select()[0];
			root.currentReport = $("#report_back_eidt_grid").data("kendoGrid").dataItem(selectItem);
		 }else{
			 lims.initNotificationMes("请选择一条已退回的检测报告！", false);
			 return;
		 }
		 
		 if(root.haveProJpg(root.currentReport.id)){
			 root.publishToFSN();
		 }else{
			 $("#pubWindow_noProJpg").data("kendoWindow").open().center();
		 }
	 };
	 
	 /*$("#check_ccie_window").kendoWindow({
			width:240,
			title:"提示",
			visible:false,
			modal:true
	});*/
	 
	 // 判断此报告有无产品图片
	 root.haveProJpg = function(proId){
		 var isHaveProJpg = true;
		 $.ajax({
             url: portal.HTTP_PREFIX + "/testReport/validatHaveProJpg/" + proId,
             type: "GET",
             dataType: "json",
             async:false,
             contentType: "application/json; charset=utf-8",
             success: function(returnValue) {
            	 if(returnValue.result.status == "true"){
            		 isHaveProJpg = returnValue.data;
            	 }
             }
    	 });
		 return isHaveProJpg;
	 };
	
	lims.initConfirmWindow(function() {
		 window.lims.system.digitalCert.doLogin("",root.signReport);
		 lims.closeConfirmWin();
	}, undefined,"检测到您当前登录的企业可以不签名，直接发布。确认要签名吗？",true);
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
	root.initialize();
});