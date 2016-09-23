$(function() {
	var upload = window.lims.upload = window.lims.upload || {};
	var businessSta = upload.businessSta = upload.businessSta || {};
	/*var filter = window.filter = window.filter || {};*/
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀

	businessSta.templatecolumns = [	     
	                 {field: "businessName",title: lims.l("BusinessName"),width: 120,filterable: false},
                     {field: "businessType",title: lims.l("BusinessType"),width: 100,filterable: false},
                     {field: "productQuantity",title: lims.l("productQuantity"),width: 100,filterable: false},
                     {field: "notPublishProQuantity",title: lims.l("NotPublishProQuantity"),width: 100,filterable: false},
                     {field: "reportQuantity",title: lims.l("ReportQuantity"),width: 100,filterable: false},
                     {field: "enterpriteDate",title: lims.l("EnterpriteDate"),width: 100,template:function(dataItem) {
                    	 if(dataItem.enterpriteDate){
                    		 return new Date(dataItem.enterpriteDate).format("YYYY-MM-dd");
                    	 }else{
                    		 return "";
                    	 }
                     }, filterable: false},
                     { command: [{                      
	                      text: lims.localized("Detail"),
						  click: function(e){
							  var editRow = $(e.target).closest("tr");
							  var temp = this.dataItem(editRow);
							  window.open(portal.CONTEXT_PATH+"/views/statistics/productStatistics-list.html?id="+temp.businessId);
						  }
					  },
		            ], title:lims.l("Operation"), width: 50 }
                     ];
	businessSta.templateDS = function(){
		return new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){
						return portal.HTTP_PREFIX + "/businessSta/businessStas/"+ options.page + "/" + options.pageSize ;
					},
					type : "GET",
	                dataType : "json",
	                contentType : "application/json",
	                data: {businessName:$("#businessName").val().trim(),
	        			businessType:$("#businessType").data("kendoDropDownList").text().trim(),
	        			startDate:$("#startDate").val().trim(),
	        			endDate:$("#endDate").val().trim(),},
	            },
	        },
	        schema: {
	        	 data : function(d) {
	                 return d.result.success?(d.data.businessStaList==null?null:d.data.businessStaList):null;  //响应到页面的数据
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
	businessSta.buildGrid=function(id,columns,ds){
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
			height: 320,
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
	businessSta.buildDatePicker=function(id){
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
	businessSta.search=function(){
		var dataSource = businessSta.templateDS();
		$("#businessStatistics").data("kendoGrid").setDataSource(dataSource);
	};
	businessSta.downExcel=function(){
		var data = new kendo.data.DataSource();
		data=$("#businessStatistics").data("kendoGrid").dataSource;
		if(data.total()>0){
			var businessName=$("#businessName").val().trim();
			if(businessName==""){
				businessName="null";
			}
			var businessType=$("#businessType").data("kendoDropDownList").text().trim();
			if(businessType==""){
				businessType="null";
			}
			var startDate=$("#startDate").val().trim();
			if(startDate==""){
				startDate="null";
			}
			var endDate=$("#endDate").val().trim();
			if(endDate==""){
				endDate="null";
			}
			var url = portal.HTTP_PREFIX + "/businessSta/downBusinessExcel/"+businessName+"/"+businessType+"/"+startDate+"/"+endDate;
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
	businessSta.reset=function(){
		$("#businessName").val("");
		$("#businessType").data("kendoDropDownList").value("0");
		$("#startDate").val("");
		$("#endDate").val("");
	};
	businessSta.initialize = function(){
		 $("#businessType").kendoDropDownList({
			dataTextField: "text",
            dataValueField: "value",
            dataSource: [
                         { text: "", value: "0" },
                         { text: "生产企业", value: "1" },
                         /*{ text: "仁怀市白酒生产企业", value: "2" },*/
                         { text: "流通企业", value: "2" },
                         { text: "餐饮企业", value: "3" }
                     ],
            index: 0
		});
		businessSta.buildDatePicker("startDate");
		businessSta.buildDatePicker("endDate");
		var dataSource = this.templateDS();
		businessSta.buildGrid("businessStatistics",this.templatecolumns, dataSource);
		$("#save_btn").bind("click",businessSta.search);
		$("#cancle_btn").bind("click",businessSta.reset);
		$("#downExcel").bind("click",businessSta.downExcel);
	};

	businessSta.initialize();
});