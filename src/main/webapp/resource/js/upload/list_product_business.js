$(function() {
	var upload = window.lims.upload = window.lims.upload || {};
	var product = upload.product = upload.product || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	var deleteItem;
	product.templatecolumns = [ {field: "id",title: lims.l("Id"),width: 40},
	                            {field: "name",title: lims.l("Name"),width: 60},
	                            {field: "businessBrand.name",title: lims.l("Owned Business Brand"),width: 60},
	                            {field: "format",title: lims.l("Format"),width: 50},
	                            {field: "barcode",title: lims.l("Barcode"),width: 40},
	                            {field: "cstm",title: lims.l("Appropriate Crowd"),width: 40},
	                            {field: "ingredient",title: lims.l("Ingredient"),width: 100},
	                            { command: [{
	                                       	  name:"view",
	           			                      text: lims.localized("View"),
	       									  click: function(e){
	       										  var editRow = $(e.target).closest("tr");
	       										  var temp = this.dataItem(editRow);
	       										product.getProductById(temp.id);
	       									  }
	       								  },], title:lims.l("Operation"), width: 40 }
	                            ];
	
	       	product.templateDS = function(keyword){
	       		return new kendo.data.DataSource({
	       			transport: {
	       	            read : {
	       	                type : "GET",
	       	                async:false,
	       	                url : function(options){
	       	                	var configure = null;
	       	                	if(options.filter){
	       	            			configure = filter.configure(options.filter);
	       	            		}
	       	                	return portal.HTTP_PREFIX + "/product/getProductByParentOrganization/" + configure + "/" + options.page + "/" + options.pageSize;
	       	                },
	       	                dataType : "json",
	       	                contentType : "application/json"
	       	            },
	       	        },
	       	        schema: {
	       	        	 data : function(returnValue) {
	       	        		 return returnValue.listOfProduct==null?[]:returnValue.listOfProduct;  //响应到页面的数据
	       	             },
	       	             total : function(returnValue) {
	       	            	 return returnValue.counts;   //总条数
	       	             }         
	       	        },
	       	        batch : true,
	       	        page:1,
	       	        pageSize : 10, //每页显示个数
	       	        serverPaging : true,
	       	        serverFiltering : true,
	       	        serverSorting : true
	       		});
	};
	
	$("#viewProductWindow").kendoWindow({
		title:"产品信息预览",
		modal:true,
		visible:false,
		actions:["Close"],
		width:"800px",
	});
	
	product.clearProductVal=function(){
		$("#name").val("");
		$("#status").val("");
		$("#format").val("");
		$("#category").val("");
		$("#barcode").val("");
		$("#brand").val("");
		$("#standard").val("");
		$("#expiration").val("");
		$("#characteristic").val("");
		$("#note").val("");
		$("#cstm").val("");
		$("#desc").val("");
		$("#ingredient").val("");
	};
	
	product.writeProductInfo=function(productVal){
		product.clearProductVal();
		if (productVal!=null) {
			$.each(productVal, function(k,v){
				if (!fsn.isBlank(v)) {
					switch (k) {
					case "name":
						$("#name").val(v);
						break;
					case "status":
						$("#status").val(v);
						break;
					case "format":
						$("#format").val(v);
						break;
					case "category":
						$("#category").val(v.code);
						break;	
					case "barcode":
						$("#barcode").val(v);
						break;
					case "businessBrand":
						$("#brand").val(v.id);
						break;
					case "regularity":
						$("#standard").val(v);
						break;
					case "expiration":
						$("#expiration").val(v);
						break;
					case "characteristic":
						$("#characteristic").val(v);
						break;
					case "note":
						$("#note").val(v);
						break;
					case "cstm":
						$("#cstm").val(v);
						break;
					case "des":
						$("#desc").val(v);
						break;
					case "ingredient":
						$("#ingredient").val(v);
						break;
					}
				}
			});
		}
	};
	
	product.initProductNutri=function(dataSource){
		$("#product-nutri-grid").kendoGrid({
			dataSource:dataSource==null?[]:dataSource,
    		navigatable: true,
    		editable: false,
            columns: [
                {field: "name", title: "项目",width:70},
                {field: "value", title: "值",width:50},
                {field: "unit", title:"单位",width:50},
                {field: "per", title: "条件[ 如：每100(mL) ]", width: 100},
                {field: "nrv", title: "NRV(%)",width:50}],
        });
	};
	
	product.initialCertification=function(dataSource){
		var conDiv=$("#certification");
		conDiv.html("");
		for(var i=0;i<dataSource.length;i++){
			var div=$("<div style='float:left;margin:10px 10px;'></div>");
			var img=$("<img width='50px' src='"+dataSource[i].documentUrl+"'></img>");
			var a=$("<a href='"+dataSource[i].documentUrl+"' target='_black'></a>");
			var divRzlb=$("<div><span style='font-size:12px;'>认证类别:"+dataSource[i].cert.name+"</span></div>");
			var divDLine=$("<div><span style='font-size:12px;'>截止日期:"+dataSource[i].endDate+"</span></div>");
			a.append(img);
			div.append(a);
			div.append(divRzlb,divDLine);
			conDiv.append(div);
		}
	};
	
	product.setProAttachments=function(dataSource){
		var mainDiv=$("#product-res-listView");
		mainDiv.html("");
		for(var i=0;i<dataSource.length;i++){
			var div=$("<div style='float:left;margin:10px 10px;'></div>");
			var img=$("<img width='50px' src='"+dataSource[i].url+"'></img>");
			var a=$("<a href='"+dataSource[i].url+"' target='_black'></a>");
			a.append(img);
			div.append(a);
			mainDiv.append(div);
		}
	};
	
	product.getProductById=function(proId){
		$.ajax({
			url : portal.HTTP_PREFIX + "/product/" + proId,
			type : 'GET',
			datatype : 'json',
			async:false,
			success : function(returnValue) {
				if(returnValue.data != null){
					product.writeProductInfo(returnValue.data); // product信息show
					product.initProductNutri(returnValue.data.listOfNutrition); // 营养报告列表show
					product.initialCertification(returnValue.data.listOfCertification);
					product.setProAttachments(returnValue.data.proAttachments);
					$("#viewProductWindow").data("kendoWindow").open().center();
				}
			}
		});
	};
	
	product.initialize = function(){
		var dataSource = this.templateDS("");
		upload.buildGridWioutToolBar("product",this.templatecolumns, dataSource);
	};

	product.initialize();
});