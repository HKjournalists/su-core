$(function(){
	var fsn = window.fsn = window.fsn || {};
	var dishsNo = fsn.dishsNo = fsn.dishsNo || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
	var DISHSNO_GRID = null;
	var DISHSNO_DS = null;
	var TZID = null;
	var CONFIRM = null;
	function initialize(){
		CONFIRM = initKendoWindow("CONFIRM_COMMON","300px", "100px", "删除提示！", false,true,false,["Close"],null,"");
        buildGridWioutToolBar("DISHSNO_GRID",dishsNo.columns,getStoreDS(""),"400");//已确认批发的商品
        DISHSNO_GRID = $("#DISHSNO_GRID").data("kendoGrid");
	};
	
	
	 function getStoreDS(dishsName){
		if(dishsName!=""){
			 dishsName = encodeURIComponent(dishsName);
		 }
		 DISHSNO_DS = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
						return fsn.getHttpPrefix() +"/dishsNo/getListDishsNo/"+ options.page + "/" + options.pageSize + "?dishsName="+dishsName;
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
		 return DISHSNO_DS;
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
	 dishsNo.add = function(){
		 window.location.href = "dishs_prepackNo_add.html";
	 };
 
	 //查询
	 dishsNo.check = function(){
	 	var dishsName = $.trim($("#dishsName").val());
	 	DISHSNO_GRID.setDataSource(getStoreDS(dishsName));
	 	$("#dishsName").val("");
	 	DISHSNO_GRID.refresh();
	 };
	 
	 /* 查看 */
	 dishsNo.checkUrl = function(id){
	    	window.location.href = "dishs_prepackNo_add.html?id=" + id;
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
		dishsNo.deletetWhole = function(id){
			 TZID = id;
			 CONFIRM.open().center();
		};
	 
	 /* 删除提交  */
		dishsNo.deleteRow = function(status){
			 if(status=="save"){
					$.ajax({
						url:HTTPPREFIX + "/dishsNo/deleteRow/"+TZID,
						type:"GET",
						dataType: "json",
						async:false,
						success:function(returnValue){
							if (returnValue.result.status == "true") {
								fsn.initNotificationMes("已成功删除！", true);
								initialize();
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
	 dishsNo.columns = [
	               {field:"dishsName", title:"菜品名称", width:"20%",filterable: false,editable: false},
	               {field: "alias", title: "别名", width:"20%",filterable: false,editable: false},
				   {field: "baching", title: "配料", width:"20%",filterable: false,editable: false},
				   {field:"",title:fsn.l("操作"), width: "25%",template:function(dataItem){
	               		var status=dataItem.outStatus;
		                     return "<a class='k-button k-button-icontext k-grid-ViewDetail ' onclick='fsn.dishsNo.checkUrl("+dataItem.id+");'><span class='k-icon k-edit' ></span>编辑</a>"+
		                     		"<a class='k-button k-button-icontext k-grid-ViewDetail '  onclick='fsn.dishsNo.deletetWhole("+dataItem.id+");'><span class='k-icon k-update'></span>删除</a>";
					   	}
					 }
		                  ];
	 initialize();
});
