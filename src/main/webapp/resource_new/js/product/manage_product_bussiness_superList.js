$(function() {
	var fsn = window.fsn = window.fsn || {};
	var upload = fsn.upload = fsn.upload || {};
	var product = upload.product = upload.product || {};
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	try {
		var arrayParam = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
		product.fromBusId = arrayParam[0];
		product.fromBuName = decodeURIComponent(arrayParam[1]);
		product.org = arrayParam[2];
    } catch(e) {}
    
	product.initialize = function(){
		upload.buildGridWioutToolBar("product_bussiness_super_grid",this.mytemplatecolumns, this.mytemplateDS,"350px");
		
		// 查找报告总数
		product.queryCountOfReport();
	};
	
	product.mytemplatecolumns = [
            {field: "name",title: fsn.l("Name"),width: 60},
            {field: "businessBrand.name",title: fsn.l("Owned Business Brand"),width: 40, filterable: false},
            {field: "format",title: fsn.l("Format"),width: 40, filterable: false},
            {field: "barcode",title: fsn.l("Barcode"),width: 40},
            {field: "cstm",title: fsn.l("Appropriate Crowd"),width: 40, filterable: false},
            {field: "lastModifyTime",title: fsn.l("产品上传时间"),template: '#=lastModifyTime=fsn.formatGridDate(lastModifyTime)#',width: 30, filterable: false},
            { command: [{name:"review",
            	    text:"<span class='k-icon k-cancel'></span>" + "查看报告详情", 
            	    click:function(e){
            	    	e.preventDefault();
            	    	var	dataItem = this.dataItem($(e.currentTarget).closest("tr"));
         			    /* 打开产品信息预览界面 */
            	    	window.open("/fsn-core/views/product_new/view_report_of_product_bussiness_superList.html?" + product.fromBusId + "&" + dataItem.id+"&" + product.fromBuName + "&" + product.org);
              }}], title: "操作", width: 30 }];
	
   	product.mytemplateDS = new kendo.data.DataSource({
   			transport: {
   	            read : {
   	                type : "GET",
   	                async:false,
   	                data:{flag:true},
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
	
   	/**
   	 * 查询报告总数 
   	 */
   	product.queryCountOfReport = function(){
   		var totalOfReports = 0;
   		
		$.ajax({
	        url: fsn.getHttpPrefix() + "/report/getKnownDealerCountsOfReports/" + product.fromBusId,
	        type: "GET",
	        data:{flag:true},
	        dataType: "json",
	        async: false,
	        contentType: "application/json; charset=utf-8",
	        success: function(returnValue) {
	            fsn.endTime = new Date();
	            if (returnValue.result.status == "true") {
	            	totalOfReports = returnValue.totalOfReports;
	            }
	        }
	    });
		var backUrl = "/fsn-core/views/erp_new/basic/customerList.html";
		var topTitle = "当前位置："+product.fromBuName+" &raquo; 供销产品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"上传报告总数：" + totalOfReports;
		    topTitle +="<div style=\"float: right;\"><a href="+backUrl+" target=_parent>返回</a></div>"
		$("#status_bar").html(topTitle);
   	};
	product.initialize();
});