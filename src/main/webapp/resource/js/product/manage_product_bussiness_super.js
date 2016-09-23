$(function() {
	var fsn = window.fsn = window.fsn || {};
	var upload = fsn.upload = fsn.upload || {};
	var product = upload.product = upload.product || {};
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	/**
	 * 获取供应商企业id
	 * @author Zhanghui 2015/4/9
	 */
	try {
        product.fromBusId = $.cookie("view_0_product_by_fromBusId_and_toBusId").id;
    } catch(e) {}
    
	product.initialize = function(){
		upload.buildGrid("product_bussiness_super_grid",this.mytemplatecolumns, this.mytemplateDS());
	};
	
	product.mytemplatecolumns = [
			{field: "id",title: fsn.l("Id"),width: 1, filterable: false},
            {field: "name",title: fsn.l("Name"),width: 60, filterable: false},
            {field: "businessBrand.name",title: fsn.l("Owned Business Brand"),width: 40, filterable: false},
            {field: "format",title: fsn.l("Format"),width: 40, filterable: false},
            {field: "barcode",title: fsn.l("Barcode"),width: 40, filterable: false},
            {field: "cstm",title: fsn.l("Appropriate Crowd"),width: 40, filterable: false},
            { command: [{name:"review",
            	    text:"<span class='k-icon k-cancel'></span>" + "查看报告详情", 
            	    click:function(e){
            	    	e.preventDefault();
            	    	var	dataItem = this.dataItem($(e.currentTarget).closest("tr"));
            	    	/* 缓存产品id */
	            	    var product = {
	            			   id : dataItem.id
         			    };
         			    $.cookie("search_0_report_by_product", JSON.stringify(product), {
         				    	path : '/'
         			    });
         			    /* 打开产品信息预览界面 */
         			    window.open('/fsn-core/views/product/view_report_of_product_bussiness_super.html');
              }}], title: "操作", width: 30 }];
	
   	product.mytemplateDS = function(){
   		return new kendo.data.DataSource({
   			transport: {
   	            read : {
   	                type : "GET",
   	                async:false,
   	                url : function(options){
   	                	var configure = null;
   	                	if(options.filter){
   	            			configure = filter.configure(options.filter);
   	            		}
   	                	return portal.HTTP_PREFIX + "/product/getProductOfBusinessSuper/" + configure + "/" + options.page + "/" 
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
	};
	
	product.initialize();
});