$(function(){
	var fsn = window.fsn = window.fsn || {};
	var eWidget = fsn.eWidget = fsn.eWidget||{};
	var common = fsn.common = fsn.common || {};
	var merchandiseinfo = fsn.merchandiseinfo = fsn.merchandiseinfo || {};
	$.extend(merchandiseinfo,common);
	merchandiseinfo.datasource = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
				url : function(options){
                	if(options.filter){
                		var configure = filter.configure(options.filter);
                 		return fsn.getHttpPrefix() + "/erp/merchandiseInfo/reglist/" + options.page + "/" + options.pageSize + "/"+configure;
                	};
                	return fsn.getHttpPrefix() + "/erp/merchandiseInfo/list/" + "local";
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
        			barcode:{type:"string"},
                 	name:{type:"string"},
                 	safeNumber:{type:"number"},
                 	note:{type:"string"},
                 	format:{type:"string"},
                 	inspectionReport:{type:"string",},
                 	firstStorage:{
                 		no:{type:"string"},
                     	name:{type:"string"}
                 	},
                 	type:{
                 		id:{type:"number"},
                     	name:{type:"string"}
                 	},
                 	unit:{
                 		id:{type:"number"},
                     	name:{type:"string"}
                 	},
                 	category:{
                 		id:{type:"number"},
                     	name:{type:"string"}
                 	}
                 }
            },
        	data : function(dataValue) {
				if(dataValue.result.status=="true"){
					 return dataValue.data.listOfModel; 
				}else{
					return null;
				}
                
            },
            total : function(dataValue) {
				if(dataValue.result.status=="true"){
					 return dataValue.data.count; 
				}else{
					return 0;
				}
            }  
        },
        batch : true,
        page:1,
        pageSize : 10, //每页显示个数
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	merchandiseinfo.columns = [
	               {field: "barcode", title: "商品条形码", width:"auto",editor:false},
	               {field: "name", title: "商品名称", width:"auto"},
	               {field: "format", title: "商品规格", width:"auto", filterable: false},
	               {field: "unit.name", title: "商品单位", width:"auto", 
	            	   template : function(dataItem) {
	            		   if(dataItem.unit == null) {
	            			   return "";
	            		   } else {
	            			   return dataItem.unit.name;
	            		   }
	            	   }
	               },{
				   	command:[{
						name:"初始化",
						text:"<span class='k-icon k-edit'></span>初始化",
						click: function(e){
							var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
	            	   var addWin = eWidget.getWindow("OPERATION_WIN","商品信息",common.SIMPLE_MODEL_NAME, common.realObj.win_width == null ? null:common.realObj.win_width);
					   addWin.data("kendoWindow").title("初始化"+common.SIMPLE_MODEL_NAME);
					   var win = $("#OPERATION_WIN").data("kendoWindow");
					   win.setOptions({
					   		width:450,
					   })
					   //绑定值
	            	   bindDataItem(true,dataItem);
					    e.preventDefault();
	            	   addWin.data("kendoWindow").open().center();
					   $("#cancel").unbind("click");
					   $("#cancel").bind("click",function(){
					   	$("#OPERATION_WIN").data("kendoWindow").close();
					   });
		           		$("#confirm").unbind("click");
		           		$("#confirm").bind("click",function(){
		           			if (!merchandiseinfo.validator.validate()) {
								return;
							}
								//校验值
								var hasNull = verifyNull();
								if (hasNull) {
									return;
								}
									var model = buildModel(true);
									model.product.id=dataItem.id;
									$.ajax({
										url: fsn.getHttpPrefix() + "/erp/initializeProduct",
										type: "PUT",
										dataType: "json",
										async: true,
										contentType: "application/json; charset=utf-8",
										data: JSON.stringify(model),
										success: function(dataValue){
											if(dataValue.result.status=="true"){
												fsn.initNotificationMes("初始化成功！", true);
												$("#OPERATION_WIN").data("kendoWindow").close();
												merchandiseinfo.introduceProducts.read();
												$("#Simple_Grid").data("kendoGrid").dataSource.read();		
											}else{
												fsn.initNotificationMes("初始化失败！", false);
											}
										
										}
									});
		        		});
						},},
						{
							name:"review",
	                            		text:"<span class='k-icon k-cancel' ></span>" + fsn.l("Preview"), 
	       								click: function(e){
											e.preventDefault();
											var editRow = $(e.target).closest("tr");
											var temp = this.dataItem(editRow);
											merchandiseinfo.viewProduct(temp.id);
										}
						}],	        		
						title: "操作",
                   		width: "15%"	
				   }];
				   

 //引进商品未初始化列表-------------------------
	merchandiseinfo.notIntroduceProducts = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
				url : function(options){
                	if(options.filter){
                		var configure = filter.configure(options.filter);
                 		return fsn.getHttpPrefix() + "/erp/merchandiseInfo/reglist/" + options.page + "/" + options.pageSize + "/"+configure;
                	};
                	return fsn.getHttpPrefix() + "/erp/merchandiseInfo/list/notlocal";
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
        			barcode:{type:"string"},
                 	name:{type:"string"},
                 	safeNumber:{type:"number"},
                 	note:{type:"string"},
                 	format:{type:"string"},
                 	inspectionReport:{type:"string",},
                 	firstStorage:{
                 		no:{type:"string"},
                     	name:{type:"string"}
                 	},
                 	type:{
                 		id:{type:"number"},
                     	name:{type:"string"}
                 	},
                 	unit:{
                 		id:{type:"number"},
                     	name:{type:"string"}
                 	},
                 	category:{
                 		id:{type:"number"},
                     	name:{type:"string"}
                 	}
                 }
            },
        	data : function(dataValue) {
				if(dataValue.result.status=="true"){
					 return dataValue.data.listOfModel; 
				}else{
					return null;
				}
                
            },
            total : function(dataValue) {
				if(dataValue.result.status=="true"){
					 return dataValue.data.count; 
				}else{
					return 0;
				}
            }  
        },
        batch : true,
        page:1,
        pageSize : 10, //每页显示个数
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	
		$("#notIntroduceProducts").kendoGrid({
			dataSource:merchandiseinfo.notIntroduceProducts,
			 pageable: {
	            refresh: true,
	            pageSizes: [5, 10, 20],
	            messages: fsn.gridPageMessage(),
	        },
			height:428,
	        width: "auto",
			sortable: true,
	        selectable: true,
	        resizable: false,
			toolbar: [
			          {template: kendo.template($("#toolbar_template").html())}
			         ],
			columns : [
	               {field: "barcode", title: "商品条形码", width:"auto",editor:false},
	               {field: "name", title: "商品名称", width:"auto"},
	               {field: "format", title: "商品规格", width:"auto", filterable: false},
	               {
				   	field: "unit.name",
				   	title: "商品单位",
				   	width: "auto",
				   	template: function(dataItem){
				   		if (dataItem.unit == null) {
				   			return "";
				   		}
				   		else {
				   			return dataItem.unit.name;
				   		}
				   	} } ,{command:[{
						name:"初始化",
						text:"<span class='k-icon k-edit'></span>初始化",
						click: function(e){
							var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
	            	   var addWin = eWidget.getWindow("OPERATION_WIN","商品信息",common.SIMPLE_MODEL_NAME, common.realObj.win_width == null ? null:common.realObj.win_width);
					   addWin.data("kendoWindow").title("初始化"+common.SIMPLE_MODEL_NAME);
					   var win = $("#OPERATION_WIN").data("kendoWindow");
					   win.setOptions({
					   		width:450,
					   })
					   //绑定值
	            	   bindDataItem(true,dataItem);
					    e.preventDefault();
	            	   addWin.data("kendoWindow").open().center();
					   $("#cancel").unbind("click");
					   $("#cancel").bind("click",function(){
					   	$("#OPERATION_WIN").data("kendoWindow").close();
					   });
		           		$("#confirm").unbind("click");
		           		$("#confirm").bind("click",function(){
		           			if (!merchandiseinfo.validator.validate()) {
								return;
							}
								//校验值
								var hasNull = verifyNull();
								if (hasNull) {
									return;
								}
									var model = buildModel(true);
									model.product.id=dataItem.id;
									$.ajax({
										url: fsn.getHttpPrefix() + "/erp/initializeProduct",
										type: "PUT",
										dataType: "json",
										async: true,
										contentType: "application/json; charset=utf-8",
										data: JSON.stringify(model),
										success: function(dataValue){
											if(dataValue.result.status=="true"){
												fsn.initNotificationMes("初始化成功！", true);
												$("#OPERATION_WIN").data("kendoWindow").close();
												merchandiseinfo.introduceProducts.read();
												merchandiseinfo.notIntroduceProducts.read();
											}else{
												fsn.initNotificationMes("初始化失败！", false);
											}
										
										}
									});
								
		        		});
						}},
						 {
							name:"review",
	            			text:"<span class='k-icon k-cancel' ></span>" + fsn.l("Preview"), 
							click: function(e){
								e.preventDefault();
								var editRow = $(e.target).closest("tr");
								var temp = this.dataItem(editRow);
								merchandiseinfo.viewProduct(temp.id);
							}
						} ],
						title: "操作",
                   		width: "18%"					
					}]
		});
	
	 //已初始化产品列表 --------------------------------
	merchandiseinfo.introduceProducts = new kendo.data.DataSource({
		transport: {
			read : {
	                type : "GET",
					url : function(options){
            			if(options.filter){
            				var configure = filter.configure(options.filter);
             				return fsn.getHttpPrefix() + "/erp/initializeProduct/listall/" + options.page + "/" + options.pageSize + "/"+configure;
            			};
            			return fsn.getHttpPrefix() + "/erp/initializeProduct/listall/InitializeProduct";
					},
	               	 dataType : "json",
	               		 contentType : "application/json"
        		},
   			 },
   			 schema: {
		        	model: {
		        		 fields: {
		        			barcode:{type:"string"},
		                 	name:{type:"string"},
		                 	safeNumber:{type:"number"},
		                 	note:{type:"string"},
		                 	format:{type:"string"},
		                 	inspectionReport:{type:"string",},
		                 	firstStorage:{
		                 		no:{type:"string"},
		                     	name:{type:"string"}
		                 	},
		                 	type:{
		                 		id:{type:"number"},
		                     	name:{type:"string"}
		                 	},
		                 	unit:{
		                 		id:{type:"number"},
		                     	name:{type:"string"}
		                 	},
		                 	category:{
		                 		id:{type:"number"},
		                     	name:{type:"string"}
		                 	}
		                 }
        		 },
        	data : function(dataValue) {
				if(dataValue.result&&dataValue){
					 return dataValue.result.listOfModel; 
				}else{
					return null;
				}
                
            },
            total : function(dataValue) {
				if(dataValue.result&&dataValue){
					 return dataValue.result.count; 
				}else{
					return 0;
				}
            }  
        },
        batch : true,
        page:1,
        pageSize : 10, //每页显示个数
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	
		$("#IntroduceProducts").kendoGrid({
			dataSource:merchandiseinfo.introduceProducts,
			 pageable: {
	            refresh: true,
	            pageSizes: [5, 10, 20],
	            messages: fsn.gridPageMessage(),
	        },
			height:428,
	        width: "auto",
			sortable: true,
	        selectable: true,
	        resizable: false,
			toolbar: [
			          {template: kendo.template($("#toolbar_template").html())}
			         ],
			columns : [
	               {field: "product.barcode", title: "商品条形码", width:"auto",editor:false},
	               {field: "product.name", title: "商品名称", width:"auto"},
	               {field: "product.format", title: "商品规格", width:"auto"},
	               {field: "type.name", title: "商品类型", width:"auto", 
	            	   template : function(dataItem) {
	            		   if(dataItem.type == null) {
	            			   return "";
	            		   } else {
	            			   return dataItem.type.name;
	            		   }
	            	   }
	               },
	               {field: "product.unit.name", title: "商品单位", width:"auto", 
	            	   template : function(dataItem) {
	            		   if(dataItem.product.unit == null) {
	            			   return "";
	            		   } else {
	            			   return dataItem.product.unit.name;
	            		   }
	            	   }
	               },
	               {field: "firstStorage.name", title: "商品首选仓库", width:"auto", 
	            	   template : function(dataItem) {
	            		   if(dataItem.firstStorage == null) {
	            			   return "";
	            		   } else {
	            			   return dataItem.firstStorage.name;
	            		   }
	            	   }
	               },
	               {field: "inspectionReport", title: "是否需要质检报告", width:"auto", filterable: false,
	            	   template : function(dataItem) {
	            		   if(dataItem.inspectionReport == "true") {
	            			   return "是";
	            		   } else {
	            			   return "否";
	            		   }
	            	   }
	               },
	               {command:[{
				   		name:"编辑",
						text:"<span class='k-icon k-edit'></span>编辑",
						click:function(e){
	            	   var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
	            	   var addWin = eWidget.getWindow("OPERATION_WIN","商品信息",common.SIMPLE_MODEL_NAME, common.realObj.win_width == null ? null:common.realObj.win_width);
	            	   addWin.data("kendoWindow").title("编辑"+common.SIMPLE_MODEL_NAME);
					   var win = $("#OPERATION_WIN").data("kendoWindow");
					   win.setOptions({
					   		width:450,
					   })
	            	   //绑定值
	            	   bindDataItem(false,dataItem);
					    e.preventDefault();
					   addWin.data("kendoWindow").open().center();
					   $("#cancel").unbind("click");
					   $("#cancel").bind("click",function(){
					   	$("#OPERATION_WIN").data("kendoWindow").close();
					   });
		           		$("#confirm").unbind("click");
		           		$("#confirm").bind("click",function(){
		           			if(!merchandiseinfo.validator.validate()){
								return;
							}
								//校验值
								var hasNull = verifyNull();
								if (hasNull) {
									return;
								}							
										var model = buildModel(false);
										//校验是否标记过
										if(model.safeNumber==dataItem.safeNumber
											&&(model.inspectionReport==(dataItem.inspectionReport=="true"))
											&&model.firstStorage.no==dataItem.firstStorage.no
											&&model.type.id==dataItem.type.id){
												fsn.initNotificationMes("还没有修改过数据！", false);
												return;
											}
										model.product.id=dataItem.product.id;
										$.ajax({
											url: fsn.getHttpPrefix() + "/erp/initializeProduct",
											type: "PUT",
											dataType: "json",
											async: true,
											contentType: "application/json; charset=utf-8",
											data: JSON.stringify(model),
											success: function(dataValue){
												if(dataValue.result.status=="true"){
													fsn.initNotificationMes("编辑成功！", true);
													$("#OPERATION_WIN").data("kendoWindow").close();
													merchandiseinfo.introduceProducts.read();
												}else{
													fsn.initNotificationMes("编辑失败！", false);
												}
											}
										});		
		        		});
	               }},
				   {
						name:"review",
            			text:"<span class='k-icon k-cancel' ></span>" + fsn.l("Preview"), 
						click: function(e){
							e.preventDefault();
							var editRow = $(e.target).closest("tr");
							var temp = this.dataItem(editRow);
							merchandiseinfo.viewProduct(temp.product.id);
						}
					} ],
	               title: "操作",
                   width: "18%"
	               }
	               ],
			
		});
		
		$("#viewWindow").kendoWindow({
		width:1000,
		visible: false,
		title:"产品预览",
	});
	/**
	 * add, update, delete
	 */
	
	merchandiseinfo.selectedData = {};
	 // 绑定from值-----------------------------
	merchandiseinfo.bindForm = function(){
		bindDataItem(true,merchandiseinfo.selectedData);
				var win = $("#OPERATION_WIN").data("kendoWindow");
					   win.setOptions({
					   		title:"初始化商品信息",
					   })
	}
	 // 初始化本企业产品--------------------------------------------------------------------
	merchandiseinfo.windowUpdateConfrim = function(){
		//validation
		if(!merchandiseinfo.validator.validate()){
			return;
		}
			//值校验，false代表不通过，true代表通过
			var hasNull = verifyNull();
			if (!hasNull) {		
					var model = buildModel(true);
//					var a=merchandiseinfo.selectedData;
					model.product.id=merchandiseinfo.selectedData.id,
					$.ajax({
						url: fsn.getHttpPrefix() + "/erp/initializeProduct",
						type: "PUT",
						dataType: "json",
						contentType: "application/json; charset=utf-8",
						data: JSON.stringify(model),
						success: function(data){
							if (data && data.result) {
								fsn.initNotificationMes("初始化成功！", true);
								$("#OPERATION_WIN").data("kendoWindow").close();
								merchandiseinfo.datasource.read();
								merchandiseinfo.introduceProducts.read();
							}
							merchandiseinfo.datasource.read();
						}
					});
			}
		
	}
	
	
	merchandiseinfo.contactGrid = null;
	merchandiseinfo.contactComboBoxType = null;
	merchandiseinfo.contactComboBoxUnit = null;
	merchandiseinfo.contactComboBoxCategory = null;
	merchandiseinfo.contactComboBoxStorage = null;
	merchandiseinfo.comboBoxCategory = {};
	merchandiseinfo.comboBoxType = {};
	merchandiseinfo.comboBoxUnit = {};
	merchandiseinfo.comboBoxStorage = {};
	merchandiseinfo.contactWin = null;
	merchandiseinfo.selectedContact = null;
	
//kendoComboBox校验
	combobox_change=function(e){
		var judge = e.sender.selectedIndex;
		if(judge==-1){
			this.value("");
			fsn.initNotificationMes("填写内容有误！请重新填写", false);
			this.value("");
		}else{
			return;
		}
	}
	
	
	 // 控件初始化-----------------------
	merchandiseinfo.initRequiredComponents = function(){
		merchandiseinfo.verifySafeNumber = eWidget.verificationNum("safe_number").data("kendoNumericTextBox");
		merchandiseinfo.contactComboBoxType = eWidget.getComboBox("type_id",7).data("kendoComboBox");
		merchandiseinfo.contactComboBoxType.bind("change", combobox_change);
		merchandiseinfo.contactComboBoxUnit = eWidget.getComboBox("unit_id",4).data("kendoComboBox");
		merchandiseinfo.contactComboBoxUnit.bind("change", combobox_change);
		merchandiseinfo.contactComboBoxCategory = eWidget.getComboBox("category_id",8).data("kendoComboBox");
		merchandiseinfo.contactComboBoxCategory.bind("change", combobox_change);
		merchandiseinfo.contactComboBoxStorage = eWidget.getStorageComboBox("first_storage_id",0).data("kendoComboBox");
		merchandiseinfo.contactComboBoxStorage.bind("change", combobox_change);
		merchandiseinfo.contactComboBoxType.setOptions({
			change: function(e) {
				merchandiseinfo.comboBoxType = {
						id:this.value(),
						name:this.text()
				}
			  }
		});
		
		merchandiseinfo.contactComboBoxUnit.setOptions({
			change: function(e) {
				merchandiseinfo.comboBoxUnit = {
						id:this.value(),
						name:this.text()
				}
			  }
		});
		merchandiseinfo.contactComboBoxCategory.setOptions({
			change: function(e) {
				merchandiseinfo.comboBoxCategory = {
						id:this.value(),
						name:this.text()
				}
			  }
		});
		merchandiseinfo.contactComboBoxStorage.setOptions({
			change: function(e) {
				merchandiseinfo.comboBoxStorage = {
						barcode:this.value(),
						name:this.text()
				}
			  }
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
	}

	/**
	 * Save方法时值校验
	 */
	verifyNull = function(){
			if($("#safe_number").val().trim() == "" || $("#safe_number").val().trim() <= 0) {
				fsn.initNotificationMes("请填写正确的安全库存（大于0）",false);
				$("#safe_number").focus();
				return true;
			}else if (fsn.merchandiseinfo.contactComboBoxStorage.value() == "") {
				fsn.initNotificationMes("请填写商品首选仓库信息！", false);
				return true;
			}else if (merchandiseinfo.contactComboBoxType.value() == "") {
				fsn.initNotificationMes("请填写商品类型信息！", false);
			return true;		
			}else{
				return false;
			} 			
	}
	
	
	  //生成model---------------------
	 
		buildModel = function(uninitialized){
			var comboboxType = $("#type_id").data("kendoComboBox");
			var dataItemType = comboboxType.dataItem();
			var typeTem = {
				id: dataItemType.id,
				name: dataItemType.name
			};
			var comboboxStorage = $("#first_storage_id").data("kendoComboBox");
			var dataItemStorage = comboboxStorage.dataItem();
			var storageTem = {
				no: dataItemStorage.no,
				name: dataItemStorage.name,
				active: dataItemStorage.active
			};	
			var model={};
				model={
						product:{
							id:null,
						},
						firstStorage:storageTem,
						type:typeTem,	
						safeNumber:$("#safe_number").val().trim(),		
				}	
				if(document.getElementById("inspection_report").checked){
							model.inspectionReport=true;
						}else{
							model.inspectionReport=false;
						}		
			return model;
		}
		
		 // 绑定dataItem----------------------------------
		 
		bindDataItem = function(uninitialized,dataItem){
			if(uninitialized){
				//绑定未初始化产品
				 		$("[data-bind='value:no']").val(dataItem.barcode);  		
		           	    $("[data-bind='value:name']").val(dataItem.name);
						merchandiseinfo.verifySafeNumber.value("");
						document.getElementById("inspection_report1").checked=true;
		           		$("[data-bind='value:note']").val(dataItem.note);
		           		$("[data-bind='value:specification']").val(dataItem.format);
						fsn.merchandiseinfo.contactComboBoxStorage.value("");
						fsn.merchandiseinfo.contactComboBoxType.value("");
		           		if(dataItem.unit != null) {
		           			fsn.merchandiseinfo.contactComboBoxUnit.value(dataItem.unit.id);
		           		}
						if (dataItem.category != null) {
							$("#category_id").data("kendoComboBox").setDataSource([{
								id: dataItem.category.id,
								name: dataItem.category.name
							}]);
							fsn.merchandiseinfo.contactComboBoxCategory.value(dataItem.category.id);
						}
			}else{
				//绑定已经初始化产品
					   $("[data-bind='value:no']").val(dataItem.product.barcode);	
		           	   $("[data-bind='value:name']").val(dataItem.product.name);
		           	   merchandiseinfo.verifySafeNumber.value(dataItem.safeNumber);
					   if(dataItem.inspectionReport=="true"){
					   		document.getElementById("inspection_report").checked=true;
					   }else{
					   		document.getElementById("inspection_report1").checked=true;
					   }
		           		$("[data-bind='value:inspection_report']").val(dataItem.inspectionReport);
		           		$("[data-bind='value:note']").val(dataItem.product.note);
		           		$("[data-bind='value:specification']").val(dataItem.product.format);
		           		if(dataItem.firstStorage != null) {
		           			fsn.merchandiseinfo.contactComboBoxStorage.value(dataItem.firstStorage.no);
		           		}
		           		if(dataItem.type != null) {
		           			fsn.merchandiseinfo.contactComboBoxType.value(dataItem.type.id);
		           		}
		           		if(dataItem.product.unit != null) {
		           			fsn.merchandiseinfo.contactComboBoxUnit.value(dataItem.product.unit.id);
		           		}
						if (dataItem.product.category != null) {
							$("#category_id").data("kendoComboBox").setDataSource([{
								id: dataItem.product.category.id,
								name: dataItem.product.category.name
							}]);
							fsn.merchandiseinfo.contactComboBoxCategory.value(dataItem.product.category.id);
						} 		
			}
			$("#no").kendoValidator().data("kendoValidator").validate();
       		$("#name").kendoValidator().data("kendoValidator").validate();
       		$("#safe_number").kendoValidator().data("kendoValidator").hideMessages();
       		$("#no").attr("disabled","disabled");
       		$("#name").attr("readonly","readonly");
       		$("#specification").attr("readonly","readonly");
       		$("#note").attr("readonly","readonly");
			fsn.merchandiseinfo.contactComboBoxCategory.readonly(true);
		    fsn.merchandiseinfo.contactComboBoxUnit.readonly(true);
		}
		
	//预览功能
	merchandiseinfo.viewProduct = function(productId){
		if (!productId) {
			return;
		}
		$.ajax({
			url: fsn.getHttpPrefix() + "/product/" + productId,
			type: "GET",
			success: function(returnValue){
				if (returnValue.result.status == "true") {
					merchandiseinfo.setProductValue(returnValue.data);
					merchandiseinfo.setProAttachments(returnValue.data.proAttachments); //show 产品图片
					var listOfCertification = returnValue.data.listOfCertification;
					if (listOfCertification.length > 0) {
						for (var i = 0; i < listOfCertification.length; i++) {
							if (listOfCertification[i].certResource == null) {
								listOfCertification[i].certResource = "";
							}
						}
					}
					merchandiseinfo.initialCertification(listOfCertification); //show 产品认证信息
					$("#viewWindow").data("kendoWindow").open().center();
				}
			},
		});
		// 加载营养报告grid(分页)
       	 merchandiseinfo.initNutriDS(productId);
       	 $("#product-nutri-grid").data("kendoGrid").setDataSource(merchandiseinfo.nutriDS);
	}
		
		merchandiseinfo.setProductValue = function(product){
    	if(product==null){return;}
		$("#id").val(product.id);
		$("#name").val(product.name);
		$("#status").val(product.status);
		$("#format").val(product.format);
		$("#category").val(product.category.name);
		$("#barcode").val(product.barcode);
		$("#expiration").val(product.expiration);
		$("#businessBrand").val(product.businessBrand.name);
		$("#regularity").val(product.regularity);
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
    merchandiseinfo.setProAttachments=function(dataSource){
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
     * 营养信息Grid 分页显示
     * @param {Object} productId
     */
    merchandiseinfo.initNutriDS = function(productId){
    	if(!productId){
    		productId = 0;
    	}
    	var FirstPageFlag = 1;
		merchandiseinfo.nutriDS = new kendo.data.DataSource({
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
	
	/**
	 * 初始化 认证信息
	 * @param {Object} dataSource
	 */
	merchandiseinfo.initialCertification=function(dataSource){
		
		/**
		 * 认证信息  图片触发
		 * @param container
		 * @param options
		 */
		function certResourceEditor(container, options){
			if(options.model.documentUrl){
    			window.open (options.model.documentUrl);
    			return;
    		}
    		$("<span>"+options.model.certResource.name +"</span>").appendTo(container);
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
                {field: "cert.name", title:"认证类别",width:70},
                {field: "certResource.name", title:"相关图片" ,editor: certResourceEditor, width:100},
                {field: "endDate", title:"有效截止至时间",width: 100,template: '#= (new Date(endDate)).format("YYYY-MM-dd")#'}],
        });
		
	};
	
	merchandiseinfo.GRIDID = "Simple_Grid";
	merchandiseinfo.DAILOGID = "OPERATION_WIN"; 
	merchandiseinfo.initailize = function(){
		this.initComponent(merchandiseinfo);
		$("#label_no").html(merchandiseinfo.SIMPLE_MODEL_NAME + "编号");
		$("#label_category").html(merchandiseinfo.SIMPLE_MODEL_NAME + "类型");
		$("#label_description").html(merchandiseinfo.SIMPLE_MODEL_NAME + "描述");
		
		this.initRequiredComponents();
	}
	merchandiseinfo.initailize();
	merchandiseinfo.judgeIsUsed = function(item){
		var flag = true;
		model = {
			barcode:item.barcode,
		}
		$.ajax({
		url: fsn.getHttpPrefix() + "/erp/merchandiseInfo/judgeIsUsed",
		type:"POST",
		dataType:"json",
		async:false,
		contentType: "application/json; charset=utf-8",
		data:JSON.stringify(model),
		success:function(data){
			if(data.result){
				flag = true;
			}else{
				flag = false;
			}
		}
	});
		return flag;
	}
	

	 // search功能-----------------------------------
	$('#search').kendoSearchBox({
				change : function(e) {
					var keywords=e.sender.options.value;
			    	if(keywords.trim()!=""){
			    		$("#Simple_Grid").data("kendoGrid").setDataSource(initStandard(keywords));
			    	    $("#Simple_Grid").data("kendoGrid").refresh();
			    	}else{
			    		$("#Simple_Grid").data("kendoGrid").setDataSource(merchandiseinfo.datasource);
			    		$("#Simple_Grid").data("kendoGrid").refresh();
			    	}
				}
			});
			
			initStandard = function(keywords){
				var ds = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
                url : function(options){
					return fsn.getHttpPrefix() + "/erp/merchandiseInfo/search?keywords="+keywords;
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
        			barcode:{type:"string"},
                 	name:{type:"string"},
                 	safeNumber:{type:"number"},
                 	note:{type:"string"},
                 	preBuyPrice:{type:"string"},
                 	preSalePrice:{type:"string"},
                 	format:{type:"string"},
                 	firstStorage:{
                 		no:{type:"string"},
                     	name:{type:"string"}
                 	},
                 	type:{
                 		id:{type:"number"},
                     	name:{type:"string"}
                 	},
                 	unit:{
                 		id:{type:"number"},
                     	name:{type:"string"}
                 	},
                 	category:{
                 		id:{type:"number"},
                     	name:{type:"string"}
                 	}
                 }
            },
        	data : function(data) {
                return data.result.listOfModel;  
            },
            total : function(data) {
               return  data.result.count;//总条数
            }  
        },
        batch : true,
        page:1,
        pageSize : 10, //每页显示个数
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
				//ds.read();
				return ds;
			}
})
