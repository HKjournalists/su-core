$(function(){
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var filter = window.filter = window.filter || {};
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	var upload = fsn.upload = fsn.upload || {}; // portal命名空间
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	var viewReportID = 0;
	portal.url_edit = '/fsn-core/views/market/manager_addReport.html';
	
	fsn.initialize=function(){
		fsn.initComponentCommon();
		upload.buildGridByBoolbar("report_no_publish_grid", fsn.publishColumn, fsn.reportDS, 350, "toolbar_template");
		initItemGrid("testItem_grid");
	};
	
	fsn.reportDS = new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){
						return portal.HTTP_PREFIX + "/test/test-resultsByThird";
					},
	                dataType : "json",
	                type: "POST",
	            },
	            parameterMap : function(options, operation) {
                    if (operation == "read") {     
                    	var data={};
                    	data.page= options.page;    //当前页
                    	data.pageSize = options.pageSize; //每页显示个数
                    	if(options.filter){
                    		data.configure=filter.configure(options.filter);
                    	}
                        return {criteria : kendo.stringify(data)};
                    }
                }
	        },
	        batch : true,
	        page:1,
	        pageSize: 5,
	        schema: {
	             data : function(response) {
                      return response.testResultList; //响应到页面的数据
                  },
                  total : function(response) {
	                    return response.totalCount;
                  }
	        },
	        dir:"asc",
            pageSize: 5,
			batch: true,
			serverPaging : true,
		    serverFiltering : true,
		    serverSorting : true
		});
		
	fsn.publishColumn = [{field: "id",title:"编号",width: "30px"},
	                  {field: "serviceOrder",title: "报告编号",width: "55px"},
	                  {field: "sample.product.name",title: "产品名称",width: "50px"},
	                  {field: "sample.batchSerialNo",title: "批次/日期",width: "50px"},
	                  {field: "sample.producer.name",title:"生产企业名称",width: "55px"},	                   
	                  {field: "testType",title: "报告类型",width: "45px"},
	                  {field: "pubUserName",title: "发布者",width: "40px"},
	                  {field: "receiveDate",title: "接受时间",width: "50px",filterable: false},
	                  {field: "organizationName",title: "组织机构名称",width: "60px"},
	                  { command: [
		                   {
							  name:"编辑",
							  text:"<span class='k-icon k-i-search'></span>编辑",
							  click:function(e){
								 fsn.edit(this.dataItem($(e.currentTarget).closest("tr")), portal.url_edit, "subBusiness_viewReport");
							  }
		                   }], 
		                  title:fsn.l("Operation"), 
		                  width: "40px" 
		               }];
	
	fsn.initialize();
});
