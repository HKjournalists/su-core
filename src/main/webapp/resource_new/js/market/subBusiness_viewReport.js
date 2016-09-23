$(function(){
	var lims = window.lims = window.lims || {};
	var root = window.lims.root = window.lims.root || {};
	var filter = window.filter = window.filter || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	var viewReportID = 0;
	lims.current_index = 0;
	
	root.initialize=function(){
		root.buildGrid("report_publish_edit_grid", root.publishColumn, root.reportDS, "");
		root.buildGrid("was_published_grid_", root.publishedColumn, root.publishedReportDS, "");
		root.buildGrid("report_back_eidt_grid", root.backColumn, root.backReportDS, "_back");
		lims.initEasyItemGrid("testItem_grid");
	};
	root.reportDS = new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		if(options.filter){
            			var configure = filter.configure(options.filter);
            			return portal.HTTP_PREFIX + "/testReport/byuser/" + options.page + "/" + options.pageSize + "/3/" + configure + "/false";
            		}
					return portal.HTTP_PREFIX + "/testReport/byuser/" + options.page + "/" + options.pageSize + "/3/false";
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
            		returnValue.data.listOfReport[i].status = "未发布";
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
	
	root.publishedReportDS= new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		if(options.filter){
            			var configure = filter.configure(options.filter);
            			return portal.HTTP_PREFIX + "/testReport/byuser/" + options.page + "/" + options.pageSize+"/1/"+ configure + "/false";
            		}
					return portal.HTTP_PREFIX + "/testReport/byuser/" + options.page + "/" + options.pageSize + "/1/false";
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
	
	root.backReportDS = new kendo.data.DataSource({
			transport: {
	            read: {
	            	async:false,
	            	url : function(options){
	            		if(options.filter){
	            			var configure = filter.configure(options.filter);
	            			return portal.HTTP_PREFIX + "/testReport/getAllBackRep/" + options.page + "/" + options.pageSize + "/" + configure + "/false";
	            		}
	            		return portal.HTTP_PREFIX + "/testReport/getAllBackRep/" + options.page + "/" + options.pageSize + "/false";
	            	},
	                dataType : "json",
	                contentType : "application/json;charset=utf-8"
	            }
	        },
	        batch : true,
	        page:1,
	        pageSize: 5,
	        schema: {
	            data : function(returnValue) {
	            	for(var i=0; i<returnValue.data.listOfReport.length; i++){
	            		returnValue.data.listOfReport[i].status = "已退回";
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
	
	root.buildGrid = function (id,columns,ds,isBack){
		ds = (ds == null ? [] : ds);
		var grid = $("#"+id).data("kendoGrid");
		if(grid == null) {
			$("#" + id).kendoGrid({
				dataSource: ds,
				filterable: {
					extra:false,
					messages: lims.gridfilterMessage(),
					operators: {
						  string: lims.gridfilterOperators()
					}
				},
				height: 300,
		        width: 1000,
		        sortable: true,
		        selectable: true,
		        autoBind: true,
		        toolbar: [
		    	        	{template: id=="was_published_grid_"?kendo.template(""):kendo.template($("#toolbar_template"+isBack).html())}
		    	         ],
		        pageable: {
		            refresh: true,
		            pageSizes: 5,
		            messages: lims.gridPageMessage()
		        },
		        columns: columns
			});
		} else {
			grid.setDataSource(ds);
		}
		
	};
	
	root.publishColumn = [	
	                   { field: "id", title:"ID", width: 35 },
	                   { field: "serviceOrder", title:"报告编号", width: 70 },
	                   { field: "sample.product.name", title:"食品名称",width: 70 },
	                   { field: "sample.batchSerialNo", title:"批次", width: 50},
	                   { field: "status", title:"状态", width: 30, filterable: false},
	                   { field: "lastModifyUserName",title:"最后更新者", filterable: false,width:45},
	                   { field: "lastModifyTime",title:"最后更新时间",width:55,template:'#=lastModifyTime=fsn.formatGridDate(lastModifyTime)#',filterable: false},
	                   { field: "tips",title:"消息提示",width:55},
		                   { command: [{name:"edit",
		                   	    text:"<span class='k-edit'></span>" + "编辑", 
		                   	    click:function(e){
		                   	    	root.edit(this.dataItem($(e.currentTarget).closest("tr")));
		                     }},
		                     {name:"review",
		                   	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
		                   	    click:function(e){
		                   	    	e.preventDefault();
		                   	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));
		                   	    	viewReportID=currentItem.id;
		                   	    	lims.easyViewReport(viewReportID);
		                     }},
		                     {name:lims.localized("Delete"),
							  text:"<span class='k-icon k-cancel'></span>" + lims.localized("Delete"),
						      click: function(e){
						          var deleteRow = $(e.target).closest("tr");
						          deleteItem = this.dataItem(deleteRow);
							      $("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center(); 
						     }}
		                     ], title: "操作", width: 150 }];
	
	root.backColumn = [	
		                   { field: "id", title:"ID", width: 35 },
		                   { field: "serviceOrder", title:"报告编号", width: 70 },
		                   { field: "sample.product.name", title:"食品名称",width: 70 },
		                   { field: "status", title:"状态", width: 30, filterable: false},
		                   { field: "backResult",title:"退回原因",width:60},
		                   { field: "backTime",title:"退回时间",width:55,template: '#=backTime= fsn.formatGridDate(backTime)#', filterable: false},
		                   { field: "lastModifyUserName",title:"最后更新者",width:45},
		                   { field: "tips",title:"消息提示",width:50},
			                   { command: [{name:"edit",
			                   	    text:"<span class='k-edit'></span>" + "编辑", 
			                   	    click:function(e){
			                   	    	root.edit(this.dataItem($(e.currentTarget).closest("tr")));
			                     }},
			                     {name:"review",
			                   	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
			                   	    click:function(e){
			                   	    	e.preventDefault();
			                   	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));
			                   	    	viewReportID=currentItem.id;
			                   	    	lims.easyViewReport(viewReportID);
			                     }},
			                     {name:lims.localized("Delete"),
									  text:"<span class='k-icon k-cancel'></span>"+ lims.localized("Delete"),
								      click: function(e){
								          var deleteRow = $(e.target).closest("tr");
								          deleteItem = this.dataItem(deleteRow);
									      $("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center(); 
								 }}
			                     ], title: "操作", width: 110 }];
	
	root.publishedColumn=[
                       { field: "id", title:"ID", width: 35 },
	                   { field: "serviceOrder", title:"报告编号", width: 70 },
	                   { field: "sample.product.name", title:"食品名称",width: 70 },
	                   { field: "sample.productionDate", title:"生产日期", width: 50,template: '#=sample.productionDate!=null?sample.productionDate.substr(0,10):""#', filterable: false},
	                   { field: "sample.batchSerialNo", title:"批次", width: 60},
	                   { field: "testOrgnization", title:"检验机关", width: 70},
	                   { field: "status", title:"状态", width: 30, filterable: false, template: function(dataItem) {
                                   var reportStatue="";
                                   switch (dataItem.publishFlag){
                                       case "1": reportStatue = "已发布" ;break;
                                       default: reportStatue = "审核中" ;
                                   }
		                    		return "<strong style='color:#006536;'>" + reportStatue + "</strong>";                    		 
		                     }},
	                   { field: "receiveDate", title:"发布时间", width: 55, filterable: false},
	                   { command: [
	                     {name:"review",
	                   	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
	                   	    click:function(e){
	                   	    	e.preventDefault();
	                   	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));	
	                   	    	viewReportID=currentItem.id;
	                   	    	lims.easyViewReport(viewReportID);
	                   	    }
	                   	    	},], title: "操作", width: 100 }];
    
	root.edit = function(e) {
		root.currentReportID = e.id;
		var testreport = {
			id : root.currentReportID
		};
		$.cookie("user_0_edit_testreport", JSON.stringify(testreport), {
			path : '/'
		});
		window.open('/fsn-core/views/market/subBusiness_addReport.html');
	};
	
	$("#viewWindow").kendoWindow({
		width:1000,
		visible: false,
		title:"发布报告预览"
	});
	
	$("#addRemarkWin").kendoWindow({
		width:500,
		height:200,
		visible: false,
		title:"添加备注"
	});
	
	$("#tsWin").kendoWindow({
		width:500,
		visible:false,
		title:"提示"
	});
	
	root.editTips=function(){
		root.eidtGrid_id = "report_publish_edit_grid";
		var length=$("#report_publish_edit_grid").data("kendoGrid").select().length;
		if(length<1){
			lims.initNotificationMes("请选择一条未发布的检测报告！", false);
			return;
		}
		$("#remarkText").val("");
		$("#addRemarkWin").data("kendoWindow").open().center();
	};
	
	root.editTips_back=function(){
		root.eidtGrid_id = "report_back_eidt_grid";
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
		var tipText=$("#tipText").val();
		if($.trim(tipText)==""){
			$("#tsWin").html("你没有输入任何备注文本，不能修改提示消息！");
			$("#tsWin").data("kendoWindow").open().center();
			return;
		}
		$("#addRemarkWin").data("kendoWindow").close();
		var selectRow=$("#"+root.eidtGrid_id).data("kendoGrid").select();
		var seItem=$("#"+root.eidtGrid_id).data("kendoGrid").dataItem(selectRow);
		$.ajax({
			url:portal.HTTP_PREFIX+"/testReport/editTips/"+tipText+"/"+seItem.id,
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
			}
		});
	});
	
   /**
	 * 处理未发布的报告grid的分页功能
	 * @author tangxin
	 */
	function waitPubGirdPageFun(){
		var gridDS = root.reportDS;
		var page = gridDS.page();
		var flage = (gridDS.data().length < 2);
    	if(page > 1 && flage) {
    		gridDS.page(--page);
    	} else {
    		gridDS.read();
    	}
	}
	
	 /**
	 * 处理已退回报告grid的分页功能
	 * @author tangxin
	 */
	function backGirdPageFun(){
		var gridDS = root.backReportDS;
		var page = gridDS.page();
		var flage = (gridDS.data().length < 2);
    	if(page > 1 && flage) {
    		gridDS.page(--page);
    	} else {
    		gridDS.read();
    	}
	}
	
	lims.initConfirmWindow(function() {
		 $.ajax({
				url:portal.HTTP_PREFIX + "/testReport/" + deleteItem.id,
				type:"DELETE",
				success:function(data){
					if(data.result.status == "true"){
						if(data.result.show){
							lims.initNotificationMes(data.result.errorMessage, false);
						}else{
							lims.initNotificationMes("删除成功！", true);
						}
						waitPubGirdPageFun();
						backGirdPageFun();
						root.publishedReportDS.read();
					}else{
						lims.initNotificationMes("删除失败！", false);
					}
				}
		  });
		  lims.closeConfirmWin();
	}, function() {lims.closeConfirmWin();},lims.l("Are you sure to delete registration form"),true);
	
	root.initialize();
});
