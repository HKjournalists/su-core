$(function(){
	var fsn = window.fsn = window.fsn || {};
	var wholesale = fsn.wholesale = fsn.wholesale || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
	var WHOLESALE_GRID = null;
	var WHOLESALE_DS = null;
	var TZID = null;
	var CONFIRM = null;
	function initialize(){
		CONFIRM = initKendoWindow("CONFIRM_COMMON","300px", "100px", "删除提示！", false,true,false,["Close"],null,"");
        buildGridWioutToolBar("WHOLESALE_Grid",wholesale.columns,getStoreDS(""),"400");//已确认批发的商品
        WHOLESALE_GRID = $("#WHOLESALE_Grid").data("kendoGrid");
	};
	
	
	 function getStoreDS(handler){
	    if(handler!=""){
			 handler = encodeURIComponent(handler);
		 }
		 WHOLESALE_DS = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
						return fsn.getHttpPrefix() +"/wasteDisposa/getListWaste/"+ options.page + "/" + options.pageSize + "?handler="+handler;
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	data : function(data) {
	                return data.data;  
	            },
	            total : function(data) {
	               return  data.total;//总条数
	            }  
	        },
	        batch : true,
	        page:1,
	        pageSize : 10, //每页显示个数
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		   });
		 return WHOLESALE_DS;
	 }
	 
	 function buildGridWioutToolBar(id,columns,ds, height){
	        $("#" + id).kendoGrid({
	            dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
	            filterable: {
	                extra: false,
	                messages: fsn.gridfilterMessage(),
	                operators: {
	                    string: fsn.gridfilterOperators(),
	                    date: fsn.gridfilterOperatorsDate(),
	                    number: fsn.gridfilterOperatorsNumber()
	                }
	            },
	            editable: false,
	            height:height,
	            width: "100%",
	            sortable: true,
	            resizable: true,
	            selectable: false,
	            pageable: {
	                refresh: true,
	                messages: fsn.gridPageMessage()
	            },
	            columns: columns
	        });
	    };

	
	 /*新增 */
	 wholesale.add = function(){
		 window.location.href = "waste_disposa_add.html";
	 };
 	 /* 查看 */
	 wholesale.checkUrl = function(id){
	    	window.location.href = "waste_disposa_add.html?id=" + id;
	 };
	 //查询
	 wholesale.check = function(){
	 	var handler = $.trim($("#handler").val());
	 	WHOLESALE_GRID.setDataSource(getStoreDS(handler));
	 	$("#handler").val("");
	 	WHOLESALE_GRID.refresh();
	 };
	 
	 
	//初始化弹出框
	 function initKendoWindow(formId, width, heigth, title,visible, modal, resizable, actions, mesgLabelId, message){
			if(mesgLabelId != null) {
				$("#"+mesgLabelId).html(message);
			}
			$("#"+formId).kendoWindow({
				width:width,
				height:heigth,
		   		visible:visible,
		   		title:title,
		   		modal: modal,
		   		resizable:resizable,
		   		actions:actions
			});
			return $("#"+formId).data("kendoWindow");
		};
		
		//删除调用方法
		wholesale.deletetWhole = function(id){
			 TZID = id;
			 CONFIRM.open().center();
		};
	 
	 /* 删除提交  */
		wholesale.deleteRow = function(status){
			 if(status=="save"){
					$.ajax({
						url:HTTPPREFIX + "/wasteDisposa/deleteRow/"+TZID,
						type:"GET",
						dataType: "json",
						async:false,
						success:function(returnValue){
							if (returnValue.result.status == "true") {
								fsn.initNotificationMes("已成功删除！", true);
								if(returnValue.total>0){
								   initialize();
								}else{
								buildGridWioutToolBar("WHOLESALE_Grid",wholesale.columns,"","400");
								}
							} else {
								fsn.initNotificationMes("删除失败", false);
								initialize();
							}
						}
					});
					CONFIRM.close();
				}else{
					CONFIRM.close();
				}
	 };
	//显示字段
	 wholesale.columns = [
	               {field:"handler", title:"处理人", width:"20%",filterable: false,editable: false},
	               {field:"handleWay", title:"处理方式", width:"20%",filterable: false,editable: false},
	               {field:"destory", title:"销毁地点", width:"20%",filterable: false,editable: false},
	               {field:"handleNumber", title:"处理数量", width:"20%",filterable: false,editable: false},
	               {field:"handleTime", title:"处理时间", width:"20%",filterable: false,editable: false},
				   {field:"",title:fsn.l("操作"), width: "25%",template:function(dataItem){
		                     return "<a class='k-button k-button-icontext k-grid-ViewDetail ' onclick='fsn.wholesale.checkUrl("+dataItem.id+");'><span class='k-icon k-edit' ></span>编辑</a>"+
		                     		"<a class='k-button k-button-icontext k-grid-ViewDetail '  onclick='fsn.wholesale.deletetWhole("+dataItem.id+");'><span class='k-icon k-update'></span>删除</a>";
					   	}
					 }
		                  ];
	 initialize();
});
