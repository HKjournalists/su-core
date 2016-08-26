$(function(){
	var lims = window.lims = window.lims || {};
	var root = window.lims.root = window.lims.root || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	lims.current_index = 0;
	
	root.initialize=function(){
		
		var urlVal = decodeURI(window.location.search);//获取url后的参数
		if(urlVal){
			var index=urlVal.indexOf("=");
			portal.urlKeyWord=urlVal.substr(index+1);
		}
		
		if(portal.urlKeyWord){
			$("#showMsg").html("包含【"+portal.urlKeyWord+"】成分的产品列表&nbsp;&nbsp;&nbsp;&nbsp;" +
					"<a href='javascript:void(0);' onclick='lims.listNutrients();'>营养列表</a>");
			root.bulidListProductGrid("product_grid",root.listProductColumn,root.getListProductDS(portal.urlKeyWord,15),"");
		}else{
			root.buildListNutrGrid("product_grid", root.listNutritionColumn,root.getListNutritionDS(),"");
		}
	};
	
	root.getListNutritionDS=function(){
		return new kendo.data.DataSource({
			transport: {
				async:false,
	            read: {
	            	url : function(options){
						return portal.HTTP_PREFIX + "/bigdata";
					},
	                dataType : "json",
	                contentType : "application/json"
	            }
	        },
	        batch : true,
	        page:1,
	        pageSize: 10,
	        schema: {
	            data : function(returnValue) {
	                return returnValue.result;
	            },
	            total : function(returnValue) {       
	                return 5;
	            }     
	        },
	        change: function(e) {},
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
	};
	
	root.getListProductDS=function(nutritionName,size){
		return new kendo.data.HierarchicalDataSource({
			transport: {
				async:false,
				read : {
					url : function(options){
							return encodeURI(portal.HTTP_PREFIX + "/bigdata/getProductByHotWord/" + options.page + "/" + options.pageSize + "/" + nutritionName);
					}
				}
			},
			batch : true,
			page: 1,
			pageSize : size||5,
			serverPaging: true,
			schema : {
				model : {
					id : "id",
					fields : {
						id : {
							editable : false,
							nullable : true
						},
						name : "name",
					}
				},
				data : function(returnVal) {
					if(returnVal.data != null){
						return returnVal.data.listOfProduct;
					}
	            },
	            total : function(returnVal) {
	            	if(returnVal.data != null){
	            		return returnVal.data.counts;
	            	}
	            }
			}
		});
	};
	
	/*展示营养列表的Column*/
	root.listNutritionColumn = [{ field: "wordName", title:"营养名称", width: 1000 },];
	
	/*展示产品列表的Column*/
	root.listProductColumn=[
	                        { field: "id", title:"Id", width: "20px" },
	                        { field: "name", title: "产品名称", width: "80px"},
	                        { field:"ingredient",title:"营养成分",width:"100px",filterable: false, sortable: false,},
	                        { field:"characteristic",title:"产品介绍",width:"100px",filterable: false, sortable: false,}
	                      ];
	
	function detailInit(e) {
		var product = e.data;
	    
	    $("<div/>").appendTo(e.detailCell).kendoGrid({
        	dataSource : root.getListProductDS(product.wordName,5),
            scrollable: false,
            sortable: true,
            pageable: {
	            refresh: true,
	            pageSizes: 10,
	            messages: lims.gridPageMessage(),
	        },
            columns:root.listProductColumn,
        });
	}
	
	root.buildListNutrGrid = function (id,columns,ds,isBack){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ? []:ds,
			filterable: {
				extra:false,
				messages: lims.gridfilterMessage(),
				operators: {
					  string: lims.gridfilterOperators()
				}
			},
			height: 500,
	        width: 1000,
	        sortable: true,
	        selectable: true,
	        resizable: true,
	        toolbar: [],
	        pageable: {
	            refresh: true,
	            pageSizes: 10,
	            messages: lims.gridPageMessage(),
	        },
	        detailInit: detailInit,
	        detailExpand:function(e){
				console.log(e.masterRow, e.detailRow);
			},
	        columns: columns,
		});
	};
		
		root.bulidListProductGrid=function (id,columns,ds,isBack){
			$("#" + id).kendoGrid({
				dataSource: ds == undefined ? []:ds,
				filterable: {
					extra:false,
					messages: lims.gridfilterMessage(),
					operators: {
						  string: lims.gridfilterOperators()
					}
				},
				height: 500,
		        width: 1000,
		        sortable: true,
		        selectable: true,
		        resizable: true,
		        toolbar: [],
		        pageable: {
		            refresh: true,
		            pageSizes: 10,
		            messages: lims.gridPageMessage(),
		        },
		        columns: columns,
			});
	};
	
	lims.listNutrients=function(){
		window.location.pathname="/fsn-core/views/portal/product_by_hotword.html";
	};
	
	root.initialize();
	
});
