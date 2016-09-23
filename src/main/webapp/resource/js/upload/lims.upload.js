$(function(){
	var fsn = window.fsn = window.fsn || {};
	var upload = fsn.upload = fsn.upload || {};
	
	upload.HTTP_PREFIX = fsn.getHttpPrefix();
	
	upload.buildGrid = function (id,columns,ds){
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
			width: "100%",
			height:470,
	        sortable: true,
	        resizable: true,
	        toolbar: [
	        		  {template: kendo.template($("#toolbar_template").html())}
	                  ],
	        selectable: true,
	        pageable: {
	            refresh: true,
	            messages: fsn.gridPageMessage(),
	        },
	        columns: columns
		});
		//$(".k-toolbar").after("<div class=''><input type='checkbox' />全选<a>批量删除</a></div>");
	};
	upload.buildUrlGrid = function (id,columns,ds){
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
					width: "100%",
					height:470,
					sortable: true,
					resizable: true,
					          selectable: true,
					          pageable: {
					        	  refresh: true,
					        	  messages: fsn.gridPageMessage(),
					          },
					          columns: columns
		});
		//$(".k-toolbar").after("<div class=''><input type='checkbox' />全选<a>批量删除</a></div>");
	};
	
	upload.buildGridWioutToolBar = function (id,columns,ds, height){
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
			height:height,
	        width: "100%",
	        sortable: true,
	        resizable: true,
	        selectable: true,
	        pageable: {
	            refresh: true,
	            messages: fsn.gridPageMessage(),
	        },
	        columns: columns
		});
	};
	
	/**
	 * 初始化grid
	 * @author ZhangHui 2015/4/10
	 */
	upload.buildGridByBoolbar = function (id, columns, ds, height, toolbar){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
			filterable: {
				extra: false,
				messages: fsn.gridfilterMessage(),
				operators: {
					 string: {
						 contains: lims.l("Contains"),
	                    	eq: lims.l("Is equal to"),
	                   	    neq: lims.l("Is not equal to")
	                    }
				}
			},
			width: "100%",
			height:height,
	        sortable: true,
	        resizable: true,
	        toolbar: [
	                  {template: toolbar==""?kendo.template(""):kendo.template($("#"+toolbar).html())}
	                  ],
	        selectable: true,
	        pageable: {
	            refresh: true,
	            messages: fsn.gridPageMessage(),
	        },
	        columns: columns
		});
	};
	
	upload.searchDataSource = function(name){
		return  new kendo.data.DataSource({
			transport: {
                read: {
                	url:encodeURI(encodeURI(fsn.steps.HTTP_PREFIX + "/step/search?name=" + name)),
                	type:"GET"
                }
            },
            schema: {
                data: "steps",
                total: function(response) {
                    return response.steps.length;
                },
				pageSizes: 10
            },
			page:1
		});
	}
	
	upload.searchStepByKeywords = function(id, keywords){
		var ds = upload.searchDataSource(keywords);
		$("#" + id).data("kendoGrid").setDataSource(ds);
	};
	upload.addCheckBox =function (){
		$("tbody tr").each(function(){
			//alert($(this).children("td").first().text());
			$(this).children("td").first().prepend("<input type='checkbox' />");
		});
	};
	$("#main").on("click","#checkAll",function(){
		var _b = this.checked;
		$("input.checkboxes").each(function(){
			this.checked = _b;
		});
	});
	$("#main").on("click",".checkboxes",function(){
		$("#checkAll").attr("checked",$("input.checkboxes").length == $("input.checkboxes:checked").length ? true : false);
	});
});