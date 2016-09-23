$(function(){
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var lims = window.lims = window.lims || {};
	var filter = window.filter = window.filter || {};
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	var upload = fsn.upload = fsn.upload || {}; // portal命名空间
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	var viewReportID = 0;
	
	fsn.initialize=function(){
		fsn.initComponentCommon();
		
		upload.buildGridByBoolbar("report_no_publish_grid", fsn.publishColumn, fsn.reportDS, 350, "");
		upload.buildGridWioutToolBar("report_has_published_grid", fsn.publishedColumn, fsn.publishedReportDS, 350, "toolbar_template");
		upload.buildGridByBoolbar("report_back_grid", fsn.backColumn, fsn.backReportInputDS, 350, "toolbar_template_back");
		
		lims.initEasyItemGrid("testItem_grid");
		
		/* 热词推荐 */
        $("#hotword").bind("click", function(){fsn.changeGuidStyle(true);});
        $("#closeBtn").bind("click", function(){fsn.changeGuidStyle(false);});
        
        /* 初始化【热词推荐】栏中，符合当前热词的产品总数 */
        fsn.initProductCount();
	};
	
	fsn.reportDS = new kendo.data.DataSource({
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
		
	fsn.publishColumn = [	
	                   { field: "id", title:"ID", width: 35 },
	                   { field: "serviceOrder", title:"报告编号", width: 70 },
	                   { field: "sample.product.name", title:"食品名称",width: 70 },
	                   { field: "sample.batchSerialNo", title:"批次", width: 50},
	                   { field: "status", title:"状态", width: 30, filterable: false},
	                   { field: "lastModifyUserName",title:"最后更新者",width:45},
	                   { field: "lastModifyTime",title:"最后更新时间",width:55,template: '#=lastModifyTime=fsn.formatGridDate(lastModifyTime)#', filterable: false},
	                   { field: "tips",title:"消息提示",width:55},
		                   { command: [{name:"edit",
		                   	    text:"<span class='k-edit'></span>" + "编辑", 
		                   	    click:function(e){
		                   	    	var url = '/fsn-core/views/market/add_testreport.html';
		                   	    	fsn.edit(this.dataItem($(e.currentTarget).closest("tr")), url);
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
		                     ], title: "操作", width: 110 }];
	
	fsn.initialize();
});
