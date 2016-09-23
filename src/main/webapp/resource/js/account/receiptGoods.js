$(function(){
	var fsn = window.fsn = window.fsn || {};
	var receiptGoods = fsn.receiptGoods = fsn.receiptGoods || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
	var CONFIRM = null;
	var TZID = null;
	var RECEIPT_GRID;
	var RECEIPT_DS;
	
	function initailize(){
		CONFIRM = initKendoWindow("CONFIRM_COMMON","300px", "100px", "收货提示！", false,true,false,["Close"],null,"");
		buildGridWioutToolBar("Recept_Grid",receiptGoods.columns,getStoreDS("",""),"400");//已确认批发的商品
		RECEIPT_GRID = $("#Recept_Grid").data("kendoGrid");
	};
	
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
	
	 function getStoreDS(number,licOrName){
		 if(licOrName!=""){
			 licOrName = encodeURIComponent(licOrName);
		 }
		 RECEIPT_DS = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
	                	return fsn.getHttpPrefix() + "/tzAccount/viewReceipt/list/" + options.page + "/" + options.pageSize + "?number="+number+"&licOrName="+licOrName;
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
		 return RECEIPT_DS;
	 }
	 
	 /* 查看 */
	 receiptGoods.checkUrl = function(id){
	    	window.location.href = "addReceiptGoods.html?id=" + id;
	 };
	 
	//显示字段
	receiptGoods.columns = [
	               {field: "id",title: "编号", width:"5%",filterable: false},
	               {field:"outBusName", title:"供应商名称",width:"20%",filterable: false},
	               {field: "licNo", title: "供应商执照号", width:"20%",filterable: false},
	               {field: "createDate", title: "交易时间", width:"20%",filterable: false},
				   {field: "inBusName", title: "购货商名称", width:"20%",filterable: false},
				   {field:"",title:fsn.l("状态"), width: "15%",template:function(dataItem){
	               		var status=dataItem.inStatus;
	                   	 if(status==1){
	                   		 return "<span >已确认</span>";
	                      }else{
		                     return "<span style='color:red'>未确认</span>";
	                      	 }
					   	}
					 },
				   {field:"",title:fsn.l("操作"), width: "25%",template:function(dataItem){
	                   	  return "<a class='k-button k-button-icontext k-grid-ViewDetail' onclick='fsn.receiptGoods.checkUrl("+dataItem.id+");'><span class='k-icon k-edit'></span>查看</a>";
					   	}
					 }
		        ];
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
		
		receiptGoods.submitReceipt = function(id){
			 TZID = id;
			 CONFIRM.open().center();
		};
		
		//查询
		receiptGoods.check = function(){
	    	var number = $.trim($("#number").val());
	    	var licOrName = $.trim($("#licOrName").val());
	    	RECEIPT_GRID.setDataSource(getStoreDS(number,licOrName));
	    	$("#number").val("");
	    	$("#licOrName").val("");
	    	RECEIPT_GRID.refresh();
	    };
	
	//确定收货
		receiptGoods.submit = function(status){
		if(status=="save"){
			$.ajax({
				url:HTTPPREFIX + "/tzAccount/submitReceipt/"+TZID,
				type:"GET",
				dataType: "json",
				async:false,
				success:function(returnValue){
					if (returnValue.result.status == "true") {
						fsn.initNotificationMes("已成功确认收货", true);
						initailize();
					} else {
						fsn.initNotificationMes("收货失败", false);
						initailize();
					}
				}
			});
			CONFIRM.close();
		}else{
			CONFIRM.close();
		}
	};
	
	initailize();
});
