$(function() {
	// 认证信息下拉框
	var portal = fsn.portal = fsn.portal || {};
	var	upload = portal.upload = portal.upload || {};
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    portal.isNew = true;
    portal.oldBarcode;
    portal.aryProAttachments = new Array();
    portal.aryCertAttachments = new Array();
    portal.edit_id = null;
    try {
		portal.edit_id = $.cookie("user_0_edit_product").id;
		$.cookie("user_0_edit_product", JSON.stringify({}), {
            path: '/'
        });
    } catch (e) {
    }
	(function() {
		var Product = (function() {
			function _fn(){}
			
			// 初始化数据
			_fn.prototype.loadDataSource = function(sid) {
				if(sid){
					var mws = this;
					portal.isNew = false;
					$.ajax({
						url : portal.HTTP_PREFIX + "/product/" + sid,
						type : 'GET',
						datatype : 'json',
						async:false,
						success : function(returnValue) {
							mws.cacheModel = returnValue.data;
							if(returnValue.data != null){
								mws.writeProductInfo(returnValue.data); // product信息show
								/* 为营养报告grid上的条件赋值 */
								initNutriPer(returnValue.data.listOfNutrition);
								mws.initProductNutri(returnValue.data.listOfNutrition); // 营养报告列表show
								fsn.initialCertGrid(returnValue.data.listOfCertification);
								mws.setProAttachments(returnValue.data.proAttachments);
								portal.oldBarcode = returnValue.data.barcode;
							}
						}
					});
				};
			};
			
			// 初始化产品图片 show
			_fn.prototype.setProAttachments=function(proAttachments){
				 var dataSource = new kendo.data.DataSource();
				 portal.aryProAttachments.length=0;
				 if(proAttachments.length>0){
					 $("#btn_clearProFiles").show();
                     $("#logListView").show();
					 for(var i=0;i<proAttachments.length;i++){
						 portal.aryProAttachments.push(proAttachments[i]);
						 dataSource.add({attachments:proAttachments[i]});
					 }
				 }
				 
				 $("#proAttachmentsListView").kendoListView({
		             dataSource: dataSource,
		             template:kendo.template($("#uploadedFilesTemplate").html()),
		         });
			 };
			
			fsn.addNutri = function () {
				$("#product-nutri-grid").data("kendoGrid").dataSource.add({ name:"", value:"", unit:"", nrv:""});
			};
			
			/*编辑商品信息时，给品牌赋值*/
			fsn.editProductSetBrand = function (brand){
				$("#businessBrand").data("kendoComboBox").value(brand.id);
				var bval = $("#businessBrand").data("kendoComboBox").value();
				/*判断当前商品关联的品牌是否属于该企业信息，相等则不属于*/
				if(bval==$("#businessBrand").val().trim()){
					$("#businessBrand").data("kendoComboBox").text(brand.name);
					bval = $("#businessBrand").data("kendoComboBox").value();
					if(bval==brand.name){
						$("#businessBrand").data("kendoComboBox").text("");
						lims.initNotificationMes("当前商品关联的品牌信息无效，请重新选择本企业下对应的品牌，如果没有对应的品牌，请先新增。", false);
					}
				}
			};
			
			// 编辑产品时，页面赋值操作
			_fn.prototype.writeProductInfo = function (data) {
				if (data!=null) {
					$.each(data, function(k,v){
						if (!portal.isBlank(v)) {
							switch (k) {
							case "id":
								$("#id").val(v);
								break;
							case "name":
								$("#name").val(v);
								break;
							case "status":
								$("#status").val(v);
								break;
							case "format":
								$("#format").val(v);
								break;
							/*case "nutriLabel":
								$("#nutriLabel").text(v);
								break;*/
							case "category":
								$("#category1").data("kendoComboBox").value(v.category.code.substring(0,2));
								$.ajax({
									url:portal.HTTP_PREFIX + "/testReport/searchCategory/2?parentCode="+v.category.code.substring(0,2),
									type:"GET",
									dataType:"json",
									async:false,
									success:function(returnValue){
										if(returnValue.result.status == "true"){
											$("#category2").data("kendoComboBox").setDataSource(returnValue.data);											
											$("#category2").data("kendoComboBox").value(v.category.id);
										}
									},	
								});
									$.ajax({
										url:portal.HTTP_PREFIX + "/testReport/searchLastCategory/"+v.category.id+"?categoryFlag="+true,
										type:"GET",
										dataType:"json",
										async:false,
										success:function(returnValue){
											if(returnValue.result.status == "true"){
												$("#category3").data("kendoComboBox").setDataSource(returnValue.data);
											}
										},	
									});	
									$("#category3").data("kendoComboBox").value(v.id);
								break;
							case "regularity":
								var text = "";
								for(var i=0;i<v.length;i++){
									text = text+v[i].name+";"
								}
								$("#regularity").val(text);
								break;
							case "barcode":
								$("#barcode").val(v);
								break;
							case "businessBrand":
								if($("#businessBrand").data("kendoComboBox")){
									/*编辑商品信息时，给品牌赋值*/
									fsn.editProductSetBrand(v);
								}else{
									$("#businessBrand").val(v.name);
									$("#businessBrand").attr("data-id-value",v.id);

								}
								break;							
							case "expirationDate":
								$("#expirationDate-num").val(v);
								break;
							case "expiration":
								$("#expiration").val(portal.setExpirDay(v));
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
								$("#des").val(v);
								break;
							case "ingredient":
								$("#ingredient").val(v);
								break;
							case "unit":
								$("#productUnit").val(v.name);
								break;
							case "otherName":
								$("#otherName").val(v);
								break;
							}
						}
					});
                    if(portal.isLiuTong) {
                        $("#businessName").val(data.producer!=null?data.producer.name:"");
                        if(portal.busType == "流通企业.供应商"){
                        	$("#customerSelect").data("kendoMultiSelect").value(data.selectedCustomerIds.split(","));
                        }
                    }
				}
			};
			
			var barcodeIsChange = true;
			/* 条形码change事件 */
			$("#barcode").bind("change",function (){
				portal.barcodeIsExists = false;
				barcodeIsChange = true;
			});
			
			//判断产品条形码是否已经存在 
			$("#barcode").bind("blur",function validateBarcodeUnique(){
				/* 当前页面的barcode */
				var curBarcode = $("#barcode").val().trim();
				if(curBarcode.length<1) return;
				/* 当前页面的barcode 等于 编辑状态下原来的barcode */
				if(curBarcode == portal.oldBarcode){
					portal.barcodeIsExists = false;
				}
				/* 如果已确定当前barcode为引进barcode */
				if(portal.barcodeIsExists) {
					portal.showLeadMsg();
					return;
				}
				if(!barcodeIsChange) return;
				barcodeIsChange = false;
				/* 取法确定当前barcode为引进barcode */
				$.ajax({
		             url: portal.HTTP_PREFIX + "/product/validateBarcodeUnique/" + curBarcode,
		             type: "GET",
		             dataType: "json",
		             async:false,
		             contentType: "application/json; charset=utf-8",
		             success: function(returnValue) {
		            	 /* 条形码时候重复，true是已存在，false不存在 */
		            	 if(returnValue.isExist){
		            		 /* 当前条形码的产品是否已经引进，true是已存引进，false未引进 */
		            		 if(returnValue.isLead){
		            			 lims.initNotificationMes("该产品已经引进，可前往产品管理界面查看详情！", true);
		            		 }else{
		            			 portal.barcodeIsExists = true;
		            			 portal.showLeadMsg();
		            		 }
		            	 }else{
		            		 portal.barcodeIsExists = false;
		            	 }
		             }
		    	 });
			});
			
			/**
			 * 当产品条形码已经存在时，弹出引进产品的提示框
			 * @author ZhangHui 2015/4/8
			 */
			portal.showLeadMsg = function(){
				if(portal.isLiuTong){
					$("#lead_window").data("kendoWindow").open().center();
					 if(portal.busType == "流通企业.供应商"){
						 var customerItems = $("#customerSelect").data("kendoMultiSelect").dataItems();
						 if(customerItems.length<1){
							 $("#leadMsg").html("该条形码已存在，可直接引用！<br><br>检测到【销往企业】为空，请选择销往企业!");
							 //$("#btn_leadOK").hide();
						 }else{
							 customerNames = "";
							 portal.customerIds = "";
							 for ( var entry in customerItems) {
								 customerNames += (customerItems[entry].name + ",");
								 portal.customerIds += (customerItems[entry].id + ",");
							 }
							 $("#leadMsg").html("该条形码已存在，可直接引用！<br><br>检测到【销往企业】为：" + customerNames);
							 $("#btn_leadOK").show();
						 }
					 }else{
						 $("#leadMsg").html("该条形码已存在，可直接引用");
					 }
				}else{
					lims.initNotificationMes("产品条形码已经存在，请重新填写！", false);
				}
			};
			
			// 提交前验证必填项
			_fn.prototype.validateProduct = function () {					
				//验证品牌
				if($("#businessBrand").data("kendoComboBox")){
					/* 生产企业 */
					var businessBrand = $("#businessBrand").data("kendoComboBox").dataSource.data();
					if(businessBrand.length==0){
						lims.initNotificationMes("品牌为空，请先到品牌管理页面添加相关品牌信息！", false);
						return false;
					}
					var brandVal = $("#businessBrand").val().trim();//如果用户选择了商品，该值为商标的id
					var text = $("#businessBrand").data("kendoComboBox").text().trim();//始终为用户输入的值
					if(brandVal.length<1 || brandVal == text){
						lims.initNotificationMes("您没有选择商品所属品牌，或者您输入的品牌不存在！", false);
						return false;
					}
				}else{
					/* 流通企业 */
					var brandVal = $("#businessBrand").val().trim();
					if(brandVal.length<1){
						lims.initNotificationMes("请填写商品的所属品牌！", false);
						return false;
					}
					var categoryName = $("#categoryGroupName").val();
		   		 	if(categoryName == ""){
		   		 		lims.initNotificationMes("请选择品牌类型！", false);
		   		 		return false;
		   		 	}
					var busName = $("#businessName").val().trim();
					if(busName.length<1){
						lims.initNotificationMes("请填写商品的生产企业！", false);
						return false;
					}
				}
				// 检测用户输入，如果不合法直接提示并中断提交操作
				var productName = $("#name").val();
				if (portal.isBlank(productName)) {
					$("#name").focus();
					lims.initNotificationMes("请填写产品名称！", false);
					return false;
				}
				
				var barcode = $("#barcode").val();
				if (portal.isBlank(barcode)) {
					$("#barcode").focus();
					lims.initNotificationMes("请填写产品的条形码！", false);
					return false;
				}else{
//					 var barcodeRel = /^[0-9]*$/;
//					 if(!barcode.match(barcodeRel)){
//						 lims.initNotificationMes(fsn.l("Product barcode only by numbers!"), false);
//			    		 return false;
//					 }
					 if(portal.barcodeIsExists!=null&&portal.barcodeIsExists){
						 lims.initNotificationMes("产品条形码已经存在，请重新填写！", false);
						 return false;
					 }
					
				}
				
				var proUnit=$("#productUnit").val().trim();
				if(portal.isBlank(proUnit)){
					$("#productUnit").focus();
					lims.initNotificationMes("请填写产品的单位！", false);
					return false;
				}
				
				var brandName = $("#businessBrand").val();
				if (portal.isBlank(brandName)) {
					lims.initNotificationMes("请填写产品所属品牌！", false);
					return null;
				}
				
				var indexCategory1 =$("#category1").data("kendoComboBox").select();
				if (indexCategory1==-1) {
					$("#category1").focus();
					lims.initNotificationMes("请选择产品所属的食品种类一级分类！", false);
					return false;
				}
				var indexCategory2 =$("#category2").data("kendoComboBox").select();
				if (indexCategory2==-1) {
					$("#category2").focus();
					lims.initNotificationMes("请选择产品所属的食品种类二级分类！", false);
					return false;
				}
				var category3 =$("#category3").data("kendoComboBox").text().trim();
				if (portal.isBlank(category3) || category3 == "请选择...") {
					$("#category3").focus();
					lims.initNotificationMes("请选择产品所属的食品种类三级分类！", false);
					return false;
				}
				var regularity= $("#regularity").val().trim();
				if (regularity=="") {
					$("#regularity").focus();
					lims.initNotificationMes("请填写执行标准！", false);
					return false;
				}
				//保质期验证
				var expirationDate = $("#expirationDate-num").val();
				var code =$("#category2").data("kendoComboBox").dataItem().code;	
				if (portal.isBlank(expirationDate)) {
					//若产品分类为酒类(酒类code以15开头)，则保质期可为空
					if (code.substr(0, 2) != "15") {
						$("#expirationDate-num").focus();
						lims.initNotificationMes("请填写产品保质期！", false);
						return false;
					}
				} else{
					//若保质期不为空
					 if (!/^\d+$/.test(expirationDate)) {
					 	$("#expirationDate-num").focus();
					 	lims.initNotificationMes("保质期必须是数字！", false);
					 	return false;
					 }else if (!portal.validateExpiration()){
                   		 return false;
               		 }
				}
				/* 
                 *验证营养报告的条件值 
                 *@author tangxin
                 */
				var nutriPer = $("#nutriPer").val().trim();
				var nutriItems =  $("#product-nutri-grid").data("kendoGrid").dataSource.data();
				var count = 0;
				if(nutriItems !=null && nutriItems.length > 0){
					for(var i=0;i<nutriItems.length;i++){
						if(nutriItems[i].name != "") count++;
					}
				}
				if(nutriPer == "" && count > 0){
					lims.initNotificationMes(fsn.l("Please enter the nutritional condition report")+"!", false);
					return false;
				}
				return true;
			};
			
			// 封装认证信息
			fsn.getCerts = function(){
			    var listProductCerts=[];
			    var certs=$("#certification-grid").data("kendoGrid").dataSource.data();
			    var j=0;
			    for(var i=0;i<certs.length;i++){
			    	if(certs[i].name==""){
			    		j++;
			    		continue;
			    	}
			    	listProductCerts[i-j]={
			    		id: certs[i].id,
			    	};
			    }
			    return listProductCerts;
			 };
			 
			// 提交时，构建产品对象
			_fn.prototype.createProduct = function(){
				var mws = this;
				var index = $("#category3").data("kendoComboBox").select();
				var category = {};
				if(index==-1){
					category = {
		    			name: $("#category3").val(),
						category:{
							id:$("#category2").data("kendoComboBox").value()
						}
		    		};
				}else{
					category = {
		    			id: $("#category3").data("kendoComboBox").value().trim(),
		    		};
				}
				var businessBrand = {
		    			id: ($("#businessBrand").data("kendoComboBox")==null?
		    					$("#businessBrand").attr("data-id-value"):$("#businessBrand").data("kendoComboBox").value().trim()),
		    			name:($("#businessBrand").data("kendoComboBox")==null?
		    					$("#businessBrand").val():$("#businessBrand").data("kendoComboBox").text().trim()),
		    	};
				
				portal.isLocal = true;
					if(busName != portal.enterpriseName){
						portal.isLocal = false;
				}
				var product = {
						id: $("#id").val().trim(), 
						name: $("#name").val().trim(),  // 产品名称
						otherName: $("#otherName").val().trim(), // 产品别名
						barcode: $("#barcode").val().trim(),  // 产品barcode
						format: $("#format").val().trim(),  // 规格
						regularity:lims.convertRegularityToItem($("#regularity").val().trim()), // 国家标准 
						status: $("#status").val().trim(), // 状态
						characteristic: $("#characteristic").val().trim(),  // 特色
						cstm: $("#cstm").val().trim(),  // 适宜人群
						businessBrand: businessBrand,  // 商标
						category: category,  // 产品种类
						ingredient: $("#ingredient").val().trim(),  // 配料
						des: $("#des").val().trim(), // 产品描述
						note: $("#note").val().trim(), // 备注
						proAttachments: portal.aryProAttachments,// 产品图片
						listOfCertification: fsn.getCerts(),  // 认证信息
						listOfNutrition:mws.getProductNutris(), //营业标签
                        expirationDate: portal.countExpirday(),//保质天数
                        expiration:  portal.getExpirDay(),//保质期
                        unit: $("#productUnit").val().trim(),//单位
                        local:portal.isLocal,
						packageFlag:'0',
		    	};
				/* 销往企业ids */
				var customerIds = "";
				if(portal.busType == "流通企业.供应商"){
					var customerItems = $("#customerSelect").data("kendoMultiSelect").dataItems();
					for ( var entry in customerItems) {
						customerIds += (customerItems[entry].id + ",");
					}
				}
				product.selectedCustomerIds = customerIds;
				return product;
			};
			
			 $("#btn_saveOK").click(function(){
				 $("#saveWindow").data("kendoWindow").close();
				 _fn.prototype.upload();
			 });
			 
			 $("#btn_saveCancel").click(function(){
				 $("#saveWindow").data("kendoWindow").close();
			 });
			 //引进产品确定和取消按钮
			 $("#btn_leadOK").click(function(){
				 _fn.prototype.leadProduct();
			 });
			 
			 $("#btn_leadCancel").click(function(){
				 $("#lead_window").data("kendoWindow").close();
			 });
			 
			 //新增执行标准新增、确定和取消按钮
			 $("#regularityDiv").click(function(){
			 	var index = $("#category2").data("kendoComboBox").select();
				if(index==-1){
					lims.initNotificationMes("请先选择食品分类！", false);
					return;
				}else{
					var index = $("#category2").data("kendoComboBox").select();
					if(index==-1){
						lims.initNotificationMes("请先选择食品分类后再新增执行标准！", false);
						return;
					}
					//清空信息
					$("#regularity_type").val("");
					$("#regularity_year").val("");
					$("#regularity_name").val("");
					$("#regularityInfo").data("kendoComboBox").value("");
					$("#regularityList").text("");
					
					//执行标准赋值
					portal.setRegularityValue();
					$("#addRegularity_window").data("kendoWindow").open().center();
				}
			 });
			 $("#btn_regularity_save").click(function(){
				$("#regularity").val($("#regularityList").text());
				$("#addRegularity_window").data("kendoWindow").close();
			 });
			 
			 $("#btn_regularity_add").click(function(){
			 	portal.addRegularity();
			 });
			 
			 $("#btn_regularity_cancel").click(function(){
				 $("#addRegularity_window").data("kendoWindow").close();
			 });
			 
			//新增执行标准
			portal.addRegularity = function(){
				//空值校验
				if($("#regularity_type").val().replace(/[ ]/g,"")==""){
					lims.initNotificationMes("请填写标准类型！", false);
					return;
				}
				if($("#regularity_year").val().replace(/[ ]/g,"")==""){
					lims.initNotificationMes("请填写执行序号及年份！", false);
					return;
				}
				//添加执行标准时屏蔽标准名称以为必填
				/*if($("#regularity_name").val().replace(/[ ]/g,"")==""){
					lims.initNotificationMes("请填写标准名称！", false);
					return;
				}*/
				//如果超过12，则不允许添加
				var ul = document.getElementById("regularityList");
				var count = ul.getElementsByTagName("li").length;
				if(count>=12){
					lims.initNotificationMes("执行标准过多，不能再添加！", false);
					return;
				}
				//将新增标准格式化后添加到列表中
			 	var text = $("#regularity_type").val().replace(/[ ]/g,"")+" "+$("#regularity_year").val().replace(/[ ]/g,"")+" "+$("#regularity_name").val();		
				if(!upload.validaRegularity(text)){return;}
				var li= document.createElement("li");
				li.innerHTML="<a herf='#' title='"+text+"'><span class='k-icon k-cancel' onclick='delRegularity(this)'></span>"+text+";</a>";
				ul.appendChild(li);
				//清空信息
				$("#regularity_type").val("");
				$("#regularity_year").val("");
				$("#regularity_name").val("");
			}
			
			//执行标准赋值
			portal.setRegularityValue = function(){
				/*刷新执行标准datasource*/
				$.ajax({
					url:portal.HTTP_PREFIX + "/testReport/searchLastCategory/"+$("#category2").data("kendoComboBox").value()+"?categoryFlag="+false,
					type:"GET",
					dataType:"json",
					async:false,
					success:function(value){
						if(value.result.status == "true"){
							$("#regularityInfo").data("kendoComboBox").setDataSource(value.data);
							if(value.data==null||value.data.length<=0){
								lims.initNotificationMes("该二级分类下没有执行标准，请先新增执行标准！", false);
							}													
						}
					},	
				});
				//将是执行标准格式化添加到已选择的执行标准框里
				var text = $("#regularity").val().trim();
				var regularity = text.split(";");
				var ul = document.getElementById("regularityList");
				var ds = $("#regularityInfo").data("kendoComboBox").dataSource.data();
				//将执行标准格式化添加到已选择的执行标准框中
				for (var i = 0; i < regularity.length - 1; i++) {
					var li = document.createElement("li");
					li.innerHTML = "<a herf='#' title='" + regularity[i] + "'><span class='k-icon k-cancel' onclick='delRegularity(this)'></span>" + regularity[i] + ";</a>";
					ul.appendChild(li);
				}
				//将执行标准datasource进行筛选
				for(var j=0;j<ds.length;j++){
					if(text.indexOf(ds[j].name)!=-1){
						$("#regularityInfo").data("kendoComboBox").dataSource.remove(ds[j]);
						j=j-1;
					}
				};
			}
			
			// save 数据保存
			_fn.prototype.save=function(){
				var mws = this;
				if(!mws.validateProduct()){
					return;
				};
				if(portal.aryProAttachments.length==0){
					$("#saveWindow").data("kendoWindow").open().center();
				}else{
					_fn.prototype.upload();
				}
			};
			
			//引进该barcode 的产品
			_fn.prototype.leadProduct = function(){
				var barcode = $("#barcode").val().trim();
				var customerItems = $("#customerSelect").data("kendoMultiSelect").dataItems();
				var _customerItems = $("#customerSelect_").data("kendoMultiSelect").dataItems();
                if (customerItems.length < 1 && _customerItems.length<1) {
                    lims.initNotificationMes("请选择销往企业", false);
					return ;
                }
				/* 保存产品与当前登录企业的关系 */
				$.ajax({
					url : portal.HTTP_PREFIX + "/product/leadProduct/" + barcode,
					type : "GET",
					datatype : "json",
					contentType: "application/json; charset=utf-8",
					success : function(returnValue) {
						if (returnValue.result.status == "true") {
							$("#lead_window").data("kendoWindow").close();
							lims.initNotificationMes("条形码为：" + barcode + "的产品引进成功", true);
							$("#barcode").val("");
						}else{
							lims.initNotificationMes("条形码为：" + barcode + "的产品引进失败", false);
						}
					}
				});
				/* 保存 产品-当前登录企业-销往企业 的关系 */
				var productVO = {
						barcode: barcode,
						selectedCustomerIds: portal.customerIds,
				};
				portal.saveSelectCustomer(productVO);
			};
			
			// save 提交验证通过的数据
			_fn.prototype.upload = function() {
				var mws = this;
				var save_product = mws.createProduct();
				if(portal.isNew){
					$("#winMsg").html("正在保存数据，请稍候....");
				}else{
					$("#winMsg").html("正在执行更新操作，请稍候....");
				}
				$("#k_window").data("kendoWindow").open().center();
				/* 所属生产企业名称  */
				var busName = $("#businessName").val().trim();
				$.ajax({
						url : portal.HTTP_PREFIX + "/product/" + busName,
						type : portal.isNew ? "POST" : "PUT",
						datatype : "json",
						contentType: "application/json; charset=utf-8",
						data : JSON.stringify(save_product),
						success : function(returnValue) {
							$("#k_window").data("kendoWindow").close();
							if (returnValue.result.status == "true") {
								lims.initNotificationMes(portal.isNew ? '保存成功！' : '更新成功！', true);
								if (portal.isNew) {
									$("#id").val(returnValue.data.id);
									//把返回的ID保存在cookie中
									/*try {
										if(returnValue.data.id!=null&&returnValue.data.id!=''){
											$.cookie("user_0_edit_product", JSON.stringify({id : returnValue.data.id}), {
												path : '/fsn-core/views/portal/'
											});
										}
								    } catch (e) {
								    }*/
									portal.isNew = false;
									$("#update").show();
									$("#save").hide();
								}
								portal.oldBarcode = returnValue.data.barcode;
								fsn.initialCertGrid(returnValue.data.listOfCertification); // 认证信息列表
								mws.initProductNutri(returnValue.data.listOfNutrition); // 营养报告列表show
								mws.setProAttachments(returnValue.data.proAttachments); // 产品图片列表
								/* 为营养指标字段赋值 */
								$("#nutriLabel").text(returnValue.data.nutriLabel);
								$("ul.k-upload-files").remove();
								//二级分类
								$.ajax({
									url: portal.HTTP_PREFIX + "/testReport/searchLastCategory/" + $("#category2").data("kendoComboBox").value() + "?categoryFlag=" + true,
									type: "GET",
									dataType: "json",
									async: false,
									success: function(value){
										if (value.result.status == "true") {
											$("#category3").data("kendoComboBox").setDataSource(value.data);
											$("#category3").data("kendoComboBox").value(returnValue.data.category.id);
										}
									},
								});
							}
						}
				});
			};
			
			// 页面初始化
			_fn.prototype.init = function() {
				var mws = this;
				//portal.certMap = new Map();
				mws.initialComponent();  // 初始化页面控件
				// 加载数据
				$("#btn_clearProFiles").hide();
				mws.loadDataSource(portal.edit_id);
				
				if(portal.isNew){
					 $("#update").hide();
				}else{
					 $("#save").hide();
				}
				
				// save事件
				$(".k-button").on("click",function() {
					var _this = $(this), _span = _this.find("span.ui-button-text");
					switch (_span.attr("data-fsn-text")) {
					case 'Save':
						mws.save();
						break;
					case 'Update':
						mws.save();
						break;
					default:
						break;
					}
				});
				$("#btn_clearProFiles").bind("click",portal.clearProFiles);
				
				/*
                 * 初始化营养报告 单位的下拉框 
                 *@author tangxin
                 */
				$("#nutriUnitDropDown").kendoDropDownList({
	  				dataSource: [
	    				{ id: 1, name: "g" },
	    				{ id: 2, name: "ml" },
						{ id: 3, name: "KJ" },
	  				],
	  				dataTextField: "name",
	  				dataValueField: "id",
	  				change: function(e) {
	  				    var text = this.text();
	  				    if(text == null) return;
	  				    if(text == "g" || text == "ml"){
	  				    	$("#nutriPer").val("100");
	  				    } else {
	  				    	$("#nutriPer").val("420");
	  				    }
	  				}
				 });
				
				if(globNutriUnit != null && globNutriUnit != ""){
					$("#nutriUnitDropDown").data("kendoDropDownList").text(globNutriUnit);
				}
				
				/* 营养报告条件输入值的验证 */
				 $("#nutriPer").blur(function(){
					 var nutriPer = $("#nutriPer").val().trim();
					 var index = nutriPer.indexOf("0");
					 if(!/^[0-9]*$/.test(nutriPer) || index == 0){
						 fsn.initNotificationMes("此处只能输入整数，或者您输入的值不合法。", false);
						 $("#nutriPer").val("");
					 }
				 });
			};
			
			$("#productUnit").kendoAutoComplete({
			    dataSource: lims.getAutoLoadDsByUrl("/product/getAllUnitName"),
			    filter: "contains",
			    placeholder: "例如：60g、箱、500ml/瓶、2瓶/盒、克/袋",
			    select: portal.onSelectUnitName,
			}); 
		
			portal.getBrandByComboBox=function(brandName){
				var brands = $("#businessBrand").data("kendoComboBox").dataSource.data();
				var brandCagegoryDS = new Array();
				if(brands!=null && brandName!=null){
					for(var i=0;i<brands.length;i++){
						if(brands[i].name == brandName){
							brandCagegoryDS.push({id:brands[i].id,name:(brands[i].brandCategory!=null?brands[i].brandCategory.name:"未知类别."+brandName)});
						}
					}
				}
				return brandCagegoryDS;
			};
			
			$("#brandCategory").kendoDropDownList({
		        dataTextField: "name",
		        dataValueField: "id",
		        dataSource: [],
		        filter: "contains",
		        minLength: 0,
		        index:0
			});
			portal.brandChange=function(){
				if(!portal.isSelectBrand){
					$("#businessBrand").data("kendoComboBox").value("");
					lims.initNotificationMes("请选择下拉框内的品牌！", false);
				}else{
					portal.isSelectBrand=false;
				}
			}
			portal.brandBlurFun=function(){
				var val = $("#businessBrand").val().trim();
				var text = $("#businessBrand").data("kendoComboBox").text().trim();
				if(!isNaN(val)&&portal.isSelectBrand!=null&&portal.isSelectBrand){
					portal.isSelectBrand = false;
					portal.selectBrandId = $("#businessBrand").val().trim();
					return ;
				}
				if(portal.selectBrandId!=null && val==portal.selectBrandId) return;
				/*判断brandv是否是数字，isNaN返回false 表示是数字*/
				//if(!isNaN(val)) return;
				if(val == text) {
					portal.selectBrandId = null;
					lims.initNotificationMes("您输入的品牌不存在，请重新输入！", false);
					return;
				}
				$("#brandCategory").data("kendoDropDownList").setDataSource([]);
				var brandCategoryDs = portal.getBrandByComboBox(text);
				if(brandCategoryDs!=null&&brandCategoryDs.length==1) {
					portal.selectBrandId = null;
					return;
				}
				$("#brandCategory").data("kendoDropDownList").setDataSource(brandCategoryDs);
				$("#chooseBrandWindow").data("kendoWindow").open().center();
			};
			
			$("#btn_bcCancel").click(function(){
				$("#chooseBrandWindow").data("kendoWindow").close();
			});
			
			$("#btn_bcOK").click(function(){
				$("#chooseBrandWindow").data("kendoWindow").close();
				var brandId = $("#brandCategory").data("kendoDropDownList").value();
				$("#businessBrand").data("kendoComboBox").value(brandId);
				portal.isSelectBrand = true;
			});
			/* 流通企业：初始化 所属品牌 */
			_fn.prototype.initialBrands = function(){
				
				/*绑定blur事件*/
//				$("#businessBrand").bind("blur",portal.brandBlurFun);
				$("#businessBrand").bind("change",portal.brandChange);
				
				$("#businessBrand").kendoComboBox({
			        dataTextField: "name",
			        dataValueField: "id",
			        headerTemplate: '<div class="dropdown-header">' +
                    '<span class="k-widget k-header">品牌类型</span>' +
                    '<span class="k-widget k-header">品牌名称</span>' +
                    '</div>',
                    template: '<div class="dropdown">' +
                    '<span class="k-state-default">#: data.brandCategory.name #</span>' +
                    '<span class="k-state-default">#: data.name #</span>' +
                    '</div>',
			        dataSource: [],
			        filter: "contains",
			        minLength: 0,
			        index:0,
			        select: function(e) {
			            portal.isSelectBrand = true;
			          }
			    });
				$.ajax({
					url:portal.HTTP_PREFIX + "/business/business-brand/getMyBrandsAll",
					type:"GET",
					dataType:"json",
					async:false,
					success:function(returnValue){
						if(returnValue.result.status == "true"){
							$("#businessBrand").data("kendoComboBox").setDataSource(returnValue.data);
							if(portal.isNew){
								 if(returnValue.data.length>0){
									 $("#businessBrand").data("kendoComboBox").value(returnValue.data[0].name);
									 $("#businessBrand").focus();
								 }else{
									 $("#businessBrand").focus();
									 lims.initNotificationMes("品牌为空，请先到品牌管理页面添加相关品牌信息！", false);
								 }
							}
						}
					},
				});
			};
			
			// 初始化页面组件
			_fn.prototype.initialComponent = function(){
				var mws = this;
				portal.initComponentByType();
				mws.buildUpload("upload_product_files", portal.aryProAttachments, "proFileMsg", "product");
				portal.initialCategorys(); // 初始化产品类别
				fsn.initialCertGrid(null); // 初始化认证信息
				// 初始化营养报告table
				var ds_nutri = [{ name:"", value:"", unit:"", nrv:""},
					     	    { name:"", value:"", unit:"", nrv:""},
					     	    { name:"", value:"", unit:"", nrv:""}];
				mws.initProductNutri(ds_nutri);
				
				$("#panelbar").kendoPanelBar({
		    	    animation: {
		    	        open : { effects: "fadeIn" }
		    	    },
		    	    expandMode: "single"
		    	});
				
				$("#expirationDate-unit").kendoDropDownList({
			    	 dataTextField: "text",
			         dataValueField: "value",
			         dataSource: [
			             { text: "年", value: "年" },
			             { text: "月", value: "月" },
			             { text: "天", value: "天" },
			         ],
			         filter: "contains",
			         suggest: true,
			         index: 0
			    });
			};
		
		/*增加产品认证信息*/
		fsn.addCert = function(){
	    	$("#certification-grid").data("kendoGrid").dataSource.add({ cert:"", certResource:"", endDate:""});
		};	
		
		/*其他认证信息的下拉选项*/
		function certNameDropDownEditor(container, options){
			$('<input required data-text-field="name" data-value-field="name" data-bind="value:' + options.field + '"/>')
			.appendTo(container)
			.kendoDropDownList({
				autoBind: false,
				optionLabel:"请选择产品认证信息",
				dataTextField: "name",
	            dataValueField: "id",
				dataSource: {
					transport: {
						read: portal.HTTP_PREFIX + "/business/getCertificationsByOrg"
					},
					schema: {
						data : function(returnValue) {
							fsn.listCert=returnValue.data;
							if(returnValue.data == null) lims.initNotificationMes("认证信息为空，请到企业基本信息页面添加！", false);
							return returnValue.data == null?[]:returnValue.data;  //响应到页面的数据
			            }
					}
				},
				index: 0,
				change: function(e) {
				    var value = this.value();
				    options.model.cert={name:value};
				    if(fsn.listCert){
				    	for(var i=0;i<fsn.listCert.length;i++){
				    		if(value==fsn.listCert[i].name){
				    			options.model.id=fsn.listCert[i].id;
				    			options.model.certResource={name:fsn.listCert[i].fileName,url:fsn.listCert[i].url};
				    			options.model.endDate=fsn.formatGridDate(fsn.listCert[i].endDate);
				    			$("#certification-grid").data("kendoGrid").refresh();
				    			break;
				    		}
				    	}
				    }
				  },
			});
	    };
		
	    /*认证信息gird的单元格的编辑事件*/
		function certResourceEditor(container, options){
			if(options.model.certResource.url){
				window.open (options.model.certResource.url);
				return;
			}
			$("<span>"+options.model.certResource +"</span>").appendTo(container);
		};
		
		/**
		 * 产品认证信息截止日期编辑事件
		 * @author tangxin
		 */
		function certEndDateEditor(container, options) {
			var input = $("<input />")
			input.attr("name", options.field);
	        input.attr("class", "k-textbox");
	        input.attr("disabled", "disabled");
	        input.val(options.model.endDate);
	        input.appendTo(container);
		}
		
		/*初始化其他认证信息grid*/
		fsn.initialCertGrid=function(cerDs){
			cerDs = (cerDs == null ? [] : cerDs);
			var certGrid = $("#certification-grid").data("kendoGrid");
			if(certGrid == null) {
				$("#certification-grid").kendoGrid({
		    		 dataSource:new kendo.data.DataSource({data: cerDs, page: 1,pageSize: 10 }),
		    		 navigatable: true,
		    		 editable: true,
		    		 pageable: {
		    			 messages: lims.gridPageMessage(),
		    		 },
		    		 toolbar: [
		 		          {template: kendo.template($("#toolbar_template_cert").html())}
		 		          ],
		 		     columns: [
		                   { fild:"id",title:"id",editable: false,width:1},
		                   { field: "cert.name", title:"认证类别",editor: certNameDropDownEditor, width: 60 },
		                   { field: "certResource.name", title:"认证图片名称", editor: certResourceEditor, width: 60 },
		                   { field: "endDate", title:"有效期截止时间",editor: certEndDateEditor,width:50,template: '#=endDate=fsn.formatGridDate(endDate)#'},
		                   { command: [{name:"Remove",
		                   	    text:"<span class='k-icon k-cancel'></span>" + fsn.l("Delete"),
		                   	    click:function(e){
		                   	    	e.preventDefault();
		                   	    	var delItem = $("#certification-grid").data("kendoGrid").dataItem($(e.currentTarget).closest("tr"));
		                   	    	$("#certification-grid").data("kendoGrid").dataSource.remove(delItem);
		                   }}], title: fsn.l("Operation"), width: 30 }],
		    	});
			} else {
				certGrid.setDataSource(new kendo.data.DataSource({data: cerDs, page: 1,pageSize: 10 }));
			}
		};
		
        /**
         *营养报告名称下拉选择方法
         *@author tangxin
         */
		function nutriNameDropDownEditor(container, options){
			$('<input required data-text-field="name" data-value-field="name" data-bind="value:' + options.field + '"/>')
			.appendTo(container)
			.kendoDropDownList({
				autoBind: false,
                optionLabel:"请选择...",
				dataTextField: "name",
                dataValueField: "id",
				dataSource: {
					transport: {
						read: portal.HTTP_PREFIX + "/product/getStandNutris"
					},
					schema: {
						data : function(returnValue) {
							return removeRepeatNutri(returnValue.data); //响应到页面的数据
			            }
					}
				}
                
			});
		}
		
		// 初始化营养报告
		_fn.prototype.initProductNutri = function (nutrDS) {
			nutrDS = (nutrDS == null ? [] : nutrDS);
			var nutriGrid = $("#product-nutri-grid").data("kendoGrid");
			if(nutriGrid == null) {
				$("#product-nutri-grid").kendoGrid({
					dataSource:new kendo.data.DataSource({data: nutrDS, page: 1,pageSize: 30 }),
		    		navigatable: true,
		    		editable: true,
		    		toolbar: [
		     		          {template: kendo.template($("#toolbar_template_nutri").html())}
		     		          ],
                    columns: [
                        {field: "id", title: "id", editable: false, width:1},
                        {field: "name", title: "项目", editor: nutriNameDropDownEditor},
                        {field: "value", title: "值",editor:portal.autoNutriValue},
                        {field: "unit", title:"单位",editor:portal.autoNutriUnit},
                        {field: "nrv", title: "NRV(%)",editor:portal.autoNutriNrv},
                        { command: [{name:"Remove",
                       	    text:"<span class='k-icon k-cancel'></span>" + fsn.l("Delete"), 
                       	    click:function(e){
                       	    	e.preventDefault();
                       	    	var NutriGrid = $("#product-nutri-grid").data("kendoGrid");
                       	    	var delItem = NutriGrid.dataItem($(e.currentTarget).closest("tr"));
                       	    	NutriGrid.dataSource.remove(delItem);
                       }}], title: fsn.l("Operation"), width: 100 }],
                });
			} else {
				nutriGrid.setDataSource(new kendo.data.DataSource({data: nutrDS, page: 1,pageSize: 30 }));
			}
		};
			
			/* 
             *移除已经选择的营养标签 
             *@author tangxin
             */
			 function removeRepeatNutri(listNutris){
				 if(listNutris == null && listNutris.length<1){
					 return listNutris;
				 }
				 var selectNutris = upload.Product.getProductNutris();
				 if(selectNutris == null || selectNutris.length < 1){
					 return listNutris;
				 }
				 for(var i=0;i<selectNutris.length;i++){
					 var nutriName = selectNutris[i].name ;
					 for(var j=0;j<listNutris.length;j++){
						 if(nutriName == listNutris[j].name){
							 listNutris.splice(j,1);
						 }
					 }
				 }
				 return listNutris;
			 }
			
			// 封装营养报告
			_fn.prototype.getProductNutris = function(){
		    	 var productNutris=[];
		    	 var nutris=$("#product-nutri-grid").data("kendoGrid").dataSource.data();
		    	 var nutriPer ="每" + $("#nutriPer").val().trim() + $("#nutriUnitDropDown").data("kendoDropDownList").text();
		    	 var j=0;
		    	 for(var i=0;i<nutris.length;i++){
		    		 if(nutris[i].name==""){
		    			 j++;
		    			 continue;
		    		 }
		    		 productNutris[i-j]={
		    				 id: nutris[i].id==null?"":nutris[i].id,
		    				 name: nutris[i].name,
		    				 value: nutris[i].value,
		    				 unit: nutris[i].unit,
		    				 per: nutriPer,
		    				 nrv: nutris[i].nrv,
		    		 };
		    	 }
		    	 return productNutris;
		     };
			
		     // 从页面删除产品图片
		     fsn.removeRes = function(resID){
				 var dataSource = new kendo.data.DataSource();
				 for(var i=0; i<portal.aryProAttachments.length; i++){
		        	 if(portal.aryProAttachments[i].id == resID){
		        		 while((i+1)<portal.aryProAttachments.length){
		        			 portal.aryProAttachments[i] = portal.aryProAttachments[i+1];
		        			 i++;
		        		 }
		        		 portal.aryProAttachments.pop();
		        		 break;
		        	 }
		         }
				 
				 if(portal.aryProAttachments.length>0){
					for(i=0; i<portal.aryProAttachments.length; i++){
						dataSource.add({attachments:portal.aryProAttachments[i]});
					}
				 }
				 $("#proAttachmentsListView").data("kendoListView").setDataSource(dataSource);
					if(portal.aryProAttachments.length == 0){
						$("#logListView").hide();
				 }
			 };
				
		    // 初始化上传按钮
			_fn.prototype.buildUpload = function(id, attachments, msg, flag){
			    	 $("#"+id).kendoUpload({
			        	 async: {
			                 saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/" + flag,
			                 removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
			                 autoUpload: true,
			                 removeVerb:"POST",
			                 removeField:"fileNames",
			                 saveField:"attachments",
			        	 },localization: {
			                 select:id=="upload_product_files"?fsn.l("upload_Product"):fsn.l("upload_cert"),
			                 retry:lims.l("retry",'upload'),
			                 remove:lims.l("remove",'upload'),
			                 cancel:lims.l("cancel",'upload'),
			                 dropFilesHere: lims.l("drop files here to upload",'upload'),
			                 statusFailed: lims.l("failed",'upload'),
			                 statusUploaded: lims.l("uploaded",'upload'),
			                 statusUploading: lims.l("uploading",'upload'),
			                 uploadSelectedFiles: lims.l("Upload files",'upload'),
			             },
                         upload: function(e){
                             var files = e.files;
                              $.each(files, function () {
                            	  if(this.name.length > 100){
	                       		  		lims.initNotificationMes('上传的文件名称应该小于50个汉字！',false);
	          		                    e.preventDefault();
	          		                    return;
                       	  	  	  }
	                              var extension = this.extension.toLowerCase();
	                              if(id =="upload_product_files" ||id =="upload_certification_files"){
	                                  if (extension != ".png" && extension != ".bmp" && extension != ".jpeg" && extension != ".jpg") {
	                                      lims.initNotificationMes('图片格式错误,请上传 .png .bmp .jpeg .jpg类型图片!', false);
	                                      e.preventDefault();
	                                  }
	                              }
                             
                            });
                         },
			             multiple:true,
			             success: function(e){
			            	 if(e.response.typeEror){
			            		 $("#"+msg).html("文件类型有误，该文件不会被保存，请删除后重新上传：.png .bmp .jpeg .jpg格式的文件！");
			            		 return;
			            	 }
			            	 if(e.response.morSize){
			            		 $("#"+msg).html("您上传的文件已经超过10M，请删除后重新上传pdf、png、bmp、jpeg .jpg格式的文件!");
			            		 return;
			            	 }
			                 if (e.operation == "upload") {
			                	 if(msg=="proFileMsg"){
			                		 lims.log("upload");
			                         lims.log(e.response);
			                         attachments.push(e.response.results[0]);
			                     }
								 $("#"+msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
			                 }else if(e.operation == "remove"){
			                     lims.log(e.response);
			                     for(var i=0; i<attachments.length; i++){
			                    	 if(attachments[i].name == e.files[0].name){
			                    		 while((i+1)<attachments.length){
			                    			 attachments[i]=attachments[i+1];
			                    			 i++;
			                    		 }
			                    		 attachments.pop();
			                    		 break;
			                    	 }
			                     }
			                 }
			             },
			             remove:function(e){
			            	 if(msg=="repFileMsg"){
			            		 $("#"+msg).html("("+fsn.l("Note: The file size can not exceed 10M! Only supports file formats: pdf")+")");
			            	 }else{
			            		 $("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
			            	 }
			             },
			             error:function(e){
			            	 $("#"+msg).html(fsn.l("An exception occurred while uploading the file! Please try again later ..."));
			             },
			       });
			};
			
			return _fn;
		})();
		this.Product = new Product();
	}).call(upload);
	
     portal.autoNutriName=function(container,options){
		 var input = $("<input/>");
		 portal.buildAutoComplete(input,container,options,portal.getAutoItemsDS(1));
	 };
     
	 portal.autoNutriUnit=function(container,options){
		 var input = $("<input/>");
		 globNutrModel = options.model;
		 portal.buildAutoComplete(input,container,options,portal.getAutoItemsDS(3));
		 input.blur(function(){
			 var unit = $(this).val().trim();
			 if(unit !="" && !/^[a-zA-Zµ]+$/.test(unit)){
				 if(globNutrModel != null){
					 globNutrModel.unit = "";
				 }
				 fsn.initNotificationMes(fsn.l("Units can only be letters")+"!",false);
			 }
		 });
	 };

	 /*
      * 全局的营养报实体 
      * 营养报告值自动加载方法
      *@author tangxin
      */
	 var globNutrModel = null;
	 portal.autoNutriValue=function(container,options){
		 var input = $("<input/>");
		 globNutrModel = options.model;
		 portal.buildAutoComplete(input,container,options,portal.getAutoItemsDS(2));
		 input.blur(function(){
			 var value = $(this).val().trim();
			 /* 验证指定的值只能是整数或者浮点数  */
			 validateIntegerAndfractional(value,"value");
		 });
	 };
	 
	 portal.autoNutriNrv=function(container,options){
		 var input = $("<input/>");
		 globNutrModel = options.model;
		 portal.buildAutoComplete(input,container,options,portal.getAutoItemsDS(5));
		 input.blur(function(){
			 var value = $(this).val().trim();
			 /* 验证指定的值只能是整数或者浮点数  */
			 validateIntegerAndfractional(value,"nrv");
		 });
	 };
	
	/**
     * 验证指定的值只能是整数或者浮点数
     * @author tangxin
     */
	function validateIntegerAndfractional(value,target){
		if(value == null || value == ""){
			return;
		}
		var regStr = /^[0-9]*$|([0-9]*(\.)[0-9]*)$/;
		if(!regStr.test(value)){
			 if(globNutrModel != null){
				 if(target == "nrv"){
					 globNutrModel.nrv = "";
				 }else if(target == "value"){
					 globNutrModel.value = "";
				 }
			 }
			 fsn.initNotificationMes(fsn.l("Enter only integer or fractional")+"!",false);
		 }
	}
	 
	portal.initKendoWindow("k_window","保存状态","300px","60px",false,'[]');
	portal.initKendoWindow("saveWindow","警告","400px","",false,'["Close"]');
	portal.initKendoWindow("chooseBrandWindow","请选择品牌类别","320px","200px",false,'[]');
	portal.initKendoWindow("lead_window","引进产品","450px","240px",false,'[]');
	portal.initKendoWindow("addRegularity_window","选择执行标准","800px","400px",false,null);
	
	//移除执行标准
	 delRegularity = function(e){
	 	var name = e.parentNode.title;
		$("#regularityInfo").data("kendoComboBox").dataSource.add({ name:name});
 		 e.parentNode.parentNode.parentNode.removeChild(e.parentNode.parentNode);
	};
	//验证该标准是否已经选择
	upload.validaRegularity = function(text){
		var ul = document.getElementById("regularityList");
		var li = ul.getElementsByTagName("li");
		if (li.length >= 1) {
			for (var i = 0; i < li.length; i++) {
				var value = li[i].innerHTML;
				if (value.length > value.replace(text, "").length) {
					lims.initNotificationMes(text + "该标准已经被选择，不能再次选择！", false);
					return false;
				}
			}
		}
		return true;
	}
	
	/**
     * 为营养报告grid上的条件赋值 
     * @author tangxin
     */
	var globNutriUnit = null;
	function initNutriPer(listNutris){
		if(listNutris == null || listNutris.length == 0){
			return ;
		}
		var nutriPer = listNutris[0].per;
		if(nutriPer == null || nutriPer == ""){
			return ;
		}
		globNutriUnit = null;
		nutriPer = nutriPer.toLowerCase();
		if(nutriPer.indexOf("g") > -1){
			var val = "";
			if(nutriPer.indexOf("每") > -1){
				val = nutriPer.substr(1,nutriPer.length-2);
			}else{
				val = nutriPer.substr(0,nutriPer.length-2);
			}
			$("#nutriPer").val(val);
			globNutriUnit = "g";
			if($("#nutriUnitDropDown").data("kendoDropDownList")){
				$("#nutriUnitDropDown").data("kendoDropDownList").text("g");
			}
			return;
		}
		
		if(nutriPer.indexOf("ml") > -1){
			var val = "";
			if(nutriPer.indexOf("每") > -1){
				val = nutriPer.substr(1,nutriPer.length-3);
			}else{
				val = nutriPer.substr(0,nutriPer.length-3);
			}
			$("#nutriPer").val(val);
			globNutriUnit = "ml";
			if($("#nutriUnitDropDown").data("kendoDropDownList")){
				$("#nutriUnitDropDown").data("kendoDropDownList").text("ml");
			}
			return;
		}
		
		if(nutriPer.indexOf("kj") > -1){
			var val = "";
			if(nutriPer.indexOf("每") > -1){
				val = nutriPer.substr(1,nutriPer.length-3);
			}else{
				val = nutriPer.substr(0,nutriPer.length-3);
			}
			$("#nutriPer").val(val);
			globNutriUnit = "KJ";
			if($("#nutriUnitDropDown").data("kendoDropDownList")){
				$("#nutriUnitDropDown").data("kendoDropDownList").text("KJ");
			}
			return;
		}
	};
	
	upload.Product.init();
});