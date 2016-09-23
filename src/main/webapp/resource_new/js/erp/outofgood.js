$(function(){
	var fsn = window.fsn = window.fsn || {};
	var eWidget = fsn.eWidget = fsn.eWidget||{};
	var common = fsn.common = fsn.common || {};
	var outofgood = fsn.outofgood = fsn.outofgood || {};
	$.extend(outofgood,common);
	
	outofgood.datasource = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
                url : function(options){
                	if(options.filter){
                		var configure = filter.configure(options.filter);
                 		return fsn.getHttpPrefix() + "/erp/outOfGoods/reglist/" + options.page + "/" + options.pageSize + "/"+configure;;
                	};
					return fsn.getHttpPrefix() + "/erp/outOfGoods/list";
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
        			 outOfBillNo:{
        				 editable : false,
        			 },
        			 outDate:{type:"Date(yyyy-MM-dd)"},
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
	//初始化商品,根据出货单号
	outofgood.initOutGoodItemDS = function(outGoodNum){
    	if(outGoodNum){
    		outofgood.outGoodItemNum = outGoodNum;
    	}else{
    		outofgood.outGoodItemNum = 0;
    	}
    	outofgood.outGoodItemFirstPageFlag = 1;
    	outofgood.outGoodItemDS = new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){
	            		if(outofgood.outGoodItemNum == 0){
	            			return;
	            		}
	            		if(outofgood.outGoodItemFirstPageFlag == 1){  //每次预览新的报告的时候，都从第一页开始显示
	            			options.page=1;
	            			options.pageSize=5;
	            			outofgood.outGoodItemFirstPageFlag=0;
	            		}
	            		return fsn.getHttpPrefix() + "/erp/outOfGoods/findOutGoods/" + options.page + "/" + options.pageSize + "/" + outofgood.outGoodItemNum;
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
	            data : function(data) {
	            	if(data && data.result && data.result.listOfModel){
	            		console.log(data);
	        			return data.result.listOfModel;  
	        		}
	                return null;
	            },
	            total : function(data) {       
	            	if(data && data.result && data.result.count){
	            		return  data.result.count;
	            	}
	            	return 0;
	            }     
	        },
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
	};
	outofgood.columns = [
	               {field: "outOfBillNo", title: "出货单号", width:"15%",editor:false},
	               {field:"outDate", 
	                	  title:"出货日期",
	                	  filterable: false,},
	               {field: "customer.name", title: "客户名称", width:"15%"},
	               {field: "outOfBillState", title: "出货单状态", width:"20%"},
				    {field: "userName", title: "操作人", width:"20%"},
	               { command: [{name : "交易完成",
           			 click : function(e) {
           				var tr = $(e.target).closest("tr");
           				var item = this.dataItem(tr);
           				
           				if(item.outOfBillState == "待发货") {
           					fsn.initNotificationMes("还没有发货，不能交易完成",false);
           					return;
           				}
           				
           				if(item.outOfBillState == "已交付") {
           					fsn.initNotificationMes("已交付",false);
           					return;
           				}
           				
           				var model = {
           				};
           				model = {
           						outOfBillNo: item.outOfBillNo,
           				}
           				var win = eWidget.getPromptWindow1();
						win.data("kendoWindow").open().center();
						$("#confirmWindowSure").unbind("click");
						$("#cancelWindowNot").unbind("click");
						$("#confirmWindowSure").bind("click",function(){
							$.ajax({
           						url: fsn.getHttpPrefix() + "/erp/outOfGoods/checkTwo",
           						type:"POST",
           						dataType:"json",
           						contentType: "application/json; charset=utf-8",
           						data:JSON.stringify(model),
           						success:function(data){
           							if(data && data.result){
           								console.log("OK");
           							}
           							outofgood.datasource.read();
           						}
           					});
							win.data("kendoWindow").close();
						});
						$("#cancelWindowNot").bind("click",function(){
							win.data("kendoWindow").close();
						});
           			 }
           			 },{name:"预览",click:function(e){
           				 var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
           				 outofgood.initOutGoodItemDS(dataItem.outOfBillNo);
           				 $("#OUTOFGOODS_INFO_GRID").data("kendoGrid").setDataSource(outofgood.outGoodItemDS);
           				 outofgood.detailOutOfGoodsInfo(dataItem);
           				 var addWin = eWidget.getWindow("OUTOFGOODS_DETAIL_INFO","详细信息",common.SIMPLE_MODEL_NAME, common.realObj.win_width == null ? null:common.realObj.win_width);
           				 addWin.data("kendoWindow").open().center();
           			 }}],
                       title: "操作",
                       width: "18%"
                       }
	               
	               ];
	
	
	/**
	 * add, update, delete
	 */
	outofgood.win_width = 850;
	
	outofgood.selectedData = {};
	
	outofgood.clearForm = function(){
		fsn.outofgood.dropListOrderType.readonly(false);
		fsn.outofgood.dropListOrderType.text("请选择单别");
		$("#note").val("");
		$("#type").data( "kendoComboBox" ).value("");
		$("#contactman").data( "kendoComboBox" ).value("");
		outofgood.contactGrid.data("kendoGrid").dataSource.data([]);
		
		$("#invoice").val("");
		$("#transportation").val("");
		$("#total_price").val("");
		
		$("#cprovince").val("");
		$("#ccity").val("");
		$("#carea").val("");
		$("#zip_code").val("");
		$("#address").val("");
		$("#tel").val("");
		fsn.outofgood.dropListOrderType.readonly(false);
		$("#no").data("kendoDropDownList").value("");
	}
	
	outofgood.bindForm = function(){
		outofgood.initOutGoodItemDS(outofgood.selectedData.outOfBillNo);
		$("#CONTACT_INFO_GRID").data("kendoGrid").setDataSource(outofgood.outGoodItemDS);
		fsn.outofgood.dropListOrderType.text(outofgood.selectedData.outOfBillNo);
		fsn.outofgood.dropListOrderType.readonly(true);
		$("[data-bind='value:outGoodDate']").val(outofgood.selectedData.outDate);
		$("[data-bind='value:note']").val(outofgood.selectedData.note);
		$("[data-bind='value:invoice']").val(outofgood.selectedData.invoice);
		$("[data-bind='value:transportation']").val(outofgood.selectedData.transportation);
		$("[data-bind='value:total_price']").val(outofgood.selectedData.totalPrice);
		
		fsn.outofgood.dropListOrderType.readonly(true);
		$("#type").data( "kendoComboBox" ).value(outofgood.selectedData.customer.name);
		
		outofgood.contactComboBoxContact = eWidget.getComboBoxContact("contactman",outofgood.SIMPLE_TYPE,outofgood.selectedData.customer.id).data("kendoComboBox");
		
		$("#cprovince").val(outofgood.selectedData.contactProvince);
		$("#ccity").val(outofgood.selectedData.contactCity);
		$("#carea").val(outofgood.selectedData.contactArea);
		$("#zip_code").val(outofgood.selectedData.contactZipcode);
		$("#address").val(outofgood.selectedData.contactAddr);
		$("#tel").val(outofgood.selectedData.contactTel);
		
		$( "#contactman").data( "kendoComboBox" ).value(outofgood.selectedData.contactInfo.id);
		$( "#contactman").data( "kendoComboBox" ).text(outofgood.selectedData.contactInfo.name);
	}
	
	outofgood.flag = false;
	
	outofgood.windowSaveConfrim = function(){
		
		outofgood.flag = outofgood.verifyBill();
		//validation
		if(outofgood.flag){
			outofgood.checkFlag = false;
			outofgood.flag = false;
			var model = {
			};
			var combobox = $("#type").data("kendoComboBox");
			var dataItem = combobox.dataItem();
			var customerTem = {
				id:dataItem.id,
				name:dataItem.name,
				note:dataItem.note
			};
			
			var combobox1 = $("#contactman").data("kendoComboBox");
			var dataItem1 = combobox1.dataItem();
			var contactTem = {
				id:dataItem1.id,
				name:dataItem1.name,
			};
			
			var combobox2 = $("#no").data("kendoDropDownList");
			var dataItem2 = combobox2.dataItem();

			var contacts = outofgood.contactGrid.data("kendoGrid").dataSource.data();
			
			if(contacts.length==0){
				fsn.initNotificationMes("未添加商品！</br>新增失败！", false);
				return;
			}
			
			model = {
					outOfBillNo: dataItem2.ot_type,
					outDate: $("#outGoodDate").val().trim(),
					source: $("#bill_source").val().trim(),
					customer: customerTem,
					contactInfo: contactTem,
					invoice: $("#invoice").val().trim(),
					transportation: $("#transportation").val().trim(),
					note: $("#note").val().trim(),
					outOfBillState: "待发货",
					contactProvince: $("#cprovince").val(),
					contactCity:  $("#ccity").val(),
					contactArea:  $("#carea").val(),
					totalPrice: $("#total_price").val().trim() == "" ? 0 : $("#total_price").val().trim(),
					contactAddr: $("#address").val().trim(),
					contactTel: $("#tel").val().trim(),
					contactZipcode: $("#zip_code").val().trim(),
					contacts: contacts,
			}
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/outOfGoods/add",
				type:"POST",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				data:JSON.stringify(model),

				success:function(data){
					if(data.result.success==true){
						fsn.initNotificationMes("新增成功！", true);
						$("#OPERATION_WIN").data("kendoWindow").close();
						outofgood.datasource.read();
					}
					else{
						fsn.initNotificationMes("新增失败！", false);
					}		
				}
			});
		}else{
			outofgood.flag = true;
		}
	}
	
	outofgood.windowUpdateConfrim = function(){
		outofgood.flag = outofgood.verifyBill();
		if(outofgood.flag){
			if(outofgood.selectedData.outDate != $("#outGoodDate").val().trim() ||
					outofgood.selectedData.invoice != $("#invoice").val().trim() ||
					outofgood.selectedData.transportation != $("#transportation").val().trim() ||
					outofgood.selectedData.transportation != $("#transportation").val().trim() ||
					outofgood.selectedData.customer.name !=  $("#type").data("kendoComboBox").dataItem().name ||
					outofgood.selectedData.contactInfo.name !=  $("#contactman").data("kendoComboBox").dataItem().name ||
					outofgood.selectedData.note != $("#note").val().trim() || outofgood.checkFlag) {
				outofgood.flag = false;
				outofgood.checkFlag = false;
				var model = {
				};
				var combobox = $("#type").data("kendoComboBox");
				var dataItem = combobox.dataItem();
				var customerTem = {
					id:dataItem.id,
					name:dataItem.name,
					type:dataItem.type,
					note:dataItem.note
				};
				
				var combobox1 = $("#contactman").data("kendoComboBox");
				var dataItem1 = combobox1.dataItem();
				
				var combobox2 = $("#no").data("kendoDropDownList");
				var dataItem = {
						ot_type:combobox2.text()
						};
	
				var contactTem = {
					id: dataItem1 == undefined ? outofgood.selectedData.contactInfo.id : dataItem1.id,
					name: dataItem1 == undefined ? outofgood.selectedData.contactInfo.name : dataItem1.name,
				};
				
				var contacts = outofgood.contactGrid.data("kendoGrid").dataSource.data();
				
				if(contacts.length==0){
					fsn.initNotificationMes("商品不能为空！</br>编辑失败！", false);
					return;
				}
				
				model = {
						outOfBillNo: dataItem.ot_type,
						outDate: $("#outGoodDate").val().trim(),
						source: $("#bill_source").val().trim(),
						customer: customerTem,
						contactInfo: contactTem,
						invoice: $("#invoice").val().trim(),
						transportation: $("#transportation").val().trim(),
						note: $("#note").val().trim(),
						outOfBillState: "待发货",
						contactProvince:$("#cprovince").val(),
						contactCity:$("#ccity").val(),
						contactArea: $("#carea").val(),
						totalPrice: $("#total_price").val().trim() == "" ? 0 : $("#total_price").val().trim(),
						contactAddr: $("#address").val().trim(),
						contactTel: $("#tel").val().trim(),
						contactZipcode: $("#zip_code").val().trim(),
						contacts: contacts,
				}
				$.ajax({
					url: fsn.getHttpPrefix() + "/erp/outOfGoods/update",
					type:"POST",
					dataType:"json",
					contentType: "application/json; charset=utf-8",
					data:JSON.stringify(model),
					success:function(data){
						console.log(data);
						if(data && data.result){
							if(data.result.success==true){
						fsn.initNotificationMes("编辑成功！", true);
						$("#OPERATION_WIN").data("kendoWindow").close();
					}
					else{
						fsn.initNotificationMes("编辑失败！", false);
					}
							console.log("OK");
						}
						outofgood.datasource.read();
					}
				});
			} else {
				fsn.initNotificationMes("还没有修改数据",false);
			}
		}else{
			outofgood.flag = true;
		}
	}
	
	outofgood.windowDeleteConfrim = function(){
		var model = {
				outOfBillNo: outofgood.selectedData.outOfBillNo
		}
		if(outofgood.selectedData.outOfBillNo){
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/outOfGoods/delete",
				type:"POST",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				data:JSON.stringify(model),
				success:function(data){
					console.log(data);
					if(data.result){
						fsn.initNotificationMes("删除成功！", true);
					}else{
						fsn.initNotificationMes("删除失败！", false);
					}
					outofgood.datasource.read();
				}
			});
		}
	}
	
	outofgood.contactGrid = null;
	outofgood.contactComboBox = null;
	outofgood.comboBoxType = {};
	outofgood.contactWin = null;
	outofgood.selectedContact = null;
	outofgood.contactComboBoxContact = null;
	
	outofgood.provinceCombox = null;
	outofgood.outofgoodGrid = null;
	outofgood.ComboBoxGetProductBatchNo = null;
	outofgood.contactComboBoxType = null;
	outofgood.contactComboBoxUnit = null;
	outofgood.contactComboBoxCategory = null;
	outofgood.contactComboBoxStorage = null;
	outofgood.comboBoxCategory = {};
	outofgood.comboBoxType = {};
	outofgood.comboBoxUnit = {};
	outofgood.comboBoxStorage = {};
	
	outofgood.contactComboBoxProduct = null;
	outofgood.comboBoxProduct = {};
	
	outofgood.merchandiseComboBox = null;
	outofgood.comboBoxContactMan = {};
	
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
	ButchChange = function(e){
		var judge = e.sender.selectedIndex;
		if(judge==-1){
			fsn.initNotificationMes("填写内容有误！请重新填写", false);
			outofgood.ComboBoxGetProductBatchNo.value("");
		}else{
			var batch = e.sender._prev;
			var id = outofgood.contactComboBoxProduct.value();
			var storage = outofgood.contactComboBoxStorage.value();
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/outOfGoods/isHasReporter?id="+id+"&batch="+batch,
				type:"POST",
				dataType:"json",
				async:false,
				contentType: "application/json; charset=utf-8",
				success:function(data){
					$("input[name='has_inspectionReport']").attr("disabled",false);
					if(data.flag==true){
						document.getElementById("has_inspectionReport").checked=true;
						document.getElementById("has_inspectionReport1").disabled=true;
					}else{
						document.getElementById("has_inspectionReport1").checked=true;
						document.getElementById("has_inspectionReport").disabled=true;
					}
				}
			});
			if(storage==""){
				$.ajax({
					url: fsn.getHttpPrefix() + "/erp/outOfGoods/getStorages?productInstanceId=" + outofgood.ComboBoxGetProductBatchNo.dataItem().instanceID +"&barcode=null",
					type: "GET",
					dataType: "json",
					async:false,
					contentType: "application/json; charset=utf-8",
					success: function(data){
						outofgood.contactComboBoxStorage.setDataSource("");
						outofgood.contactComboBoxStorage.setDataSource(data.result);
					}
				});
			}else{
				var instanceId = outofgood.ComboBoxGetProductBatchNo.dataItem().instanceID;
				var storageId = outofgood.contactComboBoxStorage.dataItem().no;
				$.ajax({
		       			url: fsn.getHttpPrefix() + "/erp/merchandiseStorage/getStorageNum?instanceId="+instanceId+"&&storageId="+storageId,
		       				type:"GET",
		       				dataType:"json",
		       				contentType: "application/json; charset=utf-8",
		       				success:function(data){
								if(data){
									var storageNum = data.result.count;
									$("#num").val(storageNum);
								}else{
									$("#num").val("");
								}
							}
				})
			}
		}
	}
	StorageChange = function(e){
		var judge = e.sender.selectedIndex;
		if(judge==-1){
			this.value("");
			fsn.initNotificationMes("填写内容有误！请重新填写", false);
			this.value("");
		}else{
			var storage = e.sender._selectedValue;
			var id = outofgood.contactComboBoxProduct.value();
			var batch = outofgood.ComboBoxGetProductBatchNo.value();
			if(batch==""){
				$.ajax({
					url: fsn.getHttpPrefix() + "/erp/outOfGoods/findBatches?id=" + id +"&storage="+ storage,
					type: "GET",
					dataType: "json",
					contentType: "application/json; charset=utf-8",
					success: function(data){
						outofgood.ComboBoxGetProductBatchNo.setDataSource("");
						outofgood.ComboBoxGetProductBatchNo.setDataSource(data.result);
					}
				});
			}else{
				var instanceId = outofgood.ComboBoxGetProductBatchNo.dataItem().instanceID;
				var storageId =this.value();
				$.ajax({
		       				url: fsn.getHttpPrefix() + "/erp/merchandiseStorage/getStorageNum?instanceId="+instanceId+"&&storageId="+storageId,
		       				type:"GET",
		       				dataType:"json",
		       				contentType: "application/json; charset=utf-8",
		       				success:function(data){
								if(data){
									var storageNum = data.result.count;
									$("#num").val(storageNum);
								}else{
									$("#num").val("");
								}
							}
				})
			}
		}
	}
	outofgood.initRequiredComponents = function(){
		
		outofgood.contactComboBoxType = eWidget.getComboBox("type_id",7).data("kendoComboBox");
		outofgood.contactComboBoxType.bind("change", combobox_change);
		outofgood.contactComboBoxUnit = eWidget.getComboBox("unit_id",4).data("kendoComboBox");
		outofgood.contactComboBoxUnit.bind("change", combobox_change);
		outofgood.contactComboBoxCategory = eWidget.getComboBox("category_id",8).data("kendoComboBox");
		outofgood.contactComboBoxCategory.bind("change", combobox_change);
		outofgood.contactComboBoxType.setOptions({
			change: function(e) {
				outofgood.comboBoxType = {
						id:this.value(),
						name:this.text()
				}
			  }
		});
		
		outofgood.contactComboBoxUnit.setOptions({
			change: function(e) {
				outofgood.comboBoxUnit = {
						id:this.value(),
						name:this.text()
				}
			  }
		});
		outofgood.contactComboBoxCategory.setOptions({
			change: function(e) {
				outofgood.comboBoxCategory = {
						id:this.value(),
						name:this.text()
				}
			  }
		});
		
		outofgood.contactGrid = $("#CONTACT_INFO_GRID").kendoGrid({
						            dataSource: new kendo.data.DataSource({
     	                             data:[],
     	                             batch : true,
                                   	 page:1,
						             pageSize: 5,
						             serverPaging : true,
						             serverFiltering : true,
						             serverSorting : true
          							 }),
						            schema: {
		                                model: {
		                                    no: "no",
		                                    fields: { 
		                                    	name: {editable: false},
		                                    	no:{editable: false},
		                                    }  
		                                },
		                            },
						            height: 300,
						            sortable: false,
						            selectable:true,
						            pageable: {
						            	refresh: false,
						                pageSizes: false,
										messages: fsn.gridPageMessage(),
						            },
						            toolbar: [
									          {template: kendo.template($("#CONTACT_DETAIL").html())}
									         ],
						            columns: [
						                      {field: "no", title: "商品编号", width:"20%"},
						                      {field: "name", title: "商品名称", width:"auto"},
						                      {field: "unitPrice", title: "商品单价", width:"auto"},
						                      {field: "outNumber", title: "出货数量", width:"auto"},
						                      {field: "unit.name", title: "商品单位", width:"auto"},
						                      {field: "category.name", title: "商品分类", width:"auto"},
						                      {field: "batchNo", title: "商品批次", width:"auto"},
						                      {field: "totalAmount", title: "总金额", width:"auto"}
						                      ]
						        });
		outofgood.productGrid = $("#OUTOFGOODS_INFO_GRID").kendoGrid({
            dataSource:new kendo.data.DataSource({
     	                             data:[],
     	                             batch : true,
                                   	 page:1,
						             pageSize: 5,
						             serverPaging : true,
						             serverFiltering : true,
						             serverSorting : true
          							 }),
            height: 300,
            scrollable: true,
            sortable: true,
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
                      {field: "no", title: "商品编号", width:"170px"},
                      {field: "name", title: "商品名称", width:"100px"},
                      {field: "specification", title: "商品规格", width:"100px"},
                      {field: "batchNo", title: "批次", width:"100px"},
                      {field: "mode", title: "样品否", width:"100px",template:'#=(mode?"是":"否")#'},
                      {field: "hasInspectionReport", title: "是否有质检报告", width:"150px",template:'#=(hasInspectionReport?"是":"否")#'},
                      {field: "inspectionReport", title: "是否需要质检报告", width:"150px",template:'#=(inspectionReport?"是":"否")#'},
                      {field: "outNumber", title: "出货数量", width:"100px"},
                      {field: "unit.name", title: "单位", width:"100px"},
                      {field: "firstStorage.name", title: "首选仓库", width:"100px"},
                      {field: "category.name", title: "商品分类", width:"100px"},
                      {field: "type.name", title: "商品类型", width:"100px"},
                      {field: "moneyType", title: "币种", width:"100px"},
                      {field: "unitPrice", title: "单价", width:"100px"},
                      {field: "totalAmount", title: "单笔总金额", width:"120px"},
                      {field: "note", title: "备注", width:"100px"},
                      ]
        });
		$("#CONTACT_INFO_GRID .k-grid-content").attr("style","height:190px;");
		$("#OUTOFGOODS_INFO_GRID").attr("style","width:800px;height:300px;");
		
		outofgood.contactWinMerchandise = $("#outofgood").kendoWindow({
			width:1200,
			title:"商品信息",
			visible:false,
			modal:true
		});
		
		outofgood.verifyOutNum = eWidget.verificationNum("out_num").data("kendoNumericTextBox");
		outofgood.verifyOutNum.bind("change",function(){
			var value = this.value();
			var goodNo = outofgood.contactComboBoxProduct.dataItem();
			var goods_batch = outofgood.ComboBoxGetProductBatchNo.value();
			var storage = outofgood.contactComboBoxStorage.value();
			if(typeof(goodNo)=="undefined"){
				fsn.initNotificationMes("请先选择商品", false);
				this.value("");
			}else if(goods_batch == ""||goods_batch == "请选择..."){
				fsn.initNotificationMes("请先选择批次", false);
				this.value("");
			}else if(storage == ""){
				fsn.initNotificationMes("请先选择仓库", false);
				this.value("");
			}else{
				$.ajax({
	    			url:fsn.getHttpPrefix() +"/erp/outOfGoods/JudgeStorage?value="+value+"&productId="+goodNo.id+"&batch="+goods_batch+"&storage="+storage,
	    			type:"GET",
	    			dataType:"json",
	    			contentType: "application/json; charset=utf-8",
	    			async:false,
	    			success:function(data){
	    				console.log(data);
	    				if(parseInt($("#out_num").val()) > parseInt($("#num").val())){
	    					fsn.initNotificationMes("出货数量大于库存数量", false);
	    					outofgood.verifyOutNum.value("");
	    				}
	    			}
	    		})
			}
		})
		outofgood.verifyPrice = eWidget.verificationNum("unit_price").data("kendoNumericTextBox");
		$("#contactman").kendoComboBox();
		outofgood.ComboBoxGetProductBatchNo = $("#goods_batch").kendoComboBox().data("kendoComboBox");
		outofgood.contactComboBoxStorage = $("#first_storage_id").kendoComboBox().data("kendoComboBox");
		//获取单别信息
		outofgood.dropListOrderType = eWidget.getComboBoxOrderType("no",outofgood.SIMPLE_TYPE,"销售模块","出货单/发货单管理").data("kendoDropDownList");
		outofgood.contactComboBox = eWidget.getCustomerComboBox("type",2).data("kendoComboBox");
		outofgood.contactComboBox.bind("change", combobox_change);
		outofgood.contactComboBox.setOptions({
			change: function(e) {
				outofgood.comboBoxType = {
						id:this.value(),
						name:this.text()
				};
				outofgood.changeCustomer();//重选客户的时候，清除联系人信息
				outofgood.contactComboBoxContact = eWidget.getComboBoxContact("contactman",outofgood.SIMPLE_TYPE,outofgood.comboBoxType.id).data("kendoComboBox");
				outofgood.contactComboBoxContact.bind("change", combobox_change);
				outofgood.contactComboBoxContact.setOptions({
					change: function(e) {
						outofgood.comboBoxContactMan = {
							id: this.value(),
							name: this.text()
						};
						$("#cprovince").val(outofgood.contactComboBoxContact.dataItem().province);
						$("#ccity").val(outofgood.contactComboBoxContact.dataItem().city);
						$("#carea").val(outofgood.contactComboBoxContact.dataItem().area);
						$("#zip_code").val(outofgood.contactComboBoxContact.dataItem().zipcode);
						$("#address").val(outofgood.contactComboBoxContact.dataItem().addr);
						$("#tel").val(outofgood.contactComboBoxContact.dataItem().tel_1);
					}
				});
			  }
		});
		
		this.initContactInfoButtons();
		
		this.initDailogAndInfo();
	}
	
	outofgood.changeCustomer = function() {
		$("#contactman").data( "kendoComboBox" ).value("");
		$("#cprovince").val("");
		$("#ccity").val("");
		$("#carea").val("");
		$("#zip_code").val("");
		$("#address").val("");
		$("#tel").val("");
	}
	
	outofgood.initDailogAndInfo = function(){
		
		outofgood.contactWin = $("#CONTACT_INFO_WIN").kendoWindow({
			width:600,
			title:"商品信息",
			visible:false,
			modal:true
		});
		outofgood.contactComboBoxProduct = eWidget.getCommonComboBox("goods_no","/initializeProduct/outlistall","product.barcode", "id").data("kendoComboBox");
		outofgood.contactComboBoxProduct.bind("change", combobox_change);
		outofgood.contactComboBoxProduct.setOptions({
			change: function(e) {
				outofgood.ComboBoxGetProductBatchNo = eWidget.getCommonComboBox("goods_batch","/merchandiseStorage/getBatchNum?productId="+this.value(),"batch_number","instanceId").data("kendoComboBox");
				outofgood.ComboBoxGetProductBatchNo.setOptions({
					change:function(e){
						ButchChange(e);
					}
				});
				outofgood.contactComboBoxStorage = eWidget.getCommonComboBox("first_storage_id","/merchandiseStorage/getStorage?productId="+this.value(),"name","no").data("kendoComboBox");		
				outofgood.contactComboBoxStorage.bind("change", StorageChange);
				$("#goods_name").val(outofgood.contactComboBoxProduct.dataItem().product.name);
				$("#goods_specification").val(outofgood.contactComboBoxProduct.dataItem().product.format);
				$("#unit_id").data("kendoComboBox" ).value(outofgood.contactComboBoxProduct.dataItem().product.unit.name);
				$("#category_id").data("kendoComboBox").setDataSource([{
					id: outofgood.contactComboBoxProduct.dataItem().product.category.id,
					name: outofgood.contactComboBoxProduct.dataItem().product.category.name
				}]);
				$("#category_id").data("kendoComboBox" ).value(outofgood.contactComboBoxProduct.dataItem().product.category.id);
				$("#type_id").data("kendoComboBox" ).value(outofgood.contactComboBoxProduct.dataItem().type.id);
			}
		});
	}
	
	outofgood.initContactInfoButtons = function(){
		$("#add_").bind("click",function(){
			outofgood.addContact();
		});
		$("#update_").bind("click",function(){
			outofgood.updateContact();
		});
		$("#delete_").bind("click",function(){
			if(outofgood.contactGrid.data("kendoGrid").select().length > 0 ){
				var win = eWidget.getPromptWindow();
				win.data("kendoWindow").open().center();
				$("#confirmWindow").unbind("click");
				$("#cancelWindow").unbind("click");
				$("#confirmWindow").bind("click",function(){
					outofgood.deleteContact();
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
	}
	
	outofgood.addContact = function(){
		
		$("#goods_no").data("kendoComboBox").readonly(false);
		$("#no").removeAttr("disabled");
		outofgood.cleanContactForm();
		outofgood.contactWin.data("kendoWindow").open().center();
		
		var oriLength = outofgood.contactGrid.data("kendoGrid").dataSource.data().length;
		
		$("#confirm_").unbind("click");
		$("#cancel_").unbind("click");
		
		$("#confirm_").bind("click",function(){
			
			var flag = outofgood.verifyGoods();
			if(flag) {
				var value = outofgood.verifyOutNum.value();
				var goodNo = outofgood.contactComboBoxProduct.dataItem().id;
				var goods_batch = outofgood.ComboBoxGetProductBatchNo.text();
				var storage = outofgood.contactComboBoxStorage.value();
				if(goodNo == ""){
					fsn.initNotificationMes("请先选择商品", false);
				}else if(goods_batch == ""||goods_batch == "请选择..."){
					fsn.initNotificationMes("请先选择批次", false);
				}else if(storage == ""){
					fsn.initNotificationMes("请先选择仓库", false);
				}else{
					$.ajax({
		    			url:fsn.getHttpPrefix() +"/erp/outOfGoods/JudgeStorage?value="+value+"&productId="+goodNo+"&batch="+goods_batch+"&storage="+storage,
		    			type:"GET",
		    			dataType:"json",
		    			contentType: "application/json; charset=utf-8",
		    			async:false,
		    			success:function(data){
		    				if(data.flag==false){
		    					fsn.initNotificationMes("出货数量大于库存数量", false);
		    					outofgood.verifyOutNum.value("");
		    				}else{
		    					outofgood.saveConfirm(true);
		    					fsn.initNotificationMes("保存成功",true);
		    					var nowLength = outofgood.contactGrid.data("kendoGrid").dataSource.data().length;
		    					if(oriLength != nowLength) {
		    						outofgood.checkFlag = true;
		    					}
		    					outofgood.contactWin.data("kendoWindow").close();
		    				}
		    			}
		    		})
				}
			}
		});
		
		$("#cancel_").bind("click",function(){
			outofgood.contactWin.data("kendoWindow").close();
		})
	};
	
	
	outofgood.updateContact = function(){
		$("#goods_no").data("kendoComboBox").readonly(true);
		
		if(outofgood.contactGrid.data("kendoGrid").select().length > 0 ){
			
			var contactItem = outofgood.selectedContact = outofgood.contactGrid.data("kendoGrid").dataItem(outofgood.contactGrid.data("kendoGrid").select());
			
			outofgood.bindContactForm(contactItem);
			
			outofgood.contactWin.data("kendoWindow").open().center();
			
			$("#confirm_").unbind("click");
			$("#cancel_").unbind("click");
			
			$("#confirm_").bind("click",function(){
				var cs = $("#first_storage_id").data("kendoComboBox").dataItem();
				var csTem = {
					no: cs.no,
					name: cs.name
				};
				var cu = $("#unit_id").data("kendoComboBox").dataItem();
				var cuTem = {
					id: cu.id,
					name: cu.name
				};
				var ct = $("#type_id").data("kendoComboBox");
				var ctTem = {
					id: ct.value(),
					name: ct.text()
				};
				var cc = $("#category_id").data("kendoComboBox");
				var ccTem = {
					id: cc.value(),
					name: cc.text()
				};
				var batch = $("#goods_batch").data("kendoComboBox");
				var batchTem = {
					batch_number:batch.text()
				};
				var flag = outofgood.checkDiff(csTem,cuTem,ctTem,ccTem,batchTem);
				if(flag){
					outofgood.saveConfirm(false);
					fsn.initNotificationMes("修改成功",true);
					outofgood.contactWin.data("kendoWindow").close();
				}else{
					fsn.initNotificationMes("未修改,不能保存",false);
				}
				
			});
			
			$("#cancel_").bind("click",function(){
				outofgood.contactWin.data("kendoWindow").close();
			})
		}else{
			fsn.initNotificationMes("请选择一条记录！",false);
		}
	}
	
	outofgood.deleteContact = function(){
		if(outofgood.contactGrid.data("kendoGrid").select().length > 0 ){
			var oriLength = outofgood.contactGrid.data("kendoGrid").dataSource.data().length;
			var contactItem = outofgood.contactGrid.data("kendoGrid").dataItem(outofgood.contactGrid.data("kendoGrid").select());
			outofgood.contactGrid.data("kendoGrid").dataSource.remove(contactItem);
			var nowLength = outofgood.contactGrid.data("kendoGrid").dataSource.data().length;
			if(oriLength != nowLength) {
				outofgood.checkFlag = true;
			}
		}
	}
	
	outofgood.saveConfirm = function(isNew){
		var contact_validator = $("#CONTACT_INFO_WIN").kendoValidator().data("kendoValidator");

		if(contact_validator.validate()){
			var model = {};
			
			var cs = $("#first_storage_id").data("kendoComboBox").dataItem();
			var csTem = {
				no: cs.no,
				name: cs.name
			};
			var cu = $("#unit_id").data("kendoComboBox").dataItem();
			var cuTem = {
				id: cu.id,
				name: cu.name
			};
			var ct = $("#type_id").data("kendoComboBox").dataItem();
			var ctTem = {
				id: ct.id,
				name: ct.name
			};
			var cc = $("#category_id").data("kendoComboBox").dataItem();
			var ccTem = {
				id: cc.id,
				name: cc.name
							};
			var batch = $("#goods_batch").data("kendoComboBox");
			var batchTem = {
				batch_number:batch.text()
			};
			if(isNew){
				model = {
					no: $("#goods_no").data("kendoComboBox").dataItem().product.barcode,
					name: $("#goods_name").val().trim(),
					batchNo: batchTem.batch_number,
					outNumber: $("#out_num").val().trim() != "" ? $("#out_num").val().trim() : 0,
//					realOutNumber: $("#real_num").val().trim() != "" ? $("#real_num").val().trim() : 0,
					firstStorage: csTem,
					unit: cuTem,
					category: ccTem,
					type: ctTem,
					specification: $("#goods_specification").val().trim(),
					totalAmount: ($("#out_num").val().trim() != "" ? $("#out_num").val().trim() : 0) * ($("#unit_price").val().trim() != "" ? $("#unit_price").val().trim() : 0),
					unitPrice: $("#unit_price").val().trim() != "" ? $("#unit_price").val().trim() : 0,
					note: $("#goods_note").val().trim(),
					moneyType: $("#money_type").val().trim(),
					mode: $('input:radio:checked').val().trim(),
					inspectionReport: $("input[name='inspectionReport']:checked").val().trim(),
					hasInspectionReport: $("input[name='has_inspectionReport']:checked").val().trim(),
				}
			}else{
				if(outofgood.selectedContact.id){
					model.id = outofgood.selectedContact.id;
				}
			}
			
			if(isNew){
				outofgood.contactGrid.data("kendoGrid").dataSource.add(model);
				
				var totalPrice = 0;
				var a = outofgood.contactGrid.data("kendoGrid").dataSource.data();
				for(var i = 0; i < a.length; i++) {
					totalPrice = totalPrice + a[i].totalAmount;
				}
				$("#total_price").val(totalPrice);
			}else{
				
				model = {
						no: $("#goods_no").data("kendoComboBox").dataItem().product.barcode,
						name: $("#goods_name").val().trim(),
						batchNo: batchTem.batch_number,
						outNumber: $("#out_num").val().trim() != "" ? $("#out_num").val().trim() : 0,
						firstStorage: csTem,
						unit: cuTem,
						category: ccTem,
						type: ctTem,
						specification: $("#goods_specification").val().trim(),
						totalAmount: ($("#out_num").val().trim() != "" ? $("#out_num").val().trim() : 0) * ($("#unit_price").val().trim() != "" ? $("#unit_price").val().trim() : 0),
						unitPrice: $("#unit_price").val().trim() != "" ? $("#unit_price").val().trim() : 0,
						note: $("#goods_note").val().trim(),
						moneyType: $("#money_type").val().trim(),
						mode: $("input[name='goods_mode']:checked").val().trim(),
						inspectionReport: $("input[name='inspectionReport']:checked").val().trim(),
						hasInspectionReport: $("input[name='has_inspectionReport']:checked").val().trim(),
					}
				if(outofgood.selectedContact.id){
					model.id = outofgood.selectedContact.id;
				}
				outofgood.contactGrid.data("kendoGrid").dataSource.remove(outofgood.selectedContact);
				outofgood.checkDiff(csTem, cuTem, ctTem, ccTem,batchTem);//更新的时候检查
				outofgood.contactGrid.data("kendoGrid").dataSource.add(model);
				var totalPrice = 0;
				var a = outofgood.contactGrid.data("kendoGrid").dataSource.data();
				for(var i = 0; i < a.length; i++) {
					totalPrice = totalPrice + a[i].totalAmount;
				}
				$("#total_price").val(totalPrice);
			}
		}
	};
	
	outofgood.cleanContactForm = function(){
		$("#num").val("");
		$("#goods_no").data("kendoComboBox" ).value("");
		$("#goods_name").val("");
		outofgood.ComboBoxGetProductBatchNo.value("");
		outofgood.verifyOutNum.value("");
		outofgood.verifyPrice.value("");
		outofgood.contactComboBoxStorage.value("");
		$("#unit_id").data("kendoComboBox" ).value("");
		$("#category_id").data("kendoComboBox" ).value("");
		$("#type_id").data("kendoComboBox" ).value("");
		$("#goods_specification").val("");
		$("#total_amount").val("");
		$("#unit_price").val("");
		$("#goods_note").val("");
		$("#money_type").val("RMB");
		document.getElementById("goods_mode1").checked=true;
		document.getElementById("inspectionReport1").checked=true;
	};
	
	outofgood.bindContactForm = function(item){
		var ds = $("#goods_no").data("kendoComboBox").dataSource;
		for(var i=0;i<ds._data.length;i++){
			if(ds._data[i].product.barcode==item.no){
				$("#goods_no").data("kendoComboBox").value(ds._data[i].id);
			}
		}
		$("#goods_name").val(item.name);
		$("#goods_batch").data("kendoComboBox").text(item.batchNo);
		outofgood.verifyOutNum.value(item.outNumber);
		outofgood.contactComboBoxStorage.value(item.firstStorage.no);
		eWidget.getCommonComboBox("first_storage_id","/merchandiseStorage/getStorageBybarcode?barcode="+item.no,"name","no").data("kendoComboBox");
		outofgood.contactComboBoxStorage.value(item.firstStorage.no);
		$.ajax({
				url: fsn.getHttpPrefix() + "/erp/merchandiseStorage/getStorageNumByBatchAndStorage?batch="+item.batchNo+"&&storageId="+item.firstStorage.no+"&&barcode="+item.no,
				type:"GET",
				dataType:"json",
				async:false,
				contentType: "application/json; charset=utf-8",
				success:function(data){
					console.log(data);
				if(data){
					$("#num").val(data.result.count);
				}else{
					$("#num").val("");
				}
			}
})
		$("#unit_id").data("kendoComboBox").value(item.unit.id);
		$("#category_id").data("kendoComboBox").value(item.category.id);
		$("#type_id").data("kendoComboBox").value(item.type.id);
		$("#goods_specification").val(item.specification);
		$("#total_amount").val(item.totalAmount);
		outofgood.verifyPrice.value(item.unitPrice);
		$("#goods_note").val(item.note);
		$("#money_type").val(item.moneyType);
		
		if(item.inspectionReport=="true") {
			document.getElementById("inspectionReport").checked=true;
		} else {
			document.getElementById("inspectionReport1").checked=true;
		}
		
		if(item.hasInspectionReport=="true") {
			document.getElementById("has_inspectionReport").checked=true;
		} else {
			document.getElementById("has_inspectionReport1").checked=true;
		}
		
		if(item.mode=="true") {
			document.getElementById("goods_mode").checked=true;
		} else {
			document.getElementById("goods_mode1").checked=true;
		}
	};
	
	outofgood.verifyGoods = function() {
		if($("#goods_no").data("kendoComboBox" ).value() == "") {
			fsn.initNotificationMes("请填写商品编号！",false);
			$("#goods_no").focus();
			return;
		}
		if($("#goods_name").val().trim() == "") {
			fsn.initNotificationMes("请填写商品名称！",false);
			$("#goods_name").focus();
			return;
		}
		if(outofgood.ComboBoxGetProductBatchNo.text == ""||outofgood.ComboBoxGetProductBatchNo.text == "请选择...") {
			fsn.initNotificationMes("请填写商品批次号！",false);
			$("#goods_batch").focus();
			return false;
		}
		if($("#out_num").val().trim() == "" || $("#out_num").val().trim() <= 0) {
			fsn.initNotificationMes("请填写正确的出货数量！",false);
			$("#out_num").focus();
			return false;
		}
		if($("#unit_price").val().trim() == "" || $("#unit_price").val().trim() <= 0) {
			fsn.initNotificationMes("请填写商品单价！",false);
			$("#unit_price").focus();
			return false;
		}
		return true;
	};
	
	outofgood.verifyBill = function() {
		if($("#no").data("kendoDropDownList").text() == ""||$("#no").data("kendoDropDownList").text() == "请选择单别") {
			fsn.initNotificationMes("请填写单别号！",false);
			$("#no").focus();
			return false;
		}
		if($("#type").data("kendoComboBox" ).value() == "") {
			fsn.initNotificationMes("请填写客户！",false);
			$("#type").focus();
			return false;
		}
		if($("#contactman").data("kendoComboBox" ).value() == "") {
			fsn.initNotificationMes("请填写联系人！",false);
			$("#contactman").focus();
			return false;
		}
		if($("#cprovince").val() == "") {
			fsn.initNotificationMes("请填写联系人省！",false);
			$("#cprovince").focus();
			return false;
		}
		if($("#ccity").val() == "") {
			fsn.initNotificationMes("请填写联系人市！",false);
			$("#ccity").focus();
			return false;
		}
		if($("#carea").val() == "") {
			fsn.initNotificationMes("请填写联系人区！",false);
			$("#carea").focus();
			return false;
		}
		if($("#address").val().trim() == "") {
			fsn.initNotificationMes("请填写联系人详细地址！",false);
			$("#address").focus();
			return false;
		}
		return true;
	};
	
	outofgood.detailOutOfGoodsInfo = function(Item){
		$("#detail_label_num_content").html(Item.outOfBillNo);
		$("#detail_label_source_content").html(Item.source);
		$("#detail_label_customer_content").html(Item.customer.name);
		$("#detail_label_sourceno_content").html(Item.re_outofbill_num);
		$("#detail_label_contactman_content").html(Item.contactInfo.name);
		$("#detail_label_caddress_content").html(Item.contactProvince+Item.contactCity+Item.contactArea+Item.contactAddr);
		$("#detail_label_contacttel_content").html(Item.contactTel);
		$("#detail_label_ctime_content").html((new Date(Item.outDate)).format("YYYY-MM-dd"));
		$("#detail_label_piecetop_content").html(Item.invoice);
		$("#detail_lable_code_content").html(Item.contactZipcode);
		$("#detail_label_totalp_content").html(Item.totalPrice);
		$("#detail_label_transport_content").html(Item.transportation);
		$("#detail_lable_base_remarks_content").html(Item.note);
		$("#detail_label_state_content").html(Item.outOfBillState);
	};
	
	
	outofgood.GRIDID = "Simple_Grid";
	outofgood.DAILOGID = "OPERATION_WIN"; 
	outofgood.initailize = function(){
		this.initComponent(outofgood);
		$("#label_no").html(outofgood.SIMPLE_MODEL_NAME + "编号");
		$("#label_category").html(outofgood.SIMPLE_MODEL_NAME + "类型");
		$("#label_description").html(outofgood.SIMPLE_MODEL_NAME + "描述");
		
		this.initRequiredComponents();
	};
	outofgood.initailize();
	outofgood.judgeIsUsed = function(){
		return true;
	};

	
	//检查商品
	outofgood.checkFlag = false;
	outofgood.checkDiff = function(csTem, cuTem, ctTem, ccTem,batchTem) {
		if(outofgood.selectedContact.name != $("#goods_name").val().trim() ||
				outofgood.selectedContact.batchNo.text != batchTem.batch_number ||
				"" + outofgood.selectedContact.mode + "" != $("input[name='goods_mode']:checked").val().trim() ||
				outofgood.selectedContact.outNumber != $("#out_num").val().trim() ||
				"" + outofgood.selectedContact.inspectionReport + "" != $("input[name='inspectionReport']:checked").val().trim() ||
				outofgood.selectedContact.unitPrice != $("#unit_price").val().trim() ||
				"" + outofgood.selectedContact.hasInspectionReport + "" != $("input[name='has_inspectionReport']:checked").val().trim() ||
				outofgood.selectedContact.firstStorage.name != csTem.name ||	
				outofgood.selectedContact.unit.name != cuTem.name ||
				outofgood.selectedContact.type.name != ctTem.name ||
				outofgood.selectedContact.category.name != ccTem.name ||
				outofgood.selectedContact.note != $("#goods_note").val().trim() ||
				outofgood.selectedContact.moneyType != $("#money_type").val().trim()) {
			outofgood.checkFlag = true;
		}else{
			outofgood.checkFlag = false;
		}
		return outofgood.checkFlag;
	};
	
		
//	$('#search').kendoSearchBox({
//				change : function(e) {
//					var keywords=e.sender.options.value;
//			    	if(keywords.trim()!=""){
//			    		$("#Simple_Grid").data("kendoGrid").setDataSource(initStandard(keywords));
//			    	    $("#Simple_Grid").data("kendoGrid").refresh();
//			    	}else{
//			    		$("#Simple_Grid").data("kendoGrid").setDataSource(outofgood.datasource);
//			    		$("#Simple_Grid").data("kendoGrid").refresh();
//			    	}
//				}
//			});
			
	initStandard = function(keywords){
		var ds = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
                url : function(options){
                	if(options.filter){
                		var configure = filter.configure(options.filter);
                 		return fsn.getHttpPrefix() + "/erp/outOfGoods/reglist/" + options.page + "/" + options.pageSize + "/"+configure;;
                	};//filter zl
					return fsn.getHttpPrefix() + "/erp/outOfGoods/search?keywords="+keywords;
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
        			 outOfBillNo:{
        				 editable : false,
        			 },
        			 outDate:{type:"string"},
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
	return ds;
  };
});
