$(function(){
	var fsn = window.fsn = window.fsn || {};
	var eWidget = fsn.eWidget = fsn.eWidget||{};
	var common = fsn.common = fsn.common || {};
	
	var buss_operation = fsn.buss_operation = fsn.buss_operation || {};
	var productList = new Array();
	var productCount = 0;
	var updataCount = null;
	
	buss_operation.initailize = function(){
		this.initComponent(buss_operation);
		$("#label_no").html(buss_operation.SIMPLE_MODEL_NAME + "编号<font color='red'>*</font>");
		$("#label_type").html(buss_operation.SIMPLE_MODEL_NAME + "类型<font color='red'>*</font>");
		$("#label_note").html(buss_operation.SIMPLE_MODEL_NAME + "备注");
		$("#label_detail_no_name").html(buss_operation.SIMPLE_MODEL_NAME + "编号");
		$("#label_detail_type_name").html(buss_operation.SIMPLE_MODEL_NAME + "类型");
		$("#label_detail_note_name").html(buss_operation.SIMPLE_MODEL_NAME + "备注");
		$("#label_detail_op_name").html(buss_operation.SIMPLE_MODEL_NAME + "操作人员");
		$("#label_detail_ctime_name").html(buss_operation.SIMPLE_MODEL_NAME + "创建时间");
//		buss_operation.initGridColumn();
		this.initRequiredComponents();
	};
	
	$.extend(buss_operation,common);
	
	buss_operation.COMMAND_LIST = {
			add:{elementID:""}
	};
	buss_operation.BUSINESS_FLAG = true;
	
	buss_operation.datasource = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
				url : function(options){
                	if(options.filter){
                		var configure = filter.configure(options.filter);
                 		return fsn.getHttpPrefix() + "/erp/buss/" + buss_operation.SIMPLE_TYPE + "/reglist/" + options.page + "/" + options.pageSize + "/"+configure;;
                	};
                	return fsn.getHttpPrefix() + "/erp/buss/" + buss_operation.SIMPLE_TYPE + "/list";
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
                 	no:{type:"string"},
                 	name:{type:"string"},
                 	type:{
                 		id:{type:"number"},    
                     	name:{type:"string"}
                 	},
                 	note:{type:"string"}
                 }
            },
        	data : function(dataValue) {
        		if(dataValue.resultVO.status=="true"){
        			dataValue.result.listOfModel.forEach(function(item){
        				if(item.type == undefined){
            				item.type = {name:"空"};
        				}
        			});
        			return dataValue.result.listOfModel;  
        		}
                return null;
            },
            total : function(dataValue) {//总条数
            	if(dataValue.resultVO.status=="true"){
            		return  dataValue.result.count;
            	}
            	return 0;
            }  
        },
        batch : true,
        page:1,
        pageSize : 10, //每页显示个数
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	
	/*预览库存商品时，获取商品列表*/
	buss_operation.getViewSampleDS=function(dataItem){
		return  new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){
	            		return fsn.getHttpPrefix() + "/erp/buss/" + buss_operation.SIMPLE_TYPE +"/listinfo?num="+dataItem.no;
					},
	            }
	        },
	        schema: {
	            data : function(returnValue) {
	                return returnValue.result;
	            },
	            total : function(returnValue) {    
	                return returnValue.count;
	            }     
	        },
	        batch : true,
	        page:1,
	        pageSize : 5, //每页显示个数
	        serverPaging : true,
	        serverFiltering : true,
		});
	};
	
	/**
	 * 初始化，gird的column SIMPLE_TYPE=7：商品调拨,SIMPLE_TYPLE=5：商品入库,SIMPLE_TYPLE=6：商品出库
	 */
//	buss_operation.initGridColumn=function(){
		if(buss_operation.SIMPLE_TYPE ==5||buss_operation.SIMPLE_TYPE ==6 ){
			buss_operation.columns = [
			               {field: "no", title: buss_operation.SIMPLE_MODEL_NAME + "单号", width:"auto",editor:false},
			               {field: "type.name", title: buss_operation.SIMPLE_MODEL_NAME + "类型", width:"auto"},
			               {field: "createTime", title: "创建时间", width:"auto",template:'#= (new Date(createTime)).format("YYYY-MM-dd hh:mm:ss") #',filterable: false , width:200},
			               {field: "createUserName", title: "操作人员", width:"auto"},
			               {field: "note", title: "备注"},
			               {command:[{name:"预览",click:function(e){
			            	   var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
			            	   var addWin = eWidget.getWindow("INOUTFLITTING_DETAIL_INFO","详细信息",common.SIMPLE_MODEL_NAME, common.realObj.win_width == null ? null:common.realObj.win_width);
			            	   addWin.data("kendoWindow").open().center();
			            	   var ds = buss_operation.getViewSampleDS(dataItem);
			            	   buss_operation.detailBaseInfo(dataItem,ds);
			               }}]}
			               ];
			} else {
				buss_operation.columns = [
				       	               {field: "no", title: buss_operation.SIMPLE_MODEL_NAME + "单号", width:"auto",editor:false},
				       	               {field: "createTime", title: "创建时间", width:"auto",template:'#= (new Date(createTime)).format("YYYY-MM-dd hh:mm:ss") #',filterable: false , width:200},
				       	               {field: "createUserName", title: "操作人员", width:"auto"},
				       	               {field: "note", title: "备注"},
				       	               {command:[{name:"预览",click:function(e){
				       	            	   var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
				       	            	   var ds = buss_operation.getViewSampleDS(dataItem);
						            	   buss_operation.detailBaseInfo(dataItem,ds);
						            	   var viewWin = eWidget.getWindow("INOUTFLITTING_DETAIL_INFO","详细信息",common.SIMPLE_MODEL_NAME, common.realObj.win_width == null ? null:common.realObj.win_width);
				       	            	   viewWin.data("kendoWindow").open().center();
				       	               }}]}
				       	              ];
			}
