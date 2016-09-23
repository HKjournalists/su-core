$(function(){
	var fsn = window.fsn = window.fsn || {};
	var common = fsn.common = fsn.common || {};
	var st_customer = fsn.st_customer = fsn.st_customer || {};
	
	$.extend(st_customer,common);
	
	/**
	 * 是否刷新总供应商数的标记
	 * @author ZhangHui 2015/5/5
	 */
	st_customer.isFreshTotalOfDealer = true;
	
	/**
	 * 页面初始化
	 */
	st_customer.initailize = function(){
		st_customer.initailize_common();
		if(st_customer.SIMPLE_TYPE==2){
			st_customer.initSearch();
			$("#GRID_PROVIDER").hide();
			$("#GRID_COUSTOMER").show();
		}else if(st_customer.SIMPLE_TYPE==3){
			$("#GRID_PROVIDER").hide();
			$("#GRID_COUSTOMER").show();
		}
		/**
		 * [查看报告待处理企业]按钮Click事件
		 * @author ZhangHui 2015/5/4
		 *//*
		$("#reviewOnHandProducer").bind("click",function(){
			window.location.href = "/fsn-core/views/erp_new/basic/customer_onHandleProducer.html?type=3&name=供应商";
		});
		$("#customer_sourc").click(function(){
//			window.location.href = "/fsn-core/views/erp_new/basic/customer_onHandleProducer.html?type=3&name=供应商";
		});*/
		st_customer.initConfirmWindow();
		if(typeId==2){
			st_customer.getSourceCustomer('sourceContext',2);
			}
		else if(typeId==1){
			st_customer.getSourceCustomer('soldContext',1);
		}
		
	};
	/**
	 * ==========================开始==============================
	 * ===========================================================
	 */
	/**
	 * 查询入住企业
	 */
	st_customer.initSearch = function(){
		$("#searchId").css("background","#04bf72");
		$("#sourceId").css("background","#C4C400");
		$("#soldId").css("background","#C4C400");
		$("#searchAdd").hide();
	}
	//查询不到数据时，去新增客户
	st_customer.addSearch = function(){
		common.add(common.realObj.DAILOGID,common.realObj.clearForm,common.realObj.windowSaveConfrim);
	}
	//回车键查询按钮
	st_customer.getEnterPress = function(e){
		 if(e.keyCode==13){
			 st_customer.initGridSearch();
        }
	}
	//点击查询按钮
	st_customer.initGridSearch = function(keyword){
		var grid = $("#searchContext").data("kendoGrid");
		var dataSource =  st_customer.searchDataSource();
		document.getElementById('but_back').style.display="";
		$("#div_source_coustomer").hide();	
		$("#div_soldTo_coustomer").hide();	
		if(!grid){
			$("#searchContext").kendoGrid({
				dataSource:dataSource,
				editable: false,
				selectable : true,//true是可以选择列表中的数据复制;"multiple, row"：表示不可选择类别的值,不可复制
				resizable: true,
				pageable: {
		            refresh: false,
		            pageSizes: [10,20,50,100],
			        messages: fsn.gridPageMessage(),
		        },
				columns: [{field:"countNum",title:"序号",width:50},
				          {field:"name",title:"客户名称",width:150},
				          {field:"lincesNo",title:"营业执照号" ,width:100},
				          { field:"address",title:"联系地址" ,width:200},
				          { field:"signFlag",hidden:true, width:100},
				          {title:"操作",width:100,
	        			      template:function(e){
	    			              var tag="";
	    			              if(e.signFlag){
	    			            	  tag="<a onclick='return fsn.st_customer.views(" + e.id + ",\"" + e.name + "\",\"" + e.lincesNo + "\",\"" + (e.diyType?e.diyType.name:"") + "\",\"" + e.note + "\")' " +
	    	        					"class='k-button k-button-icontext k-grid-ViewDetail'><span class='k-icon k-edit'> </span>查看</a>";  
	    			              }else{
	    			            	  tag+="<a onclick='return fsn.st_customer.addCustomer(" + e.id + ",\"" + e.name + "\",\"" + e.lincesNo + "\",\"" + (e.diyType?e.diyType.id:"") + "\",\"" + (e.diyType?e.diyType.name:"") + "\",\"" + e.note + "\",0)' ";
	    			            	  tag+="class='k-button k-button-icontext k-grid-ViewDetail'><span class='k-icon k-edit'> </span>添加</a>";
	    			              }
	        					return tag;
	        				}
	        		    }
				 ]
			});
		}else{
			grid.setDataSource(dataSource);
			grid.refresh();	
		}
	};
	st_customer.searchDataSource = function(){
		
		var ds = new kendo.data.DataSource({
			transport:{
				read:{
					type:"GET",
					dataType:"json",
					contentType:"application/json",
					url : function(options){
						var keyword = $("#search_keyword").val();
						if(keyword.trim()==''){
							fsn.initNotificationMes("请输入您要查询条件！",false);
							$("#searchContext").hide();	
							$("#searchAdd").hide();
							return ;
						}
						return fsn.getHttpPrefix() + "/erp/customer/" + st_customer.SIMPLE_TYPE + "/searchCustomer/" + options.page + "/" + options.pageSize + "/"+keyword.trim()+"/"+Math.random();
					},
				}
			},
			schema:{
				fields: {
					id:{type:"number"},
					name:{type:"string"},
					diyType:{
						id:{type:"number"},
						name:{type:"string"}
					},
					note:{type:"string"}
				},
				data:function(data) {
					return data.result.listOfModel;
				},
				total:function(data){
					var count = data.result.count;
					if(count == 0){
                          $("#telab").text("没有该企业的相关信息！");
						$("#searchContext").hide();	
					}else{
                        $("#telab").text("已经查询到"+count+"条信息！");
						$("#searchContext").show();
					}
                    $("#searchAdd").show();
					return count;
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
	}
	//展示页面和影藏页面的方法（查询，来源客户，销往客户）面板切换
	st_customer.getType = function(zone,type){
		if(type==1){
			$("#searchId").css("background","#C4C400");
			$("#sourceId").css("background","#C4C400");
			$("#soldId").css("background","#04bf72");
			$("#div_search_coustomer").hide();
			$("#div_source_coustomer").hide();
			$("#div_soldTo_coustomer").show();
		}else if(type==2){
			$("#searchId").css("background","#C4C400");
			$("#sourceId").css("background","#04bf72");
			$("#soldId").css("background","#C4C400");
			$("#div_search_coustomer").hide();
			$("#div_source_coustomer").show();
			$("#div_soldTo_coustomer").hide();
		}else{
			$("#searchId").css("background","#04bf72");
			$("#sourceId").css("background","#C4C400");
			$("#soldId").css("background","#C4C400");
			$("#div_search_coustomer").show();
			$("#div_source_coustomer").hide();
			$("#div_soldTo_coustomer").hide();
			$("#searchContext").hide();	
			$("#searchAdd").hide();
			$("#search_keyword").val("");
		}	
	}
	
	/**
	 * 获取来源或销往客户
	 * authar:wubiao
	 */
	st_customer.getSourceCustomer = function(zone,type){
		$("#typeStatus").val(type);
		st_customer.getType(zone,type);
		if(type == 0){
			return;
		}
		var grid = $("#"+zone).data("kendoGrid");
		var dataSource = st_customer.sourceCustomer();
		if(!grid){
			$("#"+zone).kendoGrid({
				dataSource:dataSource,
				selectable : true, //"multiple, row",
				filterable: {
					messages: fsn.gridfilterMessage(),
	                extra: false,
	                operators: {
	                   string: {
	                	   contains: fsn.l("Contains"),
	                    	eq: fsn.l("Is equal to"),
	                   	    neq: fsn.l("Is not equal to")
	                    }
	                }
	            },
	            pageable: {
		            refresh: false,
		            pageSizes: [10,20,50,100],
			        messages: fsn.gridPageMessage(),
		        },
				columns: [{field:"name",title:"客户名称",width:300},
				          {field:"lincesNo",title:"营业执照号" ,width:200},
				          { field:"contact",title:"联系人" ,width:100,filterable:false},
				          { field:"telephone",title:"联系电话" ,width:100,filterable:false},
				        {title:"操作",width:220,command:[{name:"ViewDetail",text:"预览",click:fsn.st_customer.views_},
				                  {name:"edit",text:"编辑",click:fsn.st_customer.addCustomer},
				                  {name:"delete",text:"删除",click:fsn.st_customer.remove}
				        ]}
				 ]
			})
		}else{
			 grid.setDataSource(dataSource);
			 grid.refresh();
		}
	};
    /**
     * 查询来源或销往客户
     */
	st_customer.sourceCustomer = function(){
	  var  ds = new kendo.data.DataSource({
			transport:{
				read:{
					type:"GET",
					url:function(options){
						var type = $("#typeStatus").val();
						var configure = "0";
						if(options.filter!=undefined){
						   configure = filter.configure(options.filter);
						}
						return fsn.getHttpPrefix() + "/erp/customer/" + st_customer.SIMPLE_TYPE + "/sourceOrsoldCustomer/"+type+"/" + options.page + "/" + options.pageSize + "/"+configure+"/"+Math.random();
					},
					dataType:"json",
					contentType:"application/json"
				}
			},
		    schema:{
		    	fields: {
	    			id:{type:"number"},
	             	name:{type:"string"},
	             	diyType:{
	             		id:{type:"number"},
	                 	name:{type:"string"}
	             	},
	             	note:{type:"string"}
	             },
				data:function(data) {
					return data.result.listOfModel;
				},
				total:function(data){
					var count = data.result.count;
					return count;
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
	}
	//预览
	st_customer.views_ = function(e){
		var tr = $(e.target).closest("tr");
		var data = this.dataItem(tr);
		fsn.st_customer.views(data.id,data.name,data.lincesNo,data.diyType.name,data.note);	
	}
	//添加或者修改
	st_customer.addCustomer = function(e,name,lincesNo,typeId,typeName,note,status){
		var addWin = eWidget.getWindow("OPERATION_WIN",st_customer.SIMPLE_MODEL_NAME, common.realObj.win_width == null ? null:common.realObj.win_width);					
		var _id,_name,_lincesNo,_typeId,_typeName,_note,_type;
		if(status!=undefined && status == 0){
			addWin.data("kendoWindow").title(typeName==null||typeName=='null'||typeName==''?"添加成为我的客户":typeName);
			_id = e;_name=name;
			_lincesNo = lincesNo;
			_typeId = typeId;
			_typeName = typeName;
			_note=note;
		}else{
			var tr = $(e.target).closest("tr");
			var data = this.dataItem(tr);
			addWin.data("kendoWindow").title(data.diyType.name);
			_id = data.id;
			_name=data.name;
			_lincesNo = data.lincesNo;
			_typeId = typeId;
			_typeName = typeName;
			_note=data.note;
			_type = data.diyType.type;
		}
		$("#id").val(_id);
		$("#name").val(_name);
		$("#license").val(_lincesNo);
		$("#note").val(_note);
		$("#name").attr("readonly",true);
		$("#license").attr("readonly",true);
		/*var types = $("#type").data("kendoDropDownList" );
		types.value(_typeId); 
		types.text(_typeName);*/
		st_customer.initBusnessItemDS(_id);
		$("#CONTACT_INFO_GRID").data("kendoGrid").setDataSource(st_customer.busnessItemDS);
		addWin.data("kendoWindow").open().center();
		$("#confirm").unbind("click");
		$("#cancel").unbind("click");
		
		
		$("#confirm").bind("click",function(){
			var title ="";
			if(status!=undefined && status == 0){
				title = "添加成为我的客户吗?";
			}else{
				title = "修改成为我的客户吗?";
			}
			$("#CONFIRM_MSG").html("您确定要把《"+_name+"》"+title);
			$("confirm_yes_btn label").val("bb");
			$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
			$("#confirm_yes_btn").unbind("click");
			$("#confirm_yes_btn").click(function(){
				$("#CONFIRM_COMMON_WIN").data("kendoWindow").close();
				var zone = "";
			   if(status == 0){
			    typeId = 0;
			    st_customer.windowSaveConfrim(zone,typeId);
			   }else{
				    zone = "searchContext";
					if(typeId==1){
						zone ="soldContext";
					}else if(typeId==2){
						zone ="sourceContext";
						
					}
					st_customer.windowUpdateConfrim(zone,typeId);
			   }
				
			});
		});
		$("#cancel").bind("click",function(){
			if(addWin!=null){
				addWin.data("kendoWindow").close();
			}
		});
	};
	/**
	 * 删除客户
	 */
 st_customer.remove = function(e){
	var tr = $(e.target).closest("tr");
	var data = this.dataItem(tr);
	if(tr){
		var item = common.realObj.selectedData = data;
		//不影响其他模块的修改。判断是如果没有这个字段就自动到else了。所以不会影响其他的模块的
			if("check" in item && item.check == true) {
				fsn.initNotificationMes("已经审核不能删除！",false);
			} else if("outOfBillState" in item && (item.outOfBillState == "已交付" || item.outOfBillState == "已发货")) {
					fsn.initNotificationMes("已发货或者已交付,不能删除！",false);
				} else if("re_purchase_check" in item&&item.re_purchase_check == "已收货"){
						fsn.initNotificationMes("状态为已收货,不能删除！",false);
					}else{
						var win = eWidget.getPromptWindow();
						win.data("kendoWindow").open().center();
						$("#confirmWindow").unbind("click");
						$("#cancelWindow").unbind("click");
						$("#confirmWindow").bind("click",function(){
							var zone = "";
							var type = data.diyType.type;
							if(type==1){
								zone ="soldContext";
							}else if(type==2){
								zone ="sourceContext";
							}
							st_customer.windowDeleteConfrim(zone,type);
							//confrimFun();
							win.data("kendoWindow").close();
						});
						$("#cancelWindow").bind("click",function(){
							win.data("kendoWindow").close();
						});
					}
		}else{
			fsn.initNotificationMes("请选择一条记录！",false);
		}	
	}
	/**
	 * ==========================结束==============================
	 * ===========================================================
	 */
	
	
	st_customer.datasource = new kendo.data.DataSource({
	
		transport: {
			read : {
                type : "GET",
				url : function(options){
                	if(options.filter){
                		st_customer.isFreshTotalOfDealer = false;
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
            		if(st_customer.SIMPLE_TYPE == 3 && st_customer.isFreshTotalOfDealer){
            			st_customer.totalOfDealer = data.result.count;
            		}
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
	
	/**
	 * 查看供销产品
	 * @author Zhanghui 2015/4/10
	 */
	st_customer.viewproinfo = function(organization, fromBusId, providername){
	   if(!organization || organization==""){
		   fsn.initNotificationMes("该企业未注册！", false);
		   return;
	   }
	   /* 校验该供应商下有无当前客户 */
	   var isExist = st_customer.validatCustomer(fromBusId);
	   if(!isExist){
		   fsn.initNotificationMes("检测到您并不是该供应商的客户，无法查看供销产品，确认无误后可删除该供应商！", false);
		   return;
	   }
	   /* 校验该供应商对当前客户有无供销产品 */
	   isExist = st_customer.validateProduct(fromBusId);
	   if(!isExist){
		   fsn.initNotificationMes("检测到该供应商并未给您供应任何产品！", false);
		   return;
	   }
	   /* 打开供销产品界面 */
	   //window.location.href = "/fsn-core/views/product_new/manage_product_bussiness_super.html?" + fromBusId+"&"+providername;
	   window.open("/fsn-core/views/product_new/manage_product_bussiness_super.html?" + fromBusId+"&"+providername);
	};
	
	
		/**
	 * 查找当前商超的 总供应商数、总产品数、待处理报告数
	 * @author ZhangHui 2015/5/1
	 */
	/*st_customer.queryCount = function(){
		if(st_customer.SIMPLE_TYPE != 3){
			return;
		}
		
		var totalOfProduct = 0;
		var totalOfOnHandReports = 0;
		var totalOfReports = 0;
		$.ajax({
	        url: fsn.getHttpPrefix() + "/report/getCountsOfReports",
	        type: "GET",
	        dataType: "json",
	        async: false,
	        contentType: "application/json; charset=utf-8",
	        success: function(returnValue) {
	            fsn.endTime = new Date();
	            if (returnValue.result.status == "true") {
	            	totalOfProduct = returnValue.totalOfProduct;
	            	totalOfOnHandReports = returnValue.totalOfOnHandReports;
	            	totalOfReports = returnValue.totalOfReports;
	            }
	        }
	    });
		
		$("#status_bar").html("当前位置：我的供应商&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
					"总供应商数：" + st_customer.totalOfDealer + "&nbsp;&nbsp;&nbsp;&nbsp;" +
					"总产品数：" + totalOfProduct + "&nbsp;&nbsp;&nbsp;&nbsp;" +
					"总报告数：" + totalOfReports + "&nbsp;&nbsp;&nbsp;&nbsp;" +
					"待处理报告数：" + totalOfOnHandReports);
	};*/
	
	st_customer.initConfirmWindow = function(){
    	var confirmWin = $("#CONFIRM_COMMON_WIN").data("kendoWindow");
    	if(!confirmWin){
    		$("#CONFIRM_COMMON_WIN").kendoWindow({
    			width: "450",
    			height:"auto",
    	        title: "确定",
    	        visible: false,
    	        resizable: false,
    	        draggable:false,
    	        modal: true
    		});
    		}

    	//add the "k-button" css onto buttons
    	$("#CONFIRM_COMMON_WIN button").addClass("k-button");
    	$("#confirm_no_btn").bind("click",function(){$("#CONFIRM_COMMON_WIN").data("kendoWindow").close();});
    }
	st_customer.initailize();
});
