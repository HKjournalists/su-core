$(function() {
	var upload = window.lims.upload = window.lims.upload || {};
	var proViSta = upload.proViSta = upload.proViSta || {};
	var filter = window.filter = window.filter || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀

	proViSta.templatecolumns = [
	                 {field: "id",title: lims.l("Id"),width: 40,filterable: true},
	                 {field: "product.name",title: lims.l("ProName"),width: 100,filterable: true},
                     {field: "product.barcode",title: lims.l("Barcode"),width: 100,filterable: true},
                     {field: "appStatistics",title: lims.l("AppStatistics"),width: 100,filterable: true},
                     {field: "portalStatistics",title: lims.l("PortalStatistics"),width: 80,filterable: true}
                     ];
	proViSta.templateDS = function(keyword){
		return new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){
	            		var configure=null;
	            		if(options.filter){
	            		 configure = filter.configure(options.filter);	            
	            		}
						return portal.HTTP_PREFIX + "/proViSta/proViStas/" + options.page + "/" + options.pageSize + "/"+configure ;
					},
					type : "GET",
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	 data : function(d) {
	                 return d.result.success?d.data.proViStaList:null;  //响应到页面的数据
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
	proViSta.buildGrid=function(id,columns,ds){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
			filterable: {
				messages: lims.gridfilterMessage(),
				operators: {
					string: lims.gridfilterOperators(),
				    date: lims.gridfilterOperatorsDate(),
				    number: lims.gridfilterOperatorsNumber()
				}
			},
			height: 260,
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
	proViSta.initialize = function(){
		var dataSource = this.templateDS("");
		proViSta.buildGrid("msg_success",this.templatecolumns, dataSource); 		
	};

	proViSta.initialize();
});