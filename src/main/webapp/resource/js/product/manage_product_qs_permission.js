$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var upload = fsn.upload = fsn.upload || {};
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    
	portal.initialize = function(){
		upload.buildGridWioutToolBar("product_qs_grid", this.templatecolumns, this.templateDS, 350);
		fsn.initKendoWindow("edit_qs_permission_window","qs权限预览/修改","750px","490px",false, null);
		fsn.initKendoWindow("qs_permission_save_confirm_window","友情提示","300px","190px",false, []);
		portal.bindClick();
		portal.initComponent();
		portal.currentBusiness = getCurrentBusiness();
	};
	
	portal.templatecolumns = [
			{field: "qsno",title: "证件编号",width: 80, filterable: false},
            {field: "productName",title: "产品名称",width: 70, filterable: false},
            {field: "can_use_staus",title: "是否允许他人使用",width: 50, filterable: false, template: function(dataItem) {
                if(dataItem.can_use){
                	return "<strong style='color:#006536;'>是</strong>";
                }else{
                	return "<strong style='color:#006536;'>否</strong>";
                }
            }},
            {field: "can_eidt_staus",title: "是否允许他人编辑",width: 50, filterable: false, template: function(dataItem) {
            	if(dataItem.can_eidt){
                	return "<strong style='color:#006536;'>是</strong>";
                }else{
                	return "<strong style='color:#006536;'>否</strong>";
                }                  		 
            }},
            {command: [{name:"review",
           	    text:"<span class='k-icon k-i-search k-search'></span>" + "查看授权明细", 
           	    click:function(e){
           	    	portal.operate(this.dataItem($(e.currentTarget).closest("tr")), "review");
             }},
             {name:"edit",
           	    text:"<span class='k-edit'></span>" + "编辑", 
           	    click:function(e){
           	    	portal.operate(this.dataItem($(e.currentTarget).closest("tr")), "edit");
             }},
             ], title: "操作", width: 60 }];
	
	portal.templateDS = new kendo.data.DataSource({
   			transport: {
   	            read : {
   	                type : "GET",
   	                async:false,
   	                url : function(options){
   	                	return portal.HTTP_PREFIX + "/product/qsno/list/myown/" + options.page + "/" + options.pageSize;
   	                },
   	                dataType : "json",
   	                contentType : "application/json"
   	            },
   	        },
   	        schema: {
   	        	 data : function(returnValue) {
   	        		 return returnValue.data.listOfQs;  //响应到页面的数据
   	             },
   	             total : function(returnValue) {
   	            	 return returnValue.data.counts;   //总条数
   	             }
   	        },
   	        batch : true,
   	        page:1,
   	        pageSize : 5, //每页显示个数
   	        serverPaging : true,
   	        serverFiltering : true,
   	        serverSorting : true
   		});
	
	/**
	 * Click绑定事件
	 * @author ZhangHui 2015/5/20
	 */
	portal.bindClick = function(){
		$("#btn_qs_permission_save").click(function(){
			var mychoice = $("#mychoice").data("kendoDropDownList").value();
			if(mychoice == 0){
				$("#qs_permission_save_confirm_window").data("kendoWindow").open().center();
			}else if(mychoice == 1){
				portal.saveAuthorType();
			}
		});
		
		$("#btn_qs_permission_cancel").click(function(){
			$("#edit_qs_permission_window").data("kendoWindow").close();
		});
		
		$("#btn_qs_permission_close").click(function(){
			$("#edit_qs_permission_window").data("kendoWindow").close();
		});
		
		$("#btn_qs_permission_save_confirm").click(function(){
			portal.saveAuthorType();
			$("#qs_permission_save_confirm_window").data("kendoWindow").close();
			$("#edit_qs_permission_window").data("kendoWindow").close();
		});
		
		$("#btn_qs_permission_cancel_confirm").click(function(){
			$("#qs_permission_save_confirm_window").data("kendoWindow").close();
		});
	};
	
	/**
	 * 初始化控件
	 * @author ZhangHui 2015/5/20
	 */
	portal.initComponent = function(){
		$("#edit_qs_permission_window").kendoWindow({
    		title: "qs权限修改",
    		width: "750px",
    		height: "490px",
    		modal: true,
    		visible: false,
    		actions:["Close"],
    		close: onClose
    	});
		
		$("#mychoice").kendoDropDownList({
            dataTextField: "text",
            dataValueField: "value",
            dataSource: [{
                text: "仅自己",
                value: "0"
            },{
                text: "指定企业",
                value: "1"
            }],
            filter: "contains",
            suggest: true,
            index: 0,
            change: onChange
        });
	};
	
	/**
	 * 编辑
	 * @author ZhangHui 2015/5/15
	 */
	portal.operate = function(data, operate){
		portal.is_authing_ownerId = data.id; // 记录当前正在进行授权的qs号的主企业id
		portal.is_authing_qsId = data.qsId;  // 记录当前正在进行授权的qs号
		
		$("#edit_qs_permission_window").data("kendoWindow").open().center();
		$("#edit_qs_no").html(data.qsno);
		$("#current_business_name").html(portal.currentBusiness.name);
		
		$("#mychoice").data("kendoDropDownList").enable();
		if(data.can_use){
			portal.is_authing_init_mychoice = 1; // 记录当前 qs-主企业 记录的是否允许其他企业使用的标识
			$("#mychoice").data("kendoDropDownList").value(1);
			$("#grid").show();
		}else{
			portal.is_authing_init_mychoice = 0;
			$("#mychoice").data("kendoDropDownList").value(0);
			$("#grid").hide();
		}
		
		$("#btn_qs_permission_save").hide();
		$("#btn_qs_permission_cancel").hide();
		$("#btn_qs_permission_close").show();
		
		var dataSource = new kendo.data.DataSource({
            transport: {
                read:  {
                    url: portal.HTTP_PREFIX + "/product/qsno/getBus2QsOfHaveRight/" + data.qsId,
                    dataType: "jsonp"
                },
                update: {
                    url: portal.HTTP_PREFIX + "/product/qsno/bus2QsUpdate/",
                    dataType: "jsonp"
                },
                destroy: {
                    url: portal.HTTP_PREFIX + "/product/qsno/bus2QsDestroy/" + data.qsId,
                    dataType: "jsonp"
                },
                create: {
                    url: portal.HTTP_PREFIX + "/product/qsno/bus2QsCreate/" + data.qsId + "/" + data.id + "/" + data.businessName,
                    dataType: "jsonp"
                },
                parameterMap: function(options, operation) {
                    if (operation !== "read" && options.models) {
                    	// 是否允许使用和是否允许编辑选择框：赋值和校验
                    	var can_use = $('input[name=can_use]').is(':checked');
                    	var can_eidt = $('input[name=can_eidt]').is(':checked');
                    	if(!can_use){
                    		if(can_eidt){
                    			fsn.initNotificationMes('当选择不允许使用时，只能选择不允许编辑！', false);
                        		return null;
                    		}else{
                    			if(operation == "update"){
                    				fsn.initNotificationMes('当既不允许使用也不允许编辑时，请直接点击删除！', false);
                            		return null;
                    			}else if(operation == "create"){
                    				fsn.initNotificationMes('请选择可以使用！', false);
                            		return null;
                    			}
                    		}
                    	}
                    	options.models[0].can_use = can_use;
                		options.models[0].can_eidt = can_eidt;
                		
                    	// 企业选择框：赋值和校验
                		if(operation == "create"){
                			if(portal.viewModel){
                        		options.models[0].businessName = portal.viewModel.businessName;
                        		options.models[0].businessId = portal.viewModel.businessId;
                        		portal.viewModel = null;
                        	}else{
                        		fsn.initNotificationMes('请先选择企业！', false);
                        		return null;
                        	}
                			
                			var mychoice = $("#mychoice").data("kendoDropDownList").value();
                			if(portal.is_authing_init_mychoice != mychoice){
                				portal.is_authing_init_mychoice = mychoice;
                				onChange();
                			}
                		}
                        return {models: kendo.stringify(options.models)};
                    }
                }
            },
            batch: true,
            pageSize: 5,
            schema: {
                model: {
                    id: "id",
                    fields: {
                        id: { editable: false, nullable: true },
                        can_use: { type: "boolean" },
                        can_eidt: { type: "boolean" },
                        businessName:{ validation: { required: true } },
                        businessId: { editable: false, nullable: true }
                    }
                },
            }
        });
		
		$("#grid").html("");
		if(operate == "edit"){
			$("#grid").kendoGrid({
	            dataSource: dataSource,
	            pageable: {
	                refresh: true,
	                messages: fsn.gridPageMessage(),
	            },
	            /*height: 300,*/
	            toolbar: [ "create" ],
	            columns: [
	                { field: "businessName", title:"企业名称", width: "150px", editor: portal.businessNameSelect},
	                { field: "can_use", title:"是否允许使用", width: "65px", template: function(dataItem) {
	                    if(dataItem.can_use){
	                 		return "<strong style='color:#006536;'>是</strong>"; 
	                    }
	             		return "<strong style='color:#006536;'>否</strong>";                    		 
	                }},
	                { field: "can_eidt", title:"是否允许编辑", width: "65px" , template: function(dataItem) {
	                    if(dataItem.can_eidt){
	                 		return "<strong style='color:#006536;'>是</strong>"; 
	                    }
	             		return "<strong style='color:#006536;'>否</strong>";                    		 
	                }},
	                { command: ["edit", "destroy"], title: "&nbsp;", width: "85px" },
	            ],
	            editable: "inline"
	        });
		}else if(operate == "review"){
			$("#grid").kendoGrid({
	            dataSource: dataSource,
	            pageable: {
	                refresh: true,
	                messages: fsn.gridPageMessage(),
	            },
	            /*height: 300,*/
	            toolbar: [],
	            columns: [
	                { field: "businessName", title:"企业名称", width: "150px", editor: portal.businessNameSelect},
	                { field: "can_use", title:"是否允许使用", width: "65px", template: function(dataItem) {
	                    if(dataItem.can_use){
	                 		return "<strong style='color:#006536;'>是</strong>"; 
	                    }
	             		return "<strong style='color:#006536;'>否</strong>";                    		 
	                }},
	                { field: "can_eidt", title:"是否允许编辑", width: "65px" , template: function(dataItem) {
	                    if(dataItem.can_eidt){
	                 		return "<strong style='color:#006536;'>是</strong>"; 
	                    }
	             		return "<strong style='color:#006536;'>否</strong>";                    		 
	                }},
	            ],
	        });
			
			// 预览时，授权类型不可编辑
			$("#mychoice").data("kendoDropDownList").enable(false);
		}
	};
	
	/**
	 * 点击选择企业时，弹出选择框
	 * @author ZhangHui 2015/5/18
	 */
	portal.businessNameSelect = function(container, options){
		if(options.model.id){
			$('<input readonly="readonly" style="width: 235px;" type="text" class="k-textbox" data-bind="value:' + options.field + '"/>')
			.appendTo(container);
		}else{
			$('<div id="businessNameDiv"><input readonly="readonly" style="width: 235px;" type="text" class="k-textbox" data-bind="value:' + options.field + '"/></div>')
			.appendTo(container);
			
			$("#businessNameDiv").bind("click", portal.openTree);
		}
	};
 
	portal.openTree = function(){
		openTreeWin("businessNameGroupWin", "businessName");
		
	};
	
	/**
	 * 功能描述：grid中点击数据框，弹出选择框，选择一条后，需要将将选择的id赋值给grid的datasource
	 * @author ZhangHui 2015/5/19
	 */
	function openTreeWin(winNode, nameNode) {
		var serviceRoot = fsn.getHttpPrefix() + "/business/tree/getRelativesOftree";
		var homogeneous = new kendo.data.HierarchicalDataSource({
			transport : {
				read : {
					url : serviceRoot,
					dataType : "json"
				}
			},
			schema : {
				model : {
					id : "id",
					leafId : "leafId",
					hasChildren : "hasChildren",
				}
			},
		});

		var window = $("#" + winNode);
	  	if (!window.data("kendoWindow")) {
			$("#listView").kendoTreeView({
				dataSource : homogeneous,
				dataTextField : "name",
				select: onSelect
			});
	        window.kendoWindow({
	            width: "400px",
	            height:"400px",
	            title: "请选择",
	            visible: false
	        });
	    } else {
	    	$("#listView").data("kendoTreeView").select(null);
	    }

	  	function onSelect(e) {
			 var tempStr = this.text(e.node);
			 var treeview = $("#" + winNode + " #listView").data("kendoTreeView");
			 var dataItem = treeview.dataItem(treeview.findByText(this.text(e.node)));
			 
			 // 不允许选择当前登录企业
			 if(portal.currentBusiness.id == dataItem.leafId){
				 fsn.initNotificationMes('不可以选择当前登录企业，请选择其他企业！', false);
				 return;
			 }
			 
			 // 验证该企业是否已经授权
			 var isAuthorized = portal.isHaveAuthorized(dataItem.leafId, portal.is_authing_qsId);
			 if(isAuthorized){
				 fsn.initNotificationMes('该企业已经被授权，请选择其他企业！', false);
				 return;
			 }
			 
			 // 选择成功后，页面赋值
			 //$(".k-grid-edit-row td:nth-child(2)").html('<input class="k-input k-textbox" type="text" name="businessId" required="required" data-bind="value:businessId">');
			 portal.viewModel = kendo.observable({
				    businessName: tempStr,
				    businessId: dataItem.leafId,
				    can_use: true,
				    can_eidt: true,
			 });
			 kendo.bind($(".k-grid-edit-row"), portal.viewModel);
			 
			 // 选择结束，关闭窗口
			 window.data("kendoWindow").close();
	     }
	     
	     window.data("kendoWindow").open().center();
	}
	
	/**
	 * 验证当前qs号有无给该企业授权
	 * @author ZhangHui 2015/5/19
	 */
	portal.isHaveAuthorized = function(businessId, qsId){
		var isAuthorized = false;
		$.ajax({
			url: portal.HTTP_PREFIX + "/product/qsno/isHaveAuthorized/" + businessId + "/" + qsId,
			type:"GET",
	        async:false,
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					isAuthorized = returnValue.data;
				}
			}
		});
		return isAuthorized;
	};
	
	/**
	 * #mychoice 值发生改变后，执行此方法
	 * 举例:当值从 指定企业 变为 仅自己   时，需要隐藏企业授权的grid
	 *     当值从 仅自己  变为 指定企业  时，需要显示企业授权的grid
	 * @author ZhangHui 2015/5/20
	 */
	function onChange () {
		var style = document.getElementById("btn_qs_permission_save").style.display;
		if(style == "none"){
			$("#btn_qs_permission_save").show();
			$("#btn_qs_permission_cancel").show();
			$("#btn_qs_permission_close").hide();
		}else{
			$("#btn_qs_permission_save").hide();
			$("#btn_qs_permission_cancel").hide();
			$("#btn_qs_permission_close").show();
		}
		
		var mychoice = $("#mychoice").data("kendoDropDownList").value();
		if(mychoice == 0){
			$("#grid").hide();
		}else if(mychoice == 1){
			$("#grid").show();
		}
	};
	
	/**
	 * #edit_qs_permission_window 窗口关闭后，执行此方法
	 * 功能描述：刷新 #product_qs_grid
	 * @author ZhangHui 2015/5/20
	 */
	function onClose () {
		portal.templateDS.read();
	};
	
	/**
	 * 保存当前qs号的授权类型
	 * @author ZhangHui 2015/5/20
	 */
	portal.saveAuthorType = function (){
		var mychoice = $("#mychoice").data("kendoDropDownList").value();
		$.ajax({
			url:portal.HTTP_PREFIX + "/product/qsno/update/" + portal.currentBusiness.id + "/" + portal.is_authing_ownerId + 
					"/" + portal.is_authing_qsId + "/" + mychoice,
			type:"GET",
	        async:false,
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					$("#edit_qs_permission_window").data("kendoWindow").close();
					fsn.initNotificationMes('授权类型成功保存为【' + $("#mychoice").data("kendoDropDownList").text() + '】！', true);
				}else{
					fsn.initNotificationMes('授权类型保存失败！', false);
				}
			}
		});
	};
	
	portal.initialize();
});