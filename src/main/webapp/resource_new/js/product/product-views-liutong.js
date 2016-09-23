$(function(){
	var fsn = window.fsn = window.fsn || {};
	var views = window.views || {};
	//控件初始化
	views.initialize = function(){
		views.buildGrid("product_grid", views.productColumns, views.ownProductDS, 300);
		views.buildGrid("introduce_product_grid", views.productColumns, views.otherProductDS, 300);
	};
	
	views.buildGrid = function (id,columns,ds, height){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ? []:ds,
			filterable: {
				extra:false,
				messages: fsn.gridfilterMessage(),
				operators: {
					  string: fsn.gridfilterOperators()
				}
			},
			height: height,
	        width: 1000,
	        sortable: true,
	        selectable: true,
	        resizable: true,
	        toolbar: [],
	        pageable: {
	            refresh: true,
	            pageSizes: 5,
	            messages: fsn.gridPageMessage(),
	        },
	        columns: columns,
		});
	};
	
	views.productColumns = [
       						{field: "product.id",title: "序号",width: 40},
                            {field: "product.name",title: "名称",width: 60},
                            {field: "product.businessBrand.name",title: "所属品牌",width: 50},
                            {field: "product.format",title:"规格",width: 50},
                            {field: "product.barcode",title: "条形码",width: 60},
                            {field: "product.cstm",title:"适宜人群",width: 40},
                            {field: "product.ingredient",title: "配料",width: 100},
                            { command: [
                                        {
                                       	  name:"review",
                            			  text:"<span class='k-icon k-cancel' ></span>" + fsn.l("Preview"), 
       								  	  click: function(e){
											e.preventDefault();
											var editRow = $(e.target).closest("tr");
											var temp = this.dataItem(editRow);
											views.viewProduct(temp.product.id);
											}
								}], title:lims.l("Operation"), width: 40 }
							];
								
	views.ownProductDS = new kendo.data.DataSource({
			transport: {
				read: {
					type: "GET",
					async: false,
					url: function(options){
						var configure = null;
						if (options.filter) {
							configure = filter.configure(options.filter);
						}
						return fsn.HTTP_PREFIX + "/erp/initializeProduct/getInitProductByOrgAndLocal/true/" + configure + "/" + options.page + "/" + options.pageSize;
					},
					dataType: "json",
					contentType: "application/json"
				},
			},
			schema: {
				data: function(returnValue){
					return returnValue.data!=null?returnValue.data:[]; //响应到页面的数据
				},
				total: function(returnValue){
					return returnValue.counts; //总条数
				}
			},
			batch: true,
			page: 1,
			pageSize: 10, //每页显示个数
			serverPaging: true,
			serverFiltering: true,
			serverSorting: true
		});
		
	views.otherProductDS = new kendo.data.DataSource({
			transport: {
				read: {
					type: "GET",
					async: false,
					url: function(options){
						var configure = null;
						if (options.filter) {
							configure = filter.configure(options.filter);
						}
						return fsn.HTTP_PREFIX + "/erp/initializeProduct/getInitProductByOrgAndLocal/false/" + configure + "/" + options.page + "/" + options.pageSize;
					},
					dataType: "json",
					contentType: "application/json"
				},
			},
			schema: {
				data: function(returnValue){
					return returnValue.data!=null?returnValue.data:[]; //响应到页面的数据
				},
				total: function(returnValue){
					return returnValue.counts; //总条数
				}
			},
			batch: true,
			page: 1,
			pageSize: 10, //每页显示个数
			serverPaging: true,
			serverFiltering: true,
			serverSorting: true
		});
		
		
		views.viewProduct = function(productId){
		if (!productId) {
			return;
		}
		$.ajax({
			url: fsn.getHttpPrefix() + "/product/" + productId,
			type: "GET",
			success: function(returnValue){
				if (returnValue.result.status == "true") {
					views.setProductValue(returnValue.data);
					views.setProAttachments(returnValue.data.proAttachments); //show 产品图片
					var listOfCertification = returnValue.data.listOfCertification;
					if (listOfCertification.length > 0) {
						for (var i = 0; i < listOfCertification.length; i++) {
							if (listOfCertification[i].certResource == null) {
								listOfCertification[i].certResource = "";
							}
						}
					}
					views.initialCertification(listOfCertification); //show 产品认证信息
					$("#viewWindow").data("kendoWindow").open().center();
				}
			},
		});
		// 加载营养报告grid(分页)
       	 views.initNutriDS(productId);
       	 $("#product-nutri-grid").data("kendoGrid").setDataSource(views.nutriDS);
	}
	
	views.setProductValue = function(product){
    	if(product==null){return;}
		$("#id").val(product.id);
		$("#name").val(product.name);
		$("#status").val(product.status);
		$("#format").val(product.format);
		$("#category").val(product.category.name);
		$("#barcode").val(product.barcode);
		$("#expiration").val(product.expiration);
		$("#businessBrand").val(product.businessBrand.name);
		$("#regularity").val(lims.convertRegularityToString(product.regularity));
		$("#characteristic").val(product.characteristic);
		$("#note").val(product.note);
		$("#cstm").val(product.cstm);
		$("#des").val(product.des);
		$("#ingredient").val(product.ingredient);
    };
	
	/**
     * Show 产品图片信息
     * @param dataSource
     */
    views.setProAttachments=function(dataSource){
		var mainDiv=$("#proAttachments");
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
	
	/**
	 * 初始化 认证信息
	 * @param {Object} dataSource
	 */
	views.initialCertification=function(dataSource){
		
		/**
		 * 认证信息  图片触发
		 * @param container
		 * @param options
		 */
		function certResourceEditor(container, options){
			var certRes = options.model.certResource;
			$("<span class='a-span'>"+certRes.name +"</span>").appendTo(container);
			if(certRes.url){
    			window.open (certRes.url);
    			return;
    		}
    	}
		
		/**
		 * 认证信息Grid中 认证名称和截止日期的点击事件
		 */
		function certColumnEditor(container,options){
			if(options.field=="endDate"){
				var endDate = options.model.endDate;
				$("<span>"+(endDate!=null?endDate.substr(0,10):"")+"</span>").appendTo(container);
			}else{
				$("<span>"+options.model.cert.name+"</span>").appendTo(container);
			}
		}
		
		/**
		 * 认证信息Grid
		 */
		$("#certification-grid").kendoGrid({
			dataSource:dataSource==null?[]:dataSource,
    		navigatable: true,
    		editable: true,
    		selectable: "multiple cell",
    		 columns: [
    	                {field: "cert.name", title: fsn.l("Certification category"),width:70, editor:certColumnEditor},
    	                {field: "certResource.name", title: fsn.l("Related images"),
    	                		editor: certResourceEditor, width:100,
    	                		template: function(dataItem) {
    	            	 return "<span class='a-span'>" + (dataItem.certResource.name) + "</span>";         		 
    	                 }},
    	                {field: "endDate", title: fsn.l("Is valid as of the time"),width: 100, editor:certColumnEditor, template: '#= endDate.substring(0,10)#'}],
        });
		
	};
	
	/**
     * 营养信息Grid 分页显示
     * @param {Object} productId
     */
    views.initNutriDS = function(productId){
    	if(!productId){
    		productId = 0;
    	}
    	var FirstPageFlag = 1;
		views.nutriDS = new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){
	            		    if(productId == 0){
	            			return;
	            		    }
    	            		if(FirstPageFlag == 1){  //每次预览新的报告的时候，都从第一页开始显示
    	            			options.page=1;
    	            			options.pageSize=5;
    	            			FirstPageFlag=0;
    	            		}
	            		return fsn.getHttpPrefix() + "/product/getStandNutris/" + options.page + "/" + options.pageSize + "/" + productId;
	            	},
	            	type:"GET",
	                dataType : "json",
	                contentType : "application/json"
	            }
	        },
	        batch : true,
	        page:1,
	        pageSize: 5,
	        schema: {
	            data : function(returnValue) {
	                return returnValue.data.listOfProductNutrition;
	            },
	            total : function(returnValue) {       
	                return returnValue.data.counts;
	            }     
	        },
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
	};
	
	$("#viewWindow").kendoWindow({
		width:1000,
		visible: false,
		title:fsn.l("Product preview"),
	});
	
	$("#product-nutri-grid").kendoGrid({
				dataSource:[],
		        editable: false,
		        pageSize : 10,
		        pageable: {
		            refresh: true,
		            pageSizes: 5,
		            messages: fsn.gridPageMessage(),
		        },
		        columns: [
                        {field: "name", title: "项目",width:70},
                        {field: "value", title: "值",width:50},
                        {field: "unit", title:"单位",width:50},
                        {field: "per", title: "条件[如：每100(mL)]", width: 100},
                        {field: "nrv", title: "NRV(%)",width:50}]
			});


views.initialize();
});