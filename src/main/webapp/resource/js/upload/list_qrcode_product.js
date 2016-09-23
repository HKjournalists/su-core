$(document).ready(function(){
	var upload = window.fsn.upload = window.fsn.upload || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var root = fsn.root = fsn.root || {}; // 全局命名空间
	root.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	root.edit_id = null;
	//判断cookie不为空时设置为空
	 try {
		 root.edit_id = $.cookie("user_0_edit_product").id;
			if(root.edit_id!=null){
				$.cookie("user_0_edit_product", JSON.stringify({}), {
				path : '/fsn-core/views/portal/'
			});
			}
	    } catch (e) {
	    }
	/*初始化页面函数*/
	var initialize = function(){
		var listQRCodeProduct = getQRCodeProductDS();
		upload.buildGrid("qrCodeProductGrid",qRColumn,listQRCodeProduct);
	};
	
	/*第一个grid的列集合，二维码产品管理列表*/
	var qRColumn = [
	                {field: "product.id",title: fsn.l("Id"),width: 40},
	                {field: "product.name",title: fsn.l("Name"),width: 60},
	                {field: "product.businessBrand.name",title: fsn.l("Brand"),width: 60},
	                {field: "product.format",title: fsn.l("Format"),width: 50},
	                {field: "product.barcode",title: fsn.l("Barcode"),width: 40},
	                {field: "product.cstm",title: fsn.l("Appropriate Crowd"),width: 40},
	                {field: "product.ingredient",title: fsn.l("Ingredient"),width: 100},
	                { command: [{name:"edit",text: fsn.l("Edit"),
	                	click: function(e){
	                		/*编辑事件*/
	                		var editRow = $(e.target).closest("tr");
	                		var temp = this.dataItem(editRow);
	                		/*编辑二维码产品信息*/
	                		editQRCodeProductFun(temp.product.id);
	                	}
	                },
	                ], title:fsn.l("Operation"), width: 40 }
	       ];
	
	/*定义方法，返回一个kendoDateSource,获取二维码产品列表*/
	var getQRCodeProductDS = function (){
		return new kendo.data.DataSource({
   			transport: {
   	            read : {
   	                type : "GET",
   	                url : function(options){
   	                	var configure = null;
   	                	if(options.filter){
   	            			configure = filter.configure(options.filter);
   	            		}
   	                	return root.HTTP_PREFIX + "/product/getListQRCodeProductByOrg/" + options.page + "/" + options.pageSize + "/" +configure;
   	                },
   	                dataType : "json",
   	                contentType : "application/json"
   	            },
   	        },
   	        schema: {
   	        	 data : function(returnValue) {
   	        		 return returnValue.data;  //响应到页面的数据
   	             },
   	             total : function(returnValue) {
   	            	 return returnValue.counts;   //总条数
   	             }         
   	        },
   	        batch : true,
   	        page:1,
   	        pageSize : 10, //每页显示个数
   	        serverPaging : true,
   	        serverFiltering : true,
   	        serverSorting : true
   		});
	};
	
	/*二维码产品的编辑事件*/
	var editQRCodeProductFun = function(id){
		var product = {
			id : id
		};
		$.cookie("user_0_edit_product", JSON.stringify(product), {
			path :'/fsn-core/views/portal/'
		});
		window.location.pathname = "/fsn-core/views/portal/add-qrcode-product.html";
	};
	
	initialize();
});