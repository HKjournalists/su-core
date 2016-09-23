$(function(){
	
	//st_ordertype.js cgw
	//describe：单别的增、删、查
	var fsn = window.fsn = window.fsn || {};
	var eWidget = fsn.eWidget = fsn.eWidget||{};
	var common = fsn.common = fsn.common || {};
	var st_ordertype = fsn.st_ordertype = fsn.st_ordertype || {};
	$.extend(st_ordertype,common);
	
	st_ordertype.datasource = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
				url : function(options){
                	if(options.filter){
                		var configure = filter.configure(options.filter);
                 		return fsn.getHttpPrefix() + "/erp/customer/" + st_ordertype.SIMPLE_TYPE + "/reglist/" + options.page + "/" + options.pageSize + "/"+configure;;
                	};
                	return fsn.getHttpPrefix() + "/erp/customer/" + st_ordertype.SIMPLE_TYPE + "/list";
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
        			 ot_id:{type:"number"},
        			 ot_type:{type:"string"},
        			 ot_describe:{type:"string"},
        			 ot_belong_module:{type:"string"},
        			 ot_belong_order:{type:"string"}
                 }
            },
        	data : function(data) {
                return data.result.listOfModel;  
            },
            total : function(data) {
               return  data.result.count;//总条数
            }  
        },
        batch : true,
        page:1,
        pageSize : 10, //每页显示个数
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	st_ordertype.columns = [
	               {field: "ot_id", title:"单别ID", width:"1px",editor:false},
	               {field: "ot_type", title: "单别名称", width:"auto"},
	               {field: "ot_describe", title: "单别描述", width:"auto"},
	               {field: "ot_belong_module", title: "所属模块", width:"auto"},
	               {field: "ot_belong_order", title: "所属单据", width:"auto"},
	               ];
	
	/**
	 * add, delete
	 */
	
	st_ordertype.selectedData = {};
	
	
	st_ordertype.clearForm = function(){
		$("#name").kendoValidator().data("kendoValidator").hideMessages();
		$("#describe").kendoValidator().data("kendoValidator").hideMessages();
		$("#id").val(0);
		$("#name").val("");
		$("#describe").val("");
		$("#module").data("kendoDropDownList").value("----请选择模块----"); 
	}
	
	st_ordertype.bindForm = function(){
		$("[data-bind='value:id']").val(st_ordertype.selectedData.id);
		$("[data-bind='value:name']").val(st_ordertype.selectedData.name);
		$("[data-bind='value:describe']").val(st_ordertype.selectedData.describe);
		$("[data-bind='value:module']").val(st_ordertype.selectedData.module);
		$("[data-bind='value:order']").val(st_ordertype.selectedData.order);
	}
	
	$("#module").kendoDropDownList({
	    dataTextField: "parentName",
	    dataValueField: "parentId",
	    index:0,
	    dataSource: [
	        { parentName: "----请选择模块----", parentId: "----请选择模块----" },
	        { parentName: "销售模块", parentId: "销售模块" },
	        { parentName: "库存模块", parentId: "库存模块" },
	        { parentName: "采购模块", parentId: "采购模块" }
	    ]
	});

	 $("#order").kendoDropDownList({
	    cascadeFrom: "module",
	    dataTextField: "childName",
	    dataValueField: "childName",
	    dataSource: [
//	        { childName: "销售订单/合同管理",  parentId: "销售模块" },
//	        { childName: "销售单/出货通知单管理",  parentId: "销售模块" },
//	        { childName: "送检单管理",  parentId: "销售模块" },
	        { childName: "出货单/发货单管理",  parentId: "销售模块" },
//	        { childName: "销退换货管理",  parentId: "销售模块" },
	        { childName: "商品出库管理",  parentId: "库存模块" },
	        { childName: "商品入库管理",  parentId: "库存模块" },
	        { childName: "调拨管理",  parentId: "库存模块" },
//	        { childName: "请购单管理",  parentId: "采购模块" },
//	        { childName: "采购单管理",  parentId: "采购模块" },
	        { childName: "收货单管理",  parentId: "采购模块" },
	        { childName: "---请选择单据---", parentId: "----请选择模块----" }
	    ]
	});
	
	//保存
	st_ordertype.windowSaveConfrim = function(){
		//validation
		var module = $("#module").val();
		var type = $("#name").val();
		var describe = $("#describe").val();
		if(st_ordertype.validator.validate()&&module != "----请选择模块----"){
			//fsn.showNotificationMes("Sucess",true);
			var model = {};
			model.ordertype = {
					ot_type:type,
					ot_describe:describe,
					ot_belong_module:module,
					ot_belong_order:$("#order").val()
			};
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/customer/" + st_ordertype.SIMPLE_TYPE,
				type:"POST",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				data:JSON.stringify(model),
				success:function(data){	
					if(data.result.status == "true"){
						fsn.initNotificationMes(data.result.message, true);
						$("#OPERATION_WIN").data("kendoWindow").close();	
						st_ordertype.datasource.read();
					}else{
						fsn.initNotificationMes(data.result.errorMessage, false);
					}
				
				}
			});
		}else{
			if(type == ""){
				fsn.initNotificationMes("单别不能为空",false);
			} else if(!$("#name").kendoValidator().data("kendoValidator").validate()){
				fsn.initNotificationMes("单别必须为3位",false);
			} else if(describe == ""){
				fsn.initNotificationMes("描述不能为空",false);
			} else{
				fsn.initNotificationMes("请先选择模块",false);
			}
		}
	}
	
	//delete
	st_ordertype.windowDeleteConfrim = function(){
		var model = {
				ordertype:{
					ot_id:st_ordertype.selectedData.ot_id
				}
		};
		$.ajax({
			url: fsn.getHttpPrefix() + "/erp/customer/" + st_ordertype.SIMPLE_TYPE,
			type:"DELETE",
			dataType:"json",
			contentType: "application/json; charset=utf-8",
			data:JSON.stringify(model),
			success:function(data){
				if(data.result.status == "true"){
					st_ordertype.datasource.remove(st_ordertype.grid.dataItem(st_ordertype.grid.select()));
					fsn.initNotificationMes(data.result.message, true);
				}else{
					fsn.initNotificationMes(data.result.errorMessage, false);
				}
			}
		});
	}
	st_ordertype.GRIDID = "Simple_Grid";
	st_ordertype.DAILOGID = "OPERATION_WIN"; 
	st_ordertype.initailize = function(){
		this.initComponent(st_ordertype);
	}
	st_ordertype.initailize();
