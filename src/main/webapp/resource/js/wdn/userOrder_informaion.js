$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var owdn=window.excel=window.wdn||{};
	var wdnServiceBaseUrl = fsn.getHttpPrefix()+"/sampling/import/";

	var orderInfo = new kendo.data.DataSource({
		type : "JSON",
		transport : {
			read : {
				url : fsn.HTTP_PREFIX + "/wdn/getOrdersInfo"
			}
		},
		schema : {
			data : function(response) {
				if (response.results&&response.results.length>0) {
					return response.results;
				}
				return [];
			},
			total : function(response) {
				if (response.result) {
					return response.result.length;
				}
				return 0;
			}
		},
		serverPaging : true,
		pageSize : 10
	});

	$("#grid").kendoGrid({
		columns: [
			{ field: "orderId", title: "订单号", width: 60},
			{ field: "title", title: "文献标题", width: 100},
			{ field: "authors", title: "文献作者", width: 100},
			{ field: "dataSource", title: "文献来源", width: 100},
			{ field: "status", title: "订单状态", width: 60},
			{ field: "applyDate", title: "请求时间",
				template:'#:new Date(applyDate).format("YYYY-MM-dd")#', width: 80},
			{ command: [{name:"review",
				text:"查看",
				click:function(e){
					e.preventDefault();
					var	dataItem = this.dataItem($(e.currentTarget).closest("tr"));
					window.sessionStorage.setItem("currentOrder", JSON.stringify(dataItem));
					window.open( fsn.getContextPath() + "/views/wdn/viewOrderInfo.html", "_self");
				}}], title: "操作", width: 30 }
		],
		dataSource: orderInfo,
		pageable: {
			refresh: true,
			messages: fsn.gridPageMessage(),
		},
	});

});
