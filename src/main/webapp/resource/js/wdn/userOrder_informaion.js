$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var owdn=window.excel=window.wdn||{};
	var wdnServiceBaseUrl = fsn.getHttpPrefix()+"/sampling/import/";

	$("#grid").kendoGrid({
		columns: [
			{ field: "orderId", title: "订单号", width: "auto"},
			{ field: "title", title: "文献标题", width: "auto"},
			{ field: "authors", title: "文献作者", width: "auto"},
			{ field: "dataSource", title: "文献来源", width: "auto"},
			{ field: "status", title: "订单状态", width: "auto"},
			{ field: "applyDate", title: "请求时间",
				template:'#:new Date(applyDate).format("YYYY-MM-dd")#', width: "auto"},
			{ command: [{name:"review",
				text:"查看",
				click:function(e){
					e.preventDefault();
					var	dataItem = this.dataItem($(e.currentTarget).closest("tr"));
					window.sessionStorage.setItem("currentOrder", JSON.stringify(dataItem));
					window.open( fsn.getContextPath() + "/views/wdn/viewOrderInfo.html", "_self");
				}}], title: "操作", width: "auto" }
		],
		dataSource: {
		    type : "JSON",
            		transport : {
            			read : {
            				url : fsn.HTTP_PREFIX + "/wdn/getOrdersInfo"
            			}
            		},
            		schema : {
            			data : function(response) {
            				if (response.results && response.results.length>0) {
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
            		pageSize : 10,
                    serverPaging: true
		},
		pageSize : 10,
		pageable: true,
	});

});