//	$('#search').kendoSearchBox({
//				change : function(e) {
//					var keywords=e.sender.options.value;
//			    	if(keywords.trim()!=""){
//			    		$("#Simple_Grid").data("kendoGrid").setDataSource(initStandard(keywords));
//			    	    $("#Simple_Grid").data("kendoGrid").refresh();
//			    	}else{
//			    		$("#Simple_Grid").data("kendoGrid").setDataSource(st_ordertype.datasource);
//			    		$("#Simple_Grid").data("kendoGrid").refresh();
//			    	}
//				}
//			});
			
			initStandard = function(keywords){
			var ds = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
                url : function(options){
//                	if(options.filter){
//                		var configure = filter.configure(options.filter);
//                 		return fsn.getHttpPrefix() + "/model/" + st.SIMPLE_TYPE + "/reglist/" + options.page + "/" + options.pageSize + "/"+configure;;
//                	};//filter cgw
					return fsn.getHttpPrefix() + "/erp/customer/" + st_ordertype.SIMPLE_TYPE + "/search?keywords="+keywords;
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
        			 ot_id:{type:"number"},
        			 ot_type:{type:"string"},
        			 ot_describe:{type:"string"},
        			 ot_belong_module:{type:"string"},
        			 ot_belong_order:{type:"string"}
                 }
            },
        	data : function(data) {
                return data.result.listOfModel;  
            },
            total : function(data) {
               return  data.result.count;//总条数
            }  
        },
        batch : true,
        page:1,
        pageSize : 10, //每页显示个数
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
				//ds.read();
				return ds;
			}
})