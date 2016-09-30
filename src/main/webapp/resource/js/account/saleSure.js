$(function(){
	var fsn = window.fsn = window.fsn || {};
	var saleSure = fsn.saleSure = fsn.saleSure || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
	var SALESURE_DS = [];
	var SALESURE_GRID = null;
	var TZID = null;
	var CONFIRM = null;
	 function initialize(){
		 	CONFIRM = initKendoWindow("CONFIRM_COMMON","300px", "100px", "删除提示！", false,true,false,["Close"],null,"");
	        buildGridWioutToolBar("SALESURE_GRID",saleSure.columns,getStoreDS("",""),"400");//已确认销售的商品数量
		    SALESURE_GRID = $("#SALESURE_GRID").data("kendoGrid");
	  };
	
	 function getStoreDS(number,licOrName){
		 if(licOrName!=""){
			 licOrName = encodeURIComponent(licOrName);
		 }
		 SALESURE_DS = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
//						return fsn.getHttpPrefix() +"/tzAccount/getSaleSureList/"+ options.page + "/" + options.pageSize ;
						return fsn.getHttpPrefix() +"/tzAccount/viewGYS/list/"+ options.page + "/" + options.pageSize + "?number="+number+"&licOrName="+licOrName+"&status=0";
					},
	                dataType : "json",
	                contentType : "application/json"
	            }
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
	        serverFiltering : false,
	        serverSorting : false
		});
		 return SALESURE_DS;
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
	saleSure.add = function(){
    	window.location.href = "addpurchase.html";
    };
    
    //查询
	saleSure.check = function(){
    	var number = $.trim($("#number").val());
    	var licOrName = $.trim($("#licOrName").val());
		SALESURE_GRID.setDataSource(getStoreDS(number,licOrName));
    	 $("#number").val("");
    	 $("#licOrName").val("");
		SALESURE_GRID.refresh();
    };
    
    /* 查看 */
	saleSure.checkUrl = function(id){
	    	window.location.href = "addpurchase.html?id=" + id;
	 };
	 
	 /**
	  * 生产商确认供应商进货台账
	  */
	 saleSure.sure = function(id){
		 $.ajax({
				url:HTTPPREFIX + "/tzAccount/saleSure/"+id,
				type:"GET",
				dataType: "json",
				async:false,
				success:function(returnValue){
					if (returnValue.result== true) {
						fsn.initNotificationMes("确认成功！", true);
						initialize();
					} else {
						fsn.initNotificationMes("确认失败", false);
						initialize();
					}
				}
			});
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
	saleSure.deletetPurchase = function(id){
			 TZID = id;
			 CONFIRM.open().center();
		};
	 
	 /* 删除提交  */
	saleSure.deleteRow = function(status){
		 if(status=="save"){
				$.ajax({
					url:HTTPPREFIX + "/tzAccount/deleteRow/"+TZID+"/buyGoods",
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
	saleSure.columns = [
	               {field: "id", title: "编号", width:"10%",filterable: false},
	               {field:"inBusName", title:"供应商名称",width:"20%",filterable: false},
	               {field: "licNo", title: "供应商执照号", width:"20%",filterable: false},
	               {field: "createDate", title: "交易时间", width:"20%",filterable: false},
//				   {field: "productNum", title: "交易数量", width:"20%",filterable: false},
				   {field:"",title:fsn.l("状态"), width: "20%",template:function(dataItem){
	               		var status=dataItem.outStatus;
	                   	 if(status==1){
	                   		 return "<span >已确认</span>";
	                      }else{
		                     return "<span style='color:red'>未确认</span>";
	                      	 }
					   	}
					 },
				   {field:"",title:fsn.l("操作"), width: "40%",template:function(dataItem){
	               		var status=dataItem.outStatus;
	                   	 if(status==0){
	                   	  return "<a class='k-button k-button-icontext k-grid-ViewDetail' onclick='fsn.saleSure.sure("+dataItem.id+");'><span class='k-icon k-edit' ></span>确认</a>"+
	                   	  		 "<a class='k-button k-button-icontext k-grid-ViewDetail' onclick='fsn.saleSure.checkUrl("+dataItem.id+");'><span class='k-icon k-edit' ></span>查看</a>";
	                      }else{
		                     return "<a class='k-button k-button-icontext k-grid-ViewDetail' onclick='fsn.saleSure.checkUrl("+dataItem.id+");'><span class='k-icon k-edit' ></span>查看</a>";
//		                     		"<a class='k-button k-button-icontext k-grid-ViewDetail'  onclick='fsn.saleSure.deletetPurchase("+dataItem.id+");'><span class='k-icon k-update'></span>删除</a>";
	                      	 }
					   	}
					 }
		          ];
    initialize();
});
