$(function(){
	var fsn = window.fsn = window.fsn || {};
	var eWidget = fsn.eWidget = fsn.eWidget||{};
	var common = fsn.common = fsn.common || {};
	
	var st_customer = fsn.st_customer = fsn.st_customer || {};
	
	$.extend(st_customer,common);
	
	st_customer.datasource = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
				url : function(options){
                	if(options.filter){
                		var configure = filter.configure(options.filter);
                 		return fsn.getHttpPrefix() + "/erp/customer/" + st_customer.SIMPLE_TYPE + "/reglist/" + options.page + "/" + options.pageSize + "/"+configure;;
                	};
                	return fsn.getHttpPrefix() + "/erp/customer/" + st_customer.SIMPLE_TYPE + "/list";
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
        			id:{type:"number"},
                 	name:{type:"string"},
                 	diyType:{
                 		id:{type:"number"},
                     	name:{type:"string"}
                 	},
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
	
	//初始化联系人,根据客户、供应商id
	st_customer.initBusnessItemDS = function(busnessID){
    	if(busnessID){
    		st_customer.busnessItemID = busnessID;
    	}else{
    		st_customer.busnessItemID = 0;
    	}
    	st_customer.busnessItemFirstPageFlag = 1;
    	st_customer.busnessItemDS = new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){
	            		if(st_customer.busnessItemID == 0){
	            			return;
	            		}
	            		if(st_customer.busnessItemFirstPageFlag == 1){  //每次预览新的报告的时候，都从第一页开始显示
	            			options.page=1;
	            			options.pageSize=5;
	            			st_customer.busnessItemFirstPageFlag=0;
	            		}
	            		return fsn.getHttpPrefix() + "/erp/customer/" + st_customer.SIMPLE_TYPE + "/findContact/" + options.page + "/" + options.pageSize + "/" + st_customer.busnessItemID;
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
	
	st_customer.columns = [
               {field: "id", title: st_customer.SIMPLE_MODEL_NAME + "ID", width:"20px",editor:false, editable: true},
               {field: "name", title: st_customer.SIMPLE_MODEL_NAME + "名称", width:"60px"},
               {field: "note", title: "备注", width:"80px"},
               {width:"60px",title:"操作",
				      template:function(e){
						var tag="<a onclick='return fsn.st_customer.views(" + e.id + ",\"" + e.name + "\",\"" + (e.license?e.license.licenseNo:"") + "\",\"" + e.diyType.name + "\",\"" + e.note + "\")' " +
								"class='k-button k-button-icontext k-grid-ViewDetail'><span class='k-icon k-edit'> </span>预览</a>";
						tag = tag + 
								"<a onclick='return fsn.st_customer.viewbusinfo(" + e.organization + ")' class='k-button k-button-icontext k-grid-Backout'>" +
								"查看企业信息</a>";
						
						/**
						 * 2代表客户，3代表经销商
						 */
					    if (st_customer.SIMPLE_TYPE == 3) {
							tag = tag  +
							"<a onclick='return fsn.st_customer.viewproinfo(" + e.organization + "," + e.id + ")' class='k-button k-button-icontext k-grid-Backout'>" +
									"查看供销产品</a>";
						}
						return tag;
					}
			    }];
	
	/**
	 * 预览
	 * @author Zhanghui 2015/4/10
	 */
	st_customer.views = function(id, name, licenseno, diyTypeName, note){
	   st_customer.initBusnessItemDS(id);
  	   $("#MERCHANDISE_INFO_GRID").data("kendoGrid").setDataSource(st_customer.busnessItemDS);
  	   st_customer.PorCDetail(id, name, licenseno, diyTypeName, note);
  	   var addWin = eWidget.getWindow("PROVIDERORCUSTOMER_DETAIL_INFO","详细信息",common.SIMPLE_MODEL_NAME, common.realObj.win_width == null ? null:common.realObj.win_width);
  	   addWin.data("kendoWindow").open().center();
	};
	
	/**
	 * 预览企业信息
	 * @author Zhanghui 2015/4/10
	 */
	st_customer.viewbusinfo = function(organization){
		if(!organization || organization==""){
 		   fsn.initNotificationMes("该企业未注册！", false);
 		   return;
 	   }
 	   /* 缓存供应商组织机构  */
 	   var business = {
		   	organization : organization
	   };
	   $.cookie("view_0_business", JSON.stringify(business), {
		    	path : '/'
	   });
	   /* 打开企业信息预览界面 */ 
	   window.open('/fsn-core/ui/portal/unit-liutong');
	};
	
	/**
	 * 查看供销产品
	 * @author Zhanghui 2015/4/10
	 */
	st_customer.viewproinfo = function(organization, id){
	   if(!organization || organization==""){
 		   fsn.initNotificationMes("该企业未注册！", false);
 		   return;
 	   }
	   /* 缓存供应商企业id */ 
 	   var frombusiness = {
		   id : id
	   };
	   $.cookie("view_0_product_by_fromBusId_and_toBusId", JSON.stringify(frombusiness), {
		    	path : '/'
	   });
	   /* 打开供销产品界面 */
	   window.open('/fsn-core/views/product/manage_product_bussiness_super.html');
	};
	
	/**
	 * add, update, delete
	 */
	
	st_customer.win_width = 800;
	
	st_customer.selectedData = {};
	
	st_customer.clearForm = function(){
		$("#license").kendoValidator().data("kendoValidator").hideMessages();
		$("#no").val("");
		$("#no").removeAttr("disabled");
		$("#name").val("");
		$("#name").removeAttr("readonly");
		$("#license").val("");
		$("#note").val("");
		$("#name").val("");
		$("#orgid").val("");
		$("#type").data("kendoComboBox").value("");
		

//		fsn.st_customer.contactComboBox.value("");

		st_customer.contactGrid.data("kendoGrid").dataSource.data([]);
	}
	
	st_customer.bindForm = function(){
		$("#id").attr("disabled","disabled");
		$("[data-bind='value:id']").val(st_customer.selectedData.id);
		st_customer.businessUnitComboBox.readonly(true);
		$("#name").val(st_customer.selectedData.name);
		$("[data-bind='value:license']").val(st_customer.selectedData.license==null?"":st_customer.selectedData.license.licenseNo);
		var vnote = st_customer.selectedData;
		$("#note").val(vnote == null ? "" vnote);
		st_customer.contactComboBox = eWidget.getComboBox("type",st_customer.SIMPLE_TYPE).data("kendoComboBox");
		st_customer.contactComboBox.value(st_customer.selectedData.diyType.id);
		//st_customer.contactComboBox.text(st_customer.selectedData.diyType.name);
		st_customer.initBusnessItemDS(st_customer.selectedData.id);
		$("#CONTACT_INFO_GRID").data("kendoGrid").setDataSource(st_customer.busnessItemDS);
	//	st_customer.contactGrid.data("kendoGrid").dataSource.data(st_customer.selectedData.contacts);
		$("#license").attr("readonly",true);
		$("#license").kendoValidator().data("kendoValidator").validate();
	}
	st_customer.flag = false;
	
	st_customer.windowSaveConfrim = function(){
		//validation
		
		st_customer.flag = st_customer.verifyCustomer();
		
		if(st_customer.flag&&st_customer.validator.validate()){
			st_customer.checkFlag = false;
			st_customer.flag = false;
			//fsn.showNotificationMes("Sucess",true);
			var combobox = $("#type").data("kendoComboBox");
			var dataItem = combobox.dataItem();
			var typeTem = {
				id:dataItem.id,
				name:dataItem.name
			};

			var model = {
			};
			
			var contacts = st_customer.contactGrid.data("kendoGrid").dataSource.data();
			if(contacts.length==0){
				fsn.initNotificationMes("没有添加联系人！</br>新增失败！", false);
				return;
			}
			if(st_customer.SIMPLE_TYPE == 2){
				model.businessUnit = {
						name:$("#name").val().trim(),
						license:{licenseNo:$("#license").val().trim()},
						diyType:typeTem,
						note:$("#note").val().trim(),
						contacts:contacts
				};
			}else if(st_customer.SIMPLE_TYPE == 3){
				model.businessUnit = {
						name:$("#name").val().trim(),
						license:{licenseNo:$("#license").val().trim()},
						diyType:typeTem,
						note:$("#note").val().trim(),
						contacts:contacts
				};
			}
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/customer/" + st_customer.SIMPLE_TYPE,
				type:"POST",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				data:JSON.stringify(model),
				success:function(data){
					if(data.result&&data.result.status=="true"){
						fsn.initNotificationMes("新增成功！", true);
						$("#OPERATION_WIN").data("kendoWindow").close();
					}else{
						fsn.initNotificationMes(data.result.errorMessage, false);
					}
					st_customer.datasource.read();
					
				}
			});
		}else{
			st_customer.flag = true;
			//fsn.initNotificationMes("Failure",false);
		}
	}
	
	/**
	 * 加载客户名称的dataSource
	 */
    st_customer.nameComboxDs = function(page, keyword){
		st_customer.comboxPage = page;
	    var nameComboxDsUrl = "/customer/" + st_customer.SIMPLE_TYPE + "/getListBusinessUnit?page="+st_customer.comboxPage+"&keyword="+keyword;
        var listDs = [];
        $.ajax({
            async: false,
            url: fsn.getHttpPrefix() +"/erp"+ nameComboxDsUrl,
            type: "GET",
            success: function(returnVal){
                listDs = returnVal.result;
            }
        });
        return listDs;
    };

	$("#name").kendoAutoComplete({
            dataSource: [],
			filter: "contains",
			dataTextField: "name",
            placeholder: "请输入关键字后按空格...",
            dataBound: function() {
				var keyword = $("#name").data("kendoAutoComplete").value().trim();
                if (st_customer.keyword == keyword) {
                    return;
                }
                st_customer.keyword = keyword;
                /*关键字被更改后，将page设置为1*/
                st_customer.comboxPage = 1;
                var listItemDs = st_customer.nameComboxDs(st_customer.comboxPage, st_customer.keyword);
                $("#name").data("kendoAutoComplete").setDataSource(listItemDs);
                $("#name").data("kendoAutoComplete").search(st_customer.keyword);
            },
			open:function(e){
				$("#name_listbox").scroll(function() {
        			$("#name_listbox").scroll(function() {
						st_customer.appendComboxDs(this,st_customer.comboxPage,st_customer.keyword);
			        });
				});
			},
        });
		
		/**
		 * 分页查询客户名称的dataSource
		 */
		st_customer.appendComboxDs = function(listBoxDom,page,keyword){
			var scrollTop = $(listBoxDom).scrollTop();
		 	var windowHeight = $(listBoxDom).height();
		 	var listLi = $("#name_listbox li");
		 	var scrollHeight = 0;
		 	for(var i=0;i<listLi.length;i++){
			 	var h = listLi[i].offsetHeight;
			 	scrollHeight += h;
		 	}
		 	scrollHeight = parseInt(scrollHeight)-6;
		 	if((scrollTop+windowHeight)>scrollHeight&&st_customer.comboxPage!=-1){
				if(keyword!=""){
					st_customer.comboxPage = 1;
				}
		　　 		st_customer.comboxPage+=1;
		 		$(listBoxDom).scrollTop(scrollTop-50);
		 		var listDs = st_customer.nameComboxDs(st_customer.comboxPage,keyword);
		 		if(listDs==null) return;
		 		if(listDs.length<1) st_customer.comboxPage = -1;
		 		for(var j=0;j<listDs.length;j++){
		 			$("#name").data("kendoAutoComplete").dataSource.add(listDs[j]);
		 		}
		 		//$("#name").data("kendoAutoComplete").refresh();
		 	}
		}
	
	st_customer.windowUpdateConfrim = function(){
		//validation
		if(st_customer.validator.validate()){
			if(st_customer.selectedData.name != $("#name").val().trim()|| 
					st_customer.selectedData.license.licenseNo != $("#license").val().trim() ||
					st_customer.selectedData.diyType.name != $("#type").data("kendoComboBox").dataItem().name ||
					st_customer.selectedData.note != $("#note").val().trim() ||
					st_customer.checkFlag) {
				//fsn.showNotificationMes("Sucess",true);
				st_customer.checkFlag = false;
				var combobox = $("#type").data("kendoComboBox");
				var dataItem = combobox.dataItem();
				var typeTem = {
					id:dataItem.id,
					name:dataItem.name
				};
				
				var model = {
				};
				
				var contacts = st_customer.contactGrid.data("kendoGrid").dataSource.data();
				if(contacts.length==0){
					fsn.initNotificationMes("联系人不能为空！</br>编辑失败！", false);
					return;
				}
					if(st_customer.SIMPLE_TYPE == 2){
						model.businessUnit = {
								id:$("#id").val().trim(),
								name:$("#name").val().trim(),
								license:{licenseNo:$("#license").val().trim()},
								diyType:typeTem,
								note:$("#note").val().trim(),
								contacts:contacts
						};
					}else if(st_customer.SIMPLE_TYPE == 3){
						model.businessUnit = {
								id:$("#id").val().trim(),
								name:$("#name").val().trim(),
								license:{licenseNo:$("#license").val().trim()},
								diyType:typeTem,
								note:$("#note").val().trim(),
								contacts:contacts
						};
					}
					$.ajax({
						url: fsn.getHttpPrefix() + "/erp/customer/" + st_customer.SIMPLE_TYPE,
						type:"PUT",
						dataType:"json",
						contentType: "application/json; charset=utf-8",
						data:JSON.stringify(model),
						success:function(data){
							if(data.result.status=="true"){
								fsn.initNotificationMes("编辑成功！", true);
								$("#OPERATION_WIN").data("kendoWindow").close();
							}else{
								fsn.initNotificationMes("编辑失败", false);
							}
							st_customer.datasource.read();
							
						}
					});
				
			}else {
				fsn.initNotificationMes("还没有修改数据",false);
			}
		}else{
		}
	}
	
	st_customer.windowDeleteConfrim = function(){
		
		if(st_customer.selectedData.id){
			var model = {
				businessUnit:{
					id:st_customer.selectedData.id
				},
			}
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/customer/" + st_customer.SIMPLE_TYPE,
				type:"DELETE",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				data:JSON.stringify(model),
				success:function(data){
					if(data && data.result.status == "true"){
						fsn.initNotificationMes("删除成功！", true);
						st_customer.datasource.remove(st_customer.grid.dataItem(st_customer.grid.select()));
					}else{
						fsn.initNotificationMes("该条信息已被使用！</br>删除失败！", false);
					}
				}
			});
		}
	}
	
	st_customer.contactGrid = null;
	st_customer.contactComboBox = null;
	st_customer.comboBoxType = {};
	st_customer.contactWin = null;
	st_customer.selectedContact = null;
	st_customer.businessUnitComboBox = null;
	st_customer.provinceCombox = null;
	st_customer.cityCombox = null;
	
	st_customer.initRequiredComponents = function(){
		st_customer.contactGrid = $("#CONTACT_INFO_GRID").kendoGrid({
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
									          {template: kendo.template($("#CONTACT_DETAIL").html())}
									         ],
						            columns: [
						                      {field: "id", title: "ID", width:"auto"},
						                      {field: "name", title: "姓名", width:"auto"},
						                      {field: "tel_1", title: "手机", width:"auto"},
						                      {field: "im_account", title: "聊天账号", width:"auto"},
						                      {field: "zipcode", title: "邮编", width:"auto"},
						                      {field: "province", title: "省", width:"auto"},
						                      {field: "city", title: "市", width:"auto"},
						                      {field: "area", title: "区", width:"auto"},
						                      {field: "addr", title: "地址", width:"auto"},
						                      ]
						        });
		st_customer.detailContactGrid = $("#MERCHANDISE_INFO_GRID").kendoGrid({
            dataSource: new kendo.data.DataSource({
     	                             data:[],
     	                             batch : true,
                                   	 page:1,
						             pageSize: 5,
						             serverPaging : false,
						             serverFiltering : false,
						             serverSorting : false
          							 }),
            height: 300,
            sortable: false,
            selectable:false,
            pageable: {
            	refresh: false,
                pageSizes: 5,
				messages: fsn.gridPageMessage(),
            },
            toolbar: [
			          {template: kendo.template($("#CONTACTS_DETAIL").html())}
			         ],
            columns: [
                      {field: "id", title: "ID", width:"100px"},
                      {field: "name", title: "姓名", width:"100px"},
                      {field: "tel_1", title: "手机", width:"150px"},
                      {field: "tel_2", title: "座机", width:"150px"},
                      {field: "zipcode", title: "邮编", width:"100px"},
                      {field: "province", title: "省", width:"100px"},
                      {field: "city", title: "市", width:"100px"},
                      {field: "area", title: "区", width:"100px"},
                      {field: "addr", title: "地址", width:"200px"},
                      {field: "im_account", title: "聊天账号", width:"200px"},
                      {field: "email", title: "电子邮箱", width:"200px"},
                      ]
        });
		$("#CONTACT_INFO_GRID .k-grid-content").attr("style","height:190px;");
		$("#MERCHANDISE_INFO_GRID").attr("style","width:800px;height:300px;");
		st_customer.contactWin = $("#CONTACT_INFO_WIN").kendoWindow({
					width:800,
					title:"管理联系人",
					visible:false,
					modal:true
				});
		
		st_customer.contactComboBox = eWidget.getComboBox("type",st_customer.SIMPLE_TYPE).data("kendoComboBox");
		st_customer.contactComboBox.bind("change", combobox_change);
		st_customer.businessUnitComboBox = $("#name").data("kendoAutoComplete");
