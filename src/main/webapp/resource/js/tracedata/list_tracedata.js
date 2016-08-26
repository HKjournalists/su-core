$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var upload = fsn.upload = fsn.upload || {};
	var portal = fsn.portal = fsn.portal || {};
	var product = upload.product = upload.product || {};
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	portal.edit_id = null;

	//判断cookie不为空时设置为空
	try {
		portal.edit_id = $.cookie("user_0_edit_product").id;
		if(portal.edit_id!=null){
			$.cookie("user_0_edit_product", JSON.stringify({}), {
				path : '/'
			});
		}
	} catch (e) {
	}

	product.initialize = function(){
		upload.buildGrid("product", this.templatecolumns, this.templateDS);
		$("#product .k-grid-content tr td:first-child a").removeAttr("class");
		$("#product .k-grid-content").attr("style","height: 364px");
	};
	product.templatecolumns = [
	                           {field: "id",title: "序号",width: 25,filterable: false},
	                           {field: "productName",title: "产品名称",width: 50,filterable: false},
	                           {field: "sourceArea",title: "原材料来源区域",width: 50,filterable: false},
	                           {field: "warehouseDate",title: "入库时间",template: '#=warehouseDate=fsn.formatGridDate(warehouseDate)#',width: 40,filterable: false},
	                           {field: "productDate",title: "生产时间",template: '#=productDate=fsn.formatGridDate(productDate)#',width: 40,filterable: false},
	                           {field: "leaveDate",title: "出厂时间",template: '#=leaveDate=fsn.formatGridDate(leaveDate)#',width: 40,filterable: false },
	                           {width:60,title: fsn.l("Operation"),
	                        	   template:function(e){
	                        		   var tag="<a  onclick='return fsn.upload.product.edit("+e.id+")' class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> </span>" + fsn.l('Edit') + "</a>";
	                        		   tag+="<a id='"+e.id+"' class='k-button k-button-icontext k-grid-ViewDetail delete'><span class='k-icon k-delete'> </span>" + "删除" + "</a>";
	                        		   return tag
	                        	   }
	                           }];

	product.templateDS = new kendo.data.DataSource({
		transport: {
			read : {
				type : "GET",
				async:false,
				url : function(options){
					var configure = null;
					if(options.filter){
						configure = filter.configure(options.filter);
					}
					return portal.HTTP_PREFIX + "/traceData/getListTracedata?condition=" + configure;
				},
				dataType : "json",
				contentType : "application/json"
			},
		},
		schema: {
			data : function(returnValue) {
				return returnValue.list;  //响应到页面的数据
			},
			total : function(returnValue) {
				return returnValue.count;   //总条数
			}         
		},
		batch : true,
		page:1,
		pageSize : 10, //每页显示个数
		serverPaging : true,
		serverFiltering : true,
		serverSorting : true
	});

	// 编辑
	product.edit = function(id){
		window.location.href = fsn.getResourcePrefix()+"/views/traceData/add-tracedata.html?id="+id;
	};
	/*删除数据*/
	$(document).on("click",".delete",function(){
		var id=$(this).attr('id');
		if(confirm("确定要删除这条数据吗？")){
			$.ajax({
				url : portal.HTTP_PREFIX+ "/traceData/delete?id="+id,
				type : "DELETE",
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				success : function(rs) {
					if (rs.status) {
						fsn.initNotificationMes('删除成功',true);
						$("#product").data("kendoGrid").dataSource.read();
					} else {
						fsn.initNotificationMes('删除失败',true);
					}
				}
			});
		}
		return false;
	});
	product.initialize();
});