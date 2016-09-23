$(function(){
	var fsn = window.fsn = window.fsn || {};
	var businessSta = fsn.businessSta = fsn.businessSta || {};
	var portal = fsn.portal = fsn.portal || {}; // 
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	var PURCHASE_DS = [];
	var PURCHASE_GRID = null;
	var type = 0;
	 function initialize(){
	        buildGridWioutToolBar("businessStatistics",businessSta.columns,getStoreDS("",type),"400");//已确认批发的商品
	        PURCHASE_GRID = $("#businessStatistics").data("kendoGrid");
	  };
	
	 function getStoreDS(name,type){
		 if(name!=""){
			 name = encodeURIComponent(name);
		 }
		 PURCHASE_DS = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
						return portal.HTTP_PREFIX +"/tzAccount/zhengfuEnter/list/"+ options.page + "/" + options.pageSize +"?name="+name+"&type="+type;
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	data : function(data) {
	                return data.data;  
	            },
	            total : function(data) {
	               return  data.total;//总条数
	            }  
	        },
	        batch : true,
	        page:1,
	        pageSize : 10, //每页显示个数
	        serverPaging : true,
	        serverFiltering : false,
	        serverSorting : false
		});
		 return PURCHASE_DS;
	 }
	 
	 function buildGridWioutToolBar(id,columns,ds, height){
	        $("#" + id).kendoGrid({
	            dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
	            filterable: {
	                extra: false,
	                messages: fsn.gridfilterMessage(),
	                operators: {
	                    string: fsn.gridfilterOperators(),
	                    date: fsn.gridfilterOperatorsDate(),
	                    number: fsn.gridfilterOperatorsNumber()
	                }
	            },
	            editable: false,
	            height:height,
	            width: "100%",
	            sortable: true,
	            resizable: true,
	            selectable: false,
	            pageable: {
	                refresh: true,
	                messages: fsn.gridPageMessage()
	            },
	            columns: columns
	        });
	    };

    //查询
	    businessSta.check = function(){
    	var name = $.trim($("#businessName").val());
    	var type2 = $("#businessType").val();
    	type = type2;
    	 PURCHASE_GRID.setDataSource(getStoreDS(name,type));
    	 $("#businessName").val("");
    	 $("#businessType").val(0);
    	 PURCHASE_GRID.refresh();
    };
    
    //查看发布的报告列表
    businessSta.views = function(id){
    	window.open("admin_test-result-manage.html?id="+id);
    };
    
	//显示字段
    businessSta.columns =[	     
     {field: "businessName",title: lims.l("BusinessName"),width: 120,filterable: false},
     {field: "businessType",title: lims.l("BusinessType"),width: 50,filterable: false},
     {field: "productQuantity",title: lims.l("productQuantity"),width: 100,filterable: false},
     {field: "notPublishProQuantity",title: lims.l("NotPublishProQuantity"),width: 100,filterable: false},
     {field: "reportQuantity",title: lims.l("ReportQuantity"),width: 60,filterable: false},
     {width:80,title: lims.l("Operation"),
	      template:function(e){
			ids = e.businessId;
			var tag="<a  onclick='return fsn.businessSta.views("+e.businessId+")' class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-i-search'> </span>" + fsn.l('查看发布的报告列表') + "</a>";
				return tag;
		}
     }
     ];
    initialize();
});
