$(function() {
	var upload = window.fsn.upload = window.fsn.upload || {};
	var product = upload.product = upload.product || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	//判断cookie不为空时设置为空
	try {
		var edit_id = $.cookie("user_0_edit_product").id;
		if(edit_id!=null){
			$.cookie("user_0_edit_product", JSON.stringify({}), {
			path : '/'
		});
		}
    } catch (e) {
    }
    
	product.initialize = function(){
		upload.buildGridByBoolbar("my_product_grid",this.mytemplatecolumns, this.mytemplateDS, 350, "toolbar_template");
		upload.buildGridByBoolbar("introduce_product_grid", portal.otherproductColumns, portal.otherProductDS, 350, "toolbar_template");
		product.initComponent();
		
		// 初始化[销往企业]多选框控件
		portal.initCustomerMultiSelect();
		portal.fillCustomerSelect();
	};
	
	product.mytemplatecolumns = [
			{field: "id",title: fsn.l("Id"),width: 25},
            {field: "name",title: fsn.l("Name"),width: 60},
            {field: "brandName",title: fsn.l("Owned Business Brand"),width: 40},
            {field: "format",title: fsn.l("Format"),width: 40},
            {field: "barcode",title: fsn.l("Barcode"),width: 40},
            {field: "cstm",title: fsn.l("Appropriate Crowd"),width: 40},
            {field: "selectedCustomerNames",title: fsn.l("selectedCustomers"),width: 80, filterable: false},
            {width:30,title: fsn.l("Operation"),
			      template:function(e){
					ids = e.id;
					var tag="<a  onclick='return fsn.portal.edit("+e.id+","+e.packageFlag+")' class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> </span>" + fsn.l('Edit') + "</a>";
					return tag;
				}
		    }];
	
   	product.mytemplateDS = new kendo.data.DataSource({
   			transport: {
   	            read : {
   	                type : "GET",
   	                async:false,
   	                url : function(options){
   	                	var configure = null;
   	                	if(options.filter){
   	            			configure = filter.configure(options.filter);
   	            		}
   	                	return portal.HTTP_PREFIX + "/product/getProducts/" + configure + "/" + options.page + "/" + options.pageSize;
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
   	        pageSize : 5, //每页显示个数
   	        serverPaging : true,
   	        serverFiltering : true,
   	        serverSorting : true
   		});
	
	portal.otherproductColumns = [
       						{field: "product.id",title: "序号",width: 1},
                            {field: "product.name",title: "名称",width: 60},
                            {field: "product.businessBrand.name",title: "所属品牌",width: 50},
                            {field: "product.format",title:"规格",width: 50},
                            {field: "product.barcode",title: "条形码",width: 60},
                            {field: "selectedCustomerNames",title: fsn.l("selectedCustomers"),width: 80, filterable: false},
                            { command: [
                                {
                               	  name:"review",
                    			  text:"<span class='k-icon k-cancel' ></span>" + fsn.l("Preview"), 
							  	  click: function(e){
										e.preventDefault();
										var editRow = $(e.target).closest("tr");
										var temp = this.dataItem(editRow);
										portal.viewProduct(temp.product.id);
									}
								},{
	                               	  name:"Edit",
	                    			  text:"<span class='k-icon k-edit' ></span>" + fsn.l("修改"), 
								  	  click: function(e){
											e.preventDefault();
											var editRow = $(e.target).closest("tr");
											var temp = this.dataItem(editRow);
											portal.barcode = temp.product.barcode;
											portal.editCustomers(temp.product.id);
									  }
									}
								], title:fsn.l("Operation"), width: 50 }
							];
	
	portal.otherProductDS = new kendo.data.DataSource({
		transport: {
			read: {
				type: "GET",
				async: false,
				url: function(options){
					var configure = null;
					if (options.filter) {
						configure = filter.configure(options.filter);
					}
					return fsn.HTTP_PREFIX + "/erp/initializeProduct/getInitProductByOrgAndLocal/false/" + configure + "/" + options.page + "/" + options.pageSize;
				},
				dataType: "json",
				contentType: "application/json"
			},
		},
		schema: {
			data: function(returnValue){
				return returnValue.data!=null?returnValue.data:[]; //响应到页面的数据
			},
			total: function(returnValue){
				return returnValue.counts; //总条数
			}
		},
		batch: true,
		page: 1,
		pageSize: 10, //每页显示个数
		serverPaging: true,
		serverFiltering: true,
		serverSorting: true
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
		
		$("#viewWindow").kendoWindow({
			width:1000,
			visible: false,
			title:fsn.l("Product preview"),
		});
		
		$("#editCustomerWindow").kendoWindow({
			width:450,
			height:150,
			visible: false,
			title:"修改产品的销往企业",
		});
		
		 // 修改引进产品的销往企业弹出框：确定和取消按钮
		 $("#edit_customer_yes_btn").click(function(){
			 var customerItems = $("#customerSelect").data("kendoMultiSelect").dataItems();
			 var customerIds = "";
			 for ( var entry in customerItems) {
				 customerIds += (customerItems[entry].id + ",");
			 }
			 var productVO = {
					barcode: portal.barcode,
					selectedCustomerIds: customerIds,
			 };
			 var success = portal.saveSelectCustomer(productVO);
			 if(success){
				 $("#editCustomerWindow").data("kendoWindow").close();
				 portal.otherProductDS.read();
				 fsn.initNotificationMes("更新成功！", true);
			 }else{
				 fsn.initNotificationMes("保存失败！", false);
			 }
		 });
		 
		 $("#edit_confirm_no_btn").click(function(){
			 $("#editCustomerWindow").data("kendoWindow").close();
		 });
	};
 	
	product.initialize();
});