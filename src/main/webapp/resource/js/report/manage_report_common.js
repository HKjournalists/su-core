var fsn = window.fsn = window.fsn || {};
var lims = window.lims = window.lims || {};
var portal = fsn.portal = fsn.portal || {};
portal.HTTP_PREFIX = fsn.getHttpPrefix();
/* 修改消息提示是，用户当前操作Grid的id */
var eidtGrid_id = "";
/**
 * 报告编辑功能
 * @author ZhangHui 2015/4/10
 */
fsn.edit = function(e, url, fromPage) {
	var report = {
		id : e.id,
		from : fromPage
	};
	 sessionStorage.removeItem("user_0_edit_testreport");  
	 sessionStorage.setItem("user_0_edit_testreport",JSON.stringify(e.id));
	$.cookie("user_0_edit_testreport", JSON.stringify(report), {
		path : '/'
	});
	window.open(url);
	//window.location.pathname = "/fsn-core/views/market/add_testreport.html";
};

/**
 * 初始化窗口
 * @author Zhanghui 2015/4/10
 */
fsn.initComponentCommon = function(){
	$("#viewWindow").kendoWindow({
		width:1000,
		visible: false,
		title:"发布报告预览",
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
		var selectRow=$("#"+eidtGrid_id).data("kendoGrid").select();
		var seItem=$("#"+eidtGrid_id).data("kendoGrid").dataItem(selectRow);
		$.ajax({
			url:portal.HTTP_PREFIX+"/testReport/editTips/"+tipText+"/"+seItem.id,
    		type:"GET",
    		dataType: "json",
            contentType: "application/json; charset=utf-8",
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					fsn.reportDS.read();
					fsn.backReportInputDS.read();
					lims.msg("ts_msg",returnValue.result,"修改消息成功！");
				}else{
					lims.msg("ts_msg",returnValue.result,"修改消息失败！");
				}
			},
		});
	});
	
	fsn.initKendoWindow("delete_window","友情提示","300px","190px",false, []);
	
	$("#delete_no_btn").click(function(){
		$("#delete_window").data("kendoWindow").close();
	});
	
	$("#delete_yes_btn").click(function(){
		if(!fsn.delete_id){
			fsn.initNotificationMes("请先选择一个要删除的报告!", false);
			return;
		}
		
		$.ajax({
			url:portal.HTTP_PREFIX + "/testReport/" + fsn.delete_id,
			type:"DELETE",
			success:function(data){
				$("#delete_window").data("kendoWindow").close();
				
				if(data.result.status == "true"){
					if(data.result.show){
						fsn.initNotificationMes(data.result.errorMessage, false);
					}else{
						fsn.initNotificationMes("删除成功!", true);
					}
					
					if(fsn.current_page_name && fsn.current_page_name=="manage_report_dealer"){
						root.dealer_reportDS.read();
						root.dealer_backReportDS.read();
						root.dealer_publishedReportDS.read();
					}else{
						//fsn.fleshGirdPageFun(fsn.reportDS);
						//fsn.fleshGirdPageFun(fsn.backReportInputDS);
						fsn.reportDS.read();
						fsn.backReportInputDS.read();
						fsn.publishedReportDS.read();
					}
				}else{
					fsn.initNotificationMes("删除失败!", false);
				}
			}
		});
	});
	
	/**
	 * 已发布的报告数据源
	 * @author Zhanghui 2015/4/10
	 */
	fsn.publishedReportDS = new kendo.data.DataSource({
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

	/**
	 * 已退回的报告数据源
	 * @author Zhanghui 2015/4/10
	 */
	fsn.backReportInputDS = new kendo.data.DataSource({
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
};

/**
 * 弹出提示修改消息的窗口
 * @author Zhanghui 2015/4/10
 */
fsn.editTips = function(divId, msg){
	eidtGrid_id = divId;
	var length=$("#" + divId).data("kendoGrid").select().length;
	if(length < 1){
		$("#tsWin").html(msg);
		$("#tsWin").data("kendoWindow").open().center();
		return;
	}
	$("#tipText").val("");
	$("#addRemarkWin").data("kendoWindow").open().center();
};

/**
 * 刷新数据源（解决最后一页无法自动刷新的问题）
 * @author tangxin
 */
fsn.fleshGirdPageFun = function(dataSource){
	var page = dataSource.page();
	var flage = (dataSource.data().length < 2);
	if(page > 1 && flage) {
		dataSource.page(--page);
	} else {
		dataSource.read();
	}
};

fsn.backColumn = [	
                   { field: "id", title:"ID", width: 35 },
                   { field: "serviceOrder", title:"报告编号", width: 70 },
                   { field: "sample.product.name", title:"食品名称",width: 70 },
                   { field: "status", title:"状态", width: 30, filterable: false, template: function(dataItem) {
                    		return "<strong style='color:red;'>" + dataItem.status + "</strong>";                    		 
                     }},
                   { field: "backResult",title:"退回原因",width:60},
                   { field: "backTime",title:"退回时间",width:55,template: '#=backTime=fsn.formatGridDate(backTime)#', filterable: false},
                   { field: "lastModifyUserName",title:"最后更新者",width:45},
                   { field: "tips",title:"消息提示",width:50},
	                   { command: [{name:"edit",
	                   	    text:"<span class='k-edit'></span>" + "编辑", 
	                   	    click:function(e){
	                   	    	fsn.edit(this.dataItem($(e.currentTarget).closest("tr")), portal.url_edit, "");
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
						          fsn.delete_id = deleteItem.id;
							      $("#delete_window").data("kendoWindow").open().center(); 
						 }}
	                     ], title: "操作", width: 110 }];

fsn.publishedColumn=[
               { field: "id", title:"ID", width: 35 },
               { field: "serviceOrder", title:"报告编号", width: 70 },
               { field: "sample.product.name", title:"食品名称",width: 70 },
               { field: "sample.productionDate", title:"生产日期", width: 50,template: '#=sample.productionDate!=null?sample.productionDate.substr(0,10):""#',filterable: false},
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
               	    	viewReportInfo(currentItem.id);
               	    },
               	},], title: "操作", width: 100 }];

