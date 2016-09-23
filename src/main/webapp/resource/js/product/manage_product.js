var fsn = window.fsn = window.fsn || {}; // 全局命名空间
var portal = fsn.portal = fsn.portal || {}; // portal命名空间
portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀

/**
 * 重新计算营养成分指数 
 * @author tangxin
 */
portal.recalculatedNutri = function (productID){
	if(productID == null){
		return;
	} 
	$.ajax({
		url:portal.HTTP_PREFIX + "/product/recalculatedNutri/" + productID,
	type:"PUT",
	dataType:"json",
	contentType: "application/json; charset=utf-8",
	success:function(data){
		$("#product").data("kendoGrid").dataSource.read();
		if(data.result.status == "true"){
			fsn.initNotificationMes("计算成功！",true);
		}else{
			message = data.result.message;
			fsn.initNotificationMes( (message!= null && message !="") ? message : "计算失败，后台出现异常！",false);
			}
		}
	});
};

/**
 * 编辑
 * @author tangxin
 */
portal.edit = function(id,packageFlag){
	var product = {
		id : id
	};
	$.cookie("user_0_edit_product", JSON.stringify(product), {
		path : '/'
	});
	/**
	 * 产品的包装标志：0：预包装、1：散装、2：无条码
	 * 根据产品的不同跳转到不同的编辑界面
	 */
	if(packageFlag=='0'){
		window.open("/fsn-core/views/portal/product.html");
	}else{
		window.open("/fsn-core/views/portal/add-qrcode-product.html");
	}
};

/**
 * 重新计算营养指数的click事件 
 * @author tangxin
 */
portal.recalculatedOnClick = function(productId,nutriStatus){
	if(productId == null){ return; }
    if(nutriStatus == '1') {
	    fsn.initConfirmWindow(function() {
		portal.recalculatedNutri(product.id);
		fsn.closeConfirmWin();
	}, undefined,fsn.l("WIN_RECALCULATEDCONFIRM_MSG"),true);
	    $("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
    }else{
	    /* 重新计算营养成分指数 */
    	portal.recalculatedNutri(productId);
    }
};

/**
 * 产品预览
 * @author tangxin
 */
portal.viewProduct = function(productId){
	if (!productId) {return;}
	$("#viewWindow").data("kendoWindow").open().center();
	$.ajax({
		url: fsn.getHttpPrefix() + "/product/" + productId,
		type: "GET",
		success: function(returnValue){
			if (returnValue.result.status == "true") {
				portal.setProductValue(returnValue.data);
				portal.setProAttachments(returnValue.data.proAttachments); //show 产品图片
				var listOfCertification = returnValue.data.listOfCertification;
				if (listOfCertification.length > 0) {
					for (var i = 0; i < listOfCertification.length; i++) {
						if (listOfCertification[i].certResource == null) {
							listOfCertification[i].certResource = "";
						}
					}
				}
				portal.initialCertification(listOfCertification); //show 产品认证信息
			}
		},
	});
	// 加载营养报告grid(分页)
   	portal.initNutriDS(productId);
   	$("#product-nutri-grid").data("kendoGrid").setDataSource(portal.nutriDS);
};

/**
 * 修改引进产品的销往企业
 * @author ZhangHui 2015/4/8
 */
portal.editCustomers = function(proId){
	 $("#editCustomerWindow").data("kendoWindow").open().center();
	 $.ajax({
		   url: portal.HTTP_PREFIX + "/erp/fromToBus/" + proId,
		   type: "GET",
		   dataType: "json",
		   async:false,
		   contentType: "application/json; charset=utf-8",
		   success: function(returnValue) {
			  	 if(returnValue.result.status == "true"){
			  		$("#customerSelect").data("kendoMultiSelect").value(returnValue.data.split(","));
			  	 }
		   }
	 });
};

/**
 * Show 产品图片信息
 * @author tangxin
 */
portal.setProAttachments = function(dataSource){
	var mainDiv = $("#proAttachments");
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

portal.setProductValue = function(product){
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
 * 初始化 认证信息
 * @param {Object} dataSource
 */
portal.initialCertification=function(dataSource){
	
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
portal.initNutriDS = function(productId){
	if(!productId){
		productId = 0;
	}
	var FirstPageFlag = 1;
	portal.nutriDS = new kendo.data.DataSource({
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

