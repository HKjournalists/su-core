$(document).ready(function() {
var fsn = window.fsn = window.fsn || {}; // 全局命名空间
var productQRcode = fsn.productQRcode = fsn.productQRcode || {};
var upload = window.lims.upload = window.lims.upload || {};

var portal = fsn.portal = fsn.portal || {}; // portal命名空间
portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀


/**
 * 初始化页面
 */
		loadData();
		
         /**
          * 页面载入数据
          */    
		 function loadData() {
				$.ajax({
					async : false,
					url : portal.HTTP_PREFIX
							+ "/product/getProductToQRcodeSource",
					type : "GET",
					success : function(returnVal) {
						if (returnVal.result.success) {
							QRlist = returnVal.data.barcodeToQRcodeVOList;
							// 页面赋值操作
							writeData(QRlist);
						}
					},
					error : function() {
						alert(234);
					}
				});

			};
		
			/**
			 * 删除操作
			 */
			function remove(id){
				
				$.ajax({
					url : portal.HTTP_PREFIX+ "/product/productToQRcode/delete/" + id,
					type : "POST",
					dataType : "json",
					contentType : "application/json; charset=utf-8",
					success : function(returnValue) {
					console.log(returnValue.success);
					if (returnValue.success) {
						alert("删除成功");
						loadData();
						
					} else {
						alert("删除失败，请重新操作");
						loadData();
								}
							}
						});
				
			}
		 	/**
		 	 * 页面表格赋值
		 	 */
			function writeData(data) {
				$("#barcode_to_qrcode_list").kendoGrid({
					dataSource : data,
					columns : [ {field : "product_id",title : "产品id",filterable : false}, 
		                         {field : "barcode",title : "产品条形码",filterable : false}, 
		                         {field : "qrstart_num",title : "二维码起始位置",filterable : false}, 
		                         {field : "qrend_num",title : "二维码终止位置",filterable : false},
		                         { command: [{
                               	  name:"Edit",
   			                      text: lims.localized("删除"),
									  click: function(e){
										  var editRow = $(e.target).closest("tr");
										  var temp = this.dataItem(editRow);
										  remove(temp.id);
									  }
								  } 
					            ], title:"操作" }
		                         ],
					page:1,
			        pageSize : 10,
			        height:500
				})

			}

			/**
			 * 验证改条形码是否存在 
			 * @author liuyuanjing 2015-12-29
			 */
			function check(barcode,QRStart,QREnd) {
				var barcode = $("#barcode").val().trim();
				var QRStart = $("#QRStart").val().trim();
				var QREnd = $("#QREnd").val().trim().trim();
				var QRNum_RegExp = /^[1-9]\d{0,5}$/;
				if (barcode === "") {
					alert("很抱歉，请输入条形码！");
					return false;
				}
				// 产品条码长度验证：长度可为8、12、13、14位
				if ($.inArray(barcode.length, [ 8, 12, 13, 14 ]) == -1) {
					alert("产品条码长度应为8、12、13、14位！");
					return false;
				}
				if (parseInt(QRStart) > parseInt(QREnd)) {
					alert("输入范围的起始点不能大于终止点");
					return false;
				}
				$.ajax({
					url : portal.HTTP_PREFIX+ "/product/validateBarcodeUnique/" + barcode,
					type : "GET",
					dataType : "json",
					contentType : "application/json; charset=utf-8",
					success : function(returnValue) {
						if (!returnValue.isExist) {
							alert("当前产品不存在，需新增产品后方能为其生成二维码");
							return false;
						}
						else{
							return true;
						}
					}
				});
					
               return true;
					
			}

		/**
		 * 保存方法
		 */
		$("#save").click(function() {

			var barcode = $("#barcode").val().trim();
			var QRStart = $("#QRStart").val();
			var QREnd = $("#QREnd").val();
			var success=check(barcode,QRStart,QREnd);
			if(success==true){
				$.ajax({
					url : portal.HTTP_PREFIX+ "/product/productToQRcode/" + barcode+ "/" + QRStart + "/" + QREnd,
					type : "POST",
					dataType : "json",
					contentType : "application/json; charset=utf-8",
					success : function(returnValue) {
					console.log(returnValue.success);
					if (returnValue.success) {
						alert("保存成功");
						loadData();
						
					} else {
						alert("输入范围已存在，请重新输入");
					    $("#barcode").val("");
						$("#QRStart").val("");
						$("#QREnd").val("");
								}
							}
						});
			}
			});

			/**
			 * 清空输入框
			 */
			$("#cancel").click(function() {
				$("#barcode").val("");
				$("#QRStart").val("");
				$("#QREnd").val("");

			});
	
	
	
	
});