//	};
	
	/**
	 * add, update, delete
	 */
	
	buss_operation.win_width = 800;
	
	buss_operation.selectedData = {};
	
	buss_operation.clearForm = function(){
	    productList = new Array();
 		productCount = 0;
 		updataCount = null;
		if(buss_operation.SIMPLE_TYPE == 5){
			$("#in_no").kendoValidator().data("kendoValidator").hideMessages();
		}else if(buss_operation.SIMPLE_TYPE == 6){
			$("#out_no").kendoValidator().data("kendoValidator").hideMessages();
		}else if(buss_operation.SIMPLE_TYPE == 7){
			$("#flitting_no").kendoValidator().data("kendoValidator").hideMessages();
		}
		$("#no").val("");
		$("#no").removeAttr("disabled");
		$("#type").val("");
		$("#note").val("");
		buss_operation.dropListOrderType.value("");
		buss_operation.contactComboBox.value("");
		buss_operation.contactGrid.data("kendoGrid").dataSource.data([]);
		$("#type").kendoValidator().data("kendoValidator").hideMessages();
	};
	
	buss_operation.bindForm = function(){
		$("#no").attr("disabled","disabled");
		$("[data-bind='value:no']").val(buss_operation.selectedData.no);
		$("[data-bind='value:name']").val(buss_operation.selectedData.name);
		$("#note").val(buss_operation.selectedData.note);
	};
	
	
	buss_operation.windowSaveConfrim = function(){
		//validation
		if(buss_operation.validator.validate()){
			var inOrOutDataItem = null;
			
			if(buss_operation.SIMPLE_TYPE == 5){
				inOrOutDataItem = $("#in_no").data("kendoDropDownList").dataItem();
			}else if(buss_operation.SIMPLE_TYPE == 6){
				inOrOutDataItem = $("#out_no").data("kendoDropDownList").dataItem();
			}else if(buss_operation.SIMPLE_TYPE == 7){
				inOrOutDataItem = $("#flitting_no").data("kendoDropDownList").dataItem();
			}
			if(buss_operation.contactGrid.data("kendoGrid").dataSource.data().length==0){
				fsn.initNotificationMes("没有添加任何商品！</br>新增失败！", false);
				return;
			}
			var model = {
					no:inOrOutDataItem.ot_type,
					note:$("#note").val().trim(),
					merchandises:buss_operation.contactGrid.data("kendoGrid").dataSource.data()
			};
			//调拨无类型
			if(buss_operation.SIMPLE_TYPE == 5 || buss_operation.SIMPLE_TYPE == 6){
				model.typeInstance = $("#type").data("kendoComboBox").dataItem().id;
			}
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/buss/" + buss_operation.SIMPLE_TYPE,
				type:"POST",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				data:JSON.stringify(model),
				success:function(dataValue){
					if(dataValue.result.status=="true"){
						fsn.initNotificationMes("添加成功", true);
						$("#OPERATION_WIN").data("kendoWindow").close();
						buss_operation.datasource.read();
					}else{
						fsn.initNotificationMes("添加失败", false);
					}
					
				}
			});
		}else{
			return false;
			//fsn.initNotificationMes("Failure",false);
		}
		return true;
	};
	
	buss_operation.windowUpdateConfrim = function(){
		//validation
		if(buss_operation.validator.validate()){
			//fsn.showNotificationMes("Sucess",true);
			var combobox = $("#type").data("kendoComboBox");
			var dataItem = combobox.dataItem();
			var typeTem = {
				id:dataItem.id,
				name:dataItem.name
			};
			
			var model = {
			};
			var tempModel={
					no:$("#no").val().trim(),
					name:$("#name").val().trim(),
					type:typeTem,
					note:$("#note").val().trim(),
					contacts:buss_operation.contactGrid.data("kendoGrid").dataSource.data(),
			};
			if(buss_operation.SIMPLE_TYPE == 2){
				model.customer = tempModel;
			}else if(buss_operation.SIMPLE_TYPE == 3){
				model.provider = tempModel;
			}
			console.log(model);
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/customer/" + buss_operation.SIMPLE_TYPE,
				type:"PUT",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				data:JSON.stringify(model),
				success:function(data){
					buss_operation.datasource.read();
					
				}
			});
		}else{
			console.log("No");
			//fsn.initNotificationMes("Failure",false);
		}
	};
	
	buss_operation.windowDeleteConfrim = function(){
		
		if(buss_operation.selectedData.no){
			var model = {
				customer:{
					no:buss_operation.selectedData.no
				},
				provider:{
					no:buss_operation.selectedData.no
				}
			};
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/customer/" + buss_operation.SIMPLE_TYPE,
				type:"DELETE",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				data:JSON.stringify(model),
				success:function(data){
					if(data && data.result){
						buss_operation.datasource.remove(buss_operation.grid.dataItem(buss_operation.grid.select()));
					}
				}
			});
		}
	};
	
	buss_operation.contactGrid = null;
	buss_operation.contactComboBox = null;
	buss_operation.merchandiseComboBox = null;
	buss_operation.storage1ComboBox = null;
	buss_operation.storage2ComboBox = null;
	buss_operation.comboBoxType = {};
	buss_operation.contactWin = null;
	buss_operation.selectedContact = null;
	buss_operation.batchNumComboBox = null;
	
	//kendoComboBox校验
	combobox_change=function(e){
		var judge = e.sender.selectedIndex;
		if(judge==-1){
			this.value("");
			fsn.initNotificationMes("填写内容有误！请重新填写", false);
			this.value("");
			$("#unit").val("");
		}else{
			return;
		}
	};
	
	buss_operation.OUT_STORAGE_COLUMNS = [
								               {field: "displayName", title: "商品名称", width:"auto"},
								               {field: "count", title: "数量", width:"auto"},
								               {field: "batch_number", title: "批次", width:"auto"},
								               {field: "unit_name", title: "单位", width:"auto"},
								               {field: "storage_2.name", title: buss_operation.SIMPLE_MODEL_NAME + "仓库", width:"auto"}];
	buss_operation.IN_STORAGE_COLUMNS = [
							               {field: "displayName", title: "商品名称", width:"auto"},
							               {field: "count", title: "数量", width:"auto"},
							               {field: "batch_number", title: "批次", width:"auto"},
							               {field: "unit_name", title: "单位", width:"auto"},
							               {field: "storage_1.name", title: buss_operation.SIMPLE_MODEL_NAME + "仓库", width:"auto"},
							                      ];
	buss_operation.FLITTING_STORAGE_COLUMNS = [
								               {field: "displayName", title: "商品名称", width:"auto"},
								               {field: "count", title: "数量", width:"auto"},
								               {field: "batch_number", title: "批次", width:"auto"},
								               {field: "unit_name", title: "单位", width:"auto"},
								               {field: "storage_1.name", title: "调入仓库", width:"auto"},
								               {field: "storage_2.name", title: "调出仓库", width:"auto"},
								               ];
	
	buss_operation.getOptions=function(templateId){
		var opt = {
				dataSource: new kendo.data.DataSource({
                     data:[],
                     batch : true,
                  	 page:1,
		             pageSize: 5,
		             serverPaging : true,
		             serverFiltering : true,
		             serverSorting : true
				}),
				height: 300,
				sortable: false,
				selectable:true,
				pageable: {
					refresh: false,
				   pageSizes: 5,
					messages: fsn.gridPageMessage(),
				},
				toolbar: [
				         {template: kendo.template($("#"+templateId).html())}
				]	
		};
		return opt;
	};
	
	buss_operation.initRequiredComponents = function(){
		var options = buss_operation.getOptions("BUSS_2_MERCHANDISE_DETAIL");
		var detailoptions = buss_operation.getOptions("INOUTFLITTING_DETAIL_INFO_DETAIL");
		
		if(buss_operation.SIMPLE_TYPE == 5){
			$("#no").attr({"id":"in_no"});
			//入库自动编号
			buss_operation.dropListOrderType = eWidget.getComboBoxOrderType("in_no",buss_operation.SIMPLE_TYPE,"库存模块","商品入库管理").data("kendoDropDownList");
			options.columns = buss_operation.IN_STORAGE_COLUMNS;
			detailoptions.columns = buss_operation.IN_STORAGE_COLUMNS;
		}else if(buss_operation.SIMPLE_TYPE == 6){
			$("#no").attr({"id":"out_no"});
			//出库自动编号
			buss_operation.dropListOrderType = eWidget.getComboBoxOrderType("out_no",buss_operation.SIMPLE_TYPE,"库存模块","商品出库管理").data("kendoDropDownList");
			options.columns = buss_operation.OUT_STORAGE_COLUMNS;
			detailoptions.columns = buss_operation.OUT_STORAGE_COLUMNS;
		}else if(buss_operation.SIMPLE_TYPE == 7){
			$("#no").attr({"id":"flitting_no"});
			//调拨自动编号
			buss_operation.dropListOrderType = eWidget.getComboBoxOrderType("flitting_no",buss_operation.SIMPLE_TYPE,"库存模块","调拨管理").data("kendoDropDownList");
			options.columns = buss_operation.FLITTING_STORAGE_COLUMNS;
			detailoptions.columns = buss_operation.FLITTING_STORAGE_COLUMNS;
		}
		buss_operation.contactGrid = $("#BUSS_2_MERCHANDISE_INFO_GRID").kendoGrid(options);
		buss_operation.contactGridDetail = $("#INOUTFLITTING_DETAIL_INFO_GRID").kendoGrid(detailoptions);
		
//		//获取单别信息
//		buss_operation.dropListOrderType = eWidget.getComboBoxOrderType("no",buss_operation.SIMPLE_TYPE,"库存模块","调拨管理").data("kendoDropDownList");
		
		$("#BUSS_2_MERCHANDISE_INFO_GRID .k-grid-content").attr("style","height:190px;");
		$("#INOUTFLITTING_DETAIL_INFO_GRID").attr("style","width:800px;height:300px;");
		this.initDailogAndType();
		
		this.initContactInfoButtons();
	  	
		
		//this.initRequiredAreas();
	};
	
	
	
	buss_operation.initDailogAndType = function(){
		buss_operation.contactWin = $("#BUSS_2_MERCHANDISE_INFO_WIN").kendoWindow({
			width:600,
			title:"管理业务单商品信息",
			visible:false,
			modal:true
		});
	
		buss_operation.getMerchandiseStorage=function(service,argm){
			var result=null;
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/merchandiseStorage/"+service+"?"+argm,
				type: "GET",
				dataType: "json",
				async:false,
				contentType: "application/json; charset=utf-8",
				success: function(data){
					result = data.result;
				}
			});
			return result;
		};

	/*batchNumComboBoxChange 选择事件*/
	buss_operation.batchNumComboBoxChange=function(index){
		if (index == -1) {
//			this.value("");
			buss_operation.batchNumComboBox.data("kendoComboBox").value("");
			fsn.initNotificationMes("填写内容有误！请重新填写", false);
			if ($("#storage1").val() == "") {
				var productId = buss_operation.merchandiseComboBox.dataItem().id;
				var mers = buss_operation.getMerchandiseStorage("getStorage","productId=" + productId);
				buss_operation.storage1ComboBox.data("kendoComboBox").setDataSource("");
				buss_operation.storage1ComboBox.data("kendoComboBox").setDataSource(mers);
			}
		}else if ($("#storage1").val() == "") {
				var mers = buss_operation.getMerchandiseStorage("getStorage","instanceId=" + buss_operation.batchNumComboBox.data("kendoComboBox").value());
				buss_operation.storage1ComboBox.data("kendoComboBox").setDataSource("");
				buss_operation.storage1ComboBox.data("kendoComboBox").setDataSource(mers);
		}else{
			var instanceId=buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().instanceID;
			var storNo = buss_operation.storage1ComboBox.data("kendoComboBox").dataItem().no;
			var storageNum = buss_operation.getMerchandiseStorage("getStorageNum","instanceId="+instanceId+"&&storageId="+storNo).count;
				var datas = buss_operation.contactGrid.data("kendoGrid").dataSource.data();
				for(var i = 0; i < datas.length; i++) {
					storageNum = storageNum - datas[i].count;
				}
			$("#num").val(storageNum);
		}
	};
		
		//批次ComboBox初始化
		buss_operation.batchNumComboBox = $("#batch_number").kendoComboBox({
            dataTextField: "batch_number",
            dataValueField: "instanceID",
			dataSource:[],
            filter: "contains",
            placeholder: "请选择...",
            suggest: true,
			change: function(e){
				if (buss_operation.SIMPLE_TYPE != 5) {
					buss_operation.batchNumComboBoxChange(e.sender.selectedIndex);
				}
			}
        });	
	
		buss_operation.verifyCount = eWidget.verificationNum("count");
		buss_operation.contactComboBox = eWidget.getComboBox("type",buss_operation.SIMPLE_TYPE).data("kendoComboBox");
		buss_operation.contactComboBox.bind("change", combobox_change);
		buss_operation.contactComboBox.setOptions({
			change: function(e) {
				buss_operation.comboBoxType = {
						id:this.value(),
						name:this.text()
				};
			  }
		});
		
		//显示类型
		if(buss_operation.SIMPLE_TYPE ==5||buss_operation.SIMPLE_TYPE ==6 ){
			$("#showType").show();
			$("#showDetailType").show();
			$("#label_storage1").html("仓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;库<font color=\"red\">*</font>");
		}
		if (buss_operation.SIMPLE_TYPE == 5) {
			buss_operation.merchandiseComboBox = eWidget.getCommonComboBox("merchandise", "/initializeProduct/listall", "product.name", "id").data("kendoComboBox");
			buss_operation.storage1ComboBox = eWidget.getCommonComboBox("storage1","/storage/lists","name", "no").data("kendoComboBox");
			buss_operation.storage1ComboBox.bind("change", combobox_change);
		} else {
			$("#safeNum").show();
			buss_operation.merchandiseComboBox = eWidget.getCommonComboBox("merchandise", "/initializeProduct/outlistall", "product.name", "id").data("kendoComboBox");
			//仓库ComboBox初始化
			buss_operation.storage1ComboBox = $("#storage1").kendoComboBox({
				dataTextField: "name",
	            dataValueField: "no",
				dataSource:[],
	            filter: "contains",
	            placeholder: "请选择...",
	            suggest: true,
				change: function(e){
					var judge = e.sender.selectedIndex;
					if (judge == -1) {
						this.value("");
						fsn.initNotificationMes("填写内容有误！请重新填写", false);
						if ($("#batch_number").val() == "") {
							var mers = buss_operation.getMerchandiseStorage("getBatchNum","productId=" + buss_operation.merchandiseComboBox.dataItem().id);
							buss_operation.batchNumComboBox.data("kendoComboBox").setDataSource("");
							buss_operation.batchNumComboBox.data("kendoComboBox").setDataSource(mers);
						};
					} else if($("#batch_number").val()==""){
						var mers = buss_operation.getMerchandiseStorage("getBatchNum","productId=" + buss_operation.merchandiseComboBox.dataItem().id+"&&storageId="+this.value());
						buss_operation.batchNumComboBox.data("kendoComboBox").setDataSource("");
						buss_operation.batchNumComboBox.data("kendoComboBox").setDataSource(mers);
					}else{
						var mers = buss_operation.getMerchandiseStorage("getStorageNum","instanceId="+buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().instanceID+"&&storageId="+this.value());
						var storageNum = mers.count;
       					var datas = buss_operation.contactGrid.data("kendoGrid").dataSource.data();
       					var merchandiseNo = $("#merchandise").data("kendoComboBox").dataItem().product.name;
       					var batchNumber = $("#batch_number").data("kendoComboBox").dataItem().batch_number;
       					var storageNo = $("#storage1").data("kendoComboBox").dataItem().no;
       					for(var i = 0; i < datas.length; i++) {
       						if(datas[i].displayName == merchandiseNo && datas[i].batch_number == batchNumber && datas[i].storage_2.no == storageNo) {
       							storageNum = storageNum - datas[i].count;
       						}
       					}
						$("#num").val(storageNum);
					}
				}
			});
		}
		
		buss_operation.merchandiseComboBox.bind("change", combobox_change);
		buss_operation.merchandiseComboBox.setOptions({
			change: function(e) {
				$("#unit").val(buss_operation.merchandiseComboBox.dataItem().product.unit.name);	
				$("#format").val(buss_operation.merchandiseComboBox.dataItem().product.format);	
				$("#expiration").val(buss_operation.merchandiseComboBox.dataItem().product.expiration);
				var dataSource = buss_operation.getMerchandiseStorage("getBatchNum","productId="+buss_operation.merchandiseComboBox.dataItem().id);
				buss_operation.batchNumComboBox.data("kendoComboBox").setDataSource("");
				buss_operation.batchNumComboBox.data("kendoComboBox").setDataSource(dataSource);
				buss_operation.batchNumComboBox.data("kendoComboBox").value("");
				$("#num").val("");
				if (buss_operation.SIMPLE_TYPE == 5) {
					$("#storage1").data("kendoComboBox").value(buss_operation.merchandiseComboBox.dataItem().firstStorage.no);
				}else{
					var dataSource = buss_operation.getMerchandiseStorage("getStorage","productId=" + buss_operation.merchandiseComboBox.dataItem().id);
					buss_operation.storage1ComboBox.data("kendoComboBox").setDataSource("");
					buss_operation.storage1ComboBox.data("kendoComboBox").setDataSource(dataSource);
					buss_operation.storage1ComboBox.data("kendoComboBox").value("");
				}
			}
		});
		
		if(buss_operation.SIMPLE_TYPE == 7){
			$("#type").removeAttr("required");
			$("#storageDIV").show();
			buss_operation.storage2ComboBox = eWidget.getCommonComboBox("storage2","/storage/lists","name", "no").data("kendoComboBox");
			buss_operation.storage2ComboBox.bind("change", combobox_change);
		}
	};
	
	buss_operation.initContactInfoButtons = function(){
		$("#add_").bind("click",function(){
			buss_operation.addContact();
		});
		$("#update_").bind("click",function(){
			buss_operation.updateContact();
		});
		$("#delete_").bind("click",function(){
			if(buss_operation.contactGrid.data("kendoGrid").select().length <= 0) {
				fsn.initNotificationMes("请选择一条记录!", false);
			} else {
				var win = eWidget.getPromptWindow();
				win.data("kendoWindow").open().center();
				$("#confirmWindow").unbind("click");
				$("#cancelWindow").unbind("click");
				$("#confirmWindow").bind("click",function(){
					buss_operation.deleteContact();
					win.data("kendoWindow").close();
				});
				$("#cancelWindow").bind("click",function(){
					win.data("kendoWindow").close();
				});
			}
		});
	};
	
	buss_operation.cleanContactForm = function(){
		buss_operation.verifyCount.data("kendoNumericTextBox").value("");
		buss_operation.batchNumComboBox.data("kendoComboBox").value("");
		$("#unit").val("");
		$("#format").val("");
		$("#expiration").val("");
		$("#num").val("");
		buss_operation.merchandiseComboBox.value("");
		if(buss_operation.SIMPLE_TYPE == 5){
			buss_operation.storage1ComboBox.value("");
		} else if (buss_operation.SIMPLE_TYPE == 6){
			buss_operation.storage1ComboBox.data("kendoComboBox").value("");
		} else if (buss_operation.SIMPLE_TYPE == 7){
			buss_operation.storage2ComboBox.value("");
			buss_operation.storage1ComboBox.data("kendoComboBox").value("");
		}else{
				buss_operation.storage2ComboBox.value("");
				buss_operation.storage1ComboBox.data("kendoComboBox").value("");
		}
	};
	
	buss_operation.bindContactForm = function(e){	
		var dataItem = new Array();
		for(var i=0;i<=productCount;i++){
			if(productList[i][0]==e.displayName 
				&& productList[i][4]==e.batch_number
				&& productList[i][7]==e.count
			){
				dataItem=productList[i];	
				updataCount=i;
				break;
			}
		}
		
		$("#format").val(dataItem[2]);
		$("#expiration").val(dataItem[3]);
		buss_operation.merchandiseComboBox.value(e.displayName);
		buss_operation.verifyCount.data("kendoNumericTextBox").value(e.count);
//		$("#batch_number").val(dataItem[4]);
		buss_operation.batchNumComboBox.data("kendoComboBox").value(dataItem[4]);
		$("#unit").val(e.unit_name);
		if(buss_operation.SIMPLE_TYPE == 5){
			buss_operation.storage1ComboBox.value(e.storage_1.no);
		} else if (buss_operation.SIMPLE_TYPE == 6){
			$("#num").val(dataItem[6]);
			buss_operation.storage1ComboBox.data("kendoComboBox").value(e.storage_2.no);
		} else if (buss_operation.SIMPLE_TYPE == 7){
			$("#num").val(dataItem[6]);
			buss_operation.storage2ComboBox.value(e.storage_1.no);
			buss_operation.storage1ComboBox.data("kendoComboBox").value(e.storage_2.no);
		}else{
				buss_operation.storage2ComboBox.value("");
				buss_operation.storage1ComboBox.value("");
		}
				$("#format").val(buss_operation.merchandiseComboBox.dataItem().product.format);	
				$("#expiration").val(buss_operation.merchandiseComboBox.dataItem().product.expiration);
	};
	
	buss_operation.addContact = function(){
		var productNum = buss_operation.merchandiseComboBox.dataSource._data.length;
		if(productNum==0){
			if (buss_operation.SIMPLE_TYPE == 5) {
				fsn.initNotificationMes("目前没有可操作的商品，请到商品信息初始化商品后进行操作！", false);
				return;
			}else{
				fsn.initNotificationMes("目前库存没有可操作的商品，请先将商品入库后进行操作！", false);
				return;
			}
		}
		
		buss_operation.cleanContactForm();
		$("#BUSS_2_MERCHANDISE_INFO_WIN").data("kendoWindow").title("添加商品信息");
		buss_operation.contactWin.data("kendoWindow").open().center();
		
		$("#confirm_").unbind("click");
		$("#cancel_").unbind("click");
		
		$("#confirm_").bind("click",function(){
				if (buss_operation.productVerify()) {			
					var success = buss_operation.saveConfirm(true);
					if (success == true) {
						fsn.initNotificationMes("新增成功！", true);
						buss_operation.contactWin.data("kendoWindow").close();
					}
					else {
						fsn.initNotificationMes("新增失败！", false);
					}
				}else{
					return;
				}
										
		});
		
		$("#cancel_").bind("click",function(){
			buss_operation.contactWin.data("kendoWindow").close();
		});
	};
	
	buss_operation.updateContact = function(){
		if(buss_operation.contactGrid.data("kendoGrid").select().length > 0 ){
			
			var contactItem = buss_operation.selectedContact = buss_operation.contactGrid.data("kendoGrid").dataItem(buss_operation.contactGrid.data("kendoGrid").select());
			
			buss_operation.bindContactForm(contactItem);
			$("#BUSS_2_MERCHANDISE_INFO_WIN").data("kendoWindow").title("编辑商品信息");
			buss_operation.contactWin.data("kendoWindow").open().center();
			
			$("#confirm_").unbind("click");
			$("#cancel_").unbind("click");
			
			$("#confirm_").bind("click",function(){			
				if (!buss_operation.productVerify()||buss_operation.updateVerify(contactItem)) {	
					return;					
				} else {
					var success = buss_operation.saveConfirm(false);
					if (success == true) {
						fsn.initNotificationMes("更新成功！", true);
						buss_operation.contactWin.data("kendoWindow").close();
					} else {
						fsn.initNotificationMes("更新失败！", false);
					}		
				}
			});
			
			$("#cancel_").bind("click",function(){
				buss_operation.contactWin.data("kendoWindow").close();
			});
		} else {
			fsn.initNotificationMes("请选择一条记录！",false);
		}
	};
	
	buss_operation.deleteContact = function(){
		if(buss_operation.contactGrid.data("kendoGrid").select().length > 0 ){
			var contactItem = buss_operation.contactGrid.data("kendoGrid").dataItem(buss_operation.contactGrid.data("kendoGrid").select());
			buss_operation.contactGrid.data("kendoGrid").dataSource.remove(contactItem);
			fsn.initNotificationMes("删除成功！", true);
			
		}
	};
	
	buss_operation.saveConfirm = function(isNew){
		var contact_validator = $("#BUSS_2_MERCHANDISE_INFO_WIN").kendoValidator().data("kendoValidator");
		if(contact_validator.validate()){
			if(parseInt($("#count").val()) > 0){
				var model = {};
				var isBatchSelect = buss_operation.batchNumComboBox.data("kendoComboBox").select();
				if(isNew){
					//将数据添加到数组
					productList[productCount]=new Array();
					productList[productCount][0]=buss_operation.merchandiseComboBox.dataItem().product.name;
					productList[productCount][1]=$("#unit").val();
					productList[productCount][2]=$("#format").val();
					productList[productCount][3]=$("#expiration").val();
					productList[productCount][7]=$("#count").val();	
					model = {
						displayName:buss_operation.merchandiseComboBox.dataItem().product.name,
						count:parseInt($("#count").val()),
						merchandiseNo:buss_operation.merchandiseComboBox.dataItem().id,
						unit_name:$("#unit").val(),
					};
					if(buss_operation.SIMPLE_TYPE == 5){
						//var a =buss_operation.storage1ComboBox;
						model.storage_1 = buss_operation.storage1ComboBox.dataItem();
						productList[productCount][5]=buss_operation.storage1ComboBox.dataItem();				
						if(isBatchSelect==-1){
							productList[productCount][4]=buss_operation.batchNumComboBox.data("kendoComboBox").value();
							model.batch_number=buss_operation.batchNumComboBox.data("kendoComboBox").value();
						}else{
							productList[productCount][4]=buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().batch_number;
							model.batch_number=buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().batch_number;
						}
					}else if(buss_operation.SIMPLE_TYPE == 6){
						model.storage_2 = buss_operation.storage1ComboBox.data("kendoComboBox").dataItem();
						model.batch_number = buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().batch_number;
						productList[productCount][4]=buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().batch_number;
						productList[productCount][5]=buss_operation.storage1ComboBox.data("kendoComboBox").dataItem();
						productList[productCount][6]=$("#num").val();
					}else if(buss_operation.SIMPLE_TYPE == 7){
						model.storage_1 = buss_operation.storage2ComboBox.dataItem();
						model.storage_2 = buss_operation.storage1ComboBox.data("kendoComboBox").dataItem();
						model.batch_number = buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().batch_number;
						productList[productCount][4]=buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().batch_number;
						productList[productCount][5]=buss_operation.storage1ComboBox.data("kendoComboBox").dataItem();
						productList[productCount][6]=$("#num").val();
						productList[productCount][8]=buss_operation.storage2ComboBox.dataItem();
					}
					productCount=+1;
				}else{
					if(buss_operation.selectedContact.id){
						model.id = buss_operation.selectedContact.id;
					}
				}
				if(isNew){
					buss_operation.contactGrid.data("kendoGrid").dataSource.add(model);
				}else{
					buss_operation.contactGrid.data("kendoGrid").dataSource.remove(buss_operation.selectedContact);
					var contact = buss_operation.selectedContact;
					contact.displayName = buss_operation.merchandiseComboBox.dataItem().product.name;
					contact.count = parseInt($("#count").val());
					contact.merchandiseNo = buss_operation.merchandiseComboBox.dataItem().id;
					contact.unit_name = $("#unit").val();
					
					var dataItem = new Array();
					dataItem[0]=buss_operation.merchandiseComboBox.dataItem().product.name;
					dataItem[1]=$("#unit").val();
					dataItem[2]=$("#format").val();
					dataItem[3]=$("#expiration").val();
					dataItem[7]=	$("#count").val();
					
					if(buss_operation.SIMPLE_TYPE == 5){
						contact.storage_1 = buss_operation.storage1ComboBox.dataItem();
						dataItem[5]=buss_operation.storage1ComboBox.dataItem();
						if(isBatchSelect==-1){
							contact.batch_number=buss_operation.batchNumComboBox.data("kendoComboBox").value();
							dataItem[4]=buss_operation.batchNumComboBox.data("kendoComboBox").value();	
						}else{
							contact.batch_number=buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().batch_number;
							dataItem[4]=buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().batch_number;
						}
					}else if(buss_operation.SIMPLE_TYPE == 6){
						contact.storage_2 = buss_operation.storage1ComboBox.data("kendoComboBox").dataItem();
						contact.batch_number =buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().batch_number;
						dataItem[4]=buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().batch_number;
						dataItem[5]=buss_operation.storage1ComboBox.data("kendoComboBox").dataItem();
						dataItem[6]=$("#num").val();
					}else if(buss_operation.SIMPLE_TYPE == 7){
						contact.storage_1 = buss_operation.storage2ComboBox.dataItem();
						contact.storage_2 = buss_operation.storage1ComboBox.data("kendoComboBox").dataItem();
						contact.batch_number =buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().batch_number;
						dataItem[4]=buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().batch_number;
						dataItem[5]=buss_operation.storage1ComboBox.data("kendoComboBox").dataItem();
						dataItem[6]=$("#num").val();
						dataItem[8]=buss_operation.storage2ComboBox.dataItem();
					}
					productList[updataCount]=dataItem;
					buss_operation.contactGrid.data("kendoGrid").dataSource.add(contact);
				}
			}else{
				buss_operation.verifyCount.data("kendoNumericTextBox").value("");
				return false;
			}
		}else{
			return false;
		}
		return true;
	};
	
	
	buss_operation.detailBaseInfo = function(Item,data){
		$("#label_detail_no_content").html(Item.no);
		$("#label_detail_type_content").html(Item.type.name);
		$("#label_detail_note_content").html(Item.note);
		$("#label_detail_op_content").html(Item.createUserName);
		$("#label_detail_ctime_content").html((new Date(Item.createTime)).format("YYYY-MM-dd hh:mm:ss"));
		buss_operation.contactGridDetail.data("kendoGrid").setDataSource(data);
		//buss_operation.contactGridDetail.data("kendoGrid").dataSource.data(data.result.listOfModel);
	};
	
	
	
	buss_operation.GRIDID = "Simple_Grid";
	buss_operation.hight=500;
	buss_operation.DAILOGID = "OPERATION_WIN"; 
	
			
	initStandard = function(keywords){
		var ds = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
                url : function(options){
					return fsn.getHttpPrefix() + "/erp/buss/" + buss_operation.SIMPLE_TYPE + "/search?keywords="+keywords;
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
                 	no:{type:"string"},
                 	name:{type:"string"},
                 	type:{
                 		id:{type:"number"},
                     	name:{type:"string"}
                 	},
                 	note:{type:"string"}
                 }
            },
        	data : function(data) {
        		if(data && data.result && data.result.listOfModel){
        			data.result.listOfModel.forEach(function(item){
        				if(item.type == undefined){
            				item.type = {name:"空"};
        				}
        			});
        			return data.result.listOfModel;  
        		}
                return null;
            },
            total : function(data) {//总条数
            	if(data && data.result && data.result.count){
            		return  data.result.count;
            	}
            	return 0;
            }  
        },
        batch : true,
        page:1,
        pageSize : 10, //每页显示个数
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	    });
		return ds;
	};			
		//商品信息验证
		buss_operation.productVerify = function(){
				var batch = null;
				var count = $("#count").data("kendoNumericTextBox").value();
				var product = buss_operation.merchandiseComboBox.dataItem();
				if(product == undefined){
					fsn.initNotificationMes("请先填写商品信息", false);
					return false;
				}else{
					var productId = product.id;
				}
				
				if (buss_operation.SIMPLE_TYPE == 5) {
					batch = $("#batch_number").val().trim();
				}
				else if(buss_operation.batchNumComboBox.data("kendoComboBox").dataItem() == undefined){
						fsn.initNotificationMes("请选择批次", false);
					}else{
						batch = buss_operation.batchNumComboBox.data("kendoComboBox").dataItem().batch_number;
					}
				var storage = $("#storage1").val(); 
		    	if (productId == "") {
					fsn.initNotificationMes("请先选择商品", false);
					return false;
				} else if (batch == "") {
						fsn.initNotificationMes("请先填写批次", false);
						return false;
					 } else if (storage == "") {
							fsn.initNotificationMes("请先填写仓库", false);
							return false;
						} else if (count == null || count == 0) {
								fsn.initNotificationMes("请填写数量！", false);
								return false;
							 } else {
								if (buss_operation.SIMPLE_TYPE == 7) {
									var Istorage = buss_operation.storage2ComboBox.value();
									if (Istorage == "") {
										fsn.initNotificationMes("请填写调入仓库", false);
										return false;
									} else if (Istorage == storage) {
											fsn.initNotificationMes("调入仓库和调出仓库不可相同！", false);
											return false;
										}
									}
								
				if (buss_operation.SIMPLE_TYPE == 6 || buss_operation.SIMPLE_TYPE == 7) {
					$.ajax({
						url: fsn.getHttpPrefix() + "/erp/buss/" + buss_operation.SIMPLE_TYPE + "/JudgeStorage?value=" + count + "&productId=" + productId + "&batch=" + batch + "&storage=" + storage,
						type: "GET",
						dataType: "json",
						contentType: "application/json; charset=utf-8",
						async: false,
						success: function(data){
							/*var a = data.flag;
							var saveFlag = data.flag;*/
						}
					});
					if (parseInt($("#count").val()) > parseInt($("#num").val())) {
						fsn.initNotificationMes("库存不足，无法支出！", false);
						return false;
					}
				}
				return true;
			}
					
		};	
		
		buss_operation.updateVerify = function(contactItem){
				var contacts =	contactItem;	
				var merchandise = $("#merchandise").val();
				/*变量c没有用到*/
				//var unit = $("#unit").val(); 
				var batchNo =$("#batch_number").val();
				var count = $("#count").val();
				var storage1 = $("#storage1").val();
				var storage2 = $("#storage2").val();
				
				if(merchandise!=contacts.merchandiseNo){
					return false;
				}else if(batchNo!=contacts.batch_number){
					return false;
				}else if(count!=contacts.count){
					return false;
				}else{
					if(buss_operation.SIMPLE_TYPE == 5){
						if(storage1!=contacts.storage_1.no){
							return false;
						}else{
							fsn.initNotificationMes("没有对数据进行任何修改！", false);
							return true;
						}
					}else if(buss_operation.SIMPLE_TYPE == 6){
						if(storage1!=contacts.storage_2.no){
							return false;
						}else{
							fsn.initNotificationMes("没有对数据进行任何修改！", false);
								return true;
						}
					}else if(buss_operation.SIMPLE_TYPE == 7){
						if(storage1!=contacts.storage_2.no){
							return false;
						}else if(storage2!=contacts.storage_1.no){
							return false;
						}else{
							fsn.initNotificationMes("没有对数据进行任何修改！", false);
							return true;
						}
					}
				}
			
		};
		
		buss_operation.initailize();
});
