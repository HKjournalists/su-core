$(function() {
	var fsn = window.fsn = window.fsn || {};
	var productSta = fsn.productSta = fsn.productSta || {};
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	productSta.editBuId=null;
	productSta.templatecolumns = [	     
	                 {field: "productName",title: lims.l("ProductName"),width: 120,filterable: false},
                     {field: "barcode",title: lims.l("Barcode"),width: 100,filterable: false},
                     {field: "reportQuantity",title: lims.l("ReportQuantity"),width: 80,filterable: false},
                     {field: "notPublishReportQuantity",title: lims.l("NotPublishReportQuantity"),width: 80,filterable: false},
                     {field: "lastPubDate",title: lims.l("LastPubDate"),width: 80,template:function(dataItem) {
                    	 if(dataItem.lastPubDate){
                    		 return new Date(dataItem.lastPubDate).format("YYYY-MM-dd");
                    	 }else{
                    		 return "";
                    	 }
                     },filterable: false}
                     ];
	productSta.templateDS = function(){
		return new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){	      
						return portal.HTTP_PREFIX + "/businessSta/productStas/" + options.page + "/" + options.pageSize ;
	            	},
					type : "GET",
	                dataType : "json",
	                contentType : "application/json",
	                data: {businessId:productSta.editBuId,
	        			productName:$("#productName").val().trim(),
	        			barcode:$("#barcode").val().trim(),
	        			startDate:$("#startDate").val().trim(),
	        			endDate:$("#endDate").val().trim(),}
	            }
	        },
	        schema: {
	        	 data : function(d) {
	        		 $("#businessName").val(d.data.buName);
	                 return d.result.success?(d.data.productStaList==null?null:d.data.productStaList):null;  //响应到页面的数据
	             },
	             total : function(d) {
	                 return d.result.success?d.data.totalCount:0;   //总条数
	             }         
	        },
	        batch : true,
	        pageSize : 5, //每页显示个数
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
	};
	productSta.buildGrid=function(id,columns,ds){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ?{data:[],pageSize:10}:ds,//{data:[],pageSize:5},
			filterable: {
				messages: lims.gridfilterMessage(),
				operators: {
					string: lims.gridfilterOperators(),
				    date: lims.gridfilterOperatorsDate(),
				    number: lims.gridfilterOperatorsNumber()
				}
			},
			height: 260,
			toolbar: [
	        		  {template: kendo.template($("#toolbar_template").html())}
	                  ],
	        sortable: true,
	        resizable: true,
	        selectable: true,
	        pageable: {
	            refresh: true,
	            pageSizes: true,
	            messages: lims.gridPageMessage(),
	        },
	        columns: columns
		});
	};
	productSta.buildDatePicker=function(id){
		$("#"+id).kendoDatePicker({
	    	 format: "yyyy-MM-dd", 
	    	 height:25,
	    	 culture : "zh-CN",
	    	 animation: {
	    	   close: {
	    	     effects: "fadeOut zoom:out",
	    	     duration: 300
	    	   },
	    	   open: {
	    	     effects: "fadeIn zoom:in",
	    	     duration: 300
	    	   }
	    	  }
	    	});
	};
	productSta.search=function(){
		var dataSource = productSta.templateDS();
		$("#productStatistics").data("kendoGrid").setDataSource(dataSource);
	};
	productSta.downExcel=function(){
		var data = new kendo.data.DataSource();
		data=$("#productStatistics").data("kendoGrid").dataSource;
		if(data.total()>0){
			var productName=$("#productName").val().trim();
			if(productName==""){
				productName="null";
			}
			var barcode=$("#barcode").val().trim();
			if(barcode==""){
				barcode="null";
			}
			var startDate=$("#startDate").val().trim();
			if(startDate==""){
				startDate="null";
			}
			var endDate=$("#endDate").val().trim();
			if(endDate==""){
				endDate="null";
			}
			var url = portal.HTTP_PREFIX + "/businessSta/downProductExcel/"+productName+"/"+barcode+"/"+startDate+"/"+endDate+"/"+productSta.editBuId;
            var hiddenIFrameID = 'hiddenDownloader',
            iframe = document.getElementById(hiddenIFrameID);
            if (iframe === null) {
                iframe = document.createElement('iframe');
                iframe.id = hiddenIFrameID;
                iframe.style.display = 'none';
                document.body.appendChild(iframe);
            }
            iframe.src = url;
			
		}else{
			alert("没有查询到数据！");
		}
	};
	productSta.reset=function(){
		$("#productName").val("");
		$("#barcode").val("");
		$("#startDate").val("");
		$("#endDate").val("");
	};
	productSta.initialize = function(){
		var params = fsn.getUrlVars();
		productSta.editBuId=params[params[0]];
		productSta.buildDatePicker("startDate");
		productSta.buildDatePicker("endDate");
		var dataSource = this.templateDS();
		productSta.buildGrid("productStatistics",this.templatecolumns, dataSource);
		$("#save_btn").bind("click",productSta.search);
		 $("#cancle_btn").bind("click",productSta.reset);
		 $("#downExcel").bind("click",productSta.downExcel);
	};

	productSta.initialize();
});