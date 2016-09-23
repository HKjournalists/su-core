$(function(){
	var fsn = window.fsn = window.fsn || {};
	var purchase = fsn.purchase = fsn.purchase || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
	var PURCHASE_DS = [];
	var PURCHASE_GRID = null;
	var busId = null;
	 function initialize(){
		 	busId = window.location.href.split("id=")[1];
	        buildGridWioutToolBar("Purchase_Grid",purchase.columns,getStoreDS("",""),"400");//已确认批发的商品
	        PURCHASE_GRID = $("#Purchase_Grid").data("kendoGrid");
	  };
	
	 function getStoreDS(name,batch){
		 if(name!=""){
			 name = encodeURIComponent(name);
		 }
		 PURCHASE_DS = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
						return HTTPPREFIX +"/tzAccount/zhengfu/list/"+ options.page + "/" + options.pageSize +"/"+busId+"?name="+name+"&batch="+batch;
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
    purchase.check = function(){
    	var name = $.trim($("#number").val());
    	var batch = $.trim($("#licOrName").val());
    	 PURCHASE_GRID.setDataSource(getStoreDS(name,batch));
    	 $("#number").val("");
    	 $("#licOrName").val("");
    	 PURCHASE_GRID.refresh();
    };
    
    /* 查看 */
    purchase.views = function(id){
	      window.open("admin_test-result-preview1.html?id=" + id+"&busId="+busId);
    };
    
    purchase.goBack = function(){
    	window.location.href = "admin_businessStatistics-list.html";
    };
	 
	//显示字段
	purchase.columns = [
	                   {field: "id",title: fsn.l("Id"),width: "50px",filterable: false},
	                   {field: "serviceOrder",title: fsn.l("ReportNO"),width: "100px",filterable: false},
	                   {field: "name",title: fsn.l("Product"),width: "100px",filterable: false},
	                   {field: "batchSerialNo",title: fsn.l("样品批次"),width: "70px",filterable: false},
	                   {field: "testType",title: fsn.l("TestType"),width: "80px",filterable: false},
	                   {field: "publishDate",title: fsn.l("检验日期"),width: "120px",filterable: false},
					   {width:80,title: fsn.l("Operation"),
						      template:function(e){
								ids = e.id;
								var tag="<a  onclick='return fsn.purchase.views("+e.id+")' class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-i-search'> </span>" + fsn.l('View Detail') + "</a>";
									return tag;
							}
					    }
	       
	   ];	
    initialize();
});
