$(function() {
	var upload = window.fsn.upload = window.fsn.upload || {};
	var product = upload.product = upload.product || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	portal.type = false;
	//判断cookie不为空时设置为空
	try {
		var edit_id = $.cookie("user_0_edit_product").id;
		if(edit_id!=null){
			$.cookie("user_0_edit_product", JSON.stringify({}), {
			path : '/fsn-core/views/portal/'
		});
		}
    } catch (e) {
    }
    
	product.initialize = function(){
		/* 初始化销往客户弹出框*/
		 fsn.initKendoWindow("customer_select_window","修改销往客户","550px","410px",false,null);
		 $("#viewWindow").kendoWindow({
				width:1000,
				visible: false,
				title:fsn.l("Product preview"),
			});
			
			$("#delProductWindow").kendoWindow({
				width:360,
				height:150,
				visible: false,
				title:"删除产品",
			});
			portal.currentBusiness = getCurrentBusiness();
			var toolbar_template = "toolbar_template";
			if(portal.currentBusiness!=undefined&&portal.currentBusiness!=null&&portal.currentBusiness.type.trim().indexOf("流通企业.商超")!=-1){
				toolbar_template="";
				portal.type = true;
			}
		upload.buildGridByBoolbar("my_product_grid",portal.mytemplatecolumns, portal.mytemplateDS, "460px", toolbar_template);
		product.initComponent();
		
		// 初始化[销往企业]多选框控件
		portal.initCustomerMultiSelect();
		//portal.fillCustomerSelect();
	};
	
	portal.mytemplatecolumns = [
			/*{field: "id",title: fsn.l("Id"),width: 1},*/
            {field: "name",title: fsn.l("Name"),width: 40},
            {field: "brandName",title: fsn.l("Owned Business Brand"),width: 40},
            {field: "barcode",title: fsn.l("Barcode"),width: 60},
            {field: "selectedCustomerNames",title: fsn.l("selectedCustomers"),width: 90, filterable: false},
            {width:75,title: fsn.l("Operation"),
			      template:function(e){
			    	var tag = "";
			    	if(e.local == "true"){
			    		tag += "<a  onclick='return fsn.portal.edit("+e.id+","+e.packageFlag+")' " +
			    				"class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> " +
			    				"</span>" + fsn.l('Edit') + "</a>";
			    	}else
					  if(e.local == "false"){
						  tag += "<a  onclick='return fsn.portal.viewProduct("+e.id+")' " +
						  "class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> " +
						  "</span>" + fsn.l('Preview') + "</a>";
					  }
					  if(!portal.type){
			    		tag += "<a  onclick='return fsn.portal.editCustomers("+e.id+",\""+e.barcode+"\")' " +
			    		"class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> " +
			    		"</span>修改客户</a>";
			    		tag += "<a onclick='return portal.delProduct("+e.id+")' " +
			    		"class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> " +
			    		"</span>删除</a>";
			    	}
					return tag;
				}
		    }];
	
	portal.mytemplateDS = new kendo.data.DataSource({
   			transport: {
   	            read : {
   	                type : "GET",
   	                async:false,
   	                url : function(options){
   	                	var configure = null;
   	                	if(options.filter){
   	            			configure = filter.configure(options.filter);
   	            		}
   	                	return portal.HTTP_PREFIX + "/product/getAllProducts/" + configure + "/" + options.page + "/" + options.pageSize;
   	                },
   	                dataType : "json",
   	                contentType : "application/json"
   	            },
   	        },
   	        schema: {
   	        	 data : function(returnValue) {
   	        		 return returnValue.data.listOfProduct;  //响应到页面的数据
   	             },
   	             total : function(returnValue) {
   	            	 return returnValue.data.counts;   //总条数
   	             }
   	        },
   	        batch : true,
   	        page:1,
   	        pageSize : 10, //每页显示个数
   	        serverPaging : true,
   	        serverFiltering : true,
   	        serverSorting : true
   		});
	
	product.initComponent = function(){
		$("#product-nutri-grid").kendoGrid({
			dataSource:[],
	        editable: false,
	        pageSize : 10,
	        pageable: {
	            refresh: true,
	            pageSizes: 5,
	            messages: fsn.gridPageMessage(),
	        },
	        columns: [
	                {field: "name", title: "项目",width:70},
	                {field: "value", title: "值",width:50},
	                {field: "unit", title:"单位",width:50},
	                {field: "per", title: "条件[如：每100(mL)]", width: 100},
	                {field: "nrv", title: "NRV(%)",width:50}]
		});
		
		portal.delProduct = function(proId){
			$("#tempProductId").val(proId);
			$("#delProductWindow").data("kendoWindow").open().center();
		};
		
		/* 确定删除产品 */
		$("#delProduct_yes_btn").click(function(){
			portal.deleteProduct($("#tempProductId").val().trim());//删除产品
			$("#delProductWindow").data("kendoWindow").close();
		});
		
		$("#delProduct_no_btn").click(function(){
			$("#delProductWindow").data("kendoWindow").close();
		});
		
		 // 修改引进产品的销往企业弹出框：确定和取消按钮
		 $("#btn_customer_select_save").click(function(){
			 var customerItems = $("#customerSelectInfo").data("kendoMultiSelect").dataItems();
			 if(customerItems.length <1){
				 fsn.initNotificationMes("请选择销往客户！", false);
				 return;
			 }
			 var customerIds = "";
			 for ( var entry in customerItems) {
				 customerIds += (customerItems[entry].id + ",");
			 }
			 var productVO = {
					barcode: portal.barcode,
					selectedCustomerIds: customerIds,
			 };
			 var result = portal.saveSelectCustomer(productVO);
			 if(result!=null && result.status == "true"){
				 $("#customer_select_window").data("kendoWindow").close();
				 portal.mytemplateDS.read();
				 
				 if(result.message != ""){
					 fsn.initNotificationMes(result.message, true);
				 }
				 
				 if(result.errorMessage != ""){
					 fsn.initNotificationMes(result.errorMessage, false);
				 }
				 
				 if(result.message=="" && result.errorMessage==""){
					 fsn.initNotificationMes("更新成功！", true);
				 }
			 }else{
				 fsn.initNotificationMes("保存失败！", false);
			 }
		 });
		 
		 $("#btn_customer_select_cancel").click(function(){
			 $("#customer_select_window").data("kendoWindow").close();
		 });
		 
	};
	
	/**
 * 编辑
 * @author tangxin
 */
portal.edit = function(id,packageFlag){
	/* md5加密pid */ 
	var canshu = "?"+id+"&"+$.md5(""+id);
	
	/**
	 * 产品的包装标志：0：预包装、1：散装、2：无条码
	 * 根据产品的不同跳转到不同的编辑界面
	 */
	if(packageFlag=='0'){
		window.location.href = "/fsn-core/views/portal_new/product.html"+canshu;
	}else{
		window.location.href = "/fsn-core/views/portal/add-qrcode-product.html"+canshu;
	}
};
 	
	product.initialize();
});