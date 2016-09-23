$(function() {
	var fsn = window.fsn = window.fsn || {};
	var upload = fsn.upload = fsn.upload || {};
	var product = upload.product = upload.product || {};
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	try {
		var arrayParam = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
		product.fromBusId = arrayParam[0];
		product.providername = decodeURIComponent(arrayParam[1]);
    } catch(e) {}
    
	product.initialize = function(){
		upload.buildGridByBoolbar("product_bussiness_super_grid",this.mytemplatecolumns, this.mytemplateDS,"350px","toolbar_template_back");
		if(product.providername){
			$("#status_bar").html("当前位置：" + product.providername + ">>供销产品>>报告待处理产品");
		}
		$("#back").bind("click",function(){
			window.location.href = "/fsn-core/views/erp_new/basic/customer_onHandleProducer.html?type=3&name=供应商";
		});
	};
	
	product.mytemplatecolumns = [
            {field: "name",title: fsn.l("Name"),width: 60},
            {field: "businessBrand.name",title: fsn.l("Owned Business Brand"),width: 40, filterable: false},
            {field: "format",title: fsn.l("Format"),width: 40, filterable: false},
            {field: "barcode",title: fsn.l("Barcode"),width: 40,},
            {field: "cstm",title: fsn.l("Appropriate Crowd"),width: 40, filterable: false},
            {field: "lastModifyTime",title: fsn.l("上传日期"), template:'#= new Date(lastModifyTime).format("YYYY-MM-dd hh:mm:ss")#',width: 45, filterable: false
//            	filterable: {
//            		ui: function(element) {
//            	        element.kendoDatePicker({
//            	        	format: "yyyy-MM-dd"
//            	        });
//            	    }
//            	}
            },
            { command: [{name:"review",
            	    text:"<span class='k-icon k-cancel'></span>" + "查看报告详情", 
            	    click:function(e){
            	    	e.preventDefault();
            	    	var	dataItem = this.dataItem($(e.currentTarget).closest("tr"));
         			    /* 打开产品信息预览界面 */
            	    	window.open("/fsn-core/views/product_new/view_report_of_product_bussiness_super.html?" + product.fromBusId + "&" + dataItem.id+"&"+product.providername);
              }}], title: "操作", width: 30 }];
	
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
   	                	return portal.HTTP_PREFIX + "/product/getProductOfBusinessSuper/onHandle/" + configure + "/" + options.page + "/" 
   	                					+ options.pageSize + "/" + product.fromBusId;
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
	
	product.initialize();
});