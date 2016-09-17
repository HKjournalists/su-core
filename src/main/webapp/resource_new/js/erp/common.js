$(function(){
	var fsn = window.fsn = window.fsn || {};
	var eWidget = fsn.eWidget = fsn.eWidget||{};
	var common = fsn.common = fsn.common || {};
	
	common.validator = null;
	common.grid = null;
	common.SIMPLE_TYPE = parseInt(window.location.search.substr(6));
	common.SIMPLE_MODEL_NAME = decodeURIComponent(window.location.search.substr(13));
	
	if(common.SIMPLE_MODEL_NAME.substring(0, 1) == "=") {
		common.SIMPLE_MODEL_NAME = common.SIMPLE_MODEL_NAME.substr(1);
	}
	
	common.realObj = {};
	
	common.initComponent = function(e){
		common.realObj = e;
		console.log("initComponent...");
		eWidget.initGrid(common.realObj.GRIDID,this.columns,null,common.realObj.hight,this.datasource,"toolbar_template");
		this.grid = $("#"+common.realObj.GRIDID).data("kendoGrid");
		this.initGridCommand();
		this.validator = $("#" + this.DAILOGID).kendoValidator().data("kendoValidator");
		$("#page_title").html("首页 > " + common.realObj.SIMPLE_MODEL_NAME + "管理");
		if(common.realObj.SIMPLE_TYPE!="3"){
			$("#pending").hide();
		}
	};
	
	common.initGridCommand = function(){
		$("#add").bind("click",function(dailogid){
			common.add(common.realObj.DAILOGID,common.realObj.clearForm,common.realObj.windowSaveConfrim);
		});
		if(!common.realObj.BUSINESS_FLAG){
			$("#update").bind("click",function(dailogid){
			if(common.realObj.grid.select().length!=0){
				var item = common.realObj.selectedData = common.realObj.grid.dataItem(common.realObj.grid.select());
				var flag = common.realObj.judgeIsUsed(item);
				if(flag){
					common.update(common.realObj.DAILOGID,common.realObj.bindForm,common.realObj.windowUpdateConfrim);
				}else{
					var win = eWidget.getPromptWindow1();
					win.data("kendoWindow").open().center();
					$("#confirmWindowSave").unbind("click");
					$("#cancelWindowCon").unbind("click");
					$("#confirmWindowSave").bind("click",function(){
						win.data("kendoWindow").close();
						common.update(common.realObj.DAILOGID,common.realObj.bindForm,common.realObj.windowUpdateConfrim);
					});
					$("#cancelWindowCon").bind("click",function(){
						win.data("kendoWindow").close();
					});
				}
			}else{
				fsn.initNotificationMes("请选中一项！",false);
			}
			});
			$("#delete").bind("click",function(){
				common.remove(common.realObj.windowDeleteConfrim);
			});
		}
	}
	
	/**
	 * add, update, delete
	 */
	common.add = function(dailogid, clearFromFun, confrimFun){
		var addWin = eWidget.getWindow(dailogid,st_customer.SIMPLE_MODEL_NAME, common.realObj.win_width == null ? null:common.realObj.win_width);
		clearFromFun();
		//if(st_customer.SIMPLE_TYPE == 2) $(".t_hide").removeClass("t_hide");
		addWin.data("kendoWindow").title("新增"+st_customer.SIMPLE_MODEL_NAME);
		addWin.data("kendoWindow").open().center();
		$("#confirm").show();
		// unbind click event
		$("#confirm").unbind("click");
		$("#cancel").unbind("click");
		
		$("#confirm").bind("click",function(){
			confrimFun();
			if(common.realObj.flag != undefined && common.realObj.flag == true) {
				return;
			}
//			addWin.data("kendoWindow").close();
		});
		
		$("#cancel").bind("click",function(){
			addWin.data("kendoWindow").close();
		});
	};
	
	common.selectedData = {};
	
	common.update = function(dailogid, bindFormFun, confrimFun){
		if(common.realObj.grid.select()){
			var item = common.realObj.selectedData = common.realObj.grid.dataItem(common.realObj.grid.select());
			
			$("#confirm").show();
			
			//不影响其他模块的修改。判断是如果没有这个字段就自动到else了。所以不会影响其他的模块的
				if(item != null) {
					var addWin = null;
					if(item.outOfBillState!=null && (item.outOfBillState == "已交付" || item.outOfBillState == "已发货")) {
						$("#confirm").hide();
						fsn.initNotificationMes("状态为已发货或已交付，不能编辑！",false);
					}else if(item.re_purchase_check!=null && item.re_purchase_check == "已收货"){
						fsn.initNotificationMes("状态为已收货，不能编辑！",false);
					}else{
						addWin = eWidget.getWindow(dailogid,st_customer.SIMPLE_MODEL_NAME, common.realObj.win_width == null ? null:common.realObj.win_width);					
						addWin.data("kendoWindow").title("编辑"+st_customer.SIMPLE_MODEL_NAME);
						addWin.data("kendoWindow").open().center();
					
						bindFormFun();
					}
					// unbind click event
					$("#confirm").unbind("click");
					$("#cancel").unbind("click");
					
					$("#confirm").bind("click",function(){
						confrimFun();
						if(common.realObj.flag != undefined && common.realObj.flag == true) {
							return;
						}
//						addWin.data("kendoWindow").close();
					});
					$("#cancel").bind("click",function(){
						if(addWin!=null){
							addWin.data("kendoWindow").close();
						}
					});
				} else {
					fsn.initNotificationMes("请选中一项！",false);
				}				
			
			}
	};
	
	common.remove = function(confrimFun){
		if(common.realObj.grid.select()){
			var item = common.realObj.selectedData = common.realObj.grid.dataItem(common.realObj.grid.select());
			//不影响其他模块的修改。判断是如果没有这个字段就自动到else了。所以不会影响其他的模块的
			if(item != null){
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
								confrimFun();
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
	};
	
});
