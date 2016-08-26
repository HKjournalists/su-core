$(function(){
	var fsn = window.fsn = window.fsn || {};
	var eWidget = fsn.eWidget = fsn.eWidget||{};
	var common = fsn.common = fsn.common || {};
	
	var receivingnote = fsn.receivingnote = fsn.receivingnote || {};
	
	$.extend(receivingnote,common);
	
	receivingnote.datasource = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
				url : function(options){
                	if(options.filter){
                		var configure = filter.configure(options.filter);
                 		return fsn.getHttpPrefix() + "/erp/customer/" + receivingnote.SIMPLE_TYPE + "/reglist/" + options.page + "/" + options.pageSize + "/"+configure;;
                	};
                	return fsn.getHttpPrefix() + "/erp/customer/" + receivingnote.SIMPLE_TYPE + "/list";
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
        			re_num:{type:"string"},
        			re_checkman:{type:"string"},
        			re_date:{type:"Date(yyyy-MM-dd)"},
                 	note:{type:"string"}
                 }
            },
        	data : function(data) {
        		if(data && data.result && data.result.listOfModel){
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
	
	/*根据收货单号获取收货单商品信息*/
	receivingnote.getGoodsInfoByReceiveNo=function(receiveNo){
		return new kendo.data.DataSource({
			transport: {
	            read: {
	            	type:"GET",
	            	url : function(options){
						return fsn.getHttpPrefix() + "/erp/customer/0/getGoodsByReceivNo/"+receiveNo;
					},
					async:false,
	            }
	        },
	        batch : true,
	        page:1,
	        pageSize: 5,
	        schema: {
	            data : function(returnValue) {
	            	var listOfGoods = returnValue.data.listOfGoods;
	            	for(var i=0;i<listOfGoods.length;i++){
	            		listOfGoods[i].po_totalmoney = listOfGoods[i].po_price*listOfGoods[i].po_receivenum;
	    			}
	                return listOfGoods;
	            },
	            total : function(returnValue) {
	                return returnValue.data.counts;
	            }     
	        },
	        serverPaging : true,
	        serverFiltering : true,
		});
	};
	
	receivingnote.columns = [
	               {field: "re_num",title: "收货单号", width:"18%",editor:false},
	               {field: "re_checkman",title: "检收员", width:"auto"},
	               {field: "re_provide_num.name",title:"供货商", width:"auto"},
	               {field: "re_date",title: "收货日期", width:"auto", filterable: false},
	               {field: "re_purchase_check",title:"状态", width:"auto", filterable: false},
	               {field: "re_remarks",title:"备注",width:"auto"},
				   {field: "userName",title:"操作人",width:"auto"},
	               {command:[{name:"预览",click:function(e){
	            	   var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
	            	   var addWin = eWidget.getWindow("RECEIVING_DETAIL_INFO","详细信息",common.SIMPLE_MODEL_NAME, null);
	            	   var goodsInfoDs = receivingnote.getGoodsInfoByReceiveNo(dataItem.re_num);
	            	   dataItem.contacts = goodsInfoDs;
	            	   addWin.data("kendoWindow").open().center();
	            	   receivingnote.detailBaseInfo(dataItem);
	               }}]}
	               ];
	
	//按钮的显示与影藏
	receivingnote.updateOrCheck=function(operation){
		console.log(operation);
		if(operation=='update'){
			$("#add_,#update_,#delete_,#confirm,#cancel").show();
		}
	};
	
	receivingnote.contactGrid = null;
	receivingnote.dropDownListOutGood = null;
	receivingnote.contactComboBox = null;
	receivingnote.contactComboBoxProduct = null;
	receivingnote.dropListOrderType = null;
	receivingnote.comboBoxUnit = {};
	receivingnote.comboBoxProduct = {};
	receivingnote.comboBoxprovider = {};
	receivingnote.ComboBoxContact = {};
	receivingnote.comboBoxOType = {};
	receivingnote.ListOutGood = {};
	receivingnote.contactWin = null;
	receivingnote.selectedContact = null;
	receivingnote.contactComboBoxProvider=null;
	receivingnote.dropDownListStorage = null;
	receivingnote.dropDownListProviderContacts = null;
	
	receivingnote.initRequiredComponents = function(){
		receivingnote.contactGrid = $("#CONTACT_INFO_GRID").kendoGrid({
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
	                pageSizes: false,
					messages: fsn.gridPageMessage(),
	            },
	            toolbar: [{template: kendo.template($("#CONTACT_DETAIL").html())}],
	            columns: [
	                      {field: "product.name", title: "产品名称", width:"auto"},
	                      {field: "po_receivenum", title: "收货数量", width:"auto"},
	                      {field: "po_batch", title: "批次", width:"auto"},         
	                      {field: "po_price", title: "单价", width:"auto"},
	                      {field: "label_totalp", title: "单笔总金额", width:"auto"},
	                      ]
	        });
		
		receivingnote.productGrid = $("#RECEIVING_INFO_GRID").kendoGrid({
            dataSource: new kendo.data.DataSource({
            	data:[],
                batch : true,
                page:1,
                pageSize: false,
                serverPaging : true,
                serverFiltering : true,
                serverSorting : true
           }),
            width:450,
            height: 300,
            scrollable: true,
            sortable: false,
            selectable:true,
            pageable: {
            	refresh: false,
                pageSizes: 5,
				messages: fsn.gridPageMessage(),
            },
            toolbar: [
			          {template: kendo.template($("#PRODUCT_DETAIL").html())}
			         ],
            columns: [
                      {field: "product.name", title: "产品名称", width:"100px"},
                      {field: "product.format", title: "规格", width:"100px"},
                      {field: "po_ismode", title: "样品否", width:"100px",template:function(dataItem){
				  		if(dataItem.po_ismode){
							return "是";
						}else{
							return "否";
						}
				  }},
                      {field: "hasreport", title: "是否有质检报告", width:"150px",template:function(dataItem){
				  		if(dataItem.hasreport){
							return "是";
						}else{
							return "否";
						}
				  }},
                      {field: "po_isneedqr", title: "是否需要质检报告", width:"150px",template:function(dataItem){
				  		if(dataItem.po_isneedqr){
							return "是";
						}else{
							return "否";
						}
				  }},
                      {field: "po_receivenum", title: "收货数量", width:"100px"},
                      {field: "po_unit.name", title: "单位", width:"100px"},
                      {field: "po_mtype", title: "币种", width:"100px"},
                      {field: "po_batch", title: "批次", width:"100px"},
                      {field: "po_price", title: "单价", width:"100px"},
                      {field: "po_storage_address", title: "仓库地址", width:"100px"},
                      {field: "po_totalmoney", title: "单笔总金额", width:"120px"},
                      {field: "po_remark", title: "备注", width:"100px"},
                      ]
        });
		
		$("#CONTACT_INFO_GRID .k-grid-content").attr("style","height:190px;");
		$("#RECEIVING_INFO_GRID").attr("style","width:700px;height:300px;");
		
		receivingnote.contactWin = $("#CONTACT_INFO_WIN").kendoWindow({
					width:800,
					title:"管理商品收货单",
					visible:false,
					modal:true
				});

		receivingnote.dropDownListOutGood = $("#receive_no").kendoDropDownList({optionLabel:"请选择来源单号"}).data("kendoDropDownList");
		$("#cid").kendoDropDownList({optionLabel:"请选择产品"});
		receivingnote.verifyPpay = eWidget.verificationNum("ppay").data("kendoNumericTextBox");
		receivingnote.verifyFpay = eWidget.verificationNum("fpay").data("kendoNumericTextBox");
		receivingnote.verifyCnum = eWidget.verificationNum("cnum").data("kendoNumericTextBox");
		receivingnote.verifyCmoney = eWidget.verificationNum("cmoney").data("kendoNumericTextBox");
		receivingnote.verifyCcnum = eWidget.verificationNum("ccnum").data("kendoNumericTextBox");
		receivingnote.dropDownListProviderContacts = $("#contactman").kendoDropDownList({optionLabel: "请选择联系人"}).data("kendoDropDownList");
		//获取供货商信息
		receivingnote.contactComboBoxProvider = eWidget.getDropDownListProvider("provider",receivingnote.SIMPLE_TYPE).data("kendoDropDownList");
		receivingnote.contactComboBoxProvider.setOptions({
			change: function(e) {
				receivingnote.dropDownListProviderContacts.value("");
				$("#contacttel").val("");
				$("#caddress").val("");
				receivingnote.comboBoxProvider = {
						no:this.value(),
						name:this.text()
				};
				//出货单号
				if(receivingnote.comboBoxProvider.no !=""){
					receivingnote.dropDownListOutGood = eWidget.getDropDownListOutGood("receive_no",receivingnote.SIMPLE_TYPE,receivingnote.comboBoxProvider.no).data("kendoDropDownList");
					receivingnote.dropDownListOutGood.bind("change", function(){
						receivingnote.contactComboBoxProduct = eWidget.getComboBoxProduct("cid",receivingnote.SIMPLE_TYPE,receivingnote.dropDownListOutGood.dataItem().name).data("kendoDropDownList");
					});
					receivingnote.dropDownListProviderContacts = eWidget.getDropDownListProviderContacts("contactman",receivingnote.SIMPLE_TYPE,receivingnote.comboBoxProvider.no).data("kendoDropDownList");
				}else{
					receivingnote.dropDownListProviderContacts.value("");
					receivingnote.dropDownListOutGood.value("");
					$("#contacttel").val("");
					$("#caddress").val("");
				}
			  }
		});
		//获取单别信息
		receivingnote.dropListOrderType = eWidget.getComboBoxOrderType("no",receivingnote.SIMPLE_TYPE,"采购模块","收货单管理").data("kendoDropDownList");
		//获取仓库信息
		receivingnote.dropDownListStorage = eWidget.getDropDownListStorage("saddress",receivingnote.SIMPLE_TYPE).data("kendoDropDownList");
		//建单时间插件
		$("#ctime").kendoDatePicker({culture:"zh-CN",value:new Date(),format:"yyyy-MM-dd"});
		//预计收货时间插件
		$("#p_date").kendoDatePicker({culture:"zh-CN",value:new Date(),format:"yyyy-MM-dd"});
		
		this.initContactInfoButtons();
	};
	
receivingnote.win_width = 800;
	
	receivingnote.selectedData = {};

	//收货单基础信息修改
	receivingnote.bindForm = function(){
		/**显示更新的3个按钮，隐藏check按钮*/
		fsn.receivingnote.dropListOrderType.text(receivingnote.selectedData.re_num);		
		fsn.receivingnote.contactComboBoxProvider.value(receivingnote.selectedData.re_provide_num.id);
		var datasource = new kendo.data.DataSource({
			data:[receivingnote.selectedData.re_outofbill_num]
		});
		fsn.receivingnote.dropDownListOutGood.setDataSource(datasource);
		fsn.receivingnote.dropDownListOutGood.text(receivingnote.selectedData.re_outofbill_num);
		receivingnote.contactComboBoxProduct = eWidget.getComboBoxProduct("cid",receivingnote.SIMPLE_TYPE,receivingnote.selectedData.re_outofbill_num).data("kendoDropDownList");
		
		$("#source").val(receivingnote.selectedData.re_source);
		receivingnote.dropDownListProviderContacts = eWidget.getDropDownListProviderContacts("contactman",receivingnote.SIMPLE_TYPE,receivingnote.selectedData.re_provide_num.id).data("kendoDropDownList");
		receivingnote.dropDownListProviderContacts.value(receivingnote.selectedData.re_contact_id.id);
		receivingnote.dropDownListProviderContacts.text(receivingnote.selectedData.re_contact_id.name);
		/**为联系人电话，联系人地址赋值*/
		$("#contacttel").val(receivingnote.selectedData.re_contact_id.tel_1);
		$("#caddress").val(receivingnote.selectedData.re_contact_id.province+receivingnote.selectedData.re_contact_id.city+receivingnote.selectedData.re_contact_id.area+receivingnote.selectedData.re_contact_id.addr);
		/**构造单笔总金额*/
		var total = 0;
		for(var i = 0;i<receivingnote.selectedData.contacts.length;i++){
			var singleData = receivingnote.selectedData.contacts[i].label_totalp=receivingnote.selectedData.contacts[i].po_receivenum*receivingnote.selectedData.contacts[i].po_price;
			total = total + singleData;
		}
		$("#totalp").attr("disabled","disabled");
		$("[data-bind='value:totalp']").val(total);
		$("[data-bind='value:pricec']").val(receivingnote.selectedData.re_price_condition);
		receivingnote.verifyPpay.value(receivingnote.selectedData.re_before_pay);
		receivingnote.verifyFpay.value(receivingnote.selectedData.re_fact_pay);
		$("[data-bind='value:p_date']").val(receivingnote.selectedData.re_pdate);
		$("[data-bind='value:ctime']").val(receivingnote.selectedData.re_date);
		$("[data-bind='value:checkman']").val(receivingnote.selectedData.re_checkman);
		$("[data-bind='value:base_remarks']").val(receivingnote.selectedData.re_remarks);
		
		receivingnote.contactGrid.data("kendoGrid").dataSource.data(receivingnote.selectedData.contacts);
		
    	$("#no").kendoValidator().data("kendoValidator").hideMessages();
		$("#provider").kendoValidator().data("kendoValidator").validate();
		$("#contactman").kendoValidator().data("kendoValidator").hideMessages();
		$("#checkman").kendoValidator().data("kendoValidator").validate();
		$("#ppay").kendoValidator().data("kendoValidator").validate();
		$("#fpay").kendoValidator().data("kendoValidator").validate();
		fsn.receivingnote.dropListOrderType.readonly(true);
		fsn.receivingnote.dropDownListOutGood.readonly(true);
	};
		
	receivingnote.clearForm = function(){
		$("#cid").data("kendoDropDownList").readonly(false);
		$("#no").kendoValidator().data("kendoValidator").hideMessages();
		$("#provider").kendoValidator().data("kendoValidator").hideMessages();
		$("#contactman").kendoValidator().data("kendoValidator").hideMessages();
		$("#checkman").kendoValidator().data("kendoValidator").hideMessages();
		$("#ppay").kendoValidator().data("kendoValidator").hideMessages();
		$("#fpay").kendoValidator().data("kendoValidator").hideMessages();
		fsn.receivingnote.dropListOrderType.text("请选择单别");
		fsn.receivingnote.dropListOrderType.readonly(false);
		receivingnote.contactComboBoxProvider.value("");
		fsn.receivingnote.contactComboBoxProvider.readonly(false);
		receivingnote.dropDownListOutGood.value("");
		receivingnote.dropDownListProviderContacts.value("");
		$("#caddress").val("");
		$("#contacttel").val("");
		receivingnote.verifyPpay.value("");
		receivingnote.verifyFpay.value("");
		$("#checkman").val("");
		$("#checkman").removeAttr("readonly");
		$("#base_remarks").val("");
		$("#base_remarks").removeAttr("readonly");
		$("#totalp").val("");
		receivingnote.dropListOrderType.value("");
		receivingnote.contactGrid.data("kendoGrid").dataSource.data([]);
	};
	
	//用于添加确定保存
	receivingnote.windowSaveConfrim = function(){
		var flag = receivingnote.verifyBaseInfo();
		//validation
		if(flag){
		if(receivingnote.validator.validate()){
			//provider
			receivingnote.checkFlag = false;
			var combobox = $("#provider").data("kendoDropDownList");
			var dataItem = combobox.dataItem();
			var providerTem = {
				id:dataItem.id,
			};
			//contact
			var proContacts = $("#contactman").data("kendoDropDownList");
			var proItem = proContacts.dataItem();
			var contactTem = {
				id:proItem.id,
			};
			//单别号
			var combobox2 = $("#no").data("kendoDropDownList");
			var dataItem2 = combobox2.dataItem();
			var orderTypeTem = {
				ot_type:dataItem2.ot_type,
			};
			//出货单号
			var combobox3 = $("#receive_no").data("kendoDropDownList");
			var dataItem3 = combobox3.dataItem();
			
			var model = {
			};
			/********************************/
			var _contacts=receivingnote.contactGrid.data("kendoGrid").dataSource.data();
			var contacts=[];
			var totalMoney = 0;
			for(var i=0;i<_contacts.length;i++){
				totalMoney = totalMoney + _contacts[i].label_totalp;
				delete _contacts[i].label_totalp;
				contacts[i] = _contacts[i];
			}
			/********************************/
			console.log(contacts);
			if(contacts.length==0){
				fsn.initNotificationMes("没有添加商品！</br>新增失败", false);
				return;
			}
			model.receivingNote = {
					
					re_num:orderTypeTem.ot_type,
					re_source:$("#source").val().trim(),
					re_checkman:$("#checkman").val().trim(),
					re_provide_num:providerTem,
					re_contact_id:contactTem,
					re_outofbill_num:dataItem3.name,
					re_date:$("#ctime").val().trim(),
					re_pdate:$("#p_date").val().trim(),
					re_totalmoney:totalMoney,
					re_remarks:$("#base_remarks").val().trim(),
					re_before_pay:$("#ppay").val().trim(),
					re_fact_pay:$("#fpay").val().trim(),
					contacts:contacts
			};
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/customer/" + receivingnote.SIMPLE_TYPE,
				type:"POST",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				data:JSON.stringify(model),
				success:function(data){
					if(data.result&&data.result.status=="true"){
						fsn.initNotificationMes("新增成功！", true);
						$("#OPERATION_WIN").data("kendoWindow").close();
					}
					else{
						fsn.initNotificationMes("新增失败！", false);
					}
					receivingnote.datasource.read();
					
				}
			});
		}else{
			console.log("No");
			fsn.initNotificationMes("Failure",false);
		}
	 }
	};
	
	
	receivingnote.initContactInfoButtons = function(){
		$("#add_").bind("click",function(){
			receivingnote.addContact();
		});
		$("#update_").bind("click",function(){
			receivingnote.updateProduct();
		});
		$("#delete_").bind("click",function(){
			if(receivingnote.contactGrid.data("kendoGrid").select().length > 0){
				var win = eWidget.getPromptWindow();
				win.data("kendoWindow").open().center();
				$("#confirmWindow").unbind("click");
				$("#cancelWindow").unbind("click");
				$("#confirmWindow").bind("click",function(){
					receivingnote.deleteContact();
					fsn.initNotificationMes("删除成功",true);
					win.data("kendoWindow").close();
				});
				$("#cancelWindow").bind("click",function(){
					win.data("kendoWindow").close();
				});
			}else{
				fsn.initNotificationMes("请选择一条记录！",false);
			}
			
		});
	};
	
	//修改商品收货信息
	receivingnote.updateProduct = function(){
		if(receivingnote.contactGrid.data("kendoGrid").select().length > 0 ){
			
			var contactItem = receivingnote.selectedContact = receivingnote.contactGrid.data("kendoGrid").dataItem(receivingnote.contactGrid.data("kendoGrid").select());
			if(contactItem !=null){
				receivingnote.bindUpdateForm(contactItem);
				receivingnote.contactWin.data("kendoWindow").open().center();
				
				$("#confirm_").unbind("click");
				$("#cancel_").unbind("click");
				
				$("#confirm_").bind("click",function(){
					var dataItem3 = $("#saddress").data("kendoDropDownList").dataItem();
					var StroageTem = {
						name:dataItem3.name,
					};
					var dataItem4 = $("#cid").data("kendoDropDownList").dataItem();
					var pItem = {
						no:dataItem4.no,
						name:dataItem4.name,
						specification:dataItem4.specification,
					};
						var flag = receivingnote.checkDiff(StroageTem,pItem);
					if(flag){
						receivingnote.saveConfirm(false);
						fsn.initNotificationMes("修改成功",true);
						receivingnote.contactWin.data("kendoWindow").close();
					}else{
						fsn.initNotificationMes("未修改,不能保存！",false);
					}
					
				});
				
				$("#cancel_").bind("click",function(){
					receivingnote.contactWin.data("kendoWindow").close();
				});
			}
		}else{
			fsn.initNotificationMes("请选择一条记录！",false);
		}
	};
	
	//删除
	receivingnote.deleteContact = function(){
		if(receivingnote.contactGrid.data("kendoGrid").select().length > 0 ){
			var oriLength = receivingnote.contactGrid.data("kendoGrid").dataSource.data().length;
			var contactItem = receivingnote.contactGrid.data("kendoGrid").dataItem(receivingnote.contactGrid.data("kendoGrid").select());
			receivingnote.contactGrid.data("kendoGrid").dataSource.remove(contactItem);
			var nowLength = receivingnote.contactGrid.data("kendoGrid").dataSource.data().length;
			if(oriLength != nowLength) {
				receivingnote.checkFlag = true;
			}
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/customer/12/findProduct?num=" +fsn.receivingnote.dropDownListOutGood.text(), //receivingnote.dropDownListOutGood.dataItem().name,
				type:"GET",
				dataType:"json",
				async:false,
				contentType: "application/json; charset=utf-8",
				success: function(data){
					if(data && data.result){
						$("#cid").data("kendoDropDownList").setDataSource(data.result); 
//	        			receivingnote.contactComboBoxProduct.setDataSource(data.result); 
	        		}
				}	
			});
		 }
		}
	//修改
	receivingnote.windowUpdateConfrim = function(){
		//validation
		if(receivingnote.validator.validate()){
			if(receivingnote.selectedData.re_checkman != $("#checkman").val().trim() ||
					receivingnote.selectedData.re_pdate != $("#p_date").val().trim() ||
					receivingnote.selectedData.re_before_pay != $("#ppay").val().trim() ||
					receivingnote.selectedData.re_fact_pay != $("#fpay").val().trim() ||
					receivingnote.selectedData.re_remarks != $("#base_remarks").val().trim() ||
					receivingnote.checkFlag) {
				receivingnote.checkFlag = false;
				var combobox = $("#provider").data("kendoDropDownList");
				var dataItem = combobox.dataItem();
				var providerTem = {
					id:dataItem.id,
				};
				//contact
				var proContacts = $("#contactman").data("kendoDropDownList");
				var proItem = proContacts.dataItem();
				var contactTem = {
					id:proItem.id,
				};
				//收货单号
				var combobox2 = $("#no").data("kendoDropDownList");
				var orderTypeTem = {
					ot_type:combobox2.text()
				};
				
				//出货单号
				var combobox3 = $("#receive_no").data("kendoDropDownList");
				var dataItem3 = combobox3.text();
				
				var model = {
				};
				/********************************/
				var _contacts=receivingnote.contactGrid.data("kendoGrid").dataSource.data();
				if(_contacts.length==0){
					fsn.initNotificationMes("商品不能为空！</br>编辑失败", false);
					return;
				}
				var totalMoney = 0;
				var contacts=[];
				for(var i=0;i<_contacts.length;i++){
					totalMoney = totalMoney + _contacts[i].label_totalp;
					delete _contacts[i].label_totalp;
					contacts[i]=_contacts[i];
				} 
//				receivingnote.checkFlag = false;
				/********************************/
				model.receivingNote = {
						re_num:orderTypeTem.ot_type,
						re_source:$("#source").val().trim(),
						re_checkman:$("#checkman").val().trim(),
						re_provide_num:providerTem,
						re_contact_id:contactTem,
						re_outofbill_num:dataItem3,
						re_date:$("#ctime").val().trim(),
						re_pdate:$("#p_date").val().trim(),
						re_totalmoney:totalMoney,
						re_before_pay:$("#ppay").val().trim(),
						re_fact_pay:$("#fpay").val().trim(),
						re_remarks:$("#base_remarks").val().trim(),
						re_purchase_check:"已确认",
						contacts:contacts
				};
				$.ajax({
					url: fsn.getHttpPrefix() + "/erp/customer/" + receivingnote.SIMPLE_TYPE,
					type:"PUT",
					dataType:"json",
					contentType: "application/json; charset=utf-8",
					data:JSON.stringify(model),
					success:function(data){
						if(data.model){
							fsn.initNotificationMes("编辑成功！", true);
							$("#OPERATION_WIN").data("kendoWindow").close();
						}
						else{
							fsn.initNotificationMes("编辑失败！", false);
						}
						receivingnote.datasource.read();
						
					}
				});
			}else {
				fsn.initNotificationMes("还没有修改数据",false);
			}
		}else{
			console.log("No");
			//fsn.initNotificationMes("Failure",false);
		}
	};
	
	receivingnote.windowDeleteConfrim = function(){
		
		if(receivingnote.selectedData.re_num){
			var model = {
				receivingNote:{
					re_num:receivingnote.selectedData.re_num
				},
			};
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/customer/" + receivingnote.SIMPLE_TYPE,
				type:"DELETE",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				data:JSON.stringify(model),
				success:function(data){
					if(data && data.result){
						fsn.initNotificationMes("删除成功！", true);
						receivingnote.datasource.remove(receivingnote.grid.dataItem(receivingnote.grid.select()));
					}else{
						fsn.initNotificationMes("删除失败！", false);
					}
				}
			});
		}
	};
	
	receivingnote.addContact = function(){
			if (receivingnote.contactComboBoxProvider.value() == "") {
				fsn.initNotificationMes("请先选择供应商！", false);
				return;
			}else if(receivingnote.dropDownListOutGood.text()==""||receivingnote.dropDownListOutGood.text()=="请选择来源单号"){
				fsn.initNotificationMes("请先选择单号！", false);
				return;
			}
		var ds = receivingnote.contactComboBoxProduct.dataSource.data();
		var gridDs = receivingnote.contactGrid.data("kendoGrid").dataSource.data();

			if(gridDs.length>0){
				for(var i=0;i<gridDs.length;i++){
					var dataItem = gridDs[i];
					for(var j=0;j<ds.length;j++){
						if(ds[j].batchNo==gridDs[i].po_batch&&ds[j].no==gridDs[i].product.barcode){
							ds.remove(ds[j]);
						}
					}
				}
				if(ds.length<1){
					fsn.initNotificationMes("没有可添加的商品",false);
					return;
				}
			}
		receivingnote.cleanContactForm();
		receivingnote.contactWin.data("kendoWindow").open().center();
		
		var oriLength = receivingnote.contactGrid.data("kendoGrid").dataSource.data().length;
		
		$("#confirm_").unbind("click");
		$("#cancel_").unbind("click");
		
		$("#confirm_").bind("click",function(){
			var flag = receivingnote.verifyProducts();
			if(flag){
				receivingnote.saveConfirm(true);
				fsn.initNotificationMes("保存成功",true);
				var nowLength = receivingnote.contactGrid.data("kendoGrid").dataSource.data().length;
				if(oriLength != nowLength) {
					receivingnote.checkFlag = true;
				}
				receivingnote.contactWin.data("kendoWindow").close();
			}
		});
		
		$("#cancel_").bind("click",function(){
			receivingnote.contactWin.data("kendoWindow").close();
		});
	};
	
		receivingnote.saveConfirm = function(isNew){
			var contact_validator = $("#CONTACT_INFO_WIN").kendoValidator().data("kendoValidator");
			if(contact_validator.validate()){
				//仓库号
				var combobox3 = $("#saddress").data("kendoDropDownList");
				var dataItem3 = combobox3.dataItem();
				var StroageTem = {
					name:dataItem3.name,
				};
				//unit
				var unitTem ={
						id:$("#unit").attr("value"),
						name:$("#unit").val().trim()
				};
				//product
				var combobox4 = $("#cid").data("kendoDropDownList");
				var dataItem4 = combobox4.dataItem();
				var pItem = {
					barcode:dataItem4.no,
					name:dataItem4.name,
					format:dataItem4.specification,
				};
				var price = $("#singerprice").val().trim()==""?0:$("#singerprice").val().trim();
				var model = {};
				if(isNew){
					model = {
						product:pItem,
						po_ismode:$("input[name='ismode']:checked").val().trim(),
						hasreport:$("input[name='ishasreporter']:checked").val().trim(),
						po_isneedqr:$('input[name="isreport"]:checked').val().trim(),
						po_receivenum:$("#rnum").val().trim(),
						po_unit:unitTem,
						po_batch:$("#batch").val().trim(),
						po_remark:$("#p_remarks").val().trim(),
						po_mtype:$("#m_type").val().trim(),
						po_price:price,
						po_storage_address:StroageTem.name,
						label_totalp:price*$("#rnum").val().trim(),
					};
				}else{
					if(receivingnote.selectedContact.id){
						model.id = receivingnote.selectedContact.id;
					}
				}
				
				if(isNew){
					receivingnote.contactGrid.data("kendoGrid").dataSource.add(model);
				}else{
					var modelUpdateAndChecked = {};
					receivingnote.contactGrid.data("kendoGrid").dataSource.remove(receivingnote.selectedContact);
					modelUpdateAndChecked = {
							product:pItem,
							po_ismode:$("input[name='ismode']:checked").val().trim(),
							po_isgift:$("input[name='ishasreporter']:checked").val().trim(),
							po_isneedqr:$('input[name="isreport"]:checked').val().trim(),
							po_receivenum:$("#rnum").val().trim(),
							po_unit:unitTem,
							po_batch:$("#batch").val().trim(),
							po_remark:$("#p_remarks").val().trim(),
							po_mtype:$("#m_type").val().trim(),
							po_price:price,
							po_storage_address:StroageTem.name,
							label_totalp:price*$("#rnum").val().trim(),
						};
					receivingnote.checkDiff(StroageTem, pItem);//更新的时候检查
					receivingnote.contactGrid.data("kendoGrid").dataSource.add(modelUpdateAndChecked);
				}
			}
		};
		
		receivingnote.cleanContactForm = function(){
			receivingnote.contactComboBoxProduct.value("");
			$("#spec").val("");
			$("#rnum").val("");
			$("#unit").val("");
			receivingnote.verifyCnum.value("");
			receivingnote.verifyCmoney.value("");
			receivingnote.verifyCcnum.value("");
			$("#batch").val("");
			$("#singerprice").val("");
			$("#p_remarks").val("");
			receivingnote.dropDownListStorage.value("");
		};
		
		//修改
		receivingnote.bindUpdateForm = function(e){
			var num = receivingnote.dropDownListOutGood.text();
			receivingnote.contactComboBoxProduct = eWidget.getComboBoxProduct("cid",receivingnote.SIMPLE_TYPE,num).data("kendoDropDownList");
			fsn.receivingnote.contactComboBoxProduct.value(e.product.barcode);
			fsn.receivingnote.contactComboBoxProduct.readonly(true);
			$("#spec").val(e.product.format);
			if(e.po_ismode == true||e.po_isgift == "true"){
				$("input[name='ismode'][value='false']").attr("disabled",true);
			}else{
				$("input[name='ismode'][value='true']").attr("disabled",true);
			}
		    $("input[name='ismode'][value="+e.po_ismode+"]").attr("checked",true);
		    if(e.hasreport == true||e.hasreport == "true"){
		    	$("input[name='ishasreporter'][value='false']").attr("disabled",true);
		    }else{
		    	$("input[name='ishasreporter'][value='true']").attr("disabled",true);
		    }
		    $("input[name='ishasreporter'][value="+e.po_isgift+"]").attr("checked",true); 
			$("#rnum").val(e.po_receivenum);
			$("#unit").attr("readonly","readonly");
			$("#unit").val(e.po_unit.name);
			$("#unit").attr({"value":e.po_unit.id});
			$("#batch").val(e.po_batch);
			$("#singerprice").val(e.po_price);
			$("#p_remarks").val(e.po_remark);
			fsn.receivingnote.dropDownListStorage.text(e.po_storage_address);
			if(e.po_isneedqr == true||e.po_isneedqr == "true"){
				$("input[name='isreport'][value='false']").attr("disabled",true);
			}else{
				$("input[name='isreport'][value='true']").attr("disabled",true);
			}
			$("input[name='isreport'][value="+e.po_isneedqr+"]").attr("checked",true); 
			
		};
		
		receivingnote.verifyProducts = function() {
			if($("#cid").data("kendoDropDownList" ).value() == "") {
				fsn.initNotificationMes("请选择商品！",false);
				$("#cid").focus();
				return;
			}
			if($("#saddress").data("kendoDropDownList" ).text() == ""||$("#saddress").data("kendoDropDownList" ).text() == "请选择仓库") {
				fsn.initNotificationMes("请选择仓库！",false);
				$("#saddress").focus();
				return;
			}
			if($("#m_type").val().trim() == "") {
				fsn.initNotificationMes("请填写币种！",false);
				$("#m_type").focus();
				return;
			}
			return true;
		};
		
		receivingnote.verifyBaseInfo = function() {
			if($("#no").data("kendoDropDownList").value() == "") {
				fsn.initNotificationMes("请选择收货单编号！",false);
				$("#no").focus();
				return false;
			}
			if($("#provider").data("kendoDropDownList").value() == "") {
				fsn.initNotificationMes("请选择供货商！",false);
				$("#provider").focus();
				return false;
			}
			if($("#receive_no").data("kendoDropDownList").value() == "") {
				fsn.initNotificationMes("请选择来源单号！",false);
				$("#receive_no").focus();
				return false;
			}
			if($("#contactman").data("kendoDropDownList").value() == "") {
				fsn.initNotificationMes("请选择联系人！",false);
				$("#contactman").focus();
				return false;
			}
			if($("#checkman").val().trim() == "") {
				fsn.initNotificationMes("请填写验收员！",false);
				$("#checkman").focus();
				return false;
			}
			if($("#ppay").val().trim() == "") {
				fsn.initNotificationMes("请填写预付款！",false);
				$("#ppay").focus();
				return false;
			}
			return true;
		};
		
	receivingnote.detailBaseInfo = function(Item){
		$("#detail_label_num_content").html(Item.re_num);
		$("#detail_label_source_content").html(Item.re_source);
		$("#detail_label_provider_content").html(Item.re_provide_num.name);
		$("#detail_label_receive_no_content").html(Item.re_outofbill_num);
		$("#detail_label_contactman_content").html(Item.re_contact_id.name);
		$("#detail_label_caddress_content").html(Item.re_contact_id.province+Item.re_contact_id.city+Item.re_contact_id.area+Item.re_contact_id.addr);
		$("#detail_label_contacttel_content").html(Item.re_contact_id.tel_1);
		$("#detail_label_ctime_content").html(Item.re_date);
		$("#detail_label_checkman_content").html(Item.re_checkman);
		$("#detail_lable_p_date_content").html(Item.re_pdate);
		$("#detail_label_ppay_content").html(Item.re_before_pay);
		$("#detail_label_fpay_content").html(Item.re_fact_pay);
		$("#detail_label_totalp_content").html(Item.re_totalmoney);
		$("#detail_lable_base_remarks_content").html(Item.re_remarks);
		$("#detail_label_state_content").html(Item.re_purchase_check);
		receivingnote.productGrid.data("kendoGrid").setDataSource(Item.contacts==null?[]:Item.contacts);
	};
	
	receivingnote.GRIDID = "Simple_Grid";
	receivingnote.DAILOGID = "OPERATION_WIN"; 
	
	receivingnote.initailize = function(){
		this.initComponent(receivingnote);
		$("#label_no").html("收货单编号");
		$("#label_source").html("来源");
		$("#label_checkman").html("检收员");
		$("#label_provider").html("供应商");
		$("#label_contactman").html("联系人");
		$("#label_contacttel").html("联系人电话");
		$("#label_caddress").html("联系人地址");
		$("#label_payc").html("付款条件");
		$("#label_totalp").html("总金额");
		$("#label_receiveno").html("收货单号");
		$("#label_ppay").html("预付款");
		$("#label_fpay").html("实付金额");
		$("#label_isreport").html("是否需要质检报告");
		$("#label_fpay").html("实付金额");
		$("#label_remark").html("备注");
		$("#label_unit").html("单位");
		this.initRequiredComponents();
	};
	
	receivingnote.checkFlag = false; // 验证字段
	receivingnote.checkDiff = function(StroageTem, pItem) {
		if(receivingnote.selectedContact.product.name != pItem.name ||
				receivingnote.selectedContact.po_storage_address != StroageTem.name ||
				receivingnote.selectedContact.po_remark != $("#p_remarks").val().trim() ||
				receivingnote.selectedContact.po_mtype != $("#m_type").val().trim() ||
				receivingnote.selectedContact.po_chbackmoney != $("#cmoney").val().trim() ||	
				receivingnote.selectedContact.po_changenum != $("#ccnum").val().trim() ||
				receivingnote.selectedContact.po_backnum != $("#cnum").val().trim()) {
			receivingnote.checkFlag = true;
		}else{
			receivingnote.checkFlag = false;
		}
		return receivingnote.checkFlag;
	};
	
	receivingnote.initailize();
	receivingnote.judgeIsUsed = function(){
		return true;
	};
			
	initStandard = function(keywords){
		var ds = new kendo.data.DataSource({
					transport: {
						read : {
				            type : "GET",
				            url : function(options){
								return fsn.getHttpPrefix() + "/erp/customer/" + receivingnote.SIMPLE_TYPE + "/search?keywords="+keywords;
							},
				            dataType : "json",
				            contentType : "application/json"
				        },
				    },
				    schema: {
				    	model: {
				    		 fields: {
				    			re_num:{type:"string"},
				    			re_checkman:{type:"string"},
				    			re_date:{type:"Date(yyyy-MM-dd)"},
				             	note:{type:"string"}
				             }
				        },
				    	data : function(data) {
				    		if(data && data.result && data.result.listOfModel){
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
});