//		st_customer.businessUnitComboBox = eWidget.getSingleComboBox("name","/customer/" + st_customer.SIMPLE_TYPE + "/getListBusinessUnit","name","").data("kendoComboBox");
		st_customer.businessUnitComboBox.setOptions({
			change: function(e) {
				var businessUnitName = $("#name").val().trim();
				$.ajax({
					url:fsn.getHttpPrefix() + "/erp/customer/" + st_customer.SIMPLE_TYPE +"/getLicense?businessUnitName=" + businessUnitName,
					type:"POST",
					dataType:"json",
					async:false,
					contentType: "application/json; charset=utf-8",
					success:function(data){
						if(data.result != null){
							$("#license").val(data.result);
							$("#license").attr("readonly",true);
						}else{
							$("#license").val("");
							$("#license").attr("readonly",false);
						}
					}
				})
			  }
		});
		this.initContactInfoButtons();
		
		this.initRequiredAreas();
	}
	
	st_customer.initContactInfoButtons = function(){
		$("#add_").bind("click",function(){
			st_customer.addContact();
		});
		$("#update_").bind("click",function(){
			st_customer.updateContact();
		});
		$("#delete_").bind("click",function(){
			if(st_customer.contactGrid.data("kendoGrid").select().length <= 0) {
				fsn.initNotificationMes("请选择一条记录!", false);
			} else {
				var win = eWidget.getPromptWindow();
				win.data("kendoWindow").open().center();
				$("#confirmWindow").unbind("click");
				$("#cancelWindow").unbind("click");
				$("#confirmWindow").bind("click",function(){
					st_customer.deleteContact();
					fsn.initNotificationMes("删除成功",true);
					win.data("kendoWindow").close();
				});
				$("#cancelWindow").bind("click",function(){
					win.data("kendoWindow").close();
				});
			}
			
		});
	}
	
	st_customer.cleanContactForm = function(){
		$("#ctel1").kendoValidator().data("kendoValidator").hideMessages();
		$("#czipcode").kendoValidator().data("kendoValidator").hideMessages();
		$("#cemail").kendoValidator().data("kendoValidator").hideMessages();
		$("#cid").val("");
		$("#cname").removeAttr("readonly");
		$("#cname").val("");
		$("#ctel1").val("");
		$("#ctel2").val("");
		$("#cemail").val("");
		$("#cim").val("");
		$("#caddr").val("");
		$("#czipcode").val("");
		$("#cprovince").data("kendoComboBox").value("");
		$("#ccity").data("kendoComboBox").value("");
		$("#carea").data("kendoComboBox").value("");
		$( "#cprovince").data( "kendoComboBox" ).value(null);
		$( "#ccity" ).data("kendoComboBox" ).value(null);
		$( "#carea" ).data("kendoComboBox" ).value(null); 
	}
	
	st_customer.bindContactForm = function(e){
		if(e.id){
			$("#cid").val(e.id);
		}
		$("#cname").attr("readonly","readonly");
		$("#cname").val(e.name);
		$("#ctel1").val(e.tel_1);
		$("#ctel2").val(e.tel_2);
		$("#cemail").val(e.email);
		$("#cim").val(e.im_account);
		$("#caddr").val(e.addr);
		$("#czipcode").val(e.zipcode);
		$("#cprovince").data( "kendoComboBox" ).value(e.province);
		$("#ccity").data("kendoComboBox" ).value(e.city);
		$("#carea").data("kendoComboBox" ).value(e.area); 
		$("#ctel1").kendoValidator().data("kendoValidator").validate();
		$("#cemail").kendoValidator().data("kendoValidator").hideMessages();
		$("#czipcode").kendoValidator().data("kendoValidator").hideMessages();
	}
	
	st_customer.addContact = function(){
		$("#cid").removeAttr("disabled");
		st_customer.cleanContactForm();
		st_customer.contactWin.data("kendoWindow").open().center();
		
		var oriLength = st_customer.contactGrid.data("kendoGrid").dataSource.data().length;
		
		$("#confirm_").unbind("click");
		$("#cancel_").unbind("click");
		
		$("#confirm_").bind("click",function(){
			
			var flag = st_customer.verifyContacts();
				if(flag) {
					var tel = $("#ctel1").kendoValidator().data("kendoValidator");
					var cmail = $("#cemail").kendoValidator().data("kendoValidator");
					var czipcode = $("#czipcode").kendoValidator().data("kendoValidator");
					var contact_validator = $("#CONTACT_INFO_WIN").kendoValidator().data("kendoValidator");
					var areaCo = st_customer.areaCombox.dataItem();
					if(areaCo == undefined){
						fsn.initNotificationMes("输入的区域不正确", false);
					}else{
						if(tel.validate()&&czipcode.validate()&&cmail.validate()||tel.validate()&&$("#czipcode").val().trim()!=""&&$("#cemail").val().trim()==""&&czipcode.validate()||tel.validate()&&$("#cemail").val().trim()!=""&&$("#czipcode").val().trim()==""&&cmail.validate()||tel.validate()&&$("#czipcode").val().trim()==""&&$("#cemail").val().trim()==""){
							st_customer.saveConfirm(true);
							fsn.initNotificationMes("保存成功",true);
							var nowLength = st_customer.contactGrid.data("kendoGrid").dataSource.data().length;
							if(oriLength != nowLength) {
								st_customer.checkFlag = true;
							}
							st_customer.contactWin.data("kendoWindow").close();
						}
					}
					
				}
		});
		
		$("#cancel_").bind("click",function(){
			st_customer.contactWin.data("kendoWindow").close();
		})
	}
	st_customer.updateContact = function(){
		if(st_customer.contactGrid.data("kendoGrid").select().length > 0 ){
			$("#cid").attr("disabled","disabled");
			var contactItem = st_customer.selectedContact = st_customer.contactGrid.data("kendoGrid").dataItem(st_customer.contactGrid.data("kendoGrid").select());
			
			st_customer.bindContactForm(contactItem);
			
			st_customer.contactWin.data("kendoWindow").open().center();
			
			$("#confirm_").unbind("click");
			$("#cancel_").unbind("click");
			
			$("#confirm_").bind("click",function(){
				
				var flag = st_customer.verifyContacts();
				var provinceCom = st_customer.provinceCombox.dataItem().province;
				var cityCom = st_customer.cityCombox.dataItem().city;
				var areaCo = st_customer.areaCombox.dataItem().area;
				var check = st_customer.checkDiff(provinceCom,cityCom,areaCo);
				if(check){
					if(flag) {
						var tel = $("#ctel1").kendoValidator().data("kendoValidator");
						var cmail = $("#cemail").kendoValidator().data("kendoValidator");
						var czipcode = $("#czipcode").kendoValidator().data("kendoValidator");
						var contact_validator = $("#CONTACT_INFO_WIN").kendoValidator().data("kendoValidator");
						var areaCo = st_customer.areaCombox.dataItem();
						if(areaCo == undefined){
							fsn.initNotificationMes("输入的区域不正确", false);
						}else{
							if(tel.validate()&&czipcode.validate()&&cmail.validate()||tel.validate()&&$("#czipcode").val().trim()!=""&&$("#cemail").val().trim()==""&&czipcode.validate()||tel.validate()&&$("#cemail").val().trim()!=""&&$("#czipcode").val().trim()==""&&cmail.validate()||tel.validate()&&$("#czipcode").val().trim()==""&&$("#cemail").val().trim()==""){
								st_customer.saveConfirm(false);
								fsn.initNotificationMes("修改成功",true);
								st_customer.contactWin.data("kendoWindow").close();
							}
						}
						
					}
				}else{
					fsn.initNotificationMes("未修改,不能保存", false);
				}
				
			});
			
			$("#cancel_").bind("click",function(){
				st_customer.contactWin.data("kendoWindow").close();
			})
		} else {
			fsn.initNotificationMes("请选择一条记录！",false);
		}
	}
	
	st_customer.deleteContact = function(){
		if(st_customer.contactGrid.data("kendoGrid").select().length > 0 ){
			var oriLength = st_customer.contactGrid.data("kendoGrid").dataSource.data().length;
			var contactItem = st_customer.contactGrid.data("kendoGrid").dataItem(st_customer.contactGrid.data("kendoGrid").select());
			st_customer.contactGrid.data("kendoGrid").dataSource.remove(contactItem);
			var nowLength = st_customer.contactGrid.data("kendoGrid").dataSource.data().length;
			if(oriLength != nowLength) {
				st_customer.checkFlag = true;
			}
		}
	}
	
	st_customer.saveConfirm = function(isNew){
		var contact_validator = $("#CONTACT_INFO_WIN").kendoValidator().data("kendoValidator");
		
		var provinceCom = st_customer.provinceCombox.dataItem().province;
		var cityCom = st_customer.cityCombox.dataItem().city;
		var areaCo = st_customer.areaCombox.dataItem();
	
		var tel = $("#ctel1").kendoValidator().data("kendoValidator");
		var cmail = $("#cemail").kendoValidator().data("kendoValidator");
		var czipcode = $("#czipcode").kendoValidator().data("kendoValidator");
		if(areaCo==undefined){
			fsn.initNotificationMes("输入的区域不正确", false);
		}else{
			areaCom = areaCo.area
			if(tel.validate()&&czipcode.validate()&&cmail.validate()||tel.validate()&&$("#czipcode").val().trim()!=""&&$("#cemail").val().trim()==""&&czipcode.validate()||tel.validate()&&$("#cemail").val().trim()!=""&&$("#czipcode").val().trim()==""&&cmail.validate()||tel.validate()&&$("#czipcode").val().trim()==""&&$("#cemail").val().trim()==""){
				var model = {};
				if(isNew){
					model = {
						name:$("#cname").val().trim(),
						tel_1:$("#ctel1").val().trim(),
						tel_2:$("#ctel2").val().trim(),
						email:$("#cemail").val().trim(),
						im_account:$("#cim").val().trim(),
						addr:$("#caddr").val().trim(),
						zipcode:$("#czipcode").val().trim(),
						province:provinceCom,
						city:cityCom,
						area:areaCom
					}
				}else{
					if(st_customer.selectedContact.id){
						model.id = st_customer.selectedContact.id;
					}
				}
				
				if(isNew){
					st_customer.contactGrid.data("kendoGrid").dataSource.add(model);
				}else{
					var modelUpdateAndChecked = {};
					st_customer.contactGrid.data("kendoGrid").dataSource.remove(st_customer.selectedContact);
					modelUpdateAndChecked = {
							id: $("#cid").val().trim(),
							name:$("#cname").val().trim(),
							tel_1:$("#ctel1").val().trim(),
							tel_2:$("#ctel2").val().trim(),
							email:$("#cemail").val().trim(),
							im_account:$("#cim").val().trim(),
							addr:$("#caddr").val().trim(),
							zipcode:$("#czipcode").val().trim(),
							province:provinceCom,
							city:cityCom,
							area:areaCom
					}
					st_customer.checkDiff(provinceCom,cityCom,areaCom);//更新的时候检查
					st_customer.contactGrid.data("kendoGrid").dataSource.add(modelUpdateAndChecked);
				}
			}
		}
	}
	
	st_customer.comboBoxProvince = {};
	st_customer.comboBoxCity = {};
	st_customer.comboBoxArea = {};
	st_customer.provinceCombox = null;
	st_customer.cityCombox = null;
	st_customer.areaCombox = null;
	
	//kendoComboBox校验
	combobox_change=function(e){
		var judge = e.sender.selectedIndex;
		if(judge==-1){
			this.value("");
			fsn.initNotificationMes("填写内容有误！请重新填写", false);
		}else{
			return;
		}
	}
	
	st_customer.initRequiredAreas = function(){
		
		var datasourceProvince = new kendo.data.DataSource({
			transport : {
				read : {
					type : "POST",
					url : function(options) {
						return fsn.getHttpPrefix() + "/erp/customer/" + st_customer.SIMPLE_TYPE + "/findPro";
					},
					dataType : "json",
					contentType : "application/json"
				},
			},
			schema : {
				model : {
					fields : {
						id : {
							type : "number"
						},
						provinceId : {
							type : "number"
						},
						province : {
							type : "string"
						}
					}
				},
				data : function(data) {
					if (data && data.result) {
						return data.result;
					}
				},
				total : function(data) {// 总条数
					if (data && data.count) {
						return data.count;
					}
				}
			}
		});
		
		var datasourceCity = new kendo.data.DataSource({
			transport : {
				read : {
					type : "GET",
					url : function(options) {
						var prov = $("#cprovince").data("kendoComboBox");
						if(prov!=null){
							return fsn.getHttpPrefix() + "/erp/address/findCityByProId/" + prov.value();
						}
					},
					dataType : "json",
					contentType : "application/json"
				},
			},
			schema : {
				model : {
					fields : {
						id : {
							type : "number"
						},
						cityId : {
							type : "number"
						},
						city : {
							type : "string"
						},
						provinceId : {
							type : "string"
						}
					}
				},
				data : function(data) {
					if (data && data.cities) {
						return data.cities;
					}
				},
			}
		});
		
		var datasourceArea = new kendo.data.DataSource({
			transport : {
				read : {
					type : "GET",
					url : function(options) {
						var city = $("#ccity").data("kendoComboBox");
						if(city != null) {
							return fsn.getHttpPrefix() + "/erp/address/findAreaByCityId/" + city.value();
						}
					},
					dataType : "json",
					contentType : "application/json"
				},
			},
			schema : {
				model : {
					fields : {
						id : {
							type : "number"
						},
						areaId : {
							type : "number"
						},
						area : {
							type : "string"
						},
						cityId : {
							type : "string"
						}
					}
				},
				data : function(data) {
					if (data && data.areas) {
						return data.areas;
					}
				},
			}
		});
	
		/* 地址控件的选择事件 */
		function addressCombobox_change(index,cobmox){
			if(index==-1){
				cobmox.value("");
				fsn.initNotificationMes("您输入的地址有误！请重新填写", false);
			}
		}
		
		st_customer.provinceCombox = $("#cprovince").kendoComboBox({
			dataTextField : "province",
			dataValueField : "provinceId",
			dataSource : datasourceProvince,
			filter : "contains",
			placeholder : "请选择...",
			suggest : true
		}).data("kendoComboBox");		
		st_customer.provinceCombox.bind("change", function(e){
			/* 当index=-1时，说明输入的内容不匹配，或者没有输入任何内容 */
			var index = e.sender.selectedIndex;
			if(datasourceCity!=null && index != -1) datasourceCity.read();
			addressCombobox_change(index,this);
		});
		
		st_customer.cityCombox = $("#ccity").kendoComboBox({
			cascadeFrom: "cprovince",
			dataTextField : "city",
			dataValueField : "cityId",
			dataSource : datasourceCity,
			filter : "contains",
			placeholder : "请选择...",
			suggest : true
		}).data("kendoComboBox");
		st_customer.cityCombox.bind("change", function(e){
			/* 当index=-1时，说明输入的内容不匹配，或者没有输入任何内容 */
			var index = e.sender.selectedIndex;
			if(datasourceArea!=null && index != -1) datasourceArea.read();
			addressCombobox_change(index,this);
		});
		
		st_customer.areaCombox = $("#carea").kendoComboBox({
			cascadeFrom: "ccity",
			dataTextField : "area",
			dataValueField : "areaId",
			dataSource : datasourceArea,
			filter : "contains",
			placeholder : "请选择...",
			suggest : true
		}).data("kendoComboBox");
		st_customer.areaCombox.bind("change", combobox_change);
			
		st_customer.provinceCombox.setOptions({
			change: function(e) {
				st_customer.comboBoxProvince = {
						provinceId:this.value(),
						province:this.text()
				}
			  }
		});
		
		st_customer.cityCombox.setOptions({
			change: function(e) {
				st_customer.comboBoxArea = {
						areaId:this.value(),
						area:this.text()
				}
			  }
		});
		
		st_customer.areaCombox.setOptions({
			change: function(e) {
				st_customer.comboBoxCity = {
						cityId:this.value(),
						city:this.text()
				}
			  }
		});
		
	}
	

	st_customer.verifyContacts = function() {
		if($("#cname").val().trim() == "") {
			fsn.initNotificationMes("请填写联系人姓名！",false);
			$("#cname").focus();
			return;
		}
		if($("#ctel1").val().trim() == "") {
			fsn.initNotificationMes("请填写联系人手机！",false);
			$("#ctel1").focus();
			return;
		}
		if($("#cprovince").data("kendoComboBox" ).value() == "") {
			fsn.initNotificationMes("请填写联系人省！",false);
			$("#cprovince").focus();
			return false;
		}
		if($("#ccity").data("kendoComboBox" ).value() == "") {
			fsn.initNotificationMes("请填写联系人市！",false);
			$("#ccity").focus();
			return false;
		}
		if($("#carea").data("kendoComboBox" ).value() == "") {
			fsn.initNotificationMes("请填写联系人区！",false);
			$("#carea").focus();
			return false;
		}
		if($("#caddr").val().trim() == "") {
			fsn.initNotificationMes("请填写联系人地址！",false);
			$("#caddr").focus();
			return;
		}
		return true;
	}

	st_customer.PorCDetail = function(id, name, licenseno, diyTypeName, note){
		$("#detail_label_num_content").html(id);
		$("#detail_label_min_content").html(name);
		$("#detail_label_license_content").html(licenseno);
		$("#detail_label_type_content").html(diyTypeName);
		$("#detail_lable_base_remarks_content").html(note == null ? "" : note);
	}

	st_customer.verifyCustomer = function() {
		if($("#name").val().trim() == "") {
			fsn.initNotificationMes("请填写" + st_customer.SIMPLE_MODEL_NAME + "名称！",false);
			$("#name").focus();
			return;
		}
		if($("#license").val().trim() == "") {
			fsn.initNotificationMes("请填写" + st_customer.SIMPLE_MODEL_NAME + "执照号！",false);
			$("#license").focus();
			return;
		}
		if($("#type").data("kendoComboBox" ).value() == "") {
			fsn.initNotificationMes("请填写" + st_customer.SIMPLE_MODEL_NAME + "类型！",false);
			$("#type").focus();
			return;
		}
		return true;
	}
	

	st_customer.GRIDID = "Simple_Grid";
	st_customer.DAILOGID = "OPERATION_WIN"; 
	
	st_customer.initailize = function(){
		this.initComponent(st_customer);
		$("#label_no").html(st_customer.SIMPLE_MODEL_NAME + "编号");
		$("#label_name").html(st_customer.SIMPLE_MODEL_NAME + "名称");
		$("#label_license").html(st_customer.SIMPLE_MODEL_NAME + "执照号");		
		$("#label_type").html(st_customer.SIMPLE_MODEL_NAME + "类型");
		$("#label_note").html(st_customer.SIMPLE_MODEL_NAME + "备注");
		$("#detail_label_num_name").html(st_customer.SIMPLE_MODEL_NAME + "编号");
		$("#detail_label_min_name").html(st_customer.SIMPLE_MODEL_NAME + "名称");
		$("#detail_label_license_name").html(st_customer.SIMPLE_MODEL_NAME + "执照号");		
		$("#detail_label_type_name").html(st_customer.SIMPLE_MODEL_NAME + "类型");
		$("#detail_lable_base_remarks_name").html(st_customer.SIMPLE_MODEL_NAME + "备注");
		this.initRequiredComponents();
	}
	
	st_customer.checkFlag = false;
	st_customer.checkDiff = function(provinceCom,cityCom,areaCom) {
//		var datas = st_customer.contactGrid.data("kendoGrid").dataSource.data();
//		var index = datas.indexOf(st_customer.selectedContact);
		if(st_customer.selectedContact.name != $("#cname").val().trim() ||
				st_customer.selectedContact.tel_1 != $("#ctel1").val().trim() ||
				st_customer.selectedContact.email != $("#cemail").val().trim() ||
				st_customer.selectedContact.im_account != $("#cim").val().trim() ||
				st_customer.selectedContact.province != provinceCom ||
				st_customer.selectedContact.city != cityCom ||
				st_customer.selectedContact.area != areaCom ||
				st_customer.selectedContact.zipcode != $("#czipcode").val().trim() ||	
				st_customer.selectedContact.addr != $("#caddr").val().trim() ||
				st_customer.selectedContact.tel_2 != $("#ctel2").val().trim()) {
			st_customer.checkFlag = true;
		}else{
			st_customer.checkFlag = false;
		}
		return st_customer.checkFlag;
	}
	
	st_customer.initailize();
	st_customer.judgeIsUsed = function(item){
		var flag = true;
		var model = {
				
		};
		
		if(st_customer.SIMPLE_TYPE == 2){
			model.businessUnit = {
					id:st_customer.selectedData.id,
					name:$("#name").val().trim(),
					license:{licenseNo:$("#license").val().trim()},
					note:$("#note").val().trim(),
			};
		}else if(st_customer.SIMPLE_TYPE == 3){
			model.businessUnit = {
					id:st_customer.selectedData.id,
					name:$("#name").val().trim(),
					license:{licenseNo:$("#license").val().trim()},
					note:$("#note").val().trim(),
			};
		}
		
		$.ajax({
			url: fsn.getHttpPrefix() + "/erp/customer/" + st_customer.SIMPLE_TYPE +"/judgeIsUsed",
			type:"POST",
			dataType:"json",
			async:false,
			contentType: "application/json; charset=utf-8",
			data:JSON.stringify(model),
			success:function(data){
				if(data.flag == true){
					flag = true;
				}else{
					flag = false;
				}
			}
		});
		return flag;
	}
//	$('#search').kendoSearchBox({
//				change : function(e) {
//					var keywords=e.sender.options.value;
//			    	if(keywords.trim()!=""){
//			    		$("#Simple_Grid").data("kendoGrid").setDataSource(initStandard(keywords));
//			    	    $("#Simple_Grid").data("kendoGrid").refresh();
//			    	}else{
//			    		$("#Simple_Grid").data("kendoGrid").setDataSource(st_customer.datasource);
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
					return fsn.getHttpPrefix() + "/erp/customer/" + st_customer.SIMPLE_TYPE + "/search?keywords="+keywords;
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
				//ds.read();
				return ds;
			}
})