/**
 * 初始化预览报告时检测项目的Grid
 */
initItemGrid = function(girdID){
	if(girdID){
		$("#" + girdID).kendoGrid({
			dataSource:[],
	        editable: false,
	        pageSize : 10,
	        pageable: {
	            refresh: true,
	            pageSizes: 5,
	            messages: fsn.gridPageMessage(),
	        },
	        columns: [
	                  { field: "name", title:lims.l("Item Name"), width: 40 },
	                  { field: "unit", title:lims.l("Unit"),width: 30 },
	                  { field: "techIndicator", title:lims.l("Specifications"), width: 50},
	                  { field: "result", title:lims.l("Test Result"), width: 40},
	                  { field: "assessment", title:lims.l("Conslusion"), width: 40},
	                  { field: "standard", title:lims.l("Standard"), width: 50},
		             ],
		});
	}
};


/**
 * 功能描述：报告预览功能
 * @author ZhangHui 2015/6/7
 */
viewReportInfo = function(report_id){
	if(!report_id){
		return;
	}
	
	$.ajax({
		url:portal.HTTP_PREFIX + "/testReport/" + report_id + "?view=true",
		type:"GET",
		success:function(returnValue){
			if(returnValue.result.status == "true"){
				var report_vo = returnValue.data;
				fsn.setReportValue(report_vo, "product_input");
				var product_vo = report_vo.product_vo;
				$("#proExpiratio").text(product_vo.expiration != null ? product_vo.expiration : ""); // 质保期
				 
				$("#viewWindow").data("kendoWindow").open().center();
			}
		},
	});
	
	var testItemDS = new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		if(lims.easyViewReportID == 0){
            			return;
            		}
            		return portal.HTTP_PREFIX + "/testReport/testItems/" + options.page + "/" + options.pageSize + "/" + report_id;
            	},
            	type:"GET",
                dataType : "json",
                contentType : "application/json"
            }
        },
        batch : true,
        page:1,
        pageSize: 5,
        schema: {
            data : function(returnValue) {
                return returnValue.data.listOfItem;
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
	
	$("#testItem_grid").data("kendoGrid").setDataSource(testItemDS);
};