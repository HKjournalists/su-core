$(function() {
	var upload = window.lims.upload = window.lims.upload || {};
	var proReport = upload.proReport = upload.proReport || {};
	var filter = window.filter = window.filter || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	proReport.configure=null;
	proReport.templatecolumns = [	     
	                 {field: "serviceOrder",title: lims.l("ServiceOrder"),width: 80},
                     {field: "sample.product.barcode",title: lims.l("Barcode"),width: 80},
                     {field: "sample.product.name",title: lims.l("ProName"),width: 80},
                     {field: "testType",title: lims.l("TestType"),width: 75},
                     {field: "sample.product.format",title: lims.l("Format"),width: 60},
                     {field: "sample.producer.name",title: lims.l("ProducerName"),width: 80},
                     {field: "sample.product.businessBrand.name",title: lims.l("BusinessBrand"),width: 50},
                     {field: "sample.batchSerialNo",title: lims.l("BatchSerialNo"),width: 80},
                     {field: "sample.productionDate",title: lims.l("ProductionDate"),width: 80},
                     {field: "testDate",title: lims.l("TestDate"),width: 80},
                     {field: "publishFlag",title: lims.l("PublishFlag"),width: 75,template:function(dataItem) {
                    	 if(dataItem.publishFlag=='1'){
                    		 return "已发布";
                    	 }else if(dataItem.publishFlag=='0'){
                    		 return "未发布";
                    	 }
                     }},
                     { command: [{                      
	                      text: lims.localized("Detail"),
						  click: function(e){
							  var editRow = $(e.target).closest("tr");
							  var temp = this.dataItem(editRow);
							  //var pictureFtpPath=fsn.getPictureFtpPath();
							  //window.location.href = pictureFtpPath+"/portal"+temp.pdfReport;
							  window.open(temp.fullPdfPath);
						  }
					  },
		            ], title:lims.l("Operation"), width: 80 }
                     ];
	proReport.templateDS = function(){
		return new kendo.data.DataSource({
			transport: {
	            read: {
	            	url :function(options){
	            		var configure=null;
	            		if(options.filter){
	            		 configure = filter.configure(options.filter).replace(".","_");
	            		 configure=configure.replace(".","_");
	            		 proReport.configure=configure;
	            		}else{
	            			proReport.configure=null;
	            		}
						return portal.HTTP_PREFIX + "/proViSta/proReport/" + options.page + "/" + options.pageSize + "/"+configure ;
					},
					type : "GET",
	                dataType : "json",
	                contentType : "application/json",
	                /*data: {proName:$("#proName").val().trim(),
	                	barcode:$("#barcode").val().trim(),
	                	format:$("#format").val().trim(),
	                	producer:$("#producer").val().trim(),},*/
	            },
	        },
	        schema: {
	        	 data : function(d) {
	                 return d.result.success?(d.data.proReportList==null?null:d.data.proReportList):null;  //响应到页面的数据
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
	proReport.buildGrid=function(id,columns,ds){
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

	/*proReport.search=function(){
		var dataSource = proReport.templateDS();
		$("#businessStatistics").data("kendoGrid").setDataSource(dataSource);
	};*/
	proReport.downExcel=function(){
		var data = new kendo.data.DataSource();
		data=$("#proReportQuery").data("kendoGrid").dataSource;
		if(data.total()>0){
			//var url = portal.HTTP_PREFIX + "/businessSta/downBusinessExcel/"+proReport.configure;
			var url = portal.HTTP_PREFIX + "/proViSta/downExcel/"+proReport.configure;
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
	/*proReport.reset=function(){
		$("#proName").val("");
		$("#barcode").val("");
		$("#format").val("");
		$("#producer").val("");
		$("#reportNO").val("");
	};*/
	proReport.initialize = function(){
		var dataSource = this.templateDS();
		proReport.buildGrid("proReportQuery",this.templatecolumns, dataSource);
		/*$("#save_btn").bind("click",proReport.search);
		$("#cancle_btn").bind("click",proReport.reset);*/
		$("#downExcel").bind("click",proReport.downExcel);
	};
	proReport.initialize();
});