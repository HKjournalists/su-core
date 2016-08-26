$(function(){
	var lims = window.lims = window.lims || {};
	var root = window.lims.root = window.lims.root || {};
	var filter = window.filter = window.filter || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	var upload = fsn.upload = fsn.upload || {}; // portal命名空间
	
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	fsn.current_page_name = "manage_report_dealer";
	
	root.initialize=function(){
		fsn.initComponentCommon();
		
		upload.buildGridWioutToolBar("report_publish_edit_grid", root.publishColumn, root.dealer_reportDS, 350);
		upload.buildGridWioutToolBar("report_back_eidt_grid", root.backColumn, root.dealer_backReportDS, 350);
		upload.buildGridWioutToolBar("was_published_grid", root.publishedColumn, root.dealer_publishedReportDS, 350);
		
		initItemGrid("testItem_grid");
	};
	
	root.dealer_reportDS = new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		var configure = null;
            		if(options.filter){
            			 configure = filter.configure(options.filter);
            		}
            		return portal.HTTP_PREFIX + "/testReport/byuserFromSC/" + options.page + "/" + options.pageSize + "/4/" + configure ;
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
	
	root.dealer_backReportDS = new kendo.data.DataSource({
			transport: {
	            read: {
	            	async:false,
	            	url : function(options){
	            		var configure = null;
	            		if(options.filter){
	            			configure = filter.configure(options.filter);
	            		}
	            		return portal.HTTP_PREFIX + "/testReport/byuser/getBackToGYS/" + options.page + "/" + options.pageSize + "/" + configure;
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
	
	root.dealer_publishedReportDS= new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		var configure = null;
            		if(options.filter){
            			configure = filter.configure(options.filter);
            		}
            		return portal.HTTP_PREFIX + "/report/operation/byuser/getPassReportOfDealer/"+ configure + "/" + options.page + "/" + options.pageSize;
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
	
	root.publishColumn = [	
	                   /*{ field: "id", title:"ID", width: 35 },*/
	                   { field: "serviceOrder", title:"报告编号", width: 80 },
	                   { field: "sample.product.name", title:"食品名称",width: 100 },
	                   { field: "sample.product.barcode", title:"条形码",width: 100 },
	                   { field: "sample.batchSerialNo", title:"批次", width: 50},
	                   { field: "status", title:"状态", width: 40, filterable: false, template: function(dataItem) {
                           var reportStatue="";
                           switch (dataItem.publishFlag){
                               case "4": reportStatue = "未审核" ;break;
                               default: ;
                           }
                    	   return "<strong style='color:#006536;'>" + reportStatue + "</strong>";                    		 
	                   	}},
	                   { field: "lastModifyUserName",title:"最后更新者",width:60},
	                   { field: "lastModifyTime",title:"最后更新时间",width:55,template: '#= fsn.formatGridDate(lastModifyTime)#', filterable: false},
		                   { command: [{name:"edit",
		                   	    text:"<span class='k-edit'></span>" + "编辑", 
		                   	    click:function(e){
		                   	    	var url = '/fsn-core/views/report_new/input_report_dealer.html';
		                   	    	fsn.edit(this.dataItem($(e.currentTarget).closest("tr")), url, "manage_report_dealer.html");
		                     }},
		                     {name:"review",
		                   	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
		                   	    click:function(e){
		                   	    	e.preventDefault();
		                   	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));
		                   	    	viewReportInfo(currentItem.id);
		                     }},
		                     {name:"delete",
							  text:"<span class='k-icon k-cancel'></span>" + lims.localized("Delete"),
						      click: function(e){
						          var deleteRow = $(e.target).closest("tr");
						          deleteItem = this.dataItem(deleteRow);
						          fsn.delete_id = deleteItem.id;
							      $("#delete_window").data("kendoWindow").open().center(); 
						     }}
		                     ], title: "操作", width: 125 }];
	
	root.backColumn = [	
		                   /*{ field: "id", title:"ID", width: 35 },*/
		                   { field: "serviceOrder", title:"报告编号", width: 70 },
		                   { field: "sample.product.name", title:"食品名称",width: 80 },
		                   { field: "sample.product.barcode", title:"条形码",width: 100 },
		                   { field: "status", title:"状态", width: 40, filterable: false, template: function(dataItem) {
		                    		return "<strong style='color:red;'>" + dataItem.status + "</strong>";                    		 
		                     }},
		                   { field: "backResult",title:"退回原因",width:100},
		                   { field: "backTime",title:"退回时间",width:45,template: '#= fsn.formatGridDate(backTime)#', filterable: false},
		                   { field: "lastModifyUserName",title:"最后更新者",width:55},
			                   { command: [{name:"edit",
			                   	    text:"<span class='k-edit'></span>" + "编辑", 
			                   	    click:function(e){
			                   	    	var url = '/fsn-core/views/report_new/input_report_dealer.html';
			                   	    	fsn.edit(this.dataItem($(e.currentTarget).closest("tr")), url, "manage_report_dealer.html");
			                     }},
			                     {name:"review",
			                   	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
			                   	    click:function(e){
			                   	    	e.preventDefault();
			                   	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));
			                   	    	viewReportInfo(currentItem.id);
			                     }},
			                     {name:"delete",
									  text:"<span class='k-icon k-cancel'></span>"+ lims.localized("Delete"),
								      click: function(e){
								          var deleteRow = $(e.target).closest("tr");
								          deleteItem = this.dataItem(deleteRow);
								          fsn.delete_id = deleteItem.id;
							      		  $("#delete_window").data("kendoWindow").open().center(); 
								 }}
			                     ], title: "操作", width: 125 }];
	
	root.publishedColumn=[
	                       /*{ field: "id", title:"ID", width: 35 },*/
		                   { field: "serviceOrder", title:"报告编号", width: 110 },
		                   { field: "sample.product.name", title:"食品名称",width: 110 },
		                   { field: "sample.product.barcode", title:"条形码",width: 100 },
		                   { field: "sample.productionDate", title:"生产日期", width: 50,template: '#=sample.productionDate!=null?sample.productionDate.substr(0,10):""#',filterable: false},
		                   { field: "sample.batchSerialNo", title:"批次", width: 60},
		                   { field: "status", title:"状态", width: 60, filterable: false, template: function(dataItem) {
	                           var reportStatue="";
	                           switch (dataItem.publishFlag){
	                           	   case "2":
	                           	   case "3":
	                               case "6": reportStatue = "审核中";
	                               				break;
	                               case "1": reportStatue = "审核通过";
	                            	    		break;
	                               default: ;
	                           }
		                    	return "<strong style='color:#006536;'>" + reportStatue + "</strong>";                    		 
		                     }},
		                   { field: "lastModifyTime",title:"最后更新时间",width:55,template: '#= fsn.formatGridDate(lastModifyTime)#', filterable: false},
		                   { command: [
		                     {name:"review",
		                   	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
		                   	    click:function(e){
		                   	    	e.preventDefault();
		                   	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));
		                   	    	viewReportInfo(currentItem.id);
		                   	    },
		                   	    	},], title: "操作", width: 50 }];
	
	root.initialize();
});
