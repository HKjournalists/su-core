$(function(){
	var lims = window.lims = window.lims || {};
	var root = window.lims.root = window.lims.root || {};
	var filter = window.filter = window.filter || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	var viewReportID = 0;
	
	root.initialize=function(){
		root.buildGrid("report_publish_edit_grid", root.publishColumn, root.reportDS, "");
		root.buildGrid("report_back_eidt_grid", root.backColumn, root.backReportDS, "_back");
		root.buildGrid("was_published_grid_", root.publishedColumn, root.publishedReportDS, "");
		lims.initEasyItemGrid("testItem_grid");
	};
	root.reportDS = new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		if(options.filter){
            			var configure = filter.configure(options.filter);
            			return portal.HTTP_PREFIX + "/testReport/byuser/" + options.page + "/" + options.pageSize + "/4/" + configure + "/false";
            		}
					return portal.HTTP_PREFIX + "/testReport/byuser/" + options.page + "/" + options.pageSize + "/4/false";
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
	            			return portal.HTTP_PREFIX + "/testReport/byuser/" + options.page + "/" + options.pageSize + "/5/" + configure + "/false";
	            		}
						return portal.HTTP_PREFIX + "/testReport/byuser/" + options.page + "/" + options.pageSize + "/5/false";
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
	
	root.publishedReportDS= new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		if(options.filter){
            			var configure = filter.configure(options.filter);
            			return portal.HTTP_PREFIX + "/testReport/byuser/" + options.page + "/" + options.pageSize+"/6/"+ configure + "/false";
            		}
					return portal.HTTP_PREFIX + "/testReport/byuser/" + options.page + "/" + options.pageSize + "/6/false";
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
	
	root.buildGrid = function (id,columns,ds,isBack){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ? []:ds,
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
	        resizable: true,
	        toolbar: [],
	        pageable: {
	            refresh: true,
	            pageSizes: 5,
	            messages: lims.gridPageMessage(),
	        },
	        columns: columns,
		});
	};
	
	root.publishColumn = [	
	                   { field: "id", title:"ID", width: 35 },
	                   { field: "serviceOrder", title:"报告编号", width: 70 },
	                   { field: "sample.product.name", title:"食品名称",width: 70 },
	                   { field: "sample.batchSerialNo", title:"批次", width: 50},
	                   { field: "status", title:"状态", width: 30, filterable: false, template: function(dataItem) {
                           var reportStatue="";
                           switch (dataItem.publishFlag){
                               case "4": reportStatue = "未审核" ;break;
                               default: ;
                           }
                    	   return "<strong style='color:#006536;'>" + reportStatue + "</strong>";                    		 
	                   	}},
	                   { field: "lastModifyUserName",title:"最后更新者",width:45},
	                   { field: "lastModifyTime",title:"最后更新时间",width:55,template: '#= (new Date(lastModifyTime)).format("YYYY-MM-dd")#', filterable: false},
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
		                   	    	viewReportInfo(currentItem.id);
		                     }},
		                     {name:lims.localized("Delete"),
							  text:"<span class='k-icon k-cancel'></span>" + lims.localized("Delete"),
						      click: function(e){
						          var deleteRow = $(e.target).closest("tr");
						          deleteItem = this.dataItem(deleteRow);
							      $("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center(); 
						     }}
		                     ], title: "操作", width: 110 }];
	
	root.backColumn = [	
		                   { field: "id", title:"ID", width: 35 },
		                   { field: "serviceOrder", title:"报告编号", width: 70 },
		                   { field: "sample.product.name", title:"食品名称",width: 70 },
		                   { field: "status", title:"状态", width: 30, filterable: false, template: function(dataItem) {
		                    		return "<strong style='color:red;'>" + dataItem.status + "</strong>";                    		 
		                     }},
		                   { field: "backResult",title:"退回原因",width:60},
		                   { field: "backTime",title:"退回时间",width:55,template: '#= (new Date(backTime)).format("YYYY-MM-dd")#', filterable: false},
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
			                   	    	viewReportInfo(currentItem.id);
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
		                   { field: "sample.productionDate", title:"生产日期", width: 50,template: '#=sample.productionDate!=null?sample.productionDate.substr(0,10):""#',filterable: false},
		                   { field: "sample.batchSerialNo", title:"批次", width: 60},
		                   { field: "status", title:"状态", width: 100, filterable: false, template: function(dataItem) {
	                           var reportStatue="";
	                           switch (dataItem.publishFlag){
	                               case "6": reportStatue = "商超审核通过";
	                               				break;
	                               case "3": reportStatue = "已完善，未发布到testlab";
	                                    		break;
	                               case "0": reportStatue = "已发布到testlab，审核中";
	                               				break;
	                               case "1": reportStatue = "testlab审核通过";
	                            	    		break;
	                               case "2": reportStatue = "testlab审核退回"; 
	                               				break;
	                               default: ;
	                           }
		                    	return "<strong style='color:#006536;'>" + reportStatue + "</strong>";                    		 
		                     }},
		                   { field: "lastModifyTime",title:"最后更新时间",width:55,template: '#= (new Date(lastModifyTime)).format("YYYY-MM-dd")#', filterable: false},
		                   { command: [
		                     {name:"review",
		                   	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
		                   	    click:function(e){
		                   	    	e.preventDefault();
		                   	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));
		                   	    	viewReportInfo(currentItem.id);
		                   	    },
		                   	    	},], title: "操作", width: 50 }];
	
	root.edit = function(e) {
		root.currentReportID = e.id;
		var testreport = {
			id : root.currentReportID
		};
		$.cookie("user_0_edit_testreport", JSON.stringify(testreport), {
			path : '/'
		});
		window.open('/fsn-core/views/report/input_report_dealer.html');
		//window.location.pathname = "/fsn-core/views/market/add_testreport.html";
	};
	
	$("#viewWindow").kendoWindow({
		width:1000,
		visible: false,
		title:"发布报告预览",
	});
	
	
	$("#tsWin").kendoWindow({
		width:500,
		visible:false,
		title:"提示",
	});
	
	lims.initConfirmWindow(function() {
		  $.ajax({
					url:portal.HTTP_PREFIX + "/testReport/" + deleteItem.id,
					type:"DELETE",
					success:function(data){
						if(data.result.status == "true"){
							if(data.result.show){
								lims.msg("error_msg",null,data.result.errorMessage);
							}else{
								lims.msg("ts_msg",data.result,"删除成功！");
							}
							root.reportDS.read();
							root.backReportDS.read();
							root.publishedReportDS.read();
						}else{
							lims.msg("ts_msg",data.result,"删除失败！");
						}
					}
				});
		  lims.closeConfirmWin();
	}, undefined,lims.l("Are you sure to delete registration form"),true);
	
	root.initialize();
});
