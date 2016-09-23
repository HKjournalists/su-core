var fsn = window.fsn = window.fsn || {}; // 全局命名空间
var portal = fsn.portal = fsn.portal || {}; // portal命名空间
portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀

/**
 * 引进产品
 * @author HuangYog 2015/4/14
 */
portal.saveSelectCustomer = function(productVO){
	var success = true;
	$.ajax({
		url : portal.HTTP_PREFIX + "/erp/fromToBus",
		type : "PUT",
		datatype : "json",
		async: false,
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify(productVO),
		success : function(returnValue) {
			if(returnValue.result.status == "false"){
				success = false;
			}
		}
	});
	return success;
};

/**
 * 经销商产品管理界面和产品新增界面，初始化[销往企业]控件
 * @author Zhanghui 2015/4/3
 */
portal.initCustomerMultiSelect = function() {
	$("#customerSelectInfo").kendoMultiSelect({
		placeholder : "请选择...",
		autoBind : true,
		select: portal.setCustomerWidth,
		dataTextField : "name",
		dataValueField : "id"
	});
	/* 主动设置长度，否则会影响页面展示 */
	portal.setCustomerWidth();
};

/**
 * 产品新增界面，初始化引进产品[销往企业]控件
 * @author HuangYog 2015/4/3
 */
portal.initCustomerMultiSelect_lead = function() {
	$("#customerSelect_lead").kendoMultiSelect({
		dataSource:{
			transport : {
				read : function(options) {
					$.ajax({
						url : portal.HTTP_PREFIX + "/erp/customer/2/lists",
						type : "GET",
						dataType : "json",
						success : function(data) {
							options.success(data.result);
						}
					});
				}
			}
		},
		placeholder : "请选择...",
		autoBind : true,
		select: portal.setCustomerWidth,
		dataTextField : "name",
		dataValueField : "id"
	});
	/* 主动设置长度，否则会影响页面展示 */
	$("#customerSelect_taglist").attr("style","width:348px;");
};

/**
 * 经销商产品管理界面，[销往企业]数据初始化
 * @author Zhanghui 2015/4/3
 */
portal.fillCustomerSelect = function() {
	var dataSource = new kendo.data.DataSource({
		transport : {
			read : function(options) {
				$.ajax({
					url : portal.HTTP_PREFIX + "/erp/customer/2/lists",
					type : "GET",
					dataType : "json",
					success : function(data) {
						options.success(data.result);
					}
				});
			}
		}
	});

	/**
	 * 产品新增/编辑页面，销往客户控件数据初始化
	 */
	var cts = $("#customerSelectInfo").data("kendoMultiSelect");
	if(cts){
		$("#customerSelectInfo").data("kendoMultiSelect").setDataSource(dataSource);
		$("#customerSelectInfo").data("kendoMultiSelect").refresh();
	}
	
	/**
	 * 产品引进时，销往客户控件数据初始化
	 * @author HuangYong 2015/4/14
	 */
	var cts_ = $("#customerSelect_lead").data("kendoMultiSelect");
	if(cts_){
		$("#customerSelect_lead").data("kendoMultiSelect").setDataSource(dataSource);
		$("#customerSelect_lead").data("kendoMultiSelect").refresh();
	}
};

/**
 * 查找产品的销往企业
 * @author ZhangHui 2015/4/14
 */
portal.getCustomerNamesOfProduct = function(barcode) {
	var customerNames = "";
	$.ajax({
        url: portal.HTTP_PREFIX + "/erp/fromToBus/getCustomerNames/" + barcode,
        type: "GET",
        dataType: "json",
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function(returnValue){
            if (returnValue.result.status == "true") {
            	customerNames = returnValue.data;
            }
        }
    });
	return customerNames;
};