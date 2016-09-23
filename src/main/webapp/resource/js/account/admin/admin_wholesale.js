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
        buildGridWioutToolBar("WHOLESALE_Grid",wholesale.columns,getStoreDS("",""),"400");//已确认批发的商品
        WHOLESALE_GRID = $("#WHOLESALE_Grid").data("kendoGrid");
	};
	
	
	 function getStoreDS(number,licOrName){
		 if(licOrName!=""){
			 licOrName = encodeURIComponent(licOrName);
		 }
		 WHOLESALE_DS = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
						return fsn.getHttpPrefix() +"/tzAccount/view/list/"+ options.page + "/" + options.pageSize + "?number="+number+"&licOrName="+licOrName+"&status=1";
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
		 window.location.href = "addWholesale.html";
	 };
 
	 //查询
	 wholesale.check = function(){
	 	var number = $.trim($("#number").val());
	 	var licOrName = $.trim($("#licOrName").val());
	 	WHOLESALE_GRID.setDataSource(getStoreDS(number,licOrName));
	 	$("#number").val("");
	 	$("#licOrName").val("");
	 	WHOLESALE_GRID.refresh();
	 };
	 
	 /* 查看 */
	 wholesale.checkUrl = function(id){
	    	window.open("admin_addWholesale.html?id=" + id);
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
						url:HTTPPREFIX + "/tzAccount/deleteRow/"+TZID+"/saleGoods",
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
	 wholesale.columns = [
	               {field: "id", title: "编号", width:"8%",filterable: false,editable: false},
	               {field:"inBusName", title:"购货商名称", width:"25%",filterable: false,editable: false},
	               {field: "licNo", title: "购货商执照号", width:"20%",filterable: false,editable: false},
	               {field: "createDate", title: "交易时间", width:"20%",filterable: false,editable: false},
				   {field: "outBusName", title: "供应商名称", width:"25%",filterable: false,editable: false},
				   {field: "buylicNo", title: "供应商执照号", width:"20%",filterable: false,editable: false},
				   {field:"",title:fsn.l("状态"), width: "10%",template:function(dataItem){
	               		var status=dataItem.inStatus;
	                   	 if(status==1){
	                   		 return "<span >已确认</span>";
	                      }else{
		                     return "<span style='color:red'>未确认</span>";
	                      	 }
					   	}
					 },
				   {field:"",title:fsn.l("操作"), width: "10%",template:function(dataItem){
	               		//var status=dataItem.outStatus;
	                   	 //if(status==1){
	                   	  return "<a class='k-button k-button-icontext k-grid-ViewDetail ' onclick='fsn.wholesale.checkUrl("+dataItem.id+");'><span class='k-icon k-edit' ></span>查看</a>";
	                     // }else{
		                     //return "<a class='k-button k-button-icontext k-grid-ViewDetail ' onclick='fsn.wholesale.checkUrl("+dataItem.id+");'><span class='k-icon k-edit' ></span>查看</a>"+
		                     		//"<a class='k-button k-button-icontext k-grid-ViewDetail '  onclick='fsn.wholesale.deletetWhole("+dataItem.id+");'><span class='k-icon k-update'></span>删除</a>";
	                      	// }
					   	}
					 }
		                  ];
	 initialize();
});
